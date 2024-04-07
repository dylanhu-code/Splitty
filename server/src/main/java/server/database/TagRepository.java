package server.database;

import commons.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    /**
     * method to retrieve tags by eventId
     * @param event - event id
     * @return - list of tags
     */
    List<Tag> getTagsByEvent(Long event);

}
