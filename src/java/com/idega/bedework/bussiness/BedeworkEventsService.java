/**
 * @(#)BedeworkEventsService.java    1.0.0 10:48:24 AM
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
package com.idega.bedework.bussiness;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.bedework.caldav.util.filter.FilterBase;
import org.bedework.calfacade.BwCalendar;
import org.bedework.calfacade.BwDateTime;
import org.bedework.calfacade.BwEvent;
import org.bedework.calfacade.BwLocation;
import org.bedework.calfacade.RecurringRetrievalMode;
import org.bedework.calfacade.svc.EventInfo;

import com.idega.user.data.User;

/**
 * Class description goes here.
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.com">Martynas Stakė</a></p>
 * <p>You can expect to find some test cases notice in the end of the file.</p>
 *
 * @version 1.0.0 Sep 29, 2012
 * @author martynasstake
 */
public interface BedeworkEventsService {

	/**
	 * @param user which can read calendar. Not <code>null</code>.
	 * @param directory which holds {@link EventInfo}s, not <code>null</code>.
	 * @return {@link Collection} of {@link EventInfo}s or <code>null</code>
	 * on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public Collection<EventInfo> getEvents(User user, BwCalendar directory);

	/**
	 * <p>Return the events for the current user within the given date and time 
	 * range. If retrieveList is supplied only those fields (and a few required 
	 * fields) will be returned.</p>
	 * @param user not <code>null</code>
	 * @return {@link Collection} of {@link EventInfo} or <code>null</code>
	 * on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public Collection<EventInfo> getEvents(User user);

	/**
	 * <p>Get {@link BwEvent#getName()} from each {@link EventInfo}.</p>
	 * @param events which names to get.
	 * @return {@link Collection} of {@link BwEvent#getName()} or 
	 * <code>null</code> on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public Collection<String> getNamesOfEvents(Collection<EventInfo> events);

	/**
	 * <p>Return the events for the current user within the given date and time 
	 * range. If retrieveList is supplied only those fields (and a few required 
	 * fields) will be returned.</p>
	 * @param user not <code>null</code>
	 * @param directory
	 * @param filter
	 * @param start
	 * @param end
	 * @param properties
	 * @param recurringRetrievalMode
	 * @return {@link Collection} of {@link EventInfo} or <code>null</code>
	 * on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public Collection<EventInfo> getEvents(User user, BwCalendar directory,
			FilterBase filter, BwDateTime start, BwDateTime end,
			List<String> properties,
			RecurringRetrievalMode recurringRetrievalMode);

	/**
	 * 
	 * @param user
	 * @param calendar
	 * @param event
	 * @return
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public boolean createEvent(User user, BwCalendar calendar, BwEvent event);

	/**
	 * <p>Sets CalDAV event to Bedework side of system.</p>
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
	public BwEvent setEvent(BwEvent event, User user, String headline,
			String description, String location, String type,
			BwCalendar calendar, boolean reccuring, Date startDate,
			Date endDate, List<User> attendees);

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
	public boolean createEvent(User user, String headline, String description,
			String location, String type, BwCalendar calendar,
			boolean reccuring, Date startDate, Date endDate,
			List<User> attendees);
	
	public boolean createEvent(User user, String headline, String description,
			String location, String type, BwCalendar calendar,
			boolean reccuring, String startDate, String startHour,
			String startMinute, String endDate, String endHour,
			String endMinute, List<User> attendees);

	public EventInfo getEvent(int ID, User user, BwCalendar directory,
			FilterBase filter, BwDateTime start, BwDateTime end,
			List<String> properties,
			RecurringRetrievalMode recurringRetrievalMode);

	public boolean updateEvent(int entryID, String headline, User user,
			String type, Boolean reccur, String startDate, String startHour,
			String startMinute, String endDate, String endHour,
			String endMinute, String attendees, String ledger,
			String description, String location, String oneOrMany);

	/**
	 * FIXME merge with the create event
	 * @param user
	 * @param event
	 * @return
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public boolean updateEvent(User user, EventInfo event);

	public boolean deleteEvent(User currentUser, int entryID);

//	public BwEvent setEvent(BwEvent event, User user, String headline,
//			String description, BwLocation location, String type,
//			BwCalendar calendar, boolean reccuring, Date startDate,
//			Date endDate, List<User> attendees);
}
