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






    public void sendEmailBuy(String to, CampaingModel model, UserModel userModel, int quantity) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(userModel.getEmail());
            helper.setSubject("Parabéns " + userModel.getName() + " " + userModel.getLastName() + ", você adquiriu " + quantity + " bilhetes!");


            String htmlContent = "<html lang='pt-BR'>"
                    + "<body style='background-image: linear-gradient(to right, rgb(245, 245, 209), rgb(243, 233, 233)); display: flex; align-items: center; justify-content: center;'>"
                    + "    <div style='overflow: hidden; position: relative; text-align: left; border-radius: 0.5rem; max-width: 590px; box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04); background-color: #fff;'>"
                    + "        <div style='padding: 1.25rem 1rem 1rem 1rem;'>"
                    + "            <div style='background: #47c9a2; border-bottom: none; position: relative; text-align: center; margin: -20px -20px 0; border-radius: 5px 5px 0 0; padding: 35px;'>"
                    + "                <div style='display: flex; margin-left: auto; margin-right: auto; background-color: #e2feee; flex-shrink: 0; justify-content: center; align-items: center; width: 3rem; height: 3rem; border-radius: 9999px; animation: animate .6s linear alternate-reverse infinite; transition: .6s ease;'>"
                    + "                    <h2 style='margin: auto; font-size: 1.5rem;'>✅</h2>"
                    + "                </div>"
                    + "            </div>"
                    + "            <div style='margin-top: 0.75rem; text-align: center;'>"
                    + "                <span style='color: #066e29; font-size: 1rem; font-weight: 600; line-height: 1.5rem;'>Você adquiriu <strong>" + quantity + "</strong> bilhetes na Ação: <strong>" + model.getNameAward() + "</strong></span>"
                    + "                <p style='margin-top: 0.5rem; color: #595b5f; font-size: 0.875rem; line-height: 1.25rem;'>Obrigado pela confiança, não perca a chance de mudar de vida, atualmente ainda restam <strong>" + model.getTicketQuantity() + "</strong> bilhetes para finalizar a ação!</p>"
                    + "            </div>"
                    + "        </div>"
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
