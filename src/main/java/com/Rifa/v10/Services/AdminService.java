package com.Rifa.v10.Services;

import com.Rifa.v10.Models.CampaingModel;
import com.Rifa.v10.Models.TicketOfUserModel;
import com.Rifa.v10.Repositories.CampaingRepository;
import com.Rifa.v10.Repositories.TicketOfUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminService {
    @Autowired private CampaingRepository campaingRepository;
    @Autowired private TicketOfUserRepository ticketOfUserRepository;



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
}
