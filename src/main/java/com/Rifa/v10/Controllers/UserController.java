package com.Rifa.v10.Controllers;

import com.Rifa.v10.Models.CampaingModel;
import com.Rifa.v10.Models.TicketOfUserModel;
import com.Rifa.v10.Models.UserModel;
import com.Rifa.v10.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @GetMapping("/tickets")
    public ResponseEntity<List<TicketOfUserModel>> getAllTickets(@AuthenticationPrincipal UserModel userModel){
        return ResponseEntity.status(HttpStatus.FOUND).body(this.userService.getAllTickets(userModel.getId()));
    }

    @GetMapping("/ticket")
    public ResponseEntity<TicketOfUserModel> getAllTickets(@AuthenticationPrincipal UserModel userModel, @RequestParam(value = "idCampaign") long idCampaign){
        return ResponseEntity.status(HttpStatus.FOUND).body(this.userService.getTicketsId(userModel.getId(), idCampaign));
    }
}
