protected void logWarnIfNonZero(int val, String warning) {
    if (val > 0) {
        getLog().info(warning);
    } else {
        getLog().debug(warning);
    }
}
