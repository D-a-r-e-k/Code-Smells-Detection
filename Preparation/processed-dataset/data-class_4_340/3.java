private void disableChangeListeners() {
    DrJava.getConfig().removeOptionListener(OptionConstants.JUNIT_LOCATION_ENABLED, _junitLocationEnabledListener);
    DrJava.getConfig().removeOptionListener(OptionConstants.JUNIT_LOCATION, _junitLocationListener);
    DrJava.getConfig().removeOptionListener(OptionConstants.CONCJUNIT_CHECKS_ENABLED, _concJUnitChecksEnabledListener);
    DrJava.getConfig().removeOptionListener(OptionConstants.RT_CONCJUNIT_LOCATION, _rtConcJUnitLocationListener);
}
