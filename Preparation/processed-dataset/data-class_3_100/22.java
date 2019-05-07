/**
     * @see org.java.plugin.registry.PluginRegistry#getRegistrationReport()
     */
public IntegrityCheckReport getRegistrationReport() {
    return new IntegrityChecker(this, registrationReport);
}
