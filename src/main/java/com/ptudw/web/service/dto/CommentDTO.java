package com.ptudw.web.service.dto;

import java.io.Serializable;
import java.util.Objects;

public class CommentDTO implements Serializable {

    private String author;

    private String content;

    private String login;

    public CommentDTO() {}

    public CommentDTO(String author, String content, String login) {
        this.author = author;
        this.content = content;
        this.login = login;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public CommentDTO author(String author) {
        setAuthor(author);
        return this;
    }

    public CommentDTO content(String content) {
        setContent(content);
        return this;
    }

    public CommentDTO login(String login) {
        setLogin(login);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof CommentDTO)) {
            return false;
        }
        CommentDTO commentDTO = (CommentDTO) o;
        return (
            Objects.equals(author, commentDTO.author) &&
            Objects.equals(content, commentDTO.content) &&
            Objects.equals(login, commentDTO.login)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, content, login);
    }

    @Override
    public String toString() {
        return "{" + " author='" + getAuthor() + "'" + ", content='" + getContent() + "'" + ", login='" + getLogin() + "'" + "}";
    }
}
