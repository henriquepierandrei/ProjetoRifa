package com.Rifa.v10.Repositories;

import com.Rifa.v10.Models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel,Long> {
    Optional<UserModel> findByEmail(String username);

    Optional<UserModel> findByCpf(String cpf);

    Optional<UserModel> findByPhone(String phone);
}
