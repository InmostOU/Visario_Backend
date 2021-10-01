package pro.inmost.amazon.chime.service;

public interface SendingMailService {

    boolean sendVerificationMail(String toEmail, String verificationCode);

}
