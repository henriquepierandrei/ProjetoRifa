package com.Rifa.v10.Controllers;

import com.Rifa.v10.Dtos.BuyTicketDto;
import com.Rifa.v10.Dtos.CreateCampaignDto;
import com.Rifa.v10.Models.CampaingModel;
import com.Rifa.v10.Models.UserModel;
import com.Rifa.v10.Services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;


    @GetMapping("/campaings")
    public ResponseEntity<List<CampaingModel>> getAllCampaings(){
        return ResponseEntity.ok(this.adminService.findAllCampaings());
    }

    @PostMapping("/register/campaign")
    public ResponseEntity registerCampaign(@Validated @RequestBody CreateCampaignDto createCampaignDto){
        CampaingModel model = new CampaingModel();

        model.setNameAward(createCampaignDto.name());
        model.setDescription(createCampaignDto.description());
        model.setTicketQuantity(createCampaignDto.quantityTickets()+1);
        model.setOnline(createCampaignDto.isOnline());
        model.setWinningNumbers(createCampaignDto.numbersWinning());
        model.setInicialQuantity(createCampaignDto.quantityTickets());

        List<Integer> list = new ArrayList<>();
        list.add(null);

        model.setGeneratedNumbers(list);
        this.adminService.saveCampaing(model);

        return ResponseEntity.ok("Campaing Created: ID: "+model.getId());

    }

    @GetMapping("/campaign")
    public ResponseEntity getUserByNumersWinner(@RequestParam(value = "idCampaign") UUID idCampaign){
        List<UserModel> userModels = this.adminService.getUserByCampaign(idCampaign);
        if (userModels.isEmpty()){return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found!");}
        return ResponseEntity.status(HttpStatus.FOUND).body(userModels);
    }

}
