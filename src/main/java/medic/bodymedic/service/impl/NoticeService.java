package medic.bodymedic.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medic.bodymedic.dto.CommentDTO;
import medic.bodymedic.dto.NoticeDTO;
import medic.bodymedic.persistence.mapper.INoticeMapper;
import medic.bodymedic.service.INoticeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoticeService implements INoticeService {
    private final INoticeMapper noticeMapper;

    @Override
    public List<NoticeDTO> getNoticeList(NoticeDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getNoticeList start!");

        return noticeMapper.getNoticeList(pDTO);

    }
    @Override
    public void deleteComment(CommentDTO cDTO) throws Exception {
        noticeMapper.deleteComment(cDTO);
    }

    @Override
    public int commentCount(CommentDTO nDTO) throws Exception {
        return noticeMapper.commentCount(nDTO);
    }
    @Transactional
    @Override
    public void commentUpdate(CommentDTO cDTO) throws Exception {

        noticeMapper.commentUpdate(cDTO);
    }
    @Transactional
    @Override
    public NoticeDTO getNoticeInfo(NoticeDTO pDTO, boolean type) throws Exception {

        log.info(this.getClass().getName() + ".getNoticeInfo start!");

        // 상세보기할 때마다, 조회수 증가하기(수정보기는 제외)
        if (type) {
            log.info("Update ReadCNT");
            noticeMapper.updateNoticeReadCnt(pDTO);
            log.info("update end!");
        }
        NoticeDTO nDTO = noticeMapper.getNoticeInfo(pDTO);
        log.info(nDTO.getContents());
        return nDTO;

    }
    @Transactional
    @Override
    public List<CommentDTO> getCommentsList(CommentDTO pDTO) throws Exception{
        log.info(this.getClass().getName() + ".getCommentsList start!");

        return noticeMapper.getCommentsList(pDTO);
    }
    @Override
    public void InsertComment(CommentDTO cDTO)throws Exception{
        log.info(this.getClass().getName() + ".InsertComment start!");
        if(cDTO.getRef()==null){
            cDTO.setRef(String.valueOf(noticeMapper.commentCount(cDTO)));
            cDTO.setRefRank("0");
        }
        noticeMapper.InsertComment(cDTO);
    }
    @Transactional
    @Override
    public void insertNoticeInfo(NoticeDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".InsertNoticeInfo start!");

        noticeMapper.insertNoticeInfo(pDTO);
    }

    @Transactional
    @Override
    public void updateNoticeInfo(NoticeDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updateNoticeInfo start!");

        noticeMapper.updateNoticeInfo(pDTO);

    }
    @Override
    public int noticeCount(NoticeDTO nDTO) {
        log.info(this.getClass().getName()+".count start!!");

        return noticeMapper.noticeCount(nDTO);
    }
    @Transactional
    @Override
    public void deleteNoticeInfo(NoticeDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".deleteNoticeInfo start!");

        noticeMapper.deleteNoticeInfo(pDTO);

    }
}
