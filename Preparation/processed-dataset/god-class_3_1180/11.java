/**
   * Set an object that will be used to resolve URIs used in
   * document().
   *
   * <p>If the resolver argument is null, the URIResolver value will
   * be cleared, and the default behavior will be used.</p>
   *
   * @param resolver An object that implements the URIResolver interface,
   * or null.
   */
public void setURIResolver(URIResolver resolver) {
    m_URIResolver = resolver;
}
