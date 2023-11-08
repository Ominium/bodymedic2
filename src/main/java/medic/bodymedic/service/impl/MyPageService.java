package medic.bodymedic.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medic.bodymedic.dto.FavoriteDTO;
import medic.bodymedic.dto.UserInfoDTO;
import medic.bodymedic.persistence.mapper.IMyPageMapper;
import medic.bodymedic.persistence.mapper.IUserInfoMapper;
import medic.bodymedic.service.IMyPageService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyPageService implements IMyPageService {
    private final IMyPageMapper myPageMapper;
    private final IUserInfoMapper userInfoMapper;

    @Override
    public int newPassword(UserInfoDTO pDTO, String newPW) throws Exception{
        ;
        int res = userInfoMapper.getUserLoginCheck(pDTO);
        if(res >0){
            pDTO.setPassword(newPW);
            userInfoMapper.ps_change(pDTO);
        }
        return res;
    }
    @Override
    public void insertConsult(String user_id, String consult) throws Exception{
        myPageMapper.insertConsult(user_id,consult);
    }
    @Override
    public int deleteConsult(String user_id, String consult) throws Exception{
        return myPageMapper.deleteConsult(user_id,consult);
    }
    @Override
    public UserInfoDTO myPage(UserInfoDTO pDTO) throws Exception{
        return myPageMapper.myUserList(pDTO);
    }
    @Override
    public FavoriteDTO getFav(String user_id) throws Exception{
        return myPageMapper.getFav(user_id);
    }
    @Override
    public int insertFav(FavoriteDTO fDTO, String favorite) throws Exception{
        return myPageMapper.insertFav(fDTO,favorite);
    }
    @Override
    public int deleteFav(FavoriteDTO fDTO, String favorite) throws Exception{
        return myPageMapper.deleteFav(fDTO,favorite);
    }
    @Override
    public int userDelete(String user_id) throws Exception{
        return myPageMapper.userDelete(user_id);
    }
}
