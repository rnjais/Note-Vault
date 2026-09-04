package com.Note_Vault.dto;

import jakarta.validation.constraints.NotBlank;

public class NoteDTO {

    private Long id;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Content cannot be empty")
    private String content;

    private String category;

    public NoteDTO() {
    }

    public NoteDTO(Long id, String title, String content, String category) {
        this.id = id;
        this.title = title;
        this.content = content;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

//POST request with DTO:-

//Postman
//   ↓
//POST /api/notes
//   ↓
//Controller
//   ↓
//Receives JSON → stores it in NoteDTO object
//   ↓
//Service
//   ↓
//Takes NoteDTO → creates Note Entity
//   ↓
//Repository
//   ↓
//Saves Note Entity into MySQL


//Get request with DTO:-

//Database
//   ↓
//Repository
//   ↓
//Entity
//   ↓
//Service converts Entity → DTO
//   ↓
//Controller
//   ↓
//JSON
//   ↓
//Postman