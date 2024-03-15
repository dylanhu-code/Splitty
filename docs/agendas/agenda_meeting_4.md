| Key | Value                                                             |
| --- |-------------------------------------------------------------------|
| Date: | 05.03.2024                                                        |
| Time: | 14:45-15:30                                                       |
| Location: | Drebbleweg project room 3 cubicle 11                              |
| Chair | Adrian Todorov                                                    |
| Minute Taker | Dylan Hu                                                                  |
| Attendees: | Mireia Carrio Cortada, Timo Rouw, Tigo Schutgens, Daria Condratov |
Agenda Items:
- Opening by chair (1 min) 

- Check-in: How is everyone doing? (2 min) 
    - Good, except some issues elaborated later.

- Announcements by the team (2 min) -

- Approval of the agenda - Does anyone have any additions? (2 min) -
    - No additions.

- Approval of last minutes - Did everyone read the minutes from the previous meeting? (1 min) 

- Announcements by the TA (3 min) 
    - A MR with a failing pipeline does not count.
    - Mistake in knockout criteria emails this week. 
    - Knockout gets manual checked until maybe this week.
    - If u have extra exams in the week of the finals, notice the TA.

- Presentation of the current app to TA (3 min)

- Announcements by members of the team if they encountered any issues during week 3 (3 min) +
    - Language switch issues. 
    - Start screen and overview page are not connected.
    - Recently viewed events issues. 
    - Issues with the connection to the database, how to include user (event creator) when adding event to database.

- Discussion of the current state of the app - ~~recap on what was done during week~~ 3 and what needs to be fixed or implemented in the future (7 min) +
    - Recap was already done.
    - The admin should have a specific url.
    - Connecting backend to frontend. 
    - Connecting pages to each other.
    - Debt and invitation page implementation.
    - Further backend implementation.
    - Language switch.
    - Setting up the connection to the database.

- Next steps that need to be taken - discussion of the next step of the project and division of the work for the following week (10 min) -
    - Meeting tommorow (6-3-24) 11:00.
    - Adrian switches from frontend to backend.
    - Add/delete participant should edit the participantlist of the event instead of the whole event.

- Summarize action points: Who , what , when? (2 min) -
    - Will be discussed in the meeting tommorow.
    - Finalized in the section 'Action Points' below.

- Feedback round: What went well and what can be improved next time? (2 min) -
    - The MR should be requested a bit earlier, to give the reviewer more time.

- Planned meeting duration != actual duration? Where/why did you misestimate? (2 min)
    - Meeting ended 5min early, misestimations are noted with '+' and '-' behind the estimations.

- Question round: Does anyone have anything to add before the meeting closes? (3 min) -

- Closure and recap on the meeting if needed (2 min) -

- Action points (discussed in the meeting 6-3-24): 
    - Timo:
        - Event controller; put for participant delete + event tests?
        - JSON request body.
        - Web Sockets endpoints.

    - Adrian: 
        - Password generation.
        - User controller.
        - Service.

    - Daria: 
        - Filter events by title, creation date, last activity (change all methods to update activity).
        - JSON backups.

    - Mireia:
        - Debt calculator
        - Statistics.

    - Tigo: 
        - Main ctrl (hard coded?).
        - Launching app.
        - Return buttons.

    - Dylan:
        - Debt page and Ctrl.
        - Invite page and Ctrl.

    - Whoever has time:
        - Language switch.
        - Client config file.


