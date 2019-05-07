/*
     * @param res HTTPSampleResult to check
     * @return parser class name (may be "") or null if entry does not exist
     */
private String getParserClass(HTTPSampleResult res) {
    final String ct = res.getMediaType();
    return parsersForType.get(ct);
}
