/*
 * $Id: AbstractAxionBlob.java,v 1.3 2003/07/10 16:34:34 rwald Exp $
 * =======================================================================
 * Copyright (c) 2002-2003 Axion Development Team.  All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 * 
 * 1. Redistributions of source code must retain the above 
 *    copyright notice, this list of conditions and the following 
 *    disclaimer. 
 *   
 * 2. Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in 
 *    the documentation and/or other materials provided with the 
 *    distribution. 
 *   
 * 3. The names "Tigris", "Axion", nor the names of its contributors may 
 *    not be used to endorse or promote products derived from this 
 *    software without specific prior written permission. 
 *  
 * 4. Products derived from this software may not be called "Axion", nor 
 *    may "Tigris" or "Axion" appear in their names without specific prior
 *    written permission.
 *   
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT 
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY 
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * =======================================================================
 */

package org.axiondb.jdbc;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * Abstract base implementation of {@link AxionBlob}.
 * 
 * @TODO Not actually being abstract, perhaps we should rename this class.
 * @version $Revision: 1.3 $ $Date: 2003/07/10 16:34:34 $
 * @author Rodney Waldhoff
 */
public class AbstractAxionBlob implements AxionBlob {
    /** @throws SQLException indicating this method is not supported. */
    public InputStream getBinaryStream() throws SQLException {
        throw new SQLException("Not implemented");
    }

    /** @throws SQLException indicating this method is not supported. */
    public byte[] getBytes(long pos, int length) throws SQLException {
        throw new SQLException("Not implemented");
    }

    /** @throws SQLException indicating this method is not supported. */
    public long length() throws SQLException {
        throw new SQLException("Not implemented");
    }

    /** @throws SQLException indicating this method is not supported. */
    public long position(Blob pattern, long start) throws SQLException {
        throw new SQLException("Not implemented");
    }

    /** @throws SQLException indicating this method is not supported. */
    public long position(byte[] pattern, long start) throws SQLException {
        throw new SQLException("Not implemented");
    }

    /** @throws SQLException indicating this method is not supported. */
    public OutputStream setBinaryStream(long pos) throws SQLException {
        throw new SQLException("Not implemented");
    }

    /** Invokes {@link #setBytes(long,byte[],int,int) setBytes(pos,bytes,0,bytes.length)} */
    public int setBytes(long pos, byte[] bytes) throws SQLException {
        return setBytes(pos,bytes,0,bytes.length);
    }

    /** @throws SQLException indicating this method is not supported. */
    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
        throw new SQLException("Not implemented");
    }

    /** @throws SQLException indicating this method is not supported. */
    public void truncate(long len) throws SQLException {
        throw new SQLException("Not implemented");
    }
}
