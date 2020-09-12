package fm.douban.spider;

import com.alibaba.fastjson.JSON;
import fm.douban.model.Singer;
import fm.douban.model.Song;
import fm.douban.service.SingerService;
import fm.douban.service.SongService;
import fm.douban.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SingerSongSpider {

    @Autowired
    private SingerService singerService;
    @Autowired
    private SongService songService;

    private static final HttpUtil HTTPUTIL = new HttpUtil();
    private static final Map<String, String> DOUBAN_HEADERS = HTTPUTIL.buildHeaderData("https://fm.douban.com/","fm.douban.com");

    private static final Logger logger = LoggerFactory.getLogger(SingerSongSpider .class);

//     @PostConstruct
    public void init() {
         System.out.println("执行了");
        doExcute();
    }

    public void doExcute() {
        getSongDataBySingers();
        logger.info("getSongDataBySingers finished");
    }

    private void getSongDataBySingers() {
        List<Singer> singers = singerService.getAll();
        for (Singer singer : singers) {
            String url = "https://fm.douban.com/j/v2/artist/"+singer.getId()+"/";
            String content = HTTPUTIL.getContent(url, DOUBAN_HEADERS);
            Map contentObj= JSON.parseObject(content, Map.class);
            Map songlist = (Map)contentObj.get("songlist");
            if (songlist == null) {
                continue;
            }
            List<String> songIds = new ArrayList<>();
            List<Map> songs = (List<Map>) songlist.get("songs");
            for (Map song : songs) {
                Song newSong = new Song();
                newSong.setId(song.get("sid").toString());
                songIds.add(song.get("sid").toString());
                newSong.setName((String)song.get("title"));
                newSong.setCover((String)song.get("picture"));
                newSong.setUrl((String)song.get("url"));
                newSong.setLyrics("新的风暴已经出现，怎么能够停滞不前");
                List<Map>singerMaps = (List<Map>) song.get("singers");
                List<String> singerIds = new ArrayList<>();
                for (Map singerMap : singerMaps) {
                    Singer ssinger = new Singer();
                    ssinger.setId(singerMap.get("id").toString());
                    ssinger.setName(singerMap.get("name").toString());
                    ssinger.setAvatar(singerMap.get("avatar").toString());
                    singerService.addSinger(ssinger);
                    singerIds.add(singerMap.get("id").toString());
                }
                newSong.setSingerIds(singerIds);
                songService.add(newSong);
            }
            singer.setSongIds(songIds);
            singerService.modify(singer);
            getRelatedSingers(contentObj);
        }
    }
    private void getRelatedSingers(Map contentObj) {
        // 得到此时这个歌手
        String id = contentObj.get("id").toString();
        Singer recentSinger = singerService.get(id);
        // 解析出相似歌手
        Map relatedChannel = (Map) contentObj.get("related_channel");
        List<Map> similarSingers = (List<Map>) relatedChannel.get("similar_artists");
        // 每个相似歌手存入库中，并将其id 存入此歌手的 相似歌手属性中
        for (Map similarSinger : similarSingers) {
            Singer ssinger = new Singer();
            ssinger.setId(similarSinger.get("id").toString());
            ssinger.setAvatar((String)similarSinger.get("avatar"));
            ssinger.setName((String)similarSinger.get("name"));
            // 若查找此id的歌手不为空，即已存在，就修改
            if (singerService.get(ssinger.getId()) != null) {
                singerService.modify(ssinger);
                // 否则进行增加
            } else {
                singerService.addSinger(ssinger);
            }
            // 为这个歌手的相似歌手属性增加值
            if (recentSinger.getSimilarSingerIds() == null) {
                recentSinger.setSimilarSingerIds(new ArrayList<>());
                recentSinger.getSimilarSingerIds().add(ssinger.getId());
                singerService.modify(recentSinger);
            } else {
                boolean exist = false;
                for (String existId : recentSinger.getSimilarSingerIds()) {
                    if (existId.equals(ssinger.getId())) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    recentSinger.getSimilarSingerIds().add(ssinger.getId());
                    singerService.modify(recentSinger);
                }
            }
        }
    }

}
