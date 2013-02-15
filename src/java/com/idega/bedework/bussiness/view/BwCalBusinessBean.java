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
 *     Licensee shall also mean the individual using or installing 
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
package com.idega.bedework.bussiness.view;

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

import com.idega.bedework.bussiness.BedeworkCalendarsService;
import com.idega.bedework.bussiness.BedeworkDateService;
import com.idega.bedework.bussiness.BedeworkEventsService;
import com.idega.bedework.bussiness.BedeworkAPI;
import com.idega.bedework.bussiness.UserAdapter;
import com.idega.bedework.data.BedeworkCalendarEntry;
import com.idega.block.cal.business.CalBusiness;
import com.idega.block.cal.business.CalBusinessBean;
import com.idega.block.cal.data.AttendanceEntity;
import com.idega.block.cal.data.AttendanceMark;
import com.idega.block.cal.data.CalendarEntry;
import com.idega.block.cal.data.CalendarEntryGroup;
import com.idega.block.cal.data.CalendarEntryType;
import com.idega.block.cal.data.CalendarLedger;
import com.idega.block.cal.presentation.CalendarEntryCreator;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWMainApplicationSettings;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;
import com.idega.util.expression.ELUtil;

import edu.rpi.cmt.calendar.IcalDefs;


/**
 * <p>Bedework apdapter for {@link CalBusiness}.</p>
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.com">Martynas Stakė</a></p>
 * <p>You can expect to find some test cases notice in the end of the file.</p>
 *
 * @version 1.0.0 Apr 19, 2012
 * @author martynasstake
 */
public class BwCalBusinessBean extends CalBusinessBean implements BwCalBusiness {

	private static final long serialVersionUID = 7960798838568619167L;
	private static final Logger LOGGER = Logger.getLogger(
			BwCalBusinessBean.class.getName());
	
	private BwCalendar currentCalendar = null;
	
	/**
	 * @return the currentCalendar
	 */
	public BwCalendar getCurrentCalendar() {
		return currentCalendar;
	}

	/**
	 * @param currentCalendar the currentCalendar to set
	 */
	public void setCurrentCalendar(BwCalendar currentCalendar) {
		this.currentCalendar = currentCalendar;
	}
	
	@Override
	public CalendarEntry getEntry(int entryID) {
		EventInfo ei = getBedeworkEventsService()
				.getEvent(entryID, getCurrentUser(), null, null, null, null, 
						null, null);
		
		return new BedeworkCalendarEntry(ei.getEvent());
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
		getBedeworkEventsService().deleteEvent(getCurrentUser(), entryID);
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
	
	public static final String CALENDAR_ENTRY_TYPES_APP_PROP = 
			"calendar_entry_types_reloaded";

	public boolean recreateCalendarEntryTypes() {
		IWMainApplicationSettings settings = getIWMainApplication().getSettings();
		if (settings == null || settings.getBoolean(CALENDAR_ENTRY_TYPES_APP_PROP)) {
			return Boolean.TRUE;
		}
		
		List<CalendarEntryType> types = getAllEntryTypes();
		if (!ListUtil.isEmpty(types)) {
			for (CalendarEntryType type : types) {
				deleteEntryType(Integer.valueOf(type.getId()));
			}
		}
		
		for (String name : IcalDefs.entityTypeNames) {
			if (!createNewEntryType(name)) {
				return Boolean.FALSE;
			}
		}
		
		settings.setProperty(CALENDAR_ENTRY_TYPES_APP_PROP, Boolean.TRUE.toString());
		return settings.getBoolean(CALENDAR_ENTRY_TYPES_APP_PROP);
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

		getBedeworkEventsService().createEvent(
				user, headline, description, location, type, 
				getCurrentCalendar(), reccur, startDate, startHour, startMinute, 
				endDate, endHour, endMinute, null);
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
		
		boolean reccur = Boolean.FALSE;
		if (StringUtil.isEmpty(repeat)) {
			reccur = Boolean.FALSE;
		} else {
			reccur = Boolean.valueOf(repeat);
		}
		
		getBedeworkEventsService().updateEvent(entryID, headline, 
				user, type, reccur, startDate, startHour, 
				startMinute, endDate, endHour, endMinute, attendees, ledger, 
				description, location, oneOrMany);
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
		if (user == null) {
			return null;
		}
		
		Collection<EventInfo> bedeworkEventsInfo = getBedeworkEventsService()
				.getEvents(user, getCurrentCalendar(), null, 
						getBedeworkDateService().convertDate(fromStamp), 
						getBedeworkDateService().convertDate(toStamp), 
						null, null);
		
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
	
	@Autowired
	private BedeworkDateService bts;
	
	private BedeworkDateService getBedeworkDateService() {
		if (this.bts == null) {
			ELUtil.getInstance().autowire(this);
		}

		return this.bts;
	}
	
	@Autowired
	private BedeworkEventsService bes;
	
	private BedeworkEventsService getBedeworkEventsService() {
		if (this.bes == null) {
			ELUtil.getInstance().autowire(this);
		}

		return this.bes;
	}
}
