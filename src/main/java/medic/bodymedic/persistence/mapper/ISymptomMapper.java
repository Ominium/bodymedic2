package medic.bodymedic.persistence.mapper;

import medic.bodymedic.dto.SymptomDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISymptomMapper {
    List<SymptomDTO> symptomSearch(SymptomDTO symptomDTO) throws Exception;

    int symptomCount(SymptomDTO symptomDTO) throws Exception;
}
