/**
     * Set parent successful attribute based on IGNORE_FAILED_EMBEDDED_RESOURCES parameter
     * @param res {@link HTTPSampleResult}
     * @param initialValue boolean
     */
private void setParentSampleSuccess(HTTPSampleResult res, boolean initialValue) {
    if (!IGNORE_FAILED_EMBEDDED_RESOURCES) {
        res.setSuccessful(initialValue);
    }
}
