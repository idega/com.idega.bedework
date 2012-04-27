/**
 * Copyright (c) 2010, Ben Fortuna
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
package net.fortuna.ical4j.vcard.property;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.fortuna.ical4j.vcard.Group;
import net.fortuna.ical4j.vcard.Parameter;
import net.fortuna.ical4j.vcard.Property;
import net.fortuna.ical4j.vcard.PropertyFactory;

/**
 * SEX property.
 * 
 * $Id: Gender.java,v 1.13 2010/03/06 12:49:49 fortuna Exp $
 *
 * Created on 23/08/2008
 *
 * @author Ben
 *
 */
public final class Sex extends Property {

    /**
     * 
     */
    private static final long serialVersionUID = -2739534182576803750L;
    
    /**
     * Standard gender.
     */
    public static final Sex MALE = new Sex(Collections.unmodifiableList(new ArrayList<Parameter>()), "M");
    
    /**
     * Standard gender.
     */
    public static final Sex FEMALE = new Sex(Collections.unmodifiableList(new ArrayList<Parameter>()), "F");
    
    public static final PropertyFactory<Sex> FACTORY = new Factory();
    
    private final String value;
    
    /**
     * @param value string representation of a property value
     */
    public Sex(String value) {
        super(Id.SEX);
        this.value = value;
    }
    
    /**
     * Factory constructor.
     * @param params property parameters
     * @param value string representation of a property value
     */
    private Sex(List<Parameter> params, String value) {
        super(Id.SEX, params);
        this.value = value;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return value;
    }

    private static class Factory implements PropertyFactory<Sex> {

        /**
         * {@inheritDoc}
         */
        public Sex createProperty(final List<Parameter> params, final String value) {
            Sex property = null;
            if (Sex.FEMALE.getValue().equals(value)) {
                property = Sex.FEMALE;
            }
            else if (Sex.MALE.getValue().equals(value)) {
                property = Sex.MALE;
            }
            else {
                property = new Sex(value);
            }
            return property;
        }

        /**
         * {@inheritDoc}
         */
        public Sex createProperty(final Group group, final List<Parameter> params, final String value)
                throws URISyntaxException, ParseException {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
