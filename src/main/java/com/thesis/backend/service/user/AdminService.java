package com.thesis.backend.service.user;

import com.thesis.backend.common.exceptions.EmailAlreadyExistsException;
import com.thesis.backend.infrastructure.user.dto.UserDTO;

public interface AdminService {
    UserDTO registerUser(UserDTO userDTO) throws EmailAlreadyExistsException;
}
