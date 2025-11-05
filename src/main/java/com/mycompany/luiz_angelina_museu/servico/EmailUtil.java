package com.mycompany.luiz_angelina_museu.servico;

import java.io.InputStream;
import java.util.Properties;
// IMPORTS ATUALIZADOS DE 'javax.mail' PARA 'jakarta.mail'
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * Utilitário para enviar e-mails.
 */
public class EmailUtil {

    public static void sendPasswordResetEmail(String recipientEmail, String token) {
        try {
            // Carrega as propriedades do arquivo config.properties
            Properties props = new Properties();
            try (InputStream input = EmailUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (input == null) {
                    System.out.println("Desculpe, não foi possível encontrar o arquivo config.properties");
                    return;
                }
                props.load(input);
            }

            final String fromEmail = props.getProperty("smtp.user");
            final String password = props.getProperty("smtp.password");

            Properties mailProps = new Properties();
            mailProps.put("mail.smtp.host", props.getProperty("smtp.host"));
            mailProps.put("mail.smtp.port", props.getProperty("smtp.port"));
            mailProps.put("mail.smtp.auth", props.getProperty("smtp.auth"));
            mailProps.put("mail.smtp.starttls.enable", props.getProperty("smtp.starttls.enable"));
            mailProps.put("mail.smtp.ssl.trust", props.getProperty("smtp.host"));


            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            };

            Session session = Session.getInstance(mailProps, auth);

            // Conteúdo do E-mail
            String subject = "Redefinição de Senha - Museu";
            String resetUrl = "http://localhost:8080/luiz_angelina_museu/redefinir-senha?token=" + token;
            String emailBody = "Olá,<br><br>Você solicitou a redefinição da sua senha. " +
                               "Clique no link abaixo para criar uma nova senha:<br><br>" +
                               "<a href=\"" + resetUrl + "\">Redefinir Senha</a><br><br>" +
                               "Se você não solicitou esta alteração, por favor, ignore este e-mail.<br><br>" +
                               "Obrigado,<br>Equipe do Museu";

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(fromEmail));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            mimeMessage.setSubject(subject);
            mimeMessage.setContent(emailBody, "text/html; charset=utf-8");

            Transport.send(mimeMessage);
            System.out.println("E-mail de redefinição de senha enviado com sucesso para " + recipientEmail);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Falha ao enviar e-mail de redefinição de senha.", e);
        }
    }
}
