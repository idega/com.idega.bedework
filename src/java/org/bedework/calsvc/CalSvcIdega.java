/**
 * @(#)CalSvcIdega.java    1.0.0 10:35:59 AM
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
package org.bedework.calsvc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import net.fortuna.ical4j.model.property.DtStamp;

import org.apache.log4j.Logger;
import org.bedework.calcorei.Calintf;
import org.bedework.calcorei.CalintfFactory;
import org.bedework.calcorei.HibSession;
import org.bedework.calfacade.BwCalendar;
import org.bedework.calfacade.BwCategory;
import org.bedework.calfacade.BwContact;
import org.bedework.calfacade.BwLocation;
import org.bedework.calfacade.BwPrincipal;
import org.bedework.calfacade.BwStats;
import org.bedework.calfacade.BwStats.CacheStats;
import org.bedework.calfacade.BwStats.StatsEntry;
import org.bedework.calfacade.BwString;
import org.bedework.calfacade.BwSystem;
import org.bedework.calfacade.BwUser;
import org.bedework.calfacade.RecurringRetrievalMode;
import org.bedework.calfacade.base.BwDbentity;
import org.bedework.calfacade.base.BwShareableDbentity;
import org.bedework.calfacade.base.UpdateFromTimeZonesInfo;
import org.bedework.calfacade.env.CalOptionsFactory;
import org.bedework.calfacade.exc.CalFacadeAccessException;
import org.bedework.calfacade.exc.CalFacadeException;
import org.bedework.calfacade.filter.SimpleFilterParser;
import org.bedework.calfacade.ifs.Directories;
import org.bedework.calfacade.mail.MailerIntf;
import org.bedework.calfacade.svc.BwAuthUser;
import org.bedework.calfacade.svc.BwCalSuite;
import org.bedework.calfacade.svc.EventInfo;
import org.bedework.calfacade.svc.UserAuth;
import org.bedework.calfacade.svc.wrappers.BwCalSuiteWrapper;
import org.bedework.calfacade.util.AccessUtilI;
import org.bedework.calfacade.util.CalFacadeUtil;
import org.bedework.calsvc.client.ClientState;
import org.bedework.calsvc.scheduling.Scheduling;
import org.bedework.calsvc.scheduling.SchedulingIntf;
import org.bedework.calsvci.AdminI;
import org.bedework.calsvci.CalSuitesI;
import org.bedework.calsvci.CalSvcIPars;
import org.bedework.calsvci.CalendarsI;
import org.bedework.calsvci.ClientStateI;
import org.bedework.calsvci.EventProperties;
import org.bedework.calsvci.EventsI;
import org.bedework.calsvci.FiltersI;
import org.bedework.calsvci.HostsI;
import org.bedework.calsvci.IndexingI;
import org.bedework.calsvci.PreferencesI;
import org.bedework.calsvci.ResourcesI;
import org.bedework.calsvci.SchedulingI;
import org.bedework.calsvci.SynchI;
import org.bedework.calsvci.SysparsI;
import org.bedework.calsvci.TimeZonesStoreI;
import org.bedework.calsvci.UsersI;
import org.bedework.calsvci.ViewsI;
import org.bedework.icalendar.IcalCallback;
import org.bedework.icalendar.URIgen;
import org.bedework.sysevents.events.SysEvent;
import org.bedework.sysevents.events.SysEventBase;
import org.hibernate.exception.ConstraintViolationException;

import com.idega.bedework.BedeworkConstants;

import edu.rpi.cmt.access.Access;
import edu.rpi.cmt.access.AccessException;
import edu.rpi.cmt.access.AccessPrincipal;
import edu.rpi.cmt.access.Ace;
import edu.rpi.cmt.access.AceWho;
import edu.rpi.cmt.access.Acl.CurrentAccess;
import edu.rpi.cmt.access.PrivilegeDefs;
import edu.rpi.cmt.access.PrivilegeSet;
import edu.rpi.cmt.security.PwEncryptionIntf;
import edu.rpi.cmt.timezones.Timezones;
import edu.rpi.sss.util.OptionsI;
import edu.rpi.sss.util.Util;

/**
 * Class description goes here.
 * <p>
 * You can report about problems to: <a
 * href="mailto:martynas@idega.com">Martynas Stakė</a>
 * </p>
 * <p>
 * You can expect to find some test cases notice in the end of the file.
 * </p>
 * 
 * @version 1.0.0 Apr 4, 2012
 * @author martynasstake
 */
public class CalSvcIdega extends CalSvc {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1865126339401920077L;

	private String systemName;

	private CalSvcIPars pars;

	private boolean debug;

	private boolean open;

	private boolean superUser;

	/**
	 * True if this is a session to create a new account. Do not try to create
	 * one
	 */
	private boolean creating;

	/*
	 * The account that we are representing
	 */
	private BwUser currentUser;

	/*
	 * The account we logged in as - for user access equals currentUser, for
	 * admin access currentUser is the group we are managing.
	 */
	private BwUser currentAuthUser;

	/*
	 * If we're doing admin this is the authorised user entry
	 */
	BwAuthUser adminUser;

	/* ....................... Handlers ..................................... */

	// private MailerIntf mailer;

	private HostsI hostsHandler;

	private PreferencesI prefsHandler;

	private AdminI adminHandler;

	private EventsI eventsHandler;

	private FiltersI filtersHandler;

	private CalendarsI calendarsHandler;

	private SysparsI sysparsHandler;

	private CalSuitesI calSuitesHandler;

	private IndexingI indexingHandler;

	private ResourcesI resourcesHandler;

	private SchedulingIntf sched;

	private SynchI synch;

	private UsersI usersHandler;

	private ViewsI viewsHandler;

	private EventProperties<BwCategory> categoriesHandler;

	private EventProperties<BwLocation> locationsHandler;

	private EventProperties<BwContact> contactsHandler;

	private Collection<CalSvcDb> handlers = new ArrayList<CalSvcDb>();

	/* ....................... ... ..................................... */

	private ClientState clientState;

	/**
	 * Core calendar interface
	 */
	private transient Calintf cali;

	private transient PwEncryptionIntf pwEncrypt;

	/**
	 * handles timezone info.
	 */
	private Timezones timezones;
	private TimeZonesStoreI tzstore;

	/* null if timezones not initialised */
	private static String tzserverUri = null;

	/**
	 * The user authorisation object
	 */
	private UserAuth userAuth;

	private transient UserAuth.CallBack uacb;

	private transient Directories.CallBack gcb;

	/**
	 * @author douglm
	 * 
	 */
	private class AccessUtilCb extends AccessUtilI.CallBack {

		private static final long serialVersionUID = 617663900476779521L;
		CalSvcIdega svci;

		AccessUtilCb(final CalSvcIdega svci) {
			this.svci = svci;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.bedework.calfacade.util.AccessUtilI.CallBack#getPrincipal(java
		 * .lang.String)
		 */
		@Override
		public AccessPrincipal getPrincipal(final String href)
				throws CalFacadeException {
			return svci.getUsersHandler().getPrincipal(href);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.bedework.calfacade.util.AccessUtilI.CallBack#getUserCalendarRoot
		 * ()
		 */
		@Override
		public String getUserCalendarRoot() throws CalFacadeException {
			return getSysparsHandler().get().getUserCalendarRoot();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see edu.rpi.cmt.access.Access.AccessCb#makeHref(java.lang.String,
		 * int)
		 */
		@Override
		public String makeHref(final String id, final int whoType)
				throws AccessException {
			try {
				return svci.getDirectories().makePrincipalUri(id, whoType);
			} catch (Throwable t) {
				throw new AccessException(t);
			}
		}

	}

	/**
	 * Class used by UseAuth to do calls into CalSvci
	 * 
	 */
	public static class UserAuthCallBack extends UserAuth.CallBack {

		private static final long serialVersionUID = -4897660907491982492L;
		CalSvc svci;

		UserAuthCallBack(final CalSvc svci) {
			this.svci = svci;
		}

		@Override
		public BwUser getUser(final String account) throws CalFacadeException {
			return svci.getUsersHandler().getUser(account);
		}

		@Override
		public UserAuth getUserAuth() throws CalFacadeException {
			return svci.getUserAuth();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.bedework.calfacade.svc.UserAuth.CallBack#getDbSession()
		 */
		@Override
		public Object getDbSession() throws CalFacadeException {
			return svci.getCal().getDbSession();
		}
	}

	/**
	 * Class used by groups implementations for calls into CalSvci
	 * 
	 */
	public static class GroupsCallBack extends Directories.CallBack {

		private static final long serialVersionUID = -127454255262670496L;
		CalSvc svci;

		GroupsCallBack(final CalSvc svci) {
			this.svci = svci;
		}

		@Override
		public String getSysid() throws CalFacadeException {
			return svci.getSysparsHandler().getSysid();
		}

		@Override
		public BwUser getUser(final String account) throws CalFacadeException {
			return svci.getUsersHandler().getUser(account);
		}

		@Override
		public BwUser getCurrentUser() throws CalFacadeException {
			return svci.getUser();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.bedework.calfacade.svc.UserAuth.CallBack#getDbSession()
		 */
		@Override
		public Object getDbSession() throws CalFacadeException {
			return svci.getCal().getDbSession();
		}
	}

	/**
	 * The user groups object.
	 */
	private Directories userGroups;

	/**
	 * The admin groups object.
	 */
	private Directories adminGroups;

	private IcalCallback icalcb;

	/*
	 * These are only relevant for the public admin client.
	 */
	// private boolean adminAutoDeleteSponsors;
	// private boolean adminAutoDeleteLocations;

	private transient Logger log;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.calsvci.CalSvcI#init(org.bedework.calsvci.CalSvcIPars)
	 */
	@Override
	public void init(final CalSvcIPars parsParam) throws CalFacadeException {
		init(parsParam, false);
	}

	private void init(final CalSvcIPars parsParam, final boolean creating)
			throws CalFacadeException {
		pars = (CalSvcIPars) parsParam.clone();

		this.creating = creating;

		debug = getLogger().isDebugEnabled();

		fixUsers();

		try {
			OptionsI opts = CalOptionsFactory.getOptions();
			systemName = (String) opts.getGlobalProperty("systemName");

			open();
			beginTransaction();

			if (userGroups != null) {
				userGroups.init(getGroupsCallBack());
			}

			if (adminGroups != null) {
				adminGroups.init(getGroupsCallBack());
			}

			BwSystem sys = getSysparsHandler().get();

			if (tzserverUri == null) {
				tzserverUri = CalOptionsFactory.getOptions()
						.getGlobalStringProperty("timezonesUri");

				if (tzserverUri == null) {
					throw new CalFacadeException(
							"No timezones server URI defined");
				}

				Timezones.initTimezones(tzserverUri);

				Timezones.setSystemDefaultTzid(sys.getTzid());
			}

			/*
			 * Some checks on parameter validity
			 */
			// BwUser =

			tzstore = new TimeZonesStoreImpl(getCalSvc());

			/* Nominate our timezone registry */
			System.setProperty("net.fortuna.ical4j.timezone.registry",
					"org.bedework.icalendar.TimeZoneRegistryFactoryImpl");

			if (!creating) {
				String tzid = getPrefsHandler().get().getDefaultTzid();

				if (tzid != null) {
					Timezones.setThreadDefaultTzid(tzid);
				}

				// if (pars.getCaldav() && !pars.isGuest()) {
				if (!pars.isGuest()) {
					/* Ensure scheduling resources exist */
					getCal().getSpecialCalendar(getUser(),
							BwCalendar.calTypeInbox, true,
							PrivilegeDefs.privAny);

					getCal().getSpecialCalendar(getUser(),
							BwCalendar.calTypeOutbox, true,
							PrivilegeDefs.privAny);
				}

				if ((pars.getPublicAdmin() || pars.getAllowSuperUser())
						&& (pars.getAuthUser() != null)) {
					setSuperUser(getSysparsHandler()
							.isRootUser(currentAuthUser));
				}
			}

			postNotification(SysEvent.makePrincipalEvent(
					SysEvent.SysCode.USER_SVCINIT, getUser()));
		} catch (CalFacadeException cfe) {
			rollbackTransaction();
			cfe.printStackTrace();
			throw cfe;
		} catch (Throwable t) {
			rollbackTransaction();
			t.printStackTrace();
			throw new CalFacadeException(t);
		} finally {
			try {
				endTransaction();
			} catch (Throwable t1) {
			}
			try {
				close();
			} catch (Throwable t2) {
			}
		}
	}

	void setSuperUser(final boolean val) throws CalFacadeException {
		superUser = val;
		getCal().setSuperUser(val);
	}

	@Override
	public boolean getSuperUser() {
		return superUser;
	}

	@Override
	public BwStats getStats() throws CalFacadeException {
		BwStats stats = getCal().getStats();

		if (timezones != null) {
			CacheStats cs = stats.getDateCacheStats();

			cs.setHits(timezones.getDateCacheHits());
			cs.setMisses(timezones.getDateCacheMisses());
			cs.setCached(timezones.getDatesCached());
		}

		stats.setAccessStats(Access.getStatistics());

		return stats;
	}

	@Override
	public void setDbStatsEnabled(final boolean enable)
			throws CalFacadeException {
		// if (!pars.getPublicAdmin()) {
		// throw new CalFacadeAccessException();
		// }

		getCal().setDbStatsEnabled(enable);
	}

	@Override
	public boolean getDbStatsEnabled() throws CalFacadeException {
		return getCal().getDbStatsEnabled();
	}

	@Override
	public void dumpDbStats() throws CalFacadeException {
		// if (!pars.getPublicAdmin()) {
		// throw new CalFacadeAccessException();
		// }

		trace(getStats().toString());
		getCal().dumpDbStats();
	}

	@Override
	public Collection<StatsEntry> getDbStats() throws CalFacadeException {
		// if (!pars.getPublicAdmin()) {
		// throw new CalFacadeAccessException();
		// }

		return getCal().getDbStats();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.calsvci.CalSvcI#logStats()
	 */
	@Override
	public void logStats() throws CalFacadeException {
		logIt(getStats().toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.bedework.calsvci.CalSvcI#postNotification(org.bedework.sysevents.
	 * events.SysEvent)
	 */
	@Override
	public void postNotification(final SysEventBase ev)
			throws CalFacadeException {
		getCal().postNotification(ev);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.calsvci.CalSvcI#flushAll()
	 */
	@Override
	public void flushAll() throws CalFacadeException {
		getCal().flush();
	}

	@Override
	public void open() throws CalFacadeException {
		// TimeZoneRegistryImpl.setThreadCb(getIcalCallback());

		if (open) {
			return;
		}

		open = true;
		getCal().open(pars.getWebMode());

		for (CalSvcDb handler : handlers) {
			handler.open();
		}
	}

	@Override
	public boolean isOpen() {
		return open;
	}

	@Override
	public boolean isRolledback() throws CalFacadeException {
		if (!open) {
			return false;
		}

		return getCal().isRolledback();
	}

	@Override
	public void close() throws CalFacadeException {
		open = false;
		getCal().close();

		for (CalSvcDb handler : handlers) {
			handler.close();
		}
	}

	@Override
	public void beginTransaction() throws CalFacadeException {
		getCal().beginTransaction();
	}

	@Override
	public void endTransaction() throws CalFacadeException {
		getCal().endTransaction();
	}

	@Override
	public void rollbackTransaction() throws CalFacadeException {
		getCal().rollbackTransaction();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.bedework.calsvci.CalSvcI#reAttach(org.bedework.calfacade.base.BwDbentity
	 * )
	 */
	@Override
	public void reAttach(final BwDbentity val) throws CalFacadeException {
		getCal().reAttach(val);
	}

	@Override
	public BwDbentity merge(final BwDbentity val) throws CalFacadeException {
		return (BwDbentity) ((HibSession) getCal().getDbSession()).merge(val);
	}

	@Override
	public IcalCallback getIcalCallback() {
		if (icalcb == null) {
			icalcb = new IcalCallbackcb();
		}

		return icalcb;
	}

	@Override
	public boolean refreshNeeded() throws CalFacadeException {
		/* See if the events were updated */
		return false;
	}

	/*
	 * ====================================================================
	 * Factory methods
	 * ====================================================================
	 */

	class SvcSimpleFilterParser extends SimpleFilterParser {
		@Override
		public BwCategory getCategoryByName(final String name)
				throws CalFacadeException {
			BwString s = new BwString(null, name);
			return getCategoriesHandler().find(s,
					getUsersHandler().getPublicUser().getPrincipalRef());
		}

		@Override
		public BwCategory getCategory(final String uid)
				throws CalFacadeException {
			return getCategoriesHandler().get(uid);
		}
	}

	@Override
	public SimpleFilterParser getFilterParser() throws CalFacadeException {
		return new SvcSimpleFilterParser();
	}

	@Override
	public HostsI getHostsHandler() throws CalFacadeException {
		if (hostsHandler == null) {
			hostsHandler = new Hosts(getCalSvc(), null);
			handlers.add((CalSvcDb) hostsHandler);
		}

		return hostsHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.calsvci.CalSvcI#getSysparsHandler()
	 */
	@Override
	public SysparsI getSysparsHandler() throws CalFacadeException {
		if (sysparsHandler == null) {
			sysparsHandler = new Syspars(getCalSvc(), null, systemName);
			handlers.add((CalSvcDb) sysparsHandler);
		}

		return sysparsHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.calsvci.CalSvcI#getMailer()
	 */
	@Override
	public MailerIntf getMailer() throws CalFacadeException {
		/*
		 * if (mailer != null) { return mailer; }
		 */

		try {
			MailerIntf mailer = (MailerIntf) CalFacadeUtil.getObject(
					getSysparsHandler().get().getMailerClass(),
					MailerIntf.class);
			mailer.init();

			return mailer;
		} catch (Throwable t) {
			throw new CalFacadeException(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.calsvci.CalSvcI#getPrefsHandler()
	 */
	@Override
	public PreferencesI getPrefsHandler() throws CalFacadeException {
		if (prefsHandler == null) {
			prefsHandler = new Preferences(getCalSvc(), getUser());
			handlers.add((CalSvcDb) prefsHandler);
		}

		return prefsHandler;
	}

	@Override
	public AdminI getAdminHandler() throws CalFacadeException {
		if (!isPublicAdmin()) {
			throw new CalFacadeAccessException();
		}

		if (adminHandler == null) {
			adminHandler = new Admin(getCalSvc(), getUser());
			handlers.add((CalSvcDb) adminHandler);
		}

		return adminHandler;
	}

	@Override
	public EventsI getEventsHandler() throws CalFacadeException {
		if (eventsHandler == null) {
			eventsHandler = new Events(getCalSvc(), getUser());
			handlers.add((CalSvcDb) eventsHandler);
		}

		return eventsHandler;
	}

	@Override
	public FiltersI getFiltersHandler() throws CalFacadeException {
		if (filtersHandler == null) {
			filtersHandler = new Filters(getCalSvc(), getUser());
			handlers.add((CalSvcDb) filtersHandler);
		}

		return filtersHandler;
	}

	@Override
	public CalendarsI getCalendarsHandler() throws CalFacadeException {
		if (calendarsHandler == null) {
			calendarsHandler = new Calendars(getCalSvc(), getUser());
			handlers.add((CalSvcDb) calendarsHandler);
		}

		return calendarsHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.calsvci.CalSvcI#getCalSuitesHandler()
	 */
	@Override
	public CalSuitesI getCalSuitesHandler() throws CalFacadeException {
		if (calSuitesHandler == null) {
			calSuitesHandler = new CalSuites(getCalSvc(), getUser());
			handlers.add((CalSvcDb) calSuitesHandler);
		}

		return calSuitesHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.calsvci.CalSvcI#getIndexingHandler()
	 */
	@Override
	public IndexingI getIndexingHandler() throws CalFacadeException {
		if (indexingHandler == null) {
			indexingHandler = new Indexing(getCalSvc(), getUser());
			handlers.add((CalSvcDb) indexingHandler);
		}

		return indexingHandler;
	}

	@Override
	public ResourcesI getResourcesHandler() throws CalFacadeException {
		if (resourcesHandler == null) {
			resourcesHandler = new ResourcesImpl(getCalSvc(), getUser());
			handlers.add((CalSvcDb) resourcesHandler);
		}

		return resourcesHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.calsvci.CalSvcI#getScheduler()
	 */
	@Override
	public SchedulingI getScheduler() throws CalFacadeException {
		if (sched == null) {
			sched = new Scheduling(getCalSvc(), getUser());
			handlers.add((CalSvcDb) sched);
		}

		return sched;
	}

	@Override
	public SynchI getSynch() throws CalFacadeException {
		if (synch == null) {
			synch = new Synch(getCalSvc(), getUser());
			handlers.add((CalSvcDb) synch);
		}

		return synch;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.calsvci.CalSvcI#getUsersHandler()
	 */
	@Override
	public UsersI getUsersHandler() throws CalFacadeException {
		if (usersHandler == null) {
			usersHandler = new Users(getCalSvc(), null);
			handlers.add((CalSvcDb) usersHandler);

			/* Cannot call getUser() until initialised */
			((CalSvcDb) usersHandler).setPrincipal(getUser());
		}

		return usersHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.calsvci.CalSvcI#getViewsHandler()
	 */
	@Override
	public ViewsI getViewsHandler() throws CalFacadeException {
		if (viewsHandler == null) {
			viewsHandler = new Views(getCalSvc(), getUser());
			handlers.add((CalSvcDb) viewsHandler);
		}

		return viewsHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.calsvci.CalSvcI#getDirectories()
	 */
	@Override
	public Directories getDirectories() throws CalFacadeException {
		if (isPublicAdmin()) {
			return getAdminDirectories();
		}

		return getUserDirectories();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.calsvci.CalSvcI#getUserDirectories()
	 */
	@Override
	public Directories getUserDirectories() throws CalFacadeException {
		if (userGroups != null) {
			return userGroups;
		}

		try {
			userGroups = (Directories) CalFacadeUtil.getObject(
					getSysparsHandler().get().getUsergroupsClass(),
					Directories.class);
			userGroups.init(getGroupsCallBack());
		} catch (Throwable t) {
			throw new CalFacadeException(t);
		}

		return userGroups;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.calsvci.CalSvcI#getAdminDirectories()
	 */
	@Override
	public Directories getAdminDirectories() throws CalFacadeException {
		if (adminGroups != null) {
			return adminGroups;
		}

		try {
			adminGroups = (Directories) CalFacadeUtil.getObject(
					getSysparsHandler().get().getAdmingroupsClass(),
					Directories.class);
			adminGroups.init(getGroupsCallBack());
		} catch (Throwable t) {
			throw new CalFacadeException(t);
		}

		return adminGroups;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.calsvci.CalSvcI#getCategoriesHandler()
	 */
	@Override
	public EventProperties<BwCategory> getCategoriesHandler()
			throws CalFacadeException {
		if (categoriesHandler == null) {
			categoriesHandler = new EventPropertiesImpl<BwCategory>(
					getCalSvc(), getUser());
			categoriesHandler.init("word", "word", BwCategory.class.getName(),
					"getCategoryRefs", "removeCategoryPrefForAll",
					pars.getAdminCanEditAllPublicCategories());
			handlers.add((CalSvcDb) categoriesHandler);
		}

		return categoriesHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.calsvci.CalSvcI#getLocationsHandler()
	 */
	@Override
	public EventProperties<BwLocation> getLocationsHandler()
			throws CalFacadeException {
		if (locationsHandler == null) {
			locationsHandler = new EventPropertiesImpl<BwLocation>(getCalSvc(),
					getUser());
			locationsHandler.init("uid", "address", BwLocation.class.getName(),
					"getLocationRefs", "removeLocationPrefForAll",
					pars.getAdminCanEditAllPublicLocations());
			handlers.add((CalSvcDb) locationsHandler);
		}

		return locationsHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.calsvci.CalSvcI#getContactsHandler()
	 */
	@Override
	public EventProperties<BwContact> getContactsHandler()
			throws CalFacadeException {
		if (contactsHandler == null) {
			contactsHandler = new EventPropertiesImpl<BwContact>(getCalSvc(),
					getUser());
			contactsHandler.init("uid", "name", BwContact.class.getName(),
					"getContactRefs", "removeContactPrefForAll",
					pars.getAdminCanEditAllPublicContacts());
			handlers.add((CalSvcDb) contactsHandler);
		}

		return contactsHandler;
	}

	/*
	 * ====================================================================
	 * Users and accounts
	 * ====================================================================
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.calsvci.CalSvcI#getUser()
	 */
	@Override
	public BwUser getUser() throws CalFacadeException {
		// if (pars.isGuest()) {
		// return getUsersHandler().getPublicUser();
		// }

		return currentUser;
	}

	@Override
	public UserAuth getUserAuth() throws CalFacadeException {
		if (userAuth != null) {
			return userAuth;
		}

		userAuth = (UserAuth) CalFacadeUtil.getObject(getSysparsHandler().get()
				.getUserauthClass(), UserAuth.class);

		userAuth.initialise(getUserAuthCallBack());

		return userAuth;
	}

	/*
	 * ====================================================================
	 * ClientState
	 * ====================================================================
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.calsvci.CalSvcI#getClientState()
	 */
	@Override
	public ClientStateI getClientState() throws CalFacadeException {
		if (clientState == null) {
			clientState = new ClientState(getCalSvc(), getUser());
			handlers.add(clientState);
		}

		return clientState;
	}

	/*
	 * ====================================================================
	 * Access
	 * ====================================================================
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.bedework.calsvci.CalSvcI#changeAccess(org.bedework.calfacade.base
	 * .BwShareableDbentity, java.util.Collection)
	 */
	@Override
	public void changeAccess(BwShareableDbentity ent,
			final Collection<Ace> aces, final boolean replaceAll)
			throws CalFacadeException {
		if (ent instanceof BwCalSuiteWrapper) {
			ent = ((BwCalSuiteWrapper) ent).fetchEntity();
		}
		getCal().changeAccess(ent, aces, replaceAll);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.bedework.calsvci.CalSvcI#defaultAccess(org.bedework.calfacade.base
	 * .BwShareableDbentity, edu.rpi.cmt.access.AceWho)
	 */
	@Override
	public void defaultAccess(BwShareableDbentity ent, final AceWho who)
			throws CalFacadeException {
		if (ent instanceof BwCalSuiteWrapper) {
			ent = ((BwCalSuiteWrapper) ent).fetchEntity();
		}
		getCal().defaultAccess(ent, who);
	}

	@Override
	public Collection<? extends BwShareableDbentity<? extends Object>> checkAccess(
			final Collection<? extends BwShareableDbentity<? extends Object>> ents,
			final int desiredAccess, final boolean alwaysReturn)
			throws CalFacadeException {
		return getCal().checkAccess(ents, desiredAccess, alwaysReturn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.bedework.calsvci.CalSvcI#checkAccess(org.bedework.calfacade.base.
	 * BwShareableDbentity, int, boolean)
	 */
	@Override
	public CurrentAccess checkAccess(final BwShareableDbentity ent,
			final int desiredAccess, final boolean returnResult)
			throws CalFacadeException {
		return getCal().checkAccess(ent, desiredAccess, returnResult);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.calsvci.CalSvcI#getSynchReport(java.lang.String,
	 * java.lang.String, int, boolean)
	 */
	@Override
	public SynchReport getSynchReport(final String path, final String token,
			final int limit, final boolean recurse) throws CalFacadeException {
		BwCalendar col = getCalendarsHandler().get(path);
		if (col == null) {
			throw new CalFacadeAccessException();
		}

		SynchReport res = new SynchReport();
		res.items = new ArrayList<SynchReportItem>();
		res.token = getSynchItems(col, token, res.items, recurse);

		if ((limit > 0) && (res.items.size() >= limit)) {
			if (res.items.size() == limit) {
				return res;
			}

			List<SynchReportItem> items = new ArrayList<SynchReportItem>();
			res.token = "";

			for (SynchReportItem item : res.items) {
				if (item.lastmod.compareTo(res.token) > 0) {
					res.token = item.lastmod;
				}

				items.add(item);

				if (items.size() == limit) {
					res.items = items;
					break;
				}
			}
		}

		if (res.token.length() == 0) {
			res.token = new DtStamp().getValue() + "-0000";
		}

		return res;
	}

	private String getSynchItems(final BwCalendar col, final String token,
			final List<SynchReportItem> items, final boolean recurse)
			throws CalFacadeException {
		Events eventsH = (Events) getEventsHandler();
		Calendars colsH = (Calendars) getCalendarsHandler();
		String newToken = "";

		if (debug) {
			trace("sync token: " + token + " col: " + col.getPath());
		}

		Set<EventInfo> evs = eventsH.getSynchEvents(col.getPath(), token);

		for (EventInfo ei : evs) {
			String t = ei.getEvent().getCtoken();

			if (t.compareTo(newToken) > 0) {
				newToken = t;
			}
			items.add(new SynchReportItem(ei));
		}

		Set<BwCalendar> cols = colsH.getSynchCols(col.getPath(), token);

		for (BwCalendar c : cols) {
			String t = c.getLastmod().getTagValue();

			if (t.compareTo(newToken) > 0) {
				newToken = t;
			}

			if (debug) {
				trace("     add col: " + c.getPath());
			}

			items.add(new SynchReportItem(c));
		}

		if (!recurse) {
			return newToken;
		}

		Collection<BwCalendar> chilren = colsH.getChildren(col);

		if (Util.isEmpty(chilren)) {
			return newToken;
		}

		for (BwCalendar c : chilren) {
			String t = getSynchItems(c, token, items, true);

			if (t.compareTo(newToken) > 0) {
				newToken = t;
			}
		}

		return newToken;
	}

	/*
	 * ====================================================================
	 * Timezones
	 * ====================================================================
	 */

	@Override
	public UpdateFromTimeZonesInfo updateFromTimeZones(final int limit,
			final boolean checkOnly, final UpdateFromTimeZonesInfo info)
			throws CalFacadeException {
		return tzstore.updateFromTimeZones(limit, checkOnly, info);
	}

	/*
	 * ==================================================================== Get
	 * back end interface
	 * ====================================================================
	 */

	/*
	 * This will get a calintf based on the supplied collection object.
	 */
	Calintf getCal(final BwCalendar cal) throws CalFacadeException {
		return getCal();
	}

	/*
	 * We need to synchronize this code to prevent stale update exceptions. db
	 * locking might be better - this could still fail in a clustered
	 * environment for example.
	 */
	private static volatile Object synchlock = new Object();

	/*
	 * Currently this gets a local calintf only. Later we need to use a par to
	 * get calintf from a table.
	 */
	Calintf getCal() throws CalFacadeException {
		if (cali != null) {
			return cali;
		}

		synchronized (synchlock) {
			cali = CalintfFactory.getIntf(BedeworkConstants.CLASS_IDEGA_CALINF_IMPL);

			try {
				Properties props = new Properties();
				if (pars.getDbPars() != null) {
					if (pars.getDbPars().getCachePrefix() != null) {
						props.setProperty("cachePrefix", pars.getDbPars()
								.getCachePrefix());
						props.setProperty("cachingOn",
								String.valueOf(pars.getDbPars().getCachingOn()));
					}
				}

				cali.initDb(props);
				cali.open(pars.getWebMode()); // Just for the user interactions
				cali.beginTransaction();

				String runAsUser = pars.getUser();

				if (pars.getCalSuite() != null) {
					BwCalSuite cs = CalSuites.fetch(cali.getDbSession(),
							pars.getCalSuite());

					if (cs == null) {
						error("******************************************************");
						error("Unable to fetch calendar suite "
								+ pars.getCalSuite());
						error("Is the database correctly initialised?");
						error("******************************************************");
						throw new CalFacadeException(
								CalFacadeException.unknownCalsuite,
								pars.getCalSuite());
					}

					getCalSuitesHandler().set(new BwCalSuiteWrapper(cs));
					/*
					 * For administrative use we use the account of the admin
					 * group the user is a direct member of
					 * 
					 * For public clients we use the calendar suite owning
					 * group.
					 */
					if (!pars.getPublicAdmin()) {
						runAsUser = cs.getGroup().getOwnerHref();
					}
				}

				/* Get ourselves a user object */
				String authenticatedUser = pars.getAuthUser();

				Users users = (Users) getUsersHandler();

				if (runAsUser == null) {
					runAsUser = authenticatedUser;
				}

				if (authenticatedUser == null) {
					// Unauthenticated use
					currentUser = users.getUser(runAsUser);
					if (currentUser == null) {
						// XXX Should we set this one up?
						currentUser = new BwUser();
					}

					currentUser.setUnauthenticated(true);
					currentAuthUser = currentUser;
				} else {
					currentUser = users.getUser(authenticatedUser);
					if (currentUser == null) {
						/*
						 * Add the user to the database. Presumably this is
						 * first logon
						 */
						getLogger().debug("Add new user " + authenticatedUser);

						currentUser = addUser(authenticatedUser);
					}

					currentAuthUser = currentUser;

					if (authenticatedUser.equals(runAsUser)) {
						getLogger().debug(
								"Authenticated user " + authenticatedUser
										+ " logged on");
					} else {
						currentUser = users.getUser(runAsUser);
						if (currentUser == null) {
							// throw new CalFacadeException("User " + runAsUser
							// + " does not exist.");
							/*
							 * Add the user to the database. Presumably this is
							 * first logon
							 */
							getLogger().debug(
									"Add new run-as-user " + runAsUser);

							currentUser = addUser(runAsUser);
						}

						getLogger().debug(
								"Authenticated user " + authenticatedUser
										+ " logged on - running as "
										+ runAsUser);
					}

					currentUser.setGroups(getDirectories().getAllGroups(
							currentUser));
					currentUser.setPrincipalInfo(getDirectories().getDirInfo(
							currentUser));
				}

				cali.init(getSysparsHandler().get(), new AccessUtilCb(this), null, currentUser, pars.getPublicAdmin(),
						pars.getSessionsless());

				if (!currentUser.getUnauthenticated()) {
					if (pars.getService()) {
						postNotification(SysEvent.makePrincipalEvent(
								SysEvent.SysCode.SERVICE_USER_LOGIN,
								currentUser));
					} else if (!creating) {
						users.logon(currentUser);

						postNotification(SysEvent.makePrincipalEvent(
								SysEvent.SysCode.USER_LOGIN, currentUser));
					}
				} else {
					getCal().setMaximumAllowedPrivs(
							PrivilegeSet.readOnlyPrivileges);

					// If we have a runAsUser it's a public client. Pretend we
					// authenticated
					currentUser.setUnauthenticated(runAsUser == null);
				}

				if (pars.getPublicAdmin() || pars.isGuest()) {
					if (debug) {
						trace("PublicAdmin: " + pars.getPublicAdmin()
								+ " user: " + runAsUser);
					}

					/*
					 * We may be running as a different user. The preferences we
					 * want to see are those of the user we are running as -
					 * i.e. the 'run.as' user not those of the authenticated
					 * user.
					 */

					BwCalSuiteWrapper suite = getCalSuitesHandler().get();
					BwUser user;

					if (suite != null) {
						// Use this user
						user = (BwUser) users.getPrincipal(suite.getGroup()
								.getOwnerHref());
					} else if (runAsUser == null) {
						// Unauthenticated CalDAV for example?
						user = currentUser;
					} else {
						// No calendar suite set up

						// XXX This is messy
						if (runAsUser.startsWith("/")) {
							user = (BwUser) users.getPrincipal(runAsUser);
						} else {
							user = users.getUser(runAsUser);
						}
					}

					((CalSvcDb) getPrefsHandler()).setPrincipal(user);
					((CalSvcDb) getCalSuitesHandler()).setPrincipal(user);
				}

				return cali;
			} catch (CalFacadeException cfe) {
				error(cfe);
				throw cfe;
			} catch (Throwable t) {
				error(t);
				throw new CalFacadeException(t);
			} finally {
				cali.endTransaction();
				cali.close();
				// cali.flushAll();
			}
		}
	}

	void initPrincipal(final BwPrincipal p) throws CalFacadeException {
		getCal().addNewCalendars(p);
	}

	/*
	 * Create the user. Get a new CalSvc object for that purpose.
	 */
	BwUser addUser(final String val) throws CalFacadeException {
		Users users = (Users) getUsersHandler();

		/*
		 * Run this in a separate transaction to ensure we don't fail if the
		 * user gets created by a concurrent process.
		 */

		if (creating) {
			// Get a fake user
			return users.initUserObject(val);
		}

		CalSvcIdega nsvc = new CalSvcIdega();

		nsvc.init(pars, true);

		try {
			nsvc.open();
			nsvc.beginTransaction();

			Users nusers = (Users) nsvc.getUsersHandler();

			nusers.createUser(val);
		} catch (CalFacadeException cfe) {
			nsvc.rollbackTransaction();
			if (debug) {
				cfe.printStackTrace();
			}
			throw cfe;
		} catch (Throwable t) {
			nsvc.rollbackTransaction();
			if (debug) {
				t.printStackTrace();
			}
			throw new CalFacadeException(t);
		} finally {
			try {
				nsvc.endTransaction();
			} catch (CalFacadeException cfe) {
				if (!(cfe.getCause() instanceof ConstraintViolationException)) {
					throw cfe;
				}

				// Othewise we'll assume it was created by another process.
				warn("ConstraintViolationException trying to create " + val);
			} finally {
				nsvc.close();
			}
		}

		return users.getUser(val);
	}

	private UserAuthCallBack getUserAuthCallBack() {
		if (uacb == null) {
			uacb = new UserAuthCallBack(getCalSvc());
		}

		return (UserAuthCallBack) uacb;
	}

	private GroupsCallBack getGroupsCallBack() {
		if (gcb == null) {
			gcb = new GroupsCallBack(getCalSvc());
		}

		return (GroupsCallBack) gcb;
	}

	private class IcalCallbackcb implements IcalCallback {

		private static final long serialVersionUID = -3621810806788474427L;
		private int strictness = conformanceRelaxed;

		@Override
		public void setStrictness(final int val) throws CalFacadeException {
			strictness = val;
		}

		@Override
		public int getStrictness() throws CalFacadeException {
			return strictness;
		}

		@Override
		public BwUser getUser() throws CalFacadeException {
			return getCalSvc().getUser();
		}

		@Override
		public BwUser getOwner() throws CalFacadeException {
			if (isPublicAdmin()) {
				return getUsersHandler().getPublicUser();
			}

			return getCalSvc().getUser();
		}

		@Override
		public String getCaladdr(final String val) throws CalFacadeException {
			return getDirectories().userToCaladdr(val);
		}

		@Override
		public BwCategory findCategory(final BwString val)
				throws CalFacadeException {
			return getCategoriesHandler().find(val,
					getOwner().getPrincipalRef());
		}

		@Override
		public void addCategory(final BwCategory val) throws CalFacadeException {
			getCategoriesHandler().add(val);
		}

		@Override
		public BwContact getContact(final String uid) throws CalFacadeException {
			return getContactsHandler().get(uid);
		}

		@Override
		public BwContact findContact(final BwString val)
				throws CalFacadeException {
			return getContactsHandler().find(val, getOwner().getPrincipalRef());
		}

		@Override
		public void addContact(final BwContact val) throws CalFacadeException {
			getContactsHandler().add(val);
		}

		@Override
		public BwLocation getLocation(final String uid)
				throws CalFacadeException {
			return getLocationsHandler().get(uid);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.bedework.icalendar.IcalCallback#findLocation(org.bedework.calfacade
		 * .BwString)
		 */
		@Override
		public BwLocation findLocation(final BwString address)
				throws CalFacadeException {
			BwLocation loc = BwLocation.makeLocation();
			loc.setAddress(address);

			return getLocationsHandler().ensureExists(loc,
					getOwner().getPrincipalRef()).entity;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.bedework.icalendar.IcalCallback#addLocation(org.bedework.calfacade
		 * .BwLocation)
		 */
		@Override
		public void addLocation(final BwLocation val) throws CalFacadeException {
			getLocationsHandler().add(val);
		}

		@Override
		public Collection<org.bedework.calfacade.svc.EventInfo> getEvent(final BwCalendar cal, final String guid,
				final String rid, final RecurringRetrievalMode recurRetrieval)
				throws CalFacadeException {
			return getEventsHandler().get(cal.getPath(), guid, rid,
					recurRetrieval, false);
		}

		@Override
		public URIgen getURIgen() throws CalFacadeException {
			return null;
		}

		@Override
		public boolean getTimezonesByReference() throws CalFacadeException {
			return pars.getTimezonesByReference();
		}
	}

	/*
	 * Remove trailing "/" from user principals.
	 */
	private void fixUsers() {
		String auser = pars.getAuthUser();
		while ((auser != null) && (auser.endsWith("/"))) {
			auser = auser.substring(0, auser.length() - 1);
		}

		pars.setAuthUser(auser);
	}

	/*
	 * ====================================================================
	 * Package private methods
	 * ====================================================================
	 */

	PwEncryptionIntf getEncrypter() throws CalFacadeException {
		if (pwEncrypt != null) {
			return pwEncrypt;
		}

		try {
			OptionsI opts = CalOptionsFactory.getOptions();
			String pwEncryptClass = "edu.rpi.cmt.security.PwEncryptionDefault";
			// String pwEncryptClass =
			// getSysparsHandler().get().getPwEncryptClass();

			pwEncrypt = (PwEncryptionIntf) CalFacadeUtil.getObject(
					pwEncryptClass, PwEncryptionIntf.class);

			pwEncrypt.init((String) opts.getGlobalProperty("privKeys"),
					(String) opts.getGlobalProperty("pubKeys"));

			return pwEncrypt;
		} catch (CalFacadeException cfe) {
			cfe.printStackTrace();
			throw cfe;
		} catch (Throwable t) {
			t.printStackTrace();
			throw new CalFacadeException(t);
		}
	}

	/*
	 * Get current parameters
	 */
	CalSvcIPars getPars() {
		return pars;
	}

	/*
	 * See if in public admin mode
	 */
	private boolean isPublicAdmin() throws CalFacadeException {
		return pars.getPublicAdmin();
	}

	/*
	 * Get a logger for messages
	 */
	private Logger getLogger() {
		if (log == null) {
			log = Logger.getLogger(this.getClass());
		}

		return log;
	}

	private void logIt(final String msg) {
		getLogger().info(msg);
	}

	private void trace(final String msg) {
		getLogger().debug(msg);
	}

	private void warn(final String msg) {
		getLogger().warn(msg);
	}

	private void error(final String msg) {
		getLogger().error(msg);
	}

	private void error(final Throwable t) {
		getLogger().error(this, t);
	}

	public CalSvc getCalSvc() {
		return (CalSvc) this;
	}
}