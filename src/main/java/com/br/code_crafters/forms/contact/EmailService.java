package com.br.code_crafters.forms.contact;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String DESTINATARIO;
    @Value("${app.base-url}")
    private String baseUrl;

    public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendHtmlEmail(ContactDto dto) throws MessagingException {

        Context context = new Context();
        context.setVariable("nomeRemetente", dto.getNome());
        context.setVariable("emailRemetente", dto.getEmail());
        context.setVariable("mensagemCorpo", dto.getMessage());

        context.setVariable("baseUrl", baseUrl);
        String htmlContent = templateEngine.process("mail/notificacao-contato", context);
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(DESTINATARIO);
        helper.setReplyTo(dto.getEmail());
        helper.setSubject("[CONTATO] " + dto.getAssunto());
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }

}
