/**
     * @return creates a context for performing naming operations.
     * @throws NamingException if a naming exception is encountered
     */
protected InitialContext getInitialContext() throws NamingException {
    return new InitialContext();
}
