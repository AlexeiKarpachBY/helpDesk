package com.helpDesk.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "HISTORY")
@NamedQuery(name = "allHistoriesByTicketId", query = "FROM History h WHERE h.ticket.id = :id")
public class History implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID"
            , nullable = false
            , updatable = false)
    private Long id;

    @Column(name = "DATE")
    private LocalDate date;

    @Column(name = "ACTION")
    private String action;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(cascade = {CascadeType.MERGE,
            CascadeType.REMOVE
    })
    private Ticket ticket;

    @ManyToOne(cascade = {CascadeType.MERGE,
            CascadeType.REMOVE
    })
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        History history = (History) o;
        return Objects.equals(id, history.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "History{" +
                "id=" + id +
                '}';
    }
}
