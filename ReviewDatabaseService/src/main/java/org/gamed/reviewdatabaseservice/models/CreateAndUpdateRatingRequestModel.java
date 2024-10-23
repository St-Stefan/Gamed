package org.gamed.reviewdatabaseservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateAndUpdateRatingRequestModel {
    private String category;
    private int score;
    private String graph;
}
