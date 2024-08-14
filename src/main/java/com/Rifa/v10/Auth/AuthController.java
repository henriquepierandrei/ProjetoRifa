package com.Rifa.v10.Auth;


import com.Rifa.v10.Dtos.LoginDto;
import com.Rifa.v10.Dtos.RegisterDto;
import com.Rifa.v10.Dtos.ResponseDto;
import com.Rifa.v10.Dtos.ResponseRegisterDto;
import com.Rifa.v10.Infra.Security.TokenService;
import com.Rifa.v10.Models.UserModel;
import com.Rifa.v10.Services.CpfValidatorService;
import com.Rifa.v10.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final CpfValidatorService cpfValidatorService;
    private final AuthService authService;



    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto body) {
        Optional<UserModel> user = userService.getUserByEmail(body.email());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("Email or Password Incorrect!");
        }

        if (passwordEncoder.matches(body.password(), user.get().getPassword())) {
            String token = this.tokenService.generateToken(user.get());
            return ResponseEntity.ok(new ResponseDto(token, user.get().getEmail(), user.get().getName(), user.get().getLastName()));
        }
        return ResponseEntity.badRequest().body("Email or Password Incorrect!");
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Validated RegisterDto body) {
        Optional<UserModel> user = userService.getUserByEmail(body.email());

        if (user.isEmpty()) {
            UserModel newUser = new UserModel();
            newUser.setPassword(passwordEncoder.encode(body.password()));
            newUser.setEmail(body.email());
            newUser.setName(body.name());
            newUser.setLastName(body.lastName());
            if (!this.cpfValidatorService.isValid(body.cpf())){return ResponseEntity.badRequest().body("CPF Invalid!");}
            newUser.setCpf(body.cpf());



            String token = this.tokenService.generateToken(newUser);
            if (!this.userService.isValidCpf(body.cpf()) || !this.userService.isValidEmail(body.email()) || !this.userService.isValidPhone(body.phone())){
                return ResponseEntity.badRequest().body("Existing data!");
            }
            this.userService.save(newUser);
            return ResponseEntity.ok(new ResponseRegisterDto(newUser.getName(), newUser.getLastName(), newUser.getCpf(), newUser.getEmail(), token));
        }

        return ResponseEntity.badRequest().build();
    }
}