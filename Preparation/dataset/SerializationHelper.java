/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2001 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 *
 * $Id: SerializationHelper.java,v 1.1 2004/11/26 01:51:01 tanderson Exp $
 *
 * Date			Author  Changes
 * 20/11/2001   jima    Created
 */
package org.exolab.jms.tranlog;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * A utility class to assist with serialization and desearialization of objects
 * to and from byte stream
 */
public class SerializationHelper {

    /**
     * Serialize the specified object
     *
     * @param object - object to serialize
     * @return byte[] - the serialized object
     * @throws IOException - if there is an error with serialization
     */
    static public byte[] serialize(Object object)
        throws IOException {

        ByteArrayOutputStream bstream = new ByteArrayOutputStream();
        ObjectOutputStream ostream = new ObjectOutputStream(bstream);
        ostream.writeObject(object);

        return bstream.toByteArray();
    }

    /**
     * Deserialise the specified blob into an Object and return it
     * to the client.
     * <p>
     * If there is an error with the deserialization then throw an
     * IOException
     *
     * @param blob - the blob of bytes to deserialize
     * @return Object - the object to return
     * @throws IOException - if it cannot deseriliaze
     * @throws IllegalArgumentException if the blob is null
     */
    static public Object deserialize(byte[] blob)
        throws IOException, ClassNotFoundException {

        if (blob == null) {
            throw new IllegalArgumentException("null blob to deserialize");
        }

        ByteArrayInputStream bstream = new ByteArrayInputStream(blob);
        ObjectInputStream istream = new ObjectInputStream(bstream);

        return istream.readObject();
    }
}
