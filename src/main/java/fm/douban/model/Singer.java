package fm.douban.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public class Singer {

    private String id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreated;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModified;
    private String name;
    private String avatar;
    private String homepage;
    private List<String> similarSingerIds;
    private List<String> songIds;
    private String subjectId;

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return this.id;
    }
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
    public String getSubjectId() {
        return this.subjectId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getAvatar() {
        return this.avatar;
    }
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }
    public String getHomepage() {
        return this.homepage;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }

    public LocalDateTime getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(LocalDateTime gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public List<String> getSimilarSingerIds(){
        return this.similarSingerIds;
    }
    public void setSimilarSingerIds(List<String> similarSingerIds){
        this.similarSingerIds = similarSingerIds;
    }
    public void setSongIds(List<String> songIds) {
        this.songIds = songIds;
    }
    public List<String> getSongIds() {
        return this.songIds;
    }
}
