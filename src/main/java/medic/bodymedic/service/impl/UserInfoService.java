package medic.bodymedic.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medic.bodymedic.dto.MailDTO;
import medic.bodymedic.dto.UserInfoDTO;
import medic.bodymedic.persistence.mapper.IUserInfoMapper;
import medic.bodymedic.service.IMailService;
import medic.bodymedic.service.IUserInfoService;
import medic.bodymedic.util.CmmUtil;
import medic.bodymedic.util.DateUtil;
import medic.bodymedic.util.EncryptUtil;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service("UserInfoService")
public class UserInfoService implements IUserInfoService {

    // RequiredArgsConstructor 어노테이션으로 생성자를 자동 생성함
    // userInfoMapper 변수에 이미 메모리에 올라간 Mapper 객체를 넣어줌
    // 예전에는 autowired 어노테이션를 통해 설정했었지만, 이젠 생성자를 통해 객체 주입함
    private final IUserInfoMapper userInfoMapper;

    //메일 발송을 위한 MailService 자바 객체 가져오기
    @Resource(name = "MailService")
    private IMailService mailService;

    @Override
    public List<UserInfoDTO> getUserList() throws Exception {

        log.info(this.getClass().getName() + ".getUserList start!");

        return userInfoMapper.getUserList();
    }

    @Override
    public void DeleteID(UserInfoDTO uDTO) {
        userInfoMapper.DeleteID(uDTO);
    }


    @Override
    public UserInfoDTO find_id(UserInfoDTO pDTO) throws Exception{
       UserInfoDTO  uDTO = userInfoMapper.find_id(pDTO);
       if(uDTO != null){
           MailDTO mDTO = new MailDTO();
           mDTO.setToMail(EncryptUtil.decAES128CBC(uDTO.getEmail()));
           mDTO.setTitle("아이디 찾기 입니다.");
           mDTO.setContents("회원님의 아이디 : "+uDTO.getUser_id());
           mailService.doSendMail(mDTO);
       }
       log.info(getClass().getName() + "find_id Start!!");
       log.info(Objects.requireNonNull(uDTO).getEmail());

        return uDTO;
    }
    public String RandomPs(int passwordlength){
        char[] pwdcharSet = new char[]{
                '0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j',
                'k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'
        };
        int idx = 0;
        StringBuffer sb = new StringBuffer();
        for(int i =0 ; i<passwordlength;i++){
            idx = (int)(pwdcharSet.length*Math.random());
            sb.append(pwdcharSet[idx]);
        }
        return sb.toString();
    }
    @Override
    public int find_ps(UserInfoDTO pDTO) throws Exception{
        if(pDTO ==null){
            pDTO = new UserInfoDTO();
        }
        int res = 0;
        String psnew = RandomPs(8);
        UserInfoDTO  uDTO = new UserInfoDTO();
        uDTO.setEmail(pDTO.getEmail());
        uDTO.setUser_id(pDTO.getUser_id());
        uDTO.setPassword(EncryptUtil.encHashSHA256(psnew));
        log.info("email :"+uDTO.getEmail());
        log.info("user_id :"+uDTO.getUser_id());
        if(userInfoMapper.find_ps(uDTO)){

            userInfoMapper.ps_change(uDTO);
            MailDTO mDTO = new MailDTO();
            mDTO.setToMail(EncryptUtil.decAES128CBC(uDTO.getEmail()));
            mDTO.setTitle("새로운 비밀번호로 로그인 후 비밀번호 변경 바랍니다.");
            mDTO.setContents("회원님의 비밀번호 : "+psnew);
            mailService.doSendMail(mDTO);
            res = 1;
        }else{
            res = 2;
        }
       return res;



    }

    @Override
    public int insertUserInfo(UserInfoDTO pDTO) throws Exception {

        // 회원가입 성공 : 1, 아이디 중복으로인한 가입 취소 : 2, 기타 에러 발생 : 0


        // controller에서 값이 정상적으로 못 넘어오는 경우를 대비하기 위해 사용함
        if (pDTO == null) {
            pDTO = new UserInfoDTO();
        }
        int res = userInfoMapper.insertUserInfo(pDTO);
        // 회원 가입 중복 방지를 위해 DB에서 데이터 조회


        // 중복된 회원정보가 있는 경우, 결과값을 2로 변경하고, 더 이상 작업을 진행하지 않음
        if (res == 2) {

            return 2;
            // 회원가입이 중복이 아니므로, 회원가입 진행함
        } else {

            // db에 데이터가 등록되었다면(회원가입 성공했다면....
            if (res == 1) {


                /*
                 * #######################################################
                 *        				메일 발송 로직 추가 시작!!
                 * #######################################################
                 */

                MailDTO mDTO = new MailDTO();

                //회원정보화면에서 입력받은 이메일 변수(아직 암호화되어 넘어오기 때문에 복호화 수행함)
                mDTO.setToMail(EncryptUtil.decAES128CBC(CmmUtil.nvl(pDTO.getEmail())));

                mDTO.setTitle("회원가입을 축하드립니다."); //제목

                //메일 내용에 가입자 이름넣어서 내용 발송
                mDTO.setContents(CmmUtil.nvl(pDTO.getUser_name()) + "님의 회원가입을 진심으로 축하드립니다.");

                //회원 가입이 성공했기 때문에 메일을 발송함
                mailService.doSendMail(mDTO);

                /*
                 * #######################################################
                 *        				메일 발송 로직 추가 끝!!
                 * #######################################################
                 */

            } else {
                res = 0;

            }

        }

        return res;
    }

    /**
     * 로그인을 위해 아이디와 비밀번호가 일치하는지 확인하기
     *
     * @param pDTO 로그인을 위한 회원아이디, 비밀번호
     * @return 로그인된 회원아이디 정보
     */
    @Override
    public int getUserLoginCheck(UserInfoDTO pDTO) throws Exception {
        return userInfoMapper.getUserLoginCheck(pDTO);
    }
}
