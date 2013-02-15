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
import org.bedework.calfacade.BwCategory;
import org.bedework.calfacade.BwCollectionLastmod;
import org.bedework.calfacade.BwProperty;
import org.bedework.calfacade.BwUser;
import org.bedework.calfacade.exc.CalFacadeException;
import org.bedework.calsvci.CalendarsI;
import org.bedework.calsvci.CalendarsI.CheckSubscriptionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.bedework.BedeworkConstants;
import com.idega.bedework.bussiness.BedeworkAPI;
import com.idega.bedework.bussiness.BedeworkCalendarsService;
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

import edu.rpi.cmt.access.Acl.CurrentAccess;

/**
 * <p>Implementation of {@link BedeworkCalendarsService}.</p>
 * <p>You can report about problems to:
 * <a href="mailto:martynas@idega.com">Martynas Stakė</a></p>
 * <p>You can expect to find some test cases notice in the end of the file.</p>
 *
 * @version 1.0.0 Apr 27, 2012
 * @author martynasstake
 */
@Service("bedeworkCalendarService")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class BedeworkCalendarServiceBean extends DefaultSpringBean implements BedeworkCalendarsService{

	private static final Logger LOGGER = Logger.getLogger(BedeworkCalendarServiceBean.class.getName());

	private UserBusiness userBusiness = null;

	@Autowired
	private CalendarDAO calendarDAO;

	/**
	 * @param calendars - instances of {@link BwCalendar}, not <code>null</code>.
	 * @return converted {@link Collection} or <code>null</code> on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public Collection<BwCalendar> convertDirectories(
			Collection<CalDAVCalendar> calendars) {
		if (ListUtil.isEmpty(calendars)) {
			return null;
		}

		ArrayList<BwCalendar> convertedCalendars = new ArrayList<BwCalendar>();
		for (CalDAVCalendar calendar : calendars) {
			if (calendar instanceof BwCalendar) {
				if (!convertedCalendars.add((BwCalendar) calendar))
					return null;
			}
		}

		return convertedCalendars;
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
	public boolean createCalendar(User user, String calendarName,
			String folderPath, String summary, boolean isPublic,
			Set<Long> groupsIDs) {

		return updateCalendarDirectory(user, calendarName, folderPath, summary,
				null, Boolean.FALSE, isPublic,
				BwCalendar.calTypeCalendarCollection, groupsIDs);
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

	@Override
	public BwCalendar getCalendar(User user, String calendarName) {
		return getCalendar(user, calendarName, null);
	}

	@Override
	public BwCalendar getCalendarByPath(User user, String path) {
		if (user == null || StringUtil.isEmpty(path)) {
			return null;
		}

		BedeworkAPI bwAPI = new BedeworkAPI(user);
		if (!bwAPI.openBedeworkAPI()) {
			return null;
		}

		CalendarsI calendarsHandler = bwAPI.getCalendarsHandler();

		BwCalendar calendar = null;
		try {
			calendar = calendarsHandler.get(path);
		} catch (CalFacadeException e) {
			LOGGER.log(Level.INFO, "Calendar " + path + " not found.");
		}

		bwAPI.closeBedeworkAPI();

		return calendar;
	}

	@Override
	public BwCalendar getCalendar(User user, String calendarName,
			String calendarFolder) {

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

		return getCalendarByPath(user,
				calendarPath + CoreConstants.SLASH + calendarName);
	}

	@Override
	public List<BwCalendar> getCalendarChildDirectories(BwCalendar calendar) {
		if (calendar == null) {
			return null;
		}

		BedeworkAPI bwAPI = new BedeworkAPI(getCurrentUser());
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
			List<BwCalendar> childsOfBwCalendar = getCalendarChildDirectories(bwCalendar);
			if (ListUtil.isEmpty(childsOfBwCalendar)) {
				continue;
			}

			calendars.addAll(childsOfBwCalendar);
		}

		calendars.addAll(childCalendars);

		bwAPI.closeBedeworkAPI();
		return calendars;
	}

	private CalendarDAO getCalendarDAO() {
		if (this.calendarDAO == null) {
			ELUtil.getInstance().autowire(this);
		}

		return this.calendarDAO;
	}

	@Override
	public List<BwCalendar> getCalendarDirectories(User user) {
		if (user == null) {
			return null;
		}

		BwCalendar homeCalendar = getHomeCalendar(user);

		List<BwCalendar> calendars = new ArrayList<BwCalendar>();

		List<BwCalendar> associatedCalendars = getEditableCalendarDirectories(user);
		if (!ListUtil.isEmpty(associatedCalendars)) {
			calendars.addAll(associatedCalendars);
		}

		if (homeCalendar != null) {
			calendars.add(homeCalendar);
			calendars.addAll(getCalendarChildDirectories(homeCalendar));
		}

		return calendars;
	}

	@Override
	public List<BwCalendar> getCalendarFolders(User user) {
		if (user == null) {
			return null;
		}

		List<BwCalendar> directories = getCalendarDirectories(user);
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
	public List<CalDAVCalendar> getCalendars(String userID) {
		User user = getUser(userID);
		if (user == null) {
			return null;
		}

		Collection<BwCalendar> calendars = getCalendarDirectories(user);
		if (ListUtil.isEmpty(calendars)) {
			return null;
		}

		return new ArrayList<CalDAVCalendar>(calendars);
	}

	@Override
	public List<BwCalendar> getCalendars(User user) {
		if (user == null) {
			return null;
		}

		List<BwCalendar> directories = getCalendarDirectories(user);
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
	public List<BwCalendar> getEditableCalendarDirectories(User user) {
		if (user == null) {
			return null;
		}

		BedeworkAPI bwAPI = new BedeworkAPI(user);
		if (!bwAPI.openBedeworkAPI()) {
			return null;
		}

		CalendarsI calendarsHandler = bwAPI.getCalendarsHandler();
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
	public List<Group> getGroups(Set<Long> groupIDs) {
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

	public Collection<Group> getGroups(User user) {
		if (user == null) {
			return null;
		}

		Collection<com.idega.user.data.Group> userGroups = null;
		Collection<?> groups = null;
		try {
			groups = getUserBusiness().getUserGroups(user);
		} catch (RemoteException e) {
			LOGGER.log(Level.WARNING, "Unable to get user groups: ", e);
		}

		userGroups = new ArrayList<com.idega.user.data.Group>(groups.size());
		for (Object group : groups) {
			if (group instanceof com.idega.user.data.Group) {
				userGroups.add((com.idega.user.data.Group) group);
			}
		}

		return userGroups;
	}

	@Override
	public BwCalendar getHomeCalendar(com.idega.user.data.User user) {
		if (user == null) {
			return null;
		}

		UserAdapter userAdapter = new UserAdapter(user);

		BedeworkAPI bwAPI = new BedeworkAPI(user);
		if (!bwAPI.openBedeworkAPI()) {
			return null;
		}

		CalendarsI calendarsHandler = bwAPI.getCalendarsHandler();
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
	public String getHomeCalendarPath(User user) {
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

	public List<String> getIDs(Collection<Group> groups) {
		if (!ListUtil.isEmpty(groups)) {
			return null;
		}

		List<String> groupIDs = null;

		if (!ListUtil.isEmpty(groups)) {
			groupIDs = new ArrayList<String>(groups.size());

			for (Group group : groups) {
				groupIDs.add(group.getPrimaryKey().toString());
			}
		}

		return groupIDs;
	}

	@Override
	public List<BwCalendar> getPersonalVisibleDirectories(User user) {
		if (user == null) {
			return null;
		}

		ArrayList<BwCalendar> visibleDirectories = new ArrayList<BwCalendar>();

		List<BwCalendar> subscribedCalendars = getSubscribedCalendars(user);
		if (!ListUtil.isEmpty(subscribedCalendars)) {
			visibleDirectories.addAll(subscribedCalendars);
		}

		List<BwCalendar> personalCalendars = getCalendars(user);
		if (!ListUtil.isEmpty(personalCalendars)) {
			visibleDirectories.addAll(personalCalendars);
		}

		return visibleDirectories;
	}

	@Override
	public List<CalDAVCalendar> getPrivateCalendars() {
		Collection<CalendarEntity> privateCalendars = getCalendarDAO()
				.getPrivateCalendars();

		if (ListUtil.isEmpty(privateCalendars)) {
			return null;
		}

		List<CalDAVCalendar> calendars = new ArrayList<CalDAVCalendar>(privateCalendars);
		return calendars;
	}

	@Override
	public List<CalDAVCalendar> getPrivateCalendars(List<String> groupIDs) {
		Set<Long> ids = convertGroupIDs(groupIDs);
		if (ListUtil.isEmpty(ids)) {
			return null;
		}

		return getPrivateCalendars(ids, null, null);
	}

	@Override
	public List<CalDAVCalendar> getPrivateCalendars(List<String> groupIDs,
			Integer maxResult, Integer firstResult) {
		Set<Long> ids = convertGroupIDs(groupIDs);
		if (ListUtil.isEmpty(ids)) {
			return null;
		}

		return getPrivateCalendars(ids, maxResult, firstResult);
	}

	@Override
	public List<CalDAVCalendar> getPrivateCalendars(Set<Long> groupIDs,
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
	public List<CalDAVCalendar> getPublicCalendars() {
		Collection<CalendarEntity> publicCalendars = getCalendarDAO()
				.getPublicCalendars();

		if (ListUtil.isEmpty(publicCalendars)) {
			return null;
		}

		List<CalDAVCalendar> calendars = new ArrayList<CalDAVCalendar>(publicCalendars);
		return calendars;
	}

	@Override
	public List<BwCalendar> getSubscribedCalendars(User user) {
		if (user == null) {
			return null;
		}

		Collection<BwCalendar> directories = convertDirectories(
				getSubscribedCalendars(user, null, null));
		if (ListUtil.isEmpty(directories)) {
			return null;
		}

		return new ArrayList<BwCalendar>(directories);
	}

	@Override
	public List<CalDAVCalendar> getSubscribedCalendars(User user,
			Integer maxResults, Integer firstResult) {
		if (user == null) {
			return null;
		}

		UserAdapter userAdapter = new UserAdapter(user);
		Collection<CalendarEntity> subscriptions = getCalendarDAO()
				.getSubscriptions(userAdapter.getBedeworkSystemUser());

		if (ListUtil.isEmpty(subscriptions)) {
			return null;
		}

		List<CalDAVCalendar> subscribedCalendars =
				new ArrayList<CalDAVCalendar>(subscriptions.size());

		BedeworkAPI bedeworkAPI = new BedeworkAPI(user);
		CalendarsI calendarsHandler = bedeworkAPI.getCalendarsHandler();
		if (calendarsHandler == null) {
			return subscribedCalendars;
		}

		for (CalendarEntity subscription : subscriptions) {
			BwCalendar alias = null;
			try {
				alias = calendarsHandler.resolveAlias(
						subscription,
						Boolean.TRUE, subscription.getAffectsFreeBusy());
			} catch (CalFacadeException e) {
				getLogger().log(Level.WARNING, "unable to resolve alias: ", e);
			}
			if (alias != null) {
				subscribedCalendars.add(alias);
			}
		}

		bedeworkAPI.closeBedeworkAPI();
		return subscribedCalendars;
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

		List<CalDAVCalendar> visibleCalendars = getVisibleSubscriptions(user,
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

	/**
	 * <p>Local method for making access to {@link User} easier.</p>
	 * @param userID {@link User#getID()}
	 * @return {@link User} by id or null on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	private User getUser(String userID) {
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

	@Override
	public List<CalDAVCalendar> getVisibleSubscriptions(com.idega.user.data.User user) {
		return getVisibleSubscriptions(user, null, null);
	}

	@Override
	public List<CalDAVCalendar> getVisibleSubscriptions(com.idega.user.data.User user,
			Integer maxResults, Integer firstResult) {
		if (user == null) {
			return null;
		}

		List<CalDAVCalendar> visibleCalendars = new ArrayList<CalDAVCalendar>();

		List<String> groupIDs = getIDs(getGroups(user));

		List<CalDAVCalendar> owned = getPrivateCalendars(groupIDs,
				maxResults, firstResult);
		getLogger().info("Owned calendars by " + user + ": " + owned);

		if (!ListUtil.isEmpty(owned)) {
			// trying to avoid showing calendars, which user created...
			UserAdapter userAdapter = new UserAdapter(user);
			BwUser bedeworkUser = userAdapter.getBedeworkSystemUser();
			for (CalDAVCalendar calendar : owned) {
				if (!(calendar instanceof BwCalendar)) {
					continue;
				}

				String href = ((BwCalendar) calendar).getCreatorHref();
				if (StringUtil.isEmpty(href)) {
					continue;
				}

				if (!href.equals(bedeworkUser.getPrincipalRef())) {
					visibleCalendars.add(calendar);
				}
			}
		}

		List<CalDAVCalendar> publicCalendars = getPublicCalendars();
		getLogger().info("Other public calendars available: " + publicCalendars);
		if (!ListUtil.isEmpty(publicCalendars)) {
			visibleCalendars.addAll(publicCalendars);
		}

		return visibleCalendars;
	}

	public Boolean isSubscribedBySomebody(User user, BwCalendar calendar) {
		if (user == null || calendar == null) {
			return null;
		}

		if (StringUtil.isEmpty(calendar.getSubscriptionId())) {
			return Boolean.FALSE;
		}

		BedeworkAPI bwAPI = new BedeworkAPI(user);
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
			return Boolean.TRUE;
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
	public boolean removeCalendar(CalendarEntity calendar, User user) {
		return getCalendarDAO().removeCalendar(user, calendar);
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

	/**
	 * <p>Persists subscription to database.</p>
	 * @param calendar - subscribed/unsubscribed calendar.
	 * @return <code>true</code> if  updated, <code>false</code> otherwise.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public boolean saveSubscription(BwCalendar calendar) {
		if (calendar == null) {
			return Boolean.FALSE;
		}

		String href = calendar.getCreatorHref();
		BedeworkAPI bwAPI = new BedeworkAPI(UserAdapter.getBedeworkSystemUserByID(href.substring(href.length()-1)));

		CalendarsI calendarsHandler = bwAPI.getCalendarsHandler();
		if (calendarsHandler == null) {
			return Boolean.FALSE;
		}

		try {
			calendarsHandler.update(calendar);
		} catch (CalFacadeException e) {
			LOGGER.log(Level.WARNING, "Unable to save calendar subscription: "
					+ calendar.getName() + " cause of: ", e);
			return Boolean.FALSE;
		} finally {
			bwAPI.closeBedeworkAPI();
		}

		return Boolean.TRUE;
	}

	@Override
	public CalendarEntity setCalendarEntity(
			CalendarEntity calendar,
			String access,
			CalendarEntity aliasEntity,
			String aliasURI,
			Integer byteSize,
			Integer calendarDirectoryType,
			String calendarName,
			Set<BwCategory> categories,
			String color,
			CurrentAccess currentAccess,
			String created,
			String description,
			Set<Long> groupsIDs,
			Boolean isDisabled,
			boolean isPublic,
			boolean isReadOnly,
			Boolean isVisible,
			String filterExpression,
			String folderPath,
			Boolean doIgnoreTransparency,
			Boolean isTopicalArea,
			String lastETag,
			BwCollectionLastmod lastModification,
			String lastRefresh,
			String lastRefreshStatus,
			String mailingListID,
			Boolean isOpen,
			Set<BwProperty> properties,
			Boolean doPasswordNeedsEncrypting,
			Integer refreshRate,
			String remoteID,
			String remotePassword,
			Integer seq,
			String subscriptionID,
			String summary,
			String timezone,
			BwUser user) {

		if (user == null || StringUtil.isEmpty(folderPath) ||
				StringUtil.isEmpty(calendarName)) {
			return null;
		}

		if (calendar == null) {
			calendar = new CalendarEntity();
		}

		if (!StringUtil.isEmpty(access)) {
			calendar.setAccess(access);
		}

		if (aliasEntity != null) {
			calendar.setAliasTarget(aliasEntity);
			calendar.setAliasUri("bwcal://" + aliasEntity.getPath());
		}

		if (!StringUtil.isEmpty(aliasURI)) {
			calendar.setAliasUri(aliasURI);
		}

		if (byteSize != null) {
			calendar.setByteSize(byteSize);
		}

		if (calendarDirectoryType != null &&
				calendarDirectoryType >= 1 &&
				calendarDirectoryType <= 9) {

			calendar.setCalType(calendarDirectoryType);
		} else {
			calendar.setCalType(BwCalendar.calTypeUnknown);
		}

		calendar.setName(calendarName);

		if (!ListUtil.isEmpty(categories)) {
			calendar.setCategories(categories);
		}

		if (!StringUtil.isEmpty(color)) {
			calendar.setColor(color);
		}

		if (currentAccess != null) {
			try {
				calendar.setCurrentAccess(currentAccess);
			} catch (CalFacadeException e) {
				getLogger().log(Level.WARNING, "Unable to set current access: ",
						e);
			}
		}

		if (!StringUtil.isEmpty(created)) {
			calendar.setCreated(created);
		}

		if (isDisabled != null) {
			try {
				calendar.setDisabled(isDisabled);
			} catch (CalFacadeException e) {
				getLogger().log(Level.WARNING,
						"Unable to set disabled status to: ", e);
			}
		}

		if (isVisible != null) {
			calendar.setDisplay(isVisible);
		}

		if (!StringUtil.isEmpty(filterExpression)) {
			calendar.setFilterExpr(filterExpression);
		}

		if (doIgnoreTransparency != null) {
			calendar.setIgnoreTransparency(doIgnoreTransparency);
		}

		if (isTopicalArea != null) {
			calendar.setIsTopicalArea(isTopicalArea);
		}

		if (!StringUtil.isEmpty(lastETag)) {
			calendar.setLastEtag(lastETag);
		}

		if (lastModification != null) {
			calendar.setLastmod(lastModification);
		}

		if (!StringUtil.isEmpty(lastRefresh)) {
			calendar.setLastRefresh(lastRefresh);
		}

		if (!StringUtil.isEmpty(lastRefreshStatus)) {
			calendar.setLastRefreshStatus(lastRefreshStatus);
		}

		if (!StringUtil.isEmpty(mailingListID)) {
			calendar.setMailListId(mailingListID);
		}

		if (isOpen != null) {
			try {
				calendar.setOpen(isOpen);
			} catch (CalFacadeException e) {
				getLogger().log(Level.WARNING, "Unable to set open or closed: ",
						e);
			}
		}

		if (!ListUtil.isEmpty(properties)) {
			calendar.setProperties(properties);
		}

		if (doPasswordNeedsEncrypting != null) {
			calendar.setPwNeedsEncrypt(doPasswordNeedsEncrypting);
		}

		if (refreshRate != null) {
			calendar.setRefreshRate(refreshRate);
		}

		if (!StringUtil.isEmpty(remoteID)) {
			calendar.setRemoteId(remoteID);
		}

		if (!StringUtil.isEmpty(remotePassword)) {
			calendar.setRemotePw(remotePassword);
		}

		if (seq != null) {
			calendar.setSeq(seq);
		}

		if (!StringUtil.isEmpty(timezone)) {
			calendar.setTimezone(timezone);
		}

		calendar.setCreatorEnt(user);
		calendar.setCreatorHref(user.getPrincipalRef());
		calendar.setOwnerHref(user.getPrincipalRef());
		calendar.setPublick(isPublic);
		calendar.setAffectsFreeBusy(Boolean.TRUE);

		calendar.setColPath(folderPath);
		calendar.setPath(folderPath + CoreConstants.SLASH + calendarName);

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
	public CalendarEntity setCalendarEntity(CalendarEntity calendar, User user,
			String calendarName, String folderPath, String summary, String description,
			boolean isReadOnly, boolean isPublic, int calendarDirectoryType,
			Set<Long> groupsIDs) {

		UserAdapter uadapter = new UserAdapter(user);

		return setCalendarEntity(calendar, null, null, null, null,
				calendarDirectoryType, calendarName, null, null, null, null,
				description, groupsIDs, null, isPublic, isReadOnly, null, null,
				folderPath, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, summary, null,
				uadapter.getBedeworkSystemUser());
	}

	@Override
	public boolean subscribeCalendar(com.idega.user.data.User user,
			CalendarEntity calendar) {

		if (user == null || calendar == null) {
			return Boolean.FALSE;
		}

		UserAdapter uh = new UserAdapter(user);
		CalendarEntity subscribedCalendar = null;
		BwCalendar existingCalendar = getCalendar(user, calendar.getName());
		if (existingCalendar != null) {
			subscribedCalendar = getCalendar(
					String.valueOf(existingCalendar.getId())
			);
		}


		subscribedCalendar = setCalendarEntity(subscribedCalendar, null, calendar,
				null, null, BwCalendar.calTypeAlias, calendar.getName(),
				calendar.getCategories(), null, null, null,
				calendar.getDescription(), null, null, Boolean.FALSE,
				Boolean.FALSE, null, null,
				getHomeCalendarPath(user),
				null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, calendar.getSummary(), null,
				uh.getBedeworkSystemUser());

		return updateCalendarDirectory(user, subscribedCalendar);
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
	public boolean unSubscribeCalendar(User user, BwCalendar calendar) {

		if (user == null || calendar == null) {
			return Boolean.FALSE;
		}

		Collection<CalendarEntity> userCalendars = getSubscriptions(user);
		if (ListUtil.isEmpty(userCalendars)){
			return Boolean.FALSE;
		}

		BedeworkAPI bwAPI = new BedeworkAPI(user);

		CalendarsI calendarsHandler = bwAPI.getCalendarsHandler();
		if (calendarsHandler == null) {
			return Boolean.FALSE;
		}

		for (BwCalendar userCalendar : userCalendars) {
			BwCalendar alias = null;
			try {
				alias = calendarsHandler.resolveAlias(
						userCalendar,
						Boolean.TRUE, userCalendar.getAffectsFreeBusy());
			} catch (CalFacadeException e) {
				getLogger().log(Level.WARNING, "unable to resolve alias: ", e);
			}

			if (alias != null && calendar.getPath().equals(alias.getPath())) {
				return removeCalendar(String.valueOf(userCalendar.getId()), user);
			}
		}

		return Boolean.FALSE;
	}

	@Override
	public Collection<CalendarEntity> getSubscriptions(User user) {
		if (user == null) {
			return null;
		}

		UserAdapter userAdapter = new UserAdapter(user);
		return getCalendarDAO().getSubscriptions(
				userAdapter.getBedeworkSystemUser());
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
	public boolean updateCalendar(com.idega.user.data.User user, String calendarName,
			String folderPath, String summary, boolean isPublic, Set<Long> groupsIDs) {

		if (user == null || StringUtil.isEmpty(calendarName)) {
			LOGGER.log(Level.INFO, "User or calendar name not defined.");
			return Boolean.FALSE;
		}

		String path = null;
		if (StringUtil.isEmpty(folderPath)) {
			path = getHomeCalendarPath(user);
		} else {
			path = folderPath;
		}

		BwCalendar bwCalendar = getCalendar(user, calendarName, path);
		if (bwCalendar == null) {
			LOGGER.log(Level.INFO, "Such calendar: "+ calendarName +" does not exists, creating...");
			return createCalendar(user, calendarName, path, summary, isPublic, groupsIDs);
		}

		path = path + CoreConstants.SLASH + calendarName;

		CalendarEntity calendar = getCalendarDAO().getCalendarByPath(path);
 		if (calendar == null) {
			calendar = new CalendarEntity(bwCalendar, groupsIDs);
		}

		return updateCalendarDirectory(calendar, user,
				calendarName, folderPath != null ? folderPath : calendar.getColPath(),
				summary, null, Boolean.FALSE, isPublic,
				BwCalendar.calTypeCalendarCollection, groupsIDs);
	}

	@Override
	public boolean updateCalendar(com.idega.user.data.User user,
			String calendarName, String folderPath, String summary,
			String description, boolean isReadOnly, boolean isPublic) {

		return updateCalendarDirectory(user, calendarName, folderPath, summary,
				description, isReadOnly, isPublic,
				BwCalendar.calTypeCalendarCollection, null);
	}

	@Override
	public boolean updateCalendar(User user, String calendarName) {
		return updateCalendarDirectory(user, calendarName, null, null, null,
				Boolean.FALSE, Boolean.FALSE,
				BwCalendar.calTypeCalendarCollection, null);
	}

	@Override
	public boolean updateCalendarDirectory(CalendarEntity calendar, User user,
			String calendarName, String folderPath, String summary, String description,
			boolean isReadOnly, boolean isPublic, int calendarDirectoryType, Set<Long> groupsIDs) {

		if (user == null) {
			LOGGER.log(Level.INFO, "User or calendar not defined.");
			return Boolean.FALSE;
		}

		calendar = setCalendarEntity(calendar, user, calendarName, folderPath,
				summary, description, isReadOnly, isPublic,
				calendarDirectoryType, groupsIDs);

		if (calendar == null) {
			return Boolean.FALSE;
		}

		return updateCalendarDirectory(user, calendar);
	}

	public boolean updateCalendarDirectory(User user, CalendarEntity directory) {
		if (user == null || directory == null || StringUtil.isEmpty(
				directory.getName())) {
			LOGGER.log(Level.INFO, "User or calendar name not defined.");
			return Boolean.FALSE;
		}

		return getCalendarDAO().updateCalendar(user, directory);
	}

	@Override
	public boolean updateCalendarDirectory(User user, String calendarName,
			String folderPath, String summary, String description,
			boolean isReadOnly, boolean isPublic, int calendarDirectoryType,
			Set<Long> groupsIDs) {

		CalendarEntity calendarEntity = null;
		BwCalendar calendar = getCalendar(user, calendarName, folderPath);
		if (calendar != null) {
			calendarEntity = getCalendar(String.valueOf(calendar.getId()));
		}

		return updateCalendarDirectory(calendarEntity, user, calendarName,
				folderPath, summary, description, isReadOnly, isPublic,
				calendarDirectoryType, groupsIDs);
	}

	@Override
	public boolean updateFolder(com.idega.user.data.User user,
			String calendarName, String folderPath, String summary,
			String description, boolean isReadOnly, boolean isPublic) {

		return updateCalendarDirectory(user, calendarName, folderPath, summary,
				description, isReadOnly, isPublic, BwCalendar.calTypeFolder, null);
	}
}
