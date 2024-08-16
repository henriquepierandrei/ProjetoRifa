package com.Rifa.v10.Controllers;

import com.Rifa.v10.Dtos.BuyTicketDto;
import com.Rifa.v10.Dtos.CreateCampaignDto;
import com.Rifa.v10.Models.CampaingModel;
import com.Rifa.v10.Models.UserModel;
import com.Rifa.v10.Services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

}
