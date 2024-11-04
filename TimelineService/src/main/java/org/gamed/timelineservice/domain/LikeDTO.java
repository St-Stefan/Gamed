package org.gamed.timelineservice.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LikeDTO {
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String user;

    private String itemId;

    private String type;

    private LocalDateTime time;

    public LikeDTO() {}

    public LikeDTO(String user_id, String item_id, String type) {
        this.user = user_id;
        this.itemId = item_id;
        this.type = type;
        this.time = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getItemId() {
        return itemId;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "UserToLike{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", item_id='" + itemId + '\'' +
                ", type='" + type + '\'' +
                ", time=" + time +
                '}';
    }
}
