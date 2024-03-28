| Key | Value                                                |
| --- |------------------------------------------------------|
| Date: | 26.03.2024                                           |
| Time: | 14:45-15:30                                          |
| Location: | Drebbleweg project room 3 cubicle 11                 |
| Chair | Mireia Carrio Cortada                                |
| Minute Taker | Tigo Schutgens                                       |
| Attendees: | Mireia Carrio Cortada, Timo Rouw, Dylan Hu, Daria Condratov, Adrian Todorov |
Agenda Items:
- Opening by chair (1 min)

- Check-in: How is everyone doing? Tasks completed this week + Announcements (3 min)
    - in general everything went fine, some issues that will be discussed later.

- Approval of the agenda - Does anyone have any additions? (1 min)
    - Talk about the feedback that was received after the creation of the agenda

- Approval of last minutes - Did everyone read the minutes from the previous meeting? (1 min)
    - Everything okay

- Announcements by the TA (3 min)
    - Our app can be run, but if configuration is changed we should write it in the README. Other groups did not do that.
    
- Presentation of the current app to TA (4 min)

- Product Pitch Draft Discussion - what has to be done, division ...? (5 min)
    - We have to make a presentation, Draft due thursday 28th of march
    - Discussing the assignment in more detail in our own meeting.

- Announcements by members of the team if they encountered any issues during week 6 (11 min)
    - Mireia: Problem with the creating of an event, websockets did not do that correctly
            - Look at the User class again, since preferred language e.g. is not necessary, also update db for that.
    - Dylan: Issues with getting the websocket handshake done, will have to be fixed.
    - Adrian: Closing the application with the "x" button does not fully close the process since recent MR's.
            - When language is switched in the main page, and then switch in the overview again, the start screen does not      get           updated.       
    - Tigo: Long polling did not register updates correctly, has to be worked on this week.
    - Timo: issue with manual JSON formatting, works now. Wasn't working with the right main controller, Quote Mainctrl is not necessary anymore.
    - Daria: Did not really have problems, but could not test methods for the frontend that were not implemented by herself.

- HCI and Testing Feedback:(3 min)
  -HCI: insufficient
      - look at colours as a team later on.
      - More important: implement ability to navigate through keys.
      - Also, feedback popups like: "Event added" or "are you sure?"
  Testing: Good
    - but testing should be done while working on a feature, instead of after merging. 
    - More tests is also not a bad thing.

- Discussion of the current state of the app and what needs to be fixed or implemented in the future (15 min)
  - main focus on finishing basic requirements and starting feature implementation
  - add participants, invite codes, settling debts, seeing debts
  - Language template (should not be difficult)
  - Restoring backups
  - fixing long-polling
  - fixing websockets, might depend on adding participants depending what it will be implemented for

- Division of the work for the following week and summarizing the action points: Who, what, when? (- min)
  - Tigo: finish config, fix long-polling, key navigating, frontend tests, fix bug in language switching
  - Timo: importing backups and implementing assurance messages, starting on detailed expenses
  - Dylan: fix websockets and starting on the open debts
  - Daria: error messaging, finish event overview for admins.
  - Adrian: Help with event overview with admins and do language template
  - Mireia: Adding, deleting and editing participants, possibly getting started with statistics

- Feedback round: What went well and what can be improved next time? (1 min)
  - nothing big

- Question round: Does anyone have anything to add before the meeting closes? (1 min)
  - no comments
- Planned meeting duration != actual duration? Where/why did you misestimate? (1 min)
  - duration went as planned
- Closure and recap on the meeting if needed (1 min)
  - 
