package org.gamed.timelineservice.adapters;

import org.gamed.timelineservice.domain.GameListDTO;
import org.gamed.timelineservice.domain.UserDTO;
import org.gamed.timelineservice.models.PostRequestResponseModel;
import java.time.format.DateTimeFormatter;

public class GameListDTOToPostAdapter {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    public static PostRequestResponseModel convert(GameListDTO gameListDTO, UserDTO userDTO) {
        PostRequestResponseModel post = new PostRequestResponseModel(
                gameListDTO.getName(),
                gameListDTO.getDescription(),
                userDTO.getName(),
                gameListDTO.getTimeCreated(),
                0
        );

        post.setGames(gameListDTO.getGames());
        post.setUser(userDTO);
        post.setList(true);
        post.setReview(false);

        return post;
    }
}