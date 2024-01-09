package com.ptudw.web.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Notification.
 */
@Entity
@Table(name = "notification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "message")
    private String message;

    @Column(name = "topic")
    private String topic;

    @Column(name = "receivers")
    private String receivers;

    @Column(name = "sender")
    private String sender;

    @Column(name = "role")
    private String role;

    @Column(name = "link")
    private String link;

    @Column(name = "grade_review_id")
    private Long gradeReviewId;

    @Column(name = "is_read")
    private Boolean isRead;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Notification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return this.message;
    }

    public Notification message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTopic() {
        return this.topic;
    }

    public Notification topic(String topic) {
        this.setTopic(topic);
        return this;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getReceivers() {
        return this.receivers;
    }

    public Notification receivers(String receivers) {
        this.setReceivers(receivers);
        return this;
    }

    public void setReceivers(String receivers) {
        this.receivers = receivers;
    }

    public String getSender() {
        return this.sender;
    }

    public Notification sender(String sender) {
        this.setSender(sender);
        return this;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRole() {
        return this.role;
    }

    public Notification role(String role) {
        this.setRole(role);
        return this;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLink() {
        return this.link;
    }

    public Notification link(String link) {
        this.setLink(link);
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Long getGradeReviewId() {
        return this.gradeReviewId;
    }

    public Notification gradeReviewId(Long gradeReviewId) {
        this.setGradeReviewId(gradeReviewId);
        return this;
    }

    public void setGradeReviewId(Long gradeReviewId) {
        this.gradeReviewId = gradeReviewId;
    }

    public Boolean getIsRead() {
        return this.isRead;
    }

    public Notification isRead(Boolean isRead) {
        this.setIsRead(isRead);
        return this;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return id != null && id.equals(((Notification) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", topic='" + getTopic() + "'" +
            ", receivers='" + getReceivers() + "'" +
            ", sender='" + getSender() + "'" +
            ", role='" + getRole() + "'" +
            ", link='" + getLink() + "'" +
            ", gradeReviewId=" + getGradeReviewId() +
            ", isRead='" + getIsRead() + "'" +
            "}";
    }
}
