package org.gamed.reviewdatabaseservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateAndUpdateCommentRequestModel {
    private String userId;      // ID of the user making the comment
    private String parentId;    // ID of the parent comment or review
    private String description;  // Content of the comment
}
