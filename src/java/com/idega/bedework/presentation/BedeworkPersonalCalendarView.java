/**
 * @(#)BedeworkPersonalCalendarView.java    1.0.0 8:31:27 AM
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
 *     Licensee shall also mean the individual using or installing 
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
package com.idega.bedework.presentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;

import org.bedework.calfacade.BwCalendar;
import org.springframework.beans.factory.annotation.Autowired;

import com.idega.bedework.BedeworkConstants;
import com.idega.bedework.bussiness.BedeworkCalendarsService;
import com.idega.bedework.bussiness.view.BwCalBusiness;
import com.idega.bedework.media.EventsExporter;
import com.idega.block.cal.business.CalBusiness;
import com.idega.block.cal.presentation.CalendarEntryCreator;
import com.idega.block.cal.presentation.CalendarView;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.calendar.data.CalendarEntity;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.DownloadLink;
import com.idega.presentation.text.Heading3;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SelectOption;
import com.idega.util.CoreConstants;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;
import com.idega.util.expression.ELUtil;

/**
 * <p>Presentation object for extending {@link CalendarView} to work with Bedework.</p>
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.com">Martynas Stakė</a></p>
 * <p>You can expect to find some test cases notice in the end of the file.</p>
 *
 * @version 1.0.0 Apr 19, 2012
 * @author martynasstake
 */
public class BedeworkPersonalCalendarView extends CalendarView {
	
	public static final String 	ATTRIBUTE_CALENDAR_NAME = "attr_calendar_name",
								PARAMETER_SHOW = "prm_show_calendar",
								PARAMETER_ACTION = "prm_action";
	
	private IWBundle bundle;
	private IWResourceBundle iwrb;
	private BwCalBusiness bwCalBussinessBean;
	
	private BwCalBusiness getBwCalBussinessBean(IWApplicationContext iwc) {
		if (this.bwCalBussinessBean == null) {
			try {
				this.bwCalBussinessBean = IBOLookup
						.getServiceInstance(iwc, BwCalBusiness.class);
				
				this.bwCalBussinessBean.recreateCalendarEntryTypes();
			} catch (IBOLookupException e) {
				getLogger().log(Level.WARNING, 
						"Unable to get BwCalBussinessBean: ", e);
			}
		}
		
		return this.bwCalBussinessBean;
	}
	
	@Autowired
	private BedeworkCalendarsService bcms;
	
	private BedeworkCalendarsService getBedeworkCalendarManagementService() {
		if (this.bcms == null) {
			ELUtil.getInstance().autowire(this);
		}

		return this.bcms;
	}
	
	private BwCalendar getCalendarByPath(IWContext iwc, String path) {
		if (iwc == null || StringUtil.isEmpty(path)) {
			return null;
		}
		
		Collection<BwCalendar> calendars = getCalendars(iwc);
		if (ListUtil.isEmpty(calendars)) {
			return null;
		}
		
		for (BwCalendar calendar : calendars) {
			if (path.equals(calendar.getPath())) {
				return calendar;
			}
		}
		
		return null;
	}
	
	private Collection<BwCalendar> getCalendars(IWContext iwc) {
		if (iwc == null) {
			return null;
		}
		
		Collection<BwCalendar> calendars = new ArrayList<BwCalendar>();
		
		Collection<BwCalendar> calendarsFromdatabase = 
				getBedeworkCalendarManagementService()
				.getCalendars(iwc.getCurrentUser());
		
		if (!ListUtil.isEmpty(calendarsFromdatabase)) {
			calendars.addAll(calendarsFromdatabase);
		}
		
		Collection<CalendarEntity> calendarsEntytiesFromDatabase = 
				getBedeworkCalendarManagementService()
				.getSubscriptions(iwc.getCurrentUser());
		
		if (!ListUtil.isEmpty(calendarsEntytiesFromDatabase)) {
			calendars.addAll(calendarsEntytiesFromDatabase);
		}

		return calendars;
	}
	
	private DropdownMenu getCalendarsSelectionDropdownMenu(IWContext iwc) {
		if (iwc == null) {
			return null;
		}
		
		DropdownMenu calendarsSelection = new DropdownMenu(
				ATTRIBUTE_CALENDAR_NAME
				);
		
		calendarsSelection.setToSubmit();
		
		for (BwCalendar calendar : getCalendars(iwc)) {
			calendarsSelection.addMenuElement(
					calendar.getPath(), calendar.getName());
		}
		
		if (calendarsSelection.isEmpty()) {
			calendarsSelection.addFirstOption(new SelectOption(
					iwrb.getLocalizedString(
							"no_calendars_found", "No calendars found."),
							"-1"));
		} else {
			calendarsSelection.addFirstOption(new SelectOption( 
					iwrb.getLocalizedString(
							"select_calendar", "Select calendar:"),
							"-1"));
		}
		
		String selectedCalendar = iwc.getParameter(ATTRIBUTE_CALENDAR_NAME);
		if (StringUtil.isEmpty(selectedCalendar)) {
			BwCalendar currentCalendar = getBwCalBussinessBean(iwc).getCurrentCalendar();
			if (currentCalendar != null) {
				selectedCalendar = currentCalendar.getPath();
			}
		}
		
		if (!StringUtil.isEmpty(selectedCalendar)) {
			calendarsSelection.setSelectedElement(selectedCalendar);
			
			if (!selectedCalendar.equals("-1")) {
				getBwCalBussinessBean(iwc).setCurrentCalendar(
						getCalendarByPath(iwc, selectedCalendar));
			}
		}
		
		return calendarsSelection;
	}
	
	private void addFormItem(Layer container, String label, InterfaceObject ui) {
		Label labelUI = new Label(label.concat(CoreConstants.COLON), ui);
		Layer itemContainer = new Layer();
		itemContainer.setStyleClass("formItem");
		itemContainer.add(labelUI);
		
		itemContainer.add(ui);
		container.add(itemContainer);
	}

	@Override
	public String getBundleIdentifier() {
		return BedeworkConstants.BUNDLE_IDENTIFIER;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	@Override
	public void main(IWContext iwc) throws Exception {
		bundle = iwc.getIWMainApplication().getBundle(getBundleIdentifier());
		iwrb = bundle.getResourceBundle(iwc);
		
		if (iwc == null || !iwc.isLoggedOn()) {
			add(new Heading3(
					iwrb.getLocalizedString("not_logged_on", "Not logged on!")
					));
			return;
		}
		
		Form form = new Form();
		add(form);
		
		Layer container = new Layer();
		form.add(container);
		container.setStyleClass("calendarSelection");
		
		addFormItem(container, 
				iwrb.getLocalizedString("select_calendar", "Select calendar:"), 
				getCalendarsSelectionDropdownMenu(iwc));
	
		if (getBwCalBussinessBean(iwc).getCurrentCalendar() != null) {
			setViewInGroupID(iwc.getCurrentUser().getGroupID());
			super.main(iwc);
			Layer export = new Layer();
			container.add(export);
			DownloadLink eventsExporter = new DownloadLink(iwrb.getLocalizedString("export_events", "Export events"));
			export.add(eventsExporter);
			eventsExporter.setMediaWriterClass(EventsExporter.class);
			eventsExporter.setParameter(EventsExporter.PARAMETER_CALENDAR, 
					getBwCalBussinessBean(iwc).getCurrentCalendar().getPath());
		}
	}
	
	private BedeworkCalendarEntryCreator creator = null;
	
	/* (non-Javadoc)
	 * @see com.idega.block.cal.presentation.CalendarView#getCalendarEntryCreator()
	 */
	@Override
	protected CalendarEntryCreator getCalendarEntryCreator(IWContext iwc) {
		if (this.creator == null) {
			this.creator = new BedeworkCalendarEntryCreator();
			this.creator.setBwCalBusiness(getBwCalBussinessBean(iwc));
		}
		
		return this.creator;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.cal.presentation.CalendarView#getCalBusiness(com.idega.idegaweb.IWApplicationContext)
	 */
	@Override
	public CalBusiness getCalBusiness(IWApplicationContext iwc) {
		return getBwCalBussinessBean(iwc);
	}
}
