/**
 * @(#)CalendarDAOImpl.java    1.0.0 2:23:50 PM
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
package com.idega.calendar.data.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bedework.calfacade.BwCalendar;
import org.bedework.calfacade.BwEventObj;
import org.bedework.calfacade.BwPrincipal;
import org.bedework.calfacade.exc.CalFacadeException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.idega.bedework.bussiness.BedeworkAPI;
import com.idega.calendar.data.CalendarEntity;
import com.idega.calendar.data.dao.CalendarDAO;
import com.idega.core.persistence.Param;
import com.idega.core.persistence.Query;
import com.idega.core.persistence.impl.GenericDaoImpl;
import com.idega.data.SimpleQuerier;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.CoreConstants;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;

/**
 * <p>Implementation of data access object for ca_caldav_calendar.</p>
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.com">Martynas Stakė</a></p>
 * <p>You can expect to find some test cases notice in the end of the file.</p>
 *
 * @version 1.0.0 Jul 3, 2012
 * @author martynasstake
 */
@Repository(CalendarDAO.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class CalendarDAOImpl extends GenericDaoImpl implements CalendarDAO {

	private static Logger LOGGER = Logger.getLogger(CalendarDAOImpl.class.getName());
	
	@Override
	public boolean updateCalendar(User user, CalendarEntity calendarEntity) {
		if (calendarEntity == null) {
			return Boolean.FALSE;
		}

		BedeworkAPI bwAPI = new BedeworkAPI(user);
		if (!bwAPI.openBedeworkAPI()) {
			return Boolean.FALSE;
		}
		
		org.bedework.calsvci.CalendarsI calendarsHandler = bwAPI.getCalendarsHandler();
		if (calendarsHandler == null) {
			bwAPI.closeBedeworkAPI();
			return Boolean.FALSE;
		}
		
		try {
			CalendarEntity calendar = getCalendarById(calendarEntity.getId());

			if (calendar == null) {
				// FIXME hack
				boolean publick = calendarEntity.getPublick();
				calendarsHandler.add(calendarEntity, calendarEntity.getColPath());
				if (calendarEntity.getPublick() != publick) {
					calendarEntity.setPublick(publick);
					calendarsHandler.update(calendarEntity);
				}
			} else {
				calendarsHandler.update(calendarEntity);
			}

			return bwAPI.closeBedeworkAPI();
		} catch (CalFacadeException e) {
			LOGGER.log(Level.WARNING, "Unable to add calendar: ", e);
			bwAPI.closeBedeworkAPI();
			return Boolean.FALSE;
		}
	}

	@Override
	public boolean removeCalendar(User user, CalendarEntity calendarEntity) {
		if (calendarEntity == null) {
			return Boolean.FALSE;
		}

		BedeworkAPI bwAPI = new BedeworkAPI(user);
		if (!bwAPI.openBedeworkAPI()) {
			return Boolean.FALSE;
		}
		
		org.bedework.calsvci.CalendarsI calendarsHandler = bwAPI.getCalendarsHandler();
		if (calendarsHandler == null) {
			bwAPI.closeBedeworkAPI();
			return Boolean.FALSE;
		}
		
		try {
			calendarsHandler.delete(calendarEntity, Boolean.TRUE);
			return bwAPI.closeBedeworkAPI();
		} catch (CalFacadeException e1) {
			LOGGER.log(Level.WARNING, "Unable to remove calendar: ", e1);
			bwAPI.closeBedeworkAPI();
			return Boolean.FALSE;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.idega.calendar.data.dao.CalendarDAO#getNumberOfIdegaCalendarChildren(java.lang.String)
	 */
	@Override
	public Integer getNumberOfCalendarChildren(String calendarPath) {
		if (StringUtil.isEmpty(calendarPath)) {
			LOGGER.log(Level.WARNING, "Calendar path not given.");
			return null;
		}
		
		Collection<CalendarEntity> calendars = getResultList(
				CalendarEntity.GET_NUMBER_OF_CHILD_CALENDARS, 
				CalendarEntity.class,
                new Param(CalendarEntity.PROP_COL_PATH, calendarPath));

		if (ListUtil.isEmpty(calendars)) {
			return 0;
		}
		
		return calendars.size();
	}

	/* (non-Javadoc)
	 * @see com.idega.calendar.data.dao.CalendarDAO#getNumberOfIdegaCalendarEvents(java.lang.String)
	 */
	@Override
	public Integer getNumberOfCalendarEvents(String calendarPath) {
		if (StringUtil.isEmpty(calendarPath)) {
			LOGGER.log(Level.WARNING, "Calendar path not given.");
			return null;
		}
		
		Collection<BwEventObj> events = getResultList(
				CalendarEntity.GET_NUMBER_OF_EVENTS, 
				BwEventObj.class,
                new Param(CalendarEntity.PROP_COL_PATH, calendarPath));

		if (ListUtil.isEmpty(events)) {
			return 0;
		}
		
		return events.size();
	}

	/* (non-Javadoc)
	 * @see com.idega.calendar.data.dao.CalendarDAO#getUserIdegaCalendarCollections(com.idega.user.data.User)
	 */
	@Override
	public Collection<CalendarEntity> getPrivateCalendars() {
		return getResultList(CalendarEntity.GET_PRIVATE_CALENDARS, CalendarEntity.class);
	}

	/* (non-Javadoc)
	 * @see com.idega.calendar.data.dao.CalendarDAO#getIdegaPublicCalendarCollections()
	 */
	@Override
	public Collection<CalendarEntity> getPublicCalendars() {
		return getResultList(CalendarEntity.GET_PUBLIC_CALENDARS, CalendarEntity.class);
	}

	/* (non-Javadoc)
	 * @see com.idega.calendar.data.dao.CalendarDAO#getIdegaCalendarById(java.lang.String)
	 */
	@Override
	public CalendarEntity getCalendarById(int id) {
		return this.getSingleResult(
				CalendarEntity.GET_BY_ID, 
				CalendarEntity.class, 
				new Param(CalendarEntity.PROP_ID, id)
				);
	}

	/* (non-Javadoc)
	 * @see com.idega.calendar.data.dao.CalendarDAO#getIdegaCalendarByPath(java.lang.String)
	 */
	@Override
	public CalendarEntity getCalendarByPath(String calendarPath) {
		if (StringUtil.isEmpty(calendarPath)) {
			LOGGER.log(Level.WARNING, "Calendar path not given.");
			return null;
		}
		
		return this.getSingleResult(
				CalendarEntity.GET_BY_PATH,
				CalendarEntity.class, 
				new Param(CalendarEntity.PROP_PATH, calendarPath)
				);
	}

	/* (non-Javadoc)
	 * @see com.idega.calendar.data.dao.CalendarDAO#getIdegaCalendarsByName(java.lang.String)
	 */
	@Override
	public Collection<CalendarEntity> getCalendarsByName(String name) {
		if (StringUtil.isEmpty(name)) {
			LOGGER.log(Level.WARNING, "Calendar name not given.");
			return null;
		}
		
		return getResultList(CalendarEntity.GET_BY_NAME, CalendarEntity.class,
                new Param(CalendarEntity.PROP_NAME, name));
	}

	@Override
	public boolean removeCalendar(User user, int calendarID) {
		if (user == null) {
			return Boolean.FALSE;
		}
		
		CalendarEntity calendar = getCalendarById(calendarID);
		if (calendar == null) {
			return Boolean.FALSE;
		}
		
		return removeCalendar(user, calendar);
	}

	/**
	 * <p>Fetches {@link CalendarEntity#getId()}s by {@link Group#getPrimaryKey()} from
	 * database.</p>
	 * @param groupIDs - {@link Group#getPrimaryKey()}s. Not <code>null</code>.
	 * @return {@link List} of {@link CalendarEntity#getId()}s or <code>null</code> on 
	 * failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	private List<Integer> getCalendarIDsByGroupIDs(Set<Long> groupIDs) {
		if (ListUtil.isEmpty(groupIDs)) {
			LOGGER.log(Level.WARNING, "No group ID's are given.");
			return null;
		}
		
		StringBuilder queryForCalendarIDs = new StringBuilder();
		queryForCalendarIDs.append("SELECT DISTINCT id FROM ca_calendar_group c ")
			.append("WHERE ic_group_id in (");
		for (java.util.Iterator<Long> iterator = groupIDs.iterator(); iterator.hasNext();) {
			queryForCalendarIDs.append(String.valueOf(iterator.next()));
			if (iterator.hasNext()) {
				queryForCalendarIDs.append(CoreConstants.COMMA);
				queryForCalendarIDs.append(CoreConstants.SPACE);
			}
		}
		queryForCalendarIDs.append(")");
		
		List<Integer> ids = null;
		try {
			String[] arrayOfIDs = SimpleQuerier.executeStringQuery(
					queryForCalendarIDs.toString());
			
			ids = new ArrayList<Integer>(arrayOfIDs.length);
			for (String arrayID : arrayOfIDs) {
				try {
					ids.add(Integer.valueOf(arrayID));
				} catch (NumberFormatException e1) {}
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Unable to get calendar ids: ", e);
		}
		
		return ids;
	}
	
	@Override
	public Collection<CalendarEntity> getCalendars(Set<Long> groupIDs, 
			Boolean isPublic, Integer calendarType, boolean showDeleted,
			Integer resultsNumber, Integer firstResultNumber) {
		
		StringBuilder inlineQuery = new StringBuilder();
		inlineQuery.append("from com.idega.calendar.data.CalendarEntity as cal");
		if (!ListUtil.isEmpty(groupIDs) || isPublic != null || calendarType != null
				|| !showDeleted) {
			inlineQuery.append(" where ");
		}
		
		if (calendarType != null && calendarType <= 9 && calendarType >=0) {
			inlineQuery.append("cal.calType=").append(calendarType);
		}
		
		if (isPublic != null) {
			inlineQuery.append(" and ").append("cal.publick=").append(isPublic);
		}
		
		if (!showDeleted) {
			inlineQuery.append(" and ").append("(cal.filterExpr = null").append(" or ")
				.append("cal.filterExpr <> '--TOMBSTONED--')");
		}
		
		List<Integer> ids = null;
		if (!ListUtil.isEmpty(groupIDs)) {
			ids = getCalendarIDsByGroupIDs(groupIDs);
			if (!ListUtil.isEmpty(ids)) {
				inlineQuery.append(" and ").append("cal.id in (")
				.append(CoreConstants.COLON).append(CalendarEntity.PROP_ID)
				.append(") ");
			}
		}
		
		if (resultsNumber != null && resultsNumber > 0) {
			inlineQuery.append("limit ").append(resultsNumber);
			
			if (firstResultNumber != null && firstResultNumber > 0) {
				inlineQuery.append(CoreConstants.COMMA).append(firstResultNumber);
			}
		}

		Query query = getQueryInline(inlineQuery.toString());

		List<CalendarEntity> entities = null;
		if (ListUtil.isEmpty(ids)) {
			entities = query.getResultList(CalendarEntity.class);
		} else {
			entities = query.getResultList(CalendarEntity.class, 
					new Param(CalendarEntity.PROP_ID, ids));
		}

		return entities;
	}
	
	@Override
	public Collection<CalendarEntity> getPrivateCalendarsByGroupIDs(
			Set<Long> groupIDs, Integer resultsNumber, Integer firstResultNumber) {
		if (ListUtil.isEmpty(groupIDs)) {
			LOGGER.log(Level.WARNING, "No group ID's are given.");
			return null;
		}
		
		return getCalendars(groupIDs, Boolean.FALSE,
				CalendarEntity.calTypeCalendarCollection, 
				Boolean.FALSE, resultsNumber, firstResultNumber);
	}
	
	@Override
	public Collection<CalendarEntity> getPrivateCalendarsByGroupIDs(Set<Long> groupIDs) {
		if (ListUtil.isEmpty(groupIDs)) {
			LOGGER.log(Level.WARNING, "No group ID's are given.");
			return null;
		}
		
		return getCalendars(groupIDs, Boolean.FALSE,
				CalendarEntity.calTypeCalendarCollection, Boolean.FALSE, null, null);
	}

	@Override
	public List<CalendarEntity> getCalendarsByPaths(
			Collection<String> calendarPath) {
		return getResultList(CalendarEntity.GET_BY_MULTIPLE_PATH, CalendarEntity.class,
                new Param(CalendarEntity.PROP_PATH, calendarPath));
	}

	@Override
	public Collection<CalendarEntity> getSubscriptions() {
		return getCalendars(null, null, BwCalendar.calTypeExtSub, 
				Boolean.TRUE, null, null);
	}

	@Override
	public Collection<CalendarEntity> getSubscriptions(BwPrincipal user) {
		if (user == null) {
			return null;
		}
		
		return getResultList(CalendarEntity.GET_SUBSCRIPTIONS_BY_USER, CalendarEntity.class,
                new Param(CalendarEntity.PROP_USER_HREF, user.getPrincipalRef()));
	}
}
