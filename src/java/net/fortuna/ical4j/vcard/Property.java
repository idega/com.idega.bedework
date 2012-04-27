/**
 * Copyright (c) 2012, Ben Fortuna
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  o Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 *  o Neither the name of Ben Fortuna nor the names of any other contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.fortuna.ical4j.vcard;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.fortuna.ical4j.model.Escapable;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.util.Strings;
import net.fortuna.ical4j.vcard.Property.Id.Par;
import net.fortuna.ical4j.vcard.Property.Id.ParameterInfo;
import net.fortuna.ical4j.vcard.parameter.Value;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * A vCard property.
 * 
 * $Id$
 *
 * Created on 21/08/2008
 *
 * @author Ben
 *
 */
public abstract class Property implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7813173744145071469L;

    protected static final String ILLEGAL_PARAMETER_MESSAGE = "Illegal parameter [{0}]";
    
    private static final String ILLEGAL_PARAMETER_COUNT_MESSAGE = "Parameter [{0}] exceeds allowable count";
    
    /**
     * Enumeration of property identifiers.
     */
    public enum Id {
        // 6.1.  General Properties
    	
    	SOURCE(new ParameterInfo(Value.URI, true, 
	    			Parameter.Id.PID,
	    			Parameter.Id.PREF,
	    			Parameter.Id.ALTID)), // any-param 

        KIND(new ParameterInfo(Value.TEXT)),
        
        XML(new ParameterInfo(Value.TEXT, true, 
	        		Par.make(Parameter.Id.ALTID),
	        		Par.make(Parameter.Id.VERSION, true)),
				new ParameterInfo(Value.BINARY, false, 
						Par.make(Parameter.Id.ENCODING), 
						Par.make(Parameter.Id.ALTID),
						Par.make(Parameter.Id.VERSION, true))),
        
        // 6.2.  Identification Properties
        		
        NAME(new ParameterInfo(Value.TEXT)),  // vcard 3
        
        FN(new ParameterInfo(Value.TEXT, true, 
	        		Parameter.Id.TYPE,
	        		Parameter.Id.LANGUAGE,
	        		Parameter.Id.ALTID,
	        		Parameter.Id.PID,
	        		Parameter.Id.PREF)), 

        N(new ParameterInfo(Value.TEXT, true, 
	        		Parameter.Id.SORT_AS,
	        		Parameter.Id.LANGUAGE,
	        		Parameter.Id.ALTID)), 

        NICKNAME(new ParameterInfo(Value.TEXT, true, 
	        		Parameter.Id.TYPE,
	        		Parameter.Id.LANGUAGE,
	        		Parameter.Id.ALTID,
	        		Parameter.Id.PID,
	        		Parameter.Id.PREF)), 

        PHOTO(new ParameterInfo(Value.BINARY, true,
				Parameter.Id.ENCODING, // binary only
		        	Parameter.Id.FMTTYPE,
		        	Parameter.Id.ALTID,
		        	Parameter.Id.TYPE,
				Parameter.Id.PID,
				Parameter.Id.PREF),
				new ParameterInfo(Value.URI, false,
						Parameter.Id.FMTTYPE,
						Parameter.Id.ALTID,
						Parameter.Id.TYPE,
						Parameter.Id.PID,
						Parameter.Id.PREF)),
			
        BDAY(new ParameterInfo(Value.DATE_AND_OR_TIME, true,
	        		Parameter.Id.ALTID,
	        		Parameter.Id.CALSCALE),
	        		new ParameterInfo(Value.TEXT, false,
	        				Parameter.Id.ALTID)),
        		
        DDAY(new ParameterInfo(Value.DATE_AND_OR_TIME, true,
	        		Parameter.Id.ALTID,
	        		Parameter.Id.CALSCALE),
	        		new ParameterInfo(Value.TEXT, false,
	        				Parameter.Id.ALTID)), 
    			
        BIRTH(new ParameterInfo(Value.TEXT, true,
	        		Parameter.Id.ALTID,
	        		Parameter.Id.LANGUAGE),
	        		new ParameterInfo(Value.URI, false,
	        				Parameter.Id.ALTID,
	        				Parameter.Id.LANGUAGE)), 
    			 
        DEATH(new ParameterInfo(Value.TEXT, true,
        			Parameter.Id.ALTID,
        			Parameter.Id.LANGUAGE),
        			new ParameterInfo(Value.URI, false,
        					Parameter.Id.ALTID,
        					Parameter.Id.LANGUAGE)),

        ANNIVERSARY(new ParameterInfo(Value.DATE_AND_OR_TIME, true,
        			Parameter.Id.ALTID,
        			Parameter.Id.CALSCALE),
        			new ParameterInfo(Value.TEXT, false,
        					Parameter.Id.ALTID)),
        
        SEX(new ParameterInfo(Value.INTEGER)),
        
        // 6.3.  Delivery Addressing Properties
        
        ADR(new ParameterInfo(Value.TEXT, true,
        			Parameter.Id.LANGUAGE,
        			Parameter.Id.GEO,
        			Parameter.Id.TZ,
        			Parameter.Id.ALTID,
        			Parameter.Id.PID,
        			Parameter.Id.PREF,
        			Parameter.Id.TYPE)), // any-param 
        
        LABEL(new ParameterInfo(Value.TEXT, true,
        			Parameter.Id.LANGUAGE,
        			Parameter.Id.PID,
        			Parameter.Id.PREF,
        			Parameter.Id.ALTID,
        			Parameter.Id.TYPE)),

        // 6.4.  Communications Properties
        
        TEL(new ParameterInfo(Value.URI, true,
        			Parameter.Id.TYPE,
        			Parameter.Id.PID,
        			Parameter.Id.PREF,
        			Parameter.Id.ALTID)), 
        
        EMAIL(new ParameterInfo(Value.TEXT, true,
        			Parameter.Id.PID,
        			Parameter.Id.PREF,
        			Parameter.Id.TYPE,
        			Parameter.Id.ALTID)),

        IMPP(new ParameterInfo(Value.URI, true,
        			Parameter.Id.PID,
        			Parameter.Id.PREF,
        			Parameter.Id.TYPE,
        			Parameter.Id.ALTID)), 

        LANG(new ParameterInfo(Value.LANGUAGE_TAG, true,
        			Parameter.Id.PID,
        			Parameter.Id.PREF,
        			Parameter.Id.TYPE,
        			Parameter.Id.ALTID)),
        
        // 6.5.  Geographical Properties

        TZ(new ParameterInfo(Value.TEXT),
        			new ParameterInfo(Value.URI),
        			new ParameterInfo(Value.UTC_OFFSET)), 

        GEO(new ParameterInfo(Value.URI, true,
        			Parameter.Id.PID,
        			Parameter.Id.PREF,
        			Parameter.Id.TYPE,
        			Parameter.Id.ALTID)),

        // 6.6.  Organizational Properties
        
        AGENT(new ParameterInfo(Value.TEXT)), // V3 Should be rewritten as RELATED
        
        TITLE(new ParameterInfo(Value.TEXT, true,
        			Parameter.Id.LANGUAGE,
        			Parameter.Id.PID,
        			Parameter.Id.PREF,
        			Parameter.Id.ALTID,
        			Parameter.Id.TYPE)), 

        ROLE(new ParameterInfo(Value.TEXT, true,
        			Parameter.Id.LANGUAGE,
        			Parameter.Id.PID,
        			Parameter.Id.PREF,
        			Parameter.Id.ALTID,
        			Parameter.Id.TYPE)), 

        LOGO(new ParameterInfo(Value.BINARY, true,
				Parameter.Id.ENCODING, // binary only
	        		Parameter.Id.FMTTYPE,
	        		Parameter.Id.LANGUAGE,
	        		Parameter.Id.ALTID,
	        		Parameter.Id.TYPE,
				Parameter.Id.PID,
				Parameter.Id.PREF),
				new ParameterInfo(Value.URI, false,
						Parameter.Id.FMTTYPE,
						Parameter.Id.LANGUAGE,
						Parameter.Id.ALTID,
						Parameter.Id.TYPE,
						Parameter.Id.PID,
						Parameter.Id.PREF)),
         
        ORG(new ParameterInfo(Value.TEXT, true,
        			Parameter.Id.SORT_AS,
        			Parameter.Id.LANGUAGE,
        			Parameter.Id.PID,
        			Parameter.Id.PREF,
        			Parameter.Id.ALTID,
        			Parameter.Id.TYPE)), 

        MEMBER(new ParameterInfo(Value.URI, true,
        			Parameter.Id.ALTID,
				Parameter.Id.PID,
				Parameter.Id.PREF)), 
    
        RELATED(new ParameterInfo(Value.URI, true,
        			Parameter.Id.ALTID,
        			Parameter.Id.TYPE,
				Parameter.Id.PID,
				Parameter.Id.PREF),
				new ParameterInfo(Value.TEXT, false,
						Parameter.Id.LANGUAGE,
						Parameter.Id.ALTID,
						Parameter.Id.TYPE,
						Parameter.Id.PID,
						Parameter.Id.PREF)),

        // 6.7.  Explanatory Properties
        
        CATEGORIES(new ParameterInfo(Value.TEXT, true,
        			Parameter.Id.ALTID,
	        		Parameter.Id.TYPE,
				Parameter.Id.PID,
				Parameter.Id.PREF)), 
  
        NOTE(new ParameterInfo(Value.TEXT, true,
	        		Parameter.Id.LANGUAGE,
	        		Parameter.Id.ALTID,
	        		Parameter.Id.TYPE,
				Parameter.Id.PID,
				Parameter.Id.PREF)),

        PRODID(new ParameterInfo(Value.TEXT)),
        
        REV(new ParameterInfo(Value.TIMESTAMP)),
        
        SOUND(new ParameterInfo(Value.BINARY, true,
				Parameter.Id.ENCODING, // binary only
	        		Parameter.Id.FMTTYPE,
	        		Parameter.Id.LANGUAGE,
	        		Parameter.Id.ALTID,
	        		Parameter.Id.TYPE,
				Parameter.Id.PID,
				Parameter.Id.PREF),
				new ParameterInfo(Value.URI, false,
						Parameter.Id.FMTTYPE,
						Parameter.Id.LANGUAGE,
						Parameter.Id.ALTID,
						Parameter.Id.TYPE,
						Parameter.Id.PID,
						Parameter.Id.PREF)), 

        UID(new ParameterInfo(Value.URI)),
        
        CLIENTPIDMAP(new ParameterInfo(Value.TEXT)),
        
        URL(new ParameterInfo(Value.URI, true,
	        		Parameter.Id.ALTID,
	        		Parameter.Id.TYPE,
				Parameter.Id.PID,
				Parameter.Id.PREF)), 

        VERSION(new ParameterInfo(Value.TEXT)),
        
        // 6.8.  Security Properties
        
        CLASS(new ParameterInfo(Value.TEXT)), 
        
        KEY(new ParameterInfo(Value.BINARY, true,
        			Parameter.Id.ENCODING, // binary only
        			Parameter.Id.FMTTYPE,
        			Parameter.Id.ALTID,
        			Parameter.Id.TYPE,
				Parameter.Id.PID,
				Parameter.Id.PREF),
				new ParameterInfo(Value.URI, false,
						Parameter.Id.FMTTYPE,
						Parameter.Id.ALTID,
						Parameter.Id.TYPE,
						Parameter.Id.PID,
						Parameter.Id.PREF),
				new ParameterInfo(Value.TEXT, false,
						Parameter.Id.LANGUAGE,
						Parameter.Id.ALTID,
						Parameter.Id.TYPE,
						Parameter.Id.PID,
						Parameter.Id.PREF)),
        
        // 6.9.  Calendar Properties
        
        FBURL(new ParameterInfo(Value.URI, true,
        			Parameter.Id.PID,
        			Parameter.Id.PREF,
        			Parameter.Id.TYPE,
        			Parameter.Id.ALTID)), 

        CALADRURI(new ParameterInfo(Value.URI, true,
        			Parameter.Id.PID,
        			Parameter.Id.PREF,
        			Parameter.Id.TYPE,
        			Parameter.Id.ALTID)), 
        		
        CALURI(new ParameterInfo(Value.URI, true,
        			Parameter.Id.PID,
        			Parameter.Id.PREF,
        			Parameter.Id.TYPE,
        			Parameter.Id.ALTID)),
        
        // 6.10. Extended Properties and Parameters
        EXTENDED(new ParameterInfo(Value.TEXT)),
        
        // Resource properties
        
        ACCESSABILITYINFO(new ParameterInfo(Value.URI)), 
        
        ADMISSIONINFO(new ParameterInfo(Value.URI)), 
        
        APPROVALINFO(new ParameterInfo(Value.URI)), 
        
        AUTOSCHEDULE(new ParameterInfo(Value.BOOLEAN)),
        
        BOOKINGEND(new ParameterInfo(Value.DURATION)),
        
        BOOKINGSTART(new ParameterInfo(Value.DURATION)), 
        
        CAPACITY(new ParameterInfo(Value.INTEGER)), 
        
        COSTINFO(new ParameterInfo(Value.URI)), 
        
        INVENTORYLIST(new ParameterInfo(Value.TEXT)), 
        
        INVENTORYURL(new ParameterInfo(Value.URI)), 
        
        MAXINSTANCES(new ParameterInfo(Value.INTEGER)), 
        
        MULTIBOOK(new ParameterInfo(Value.BOOLEAN)), 
        
        NOCOST(new ParameterInfo(Value.BOOLEAN)),
        
        RESOURCEMANAGERINFO(new ParameterInfo(Value.URI)), 
        
        RESOURCEOWNERINFO(new ParameterInfo(Value.URI)),
        
        RESTRICTEDACCESS(new ParameterInfo(Value.BOOLEAN)),
        
        SCHEDADMININFO(new ParameterInfo(Value.URI));
        
        public static class Par {
        		public Parameter.Id id;
        	
        		public boolean must;
        	
        		Par(Parameter.Id id) {
        			this(id, false);
        		}
        	
        		Par(Parameter.Id id,	boolean must) {
        			this.id = id;
        			this.must = must;
        		}
        	
        		static Par make(Parameter.Id id) {
        			return new Par(id);
        		}
        	
        		static Par make(Parameter.Id id, boolean must) {
        			return new Par(id, must);
        		}
        }

    		public static class ParameterInfo {
    			public Value valueType;
            
    			public boolean defType;
    		
            public List<Par> pars;
            
            public ParameterInfo(Value valueType) {
            		this.valueType = valueType;
            		this.defType = true;
            }
            
            public ParameterInfo(Value valueType, boolean defType, Parameter.Id... parameters) {
            		this.valueType = valueType;
            		this.defType = defType;

                if (parameters != null) {
                    this.pars = new ArrayList<Par>();
                    for (Parameter.Id id: parameters) {
                    		this.pars.add(new Par(id));
                    }
                }
            }
            
            public ParameterInfo(Value valueType,	boolean defType, 	Par... pars) {
            		this.valueType = valueType;
            		this.defType = defType;
            	
                if (pars != null) {
                    this.pars = new ArrayList<Par>();
                    for (Par p: pars) {
                    		this.pars.add(p);
                    }
                }
            }
            
            public Par getPar(Parameter.Id id) {
            		if (pars == null) {
            			return null;
            		}
            	
            		for (Par p: pars) {
            			if (p.id == id) {
            				return p;
            			}
            		}
            	
            		return null;
            }
    		}
        
        private String propertyName;
     
        private List<ParameterInfo> parInfo;

        /**
         * @param parameters list of valid parameter ids
         */
        private Id(ParameterInfo... parInfo) {
            this(null, parInfo);
        }

        /**
         * @param propertyName the property name
         * @param parameters list of valid parameter ids
         */
        private Id(String propertyName, 
        		ParameterInfo... parInfo) {
            this.propertyName = propertyName;

            if (parInfo != null) {
                this.parInfo = new ArrayList<ParameterInfo>();
                for (ParameterInfo pi: parInfo) {
                	this.parInfo.add(pi);
                }
            }
        }
        
        /**
         * FIXME
         */
//        private Id() {
//            this(null);
//        }
        
        /**
         * @param propertyName the property name
         */
        private Id(String propertyName) {
            this.propertyName = propertyName;
        }
        
        /**
         * @return the property name
         */
        public String getPropertyName() {
            if (isNotEmpty(propertyName)) {
                return propertyName;
            }
            return toString();
        }
        
        public ParameterInfo getParameterInfo(Value valueType) {
        		for (ParameterInfo pi: this.parInfo) {
        			if (valueType == null) {
        				if (pi.defType) {
        					return pi;
        				}
        				continue;
        			}
        		
        			if (pi.valueType.equals(valueType)) {
        				return pi;
        			}
        		}
        		
        		return null;
        }
    };
    
    private final Group group;
    
    private final Id id;
    
    String extendedName = "";
    
    private final List<Parameter> parameters;

    /**
     * @param extendedName a non-standard property name
     */
    public Property(String extendedName) {
        this(null, extendedName);
    }
    
    /**
     * @param group a property group
     * @param extendedName the non-standard property name
     */
    public Property(Group group, String extendedName) {
        this(group, Id.EXTENDED);
        this.extendedName = extendedName;
    }

    /**
     * @param extendedName a non-standard property name
     * @param parameters property parameters
     */
    public Property(String extendedName, List<Parameter> parameters) {
        this(null, extendedName, parameters);
    }
    
    /**
     * @param group a property group
     * @param extendedName the non-standard property name
     * @param parameters property parameters
     */
    public Property(Group group, String extendedName, List<Parameter> parameters) {
        this(group, Id.EXTENDED, parameters);
        this.extendedName = extendedName;
    }
    
    /**
     * @param id the property type
     */
    public Property(Id id) {
        this(null, id);
    }

    /**
     * @param group a property group
     * @param id a standard property identifier
     */
    public Property(Group group, Id id) {
        this(group, id, new ArrayList<Parameter>());
    }
    
    /**
     * @param id a standard property identifier
     * @param parameters property parameters
     */
    protected Property(Id id, List<Parameter> parameters) {
        this(null, id, parameters);
    }

    /**
     * @param group a property group
     * @param id a standard property identifier
     * @param parameters property parameters
     */
    protected Property(Group group, Id id, List<Parameter> parameters) {
        this.group = group;
        this.id = id;
        this.parameters = new CopyOnWriteArrayList<Parameter>(parameters);
    }
    
    /**
     * @return the group
     */
    public final Group getGroup() {
        return group;
    }

    /**
     * @return the id
     */
    public final Id getId() {
        return id;
    }

    /**
     * @return the parameters
     */
    public final List<Parameter> getParameters() {
        return parameters;
    }

    /**
     * Returns a list of parameters matching the specified identifier.
     * @param id a parameter identifier
     * @return a list of parameters
     */
    public final List<Parameter> getParameters(final Parameter.Id id) {
        final List<Parameter> matches = new ArrayList<Parameter>();
        for (Parameter p : parameters) {
            if (p.getId().equals(id)) {
                matches.add(p);
            }
        }
        return Collections.unmodifiableList(matches);
    }
    
    /**
     * Returns the first parameter with a matching identifier.
     * @param id a parameter identifier
     * @return the first matching parameter, or null if no parameters with the specified identifier are found
     */
    public final Parameter getParameter(final Parameter.Id id) {
        for (Parameter p : parameters) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Returns a list of non-standard parameters matching the specified name.
     * @param name a non-standard parameter name
     * @return a list of parameters
     */
    public final List<Parameter> getExtendedParameters(final String name) {
        final List<Parameter> matches = new ArrayList<Parameter>();
        for (Parameter p : parameters) {
            if (p.getId().equals(Parameter.Id.EXTENDED) && p.extendedName.equals(name)) {
                matches.add(p);
            }
        }
        return Collections.unmodifiableList(matches);
    }
    
    /**
     * Returns the first non-standard parameter with a matching name.
     * @param name a non-standard parameter name
     * @return the first matching parameter, or null if no non-standard parameters with the specified name are found
     */
    public final Parameter getExtendedParameter(final String name) {
        for (Parameter p : parameters) {
            if (p.getId().equals(Parameter.Id.EXTENDED) && p.extendedName.equals(name)) {
                return p;
            }
        }
        return null;
    }
    
    /**
     * @return a string representaion of the property propertyName
     */
    public abstract String getValue();
    
    /**
     * @throws ValidationException where the property fails validation rules
     */
    public void validate() throws ValidationException {
    		Value valueType = (Value) getParameter(Parameter.Id.VALUE);

    		ParameterInfo pi = getId().getParameterInfo(valueType);

    		if (pi == null) {
    			// Invalid value
    			throw new ValidationException("Invalid type " + valueType +
    					" Found on property " + this);
    		}

    		boolean hadValue = false;

    		for (Parameter p: getParameters()) {
    			if (p.getId() == Parameter.Id.VALUE) {
    				if (hadValue) {
    					throw new ValidationException("Value must only appear once." +
   							" Found on property " + this);
    				}
    				hadValue = true;
    				continue;
    		}

    		if (pi.getPar(p.getId()) == null) {
    			throw new ValidationException("Invalid parameter " + p +
    					" Found on property " + this);
    		}
    	}

    	if (pi.pars != null) {
    		for (Par p: pi.pars) {
    			if (p.must) {
    				if (getParameter(p.id) == null) {
    					throw new ValidationException("Missing required parameter " + p.id +
    							" Found on property " + this);
    				}
    			}
    		}
    	}
    }
    
    /**
     * @throws ValidationException where the parameter list is not empty
     */
    protected final void assertParametersEmpty() throws ValidationException {
        if (!getParameters().isEmpty()) {
            throw new ValidationException("No parameters allowed for property: " + id);
        }
    }
    
    /**
     * @param param a parameter to validate
     * @throws ValidationException where the specified parameter is not a text parameter
     */
    protected final void assertTextParameter(final Parameter param) throws ValidationException {
        if (!Value.TEXT.equals(param) && !Parameter.Id.LANGUAGE.equals(param.getId())
                && !Parameter.Id.EXTENDED.equals(param.getId())) {
            throw new ValidationException(MessageFormat.format(ILLEGAL_PARAMETER_MESSAGE, param.getId()));
        }
    }
    
    /**
     * @param param a parameter to validate
     * @throws ValidationException where the specified parameter is not a type parameter
     */
    protected final void assertTypeParameter(final Parameter param) throws ValidationException {
        if (!Parameter.Id.TYPE.equals(param.getId())) {
            throw new ValidationException(MessageFormat.format(ILLEGAL_PARAMETER_MESSAGE, param.getId()));
        }
    }
    
    /**
     * @param param a parameter to validate
     * @throws ValidationException where the specified parameter is not a PID parameter
     */
    protected final void assertPidParameter(final Parameter param) throws ValidationException {
        if (!Parameter.Id.PID.equals(param.getId())) {
            throw new ValidationException(MessageFormat.format(ILLEGAL_PARAMETER_MESSAGE, param.getId()));
        }
    }
    
    /**
     * @param param a parameter to validate
     * @throws ValidationException where the specified parameter is not a Pref parameter
     */
    protected final void assertPrefParameter(final Parameter param) throws ValidationException {
        if (!Parameter.Id.PREF.equals(param.getId())) {
            throw new ValidationException(MessageFormat.format(ILLEGAL_PARAMETER_MESSAGE, param.getId()));
        }
    }
    
    /**
     * @param paramId a parameter identifier to validate from
     * @throws ValidationException where there is not one or less of the specified
     *  parameter in the parameter list
     */
    protected final void assertOneOrLess(final Parameter.Id paramId) throws ValidationException {
        if (getParameters(paramId).size() > 1) {
            throw new ValidationException(MessageFormat.format(ILLEGAL_PARAMETER_COUNT_MESSAGE, paramId));
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    
    /**
     * @return a vCard-compliant string representation of the property
     */
    @Override
    public final String toString() {
        final StringBuilder b = new StringBuilder();
        if (group != null) {
            b.append(group);
            b.append('.');
        }
        if (Id.EXTENDED.equals(id)) {
            b.append("X-");
            b.append(extendedName);
        }
        else {
            b.append(id.getPropertyName());
        }
        for (Parameter param : parameters) {
            b.append(';');
            b.append(param);
        }
        b.append(':');
        if (this instanceof Escapable) {
            b.append(Strings.escape(Strings.valueOf(getValue())));
        }
        else {
            b.append(Strings.valueOf(getValue()));
        }
        b.append(Strings.LINE_SEPARATOR);
        return b.toString();
    }
}
