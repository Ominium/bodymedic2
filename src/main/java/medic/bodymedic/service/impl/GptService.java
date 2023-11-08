package medic.bodymedic.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import medic.bodymedic.dto.Gpt35DTO;
import medic.bodymedic.dto.MessageDTO;
import medic.bodymedic.service.IGptService;
import medic.bodymedic.util.CmmUtil;
import medic.bodymedic.util.NetworkUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GptService implements IGptService {


    @Value("${chat.gpt.v35.url}")
    private String gpt35Api; // GPT-3.5 API URL

    @Value("${chat.gpt.api.key}")
    private String apiKey; // OpenAI 홈페이지에서 발급받은 인증키


    @Override
    public Gpt35DTO getGpt35(List<MessageDTO> pList) throws Exception {

        log.info(this.getClass().getName() + ".getGpt35 Start!");

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json;utf-8");
        header.put("Authorization", "Bearer " + apiKey);

        Gpt35DTO dto = new Gpt35DTO();
        dto.setModel("gpt-3.5-turbo"); // GPT-3.5 모델 정의
        dto.setMessages(pList); // 질문 및 role 정의

        String param = new ObjectMapper().writeValueAsString(dto); // GPT 3.5 전송할 파마미터를 JSON 구조로 만들기

        log.info("param : " + param);

        // "choices":[{"message":{"role":"assistant","content":"\n\n안녕하세요. 저는 AI 어시스턴트입니다. 무엇을 도와드릴까요?"}
        // ,"finish_reason":"stop","index":0}]}
        // 결과는 choices 항목의 text 항목에 저장됨
        String json = NetworkUtil.post(gpt35Api, header, param); // GPT에 응답 결과

        log.info("json : " + json);

        String result = ""; // GPT 3.5 전체 답변

        // ChatGPT 응답 메시지를 Map 객체로 변환하기
        Map<String, Object> rMap = new ObjectMapper().readValue(json, HashMap.class);

        List<Map> choices = (List<Map>) rMap.get("choices");

        for (Map<String, Object> answer : choices) {
            Map<String, Object> message = (Map<String, Object>) answer.get("message");

            result += CmmUtil.nvl((String) message.get("content")); // 결과

        }

        log.info("result : " + result);

        // HTML 출력을 이쁘게 하기 위해 \n을 <br/>로 변경함
        dto.setResult(result.replaceAll("\n", "<br/>")); // Controller 값을 전달하기 위해 저장

        log.info(this.getClass().getName() + ".getGpt35 Start!");

        return dto;
    }

}