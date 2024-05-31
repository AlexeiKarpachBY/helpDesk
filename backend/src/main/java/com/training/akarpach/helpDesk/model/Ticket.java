package com.training.akarpach.helpDesk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.training.akarpach.helpDesk.enums.State;
import com.training.akarpach.helpDesk.enums.Urgency;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "TICKETS")
@NamedQuery(name = "allUserTicket", query = "FROM Ticket t WHERE t.owner.id = :id")
@NamedQuery(name = "managerRole", query = "FROM Ticket t WHERE t.owner.id = :id OR t.approver.id = :id OR t.state ='NEW'")
@NamedQuery(name = "engineerRole", query = "FROM Ticket t WHERE t.assignee.id = :id OR t.approver.id !=null AND t.state ='APPROVED'")
@NamedQuery(name = "allEngineerTicket", query = "FROM Ticket t WHERE t.assignee.id =:id")
@NamedQuery(name = "allManagerTicket", query = "FROM Ticket t WHERE t.owner.id =:id OR t.approver.id = :id")
public class Ticket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID"
            , nullable = false
            , updatable = false)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DESIRED_RESOLUTION_DATE")
    private LocalDate desiredResolutionDate;

    @Column(name = "CREATED_ON")
    private LocalDate createdOn;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATE")
    private State state;

    @Enumerated(EnumType.STRING)
    @Column(name = "URGENCY")
    private Urgency urgency;

    @OneToOne(mappedBy = "ticket"
            , cascade = CascadeType.ALL
            , orphanRemoval = true
            , fetch = FetchType.LAZY)
    private Feedback feedback;

    @ManyToOne(cascade = {CascadeType.MERGE,
            CascadeType.REMOVE
    })
    private User approver;

    @ManyToOne(cascade = {CascadeType.MERGE,
            CascadeType.REMOVE
    })
    private User owner;

    @ManyToOne(cascade = {CascadeType.MERGE,
            CascadeType.REMOVE
    })
    private User assignee;

    @ManyToOne(cascade = {CascadeType.MERGE,
            CascadeType.REMOVE
    })
    private Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "ticket"
            , cascade = CascadeType.ALL
            , orphanRemoval = true)
    private Set<History> histories = new HashSet<>();


    @OneToMany(mappedBy = "ticket"
            , cascade = CascadeType.ALL
            , orphanRemoval = true)
    private Set<Attachment> attachments = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "ticket"
            , cascade = CascadeType.ALL
            , orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    public void removeAttachment(Attachment attachment) {
        attachments.remove(attachment);
        attachment.setTicket(null);
    }

    public void addAttachment(Attachment attachment) {
        attachments.add(attachment);
        attachment.setTicket(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setTicket(null);
    }

    public void removeHistory(History history) {
        histories.remove(history);
        history.setTicket(null);
    }

    public void addHistory(History history) {
        histories.add(history);
        history.setTicket(this);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setTicket(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id) && Objects.equals(name, ticket.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
