package medic.bodymedic.service;


import medic.bodymedic.dto.MailDTO;

public interface IMailService {

    //메일 발송
    int doSendMail(MailDTO pDTO);
}
