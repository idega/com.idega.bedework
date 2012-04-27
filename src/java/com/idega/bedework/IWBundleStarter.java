package com.idega.bedework;

import javax.management.ObjectName;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.idega.idegaweb.IWBundleStartable#start(com.idega.idegaweb.IWBundle)
	 */
	public void start(IWBundle starterBundle) {
		
		javax.management.MBeanServer mbs = java.lang.management.ManagementFactory
				.getPlatformMBeanServer();
		
//		org.apache.activemq.ActiveMQConnection activeMQConnection;
//		try {
//			activeMQConnection = org.apache.activemq.ActiveMQConnection.makeConnection("tcp://localhost:61616");
//			activeMQConnection.start();
//			//Get queues
//			org.apache.activemq.advisory.DestinationSource destinationSource = 
//					activeMQConnection.getDestinationSource();
//			java.util.Set<org.apache.activemq.command.ActiveMQQueue> queues = 
//					destinationSource.getQueues();
//		} catch (JMSException e1) {
//			e1.printStackTrace();
//		} catch (URISyntaxException e1) {
//			e1.printStackTrace();
//		}
 
		BwIndexerMBean indexerMBean = new BwIndexer();
		try {
			ObjectName indexerName = new ObjectName(indexerMBean.getName());
			if (!mbs.isRegistered(indexerName)) {
				mbs.registerMBean(indexerMBean, indexerName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		TzsvcMBean timeServerMBean = new Tzsvc();
		try {
			ObjectName timeServerName = new ObjectName("org.bedework:service=Tzsvr");
			if (!mbs.isRegistered(timeServerName)) {
				mbs.registerMBean(timeServerMBean, timeServerName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		BwDumpRestoreMBean dumpRestoreMBean = new BwDumpRestore();
		try {
			ObjectName dumpRestoreName = new ObjectName(dumpRestoreMBean.getName());
			if (!mbs.isRegistered(dumpRestoreName)) {
				mbs.registerMBean(dumpRestoreMBean, dumpRestoreName);
			}
		} catch (Exception e) {
			e.printStackTrace();
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