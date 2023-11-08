package medic.bodymedic.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medic.bodymedic.dto.SymptomDTO;
import medic.bodymedic.persistence.mapper.IMainMapper;
import medic.bodymedic.persistence.mapper.ISymptomMapper;
import medic.bodymedic.service.IMainService;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service("MainService")
public class MainService implements IMainService {
    private final IMainMapper mainMapper;

    private final ISymptomMapper symptomMapper;
    @Value("${data25.service.api.key}")
    private String service_key;
    @Override
    public String callApi(StringBuilder urlbuilder) throws IOException {
        log.info(this.getClass().getName()+".callApi start!");
        URL url = new URL(urlbuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type","application/json");
        System.out.println("Response code: "+conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode()<=300){
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }else{
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = rd.readLine())!= null){
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        log.info(this.getClass().getName()+".callApi end!");
        log.info("sb = "+sb.toString());

        return XmltoJson(sb.toString());
       /* String text = XmltoJson(sb.toString());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS.mappedFeature());

        try {
            Map<String, Object> map = mapper.readValue(text, Map.class);
            log.info(map.toString());
            return map;
        }catch(Exception e) {
            List<String> data = mapper.readValue(text, List.class);
            Map<String, Object> map = new HashMap<>();
            map.put("data", data);

            return map;
        }*/
    }
    @Override
    public String XmltoJson(String xml)throws JSONException{
        String json="";
        try {
            JSONObject jsonObj = XML.toJSONObject(xml);
            json = jsonObj.toString();

            System.out.println("json = "+json);

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return json;
    }
    @Override
    public StringBuilder urlBuilder(String url) throws IOException {
        StringBuilder urlbuilder = new StringBuilder(url);
        urlbuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8")+ "=" +service_key);
        return urlbuilder;
    }

    @Override
    public List<SymptomDTO> symptomSearch(SymptomDTO symptomDTO) throws Exception{
        return symptomMapper.symptomSearch(symptomDTO);
    }
    @Override
    public int symptomCount(SymptomDTO symptomDTO) throws Exception{
        return symptomMapper.symptomCount(symptomDTO);
    }

}
