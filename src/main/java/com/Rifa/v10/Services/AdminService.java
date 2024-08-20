package com.Rifa.v10.Services;

import com.Rifa.v10.Dtos.ReportCampaignDto;
import com.Rifa.v10.Models.CampaingModel;
import com.Rifa.v10.Models.TicketOfUserModel;
import com.Rifa.v10.Models.UserModel;
import com.Rifa.v10.Repositories.CampaingRepository;
import com.Rifa.v10.Repositories.TicketOfUserRepository;
import com.Rifa.v10.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminService {
    @Autowired private CampaingRepository campaingRepository;
    @Autowired private TicketOfUserRepository ticketOfUserRepository;
    @Autowired private UserRepository userRepository;



    public List<CampaingModel> findAllCampaings() {
        return this.campaingRepository.findAll();
    }


    public void saveCampaing(CampaingModel model) {
        this.campaingRepository.save(model);
    }

    public List<Integer> generateNumbers(int quantity){
        List<Integer> integers = new ArrayList<>();
        for(int i = 0; i < quantity; i++){
            integers.add(i);
        }
        Collections.shuffle(integers);

        return integers;
    }

    public List<UserModel> getUserByCampaign(UUID id){
        Optional<CampaingModel> campaingModelOptional = this.campaingRepository.findById(id);

        System.out.println(campaingModelOptional.get());
        if (campaingModelOptional.isEmpty()){return Collections.emptyList();}

        List<Long> idUsers = campaingModelOptional.get().getIdUsersBuyers();
        List<UserModel> userWinner = new ArrayList<>();



        System.out.println(idUsers);

        for(int i = 0; i < idUsers.size(); i++){
            Optional<TicketOfUserModel> ticketOfUserModel = this.ticketOfUserRepository.findByIdUserAndIdCampaign(idUsers.get(i),id);
            if (ticketOfUserModel.isEmpty()){
                return Collections.emptyList();
            }

            for(Integer n : ticketOfUserModel.get().getNumbersOfUser()){
                System.out.println(">> "+n);
                if (campaingModelOptional.get().getWinningNumbers().contains(n)){
                    Optional<UserModel> userModel = this.userRepository.findById(idUsers.get(i));
                    userWinner.add(userModel.get());


                }

            }
        }


        return userWinner;

    }

    public List<Object> numberWinnerUser(UUID idCampaign, long idUser) {
        Optional<CampaingModel> model = this.campaingRepository.findById(idCampaign);

        List<Object> objects = new ArrayList<>();

        if (model.isPresent()) {
            List<Integer> numbersWinners = model.get().getWinningNumbers();

            Optional<TicketOfUserModel> userModel = this.ticketOfUserRepository.findByIdUserAndIdCampaign(idUser, idCampaign);

            if (userModel.isPresent()) {
                for (Integer userNumber : userModel.get().getNumbersOfUser()) {
                    if (numbersWinners.contains(userNumber)) {
                        objects.add(userNumber);
                    }
                }
            }
        }

        return objects;
    }

    @Transactional
    public Object changeTheStatus(UUID idCampaign) {
        Optional<CampaingModel> model = this.campaingRepository.findById(idCampaign);

        if (model.isPresent()) {
            CampaingModel campaing = model.get();

            boolean isOnline = !campaing.isOnline();



            campaing.setOnline(isOnline);

            System.out.println(">>>"+isOnline);
            this.campaingRepository.save(campaing);



            return campaing;
        }

        return "Campaign not found!";
    }

    public Optional<CampaingModel> getCampaign(UUID id){
        return this.campaingRepository.findById(id);
    }

    @Transactional
    public void deleteCampaign(UUID idCampaign) {
        this.ticketOfUserRepository.deleteByIdCampaign(idCampaign);
        this.campaingRepository.deleteById(idCampaign);
    }

    public List<Object> reportAllCampaign(){
        List<CampaingModel> campaingModels = this.campaingRepository.findByIsOnline(true);

        if (!campaingModels.isEmpty()){
            List<ReportCampaignDto> reportCampaignDtos = new ArrayList<>();

            for (CampaingModel model : campaingModels){
                ReportCampaignDto reportCampaignDto = new ReportCampaignDto(model,model.getTicketQuantity(), model.getIdUsersBuyers().size());
                reportCampaignDtos.add(reportCampaignDto);
            }

            return Collections.singletonList(reportCampaignDtos);
        }
        return null;


    }

    public Object reportCampaign(UUID uuid){
        Optional<CampaingModel> campaingModel = this.campaingRepository.findById(uuid);

        if (!campaingModel.isEmpty()){
            ReportCampaignDto reportCampaignDto = new ReportCampaignDto(campaingModel.get(),campaingModel.get().getTicketQuantity(), campaingModel.get().getIdUsersBuyers().size());

            return reportCampaignDto;
        }

        return null;
    }
}

//    public List<Integer> generateTicket(UUID id, int quantity, long idUser){
//        Optional<CampaingModel> campaingModelOptional = this.campaingRepository.findById(id);
//
//        // check campaign ticket quantity
//        int n = 0;
//
//        if (campaingModelOptional.get().getTicketQuantity()==1000){
//            n = 1000;
//        }
//        if (campaingModelOptional.get().getTicketQuantity()==10000){
//            n = 10000;
//        }
//        if (campaingModelOptional.get().getTicketQuantity()==10000){
//            n = 10000;
//        }
//        if (campaingModelOptional.get().getTicketQuantity()==100000){
//            n = 100000;
//        }
//        if (campaingModelOptional.get().getTicketQuantity()==1000000){
//            n = 1000000;
//        }
//
//        // create a list for the numbers
//
//        List<Integer> numbers = new ArrayList<>();
//
//        // created numbers and add in the list
//
//        List<Integer> numbersGenerated = campaingModelOptional.get().getGeneratedNumbers();
//
//        for(int i = 0; i < quantity; i++){
//            Random random = new Random();
//            int number = random.nextInt(n);
//            for(Integer num : numbersGenerated){
//                if (n!=num){numbers.add(number);}
//            }
//        }
//
//
//
//        if (campaingModelOptional.isPresent()){
//            campaingModelOptional.get().setTicketQuantity(campaingModelOptional.get().getTicketQuantity()-quantity);
//            campaingModelOptional.get().setGeneratedNumbers(numbers);
//
//            Optional<TicketOfUserModel> ticketOfUserModel = this.ticketOfUserRepository.findByIdUserAndIdCampaign(idUser,id);
//            if (ticketOfUserModel.isPresent()){
//                ticketOfUserModel.get().setIdUser(idUser);
//                List<Integer> numberOfUser = ticketOfUserModel.get().getNumbersOfUser();
//                for(Integer num : numbers){
//                    numbers.add(num);
//                }
//                ticketOfUserModel.get().setNumbersOfUser(numberOfUser);
//                ticketOfUserModel.get().setIdCampaign(id);
//            }
//        }
//        return numbers;
//
//    }

