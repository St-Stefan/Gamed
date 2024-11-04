package org.gamed.userpageservice.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.gamed.userpageservice.DTOs.PlaytimeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class PlaytimeServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PlaytimeService playtimeService;

    private final String userPlaytimeDatabaseUrl = "http://localhost:8090/user/playtime";

    private PlaytimeDTO mockPlaytimeDTO1;
    private PlaytimeDTO mockPlaytimeDTO2;

    @BeforeEach
    public void setUp() {
        mockPlaytimeDTO1 = new PlaytimeDTO(
                "user123",
                "playtime1",
                "game1",
                120,
                LocalDateTime.parse("2023-10-01T10:00:00"),
                LocalDateTime.parse("2023-10-02T12:00:00")
        );

        mockPlaytimeDTO2 = new PlaytimeDTO(
                "user123",
                "playtime2",
                "game2",
                90,
                LocalDateTime.parse("2023-11-01T09:30:00"),
                LocalDateTime.parse("2023-11-02T11:45:00")
        );
    }

    @Test
    public void testRequestPlaytime_Success() {
        String userId = "user123";

        List<Map<String, Object>> mockPlaytimeRecords = new ArrayList<>();

        Map<String, Object> record1 = new LinkedHashMap<>();
        record1.put("id", "playtime1");
        record1.put("playtime", 120);
        record1.put("gameId", "game1");
        record1.put("timeCreated", "2023-10-01T10:00:00");
        record1.put("timeModified", "2023-10-02T12:00:00");
        mockPlaytimeRecords.add(record1);

        Map<String, Object> record2 = new LinkedHashMap<>();
        record2.put("id", "playtime2");
        record2.put("playtime", 90);
        record2.put("gameId", "game2");
        record2.put("timeCreated", "2023-11-01T09:30:00");
        record2.put("timeModified", "2023-11-02T11:45:00");
        mockPlaytimeRecords.add(record2);

        ResponseEntity<List> responseEntity = new ResponseEntity<>(mockPlaytimeRecords, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(userPlaytimeDatabaseUrl + "/" + userId + "/records"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class))
        ).thenReturn(responseEntity);

        List<PlaytimeDTO> result = playtimeService.requestPlaytime(userId);

        assertNotNull(result);
        assertEquals(2, result.size());

        PlaytimeDTO dto1 = result.get(0);
        assertEquals("playtime1", dto1.getId());
        assertEquals("game1", dto1.getGameId());
        assertEquals(120, dto1.getPlaytime());
        assertEquals(LocalDateTime.parse("2023-10-01T10:00:00"), dto1.getTimeCreated());
        assertEquals(LocalDateTime.parse("2023-10-02T12:00:00"), dto1.getTimeModified());

        PlaytimeDTO dto2 = result.get(1);
        assertEquals("playtime2", dto2.getId());
        assertEquals("game2", dto2.getGameId());
        assertEquals(90, dto2.getPlaytime());
        assertEquals(LocalDateTime.parse("2023-11-01T09:30:00"), dto2.getTimeCreated());
        assertEquals(LocalDateTime.parse("2023-11-02T11:45:00"), dto2.getTimeModified());
    }

    @Test
    public void testRequestPlaytime_HttpClientErrorException() {
        String userId = "user123";

        when(restTemplate.exchange(
                eq(userPlaytimeDatabaseUrl + "/" + userId + "/records"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class))
        ).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        List<PlaytimeDTO> result = playtimeService.requestPlaytime(userId);

        assertNull(result);
    }

    @Test
    public void testRequestPlaytime_EmptyList() {
        String userId = "user123";

        List<Map<String, Object>> mockPlaytimeRecords = new ArrayList<>();
        ResponseEntity<List> responseEntity = new ResponseEntity<>(mockPlaytimeRecords, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(userPlaytimeDatabaseUrl + "/" + userId + "/records"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class))
        ).thenReturn(responseEntity);

        List<PlaytimeDTO> result = playtimeService.requestPlaytime(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testRequestPlaytime_MalformedData() {
        String userId = "user123";

        List<Map<String, Object>> mockPlaytimeRecords = new ArrayList<>();

        Map<String, Object> malformedRecord = new LinkedHashMap<>();
        malformedRecord.put("id", "playtime1");
        malformedRecord.put("playtime", "two hours"); // Incorrect type: should be integer
        malformedRecord.put("gameId", "game1");
        malformedRecord.put("timeCreated", "2023-10-01T10:00:00");
        malformedRecord.put("timeModified", "2023-10-02T12:00:00");
        mockPlaytimeRecords.add(malformedRecord);

        ResponseEntity<List> responseEntity = new ResponseEntity<>(mockPlaytimeRecords, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(userPlaytimeDatabaseUrl + "/" + userId + "/records"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class))
        ).thenReturn(responseEntity);

        assertThrows(ClassCastException.class, () -> {
            playtimeService.requestPlaytime(userId);
        });
    }
}
