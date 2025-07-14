package com.thesis.backend.service.auth.impl;

import com.thesis.backend.common.exceptions.DataNotFoundException;
import com.thesis.backend.entity.user.User;
import com.thesis.backend.infrastructure.auth.dto.UserAuth;
import com.thesis.backend.infrastructure.user.repository.UserRepository;
import com.thesis.backend.service.auth.GetUserAuthDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GetUserAuthDetailsServiceImpl implements GetUserAuthDetailsService {
    private final UserRepository usersRepository;

    public GetUserAuthDetailsServiceImpl(UserRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User existingUser = usersRepository.findByEmail(username).orElseThrow(() -> new DataNotFoundException("User not found with email: " + username));

        UserAuth userAuth = new UserAuth();
        userAuth.setUser(existingUser);
        return userAuth;
    }
}
