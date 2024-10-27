package org.gamed.searchservice.controllers;


import org.gamed.searchservice.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/search")
public class FrontEndController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/{query}")
    public ResponseEntity
}
