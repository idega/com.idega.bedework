/**
 * @(#)BwCalBussiness.java    1.0.0 10:28:33 AM
 *
 * Idega Software hf. Source Code Licence Agreement x
 *
 * This agreement, made this 10th of February 2006 by and between 
 * Idega Software hf., a business formed and operating under laws 
 * of Iceland, having its principal place of business in Reykjavik, 
 * Iceland, hereinafter after referred to as "Manufacturer" and Agura 
 * IT hereinafter referred to as "Licensee".
 * 1.  License Grant: Upon completion of this agreement, the source 
 *     code that may be made available according to the documentation for 
 *     a particular software product (Software) from Manufacturer 
 *     (Source Code) shall be provided to Licensee, provided that 
 *     (1) funds have been received for payment of the License for Software and 
 *     (2) the appropriate License has been purchased as stated in the 
 *     documentation for Software. As used in this License Agreement, 
 *     �Licensee� shall also mean the individual using or installing 
 *     the source code together with any individual or entity, including 
 *     but not limited to your employer, on whose behalf you are acting 
 *     in using or installing the Source Code. By completing this agreement, 
 *     Licensee agrees to be bound by the terms and conditions of this Source 
 *     Code License Agreement. This Source Code License Agreement shall 
 *     be an extension of the Software License Agreement for the associated 
 *     product. No additional amendment or modification shall be made 
 *     to this Agreement except in writing signed by Licensee and 
 *     Manufacturer. This Agreement is effective indefinitely and once
 *     completed, cannot be terminated. Manufacturer hereby grants to 
 *     Licensee a non-transferable, worldwide license during the term of 
 *     this Agreement to use the Source Code for the associated product 
 *     purchased. In the event the Software License Agreement to the 
 *     associated product is terminated; (1) Licensee's rights to use 
 *     the Source Code are revoked and (2) Licensee shall destroy all 
 *     copies of the Source Code including any Source Code used in 
 *     Licensee's applications.
 * 2.  License Limitations
 *     2.1 Licensee may not resell, rent, lease or distribute the 
 *         Source Code alone, it shall only be distributed as a 
 *         compiled component of an application.
 *     2.2 Licensee shall protect and keep secure all Source Code 
 *         provided by this this Source Code License Agreement. 
 *         All Source Code provided by this Agreement that is used 
 *         with an application that is distributed or accessible outside
 *         Licensee's organization (including use from the Internet), 
 *         must be protected to the extent that it cannot be easily 
 *         extracted or decompiled.
 *     2.3 The Licensee shall not resell, rent, lease or distribute 
 *         the products created from the Source Code in any way that 
 *         would compete with Idega Software.
 *     2.4 Manufacturer's copyright notices may not be removed from 
 *         the Source Code.
 *     2.5 All modifications on the source code by Licencee must 
 *         be submitted to or provided to Manufacturer.
 * 3.  Copyright: Manufacturer's source code is copyrighted and contains 
 *     proprietary information. Licensee shall not distribute or 
 *     reveal the Source Code to anyone other than the software 
 *     developers of Licensee's organization. Licensee may be held 
 *     legally responsible for any infringement of intellectual property 
 *     rights that is caused or encouraged by Licensee's failure to abide 
 *     by the terms of this Agreement. Licensee may make copies of the 
 *     Source Code provided the copyright and trademark notices are 
 *     reproduced in their entirety on the copy. Manufacturer reserves 
 *     all rights not specifically granted to Licensee.
 *
 * 4.  Warranty & Risks: Although efforts have been made to assure that the 
 *     Source Code is correct, reliable, date compliant, and technically 
 *     accurate, the Source Code is licensed to Licensee as is and without 
 *     warranties as to performance of merchantability, fitness for a 
 *     particular purpose or use, or any other warranties whether 
 *     expressed or implied. Licensee's organization and all users 
 *     of the source code assume all risks when using it. The manufacturers, 
 *     distributors and resellers of the Source Code shall not be liable 
 *     for any consequential, incidental, punitive or special damages 
 *     arising out of the use of or inability to use the source code or 
 *     the provision of or failure to provide support services, even if we 
 *     have been advised of the possibility of such damages. In any case, 
 *     the entire liability under any provision of this agreement shall be 
 *     limited to the greater of the amount actually paid by Licensee for the 
 *     Software or 5.00 USD. No returns will be provided for the associated 
 *     License that was purchased to become eligible to receive the Source 
 *     Code after Licensee receives the source code. 
 */
package com.idega.bedework.bussiness;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;

import org.apache.myfaces.dateformat.DateFormatSymbols;
import org.apache.myfaces.dateformat.SimpleDateFormatter;
import org.bedework.calfacade.BwCalendar;
import org.bedework.calfacade.BwDateTime;
import org.bedework.calfacade.BwEvent;
import org.bedework.calfacade.BwEventObj;
import org.bedework.calfacade.BwRecurrenceInstance;
import org.bedework.calfacade.RecurringRetrievalMode;
import org.bedework.calfacade.exc.CalFacadeException;
import org.bedework.calfacade.svc.EventInfo;
import org.bedework.calsvci.EventsI;
import org.springframework.beans.factory.annotation.Autowired;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.idega.bedework.BedeworkConstants;
import com.idega.bedework.data.BedeworkCalendarEntry;
import com.idega.block.cal.business.CalBusiness;
import com.idega.block.cal.data.AttendanceEntity;
import com.idega.block.cal.data.AttendanceMark;
import com.idega.block.cal.data.CalendarEntry;
import com.idega.block.cal.data.CalendarEntryGroup;
import com.idega.block.cal.data.CalendarEntryType;
import com.idega.block.cal.data.CalendarLedger;
import com.idega.block.cal.presentation.CalendarEntryCreator;
import com.idega.business.IBOServiceBean;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;
import com.idega.util.expression.ELUtil;

/**
 * <p>Bedework apdapter for {@link CalBusiness}.</p>
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.com">Martynas Stakė</a></p>
 * <p>You can expect to find some test cases notice in the end of the file.</p>
 *
 * @version 1.0.0 Apr 19, 2012
 * @author martynasstake
 */
public class BwCalBussinessBean extends IBOServiceBean implements CalBusiness, UserGroupPlugInBusiness {

	private static final long serialVersionUID = 7960798838568619167L;
	private static final Logger LOGGER = Logger.getLogger(BwCalBussinessBean.class.getName());

	private static final SimpleDateFormatter DATE_FORMATTER = 
			new SimpleDateFormatter("yyyy-MM-dd hh:mm:ss.S", new DateFormatSymbols());
	
	@Override
	public CalendarEntry getEntry(int entryID) {
		BwAPI bwAPI = new BwAPI(null);
		
		EventsI eventsHandler = bwAPI.getEventsHandler();
		
		if (eventsHandler == null) {
			return null;
		}
		
		Collection<EventInfo> events = null;
		try {
			events = eventsHandler.get(null, String.valueOf(entryID), null, null, false);
		} catch (CalFacadeException e) {
			LOGGER.log(Level.INFO, "Event by ID: " + entryID + " not found.");
		}
		
		if (ListUtil.isEmpty(events)) {
			return null;
		}
		
		BwEvent desiredEvent = null;
		for (EventInfo ei : events) {
			BwEvent tmp = ei.getEvent();
			
			if (tmp != null && tmp.getId() == entryID) {
				desiredEvent = ei.getEvent();
			}
		}
		
		if (desiredEvent == null) {
			return null;
		}
		
		 return new BedeworkCalendarEntry(desiredEvent);
	}

	@Override
	public Collection<CalendarEntry> getEntriesByTimestamp(Timestamp stamp) {
		throw new NotImplementedException();
	}

	@Override
	public Collection<CalendarEntry> getEntriesBetweenTimestamps(Timestamp fromStamp,
			Timestamp toStamp) {
		throw new NotImplementedException();
	}

	@Override
	public Collection<CalendarEntry> getEntriesByLedgerID(int ledgerID) {
		throw new NotImplementedException();
	}

	@Override
	public List<CalendarEntry> getEntriesByLedgers(List<String> ledgersIds) {
		throw new NotImplementedException();
	}

	@Override
	public Collection<CalendarEntry> getEntriesByEntryGroupID(int entryGroupID) {
		throw new NotImplementedException();
	}

	@Override
	public Collection getPracticesByLedgerID(int ledgerID) {
		throw new NotImplementedException();
	}

	@Override
	public Collection getMarkedEntriesByUserIDandLedgerID(int userID,
			int ledgerID) {
		throw new NotImplementedException();
	}

	@Override
	public Collection getPracticesByLedIDandMonth(int ledgerID, int month,
			int year) {
		throw new NotImplementedException();
	}

	@Override
	public CalendarEntryGroup getEntryGroup(int entryGroupID) {
		throw new NotImplementedException();
	}

	@Override
	public Collection getEntryGroupsByLedgerID(int ledgerID) {
		throw new NotImplementedException();
	}

	@Override
	public CalendarEntryType getEntryTypeByName(String entryTypeName) {
		throw new NotImplementedException();
	}

	@Override
	public List getAllEntryTypes() {
		throw new NotImplementedException();
	}

	@Override
	public CalendarLedger getLedger(int ledgerID) {
		throw new NotImplementedException();
	}

	@Override
	public int getLedgerIDByName(String name) {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.business.CalBusiness#getAllLedgers()
	 */
	@Override
	public List<CalendarLedger> getAllLedgers() {
		return java.util.Collections.emptyList();
	}

	@Override
	public AttendanceEntity getAttendanceEntity(int attendanceID) {
		throw new NotImplementedException();
	}

	@Override
	public AttendanceEntity getAttendanceByUserIDandEntry(int userID,
			CalendarEntry entry) {
		throw new NotImplementedException();
	}

	@Override
	public List getAttendancesByLedgerID(int ledgerID) {
		throw new NotImplementedException();
	}

	@Override
	public int getNumberOfPractices(int ledgerID) {
		throw new NotImplementedException();
	}

	@Override
	public List getAttendanceMarks(int userID, int ledgerID, String mark) {
		throw new NotImplementedException();
	}

	@Override
	public List getAllMarks() {
		throw new NotImplementedException();
	}

	@Override
	public AttendanceMark getMark(int markID) {
		throw new NotImplementedException();
	}

	@Override
	public void deleteEntry(int entryID) {
		throw new NotImplementedException();
	}

	@Override
	public void deleteEntryGroup(int entryGroupID) {
		throw new NotImplementedException();
	}

	@Override
	public void deleteEntryGroupByEntryID(int entryID) {
		throw new NotImplementedException();
	}

	@Override
	public void deleteLedger(int ledgerID) {
		throw new NotImplementedException();
	}

	@Override
	public void deleteUserFromLedger(int userID, int ledgerID, IWContext iwc) {
		throw new NotImplementedException();
	}

	@Override
	public void deleteMark(int markID) {
		throw new NotImplementedException();
	}

	@Override
	public boolean createNewEntryType(String typeName) {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.business.CalBusiness#createNewEntry(
	 * 		java.lang.String, 
	 * 		com.idega.user.data.User, 
	 * 		java.lang.String, 
	 * 		java.lang.String,
	 * 		java.lang.String, 
	 * 		java.lang.String, 
	 * 		java.lang.String, 
	 * 		java.lang.String, 
	 * 		java.lang.String, 
	 * 		java.lang.String, 
	 * 		java.lang.String, 
	 * 		java.lang.String, 
	 * 		java.lang.String, 
	 * 		java.lang.String
	 * )
	 */
	@Override
	public void createNewEntry(String headline, User user, String type,
			String repeat, String startDate, String startHour,
			String startMinute, String endDate, String endHour,
			String endMinute, String attendees, String ledger,
			String description, String location) {
		
		boolean reccur = Boolean.FALSE;
		if (StringUtil.isEmpty(repeat)) {
			reccur = Boolean.FALSE;
		} else {
			reccur = Boolean.valueOf(repeat);
		}
		
		java.util.Date begining = null;
		if (!StringUtil.isEmpty(startDate)) {
			begining = DATE_FORMATTER.parse(startDate);
			
			if (begining != null) {
				if (!StringUtil.isEmpty(startHour)) {
					try {
						long time = begining.getTime();
						time = time + Integer.parseInt(startHour)*60*60*1000; //From hour to milliseconds
						begining.setTime(time);
					} catch (NumberFormatException e) {
						LOGGER.log(Level.WARNING, "Unrecognizable number of hours: " + startHour);
					}
				}
				
				if (!StringUtil.isEmpty(startMinute)) {
					try {
						long time = begining.getTime();
						time = time + Integer.parseInt(startMinute)*60*1000; //From minute to milliseconds
						begining.setTime(time);
					} catch (NumberFormatException e) {
						LOGGER.log(Level.WARNING, "Unrecognizable number of minutes: " + startMinute);
					}
				}
			}
		}
		
		java.util.Date ending = null;
		if (!StringUtil.isEmpty(endDate)) {
			ending = DATE_FORMATTER.parse(endDate);
			
			if (ending != null) {
				if (!StringUtil.isEmpty(endHour)) {
					try {
						long time = ending.getTime();
						time = time + Integer.parseInt(endHour)*60*60*1000; //From hour to milliseconds
						ending.setTime(time);
					} catch (NumberFormatException e) {
						LOGGER.log(Level.WARNING, "Unrecognizable number of hours: " +endHour);
					}
				}
				
				if (!StringUtil.isEmpty(endMinute)) {
					try {
						long time = ending.getTime();
						time = time + Integer.parseInt(endMinute)*60*1000; //From minute to milliseconds
						ending.setTime(time);
					} catch (NumberFormatException e) {
						LOGGER.log(Level.WARNING, "Unrecognizable number of minutes: " + endMinute);
					}
				}
			}
		}

		createNewBedeworkCalDAVEvent(user, headline, description, location, type, "kitas4", reccur, begining, ending, null);
	}
	
	@Autowired
	private BedeworkCalendarManagementService bcms;
	
	private BedeworkCalendarManagementService getBedeworkCalendarManagementService() {
		if (this.bcms == null) {
			ELUtil.getInstance().autowire(this);
		}

		return this.bcms;
	}
	
	/**
	 * <p>Creates CalDAV event to Bedework side of system.</p>
	 * @param user - the one, who creates. Should not be <code>null</code>.
	 * @param headline - Name, headline of event. Should not be <code>null</code>.
	 * @param description - Description of event.
	 * @param location - Location of event.
	 * @param type - ?
	 * @param calendarName - Name of calendar, where event should be placed. If 
	 * <code>null</code>, then event will be placed on "default_userID" calendar. If 
	 * calendar name was incorrect or not found, <code>false</code> will be returned.
	 * @param reccuring - <code>true</code>, if events recur, <code>false</code> otherwise. 
	 * @param startDate - {@link java.util.Date}, when event first occur. If set to 
	 * <code>null</code>, then today will be used as event start.
	 * @param endDate - {@link java.util.Date}, when event stops. Should not be 
	 * <code>null</code>.
	 * @param attendees - {@link List} of {@link User}s, who should see this event.
	 * @return <code>true</code> if event successfully created, <code>false</code>
	 * otherwise.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public boolean createNewBedeworkCalDAVEvent(
			User user, 
			String headline,
			String description, 
			String location,
			String type,
			String calendarName,
			boolean reccuring, 
			java.util.Date startDate, 
			java.util.Date endDate, 
			List<User> attendees) {
		
		
		if (user == null) {
			return Boolean.FALSE;
		}

		if (StringUtil.isEmpty(calendarName)) {
			if (getBedeworkCalendarManagementService().getUserCalendar(user, 
					BedeworkConstants.BW_USER_CALENDAR_DEFAULT+user.getId()) == null) {
				if (!getBedeworkCalendarManagementService().createCalendar(user, 
						BedeworkConstants.BW_USER_CALENDAR_DEFAULT+user.getId())) {
					LOGGER.log(Level.WARNING, "Unable to create default calendar for user.");
					return Boolean.FALSE;
				}
			}
		}
		
		BwCalendar calendar = getBedeworkCalendarManagementService().getUserCalendar(user, calendarName);
		if (calendar == null) {
			return Boolean.FALSE;
		}
		
		BwAPI bwAPI = new BwAPI(user);
		if (!bwAPI.openBedeworkAPI()) {
			return Boolean.FALSE;
		}
		
		EventsI eventsHandler = bwAPI.getEventsHandler();
		if (eventsHandler == null) {
			return Boolean.FALSE;
		}
		
		UserAdapter userAdapter = new UserAdapter(user);

		BwEvent event = new BwEventObj();
		event.setName(headline);
		event.setDescription(description);
		
		event.setCreatorEnt(userAdapter.getBedeworkSystemUser());
		event.setColPath(calendar.getPath());
		
		event.setCreatorHref(userAdapter.getBedeworkSystemUser().getPrincipalRef());
		event.setOwnerHref(userAdapter.getBedeworkSystemUser().getPrincipalRef());
		
		event.setDuration("0");
		if (reccuring) {
			BwRecurrenceInstance bwRecurrenceInstance = new BwRecurrenceInstance();
			bwRecurrenceInstance.setDtstart(null);
			bwRecurrenceInstance.setRecurrenceId(bwRecurrenceInstance.getDtstart().getDate());
			bwRecurrenceInstance.setMaster(event);
			
			event.setRecurring(reccuring);
			event.setRecurrenceId(null);
		}

		event.setNoStart(Boolean.FALSE);
		event.setAccess(userAdapter.getBedeworkSystemUser().getLocationAccess());
		
		if (startDate != null) {
			event.setDtstart(getDateInBedeworkFormat(startDate));
		} else {
			event.setDtstart(getDateInBedeworkFormat(
					new Timestamp(System.currentTimeMillis())));
		}
		
		if (endDate != null) {
			event.setDtend(getDateInBedeworkFormat(endDate));
		} else {
			return Boolean.FALSE;
		}
		
		EventInfo ei = new EventInfo(event);
		try {
			eventsHandler.add(ei, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);
		} catch (CalFacadeException e) {
			LOGGER.log(Level.WARNING, "Failed to add event", e);
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}
	
	/**
	 * <p>Adds recurrence for event for certain period. Returns recurrence id by which
	 * this recurrence should be recognized.
	 * .</p>
	 * @param event - {@link BwEvent} which needs recurrence.
	 * @param period - Some period for recurrence. Possible periods are:
	 * <li>{@link CalendarEntryCreator#dailyFieldParameterName}</li>
	 * <li>{@link CalendarEntryCreator#weeklyFieldParameterName}</li>
	 * <li>{@link CalendarEntryCreator#monthlyFieldParameterName}</li>
	 * <li>{@link CalendarEntryCreator#yearlyFieldParameterName}</li>
 	 * @return {@link BwEvent#getRecurrenceId()} or <code>null</code> on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public String createRecurrenceForEvent(BwEvent event, String period) {
		
		return event.getRecurrenceId();
	}

		
	@Override
	public void updateEntry(int entryID, String headline, User user,
			String type, String repeat, String startDate, String startHour,
			String startMinute, String endDate, String endHour,
			String endMinute, String attendees, String ledger,
			String description, String location, String oneOrMany) {
		throw new NotImplementedException();
	}

	@Override
	public void createNewLedger(String name, int groupID, String coachName,
			String date, int coachGroupID) {
		throw new NotImplementedException();
	}

	@Override
	public void createNewMark(int markID, String markName, String description) {
		throw new NotImplementedException();
	}

	@Override
	public Collection getUsersInLedger(int ledgerID) {
		throw new NotImplementedException();
	}

	@Override
	public void saveAttendance(int userID, int ledgerID, CalendarEntry entry,
			String mark) {
		throw new NotImplementedException();
	}

	@Override
	public void updateAttendance(int attendanceID, int userID, int ledgerID,
			CalendarEntry entry, String mark) {
		throw new NotImplementedException();
	}

	@Override
	public void deleteEntryType(int typeID) {
		throw new NotImplementedException();
	}

	@Override
	public boolean deleteBlock(int iObjectInstanceId) {
		throw new NotImplementedException();
	}

	@Override
	public GroupBusiness getGroupBusiness(IWApplicationContext iwc) {
		throw new NotImplementedException();
	}

	@Override
	public void beforeUserRemove(User user, Group parentGroup)
			throws RemoveException, RemoteException {
		throw new NotImplementedException();
	}

	@Override
	public void afterUserCreateOrUpdate(User user, Group parentGroup)
			throws CreateException, RemoteException {
		throw new NotImplementedException();
	}

	@Override
	public void beforeGroupRemove(Group group, Group parentGroup)
			throws RemoveException, RemoteException {
		throw new NotImplementedException();
	}

	@Override
	public void afterGroupCreateOrUpdate(Group group, Group parentGroup)
			throws CreateException, RemoteException {
		throw new NotImplementedException();
	}

	@Override
	public PresentationObject instanciateEditor(Group group)
			throws RemoteException {
		throw new NotImplementedException();
	}

	@Override
	public PresentationObject instanciateViewer(Group group)
			throws RemoteException {
		throw new NotImplementedException();
	}

	@Override
	public List getUserPropertiesTabs(User user) throws RemoteException {
		throw new NotImplementedException();
	}

	@Override
	public List getGroupPropertiesTabs(Group group) throws RemoteException {
		throw new NotImplementedException();
	}

	@Override
	public List getMainToolbarElements() throws RemoteException {
		throw new NotImplementedException();
	}

	@Override
	public List getGroupToolbarElements(Group group) throws RemoteException {
		throw new NotImplementedException();
	}

	@Override
	public String isUserAssignableFromGroupToGroup(User user,
			Group sourceGroup, Group targetGroup) {
		throw new NotImplementedException();
	}

	@Override
	public String isUserSuitedForGroup(User user, Group targetGroup) {
		throw new NotImplementedException();
	}

	@Override
	public String canCreateSubGroup(Group group, String groupTypeOfSubGroup)
			throws RemoteException {
		throw new NotImplementedException();
	}

	@Override
	public Collection getEntriesByICGroup(int groupId) {
		throw new NotImplementedException();
	}

	@Override
	public Collection getEntriesByEvents(List eventsList) {
		throw new NotImplementedException();
	}

	@Override
	public List getLedgersByGroupId(String groupId) {
		throw new NotImplementedException();
	}

	@Override
	public List getEntriesByLedgersAndEntryTypes(
			List<String> listOfEntryTypesIds, List<String> listOfLedgerIds) {
		throw new NotImplementedException();
	}

	@Override
	public List<CalendarEntry> getEntriesByEventsIds(List<String> eventsIds) {
		throw new NotImplementedException();
	}

	@Override
	public List<CalendarEntry> getEntriesByEventsIdsAndGroupsIds(
			List<String> eventsIds, List<String> groupsIds) {
		throw new NotImplementedException();
	}

	@Override
	public List<CalendarEntry> getEntriesByLedgersIdsAndGroupsIds(
			List<String> ledgersIds, List<String> groupsIds) {
		throw new NotImplementedException();
	}

	@Override
	public List<CalendarLedger> getUserLedgers(User user, IWContext iwc) {
		throw new NotImplementedException();
	}

	@Override
	public List<CalendarLedger> getUserLedgers(String userId, IWContext iwc) {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.business.CalBusiness#getUserEntriesBetweenTimestamps(
	 * 		com.idega.user.data.User, 
	 * 		java.sql.Timestamp, 
	 * 		java.sql.Timestamp, 
	 * 		com.idega.presentation.IWContext
	 * )
	 */
	@Override
	public List<CalendarEntry> getUserEntriesBetweenTimestamps(User user,
			Timestamp fromStamp, Timestamp toStamp, IWContext iwc) {
		BwAPI bwAPI = new BwAPI(user);
		
		org.bedework.calsvci.EventsI eventsHandler = bwAPI.getEventsHandler();
		if (eventsHandler == null) {
			return java.util.Collections.emptyList();
		}
		
		BwDateTime bwStartDate = getDateInBedeworkFormat(fromStamp);
		BwDateTime bwEndDate = getDateInBedeworkFormat(toStamp);
		
		org.bedework.calfacade.RecurringRetrievalMode rrm = new RecurringRetrievalMode();
		
		Collection<EventInfo> bedeworkEventsInfo = null;
		try {
			bedeworkEventsInfo = eventsHandler.getEvents(null, null, bwStartDate, 
					bwEndDate, null, rrm);
		} catch (CalFacadeException e) {
			LOGGER.log(Level.WARNING, "Unable to get Collection<EventsInfo>: ", e);
			return java.util.Collections.emptyList();
		}
		
		if (ListUtil.isEmpty(bedeworkEventsInfo)) {
			return java.util.Collections.emptyList();
		}
		
		List<CalendarEntry> idegaCalendarEntries = new ArrayList<CalendarEntry>(
				bedeworkEventsInfo.size()
		);
		
		for (EventInfo ei : bedeworkEventsInfo) {
			idegaCalendarEntries.add(new BedeworkCalendarEntry(ei.getEvent()));
		}
		
		return idegaCalendarEntries;
	}
	
	/**
	 * <p>Method for converting date from {@link java.sql.Timestamp} to 
	 * {@link org.bedework.calfacade.BwDateTime}.</p>
	 * @param date to convert.
	 * @return Converted date or <code>null</code> on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public org.bedework.calfacade.BwDateTime getDateInBedeworkFormat(Timestamp date) {
		net.fortuna.ical4j.model.DateTime ical4jDate = new net.fortuna.ical4j.model.DateTime(
				date.getTime());
		
		BwDateTime bwDate = null;
		try {
			bwDate = BwDateTime.makeBwDateTime(ical4jDate);
		} catch (CalFacadeException e1) {
			LOGGER.log(Level.WARNING, "Unable to convert net.fortuna.ical4j.model.Date to " +
					"org.bedework.calfacade.BwDateTime");
			return null;
		}
		
		return bwDate;
	}
	
	/**
	 * <p>Method for converting date from {@link java.util.Date} to 
	 * {@link org.bedework.calfacade.BwDateTime}.</p>
	 * @param date to convert.
	 * @return Converted date or <code>null</code> on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public org.bedework.calfacade.BwDateTime getDateInBedeworkFormat(java.util.Date date) {
		return getDateInBedeworkFormat(new Timestamp(date.getTime())); 
	}
}
