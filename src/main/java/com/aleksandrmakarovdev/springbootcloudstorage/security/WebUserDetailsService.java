package com.aleksandrmakarovdev.springbootcloudstorage.security;

import com.aleksandrmakarovdev.springbootcloudstorage.entity.User;
import com.aleksandrmakarovdev.springbootcloudstorage.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundUser = usersRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return new WebUserDetails(foundUser);
    }
}
