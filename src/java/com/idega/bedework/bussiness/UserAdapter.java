/**
 * @(#)UserAdapter.java    1.0.0 1:21:28 PM
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
package com.idega.bedework.bussiness;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJBException;

import org.bedework.calfacade.BwPrincipalInfo;
import org.bedework.calfacade.exc.CalFacadeException;
import org.bedework.calsvci.UsersI;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.presentation.IWContext;
import com.idega.user.business.UserBusiness;


/**
 * <p>Adapter to communicate between Idega and Bedework users.</p>
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.com">Martynas Stakė</a></p>
 * <p>You can expect to find some test cases notice in the end of the file.</p>
 *
 * @version 1.0.0 Apr 24, 2012
 * @author martynasstake
 */
public class UserAdapter {
	private static final Logger LOGGER = Logger.getLogger(UserAdapter.class.getName());
	
	private org.bedework.calfacade.BwUser bedeworkUser = null;
	private com.idega.user.data.User idegaUser = null;
	
	private UserBusiness userBusiness = null;
	
	public UserAdapter(org.bedework.calfacade.BwUser user) {
		this.bedeworkUser = user;
	}
	
	public UserAdapter(com.idega.user.data.User user) {
		this.idegaUser = user;
	}
	
	/**
	 * <p>We use this id for calendar and directory names.</p>
	 * @return id for names of directories.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public String getID() {
		com.idega.user.data.User user = getIdegaSystemUser();
		if (user == null) {
			return null;
		}
		
		return String.valueOf(user.getPrimaryKey());
	}
	
	/**
	 * <p>Returns given user in Bedework framework representation.</p>
	 * @return Bedework framework user. <code>null</code> on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public org.bedework.calfacade.BwUser getBedeworkSystemUser() {
		if (this.bedeworkUser != null) {
			return this.bedeworkUser;
		}
		
		BwAPI bwAPI = new BwAPI(idegaUser);
		if (!bwAPI.openBedeworkAPI()) {
			return null;
		}
		
		UsersI usersHandler = bwAPI.getUsersHandler();
		
		try {
			this.bedeworkUser = usersHandler.getUser(getID());
		} catch (EJBException e) {
			LOGGER.log(Level.WARNING, "Unable to find primary key of user.");
		} catch (CalFacadeException e) {
			LOGGER.log(Level.INFO, "Unable to get such user from Bedework. " +
					"User data will be automatically syncronized with Bedework system.");
		}
		
		if (this.bedeworkUser == null) {
			synchronizeUser();
		}
		
		bwAPI.closeBedeworkAPI();
		return this.bedeworkUser;
	}

	/**
	 * <p>Returns given user in Idega framework representation.</p>
	 * @return Idega framework user. <code>null</code> on failure.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public com.idega.user.data.User getIdegaSystemUser() {
		if (this.idegaUser != null) {
			return this.idegaUser;
		}
		
		try {
			this.idegaUser = getUserBusiness().getUser(this.bedeworkUser.getId());
		} catch (RemoteException e) {
			LOGGER.log(Level.WARNING, "Unable to get idega user.");
		}
		
		return this.idegaUser;
	}
	
	/**
	 * <p>Creates or updates user in bedework framework, as it is on Idega framework.
	 * Only changes on Idega framework are synchronized to Bedework. 
	 * </p>
	 * @return <code>true</code> if synchronization successful, <code>false</code> 
	 * otherwise.
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public boolean synchronizeUser() {
		BwAPI bwAPI = new BwAPI(getIdegaSystemUser());
		if (!bwAPI.openBedeworkAPI()) {
			return Boolean.FALSE;
		}
		
		UsersI usersHandler = bwAPI.getUsersHandler();
		if (usersHandler  == null) {
			return Boolean.FALSE;
		}

		try {
			if (usersHandler.getUser(getID()) == null) {
				usersHandler.add(getID());
			}

			this.bedeworkUser = usersHandler.getUser(getID());
		} catch (CalFacadeException e) {
			LOGGER.log(Level.WARNING, "Unable to initialize user.", e);
			return Boolean.FALSE;
		}
		
		BwPrincipalInfo bwpi = new BwPrincipalInfo();
		try {
			com.idega.core.contact.data.Email email = getIdegaSystemUser().getUsersEmail();
			if (email != null) {
				bwpi.setEmail(email.getEmailAddress());
			}
			
			com.idega.core.contact.data.Phone phone = getIdegaSystemUser().getUsersHomePhone();
			if (phone != null) {
				bwpi.setPhone(phone.getNumber());
			}
		} catch (EJBException e) {
			LOGGER.log(Level.WARNING, "Unable to find idega user metadata.");
		} catch (RemoteException e) {
			LOGGER.log(Level.WARNING, "Unable to get idega user metadata.");
		}
		
		bwpi.setFirstname(getIdegaSystemUser().getFirstName());
		bwpi.setLastname(getIdegaSystemUser().getLastName());
		bwpi.setPrincipalHref(this.bedeworkUser.getPrincipalRef());
		
		this.bedeworkUser.setCreated(getIdegaSystemUser().getCreated());
		this.bedeworkUser.setLastAccess(getIdegaSystemUser().getCreated());
		this.bedeworkUser.setLastModify(getIdegaSystemUser().getCreated());
		this.bedeworkUser.setPrincipalInfo(bwpi);
		
		try {
			usersHandler.update(getBedeworkSystemUser());
		} catch (CalFacadeException e) {
			LOGGER.log(Level.SEVERE, "Unable to update user in Bedework system", e);
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}
	
	private UserBusiness getUserBusiness() {
		if (this.userBusiness == null) {
			try {
				this.userBusiness= IBOLookup.getServiceInstance(
						IWContext.getCurrentInstance().getApplicationContext(), 
						UserBusiness.class);
			} catch (IBOLookupException e) {
				LOGGER.log(Level.WARNING, "Unable to get UserBusiness.");
			}
		}
		
		return this.userBusiness;
	}
}
