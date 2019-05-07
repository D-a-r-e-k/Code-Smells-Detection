private void enableChangeListeners() {
    DrJava.getConfig().addOptionListener(OptionConstants.JUNIT_LOCATION_ENABLED, _junitLocationEnabledListener);
    DrJava.getConfig().addOptionListener(OptionConstants.JUNIT_LOCATION, _junitLocationListener);
    DrJava.getConfig().addOptionListener(OptionConstants.CONCJUNIT_CHECKS_ENABLED, _concJUnitChecksEnabledListener);
    DrJava.getConfig().addOptionListener(OptionConstants.RT_CONCJUNIT_LOCATION, _rtConcJUnitLocationListener);
}
