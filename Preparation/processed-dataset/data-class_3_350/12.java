/**
   * Get an object that will be used to resolve URIs used in
   * document(), etc.
   *
   * @return An object that implements the URIResolver interface,
   * or null.
   */
public URIResolver getURIResolver() {
    return m_URIResolver;
}
