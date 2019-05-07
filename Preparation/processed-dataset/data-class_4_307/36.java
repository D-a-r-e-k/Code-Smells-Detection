/** Root element specified. */
private final void rootElementSpecified(QName rootElement) throws XNIException {
    if (fPerformValidation) {
        String root1 = fRootElement.rawname;
        String root2 = rootElement.rawname;
        if (root1 == null || !root1.equals(root2)) {
            fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "RootElementTypeMustMatchDoctypedecl", new Object[] { root1, root2 }, XMLErrorReporter.SEVERITY_ERROR);
        }
    }
}
