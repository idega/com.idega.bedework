/**
 * Javascript helper for BedeworkCalendarCreator.java component.
 */

if (BedeworkCalendarCreator == null) 
	var BedeworkCalendarCreator = {};

BedeworkCalendarCreator.STRING_CREATING = 'Creating...';

BedeworkCalendarCreator.hide = function(styleID) {
	if (styleID == null || styleID == '') {
		return false;
	}
	
	var element = jQuery('.'+styleID);
	if (element == null || element.length <= 0) {
		return false;
	}
	element.slideUp();

//	element.fadeOut('slow');
	return true;
}

BedeworkCalendarCreator.show = function(styleID) {
	if (styleID == null || styleID == '') {
		return false;
	}
	
	var element = jQuery('.'+styleID);
	if (element == null || element.length <= 0) {
		return false;
	}
	element.slideDown("slow");

//	element.fadeIn('slow');
	
	return true;
}

BedeworkCalendarCreator.showCreatingMessage = function(styleID) {
	if (styleID == null || styleID == '') {
		return false;
	}
	
	showLoadingMessage(BedeworkCalendarCreator.STRING_CREATING);
	jQuery('div.'+styleID).delay(3000);
	closeAllLoadingMessages();
}
