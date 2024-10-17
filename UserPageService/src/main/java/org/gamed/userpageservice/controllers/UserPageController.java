package org.gamed.userpageservice.controllers;

import jakarta.transaction.Transactional;
import org.gamed.userpageservice.domain.UserPage;
import org.gamed.userpageservice.services.UserPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class UserPageController {

    @Autowired
    private UserPageService userPageService;

    private RestTemplate restTemplate = new RestTemplate();

    @Transactional
    @GetMapping("/user_page/{userId}")
    public ResponseEntity<UserPage> getUserPage(@PathVariable(name = "userId") Long userId) {
        UserPage userPage = userPageService.requestUserPage(userId);

        if (userPage == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userPage);
    }
}
