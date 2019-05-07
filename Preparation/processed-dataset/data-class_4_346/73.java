/**
     * @see SerializationHandler#setTransformer(Transformer)
     */
public void setTransformer(Transformer transformer) {
    super.setTransformer(transformer);
    if (m_tracer != null && !(m_writer instanceof SerializerTraceWriter))
        setWriterInternal(new SerializerTraceWriter(m_writer, m_tracer), false);
}
