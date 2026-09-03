package com.Note_Vault.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name= "notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Content cannot be empty")
    private String content;
    private LocalDateTime createdAt;
    private String category;

    public Note(Long id, String title, String content, LocalDateTime createdAt,String category) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.category = category;
    }
    public Note(){

    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
