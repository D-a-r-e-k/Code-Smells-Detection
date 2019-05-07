/**
     * Creates a new protocol given the protocol specification. Initializes the properties and starts the
     * up and down handler threads.
     * @param prot_spec The specification of the protocol. Same convention as for specifying a protocol stack.
     *                  An exception will be thrown if the class cannot be created. Example:
     *                  <pre>"VERIFY_SUSPECT(timeout=1500)"</pre> Note that no colons (:) have to be
     *                  specified
     * @param stack The protocol stack
     * @return Protocol The newly created protocol
     * @exception Exception Will be thrown when the new protocol cannot be created
     */
public static Protocol createProtocol(String prot_spec, ProtocolStack stack) throws Exception {
    ProtocolConfiguration config;
    Protocol prot;
    if (prot_spec == null)
        throw new Exception("Configurator.createProtocol(): prot_spec is null");
    // parse the configuration for this protocol 
    config = new ProtocolConfiguration(prot_spec);
    // create an instance of the protocol class and configure it 
    prot = config.createLayer(stack);
    prot.init();
    return prot;
}
