# OOPP Project
To execute the project, it needs to be ran with Gradle bootrun and run.

email: ooppteam42@gmail.com

The server URL to connect to can be changed in client/config.txt:2

Websockets are implemented in client.scenes.StartScreenCtrl.registerEventListeners and
client.scenes.AdminCtrl.registerEventListeners

Long-polling implemented in client.utils.ServerUtils.registerForEventUpdates

Keyboard shortcuts found in all javafx.scene.Scene.setOnKeyPressed methods in
client.scenes.SplittyMainCtrl. These mostly consist of ENTERS and ESCAPE keys.