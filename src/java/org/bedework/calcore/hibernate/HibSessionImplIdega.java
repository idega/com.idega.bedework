package org.bedework.calcore.hibernate;

import org.apache.log4j.Logger;
import org.bedework.calfacade.exc.CalFacadeException;
import org.hibernate.SessionFactory;

/**
 * Convenience class to do the actual hibernate interaction. Intended for one
 * use only.
 * <p>
 * You can report about problems to: <a
 * href="mailto:martynas@idega.com">Martynas Stakė</a>
 * </p>
 * <p>
 * You can expect to find some test cases notice in the end of the file.
 * </p>
 * 
 * @version 1.0.0 Apr 5, 2012
 * @author martynasstake
 */
public class HibSessionImplIdega extends HibSessionImpl {

	private static final long serialVersionUID = -8231831912796123388L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bedework.calcore.hibernate.HibSessionImpl#init(
	 * org.hibernate.SessionFactory, org.apache.log4j.Logger )
	 */
	@Override
	public void init(final SessionFactory sessFactory, final Logger log)
			throws CalFacadeException {
		try {
			this.log = log;
			sess = com.idega.hibernate.HibernateUtil.getSessionFactory()
					.getCurrentSession();
			rolledBack = false;
		} catch (Throwable t) {
			exc = t;
			tx = null; // not even started. Should be null anyway
			close();
		}
	}
}
