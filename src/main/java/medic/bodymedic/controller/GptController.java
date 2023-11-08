package medic.bodymedic.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medic.bodymedic.dto.Gpt35DTO;
import medic.bodymedic.dto.MessageDTO;
import medic.bodymedic.service.IGptService;
import medic.bodymedic.service.IMyPageService;
import medic.bodymedic.util.CmmUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequestMapping(value = "/chat")
@RequiredArgsConstructor
@Controller
public class GptController {

    private final IGptService gptService;
    private final IMyPageService myPageService;
    /**
     * GPT-3 모델 호출하여 결과받기
     */
    @GetMapping(value ="chatGpt35")
    public String chatGpt35() throws Exception{
        return "chat/chatGpt35";
    }

    /**
     * GPT-3.5 모델 호출하여 결과받기
     */
    @PostMapping(value = "Gpt35")
    @ResponseBody
    public Gpt35DTO gpt35(HttpServletRequest request, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".gpt35 Start!");

        String content = CmmUtil.nvl(request.getParameter("content"));
        String user_id = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
        log.info("content : " + content);
        MessageDTO pDTO = new MessageDTO();
        pDTO.setRole("user"); // 질문하는 사람은 user 정의
        pDTO.setContent(content);

        List<MessageDTO> pList = new LinkedList<>(); // GPT 3.5 파라미터 구조는 배열 구조로 메시지 전달
        pList.add(pDTO);

        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        Gpt35DTO rDTO = Optional.ofNullable(gptService.getGpt35(pList)).orElseGet(Gpt35DTO::new);
        myPageService.insertConsult(user_id,rDTO.getResult());
        rDTO.setMessage(content); // 웹에서 입력받은 질문을 Controller 결과로 추가하기, HTML 화면에 질문 출력을 위해서...
        log.info(this.getClass().getName() + ".gpt35 End!");

        return rDTO;
    }


}