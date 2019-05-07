private void setWriterInternal(Writer writer, boolean setByUser) {
    m_writer_set_by_user = setByUser;
    m_writer = writer;
    // if we are tracing events we need to trace what  
    // characters are written to the output writer.  
    if (m_tracer != null) {
        boolean noTracerYet = true;
        Writer w2 = m_writer;
        while (w2 instanceof WriterChain) {
            if (w2 instanceof SerializerTraceWriter) {
                noTracerYet = false;
                break;
            }
            w2 = ((WriterChain) w2).getWriter();
        }
        if (noTracerYet)
            m_writer = new SerializerTraceWriter(m_writer, m_tracer);
    }
}
