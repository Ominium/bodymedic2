package medic.bodymedic.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medic.bodymedic.dto.FavoriteDTO;
import medic.bodymedic.dto.UserInfoDTO;
import medic.bodymedic.service.IMyPageService;
import medic.bodymedic.service.IUserInfoService;
import medic.bodymedic.util.EncryptUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RequestMapping(value = "/myPage")
@RequiredArgsConstructor
@Controller
public class MyPageController {


    private final IMyPageService myPageService;

    @GetMapping(value = "/myPage")
    public String myPage(HttpSession session, ModelMap model) throws Exception {
        UserInfoDTO pDTO = new UserInfoDTO();
        String user_id = (String) session.getAttribute("SS_USER_ID");
        pDTO.setUser_id(user_id);
        FavoriteDTO fDTO = myPageService.getFav(user_id);
        UserInfoDTO rDTO = myPageService.myPage(pDTO);
        model.addAttribute("rDTO", rDTO);
        model.addAttribute("fDTO", fDTO);
        return "myPage/myPage";
    }

    @PostMapping(value = "/newPassword")
    public String newPassword(HttpServletRequest request, HttpSession session, ModelMap model) throws Exception {
        log.info("new Password Start");
        UserInfoDTO pDTO = new UserInfoDTO();


        String user_id = (String) session.getAttribute("SS_USER_ID");
        String password = request.getParameter("password");
        String newPassword = request.getParameter("newpassword");

        pDTO.setUser_id(user_id);
        pDTO.setPassword(EncryptUtil.encHashSHA256(password));
        String msg = "";
        int res = myPageService.newPassword(pDTO, (EncryptUtil.encHashSHA256(newPassword)));
        if (res > 0) {
            msg = "성공하였습니다.";
        } else {
            msg = "실패하였습니다.";
        }

        model.addAttribute("msg", msg);

        return "myPage/newPassword";
    }

    @GetMapping(value = "/myPassword")
    public String myPassword() throws Exception {

        return "myPage/myPassword";
    }

    @PostMapping(value = "/insertFav")
    @ResponseBody
    public String insertFav(String user_id, String favorite) throws Exception {
        log.info("왜안들어와");
        log.info(favorite);
        FavoriteDTO fDTO = new FavoriteDTO();
        fDTO.setUser_id(user_id);
        int res = myPageService.insertFav(fDTO, favorite);
        if (res > 0) {
            return "성공하였습니다.";
        } else {
            return "실패하였습니다.";
        }

    }

    @PostMapping(value = "/userDelete")
    @ResponseBody
    public String userDelet(String user_id, HttpSession session) throws Exception {
        int res = myPageService.userDelete(user_id);
        session.removeAttribute("SS_USER_ID");
        if (res > 0) {
            return "회원탈퇴 되었습니다.";
        } else return "실패했습니다";
    }

    @PostMapping(value = "/deleteFav")
    @ResponseBody
    public String deleteFav(String user_id, String favorite) throws Exception {
        FavoriteDTO fDTO = new FavoriteDTO();
        fDTO.setUser_id(user_id);
        int res = myPageService.deleteFav(fDTO, favorite);
        if (res > 0) {
            return "성공하였습니다.";
        } else {
            return "실패하였습니다.";
        }
    }

    @PostMapping(value = "/deleteCon")
    @ResponseBody
    public String deleteCon(String user_id, String favorite) throws Exception {
        FavoriteDTO fDTO = new FavoriteDTO();
        fDTO.setUser_id(user_id);
        int res = myPageService.deleteConsult(user_id, favorite);
        if (res > 0) {
            return "성공하였습니다.";
        } else {
            return "실패하였습니다.";
        }
    }
}
