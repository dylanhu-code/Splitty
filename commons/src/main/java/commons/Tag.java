package commons;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String color;

    /**
     * empty constructor for object mapping
     */
    public Tag() {

    }

    /**
     * Contructore
     * @param name - name of tag
     * @param color - color of tag
     */
    public Tag(String name, String color) {
        this.name = name;
        this.color = color;
    }

    /**
     * getter for id
     * @return - id of tag
     */
    public Long getId() {
        return id;
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
        return Objects.equals(color, tag.color);
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
        return result;
    }
}
