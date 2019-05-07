// startGeneralEntity(String,XMLResourceIdentifier,String,Augmentations) 
/**
     * Generates a missing <body> (which creates missing <head> when needed)
     */
private void forceStartBody() {
    final QName body = createQName("body");
    if (fReportErrors) {
        fErrorReporter.reportWarning("HTML2006", new Object[] { body.localpart });
    }
    forceStartElement(body, null, synthesizedAugs());
}
