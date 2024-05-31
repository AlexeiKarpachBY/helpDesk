package com.training.akarpach.helpDesk.dto;

import com.training.akarpach.helpDesk.enums.Action;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class TicketDto {

    private String id;

    @NotBlank(message = "The <Name> field must be filled.")
    @Size(max = 100, message = "<Name> field must be no more than 100 characters.")
    @Pattern(regexp = "[[a-z0-9]~.(),:;<>@\\[\\]!#$%&'*+\\-/=?^_`{|}^]+", message = "The <Name> uses a forbidden character.")
    private String name;

    @NotBlank(message = "The <Description> field must be filled.")
    @Size(max = 500, message = "<Description> field must be no more than 500 characters.")
    @Pattern(regexp = "[[a-zA-Z0-9]~.(),\\s:;<>@\\[\\]!#$%&'*+\\-/=?^_`{|}]+", message = "The <Description> uses a forbidden character.")
    private String description;

    @NotBlank(message = "The <Desired Resolution Date> field must be filled.")
    private String desiredResolutionDate;

    private String createdOn;

    private String state;

    private String urgency;

    private String category;

    private String owner;

    private String assignee;

    private String approver;

    private List<Action> action;

    private List<AttachmentDto> attachment;

    private FeedbackDto feedbackDto;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketDto ticketDto = (TicketDto) o;
        return Objects.equals(id, ticketDto.id) && Objects.equals(name, ticketDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "TicketDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
