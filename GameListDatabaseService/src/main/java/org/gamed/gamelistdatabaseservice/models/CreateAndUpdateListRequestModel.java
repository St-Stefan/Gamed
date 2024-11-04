package org.gamed.gamelistdatabaseservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateAndUpdateListRequestModel {
    private String name;
    private String userId;
    private String description;
}
