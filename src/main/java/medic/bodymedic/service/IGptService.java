package medic.bodymedic.service;

import medic.bodymedic.dto.Gpt35DTO;
import medic.bodymedic.dto.MessageDTO;

import java.util.List;

public interface IGptService {
    /**
     * GPT-3.5 모델 기반 결과 받아오기
     *
     * @param pList 질문 문장을 컬렉션 구조로 저장
     * @return 결과
     */
    Gpt35DTO getGpt35(List<MessageDTO> pList) throws Exception;
}
