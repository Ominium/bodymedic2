package medic.bodymedic.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Data
public class Gpt35DTO {

    private String model; // 학습모델 : gpt-3.5-turbo
    private List<MessageDTO> messages; // ChatGPT에 물어볼 질문

    private String message; // GPT 3.5 질문
    private String result; // GPT 3.5 대답

}