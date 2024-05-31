package com.helpDesk.model;

import com.helpDesk.enums.Rate;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "FEEDBACKS")
public class Feedback implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID"
            , nullable = false
            , updatable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "RATE")
    private Rate rate;

    @Column(name = "DATE")
    private LocalDate date;

    @Column(name = "TEXT")
    private String text;

    @ManyToOne(cascade = {CascadeType.MERGE,
            CascadeType.REMOVE
    })
    private User user;

    @OneToOne
    @JoinColumn(name = "TICKET_ID")
    private Ticket ticket;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(id, feedback.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                '}';
    }
}
