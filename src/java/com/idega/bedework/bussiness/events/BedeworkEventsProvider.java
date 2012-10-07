package com.idega.bedework.bussiness.events;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;

import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.bedework.BedeworkConstants;
import com.idega.bedework.bussiness.view.BwCalBusiness;
import com.idega.bedework.media.EventsExporter;
import com.idega.block.cal.business.events.EventsProvider;
import com.idega.block.cal.data.CalendarEntry;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.core.business.DefaultSpringBean;
import com.idega.dwr.business.DWRAnnotationPersistance;
import com.idega.idegaweb.IWMainApplication;
import com.idega.io.MediaWritable;
import com.idega.user.data.User;
import com.idega.util.CoreUtil;
import com.idega.util.URIUtil;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
@RemoteProxy(creator=SpringCreator.class, creatorParams={
	@Param(name="beanName", value=BedeworkEventsProvider.BEAN_NAME),
	@Param(name="javascript", value=BedeworkEventsProvider.DWR_OBJECT)
}, name=BedeworkEventsProvider.DWR_OBJECT)
public class BedeworkEventsProvider extends DefaultSpringBean implements EventsProvider, DWRAnnotationPersistance {

	static final String BEAN_NAME = "bedeworkEventsProvider",
						DWR_OBJECT = "BedeworkEventsProvider";

	@Override
	public List<CalendarEntry> getEvents(User user, Timestamp from, Timestamp to) {
		if (user == null) {
			getLogger().warning("User is not provided");
			return null;
		}

		try {
			BwCalBusiness bwCalBusiness = getServiceInstance(BwCalBusiness.class);
			return bwCalBusiness.getUserEntriesBetweenTimestamps(user, from, to, CoreUtil.getIWContext());
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting events for " + user + " from " + from + " to " + to, e);
		}
		return null;
	}

	@RemoteMethod
	public AdvancedProperty getEventsExporterLink() {
		URIUtil uri = new URIUtil(IWMainApplication.getDefaultIWMainApplication().getMediaServletURI());
		uri.setParameter(MediaWritable.PRM_WRITABLE_CLASS, IWMainApplication.getEncryptedClassName(EventsExporter.class));
		uri.setParameter(EventsExporter.PARAMETER_SHOW_ALL_MY_EVENTS, Boolean.TRUE.toString());
		AdvancedProperty link = new AdvancedProperty(uri.getUri());
		link.setValue(getResourceBundle(getBundle(BedeworkConstants.BUNDLE_IDENTIFIER)).getLocalizedString("export_events", "Export events"));
		return link;
	}

}
