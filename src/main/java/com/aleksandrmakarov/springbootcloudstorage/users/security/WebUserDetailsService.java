package com.aleksandrmakarov.springbootcloudstorage.users.security;

import com.aleksandrmakarov.springbootcloudstorage.users.entity.User;
import com.aleksandrmakarov.springbootcloudstorage.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found."));

        return new WebUserDetails(user);
    }
}
