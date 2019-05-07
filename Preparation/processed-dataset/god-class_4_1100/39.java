/**
     * Returns a value as a BezierPath array.
     * as specified in http://www.w3.org/TR/SVGMobile12/paths.html#PathDataBNF
     *
     * Also supports elliptical arc commands 'a' and 'A' as specified in
     * http://www.w3.org/TR/SVG/paths.html#PathDataEllipticalArcCommands
     */
private BezierPath[] toPath(IXMLElement elem, String str) throws IOException {
    LinkedList<BezierPath> paths = new LinkedList<BezierPath>();
    BezierPath path = null;
    Point2D.Double p = new Point2D.Double();
    Point2D.Double c1 = new Point2D.Double();
    Point2D.Double c2 = new Point2D.Double();
    StreamPosTokenizer tt;
    if (toPathTokenizer == null) {
        tt = new StreamPosTokenizer(new StringReader(str));
        tt.resetSyntax();
        tt.parseNumbers();
        tt.parseExponents();
        tt.parsePlusAsNumber();
        tt.whitespaceChars(0, ' ');
        tt.whitespaceChars(',', ',');
        toPathTokenizer = tt;
    } else {
        tt = toPathTokenizer;
        tt.setReader(new StringReader(str));
    }
    char nextCommand = 'M';
    char command = 'M';
    Commands: while (tt.nextToken() != StreamPosTokenizer.TT_EOF) {
        if (tt.ttype > 0) {
            command = (char) tt.ttype;
        } else {
            command = nextCommand;
            tt.pushBack();
        }
        BezierPath.Node node;
        switch(command) {
            case 'M':
                // absolute-moveto x y 
                if (path != null) {
                    paths.add(path);
                }
                path = new BezierPath();
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("x coordinate missing for 'M' at position " + tt.getStartPosition() + " in " + str);
                }
                p.x = tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("y coordinate missing for 'M' at position " + tt.getStartPosition() + " in " + str);
                }
                p.y = tt.nval;
                path.moveTo(p.x, p.y);
                nextCommand = 'L';
                break;
            case 'm':
                // relative-moveto dx dy 
                if (path != null) {
                    paths.add(path);
                }
                path = new BezierPath();
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("dx coordinate missing for 'm' at position " + tt.getStartPosition() + " in " + str);
                }
                p.x += tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("dy coordinate missing for 'm' at position " + tt.getStartPosition() + " in " + str);
                }
                p.y += tt.nval;
                path.moveTo(p.x, p.y);
                nextCommand = 'l';
                break;
            case 'Z':
            case 'z':
                // close path 
                p.x = path.get(0).x[0];
                p.y = path.get(0).y[0];
                // If the last point and the first point are the same, we 
                // can merge them 
                if (path.size() > 1) {
                    BezierPath.Node first = path.get(0);
                    BezierPath.Node last = path.get(path.size() - 1);
                    if (first.x[0] == last.x[0] && first.y[0] == last.y[0]) {
                        if ((last.mask & BezierPath.C1_MASK) != 0) {
                            first.mask |= BezierPath.C1_MASK;
                            first.x[1] = last.x[1];
                            first.y[1] = last.y[1];
                        }
                        path.remove(path.size() - 1);
                    }
                }
                path.setClosed(true);
                break;
            case 'L':
                // absolute-lineto x y 
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("x coordinate missing for 'L' at position " + tt.getStartPosition() + " in " + str);
                }
                p.x = tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("y coordinate missing for 'L' at position " + tt.getStartPosition() + " in " + str);
                }
                p.y = tt.nval;
                path.lineTo(p.x, p.y);
                nextCommand = 'L';
                break;
            case 'l':
                // relative-lineto dx dy 
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("dx coordinate missing for 'l' at position " + tt.getStartPosition() + " in " + str);
                }
                p.x += tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("dy coordinate missing for 'l' at position " + tt.getStartPosition() + " in " + str);
                }
                p.y += tt.nval;
                path.lineTo(p.x, p.y);
                nextCommand = 'l';
                break;
            case 'H':
                // absolute-horizontal-lineto x 
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("x coordinate missing for 'H' at position " + tt.getStartPosition() + " in " + str);
                }
                p.x = tt.nval;
                path.lineTo(p.x, p.y);
                nextCommand = 'H';
                break;
            case 'h':
                // relative-horizontal-lineto dx 
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("dx coordinate missing for 'h' at position " + tt.getStartPosition() + " in " + str);
                }
                p.x += tt.nval;
                path.lineTo(p.x, p.y);
                nextCommand = 'h';
                break;
            case 'V':
                // absolute-vertical-lineto y 
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("y coordinate missing for 'V' at position " + tt.getStartPosition() + " in " + str);
                }
                p.y = tt.nval;
                path.lineTo(p.x, p.y);
                nextCommand = 'V';
                break;
            case 'v':
                // relative-vertical-lineto dy 
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("dy coordinate missing for 'v' at position " + tt.getStartPosition() + " in " + str);
                }
                p.y += tt.nval;
                path.lineTo(p.x, p.y);
                nextCommand = 'v';
                break;
            case 'C':
                // absolute-curveto x1 y1 x2 y2 x y 
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("x1 coordinate missing for 'C' at position " + tt.getStartPosition() + " in " + str);
                }
                c1.x = tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("y1 coordinate missing for 'C' at position " + tt.getStartPosition() + " in " + str);
                }
                c1.y = tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("x2 coordinate missing for 'C' at position " + tt.getStartPosition() + " in " + str);
                }
                c2.x = tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("y2 coordinate missing for 'C' at position " + tt.getStartPosition() + " in " + str);
                }
                c2.y = tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("x coordinate missing for 'C' at position " + tt.getStartPosition() + " in " + str);
                }
                p.x = tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("y coordinate missing for 'C' at position " + tt.getStartPosition() + " in " + str);
                }
                p.y = tt.nval;
                path.curveTo(c1.x, c1.y, c2.x, c2.y, p.x, p.y);
                nextCommand = 'C';
                break;
            case 'c':
                // relative-curveto dx1 dy1 dx2 dy2 dx dy 
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("dx1 coordinate missing for 'c' at position " + tt.getStartPosition() + " in " + str);
                }
                c1.x = p.x + tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("dy1 coordinate missing for 'c' at position " + tt.getStartPosition() + " in " + str);
                }
                c1.y = p.y + tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("dx2 coordinate missing for 'c' at position " + tt.getStartPosition() + " in " + str);
                }
                c2.x = p.x + tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("dy2 coordinate missing for 'c' at position " + tt.getStartPosition() + " in " + str);
                }
                c2.y = p.y + tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("dx coordinate missing for 'c' at position " + tt.getStartPosition() + " in " + str);
                }
                p.x += tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("dy coordinate missing for 'c' at position " + tt.getStartPosition() + " in " + str);
                }
                p.y += tt.nval;
                path.curveTo(c1.x, c1.y, c2.x, c2.y, p.x, p.y);
                nextCommand = 'c';
                break;
            case 'S':
                // absolute-shorthand-curveto x2 y2 x y 
                node = path.get(path.size() - 1);
                c1.x = node.x[0] * 2d - node.x[1];
                c1.y = node.y[0] * 2d - node.y[1];
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("x2 coordinate missing for 'S' at position " + tt.getStartPosition() + " in " + str);
                }
                c2.x = tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("y2 coordinate missing for 'S' at position " + tt.getStartPosition() + " in " + str);
                }
                c2.y = tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("x coordinate missing for 'S' at position " + tt.getStartPosition() + " in " + str);
                }
                p.x = tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("y coordinate missing for 'S' at position " + tt.getStartPosition() + " in " + str);
                }
                p.y = tt.nval;
                path.curveTo(c1.x, c1.y, c2.x, c2.y, p.x, p.y);
                nextCommand = 'S';
                break;
            case 's':
                // relative-shorthand-curveto dx2 dy2 dx dy 
                node = path.get(path.size() - 1);
                c1.x = node.x[0] * 2d - node.x[1];
                c1.y = node.y[0] * 2d - node.y[1];
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("dx2 coordinate missing for 's' at position " + tt.getStartPosition() + " in " + str);
                }
                c2.x = p.x + tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("dy2 coordinate missing for 's' at position " + tt.getStartPosition() + " in " + str);
                }
                c2.y = p.y + tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("dx coordinate missing for 's' at position " + tt.getStartPosition() + " in " + str);
                }
                p.x += tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("dy coordinate missing for 's' at position " + tt.getStartPosition() + " in " + str);
                }
                p.y += tt.nval;
                path.curveTo(c1.x, c1.y, c2.x, c2.y, p.x, p.y);
                nextCommand = 's';
                break;
            case 'Q':
                // absolute-quadto x1 y1 x y 
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("x1 coordinate missing for 'Q' at position " + tt.getStartPosition() + " in " + str);
                }
                c1.x = tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("y1 coordinate missing for 'Q' at position " + tt.getStartPosition() + " in " + str);
                }
                c1.y = tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("x coordinate missing for 'Q' at position " + tt.getStartPosition() + " in " + str);
                }
                p.x = tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("y coordinate missing for 'Q' at position " + tt.getStartPosition() + " in " + str);
                }
                p.y = tt.nval;
                path.quadTo(c1.x, c1.y, p.x, p.y);
                nextCommand = 'Q';
                break;
            case 'q':
                // relative-quadto dx1 dy1 dx dy 
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("dx1 coordinate missing for 'q' at position " + tt.getStartPosition() + " in " + str);
                }
                c1.x = p.x + tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("dy1 coordinate missing for 'q' at position " + tt.getStartPosition() + " in " + str);
                }
                c1.y = p.y + tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("dx coordinate missing for 'q' at position " + tt.getStartPosition() + " in " + str);
                }
                p.x += tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("dy coordinate missing for 'q' at position " + tt.getStartPosition() + " in " + str);
                }
                p.y += tt.nval;
                path.quadTo(c1.x, c1.y, p.x, p.y);
                nextCommand = 'q';
                break;
            case 'T':
                // absolute-shorthand-quadto x y 
                node = path.get(path.size() - 1);
                c1.x = node.x[0] * 2d - node.x[1];
                c1.y = node.y[0] * 2d - node.y[1];
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("x coordinate missing for 'T' at position " + tt.getStartPosition() + " in " + str);
                }
                p.x = tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("y coordinate missing for 'T' at position " + tt.getStartPosition() + " in " + str);
                }
                p.y = tt.nval;
                path.quadTo(c1.x, c1.y, p.x, p.y);
                nextCommand = 'T';
                break;
            case 't':
                // relative-shorthand-quadto dx dy 
                node = path.get(path.size() - 1);
                c1.x = node.x[0] * 2d - node.x[1];
                c1.y = node.y[0] * 2d - node.y[1];
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("dx coordinate missing for 't' at position " + tt.getStartPosition() + " in " + str);
                }
                p.x += tt.nval;
                if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                    throw new IOException("dy coordinate missing for 't' at position " + tt.getStartPosition() + " in " + str);
                }
                p.y += tt.nval;
                path.quadTo(c1.x, c1.y, p.x, p.y);
                nextCommand = 's';
                break;
            case 'A':
                {
                    // absolute-elliptical-arc rx ry x-axis-rotation large-arc-flag sweep-flag x y 
                    if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                        throw new IOException("rx coordinate missing for 'A' at position " + tt.getStartPosition() + " in " + str);
                    }
                    // If rX or rY have negative signs, these are dropped; 
                    // the absolute value is used instead. 
                    double rx = tt.nval;
                    if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                        throw new IOException("ry coordinate missing for 'A' at position " + tt.getStartPosition() + " in " + str);
                    }
                    double ry = tt.nval;
                    if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                        throw new IOException("x-axis-rotation missing for 'A' at position " + tt.getStartPosition() + " in " + str);
                    }
                    double xAxisRotation = tt.nval;
                    if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                        throw new IOException("large-arc-flag missing for 'A' at position " + tt.getStartPosition() + " in " + str);
                    }
                    boolean largeArcFlag = tt.nval != 0;
                    if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                        throw new IOException("sweep-flag missing for 'A' at position " + tt.getStartPosition() + " in " + str);
                    }
                    boolean sweepFlag = tt.nval != 0;
                    if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                        throw new IOException("x coordinate missing for 'A' at position " + tt.getStartPosition() + " in " + str);
                    }
                    p.x = tt.nval;
                    if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                        throw new IOException("y coordinate missing for 'A' at position " + tt.getStartPosition() + " in " + str);
                    }
                    p.y = tt.nval;
                    path.arcTo(rx, ry, xAxisRotation, largeArcFlag, sweepFlag, p.x, p.y);
                    nextCommand = 'A';
                    break;
                }
            case 'a':
                {
                    // absolute-elliptical-arc rx ry x-axis-rotation large-arc-flag sweep-flag x y 
                    if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                        throw new IOException("rx coordinate missing for 'A' at position " + tt.getStartPosition() + " in " + str);
                    }
                    // If rX or rY have negative signs, these are dropped; 
                    // the absolute value is used instead. 
                    double rx = tt.nval;
                    if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                        throw new IOException("ry coordinate missing for 'A' at position " + tt.getStartPosition() + " in " + str);
                    }
                    double ry = tt.nval;
                    if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                        throw new IOException("x-axis-rotation missing for 'A' at position " + tt.getStartPosition() + " in " + str);
                    }
                    double xAxisRotation = tt.nval;
                    if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                        throw new IOException("large-arc-flag missing for 'A' at position " + tt.getStartPosition() + " in " + str);
                    }
                    boolean largeArcFlag = tt.nval != 0;
                    if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                        throw new IOException("sweep-flag missing for 'A' at position " + tt.getStartPosition() + " in " + str);
                    }
                    boolean sweepFlag = tt.nval != 0;
                    if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                        throw new IOException("x coordinate missing for 'A' at position " + tt.getStartPosition() + " in " + str);
                    }
                    p.x += tt.nval;
                    if (tt.nextToken() != StreamPosTokenizer.TT_NUMBER) {
                        throw new IOException("y coordinate missing for 'A' at position " + tt.getStartPosition() + " in " + str);
                    }
                    p.y += tt.nval;
                    path.arcTo(rx, ry, xAxisRotation, largeArcFlag, sweepFlag, p.x, p.y);
                    nextCommand = 'a';
                    break;
                }
            default:
                if (DEBUG) {
                    System.out.println("SVGInputFormat.toPath aborting after illegal path command: " + command + " found in path " + str);
                }
                break Commands;
        }
    }
    if (path != null) {
        paths.add(path);
    }
    return paths.toArray(new BezierPath[paths.size()]);
}
