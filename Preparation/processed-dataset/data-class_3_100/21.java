/**
     * @see org.java.plugin.registry.PluginRegistry#checkIntegrity(
     *      org.java.plugin.PathResolver, boolean)
     */
public IntegrityCheckReport checkIntegrity(final PathResolver pathResolver, final boolean includeRegistrationReport) {
    final Collection<ReportItem> empty_collection = Collections.emptyList();
    IntegrityChecker intergityCheckReport = new IntegrityChecker(this, includeRegistrationReport ? registrationReport : empty_collection);
    intergityCheckReport.doCheck(pathResolver);
    return intergityCheckReport;
}
