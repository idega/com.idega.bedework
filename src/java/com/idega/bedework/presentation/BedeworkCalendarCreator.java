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

import java.util.Arrays;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.bedework.BedeworkConstants;
import com.idega.bedework.bussiness.BedeworkCalendarManagementService;
import com.idega.bedework.bussiness.BedeworkCalendarPresentationComponentsService;
import com.idega.bedework.bussiness.impl.BedeworkCalendarManagementServiceBean;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.RadioGroup;
import com.idega.presentation.ui.SelectOption;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.util.ArrayUtil;
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
	
	public static final String ATTRIBUTE_NAME = "attr_name";
	public static final String ATTRIBUTE_SUMMARY = "attr_summary";
	public static final String ATTRIBUTE_DESCRIPTION = "attr_description";
	public static final String ATTRIBUTE_FOLDER = "attr_folder";
	public static final String ATTRIBUTE_IS_REMOVABLE = "attr_is_removable";
	public static final String ATTRIBUTE_IS_PUBLIC = "attr_is_public";
	public static final String ATTRIBUTE_GROUPS = "attr_groups";
	
	public static final String PARAMETER_ACTION = "prm_action";
	public static final String PARAMETER_CREATE = "prm_create";
	public static final String PARAMETER_CANCEL = "prm_cancel";
	
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
		
		IWBundle bundle = getBundle(iwc);
		if (bundle == null) {
			return;
		}
		
		PresentationUtil.addStyleSheetsToHeader(iwc, Arrays.asList(
				iwc.getIWMainApplication().getBundle(BedeworkConstants.BUNDLE_IDENTIFIER)
				.getVirtualPathWithFileNameString("style/BedeworkCalendarCreator.css")
		));
		
		Form form = new Form();
		
		form.add(getNameLayer(iwc));
		form.add(getSummaryLayer(iwc));
		form.add(getPublicPrivateSelection(iwc));
		form.add(getGroupsSelectionBoxLayer(iwc));
		form.add(getButtonsLayer(iwc));
		
		add(form);
		
		String action = iwc.getParameter(PARAMETER_ACTION);
		if (PARAMETER_CREATE.equals(action)) {
			boolean isPublic = Boolean.FALSE;
			String publicity = iwc.getParameter(ATTRIBUTE_IS_PUBLIC);
			if (!StringUtil.isEmpty(publicity)) {
				if (publicity.equals("public")) {
					isPublic = Boolean.TRUE;
				}
			}

			String[] groups = iwc.getParameterValues(ATTRIBUTE_GROUPS);
			Set<Long> groupIDs = getBedeworkCalendarManagementService().convertGroupIDs(Arrays.asList(groups));
			
			getBedeworkCalendarManagementService().updateCalendar(
					iwc.getCurrentUser(), 
					iwc.getParameter(ATTRIBUTE_NAME), 
					iwc.getParameter(ATTRIBUTE_FOLDER), 
					iwc.getParameter(ATTRIBUTE_SUMMARY), 
					isPublic, groupIDs);
		}
	}
	
	private Layer getGroupsSelectionBoxLayer(IWContext iwc) {
		if (iwc == null) {
			return null;
		}
		
		Layer groupsSelectionBoxLayer = new Layer();
		groupsSelectionBoxLayer.setStyleClass("calendar_groups_selection_layer");
		
		SelectionBox groupsSelectionBox = getBedeworkCalendarPresentationComponentsService()
				.getGroupsSelectionBox(null);
		
		if (groupsSelectionBox == null) {
			return null;
		}
		
		groupsSelectionBox.setName(ATTRIBUTE_GROUPS);
		groupsSelectionBox.setStyleClass("calendar_groups_selectionBox");
		
		String[] groups = iwc.getParameterValues(ATTRIBUTE_GROUPS);
		if (!ArrayUtil.isEmpty(groups)) {
			for (String group : groups) {
				groupsSelectionBox.setSelectedElement(group);
			}
		}
		
		Label inputLabel = new Label(getResourceBundle(iwc)
				.getLocalizedString("choose_calendar_groups", "Choose calendar groups:"), 
				groupsSelectionBox);
		
		groupsSelectionBoxLayer.add(inputLabel);
		groupsSelectionBoxLayer.add(groupsSelectionBox);
		
		return groupsSelectionBoxLayer;
	}
	
	private Layer getNameLayer(IWContext iwc) {
		Layer textInputLayer = new Layer();
		textInputLayer.setStyleClass("calendar_name_layer");
		
		TextInput textInput =  new TextInput(ATTRIBUTE_NAME);
		textInput.setStyleClass("calendar_name");
		
		String name = iwc.getParameter(ATTRIBUTE_NAME);
		if (!StringUtil.isEmpty(name)) {
			textInput.setContent(name);
		}
		
		Label inputLabel = new Label(getResourceBundle(iwc)
				.getLocalizedString("enter_calendar_name", "Enter calendar name:"), textInput);
		
		textInputLayer.add(inputLabel);
		textInputLayer.add(textInput);
		
		return textInputLayer;
	}
	
	private Layer getDescriptionLayer(IWContext iwc) {
		Layer descriptionLayer = new Layer();
		descriptionLayer.setStyleClass("calendar_description_layer");
		
		TextArea textInput =  new TextArea(ATTRIBUTE_DESCRIPTION);
		textInput.setStyleClass("calendar_description");
		
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
	
	private Layer getSummaryLayer(IWContext iwc) {
		Layer summaryLayer = new Layer();
		summaryLayer.setStyleClass("calendar_summary_layer");
		
		TextArea textInput =  new TextArea(ATTRIBUTE_SUMMARY);
		textInput.setStyleClass("calendar_summary");
		String summary = iwc.getParameter(ATTRIBUTE_SUMMARY);
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
	
	@SuppressWarnings("unused")
	private Layer getFolderSelectLayer(IWContext iwc) {
		if (iwc == null) {
			return null;
		}
		
		Layer folderDropdownLayer = new Layer();
		folderDropdownLayer.setStyleClass("calendar_folder_dropdown_layer");
		
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
		folderDropdown.setStyleClass("calendar_folder_dropdown");
		
		String folder = iwc.getParameter(ATTRIBUTE_FOLDER);
		if (!StringUtil.isEmpty(folder)) {
			folderDropdown.setSelectedElement(folder);
			folderDropdown.setDisabled(Boolean.TRUE);
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
	
	@SuppressWarnings("unused")
	private Layer getUnremovableCheckbox(IWContext iwc) {
		if (iwc == null) {
			return null;
		}
		
		Layer checkBoxLayer = new Layer();
		checkBoxLayer.setStyleClass("calendar_is_unremovable_layer");
		
		CheckBox checkBox = new CheckBox(ATTRIBUTE_IS_REMOVABLE);
		checkBox.setStyleClass("calendar_is_unremovable");
		
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
	
	private Layer getPublicPrivateSelection(IWContext iwc) {
		if (iwc == null) {
			return null;
		}
		
		Layer radioButtonLayer = new Layer();
		radioButtonLayer.setStyleClass("calendar_publicity_layer");
		
		RadioGroup radioButton = new RadioGroup(ATTRIBUTE_IS_PUBLIC);
		radioButton.addRadioButton("public", 
				new Text(getResourceBundle(iwc).getLocalizedString("public",	 "Public")));
		radioButton.addRadioButton("private", 
				new Text(getResourceBundle(iwc).getLocalizedString("private",	 "Private")), 
				Boolean.TRUE);
		
		radioButton.setStyleClass("calendar_publicity");
		
		String radioButtonValue = iwc.getParameter(ATTRIBUTE_IS_PUBLIC);
		if (!StringUtil.isEmpty(radioButtonValue)) {
			radioButton.setSelected(radioButtonValue);
		}
		
		Label inputLabel = new Label(getResourceBundle(iwc)
				.getLocalizedString(
						"is_calendar_private_or_public", 
						"Is calendar private or public?"
						), radioButton);
		
		radioButtonLayer.add(inputLabel);
		radioButtonLayer.add(radioButton);
		
		return radioButtonLayer;
	}

	private Layer getButtonsLayer(IWContext iwc) {
		if (iwc == null) {
			return null;
		}
		
		Layer buttonsLayer = new Layer();
		buttonsLayer.setStyleClass("buttonsLayer");
		
		SubmitButton cancelButton = new SubmitButton(
				getResourceBundle(iwc).getLocalizedString("cancel", "Cancel"), 
				PARAMETER_ACTION, 
				PARAMETER_CANCEL);
		cancelButton.setStyleClass("cancel_button");
		buttonsLayer.add(cancelButton);
		
		SubmitButton createButton = new SubmitButton(
				getResourceBundle(iwc)
						.getLocalizedString("create_update_calendar", "Create/Update calendar"),
				PARAMETER_ACTION, 
				PARAMETER_CREATE);

		createButton.setStyleClass("create_calendar_button");
		buttonsLayer.add(createButton);
		
		return buttonsLayer;
	}
}
