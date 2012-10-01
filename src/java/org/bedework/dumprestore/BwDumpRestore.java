/**
 * @(#)BwDumpRestoreIdega.java    1.0.0 1:11:35 PM
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
package org.bedework.dumprestore;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.bedework.calfacade.exc.CalFacadeAccessException;
import org.bedework.calfacade.exc.CalFacadeException;
import org.bedework.calsvci.CalSvcFactoryIdega;
import org.bedework.calsvci.CalSvcI;
import org.bedework.calsvci.CalSvcIPars;
import org.bedework.calsvci.CalendarsI.CheckSubscriptionResult;
import org.bedework.dumprestore.dump.Dump;
import org.bedework.dumprestore.restore.Restore;
import org.bedework.indexer.BwIndexerMBean;

import com.idega.hibernate.SessionFactoryUtil;

import edu.rpi.cmt.jboss.MBeanUtil;
import edu.rpi.sss.util.Args;
import edu.rpi.sss.util.DateTimeUtil;

/**
 * <p>
 * For primary data initiation.
 * </p>
 * <p>
 * You can report about problems to: <a
 * href="mailto:martynas@idega.com">Martynas Stakė</a>
 * </p>
 * <p>
 * You can expect to find some test cases notice in the end of the file.
 * </p>
 * 
 * @version 1.0.0 Apr 6, 2012
 * @author martynasstake
 */
public class BwDumpRestore implements BwDumpRestoreMBean {

	private transient Logger log;

	private boolean started;

	private String account;

	private String appname = "dumpres";

	private boolean create;

	private String delimiter;

	private boolean drop;

	/* Be safe - default to false */
	private boolean export;

	private boolean format;

	private boolean haltOnError;

	private String schemaOutFile;

	private String sqlIn;

	private String dataIn;

	private String dataOut;

	private String dataOutPrefix;

	private List<ExternalSubInfo> externalSubs;

	private String curSvciOwner;

	private CalSvcI svci;

	/**
	 * Help for returning output
	 */
	public static class InfoLines extends ArrayList<String> {

		/**
		 * Appends newline
		 * 
		 * @param ln
		 */
		public void addLn(final String ln) {
			add(ln + "\n");
		}

		/**
		 * Emit the exception message
		 * 
		 * @param t
		 */
		public void exceptionMsg(final Throwable t) {
			addLn("Exception - check logs: " + t.getMessage());
		}
	}

	/*
	 * ========================================================================
	 * Attributes
	 * ========================================================================
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.dumprestore.BwDumpRestoreMBean#getName()
	 */
	@Override
	public String getName() {
		/*
		 * This apparently must be the same as the name attribute in the jboss
		 * service definition
		 */
		return "org.bedework:service=DumpRestore";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.bedework.dumprestore.BwDumpRestoreMBean#setAccount(java.lang.String)
	 */
	@Override
	public void setAccount(final String val) {
		account = val;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.dumprestore.BwDumpRestoreMBean#getAccount()
	 */
	@Override
	public String getAccount() {
		return account;
	}

	@Override
	public String getAppname() {
		return appname;
	}

	@Override
	public void setCreate(final boolean val) {
		create = val;
	}

	@Override
	public boolean getCreate() {
		return create;
	}

	@Override
	public void setDelimiter(final String val) {
		delimiter = val;
	}

	@Override
	public String getDelimiter() {
		return delimiter;
	}

	@Override
	public void setDrop(final boolean val) {
		drop = val;
	}

	@Override
	public boolean getDrop() {
		return drop;
	}

	@Override
	public void setExport(final boolean val) {
		export = val;
	}

	@Override
	public boolean getExport() {
		return export;
	}

	/**
	 * Format the output?
	 * 
	 * @param val
	 */
	@Override
	public void setFormat(final boolean val) {
		format = val;
	}

	@Override
	public boolean getFormat() {
		return format;
	}

	@Override
	public void setHaltOnError(final boolean val) {
		haltOnError = val;
	}

	@Override
	public boolean getHaltOnError() {
		return haltOnError;
	}

	@Override
	public void setSchemaOutFile(final String val) {
		schemaOutFile = val;
	}

	@Override
	public String getSchemaOutFile() {
		return schemaOutFile;
	}

	@Override
	public void setSqlIn(final String val) {
		sqlIn = val;
	}

	@Override
	public String getSqlIn() {
		return sqlIn;
	}

	@Override
	public void setDataIn(final String val) {
		dataIn = val;
	}

	@Override
	public String getDataIn() {
		return dataIn;
	}

	@Override
	public void setDataOut(final String val) {
		dataOut = val;
	}

	@Override
	public String getDataOut() {
		return dataOut;
	}

	@Override
	public void setDataOutPrefix(final String val) {
		dataOutPrefix = val;
	}

	@Override
	public String getDataOutPrefix() {
		return dataOutPrefix;
	}

	@Override
	public boolean testSchemaValid() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.dumprestore.BwDumpRestoreMBean#schema()
	 */
	@Override
	public String schema() {
		//TODO
		return "HOORAY, YOU HAVE NOT LOST YOUR DATABASE YET.";
//		String result = "Export complete: check logs";
//
//		try {
//			SchemaExport se = new SchemaExport(getConfiguration());
//
//			if (getDelimiter() != null) {
//				se.setDelimiter(getDelimiter());
//			}
//
//			se.setFormat(getFormat());
//			se.setHaltOnError(getHaltOnError());
//			se.setOutputFile(getSchemaOutFile());
//			se.setImportFile(getSqlIn());
//
//			se.execute(false, // script - causes write to System.out if true
//					getExport(), getDrop(), getCreate());
//		} catch (Throwable t) {
//			error(t);
//			result = "Exception: " + t.getLocalizedMessage();
//		} finally {
//			create = false;
//			drop = false;
//			export = false;
//		}
//
//		return result;
	}

	@Override
	public synchronized List<String> restoreData() {
		InfoLines infoLines = new InfoLines();

		try {
			if (!disableIndexer()) {
				infoLines.add("***********************************\n");
				infoLines.add("********* Unable to disable indexer\n");
				infoLines.add("***********************************\n");
			}

			long startTime = System.currentTimeMillis();

			Restore restorer = new Restore(SessionFactoryUtil.getSessionFactory());

			String[] args = new String[] { "-appname", appname };

			restorer.getConfigProperties(new Args(args));

			infoLines.addLn("Restore file: " + getDataIn());
			info("Restore file: " + getDataIn());

			restorer.setFilename(getDataIn());

			restorer.open();

			restorer.doRestore();

			externalSubs = restorer.getExternalSubs();

			restorer.close();

			restorer.stats(infoLines);

			long millis = System.currentTimeMillis() - startTime;
			long seconds = millis / 1000;
			long minutes = seconds / 60;
			seconds -= (minutes * 60);

			infoLines.addLn("Elapsed time: " + minutes + ":"
					+ Restore.twoDigits(seconds));

			infoLines.add("Restore complete" + "\n");
		} catch (Throwable t) {
			error(t);
			infoLines.exceptionMsg(t);
		} finally {
			try {
				if (!reindex()) {
					infoLines.addLn("***********************************");
					infoLines.addLn("********* Unable to disable indexer");
					infoLines.addLn("***********************************");
				}
			} catch (Throwable t) {
				error(t);
				infoLines.exceptionMsg(t);
			}
		}

		return infoLines;
	}

	@Override
	public List<String> checkExternalSubs() {
		InfoLines infoLines = new InfoLines();

		boolean debug = getLogger().isDebugEnabled();

		if (externalSubs.isEmpty()) {
			infoLines.addLn("No external subscriptions");

			return infoLines;
		}

		/** Number for which no action was required */
		int okCt = 0;

		/** Number not found */
		int notFoundCt = 0;

		/** Number for which no action was required */
		int notExternalCt = 0;

		/** Number resubscribed */
		int resubscribedCt = 0;

		/** Number of failures */
		int failedCt = 0;

		if (debug) {
			trace("About to process " + externalSubs.size() + " external subs");
		}

		try {
			int ct = 0;
			int accessErrorCt = 0;
			int errorCt = 0;

			resubscribe: for (ExternalSubInfo esi : externalSubs) {
				trace("About to process " + esi);
				getSvci(esi);

				try {
					CheckSubscriptionResult csr = svci.getCalendarsHandler()
							.checkSubscription(esi.path);

					switch (csr) {
					case ok:
						okCt++;
						break;
					case notFound:
						notFoundCt++;
						break;
					case notExternal:
						notExternalCt++;
						break;
					case resubscribed:
						resubscribedCt++;
						break;
					case noSynchService:
						infoLines.addLn("Synch service is unavailable");
						info("Synch service is unavailable");
						break resubscribe;
					case failed:
						failedCt++;
						break;
					} // switch

					if ((csr != CheckSubscriptionResult.ok)
							&& (csr != CheckSubscriptionResult.resubscribed)) {
						infoLines.addLn("Status: " + csr + " for " + esi.path
								+ " owner: " + esi.owner);
					}

					ct++;

					if ((ct % 100) == 0) {
						info("Touched " + ct + " collections");
					}
				} catch (CalFacadeAccessException cae) {
					accessErrorCt++;

					if ((accessErrorCt % 100) == 0) {
						info("Had " + accessErrorCt + " access errors");
					}
				} catch (Throwable t) {
					error(t);
					errorCt++;

					if ((errorCt % 100) == 0) {
						info("Had " + errorCt + " errors");
					}
				} finally {
					closeSvci();
				}
			} // resubscribe

			infoLines.addLn("Checked " + ct + " collections");
			infoLines.addLn("       errors: " + errorCt);
			infoLines.addLn("access errors: " + accessErrorCt);
			infoLines.addLn("           ok: " + okCt);
			infoLines.addLn("    not found: " + notFoundCt);
			infoLines.addLn("  notExternal: " + notExternalCt);
			infoLines.addLn(" resubscribed: " + resubscribedCt);
			infoLines.addLn("       failed: " + failedCt);
		} catch (Throwable t) {
			error(t);
			infoLines.exceptionMsg(t);
		}

		return infoLines;
	}

	@Override
	public List<String> dumpData() {
		InfoLines infoLines = new InfoLines();

		try {
			long startTime = System.currentTimeMillis();

			Dump d = new Dump(SessionFactoryUtil.getSessionFactory());

			String[] args = new String[] { "-appname", appname };

			d.getConfigProperties(args);

			StringBuilder fname = new StringBuilder(getDataOut());
			if (!getDataOut().endsWith("/")) {
				fname.append("/");
			}

			fname.append(getDataOutPrefix());

			/* append "yyyyMMddTHHmmss" */
			fname.append(DateTimeUtil.isoDateTime());
			fname.append(".xml");

			d.setFilename(fname.toString());

			d.open();

			d.doDump();

			externalSubs = d.getExternalSubs();

			d.close();

			d.stats(infoLines);

			long millis = System.currentTimeMillis() - startTime;
			long seconds = millis / 1000;
			long minutes = seconds / 60;
			seconds -= (minutes * 60);

			infoLines.addLn("Elapsed time: " + minutes + ":"
					+ Restore.twoDigits(seconds));

			infoLines.addLn("Dump complete");
		} catch (Throwable t) {
			error(t);
			infoLines.exceptionMsg(t);
		}

		return infoLines;
	}

	@Override
	public String dropTables() {
		return "Not implemented";
	}

	/*
	 * ========================================================================
	 * Lifecycle
	 * ========================================================================
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.dumprestore.BwDumpRestoreMBean#create()
	 */
	@Override
	public void create() {
		// An opportunity to initialise
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.indexer.BwIndexerMBean#start()
	 */
	@Override
	public void start() {
		started = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.indexer.BwIndexerMBean#stop()
	 */
	@Override
	public void stop() {
		started = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.indexer.BwIndexerMBean#isStarted()
	 */
	@Override
	public boolean isStarted() {
		return started;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.dumprestore.BwDumpRestoreMBean#destroy()
	 */
	@Override
	public void destroy() {
	}

	/*
	 * ====================================================================
	 * Private methods
	 * ====================================================================
	 */

//	private synchronized Configuration getConfiguration() {
//		if (cfg == null) {
//			cfg = new Configuration().configure();
//		}
//
//		return cfg;
//	}

	private BwIndexerMBean indexer;

	private boolean disableIndexer() throws CalFacadeException {
		try {
			if (indexer == null) {
				indexer = (BwIndexerMBean) MBeanUtil.getMBean(
						BwIndexerMBean.class, "org.bedework:service=Indexer");
			}

			indexer.setDiscardMessages(true);
			return true;
		} catch (Throwable t) {
			error(t);
			return false;
		}
	}

	private boolean reindex() throws CalFacadeException {
		try {
			if (indexer == null) {
				return false;
			}
			indexer.rebuildIndex();
			indexer.setDiscardMessages(false);
			return true;
		} catch (Throwable t) {
			error(t);
			return false;
		}
	}

	/**
	 * Get an svci object and return it. Also embed it in this object.
	 * 
	 * @return svci object
	 * @throws CalFacadeException
	 */
	private CalSvcI getSvci(final ExternalSubInfo esi)
			throws CalFacadeException {
		if ((svci != null) && svci.isOpen()) {
			return svci;
		}

		boolean publicAdmin = false;

		if ((curSvciOwner == null) || !curSvciOwner.equals(esi.owner)) {
			svci = null;

			curSvciOwner = esi.owner;
			publicAdmin = esi.publick;
		}

		if (svci == null) {
			CalSvcIPars pars = CalSvcIPars.getServicePars(curSvciOwner,
					publicAdmin, // publicAdmin
					true); // Allow super user
			svci = new CalSvcFactoryIdega().getSvc(pars);
		}

		svci.open();
		svci.beginTransaction();

		return svci;
	}

	/**
	 * @throws CalFacadeException
	 */
	public void closeSvci() throws CalFacadeException {
		if ((svci == null) || !svci.isOpen()) {
			return;
		}

		try {
			svci.endTransaction();
		} catch (Throwable t) {
			try {
				svci.close();
			} catch (Throwable t1) {
			}
		}

		try {
			svci.close();
		} catch (Throwable t) {
		}
	}

	/*
	 * ====================================================================
	 * Protected methods
	 * ====================================================================
	 */

	protected void info(final String msg) {
		getLogger().info(msg);
	}

	protected void trace(final String msg) {
		getLogger().debug(msg);
	}

	protected void error(final Throwable t) {
		getLogger().error(this, t);
	}

	protected void error(final String msg) {
		getLogger().error(msg);
	}

	/*
	 * Get a logger for messages
	 */
	protected Logger getLogger() {
		if (log == null) {
			log = Logger.getLogger(this.getClass().getName());
		}

		return log;
	}
}
