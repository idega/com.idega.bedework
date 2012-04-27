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
import java.util.List;

import net.fortuna.ical4j.vcard.Group;
import net.fortuna.ical4j.vcard.Parameter;
import net.fortuna.ical4j.vcard.Property;
import net.fortuna.ical4j.vcard.PropertyFactory;

/**
 * GEO property.
 * 
 * $Id: Geo.java,v 1.16 2010/03/06 12:49:48 fortuna Exp $
 *
 * Created on 19/09/2008
 *
 * @author Ben
 *
 */
public final class ClientPidMap extends Property {

    public static final PropertyFactory<ClientPidMap> FACTORY = new Factory();
    
    private static final long serialVersionUID = 1533383111522264554L;

    private static final String DELIMITER = ";";
    
    private int pid;
    
    private String urn;
    
    /**
     * @param pid
     * @param urn
     */
    public ClientPidMap(int pid, String urn) {
        super(Id.CLIENTPIDMAP);
        this.pid = pid;
        this.urn = urn;
    }
    
    /**
     * Factory constructor.
     * @param params property parameters
     * @param value string representation of a property value
     */
    public ClientPidMap(List<Parameter> params, String value) {
        this(null, params, value);
    }
    
    /**
     * Factory constructor.
     * @param group property group
     * @param params property parameters
     * @param value string representation of a property value
     */
    public ClientPidMap(Group group, List<Parameter> params, String value) {
        super(group, Id.CLIENTPIDMAP, params);
        // Allow comma as a separator if relaxed parsing enabled..
        String[] components = value.split(DELIMITER);

        this.pid = new Integer(components[0]);
        this.urn = components[1];
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return String.valueOf(pid) + DELIMITER + urn;
    }

    /**
     * @return the pid
     */
    public int getPid() {
        return pid;
    }

    /**
     * @return the urn
     */
    public String getUrn() {
        return urn;
    }

    private static class Factory implements PropertyFactory<ClientPidMap> {

        /**
         * {@inheritDoc}
         */
        public ClientPidMap createProperty(final List<Parameter> params, final String value) {
            return new ClientPidMap(params, value);
        }

        /**
         * {@inheritDoc}
         */
        public ClientPidMap createProperty(final Group group, final List<Parameter> params, final String value)
                throws URISyntaxException, ParseException {
            return new ClientPidMap(group, params, value);
        }
    }
}
