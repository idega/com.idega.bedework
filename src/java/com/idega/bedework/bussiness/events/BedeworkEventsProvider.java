package com.idega.bedework.bussiness.events;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.bedework.bussiness.view.BwCalBusiness;
import com.idega.block.cal.business.events.EventsProvider;
import com.idega.block.cal.data.CalendarEntry;
import com.idega.core.business.DefaultSpringBean;
import com.idega.user.data.User;
import com.idega.util.CoreUtil;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class BedeworkEventsProvider extends DefaultSpringBean implements EventsProvider {

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

}
