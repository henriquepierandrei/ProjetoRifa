package com.Rifa.v10.Controllers;

import com.Rifa.v10.Dtos.BuyTicketDto;
import com.Rifa.v10.Models.CampaingModel;
import com.Rifa.v10.Models.TicketOfUserModel;
import com.Rifa.v10.Models.UserModel;
import com.Rifa.v10.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public ResponseEntity<List<Integer>> getAllTickets(@AuthenticationPrincipal UserModel userModel, @RequestParam(value = "idCampaign") UUID idCampaign){
        return ResponseEntity.status(HttpStatus.FOUND).body(this.userService.getTicketsId(userModel.getId(), idCampaign));
    }

    @PostMapping("/buy")
    public ResponseEntity buyTickets(@RequestBody BuyTicketDto buyTicketDto, @AuthenticationPrincipal UserModel userModel){
        Optional<CampaingModel> campaingModelOptional = this.userService.findCampaignById(buyTicketDto.id());
        if (campaingModelOptional.isEmpty()){return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Campaign Not Found!");}
        List<Integer> integers = this.userService.generateTicket(buyTicketDto.id(), buyTicketDto.quantity(), userModel.getId());
        if (integers.isEmpty()){return ResponseEntity.badRequest().build();}
        return ResponseEntity.ok(integers);
    }
}
