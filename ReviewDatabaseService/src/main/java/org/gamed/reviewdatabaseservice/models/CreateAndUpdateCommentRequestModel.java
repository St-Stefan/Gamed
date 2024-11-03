package org.gamed.reviewdatabaseservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateAndUpdateCommentRequestModel {
    private String userId;
    private String parentId;
    private String description;
}
