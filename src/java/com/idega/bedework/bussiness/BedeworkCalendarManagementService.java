/**
 * @(#)BedeworkCalendarManagementService.java    1.0.0 3:39:06 PM
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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bedework.calfacade.BwCalendar;

import com.idega.block.cal.business.CalendarManagementService;
import com.idega.core.user.data.User;

/**
 * <p>Provides services from bedework API.</p>
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.com">Martynas Stakė</a></p>
 * <p>You can expect to find some test cases notice in the end of the file.</p>
 *
 * @version 1.0.0 Apr 27, 2012
 * @author martynasstake
 */
public interface BedeworkCalendarManagementService extends CalendarManagementService {
	
	/**
	 * <p>Gets main calendar from Bedework of {@link com.idega.user.data.User}.</p>
	 * @param user
	 * @return main calendar of {@link com.idega.user.data.User} or <code>null</code>
	 * on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public BwCalendar getHomeCalendar(com.idega.user.data.User user);

	/**
	 * <p>Searches database for given {@link User} calendar in database.</p>
	 * @param user who's calendar should be found.
	 * @param calendarName which should be found.
	 * @return {@link BwCalendar} or <code>null</code> on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public BwCalendar getUserCalendar(com.idega.user.data.User user, String calendarName);

	/**
	 * <p>Gets all calendars, where user can write, edit events, todo's.</p>
	 * @param user
	 * @return {@link List} of {@link BwCalendar}s, 
	 * where {@link BwCalendar#calTypeCalendarCollection} or 
	 * {@link BwCalendar#calTypeInbox} or
	 * {@link BwCalendar#calTypeOutbox}.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public List<org.bedework.calfacade.BwCalendar> getAllUserCalendars(com.idega.user.data.User user);
	
	/**
	 * <p>Gets all folders, where user can write, edit calendars.</p>
	 * @param user
	 * @return {@link List} of {@link BwCalendar}s, 
	 * where {@link BwCalendar#calTypeFolder}.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public List<org.bedework.calfacade.BwCalendar> getAllUserCalendarFolders(com.idega.user.data.User user);
	
	
	/**
	 * <p>Searches Bedework system for calendars, folders, where given {@link User} is creator.</p>
	 * @param userid {@link User#getPrimaryKey()};
	 * @return {@link Collection} of {@link BwCalendar}s or {@link Collections#EMPTY_SET} 
	 * on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public List<org.bedework.calfacade.BwCalendar> getAllUserCalendarDirectories(com.idega.user.data.User user);
	
	/**
	 * <p>Searches for all calendars, calendar folders where given 
	 * {@link com.idega.user.data.User} can edit or add content.</p>
	 * @param user
	 * @return directories, where user can edit.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public List<BwCalendar> getAllUserEditableCalendarDirectories(com.idega.user.data.User user);

	/**
	 * <p>Bedework calendar system is based on directories, so to find all child calendars, folders
	 * of calendar, you must go through them recursively. That is what this method does.</p>
	 * @param calendar - parent calendar. Not <code>null</code>.
	 * @return {@link List} of all {@link BwCalendar}s which given calendar has as a child or
	 * <code>null</code> on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public List<BwCalendar> getAllChildCalendarDirectories(BwCalendar calendar);
	
	public List<BwCalendar> getSubscribedCalendars(com.idega.user.data.User user);

	public List<BwCalendar> getUnSubscribedCalendars(com.idega.user.data.User user);

	/**
	 * <p>Creates calendar for given {@link User}.</p>
	 * @param user user, which has to contain calendar.
	 * @param calendarName
	 * @return <code>true</code> on success, <code>false</code> on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public boolean createCalendar(com.idega.user.data.User user, String calendarName);
	
	/**
	* Sets that user will get data from this calendar.
	* @param user		user that will get data from Calendar
	* @param calendar	calendar that will send data for user
	*/
	public boolean subscribeCalendar(com.idega.user.data.User user, BwCalendar calendar);

	/**
	* Sets that user will not get data from this calendar.
	* @param user		user that will not get data from Calendar
	* @param calendar	calendar that will not send data for user
	*/
	public boolean unSubscribeCalendar(com.idega.user.data.User user, BwCalendar calendar);
}
