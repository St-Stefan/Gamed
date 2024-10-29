package org.gamed.timelineservice.adapters;

import org.gamed.timelineservice.domain.GameDTO;
import org.gamed.timelineservice.domain.GameListDTO;
import org.gamed.timelineservice.domain.UserDTO;
import org.gamed.timelineservice.models.PostRequestResponseModel;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GameListDTOToPostAdapterTest {

    @Test
    void testConvertBasicFields() {
        // Arrange
        String id = "1";
        String userId = "user1";
        String name = "Game List Title";
        String description = "A description of the game list.";
        LocalDateTime timeCreated = LocalDateTime.now();
        LocalDateTime timeModified = LocalDateTime.now();
        List<GameDTO> games = Collections.emptyList();
        GameListDTO gameListDTO = new GameListDTO(id, userId, name, description, timeCreated, timeModified, games);

        UserDTO userDTO = new UserDTO();
        userDTO.setId("user1");
        userDTO.setName("John Doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setDeveloper(false);
        userDTO.setPremium(true);

        // Act
        PostRequestResponseModel post = GameListDTOToPostAdapter.convert(gameListDTO, userDTO);

        // Assert
        assertNotNull(post);
        assertEquals(name, post.getTitle());
        assertEquals(description, post.getContent());
        assertEquals(userDTO.getName(), post.getAuthor());
        assertEquals(timeCreated.toString(), post.getTimestamp().toString());
        assertEquals(games, post.getGames());
        assertEquals(userDTO, post.getUser());
        assertEquals(0, post.getLikes());
        assertEquals(true, post.isList());
        assertEquals(false, post.isReview());
    }

    @Test
    void testConvertWithNonEmptyGames() {
        // Arrange
        GameDTO game = new GameDTO("Test Game", "Game Dev", LocalDateTime.of(2023, 5, 20, 0, 0), "PC, Console");
        game.setId("game1");
        List<GameDTO> games = List.of(game);
        GameListDTO gameListDTO = new GameListDTO("2", "user2", "Another Title", "Another Description",
                LocalDateTime.now(), LocalDateTime.now(), games);

        UserDTO userDTO = new UserDTO();
        userDTO.setId("user2");
        userDTO.setName("Jane Smith");
        userDTO.setEmail("jane.smith@example.com");
        userDTO.setDeveloper(true);
        userDTO.setPremium(false);

        // Act
        PostRequestResponseModel post = GameListDTOToPostAdapter.convert(gameListDTO, userDTO);

        // Assert
        assertNotNull(post.getGames());
        assertEquals(1, post.getGames().size());
        assertEquals(game, post.getGames().get(0));
        assertEquals("game1", post.getGames().get(0).getId());
        assertEquals("Test Game", post.getGames().get(0).getName());
        assertEquals("Game Dev", post.getGames().get(0).getDeveloper());
        assertEquals("PC, Console", post.getGames().get(0).getPlatforms());
        assertEquals(LocalDateTime.of(2023, 5, 20, 0, 0), post.getGames().get(0).getReleaseDate());
    }

    @Test
    void testConvertWithNullDescription() {
        // Arrange
        GameListDTO gameListDTO = new GameListDTO("3", "user3", "Title without Description", null,
                LocalDateTime.now(), LocalDateTime.now(), Collections.emptyList());

        UserDTO userDTO = new UserDTO();
        userDTO.setId("user3");
        userDTO.setName("Alex Johnson");
        userDTO.setEmail("alex.johnson@example.com");
        userDTO.setDeveloper(false);
        userDTO.setPremium(true);

        // Act
        PostRequestResponseModel post = GameListDTOToPostAdapter.convert(gameListDTO, userDTO);

        // Assert
        assertNotNull(post);
        assertEquals("Title without Description", post.getTitle());
        assertEquals(null, post.getContent());  // Content should be null
        assertEquals(userDTO.getName(), post.getAuthor());
    }
}