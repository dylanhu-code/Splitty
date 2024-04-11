package commons;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String color;

    private Long event;

    /**
     * empty constructor for object mapping
     */
    public Tag() {

    }

    /**
     * Contructore
     * @param name - name of tag
     * @param color - color of tag
     * @param event - event
     */
    public Tag(String name, String color, Long event) {
        this.name = name;
        this.color = color;
        this.event = event;
    }

    /**
     * getter for id
     * @return - id of tag
     */
    public Long getId() {
        return id;
    }

    /**
     * Getter for the event id
     * @return - id
     */
    public Long getEvent() {
        return event;
    }

    /**
     * Setter for the event id
     * @param event - sets particular event
     */
    public void setEvent(Long event) {
        this.event = event;
    }

    /**
     * setter for id
     * @param id - new id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * getter for name
     * @return - new name
     */
    public String getName() {
        return name;
    }

    /**
     * setter for name
     * @param name - new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter for color
     * @return - color
     */
    public String getColor() {
        return color;
    }

    /**
     * setter for color
     * @param color - new color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Checks for equalit
     * @param o - object to compare
     * @return - true if object is the same Tag, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (!Objects.equals(id, tag.id)) return false;
        if (!Objects.equals(name, tag.name)) return false;
        if (!Objects.equals(color, tag.color)) return false;
        return Objects.equals(event, tag.event);
    }

    /**
     * HashCode function
     * @return - int htat uniquely identifies tag
     */
    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (event != null ? event.hashCode() : 0);
        return result;
    }

    /**
     * Returns a string representation of the tag.
     *
     * @return A string representation of the tag.
     */
    @Override
    public String toString() {
        return "Tag{" +
                "name= " + name +
                ", color= " + color +
                '}';
    }
}
