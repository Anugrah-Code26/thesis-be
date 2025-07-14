package com.thesis.backend.service.user;

import com.thesis.backend.entity.user.User;
import com.thesis.backend.infrastructure.user.dto.EmailRequestDTO;
import com.thesis.backend.infrastructure.user.dto.UserDTO;
import com.thesis.backend.infrastructure.user.dto.UserProfileDTO;
import jakarta.mail.MessagingException;

public interface UserService {
    User requestRegistration(EmailRequestDTO req) throws MessagingException;
    User completeRegistration(String token, UserDTO req);

//    User registerUser(UserDTO userDTO) throws EmailAlreadyExistsException;
    User getUserById(Long id);
    User getUserByEmail(String email);
    UserProfileDTO updateUserProfile(Long userId, UserProfileDTO userProfileDTO);
}
