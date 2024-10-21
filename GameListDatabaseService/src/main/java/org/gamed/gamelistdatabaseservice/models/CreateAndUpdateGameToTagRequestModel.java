package org.gamed.gamelistdatabaseservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request model for creating or updating a GameToTag entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAndUpdateGameToTagRequestModel {
    private String gameId;
    private String tagId;
}
