package medic.bodymedic.persistence.mapper.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medic.bodymedic.persistence.mapper.AbstractMongoDBComon;
import medic.bodymedic.persistence.mapper.IMainMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MainMapper extends AbstractMongoDBComon implements IMainMapper {
    private final MongoTemplate mongodb;

}
