/**
 * @(#)BedeworkEventsServiceBean.java    1.0.0 10:49:06 AM
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
package com.idega.bedework.bussiness.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import org.bedework.caldav.util.filter.FilterBase;
import org.bedework.calfacade.BwCalendar;
import org.bedework.calfacade.BwDateTime;
import org.bedework.calfacade.BwEvent;
import org.bedework.calfacade.BwEventObj;
import org.bedework.calfacade.BwRecurrenceInstance;
import org.bedework.calfacade.RecurringRetrievalMode;
import org.bedework.calfacade.exc.CalFacadeException;
import org.bedework.calfacade.svc.EventInfo;
import org.bedework.calsvci.EventsI;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.bedework.bussiness.BedeworkCalendarsService;
import com.idega.bedework.bussiness.BedeworkDateService;
import com.idega.bedework.bussiness.BedeworkEventsService;
import com.idega.bedework.bussiness.BedeworkLocationService;
import com.idega.bedework.bussiness.BedeworkAPI;
import com.idega.bedework.bussiness.UserAdapter;
import com.idega.core.business.DefaultSpringBean;
import com.idega.hibernate.SessionFactoryUtil;
import com.idega.user.data.User;
import com.idega.util.CoreConstants;
import com.idega.util.ListUtil;
import com.idega.util.expression.ELUtil;

/**
 * Class description goes here.
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.com">Martynas Stakė</a></p>
 * <p>You can expect to find some test cases notice in the end of the file.</p>
 *
 * @version 1.0.0 Sep 29, 2012
 * @author martynasstake
 */
@Service("bedeworkEventsService")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class BedeworkEventsServiceBean extends DefaultSpringBean implements BedeworkEventsService {

	@Override
	public EventInfo getEvent(int ID, User user, BwCalendar directory, 
			FilterBase filter, BwDateTime start, BwDateTime end, 
			List<String> properties, 
			RecurringRetrievalMode recurringRetrievalMode) {
		
		if (user == null) {
			return null;
		}
		
		Collection<EventInfo> events = getEvents(user, directory, filter, start, 
				end, properties, recurringRetrievalMode);
		if (ListUtil.isEmpty(events)) {
			return null;
		}
		
		for (EventInfo ei : events) {
			if (ei.getEvent().getId() == ID) {
				return ei;
			}
		}
		
		return null;
	}
	
	@Override
	public BwEvent setEvent(BwEvent event, User user, String headline,
			String description, String location, String type, 
			BwCalendar calendar, boolean reccuring, Date startDate, 
			Date endDate, List<User> attendees) {
//		
//		return setEvent(event, user, headline, description, 
//				getBedeworkLocationService().getOrCreateLocation(user, location), type, 
//				calendar, reccuring, startDate, endDate, attendees);
//	}
	
//	@Override
//	public BwEvent setEvent(BwEvent event, User user, String headline,
//			String description, BwLocation location, String type, 
//			BwCalendar calendar, boolean reccuring, Date startDate, 
//			Date endDate, List<User> attendees) {
		
		if (user == null || calendar == null) {
			return null;
		}
		
		UserAdapter userAdapter = new UserAdapter(user);
		
		if (event == null) {
			event = new BwEventObj();
		}
		
		// User info:
		event.setCreatorHref(userAdapter.getBedeworkSystemUser().getPrincipalRef());
		event.setOwnerHref(userAdapter.getBedeworkSystemUser().getPrincipalRef());
		event.setCreatorEnt(userAdapter.getBedeworkSystemUser());
		event.setAccess(userAdapter.getBedeworkSystemUser().getLocationAccess());
		
		// Calendar info:
		event.setColPath(calendar.getPath());
//		event.setPublick(calendar.getPublick());
		event.setPublick(Boolean.TRUE);

		// Date info:
		if (startDate != null) {
			event.setDtstart(getBedeworkDateService().convertDate(startDate));
		}
		
		if (endDate != null) {
			event.setDtend(getBedeworkDateService().convertDate(endDate));
		}
		
		// Event info:
		event.setName(headline);
		event.setDescription(description);
		event.setSummary(location);
//		event.setLocation(location);
		
		// WTF????
		event.setNoStart(Boolean.FALSE);
		event.setDuration("0");
		
		// Recurrence:
		event.setRecurring(reccuring);
		if (reccuring) {
			BwRecurrenceInstance bwRecurrenceInstance = new BwRecurrenceInstance();
			bwRecurrenceInstance.setDtstart(null);
			bwRecurrenceInstance.setRecurrenceId(bwRecurrenceInstance.getDtstart().getDate());
			bwRecurrenceInstance.setMaster(event);
			
			event.setRecurring(reccuring);
			event.setRecurrenceId(null);
		}
		
		return event;
	}
	
	@Override
	public boolean createEvent(User user, String headline,
			String description, String location, String type, 
			BwCalendar calendar, boolean reccuring, Date startDate, 
			Date endDate, List<User> attendees) {
		return createEvent(user, calendar, setEvent(null, user, headline, 
				description, location, type, calendar, reccuring, startDate, 
				endDate, attendees));
	}
	
	@Override
	public boolean createEvent(User user, BwCalendar calendar, BwEvent event) {
    	if (user == null || calendar == null || event == null) {
			return Boolean.FALSE;
		}

		BedeworkAPI bwAPI = new BedeworkAPI(user);
		EventsI eventsHandler = bwAPI.getEventsHandler();
		if (eventsHandler == null) {
			return Boolean.FALSE;
		}
		
		EventInfo ei = new EventInfo(event);
		try {
			eventsHandler.add(ei, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);
		} catch (CalFacadeException e) {
			getLogger().log(Level.WARNING, "Failed to add event", e);
			return Boolean.FALSE;
		} finally {
			bwAPI.closeBedeworkAPI();
		}
		
		return Boolean.TRUE;
	}
	
	@Override
	public Collection<String> getNamesOfEvents(Collection<EventInfo> events) {
		if (ListUtil.isEmpty(events)) {
			return null;
		}
		
		ArrayList<String> names = new ArrayList<String>(events.size());
		for (EventInfo ei : events) {
			if (!names.add(ei.getEvent().getName())) {
				return null;
			}
		}
		
		return names;
	}
	
	@Override
	public Collection<EventInfo> getEvents(User user) {
		if (user == null) {
			return null;
		}
		
		return getEvents(user, null, null, null, null, null, null);
	}
	
	@Override
	public Collection<EventInfo> getEvents(User user, BwCalendar directory) {
		if (user == null || directory == null) {
			return null;
		}
		
		return getEvents(user, directory, null, null, null, null, null);
	}

	@Override
	public Collection<EventInfo> getEvents(User user, BwCalendar directory, 
			FilterBase filter, BwDateTime start, BwDateTime end, 
			List<String> properties, 
			RecurringRetrievalMode recurringRetrievalMode) {
		if (user == null) {
			return null;
		}
		
		BedeworkAPI bwAPI = new BedeworkAPI(user);
		EventsI eventsHandler = bwAPI.getEventsHandler();
		if (eventsHandler == null) {
			return null;
		}
		
		if (recurringRetrievalMode == null) {
			recurringRetrievalMode = new RecurringRetrievalMode();
		}
		
		try {
			return eventsHandler.getEvents(directory, filter, start, end, 
					properties, recurringRetrievalMode);
		} catch (CalFacadeException e) {
			getLogger().log(Level.INFO, "Unable to get events: ", e);
		} finally {
			bwAPI.closeBedeworkAPI();
		}
				
		return null;
	}

	@Override
	public boolean createEvent(User user, String headline, String description,
			String location, String type, BwCalendar calendar,
			boolean reccuring, String startDate, String startHour,
			String startMinute, String endDate, String endHour,
			String endMinute, List<User> attendees) {
		
		return createEvent(user, headline, description, location, type, 
				calendar, reccuring, 
				new Date(getBedeworkDateService().getTimeInMilliseconds(
						startDate, startHour, startMinute)), 
				new Date(getBedeworkDateService().getTimeInMilliseconds(
						endDate, endHour, endMinute)), attendees);
	}

	@Override
	public boolean updateEvent(int entryID, String headline, User user,
			String type, Boolean reccur, String startDate, String startHour,
			String startMinute, String endDate, String endHour,
			String endMinute, String attendees, String ledger,
			String description, String location, String oneOrMany) {
		
		EventInfo eventInfo = getEvent(entryID, user, null, null, null, null, 
				null, null);
		if (eventInfo == null) {
			getLogger().log(Level.WARNING, "Event does not exists, " +
					"try creating first.");
			return Boolean.FALSE;
		}
		
		BwEvent event = eventInfo.getEvent();
		String calendarPath = event.getColPath();
		String calendarName = calendarPath.substring(
				calendarPath.lastIndexOf(CoreConstants.SLASH) + 1);
		
		BwCalendar calendar = getBedeworkCalendarsService()
				.getCalendar(user, calendarName, null);
		
		eventInfo.setEvent(setEvent(event, user, headline, description, 
				location, type, calendar, reccur, 
				new Date(getBedeworkDateService().getTimeInMilliseconds(
						startDate, startHour, startMinute)), 
				new Date(getBedeworkDateService().getTimeInMilliseconds(
						endDate, endHour, endMinute)), 
				null));

		return updateEvent(user, eventInfo);
	}

	@Override
	public boolean updateEvent(User user, EventInfo event) {
		if (user == null || event == null) {
			return Boolean.FALSE;
		}

		BedeworkAPI bwAPI = new BedeworkAPI(user);
		EventsI eventsHandler = bwAPI.getEventsHandler();
		if (eventsHandler == null) {
			return Boolean.FALSE;
		}
		
		Session session = SessionFactoryUtil.getSession();
		if (session != null) {
			session.merge(event.getEvent());
		}
		
		try {
			eventsHandler.update(event, Boolean.FALSE);
		} catch (CalFacadeException e) {
			getLogger().log(Level.WARNING, "Failed to update event", e);
			return Boolean.FALSE;
		} finally {
			session.close();
			bwAPI.closeBedeworkAPI();
		}
		
		return Boolean.TRUE;
	}
	
	@Override
	public boolean deleteEvent(User currentUser, int entryID) {
		if (currentUser == null) {
			return Boolean.FALSE;
		}

		EventInfo event = getEvent(entryID, currentUser, null, null, null, null, 
				null, null);
		if (event == null) {
			return Boolean.FALSE;
		}
		
		BedeworkAPI bwAPI = new BedeworkAPI(currentUser);
		EventsI eventsHandler = bwAPI.getEventsHandler();
		if (eventsHandler == null) {
			return Boolean.FALSE;
		}
		
		boolean isDeleted = Boolean.FALSE;
		try {
			isDeleted = eventsHandler.delete(event, Boolean.FALSE);
		} catch (CalFacadeException e) {
			getLogger().log(Level.WARNING, "Unable to delete event: ", e);
		} 
		
		return bwAPI.closeBedeworkAPI() && isDeleted;
	}
	
	@Autowired
	private BedeworkCalendarsService bes;
	
	private BedeworkCalendarsService getBedeworkCalendarsService() {
		if (this.bes == null) {
			ELUtil.getInstance().autowire(this);
		}

		return this.bes;
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
	private BedeworkLocationService bls;
	
	private BedeworkLocationService getBedeworkLocationService() {
		if (this.bls == null) {
			ELUtil.getInstance().autowire(this);
		}

		return this.bls;
	}
}
