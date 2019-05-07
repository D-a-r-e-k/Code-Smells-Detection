/**
     * To fire off the pseudo characters of attributes, as they currently
     * exist. This method should be called everytime an attribute is added,
     * or when an attribute value is changed, or an element is created.
     */
protected void firePseudoAttributes() {
    if (m_tracer != null) {
        try {
            // flush out the "<elemName" if not already flushed  
            m_writer.flush();
            // make a StringBuffer to write the name="value" pairs to.  
            StringBuffer sb = new StringBuffer();
            int nAttrs = m_attributes.getLength();
            if (nAttrs > 0) {
                // make a writer that internally appends to the same  
                // StringBuffer  
                java.io.Writer writer = new ToStream.WritertoStringBuffer(sb);
                processAttributes(writer, nAttrs);
            }
            sb.append('>');
            // the potential > after the attributes.  
            // convert the StringBuffer to a char array and  
            // emit the trace event that these characters "might"  
            // be written                  
            char ch[] = sb.toString().toCharArray();
            m_tracer.fireGenerateEvent(SerializerTrace.EVENTTYPE_OUTPUT_PSEUDO_CHARACTERS, ch, 0, ch.length);
        } catch (IOException ioe) {
        } catch (SAXException se) {
        }
    }
}
