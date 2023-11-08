package medic.bodymedic.persistence.mapper;

import medic.bodymedic.dto.CommentDTO;
import medic.bodymedic.dto.NoticeDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface INoticeMapper {

    //게시판 리스트
    List<NoticeDTO> getNoticeList(NoticeDTO pDTO) throws Exception;

    //게시판 글 등록
    void insertNoticeInfo(NoticeDTO pDTO) throws Exception;

    //게시판 상세보기
    NoticeDTO getNoticeInfo(NoticeDTO pDTO) throws Exception;

    //게시판 조회수 업데이트
    void updateNoticeReadCnt(NoticeDTO pDTO) throws Exception;

    //게시판 글 수정
    void updateNoticeInfo(NoticeDTO pDTO) throws Exception;

    //게시판 글 삭제
    void deleteNoticeInfo(NoticeDTO pDTO) throws Exception;

    int noticeCount(NoticeDTO nDTO);

    void deleteComment(CommentDTO cDTO);

    int commentCount(CommentDTO nDTO);

    void commentUpdate(CommentDTO cDTO);

    List<CommentDTO> getCommentsList(CommentDTO pDTO);

    void InsertComment(CommentDTO cDTO);
}
