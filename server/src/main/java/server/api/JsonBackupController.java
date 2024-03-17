package server.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Event;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.EventService;


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
    /**
     * download a json backup of all events
     * @return event to download
     */
    @GetMapping(path = {"/all"})
    public ResponseEntity<String> createBackup() {
        ObjectMapper objectMapper = new ObjectMapper();
        StringBuilder jsonString = new StringBuilder();
        jsonString.append("[\n");
        for (int i = 0; i < eventService.getAllEvents().size(); i++) {
            Event event = eventService.getAllEvents().get(i);
            if (i>0) jsonString.append(",\n");
            if (event == null) return ResponseEntity.notFound().build();
            jsonString.append( "\t{\n" +
                    "\t\t\"eventId\": " + event.getEventId() + ",\n" +
                    "\t\t\"title\": \"" + event.getTitle() + "\",\n" +
                    "\t\t\"participantList\": " + event.getParticipantList() + ",\n" +
                    "\t\t\"debtList\": " + event.getDebtList() + ",\n" +
                    "\t\t\"expenseList\": " + event.getExpenseList() + ",\n" +
                    "\t\t\"creationDate\":\" " + event.getCreationDate() + "\",\n" +
                    "\t\t\"lastActivity\": \" " + event.getLastActivity() + "\",\n" +
                    "\t\t\"inviteCode\": \"" + event.getInviteCode() + "\",\n" +
                    "\t\t\"debts\": " + event.getDebts() + ",\n" +
                    "\t\t\"expenses\": " + event.getExpenses() + ",\n" +
                    "\t\t\"participants\": " + event.getParticipants() + "\n" +
                    "\t}");
        }
        jsonString.append("\n]");
        String jsonstring = jsonString.toString();

        // Set up HTTP headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDispositionFormData("attachment", "event.json");
        headers.setContentLength(jsonString.length());
        // Return the event JSON as a downloadable file
        return ResponseEntity.ok().headers(headers).body(jsonstring);
    }
}
