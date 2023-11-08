package medic.bodymedic.service;

import medic.bodymedic.dto.FavoriteDTO;
import medic.bodymedic.dto.UserInfoDTO;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface IMyPageService {
    int newPassword(UserInfoDTO pDTO, String newPW) throws Exception;

    void insertConsult(String user_id, String consult) throws Exception;

    int deleteConsult(String user_id, String consult) throws Exception;

    UserInfoDTO myPage(UserInfoDTO pDTO) throws Exception;

    FavoriteDTO getFav(String user_id) throws Exception;

    int insertFav(FavoriteDTO fDTO, String favorite) throws Exception;

    int deleteFav(FavoriteDTO fDTO, String favorite) throws Exception;

    int userDelete(String user_id) throws Exception;
}
