package uk.ac.york.its.vle.b2.dsk.dao;

import blackboard.persist.Id;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.dao.impl.SimpleDAO;
import blackboard.persist.impl.SimpleSelectQuery;
import blackboard.persist.impl.mapping.DbObjectMap;
import blackboard.persist.impl.mapping.annotation.AnnotationMappingFactory;
import blackboard.platform.security.SecurityUtil;
import blackboard.util.StringUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.york.its.vle.b2.dsk.model.ChangeMessage;

public class ChangeMessageDao extends SimpleDAO<ChangeMessage> {
    private static final Logger logger = LoggerFactory.getLogger(ChangeMessageDao.class);
    private static final DbObjectMap CHANGE_MESSAGE_MAP = AnnotationMappingFactory.getMap(ChangeMessage.class);

    public ChangeMessageDao(){
        super(ChangeMessage.class);
    }

    public ChangeMessageDao(Class<ChangeMessage> clazz){
        super(clazz);
    }

    public ChangeMessage load(String dataSetUid) throws KeyNotFoundException {
        if (StringUtil.notEmpty(dataSetUid)){
            SimpleSelectQuery query = new SimpleSelectQuery(getDAOSupport().getMap());
            query.addWhere("dataSetUid", dataSetUid);
            return getDAOSupport().load(query);
        }
        return null;
    }

    public Id save(ChangeMessage changeMessage){
        if(null!=changeMessage) {
            String dataSetUid = SecurityUtil.calculateHash(RandomStringUtils.randomAlphabetic(50));
            changeMessage.setDataSetUid(dataSetUid);
            getDAOSupport().persist(changeMessage);
            try {
                return load(dataSetUid).getId();
            } catch (KeyNotFoundException e) {
                logger.error("Unable to load ChangeMessage: " + e.getLocalizedMessage());
            }
        }
        return null;
    }
}
