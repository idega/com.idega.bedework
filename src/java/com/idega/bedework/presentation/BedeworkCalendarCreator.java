/**
 * @(#)BedeworkCalendarCreator.java    1.0.0 9:50:51 PM
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
package com.idega.bedework.presentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.bedework.calfacade.BwCalendar;
import org.springframework.beans.factory.annotation.Autowired;

import com.idega.bedework.BedeworkConstants;
import com.idega.bedework.bussiness.BedeworkCalendarManagementService;
import com.idega.bedework.bussiness.BedeworkCalendarPresentationComponentsService;
import com.idega.bedework.bussiness.impl.BedeworkCalendarManagementServiceBean;
import com.idega.block.web2.business.JQuery;
import com.idega.calendar.data.CalendarEntity;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableBodyRowGroup;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableHeaderRowGroup;
import com.idega.presentation.TableRow;
import com.idega.presentation.text.Heading3;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.RadioGroup;
import com.idega.presentation.ui.SelectOption;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.user.presentation.group.GroupsFilter;
import com.idega.util.ArrayUtil;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;
import com.idega.util.ListUtil;
import com.idega.util.PresentationUtil;
import com.idega.util.StringUtil;
import com.idega.util.expression.ELUtil;

/**
 * <p>Bedework calendar creator. Add all info about calendar to be created or edited here.
 * </p>
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.com">Martynas Stakė</a></p>
 * <p>You can expect to find some test cases notice in the end of the file.</p>
 *
 * @version 1.0.0 Jul 1, 2012
 * @author martynasstake
 */
public class BedeworkCalendarCreator extends Block {
	
	public static final String	ATTRIBUTE_NAME = "attr_name",
											ATTRIBUTE_SUMMARY = "attr_summary",
											ATTRIBUTE_DESCRIPTION = "attr_description",
											ATTRIBUTE_FOLDER = "attr_folder",
											ATTRIBUTE_IS_REMOVABLE = "attr_is_removable",
											ATTRIBUTE_IS_PRIVATE = "attr_is_private",
											ATTRIBUTE_GROUPS = "attr_groups",
											ATTRIBUTE_CALENDAR_ID = "attr_calendar_id";
	
	public static final String 	CLASS_CALENDARS_TABLE = "calendars_table",
											CLASS_CALENDARS_TABLE_HEADER = "calendars_table_header",
											CLASS_CALENDARS_TABLE_BODY = "calendars_table_body",
											CLASS_CALENDARS_TABLE_ROW = "calendars_table_row",
											CLASS_CALENDARS_TABLE_CELL = "calendar_table_cell",
											
											CLASS_CALENDAR_NAME = "calendar_name",
											CLASS_CALENDAR_NUMBER = "calendar_number",
											
											CLASS_CALENDAR_LAYER_NAME_INPUT = "calendar_name_layer",
											CLASS_CALENDAR_LAYER_GROUPS_SELECTION = "calendar_groups_selection_layer",
											CLASS_CALENDAR_LAYER_DESCRIPTION = "calendar_description_layer",
											CLASS_CALENDAR_LAYER_SUMMARY = "calendar_summary_layer",
											CLASS_CALENDAR_LAYER_FOLDER_SELECTION = "calendar_folder_dropdown_layer",
											CLASS_CALENDAR_LAYER_REMOVABLE = "calendar_is_unremovable_layer",
											CLASS_CALENDAR_LAYER_PRIVACY = "calendar_privacy_layer",
											CLASS_CALENDAR_LAYER_BUTTON_PRIVACY = "calendar_privacy_button_layer",

											CLASS_CALENDAR_REMOVABLE_CHECKBOX = "calendar_is_unremovable",
											CLASS_CALENDAR_FOLDER_SELECTION = "calendar_folder_dropdown",
											CLASS_CALENDAR_SUMMARY_INPUT = "calendar_summary",
											CLASS_CALENDAR_DESCRIPTION_INPUT = "calendar_description",
											CLASS_CALENDAR_NAME_INPUT = "calendar_name_input",
											CLASS_CALENDAR_GROUPS_SELECTION_BOX = "calendar_groups_selectionBox",
											
											CLASS_BUTTONS_LAYER_FOR_CALENDAR_CREATION = "calendar_create_buttons_layer",
											CLASS_BUTTONS_LAYER_FOR_CALENDAR_EDIT = "calendar_edit_buttons_layer",
											
											CLASS_CALENDAR_BUTTON_ADD = "add_calendar_button",
											CLASS_CALENDAR_BUTTON_CANCEL = "cancel_button",
											CLASS_CALENDAR_BUTTON_CREATE = "create_calendar_button",
											CLASS_CALENDAR_BUTTON_EDIT = "calendar_edit_button",
											CLASS_CALENDAR_BUTTON_DELETE = "calendar_delete",
											CLASS_CALENDAR_BUTTON_PRIVACY = "calendar_privacy",
											
											CLASS_CALENDAR_FORM_EDIT = "calendar_edit_form",
											CLASS_CALENDAR_FORM_VIEW = "calendar_view_form";

	public static final String 	FILE_CALENDAR_IMAGE_EDIT = "images/edit.png",
											FILE_CALENDAR_IMAGE_DELETE = "images/delete.png",
											FILE_CALENDAR_CREATOR_CSS = "style/bedeworkCalendarCreator.css",
											FILE_CALENDAR_CREATOR_JS = "javascript/bedeworkCalendarCreator.js";
	
	public static final String 	PARAMETER_ACTION = "prm_action",
											PARAMETER_CREATE = "prm_create",
											PARAMETER_CANCEL = "prm_cancel",
											PARAMETER_EDIT = "prm_edit",
											PARAMETER_DELETE = "prm_delete",
											PARAMETER_ADD = "prm_add";
	
	public static final String 	VALUE_CALENDAR_PRIVATE = "yes",
											VALUE_CALENDAR_PUBLIC = "no";
	
	private IWBundle iwb = null;
	
	private IWBundle getIWBundle(IWContext iwc) {
		if (iwc == null) {
			iwc = CoreUtil.getIWContext();
		}
		
		if (this.iwb == null) {
			this.iwb = iwc.getIWMainApplication().getBundle(BedeworkConstants.BUNDLE_IDENTIFIER);
		}
		
		return this.iwb;
	}
	
	@Autowired
	private JQuery jQuery;
	
	private JQuery getJQuery() {
		if (this.jQuery == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return this.jQuery;
	}
	
	@Autowired
	private BedeworkCalendarManagementService bcms;
	
	/**
	 * <p>Initializes service if down.</p>
	 * @return {@link BedeworkCalendarManagementServiceBean} instance or 
	 * <code>null</code>.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	private BedeworkCalendarManagementService getBedeworkCalendarManagementService() {
		if (this.bcms == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return this.bcms;
	}
	
	@Autowired
	private BedeworkCalendarPresentationComponentsService bcpcs;
	
	/**
	 * <p>Initializes service if down.</p>
	 * @return {@link BedeworkCalendarManagementServiceBean} instance or 
	 * <code>null</code>.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	private BedeworkCalendarPresentationComponentsService getBedeworkCalendarPresentationComponentsService() {
		if (this.bcpcs == null) {
			ELUtil.getInstance().autowire(this);
		}
		
		return this.bcpcs;
	}
	
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	@Override
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
		PresentationUtil.addStyleSheetsToHeader(iwc, Arrays.asList(
				getIWBundle(iwc).getVirtualPathWithFileNameString(
						FILE_CALENDAR_CREATOR_CSS
						)
		));
		
		if (!iwc.isLoggedOn()) {
			add(new Heading3(getLocalizedString("user_not_logged_on", "User is not logged on", iwc)));
			return;
		}
		
		PresentationUtil.addJavaScriptSourcesLinesToBody(iwc, Arrays.asList(
				CoreConstants.DWR_UTIL_SCRIPT,
				getJQuery().getBundleURIToJQueryLib(),
				getIWBundle(iwc).getVirtualPathWithFileNameString(
						FILE_CALENDAR_CREATOR_JS
						)
		));
		
		Form calendarEditForm = new Form();
		calendarEditForm.setStyleClass(CLASS_CALENDAR_FORM_EDIT);
		
		Form calendarViewForm = new Form();
		calendarViewForm.setStyleClass(CLASS_CALENDAR_FORM_VIEW);
		
		String action = iwc.getParameter(PARAMETER_ACTION);
		
		/* User asked to create/update calendar. */
		if (PARAMETER_CREATE.equals(action)) {
			boolean isUpdated = Boolean.FALSE;

			/* Checks if calendar is public or private. */
			Boolean isPrivate = null;
			String privacy = iwc.getParameter(CLASS_CALENDAR_BUTTON_PRIVACY);
			if (!StringUtil.isEmpty(privacy)) {
				if (privacy.equals(VALUE_CALENDAR_PRIVATE)) {
					isPrivate = Boolean.TRUE;
				} else if (privacy.equals(VALUE_CALENDAR_PUBLIC)) {
					isPrivate = Boolean.FALSE;
				}
			}

			/* Checks which groups are selected.  */
			String[] groups = iwc.getParameterValues(ATTRIBUTE_GROUPS);
			
			Set<Long> groupIDs = null;
			if (!ArrayUtil.isEmpty(groups)) {
				groupIDs = getBedeworkCalendarManagementService().convertGroupIDs(
						Arrays.asList(groups)
						);
			}
			
			/* Tries to update/create calendar with given values. */
			isUpdated = getBedeworkCalendarManagementService().updateCalendar(
					iwc.getCurrentUser(), 
					iwc.getParameter(ATTRIBUTE_NAME), 
					iwc.getParameter(ATTRIBUTE_FOLDER), 
					iwc.getParameter(ATTRIBUTE_SUMMARY), 
					!isPrivate, isPrivate ? groupIDs : null);
			
			/* Prints results. */
			if (isUpdated) {
				add(new Heading3(getLocalizedString("your_calendar_is_updated", 
						"Your calendar is updated!", iwc)));
			} else {
				add(new Heading3(getLocalizedString("failed_to_updated", 
						"Failed to update. Check your form!", iwc)));
			}
			
		/* User asked to remove calendar. */
		} else if (PARAMETER_DELETE.equals(action)) {
			
			/* Removing calendar of current user. */
			boolean isRemoved = getBedeworkCalendarManagementService().removeCalendar(
					iwc.getParameter(ATTRIBUTE_CALENDAR_ID), iwc.getCurrentUser());
			
			/* Prints results. */
			if (isRemoved) {
				add(new Heading3(getLocalizedString("calendar_successfully_removed", 
						"Calendar removed", iwc)));
			} else {
				add(new Heading3(getLocalizedString("failed_to_remove_calendar", 
						"Failed to remove calendar", iwc)));
			}
			
		/* User asked to open edit form. */
		} else if (PARAMETER_EDIT.equals(action)){
			
			/* Getting entity to edit. */
			CalendarEntity calendar = getBedeworkCalendarManagementService().getCalendar(
					iwc.getParameter(ATTRIBUTE_CALENDAR_ID));
			if (calendar == null) {
				add(new Heading3(getLocalizedString("not_possible_to_edit", 
						"Not possible to edit default calendar", iwc)));
			} else {
			
				/* Creating form for editing. */
				calendarEditForm.add(getNameLayer(iwc, calendar.getName()));
				calendarEditForm.add(getSummaryLayer(iwc, calendar.getSummary()));
				calendarEditForm.add(getPublicPrivateSelection(iwc, calendar.getPublick()));
				
				Layer selectionBoxLayer = getGroupsSelectionBox(iwc, calendar.getGroups());
				if (calendar.getPublick()) {
					selectionBoxLayer.setStyleAttribute("display", "none");
				} 				
				calendarEditForm.add(selectionBoxLayer);

				calendarEditForm.add(getButtonsForCalendarCreationLayer(iwc));
			}

		/* User asked to open creation form. */
		} else if (PARAMETER_ADD.equals(action)) {
			
			/* Creating empty form. */
			calendarEditForm.add(getNameLayer(iwc, null));
			calendarEditForm.add(getSummaryLayer(iwc, null));
			calendarEditForm.add(getPublicPrivateSelection(iwc, null));
			calendarEditForm.add(getGroupsSelectionBox(iwc, null));
			calendarEditForm.add(getButtonsForCalendarCreationLayer(iwc));

		/* User asked nothing, opening calendar view. */
		} else {
			Table2 table = new Table2();
			table.setStyleClass(CLASS_CALENDARS_TABLE);
			
			if (addCalendarsViewHeaders(iwc, table.createHeaderRowGroup()) 
					&& addCalendarsViewRows(iwc, table.createBodyRowGroup())) {
				add(table);
			} else {
				add(new Heading3(getLocalizedString(
						"no_calendars_found", 
						"No calendars found.", 
						iwc))
				);
			}
			
			calendarViewForm.add(getButtonsForCalendarAdditionLayer(iwc));
			add(calendarViewForm);
			return;
		}
		
		add(calendarEditForm);
	}
		
	
	private Layer getGroupsSelectionBox(IWContext iwc, Set<Long> groupIDs) {
		if (iwc == null) {
			return null;
		}
		
		Layer groupsSelectionBoxLayer = new Layer();
		groupsSelectionBoxLayer.setStyleClass(CLASS_CALENDAR_LAYER_GROUPS_SELECTION);
		
		GroupsFilter groupsFilter = new GroupsFilter();
		groupsFilter.setSelectedGroupParameterName(ATTRIBUTE_GROUPS);
		
		Layer groupsFilterLayer = new Layer();
		groupsFilterLayer.setStyleClass(CLASS_CALENDAR_GROUPS_SELECTION_BOX);
		groupsFilterLayer.add(groupsFilter);
		
		if (!ListUtil.isEmpty(groupIDs)) {
			List<String> localGroups = new ArrayList<String>(groupIDs.size());
			for (Long groupID : groupIDs) {
				localGroups.add(String.valueOf(groupID));
			}
			
			groupsFilter.setSelectedGroups(localGroups);
		}

		groupsSelectionBoxLayer.add(new Text(
				getLocalizedString("visible_for_groups", "Visible for groups:", iwc)));
		groupsSelectionBoxLayer.add(groupsFilter);
		
		return groupsSelectionBoxLayer;
	}
	
	/**
	 * <p>Adds headers: 'number', 'name', 'edit', 'delete' to headers.</p>
	 * @param iwc not <code>null</code>.
	 * @param headerRows where to add. Not <code>null</code>
	 * @return <code>true</code> on success, <code>false</code> on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	private boolean addCalendarsViewHeaders(IWContext iwc, 
			TableHeaderRowGroup headerRows) {
		if (iwc == null || headerRows == null) {
			return Boolean.FALSE;
		}
		
		headerRows.setStyleClass(CLASS_CALENDARS_TABLE_HEADER);
		
		TableRow header = headerRows.createRow();
		header.setStyleClass(CLASS_CALENDARS_TABLE_ROW);
		
		TableCell2 cell = header.createCell();
		cell.setStyleClass(CLASS_CALENDARS_TABLE_CELL);
		cell.add(new Text(getLocalizedString("number", "Nr.", iwc)));
		
		cell = header.createCell();
		cell.setStyleClass(CLASS_CALENDARS_TABLE_CELL);
		cell.add(new Text(getLocalizedString("name", "Name", iwc)));
		
		cell = header.createCell();
		cell.setStyleClass(CLASS_CALENDARS_TABLE_CELL);
		cell.add(new Text(getLocalizedString("edit", "Edit", iwc)));
		
		cell = header.createCell();
		cell.setStyleClass(CLASS_CALENDARS_TABLE_CELL);
		cell.add(new Text(getLocalizedString("delete", "Delete", iwc)));
		
		return Boolean.TRUE;
	}
	
	/**
	 * <p>Fills table with content of calendars.</p>
	 * @param iwc not <code>null</code>.
	 * @param bodyRows where to add. Not <code>null</code>
	 * @return <code>true</code> on success, <code>false</code> on failure. 
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	private boolean addCalendarsViewRows(IWContext iwc, 
			TableBodyRowGroup bodyRows) {
		if (iwc == null || bodyRows == null) {
			return Boolean.FALSE;
		}
		
		List<BwCalendar> calendars = getBedeworkCalendarManagementService()
				.getAllUserCalendars(iwc.getCurrentUser());
		
		if (ListUtil.isEmpty(calendars)) {
			return Boolean.FALSE;
		}
		
		bodyRows.setStyleClass(CLASS_CALENDARS_TABLE_BODY);
		
		int rowNumber = 1;
		for (BwCalendar calendar : calendars) {
			if (!addCalendarViewRow(iwc, calendar, bodyRows.createRow(), rowNumber++)) {
				return Boolean.FALSE;
			}
		}
		
		return Boolean.TRUE;
	}
	
	/**
	 * <p>Adds number in row, name, also 'edit' and 'remove' buttons.</p>
	 * @param iwc not <code>null</code>.
	 * @param calendar which information is shown in current row. Not <code>null</code>.
	 * @param tableRow where to add information.
	 * @param rowNumber - line number. 
	 * @return <code>true</code> if added, <code>false</code> otherwise.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	private boolean addCalendarViewRow(IWContext iwc, BwCalendar calendar, 
			TableRow tableRow, int rowNumber) {
		if (iwc == null || calendar == null || tableRow == null) {
			return Boolean.FALSE;
		}
		
		tableRow.setStyleClass(CLASS_CALENDARS_TABLE_ROW);
		tableRow.setStyleClass(calendar.getName());
		
		TableCell2 tableCell = tableRow.createCell();
		tableCell.setStyleClass(CLASS_CALENDARS_TABLE_CELL);
		tableCell.setStyleClass(CLASS_CALENDAR_NUMBER);
		tableCell.add(new Text(String.valueOf(rowNumber)));
		
		tableCell = tableRow.createCell();
		tableCell.setStyleClass(CLASS_CALENDARS_TABLE_CELL);
		tableCell.setStyleClass(CLASS_CALENDAR_NAME);
		tableCell.add(new Text(calendar.getName()));

		Link edit = new Link(getIWBundle(iwc).getImage(FILE_CALENDAR_IMAGE_EDIT, 
				getLocalizedString("edit", "Edit", iwc)));
		edit.addParameter(PARAMETER_ACTION, PARAMETER_EDIT);
		edit.addParameter(ATTRIBUTE_CALENDAR_ID, calendar.getId());
		
		tableCell = tableRow.createCell();
		tableCell.setStyleClass(CLASS_CALENDARS_TABLE_CELL);
		tableCell.setStyleClass(CLASS_CALENDAR_BUTTON_EDIT);
		tableCell.add(edit);
		
		Link delete = new Link(getIWBundle(iwc).getImage(FILE_CALENDAR_IMAGE_DELETE, 
				getLocalizedString("remove", "Remove", iwc))	);
		delete.addParameter(PARAMETER_ACTION, PARAMETER_DELETE);
		delete.addParameter(ATTRIBUTE_CALENDAR_ID, calendar.getId());
		
		tableCell = tableRow.createCell();
		tableCell.setStyleClass(CLASS_CALENDARS_TABLE_CELL);
		tableCell.setStyleClass(CLASS_CALENDAR_BUTTON_DELETE);
		tableCell.add(delete);
		
		return Boolean.TRUE;
	}
	
	/**
	 * <p>Creates field for {@link CalendarEntity#getName()}.</p>
	 * @param iwc not <code>null</code>.
	 * @param name {@link CalendarEntity#getName()}
	 * @return Layer with input and label or <code>null</code> on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	private Layer getNameLayer(IWContext iwc, String name) {
		Layer textInputLayer = new Layer();
		textInputLayer.setStyleClass(CLASS_CALENDAR_LAYER_NAME_INPUT);
		
		if (StringUtil.isEmpty(name)) {
			name = iwc.getParameter(ATTRIBUTE_NAME);
		}
		
		TextInput textInput =  new TextInput(ATTRIBUTE_NAME);
		textInput.setStyleClass(CLASS_CALENDAR_NAME_INPUT);
		
		if (!StringUtil.isEmpty(name)) {
			textInput.setContent(name);
		}
		
		Label inputLabel = new Label(getResourceBundle(iwc)
				.getLocalizedString("enter_calendar_name", "Enter calendar name:"), textInput);
		
		textInputLayer.add(inputLabel);
		textInputLayer.add(textInput);
		
		return textInputLayer;
	}
	
	/**
	 * <p>Adds field for {@link CalendarEntity#getDescription()}.</p>
	 * @param iwc not <code>null</code>
	 * @return {@link Layer} with {@link Label} and {@link TextArea} for description. 
	 * <code>null</code> on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	@SuppressWarnings("unused")
	private Layer getDescriptionLayer(IWContext iwc) {
		Layer descriptionLayer = new Layer();
		
		descriptionLayer.setStyleClass(CLASS_CALENDAR_LAYER_DESCRIPTION);
		
		TextArea textInput =  new TextArea(ATTRIBUTE_DESCRIPTION);
		textInput.setStyleClass(CLASS_CALENDAR_DESCRIPTION_INPUT);
		
		String description = iwc.getParameter(ATTRIBUTE_DESCRIPTION);
		if (!StringUtil.isEmpty(description)) {
			textInput.setContent(description);
		}
		
		Label inputLabel = new Label(getResourceBundle(iwc)
				.getLocalizedString("enter_calendar_description", "Enter calendar description:"),
						textInput);
		
		descriptionLayer.add(inputLabel);
		descriptionLayer.add(textInput);

		return descriptionLayer;
	}
	
	/**
	 * <p>Adds field for {@link CalendarEntity#getSummary()}.</p>
	 * @param iwc not <code>null</code>.
	 * @param summary to be added as value. Optional.
	 * @return {@link Layer} with {@link Label} and {@link TextArea} for summary or 
	 * <code>null</code> on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	private Layer getSummaryLayer(IWContext iwc, String summary) {
		Layer summaryLayer = new Layer();
		summaryLayer.setStyleClass(CLASS_CALENDAR_LAYER_SUMMARY);
		
		TextArea textInput =  new TextArea(ATTRIBUTE_SUMMARY);
		textInput.setStyleClass(CLASS_CALENDAR_SUMMARY_INPUT);
		
		if (StringUtil.isEmpty(summary)) {
			summary = iwc.getParameter(ATTRIBUTE_SUMMARY);
		}
		
		if (!StringUtil.isEmpty(summary)) {
			textInput.setContent(summary);
		}
		
		Label inputLabel = new Label(getResourceBundle(iwc)
				.getLocalizedString("enter_calendar_summary", "Enter calendar summary:"), 
				textInput);
		
		summaryLayer.add(inputLabel);
		summaryLayer.add(textInput);
		
		return summaryLayer;
	}
	
	/**
	 * <p>Gives list of {@link CalendarEntity#getColPath()} to select.</p>
	 * @param iwc not <code>null</code>.
	 * @return {@link Layer} with {@link Label} and {@link DropdownMenu} for 
	 * {@link CalendarEntity#getColPath()} selection or <code>null</code> on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	@SuppressWarnings("unused")
	private Layer getFolderSelectLayer(IWContext iwc) {
		if (iwc == null) {
			return null;
		}
		
		Layer folderDropdownLayer = new Layer();
		folderDropdownLayer.setStyleClass(CLASS_CALENDAR_LAYER_FOLDER_SELECTION);
		
		DropdownMenu folderDropdown = getBedeworkCalendarPresentationComponentsService()
				.getUserFoldersDropdown(iwc.getCurrentUser());
		
		if (folderDropdown == null) {
			return null;
		}
		
		folderDropdown.setName(ATTRIBUTE_FOLDER);
		folderDropdown.addFirstOption(
				new SelectOption(getResourceBundle(iwc)
						.getLocalizedString("select_folder", "Select folder:"), "-1"
						)
				);
		folderDropdown.setStyleClass(CLASS_CALENDAR_FOLDER_SELECTION);
		
		String folder = iwc.getParameter(ATTRIBUTE_FOLDER);
		if (!StringUtil.isEmpty(folder)) {
			folderDropdown.setSelectedElement(folder);
		}
		
		Label inputLabel = new Label(getResourceBundle(iwc)
				.getLocalizedString("choose_calendar_folder", "Choose calendar folder:"), 
				folderDropdown);
		
		folderDropdownLayer.add(inputLabel);
		folderDropdownLayer.add(folderDropdown);
		
		return folderDropdownLayer;
	}
	
	private Layer getCategoriesSelectionLayer(IWContext iwc) {
		// TODO
		return null;
	}
	
	/**
	 * <p>Checkbox for marking calendar as unremovable.</p>
	 * @param iwc not <code>null</code>.
	 * @return {@link Layer} with {@link Label} and {@link CheckBox} or <code>null</code>
	 *  on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	@SuppressWarnings("unused")
	private Layer getUnremovableCheckbox(IWContext iwc) {
		if (iwc == null) {
			return null;
		}
		
		Layer checkBoxLayer = new Layer();
		checkBoxLayer.setStyleClass(CLASS_CALENDAR_LAYER_REMOVABLE);
		
		CheckBox checkBox = new CheckBox(ATTRIBUTE_IS_REMOVABLE);
		checkBox.setStyleClass(CLASS_CALENDAR_REMOVABLE_CHECKBOX);
		
		String checkboxValue = iwc.getParameter(ATTRIBUTE_IS_REMOVABLE);
		if (!StringUtil.isEmpty(checkboxValue)) {
			checkBox.setChecked(Boolean.TRUE);
		}
		
		Label inputLabel = new Label(getResourceBundle(iwc)
				.getLocalizedString("calendar_is_unremovable", "Calendar is unremovable:"), checkBox);
		
		checkBoxLayer.add(inputLabel);
		checkBoxLayer.add(checkBox);
		
		return checkBoxLayer;
	}
	
	private Layer getPublicPrivateSelection(IWContext iwc, Boolean isPublic) {
		if (iwc == null) {
			return null;
		}
		
		Layer radioButtonLayer = new Layer();
		radioButtonLayer.setStyleClass(CLASS_CALENDAR_LAYER_PRIVACY);
		
		Layer innerRadioButtonLayer = new Layer();
		innerRadioButtonLayer.setStyleClass(CLASS_CALENDAR_LAYER_BUTTON_PRIVACY);
		RadioGroup radioButtonGroup = new RadioGroup(ATTRIBUTE_IS_PRIVATE);
		
		RadioButton publicButton = new RadioButton(CLASS_CALENDAR_BUTTON_PRIVACY, 
				VALUE_CALENDAR_PUBLIC);
		publicButton.setOnClick("BedeworkCalendarCreator.hide('"
				+ CLASS_CALENDAR_LAYER_GROUPS_SELECTION
				+ "');");
		radioButtonGroup.addRadioButton(publicButton, 
				new Text(getLocalizedString(VALUE_CALENDAR_PUBLIC, "No", iwc)));
		
		RadioButton privateButton = new RadioButton(CLASS_CALENDAR_BUTTON_PRIVACY, 
				VALUE_CALENDAR_PRIVATE);
		privateButton.setOnClick("BedeworkCalendarCreator.show('" 
				+ CLASS_CALENDAR_LAYER_GROUPS_SELECTION
				+ "');");
		radioButtonGroup.addRadioButton(privateButton, 
				new Text(getLocalizedString(VALUE_CALENDAR_PRIVATE, "Yes", iwc)), 
				Boolean.TRUE);
		
		innerRadioButtonLayer.add(radioButtonGroup);
		
		if (isPublic != null) {
			if (isPublic) {
				publicButton.setSelected();
				privateButton.setSelected(Boolean.FALSE);
			} else {
				privateButton.setSelected();
				publicButton.setSelected(Boolean.FALSE);
			}
		}
		
		String radioButtonValue = iwc.getParameter(CLASS_CALENDAR_BUTTON_PRIVACY);
		if (!StringUtil.isEmpty(radioButtonValue) && isPublic == null) {
			privateButton.setSelected(Boolean.FALSE);
			publicButton.setSelected(Boolean.FALSE);
			radioButtonGroup.setSelected(radioButtonValue);
		}
		
		Label inputLabel = new Label(getResourceBundle(iwc)
				.getLocalizedString(
						"private", 
						"Private:"
						), radioButtonGroup);
		
		radioButtonLayer.add(inputLabel);
		radioButtonLayer.add(innerRadioButtonLayer);
		
		return radioButtonLayer;
	}
	
	/**
	 * <p>Buttons for linking to form of new calendar.</p>
	 * @param iwc not <code>null</code>
	 * @return {@link Layer} with "back" and "new calendar" buttons. <code>null</code>
	 * on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	private Layer getButtonsForCalendarAdditionLayer(IWContext iwc) {
		if (iwc == null) {
			return null;
		}
		
		Layer buttonsLayer = new Layer();
		buttonsLayer.setStyleClass(CLASS_BUTTONS_LAYER_FOR_CALENDAR_EDIT);
		
		BackButton backButton = new BackButton(
				getResourceBundle(iwc).getLocalizedString("back", "Back"));
		backButton.setStyleClass(CLASS_CALENDAR_BUTTON_CANCEL);
		buttonsLayer.add(backButton);
		
		SubmitButton addButton = new SubmitButton(
				getResourceBundle(iwc)
						.getLocalizedString("new_calendar", "New calendar"),
						PARAMETER_ACTION, 
						PARAMETER_ADD);

		addButton.setStyleClass(CLASS_CALENDAR_BUTTON_ADD);
		
		buttonsLayer.add(addButton);
		
		return buttonsLayer;
	}
	
	/**
	 * <p>Creates {@link Layer} with buttons for {@link CalendarEntity} creation.</p>
	 * @param iwc not <code>null</code>.
	 * @return {@link Layer} with "cancel" and "create/update" buttons.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	private Layer getButtonsForCalendarCreationLayer(IWContext iwc) {
		if (iwc == null) {
			return null;
		}
		
		Layer buttonsLayer = new Layer();
		buttonsLayer.setStyleClass(CLASS_BUTTONS_LAYER_FOR_CALENDAR_CREATION);
		
		BackButton cancelButton = new BackButton(
				getResourceBundle(iwc).getLocalizedString("cancel", "Cancel"));
		cancelButton.setStyleClass(CLASS_CALENDAR_BUTTON_CANCEL);
		buttonsLayer.add(cancelButton);
		
		SubmitButton createButton = new SubmitButton(
				getResourceBundle(iwc)
						.getLocalizedString("create_update_calendar", "Create/Update calendar"),
				PARAMETER_ACTION, 
				PARAMETER_CREATE);
		createButton.setStyleClass(CLASS_CALENDAR_BUTTON_CREATE);
		createButton.setOnClick("BedeworkCalendarCreator.showCreatingMessage('"
				+ CLASS_CALENDAR_BUTTON_CREATE + "');");
		buttonsLayer.add(createButton);
		
		return buttonsLayer;
	}
}
