package com.Rifa.v10.Repositories;

import com.Rifa.v10.Models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel,Long> {
    Optional<Object> findByEmail(String username);
}
