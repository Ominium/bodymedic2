package medic.bodymedic.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.net.URLEncoder;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Data
public class DrugDTO {
    private String pageNo;
    private String numOfRows;
    private String entpName;
    private String itemName;
    private String itemSeq;
    private String efcyQesitm;
    private String useMethodQesitm;
    private String atpnWarnQesitm;
    private String atpnQesitm;
    private String intrcQesitm;
    private String seQesitm;
    private String depositMethodQesitm;
    private String openDe;
    private String updateDe;

}
