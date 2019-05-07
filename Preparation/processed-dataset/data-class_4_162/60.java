/**
     *  Parses the entire document from the Reader given in the constructor or
     *  set by {@link #setInputReader(Reader)}.
     *  
     *  @return A WikiDocument, ready to be passed to the renderer.
     *  @throws IOException If parsing cannot be accomplished.
     */
public WikiDocument parse() throws IOException {
    WikiDocument d = new WikiDocument(m_context.getPage());
    d.setContext(m_context);
    Element rootElement = new Element("domroot");
    d.setRootElement(rootElement);
    fillBuffer(rootElement);
    paragraphify(rootElement);
    return d;
}
