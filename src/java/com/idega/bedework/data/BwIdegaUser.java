/**
 * @(#)BwIdegaUser.java    1.0.0 12:19:09 PM
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
package com.idega.bedework.data;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.RemoveException;

import org.bedework.calfacade.BwUser;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.idega.core.user.data.User;
import com.idega.data.BlobWrapper;
import com.idega.data.EntityAttribute;
import com.idega.data.IDOEntity;
import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOLegacyEntity;
import com.idega.data.IDOStoreException;

/**
 * Class description goes here.
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.com">Martynas Stakė</a></p>
 * <p>You can expect to find some test cases notice in the end of the file.</p>
 *
 * @version 1.0.0 Apr 19, 2012
 * @author martynasstake
 */
public class BwIdegaUser implements User {
	
	private BwUser bwUser = null;

	public BwIdegaUser(BwUser bwUser) {
		this.bwUser = bwUser;
	}
	
	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#insert()
	 */
	@Override
	public void insert() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getCharColumnValue(java.lang.String)
	 */
	@Override
	public char getCharColumnValue(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#setRelationShipClassName(java.lang.String, java.lang.String)
	 */
	@Override
	public void setRelationShipClassName(String p0, String p1) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#addTreeRelationShip()
	 */
	@Override
	public void addTreeRelationShip() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getRelationShipClass(java.lang.String)
	 */
	@Override
	public Class getRelationShipClass(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#addTo(com.idega.data.IDOLegacyEntity, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addTo(IDOLegacyEntity p0, String p1, String p2, String p3,
			String p4) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#addTo(com.idega.data.IDOLegacyEntity, java.lang.String, java.lang.String)
	 */
	@Override
	public void addTo(IDOLegacyEntity p0, String p1, String p2)
			throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#addTo(com.idega.data.IDOLegacyEntity)
	 */
	@Override
	public void addTo(IDOLegacyEntity p0) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#updateMetaData()
	 */
	@Override
	public void updateMetaData() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#addMetaData(java.lang.String, java.lang.String)
	 */
	@Override
	public void addMetaData(String p0, String p1) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#addMetaData(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addMetaData(String p0, String p1, String p2) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getNameOfMiddleTable(com.idega.data.IDOEntity, com.idega.data.IDOEntity)
	 */
	@Override
	public String getNameOfMiddleTable(IDOEntity p0, IDOEntity p1) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAllOrdered(java.lang.String)
	 */
	@Override
	public IDOLegacyEntity[] findAllOrdered(String p0) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#isPrimaryKey(java.lang.String)
	 */
	@Override
	public boolean isPrimaryKey(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#setColumn(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setColumn(String p0, Object p1) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#setColumn(java.lang.String, java.lang.Boolean)
	 */
	@Override
	public void setColumn(String p0, Boolean p1) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getIfNullable(java.lang.String)
	 */
	@Override
	public boolean getIfNullable(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#setName(java.lang.String)
	 */
	@Override
	public void setName(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#deleteMultiple(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteMultiple(String p0, String p1) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#deleteMultiple(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteMultiple(String columnName1, String stringColumnValue1,
			String columnName2, String stringColumnValue2) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#update()
	 */
	@Override
	public void update() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getIfVisible(java.lang.String)
	 */
	@Override
	public boolean getIfVisible(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getStringColumnValue(java.lang.String)
	 */
	@Override
	public String getStringColumnValue(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAssociatedOrdered(com.idega.data.IDOLegacyEntity, java.lang.String)
	 */
	@Override
	public IDOLegacyEntity[] findAssociatedOrdered(IDOLegacyEntity p0, String p1)
			throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getEditableColumnNames()
	 */
	@Override
	public String[] getEditableColumnNames() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#metaDataHasChanged(boolean)
	 */
	@Override
	public void metaDataHasChanged(boolean p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getBlobColumnValue(java.lang.String)
	 */
	@Override
	public BlobWrapper getBlobColumnValue(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getMetaDataIds()
	 */
	@Override
	public Hashtable getMetaDataIds() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getInputStreamColumnValue(java.lang.String)
	 */
	@Override
	public InputStream getInputStreamColumnValue(String p0) throws Exception {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#setStringColumn(java.lang.String, java.lang.String)
	 */
	@Override
	public void setStringColumn(String p0, String p1) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#removeFrom(com.idega.data.IDOLegacyEntity)
	 */
	@Override
	public void removeFrom(IDOLegacyEntity p0) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAll(java.lang.String, int)
	 */
	@Override
	public IDOLegacyEntity[] findAll(String p0, int p1) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#setID(java.lang.Integer)
	 */
	@Override
	public void setID(Integer p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#addTo(com.idega.data.IDOLegacyEntity, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addTo(IDOLegacyEntity p0, String p1, String p2, String p3,
			String p4, String p5, String p6) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#insertMetaData()
	 */
	@Override
	public void insertMetaData() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getMetaDataUpdateVector()
	 */
	@Override
	public Vector getMetaDataUpdateVector() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#removeMetaData(java.lang.String)
	 */
	@Override
	public boolean removeMetaData(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getMetaDataInsertVector()
	 */
	@Override
	public Vector getMetaDataInsertVector() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getNumberOfRecordsReverseRelated(com.idega.data.IDOLegacyEntity)
	 */
	@Override
	public int getNumberOfRecordsReverseRelated(IDOLegacyEntity p0)
			throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAllByColumnOrdered(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IDOLegacyEntity[] findAllByColumnOrdered(String p0, String p1,
			String p2) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAllByColumnEqualsOrdered(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IDOLegacyEntity[] findAllByColumnEqualsOrdered(String p0, String p1,
			String p2) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getStorageClassType(java.lang.String)
	 */
	@Override
	public int getStorageClassType(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#setColumn(java.lang.String, java.io.InputStream)
	 */
	@Override
	public void setColumn(String p0, InputStream p1) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#setMaxLength(java.lang.String, int)
	 */
	@Override
	public void setMaxLength(String p0, int p1) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAllByColumn(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IDOLegacyEntity[] findAllByColumn(String p0, String p1, String p2,
			String p3) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAllByColumnEquals(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IDOLegacyEntity[] findAllByColumnEquals(String p0, String p1,
			String p2, String p3) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAllByColumn(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IDOLegacyEntity[] findAllByColumn(String p0, String p1, String p2,
			String p3, String p4, String p5) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAllByColumnEquals(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IDOLegacyEntity[] findAllByColumnEquals(String p0, String p1,
			String p2, String p3, String p4, String p5) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#reverseRemoveFrom(com.idega.data.IDOLegacyEntity)
	 */
	@Override
	public void reverseRemoveFrom(IDOLegacyEntity p0) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#freeConnection(java.sql.Connection)
	 */
	@Override
	public void freeConnection(Connection p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getIDInteger()
	 */
	@Override
	public Integer getIDInteger() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#empty()
	 */
	@Override
	public void empty() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAllByColumnOrdered(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IDOLegacyEntity[] findAllByColumnOrdered(String p0, String p1,
			String p2, String p3, String p4) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAllByColumnEqualsOrdered(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IDOLegacyEntity[] findAllByColumnEqualsOrdered(String p0, String p1,
			String p2, String p3, String p4) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAllByColumnEqualsOrdered(java.lang.String, int, java.lang.String, int, java.lang.String)
	 */
	@Override
	public IDOLegacyEntity[] findAllByColumnEqualsOrdered(String p0, int p1,
			String p2, int p3, String p4) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#setDatasource(java.lang.String)
	 */
	@Override
	public void setDatasource(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getNumberOfRecords(java.lang.String, java.lang.String)
	 */
	@Override
	public int getNumberOfRecords(String p0, String p1) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getMaxColumnValue(java.lang.String)
	 */
	@Override
	public int getMaxColumnValue(String p0) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getFloatColumnValue(java.lang.String)
	 */
	@Override
	public float getFloatColumnValue(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#setStorageClassType(java.lang.String, int)
	 */
	@Override
	public void setStorageClassType(String p0, int p1) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findReverseRelated(com.idega.data.IDOLegacyEntity)
	 */
	@Override
	public IDOLegacyEntity[] findReverseRelated(IDOLegacyEntity p0)
			throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAll(java.lang.String)
	 */
	@Override
	public IDOLegacyEntity[] findAll(String p0) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getStorageClassName(java.lang.String)
	 */
	@Override
	public String getStorageClassName(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findRelated(com.idega.data.IDOLegacyEntity)
	 */
	@Override
	public IDOLegacyEntity[] findRelated(IDOLegacyEntity p0)
			throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#setColumn(java.lang.String, java.lang.Float)
	 */
	@Override
	public void setColumn(String p0, Float p1) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#setColumn(java.lang.String, java.lang.Integer)
	 */
	@Override
	public void setColumn(String p0, Integer p1) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getColumnValue(java.lang.String)
	 */
	@Override
	public Object getColumnValue(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getColumnNames()
	 */
	@Override
	public String[] getColumnNames() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#setUnique(java.lang.String, boolean)
	 */
	@Override
	public void setUnique(String p0, boolean p1) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getIntColumnValue(java.lang.String)
	 */
	@Override
	public int getIntColumnValue(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#delete()
	 */
	@Override
	public void delete() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#constructArray(java.lang.String[])
	 */
	@Override
	public IDOLegacyEntity[] constructArray(String[] p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#isInSynchWithDatastore()
	 */
	@Override
	public boolean isInSynchWithDatastore() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getMaxLength(java.lang.String)
	 */
	@Override
	public int getMaxLength(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#setStorageClassName(java.lang.String, java.lang.String)
	 */
	@Override
	public void setStorageClassName(String p0, String p1) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAssociated(com.idega.data.IDOLegacyEntity)
	 */
	@Override
	public IDOLegacyEntity[] findAssociated(IDOLegacyEntity p0)
			throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getNumberOfRecords(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public int getNumberOfRecords(String p0, String p1, String p2)
			throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getNumberOfRecords()
	 */
	@Override
	public int getNumberOfRecords() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAll()
	 */
	@Override
	public IDOLegacyEntity[] findAll() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getID()
	 */
	@Override
	public int getID() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#setColumn(java.lang.String, int)
	 */
	@Override
	public void setColumn(String p0, int p1) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#hasMetaDataRelationship()
	 */
	@Override
	public boolean hasMetaDataRelationship() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAllDescendingOrdered(java.lang.String)
	 */
	@Override
	public IDOLegacyEntity[] findAllDescendingOrdered(String p0)
			throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getDoubleColumnValue(java.lang.String)
	 */
	@Override
	public double getDoubleColumnValue(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#setColumn(java.lang.String, float)
	 */
	@Override
	public void setColumn(String p0, float p1) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getVisibleColumnNames()
	 */
	@Override
	public String[] getVisibleColumnNames() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#addTo(java.lang.Class, int)
	 */
	@Override
	public void addTo(Class p0, int p1) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#removeFromColumn(java.lang.String)
	 */
	@Override
	public void removeFromColumn(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getRelationShipType(java.lang.String)
	 */
	@Override
	public String getRelationShipType(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#setVisible(java.lang.String, boolean)
	 */
	@Override
	public void setVisible(String p0, boolean p1) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getEntityName()
	 */
	@Override
	public String getEntityName() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getEmptyBlob(java.lang.String)
	 */
	@Override
	public BlobWrapper getEmptyBlob(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAllByColumn(java.lang.String, int)
	 */
	@Override
	public IDOLegacyEntity[] findAllByColumn(String p0, int p1)
			throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAllByColumnEquals(java.lang.String, int)
	 */
	@Override
	public IDOLegacyEntity[] findAllByColumnEquals(String p0, int p1)
			throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAllByColumnDescendingOrdered(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IDOLegacyEntity[] findAllByColumnDescendingOrdered(String p0,
			String p1, String p2, String p3, String p4) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getIfEditable(java.lang.String)
	 */
	@Override
	public boolean getIfEditable(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getIfUnique(java.lang.String)
	 */
	@Override
	public boolean getIfUnique(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getMetaDataDeleteVector()
	 */
	@Override
	public Vector getMetaDataDeleteVector() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getNumberOfRecords(java.lang.String)
	 */
	@Override
	public int getNumberOfRecords(String p0) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getLongName(java.lang.String)
	 */
	@Override
	public String getLongName(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#setID(int)
	 */
	@Override
	public void setID(int p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#setLongName(java.lang.String, java.lang.String)
	 */
	@Override
	public void setLongName(String p0, String p1) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getColumnOutputStream(java.lang.String)
	 */
	@Override
	public OutputStream getColumnOutputStream(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getConnection(java.lang.String)
	 */
	@Override
	public Connection getConnection(String p0) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getLobColumnName()
	 */
	@Override
	public String getLobColumnName() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getDatasource()
	 */
	@Override
	public String getDatasource() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getIntegerColumnValue(java.lang.String)
	 */
	@Override
	public Integer getIntegerColumnValue(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#removeFrom(com.idega.data.IDOLegacyEntity[])
	 */
	@Override
	public void removeFrom(IDOLegacyEntity[] p0) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getIntTableValue(java.lang.String)
	 */
	@Override
	public int getIntTableValue(String p0) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#freeConnection(java.lang.String, java.sql.Connection)
	 */
	@Override
	public void freeConnection(String p0, Connection p1) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#setRelationShipType(java.lang.String, java.lang.String)
	 */
	@Override
	public void setRelationShipType(String p0, String p1) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAllByColumn(java.lang.String, java.lang.String)
	 */
	@Override
	public IDOLegacyEntity[] findAllByColumn(String p0, String p1)
			throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAllByColumnEquals(java.lang.String, java.lang.String)
	 */
	@Override
	public IDOLegacyEntity[] findAllByColumnEquals(String p0, String p1)
			throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findByPrimaryKey(int)
	 */
	@Override
	public void findByPrimaryKey(int p0) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#setColumn(java.lang.String, boolean)
	 */
	@Override
	public void setColumn(String p0, boolean p1) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#clear()
	 */
	@Override
	public void clear() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#metaDataHasChanged()
	 */
	@Override
	public boolean metaDataHasChanged() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#addTo(com.idega.data.IDOLegacyEntity, java.lang.String)
	 */
	@Override
	public void addTo(IDOLegacyEntity p0, String p1) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#isNull(java.lang.String)
	 */
	@Override
	public boolean isNull(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAllByColumnDescendingOrdered(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IDOLegacyEntity[] findAllByColumnDescendingOrdered(String p0,
			String p1, String p2) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getBooleanColumnValue(java.lang.String)
	 */
	@Override
	public boolean getBooleanColumnValue(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getMaxColumnValue(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public int getMaxColumnValue(String p0, String p1, String p2)
			throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#addTo(com.idega.data.IDOLegacyEntity, java.sql.Connection)
	 */
	@Override
	public void addTo(IDOLegacyEntity p0, Connection p1) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getNumberOfRecordsRelated(com.idega.data.IDOLegacyEntity)
	 */
	@Override
	public int getNumberOfRecordsRelated(IDOLegacyEntity p0)
			throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#setNullable(java.lang.String, boolean)
	 */
	@Override
	public void setNullable(String p0, boolean p1) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#deleteMetaData()
	 */
	@Override
	public void deleteMetaData() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getColumn(java.lang.String)
	 */
	@Override
	public EntityAttribute getColumn(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#columnsHaveChanged()
	 */
	@Override
	public boolean columnsHaveChanged() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#setToInsertStartData(boolean)
	 */
	@Override
	public void setToInsertStartData(boolean ifTrue) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getIfInsertStartData()
	 */
	@Override
	public boolean getIfInsertStartData() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getTableName()
	 */
	@Override
	public String getTableName() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#setEntityState(int)
	 */
	@Override
	public void setEntityState(int state) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#hasBeenSetNull(java.lang.String)
	 */
	@Override
	public boolean hasBeenSetNull(String columnName) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getPrimaryKeyValue()
	 */
	@Override
	public Object getPrimaryKeyValue() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#hasLobColumn()
	 */
	@Override
	public boolean hasLobColumn() throws Exception {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#fillColumn(java.lang.String, java.sql.ResultSet)
	 */
	@Override
	public void fillColumn(String columnName, ResultSet rs) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#delete(java.sql.Connection)
	 */
	@Override
	public void delete(Connection c) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#insert(java.sql.Connection)
	 */
	@Override
	public void insert(Connection c) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#update(java.sql.Connection)
	 */
	@Override
	public void update(Connection c) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#removeFrom(com.idega.data.IDOLegacyEntity, java.sql.Connection)
	 */
	@Override
	public void removeFrom(IDOLegacyEntity entityToRemoveFrom, Connection conn)
			throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#removeFrom(java.lang.Class)
	 */
	@Override
	public void removeFrom(Class classToRemoveFrom) throws SQLException {
		throw new NotImplementedException();

	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getEntityState()
	 */
	@Override
	public int getEntityState() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAllByColumnOrdered(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IDOLegacyEntity[] findAllByColumnOrdered(String columnName1,
			String toFind1, String columnName2, String toFind2,
			String orderByColumnName, String condition1, String condition2)
			throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAllByColumn(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IDOLegacyEntity[] findAllByColumn(String columnName, String toFind,
			String condition) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAllByColumn(java.lang.String, java.lang.String, char, java.lang.String, java.lang.String, char)
	 */
	@Override
	public IDOLegacyEntity[] findAllByColumn(String columnName1,
			String toFind1, char condition1, String columnName2,
			String toFind2, char condition2) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findRelatedIDs(com.idega.data.IDOLegacyEntity, java.lang.String, java.lang.String)
	 */
	@Override
	public int[] findRelatedIDs(IDOLegacyEntity entity,
			String entityColumnName, String entityColumnValue)
			throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findRelatedIDs(com.idega.data.IDOLegacyEntity)
	 */
	@Override
	public int[] findRelatedIDs(IDOLegacyEntity entity) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#removeFrom(java.lang.Class, int)
	 */
	@Override
	public void removeFrom(Class classToRemoveFrom, int idToRemoveFrom)
			throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findAllByColumnOrdered(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IDOLegacyEntity[] findAllByColumnOrdered(String columnName1,
			String toFind1, String columnName2, String toFind2)
			throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#findRelated(com.idega.data.IDOLegacyEntity, java.lang.String, java.lang.String)
	 */
	@Override
	public IDOLegacyEntity[] findRelated(IDOLegacyEntity entity,
			String columnName, String columnValue) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOLegacyEntity#getNumberOfRecords(java.lang.String, int)
	 */
	@Override
	public int getNumberOfRecords(String columnName, int id)
			throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOEntity#store()
	 */
	@Override
	public void store() throws IDOStoreException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOEntity#getEntityDefinition()
	 */
	@Override
	public IDOEntityDefinition getEntityDefinition() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOEntity#decode(java.lang.String)
	 */
	@Override
	public Integer decode(String pkString) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOEntity#decode(java.lang.String[])
	 */
	@Override
	public Collection<Integer> decode(String[] pkString) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see javax.ejb.EJBLocalObject#getEJBLocalHome()
	 */
	@Override
	public EJBLocalHome getEJBLocalHome() throws EJBException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see javax.ejb.EJBLocalObject#getPrimaryKey()
	 */
	@Override
	public Object getPrimaryKey() throws EJBException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see javax.ejb.EJBLocalObject#isIdentical(javax.ejb.EJBLocalObject)
	 */
	@Override
	public boolean isIdentical(EJBLocalObject arg0) throws EJBException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see javax.ejb.EJBLocalObject#remove()
	 */
	@Override
	public void remove() throws RemoveException, EJBException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(IDOEntity o) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.MetaDataCapable#setMetaDataAttributes(java.util.Map)
	 */
	@Override
	public void setMetaDataAttributes(Map<String, String> map) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.MetaDataCapable#getMetaDataAttributes()
	 */
	@Override
	public Map<String, String> getMetaDataAttributes() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.MetaDataCapable#getMetaDataTypes()
	 */
	@Override
	public Map<String, String> getMetaDataTypes() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.MetaDataCapable#setMetaData(java.lang.String, java.lang.String)
	 */
	@Override
	public void setMetaData(String metaDataKey, String value) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.MetaDataCapable#setMetaData(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void setMetaData(String metaDataKey, String value, String type) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.MetaDataCapable#getMetaData(java.lang.String)
	 */
	@Override
	public String getMetaData(String metaDataKey) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.MetaDataCapable#renameMetaData(java.lang.String, java.lang.String)
	 */
	@Override
	public void renameMetaData(String oldKeyName, String newKeyName) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.MetaDataCapable#renameMetaData(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void renameMetaData(String oldKeyName, String newKeyName,
			String value) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#setDateOfBirth(java.sql.Date)
	 */
	@Override
	public void setDateOfBirth(Date p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#setSystemImageID(int)
	 */
	@Override
	public void setSystemImageID(int p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#setGroupID(int)
	 */
	@Override
	public void setGroupID(int p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#setFirstName(java.lang.String)
	 */
	@Override
	public void setFirstName(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#getIDColumnName()
	 */
	@Override
	public String getIDColumnName() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#getGenderID()
	 */
	@Override
	public int getGenderID() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#getDateOfBirth()
	 */
	@Override
	public Date getDateOfBirth() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#getMiddleName()
	 */
	@Override
	public String getMiddleName() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#getLastName()
	 */
	@Override
	public String getLastName() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#setPrimaryGroupID(java.lang.Integer)
	 */
	@Override
	public void setPrimaryGroupID(Integer p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#setSystemImageID(java.lang.Integer)
	 */
	@Override
	public void setSystemImageID(Integer p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#getSystemImageID()
	 */
	@Override
	public int getSystemImageID() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#setLastName(java.lang.String)
	 */
	@Override
	public void setLastName(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#setGender(int)
	 */
	@Override
	public void setGender(int p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#getGroupID()
	 */
	@Override
	public int getGroupID() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#setDefaultValues()
	 */
	@Override
	public void setDefaultValues() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#getDescription()
	 */
	@Override
	public String getDescription() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#setMiddleName(java.lang.String)
	 */
	@Override
	public void setMiddleName(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#getName()
	 */
	@Override
	public String getName() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#getFirstName()
	 */
	@Override
	public String getFirstName() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#setDisplayName(java.lang.String)
	 */
	@Override
	public void setDisplayName(String p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#setGender(java.lang.Integer)
	 */
	@Override
	public void setGender(Integer p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#setPrimaryGroupID(int)
	 */
	@Override
	public void setPrimaryGroupID(int p0) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#getPrimaryGroupID()
	 */
	@Override
	public int getPrimaryGroupID() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#getPersonalID()
	 */
	@Override
	public String getPersonalID() {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.user.data.User#setPersonalID(java.lang.String)
	 */
	@Override
	public void setPersonalID(String p0) {
		throw new NotImplementedException();
	}

}
