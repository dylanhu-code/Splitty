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
package client;

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;

import client.scenes.*;
import client.utils.ServerUtils;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    /**
     *
     * @param args
     * @throws URISyntaxException
     * @throws IOException
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    /**
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException
     */

    @Override
    public void start(Stage primaryStage) throws IOException {

        var overview = FXML.load(OverviewCtrl.class, "client", "scenes", "Overview.fxml");
        var startScreen = FXML.load(StartScreenCtrl.class, "client", "scenes", "StartScreen.fxml");
        var backups = FXML.load(BackupsCtrl.class, "client", "scenes", "Backups.fxml");

        var addParticipant = FXML.load(AddParticipantCtrl.class,
                "client","scenes", "AddParticipant.fxml");
        var addExpense = FXML.load(AddExpenseCtrl.class,"client","scenes","AddExpense.fxml");
        var invitation = FXML.load(InvitationCtrl.class, "client", "scenes", "Invitation.fxml");
        var openDebts = FXML.load(OpenDebtsCtrl.class, "client", "scenes", "OpenDebts.fxml");
        var admin = FXML.load(AdminLoginCtrl.class, "client", "scenes","Admin.fxml");
        var eventsOverview = FXML.load(EventsOverviewCtrl.class, "client", "scenes", "EventsOverview.fxml");
        var mainCtrl = INJECTOR.getInstance(SplittyMainCtrl.class);
        EventStorageManager storageManager = new EventStorageManager(new ServerUtils());
        mainCtrl.initialize(primaryStage, overview, startScreen, backups, addParticipant,
                addExpense, invitation, openDebts, eventsOverview, admin, storageManager);

    }
}