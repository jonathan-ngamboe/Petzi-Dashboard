package ch.hearc.heg.petziHook.persistence;

import jakarta.persistence.*;

@Entity
@Table(name = "json_record")
public class JsonRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    private String json_value;

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJson_value() {
        return json_value;
    }

    public void setJson_value(String json_value) {
        this.json_value = json_value;
    }
}
