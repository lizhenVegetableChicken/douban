package fm.douban.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class Favorite {

    private String id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreated;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModified;
    private String userId;
    private String type;
    private String itemType;
    private String itemId;

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return this.id;
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
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return this.type;
    }
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
    public String getItemType() {
        return this.itemType;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    public String getItemId() {
        return this.itemId;
    }
}
