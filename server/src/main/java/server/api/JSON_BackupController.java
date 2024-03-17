package server.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Event;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.services.EventService;

import java.io.File;
import java.io.FileWriter;

@RestController
@RequestMapping("/api/JSON")
public class JSON_BackupController {
    private final EventService eventService;

    public JSON_BackupController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * download a json backup
     * @param id
     * @return event to download
     */
    @GetMapping(path = {"/{id}"})
    public ResponseEntity<String> createBackup(@PathVariable long id) {
        ObjectMapper objectMapper = new ObjectMapper();

        Event event = eventService.findEvent(id);
        if (event == null) return ResponseEntity.notFound().build();
        String jsonString = "{\n" +
                "\t\"eventId\": " + event.getEventId() + ",\n" +
                "\t\"title\": \"" + event.getTitle() + "\",\n" +
                "\t\"participantList\": " + event.getParticipantList() + ",\n" +
                "\t\"debtList\": " + event.getDebtList() + ",\n" +
                "\t\"expenseList\": " + event.getExpenseList() + ",\n" +
                "\t\"creationDate\": " + event.getCreationDate() + ",\n" +
                "\t\"lastActivity\": \"2024-03-17T15:32:33.260553\",\n" +
                "\t\"inviteCode\": \"" + event.getInviteCode() + "\",\n" +
                "\t\"debts\": " + event.getDebts() + ",\n" +
                "\t\"expenses\": " + event.getExpenses() + ",\n" +
                "\t\"participants\": " + event.getParticipants() + "\n" +
                "}";


        // Set up HTTP headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDispositionFormData("attachment", "event.json");
        headers.setContentLength(jsonString.length());
        // Return the event JSON as a downloadable file
        return ResponseEntity.ok().headers(headers).body(jsonString);


    }


}
