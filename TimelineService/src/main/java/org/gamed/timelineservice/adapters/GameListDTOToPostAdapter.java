package org.gamed.timelineservice.adapters;

import org.gamed.timelineservice.domain.GameListDTO;
import org.gamed.timelineservice.domain.UserDTO;
import org.gamed.timelineservice.models.PostRequestResponseModel;
import java.time.format.DateTimeFormatter;

public class GameListDTOToPostAdapter {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    public static PostRequestResponseModel convert(GameListDTO gameListDTO, UserDTO userDTO) {
        // Create a new PostRequestResponseModel using fields from GameListDTO
        PostRequestResponseModel post = new PostRequestResponseModel(
                gameListDTO.getName(),                // title
                gameListDTO.getDescription(),         // content
                userDTO.getName(),              // author
                gameListDTO.getTimeCreated(), // timestamp
                0                                     // likes, setting to 0 initially
        );

        post.setGames(gameListDTO.getGames());
        post.setUser(userDTO);  // Assuming `UserDTO` can be set to `null` as no `UserDTO` in `GameListDTO`
        post.setList(true);
        post.setReview(false);

        return post;
    }
}