/**
     * Reads a paint style attribute. This can be a Color or a Gradient or null.
     * XXX - Doesn't support url(...) colors yet.
     */
private Object toPaint(IXMLElement elem, String value) throws IOException {
    String str = value;
    if (str == null) {
        return null;
    }
    str = str.trim().toLowerCase();
    if (str.equals("none")) {
        return null;
    } else if (str.equals("currentcolor")) {
        String currentColor = readInheritAttribute(elem, "color", "black");
        if (currentColor == null || currentColor.trim().toLowerCase().equals("currentColor")) {
            return null;
        } else {
            return toPaint(elem, currentColor);
        }
    } else if (SVG_COLORS.containsKey(str)) {
        return SVG_COLORS.get(str);
    } else if (str.startsWith("#") && str.length() == 7) {
        return new Color(Integer.decode(str));
    } else if (str.startsWith("#") && str.length() == 4) {
        // Three digits hex value 
        int th = Integer.decode(str);
        return new Color((th & 0xf) | ((th & 0xf) << 4) | ((th & 0xf0) << 4) | ((th & 0xf0) << 8) | ((th & 0xf00) << 8) | ((th & 0xf00) << 12));
    } else if (str.startsWith("rgb")) {
        try {
            StringTokenizer tt = new StringTokenizer(str, "() ,");
            tt.nextToken();
            String r = tt.nextToken();
            String g = tt.nextToken();
            String b = tt.nextToken();
            Color c = new Color(r.endsWith("%") ? (int) (Double.parseDouble(r.substring(0, r.length() - 1)) * 2.55) : Integer.decode(r), g.endsWith("%") ? (int) (Double.parseDouble(g.substring(0, g.length() - 1)) * 2.55) : Integer.decode(g), b.endsWith("%") ? (int) (Double.parseDouble(b.substring(0, b.length() - 1)) * 2.55) : Integer.decode(b));
            return c;
        } catch (Exception e) {
            /*if (DEBUG)*/
            System.out.println("SVGInputFormat.toPaint illegal RGB value " + str);
            e.printStackTrace();
            return null;
        }
    } else if (str.startsWith("url(")) {
        String href = value.substring(4, value.length() - 1);
        if (identifiedElements.containsKey(href.substring(1)) && elementObjects.containsKey(identifiedElements.get(href.substring(1)))) {
            Object obj = elementObjects.get(identifiedElements.get(href.substring(1)));
            return obj;
        }
        // XXX - Implement me 
        if (DEBUG) {
            System.out.println("SVGInputFormat.toPaint not implemented for " + href);
        }
        return null;
    } else {
        return null;
    }
}
