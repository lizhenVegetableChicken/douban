package fm.douban.app.control;

import fm.douban.model.*;
import fm.douban.param.SubjectQueryParam;
import fm.douban.service.SingerService;
import fm.douban.service.SongService;
import fm.douban.service.SubjectService;
import fm.douban.util.SubjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SubjectControl {

    @Autowired
    SongService songService;
    @Autowired
    SingerService singerService;
    @Autowired
    SubjectService subjectService;

    private static final Logger LOG = LoggerFactory.getLogger(MainControl.class);

    @GetMapping("/artist")
    public String mhzDetail(Model model, @RequestParam("artistId") String artistId) {

        Singer singer = singerService.get(artistId);
        if ( singer == null) {
            return "error";
        }

        List<Song> songs = new ArrayList<>();
        List<Singer> simSingers = new ArrayList<>();
        if (singer.getSimilarSingerIds() != null) {
            for (String id : singer.getSimilarSingerIds()) {
                simSingers.add(singerService.get(id));
            }
            model.addAttribute("simSingers", simSingers);
        }

        List<String> songIds = singer.getSongIds();
        if (songIds != null) {
            LOG.info("songs number is " + songIds.size());
            for ( String id : songIds) {
                Song song = songService.get(id);
                if (song != null) {
                    songs.add(song);
                }
            }
        } else {
            LOG.info("songs is null");
        }

        model.addAttribute("songs", songs);
        model.addAttribute("singer", singer);

        return "details/mhzdetail";
    }

    @GetMapping("collectiondetail")
    public String collectionDetail(Model model, @RequestParam("subjectId") String subjectId) {
        Subject collection = subjectService.get(subjectId);
        Singer creator = new Singer();
        for (Singer singer : singerService.getAll()) {
            if (singer.getName().equals(collection.getMaster())) {
                creator = singer;
                break;
            }
        }
        List<Song> songs = new ArrayList<>();
        for (String id : collection.getSongIds()) {
            Song song = songService.get(id);
            songs.add(song);
        }
        SubjectQueryParam subjectParam = new SubjectQueryParam();
        subjectParam.setSubjectType(SubjectUtil.TYPE_COLLECTION);
        subjectParam.setMaster(creator.getName());
        List<Subject> otherSubjects = subjectService.getSubjects(subjectParam);

        model.addAttribute("subject", collection);
        model.addAttribute("singer", creator);
        model.addAttribute("songs", songs);
        model.addAttribute("otherSubjects", otherSubjects);
        return "details/collectiondetail";
    }

}
