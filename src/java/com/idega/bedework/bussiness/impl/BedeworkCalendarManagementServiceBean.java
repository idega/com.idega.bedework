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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bedework.calfacade.BwCalendar;
import org.bedework.calfacade.exc.CalFacadeException;
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
import com.idega.core.business.DefaultSpringBean;
import com.idega.presentation.IWContext;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserBusinessBean;
import com.idega.user.data.User;
import com.idega.util.CoreConstants;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;

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
	
	@Override
	public Collection<BwCalendar> getAllUserCalendars(String userID) {
		com.idega.user.data.User user = getUser(userID);
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
				
		BwCalendar homeCalendar = getHomeCalendar(user);
		bwAPI.closeBedeworkAPI();
		
		if (homeCalendar == null) {
			return null;
		}

		List<BwCalendar> calendars = new ArrayList<BwCalendar>();
		calendars.add(homeCalendar);
		
		if (homeCalendar.getCalendarCollection()) {
			calendars.addAll(getAllChildsOfCalendar(homeCalendar));
		} 

		return calendars;
	}

	public List<CalDAVCalendar> getAllChildsOfCalendar(CalDAVCalendar calendar) {
		if (!(calendar instanceof BwCalendar)) {
			return null;
		}
		
		List<BwCalendar> bwCalendars =  getAllChildsOfCalendar((BwCalendar) calendar);
		if (ListUtil.isEmpty(bwCalendars)) {
			return null;
		}
		 
		return new ArrayList<CalDAVCalendar>(bwCalendars);
	}
	
	@Override
	public List<BwCalendar> getAllChildsOfCalendar(BwCalendar calendar) {
		if (calendar == null) {
			return null;
		}
		
		if(!calendar.getCalendarCollection()) {
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
			List<BwCalendar> childsOfBwCalendar = getAllChildsOfCalendar(bwCalendar);
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
	public boolean createCalendar(com.idega.user.data.User user, String calendarName) {
		if (user == null || StringUtil.isEmpty(calendarName)) {
			LOGGER.log(Level.INFO, "User or calendar name not defined.");
			return Boolean.FALSE;
		}
		
		BwCalendar calendar = getUserCalendar(user, calendarName);
		if (calendar != null) {
			LOGGER.log(Level.INFO, "Such calendar already exists, not creating.");
			return Boolean.FALSE;
		}
		
		BwAPI bwAPI = new BwAPI(user);
		if (!bwAPI.openBedeworkAPI()) {
			return Boolean.FALSE;
		}
		
		org.bedework.calsvci.CalendarsI calendarsHandler = bwAPI.getCalendarsHandler();
		if (calendarsHandler == null) {
			bwAPI.closeBedeworkAPI();
			return Boolean.FALSE;
		}
		
		UserAdapter userAdapter = new UserAdapter(user);
		
		calendar = new BwCalendar();
		calendar.setName(calendarName);
		calendar.setCreatorEnt(userAdapter.getBedeworkSystemUser());
		calendar.setCreatorHref(userAdapter.getBedeworkSystemUser().getPrincipalRef());
		calendar.setOwnerHref(userAdapter.getBedeworkSystemUser().getPrincipalRef());
		calendar.setPublick(Boolean.FALSE);
		calendar.setAffectsFreeBusy(Boolean.TRUE);
		
		String calPath = getHomeCalendarPath(user);
		if (calPath == null) {
			return Boolean.FALSE;
		}
		
		calendar.setColPath(calPath);
		calendar.setPath(calPath + CoreConstants.SLASH + calendarName);
		calendar.setCalType(BwCalendar.calTypeCalendarCollection);
		
		try {
			calendarsHandler.add(calendar, calPath);
		} catch (CalFacadeException e) {
			LOGGER.log(Level.WARNING, "Unable to add calendar: ", e);
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}
	
	@Override
	public BwCalendar getUserCalendar(com.idega.user.data.User user, String calendarName) {
		if (user == null || StringUtil.isEmpty(calendarName)) {
			return null;
		}
		
		String calendarPath = getHomeCalendarPath(user);
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
			return null;
		}

		BwCalendar homeCalendar = null;
		try {
			homeCalendar = calendarsHandler.getHome(
					userAdapter.getBedeworkSystemUser(), Boolean.FALSE);
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

	// TODO
	@Override
	public List<BwCalendar> getSubscribedCalendars(com.idega.user.data.User user) {
		if (user == null) {
			return null;
		}
		
		BwAPI bwAPI = new BwAPI(user);
		if (!bwAPI.openBedeworkAPI()) {
			return null;
		}
		
		org.bedework.calsvci.CalendarsI calendarsHandler = bwAPI.getCalendarsHandler();
		if (calendarsHandler == null) {
			return null;
		}
		
		return null;
	}
	
	@Override
	public List<CalDAVCalendar> getSubscribedCalendars(
			com.idega.user.data.User user, int maxResults, int firstResult)
			throws Exception {
		if (user == null || maxResults < 1 || maxResults - firstResult < 1) {
			return null;
		}
		
		List<BwCalendar> calendars = getSubscribedCalendars(user);
		if (ListUtil.isEmpty(calendars)) {
			return null;
		}
		
		if (calendars.size() <= firstResult) {
			return null;
		}
		
		if (calendars.size() <= maxResults) {
			maxResults = calendars.size();
		}
		
		return new ArrayList<CalDAVCalendar>(calendars.subList(firstResult, maxResults)); 
	}

	@Override
	public boolean subscribeCalendar(com.idega.user.data.User user,
			CalDAVCalendar calendar) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unSubscribeCalendar(com.idega.user.data.User user,
			CalDAVCalendar calendar) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<BwCalendar> getUnSubscribedCalendars(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CalDAVCalendar> getUnSubscribedCalendars(User user,
			int maxResults, int firstResult) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
