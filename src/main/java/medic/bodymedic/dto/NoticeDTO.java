package medic.bodymedic.dto;

import lombok.Data;

@Data
public class NoticeDTO {
    private long noticeSeq;
    private String title; // 제목
    private String noticeYn; // 공지글 여부
    private String contents; // 글 내용
    private String userId; // 작성자
    private String readCnt; // 조회수
    private String regId; // 등록자 아이디
    private String regDt; // 등록일
    private String chgId; // 수정자 아이디
    private String chgDt; // 수정일
    private String userName; // 등록자명
    private int start;
    private int finish;
}
