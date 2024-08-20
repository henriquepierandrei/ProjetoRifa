package com.Rifa.v10.Services;

import com.Rifa.v10.Models.CampaingModel;
import com.Rifa.v10.Models.TicketOfUserModel;
import com.Rifa.v10.Models.UserModel;
import com.Rifa.v10.Repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final AdminService adminService;
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;


    public void sendEmail(String to, CampaingModel model, UserModel userModel, UUID idCampaign) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(userModel.getEmail());
            helper.setSubject("Parabéns " + userModel.getName() + " " + userModel.getLastName() + ", você é ganhador da " + model.getNameAward());

            
            String htmlContent = "<html lang='pt-BR'>"
                    + "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0;'>"
                    + "    <div style='background-color: #161515; width: 500px; height: auto; border-radius: 15px; margin: 50px auto; box-shadow: 10px 10px 30px rgba(0, 0, 0, 0.2); padding: 20px; text-align: center;'>"
                    + "        <h1 style='color: #FFD700; font-size: 2.5em; margin-bottom: 0;'>🎉 Parabéns! 🎉</h1>"
                    + "        <p style='color: #ffffff; font-size: 1.2em; margin-top: 0;'>Parabéns " + userModel.getName() + ", você é o grande ganhador da nossa rifa!</p>"
                    + "        <div style='background-color: #201a1a; border-radius: 10px; padding: 30px; margin-top: 20px; text-align: start;'>"
                    + "            <h2 style='color: #dbd1d1; font-weight: 300; background-color: rgb(82, 74, 74); border-radius: 3px; padding: 5px 0 5px 5px; width: max-content;'>🏆 Prêmio: <strong style='background-color: #fa2121; padding: 5px; border-radius: 5px;'>" + model.getNameAward() + "</strong></h2>"
                    + "            <h2 style='color: #dbd1d1; font-weight: 300; background-color: rgb(82, 74, 74); border-radius: 3px; padding: 5px 0 5px 5px; width: max-content;'>🎟️ Número Vencedor: <strong style='background-color: #fa2121; padding: 5px; border-radius: 5px;'>" + this.adminService.numberWinnerUser(idCampaign, userModel.getId()) + "</strong></h2>"
                    + "        </div>"
                    + "        <br><br><br><br>"
                    + "        <p style='color: #bbb; font-size: 1em;'>Nossa equipe entrará em contato com você para organizar a entrega do prêmio. "
                    + "        <strong style='background-color: #FFD700; color: #333; padding: 2px; margin: 5px;'>Fique atento!</strong></p>"
                    + "        <p style='color: #bbb; font-size: 0.9em; margin-top: 40px;'>Obrigado por participar e boa sorte nas próximas rifas!</p>"
                    + "    </div>"
                    + "</body>"
                    + "</html>";




            helper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }



}
