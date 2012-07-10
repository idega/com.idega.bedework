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

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bedework.calfacade.BwEventObj;
import org.bedework.calfacade.exc.CalFacadeException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.idega.bedework.bussiness.BwAPI;
import com.idega.calendar.data.CalendarEntity;
import com.idega.calendar.data.dao.CalendarDAO;
import com.idega.core.persistence.Param;
import com.idega.core.persistence.impl.GenericDaoImpl;
import com.idega.user.data.User;
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

		BwAPI bwAPI = new BwAPI(user);
		if (!bwAPI.openBedeworkAPI()) {
			return Boolean.FALSE;
		}
		
		org.bedework.calsvci.CalendarsI calendarsHandler = bwAPI.getCalendarsHandler();
		if (calendarsHandler == null) {
			bwAPI.closeBedeworkAPI();
			return Boolean.FALSE;
		}
		
		try {
			CalendarEntity calendar = getIdegaCalendarById(calendarEntity.getId());

			if (calendar == null) {
				calendarsHandler.add(calendarEntity, calendarEntity.getColPath());
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

		BwAPI bwAPI = new BwAPI(user);
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
	public Integer getNumberOfIdegaCalendarChildren(String calendarPath) {
		if (StringUtil.isEmpty(calendarPath)) {
			LOGGER.log(Level.WARNING, "Calendar path not given.");
			return null;
		}
		
		Collection<CalendarEntity> calendars = getResultList(
				CalendarEntity.GET_NUMBER_OF_CHILD_CALENDARS, 
				CalendarEntity.class,
                new Param(CalendarEntity.COL_PATH_PROP, calendarPath));

		if (ListUtil.isEmpty(calendars)) {
			return 0;
		}
		
		return calendars.size();
	}

	/* (non-Javadoc)
	 * @see com.idega.calendar.data.dao.CalendarDAO#getNumberOfIdegaCalendarEvents(java.lang.String)
	 */
	@Override
	public Integer getNumberOfIdegaCalendarEvents(String calendarPath) {
		if (StringUtil.isEmpty(calendarPath)) {
			LOGGER.log(Level.WARNING, "Calendar path not given.");
			return null;
		}
		
		Collection<BwEventObj> events = getResultList(
				CalendarEntity.GET_NUMBER_OF_EVENTS, 
				BwEventObj.class,
                new Param(CalendarEntity.COL_PATH_PROP, calendarPath));

		if (ListUtil.isEmpty(events)) {
			return 0;
		}
		
		return events.size();
	}

	/* (non-Javadoc)
	 * @see com.idega.calendar.data.dao.CalendarDAO#getUserIdegaCalendarCollections(com.idega.user.data.User)
	 */
	@Override
	public Collection<CalendarEntity> getUserIdegaCalendarCollections() {
		return getResultList(CalendarEntity.GET_PRIVATE_CALENDARS, CalendarEntity.class);
	}

	/* (non-Javadoc)
	 * @see com.idega.calendar.data.dao.CalendarDAO#getIdegaPublicCalendarCollections()
	 */
	@Override
	public Collection<CalendarEntity> getIdegaPublicCalendarCollections() {
		return getResultList(CalendarEntity.GET_PUBLIC_CALENDARS, CalendarEntity.class);
	}

	/* (non-Javadoc)
	 * @see com.idega.calendar.data.dao.CalendarDAO#getIdegaCalendarById(java.lang.String)
	 */
	@Override
	public CalendarEntity getIdegaCalendarById(int id) {
		return this.getSingleResult(
				CalendarEntity.GET_BY_ID, 
				CalendarEntity.class, 
				new Param(CalendarEntity.ID_PROP, id)
				);
	}

	/* (non-Javadoc)
	 * @see com.idega.calendar.data.dao.CalendarDAO#getIdegaCalendarByPath(java.lang.String)
	 */
	@Override
	public CalendarEntity getIdegaCalendarByPath(String calendarPath) {
		if (StringUtil.isEmpty(calendarPath)) {
			LOGGER.log(Level.WARNING, "Calendar path not given.");
			return null;
		}
		
		return this.getSingleResult(
				CalendarEntity.GET_BY_PATH,
				CalendarEntity.class, 
				new Param(CalendarEntity.PATH_PROP, calendarPath)
				);
	}

	/* (non-Javadoc)
	 * @see com.idega.calendar.data.dao.CalendarDAO#getIdegaCalendarsByName(java.lang.String)
	 */
	@Override
	public Collection<CalendarEntity> getIdegaCalendarsByName(String name) {
		if (StringUtil.isEmpty(name)) {
			LOGGER.log(Level.WARNING, "Calendar name not given.");
			return null;
		}
		
		return getResultList(CalendarEntity.GET_BY_NAME, CalendarEntity.class,
                new Param(CalendarEntity.NAME_PROP, name));
	}

	@Override
	public boolean removeCalendar(User user, int calendarID) {
		if (user == null) {
			return Boolean.FALSE;
		}
		
		CalendarEntity calendar = getIdegaCalendarById(calendarID);
		if (calendar == null) {
			return Boolean.FALSE;
		}
		
		return removeCalendar(user, calendar);
	}
}