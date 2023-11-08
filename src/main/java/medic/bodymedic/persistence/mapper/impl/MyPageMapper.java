package medic.bodymedic.persistence.mapper.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medic.bodymedic.dto.FavoriteDTO;
import medic.bodymedic.dto.UserInfoDTO;
import medic.bodymedic.persistence.mapper.AbstractMongoDBComon;
import medic.bodymedic.persistence.mapper.IMyPageMapper;
import medic.bodymedic.util.CmmUtil;
import medic.bodymedic.util.EncryptUtil;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.mongodb.client.model.Updates.set;

@Slf4j
@RequiredArgsConstructor
@Component
public class MyPageMapper extends AbstractMongoDBComon implements IMyPageMapper {

    private final MongoTemplate mongodb;

    @Override
    public int userDelete(String user_id) throws Exception{
        MongoCollection<Document> col = mongodb.getCollection("FavoriteCollection");
        MongoCollection<Document> col2 = mongodb.getCollection("UserCollection");
        Document query = new Document();
        query.append("user_id", user_id);
        Document projection = new Document();
        projection.append("user_id", "$user_id");
        // MongoDB의 find 명령어를 통해 조회할 경우 사용함
        // 조회하는 데이터의 양이 적은 경우, find를 사용하고, 데이터양이 많은 경우 무조건 Aggregate 사용한다.
        FindIterable<Document> rs = col.find(query).projection(projection);
        FindIterable<Document> rs2 = col2.find(query).projection(projection);
        rs.forEach((Consumer<? super Document>) col::deleteOne);
        rs2.forEach((Consumer<? super Document>) col2::deleteOne);
        int res =1 ;
        return res;
    }
    @Override
    public FavoriteDTO getFav(String user_id) throws Exception{
        MongoCollection<Document> col = mongodb.getCollection("FavoriteCollection");
        FavoriteDTO uDTO = new FavoriteDTO();
        Document query = new Document();
        query.append("user_id", user_id);


        Document projection = new Document();
        projection.append("user_id", "$user_id");
        projection.append("favorite", "$favorite");
        projection.append("consult", "$consult");
        // MongoDB의 find 명령어를 통해 조회할 경우 사용함
        // 조회하는 데이터의 양이 적은 경우, find를 사용하고, 데이터양이 많은 경우 무조건 Aggregate 사용한다.
        FindIterable<Document> rs = col.find(query).projection(projection);
        for (Document doc : rs) {
            if (doc == null) {
                doc = new Document();

            }

            // 조회 잘되나 출력해 봄
            if(doc.getList("favorite",String.class)!=null){
                List<String>favorite =doc.getList("favorite",String.class);
                uDTO.setFavorite(favorite);
            }
            if(doc.getList("consult",String.class)!=null){
                List<String>consult =doc.getList("consult",String.class);
                uDTO.setConsult(consult);
            }

            uDTO.setUser_id(user_id);



        }
        return uDTO;
    }
    @Override
    public int deleteFav(FavoriteDTO pDTO, String favorite) throws Exception{
        // 데이터를 저장할 컬렉션 생성
        super.createCollection(mongodb, "FavoriteCollection");

        // 저장할 컬렉션 객체 생성
        MongoCollection<Document> col = mongodb.getCollection("FavoriteCollection");
        Document query = new Document();
        query.append("user_id", CmmUtil.nvl(pDTO.getUser_id()));
        Document projection = new Document();
        projection.append("user_id", "$user_id");
        projection.append("favorite", "$favorite");
        projection.append("_id", 0);
        FindIterable<Document> rs = col.find(query).projection(projection);
        for (Document doc : rs) {
            if (doc == null) {
                doc = new Document();
            }
            List<String> favorites =doc.getList("favorite",String.class);
            favorites.remove(favorite);
            pDTO.setFavorite(favorites);

        }
        rs.forEach((Consumer<? super Document>) doc -> col.updateOne(doc, set("favorite", pDTO.getFavorite())));

        int res = 1;
        return res;

    }
    @Override
    public int deleteConsult(String user_id, String consult) throws Exception{
        super.createCollection(mongodb, "FavoriteCollection");


        // 저장할 컬렉션 객체 생성
        MongoCollection<Document> col = mongodb.getCollection("FavoriteCollection");
        FavoriteDTO pDTO = new FavoriteDTO();
        Document query = new Document();
        query.append("user_id", user_id);
        Document projection = new Document();
        projection.append("user_id", "$user_id");
        projection.append("consult", "$consult");
        projection.append("_id", 0);
        FindIterable<Document> rs = col.find(query).projection(projection);
        for (Document doc : rs) {
            if (doc == null) {
                doc = new Document();
            }
            List<String> consults =doc.getList("consult",String.class);
            consults.remove(consult);
            pDTO.setConsult(consults);

        }
        rs.forEach((Consumer<? super Document>) doc -> col.updateOne(doc, set("consult", pDTO.getConsult())));

        int res = 1;
        return res;
    }
    @Override
    public int insertFav(FavoriteDTO pDTO, String favorite) throws Exception{
        // 데이터를 저장할 컬렉션 생성
        super.createCollection(mongodb, "FavoriteCollection");

        // 저장할 컬렉션 객체 생성
        MongoCollection<Document> col = mongodb.getCollection("FavoriteCollection");
        Document query = new Document();
        query.append("user_id", CmmUtil.nvl(pDTO.getUser_id()));
        Document projection = new Document();
        projection.append("user_id", "$user_id");
        projection.append("favorite", "$favorite");
        projection.append("_id", 0);
        FindIterable<Document> rs = col.find(query).projection(projection);
        List<String> favorites = new ArrayList<>();

        for (Document doc : rs) {
            if (doc == null) {
                doc = new Document();
            }
            if(doc.getList("favorite",String.class)!=null){
                favorites = doc.getList("favorite",String.class);
            }
           favorites.add(favorite);
            pDTO.setFavorite(favorites);
            log.info(favorite + " 추가");

        }
        if(favorites.isEmpty()){
            favorites.add(favorite);
            pDTO.setFavorite(favorites);
            col.insertOne(new Document(new ObjectMapper().convertValue(pDTO, Map.class)));
        }else{
            rs.forEach((Consumer<? super Document>) doc -> col.updateOne(doc, set("favorite", pDTO.getFavorite())));
        }


        int res = 1;
        return res;
    }
    @Override
    public void insertConsult(String user_id, String consult) throws Exception{
        super.createCollection(mongodb, "FavoriteCollection");
        FavoriteDTO pDTO = new FavoriteDTO();
        // 저장할 컬렉션 객체 생성
        MongoCollection<Document> col = mongodb.getCollection("FavoriteCollection");
        Document query = new Document();
        query.append("user_id", CmmUtil.nvl(user_id));
        Document projection = new Document();
        projection.append("user_id", "$user_id");
        projection.append("consult", "$consult");
        projection.append("_id", 0);
        FindIterable<Document> rs = col.find(query).projection(projection);
        List<String> consults = new ArrayList<>();
        String id = "";
        for (Document doc : rs) {
            if (doc == null) {
                doc = new Document();
            }
            id  = doc.getString("user_id");
            if( doc.getList("consult", String.class)!=null){
                consults = doc.getList("consult", String.class);
            }
            consults.add(consult);
            pDTO.setConsult(consults);
            pDTO.setUser_id(id);
            log.info(consult + " 추가");

        }
        if(consults.isEmpty()&&id.isEmpty()){
            consults.add(consult);
            pDTO.setFavorite(consults);
            col.insertOne(new Document(new ObjectMapper().convertValue(pDTO, Map.class)));
        }else{
            rs.forEach((Consumer<? super Document>) doc -> col.updateOne(doc, set("consult", pDTO.getConsult())));
        }


    }
    @Override
    public UserInfoDTO myUserList(UserInfoDTO pDTO) throws Exception{
        MongoCollection<Document> col = mongodb.getCollection("UserCollection");

        // 조회할 조건(SQL의 WHERE 역할 /  SELECT song, singer FROM MELON_20220321 where singer ='방탄소년단')
        Document query = new Document();
        query.append("user_id", CmmUtil.nvl(pDTO.getUser_id()));

        Document projection = new Document();
        projection.append("user_id", "$user_id");
        projection.append("email", "$email");
        projection.append("user_name", "$user_name");

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
            String email = CmmUtil.nvl(doc.getString("email"));
            String user_name = CmmUtil.nvl(doc.getString("user_name"));
            log.info("user_id : " + user_id);



            rDTO.setUser_id(user_id);
            rDTO.setEmail(EncryptUtil.decAES128CBC(email));
            rDTO.setUser_name(user_name);
            // 레코드 결과를 List에 저장하기

        }
        return rDTO;
    }
}
