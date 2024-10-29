package org.gamed.searchservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.gamed.searchservice.domain.GameDTO;

import java.util.List;

public class SearchResponseModel {

    @JsonProperty
    private GameDTO game;

    public GameDTO getGame() {
        return game;
    }

    public void setGames(GameDTO game) {
        this.game = game;
    }

    public SearchResponseModel(GameDTO game) {
        this.game = game;
    }
}
