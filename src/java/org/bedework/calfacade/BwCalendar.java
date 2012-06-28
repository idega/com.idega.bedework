/* ********************************************************************
    Licensed to Jasig under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Jasig licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License. You may obtain a
    copy of the License at:

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on
    an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied. See the License for the
    specific language governing permissions and limitations
    under the License.
*/
package org.bedework.calfacade;

import java.net.URLEncoder;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import org.bedework.calfacade.annotations.Dump;
import org.bedework.calfacade.annotations.NoDump;
import org.bedework.calfacade.annotations.NoWrap;
import org.bedework.calfacade.annotations.Wrapper;
import org.bedework.calfacade.annotations.ical.NoProxy;
import org.bedework.calfacade.base.BwShareableContainedDbentity;
import org.bedework.calfacade.base.CategorisedEntity;
import org.bedework.calfacade.base.CollatableEntity;
import org.bedework.calfacade.base.PropertiesEntity;
import org.bedework.calfacade.exc.CalFacadeException;

import com.idega.block.cal.data.CalDAVCalendar;

import edu.rpi.cmt.access.Acl.CurrentAccess;
import edu.rpi.sss.util.DateTimeUtil;
import edu.rpi.sss.util.Util;
import edu.rpi.sss.util.xml.tagdefs.AppleIcalTags;
import edu.rpi.sss.util.xml.tagdefs.BedeworkServerTags;
import edu.rpi.sss.util.xml.tagdefs.CaldavTags;

/** A collection in Bedework. This is roughly equivalent to a folder with some
 * rules attached.
 *
 * <p>These objects are used to create two tree structures rooted at the public
 * calendars root and the user calendars root. The names of the roots are defined
 * at system build time.
 *
 * <p>For caldav compatability we do not allow calendar collections inside
 * calendar collections.
 *
 * <p>Collections are given a type defined below.
 *
 * <p>Entities have names which must be unique within a collection. An
 * event, either a single non-recurring event, or a master event and all it's
 * overrides, have a single effective name which would correspond to the name
 * of an ics file generated for that event.
 *
 * <p>Calendar entities also have uids. These must be unique within normal
 * calendar collections. That requirement is relaxed for other special
 * calendars.
 *
 * Collections may be tombstoned - that is they are effectively deleted but
 * remain for thew purpose of synchronization reports. Currently we indicate a
 * tombstoned collection by setting the filter value to "--TOMBSTONED--".
 *
 * XXX We suffix the name and path also to avoid some ugly clashes related to
 * lastmod
 *
 *  @author Mike Douglass douglm - rpi.edu
 *  @version 1.0
 */
@Wrapper(quotas = true)
@Dump(elementName="collection", keyFields={"path"})
public class BwCalendar extends BwShareableContainedDbentity<BwCalendar>
        implements CollatableEntity, CategorisedEntity, PropertiesEntity, CalDAVCalendar {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1561860183249031525L;

/** The internal name of the calendar
   */
  private String name;

  /** Path to this calendar - including this one.
   * The names concatenated with intervening '/'
   */
  private String path;

  /** The display name for the collection
   */
  private String summary;

  /** Some sort of description - may be null
   */
  private String description;

  /** This identifies an associated mailing list. Its actual value is set
   * by the mailer interface.
   */
  private String mailListId;

  /* The type of calendar */
  private int calType;

  /** Indicate unknown type */
  public final static int calTypeUnknown = -1;

  /** <em>Normal folder</em> Holds other collections */
  public final static int calTypeFolder = 0;

  /** <em>Normal calendar collection</em> holds events, todos etc */
  public final static int calTypeCalendarCollection = 1;

  /** <em>Trash</em> - don't use  */
  public final static int calTypeTrash = 2;

  /** <em>Deleted</em> Holds annotations which effectively delete events to
   * which the user does not have write access
   */
  public final static int calTypeDeleted = 3;

  /** <em>Busy</em> Used to store busy time - acts as a mask for freebusy */
  public final static int calTypeBusy = 4;

  /** <em>Inbox</em> Mostly used for notifcation of meeting requests */
  public final static int calTypeInbox = 5;

  /** <em>Outbox</em> Target for scheduling. Normally empty */
  public final static int calTypeOutbox = 6;

  /** <em>Alias</em>  */
  public final static int calTypeAlias = 7;

  /** <em>External subscription</em>  */
  public final static int calTypeExtSub = 8;

  /** <em>Resource collection</em> According to the CalDAV spec a collection may exist
   * inside a calendar collection but no calendar collection must be so
   * contained at any depth. (RFC 4791 Section 4.2) */
  public final static int calTypeResourceCollection = 9;

  /** There are limitations on what may be placed in each type of collection,
   *  e.g folders cannot hold entities, guids must be unique in calendars etc.
   *
   *  <p>This class allows us to create a list of characteristics for each
   *  calendar type.
   */
  public static class CollectionInfo {
    /** Allows us to use this as a parameter */
    public int collectionType;

    /** Is this 'special', e.g. Trash */
    public boolean special;

    /** Children allowed in this collection */
    public boolean childrenAllowed;

    /** Store entities in this type */
    public boolean entitiesAllowed;

    /** Guid + recurrence must be unique */
    public boolean uniqueKey;

    /** Allow annotations */
    public boolean allowAnnotations;

    /** Freebusy allowed */
    public boolean allowFreeBusy;

    /** Can be the target of an alias */
    public boolean canAlias;

    /**
     * @param collectionType
     * @param special
     * @param childrenAllowed
     * @param entitiesAllowed
     * @param uniqueKey
     * @param allowAnnotations
     * @param allowFreeBusy
     * @param canAlias
     */
    public CollectionInfo(final int collectionType,
                          final boolean special,
                          final boolean childrenAllowed,
                          final boolean entitiesAllowed,
                          final boolean uniqueKey,
                          final boolean allowAnnotations,
                          final boolean allowFreeBusy,
                          final boolean canAlias) {
      this.collectionType = collectionType;
      this.special = special;
      this.childrenAllowed = childrenAllowed;
      this.entitiesAllowed = entitiesAllowed;
      this.uniqueKey = uniqueKey;
      this.allowAnnotations = allowAnnotations;
      this.allowFreeBusy = allowFreeBusy;
      this.canAlias = canAlias;
    }
  }

  private static final boolean f = false;
  private static final boolean T = true;

  /** The info */
  private static final CollectionInfo[] collectionInfo = {
    new CollectionInfo(calTypeFolder,               f, T, f, f, f, T, T),
    new CollectionInfo(calTypeCalendarCollection,   f, T, T, T, T, T, T),
    new CollectionInfo(calTypeTrash,                T, f, T, f, T, f, f),
    new CollectionInfo(calTypeDeleted,              T, f, T, f, T, f, f),
    new CollectionInfo(calTypeBusy,                 T, f, T, T, T, T, f),
    new CollectionInfo(calTypeInbox,                T, f, T, f, f, f, f),
    new CollectionInfo(calTypeOutbox,               T, f, T, f, f, f, f),
    new CollectionInfo(calTypeAlias,                f, f, f, f, f, T, T),
    new CollectionInfo(calTypeExtSub,               f, T, T, T, f, T, T),
    new CollectionInfo(calTypeResourceCollection,   f, T, f, f, f, f, f),
  };

  /** UTC datetime */
  private String created;

  private BwCollectionLastmod lastmod;

  private String filterExpr;

  /** Value of filter for a tombstoned collection
   */
  public static final String tombstonedFilter = "--TOMBSTONED--";

  /** Value of suffix on path for a tombstoned collection
   */
  public static final String tombstonedSuffix = "(--TOMBSTONED--)";

  private Set<BwCategory> categories = null;

  private Set<BwProperty> properties;

  private String aliasUri;

  private boolean display = true;

  private boolean affectsFreeBusy;

  private boolean ignoreTransparency;

  private boolean unremoveable;

  private int refreshRate;

  private String lastRefresh;

  private String lastRefreshStatus;

  private String lastEtag;

  private String remoteId;

  private String remotePw;

  /* Non - db field */
  private BwCalendar aliasTarget;

  /* Non - db field */
  private boolean pwNeedsEncrypt;

  /** Constructor
   */
  public BwCalendar() {
    super();

    /* Set the lastmod and created */

    Date dt = new Date();
    setLastmod(new BwCollectionLastmod(this, dt));
    setCreated(DateTimeUtil.isoDateTimeUTC(dt));
  }

  /** Set the name
   *
   * @param val    String name
   */
  public void setName(final String val) {
    name = val;
  }

  /** Get the name
   *
   * @return String   name
   */
  public String getName() {
    return name;
  }

  /** Set the path
   *
   * @param val    String path
   */
  public void setPath(final String val) {
    path = val;
    if (getLastmod() != null) {
      getLastmod().setPath(val);
    }
  }

  /** Get the path
   *
   * @return String   path
   */
  public String getPath() {
    return path;
  }

  /** Set the summary
   *
   * @param val    String summary
   */
  public void setSummary(final String val) {
    summary = val;
  }

  /** Get the summary
   *
   * @return String   summary
   */
  public String getSummary() {
    if (summary == null) {
      return getName();
    }
    return summary;
  }

  /** Set the description
   *
   * @param val    description
   */
  public void setDescription(final String val) {
    description = val;
  }

  /** Get the description
   *
   *  @return String   description
   */
  public String getDescription() {
    return description;
  }

  /** Set the mail list id
   *
   * @param val    String mail list id
   */
  public void setMailListId(final String val) {
    mailListId = val;
  }

  /** Get the mail list id
   *
   *  @return String   mail list id
   */
  public String getMailListId() {
    return mailListId;
  }

  /** Set the type
   *
   * @param val    type
   */
  public void setCalType(final int val) {
    calType = val;
  }

  /** Get the type
   *
   *  @return int type
   */
  public int getCalType() {
    return calType;
  }

  /**
   * @param val
   */
  public void setCreated(final String val) {
    created = val;
  }

  /**
   * @return String created
   */
  public String getCreated() {
    return created;
  }

  /**
   * @param val
   */
  public void setLastmod(final BwCollectionLastmod val) {
    lastmod = val;
  }

  /**
   * @return BwCollectionLastmod lastmod
   */
  @Dump(elementName = "col-lastmod", compound=true)
  public BwCollectionLastmod getLastmod() {
    return lastmod;
  }

  /**
   * @param val
   */
  public void setFilterExpr(final String val) {
    filterExpr = val;
  }

  /**
   * @return String FilterExpr
   */
  public String getFilterExpr() {
    return filterExpr;
  }

  /** Set the refresh rate in seconds
   *
   * @param val    type
   */
  public void setRefreshRate(final int val) {
    refreshRate = val;
  }

  /** Get the refresh rate in seconds
   *
   *  @return String   description
   */
  public int getRefreshRate() {
    return refreshRate;
  }

  /**
   * @param val
   */
  public void setLastRefresh(final String val) {
    lastRefresh = val;
  }

  /**
   *
   * @return String lastRefresh
   */
  public String getLastRefresh() {
    return lastRefresh;
  }

  /** HTTP status or other appropriate value
   * @param val
   */
  public void setLastRefreshStatus(final String val) {
    lastRefreshStatus = val;
  }

  /**
   * @return String lastRefreshStatus
   */
  public String getLastRefreshStatus() {
    return lastRefreshStatus;
  }

  /**
   * @param val
   */
  public void setLastEtag(final String val) {
    lastEtag = val;
  }

  /**
   * @return String lastRefresh
   */
  public String getLastEtag() {
    return lastEtag;
  }

  /** If non-null we have a remote id and encrypted password
   *
   * @param val
   */
  public void setRemoteId(final String val) {
    remoteId = val;
  }

  /**
   * @return String remoteId
   */
  public String getRemoteId() {
    return remoteId;
  }

  /** If non-null the encrypted password
   *
   * @param val
   */
  public void setRemotePw(final String val) {
    remotePw = val;
  }

  /**
   * @return String encrypted password
   */
  public String getRemotePw() {
    return remotePw;
  }

  /* ====================================================================
   *               CategorisedEntity interface methods
   * ==================================================================== */

  /* (non-Javadoc)
   * @see org.bedework.calfacade.base.CategorisedEntity#setCategories(java.util.Set)
   */
  public void setCategories(final Set<BwCategory> val) {
    categories = val;
  }

  /* (non-Javadoc)
   * @see org.bedework.calfacade.base.CategorisedEntity#getCategories()
   */
  public Set<BwCategory> getCategories() {
    return categories;
  }

  /* (non-Javadoc)
   * @see org.bedework.calfacade.base.CategorisedEntity#getNumCategories()
   */
  @NoDump
  public int getNumCategories() {
    Set<BwCategory> c = getCategories();
    if (c == null) {
      return 0;
    }

    return c.size();
  }

  /* (non-Javadoc)
   * @see org.bedework.calfacade.base.CategorisedEntity#addCategory(org.bedework.calfacade.BwCategory)
   */
  public void addCategory(final BwCategory val) {
    Set<BwCategory> cats = getCategories();
    if (cats == null) {
      cats = new TreeSet<BwCategory>();
      setCategories(cats);
    }

    if (!cats.contains(val)) {
      cats.add(val);
    }
  }

  /* (non-Javadoc)
   * @see org.bedework.calfacade.base.CategorisedEntity#removeCategory(org.bedework.calfacade.BwCategory)
   */
  public boolean removeCategory(final BwCategory val) {
    Set<BwCategory> cats = getCategories();
    if (cats == null) {
      return false;
    }

    return cats.remove(val);
  }

  /* (non-Javadoc)
   * @see org.bedework.calfacade.base.CategorisedEntity#hasCategory(org.bedework.calfacade.BwCategory)
   */
  public boolean hasCategory(final BwCategory val) {
    Set<BwCategory> cats = getCategories();
    if (cats == null) {
      return false;
    }

    return cats.contains(val);
  }

  /* (non-Javadoc)
   * @see org.bedework.calfacade.base.CategorisedEntity#copyCategories()
   */
  public Set<BwCategory> copyCategories() {
    if (getNumCategories() == 0) {
      return null;
    }
    TreeSet<BwCategory> ts = new TreeSet<BwCategory>();

    for (BwCategory cat: getCategories()) {
      ts.add(cat);
    }

    return ts;
  }

  /* (non-Javadoc)
   * @see org.bedework.calfacade.base.CategorisedEntity#cloneCategories()
   */
  public Set<BwCategory> cloneCategories() {
    if (getNumCategories() == 0) {
      return null;
    }
    TreeSet<BwCategory> ts = new TreeSet<BwCategory>();

    for (BwCategory cat: getCategories()) {
      ts.add((BwCategory)cat.clone());
    }

    return ts;
  }

  /** Set the alias uri
   *
   * @param val    String uri
   */
  @NoWrap
  public void setAliasUri(final String val) {
    aliasUri = val;
  }

  /** Get the alias uri
   *
   * @return String   uri
   */
  public String getAliasUri() {
    return aliasUri;
  }

  /**
   *
   * @param val   boolean true if the password needs encrypting
   */
  public void setPwNeedsEncrypt(final boolean val) {
    pwNeedsEncrypt = val;
  }

  /**
   *
   * @return boolean  true if the password needs encrypting
   */
  @NoDump
  public boolean getPwNeedsEncrypt() {
    return pwNeedsEncrypt;
  }

  /**
   *
   * @param val   boolean true if the calendar is to be displayed
   */
  public void setDisplay(final boolean val) {
    display = val;
  }

  /**
   *
   * @return boolean  true if the calendar is to be displayed
   */
  public boolean getDisplay() {
    return display;
  }

  /**
   *
   *  @param val    true if the calendar takes part in free/busy calculations
   */
  public void setAffectsFreeBusy(final boolean val) {
    affectsFreeBusy = val;
  }

  /**
   *
   *  @return boolean    true if the calendar takes part in free/busy calculations
   */
  public boolean getAffectsFreeBusy() {
    return affectsFreeBusy;
  }

  /** Set the ignoreTransparency flag
   *
   *  @param val    true if we ignore tranparency in free/busy calculations
   */
  public void setIgnoreTransparency(final boolean val) {
    ignoreTransparency = val;
  }

  /** Do we ignore transparency?
   *
   *  @return boolean    true for ignoreTransparency
   */
  public boolean getIgnoreTransparency() {
    return ignoreTransparency;
  }

  /**
   *
   * @param val   boolean true if the calendar is unremoveable
   */
  public void setUnremoveable(final boolean val) {
    unremoveable = val;
  }

  /**
   *
   * @return boolean  true if the calendar is unremoveable
   */
  public boolean getUnremoveable() {
    return unremoveable;
  }

  /* ====================================================================
   *                   Property methods
   * ==================================================================== */

  /* (non-Javadoc)
   * @see org.bedework.calfacade.base.PropertiesEntity#setProperties(java.util.Set)
   */
  public void setProperties(final Set<BwProperty> val) {
    properties = val;
  }

  /* (non-Javadoc)
   * @see org.bedework.calfacade.base.PropertiesEntity#getProperties()
   */
  @Dump(collectionElementName = "property", compound = true)
  public Set<BwProperty> getProperties() {
    return properties;
  }

  /* (non-Javadoc)
   * @see org.bedework.calfacade.base.PropertiesEntity#getProperties(java.lang.String)
   */
  public Set<BwProperty> getProperties(final String name) {
    TreeSet<BwProperty> ps = new TreeSet<BwProperty>();

    if (getNumProperties() == 0) {
      return null;
    }

    for (BwProperty p: getProperties()) {
      if (p.getName().equals(name)) {
        ps.add(p);
      }
    }

    return ps;
  }

  /* (non-Javadoc)
   * @see org.bedework.calfacade.base.PropertiesEntity#removeProperties(java.lang.String)
   */
  public void removeProperties(final String name) {
    Set<BwProperty> ps = getProperties(name);

    if (ps == null) {
      return;
    }

    for (BwProperty p: ps) {
      removeProperty(p);
    }
  }

  /* (non-Javadoc)
   * @see org.bedework.calfacade.base.PropertiesEntity#getNumProperties()
   */
  @NoDump
  public int getNumProperties() {
    Collection<BwProperty> c = getProperties();
    if (c == null) {
      return 0;
    }

    return c.size();
  }

  /* (non-Javadoc)
   * @see org.bedework.calfacade.base.PropertiesEntity#findProperty(java.lang.String)
   */
  public BwProperty findProperty(final String name) {
    Collection<BwProperty> props = getProperties();

    if (props == null) {
      return null;
    }

    for (BwProperty prop: props) {
      if (name.equals(prop.getName())) {
        return prop;
      }
    }

    return null;
  }

  /* (non-Javadoc)
   * @see org.bedework.calfacade.base.PropertiesEntity#addProperty(org.bedework.calfacade.BwProperty)
   */
  public void addProperty(final BwProperty val) {
    Set<BwProperty> c = getProperties();
    if (c == null) {
      c = new TreeSet<BwProperty>();
      setProperties(c);
    }

    if (!c.contains(val)) {
      c.add(val);
    }
  }

  /* (non-Javadoc)
   * @see org.bedework.calfacade.base.PropertiesEntity#removeProperty(org.bedework.calfacade.BwProperty)
   */
  public boolean removeProperty(final BwProperty val) {
    Set<BwProperty> c = getProperties();
    if (c == null) {
      return false;
    }

    return c.remove(val);
  }

  /* (non-Javadoc)
   * @see org.bedework.calfacade.base.PropertiesEntity#copyProperties()
   */
  public Set<BwProperty> copyProperties() {
    if (getNumProperties() == 0) {
      return null;
    }
    TreeSet<BwProperty> ts = new TreeSet<BwProperty>();

    for (BwProperty p: getProperties()) {
      ts.add(p);
    }

    return ts;
  }

  /* (non-Javadoc)
   * @see org.bedework.calfacade.base.PropertiesEntity#cloneProperties()
   */
  public Set<BwProperty> cloneProperties() {
    if (getNumProperties() == 0) {
      return null;
    }
    TreeSet<BwProperty> ts = new TreeSet<BwProperty>();

    for (BwProperty p: getProperties()) {
      ts.add((BwProperty)p.clone());
    }

    return ts;
  }

  /* ====================================================================
   *                   Property convenience methods
   * ==================================================================== */

  /** Set the calendar color property
   *
   * @param val
   */
  public void setColor(final String val) {
    if (Util.checkNull(val) == null) {
      BwProperty p = findProperty(AppleIcalTags.calendarColor.getLocalPart());
      if (p != null) {
        removeProperty(p);
      }
    } else {
      setProperty(AppleIcalTags.calendarColor.getLocalPart(), val);
    }
  }

  /** Get the calendar color property
   *
   * @return String calendar color
   */
  @NoDump
  public String getColor() {
    return getProperty(AppleIcalTags.calendarColor.getLocalPart());
  }

  static final String subscriptionIdProperty = "org.bedework.subscriptionId";

  /** Set the subscriptionId property
   *
   * @param val
   */
  public void setSubscriptionId(final String val) {
    if (Util.checkNull(val) == null) {
      BwProperty p = findProperty(subscriptionIdProperty);
      if (p != null) {
        removeProperty(p);
      }
    } else {
      setProperty(subscriptionIdProperty, val);
    }
  }

  /** Get the subscriptionId property
   *
   * @return String subscriptionId
   */
  @NoDump
  public String getSubscriptionId() {
    return getProperty(subscriptionIdProperty);
  }

  /** Set the calendar timezone property
   *
   * @param val
   */
  public void setTimezone(final String val) {
    if (val == null) {
      BwProperty p = findProperty(CaldavTags.calendarTimezone.getLocalPart());
      if (p != null) {
        removeProperty(p);
      }
    } else {
      setProperty(CaldavTags.calendarTimezone.getLocalPart(), val);
    }
  }

  /** Get the calendar timezone property
   *
   * @return String vtimezone spec
   */
  @NoDump
  public String getTimezone() {
    return getProperty(CaldavTags.calendarTimezone.getLocalPart());
  }

  /** Set the topical area property
   *
   * @param val
   */
  public void setIsTopicalArea(final boolean val) {
    setProperty(BedeworkServerTags.isTopicalArea.getLocalPart(),
                String.valueOf(val));
  }

  /** Get the topical area property
   *
   * @return boolean on/off
   */
  @NoDump
  public boolean getIsTopicalArea() {
    return Boolean.valueOf(getProperty(
         BedeworkServerTags.isTopicalArea.getLocalPart()));
  }

  /* ====================================================================
   *                   Non-db methods
   * ==================================================================== */

  /** true if this is to 'hold' calendar objects
   *
   * @return boolean  true if this is to 'hold' calendar objects
   */
  @NoDump
  public boolean getCalendarCollection() {
    return collectionInfo[getCalType()].entitiesAllowed;
  }

  /** true if this is a special collection
   *
   * @return boolean
   */
  @NoDump
  public boolean getSpecial() {
    return collectionInfo[getCalType()].special;
  }

  /** true if this can be the target of an alias
   *
   * @return boolean
   */
  @NoDump
  public boolean getCanAlias() {
    return collectionInfo[getCalType()].canAlias;
  }

  /** Set the aliased entity
   *
   * @param val    BwCalendar object's alias target
   */
  public void setAliasTarget(final BwCalendar val) {
    aliasTarget = val;
  }

  /** Get the aliased entity
   *
   * @return BwCalendar   the object's alias target
   */
  @NoDump
  public BwCalendar getAliasTarget() {
    return aliasTarget;
  }

  /**
   * @param val   boolean true if the target is unreachable
   * @throws CalFacadeException
   */
  public void setDisabled(final boolean val) throws CalFacadeException {
    throw new CalFacadeException("org.bedework.wrapper.method.called");
  }

  /**
   * @return boolean  true if the target is unreachable
   */
  @NoDump
  public boolean getDisabled() {
    return false;
  }

  /* ====================================================================
   *                   Convenience methods
   * ==================================================================== */

  /** return the end of the chain of aliases or this object if not an alias.
   * Assumes aliases have been resolved.
   *
   * @return boolean  true if this is some sort of alias.
   */
  @NoProxy
  @NoDump
  public BwCalendar getAliasedEntity() {
    BwCalendar coll = this;
    while ((coll != null) && coll.getInternalAlias()) {
      coll = coll.getAliasTarget();
    }

    return coll;
  }

  /** true if this is some sort of alias
   *
   * @return boolean  true if this is some sort of alias.
   */
  @NoProxy
  @NoDump
  public boolean getAlias() {
    return getInternalAlias() || getExternalSub();
  }

  /** true if this is an alias to an internal calendar.
   *
   * @return boolean  true if this is an alias to an internal calendar.
   */
  @NoProxy
  @NoDump
  @NoWrap
  public boolean getInternalAlias() {
    return getCalType() == calTypeAlias;
  }

  /** true if this is an alias to an external calendar.
   *
   * @return boolean  true if this is an alias to an external calendar.
   */
  @NoProxy
  @NoDump
  @NoWrap
  public boolean getExternalSub() {
    return getCalType() == calTypeExtSub;
  }


  /** Return path if this is an alias to an internal calendar.
   *
   * @return String path if this is an alias to an internal calendar otherwise null.
   */
  @NoProxy
  @NoDump
  public String getInternalAliasPath() {
    if (!getInternalAlias()) {
      return null;
    }

    String uri = getAliasUri();

    if (uri.startsWith(CalFacadeDefs.bwUriPrefix)) {
      return uri.substring(CalFacadeDefs.bwUriPrefix.length());
    }

    return null;
  }

  /**
   * @return CollectionInfo for this entity
   */
  @NoDump
  public CollectionInfo getCollectionInfo() {
    return getCollectionInfo(getCalType());
  }

  /**
   * @param type
   * @return CollectionInfo for an entity of the given type
   */
  @NoDump
  @NoWrap
  public CollectionInfo getCollectionInfo(final int type) {
    return collectionInfo[type];
  }

  /** Make this thing a tombstoned collection. Non-reversible
   */
  public void tombstone() {
    if (getTombstoned()) {
      return; // Already tombstoned
    }

    setFilterExpr(tombstonedFilter);

    // XXX Schema
    /* We have to change the name and the path to avoid conflicts -
     * currently the lastmod is linked via the path - better linked by id
     */

    setName(getName() + tombstonedSuffix);
    setPath(getPath() + tombstonedSuffix);
    getLastmod().setPath(getPath());
  }

  /** Is this collection tombstoned?
   *
   * @return true/false
   */
  @NoProxy
  @NoDump
  @NoWrap
  public boolean getTombstoned() {
    String f = getFilterExpr();
    return (f != null) && f.equals(tombstonedFilter);
  }

  /** Check a possible name for validity
   *
   * @param val
   * @return  boolean true for valid calendar name
   */
  public static boolean checkName(final String val) {
    if ((val == null) || (val.length() == 0)) {
      return false;
    }

    /* First character - letter or digit  */

    if (!Character.isLetterOrDigit(val.charAt(0))) {
      return false;
    }

    for (int i = 1; i < val.length(); i++) {
      char ch = val.charAt(i);

      if (!Character.isLetterOrDigit(ch) &&
          (ch != '_') && (ch != ' ')) {
        return false;
      }
    }

    return true;
  }

  /** Generate an encoded url referring to this calendar.
   *
   * XXX This should not be here
   * @return String encoded url (or path)
   * @throws CalFacadeException
   */
  @NoDump
  public String getEncodedPath() throws CalFacadeException {
    if (getPath() == null) {
      return null;
    }
    try {
      return URLEncoder.encode(getPath(), "UTF-8");
    } catch (Throwable t) {
      throw new CalFacadeException(t);
    }
  }

  /** Update last mod fields
   */
  public void updateLastmod() {
    getLastmod().updateLastmod();
  }

  /** Set the single valued named property
   *
   * @param name
   * @param val
   */
  public void setProperty(final String name, final String val) {
    BwProperty prop = findProperty(name);

    if (prop == null) {
      prop = new BwProperty(name, val);
      addProperty(prop);
    } else {
      prop.setValue(val);
    }
  }

  /** Get the single valued named property
   *
   * @param name
   * @return String calendar color
   */
  public String getProperty(final String name) {
    BwProperty prop = findProperty(name);

    if (prop == null) {
      return null;
    }

    return prop.getValue();
  }

  /* ====================================================================
   *                   Wrapper methods
   * ==================================================================== */

  /**
   *
   * @param val CurrentAccess
   * @throws CalFacadeException
   */
  public void setCurrentAccess(final CurrentAccess val) throws CalFacadeException {
    throw new CalFacadeException("org.bedework.wrapper.method.called");
  }

  /**
   * @return CurrentAccess
   * @throws CalFacadeException
   */
  @NoDump
  public CurrentAccess getCurrentAccess() throws CalFacadeException {
    throw new CalFacadeException("org.bedework.wrapper.method.called");
  }

  /**
   * @param val ui open state
   * @throws CalFacadeException
   */
  public void setOpen(final boolean val) throws CalFacadeException {
    throw new CalFacadeException("org.bedework.wrapper.method.called");
  }

  /**
   * @return ui open state
   * @throws CalFacadeException
   */
  @NoDump
  public boolean getOpen() throws CalFacadeException {
    //throw new CalFacadeException("org.bedework.wrapper.method.called");
    // Allow this - may be called while creating?
    return false;
  }

  /* ====================================================================
   *                   CollatableEntity methods
   * ==================================================================== */

  /* (non-Javadoc)
   * @see org.bedework.calfacade.base.CollatableEntity#getCollateValue()
   */
  @NoDump
  public String getCollateValue() {
    return getName();
  }

  /* ====================================================================
   *                   Object methods
   * ==================================================================== */

  /* (non-Javadoc)
   * @see org.bedework.calfacade.base.BwDbentity#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final BwCalendar that) {
    if (that == this) {
      return 0;
    }

    if (that == null) {
      return -1;
    }

    return getPath().compareTo(that.getPath());
  }

  @Override
  public int hashCode() {
    if (getPath() == null) {
      return 1;
    }
    return getPath().hashCode();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("BwCalendar{");

    toStringSegment(sb);
    sb.append(", name=");
    sb.append(getName());
    sb.append(", path=");
    sb.append(getPath());
    sb.append(", tombstoned=");
    sb.append(getTombstoned());
    sb.append(", displayName=");
    sb.append(getSummary());
    sb.append(",\ndescription=");
    sb.append(getDescription());
    sb.append(", mailListId=");
    sb.append(String.valueOf(getMailListId()));
    sb.append(", calendarCollection=");
    sb.append(String.valueOf(getCalendarCollection()));
    sb.append(", calType=");
    sb.append(getCalType());

    /* Forces fetch
    if (hasChildren()) {
      sb.append(",\nchildren(");

      boolean donech = false;

      for (BwCalendar ch: getChildren()) {
        if (!donech) {
          donech = true;
        } else {
          sb.append(",\n");
        }

        sb.append(ch.getPath());
      }
      sb.append(")");
    }
    */

    sb.append(",\ncreated=");
    sb.append(getCreated());
    sb.append(", lastmod=");
    sb.append(getLastmod());

    if (getNumCategories() > 0) {
      sb.append(",\n categories=");
      for (BwCategory cat: getCategories()) {
        sb.append(cat);
        sb.append(", ");
      }
    }

    sb.append("}");

    return sb.toString();
  }

  @Override
  public Object clone() {
    BwCalendar cal = new BwCalendar();

    super.copyTo(cal);

    cal.setName(getName());
    cal.setPath(getPath());
    cal.setSummary(getSummary());
    cal.setDescription(getDescription());
    cal.setMailListId(getMailListId());
    cal.setCalType(getCalType());
    cal.setCreated(getCreated());
    cal.setLastmod((BwCollectionLastmod)getLastmod().clone());
    cal.setCategories(getCategories());
    cal.setProperties(getProperties());
    cal.setAliasUri(getAliasUri());
    cal.setDisplay(getDisplay());
    cal.setAffectsFreeBusy(getAffectsFreeBusy());
    cal.setIgnoreTransparency(getIgnoreTransparency());
    cal.setUnremoveable(getUnremoveable());
    cal.setRefreshRate(getRefreshRate());
    cal.setLastRefresh(getLastRefresh());
    cal.setLastEtag(getLastEtag());
    cal.setFilterExpr(getFilterExpr());

    //cal.setId(getId()); // Add to constructor
    //cal.setSeq(getSeq()); // Add to constructor
    return cal;
  }
}
