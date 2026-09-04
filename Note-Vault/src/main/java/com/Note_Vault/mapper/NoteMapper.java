package com.Note_Vault.mapper;

import com.Note_Vault.dto.NoteDTO;
import com.Note_Vault.entity.Note;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {

    // Entity → DTO
    public static NoteDTO toDTO(Note note) {

        NoteDTO dto = new NoteDTO();

        dto.setId(note.getId());
        dto.setTitle(note.getTitle());
        dto.setContent(note.getContent());
        dto.setCategory(note.getCategory());

        return dto;
    }

    // DTO → Entity
    public static Note toEntity(NoteDTO dto) {

        Note note = new Note();

        note.setTitle(dto.getTitle());
        note.setContent(dto.getContent());
        note.setCategory(dto.getCategory());

        return note;
    }
}