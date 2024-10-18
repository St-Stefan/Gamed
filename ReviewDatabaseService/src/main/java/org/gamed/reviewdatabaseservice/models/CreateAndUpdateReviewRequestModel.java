package org.gamed.reviewdatabaseservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateAndUpdateReviewRequestModel {
    private String userId;
    private String gameId;
    private String description;
}
