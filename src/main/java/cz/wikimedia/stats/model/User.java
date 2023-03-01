package cz.wikimedia.stats.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class User implements IdAble<Long> {

    /* ATTRIBUTES */

    @Id
    @GeneratedValue  //TODO: temporarily
    private Long id; // should be the MediaWiki user_id attribute, ideally

    //@Transient
    private String username;

    //@Transient
    private String email;

    @ManyToMany
    private Collection<UserTag> tags;

    @ManyToMany(mappedBy = "participants")
    private Collection<Event> events;

    @Transient
    private UserImpact impact;


    /* CONSTRUCTORS */

    protected User() {}

    public User(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public User(Long id, String username, String email, Collection<UserTag> tags, Collection<Event> events) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.tags = tags;
        this.events = events;
    }



    /* GETTERS */

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Collection<UserTag> getTags() {
        return tags;
    }

    public Collection<Event> getEvents() {
        return events;
    }



    /* SETTERS */

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public User setTags(Collection<UserTag> tags) {
        this.tags = tags;
        return this;
    }

    public User setEvents(Collection<Event> events) {
        this.events = events;
        return this;
    }

    public User addTag(UserTag tag) {
        tags.add(tag.addTagged(this));
        return this;
    }

    public User removeTag(UserTag tag) {
        tags.remove(tag.removeTagged(this));
        return this;
    }


}