/* Converts an SVG transform attribute value into an AffineTransform
     * as specified in
     * http://www.w3.org/TR/SVGMobile12/coords.html#TransformAttribute
     */
public static AffineTransform toTransform(IXMLElement elem, String str) throws IOException {
    AffineTransform t = new AffineTransform();
    if (str != null && !str.equals("none")) {
        StreamPosTokenizer tt = new StreamPosTokenizer(new StringReader(str));
        tt.resetSyntax();
        tt.wordChars('a', 'z');
        tt.wordChars('A', 'Z');
        tt.wordChars(128 + 32, 255);
        tt.whitespaceChars(0, ' ');
        tt.whitespaceChars(',', ',');
        tt.parseNumbers();
        tt.parseExponents();
        while (tt.nextToken() != StreamPosTokenizer.TT_EOF) {
            if (tt.ttype != StreamPosTokenizer.TT_WORD) {
                throw new IOException("Illegal transform " + str);
            }
            String type = tt.sval;
            if (tt.nextToken() != '(') {
                throw new IOException("'(' not found in transform " + str);
            }
            if (type.equals("matrix")) {
                double[] m = new double[6];
                for (int i = 0; i < 6; i++) {
                    if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                        throw new IOException("Matrix value " + i + " not found in transform " + str + " token:" + tt.ttype + " " + tt.sval);
                    }
                    m[i] = tt.nval;
                }
                t.concatenate(new AffineTransform(m));
            } else if (type.equals("translate")) {
                double tx, ty;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("X-translation value not found in transform " + str);
                }
                tx = tt.nval;
                if (tt.nextToken() == StreamPosTokenizer.TT_NUMBER) {
                    ty = tt.nval;
                } else {
                    tt.pushBack();
                    ty = 0;
                }
                t.translate(tx, ty);
            } else if (type.equals("scale")) {
                double sx, sy;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("X-scale value not found in transform " + str);
                }
                sx = tt.nval;
                if (tt.nextToken() == StreamPosTokenizer.TT_NUMBER) {
                    sy = tt.nval;
                } else {
                    tt.pushBack();
                    sy = sx;
                }
                t.scale(sx, sy);
            } else if (type.equals("rotate")) {
                double angle, cx, cy;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("Angle value not found in transform " + str);
                }
                angle = tt.nval;
                if (tt.nextToken() == StreamPosTokenizer.TT_NUMBER) {
                    cx = tt.nval;
                    if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                        throw new IOException("Y-center value not found in transform " + str);
                    }
                    cy = tt.nval;
                } else {
                    tt.pushBack();
                    cx = cy = 0;
                }
                t.rotate(angle * Math.PI / 180d, cx, cy);
            } else if (type.equals("skewX")) {
                double angle;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("Skew angle not found in transform " + str);
                }
                angle = tt.nval;
                t.concatenate(new AffineTransform(1, 0, Math.tan(angle * Math.PI / 180), 1, 0, 0));
            } else if (type.equals("skewY")) {
                double angle;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("Skew angle not found in transform " + str);
                }
                angle = tt.nval;
                t.concatenate(new AffineTransform(1, Math.tan(angle * Math.PI / 180), 0, 1, 0, 0));
            } else if (type.equals("ref")) {
                System.err.println("SVGInputFormat warning: ignored ref(...) transform attribute in element " + elem);
                while (tt.nextToken() != ')' && tt.ttype != StreamPosTokenizer.TT_EOF) {
                }
                tt.pushBack();
            } else {
                throw new IOException("Unknown transform " + type + " in " + str + " in element " + elem);
            }
            if (tt.nextToken() != ')') {
                throw new IOException("')' not found in transform " + str);
            }
        }
    }
    return t;
}
