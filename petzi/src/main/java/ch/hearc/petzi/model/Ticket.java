package ch.hearc.petzi.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Nom de la personne qui a soumis le ticket
    private String description; // Description du ticket
    @Temporal(TemporalType.TIMESTAMP)
    private Date submissionDate; // Date de soumission du ticket

    // Constructeurs
    public Ticket() {
    }

    public Ticket(String name, String description, Date submissionDate) {
        this.name = name;
        this.description = description;
        this.submissionDate = submissionDate;
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    // Méthode toString pour une représentation en chaîne de caractère de l'objet
    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", submissionDate=" + submissionDate +
                '}';
    }
}
