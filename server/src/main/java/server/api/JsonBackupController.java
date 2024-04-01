package server.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import commons.Event;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.EventService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/JSON")
public class JsonBackupController {
    private final EventService eventService;

    /**
     * I need some EventService methods
     * @param eventService
     */
    public JsonBackupController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * download a json backup
     * @param id event to download
     * @return event
     */
    @GetMapping(path = {"/{id}"})
    public ResponseEntity<String> createBackup(@PathVariable long id)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Event event = eventService.findEvent(id);
        if (event == null) return ResponseEntity.notFound().build();

        String response = objectMapper.writeValueAsString(event);

        // Set up HTTP headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDispositionFormData("attachment", "event.json");
        headers.setContentLength(response.length());
        // Return the event JSON as a downloadable file
        return ResponseEntity.ok().headers(headers).body(response);


    }
    /**
     * download a json backup of all events
     * @param ids id's to back up
     * @return event to download
     */
    @GetMapping(path = {"/multiple"})
    public ResponseEntity<byte[]> createBackup(@RequestParam List<Long> ids)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Initialize the events list
        List<Event> events = new ArrayList<>();

        // Populate the events list
        for (Long id : ids) {
            events.add(eventService.findEvent(id));
        }

        // Check if events were found for all provided IDs
        if (events.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Convert events to JSON string
        String response = objectMapper.writeValueAsString(events);

        // Convert JSON string to bytes
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

        // Set up HTTP headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDispositionFormData("attachment", "events.json");
        headers.setContentLength(responseBytes.length);

        // Return the event JSON as a downloadable file
        return ResponseEntity.ok().headers(headers).body(responseBytes);
    }

}
