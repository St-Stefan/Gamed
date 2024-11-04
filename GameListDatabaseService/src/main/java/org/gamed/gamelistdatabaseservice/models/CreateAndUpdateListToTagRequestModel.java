package org.gamed.gamelistdatabaseservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Model for creating and updating ListToTag relationships.
 */
@Data
@AllArgsConstructor
public class CreateAndUpdateListToTagRequestModel {
    private String listId;
    private String tagId;
}
