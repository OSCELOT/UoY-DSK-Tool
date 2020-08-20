/**
 * Copyright (C) 2016 University of York, UK.
 * <p>
 * This project was initiated through a donation of source code by the
 * University of York, UK. It contains free software; you can redistribute
 * it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * <p>
 * For more information please contact:
 * <p>
 * Web Services Group
 * IT Service
 * University of York
 * YO10 5DD
 * United Kingdom
 */
package uk.ac.york.its.vle.b2.dsk.service;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

import blackboard.persist.Id;
import blackboard.platform.config.BbConfig;
import blackboard.platform.config.ConfigurationService;
import blackboard.platform.config.ConfigurationServiceFactory;
import blackboard.platform.context.ContextManager;
import blackboard.platform.context.ContextManagerFactory;
import blackboard.platform.context.UnsetContextException;
import blackboard.platform.vxi.service.VirtualSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.stereotype.Service;

import blackboard.admin.data.course.CourseSite;
import blackboard.admin.data.course.Enrollment;
import blackboard.admin.data.datasource.DataSource;
import blackboard.admin.data.user.Person;
import blackboard.admin.persist.course.CourseSiteLoader;
import blackboard.admin.persist.datasource.DataSourceLoader;
import blackboard.admin.persist.user.PersonLoader;
import blackboard.data.course.Course;
import blackboard.data.user.User;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.PersistenceException;
import blackboard.util.StringUtil;
import uk.ac.york.its.vle.b2.dsk.dao.ChangeMessageDao;
import uk.ac.york.its.vle.b2.dsk.dao.PersistenceOutcomeDao;
import uk.ac.york.its.vle.b2.dsk.model.ChangeMessage;
import uk.ac.york.its.vle.b2.dsk.model.PersistenceOutcome;

/**
 * @author Kelvin Hai {@link <a href="mailto:kelvin.hai@york.ac.uk">kelvin.hai@york.ac.uk</a>}
 * @version $Revision$ $Date$
 */

@Service
public class ServiceManagerImpl implements ServiceManager {

    private static final Logger logger = LoggerFactory.getLogger(ServiceManagerImpl.class);

    private static final String DEFAULT_BB_UID = "DEFAULT_VI_DB_NAME";

    private static final ContextManager contextManager = ContextManagerFactory.getInstance();

    private static final ConfigurationService configurationService = ConfigurationServiceFactory.getInstance();

    @Autowired
    private DataSourceLoader dataSourceLoader;

    @Autowired
    private PersonLoader personLoader;

    @Autowired
    private CourseSiteLoader courseSiteLoader;


    public List<User> sortUsers(List<User> users, String objectPropertyName) {
        if (null != users && users.size() > 0 && StringUtil.notEmpty(objectPropertyName)) {
            PropertyComparator.sort(users, new MutableSortDefinition(objectPropertyName, true, true));
            return Collections.unmodifiableList(users);
        }
        return users;
    }

    public List<Course> sortCourses(List<Course> courses, String objectPropertyName) {
        if (null != courses && courses.size() > 0 && StringUtil.notEmpty(objectPropertyName)) {
            PropertyComparator.sort(courses, new MutableSortDefinition(objectPropertyName, true, true));
            return Collections.unmodifiableList(courses);
        }
        return courses;
    }

    public List<Enrollment> sortEnrolments(List<Enrollment> enrolments, String objectPropertyName) {
        if (null != enrolments && enrolments.size() > 0 && StringUtil.notEmpty(objectPropertyName)) {
            PropertyComparator.sort(enrolments, new MutableSortDefinition(objectPropertyName, true, true));
            return Collections.unmodifiableList(enrolments);
        }
        return enrolments;
    }


    public CourseSite getCourseSiteByBatchUid(String courseSiteBatchUid) {
        if (StringUtil.notEmpty(courseSiteBatchUid)) {
            try {
                return courseSiteLoader.load(courseSiteBatchUid);
            } catch (KeyNotFoundException e) {
                logger.error("Method: getCourseSiteByBatchUid: KeyNotFoundException: " + e.getMessage());
            } catch (PersistenceException e) {
                logger.error("Method: getCourseSiteByBatchUid: PersistenceException: " + e.getMessage());
            }
        }
        return null;
    }

    public CourseSite getCourseSiteByCourseSiteId(String courseSiteId) {
        if (StringUtil.notEmpty(courseSiteId)) {
            try {
                CourseSite cs = new CourseSite();
                cs.setCourseId(courseSiteId);
                List<CourseSite> courseSites = courseSiteLoader.load(cs);
                if (null != courseSites && courseSites.size() == 1) {
                    return courseSites.get(0);
                }

            } catch (KeyNotFoundException e) {
                logger.error("Method: getCourseSiteByCourseSiteId: KeyNotFoundException: " + e.getMessage());
            } catch (PersistenceException e) {
                logger.error("Method: getCourseSiteByCourseSiteId: PersistenceException: " + e.getMessage());
            }
        }
        return null;
    }

    public DataSource getDataSourceByBatchUid(String dataSourceBatchUid) {
        DataSource dataSource = null;
        try {
            dataSource = dataSourceLoader.loadByBatchUid(dataSourceBatchUid);
        } catch (KeyNotFoundException e) {
            logger.error("Method: getDataSourceByBatchUid: KeyNotFoundException: " + e.getMessage());
        } catch (PersistenceException e) {
            logger.error("Method: getDataSourceByBatchUid: PersistenceException: " + e.getMessage());
        }
        return dataSource;
    }


    public Person getPersonByUserName(String userName) {
        if (StringUtil.notEmpty(userName)) {
            Person person = new Person();
            person.setUserName(userName.trim());
            List<Person> persons = null;
            try {
                persons = personLoader.load(person);
            } catch (PersistenceException e) {
                logger.error("Method: getPerson: PersistenceException: " + e.getMessage());
            }
            if (null != persons && persons.size() == 1) {
                return persons.get(0);
            }
        }

        return null;
    }


    //Typically returns bblearn and bb_bb60: Default is bb_bb60
    protected String getVirtualInstallationBbUid() {
        try {
            return contextManager.getContext().getVirtualInstallation().getBbUid();
        } catch (VirtualSystemException e) {
            logger.error("[ERROR] " + e.getLocalizedMessage());
        } catch (PersistenceException e) {
            logger.error("[ERROR] " + e.getLocalizedMessage());
        } catch (UnsetContextException e) {
            logger.error("[ERROR] " + e.getLocalizedMessage());
        }
        return DEFAULT_BB_UID;
    }

    //Returns same result as getVirtualInstallationBbUid()
    protected String getBbViDatabaseSchema() {

        //Set default to "bb_bb60"
        return configurationService.getBbProperty(BbConfig.ANTARGS_DEFAULT_VI_DB_NAME, DEFAULT_BB_UID);
    }

    protected String getServerHostName(){
        InetAddress iAddr;
        try {
            iAddr = InetAddress.getLocalHost();
            return iAddr.getHostName();
        } catch (UnknownHostException e) {
            logger.error("[ERROR]: "+e.getLocalizedMessage());
        }
        return null;
    }

    protected String getServerIpAddress(){
        InetAddress iAddr;
        try {
            iAddr = InetAddress.getLocalHost();
            return iAddr.getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("[ERROR]: "+e.getLocalizedMessage());
        }
        return null;
    }

    protected String getClientHostFromBbContext() {
        return contextManager.getContext().getRequest().getRemoteHost();
    }

    protected String getClientIPFromBbContext() {
        return contextManager.getContext().getRequest().getRemoteAddr();
    }

    protected String getUserNameFromBbContext() {
        return contextManager.getContext().getSession().getUserName();
    }

    protected User.SystemRole getUserSystemRole() {
        //User.SystemRole.SYSTEM_ADMIN
        return contextManager.getContext().getUser().getSystemRole();
    }

    protected ChangeMessage getChangeMessage(String changeComments) {
        ChangeMessage changeMessage = new ChangeMessage();
        changeMessage.setChangeComment(changeComments);
        changeMessage.setServerHost(getServerHostName());
        changeMessage.setServerIP(getServerIpAddress());
        changeMessage.setClientHost(getClientHostFromBbContext());
        changeMessage.setClientIP(getClientIPFromBbContext());
        changeMessage.setUserId(getUserNameFromBbContext());
        return changeMessage;
    }

    private Id persistChangeMessage(String dskTyoe, String changeComments){
        ChangeMessage changeMessage = getChangeMessage(changeComments);
        if(null!=changeMessage && StringUtil.notEmpty(dskTyoe)) {
            changeMessage.setDskType(dskTyoe);
            return new ChangeMessageDao().save(changeMessage);
        }
        return null;
    }

    public void persistOutcome(String dskType, String changeComments, List<PersistenceOutcome> persistenceOutcomes){
        Id id = persistChangeMessage(dskType, changeComments);
        if(null!=id){
            PersistenceOutcomeDao dao = new PersistenceOutcomeDao();
            if(null!=persistenceOutcomes && persistenceOutcomes.size()>0){
                persistenceOutcomes.forEach(outcome -> {
                    outcome.setChangeMessageId(id);
                    dao.save(outcome);
                });
            }
        }

    }

}
