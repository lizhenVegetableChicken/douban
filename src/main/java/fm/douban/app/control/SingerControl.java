package fm.douban.app.control;

import fm.douban.model.Singer;
import fm.douban.service.SingerService;
import fm.douban.service.SongService;
import fm.douban.service.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SingerControl {

    @Autowired
    SongService songService;
    @Autowired
    SingerService singerService;
    @Autowired
    SubjectService subjectService;

    private static final Logger LOG = LoggerFactory.getLogger(MainControl.class);

    @GetMapping("/userguide")
    public String myMhz(Model model) {
        model.addAttribute("singers", randomSingers());
        return "user/userguide";
    }

    @GetMapping("/singer/random")
    @ResponseBody
    public List<Singer> randomSingers() {
        List<Singer> singers = singerService.getAll();
        List<Singer> selectedSingers = new ArrayList<>();
        int size = singers.size();
        for (int i=0 ; i<50 ; i ++) {
            int random = (int)(Math.random()*size/2);
            Singer singer = singers.get(random);
            singers.remove(singer);
            selectedSingers.add(singer);
        }
        return selectedSingers;
    }

    @GetMapping("/userguide/search")
    public String search(Model model) {
        return "user/userguide_search";
    }

    @GetMapping("/userguide/searchContent")
    @ResponseBody
    public Map searchContent(@RequestParam("keyword") String keyword) {
        List<Singer> singers = new ArrayList<>();
        for (Singer singer : singerService.getAll()) {
            if (singer.getName().contains(keyword)) {
                singers.add(singer);
            }
        }
        Map<String, Singer> map = new HashMap<>();
        for (int i=0; i<singers.size(); i++) {
            map.put("s"+i, singers.get(i));
        }
        return map;
    }

}
