/**
 * @(#)HibRestore.java    1.0.0 2:12:53 PM
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
package org.bedework.dumprestore.restore;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.bedework.calcore.AccessUtil;
import org.bedework.calcore.hibernate.CalintfHelperHib;
import org.bedework.calcore.hibernate.CoreCalendars;
import org.bedework.calcore.hibernate.CoreEvents;
import org.bedework.calcore.hibernate.HibSessionImpl;
import org.bedework.calcorei.CalintfDefs;
import org.bedework.calcorei.CoreCalendarsI;
import org.bedework.calcorei.CoreEventInfo;
import org.bedework.calcorei.CoreEventsI;
import org.bedework.calcorei.CoreEventsI.UpdateEventResult;
import org.bedework.calcorei.HibSession;
import org.bedework.calfacade.BwAttendee;
import org.bedework.calfacade.BwCalendar;
import org.bedework.calfacade.BwCategory;
import org.bedework.calfacade.BwContact;
import org.bedework.calfacade.BwEvent;
import org.bedework.calfacade.BwEventAnnotation;
import org.bedework.calfacade.BwEventObj;
import org.bedework.calfacade.BwEventProxy;
import org.bedework.calfacade.BwFilterDef;
import org.bedework.calfacade.BwLocation;
import org.bedework.calfacade.BwPrincipal;
import org.bedework.calfacade.BwStats;
import org.bedework.calfacade.BwSystem;
import org.bedework.calfacade.BwUser;
import org.bedework.calfacade.CalFacadeDefs;
import org.bedework.calfacade.RecurringRetrievalMode;
import org.bedework.calfacade.RecurringRetrievalMode.Rmode;
import org.bedework.calfacade.base.BwShareableContainedDbentity;
import org.bedework.calfacade.exc.CalFacadeException;
import org.bedework.calfacade.svc.BwAdminGroup;
import org.bedework.calfacade.svc.BwAdminGroupEntry;
import org.bedework.calfacade.svc.BwAuthUser;
import org.bedework.calfacade.svc.BwCalSuite;
import org.bedework.calfacade.svc.BwView;
import org.bedework.calfacade.svc.EventInfo;
import org.bedework.calfacade.svc.prefs.BwPreferences;
import org.bedework.calfacade.util.AccessUtilI;
import org.bedework.sysevents.events.SysEvent;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.idega.hibernate.SessionFactoryUtil;

import edu.rpi.cmt.access.AccessException;
import edu.rpi.cmt.access.AccessPrincipal;

/**
 * Class description goes here.
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.com">Martynas Stakė</a></p>
 * <p>You can expect to find some test cases notice in the end of the file.</p>
 *
 * @version 1.0.0 Apr 6, 2012
 * @author martynasstake
 */
public class HibRestore implements RestoreIntf {
	private RestoreGlobals globals;

	  private SessionFactory sessFactory;

	  private Session hibSess;

	  private HibSession hibSession;

	  private boolean manualFlush;

	  private transient Logger log;

	  protected int currentMode = CalintfDefs.userMode;

	  private AccessUtil access;
	  private CoreEventsI events;
	  private CoreCalendarsI calendars;

	  private BwStats stats = new BwStats();

	  /**
	   */
	  public HibRestore() {
	    this(SessionFactoryUtil.getSessionFactory());
	  }

	  /**
	   * @param sessionFactory
	   */
	  public HibRestore(final SessionFactory sessionFactory) {
	    log = Logger.getLogger(getClass());
	    try {
	      sessFactory = sessionFactory;
	    } catch (Throwable t) {
	      log.error("Failed to get session factory", t);
	    }
	  }

	  @Override
	  public HibSession getSession() throws CalFacadeException {
	    return hibSession;
	  }

	  /* (non-Javadoc)
	   * @see org.bedework.dumprestore.restore.RestoreIntf#init(org.bedework.dumprestore.restore.RestoreGlobals)
	   */
	  @Override
	  public void init(final RestoreGlobals globals) throws Throwable {
	    this.globals = globals;
	  }

	  /* (non-Javadoc)
	   * @see org.bedework.dumprestore.restore.RestoreIntf#open()
	   */
	  @Override
	  public void open() throws Throwable {
	  }

	  @Override
	  public void startTransaction() throws Throwable {
	    // Open delayed till restore method called
	  }

	  @Override
	  public void endTransactionNow() throws Throwable {
	    if (hibSess != null) {
	      closeHibSession();
	    }

	    globals.curHibBatchSize = 0;
	  }

	  /* (non-Javadoc)
	   * @see org.bedework.dumprestore.restore.RestoreIntf#endTransaction()
	   */
	  @Override
	  public void endTransaction() throws Throwable {
	    if ((globals.hibBatchSize > 0) &&
	        (globals.curHibBatchSize < globals.hibBatchSize)) {
	      return;
	    }

	    endTransactionNow();
	  }

	  /* (non-Javadoc)
	   * @see org.bedework.dumprestore.restore.RestoreIntf#close()
	   */
	  @Override
	  public void close() throws Throwable {
	    try {
	      if (hibSess != null) {
	        hibSess.close();
	      }
	    } finally {
	      hibSess = null;
	    }
	  }

	  /* (non-Javadoc)
	   * @see org.bedework.dumprestore.restore.RestoreIntf#restoreSyspars(org.bedework.calfacade.BwSystem)
	   */
	  @Override
	  public void restoreSyspars(final BwSystem o) throws Throwable {
	    openHibSess();

	    o.setId(CalFacadeDefs.unsavedItemKey);
	    save(o);

	    closeHibSess();
	  }

	  /* (non-Javadoc)
	   * @see org.bedework.dumprestore.restore.RestoreIntf#updateSyspars(org.bedework.calfacade.BwSystem)
	   */
	  @Override
	  public void updateSyspars(final BwSystem o) throws Throwable {
	    openHibSess();

	    hibSess.update(o);

	    closeHibSess();
	  }

	  /* (non-Javadoc)
	   * @see org.bedework.dumprestore.restore.RestoreIntf#restoreUser(org.bedework.calfacade.BwUser)
	   */
	  @Override
	  public void restoreUser(final BwUser o) throws Throwable {
	    if (!globals.onlyUsersMap.check(o.getPrincipalRef())) {
	      return;
	    }

	    try {
	      openHibSess();

	      o.setId(CalFacadeDefs.unsavedItemKey);
	      save(o);

	      closeHibSess();
	    } catch (Throwable t) {
	      handleException(t, "Exception restoring user " + o);
	    }
	  }

	  @Override
	  public void restoreAttendee(final BwAttendee o) throws Throwable {
	    // Ensure id not set
	    o.setId(CalFacadeDefs.unsavedItemKey);

	    openHibSess();

	    hibSess.save(o);

	    closeHibSess();
	  }

	  /* (non-Javadoc)
	   * @see org.bedework.dumprestore.restore.RestoreIntf#restoreAdminGroup(org.bedework.calfacade.svc.BwAdminGroup)
	   */
	  @Override
	  public void restoreAdminGroup(final BwAdminGroup o) throws Throwable {
	    openHibSess();

	    if (!globals.onlyUsersMap.check(o.getGroupOwnerHref())) {
	      o.setGroupOwnerHref(globals.getPublicUser().getPrincipalRef());
	    }

	    o.setId(CalFacadeDefs.unsavedItemKey);
	    save(o);

	    if (globals.config.getDebug()) {
	      log.debug("Saved admin group " + o);
	    }

	    closeHibSess();

	    /* Save members. */

	    Collection<BwPrincipal> c = o.getGroupMembers();
	    if (c == null) {
	      return;
	    }

	    for (BwPrincipal pr: c) {
	      addAdminGroupMember(o, pr);
	    }
	  }

	  @Override
	  public void addAdminGroupMember(final BwAdminGroup o, final BwPrincipal pr) throws Throwable {
	    if ((pr instanceof BwUser) &&
	        !globals.onlyUsersMap.check(pr.getPrincipalRef())) {
	      return;
	    }

	    openHibSess();

	    BwAdminGroupEntry entry = new BwAdminGroupEntry();

	    entry.setGrp(o);
	    entry.setMember(pr);

	    if (globals.config.getDebug()) {
	      log.debug("About to save " + entry);
	    }

	    save(entry);

	    closeHibSess();
	  }

	  /* (non-Javadoc)
	   * @see org.bedework.dumprestore.restore.RestoreIntf#getAdminGroup(java.lang.String)
	   */
	  @Override
	  public BwAdminGroup getAdminGroup(final String account) throws Throwable {
	    openHibSess();

	    Query q = hibSess.createQuery("from org.bedework.calfacade.svc.BwAdminGroup ag" +
	        " where ag.account=:account");
	    q.setString("account", account);
	    return (BwAdminGroup)q.uniqueResult();
	  }

	  /* (non-Javadoc)
	   * @see org.bedework.dumprestore.restore.RestoreIntf#updateAdminGroup(org.bedework.calfacade.svc.BwAdminGroup)
	   */
	  @Override
	  public void updateAdminGroup(final BwAdminGroup o) throws Throwable {
	    updt(o);
	    closeHibSess();
	  }

	  /* (non-Javadoc)
	   * @see org.bedework.dumprestore.restore.RestoreIntf#restoreAuthUser(org.bedework.calfacade.svc.BwAuthUser)
	   */
	  @Override
	  public void restoreAuthUser(final BwAuthUser o) throws Throwable {
	    if (!globals.onlyUsersMap.check(o.getUserHref())) {
	      return;
	    }

	    openHibSess();

	    save(o);

	    closeHibSess();
	  }

	  /* (non-Javadoc)
	   * @see org.bedework.dumprestore.restore.RestoreIntf#restoreEvent(org.bedework.calfacade.BwEvent)
	   */
	  @Override
	  public void restoreEvent(final EventInfo ei) throws Throwable {
	    try {
	      BwEvent ev = ei.getEvent();
	      BwEvent saveEv = ev;

	      if (!globals.onlyUsersMap.check(ev)) {
	        return;
	      }
	      globals.currentUser = (BwUser)globals.getPrincipal(ev.getOwnerHref());

	      if (ev instanceof BwEventProxy) {
	        BwEventProxy proxy = (BwEventProxy)ev;
	        saveEv = proxy.getRef();
	      }

	      openHibSess(FlushMode.MANUAL);
	      CoreEventsI evi = getEvents((BwUser)globals.getPrincipal(ev.getCreatorHref()));
	      UpdateEventResult uer = evi.addEvent(saveEv,
	                                           ei.getOverrideProxies(),
	                                           false, // scheduling
	                                           false);

	      if (!uer.addedUpdated) {
	        throw new CalFacadeException(uer.errorCode);
	      }
	      if (uer.failedOverrides != null) {
	        error("Following overrides failed for event ");
	        error(ev.toString());

	        for (BwEventProxy proxy: uer.failedOverrides) {
	          error(proxy.toString());
	        }
	      }
	    } finally {
	      closeHibSess();
	    }
	  }

	  /* (non-Javadoc)
	   * @see org.bedework.dumprestore.restore.RestoreIntf#getEvent(org.bedework.calfacade.BwUser, java.lang.String, java.lang.String, java.lang.String)
	   */
	  @Override
	  public BwEvent getEvent(final BwUser user,
	                          final String colPath,
	                          final String recurrenceId,
	                          final String uid) throws Throwable {
	    /* Open the session if not already open. Note we don't close it - that will
	     * happen later.
	     */
	    openHibSess(FlushMode.MANUAL);
	    CoreEventsI evi = getEvents(user);

	    Collection<CoreEventInfo> ceis = evi.getEvent(colPath,
	                                                  uid, recurrenceId,
	                                                  false,
	                                                  new RecurringRetrievalMode(Rmode.entityOnly));
	    /*
	    Query q = hibSess.createQuery("from " + BwEventObj.class.getName() +
	                                  " ev where ev.calendar=:cal " +
	                                  " and ev.uid=:uid ");
	    q.setEntity("cal", cal);
	    q.setString("uid", guid);
	    BwEvent ev = (BwEvent)q.uniqueResult();
	    */

	    if (ceis.size() != 1) {
	      error("Expected one event for {" + colPath + ", " +
	            recurrenceId + ", " + uid + "} found " + ceis.size());
	      return null;
	    }

	    BwEvent ev = ceis.iterator().next().getEvent();

	    if (ev instanceof BwEventAnnotation) {
	      ev = new BwEventProxy((BwEventAnnotation)ev);
	    }

	    return ev;
	  }

	  /* (non-Javadoc)
	   * @see org.bedework.dumprestore.restore.RestoreIntf#eventNameExists(org.bedework.calfacade.BwCalendar, java.lang.String)
	   */
	  @Override
	  public boolean eventNameExists(final BwCalendar cal, final String name) throws Throwable {
	    openHibSess();

	    Query q = hibSess.createQuery("select ev.name from " + BwEventObj.class.getName() +
	                                  " ev where ev.calendar=:cal " +
	                                  " and ev.name=:name ");
	    q.setEntity("cal", cal);
	    q.setString("name", name);
	    Collection c = q.list();

	    return (c != null) && !c.isEmpty();
	  }

	  /* (non-Javadoc)
	   * @see org.bedework.dumprestore.restore.RestoreIntf#update(org.bedework.calfacade.BwEvent)
	   */
	  @Override
	  public void update(final BwEvent o) throws Throwable {
	    if (!globals.onlyUsersMap.check(o)) {
	      return;
	    }

	    openHibSess();

	    hibSess.update(o);

	    closeHibSess();
	  }

	  /* (non-Javadoc)
	   * @see org.bedework.dumprestore.restore.RestoreIntf#restoreCategory(org.bedework.calfacade.BwCategory)
	   */
	  @Override
	  public void restoreCategory(final BwCategory o) throws Throwable {
	    if (!globals.onlyUsersMap.check(o)) {
	      return;
	    }

	    openHibSess();

	    save(o);

	    closeHibSess();
	  }

	  @Override
	  public void restoreCalSuite(final BwCalSuite o) throws Throwable {
	    openHibSess();

	    o.setId(CalFacadeDefs.unsavedItemKey);
	    save(o);

	    closeHibSess();
	  }


	  /* (non-Javadoc)
	   * @see org.bedework.dumprestore.restore.RestoreIntf#restoreLocation(org.bedework.calfacade.BwLocation)
	   */
	  @Override
	  public void restoreLocation(final BwLocation o) throws Throwable {
	    if (!globals.onlyUsersMap.check(o)) {
	      return;
	    }

	    openHibSess();

	    String uid = o.getUid();

	    StringBuilder sb = new StringBuilder();
	    sb.append("select ent.id from ");
	    sb.append(BwLocation.class.getName());
	    sb.append(" ent where uid=:uid");

	    hibSession.createQuery(sb.toString());
	    hibSession.setString("uid", uid);

	    Integer i = (Integer)hibSession.getUnique();

	    if (i != null) {
	      // Pre 3.5 did not use uid as unique key - make unique but warn.
	      warn("Location found with duplicate uid: \n" + o.toString());
	      o.initUid();
	      info("Set to " + o.getUid());

	      globals.locationsUidTbl.put(
	         new OwnerUidKey(o.getOwnerHref(), uid), o.getUid());
	    }

	    o.setId(CalFacadeDefs.unsavedItemKey);
	    save(o);

	    closeHibSess();
	  }

	  @Override
	  public void restoreContact(final BwContact o) throws Throwable {
	    if (!globals.onlyUsersMap.check(o)) {
	      return;
	    }

	    openHibSess();

	    String uid = o.getUid();

	    StringBuilder sb = new StringBuilder();
	    sb.append("select ent.id from ");
	    sb.append(BwContact.class.getName());
	    sb.append(" ent where uid=:uid");

	    hibSession.createQuery(sb.toString());
	    hibSession.setString("uid", uid);

	    Integer i = (Integer)hibSession.getUnique();

	    if (i != null) {
	      // Pre 3.5 did not use uid as unique key - make unique but warn.
	      warn("Contact found with duplicate uid: \n" + o.toString());
	      o.initUid();
	      info("Set to " + o.getUid());

	      globals.contactsUidTbl.put(
	         new OwnerUidKey(o.getOwnerHref(), uid), o.getUid());
	    }

	    o.setId(CalFacadeDefs.unsavedItemKey);
	    save(o);

	    closeHibSess();
	  }

	  @Override
	  public void restoreFilter(final BwFilterDef o) throws Throwable {
	    openHibSess();

	    o.setId(CalFacadeDefs.unsavedItemKey);
	    save(o);

	    closeHibSess();
	  }

	  @Override
	  public void restoreUserPrefs(final BwPreferences o) throws Throwable {
	    if (!globals.onlyUsersMap.check(o)) {
	      return;
	    }

	    openHibSess();

	    /* If the indexer or some other activity is running this can result in
	     * a preferences object being created. See if one exists.
	     */

	    Query q = hibSess.createQuery("from " + BwPreferences.class.getName() +
	                                  " p where p.ownerHref=:ownerHref");
	    q.setString("ownerHref", o.getOwnerHref());

	    BwPreferences p = (BwPreferences)q.uniqueResult();

	    if (p != null) {
	      warn("Found instance of preferences");
	      o.setId(p.getId());
	      hibSess.merge(o);
	    } else {

	      /* Ensure views are unsaved objects */
	      Collection<BwView> v = o.getViews();
	      if (v != null) {
	        for (BwView view: v) {
	          view.setId(CalFacadeDefs.unsavedItemKey);
	        }
	      }

	      o.setId(CalFacadeDefs.unsavedItemKey);
	      save(o);
	    }

	    closeHibSess();
	  }

	  /*
	  public void restoreAlarm(BwAlarm o) throws Throwable {
	    if (!globals.onlyUsersMap.check(o)) {
	      return;
	    }

	    openHibSess();

	    hibSave(o);

	    closeHibSess();
	  }*/

	  @Override
	  public void update(final BwUser user) throws Throwable {
	    if (!globals.onlyUsersMap.check(user.getPrincipalRef())) {
	      return;
	    }

	    openHibSess();

	    hibSess.update(user);

	    closeHibSess();
	  }

	  /* (non-Javadoc)
	   * @see org.bedework.dumprestore.restore.RestoreIntf#getCalendar(java.lang.String)
	   */
	  @Override
	  public BwCalendar getCalendar(final String path) throws Throwable {
	    openHibSess();

	    Query q = hibSess.createQuery("from " + BwCalendar.class.getName() +
	                                  " cal where cal.path=:path");
	    q.setString("path", path);
	    BwCalendar cal = (BwCalendar)q.uniqueResult();

	    return cal;
	  }

	  /* (non-Javadoc)
	   * @see org.bedework.dumprestore.restore.RestoreIntf#getCategory(org.bedework.dumprestore.restore.OwnerUidKey)
	   */
	  @Override
	  public BwCategory getCategory(final OwnerUidKey key) throws Throwable {
	    // We don't need the owner - uid is unique
	    return getCategory(key.getUid());
	  }

	  @Override
	  public BwCategory getCategory(final String uid) throws Throwable {
	    openHibSess();

	    Query q = hibSess.createQuery("from " + BwCategory.class.getName() +
	                                  " cat where cat.uid=:uid");
	    q.setString("uid", uid);

	    return (BwCategory)q.uniqueResult();
	  }

	  /* (non-Javadoc)
	   * @see org.bedework.dumprestore.restore.RestoreIntf#getContact(org.bedework.dumprestore.restore.OwnerUidKey)
	   */
	  @Override
	  public BwContact getContact(final OwnerUidKey key) throws Throwable {
	    // We don't need the owner - uid is unique
	    String uid = globals.contactsUidTbl.get(key);

	    if (uid == null) {
	      // Didn't remap this one
	      uid = key.getUid();
	    }

	    return getContact(uid);
	  }

	  /* (non-Javadoc)
	   * @see org.bedework.dumprestore.restore.RestoreIntf#getContact(java.lang.String)
	   */
	  @Override
	  public BwContact getContact(final String uid) throws Throwable {
	    openHibSess();

	    Query q = hibSess.createQuery("from " + BwContact.class.getName() +
	                                  " loc where loc.uid=:uid");
	    q.setString("uid", uid);

	    return (BwContact)q.uniqueResult();
	  }

	  /* (non-Javadoc)
	   * @see org.bedework.dumprestore.restore.RestoreIntf#getLocation(org.bedework.dumprestore.restore.OwnerUidKey)
	   */
	  @Override
	  public BwLocation getLocation(final OwnerUidKey key) throws Throwable {
	    // We don't need the owner - uid is unique
	    String uid = globals.locationsUidTbl.get(key);

	    if (uid == null) {
	      // Didn't remap this one
	      uid = key.getUid();
	    }

	    return getLocation(uid);
	  }

	  @Override
	  public BwLocation getLocation(final String uid) throws Throwable {
	    openHibSess();

	    Query q = hibSess.createQuery("from " + BwLocation.class.getName() +
	                                  " loc where loc.uid=:uid");
	    q.setString("uid", uid);

	    return (BwLocation)q.uniqueResult();
	  }

	  /* (non-Javadoc)
	   * @see org.bedework.dumprestore.restore.RestoreIntf#getUser(java.lang.String)
	   */
	  @Override
	  public BwUser getUser(final String account) throws Throwable {
	    openHibSess();

	    Query q = hibSess.createQuery("from " + BwUser.class.getName() +
	                                  " u where u.account=:account");
	    q.setString("account", account);

	    return (BwUser)q.uniqueResult();
	  }

	  /* (non-Javadoc)
	   * @see org.bedework.dumprestore.restore.RestoreIntf#saveRootCalendar(org.bedework.calfacade.BwCalendar)
	   */
	  @Override
	  public void saveRootCalendar(final BwCalendar val) throws Throwable {
	    if (!globals.onlyUsersMap.check(val)) {
	      return;
	    }

	    // Ensure id not set
	    val.setId(CalFacadeDefs.unsavedItemKey);

	    openHibSess();

	    hibSess.save(val);

	    closeHibSess();
	  }

	  /* (non-Javadoc)
	   * @see org.bedework.dumprestore.restore.RestoreIntf#addCalendar(org.bedework.calfacade.BwCalendar)
	   */
	  @Override
	  public void addCalendar(final BwCalendar o) throws Throwable {
	    if (!globals.onlyUsersMap.check(o)) {
	      return;
	    }

	    // Ensure id not set
	    o.setId(CalFacadeDefs.unsavedItemKey);

	    openHibSess();

	    hibSess.save(o);
	    globals.hibBatchSize++;

	    closeHibSess();
	  }

	  @Override
	  public String getUserHome(final BwUser user) throws Throwable {
	    return getCalendars(user).getPrincipalRootPath(user);
	  }

	  @Override
	  public void addCalendar(final BwCalendar val,
	                          final String parentPath) throws Throwable {
	    openHibSess();
	    // XXX This will fail when we get a group owning something
	    getCalendars((BwUser)globals.getPrincipal(val.getOwnerHref())).add(val, parentPath);
	    closeHibSess();
	  }

	  /* ====================================================================
	   *                       Private methods
	   * ==================================================================== */

	  /* I think we do this because of a (diminishing) number of reasons.
	   *   Efficiency concerns
	   *   Bypassing access restrictions
	   *   Switching user - just set the authuser in the access routines.
	   *
	   * It would be easier just to use the standard api
	   */
	  private CoreEventsI getEvents(final BwUser user) throws Throwable {
	    if (events != null) {
	      access.setAuthUser(user);
	      return events;
	    }

	    getCalendars(user); // To set accessutil

	    //access = new AccessUtil();
	    //access.init(new AccessUtilCb(this), globals.config.getDebug());
	    //access.setAuthUser(user);
	    //access.setSuperUser(true);

	    CalintfHelperHib.CalintfHelperHibCb chcb = new CalintfHelperHibCb(this);
	    events = new CoreEvents(chcb, globals.calCallback,
	                            access,
	                            currentMode,
	                            false);  // sessionless

	    return events;
	  }

	  private CoreCalendarsI getCalendars(final BwUser user) throws Throwable {
	    if (calendars != null) {
	      access.setAuthUser(user);
	      return calendars;
	    }

	    access = new AccessUtil();
	    access.init(new AccessUtilCb(this));
	    access.setAuthUser(user);
	    access.setSuperUser(true);

	    CalintfHelperHib.CalintfHelperHibCb hsf = new CalintfHelperHibCb(this);
	    calendars = new CoreCalendars(hsf, globals.calCallback,
	                            access,
	                            currentMode,
	                            false);  // sessionless

	    access.setCollectionGetter((CoreCalendars)calendars);

	    return calendars;
	  }

	  private class AccessUtilCb extends AccessUtilI.CallBack {
	    private HibRestore intf;

	    AccessUtilCb(final HibRestore intf) {
	      this.intf = intf;
	    }

	    /* (non-Javadoc)
	     * @see org.bedework.calfacade.util.AccessUtilI.CallBack#getPrincipal(java.lang.String)
	     */
	    @Override
	    public AccessPrincipal getPrincipal(final String href) throws CalFacadeException {
	      try {
	        return globals.getPrincipal(href);
	      } catch (Throwable t) {
	        throw new CalFacadeException(t);
	      }
	    }

	    /* (non-Javadoc)
	     * @see org.bedework.calfacade.util.AccessUtilI.CallBack#getUserCalendarRoot()
	     */
	    @Override
	    public String getUserCalendarRoot() throws CalFacadeException {
	      return globals.getSyspars().getUserCalendarRoot();
	    }

	    /* (non-Javadoc)
	     * @see org.bedework.calfacade.util.AccessUtilI.CallBack#getParent(org.bedework.calfacade.base.BwShareableContainedDbentity)
	     */
	    @SuppressWarnings("unused")
	    public BwCalendar getParent(final BwShareableContainedDbentity<?> val)
	           throws CalFacadeException {
	      try {
	        return intf.getCalendar(val.getColPath());
	      } catch (Throwable t) {
	        if (t instanceof CalFacadeException) {
	          throw (CalFacadeException)t;
	        }

	        throw new CalFacadeException(t);
	      }
	    }

	    /* (non-Javadoc)
	     * @see edu.rpi.cmt.access.Access.AccessCb#makeHref(java.lang.String, int)
	     */
	    @Override
	    public String makeHref(final String id, final int whoType) throws AccessException {
	      try {
	        return globals.getPrincipalHref(id, whoType);
	      } catch (Throwable t) {
	        throw new AccessException(t);
	      }
	    }
	  }

	  private static class CalintfHelperHibCb implements CalintfHelperHib.CalintfHelperHibCb {
	    private HibRestore intf;

	    CalintfHelperHibCb(final HibRestore intf) {
	      this.intf = intf;
	    }

	    @Override
	    public HibSession getSess() throws CalFacadeException {
	      return intf.hibSession;
	    }

	    @Override
	    public BwStats getStats() throws CalFacadeException {
	      return intf.stats;
	    }

	    @Override
	    public BwCalendar getCollection(final String path) throws CalFacadeException {
	      try {
	        return intf.getCalendar(path);
	      } catch (Throwable t) {
	        if (t instanceof CalFacadeException) {
	          throw (CalFacadeException)t;
	        }

	        throw new CalFacadeException(t);
	      }
	    }

	    @Override
	    public BwCalendar getCollection(final String path,
	                                    final int desiredAccess,
	                                    final boolean alwaysReturn) throws CalFacadeException {
	      try {
	        return intf.getCalendar(path);
	      } catch (Throwable t) {
	        if (t instanceof CalFacadeException) {
	          throw (CalFacadeException)t;
	        }

	        throw new CalFacadeException(t);
	      }
	    }

	    @Override
	    public void postNotification(final SysEvent ev) throws CalFacadeException {
	      /* Assume we are going to reindex - ignore system events for a restore */
	    }
	  }

	  private void handleException(final Throwable t, final String msg) {
	    error(msg); // Update count
	    log.error(this, t);

	    if (hibSess != null) {
	      try {
	        closeHibSession();
	      } catch (Throwable t1) {}
	    }
	    hibSess = null;
	  }

	  private void handleException(final Throwable t,
	                               final String msg,
	                               final BwEvent ev) throws Throwable {
	    log.error(ev.toString());
	    handleException(t, msg);
	  }

	  private synchronized void openHibSess() throws Throwable {
	    openHibSess(FlushMode.COMMIT);
	  }

	  private synchronized void openHibSess(final FlushMode fm) throws Throwable {
	    if (hibSess == null) {
	      hibSession = new HibSessionImpl();
	      hibSession.init(sessFactory, Logger.getLogger(getClass()));

	      hibSess = hibSession.getSession();
	      hibSess.setFlushMode(fm);
	      hibSess.beginTransaction();
	      manualFlush = fm.equals(FlushMode.MANUAL);
	    }
	  }

	  private void closeHibSess() throws Throwable {
	    endTransaction();
	  }

	  private synchronized void closeHibSession() throws Throwable {
	    try {
	      if (manualFlush) {
	        hibSess.flush();
	      }
	      hibSess.getTransaction().commit();
	    } finally {
	      try {
	        if (hibSess != null) {
	          hibSess.close();
	        }
	      } finally {
	        hibSess = null;
	      }
	    }
	  }

	  private void save(final Object o) throws Throwable {
	    hibSess.save(o);
	  }

	  private void updt(final Object o) throws Throwable {
	    hibSess.update(o);
	  }

	  protected Logger getLog() {
	    if (log == null) {
	      log = Logger.getLogger(this.getClass());
	    }

	    return log;
	  }

	  protected void info(final String msg) {
	    getLog().info(msg);
	  }

	  protected void warn(final String msg) {
	    globals.warnings++;
	    getLog().warn(msg);
	  }

	  protected void error(final String msg) {
	    globals.errors++;
	    getLog().error(msg);
	  }
}
