package org.gamed.timelineservice.domain;

import java.time.LocalDateTime;

public class LikeDTO {
    private String id;

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
