package fm.douban.app.control.test;

import fm.douban.model.Singer;
import fm.douban.service.SingerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test/singer")
public class SingerTestControl {

    @Autowired
    SingerService singerService;

    @GetMapping("/add")
    public String testAddSinger() {
        Singer singer = new Singer();
        singer.setId("0");
        singer.setName("MJ");
        singer.setAvatar("av1");
        singer.setHomepage("pc1");
        System.out.println(singerService.addSinger(singer));
        return "sd";
    }

    @GetMapping("/getAll")
    public List<Singer> testGetAll() {
        return singerService.getAll();
    }

    @GetMapping("/getOne")
    public Singer testGetSinger() {
        return singerService.get("0");
    }

    @GetMapping("/modify")
    public boolean testModifySinger() {
        Singer newSinger = new Singer();
        newSinger.setId("0");
        newSinger.setName("Aj");
        return singerService.modify(newSinger);
    }

    @GetMapping("/del")
    public boolean testDelSinger() {
        return singerService.delete("0");
    }

}
