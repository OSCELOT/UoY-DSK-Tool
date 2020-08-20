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

/**
 * @author Kelvin Hai {@link <a href="mailto:kelvin.hai@york.ac.uk">kelvin.hai@york.ac.uk</a>}
 * @version $Revision$ $Date$
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.ac.york.its.vle.b2.dsk.data.*;

import blackboard.admin.data.IAdminObject.RowStatus;
import blackboard.admin.data.user.Person;
import blackboard.admin.persist.user.PersonPersister;
import blackboard.data.ValidationException;
import blackboard.data.user.User;
import blackboard.db.ConstraintViolationException;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.PersistenceException;
import blackboard.persist.SearchOperator;
import blackboard.persist.user.UserDbLoader;
import blackboard.persist.user.UserSearch;
import blackboard.util.StringUtil;
import uk.ac.york.its.vle.b2.dsk.model.PersistenceOutcome;

@Service
public class UserDSKServiceManagerImpl implements UserDSKServiceManager {
    private static final Logger logger = LoggerFactory.getLogger(UserDSKServiceManagerImpl.class);

    @Autowired
    private ServiceManager serviceManager;

    @Autowired
    private ReferenceServiceManager referenceServiceManager;

    @Autowired
    private UserDbLoader userLoader;

    @Autowired
    private PersonPersister personPersister;


    public List<PersistenceOutcome> persistPerson(Form form) {

        //To report persistence result.
        List<PersistenceOutcome> outcomes = null;
        boolean isHeaderLogged = false;
        String[] userNames = null;
        if (null != form && null != (userNames = form.getSelectedUserNames()) && userNames.length > 0) {
            logger.info(Parameter.CHANGE_COMMENTS_TEXTAREA_KEY.getName() + ": " + form.getChangeComments());
            outcomes = new ArrayList<PersistenceOutcome>();
            PersistenceOutcome outcome = null;
            Person loadedPerson = null;
            for (String userName : userNames) {
                outcome = new PersistenceOutcome();
                loadedPerson = serviceManager.getPersonByUserName(userName);
                if (null != loadedPerson) {

                    if (!isHeaderLogged) {
                        logger.info(outcome.getHeader());
                        isHeaderLogged = true;
                    }
                    log(loadedPerson);

                    outcomes.add(getPersistenceOutcome(loadedPerson, true));
                    outcome.setUserName(userName);
                    outcome.setFamilyName(loadedPerson.getFamilyName());
                    outcome.setGivenName(loadedPerson.getGivenName());
                    outcome.setUserBatchUid(loadedPerson.getBatchUid());
                    outcome.setDataSourceKey(loadedPerson.getDataSourceBatchUid());
                    outcome.setRowStatus(loadedPerson.getRowStatus().toFieldName());
                    outcome.setIsAvailable(loadedPerson.getIsAvailable());

                    String selectedAvailability = null, selectedRowStatus = null, selectedDataSourceKey = null;

                    // Set Availability
                    if (form.getIsAvailabilityUpdateRequired()) {
                        if (StringUtil.notEmpty(selectedAvailability = form.getSelectedAvailability())
                                && selectedAvailability.trim().equals(B2Enum.Available.getName())) {
                            loadedPerson.setIsAvailable(true);
                            outcome.setIsAvailable(true);
                        } else {
                            outcome.setIsAvailable(false);
                            loadedPerson.setIsAvailable(false);
                        }
                    }

                    //Set RowStatus to enabled/disabled
                    if (form.getIsStatusUpdateRequired()) {
                        if (StringUtil.notEmpty(selectedRowStatus = form.getSelectedRowStatus())
                                && selectedRowStatus.trim().equals(B2Enum.Enabled.getName())) {
                            outcome.setRowStatus(RowStatus.ENABLED.toFieldName());
                            loadedPerson.setRowStatus(RowStatus.ENABLED);
                        } else {
                            outcome.setRowStatus(RowStatus.DISABLED.toFieldName());
                            loadedPerson.setRowStatus(RowStatus.DISABLED);
                        }
                    }

                    //Set Data Source Key
                    if (form.getIsDataSourceKeyUpdateRequired()) {
                        if (StringUtil.notEmpty(selectedDataSourceKey = form.getSelectedDataSourceKey())) {
                            loadedPerson.setDataSourceBatchUid(selectedDataSourceKey.trim());
                            outcome.setDataSourceKey(selectedDataSourceKey.trim());
                        }
                    }

                    try {
                        personPersister.save(loadedPerson);
                        outcome.setIsPersistedSuccess(true);
                        log(outcome);
                    } catch (KeyNotFoundException e) {
                        logger.error("Method: persistPerson: KeyNotFoundException: " + e.getMessage());
                    } catch (PersistenceException e) {
                        logger.error("Method: persistPerson: PersistenceException: " + e.getMessage());
                    } catch (ConstraintViolationException e) {
                        logger.error("Method: persistPerson: ConstraintViolationException: " + e.getMessage());
                    } catch (ValidationException e) {
                        logger.error("Method: persistPerson: ValidationException: " + e.getMessage());
                    }

                }
                outcomes.add(outcome);
            }
        }

        return outcomes;
    }


    public List<B2User> getUsersByUserSearch(Form form, String sortByPropertyName) {
        List<User> users = getUsers(form);
        if (null != users && users.size() > 0) {
            serviceManager.sortUsers(users, sortByPropertyName);
            Map<String, String> dskMap = referenceServiceManager.getDataSourceKeysMap();
            List<B2User> b2Users = new ArrayList<B2User>();
            B2User b2User = null;
            for (User user : users) {
                if (null != user) {
                    b2User = new B2User(user);
                    try {

                        if (!("0").equals(user.getBbAttributes().getBbAttribute("RowStatus").getValue().toString())) {
                            b2User.setIsUserDisabled(true);
                        }
                        if (null != dskMap) {
                            b2User.setDataSourceKey(dskMap.get(user.getDataSourceId().getExternalString()));
                        }
                    } catch (NullPointerException e) {
                    }
                    b2Users.add(b2User);
                }
            }

            if (null != b2Users && b2Users.size() > 0) {
                return b2Users;
            }
        }
        return null;
    }

    public String getUserInfo(String userName) {
        Person person = serviceManager.getPersonByUserName(userName);

        if (null != person) {
            StringBuffer sb = new StringBuffer();
            sb.append(person.getUserName().toUpperCase());
            sb.append(" - ");
            sb.append(person.getGivenName()).append(" ").append(person.getFamilyName());
            return sb.toString();
        }

        return null;
    }

    private List<User> getUsers(Form form) {
        UserSearch userSearch = null;
        List<blackboard.data.user.User> users = null;
        if (null != (userSearch = this.getUserSearch(form))) {
            try {
                users = userLoader.loadByUserSearch(userSearch);
                return users;
            } catch (PersistenceException e) {
                logger.error("Method: getUsers: PersistenceException: " + e.getMessage());
            }
        }

        return null;
    }

    private UserSearch getUserSearch(Form form) {
        String searchText = null;
        if (null != form && StringUtil.notEmpty(searchText = form.getSearchText())) {
            return UserSearch.getNameSearch(getUserSearchKey(form), getSearchOperator(form), searchText);
        }
        return null;
    }

    private UserSearch.SearchKey getUserSearchKey(Form form) {
        String searchKey = null;
        if (StringUtil.notEmpty(searchKey = form.getSearchKey())) {
            if (searchKey.equals(UserSearch.SearchKey.UserName.getName())) {
                return UserSearch.SearchKey.UserName;
            } else if (searchKey.equals(UserSearch.SearchKey.Email.getName())) {
                return UserSearch.SearchKey.Email;
            } else if (searchKey.equals(UserSearch.SearchKey.FamilyName.getName())) {
                return UserSearch.SearchKey.FamilyName;
            } else if (searchKey.equals(UserSearch.SearchKey.GivenName.getName())) {
                return UserSearch.SearchKey.GivenName;
            } else if (searchKey.equals(UserSearch.SearchKey.DataSourceKey.getName())) {
                return UserSearch.SearchKey.DataSourceKey;
            }
        }
        return null;
    }

    private SearchOperator getSearchOperator(Form form) {
        String searchOperator = null;
        if (StringUtil.notEmpty(searchOperator = form.getSearchOperator())) {
            if (searchOperator.equals(SearchOperator.Contains.name())) {
                return SearchOperator.Contains;
            } else if (searchOperator.equals(SearchOperator.StartsWith.name())) {
                return SearchOperator.StartsWith;
            } else if (searchOperator.equals(SearchOperator.Equals.name())) {
                return SearchOperator.Equals;
            }
        }
        return null;
    }

    private void log(Person person, PersistenceOutcome outcome) {
        log(person);
        log(outcome);
    }

    private void log(Person person) {
        PersistenceOutcome origin = getPersistenceOutcome(person, true);
        if (null != origin) {
            logger.info("ORIGIN: " + origin.toString());
        }
    }

    private void log(PersistenceOutcome outcome) {
        if (null != outcome) {
            logger.info("OUTCOME: " + outcome.toString());
        }
    }

    private PersistenceOutcome getPersistenceOutcome(Person person, boolean isBeforeUpdate) {
        if (null != person) {
            PersistenceOutcome outcome = new PersistenceOutcome();
            outcome.setUserName(person.getUserName());
            outcome.setFamilyName(person.getFamilyName());
            outcome.setGivenName(person.getGivenName());
            outcome.setUserBatchUid(person.getBatchUid());
            outcome.setDataSourceKey(person.getDataSourceBatchUid());
            outcome.setRowStatus(person.getRowStatus().toFieldName());
            outcome.setDataSourceKey(person.getDataSourceBatchUid());
            outcome.setIsAvailable(person.getIsAvailable());
            if (isBeforeUpdate) {
                outcome.setIsBeforeUpdate(isBeforeUpdate);
            }

            return outcome;
        }
        return null;
    }

}