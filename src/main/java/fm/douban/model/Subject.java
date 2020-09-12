package fm.douban.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Subject {

    private String id;
    private String artistId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreated;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModified;
    private String name;
    private String description;
    private String cover;
    // 对歌单来说是作者，对兆赫来说是音乐家
    private String master;
    private LocalDate publishedDate;
    //主题大分类，兆赫mhz 或者 歌单 collection
    private String subjectType;
    // 主题下细分类：兆赫细分 artist mood age style
    private String subjectSubType;
    // 关联的歌曲
    private List<String> songIds;

    private String collectedCount;
    private String songsCount;

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return this.id;
    }
    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }
    public String getArtistId() {
        return this.artistId;
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
    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }
    public void setCover(String cover) {
        this.cover = cover;
    }
    public String getCover() {
        return this.cover;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return this.description;
    }
    public void setMaster(String master) {
        this.master = master;
    }
    public String getMaster() {
        return this.master;
    }
    public void setPublishedDate(LocalDate publishedDate){
        this.publishedDate = publishedDate;
    }
    public LocalDate getPublishedDate(){
        return this.publishedDate;
    }
    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }
    public String getSubjectType() {
        return this.subjectType;
    }
    public void setSubjectSubType(String subjectSubType) {
        this.subjectSubType = subjectSubType;
    }
    public String getSubjectSubType() {
        return this.subjectSubType;
    }
    public void setSongIds(List<String> songIds) {
        this.songIds = songIds;
    }
    public List<String> getSongIds() {
        return this.songIds;
    }
    public void setSongsCount(String songsCount) {
        this.songsCount = songsCount;
    }
    public String getSongsCount() {
        return this.songsCount;
    }
    public void setCollectedCount(String collectedCount) {
        this.collectedCount = collectedCount;
    }
    public String getCollectedCount() {
        return this.collectedCount;
    }

}
