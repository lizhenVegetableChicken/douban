package fm.douban.app.control;

import fm.douban.model.*;
import fm.douban.param.SongQueryParam;
import fm.douban.service.*;
import fm.douban.util.FavoriteUtil;
import fm.douban.util.SubjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class MainControl {
    @Autowired
    SongService songService;
    @Autowired
    SingerService singerService;
    @Autowired
    SubjectService subjectService;
    @Autowired
    UserService userService;
    @Autowired
    FavoriteService favoriteService;

    private static final Logger LOG = LoggerFactory.getLogger(MainControl.class);

    /*@GetMapping("/index")*/
    @RequestMapping("/index")
    public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
        System.out.println();
        setPlayer(model);
        setMhz(model);
        setCollection(model);
//        setFavorite(model, request);
        return "firstpage/index";
    }

    @GetMapping("/test1")
    public String test() {
        return "test1";
    }

    @GetMapping("/result")
    @ResponseBody
    public String result(Model model,@RequestParam(name = "name")String param) {
        System.out.println(param);
        return "result";
    }

    private void setPlayer(Model model) {
        SongQueryParam songQueryParam = new SongQueryParam();
        songQueryParam.setPageSize(1);
        songQueryParam.setPageNum(1);
        Page<Song> songPage = songService.list(songQueryParam);
        List<Song> songs = songPage.getContent();
        Song song = songs.get(0);
        List<String> singerIds = song.getSingerIds();
        List<Singer> singers = new ArrayList<>();
        for (String singerId : singerIds) {
            Singer singer = singerService.get(singerId);
            singers.add(singer);
        }
        // player相关
        model.addAttribute("song",song);
        model.addAttribute("singers",singers);
    }

//    给赫兹传值，心情年代，风格等  还有一个特殊的艺术家
    private void setMhz(Model model) {
        MhzViewModel moodMhz = new MhzViewModel();
        moodMhz.setTitle("心情/场景");
        moodMhz.setSubjects(subjectService.getSubjects(SubjectUtil.TYPE_MHZ, SubjectUtil.TYPE_SUB_MOOD));
        MhzViewModel ageMhz = new MhzViewModel();
        ageMhz.setTitle("语言/年代");
        ageMhz.setSubjects(subjectService.getSubjects(SubjectUtil.TYPE_MHZ, SubjectUtil.TYPE_SUB_AGE));
        MhzViewModel styleMhz = new MhzViewModel();
        styleMhz.setTitle("风格/流派");
        styleMhz.setSubjects(subjectService.getSubjects(SubjectUtil.TYPE_MHZ, SubjectUtil.TYPE_SUB_STYLE));
        List<MhzViewModel> allMhzs = new ArrayList<>();
        allMhzs.add(moodMhz);
        allMhzs.add(ageMhz);
        allMhzs.add(styleMhz);
        List<Subject> artistDatas = subjectService.getSubjects(SubjectUtil.TYPE_MHZ, SubjectUtil.TYPE_SUB_ARTIST);

        // explore相关：兆赫
        model.addAttribute("artistDatas",artistDatas);
        model.addAttribute("mhzViewModels",allMhzs);
    }

//    给歌单传值，
    private void setCollection(Model model) {
        List<Subject> collections = subjectService.getSubjects(SubjectUtil.TYPE_COLLECTION);

        class CollectionData extends Subject{
            private List<String> songInfos;

            public List<String> getSongInfos() {
                return this.songInfos;
            }
            public void setSongInfos(List<String> songInfos) {
                this.songInfos = songInfos;
            }
        }

        List<CollectionData> collectionDatas = new ArrayList<>();

        for (Subject collection : collections) {

//            给歌单中的每个单独一块赋值：歌曲描述，作者，id，曲目，收藏，名字，封面

            CollectionData data = new CollectionData();
            data.setId(collection.getId());
            data.setMaster(collection.getMaster());
            data.setCover(collection.getCover());
            data.setName(collection.getName());
            data.setCollectedCount(collection.getCollectedCount());
            data.setSongsCount(collection.getSongsCount());

            List<String> songInfos = new ArrayList<>();
            if(collection == null) {
                continue;
            }
            if(collection.getSongIds() == null) {
                continue;
            }
            int size = collection.getSongIds().size();
            size = size>=3?3:size;
            for (int i=0; i<size; i++) {
                Song songData = songService.get(collection.getSongIds().get(i));
                if(songData==null) {
                    continue;
                }
                String songInfo = songData.getName() + " - ";
                if (songData.getSingerIds().size() == 0) {
                    continue;
                }
                Singer singer = singerService.get(songData.getSingerIds().get(0));
                songInfo += singer.getName();
                songInfos.add(songInfo);
            }
            data.setSongInfos(songInfos);
            collectionDatas.add(data);
        }
        // explore相关：歌单
        model.addAttribute("collections", collectionDatas);
    }

    private void setFavorite(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserLoginInfo user = (UserLoginInfo)session.getAttribute("userLoginInfo");
        String userId = user.getUserId();
        Favorite fav = new Favorite();
        fav.setId(userId);
        fav.setType(FavoriteUtil.TYPE_RED_HEART);

        List<Favorite> favs = favoriteService.list(fav);
        List<Song> fsongs = new ArrayList<>();
        if (favs != null && !favs.isEmpty()) {
            for (Favorite tfav : favs) {
                if (FavoriteUtil.TYPE_RED_HEART.equals(tfav.getType()) && FavoriteUtil.ITEM_TYPE_SONG.equals(
                        tfav.getItemType())) {
                    Song fsong = songService.get(tfav.getItemId());
                    if (fsong != null) {
                        fsongs.add(fsong);
                    }
                }
            }
        }
        //explore相关：我的
        model.addAttribute("favorites", favs);
        model.addAttribute("songs", fsongs);
    }

    @GetMapping("/fav")
    @ResponseBody
    public Map doFav(@RequestParam("itemId") String itemType, @RequestParam("itemType") String itemId,
                 HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        UserLoginInfo user = (UserLoginInfo)session.getAttribute("userLoginInfo");
        String userId = user.getUserId();

        Map result = new HashMap();
        Favorite fav = new Favorite();
        fav.setId(userId);
        fav.setItemId(itemId);
        fav.setItemType(itemType);
        List<Favorite> favs = favoriteService.list(fav);
        if (favs.size()>0) {
            favoriteService.delete(fav);
        } else {
            favoriteService.add(fav);
        }
        result.put("message", "successful");
        return result;
    }

    @GetMapping("/search")
    public String search(Model model) {
        return "firstpage/first-search";
    }

    //首页搜索逻辑
    @GetMapping("/index/searchContent")
    @ResponseBody
    public Map indexSearch(@RequestParam("keyword") String keyword) {

        List<Singer> singers = new ArrayList<>();
        List<Song> songs = new ArrayList<>();
        List<Subject> collections = new ArrayList<>();
        List<Subject> mhzs = new ArrayList<>();

        for (Singer singer : singerService.getAll()) {
            if (singer.getName().contains(keyword)) {
                singers.add(singer);
            }
        }

        SongQueryParam param = new SongQueryParam();
        Page<Song> allSongs = songService.list(param);
        for (Song song : allSongs.getContent()) {
            if (song.getName().contains(keyword)) {
                songs.add(song);
            }
        }

        for (Subject subject : subjectService.getSubjects(SubjectUtil.TYPE_MHZ)) {
            if (subject.getName().contains(keyword)) {
                mhzs.add(subject);
            }
        }

        for (Subject subject : subjectService.getSubjects(SubjectUtil.TYPE_COLLECTION)) {
            if (subject.getName().contains(keyword)) {
                collections.add(subject);
            }
        }

        Map map = new HashMap<>();
        map.put("singers", singers);
        map.put("songs", songs);
        map.put("mhzs", mhzs);
        map.put("collections", collections);

        return map;

    }

//    播放
    @GetMapping("/play")
    public String play(Model model) {
        List<Song>songs = songService.getAll();
        model.addAttribute("songs",songs);
        return "play";
    }

    @GetMapping("/list")
    public String list(Model model,@RequestParam(value="pageNum",defaultValue="1")int pageNum,@RequestParam(value = "pageSize",defaultValue = "10")int pageSize) {
        System.out.println("============================");
        SongQueryParam songPage = new SongQueryParam();
        songPage.setPageNum(1);
        songPage.setPageSize(10);
        Page<Song>songs = songService.list(songPage);
        System.out.println("总页数" + songs.getTotalPages());
        System.out.println("当前页是：" + pageNum);
//        System.out.println("分页数据：");
//        Iterator<Song> s = songs.iterator();
//        while (s.hasNext()){
//
//            System.out.println(s.next().toString());
//        }

        model.addAttribute("songPages", songs);


        return "page";


//        Page<Song> users=userService.getUserList(pageNum, pageSize);
//        System.out.println("总页数" + users.getTotalPages());
//        System.out.println("当前页是：" + pageNum);
//
//        System.out.println("分页数据：");
//        Iterator<User> u = users.iterator();
//        while (u.hasNext()){
//
//            System.out.println(u.next().toString());
//        }
//
//        model.addAttribute("users", users);
//
//
//        return "user/list";
    }

}
