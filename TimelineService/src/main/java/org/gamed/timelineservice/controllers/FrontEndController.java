package org.gamed.timelineservice.controllers;

import org.gamed.timelineservice.domain.Post;
import org.gamed.timelineservice.models.PostRequestResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/home")
public class FrontEndController {

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/{userID}")
    public ResponseEntity<List<PostRequestResponseModel>> getTimeline(@PathVariable String userID){

        List<PostRequestResponseModel> testList = new ArrayList<>();

        testList.add(new PostRequestResponseModel(
                "First Post",
                "This is the content of the first post.",
                "John Doe",
                "2023-10-17T12:00:00Z",
                10
        ));

        testList.add(new PostRequestResponseModel(
                "Second Post",
                "This is the content of the second post.",
                "Jane Smith",
                "2023-10-18T15:30:00Z",
                5
        ));

        return ResponseEntity.ok(testList);
    }
}
