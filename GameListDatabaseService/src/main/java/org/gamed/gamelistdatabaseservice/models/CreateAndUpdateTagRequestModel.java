package org.gamed.gamelistdatabaseservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Request model for creating and updating a Tag entity.
 */
@Data
@AllArgsConstructor
public class CreateAndUpdateTagRequestModel {
    private String name; // Name of the tag
}
