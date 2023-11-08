package medic.bodymedic.service;

import medic.bodymedic.dto.CommentDTO;
import medic.bodymedic.dto.NoticeDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface INoticeService {
    List<NoticeDTO> getNoticeList(NoticeDTO pDTO) throws Exception;

    void deleteComment(CommentDTO cDTO) throws Exception;

    int commentCount(CommentDTO nDTO) throws Exception;

    @Transactional
    void commentUpdate(CommentDTO cDTO) throws Exception;

    @Transactional
    NoticeDTO getNoticeInfo(NoticeDTO pDTO, boolean type) throws Exception;

    @Transactional
    List<CommentDTO> getCommentsList(CommentDTO pDTO) throws Exception;

    void InsertComment(CommentDTO cDTO)throws Exception;

    @Transactional
    void insertNoticeInfo(NoticeDTO pDTO) throws Exception;

    @Transactional
    void updateNoticeInfo(NoticeDTO pDTO) throws Exception;

    @Transactional
    void deleteNoticeInfo(NoticeDTO pDTO) throws Exception;

    int noticeCount(NoticeDTO nDTO);
}
