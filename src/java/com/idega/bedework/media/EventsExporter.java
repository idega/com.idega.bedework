/**
 * @(#)ICalExportServlet.java    1.0.0 9:51:42 AM
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
package com.idega.bedework.media;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import net.fortuna.ical4j.model.Calendar;

import org.bedework.calfacade.svc.EventInfo;
import org.bedework.icalendar.IcalTranslator;
import org.bedework.icalendar.Icalendar;
import org.springframework.beans.factory.annotation.Autowired;

import com.idega.bedework.bussiness.BedeworkAPI;
import com.idega.bedework.bussiness.BedeworkCalendarsService;
import com.idega.bedework.bussiness.BedeworkEventsService;
import com.idega.core.file.util.MimeTypeUtil;
import com.idega.io.DownloadWriter;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.util.FileUtil;
import com.idega.util.IOUtil;
import com.idega.util.ListUtil;
import com.idega.util.expression.ELUtil;

/**
 * <p>Servlet for exporting Ical4j calendars to external device.</p>
 * <p>You can report about problems to:
 * <a href="mailto:martynas@idega.com">Martynas Stakė</a></p>
 * <p>You can expect to find some test cases notice in the end of the file.</p>
 *
 * @version 1.0.0 Oct 3, 2012
 * @author martynasstake
 */
public class EventsExporter extends DownloadWriter {

	public static final String 	PARAMETER_CALENDAR = "prm_calendar",
								PARAMETER_DATE_TO = "prm_date_to",
								PARAMETER_DATE_FROM = "prm_date_from",
								PARAMETER_SHOW_ALL_MY_EVENTS = "prm_show_all_my_events";

	private static Logger LOGGER = Logger.getLogger(EventsExporter.class.getName());

	private byte[] data;

	@Autowired
	private BedeworkCalendarsService bcs;

	private BedeworkCalendarsService getBedeworkCalendarsService() {
		if (this.bcs == null) {
			ELUtil.getInstance().autowire(this);
		}

		return this.bcs;
	}

	@Autowired
	private BedeworkEventsService bes;

	private BedeworkEventsService getBedeworkEventsService() {
		if (this.bes == null) {
			ELUtil.getInstance().autowire(this);
		}

		return this.bes;
	}

	@Override
	public String getMimeType() {
		return MimeTypeUtil.MIME_TYPE_CALENDAR;
	}

	@Override
	public void writeTo(OutputStream output) throws IOException {
		if (data == null)
			return;

		ByteArrayInputStream input = null;
		try {
			input = new ByteArrayInputStream(data);
			FileUtil.streamToOutputStream(input, output);
		} finally {
			IOUtil.close(input);
			output.flush();
			output.close();
		}
	}

	@Override
	public void init(HttpServletRequest req, IWContext iwc) {
		if (iwc == null) {
			LOGGER.warning(IWContext.class.getName() + " is not provided");
			return;
		}

		User user = iwc.isLoggedOn() ? iwc.getCurrentUser() : null;
		if (user == null) {
			LOGGER.warning("User is not logged in");
			return;
		}

		Collection<EventInfo> events = null;
		if (iwc.isParameterSet(PARAMETER_CALENDAR))
			events = getBedeworkEventsService().getEvents(
					user,
					getBedeworkCalendarsService().getCalendarByPath(
							user,
							iwc.getParameter(PARAMETER_CALENDAR)),
					null, null, null, null, null);
		else if (iwc.isParameterSet(PARAMETER_SHOW_ALL_MY_EVENTS))
			events = getBedeworkEventsService().getEvents(user);
		if (ListUtil.isEmpty(events)) {
			LOGGER.warning("There are not events for " + user);
			events = Collections.emptyList();
		}

		BedeworkAPI bwAPI = new BedeworkAPI(user);
		if (bwAPI.getIcalCallback() == null) {
			LOGGER.warning("Bedework API is unavailable");
			return;
		}

		PrintWriter writer = null;
		ByteArrayOutputStream output = null;
		IcalTranslator trans = new IcalTranslator(bwAPI.getIcalCallback());
		try {
			output = new ByteArrayOutputStream();
			writer = new PrintWriter(output, true);
			Calendar iCal = trans.toIcal(events, Icalendar.methodTypePublish);
			IcalTranslator.writeCalendar(iCal, writer);
			data = output.toByteArray();
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Error exporting events " + events, e);
		} finally {
			if (writer != null)
				writer.close();
			IOUtil.close(output);
		}

	    setAsDownload(iwc, "Events for " + user.getName() + ".ics", data == null ? 0 : data.length);
	}
}
