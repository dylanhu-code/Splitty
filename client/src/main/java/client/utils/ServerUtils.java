/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import commons.*;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;

import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {

    private static final String SERVER = "http://localhost:8080/";
    private final StompSession session = connect("ws://localhost:8080/websocket");

    /**
     *
     * @throws IOException
     * @throws URISyntaxException
     */
    public void getQuotesTheHardWay() throws IOException, URISyntaxException {
        var url = new URI(SERVER + "api/quotes").toURL();
        var is = url.openConnection().getInputStream();
        var br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    /**
     *
     * @return -
     */
    public List<Quote> getQuotes() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Quote>>() {
                });
    }

    /**
     *
     * @param quote
     * @return -
     */
    public Quote addQuote(Quote quote) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(quote, APPLICATION_JSON), Quote.class);
    }

    /**
     * used to create a new event, by the "create" button in the start screen
     * @param event to add
     * @return event that was added
     */
    public Event addEvent(Event event) {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(SERVER).path("api/events") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .post(Entity.entity(event, APPLICATION_JSON), Event.class);
    }
    /**
     *
     * @param file file destination
     * @param ids ids to download when downloading multiple events
     */
    @FXML
    public void downloadJSONFile(File file, List<Long> ids) {
        try {
            if (file != null) {
                String url = null;
                if(Objects.equals(file.getName(), "events.json")){
                    // Convert each ID to a string and join them with ","
                    String idString = ids.stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(","));
                    // Combine with the base URL
                    url = SERVER + "api/JSON/multiple?ids=" + idString;
                }
                else url = SERVER + "api/JSON/" + ids.get(0);
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                try (InputStream inputStream = connection.getInputStream();
                     FileOutputStream outputStream = new FileOutputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds an expense to the server.
     * @param expense The expense instance.
     * @return The added expense.
     */
    public Expense addExpense(Expense expense) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/expenses") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(expense, APPLICATION_JSON), Expense.class);
    }

    private StompSession connect(String url) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
                converter.getObjectMapper().registerModule(new JavaTimeModule());
                stomp.setMessageConverter(converter);
        try {
            return stomp.connect(url, new StompSessionHandlerAdapter() {}).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }

    /**
     * Registers a consumer to listen for updates on a specific destination.
     * @param destination The destination to subscribe to.
     * @param type The type of payload.
     * @param consumer The consumer to handle received payloads.
     * @param <T> The type of payload.
     */
    public <T> void registerForUpdates(String destination, Class<T> type, Consumer<T> consumer) {
        session.subscribe(destination, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }
            @SuppressWarnings("unchecked")
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((T) payload);
            }
        });
    }

    /**
     * Method that retrieves all events currently in the database
     * @return - List of events in the database
     */
    public List<Event> getEvents() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/events") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Event>>() {
                });
    }

    /**
     * Deletes an event from the database
     *
     * @param eventid - the specified eventid of the event user wants to delete
     */
    public void deleteEvent(long eventid) {
        String deleteUrl = SERVER + "api/events/" + eventid;
        ClientBuilder.newClient(new ClientConfig())
                .target(deleteUrl)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }

    /**
     * Updates event
     * @param eventId - the id of the event
     * @param event - the new event
     * @return - the new updated event
     */
    public Event updateEvent(long eventId, Event event) {
        String updateUrl = SERVER + "api/events/" + eventId;
        return ClientBuilder.newClient(new ClientConfig())
                .target(updateUrl)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(event, APPLICATION_JSON), Event.class);
    }

    /**
     * Retrieves Event by id
     * @param id - id of event
     * @return - event with corresponding id
     */

    public Event getEventById(long id) {
        String updateUrl = SERVER + "api/events/" + id;
        return ClientBuilder.newClient(new ClientConfig())
                .target(updateUrl)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<Event>(){

                });
    }

    /**
     * Retrieves event based on INvite code
     * @param inviteCode - String representing invite code
     * @return - Event corresponding to the InviteCode
     */
    public Event getEventByInviteCode(String inviteCode) {
        String inviteUrl = SERVER+"api/events/invite/"+inviteCode;
        return ClientBuilder.newClient(new ClientConfig())
                .target(inviteUrl)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<Event>(){

                });
    }

    /**
     * Deletes an expense
     * @param expenseId - the expense's id
     * @return - a response
     */
    public Response deleteExpense(long expenseId) {
        String deleteUrl = SERVER + "api/expenses/" + expenseId;
        return ClientBuilder.newClient(new ClientConfig())
                .target(deleteUrl)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }

    /**
     * Updates an expense
     * @param expenseId - expense id of the current expense
     * @param expense - new updated expense
     * @return - the updated expense
     */
    public Expense updateExpense(long expenseId, Expense expense) {
        String updateUrl = SERVER + "api/expenses/" + expenseId;
        return ClientBuilder.newClient(new ClientConfig())
                .target(updateUrl)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(expense, APPLICATION_JSON), Expense.class);
    }

    /**
     * Adds a participant to the database
     * @param participant - the participant to add
     * @return - participant that was added
     */
    public Participant addParticipant(Participant participant) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/users/") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(participant, APPLICATION_JSON), Participant.class);

    }

    /**
     * Updates a given particpant
     * @param userId - participant's id
     * @param editedParticipant - new updated participant
     * @return - the new participant
     */
    public Participant updateParticipant(long userId, Participant editedParticipant) {
        String updateUrl = SERVER + "api/users/" + userId;
        return ClientBuilder.newClient(new ClientConfig())
                .target(updateUrl)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(editedParticipant, APPLICATION_JSON), Participant.class);
    }

    /**
     * deletes a participant
     * @param userId - the id of the participant to delete
     * @return - the response
     */
    public Response deleteParticipant(long userId) {
        String deleteUrl = SERVER + "api/users/" + userId;
        return ClientBuilder.newClient(new ClientConfig())
                .target(deleteUrl)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }

    /**
     * posts a password to the server
     * @param endpoint - the endpoint to post to
     * @param password - the password to post
     * @return - the response
     */
    public String post(String endpoint, String password) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080" + endpoint))
                .POST(HttpRequest.BodyPublishers.ofString(password))
                .build();

        try {
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sends a delete message for the specified event to the server via websockets.
     *
     * @param event The event to be deleted.
     */
    public void sendDeleteMsg(Event event) {
        String url = SERVER + "api/events/sendMsg";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(url, event, Void.class);
    }

    /**
     * deletes a tag
     * @param tagId - the id of the tag to delete
     * @return - the response
     */
    public Response deleteTag(long tagId) {
        String deleteUrl = SERVER + "api/tags/" + tagId;
        return ClientBuilder.newClient(new ClientConfig())
                .target(deleteUrl)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }

    public Tag addTag(Tag tag) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/tags") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(tag, APPLICATION_JSON), Tag.class);

    }
}