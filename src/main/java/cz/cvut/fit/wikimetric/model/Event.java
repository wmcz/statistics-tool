package cz.cvut.fit.wikimetric.model;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
public class Event { //TODO: consider a less ambiguous name (Program? Campaign?)

    /* ATTRIBUTES */

    @Id
    @GeneratedValue
    private Long ID;

    @ManyToOne
    @Nullable
    private EventType eventType;

    @Column(unique=true, nullable = false)
    private String name;

    private Date startDate;
    private Date endDate;

    @ManyToMany
    private Collection<User> participants;



    /* CONSTRUCTORS */

    protected Event() {}

    public Event(String name) {
        this.name = name;
    }

    public Event(@Nullable EventType eventType, String name, Date startDate, Date endDate, Collection<User> participants) {
        this.eventType = eventType;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.participants = participants;
    }

    public Long getID() {
        return ID;
    }



    /* GETTERS */

    @Nullable
    public EventType getEventType() {
        return eventType;
    }

    public String getName() {
        return name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Collection<User> getParticipants() {
        return participants;
    }



    /* SETTERS */

    public void setEventType(@Nullable EventType eventType) {
        this.eventType = eventType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setParticipants(Collection<User> eventParticipants) {
        this.participants = eventParticipants;
    }
}
