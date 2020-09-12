package fm.douban.app.control;

import fm.douban.model.Singer;
import fm.douban.model.Song;
import fm.douban.model.Subject;
import fm.douban.service.SingerService;
import fm.douban.service.SongService;
import fm.douban.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/crud")
public class crud {

    @Autowired
    SongService songService;
    @Autowired
    SubjectService subjectService;
    @Autowired
    SingerService singerService;

    private Song asong = new Song();
    private Song msong = new Song();
    private Song dsong = new Song();
    private Singer asinger = new Singer();
    private Singer msinger = new Singer();
    private Singer dsinger = new Singer();
    private Subject asubject = new Subject();
    private Subject msubject = new Subject();
    private Subject dsubject = new Subject();

    @GetMapping("/manage")
    public String manage() {
        return "manage";
    }

    @GetMapping("/song_add")
    public String song_add(ModelMap model) {
        model.put("asong", asong);
        return "crud";
    }

    @PostMapping("/songAdd")
//    @ResponseBody
    public String songAdd(@ModelAttribute Song song, ModelMap model) {
        if (song == null || !(StringUtils.hasText(song.getName()) && StringUtils.hasText(song.getId()))) {
            model.put("fail", "您输入的编号和歌名有误");
            model.put("asong", asong);
            return "crud";
        } else {
            songService.add(song);
            model.put("success", "操作成功！！");
            return "manage";
        }
    }

    @GetMapping("/song_modify")
    public String song_modify(ModelMap model) {
        model.put("msong", msong);
        return "crud";
    }

    @PostMapping("/songModify")
//    @ResponseBody
    public String songModify(@ModelAttribute Song song, ModelMap model) {
        if (song == null || !(StringUtils.hasText(song.getName()) && StringUtils.hasText(song.getId()))) {
            model.put("fail", "您输入的编号或名称有误");
            model.put("msong", msong);
            return "crud";
        } else {
            songService.modify(song);
            model.put("success", "操作成功！！");
            return "manage";
        }
    }

    @GetMapping("/song_mod")
    public String song_mod(@RequestParam("id")String id) {
        Song song = songService.get(id);
        songService.modify(song);
        return "page";
    }

    @GetMapping("/song_delete")
    public String song_delete(ModelMap model) {
        model.put("dsong", dsong);
        return "crud";
    }

    @GetMapping("/song_del")
    public String song_del(@RequestParam("id")String id) {
        songService.delete(id);
        return "page";
    }

    @PostMapping("/songDelete")
//    @ResponseBody
    public String songDelete(@ModelAttribute Song song, ModelMap model) {
        boolean has = false;
        List<Song> songs = songService.getAll();
        for (Song song1 : songs) {
            if (song1.getName().equals(song.getName())) {
                songService.delete(song1.getId());
                model.put("success", song1.getName() + "已成功删除！");
                has = true;
                return "manage";
            }
        }
        if (has) {
            return "manage";
        } else {
            model.put("fail", song.getName() + "不存在！");
            model.put("dsong", dsong);
            return "crud";
        }
    }






    @GetMapping("/singer_add")
    public String singer_add(ModelMap model) {
        model.put("asinger", asinger);
        return "crud";
    }

    @PostMapping("/singerAdd")
    public String singerAdd(@ModelAttribute Singer singer, ModelMap model) {
        if (singer == null || !(StringUtils.hasText(singer.getId()) && StringUtils.hasText(singer.getName()))) {
            model.put("fail", "您输入的编号或名称有误");
            model.put("asinger", asinger);
            return "crud";
        } else {
            singerService.addSinger(singer);
            model.put("success", "操作成功！！");
            return "manage";
        }
    }

    @GetMapping("/singer_modify")
    public String singer_modify(ModelMap model) {
//        Singer msinger = new Singer();
        model.put("msinger", msinger);
        return "crud";
    }

    @PostMapping("/singerModify")
//    @ResponseBody
    public String singerModify(@ModelAttribute Singer singer, ModelMap model) {
        if (singer == null || !(StringUtils.hasText(singer.getId()) && StringUtils.hasText(singer.getName()))) {
            model.put("fail", "您输入的编号或名称有误");
            model.put("msinger", msinger);
            return "crud";
        } else {
            singerService.modify(singer);
            model.put("success","操作成功！！");
            return "manage";
        }
    }

    @GetMapping("/singer_delete")
    public String singer_delete(ModelMap model) {
        model.put("dsinger", dsinger);
        return "crud";
    }

    @PostMapping("/singerDelete")
    public String singerDelete(@ModelAttribute Singer singer,ModelMap model) {
        boolean has = false;
        List<Singer> singers = singerService.getAll();
        for (Singer singer1 : singers) {
            if (singer1.getName().equals(singer.getName())) {
                singerService.delete(singer1.getId());
                model.put("success", singer1.getName() + "已成功删除！");
                has = true;
                return "manage";
            }
        }
        if (has) {
            return "manage";
        } else {
            model.put("fail", singer.getName() + "不存在！");
            model.put("dsinger", dsinger);
            return "crud";
        }
    }





    @GetMapping("/subject_add")
    public String subject_add(ModelMap model) {
        model.put("asubject", asubject);
        return "crud";
    }

    @PostMapping("/subjectAdd")
    public String subjectAdd(@ModelAttribute Subject subject,ModelMap model) {
        if (subject == null || !(StringUtils.hasText(subject.getId()) && StringUtils.hasText(subject.getName()))) {
            model.put("fail", "您输入的编号或名称有误");
            model.put("asubject", asubject);
            return "crud";
        } else {
            subjectService.addSubject(subject);
            model.put("success", "操作成功！！");
            return "manage";
        }
    }

    @GetMapping("/subject_modify")
    public String subject_modify(ModelMap model) {
        model.put("msubject", msubject);
        return "crud";
    }

    @PostMapping("/subjectModify")
    public String subjectModify(@ModelAttribute Subject subject,ModelMap model) {
        if (subject == null || !(StringUtils.hasText(subject.getId()) && StringUtils.hasText(subject.getName()))) {
            model.put("fail", "您输入的编号或名称有误");
            model.put("msubject", msubject);
            return "crud";
        } else {
            subjectService.modify(subject);
            model.put("success","操作成功！！");
            return "manage";
        }
    }

    @GetMapping("/subject_delete")
    public String subject_delete(ModelMap model) {
        model.put("dsubject", dsubject);
        return "crud";
    }

    @PostMapping("/subjectDelete")
    public String subjectDelete(@ModelAttribute Subject subject,ModelMap model) {
        boolean has = false;
        List<Subject> subjects = subjectService.getAll();
        for (Subject subject1 : subjects) {
            if (subject1.getName().equals(subject.getName())) {
                singerService.delete(subject1.getId());
                model.put("success", subject1.getName() + "已成功删除！");
                has = true;
                return "manage";
            }
        }
        if (has) {
            return "manage";
        } else {
            model.put("fail", subject.getName() + "不存在！");
            model.put("dsinger", dsinger);
            return "crud";
        }
    }

}


