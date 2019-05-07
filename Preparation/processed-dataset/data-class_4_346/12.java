/**
     * Specifies a writer to which the document should be serialized.
     * This method should not be called while the serializer is in
     * the process of serializing a document.
     *
     * @param writer The output writer stream
     */
public void setWriter(Writer writer) {
    setWriterInternal(writer, true);
}
