package medic.bodymedic.dto;

import lombok.Data;

@Data
public class SymptomDTO {
    private int symptomSeq;
    private String part;
    private String disease;
    private String sympotm;

    private int start;
    private int finish;
}
