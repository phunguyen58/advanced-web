package com.ptudw.web.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.ptudw.web.domain.Notification} entity. This class is used
 * in {@link com.ptudw.web.web.rest.NotificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter message;

    private StringFilter topic;

    private StringFilter receivers;

    private StringFilter sender;

    private StringFilter role;

    private StringFilter link;

    private LongFilter gradeReviewId;

    private Boolean distinct;

    public NotificationCriteria() {}

    public NotificationCriteria(NotificationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.message = other.message == null ? null : other.message.copy();
        this.topic = other.topic == null ? null : other.topic.copy();
        this.receivers = other.receivers == null ? null : other.receivers.copy();
        this.sender = other.sender == null ? null : other.sender.copy();
        this.role = other.role == null ? null : other.role.copy();
        this.link = other.link == null ? null : other.link.copy();
        this.gradeReviewId = other.gradeReviewId == null ? null : other.gradeReviewId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public NotificationCriteria copy() {
        return new NotificationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getMessage() {
        return message;
    }

    public StringFilter message() {
        if (message == null) {
            message = new StringFilter();
        }
        return message;
    }

    public void setMessage(StringFilter message) {
        this.message = message;
    }

    public StringFilter getTopic() {
        return topic;
    }

    public StringFilter topic() {
        if (topic == null) {
            topic = new StringFilter();
        }
        return topic;
    }

    public void setTopic(StringFilter topic) {
        this.topic = topic;
    }

    public StringFilter getReceivers() {
        return receivers;
    }

    public StringFilter receivers() {
        if (receivers == null) {
            receivers = new StringFilter();
        }
        return receivers;
    }

    public void setReceivers(StringFilter receivers) {
        this.receivers = receivers;
    }

    public StringFilter getSender() {
        return sender;
    }

    public StringFilter sender() {
        if (sender == null) {
            sender = new StringFilter();
        }
        return sender;
    }

    public void setSender(StringFilter sender) {
        this.sender = sender;
    }

    public StringFilter getRole() {
        return role;
    }

    public StringFilter role() {
        if (role == null) {
            role = new StringFilter();
        }
        return role;
    }

    public void setRole(StringFilter role) {
        this.role = role;
    }

    public StringFilter getLink() {
        return link;
    }

    public StringFilter link() {
        if (link == null) {
            link = new StringFilter();
        }
        return link;
    }

    public void setLink(StringFilter link) {
        this.link = link;
    }

    public LongFilter getGradeReviewId() {
        return gradeReviewId;
    }

    public LongFilter gradeReviewId() {
        if (gradeReviewId == null) {
            gradeReviewId = new LongFilter();
        }
        return gradeReviewId;
    }

    public void setGradeReviewId(LongFilter gradeReviewId) {
        this.gradeReviewId = gradeReviewId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NotificationCriteria that = (NotificationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(message, that.message) &&
            Objects.equals(topic, that.topic) &&
            Objects.equals(receivers, that.receivers) &&
            Objects.equals(sender, that.sender) &&
            Objects.equals(role, that.role) &&
            Objects.equals(link, that.link) &&
            Objects.equals(gradeReviewId, that.gradeReviewId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, topic, receivers, sender, role, link, gradeReviewId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (message != null ? "message=" + message + ", " : "") +
            (topic != null ? "topic=" + topic + ", " : "") +
            (receivers != null ? "receivers=" + receivers + ", " : "") +
            (sender != null ? "sender=" + sender + ", " : "") +
            (role != null ? "role=" + role + ", " : "") +
            (link != null ? "link=" + link + ", " : "") +
            (gradeReviewId != null ? "gradeReviewId=" + gradeReviewId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
