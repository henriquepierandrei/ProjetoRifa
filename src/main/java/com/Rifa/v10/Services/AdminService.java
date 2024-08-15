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


    public List<Integer> generateTicket(UUID id, int quantity, long idUser) {
        // Verifica se a campanha existe
        Optional<CampaingModel> campaingModelOptional = this.campaingRepository.findById(id);

        if (campaingModelOptional.isEmpty()) {
            throw new NoSuchElementException("Campaign not found");
        }

        CampaingModel campaingModel = campaingModelOptional.get();
        int ticketQuantity = campaingModel.getTicketQuantity();

        // Verifica se há tickets suficientes disponíveis
        if (ticketQuantity < quantity) {
            throw new IllegalArgumentException("Not enough tickets available in the campaign");
        }

        // Inicializa a lista de números gerados e o conjunto para verificação rápida
        List<Integer> numbersGenerated = new ArrayList<>();
        Set<Integer> generatedNumbersSet = new HashSet<>(Optional.ofNullable(campaingModel.getGeneratedNumbers()).orElse(Collections.emptyList()));
        Random random = new Random();

        // Gera os números aleatórios
        while (numbersGenerated.size() < quantity && ticketQuantity > 0) {
            int number = random.nextInt(campaingModel.getTicketQuantity());

            Set<Integer> generatedNumbersSet2 = new HashSet<>(Optional.ofNullable(campaingModel.getGeneratedNumbers()).orElse(Collections.emptyList()));


            System.out.println(number);
            // Verifica se o número já foi gerado
            if (!generatedNumbersSet2.contains(number) && !numbersGenerated.contains(number)) {
                numbersGenerated.add(number);
                generatedNumbersSet2.add(number); // Adiciona o número ao conjunto para evitar duplicatas

                List<Integer> numbers = campaingModel.getGeneratedNumbers();
                numbers.remove(Integer.valueOf(number));
                this.campaingRepository.save(campaingModel);

                // Decrementa a quantidade de tickets e atualiza a campanha a cada número gerado
                ticketQuantity--;
            }
        }

        // Atualiza a quantidade de tickets disponíveis na campanha
        campaingModel.setTicketQuantity(ticketQuantity);

        // Adiciona os novos números gerados à lista de números já gerados da campanha
        List<Integer> num = campaingModel.getGeneratedNumbers();
        num.addAll(numbersGenerated);
        campaingModel.setGeneratedNumbers(num);

        // Salva as alterações da campanha
        this.campaingRepository.save(campaingModel);

        // Atualiza os tickets do usuário
        Optional<TicketOfUserModel> ticketOfUserModel = this.ticketOfUserRepository.findByIdUserAndIdCampaign(idUser, id);
        if (ticketOfUserModel.isPresent()) {
            TicketOfUserModel userTicket = ticketOfUserModel.get();
            List<Integer> numberOfUser = new ArrayList<>(userTicket.getNumbersOfUser());
            numberOfUser.addAll(numbersGenerated);
            userTicket.setNumbersOfUser(numberOfUser);
            this.ticketOfUserRepository.save(userTicket);
        } else {
            // Cria um novo ticket de usuário se ele não existir
            TicketOfUserModel newTicket = new TicketOfUserModel();
            newTicket.setIdUser(idUser);
            newTicket.setIdCampaign(id);
            newTicket.setNumbersOfUser(numbersGenerated);
            this.ticketOfUserRepository.save(newTicket);
        }

        // Retorna os números gerados
        return numbersGenerated;
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
