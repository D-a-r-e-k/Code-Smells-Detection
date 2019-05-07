/*
DOMOutput.java
:tabSize=4:indentSize=4:noTabs=true:
:folding=explicit:collapseFolds=1:

Copyright (C) 2002 Ian Lewis (IanLewis@member.fsf.org)

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
Optionally, you may find a copy of the GNU General Public License
from http://www.fsf.org/copyleft/gpl.txt
*/

package net.sourceforge.jsxe.dom;

//{{{ imports

//{{{ DOM classes
import org.w3c.dom.ls.LSOutput;
//}}}

//{{{ Java classes
import java.io.OutputStream;
import java.io.Writer;
//}}}

//}}}

/**
 * An implementation of the LSOutput interface for use with the DOMSerializer
 * class. This object is used when generating locations into a document.
 * @author <a href="mailto:IanLewis at member dot fsf dot org">Ian Lewis</a>
 * @version $Id: DOMOutput.java,v 1.4 2005/04/15 20:00:52 ian_lewis Exp $
 * @see DOMSerializer
 */
public class DOMOutput implements LSOutput {
    
    //{{{ DOMOutput constructor
    
    public DOMOutput(OutputStream byteStream, String encoding) {
        m_byteStream = byteStream;
        m_encoding = encoding;
    }//}}}
    
    //{{{ DOMOutput constructor
    
    public DOMOutput(String systemId, String encoding) {
        m_systemId = systemId;
        m_encoding = encoding;
    }//}}}
    
    //{{{ DOMOutput constructor
    
    public DOMOutput(Writer characterStream) {
        m_characterStream = characterStream;
    }//}}}
    
    //{{{ Implemented LSOutput methods
    
    //{{{ getByteStream()
    
    public OutputStream getByteStream() {
        return m_byteStream;
    }//}}}
    
    //{{{ getCharacterStream()
    
    public Writer getCharacterStream() {
        return m_characterStream;
    }//}}}
    
    //{{{ getEncoding()
    
    public String getEncoding() {
        return m_encoding;
    }//}}}
    
    //{{{ getSystemId()
    
    public String getSystemId() {
        return m_systemId;
    }//}}}
    
    //{{{ getByteStream()
    
    public void setByteStream(OutputStream byteStream) {
        m_byteStream = byteStream;
    }//}}}
    
    //{{{ setCharacterStream()
    
    public void setCharacterStream(Writer characterStream) {
        m_characterStream = characterStream;
    }//}}}
    
    //{{{ setEncoding()
    
    public void setEncoding(String encoding) {
        m_encoding = encoding;
    }//}}}
    
    //{{{ setSystemId()
    
    public void setSystemId(String systemId) {
        m_systemId = systemId;
    }//}}}
    
    //}}}
    
    //{{{ Private members
    
    private OutputStream m_byteStream;
    private Writer m_characterStream;
    private String m_systemId;
    private String m_encoding;
    
    //}}}
}
