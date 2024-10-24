package org.gamed.userpageservice.controllers;

import jakarta.transaction.Transactional;
import org.gamed.userpageservice.domain.UserPage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import static org.gamed.userpageservice.services.UserPageService.requestUserPage;

@RestController
@RequestMapping("/user_page")
public class UserPageController {
    private RestTemplate restTemplate = new RestTemplate();

    @Transactional
    @GetMapping("/{userId}")
    public ResponseEntity<UserPage> getUserPage(@PathVariable(name = "userId") String userId) {
        UserPage userPage = requestUserPage(userId);

        if (userPage == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userPage);
    }
}
