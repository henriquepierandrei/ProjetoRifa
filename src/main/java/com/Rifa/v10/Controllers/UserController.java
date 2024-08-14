package com.Rifa.v10.Controllers;

import com.Rifa.v10.Models.TicketsModel;
import com.Rifa.v10.Models.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    @GetMapping("/tickets")
    public ResponseEntity getAllTickets(@AuthenticationPrincipal UserModel userModel){
        List<TicketsModel> ticketsModels = new ArrayList<>();

    }
}
