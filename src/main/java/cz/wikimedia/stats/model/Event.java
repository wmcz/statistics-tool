package cz.wikimedia.stats.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
public class Event implements IdAble<Long> {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany
    private Set<EventTag> tags;

    @Column(unique=true, nullable = false)
    private String name;

    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToMany
    private Set<User> participants;

    @ManyToMany
    private Set<Project> projects;

    @ManyToMany
    private Set<Revision> revisions;

    private String hashtag;

    protected Event() {}

    public Event(String name) {
        this.name = name;
    }

    public Event(Long id, Set<EventTag> tags, String name, String hashtag, LocalDate startDate, LocalDate endDate, Set<User> participants, Set<Project> projects, Set<Revision> revisions) {
        this.id = id;
        this.tags = tags;
        this.name = name;
        this.hashtag = hashtag;
        this.startDate = startDate;
        this.endDate = endDate;
        this.participants = participants;
        this.projects = projects;
        this.revisions = revisions;
    }

    public Long getId() {
        return id;
    }

    public Set<EventTag> getTags() {
        return tags;
    }

    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Set<User> getParticipants() {
        return participants;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public String getHashtag() {
        return hashtag;
    }

    public Set<Revision> getRevisions() {
        return revisions;
    }

    public Event addTag(EventTag tag) {
        this.tags.add(tag);
        return this;
    }

    public Event removeTag(EventTag tag) {
        this.tags.remove(tag);
        return this;
    }

    public Event setName(String name) {
        this.name = name;
        return this;
    }

    public Event setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public Event setHashtag(String hashtag) {
        this.hashtag = hashtag;
        return this;
    }

    public Event addParticipant(User participant) {
        this.participants.add(participant);
        return this;
    }

    public Event removeParticipant(User participant) {
        this.participants.remove(participant);
        return this;
    }

    public Event addRevision(Revision rev) {
        this.revisions.add(rev);
        return this;
    }

    public Event removeRevision(Revision rev) {
        this.revisions.remove(rev);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event event)) return false;
        return Objects.equals(getId(), event.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
