package org.gamed.userpageservice.controllers;

import jakarta.transaction.Transactional;
import org.gamed.userpageservice.DTOs.UserPage;
import org.gamed.userpageservice.services.UserPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/user_page")
@CrossOrigin(origins = "http://localhost:5173")
public class UserPageController {
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private UserPageService userPageService;

    @Transactional
    @GetMapping("/{userId}")
    public ResponseEntity<UserPage> getUserPage(@PathVariable(name = "userId") String userId) {
        UserPage userPage = userPageService.requestUserPage(userId);

        if (userPage == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userPage);
    }
}
