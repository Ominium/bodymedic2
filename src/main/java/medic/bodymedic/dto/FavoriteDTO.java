package medic.bodymedic.dto;



import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data

public class FavoriteDTO {
    private String user_id;

    private List<String> favorite = new ArrayList<>();

    private List<String>  consult = new ArrayList<>();
}
