/**
 * @(#)HibDump.java    1.0.0 2:26:06 PM
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
package org.bedework.dumprestore.dump;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.bedework.calfacade.BwCalendar;
import org.bedework.calfacade.BwCategory;
import org.bedework.calfacade.BwContact;
import org.bedework.calfacade.BwEvent;
import org.bedework.calfacade.BwEventAnnotation;
import org.bedework.calfacade.BwEventObj;
import org.bedework.calfacade.BwFilterDef;
import org.bedework.calfacade.BwGroup;
import org.bedework.calfacade.BwLocation;
import org.bedework.calfacade.BwSystem;
import org.bedework.calfacade.BwUser;
import org.bedework.calfacade.exc.CalFacadeException;
import org.bedework.calfacade.svc.BwAdminGroup;
import org.bedework.calfacade.svc.BwCalSuite;
import org.bedework.calfacade.svc.BwView;
import org.bedework.calfacade.svc.prefs.BwPreferences;
import org.bedework.dumprestore.HibSession;
import org.hibernate.SessionFactory;

import com.idega.hibernate.SessionFactoryUtil;

/**
 * Class which implements the functions needed to dump the
 * calendar using a jdbc connection.
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.com">Martynas Stakė</a></p>
 * <p>You can expect to find some test cases notice in the end of the file.</p>
 *
 * @version 1.0.0 Apr 6, 2012
 * @author martynasstake
 */
public class HibDump implements DumpIntf{
	private HibSession sess;
	  private SessionFactory sessFactory;

	  private Logger log;

	  private BwSystem syspars;

	  /**
	   */
	  public HibDump() {
	    this(SessionFactoryUtil.getSessionFactory());
	  }

	  /**
	   * @param cfg
	   */
	  public HibDump(final SessionFactory cfg) {
	    log = Logger.getLogger(getClass());
	    try {
	      sessFactory = cfg;
	    } catch (Throwable t) {
	      log.error("Failed to get session factory", t);
	    }
	  }

	  public void open() throws Throwable {
	    openSess();
	    beginTransaction();
	  }

	  public void close() throws Throwable {
	    endTransaction();
	    closeSess();
	  }

	  public Iterator getAdminGroups() throws Throwable {
	    sess.createQuery("from " + BwAdminGroup.class.getName());

	    Collection<BwAdminGroup> c = sess.getList();

	    for (BwGroup grp: c) {
	      getAdminMembers(grp);
	    }

	    return c.iterator();
	  }

	  public Iterator getAuthUsers() throws Throwable {
	    return getObjects("org.bedework.calfacade.svc.BwAuthUser");
	  }

	  public Iterator getCalendars() throws Throwable {
	    getIntSyspars();

	    sess.createQuery("from org.bedework.calfacade.BwCalendar cal where " +
	                     "cal.path=:path1 or cal.path=:path2");
	    sess.setString("path1", "/" + syspars.getPublicCalendarRoot());
	    sess.setString("path2", "/" + syspars.getUserCalendarRoot());

	    return sess.getList().iterator();
	  }

	  public Collection<BwCalendar> getChildren(final BwCalendar val) throws Throwable {
	    sess.createQuery("from org.bedework.calfacade.BwCalendar c where " +
	      "c.colPath=:path");
	    sess.setString("path", val.getPath());

	    return sess.getList();
	  }

	  public Iterator getCalSuites() throws Throwable {
	    return getObjects(BwCalSuite.class.getName());
	  }

	  public Iterator getCategories() throws Throwable {
	    return getObjects(BwCategory.class.getName());
	  }

	  public Iterator<BwEvent> getEvents() throws Throwable {
	    Collection<BwEvent> evs = getObjectCollection(BwEventObj.class.getName());

	    for (BwEvent ev: evs) {
	      if (ev.testRecurring()) {
	        ev.setOverrides(getOverrides(ev));
	      }
	    }

	    return evs.iterator();
	  }

	  public Iterator<BwEventAnnotation> getEventAnnotations() throws Throwable {
	    StringBuilder sb = new StringBuilder();

	    sb.append("from ");
	    sb.append(BwEventAnnotation.class.getName());
	    sb.append(" where recurrenceId=null");

	    sess.createQuery(sb.toString());

	    Collection<BwEventAnnotation> anns = sess.getList();

	    return anns.iterator();
	  }

	  public Iterator getFilters() throws Throwable {
	    return getObjects(BwFilterDef.class.getName());
	  }

	  public Iterator getLocations() throws Throwable {
	    return getObjects(BwLocation.class.getName());
	  }

	  public Iterator getPreferences() throws Throwable {
	    return getObjects(BwPreferences.class.getName());
	  }

	  public Iterator getContacts() throws Throwable {
	    return getObjects(BwContact.class.getName());
	  }

	  public Iterator getSyspars() throws Throwable {
	    return getObjects(BwSystem.class.getName());
	  }

	  public Iterator getUsers() throws Throwable {
	    return getObjects(BwUser.class.getName());
	  }

	  public Iterator getViews() throws Throwable {
	    return getObjects(BwView.class.getName());
	  }

	  /** Start a (possibly long-running) transaction. In the web environment
	   * this might do nothing. The endTransaction method should in some way
	   * check version numbers to detect concurrent updates and fail with an
	   * exception.
	   *
	   * @throws Throwable
	   */
	  public void beginTransaction() throws Throwable {
	    sess.beginTransaction();
	  }

	  /** End a (possibly long-running) transaction. In the web environment
	   * this should in some way check version numbers to detect concurrent updates
	   * and fail with an exception.
	   *
	   * @throws Throwable
	   */
	  public void endTransaction() throws Throwable {
	    /* Just commit */
	    sess.commit();
	  }

	  private Collection getObjectCollection(final String className) throws Throwable {
	    sess.createQuery("from " + className);

	    return sess.getList();
	  }

	  private Iterator getObjects(final String className) throws Throwable {
	    return getObjectCollection(className).iterator();
	  }

	  private synchronized void openSess() throws Throwable {
//	    if (sess == null) {
	      sess = new HibSession(sessFactory, log);
//	    } else {
//	      sess.reconnect();
//	    }
	  }

	  private synchronized void closeSess() throws Throwable {
	    try {
//	      sess.close();
	      if (sess != null) {
//	        sess.disconnect();
	        sess.close();
	      }
	    } catch (Throwable t) {
	      // Discard session if we get errors on close.
	      t.printStackTrace();
	      throw t;
	    } finally {
	      sess = null;
	    }
	  }

	  private void getIntSyspars() throws Throwable {
	    Iterator it = getSyspars();
	    if (!it.hasNext()) {
	      throw new Exception("Expect one and only one system parameter entry");
	    }

	    syspars = (BwSystem)it.next();

	    if (it.hasNext()) {
	      throw new Exception("Expect one and only one system parameter entry");
	    }
	  }

	  private void getAdminMembers(final BwGroup group) throws CalFacadeException {
	    sess.namedQuery("getAdminGroupUserMembers");
	    sess.setEntity("gr", group);

	    Collection ms = sess.getList();

	    sess.namedQuery("getAdminGroupGroupMembers");
	    sess.setEntity("gr", group);

	    ms.addAll(sess.getList());

	    group.setGroupMembers(ms);
	  }

	  @SuppressWarnings("unused")
	  private void getMembers(final BwGroup group) throws CalFacadeException {
	    sess.namedQuery("getGroupUserMembers");
	    sess.setEntity("gr", group);

	    Collection ms = sess.getList();

	    sess.namedQuery("getGroupGroupMembers");
	    sess.setEntity("gr", group);

	    ms.addAll(sess.getList());

	    group.setGroupMembers(ms);
	  }

	  private Collection<BwEventAnnotation> getOverrides(final BwEvent ev) throws Throwable {
	    StringBuffer sb = new StringBuffer();

	    sb.append("from ");
	    sb.append(BwEventAnnotation.class.getName());
	    sb.append(" where target=:target");
	    sb.append(" and recurrenceId<>null");

	    sess.createQuery(sb.toString());
	    sess.setEntity("target", ev);

	    return sess.getList();
	  }
}
