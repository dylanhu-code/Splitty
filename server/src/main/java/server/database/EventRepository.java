package server.database;

import commons.Event;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventRepository extends JpaRepository<Event, Long> {
    /**
     * Retrives event based on invite code
     * @param inviteCode - invite code of event wanted
     * @return - the event with the corresponding invite code
     */
    Event findByInviteCode(String inviteCode);
}
