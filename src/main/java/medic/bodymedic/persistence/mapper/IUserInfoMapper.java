package medic.bodymedic.persistence.mapper;


import medic.bodymedic.dto.FavoriteDTO;
import medic.bodymedic.dto.UserInfoDTO;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface IUserInfoMapper {



    // 회원 가입하기(회원정보 등록하기)
    int insertUserInfo(UserInfoDTO pDTO) throws Exception;

    // 회원 가입 전 중복체크하기(DB조회하기)
    UserInfoDTO getUserExists(UserInfoDTO pDTO) throws Exception;

    // 로그인을 위해 아이디와 비밀번호가 일치하는지 확인하기
    int getUserLoginCheck(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO find_id(UserInfoDTO pDTO) throws Exception;

    boolean find_ps(UserInfoDTO pDTO) throws Exception;

    List<UserInfoDTO> getUserList();

    void DeleteID(UserInfoDTO uDTO);

    void ps_change(UserInfoDTO uDTO);
}

