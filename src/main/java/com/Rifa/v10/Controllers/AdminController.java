package com.Rifa.v10.Controllers;

import com.Rifa.v10.Dtos.BuyTicketDto;
import com.Rifa.v10.Dtos.CreateCampaignDto;
import com.Rifa.v10.Dtos.ResponseRegisterDto;
import com.Rifa.v10.Dtos.ResponseWinnersDto;
import com.Rifa.v10.Models.CampaingModel;
import com.Rifa.v10.Models.UserModel;
import com.Rifa.v10.Services.AdminService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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


        model.setIdUsersBuyers(new ArrayList<>());


        model.setGeneratedNumbers(list);
        this.adminService.saveCampaing(model);

        return ResponseEntity.ok("Campaing Created: ID: "+model.getId());

    }

    @GetMapping("/winners")
    public ResponseEntity<?> getUserByNumersWinner(@RequestParam(value = "idCampaign") UUID idCampaign) {
        List<UserModel> userModels = this.adminService.getUserByCampaign(idCampaign);

        if (userModels.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No winners found for the campaign with ID: " + idCampaign);
        }

        // Converte cada UserModel para ResponseWinnersDto
        List<ResponseWinnersDto> responseWinnersDtos = userModels.stream().map(user ->
                new ResponseWinnersDto(
                        user.getName(),
                        user.getLastName(),
                        user.getPhone(),
                        user.getCpf(),
                        this.adminService.numberWinnerUser(idCampaign, user.getId())  // Passando o ID do usuário
                )
        ).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(responseWinnersDtos);
    }




}
