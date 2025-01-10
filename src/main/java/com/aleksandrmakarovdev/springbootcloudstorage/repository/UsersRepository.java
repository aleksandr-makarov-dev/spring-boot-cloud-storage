package com.aleksandrmakarovdev.springbootcloudstorage.repository;

import com.aleksandrmakarovdev.springbootcloudstorage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
