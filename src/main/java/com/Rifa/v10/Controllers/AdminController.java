package com.Rifa.v10.Controllers;

import com.Rifa.v10.Dtos.*;
import com.Rifa.v10.Models.CampaingModel;
import com.Rifa.v10.Models.UserModel;
import com.Rifa.v10.Services.AdminService;
import com.Rifa.v10.Services.EmailService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final EmailService emailService;


    // List all giveaway campaigns!
    @GetMapping("/campaings")
    public ResponseEntity<List<CampaingModel>> getAllCampaings(){
        return ResponseEntity.ok(this.adminService.findAllCampaings());
    }


    // Registers a new raffle campaign!
    @PostMapping("/register/campaign")
    public ResponseEntity createCampaign(@Validated @RequestBody CreateCampaignDto createCampaignDto){
        CampaingModel model = new CampaingModel();

        model.setNameAward(createCampaignDto.name());
        model.setDescription(createCampaignDto.description());
        model.setTicketQuantity(createCampaignDto.quantityTickets());
        model.setOnline(createCampaignDto.isOnline());
        model.setWinningNumbers(createCampaignDto.numbersWinning());
        model.setInicialQuantity(createCampaignDto.quantityTickets());
        model.setIdUsersBuyers(new ArrayList<>());
        model.setGeneratedNumbers(new ArrayList<>());


        this.adminService.saveCampaing(model);

        return ResponseEntity.ok("Campaing Created: ID: "+model.getId());

    }

    // Update Campaign
    @PutMapping("/update/campaign")
    public ResponseEntity<?> updateCampaign(@RequestParam(value = "idCampaign") UUID idCampaign, @RequestBody UpdateCampaignDto updateCampaignDto){
        Optional<CampaingModel> campaingModelOptional = this.adminService.getCampaign(idCampaign);
        if (campaingModelOptional.isPresent()){
            campaingModelOptional.get().setNameAward(campaingModelOptional.get().getNameAward());
            campaingModelOptional.get().setDescription(campaingModelOptional.get().getDescription());


            if (updateCampaignDto.name() != null){
                campaingModelOptional.get().setNameAward(updateCampaignDto.name());
            }

            if (updateCampaignDto.description() != null){
                campaingModelOptional.get().setDescription(updateCampaignDto.description());
            }

            this.adminService.saveCampaing(campaingModelOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body("Updated!");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not exists!");
    }


    // List all winners by campaign ID if there is a winner and they receive an email that they won!
    @GetMapping("/winners")
    public ResponseEntity<?> getUserForNumersWinner(@RequestParam(value = "idCampaign") UUID idCampaign) {
        List<UserModel> userModels = this.adminService.getUserByCampaign(idCampaign);

        Optional<CampaingModel> model = this.adminService.getCampaign(idCampaign);

        if (userModels.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No winners found for the campaign with ID: " + idCampaign);
        }


        List<ResponseWinnersDto> responseWinnersDtos = userModels.stream().map(user ->
                new ResponseWinnersDto(
                        user.getName(),
                        user.getLastName(),
                        user.getPhone(),
                        user.getCpf(),
                        this.adminService.numberWinnerUser(idCampaign, user.getId())
                )
        ).collect(Collectors.toList());


        userModels.forEach(user2 ->
                this.emailService.sendEmail(user2.getEmail(), model.get(), user2, idCampaign)
        );
        

        return ResponseEntity.status(HttpStatus.OK).body(responseWinnersDtos);
    }


    // Updates the campaign status, if it is online it goes offline and vice versa!
    @PutMapping("/update/status")
    public ResponseEntity<Object> updateStatusByIdCampaign(@RequestParam(value = "idCampaign") UUID idCampaign) {
        Object result = this.adminService.changeTheStatus(idCampaign);

        if (result instanceof String && result.equals("Campaign not found!")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        System.out.println(result);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    // Delete a campaign
    @DeleteMapping("/delete/campaign")
    public ResponseEntity<String> deleteCampaign(@RequestParam(value = "idCampaign") UUID idCampaign){
        Optional<CampaingModel> model = this.adminService.getCampaign(idCampaign);
        if (model.isPresent()){
            this.adminService.deleteCampaign(idCampaign);
            return ResponseEntity.status(HttpStatus.OK).body("Campaign ID: [ "+idCampaign+" ], was removed!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Campaign ID: [ "+idCampaign+" ], not exists!");

    }


    // Report about all campaigns
    @GetMapping("/reports")
    public ResponseEntity<?> reportAllCampaign(){
        if (this.adminService.reportAllCampaign()==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are no online campaigns!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.adminService.reportAllCampaign());
    }


    // Report about campaign by id
    @GetMapping("/report")
    public ResponseEntity<?> reportCampaignById(@RequestParam(value = "idCampaign") UUID idCampaign){
        if (this.adminService.reportCampaign(idCampaign)==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are no online campaigns or non-existent campaigns!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.adminService.reportAllCampaign());
    }





















}
