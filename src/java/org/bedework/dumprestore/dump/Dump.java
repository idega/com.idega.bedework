/**
 * @(#)Dump.java    1.0.0 2:23:54 PM
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

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.log4j.Logger;
import org.bedework.calfacade.configs.DumpRestoreConfig;
import org.bedework.calfacade.env.CalOptionsFactory;
import org.bedework.dumprestore.Defs;
import org.bedework.dumprestore.ExternalSubInfo;
import org.bedework.dumprestore.dump.dumpling.DumpAll;
import org.hibernate.SessionFactory;

import com.idega.hibernate.SessionFactoryUtil;

/**
 * Application to dump calendar data.
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.com">Martynas Stakė</a></p>
 * <p>You can expect to find some test cases notice in the end of the file.</p>
 *
 * @version 1.0.0 Apr 6, 2012
 * @author martynasstake
 */
public class Dump implements Defs {
	private transient Logger log;

	  private String appName;

	  /* Runtime arg -f Where we dump to.
	   */
	  private String fileName;

	  /* runtime arg -i (id) */
	  //private String id = "sa";

	  String indent = "";

	  private DumpGlobals globals = new DumpGlobals();

	  private SessionFactory cfg;

	  /** ===================================================================
	   *                       Constructor
	   *  =================================================================== */

	  /**
	   * @throws Throwable
	   */
	  public Dump() throws Throwable {
	    this(SessionFactoryUtil.getSessionFactory());
	  }

	  /**
	   * @param cfg
	   * @throws Throwable
	   */
	  public Dump(final SessionFactory cfg) throws Throwable {
	    this.cfg = cfg;
	  }

	  /** ===================================================================
	   *                       Dump methods
	   *  =================================================================== */

	  /**
	   * @param val - filename to dump to
	   */
	  public void setFilename(final String val) {
	    fileName = val;
	  }

	  /**
	   * @throws Throwable
	   */
	  public void open() throws Throwable {
	    globals.di = new HibDump(cfg);

	    if (fileName == null) {
	      globals.setOut(new OutputStreamWriter(System.out));
	    } else {
	      globals.setOut(new OutputStreamWriter(new FileOutputStream(fileName),
	                                            "UTF-8"));
	    }
	  }

	  /**
	   * @throws Throwable
	   */
	  public void close() throws Throwable {
	    globals.close();
	  }

	  /**
	   * @throws Throwable
	   */
	  public void doDump() throws Throwable {
	    new DumpAll(globals).dumpSection(null);
	  }

	  /**
	   * @return list of external subscriptions
	   */
	  public List<ExternalSubInfo> getExternalSubs() {
	    return globals.externalSubs;
	  }

	  /**
	  *
	  * @param infoLines - null for logged output only
	  */
	 public void stats(final List<String> infoLines) {
	   globals.stats(infoLines);
	 }

	  void processArgs(final String[] args) throws Throwable {
	    /* Look for appname arg */

	    for (int i = 0; i < args.length; i++) {
	      if (args[i].equals("-debug")) {
	        globals.config.setDebug(true);
	      } else if (args[i].equals("-ndebug")) {
	        globals.config.setDebug(false);
	      } else if (args[i].equals("")) {
	        // null arg generated by ant
	      } else if (args[i].equals("-noarg")) {
	        // noop
	      } else if (argpar("-appname", args, i)) {
	        i++;
	        // done earlier
	      } else if (argpar("-f", args, i)) {
	        i++;
	        fileName = args[i];
	      } else if (argpar("-i", args, i)) {
	        i++;
	        //id = args[i];
	      } else {
	        error("Illegal argument: " + args[i]);
	        throw new Exception("Invalid args");
	      }
	    }
	  }

	  boolean argpar(final String n, final String[] args, final int i) throws Exception {
	    if (!args[i].equals(n)) {
	      return false;
	    }

	    if ((i + 1) == args.length) {
	      throw new Exception("Invalid args");
	    }
	    return true;
	  }

	  /**
	   * @param args
	   * @throws Throwable
	   */
	  public void getConfigProperties(final String[] args) throws Throwable {
	    /* Look for the appname arg */

	    if (args != null) {
	      for (int i = 0; i < args.length; i++) {
	        if (argpar("-appname", args, i)) {
	          i++;
	          appName = args[i];
	          break;
	        }
	      }
	    }

	    if (appName == null) {
	      error("Missing required argument -appname");
	      throw new Exception("Invalid args");
	    }

	    globals.init((DumpRestoreConfig)CalOptionsFactory.getOptions().getAppProperty(appName));
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

	  /**
	   * @param args
	   */
	  public static void main(final String[] args) {
	    Dump d = null;
	    int rtn = 4;  // Wasn't that EOF for MTS?

	    try {
	      long startTime = System.currentTimeMillis();

	      d = new Dump();

	      d.getConfigProperties(args);

	      d.processArgs(args);

	      d.open();

	      d.doDump();

	      d.stats(null);

	      long millis = System.currentTimeMillis() - startTime;
	      long seconds = millis / 1000;
	      long minutes = seconds / 60;
	      seconds -= (minutes * 60);

	      d.info("Elapsed time: " + minutes + ":" + twoDigits(seconds));

	      /*
	      Map<Thread, StackTraceElement[]> liveThreads = Thread.getAllStackTraces();

	      for (Thread th: liveThreads.keySet()) {
	        StackTraceElement[] stes = liveThreads.get(th);

	        d.trace("Thread: " + th);
	        if (stes != null) {
	          for (StackTraceElement ste: stes) {
	            d.trace(ste.toString());
	          }
	        }
	      }*/

	      rtn = 0;
	    } catch (Throwable t) {
	      t.printStackTrace();
	    } finally {
	      try {
	        d.close();
	      } catch (Throwable t1) {
	        t1.printStackTrace();
	      }
	    }

	    System.exit(rtn);
	  }

	  private static String twoDigits(final long val) {
	    if (val < 10) {
	      return "0" + val;
	    }

	    return String.valueOf(val);
	  }
}
