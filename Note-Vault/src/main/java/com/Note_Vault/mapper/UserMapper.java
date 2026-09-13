package com.Note_Vault.mapper;

import com.Note_Vault.dto.UserDTO;
import com.Note_Vault.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}