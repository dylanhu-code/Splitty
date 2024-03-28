package server.database;

import commons.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Participant, Long> {
}
