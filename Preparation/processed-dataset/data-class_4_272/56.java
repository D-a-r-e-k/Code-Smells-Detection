/**
     * Reads a color style attribute. This can be a Color or null.
     * FIXME - Doesn't support url(...) colors yet.
     */
private Color toColor(IXMLElement elem, String value) throws IOException {
    String str = value;
    if (str == null) {
        return null;
    }
    str = str.trim().toLowerCase();
    if (str.equals("currentcolor")) {
        String currentColor = readInheritAttribute(elem, "color", "black");
        if (currentColor == null || currentColor.trim().toLowerCase().equals("currentColor")) {
            return null;
        } else {
            return toColor(elem, currentColor);
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
            Color c = new Color(r.endsWith("%") ? (int) (Integer.decode(r.substring(0, r.length() - 1)) * 2.55) : Integer.decode(r), g.endsWith("%") ? (int) (Integer.decode(g.substring(0, g.length() - 1)) * 2.55) : Integer.decode(g), b.endsWith("%") ? (int) (Integer.decode(b.substring(0, b.length() - 1)) * 2.55) : Integer.decode(b));
            return c;
        } catch (Exception e) {
            if (DEBUG) {
                System.out.println("SVGInputFormat.toColor illegal RGB value " + str);
            }
            return null;
        }
    } else if (str.startsWith("url")) {
        // FIXME - Implement me 
        if (DEBUG) {
            System.out.println("SVGInputFormat.toColor not implemented for " + str);
        }
        return null;
    } else {
        return null;
    }
}
