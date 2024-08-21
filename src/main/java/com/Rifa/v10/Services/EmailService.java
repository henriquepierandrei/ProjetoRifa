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






    public void sendEmailBuy(String to, CampaingModel model, UserModel userModel, UUID idCampaign, int quantity) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(userModel.getEmail());
            helper.setSubject("Parabéns " + userModel.getName() + " " + userModel.getLastName() + ", você adquiriu " + model.getNameAward() + " bilhetes!");


            String htmlContent = "<html lang='pt-BR'>"
                    + "<style>"
                    + ".card {"
                    + "  overflow: hidden;"
                    + "  position: relative;"
                    + "  text-align: left;"
                    + "  border-radius: 0.5rem;"
                    + "  max-width: 590px;"
                    + "  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);"
                    + "  background-color: #fff;"
                    + "}"
                    + ".div_image_v {"
                    + "  background: #47c9a2;"
                    + "  border-bottom: none;"
                    + "  position: relative;"
                    + "  text-align: center;"
                    + "  margin: -20px -20px 0;"
                    + "  border-radius: 5px 5px 0 0;"
                    + "  padding: 35px;"
                    + "}"
                    + ".dismiss {"
                    + "  position: absolute;"
                    + "  right: 10px;"
                    + "  top: 10px;"
                    + "  display: flex;"
                    + "  align-items: center;"
                    + "  justify-content: center;"
                    + "  padding: 0.5rem 1rem;"
                    + "  background-color: #fff;"
                    + "  color: black;"
                    + "  border: 2px solid #D1D5DB;"
                    + "  font-size: 1rem;"
                    + "  font-weight: 300;"
                    + "  width: 30px;"
                    + "  height: 30px;"
                    + "  border-radius: 7px;"
                    + "  transition: .3s ease;"
                    + "}"
                    + ".dismiss:hover {"
                    + "  background-color: #ee0d0d;"
                    + "  border: 2px solid #ee0d0d;"
                    + "  color: #fff;"
                    + "}"
                    + ".header {"
                    + "  padding: 1.25rem 1rem 1rem 1rem;"
                    + "}"
                    + ".image {"
                    + "  display: flex;"
                    + "  margin-left: auto;"
                    + "  margin-right: auto;"
                    + "  background-color: #e2feee;"
                    + "  flex-shrink: 0;"
                    + "  justify-content: center;"
                    + "  align-items: center;"
                    + "  width: 3rem;"
                    + "  height: 3rem;"
                    + "  border-radius: 9999px;"
                    + "  animation: animate .6s linear alternate-reverse infinite;"
                    + "  transition: .6s ease;"
                    + "}"
                    + ".image svg {"
                    + "  color: #0afa2a;"
                    + "  width: 2rem;"
                    + "  height: 2rem;"
                    + "}"
                    + ".content {"
                    + "  margin-top: 0.75rem;"
                    + "  text-align: center;"
                    + "}"
                    + ".title {"
                    + "  color: #066e29;"
                    + "  font-size: 1rem;"
                    + "  font-weight: 600;"
                    + "  line-height: 1.5rem;"
                    + "}"
                    + ".message {"
                    + "  margin-top: 0.5rem;"
                    + "  color: #595b5f;"
                    + "  font-size: 0.875rem;"
                    + "  line-height: 1.25rem;"
                    + "}"
                    + "@keyframes animate {"
                    + "  from {"
                    + "    transform: scale(1);"
                    + "  }"
                    + "  to {"
                    + "    transform: scale(1.09);"
                    + "  }"
                    + "}"
                    + "</style>"
                    + "<body style='background-image: linear-gradient(to right, rgb(245, 245, 209), rgb(243, 233, 233)); display: flex; align-items: center; justify-content: center;'>"
                    + "    <div class='card'>"
                    + "        <button class='dismiss' type='button'>×</button>"
                    + "        <div class='header'>"
                    + "            <div class='div_image_v'>"
                    + "                <div class='image'>"
                    + "                    <svg viewBox='0 0 24 24' fill='none' xmlns='http://www.w3.org/2000/svg'>"
                    + "                        <path d='M20 7L9.00004 18L3.99994 13' stroke='#000000' stroke-width='1.5' stroke-linecap='round' stroke-linejoin='round'></path>"
                    + "                    </svg>"
                    + "                </div>"
                    + "            </div>"
                    + "            <div class='content'>"
                    + "                <span class='title'>Você adquiriu <strong>" + quantity + "</strong> bilhetes na Ação: <strong>" + model.getNameAward() + "</strong></span>"
                    + "                <p class='message'>Obrigado pela confiança, não perca a chance de mudar de vida, atualmente ainda restam <strong>" + model.getTicketQuantity() + "</strong> bilhetes para finalizar a ação!</p>"
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
