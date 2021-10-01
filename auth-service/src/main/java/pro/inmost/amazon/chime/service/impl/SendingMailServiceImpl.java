package pro.inmost.amazon.chime.service.impl;

import com.netflix.discovery.converters.Auto;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import pro.inmost.amazon.chime.service.SendingMailService;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@ConfigurationProperties("link")
public class SendingMailServiceImpl implements SendingMailService {

    @Autowired
    private  Configuration templates;

    @Autowired
    private SendGrid mailSender;

    @Value("${link.for.user.verify}")
    private String verify;

    public boolean sendVerificationMail(String toEmail, String verificationCode) {
        String subject = "Please verify your email";
        String body = "";
        try {
            Template t = templates.getTemplate("email-verification.ftl");
            Map<String, String> map = new HashMap<>();
            map.put("VERIFICATION_URL", verify + verificationCode);
            body = FreeMarkerTemplateUtils.processTemplateIntoString(t, map);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return sendMail(toEmail, subject, body);
    }

    @Async
    public boolean sendMail(String recipient, String subject, String content) {
        Email from = new Email("ihor.soroka@inmost.pro");
        Email to = new Email(recipient);
        Mail mail = new Mail(from, subject, to, new Content("text/plain", content));

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = mailSender.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
            return true;
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return false;
    }
}
