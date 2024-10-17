package org.gamed.userpageservice.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.json.JSONObject;

import java.util.*;

@NoArgsConstructor
@ToString
@Getter
public class User {
    private Long id;

    private String username;

    private List<String> recentActivity;

    public User(Long id, String username, List<String> recentActivity) {
        this.id = id;
        this.username = username;
        this.recentActivity = recentActivity;
    }

    public static User readUser(JSONObject userDataJson) {
        Long id;
        String username;
        List<String> recentActivity;

        Map<String, String> userData = new HashMap<>();
        Iterator<String> keys = userDataJson.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            String value = userDataJson.get(key).toString();
            userData.put(key, value);
        }

        id = Long.parseLong(userData.get("userId"));
        username = userData.get("username");
        recentActivity = Arrays.asList(userData.get("recentActivity").split(","));

        return new User(id, username, recentActivity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return this.id.equals(user.id);
    }
}
