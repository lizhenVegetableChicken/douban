package fm.douban.app.control.test;

import fm.douban.model.Song;
import fm.douban.param.SongQueryParam;
import fm.douban.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/song")
public class SongTestControl {

    @Autowired
    SongService songService;

    @GetMapping("/add")
    public Song testAdd() {
        Song song = new Song();
        return songService.add(song);
    }

    @GetMapping("/get")
    public Song testGet() {
        return songService.get("0");
    }

    @GetMapping("/list")
    public Page<Song> testList() {
        SongQueryParam songQueryParam = new SongQueryParam();
        songQueryParam.setPageNum(1);
        songQueryParam.setPageSize(10);
        return songService.list(songQueryParam);
    }

    @GetMapping("/modify")
    public boolean testModify() {
        Song newSong = new Song();
        newSong.setId("0");
        newSong.setName("Aj");
        return songService.modify(newSong);
    }

    @GetMapping("/del")
    public boolean testDelete() {
        return songService.delete("0");
    }

}



