package medic.bodymedic.dto;

import lombok.Data;

@Data

public class CommentDTO {
    private String noticeSeq;
    private String userId;
    private String contents;
    private String regDt;
    private String chgDt;
    private String commentSeq;
    private String ref;
    private String refRank;

    private int start;
    private int finish;
}
