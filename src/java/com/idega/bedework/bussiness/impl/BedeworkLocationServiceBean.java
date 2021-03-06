/**
 * @(#)BedeworkLocationServiceBean.java    1.0.0 1:44:56 PM
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
package com.idega.bedework.bussiness.impl;

import java.util.Collection;
import java.util.logging.Level;

import org.bedework.calfacade.BwLocation;
import org.bedework.calfacade.BwString;
import org.bedework.calfacade.BwUser;
import org.bedework.calfacade.exc.CalFacadeException;
import org.bedework.calsvci.EventProperties;
import org.hsqldb.lib.StringUtil;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.bedework.bussiness.BedeworkLocationService;
import com.idega.bedework.bussiness.BedeworkAPI;
import com.idega.bedework.bussiness.UserAdapter;
import com.idega.core.business.DefaultSpringBean;
import com.idega.user.data.User;
import com.idega.util.ListUtil;

/**
 * Class description goes here.
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.com">Martynas Stakė</a></p>
 * <p>You can expect to find some test cases notice in the end of the file.</p>
 *
 * @version 1.0.0 Oct 1, 2012
 * @author martynasstake
 */
@Service(BedeworkLocationService.BEAN_IDENTIFIER)
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class BedeworkLocationServiceBean extends DefaultSpringBean implements BedeworkLocationService{

	@Override
	public BwLocation getOrCreateLocation(User user, String address) {
		if (user == null || StringUtil.isEmpty(address)) {
			return null;
		}
		
		BwLocation location = getLocation(user, address);
		if (location != null) {
			return location;
		}
		
		if (updateLocation(user, null, address, null, true)) {
			return getLocation(user, address);
		}
		
		getLogger().log(Level.WARNING, "Unable to get location: " + address);
		return null;
	}
	
	@Override
	public BwLocation setLocation(User user, BwLocation location, String address, String subAddress, boolean isPublic) {
		if (user == null || StringUtil.isEmpty(address)) {
			return null;
		}
		
		UserAdapter adapter = new UserAdapter(user);
		BwUser bedeworkUser = adapter.getBedeworkSystemUser();
		
		if (location == null) {
			location = new BwLocation();
		}
		
		location.setAddress(new BwString(getCurrentLocale().getLanguage(), address));
		if (!StringUtil.isEmpty(subAddress)) {
			location.setSubaddress(
					new BwString(getCurrentLocale().getLanguage(), subAddress));
		}
		
		// User info:
		location.setCreatorEnt(bedeworkUser);
		location.setCreatorHref(bedeworkUser.getPrincipalRef());
		location.setOwnerHref(bedeworkUser.getPrincipalRef());
		
		// Visibility:
		location.setPublick(isPublic);
		
		return location;
	}
	
	@Override
	public boolean updateLocation(User user, BwLocation location) {
		if (user == null || location == null) {
			return Boolean.FALSE;
		}
		
		BwLocation locationInDatabase = getLocation(user, location.getId());
		
		BedeworkAPI bwAPI = new BedeworkAPI(user);
		EventProperties<BwLocation> locationHandler = bwAPI.getLocationsHandler();
		if (locationHandler == null) {
			return Boolean.FALSE;
		}
		
		try {
			if (locationInDatabase == null) {
				return locationHandler.add(location);
			} else {
				locationHandler.update(location);
				return Boolean.TRUE;
			}
		} catch (CalFacadeException e) {
			getLogger().log(Level.WARNING, "Unable to update location: ", e);
			return Boolean.FALSE;
		} finally {
			bwAPI.closeBedeworkAPI();
		}
	}
	
	@Override
	public boolean deleteLocation(User user, BwLocation location) {
		if (user == null || location == null) {
			return Boolean.FALSE;
		}
				
		BedeworkAPI bwAPI = new BedeworkAPI(user);
		EventProperties<BwLocation> locationHandler = bwAPI.getLocationsHandler();
		if (locationHandler == null) {
			return Boolean.FALSE;
		}
		
		try {			
			if (locationHandler.delete(location) == 0) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}
		} catch (CalFacadeException e) {
			getLogger().log(Level.WARNING, "Unable to get locations: ", e);
			return Boolean.FALSE;
		} finally {
			bwAPI.closeBedeworkAPI();
		}
	}

	@Override
	public Collection<BwLocation> getLocations(User user) {
		if (user == null) {
			return null;
		}
		
		BedeworkAPI bwAPI = new BedeworkAPI(user);
		EventProperties<BwLocation> locationHandler = bwAPI.getLocationsHandler();
		if (locationHandler == null) {
			return null;
		}
		
		Collection<BwLocation> locations = null;
		try {
			locations = locationHandler.get();
		} catch (CalFacadeException e) {
			getLogger().log(Level.WARNING, "Unable to get locations: ", e);
		}
		
		bwAPI.closeBedeworkAPI();
		
		return locations;
	}
	
	@Override
	public BwLocation getLocation(User user, String address) {
		return getLocation(user, 
				new BwString(getCurrentLocale().getLanguage(), address)
		);
	}
	
	@Override
	public BwLocation getLocation(User user, BwString address) {
		if (address == null) {
			return null;
		}
		
		Collection<BwLocation> locations = getLocations(user);
		if (ListUtil.isEmpty(locations)) {
			return null;
		}
		
		BwLocation requiredlocation = null;
		BedeworkAPI api = new BedeworkAPI(user);
		for (BwLocation location : locations) {
			
			api.reAttach(location);
			
			if (address.equals(location.getAddress())) {
				return location;
			}
		}
		
		return null;
	}
	
	@Override
	public BwLocation getLocation(User user, int ID) {
		Collection<BwLocation> locations = getLocations(user);
		if (ListUtil.isEmpty(locations)) {
			return null;
		}
		
		for (BwLocation location : locations) {
			if (location.getId() == ID) {
				return location;
			}
		}
		
		return null;
	}



	@Override
	public boolean updateLocation(User user, Integer ID, String address,
			String subAddress, boolean isPublic) {
		BwLocation location = null;
		
		if (ID != null) {
			location = getLocation(user, ID);
		}
		
		return updateLocation(user, setLocation(user, location, address, 
				subAddress, isPublic));
	}



	@Override
	public boolean deleteLocation(User user, int locationID) {
		return deleteLocation(user, getLocation(user, locationID));
	}
}
