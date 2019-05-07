/**
     * Flattens all CSS styles.
     * Styles defined in a "style" attribute and in CSS rules are converted
     * into attributes with the same name.
     */
private void flattenStyles(IXMLElement elem) throws IOException {
    if (elem.getName() != null && elem.getName().equals("style") && readAttribute(elem, "type", "").equals("text/css") && elem.getContent() != null) {
        CSSParser cssParser = new CSSParser();
        cssParser.parse(elem.getContent(), styleManager);
    } else {
        if (elem.getNamespace() == null || elem.getNamespace().equals(SVG_NAMESPACE)) {
            String style = readAttribute(elem, "style", null);
            if (style != null) {
                for (String styleProperty : style.split(";")) {
                    String[] stylePropertyElements = styleProperty.split(":");
                    if (stylePropertyElements.length == 2 && !elem.hasAttribute(stylePropertyElements[0].trim(), SVG_NAMESPACE)) {
                        //if (DEBUG) System.out.println("flatten:"+Arrays.toString(stylePropertyElements)); 
                        elem.setAttribute(stylePropertyElements[0].trim(), SVG_NAMESPACE, stylePropertyElements[1].trim());
                    }
                }
            }
            styleManager.applyStylesTo(elem);
            for (IXMLElement node : elem.getChildren()) {
                if (node instanceof IXMLElement) {
                    IXMLElement child = (IXMLElement) node;
                    flattenStyles(child);
                }
            }
        }
    }
}
