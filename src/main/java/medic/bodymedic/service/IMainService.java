package medic.bodymedic.service;

import medic.bodymedic.dto.SymptomDTO;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IMainService {

    String callApi(StringBuilder urlbuilder) throws IOException, ParseException, ParserConfigurationException, SAXException;

    String XmltoJson(String xml) throws ParseException;

    StringBuilder urlBuilder(String url) throws IOException;

    List<SymptomDTO> symptomSearch(SymptomDTO symptomDTO) throws Exception;

    int symptomCount(SymptomDTO symptomDTO) throws Exception;
}
