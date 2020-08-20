package uk.ac.york.its.vle.b2.dsk.dao;

import blackboard.persist.dao.impl.SimpleDAO;
import blackboard.persist.impl.mapping.DbObjectMap;
import blackboard.persist.impl.mapping.annotation.AnnotationMappingFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.york.its.vle.b2.dsk.model.ChangeMessage;
import uk.ac.york.its.vle.b2.dsk.model.PersistenceOutcome;

public class PersistenceOutcomeDao extends SimpleDAO<PersistenceOutcome> {
    private static final Logger logger = LoggerFactory.getLogger(PersistenceOutcomeDao.class);
    private static final DbObjectMap CHANGE_MESSAGE_MAP = AnnotationMappingFactory.getMap(ChangeMessage.class);

    public PersistenceOutcomeDao(){
        super(PersistenceOutcome.class);
    }

    public PersistenceOutcomeDao(Class<PersistenceOutcome> clazz){
        super(clazz);
    }

    public void save(PersistenceOutcome persistenceOutcome){
        getDAOSupport().persist(persistenceOutcome);
    }
}
