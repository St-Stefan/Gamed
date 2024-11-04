package org.gamed.gamelistdatabaseservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateAndUpdateListToGameRequestModel {
    private String listId;
    private String gameId;
}
