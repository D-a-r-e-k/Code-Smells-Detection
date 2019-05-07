/** Converts an ODG draw:transform attribute value into an AffineTransform.
     * <p>
     * The draw:transform attribute specifies a list of transformations that can be applied to a
     * drawing shape.
     * The value of this attribute is a list of transform definitions, which are applied to the drawing shape
     * in the order in which they are listed. The transform definitions in the list must be separated by a
     * white space and/or a comma. The types of transform definitions available include:
     * • matrix(<a> <b> <c> <d> <e> <f>), which specifies a transformation in the form of a
     * transformation matrix of six values. matrix(a,b,c,d,e,f) is the equivalent of applying the
     * transformation matrix [a b c d e f].
     * • translate(<tx> [<ty>]), which specifies a translation by tx and ty.
     * • scale(<sx> [<sy>]), which specifies a scale operation by sx and sy. If <sy> is not
     * provided, it is assumed to be equal to <sx>.
     * • rotate(<rotate-angle>), which specifies a rotation by <rotate-angle> about the
     * origin of the shapes coordinate system.
     * • skewX(<skew-angle>), which specifies a skew transformation along the X axis.
     * • skewY(<skew-angle>), which specifies a skew transformation along the Y axis.
     */
public static AffineTransform toTransform(String str) throws IOException {
    AffineTransform t = new AffineTransform();
    AffineTransform t2 = new AffineTransform();
    if (str != null) {
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
                t.preConcatenate(new AffineTransform(m));
            } else if (type.equals("translate")) {
                double tx, ty;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("X-translation value not found in transform " + str);
                }
                tx = tt.nval;
                if (tt.nextToken() == StreamPosTokenizer.TT_WORD) {
                    tx *= toUnitFactor(tt.sval);
                } else {
                    tt.pushBack();
                }
                if (tt.nextToken() == StreamPosTokenizer.TT_NUMBER) {
                    ty = tt.nval;
                    if (tt.nextToken() == StreamPosTokenizer.TT_WORD) {
                        ty *= toUnitFactor(tt.sval);
                    } else {
                        tt.pushBack();
                    }
                } else {
                    tt.pushBack();
                    ty = 0;
                }
                t2.setToIdentity();
                t2.translate(tx, ty);
                t.preConcatenate(t2);
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
                t2.setToIdentity();
                t2.scale(sx, sy);
                t.preConcatenate(t2);
            } else if (type.equals("rotate")) {
                double angle, cx, cy;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("Angle value not found in transform " + str);
                }
                angle = tt.nval;
                t2.setToIdentity();
                t2.rotate(-angle);
                t.preConcatenate(t2);
            } else if (type.equals("skewX")) {
                double angle;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("Skew angle not found in transform " + str);
                }
                angle = tt.nval;
                t.preConcatenate(new AffineTransform(1, 0, Math.tan(angle * Math.PI / 180), 1, 0, 0));
            } else if (type.equals("skewY")) {
                double angle;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("Skew angle not found in transform " + str);
                }
                angle = tt.nval;
                t.preConcatenate(new AffineTransform(1, Math.tan(angle * Math.PI / 180), 0, 1, 0, 0));
            } else {
                throw new IOException("Unknown transform " + type + " in " + str);
            }
            if (tt.nextToken() != ')') {
                throw new IOException("')' not found in transform " + str);
            }
        }
    }
    return t;
}
