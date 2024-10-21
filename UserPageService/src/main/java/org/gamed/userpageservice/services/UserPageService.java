package org.gamed.userpageservice.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.NoArgsConstructor;
import org.gamed.userpageservice.domain.DTOs.UserDTO;
import org.gamed.userpageservice.domain.DTOs.UserToFollowedListDTO;
import org.gamed.userpageservice.domain.DTOs.UserToFollowedUserDTO;
import org.gamed.userpageservice.domain.UserPage;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@NoArgsConstructor
public class UserPageService {
    private RestTemplate restTemplate = new RestTemplate();

    private final String userDatabaseUrl = "http://localhost:8090/users";

    public UserDTO getUserDTOFromApiResponse(String userId) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

//        RestTemplate restTemplate = new RestTemplate();
//        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
//        messageConverter.setObjectMapper(mapper);
//        restTemplate.getMessageConverters().addFirst(messageConverter);

        // Make the API call and directly get the response as UserDTO
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(
                userDatabaseUrl + "/" + userId, // Replace with your actual endpoint
                HttpMethod.GET,
                null,
                JSONObject.class
        );

//        UserDTO userDTO = responseEntity.getBody();
        Object obj = responseEntity.getBody();
        System.out.println(obj.getClass());
//        return userDTO;
        return null;
    }

    public static UserDTO readUser(LinkedHashMap<String, Object> userData) {
        String id, name, email, pwdHash;
        boolean developer, premium;
        UserToFollowedUserDTO followedUsers;
        UserToFollowedListDTO followedLists;
        LocalDateTime timeCreated, timeModified;

        id = (String) userData.get("id");
        name = (String) userData.get("name");
        email = (String) userData.get("email");
        pwdHash = (String) userData.get("pwdHash");

        developer = (boolean) userData.get("developer");
        premium = (boolean) userData.get("premium");

        followedUsers = (UserToFollowedUserDTO) userData.get("followedUsers");
        followedLists = (UserToFollowedListDTO) userData.get("followedLists");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

        timeCreated = LocalDateTime.parse((String) userData.get("timeCreated"), formatter);
        timeModified = LocalDateTime.parse((String) userData.get("timeModified"), formatter);

//        return new UserDTO(id, name, email, pwdHash, developer, premium, timeCreated, timeModified);
        return new UserDTO(id, name, email, pwdHash, developer, premium, followedUsers, followedLists, timeCreated, timeModified);
    }

    public UserDTO requestUserInfo(String userId) {
        ResponseEntity<UserDTO> response = null;

        try {
            response = restTemplate.exchange(
                    userDatabaseUrl + "/" + userId,
                    HttpMethod.GET,
                    null,
                    UserDTO.class
            );
        } catch (HttpClientErrorException e) {
            return null;
        }

//        LinkedHashMap<String, Object> userDataJson = response.getBody();
//        UserDTO userDTO = readUser(userDataJson);

//        return userDTO;
        return response.getBody();
//        return null;
    }

    public UserPage requestUserPage(String userId) {
        UserDTO userDTO = requestUserInfo(userId);
//        UserDTO userDTO = getUserDTOFromApiResponse(userId);

        if (userDTO == null) {
            return null;
        }

        return new UserPage(userDTO);
    }
}
