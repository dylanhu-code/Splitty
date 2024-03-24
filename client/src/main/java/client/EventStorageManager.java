package client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Inject;
import commons.Event;
import client.utils.ServerUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EventStorageManager {
    private static final String RESOURCE_FILE_PATH = "client/src/main/resources/client-specific-events.json";

    private final ServerUtils serverUtils;

    @Inject
    public EventStorageManager(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    public void saveEventIdToFile(long eventId) {
        try {
            List<Long> eventIds = loadEventIdsFromFile();
            if (!eventIds.contains(eventId)) {
                eventIds.add(eventId);
                saveEventIdsToFile(eventIds);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Long> loadEventIdsFromFile() throws IOException {
        File file = new File(RESOURCE_FILE_PATH);
        if (!file.exists()) {
            file.createNewFile();
            return new ArrayList<>();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        List<Long> eventIds = new ArrayList<>();
        if (file.length() > 0) {
            eventIds = objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, Long.class));
        }
        return eventIds;
    }

    private void saveEventIdsToFile(List<Long> eventIds) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.writeValue(new File(RESOURCE_FILE_PATH), eventIds);
    }

    public List<Event> getEventsFromDatabase() {
        List<Long> eventIds = null;
        try {
            eventIds = loadEventIdsFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Event> events = new ArrayList<>();
        if (eventIds != null) {
            for (Long eventId : eventIds) {
                Event event = serverUtils.getEventById(eventId);
                if (event != null) {
                    events.add(event);
                } else {
                    deleteEventFromFile(eventId);
                }
            }
        }
        return events;
    }

    public void deleteEventFromFile(long eventId) {
        try {
            List<Long> eventIds = loadEventIdsFromFile();
            if (eventIds.contains(eventId)) {
                eventIds.remove(eventId);
                saveEventIdsToFile(eventIds);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
