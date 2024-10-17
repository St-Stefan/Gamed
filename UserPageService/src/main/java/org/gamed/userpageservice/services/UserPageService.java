package org.gamed.userpageservice.services;

import lombok.NoArgsConstructor;
import org.gamed.userpageservice.domain.User;
import org.gamed.userpageservice.domain.UserPage;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@NoArgsConstructor
public class UserPageService {
    private RestTemplate restTemplate = new RestTemplate();

    public User requestUserInfo(Long userId) {
        ResponseEntity<JSONObject> response = null;
        try {
            response = restTemplate.exchange(
                    "http://localhost:8080/user_database/get_user_info/{userId}",
                    HttpMethod.GET,
                    null,
                    JSONObject.class,
                    userId
            );
        } catch (HttpClientErrorException e) {
            return null;
        }

        JSONObject userDataJson = response.getBody();
        User user = User.readUser(userDataJson);

        return user;
    }

    public List<String> requestRecentActivity(Long userId) {
        ResponseEntity<JSONObject> response = null;
        List<String> recentActivity = null;

        return null;
    }

    public UserPage requestUserPage(Long userId) {
        UserPage userPage;
        User user = requestUserInfo(userId);

        if (user == null) {
            return null;
        }

        return new UserPage(user);
    }
}
