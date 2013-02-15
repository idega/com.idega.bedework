/**
 * @(#)BedeworkCalendarEntry.java    1.0.0 11:24:11 AM
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
package com.idega.bedework.data;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJBException;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.RemoveException;

import org.bedework.calfacade.BwAttendee;
import org.bedework.calfacade.BwDateTime;
import org.bedework.calfacade.BwEvent;
import org.bedework.calfacade.BwLocation;
import org.bedework.calfacade.BwPrincipal;
import org.bedework.calfacade.BwString;
import org.bedework.calfacade.BwUser;
import org.bedework.calfacade.exc.CalFacadeException;
import org.bedework.calfacade.svc.EventInfo;
import org.bedework.calsvci.EventsI;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.idega.bedework.bussiness.BedeworkEventsService;
import com.idega.bedework.bussiness.BedeworkAPI;
import com.idega.bedework.bussiness.UserAdapter;
import com.idega.block.cal.data.CalendarEntry;
import com.idega.data.IDOEntity;
import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOStoreException;
import com.idega.hibernate.SessionFactoryUtil;
import com.idega.ical4j.ICal4JConstants;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.util.CoreUtil;
import com.idega.util.ListUtil;
import com.idega.util.expression.ELUtil;

import edu.rpi.cmt.calendar.IcalDefs;

/**
 * <p>Adapter between {@link BwEvent} in Bedework and {@link CalendarEntry} in ePlatform.</p>
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.com">Martynas Stakė</a></p>
 * <p>You can expect to find some test cases notice in the end of the file.</p>
 *
 * @version 1.0.0 Apr 19, 2012
 * @author martynasstake
 */
public class BedeworkCalendarEntry implements CalendarEntry{

	@Autowired
	private BedeworkEventsService bes;
	
	private BedeworkEventsService getBedeworkEventsService() {
		if (this.bes == null) {
			ELUtil.getInstance().autowire(this);
		}

		return this.bes;
	}
	
	private BwEvent entry = null;
	private Integer entryID = null;
	
	private BwEvent getEntry() {
		EventInfo ei = getBedeworkEventsService().getEvent(this.entryID,
				CoreUtil.getIWContext().getCurrentUser(), null, null, null, 
				null, null, null);
		
		if (ei == null) {
			return null;
		}
		
		return ei.getEvent();
	}
	
	private static final Logger LOGGER = Logger.getLogger(BedeworkCalendarEntry.class.getName());
	
	public BedeworkCalendarEntry(BwEvent entry) {
		this.entry = entry;
		this.entryID = entry.getId();
	}
	
	@Override
	public void store() throws IDOStoreException {
		throw new NotImplementedException();
	}

	@Override
	public IDOEntityDefinition getEntityDefinition() {
		throw new NotImplementedException();
	}

	@Override
	public Integer decode(String pkString) {
		throw new NotImplementedException();
	}

	@Override
	public Collection<Integer> decode(String[] pkString) {
		throw new NotImplementedException();
	}

	@Override
	public String getDatasource() {
		throw new NotImplementedException();
	}

	@Override
	public void setDatasource(String datasource) {
		throw new NotImplementedException();
	}

	@Override
	public EJBLocalHome getEJBLocalHome() throws EJBException {
		throw new NotImplementedException();
	}

	@Override
	public Object getPrimaryKey() throws EJBException {
		if (getEntry() == null) {
			return null;
		}
		
		return getEntry().getId();
	}

	@Override
	public boolean isIdentical(EJBLocalObject arg0) throws EJBException {
		throw new NotImplementedException();
	}

	@Override
	public void remove() throws RemoveException, EJBException {
		BwUser user = getCreator();
		if (user == null) {
			LOGGER.log(Level.WARNING, "Unable to get event owner");
			return;
		}
		
		UserAdapter userAdapter = new UserAdapter(user);
		
		BedeworkAPI bwAPI = new BedeworkAPI(userAdapter.getIdegaSystemUser());
		if (!bwAPI.openBedeworkAPI()) {
			LOGGER.log(Level.WARNING, "Unable to get API.");
			return;
		}
		
		EventsI eventsHandler = bwAPI.getEventsHandler();
		if (eventsHandler == null) {
			LOGGER.log(Level.WARNING, "Unable to get events handler.");
			bwAPI.closeBedeworkAPI();
			return;
		}
		
		EventInfo ei = new EventInfo(getEntry());
		try {
			eventsHandler.delete(ei, Boolean.TRUE);
		} catch (CalFacadeException e) {
			LOGGER.log(Level.WARNING, "Failed to delete event: " + ei);
		}
		
		bwAPI.closeBedeworkAPI();
		return;
	}
	
	private BwUser getCreator() {
		if (this.getEntry() == null) {
			return null;
		}
		
		BwPrincipal principal = this.getEntry().getCreatorEnt();
		BwUser user = null;
		if (principal instanceof BwUser) {
			user = (BwUser) principal;
		}
		
		return user;
	}

	@Override
	public int compareTo(IDOEntity o) {
		throw new NotImplementedException();
	}

	@Override
	public int getEntryID() {
		if (getEntry() == null) {
			return -1;
		}
		
		return getEntry().getId();
	}

	@Override
	public Timestamp getDate() {
		if (getEntry() == null) {
			return null;
		}
		
		BwDateTime startDate = getEntry().getDtstart();
		if (startDate == null) {
			return null;
		}
		
		Date date = null;
		try {
			date = startDate.makeDate();
		} catch (CalFacadeException e) {
			LOGGER.log(Level.WARNING, "Failed to construct Date object from BwDateTime");
			return null;
		}
		
		if (date == null) {
			return null;
		}
		
		return new Timestamp(date.getTime());
	}

	@Override
	public int getDay() {
		if (getEntry() == null) {
			return -1;
		}
		
		Timestamp date = getDate();
		if (date == null){
			return -1;
		}
		
		return date.getDay();
	}

	@Override
	public String getDescription() {
		if (getEntry() == null) {
			return null;
		}
		
		Session session = SessionFactoryUtil.getSession();
		if (session == null) {
			return null;
		}
		
		session.merge(getEntry());
		String description = getEntry().getDescription();
		session.close();
		return description;
	}

	@Override
	public String getLocation() {
		if (getEntry() == null) {
			return null;
		}
		
		return getEntry().getSummary();
//		BwLocation location = getEntry().getLocation();
//		if (location == null) {
//			return null;
//		}
//		
//		BwString address = location.getAddress();
//		if (address == null) {
//			return null;
//		}
//		
//		return address.getValue();
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.data.CalendarEntry#getEndDate()
	 */
	@Override
	public Timestamp getEndDate() {
		if (getEntry() == null) {
			return null;
		}
		
		BwDateTime eventEnd = getEntry().getDtend();
		if (eventEnd == null) {
			return null;
		}
		
		Date date = null;
		try {
			date = eventEnd.makeDate();
		} catch (CalFacadeException e) {
			LOGGER.log(Level.WARNING, "Unable to get date from current entry: ", e);
			e.printStackTrace();
		}
		
		if (date == null) {
			return null;
		}
		return new Timestamp(date.getTime());
	}

	@Override
	public String getEntryType() {
		throw new NotImplementedException();
	}

	@Override
	public String getEntryTypeName() {
		return IcalDefs.entityTypeNames[getEntry().getEntityType()];
	}

	@Override
	public int getEntryTypeID() {
		throw new NotImplementedException();
	}

	@Override
	public int getGroupID() {	
		BwUser user = UserAdapter.getBedeworkSystemUserByID(getEntry().getCreatorHref());
		if (user == null) {
			return -1;
		}
		
		UserAdapter userAdapter = new UserAdapter(user);
		User idegaUser = userAdapter.getIdegaSystemUser();
		if (idegaUser == null) {
			return -1;
		}
		
		return idegaUser.getGroupID();
	}

	@Override
	public int getLedgerID() {
		return -1;
//		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.data.CalendarEntry#getName()
	 */
	@Override
	public String getName() {
		if (getEntry() == null) {
			return null;
		}
		
		return getEntry().getName();
	}

	@Override
	public String getRepeat() {
		if (getEntry().getRecurring()) {
			throw new NotImplementedException();
		} else {
			return "No Repeat";
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.data.CalendarEntry#getUserID()
	 */
	@Override
	public int getUserID() {
		if (getEntry() == null) {
			return -1;
		}
		
		BwPrincipal creator = UserAdapter.getBedeworkSystemUserByID(
				getEntry().getCreatorHref());

		if (creator == null) {
			return -1;
		}

		return Integer.valueOf(creator.getAccount());
	}

	@Override
	public Collection<User> getUsers() {
		if (getEntry() == null) {
			return null;
		}
		
		Set<BwAttendee> attendees = getEntry().getAttendees();
		if (ListUtil.isEmpty(attendees)) {
			return null;
		}
		
		throw new NotImplementedException();
	}

	@Override
	public int getEntryGroupID() {
		throw new NotImplementedException();
	}

	@Override
	public void setDate(Timestamp p0) {
		throw new NotImplementedException();		
	}

	@Override
	public void setDescription(String p0) {
		throw new NotImplementedException();		
	}

	@Override
	public void setLocation(String p0) {
		throw new NotImplementedException();		
	}

	@Override
	public void setEndDate(Timestamp p0) {
		throw new NotImplementedException();		
	}

	@Override
	public void setEntryType(String p0) {
		throw new NotImplementedException();		
	}

	@Override
	public void setEntryTypeID(int p0) {
		throw new NotImplementedException();		
	}

	@Override
	public void setGroupID(int p0) {
		throw new NotImplementedException();		
	}

	@Override
	public void setLedgerID(int p0) {
		throw new NotImplementedException();		
	}

	@Override
	public void setName(String p0) {
		throw new NotImplementedException();		
	}

	@Override
	public void setRepeat(String p0) {
		throw new NotImplementedException();		
	}

	@Override
	public void setUserID(int p0) {
		throw new NotImplementedException();		
	}

	@Override
	public void setEntryGroupID(int p0) {
		throw new NotImplementedException();		
	}

	@Override
	public Collection<CalendarEntry> getEntriesByEventsIds(
			List<String> eventsIds) {
		throw new NotImplementedException();
	}

	@Override
	public Collection<CalendarEntry> getEntriesByEventsIdsAndGroupsIds(
			List<String> eventsIds, List<String> groupsIds) {
		throw new NotImplementedException();
	}

	@Override
	public Collection<CalendarEntry> getEntriesByLedgersIdsAndGroupsIds(
			List<String> ledgersIds, List<String> groupsIds) {
		throw new NotImplementedException();
	}
}
