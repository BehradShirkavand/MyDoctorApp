package com.example.mydoctor.config;

import com.example.mydoctor.entity.User;
import com.example.mydoctor.enums.ERole;
import com.example.mydoctor.enums.Status;
import com.example.mydoctor.exception.ApiException;
import com.example.mydoctor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByRoles_NameAndUsernameAndStatus(ERole.ROLE_PATIENT, username, Status.ACTIVE)
                .orElseThrow(() -> new ApiException(("User not found with username: " + username), HttpStatus.NOT_FOUND));

        return new CustomUserDetails(user);
    }
}
