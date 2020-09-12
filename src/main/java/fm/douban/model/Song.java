package fm.douban.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public class Song {

    private String id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreated;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModified;
    private String name;
    private String lyrics;
    private String cover;
    private String url;
    private List<String> singerIds;

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return this.id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
    public LocalDateTime getGmtCreated() {
        return this.gmtCreated;
    }
    public void setGmtCreated(LocalDateTime gmtCreated) {
        this.gmtCreated = gmtCreated;
    }
    public LocalDateTime getGmtModified() {
        return this.gmtModified;
    }
    public void setGmtGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }
    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }
    public String getLyrics() {
        return this.lyrics;
    }
    public void setCover(String cover) {
        this.cover = cover;
    }
    public String getCover() {
        return this.cover;
    }
    public void setUrl(String cover) {
        this.url = url;
    }
    public String getUrl() {
        return this.url;
    }
    public void setSingerIds(List<String> singerIds) {
        this.singerIds = singerIds;
    }
    public List<String> getSingerIds() {
        return this.singerIds;
    }

}
