# OOPP Project
To execute the project, it needs to be ran with Gradle bootrun and run.

email: ooppteam42@gmail.com

The server URL to connect to can be changed in "client/config.txt" on line 2.

Should there be issues with adding expenses, events, etc.; try deleting "server/h2-database.mv.db"
and "client/client-specific-events.json" and restarting the application.

Websockets are implemented in client.scenes.StartScreenCtrl.registerEventListeners and
client.scenes.AdminCtrl.registerEventListeners

Long-polling implemented in client.utils.ServerUtils.registerForEventUpdates

Keyboard shortcuts found in all javafx.scene.Scene.setOnKeyPressed methods in
client.scenes.SplittyMainCtrl. These mostly consist of ENTERS and ESCAPE keys.
