package medic.bodymedic.controller;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medic.bodymedic.dto.DrugDTO;
import medic.bodymedic.dto.PageDTO;
import medic.bodymedic.dto.SymptomDTO;
import medic.bodymedic.service.IMainService;
import medic.bodymedic.service.IUserInfoService;
import medic.bodymedic.util.CmmUtil;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
@Slf4j
@RequestMapping(value = "/main")
@RequiredArgsConstructor
@Controller
public class MainController {
    @Value("${data25.sym.api.key}")
    private String sym_apikey; // 병원 키
    @Value("${data25.dis.api.key}")
    private String dis_apikey; // 약국 키
    @Value("${data25.drug.api.key}")
    private String drug_apikey; // 약국 키


    @Resource(name = "MainService")
    private IMainService mainService;



    @GetMapping(value = "symapi")
    @ResponseBody
    public String callsymApi() throws IOException, ParseException, ParserConfigurationException, SAXException {
        log.info(this.getClass().getName() + ".callsymApi Start!!");

        StringBuilder urlbuilder = mainService.urlBuilder(sym_apikey);
        log.info(this.getClass().getName() + ".callsymApi End!!");
        return mainService.callApi(urlbuilder);

    }
    @GetMapping(value = "disapi")
    @ResponseBody
    public String calldisApi() throws IOException, ParseException, ParserConfigurationException, SAXException {
        log.info(this.getClass().getName() + ".calldisApi Start!!");

        StringBuilder urlbuilder = mainService.urlBuilder(dis_apikey);
        log.info(this.getClass().getName() + ".calldisApi End!!");
        return mainService.callApi(urlbuilder);
    }
    @GetMapping(value = "drugpage")
    @ResponseBody
    public String calldrugApi(String index, String efcyQesitm,String num) throws IOException, ParseException, ParserConfigurationException, SAXException {
        log.info(this.getClass().getName() + ".calldrugApi Start!!");
        log.info(index);
        log.info(efcyQesitm);




        // 읽어들인 파일 불러오기

        StringBuilder urlbuilder = mainService.urlBuilder(drug_apikey);



        urlbuilder.append("&" + URLEncoder.encode("pageNo","UTF-8")+ "=" +URLEncoder.encode(num,"UTF-8"));
        urlbuilder.append("&" + URLEncoder.encode(index,"UTF-8")+ "=" +URLEncoder.encode(efcyQesitm,"UTF-8"));




        log.info(this.getClass().getName() + ".callsdrugApi End!!");

        return mainService.callApi(urlbuilder);

    }

    @GetMapping(value = "drugapi")
    @ResponseBody
    public String calldrugApi(String index, String efcyQesitm) throws IOException, ParseException, ParserConfigurationException, SAXException {
        log.info(this.getClass().getName() + ".calldrugApi Start!!");
        log.info(index);
        log.info(efcyQesitm);




        // 읽어들인 파일 불러오기

        StringBuilder urlbuilder = mainService.urlBuilder(drug_apikey);




        urlbuilder.append("&" + URLEncoder.encode(index,"UTF-8")+ "=" +URLEncoder.encode(efcyQesitm,"UTF-8"));




        log.info(this.getClass().getName() + ".callsdrugApi End!!");

        return mainService.callApi(urlbuilder);

    }
    @PostMapping(value = "disSearch")
    public String disSearch() throws Exception{





        return "";
    }
    @GetMapping(value = "mainPage")
    public String mainPage(HttpSession session, ModelMap model){
        return "main/mainPage";
    }
    @GetMapping(value = "mainSymptomSearch")
    public String mainSymptomSearch(HttpServletRequest request, ModelMap model) throws Exception {
        String part = CmmUtil.nvl(request.getParameter("part"));
        log.info("부위 :"+part);
        SymptomDTO symptomDTO = new SymptomDTO();
        PageDTO pageDTO;
        symptomDTO.setPart(part);
        int count = mainService.symptomCount(symptomDTO);
        String no = CmmUtil.nvl(request.getParameter("num"));
        log.info("count :"+count);
        log.info("no :" +no);
        if(no.isEmpty()){
            pageDTO = new PageDTO(1,count);
        }else {
            pageDTO = new PageDTO(Integer.parseInt(no),count);
        }
        symptomDTO.setStart(pageDTO.getStart());
        log.info("start : " + pageDTO.getStart());
        log.info("finish : "+ pageDTO.getFinish());
        symptomDTO.setFinish(pageDTO.getFinish());
        List<SymptomDTO> sList = mainService.symptomSearch(symptomDTO);

        model.addAttribute("sList",sList);
        model.addAttribute("count",count);
        model.addAttribute("part",part);
        // 현재 페이지
        model.addAttribute("select", pageDTO.getNum());
        model.addAttribute("startPageNum", pageDTO.getStartPageNum());
        model.addAttribute("endPageNum", pageDTO.getEndPageNum());

        // 이전 및 다음
        model.addAttribute("prev", pageDTO.isPrev());
        model.addAttribute("next", pageDTO.isNext());
        return "main/mainSymptomSearch";
    }
    @GetMapping(value = "mainSymptomMap")
    public String mainSymptomMap(HttpSession session, ModelMap model){
        return "main/mainSymptomMap";
    }
    @GetMapping(value = "mainDiseaseMap")
    public String mainDiseaseMap(HttpSession session, ModelMap model){
        return "main/mainDiseaseMap";
    }
    @GetMapping(value = "mainDiseaseSearch")
    public String mainDiseaseSearch(HttpSession session,HttpServletRequest request, ModelMap model) throws Exception {
        String fav = CmmUtil.nvl(request.getParameter("itemName"));
        if(fav!=null){
          model.addAttribute("fav",fav);
        }
        return "main/mainDiseaseSearch";

    }
}
