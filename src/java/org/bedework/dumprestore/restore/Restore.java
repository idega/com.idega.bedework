/**
 * @(#)Restore.java    1.0.0 2:07:43 PM
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

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RegexMatcher;
import org.apache.commons.digester.RegexRules;
import org.apache.commons.digester.SimpleRegexMatcher;
import org.apache.log4j.Logger;
import org.bedework.calfacade.BwCalendar;
import org.bedework.calfacade.BwSystem;
import org.bedework.calfacade.BwUser;
import org.bedework.calfacade.configs.DumpRestoreConfig;
import org.bedework.calfacade.env.CalOptionsFactory;
import org.bedework.calfacade.svc.BwAdminGroup;
import org.bedework.calfacade.svc.BwAuthUser;
import org.bedework.calfacade.svc.UserAuth;
import org.bedework.calfacade.svc.prefs.BwAuthUserPrefs;
import org.bedework.dumprestore.Defs;
import org.bedework.dumprestore.ExternalSubInfo;
import org.bedework.dumprestore.restore.rules.RestoreRuleSet;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.idega.bedework.BedeworkConstants;

import edu.rpi.cmt.access.Ace;
import edu.rpi.cmt.access.AceWho;
import edu.rpi.cmt.access.Acl;
import edu.rpi.cmt.access.Privilege;
import edu.rpi.cmt.access.PrivilegeDefs;
import edu.rpi.cmt.access.Privileges;
import edu.rpi.cmt.access.WhoDefs;
import edu.rpi.sss.util.Args;
import edu.rpi.sss.util.OptionsI;

/**
 * <p>Application to restore from an XML calendar dump..</p>
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.com">Martynas Stakė</a></p>
 * <p>You can expect to find some test cases notice in the end of the file.</p>
 *
 * @version 1.0.0 Apr 6, 2012
 * @author martynasstake
 */
public class Restore implements Defs {
	private transient Logger log;

	  private String appName;

	  /* File we restore from */
	  private String filename;

	  private RestoreGlobals globals;

	  /* True if we are creating a clean system */
	  private boolean newSystem;

	  private String rootId;

	  private SessionFactory sessionFactory;

	  /** ===================================================================
	   *                       Constructor
	   *  =================================================================== */

	  /**
	   * @throws Throwable
	   */
	  public Restore() throws Throwable {
	    this(new Configuration().configure().buildSessionFactory());
	  }

	  /**
	   * @param cfg
	   * @throws Throwable
	   */
	  public Restore(final SessionFactory cfg) throws Throwable {
	    this.sessionFactory = cfg;
	    globals = new RestoreGlobals();
	  }

	  /** ===================================================================
	   *                       Restore methods
	   *  =================================================================== */

	  /**
	   * @param val - filename to restore from
	   */
	  public void setFilename(final String val) {
	    filename = val;
	  }

	  /**
	   * @throws Throwable
	   */
	  public void open() throws Throwable {
	    if (globals.rintf == null) {
	      globals.rintf = new HibRestore(sessionFactory);
	      globals.rintf.init(globals);
	      globals.rintf.open();
	    }
	  }

	  /**
	   * @throws Throwable
	   */
	  public void close() throws Throwable {
	    if (globals.rintf != null) {
	      globals.rintf.close();
	    }

	    if (newSystem) {
	      return;
	    }
	  }

	  /**
	   * @throws Throwable
	   */
	  public void doRestore() throws Throwable {
	    if (newSystem) {
	      createNewSystem();

	      return;
	    }

	    globals.digester = new Digester();

	    RegexMatcher m = new SimpleRegexMatcher();
	    globals.digester.setRules(new RegexRules(m));

	    globals.digester.addRuleSet(new RestoreRuleSet(globals));
	    globals.digester.parse(new InputStreamReader(new FileInputStream(filename == null ? BedeworkConstants.FILE_PATH_INIT_BEDEWORK : filename),
	                                         "UTF-8"));
	  }

	  /**
	   * @return list of external subscriptions
	   */
	  public List<ExternalSubInfo> getExternalSubs() {
	    return globals.externalSubs;
	  }

	  private void createNewSystem() throws Throwable {
	    // Save the system settings.

	    BwSystem sys = globals.getSyspars();

	    sys.setRootUsers(rootId);

	    globals.rintf.startTransaction();
	    globals.rintf.restoreSyspars(sys);

	    // Create the public user.

	    globals.rintf.startTransaction();
	    BwUser pu = new BwUser();

	    pu.setAccount(globals.getSyspars().getPublicUser());
	    globals.setPrincipalHref(pu);

	    globals.rintf.restoreUser(pu);

	    // Create the root user.

	    globals.rintf.startTransaction();
	    BwUser rootUser = new BwUser();

	    rootUser.setAccount(rootId);
	    globals.setPrincipalHref(rootUser);

	    globals.rintf.restoreUser(rootUser);

	    // Create the an authuser entry for the root user.

	    globals.rintf.startTransaction();
	    BwAuthUser au = new BwAuthUser();
	    au.setUserHref(rootUser.getPrincipalRef());
	    au.setUsertype(UserAuth.allAuth);
	    au.setPrefs(BwAuthUserPrefs.makeAuthUserPrefs());

	    globals.rintf.restoreAuthUser(au);

	    // Create a group for all public admin groups

	    globals.rintf.startTransaction();
	    BwAdminGroup g = new BwAdminGroup();

	    String publicAdminGroupsAccount = "publicAdminGroups";   // XXX Put into config
	    g.setAccount(publicAdminGroupsAccount);
	    g.setGroupOwnerHref(pu.getPrincipalRef());
	    g.setOwnerHref(pu.getPrincipalRef());

	    globals.rintf.restoreAdminGroup(g);

	    // Create the public root.

	    globals.rintf.startTransaction();

	    Collection<Privilege> privs = new ArrayList<Privilege>();
	    privs.add(Privileges.makePriv(PrivilegeDefs.privRead));

	    Collection<Ace> aces = new ArrayList<Ace>();

	    aces.add(Ace.makeAce(AceWho.other, privs, null));

	    privs.clear();
	    privs.add(Privileges.makePriv(PrivilegeDefs.privRead));
	    privs.add(Privileges.makePriv(PrivilegeDefs.privWriteContent));

	    AceWho who = AceWho.getAceWho(publicAdminGroupsAccount,
	                                  WhoDefs.whoTypeGroup,
	                                  false);
	    aces.add(Ace.makeAce(who, privs, null));

	    makeCal(null, pu,
	            BwCalendar.calTypeFolder,
	            globals.getSyspars().getPublicCalendarRoot(),
	            new String(new Acl(aces).encode()));

	    // Create the user root.

	    globals.rintf.startTransaction();

	    privs.clear();
	    privs.add(Privileges.makePriv(PrivilegeDefs.privAll));

	    aces.clear();
	    aces.add(Ace.makeAce(AceWho.owner, privs, null));

	    BwCalendar userRoot = makeCal(null, pu,
	                                  BwCalendar.calTypeFolder,
	                                  globals.getSyspars().getUserCalendarRoot(),
	                                  new String(new Acl(aces).encode()));

	    makeUserHome(userRoot, pu);
	    makeUserHome(userRoot, rootUser);
	  }

	  private void makeUserHome(final BwCalendar userRoot,
	                            final BwUser user) throws Throwable {
	    // Create root user home and default calendar

	    BwCalendar userHome = makeCal(userRoot, user,
	                                  BwCalendar.calTypeFolder,
	                                  user.getAccount(),
	                                  null);
	    makeCal(userHome, user,
	            BwCalendar.calTypeCalendarCollection,
	            globals.getSyspars().getUserDefaultCalendar(),
	            null);
	  }

	  private BwCalendar makeCal(final BwCalendar parent,
	                             final BwUser owner,
	                             final int type,
	                             final String name,
	                             final String encodedAcl) throws Throwable {
	    BwCalendar cal = new BwCalendar();

	    cal.setCalType(type);

	    String ppath = "";
	    if (parent != null) {
	      ppath = parent.getPath();
	    }

	    cal.setColPath(parent.getPath());
	    cal.setName(name);
	    cal.setPath(ppath + "/" + cal.getName());
	    cal.setSummary(cal.getName());

	    cal.setOwnerHref(owner.getPrincipalRef());
	    cal.setCreatorHref(owner.getPrincipalRef());
	    cal.setAccess(encodedAcl);

	    globals.rintf.saveRootCalendar(cal);

	    return cal;
	  }

	  /**
	   *
	   * @param infoLines - null for logged output only
	   */
	  public void stats(final List<String> infoLines) {
	    globals.stats(infoLines);
	  }

	  boolean processArgs(final Args args) throws Throwable {
	    if (args == null) {
	      return true;
	    }

	    while (args.more()) {
	      if (args.ifMatch("-debug")) {
	        globals.config.setDebug(true);
	      } else if (args.ifMatch("-ndebug")) {
	        globals.config.setDebug(false);
	      } else if (args.ifMatch("")) {
	        // null arg generated by ant
	      } else if (args.ifMatch("-noarg")) {
	        // noop
	      } else if (args.ifMatch("-appname", 1)) {
	        args.next();
	        // done earlier
	      } else if (args.ifMatch("-newSystem")) {
	        // done earlier
	      } else if (newSystem && (args.ifMatch("-rootid", 1))) {
	        rootId = args.next();
	      } else if (args.ifMatch("-initSyspars")) {
	        // done earlier
	      } else if (args.ifMatch("-skipspecialcals")) {
	        globals.skipSpecialCals = true;
	      } else if (args.ifMatch("-indexroot", 1)) {
	        globals.getSyspars().setIndexRoot(args.next());
	      } else if (args.ifMatch("-f", 1)) {
	        filename = args.next();
	        /* Can we override these in the hibernate properties?
	      } else if (argpar("-d", args, i)) {
	        i++;
	        driver = args[i];
	      } else if (argpar("-i", args, i)) {
	        i++;
	        id = args[i];
	      } else if (argpar("-p", args, i)) {
	        i++;
	        pw = args[i];
	      } else if (argpar("-u", args, i)) {
	        i++;
	        url = args[i];
	        */

	      } else if (args.ifMatch("-onlyusers", 1)) {
	        String par = args.next();

	        if (par.equals("*")) {
	          // means ignore this par
	        } else {
	          globals.onlyUsersMap.setOnlyUsers(true);
	          String[] users = par.split(",");
	          for (int oui = 0; oui < users.length; oui++) {
	            String ou = users[oui];
	            info("Only user: " + ou);

	            globals.onlyUsersMap.add(ou);
	          }
	        }

	      } else {
	        error("Illegal argument: '" + args.current() + "'");
	        usage();
	        return false;
	      }
	    }

	    return true;
	  }

	  void usage() {
	    System.out.println("Usage:");
	    System.out.println("args   -appname name       required: provides name of this application");
	    System.out.println("       -f restorefilename  name of restore data");
	    System.out.println("       -[n]debug           debugging switch");
	    System.out.println("       -initSyspars        Override system settings with configuration");
	    System.out.println("       -newSystem -rootid <id>");
	    System.out.println("           Create new empty system from configuration");
	    System.out.println("       -noindexes          turn off lucene indexing");
	    System.out.println("       -indexroot lucene-index-root-path");
	    System.out.println("");
	  }

	  /**
	   * @param args
	   * @throws Throwable
	   */
	  public void getConfigProperties(final Args args) throws Throwable {
	    /* Look for the appname - the configured name for this applications
	     *  and initSyspars arg
	     */
	    boolean initSyspars = false;

	    if (args != null) {
	      while (args.more()) {
	        if (args.ifMatch("-appname", 1)) {
	          appName = args.next();
	        } else if (args.ifMatch("-initSyspars")) {
	          initSyspars = true;
	        } else if (args.ifMatch("-newSystem")) {
	          initSyspars = true;
	          newSystem = true;
	        } else {
	          args.next();
	        }
	      }
	    }

	    if (appName == null) {
	      error("Missing required argument -appname");
	      throw new Exception("Invalid args");
	    }

	    OptionsI opts = CalOptionsFactory.getOptions();

	    globals.init((DumpRestoreConfig)opts.getAppProperty(appName));
	    if (initSyspars || globals.config.getInitSyspars()) {
	      globals.setSyspars((BwSystem)opts.getProperty("org.bedework.syspars"));
	    }

	    globals.defaultSyspars = (BwSystem)opts.getProperty("org.bedework.syspars");
	  }

	  protected Logger getLog() {
	    if (log == null) {
	      log = Logger.getLogger(this.getClass());
	    }

	    return log;
	  }

	  protected void error(final String msg) {
	    getLog().error(msg);
	  }

	  protected void info(final String msg) {
	    getLog().info(msg);
	  }

	  protected void trace(final String msg) {
	    getLog().debug(msg);
	  }

	  /** Main
	   *
	   * @param args
	   */
	  public static void main(final String[] args) {
	    Restore r = null;

	    try {
	      long startTime = System.currentTimeMillis();

	      r = new Restore();

	      r.getConfigProperties(new Args(args));

	      if (!r.processArgs(new Args(args))) {
	        return;
	      }

	      r.open();

	      r.doRestore();

	      r.close();

	      r.stats(null);

	      long millis = System.currentTimeMillis() - startTime;
	      long seconds = millis / 1000;
	      long minutes = seconds / 60;
	      seconds -= (minutes * 60);

	      r.info("Elapsed time: " + minutes + ":" + twoDigits(seconds));

	      /*
	      Map<Thread, StackTraceElement[]> liveThreads = Thread.getAllStackTraces();

	      for (Thread th: liveThreads.keySet()) {
	        StackTraceElement[] stes = liveThreads.get(th);

	        r.trace("Thread: " + th);
	        if (stes != null) {
	          for (StackTraceElement ste: stes) {
	            r.trace(ste.toString());
	          }
	        }
	      }*/

	      System.exit(0);
	    } catch (Throwable t) {
	      t.printStackTrace();
	    } finally {
	      try {
	        r.close();
	      } catch (Throwable t1) {
	        t1.printStackTrace();
	      }
	    }

	    System.exit(4);
	  }

	  /**
	   * @param val
	   * @return 2 digit val
	   */
	  public static String twoDigits(final long val) {
	    if (val < 10) {
	      return "0" + val;
	    }

	    return String.valueOf(val);
	  }
}
