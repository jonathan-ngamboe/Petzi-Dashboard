package ch.hearc.petzi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "json_storage")
public class JsonStorage {

    @Id
    private String key;

    @Lob
    private String value;

    // Getters et setters
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
