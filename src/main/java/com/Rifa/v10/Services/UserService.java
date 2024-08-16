package com.Rifa.v10.Services;

import com.Rifa.v10.Models.CampaingModel;
import com.Rifa.v10.Models.TicketOfUserModel;
import com.Rifa.v10.Models.UserModel;
import com.Rifa.v10.Repositories.CampaingRepository;
import com.Rifa.v10.Repositories.TicketOfUserRepository;
import com.Rifa.v10.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TicketOfUserRepository ticketOfUserRepository;
    private final CampaingRepository campaingRepository;


    public Optional<UserModel> getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public void save(UserModel newUser) {
        this.userRepository.save(newUser);
    }

    public boolean isValidCpf(String cpf){
        Optional<UserModel> userModel = this.userRepository.findByCpf(cpf);
        if (userModel.isEmpty()){return true;}
        return false;
    }

    public boolean isValidEmail(String email){
        Optional<UserModel> userModel = this.userRepository.findByEmail(email);
        if (userModel.isEmpty()){return true;}
        return false;
    }

    public boolean isValidPhone(String phone){
        Optional<UserModel> userModel = this.userRepository.findByPhone(phone);
        if (userModel.isEmpty()){return true;}
        return false;
    }


    public List<TicketOfUserModel> getAllTickets(long id){
        List<TicketOfUserModel> ticketOfUserModels = this.ticketOfUserRepository.findByIdUser(id);
        return ticketOfUserModels;

    }

    public List<Integer> getTicketsId(long id, UUID idCampaing) {
        Optional<TicketOfUserModel> ticketOfUserModel = this.ticketOfUserRepository.findByIdUserAndIdCampaign(id,idCampaing);
        return ticketOfUserModel.get().getNumbersOfUser();
    }

//    public List<Integer> generateTicket(UUID id, int quantity, long idUser) {
//        // Verifica se a campanha existe
//        Optional<CampaingModel> campaingModelOptional = this.campaingRepository.findById(id);
//
//        if (campaingModelOptional.isEmpty()) {
//            throw new NoSuchElementException("Campaign not found");
//        }
//
//        CampaingModel campaingModel = campaingModelOptional.get();
//        int ticketQuantity = campaingModel.getTicketQuantity();
//
//        // Verifica se há tickets suficientes disponíveis
//        if (ticketQuantity < quantity) {
//            throw new IllegalArgumentException("Not enough tickets available in the campaign");
//        }
//
//        // Inicializa a lista de números gerados e o conjunto para verificação rápida
//        List<Integer> numbersGenerated = new ArrayList<>();
//        Set<Integer> generatedNumbersSet = new HashSet<>(Optional.ofNullable(campaingModel.getGeneratedNumbers()).orElse(Collections.emptyList()));
//        Random random = new Random();
//
//        // Gera os números aleatórios
//        while (numbersGenerated.size() < quantity && ticketQuantity > 0) {
//            int number = random.nextInt(campaingModel.getInicialQuantity());
//
//            Set<Integer> generatedNumbersSet2 = new HashSet<>(Optional.ofNullable(campaingModel.getGeneratedNumbers()).orElse(Collections.emptyList()));
//
//            // Verifica se o número já foi gerado
//            if (!generatedNumbersSet2.contains(number) && !numbersGenerated.contains(number)) {
//                numbersGenerated.add(number);
//                generatedNumbersSet2.add(number); // Adiciona o número ao conjunto para evitar duplicatas
//
//                List<Integer> numbers = campaingModel.getGeneratedNumbers();
//                numbers.remove(Integer.valueOf(number));
//                this.campaingRepository.save(campaingModel);
//
//                // Decrementa a quantidade de tickets e atualiza a campanha a cada número gerado
//                ticketQuantity--;
//            }
//        }
//
//        // Atualiza a quantidade de tickets disponíveis na campanha
//        campaingModel.setTicketQuantity(ticketQuantity);
//
//        // Adiciona os novos números gerados à lista de números já gerados da campanha
//        List<Integer> num = campaingModel.getGeneratedNumbers();
//        num.addAll(numbersGenerated);
//        campaingModel.setGeneratedNumbers(num);
//
//        // Salva as alterações da campanha
//        this.campaingRepository.save(campaingModel);
//
//        // Atualiza os tickets do usuário
//        Optional<TicketOfUserModel> ticketOfUserModel = this.ticketOfUserRepository.findByIdUserAndIdCampaign(idUser, id);
//        if (ticketOfUserModel.isPresent()) {
//            TicketOfUserModel userTicket = ticketOfUserModel.get();
//            List<Integer> numberOfUser = new ArrayList<>(userTicket.getNumbersOfUser());
//            numberOfUser.addAll(numbersGenerated);
//            userTicket.setNumbersOfUser(numberOfUser);
//            this.ticketOfUserRepository.save(userTicket);
//        } else {
//            // Cria um novo ticket de usuário se ele não existir
//            TicketOfUserModel newTicket = new TicketOfUserModel();
//            newTicket.setIdUser(idUser);
//            newTicket.setIdCampaign(id);
//            newTicket.setNumbersOfUser(numbersGenerated);
//            this.ticketOfUserRepository.save(newTicket);
//        }
//
//        // Retorna os números gerados
//        return numbersGenerated;
//    }

    public List<Integer> generateTicket(UUID id, int quantity, long idUser) {
        // Verifica se a campanha existe
        Optional<CampaingModel> campaingModelOptional = this.campaingRepository.findById(id);
        if (campaingModelOptional.isEmpty()) {
            throw new NoSuchElementException("Campaign not found");

        }

        if (campaingModelOptional.get().getTicketQuantity()==1 || !campaingModelOptional.get().isOnline()){
            List<Integer> listEmpty = new ArrayList<>();
            return listEmpty;
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

            // Verifica se o número já foi gerado
            if (!generatedNumbersSet.contains(number)) {
                numbersGenerated.add(number);
                generatedNumbersSet.add(number); // Adiciona o número ao conjunto para evitar duplicatas

                // Decrementa a quantidade de tickets disponíveis
                ticketQuantity--;
            }
        }

        // Atualiza a quantidade de tickets disponíveis na campanha
        campaingModel.setTicketQuantity(ticketQuantity);

        List<Long> idBuyers = campaingModel.getIdUsersBuyers();
        idBuyers.add(idUser);
        campaingModel.setIdUsersBuyers(idBuyers);

        // Atualiza a lista de números gerados na campanha
        List<Integer> generatedNumbers = campaingModel.getGeneratedNumbers();
        if (generatedNumbers == null) {
            generatedNumbers = new ArrayList<>();
        }
        generatedNumbers.addAll(numbersGenerated);
        campaingModel.setGeneratedNumbers(generatedNumbers);

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


    public Optional<CampaingModel> findCampaignById(UUID id) {
        return this.campaingRepository.findById(id);
    }
}
