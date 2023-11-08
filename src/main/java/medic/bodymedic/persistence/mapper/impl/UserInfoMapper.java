package medic.bodymedic.persistence.mapper.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medic.bodymedic.dto.FavoriteDTO;
import medic.bodymedic.dto.UserInfoDTO;
import medic.bodymedic.persistence.mapper.AbstractMongoDBComon;
import medic.bodymedic.persistence.mapper.IUserInfoMapper;
import medic.bodymedic.util.CmmUtil;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Consumer;

import static com.mongodb.client.model.Updates.set;
@Slf4j
@RequiredArgsConstructor
@Component
public class UserInfoMapper extends AbstractMongoDBComon implements IUserInfoMapper {

    private final MongoTemplate mongodb;

    @Override
    public int insertUserInfo(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".insertData Start!");
        log.info("user_id :"+pDTO.getUser_id());
        log.info("email :"+pDTO.getEmail());
        // 데이터를 저장할 컬렉션 생성
        super.createCollection(mongodb, "UserCollection");

        // 저장할 컬렉션 객체 생성
        MongoCollection<Document> col = mongodb.getCollection("UserCollection");
        Document query = new Document();
        query.append("user_id", CmmUtil.nvl(pDTO.getUser_id()));
        Document projection = new Document();
        projection.append("user_id", "$user_id");
        projection.append("_id", 0);

        // MongoDB의 find 명령어를 통해 조회할 경우 사용함
        // 조회하는 데이터의 양이 적은 경우, find를 사용하고, 데이터양이 많은 경우 무조건 Aggregate 사용한다.
        UserInfoDTO rDTO = new UserInfoDTO();
        FindIterable<Document> rs = col.find(query).projection(projection);

        for (Document doc : rs) {
            if (doc == null) {
                    doc = new Document();
            }
                String user_id = CmmUtil.nvl(doc.getString("user_id"));

                log.info("user_id : " + user_id);
                log.info("여기온건가?");
                rDTO.setUser_id(user_id);

        }
        if(pDTO.getUser_id().equals(rDTO.getUser_id())){
                return 2;
        }
        query = new Document();
        query.append("email", CmmUtil.nvl(pDTO.getEmail()));
        projection = new Document();
        projection.append("email", "$email");
        projection.append("_id", 0);
        rs = col.find(query).projection(projection);

        for (Document doc : rs) {
            if (doc == null) {
                doc = new Document();

            }

            String email = CmmUtil.nvl(doc.getString("email"));


            log.info("email : " + email);

            rDTO.setEmail(email);
        }
        if(pDTO.getEmail().equals(rDTO.getEmail())){
            return 2;
        }
         // DTO를 Map 데이터타입으로 변경 한 뒤, 변경된 Map 데이터타입을 Document로 변경하기
        col.insertOne(new Document(new ObjectMapper().convertValue(pDTO, Map.class)));



        log.info(this.getClass().getName() + ".insertUserInfo");

        return 1;
    }

    @Override
    public UserInfoDTO getUserExists(UserInfoDTO pDTO) throws Exception {
        return null;
    }

    @Override
    public int getUserLoginCheck(UserInfoDTO pDTO) throws Exception {
        int res = 0;
        MongoCollection<Document> col = mongodb.getCollection("UserCollection");

        // 조회할 조건(SQL의 WHERE 역할 /  SELECT song, singer FROM MELON_20220321 where singer ='방탄소년단')
        Document query = new Document();
        query.append("user_id", CmmUtil.nvl(pDTO.getUser_id()));
        query.append("password", CmmUtil.nvl(pDTO.getPassword()));
        Document projection = new Document();
        projection.append("user_id", "$user_id");
        projection.append("password", "$password");
        projection.append("_id", 0);

        // MongoDB의 find 명령어를 통해 조회할 경우 사용함
        // 조회하는 데이터의 양이 적은 경우, find를 사용하고, 데이터양이 많은 경우 무조건 Aggregate 사용한다.
        FindIterable<Document> rs = col.find(query).projection(projection);
        UserInfoDTO rDTO = new UserInfoDTO();
        for (Document doc : rs) {
            if (doc == null) {
                doc = new Document();

            }

            // 조회 잘되나 출력해 봄
            String user_id = CmmUtil.nvl(doc.getString("user_id"));
            String password = CmmUtil.nvl(doc.getString("password"));

            log.info("user_id : " + user_id);
            log.info("password : " + password);



            rDTO.setUser_id(user_id);
            rDTO.setPassword(password);
            // 레코드 결과를 List에 저장하기
        }
        if(rDTO.getUser_id()!= null){
          res = 1;
        }else{
            res = 0;
        }
        return res;
    }

    @Override
    public UserInfoDTO find_id(UserInfoDTO pDTO) throws Exception {
        int res = 0;
        MongoCollection<Document> col = mongodb.getCollection("UserCollection");
        UserInfoDTO uDTO = new UserInfoDTO();
        Document query = new Document();
        query.append("email", CmmUtil.nvl(pDTO.getEmail()));


        Document projection = new Document();
        projection.append("email", "$email");
        projection.append("user_id", "$user_id");

        // MongoDB의 find 명령어를 통해 조회할 경우 사용함
        // 조회하는 데이터의 양이 적은 경우, find를 사용하고, 데이터양이 많은 경우 무조건 Aggregate 사용한다.
        FindIterable<Document> rs = col.find(query).projection(projection);
        for (Document doc : rs) {
            if (doc == null) {
                doc = new Document();

            }

            // 조회 잘되나 출력해 봄
            String user_id = CmmUtil.nvl(doc.getString("user_id"));
            log.info("user_id"+user_id);

            uDTO.setUser_id(user_id);
            uDTO.setEmail(pDTO.getEmail());
        }
        return uDTO;
    }

    @Override
    public boolean find_ps(UserInfoDTO pDTO) throws Exception {
        MongoCollection<Document> col = mongodb.getCollection("UserCollection");
        UserInfoDTO uDTO = new UserInfoDTO();
        Document query = new Document();
        query.append("email", CmmUtil.nvl(pDTO.getEmail()));
        query.append("user_id", CmmUtil.nvl(pDTO.getUser_id()));

        Document projection = new Document();
        projection.append("email", "$email");
        projection.append("user_id", "$user_id");

        // MongoDB의 find 명령어를 통해 조회할 경우 사용함
        // 조회하는 데이터의 양이 적은 경우, find를 사용하고, 데이터양이 많은 경우 무조건 Aggregate 사용한다.
        FindIterable<Document> rs = col.find(query).projection(projection);
        for (Document doc : rs) {
            if (doc == null) {
                doc = new Document();
            }

            // 조회 잘되나 출력해 봄
            String user_id = CmmUtil.nvl(doc.getString("user_id"));
            log.info("user_id"+user_id);

            uDTO.setUser_id(user_id);
            uDTO.setEmail(pDTO.getEmail());
        }
        return uDTO.getUser_id() != null;
    }



    @Override
    public List<UserInfoDTO> getUserList() {
        return null;
    }

    @Override
    public void DeleteID(UserInfoDTO uDTO) {

    }

    @Override
    public void ps_change(UserInfoDTO uDTO) {
        MongoCollection<Document> col = mongodb.getCollection("UserCollection");
        Document query = new Document();
        query.append("user_id", uDTO.getUser_id());
        log.info(uDTO.getPassword());
        FindIterable<Document> rs = col.find(query);

        rs.forEach((Consumer<? super Document>) doc -> col.updateOne(doc, set("password", uDTO.getPassword())));
    }
}
