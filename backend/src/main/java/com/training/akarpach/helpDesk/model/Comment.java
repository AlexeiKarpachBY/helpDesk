package com.training.akarpach.helpDesk.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "COMMENTS")
@NamedQuery(name = "allCommentsByTicketId", query = "FROM Comment c WHERE c.ticket.id = :id")
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID"
            , nullable = false
            , updatable = false)
    private Long id;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "DATE")
    private LocalDate date;

    @ManyToOne(cascade = {CascadeType.MERGE,
            CascadeType.REMOVE
    })
    private User user;

    @ManyToOne(cascade = {CascadeType.MERGE,
            CascadeType.REMOVE
    })
    private Ticket ticket;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                '}';
    }
}
