package fm.douban.spider;

import com.alibaba.fastjson.JSON;
import fm.douban.model.Singer;
import fm.douban.model.Song;
import fm.douban.model.Subject;
import fm.douban.service.SingerService;
import fm.douban.service.SongService;
import fm.douban.service.SubjectService;
import fm.douban.util.HttpUtil;
import fm.douban.util.SubjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SubjectSpider {

    @Autowired
    private SubjectService subjectService;
    @Autowired
    private SingerService singerService;
    @Autowired
    private SongService songService;

    private static long songCount = 0;
    private static final HttpUtil HTTPUTIL = new HttpUtil();
    private static final Map<String, String> DOUBAN_HEADERS = HTTPUTIL.buildHeaderData("https://fm.douban.com/","fm.douban.com");

    private static final Logger logger = LoggerFactory.getLogger(SubjectSpider .class);

//    @PostConstruct
    public void init() {
        System.out.println("执行看");
        doExcute();
    }

    public void doExcute() {
        getSubjectData();
        logger.info("getSubjectData finished");
        getCollectionsData();
        logger.info("getCollectionsData finished");
        logger.info("songCount = " + songCount);
    }

    private void getSubjectData() {
        String content = HTTPUTIL.getContent("https://fm.douban.com/j/v2/rec_channels?specific=all",DOUBAN_HEADERS);
        Map contentObj = JSON.parseObject(content, Map.class);
        Map dataObj = (Map)contentObj.get("data");
        Map channelsObj = (Map)dataObj.get("channels");

        List<Map> scenario = (List<Map>)channelsObj.get("scenario");
        for (Map data : scenario) {
            Subject subject = buildSubject(data, SubjectUtil.TYPE_MHZ,SubjectUtil.TYPE_SUB_MOOD);
            subjectService.addSubject(subject);
            getSubjectSongData(subject.getId());
        }

        List<Map> language = (List<Map>)channelsObj.get("language");
        for (Map data : language) {
            Subject subject = buildSubject(data, SubjectUtil.TYPE_MHZ,SubjectUtil.TYPE_SUB_AGE);
            subjectService.addSubject(subject);
            getSubjectSongData(subject.getId());
        }

        List<Map> genre = (List<Map>)channelsObj.get("genre");
        for (Map data : genre) {
            Subject subject = buildSubject(data, SubjectUtil.TYPE_MHZ,SubjectUtil.TYPE_SUB_STYLE);
            subjectService.addSubject(subject);
            getSubjectSongData(subject.getId());
        }

        List<Map> artists = (List<Map>)channelsObj.get("artist");
        for (Map data : artists) {
            Subject subject = buildSubject(data, SubjectUtil.TYPE_MHZ,SubjectUtil.TYPE_SUB_ARTIST);
            String intro = (String)data.get("rec_reason");
            String artistId = data.get("artist_id").toString();
            String master = "";
            List<Map> relatedSingers = (List<Map>)data.get("related_artists");
            for (Map relatedSinger : relatedSingers) {
                Singer singer = new Singer();
                singer.setId(relatedSinger.get("id").toString());
                master += singer.getId() + " ";
                singer.setAvatar((String) relatedSinger.get("cover"));
                singer.setName((String) relatedSinger.get("name"));
                singerService.addSinger(singer);
            }
            subject.setArtistId(artistId);
            subject.setMaster(master);
            subject.setDescription(intro);
            subjectService.addSubject(subject);
            getSubjectSongData(subject.getId());
        }
    }

    private Subject buildSubject(Map data, String type, String subType) {
        Subject subject = new Subject();
        subject.setSubjectType(type);
        subject.setSubjectSubType(subType);
        subject.setDescription((String)data.get("intro"));
        subject.setId(data.get("id").toString());
        subject.setName((String)data.get("name"));
        subject.setCover((String)data.get("cover"));
        subject.setPublishedDate(LocalDate.now());

        return subject;
    }

    private void getSubjectSongData(String subjectId) {
        String url = "https://fm.douban.com/j/v2/playlist?channel="+subjectId+"&kbps=128&client=s%3Amainsite%7Cy%3A3.0&app_name=radio_website&version=100&type=n";
        String content = HTTPUTIL.getContent(url, DOUBAN_HEADERS);
        Map contentObj = JSON.parseObject(content, Map.class);
        List<Map> songMaps = (List<Map>) contentObj.get("song");
        if (songMaps == null) {
            return;
        }
        List<String> songIds = new ArrayList<>();
        for (Map songMap : songMaps) {
            Song song = new Song();
            song.setId(songMap.get("sid").toString());
            songIds.add(songMap.get("sid").toString());
            song.setUrl((String)songMap.get("url"));
            song.setCover((String)songMap.get("picture"));
            song.setName((String)songMap.get("title"));
            song.setLyrics("新的风暴已经出现，怎么能够停滞不前");
            List<Map>singers = (List<Map>) songMap.get("singers");
            List<String> singerIds = new ArrayList<>();
            for (Map singerMap : singers) {
                Singer singer = new Singer();
                singer.setId(singerMap.get("id").toString());
                singer.setName(singerMap.get("name").toString());
                singer.setAvatar(singerMap.get("avatar").toString());
                singerService.addSinger(singer);
                singerIds.add(singerMap.get("id").toString());
            }
            song.setSingerIds(singerIds);
            songService.add(song);
            songCount ++;
        }
        Subject modifySubject = new Subject();
        modifySubject.setId(subjectId);
        modifySubject.setSongIds(songIds);
        subjectService.modify(modifySubject);
    }


    private void getCollectionsData() {
        String url = "https://douban.fm/j/v2/songlist/explore?type=hot&genre=0&limit=20&sample_cnt=5";
        String content = HTTPUTIL.getContent(url, DOUBAN_HEADERS);
        List<Map> contentObj = JSON.parseObject(content, List.class);
        for (Map songlist : contentObj) {
            Subject subject = new Subject();
            subject.setCover(songlist.get("cover").toString());
            subject.setId(songlist.get("id").toString());
            subject.setName(songlist.get("title").toString());
            DateTimeFormatter df =   DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            subject.setPublishedDate(LocalDate.parse(songlist.get("created_time").toString(), df));
            subject.setSubjectType(SubjectUtil.TYPE_COLLECTION);
            subject.setDescription(songlist.get("description").toString());

            Map creatorData = (Map)songlist.get("creator");
            Singer creator = new Singer();
            creator.setId(creatorData.get("id").toString());
            creator.setName(creatorData.get("name").toString());
            creator.setAvatar(creatorData.get("picture").toString());
            creator.setHomepage(creatorData.get("url").toString());
            singerService.addSinger(creator);
            subject.setMaster(creatorData.get("name").toString());

            subject.setSongIds(getCollectionSongData(subject.getId()));
            subject.setSongsCount(songlist.get("songs_count").toString());
            subject.setCollectedCount(songlist.get("collected_count").toString());

            subjectService.addSubject(subject);
            singerService.addSinger(creator);
        }
    }

    private List<String> getCollectionSongData(String subjectId) {
        String url = "https://fm.douban.com/j/v2/songlist/"+ subjectId +"/?kbps=192";
        String content = HTTPUTIL.getContent(url, DOUBAN_HEADERS);
        Map contentObj = JSON.parseObject(content, Map.class);
        List<Map> songList = (List<Map>) contentObj.get("songs");

        List<String> songIds = new ArrayList<>();
        for (Map songMap : songList) {
            Song song = new Song();
            song.setId(songMap.get("aid").toString());
            song.setCover(songMap.get("picture").toString());
            song.setName(songMap.get("title").toString());
            song.setLyrics("歌单歌词妈咪雅黑");
            song.setUrl(songMap.get("url").toString());

            List<String> singerIds = new ArrayList<>();
            List<Map> singers = (List<Map>)songMap.get("singers");
            for (Map singerMap : singers) {
                Singer singer = new Singer();
                singer.setId(singerMap.get("id").toString());
                singer.setAvatar(singerMap.get("avatar").toString());
                singer.setName(singerMap.get("name").toString());
                singerIds.add(singer.getId());
                singerService.addSinger(singer);
            }

            song.setSingerIds(singerIds);
            songIds.add(song.getId());
            songService.add(song);
        }
        return songIds;
    }

}
