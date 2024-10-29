package org.gamed.searchservice.controllers;


import org.gamed.searchservice.domain.GameDTO;
import org.gamed.searchservice.models.SearchResponseModel;
import org.gamed.searchservice.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/search")
public class FrontEndController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/{query}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<List<SearchResponseModel>> searchGames(@PathVariable String query) {
        List<SearchResponseModel> searchResult = searchService.searchGames(query);

        return ResponseEntity.ok(searchResult);
    }
}
