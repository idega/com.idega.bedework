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
import java.util.List;

import net.fortuna.ical4j.vcard.Group;
import net.fortuna.ical4j.vcard.Parameter;
import net.fortuna.ical4j.vcard.Property;
import net.fortuna.ical4j.vcard.PropertyFactory;
import net.fortuna.ical4j.vcard.parameter.Encoding;
import net.fortuna.ical4j.vcard.parameter.Type;
import net.fortuna.ical4j.vcard.parameter.Value;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * KEY property.
 * 
 * $Id: Key.java,v 1.19 2010/04/11 05:06:16 fortuna Exp $
 *
 * Created on 23/10/2008
 *
 * @author Mike Douglass
 *
 */
public final class XML extends Property {

    public static final PropertyFactory<XML> FACTORY = new Factory();

    private static final long serialVersionUID = -12345L;

    private String value;
    
    private byte[] binary;

    private final Log log = LogFactory.getLog(XML.class);
    
    /**
     * @param uri a key URI
     */
    public XML(String value) {
        super(Id.XML);
        this.value = value;
        getParameters().add(Value.URI);
    }
    
    /**
     * @param binary binary key data
     */
    public XML(byte[] binary) {
        this(binary, null);
    }
    
    /**
     * @param binary binary key data
     * @param contentType key MIME type
     */
    public XML(byte[] binary, Type contentType) {
        super(Id.XML);
        this.binary = binary;
        getParameters().add(Encoding.B);
        if (contentType != null) {
            getParameters().add(contentType);
        }
    }
    
    /**
     * Factory constructor.
     * @param params property parameters
     * @param value string representation of a property value
     * @throws DecoderException if the specified string is not a valid key encoding
     */
    public XML(List<Parameter> params, String value) throws DecoderException {
        this(null, params, value);
    }
    
    /**
     * Factory constructor.
     * @param group property group
     * @param params property parameters
     * @param value string representation of a property value
     * @throws DecoderException if the specified string is not a valid key encoding
     */
    public XML(Group group, List<Parameter> params, 
    		   String value) throws DecoderException {
        super(group, Id.KEY, params);
        Parameter valueParameter = getParameter(Parameter.Id.VALUE);
        
        if (valueParameter == null || Value.TEXT.equals(valueParameter)) {
            this.value = value;
        }
        else {
            final BinaryDecoder decoder = new Base64();
            this.binary = decoder.decode(value.getBytes());
        }
    }
    
    /**
     * @return the binary
     */
    public byte[] getBinary() {
        return binary;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
    	Parameter valueParameter = getParameter(Parameter.Id.VALUE);
        
        if (valueParameter == null || Value.TEXT.equals(valueParameter)) {
        	return value;
        }
        
        if (binary != null) {
            try {
                final BinaryEncoder encoder = new Base64();
                return new String(encoder.encode(binary));
            }
            catch (EncoderException ee) {
                log.error("Error encoding binary data", ee);
            }
        }
        return null;
    }

    private static class Factory implements PropertyFactory<XML> {

        /**
         * {@inheritDoc}
         */
        public XML createProperty(final List<Parameter> params, final String value) throws DecoderException,
            URISyntaxException {
            
            return new XML(params, value);
        }

        /**
         * {@inheritDoc}
         */
        public XML createProperty(final Group group, final List<Parameter> params, final String value)
            throws DecoderException, URISyntaxException {
            
            return new XML(group, params, value);
        }
    }
}
