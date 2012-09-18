package com.idega.bedework;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.ObjectName;

import org.bedework.caldav.server.soap.synch.SynchConnections;
import org.bedework.caldav.server.soap.synch.SynchConnectionsMBean;
import org.bedework.dumprestore.BwDumpRestore;
import org.bedework.dumprestore.BwDumpRestoreMBean;
import org.bedework.indexer.BwIndexer;
import org.bedework.indexer.BwIndexerMBean;

import org.bedework.timezones.service.Tzsvc;
import org.bedework.timezones.service.TzsvcMBean;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.idegaweb.IWMainApplicationSettings;
import com.idega.presentation.IWContext;

public class IWBundleStarter implements IWBundleStartable {

	private static final Logger LOGGER = Logger.getLogger(IWBundleStarter.class.getName());
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.idega.idegaweb.IWBundleStartable#start(com.idega.idegaweb.IWBundle)
	 */
	public void start(IWBundle starterBundle) {
		
		/* Starts MBeans */
		javax.management.MBeanServer mbs = java.lang.management.ManagementFactory
				.getPlatformMBeanServer();
		
		SynchConnectionsMBean connectionsMbean = new SynchConnections();
		try {
			ObjectName connectionsMbeanName = new ObjectName(connectionsMbean.getName());
			if (!mbs.isRegistered(connectionsMbeanName)) {
				mbs.registerMBean(connectionsMbean, connectionsMbeanName);
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Unable to register SynchConnectionsMBean: ",e);
		}
		
		BwIndexerMBean indexerMBean = new BwIndexer();
		try {
			ObjectName indexerName = new ObjectName(indexerMBean.getName());
			if (!mbs.isRegistered(indexerName)) {
				mbs.registerMBean(indexerMBean, indexerName);
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Unable to register Bedework indexer MBean: ", e);
		}

		TzsvcMBean timeServerMBean = new Tzsvc();
		try {
			ObjectName timeServerName = new ObjectName("org.bedework:service=Tzsvr");
			if (!mbs.isRegistered(timeServerName)) {
				mbs.registerMBean(timeServerMBean, timeServerName);
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Unable to register timezones MBean: ", e);
		}
		
		BwDumpRestoreMBean dumpRestoreMBean = new BwDumpRestore();
		try {
			ObjectName dumpRestoreName = new ObjectName(dumpRestoreMBean.getName());
			if (!mbs.isRegistered(dumpRestoreName)) {
				mbs.registerMBean(dumpRestoreMBean, dumpRestoreName);
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Unable to register Bedework restorer MBean: ", e);
		}
		
		IWMainApplicationSettings settings = null;
		if (starterBundle != null) {
			settings = starterBundle.getApplication().getSettings();
		} else {
			IWContext iwc = IWContext.getCurrentInstance();
			settings = iwc.getIWMainApplication().getSettings();
		}
		
		if (!settings.getBoolean(BedeworkConstants.BEDEWORK_INITIATED_APP_PROP, Boolean.FALSE)) {
			dumpRestoreMBean.setDataIn(BedeworkConstants.FILE_PATH_INIT_BEDEWORK);
			dumpRestoreMBean.restoreData();
			settings.setProperty(BedeworkConstants.BEDEWORK_INITIATED_APP_PROP, Boolean.TRUE.toString());
		}
		
		//FIXME Remove this hack later
		(new com.idega.bedework.webservice.sync.IWBundleStarter()).start(starterBundle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.idega.idegaweb.IWBundleStartable#stop(com.idega.idegaweb.IWBundle)
	 */
	public void stop(IWBundle starterBundle) {
		// No action...
	}
}