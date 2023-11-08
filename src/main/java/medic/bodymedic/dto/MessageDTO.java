package medic.bodymedic.dto;
import lombok.Getter;
import lombok.Setter;

/**
 * GPT 3.5 모델의 Message 파라미터를 위한 DTO
 */
@Getter
@Setter
public class MessageDTO {

    private String role;
    private String content;
}