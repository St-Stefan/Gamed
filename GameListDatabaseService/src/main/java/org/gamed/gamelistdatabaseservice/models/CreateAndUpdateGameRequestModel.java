package org.gamed.gamelistdatabaseservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Request model for creating and updating a Game entity.
 */
@Data
@AllArgsConstructor
public class CreateAndUpdateGameRequestModel {
    private String name;
    private String developer;
    private LocalDateTime releaseDate;
    private String platforms;
}
