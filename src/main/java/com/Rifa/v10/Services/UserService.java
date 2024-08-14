package com.Rifa.v10.Services;

import com.Rifa.v10.Models.TicketOfUserModel;
import com.Rifa.v10.Models.UserModel;
import com.Rifa.v10.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public Optional<UserModel> getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public void save(UserModel newUser) {
        this.userRepository.save(newUser);
    }

    public boolean isValidCpf(String cpf){
        Optional<UserModel> userModel = this.userRepository.findByCpf(cpf);
        if (userModel.isEmpty()){return true;}
        return false;
    }

    public boolean isValidEmail(String email){
        Optional<UserModel> userModel = this.userRepository.findByEmail(email);
        if (userModel.isEmpty()){return true;}
        return false;
    }

    public boolean isValidPhone(String phone){
        Optional<UserModel> userModel = this.userRepository.findByPhone(phone);
        if (userModel.isEmpty()){return true;}
        return false;
    }


    public List<TicketOfUserModel> getAllTickets()
}
