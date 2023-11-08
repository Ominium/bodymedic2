package medic.bodymedic.persistence.mapper;

import medic.bodymedic.dto.FavoriteDTO;
import medic.bodymedic.dto.UserInfoDTO;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface IMyPageMapper {


    int userDelete(String user_id) throws Exception;

    FavoriteDTO getFav(String user_id) throws Exception;

    int deleteFav(FavoriteDTO pDTO, String favorite) throws Exception;

    int deleteConsult(String user_id, String consult) throws Exception;

    int insertFav(FavoriteDTO pDTO, String favorite) throws Exception;

    void insertConsult(String user_id, String consult) throws Exception;

    UserInfoDTO myUserList(UserInfoDTO pDTO) throws Exception;
}
