package com.helpDesk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.helpDesk.enums.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "USERS")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID"
            , nullable = false
            , updatable = false)
    private Long id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE"
            , nullable = false)
    private Role role;

    @Column(name = "EMAIL")
    private String email;

    @JsonIgnore
    @Column(name = "PASSWORD")
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "owner"
            , cascade = CascadeType.ALL
            , orphanRemoval = true)
    private Set<Ticket> userTickets = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "approver"
            , cascade = CascadeType.ALL
            , orphanRemoval = true)
    private Set<Ticket> approvedTickets = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "assignee"
            , cascade = CascadeType.ALL
            , orphanRemoval = true)
    private Set<Ticket> assigneeTickets = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user",
            fetch = FetchType.EAGER
            , cascade = CascadeType.ALL
            , orphanRemoval = true)
    private Set<History> histories = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user"
            , cascade = CascadeType.ALL
            , orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user"
            , cascade = CascadeType.ALL
            , orphanRemoval = true)
    private Set<Feedback> feedbacks = new HashSet<>();

    public void addFeedback(Feedback feedback) {
        feedbacks.add(feedback);
        feedback.setUser(this);
    }

    public void addUserTicket(Ticket ticket) {
        userTickets.add(ticket);
        ticket.setOwner(this);
    }

    public void addApprovedTicket(Ticket ticket) {
        approvedTickets.add(ticket);
        ticket.setApprover(this);
    }

    public void addAssigneeTicket(Ticket ticket) {
        assigneeTickets.add(ticket);
        ticket.setAssignee(this);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setUser(this);
    }

    public void addHistory(History history) {
        histories.add(history);
        history.setUser(this);
    }

    public void removeApprovedTicket(Ticket ticket) {
        approvedTickets.remove(ticket);
        ticket.setApprover(null);
    }

    public void removeAssigneeTicket(Ticket ticket) {
        assigneeTickets.remove(ticket);
        ticket.setAssignee(null);
    }

    public void removeFeedback(Feedback feedback) {
        feedbacks.remove(feedback);
        feedback.setUser(null);
    }

    public void removeUserTicket(Ticket ticket) {
        userTickets.remove(ticket);
        ticket.setOwner(null);
    }

    public void removeHistory(History history) {
        histories.remove(history);
        history.setUser(null);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setUser(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}
