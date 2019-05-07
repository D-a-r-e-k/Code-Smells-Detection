/**
     * Evaluates an SVG "switch" element.
     *
     */
private Figure readSwitchElement(IXMLElement elem) throws IOException {
    for (IXMLElement node : elem.getChildren()) {
        if (node instanceof IXMLElement) {
            IXMLElement child = (IXMLElement) node;
            String[] requiredFeatures = toWSOrCommaSeparatedArray(readAttribute(child, "requiredFeatures", ""));
            String[] requiredExtensions = toWSOrCommaSeparatedArray(readAttribute(child, "requiredExtensions", ""));
            String[] systemLanguage = toWSOrCommaSeparatedArray(readAttribute(child, "systemLanguage", ""));
            String[] requiredFormats = toWSOrCommaSeparatedArray(readAttribute(child, "requiredFormats", ""));
            String[] requiredFonts = toWSOrCommaSeparatedArray(readAttribute(child, "requiredFonts", ""));
            boolean isMatch;
            isMatch = supportedFeatures.containsAll(Arrays.asList(requiredFeatures)) && requiredExtensions.length == 0 && requiredFormats.length == 0 && requiredFonts.length == 0;
            if (isMatch && systemLanguage.length > 0) {
                isMatch = false;
                Locale locale = LocaleUtil.getDefault();
                for (String lng : systemLanguage) {
                    int p = lng.indexOf('-');
                    if (p == -1) {
                        if (locale.getLanguage().equals(lng)) {
                            isMatch = true;
                            break;
                        }
                    } else {
                        if (locale.getLanguage().equals(lng.substring(0, p)) && locale.getCountry().toLowerCase().equals(lng.substring(p + 1))) {
                            isMatch = true;
                            break;
                        }
                    }
                }
            }
            if (isMatch) {
                Figure figure = readElement(child);
                if (readAttribute(child, "visibility", "visible").equals("visible") && !readAttribute(child, "display", "inline").equals("none")) {
                    return figure;
                } else {
                    return null;
                }
            }
        }
    }
    return null;
}
