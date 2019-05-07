/**
     * Returns a value as a EnhancedPath array.
     *
     * The draw:enhanced-path attribute specifies a path similar to the svg:d attribute of the
     * <svg:path> element. Instructions such as moveto, lineto, arcto and other instructions
     * together with its parameter are describing the geometry of a shape which can be filled and or
     * stroked. Relative commands are not supported.
     * The syntax of draw:enhanced-path attribute is as follows:
     * • Instructions are expressed as one character (e.g., a moveto is expressed as an M).
     * • A prefix notation is being used, that means that each command is followed by its parameter.
     * • Superfluous white space and separators such as commas can be eliminated. (e.g., “M 10 10
     * L 20 20 L 30 20” can also be written: “M10 10L20 20L30 20”
     * • If the command is repeated multiple times, only the first command is required. (e.g., “M 10 10
     * L 20 20 L 30 20” can also be expressed as followed “M 10 10 L 20 20 30 20”
     * • Floats can be used, therefore the only allowable decimal point is a dot (“.”)
     * The above mentioned rules are the same as specified for the <svg:path> element.
     * A parameter can also have one of the following enhancements:
     * • A “?” is used to mark the beginning of a formula name. The result of the element's
     * draw:formula attribute is used as parameter value in this case.
     * • If “$” is preceding a integer value, the value is indexing a draw:modifiers attribute. The
     * corresponding modifier value is used as parameter value then.
     *
     */
private EnhancedPath toEnhancedPath(String str) throws IOException {
    if (DEBUG) {
        System.out.println("ODGInputFormat toEnhancedPath " + str);
    }
    EnhancedPath path = null;
    Object x, y;
    Object x1, y1, x2, y2, x3, y3;
    StreamPosTokenizer tt = new StreamPosTokenizer(new StringReader(str));
    tt.resetSyntax();
    tt.parseNumbers();
    tt.parseExponents();
    tt.parsePlusAsNumber();
    tt.whitespaceChars(0, ' ');
    tt.whitespaceChars(',', ',');
    char nextCommand = 'M';
    char command = 'M';
    Commands: while (tt.nextToken() != StreamPosTokenizer.TT_EOF) {
        if (tt.ttype > 0) {
            command = (char) tt.ttype;
        } else {
            command = nextCommand;
            tt.pushBack();
        }
        nextCommand = command;
        switch(command) {
            case 'M':
                // moveto (x y)+ 
                // Start a new sub-path at the given (x,y) 
                // coordinate. If a moveto is followed by multiple 
                // pairs of coordinates, they are treated as lineto. 
                if (path == null) {
                    path = new EnhancedPath();
                }
                // path.setFilled(isFilled); 
                //path.setStroked(isStroked); 
                x = nextEnhancedCoordinate(tt, str);
                y = nextEnhancedCoordinate(tt, str);
                path.moveTo(x, y);
                nextCommand = 'L';
                break;
            case 'L':
                // lineto (x y)+ 
                // Draws a line from the current point to (x, y). If 
                // multiple coordinate pairs are following, they 
                // are all interpreted as lineto. 
                x = nextEnhancedCoordinate(tt, str);
                y = nextEnhancedCoordinate(tt, str);
                path.lineTo(x, y);
                break;
            case 'C':
                // curveto (x1 y1 x2 y2 x y)+ 
                // Draws a cubic Bézier curve from the current 
                // point to (x,y) using (x1,y1) as the control point 
                // at the beginning of the curve and (x2,y2) as 
                // the control point at the end of the curve. 
                x1 = nextEnhancedCoordinate(tt, str);
                y1 = nextEnhancedCoordinate(tt, str);
                x2 = nextEnhancedCoordinate(tt, str);
                y2 = nextEnhancedCoordinate(tt, str);
                x = nextEnhancedCoordinate(tt, str);
                y = nextEnhancedCoordinate(tt, str);
                path.curveTo(x1, y1, x2, y2, x, y);
                break;
            case 'Z':
                // closepath 
                // Close the current sub-path by drawing a 
                // straight line from the current point to current 
                // sub-path's initial point. 
                path.close();
                break;
            case 'N':
                // endpath 
                // Ends the current put of sub-paths. The sub- 
                // paths will be filled by using the “even-odd” 
                // filling rule. Other following subpaths will be 
                // filled independently. 
                break;
            case 'F':
                // nofill 
                // Specifies that the current put of sub-paths 
                // won't be filled. 
                break;
            case 'S':
                // nostroke 
                // Specifies that the current put of sub-paths 
                // won't be stroked. 
                break;
            case 'T':
                // angle-ellipseto (x y w h t0 t1) + 
                // Draws a segment of an ellipse. The ellipse is specified 
                // by the center(x, y), the size(w, h) and the start-angle 
                // t0 and end-angle t1. 
                x = nextEnhancedCoordinate(tt, str);
                y = nextEnhancedCoordinate(tt, str);
                x1 = nextEnhancedCoordinate(tt, str);
                y1 = nextEnhancedCoordinate(tt, str);
                x2 = nextEnhancedCoordinate(tt, str);
                y2 = nextEnhancedCoordinate(tt, str);
                path.ellipseTo(x, y, x1, y1, x2, y2);
                break;
            case 'U':
                // angle-ellipse (x y w h t0 t1) + 
                // The same as the “T” command, except that a implied moveto 
                // to the starting point is done. 
                x = nextEnhancedCoordinate(tt, str);
                y = nextEnhancedCoordinate(tt, str);
                x1 = nextEnhancedCoordinate(tt, str);
                y1 = nextEnhancedCoordinate(tt, str);
                x2 = nextEnhancedCoordinate(tt, str);
                y2 = nextEnhancedCoordinate(tt, str);
                path.moveTo(x1, y1);
                path.ellipseTo(x, y, x1, y1, x2, y2);
                break;
            case 'A':
                // arcto (x1 y1 x2 y2 x3 y3 x y) + 
                // (x1, y1) and (x2, y2) is defining the bounding 
                // box of a ellipse. A line is then drawn from the 
                // current point to the start angle of the arc that is 
                // specified by the radial vector of point (x3, y3) 
                // and then counter clockwise to the end-angle 
                // that is specified by point (x4, y4). 
                x1 = nextEnhancedCoordinate(tt, str);
                y1 = nextEnhancedCoordinate(tt, str);
                x2 = nextEnhancedCoordinate(tt, str);
                y2 = nextEnhancedCoordinate(tt, str);
                x3 = nextEnhancedCoordinate(tt, str);
                y3 = nextEnhancedCoordinate(tt, str);
                x = nextEnhancedCoordinate(tt, str);
                y = nextEnhancedCoordinate(tt, str);
                path.arcTo(x1, y1, x2, y2, x3, y3, x, y);
                break;
            case 'B':
                // arc (x1 y1 x2 y2 x3 y3 x y) + 
                // The same as the “A” command, except that a 
                // implied moveto to the starting point is done. 
                x1 = nextEnhancedCoordinate(tt, str);
                y1 = nextEnhancedCoordinate(tt, str);
                x2 = nextEnhancedCoordinate(tt, str);
                y2 = nextEnhancedCoordinate(tt, str);
                x3 = nextEnhancedCoordinate(tt, str);
                y3 = nextEnhancedCoordinate(tt, str);
                x = nextEnhancedCoordinate(tt, str);
                y = nextEnhancedCoordinate(tt, str);
                path.moveTo(x1, y1);
                path.arcTo(x1, y1, x2, y2, x3, y3, x, y);
                break;
            case 'W':
                // clockwisearcto (x1 y1 x2 y2 x3 y3 x y) + 
                // The same as the “A” command except, that the arc is drawn 
                // clockwise. 
                x1 = nextEnhancedCoordinate(tt, str);
                y1 = nextEnhancedCoordinate(tt, str);
                x2 = nextEnhancedCoordinate(tt, str);
                y2 = nextEnhancedCoordinate(tt, str);
                x3 = nextEnhancedCoordinate(tt, str);
                y3 = nextEnhancedCoordinate(tt, str);
                x = nextEnhancedCoordinate(tt, str);
                y = nextEnhancedCoordinate(tt, str);
                path.clockwiseArcTo(x1, y1, x2, y2, x3, y3, x, y);
                break;
            case 'V':
                // clockwisearc (x1 y1 x2 y2 x3 y3 x y)+ 
                // The same as the “A” command, except that a implied moveto 
                // to the starting point is done and the arc is drawn 
                // clockwise. 
                x1 = nextEnhancedCoordinate(tt, str);
                y1 = nextEnhancedCoordinate(tt, str);
                x2 = nextEnhancedCoordinate(tt, str);
                y2 = nextEnhancedCoordinate(tt, str);
                x3 = nextEnhancedCoordinate(tt, str);
                y3 = nextEnhancedCoordinate(tt, str);
                x = nextEnhancedCoordinate(tt, str);
                y = nextEnhancedCoordinate(tt, str);
                path.moveTo(x1, y1);
                path.clockwiseArcTo(x1, y1, x2, y2, x3, y3, x, y);
                break;
            case 'X':
                // elliptical-quadrantx (x y) + 
                // Draws a quarter ellipse, whose initial segment is 
                // tangential to the x-axis, is drawn from the 
                // current point to (x, y). 
                x = nextEnhancedCoordinate(tt, str);
                y = nextEnhancedCoordinate(tt, str);
                path.quadrantXTo(x, y);
                break;
            case 'Y':
                // elliptical-quadranty (x y) + 
                // Draws a quarter ellipse, whose initial segment is 
                // tangential to the y-axis, is drawn from the 
                // current point to(x, y). 
                x = nextEnhancedCoordinate(tt, str);
                y = nextEnhancedCoordinate(tt, str);
                path.quadrantYTo(x, y);
                break;
            case 'Q':
                // quadratic-curveto(x1 y1 x y)+ 
                // Draws a quadratic Bézier curve from the current point 
                // to(x, y) using(x1, y1) as the control point. (x, y) 
                // becomes the new current point at the end of the command. 
                x1 = nextEnhancedCoordinate(tt, str);
                y1 = nextEnhancedCoordinate(tt, str);
                x = nextEnhancedCoordinate(tt, str);
                y = nextEnhancedCoordinate(tt, str);
                path.quadTo(x1, y1, x, y);
                break;
            default:
                if (DEBUG) {
                    System.out.println("ODGInputFormat.toEnhancedPath aborting after illegal path command: " + command + " found in path " + str);
                }
                break Commands;
        }
    }
    return path;
}
