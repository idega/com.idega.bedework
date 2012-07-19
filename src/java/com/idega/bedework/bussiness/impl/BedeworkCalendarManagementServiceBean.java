/**
 * @(#)CalendarManagementBean.java    1.0.0 3:37:13 PM
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
package com.idega.bedework.bussiness.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.FinderException;

import org.bedework.calfacade.BwCalendar;
import org.bedework.calfacade.exc.CalFacadeException;
import org.bedework.calsvci.CalendarsI.CheckSubscriptionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.bedework.BedeworkConstants;
import com.idega.bedework.bussiness.BedeworkCalendarManagementService;
import com.idega.bedework.bussiness.BwAPI;
import com.idega.bedework.bussiness.UserAdapter;
import com.idega.block.cal.data.CalDAVCalendar;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.calendar.data.CalendarEntity;
import com.idega.calendar.data.dao.CalendarDAO;
import com.idega.core.business.DefaultSpringBean;
import com.idega.presentation.IWContext;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserBusinessBean;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.util.CoreConstants;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;
import com.idega.util.expression.ELUtil;

/**
 * <p>Implementation of {@link BedeworkCalendarManagementService}.</p>
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.com">Martynas Stakė</a></p>
 * <p>You can expect to find some test cases notice in the end of the file.</p>
 *
 * @version 1.0.0 Apr 27, 2012
 * @author martynasstake
 */
@Service("bedeworkCalendarManagementService")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class BedeworkCalendarManagementServiceBean extends DefaultSpringBean implements BedeworkCalendarManagementService{

	private static final Logger LOGGER = Logger.getLogger(BedeworkCalendarManagementServiceBean.class.getName());
	
	private UserBusiness userBusiness = null;

	/**
	 * <p>Gets {@link UserBusinessBean} for managing users.</p>
	 * @return {@link UserBusinessBean} or <code>null</code> on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	private UserBusiness getUserBusiness() {
		if (this.userBusiness == null) {
			try {
				this.userBusiness= IBOLookup.getServiceInstance(
						IWContext.getCurrentInstance().getApplicationContext(), 
						UserBusiness.class);
			} catch (IBOLookupException e) {
				LOGGER.log(Level.WARNING, "Unable to get UserBusiness.");
			}
		}
		
		return this.userBusiness;
	}
	
	/**
	 * <p>Local method for making access to {@link User} easier.</p>
	 * @param userID {@link User#getID()}
	 * @return {@link User} by id or null on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	private com.idega.user.data.User getUser(String userID) {
		if (StringUtil.isEmpty(userID)) {
			return null;
		}
		
		try {
			int userIDInteger = Integer.valueOf(userID);
			return this.getUserBusiness().getUser(userIDInteger);
		} catch (RemoteException e) {
			LOGGER.log(Level.WARNING, "Unable to find such user.");
		} catch (NumberFormatException e) {
			LOGGER.log(Level.WARNING, "User id is not a number.");
		}
		
		return null;
	}
	
	@Autowired
	private CalendarDAO calendarDAO;
	
	private CalendarDAO getCalendarDAO() {
		if (this.calendarDAO == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return this.calendarDAO;
	}
	
	@Override
	public List<BwCalendar> getAllUserCalendarDirectories(com.idega.user.data.User user) {
		if (user == null) {
			return null;
		}
		
		BwCalendar homeCalendar = getHomeCalendar(user);
		
		List<BwCalendar> calendars = new ArrayList<BwCalendar>();
		
		List<BwCalendar> associatedCalendars = getAllUserEditableCalendarDirectories(user);
		if (!ListUtil.isEmpty(associatedCalendars)) {
			calendars.addAll(associatedCalendars);
		}
		
		if (homeCalendar != null) {
			calendars.add(homeCalendar);
			calendars.addAll(getAllChildCalendarDirectories(homeCalendar));
		}

		return calendars;
	}
	
	@Override
	public List<BwCalendar> getAllUserEditableCalendarDirectories(com.idega.user.data.User user) {
		if (user == null) {
			return null;
		}
		
		BwAPI bwAPI = new BwAPI(user);
		if (!bwAPI.openBedeworkAPI()) {
			return null;
		}
		
		org.bedework.calsvci.CalendarsI calendarsHandler = bwAPI.getCalendarsHandler();
		if (calendarsHandler == null) {
			bwAPI.closeBedeworkAPI();
			return null;
		}

		Collection<BwCalendar> associatedCalendars = null;
		try {
			associatedCalendars = calendarsHandler.getAddContentCollections(Boolean.TRUE);
		} catch (CalFacadeException e) {
			getLogger().log(Level.WARNING, "Unable to get calendars, where user has right to edit.");
		}
		
		bwAPI.closeBedeworkAPI();
		return new ArrayList<BwCalendar>(associatedCalendars);
	}
	
	@Override
	public List<BwCalendar> getAllChildCalendarDirectories(BwCalendar calendar) {
		if (calendar == null) {
			return null;
		}
		
		BwAPI bwAPI = new BwAPI(getCurrentUser());
		if (!bwAPI.openBedeworkAPI()) {
			return null;
		}
		
		org.bedework.calsvci.CalendarsI calendarsHandler = bwAPI.getCalendarsHandler();
		if (calendarsHandler == null) {
			bwAPI.closeBedeworkAPI();
			return null;
		}
	
		Collection<BwCalendar> childCalendars  = null;
		try {
			childCalendars = calendarsHandler.getChildren(calendar);
		} catch (CalFacadeException e) {
			getLogger().log(Level.WARNING, "Unable to get child calendars.");
		}
		
		if (ListUtil.isEmpty(childCalendars)) {
			bwAPI.closeBedeworkAPI();
			return null;
		}
		
		List<BwCalendar> calendars = new ArrayList<BwCalendar>();
		for (BwCalendar bwCalendar: childCalendars) {
			List<BwCalendar> childsOfBwCalendar = getAllChildCalendarDirectories(bwCalendar);
			if (ListUtil.isEmpty(childsOfBwCalendar)) {
				continue;
			}
			
			calendars.addAll(childsOfBwCalendar);
		}
		
		calendars.addAll(childCalendars);
		
		bwAPI.closeBedeworkAPI();
		return calendars;
	}
	
	@Override
	public BwCalendar getHomeCalendar(com.idega.user.data.User user) {
		if (user == null) {
			return null;
		}
		
		UserAdapter userAdapter = new UserAdapter(user);
		
		BwAPI bwAPI = new BwAPI(user);
		if (!bwAPI.openBedeworkAPI()) {
			return null;
		}
		
		org.bedework.calsvci.CalendarsI calendarsHandler = bwAPI.getCalendarsHandler();
		if (calendarsHandler == null) {
			bwAPI.closeBedeworkAPI();
			return null;
		}
	
		BwCalendar homeCalendar = null;
		try {
			homeCalendar = calendarsHandler.getHome(
					userAdapter.getBedeworkSystemUser(), Boolean.FALSE);
			
			if (homeCalendar == null) {
				homeCalendar = calendarsHandler.getHome(
						userAdapter.getBedeworkSystemUser(), Boolean.TRUE);
			}
		} catch (CalFacadeException e1) {
			LOGGER.log(Level.INFO, "Unable to get home calendar.");
		}
		
		bwAPI.closeBedeworkAPI();
	
		return homeCalendar;
	}

	@Override
	public String getHomeCalendarPath(com.idega.user.data.User user) {
		if (user == null) {
			return null;
		}
		
		BwCalendar homeCalendar = getHomeCalendar(user);
		
		if (homeCalendar == null) {
			UserAdapter userAdapter = new UserAdapter(user);
			
			StringBuffer path = new StringBuffer();
			path.append(BedeworkConstants.BW_USER_CALENDAR_ROOT_PATH)
			.append(userAdapter.getID());
			
			return path.toString();
		} else {
			return homeCalendar.getPath();
		}
	}

	@Override
	public BwCalendar getUserCalendar(com.idega.user.data.User user, String calendarName) {
		return getUserCalendar(user, calendarName, null);
	}
	
	@Override
	public BwCalendar getUserCalendar(
			com.idega.user.data.User user, 
			String calendarName, 
			String calendarFolder
			) {
		
		if (user == null || StringUtil.isEmpty(calendarName)) {
			return null;
		}
		
		String calendarPath = null;
		if (StringUtil.isEmpty(calendarPath)) {
			calendarPath = getHomeCalendarPath(user);
		} else {
			calendarPath = calendarFolder;
		}
				
		if (StringUtil.isEmpty(calendarPath)) {
			return null;
		}
		
		BwAPI bwAPI = new BwAPI(user);
		if (!bwAPI.openBedeworkAPI()) {
			return null;
		}
		
		org.bedework.calsvci.CalendarsI calendarsHandler = bwAPI.getCalendarsHandler();
	
		BwCalendar calendar = null;
		try {
			calendar = calendarsHandler.get(calendarPath + CoreConstants.SLASH + calendarName);
		} catch (CalFacadeException e) {
			LOGGER.log(Level.INFO, "Calendar " + calendarPath + " not found.");
		}
		
		bwAPI.closeBedeworkAPI();
		return calendar;
	}

	@Override
	public boolean createCalendar(com.idega.user.data.User user, String calendarName) {
		return createCalendarDirectory(user, calendarName, null, null, null, Boolean.FALSE, 
				Boolean.FALSE, BwCalendar.calTypeCalendarCollection, null);
	}
	
	@Override
	public boolean createCalendar(User user, String calendarName,
			String folderPath, String summary, boolean isPublic,
			Set<Long> groupsIDs) {
		return createCalendarDirectory(user, calendarName, folderPath, summary, 
				null, Boolean.FALSE, isPublic, BwCalendar.calTypeCalendarCollection, groupsIDs);
	}

	@Override
	public boolean createCalendar(com.idega.user.data.User user,
			String calendarName, String folderPath, String summary,
			String description, boolean isReadOnly, boolean isPublic) {
		
		return createCalendarDirectory(user, calendarName, folderPath, summary, 
				description, isReadOnly, isPublic, BwCalendar.calTypeCalendarCollection, null);
	}

	@Override
	public boolean createFolder(com.idega.user.data.User user,
			String calendarName, String folderPath, String summary,
			String description, boolean isReadOnly, boolean isPublic) {
		
		return createCalendarDirectory(user, calendarName, folderPath, summary, 
				description, isReadOnly, isPublic, BwCalendar.calTypeFolder, null);
	}

	@Override
	public boolean createCalendarDirectory(User user, String calendarName,
			String folderPath, String summary, String description,
			boolean isReadOnly, boolean isPublic, int calendarDirectoryType, 
			Set<Long> groupsIDs) {
		
		if (user == null || StringUtil.isEmpty(calendarName)) {
			LOGGER.log(Level.INFO, "User or calendar name not defined.");
			return Boolean.FALSE;
		}
		
		BwCalendar bwCalendar = getUserCalendar(user, calendarName, folderPath);
		if (bwCalendar != null) {
			LOGGER.log(Level.INFO, "Such calendar already exists, not creating.");
			return Boolean.FALSE;
		}
		
		CalendarEntity calendar = new CalendarEntity();
		calendar = setCalendarEntity(calendar, user, calendarName, folderPath, summary, 
				description, isReadOnly, isPublic, calendarDirectoryType, groupsIDs);
		if (calendar == null) {
			return Boolean.FALSE;
		}
		
		return getCalendarDAO().updateCalendar(user, calendar);
	}

	@Override
	public boolean updateCalendarDirectory(CalendarEntity calendar, User user,
			String calendarName, String folderPath, String summary, String description,
			boolean isReadOnly, boolean isPublic, int calendarDirectoryType, Set<Long> groupsIDs) {
		
		if (user == null) {
			LOGGER.log(Level.INFO, "User or calendar not defined.");
			return Boolean.FALSE;
		}
		
		if (calendar == null || getCalendarDAO().getCalendarById(calendar.getId()) == null) {
			return createCalendarDirectory(user, calendarName, folderPath, summary, 
					description, isReadOnly, isPublic, calendarDirectoryType, groupsIDs);
		}
		
		calendar = setCalendarEntity(calendar, user, calendarName, folderPath, summary, 
				description, isReadOnly, isPublic, calendarDirectoryType, groupsIDs);
		if (calendar == null) {
			return Boolean.FALSE;
		}
		
		return getCalendarDAO().updateCalendar(user, calendar);
	}

	@Override
	public boolean subscribeCalendar(User user, String calendarPath) {
		if (user == null || StringUtil.isEmpty(calendarPath)) {
			return Boolean.FALSE;
		}
		
		CalendarEntity calendar = getCalendarDAO().getCalendarByPath(calendarPath);
		return subscribeCalendar(user, calendar);
	}

	@Override
	public boolean subscribeCalendar(com.idega.user.data.User user,
			BwCalendar calendar) {
		
		if (user == null || calendar == null) {
			return Boolean.FALSE;
		}
		
		BwAPI bwAPI = new BwAPI(user);
		if (!bwAPI.openBedeworkAPI()) {
			return Boolean.FALSE;
		}
		
		org.bedework.calsvci.SynchI synchronizer = bwAPI.getSynchronizer();
		if (synchronizer == null) {
			bwAPI.closeBedeworkAPI();
			return Boolean.FALSE;
		}
		
		boolean isSubscribed = Boolean.FALSE;
		try { 
			isSubscribed = synchronizer.subscribe(calendar);
		} catch (CalFacadeException e) {
			LOGGER.log(Level.WARNING, "Unable to subscribe calendar: " 
					+ calendar.getName() + " cause of: ", e);
		}
		
		return isSubscribed && bwAPI.closeBedeworkAPI();
	}

	@Override
	public boolean subscribeCalendars(User user, Collection<String> calendarPaths) {
		if (user == null || ListUtil.isEmpty(calendarPaths)) {
			return Boolean.FALSE;
		}
		
		Collection<CalendarEntity> calendarEntities = getCalendarDAO()
				.getCalendarsByPaths(calendarPaths);
		if (ListUtil.isEmpty(calendarEntities)) {
			return Boolean.FALSE;
		}
		
		for (CalendarEntity calendar : calendarEntities) {
			if (!subscribeCalendar(user, calendar)) {
				return Boolean.FALSE;
			}
		}
		
		return Boolean.TRUE;
	}

	@Override
	public boolean unSubscribeCalendar(User user, String calendarPath) {
		if (user == null || StringUtil.isEmpty(calendarPath)) {
			return Boolean.FALSE;
		}
		
		CalendarEntity calendar = getCalendarDAO().getCalendarByPath(calendarPath);
		return unSubscribeCalendar(user, calendar);
	}

	@Override
	public boolean unSubscribeCalendar(com.idega.user.data.User user,
			BwCalendar calendar) {
		
		if (user == null || calendar == null) {
			return Boolean.FALSE;
		}
		
		BwAPI bwAPI = new BwAPI(user);
		if (!bwAPI.openBedeworkAPI()) {
			return Boolean.FALSE;
		}
		
		org.bedework.calsvci.SynchI synchronizer = bwAPI.getSynchronizer();
		if (synchronizer == null) {
			bwAPI.closeBedeworkAPI();
			return Boolean.FALSE;
		}
		
		boolean isUnsubscribed = Boolean.FALSE;
		try {
			isUnsubscribed = synchronizer.unsubscribe(calendar);
		} catch (CalFacadeException e) {
			LOGGER.log(Level.WARNING, "Unable to unsubscribe calendar: " 
					+ calendar.getName() + " cause of: ", e);
		}
		
		return isUnsubscribed && bwAPI.closeBedeworkAPI();
	}

	@Override
	public boolean unsubscribeCalendars(User user,
			Collection<String> calendarPaths) {
		if (user == null || ListUtil.isEmpty(calendarPaths)) {
			return Boolean.FALSE;
		}
		
		Collection<CalendarEntity> calendarEntities = getCalendarDAO()
				.getCalendarsByPaths(calendarPaths);
		if (ListUtil.isEmpty(calendarEntities)) {
			return Boolean.FALSE;
		}
		
		for (CalendarEntity calendar : calendarEntities) {
			if (!unSubscribeCalendar(user, calendar)) {
				return Boolean.FALSE;
			}
		}
		
		return Boolean.TRUE;
	}

	@Override
	public List<CalendarEntity> getSubscribedCalendars(com.idega.user.data.User user) {
		if (user == null) {
			return null;
		}
		
		List<CalDAVCalendar> calendars = getSubscribedCalendars(user, null, null);
		if (ListUtil.isEmpty(calendars)) {
			return null;
		}
		
		List<CalendarEntity> calendarEntities = new ArrayList<CalendarEntity>(calendars.size());
		for (CalDAVCalendar calendar : calendars) {
			calendarEntities.add((CalendarEntity) calendar);
		}
		
		return calendarEntities;
	}

	@Override
	public List<CalDAVCalendar> getSubscribedCalendars(
			com.idega.user.data.User user, Integer maxResults, Integer firstResult) {
		if (user == null) {
			return null;
		}
		
		List<CalDAVCalendar> calendars = getAllVisibleCalendars(user, maxResults, firstResult);
		if (ListUtil.isEmpty(calendars)) {
			return null;
		}
		
		List<CalDAVCalendar> subscribedCalendars = new ArrayList<CalDAVCalendar>();
		for (CalDAVCalendar calendar : calendars) {
			// FIXME remove that check of type somehow
			if (!(calendar instanceof BwCalendar || calendar instanceof CalendarEntity)) {
				continue;
			}
			
			if (isSubscribed(user, (BwCalendar) calendar)) {
				subscribedCalendars.add(calendar);
			}
		}
		
		return subscribedCalendars;
	}
	
	public Boolean isSubscribed(User user, BwCalendar calendar) {
		if (user == null || calendar == null) {
			return null;
		}
		
		if (StringUtil.isEmpty(calendar.getSubscriptionId())) {
			return Boolean.FALSE;
		}
		
		BwAPI bwAPI = new BwAPI(user);
		if (!bwAPI.openBedeworkAPI()) {
			return null;
		}
		
		org.bedework.calsvci.SynchI synchronizer = bwAPI.getSynchronizer();
		if (synchronizer == null) {
			bwAPI.closeBedeworkAPI();
			return null;
		}
		
		CheckSubscriptionResult subscriptionResult = null;
		try {
			subscriptionResult = synchronizer.checkSubscription(calendar);
		} catch (CalFacadeException e) {
			LOGGER.log(Level.WARNING, "Unable to check for subscription: ", e);
		}
		
		bwAPI.closeBedeworkAPI();
		
		if (subscriptionResult == null) {
			return null;
		}

		if (subscriptionResult.compareTo(CheckSubscriptionResult.ok) == 0) {
			return Boolean.TRUE;
		}
		
		if (subscriptionResult.compareTo(CheckSubscriptionResult.resubscribed) == 0) {
			LOGGER.log(Level.SEVERE, "Calendars are getting resubscribed! " +
					"It should be fixed as soon as possible!");
			return Boolean.TRUE;
		}
		
		if (subscriptionResult.compareTo(CheckSubscriptionResult.failed) == 0) {
			return Boolean.FALSE;
		}
		
		if (subscriptionResult.compareTo(CheckSubscriptionResult.notExternal) == 0) {
			return Boolean.FALSE;
		}
		
		if (subscriptionResult.compareTo(CheckSubscriptionResult.notFound) == 0) {
			return Boolean.FALSE;
		}
		
		if (subscriptionResult.compareTo(CheckSubscriptionResult.noSynchService) == 0) {
			LOGGER.log(Level.WARNING, "Bedework Sync service is not working!");
			return Boolean.FALSE;
		}
		
		return null;
	}

	@Override
	public List<CalendarEntity> getUnSubscribedCalendars(User user) {
		List<CalDAVCalendar> calendars = getUnSubscribedCalendars(user, null, null);
		if (ListUtil.isEmpty(calendars)) {
			return null;
		}
		
		List<CalendarEntity> bedeworkCalendars = new ArrayList<CalendarEntity>(
				calendars.size());
		for (CalDAVCalendar calendar : calendars) {
			if (calendar instanceof CalendarEntity) {
				bedeworkCalendars.add((CalendarEntity) calendar);
			}
		}
		
		return bedeworkCalendars;
	}

	@Override
	public List<CalDAVCalendar> getUnSubscribedCalendars(User user,
			Integer maxResults, Integer firstResult) {
		if (user == null) {
			return null;
		}
		
		List<CalDAVCalendar> visibleCalendars = getAllVisibleCalendars(user, 
				maxResults, firstResult);
		if (visibleCalendars == null) {
			return null;
		}
		
		List<CalDAVCalendar> subscribedCalendars = getSubscribedCalendars(user, 
				maxResults, firstResult);
		if (ListUtil.isEmpty(subscribedCalendars)) {
			return visibleCalendars;
		}
		
		if (visibleCalendars.removeAll(subscribedCalendars)) {
			return visibleCalendars;
		}
		
		return null;
	}

	@Override
	public List<CalDAVCalendar> getAllUserCalendars(String userID) {
		User user = getUser(userID);
		if (user == null) {
			return null;
		}
		
		Collection<BwCalendar> calendars = getAllUserCalendarDirectories(user);
		if (ListUtil.isEmpty(calendars)) {
			return null;
		}
		
		return new ArrayList<CalDAVCalendar>(calendars);
	}

	@Override
	public List<BwCalendar> getAllUserCalendars(User user) {
		if (user == null) {
			return null;
		}
		
		List<BwCalendar> directories = getAllUserCalendarDirectories(user);
		if (ListUtil.isEmpty(directories)) {
			return null;
		}
		
		List<BwCalendar> calendars = new ArrayList<BwCalendar>();
		for (BwCalendar calendarDirectory : directories) {
			if (calendarDirectory.getCalType() == BwCalendar.calTypeCalendarCollection 
					|| calendarDirectory.getCalType() == BwCalendar.calTypeInbox
					|| calendarDirectory.getCalType() == BwCalendar.calTypeOutbox) {
				calendars.add(calendarDirectory);
			}
		}
		
		return calendars;
	}

	@Override
	public List<BwCalendar> getAllUserCalendarFolders(User user) {
		if (user == null) {
			return null;
		}
		
		List<BwCalendar> directories = getAllUserCalendarDirectories(user);
		if (ListUtil.isEmpty(directories)) {
			return null;
		}
		
		List<BwCalendar> folders = new ArrayList<BwCalendar>();
		for (BwCalendar calendarDirectory : directories) {
			if (calendarDirectory.getCalType() == BwCalendar.calTypeFolder) {
				folders.add(calendarDirectory);
			}
		}
		
		return folders;
	}
	
	@Override
	public List<CalDAVCalendar> getAllPrivateCalendarsByGroupIDs(List<String> groupIDs, 
			Integer maxResult, Integer firstResult) {
		Set<Long> ids = convertGroupIDs(groupIDs);
		if (ListUtil.isEmpty(ids)) {
			return null;
		}
		
		return getAllPrivateCalendarsByGroupIDs(ids, maxResult, firstResult);
	}
	
	@Override
	public List<CalDAVCalendar> getAllPrivateCalendarsByGroupIDs(List<String> groupIDs) {
		Set<Long> ids = convertGroupIDs(groupIDs);
		if (ListUtil.isEmpty(ids)) {
			return null;
		}
		
		return getAllPrivateCalendarsByGroupIDs(ids, null, null);
	}
	
	@Override
	public List<CalDAVCalendar> getAllPrivateCalendarsByGroupIDs(Set<Long> groupIDs,
			Integer maxResult, Integer firstResult) {
		Collection<CalendarEntity> privateCalendars = getCalendarDAO()
				.getPrivateCalendarsByGroupIDs(groupIDs, maxResult, firstResult);

		if (ListUtil.isEmpty(privateCalendars)) {
			return null;
		}
		
		List<CalDAVCalendar> calendars = new ArrayList<CalDAVCalendar>(privateCalendars);
		return calendars;
	}
	
	@Override
	public List<CalDAVCalendar> getAllPrivateCalendars() {
		Collection<CalendarEntity> privateCalendars = getCalendarDAO()
				.getPrivateCalendars();

		if (ListUtil.isEmpty(privateCalendars)) {
			return null;
		}
		
		List<CalDAVCalendar> calendars = new ArrayList<CalDAVCalendar>(privateCalendars);
		return calendars;
	}
	
	@Override
	public List<CalDAVCalendar> getAllPublicCalendars() {
		Collection<CalendarEntity> publicCalendars = getCalendarDAO()
				.getPublicCalendars();

		if (ListUtil.isEmpty(publicCalendars)) {
			return null;
		}
		
		List<CalDAVCalendar> calendars = new ArrayList<CalDAVCalendar>(publicCalendars);
		return calendars;
	}
	
	@Override
	public List<CalDAVCalendar> getAllVisibleCalendars(com.idega.user.data.User user) {
		return getAllVisibleCalendars(user, null, null);
	}
	
	@Override
	public List<CalDAVCalendar> getAllVisibleCalendars(com.idega.user.data.User user, 
			Integer maxResults, Integer firstResult) {
		if (user == null) {
			return null;
		}
		
		List<CalDAVCalendar> visibleCalendars = new ArrayList<CalDAVCalendar>();
		
		List<String> groupIDs = null;
		Collection<com.idega.user.data.Group> userGroups = null;
		try {
			Collection<?> groups = getUserBusiness().getUserGroups(user);
			userGroups = new ArrayList<com.idega.user.data.Group>(groups.size());
			for (Object group : groups) {
				if (group instanceof com.idega.user.data.Group) {
					userGroups.add((com.idega.user.data.Group) group);
				}
			}
		} catch (RemoteException e) {
			LOGGER.log(Level.WARNING, "Unable to get user groups: ", e);
		}
			
		if (!ListUtil.isEmpty(userGroups)) {
			groupIDs = new ArrayList<String>(userGroups.size());
			
			for (Group group : userGroups) {
				groupIDs.add(group.getPrimaryKey().toString());
			}
		}
			
		List<CalDAVCalendar> owned = getAllPrivateCalendarsByGroupIDs(groupIDs,
				maxResults, firstResult);
		if (!ListUtil.isEmpty(owned)) {
			visibleCalendars.addAll(owned);
		}
			
		owned = getAllPublicCalendars();
		if (!ListUtil.isEmpty(owned)) {
			visibleCalendars.addAll(owned);
		}
		
		return visibleCalendars;
	}

	@Override
	public List<Group> getAllGroups(Set<Long> groupIDs) {
		GroupHome groupHome = getHomeForEntity(Group.class);
		if (groupHome == null) {
			return null;
		}
		
		List<Group> allGroups = null;
		try {
			Collection<?> groups = null;

			if (ListUtil.isEmpty(groupIDs)) {
				groups = groupHome.findAll();
			} else {
				groups = groupHome.findGroups(groupIDs.toArray(new String[groupIDs.size()]));
			}
			
			if (ListUtil.isEmpty(groups)) {
				return null;
			}
			
			allGroups = new ArrayList<Group>();
			
			for (Object group : groups) {
				if (group instanceof Group) {
					allGroups.add((Group) group);
				}
			}
			
		} catch (FinderException e) {
			getLogger().log(Level.WARNING, "Unable to get groups: " + e.getMessage());
			return null;
		}
		
		return allGroups;
	}

	@Override
	public Set<Long> convertGroupIDs(List<String> groupIDs) {
		if (ListUtil.isEmpty(groupIDs)) {
			return null;
		}
		
		Set<Long> ids = new HashSet<Long>();
		for (String groupID : groupIDs) {
			Long value = null;
			try {
				value = Long.valueOf(groupID);
			} catch (NumberFormatException e) {}
			
			if (value != null) {
				ids.add(value);
			}
		}
		
		return ids;
	}

	@Override
	public CalendarEntity setCalendarEntity(CalendarEntity calendar, User user, 
			String calendarName, String folderPath, String summary, String description,
			boolean isReadOnly, boolean isPublic, int calendarDirectoryType,
			Set<Long> groupsIDs) {
		
		if (user == null) {
			return null;
		}
		
		UserAdapter userAdapter = new UserAdapter(user);
		
		calendar.setName(calendarName);
		calendar.setCreatorEnt(userAdapter.getBedeworkSystemUser());
		calendar.setCreatorHref(userAdapter.getBedeworkSystemUser().getPrincipalRef());
		calendar.setOwnerHref(userAdapter.getBedeworkSystemUser().getPrincipalRef());
		calendar.setPublick(isPublic);
		calendar.setAffectsFreeBusy(Boolean.TRUE);
		
		String calPath = null;
		if (StringUtil.isEmpty(folderPath)) {
			calPath = getHomeCalendarPath(user);
		} else {
			calPath = folderPath;
		}
				
		if (calPath == null) {
			return null;
		}
		
		calendar.setColPath(calPath);
		calendar.setPath(calPath + CoreConstants.SLASH + calendarName);
		
		if (calendarDirectoryType < 1 || calendarDirectoryType > 9) {
			calendarDirectoryType = BwCalendar.calTypeUnknown;
		}
		
		calendar.setCalType(calendarDirectoryType);
		
		if (!StringUtil.isEmpty(summary)) {
			calendar.setSummary(summary);
		}
		
		if (!StringUtil.isEmpty(description)) {
			calendar.setDescription(description);
		}
		
		calendar.setUnremoveable(isReadOnly);
		
		if (!ListUtil.isEmpty(groupsIDs)) {
			calendar.setGroups(groupsIDs);
		}
		
		return calendar;
	}

	@Override
	public boolean updateCalendar(com.idega.user.data.User user, String calendarName,
			String folderPath, String summary, boolean isPublic, Set<Long> groupsIDs) {
		
		if (user == null || StringUtil.isEmpty(calendarName)) {
			LOGGER.log(Level.INFO, "User or calendar name not defined.");
			return Boolean.FALSE;
		}

		BwCalendar bwCalendar = getUserCalendar(user, calendarName, folderPath);
		if (bwCalendar == null) {
			LOGGER.log(Level.INFO, "Such calendar: "+ calendarName +" does not exists, creating...");
			return createCalendar(user, calendarName, folderPath, summary, isPublic, groupsIDs);
		}
		
		String path = null;
		if (StringUtil.isEmpty(folderPath)) {
			path = getHomeCalendarPath(user);
		} else {
			path = folderPath;
		}
		
		path = path + CoreConstants.SLASH + calendarName;

		CalendarEntity calendar = getCalendarDAO().getCalendarByPath(path);
 		if (calendar == null) {
			calendar = new CalendarEntity(bwCalendar, groupsIDs);
		}
		
		return updateCalendarDirectory(calendar, user, 
				calendarName, folderPath, 
				summary, null, Boolean.FALSE, isPublic, 
				BwCalendar.calTypeCalendarCollection, groupsIDs);
	}

	private int getIDFromString(String calendarID) {
		if (StringUtil.isEmpty(calendarID)) {
			return -1;
		}
		
		Integer id = null;
		try {
			id = Integer.valueOf(calendarID);
			return id;
		} catch (NumberFormatException e) {
			getLogger().log(Level.WARNING, "ID shoud be number, but: " + calendarID + " found.");
			return -1;
		}
	}
	
	@Override
	public boolean removeCalendar(String calendarID, User user) {
		if (StringUtil.isEmpty(calendarID)) {
			getLogger().log(Level.WARNING, "Unable to remove calendar. Calendar ID is not given.");
			return Boolean.FALSE;
		}
		
		int id = getIDFromString(calendarID);
		if (id == -1) {
			return Boolean.FALSE;
		}
		
		return getCalendarDAO().removeCalendar(user, id);
	}

	@Override
	public boolean removeCalendar(CalendarEntity calendar, User user) {
		return getCalendarDAO().removeCalendar(user, calendar);
	}

	@Override
	public CalendarEntity getCalendar(String calendarID) {
		if (StringUtil.isEmpty(calendarID)) {
			getLogger().log(Level.WARNING, "Unable to get calendar. Calendar ID is not given.");
			return null;
		}
		
		int id = getIDFromString(calendarID);
		if (id == -1) {
			return null;
		}
		
		return getCalendarDAO().getCalendarById(id);
	}
}
