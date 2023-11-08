package medic.bodymedic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medic.bodymedic.dto.CommentDTO;
import medic.bodymedic.dto.MsgDTO;
import medic.bodymedic.dto.NoticeDTO;
import medic.bodymedic.dto.PageDTO;
import medic.bodymedic.service.INoticeService;
import medic.bodymedic.util.CmmUtil;
import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "/notice")
@RequiredArgsConstructor
@Controller

public class NoticeController {
    // @RequiredArgsConstructor 를 통해 메모리에 올라간 서비스 객체를 Controller에서 사용할 수 있게 주입함
    private final INoticeService noticeService;

    /**
     * 게시판 리스트 보여주기
     * <p>
     * GetMapping(value = "notice/noticeList") =>  GET방식을 통해 접속되는 URL이 notice/noticeList 경우 아래 함수를 실행함
     */
    @GetMapping(value = "noticeList")
    public String noticeList(HttpServletRequest request, ModelMap model)
            throws Exception {
        // 로그 찍기(추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".NoticeList start!");
        NoticeDTO nDTO = new NoticeDTO();
        PageDTO pageDTO;
        int count = noticeService.noticeCount(nDTO);
        log.info(String.valueOf(count));
        String no = CmmUtil.nvl(request.getParameter("num"));

        if(no.isEmpty()){
            pageDTO = new PageDTO(1,count);
        }else {
            pageDTO = new PageDTO(Integer.parseInt(no),count);
        }
        log.info("page : "+pageDTO.getStart());
        nDTO.setStart(pageDTO.getStart());
        nDTO.setFinish(pageDTO.getFinish());
        // 공지사항 리스트 가져오기
        List<NoticeDTO> rList = noticeService.getNoticeList(nDTO);
        if (rList == null) {
            rList = new ArrayList<>();
        }

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rList", rList);
        model.addAttribute("count",count);

        // 현재 페이지
        model.addAttribute("select", pageDTO.getNum());
        model.addAttribute("startPageNum", pageDTO.getStartPageNum());
        model.addAttribute("endPageNum", pageDTO.getEndPageNum());

        // 이전 및 다음
        model.addAttribute("prev", pageDTO.isPrev());
        model.addAttribute("next", pageDTO.isNext());
        // 로그 찍기(추후 찍은 로그를 통해 이 함수 호출이 끝났는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".NoticeList end!");
        // 함수 처리가 끝나고 보여줄 JSP 파일명(/WEB-INF/view/notice/NoticeList.jsp)
        return "notice/noticeList";

    }



    /**
     * 게시판 작성 페이지 이동
     * <p>
     * 이 함수는 게시판 작성 페이지로 접근하기 위해 만듬
     * <p>
     * GetMapping(value = "notice/noticeReg") =>  GET방식을 통해 접속되는 URL이 notice/noticeReg 경우 아래 함수를 실행함
     */
    @GetMapping(value = "noticeReg")
    public String NoticeReg() {

        log.info(this.getClass().getName() + ".noticeReg Start!");

        log.info(this.getClass().getName() + ".noticeReg End!");

        // 함수 처리가 끝나고 보여줄 HTML (Thymeleaf) 파일명
        // templates/notice/noticeReg.html
        return "notice/noticeReg";
    }

    /**
     * 게시판 글 등록
     * <p>
     * 게시글 등록은 Ajax로 호출되기 때문에 결과는 JSON 구조로 전달해야만 함
     * JSON 구조로 결과 메시지를 전송하기 위해 @ResponseBody 어노테이션 추가함
     */

    @ResponseBody
    @PostMapping(value = "noticeInsert")
    public MsgDTO noticeInsert(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".noticeInsert Start!");

        String msg = ""; // 메시지 내용

        MsgDTO dto = null; // 결과 메시지 구조

        try {
            // 로그인된 사용자 아이디를 가져오기
            // 로그인을 아직 구현하지 않았기에 공지사항 리스트에서 로그인 한 것처럼 Session 값을 저장함
            String userId = CmmUtil.nvl((String) session.getAttribute("SESSION_USER_ID"));
            String title = CmmUtil.nvl(request.getParameter("title")); // 제목
            String noticeYn = CmmUtil.nvl(request.getParameter("noticeYn")); // 공지글 여부
            String contents = CmmUtil.nvl(request.getParameter("contents")); // 내용

            /*
             * ####################################################################################
             * 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함 반드시 작성할 것
             * ####################################################################################
             */
            log.info("session user_id : " + userId);
            log.info("title : " + title);
            log.info("noticeYn : " + noticeYn);
            log.info("contents : " + contents);

            // 데이터 저장하기 위해 DTO에 저장하기
            NoticeDTO pDTO = new NoticeDTO();
            pDTO.setUserId(userId);
            pDTO.setTitle(title);
            pDTO.setNoticeYn(noticeYn);
            pDTO.setContents(contents);

            /*
             * 게시글 등록하기위한 비즈니스 로직을 호출
             */
            noticeService.insertNoticeInfo(pDTO);

            // 저장이 완료되면 사용자에게 보여줄 메시지
            msg = "등록되었습니다.";

        } catch (Exception e) {

            // 저장이 실패되면 사용자에게 보여줄 메시지
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".noticeInsert End!");
        }

        return dto;
    }

    /**
     * 게시판 상세보기
     */
    @GetMapping(value = "noticeInfo")
    public String noticeInfo(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".noticeInfo Start!");

        String nSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 공지글번호(PK)

        /*
         * ####################################################################################
         * 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함 반드시 작성할 것
         * ####################################################################################
         */
        log.info("nSeq : " + nSeq);

        /*
         * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
         */
        NoticeDTO pDTO = new NoticeDTO();
        pDTO.setNoticeSeq(Long.parseLong(nSeq)); // String 타입을 long 타입으로 변경

        // 공지사항 상세정보 가져오기
        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        NoticeDTO rDTO = Optional.ofNullable(noticeService.getNoticeInfo(pDTO, true))
                .orElseGet(NoticeDTO::new);

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".noticeInfo End!");

        return "notice/noticeInfo";
    }

    /**
     * 게시판 수정 보기
     */
    @GetMapping(value = "noticeEditInfo")
    public String noticeEditInfo(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".noticeEditInfo Start!");

        String nSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 공지글번호(PK)

        /*
         * ####################################################################################
         * 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함 반드시 작성할 것
         * ####################################################################################
         */
        log.info("nSeq : " + nSeq);

        /*
         * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
         */
        NoticeDTO pDTO = new NoticeDTO();
        pDTO.setNoticeSeq(Long.parseLong(nSeq)); // String 타입을 long 타입으로 변경
        log.info(String.valueOf(pDTO.getNoticeSeq()));

        // 공지사항 상세정보 가져오기
        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        NoticeDTO rDTO = Optional.ofNullable(noticeService.getNoticeInfo(pDTO, true))
                .orElseGet(NoticeDTO::new);

        log.info(rDTO.getContents());
        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rDTO", rDTO);


        log.info(this.getClass().getName() + ".noticeEditInfo End!");

        return "notice/noticeEditInfo";
    }

    /**
     * 게시판 글 수정
     */
    @ResponseBody
    @PostMapping(value = "noticeUpdate")
    public MsgDTO noticeUpdate(HttpSession session, HttpServletRequest request) {

        log.info(this.getClass().getName() + ".noticeUpdate Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {
            String userId = CmmUtil.nvl((String) session.getAttribute("SESSION_USER_ID")); // 아이디
            String nSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 글번호(PK)
            String title = CmmUtil.nvl(request.getParameter("title")); // 제목
            String noticeYn = CmmUtil.nvl(request.getParameter("noticeYn")); // 공지글 여부
            String contents = CmmUtil.nvl(request.getParameter("contents")); // 내용

            /*
             * ####################################################################################
             * 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함 반드시 작성할 것
             * ####################################################################################
             */
            log.info("userId : " + userId);
            log.info("nSeq : " + nSeq);
            log.info("title : " + title);
            log.info("noticeYn : " + noticeYn);
            log.info("contents : " + contents);

            /*
             * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
             */
            NoticeDTO pDTO = new NoticeDTO();
            pDTO.setUserId(userId);
            pDTO.setNoticeSeq(Long.parseLong(nSeq)); // String 타입을 long 타입으로 변경
            pDTO.setTitle(title);
            pDTO.setNoticeYn(noticeYn);
            pDTO.setContents(contents);

            // 게시글 수정하기 DB
            noticeService.updateNoticeInfo(pDTO);

            msg = "수정되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".noticeUpdate End!");

        }

        return dto;
    }

    /**
     * 게시판 글 삭제
     */
    @ResponseBody
    @PostMapping(value = "noticeDelete")
    public MsgDTO noticeDelete(HttpServletRequest request) {

        log.info(this.getClass().getName() + ".noticeDelete Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {
            String nSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 글번호(PK)

            /*
             * ####################################################################################
             * 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함 반드시 작성할 것
             * ####################################################################################
             */
            log.info("nSeq : " + nSeq);

            /*
             * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
             */
            NoticeDTO pDTO = new NoticeDTO();


            // 게시글 삭제하기 DB
            noticeService.deleteNoticeInfo(pDTO);

            msg = "삭제되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".noticeDelete End!");

        }

        return dto;
    }

    @PostMapping(value = "InsertComment")
    @ResponseBody
    public void InsertComment(String notice_seq, String user_id, String contents) throws Exception {
        log.info(this.getClass().getName() + ".InsertComment start!");
        CommentDTO cDTO = new CommentDTO();
        cDTO.setNoticeSeq(notice_seq);
        cDTO.setUserId(user_id);
        cDTO.setContents(contents);
        noticeService.InsertComment(cDTO);

        log.info(this.getClass().getName() + ".InsertComment end!");

    }
    @PostMapping(value = "InsertReply")
    @ResponseBody
    public void InsertReply(String notice_seq,String user_id,String contents,String ref, String ref_rank) throws Exception {
        log.info(this.getClass().getName() + ".InsertReply start!");

        CommentDTO cDTO = new CommentDTO();
        cDTO.setNoticeSeq(notice_seq);
        cDTO.setUserId(user_id);
        cDTO.setContents(contents);
        cDTO.setRef(ref);
        cDTO.setRefRank(String.valueOf(Integer.parseInt(ref_rank) + 1));

        noticeService.InsertComment(cDTO);

        log.info(this.getClass().getName() + ".InsertReply end!");

    }
    @PostMapping(value ="CommentUpdate")
    @ResponseBody
    public void CommentUpdate(String comment_seq, String contents ) throws Exception {
        CommentDTO cDTO = new CommentDTO();

        log.info(contents);
        log.info(comment_seq);
        cDTO.setCommentSeq(comment_seq);
        cDTO.setContents(contents);
        noticeService.commentUpdate(cDTO);

    }

    @GetMapping(value ="Comment")
    @ResponseBody
    public Object CommentCheck(String notice_seq,String num ) throws Exception {
        log.info("Comment check start");
        CommentDTO nDTO = new CommentDTO();
        nDTO.setNoticeSeq(notice_seq);
        PageDTO pageDTO;
        int count = noticeService.commentCount(nDTO);
        if(num.isEmpty()){
            pageDTO = new PageDTO(1,count);
        }else {
            pageDTO = new PageDTO(Integer.parseInt(num),count);
        }
        nDTO.setStart(pageDTO.getStart());
        nDTO.setFinish(pageDTO.getFinish());
        List<CommentDTO> cList =  noticeService.getCommentsList(nDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        JSONArray jsonArray = new JSONArray();
        if(cList!= null){
            for (CommentDTO commentDTO : cList) {
                jsonArray.put(objectMapper.writeValueAsString(commentDTO));

            }
        }

        return jsonArray.toString();
    }
    @GetMapping(value ="CommentCount")
    @ResponseBody
    public String CommentCount(String notice_seq)throws Exception{
        log.info("CommentCount check start");
        CommentDTO nDTO = new CommentDTO();
        nDTO.setNoticeSeq(notice_seq);
        int count = noticeService.commentCount(nDTO);

        return String.valueOf(count);
    }
    @GetMapping(value ="CommentPage")
    @ResponseBody
    public Object CommentPageCheck(String notice_seq,String num ) throws Exception {
        log.info("CommentPage check start");
        CommentDTO nDTO = new CommentDTO();
        nDTO.setNoticeSeq(notice_seq);
        PageDTO pageDTO;
        int count = noticeService.commentCount(nDTO);

        if(num.isEmpty()){
            pageDTO = new PageDTO(1,count);
        }else {
            pageDTO = new PageDTO(Integer.parseInt(num),count);
        }
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(pageDTO);
    }
    @DeleteMapping(value="commentDelete")
    @ResponseBody
    public void CommentDelete(String comment_seq) throws Exception{
        CommentDTO cDTO = new CommentDTO();
        cDTO.setCommentSeq(comment_seq);
        noticeService.deleteComment(cDTO);

    }

}
