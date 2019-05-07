protected void setSimpleVars(ColumnText org) {
    maxY = org.maxY;
    minY = org.minY;
    alignment = org.alignment;
    leftWall = null;
    if (org.leftWall != null)
        leftWall = new ArrayList<float[]>(org.leftWall);
    rightWall = null;
    if (org.rightWall != null)
        rightWall = new ArrayList<float[]>(org.rightWall);
    yLine = org.yLine;
    currentLeading = org.currentLeading;
    fixedLeading = org.fixedLeading;
    multipliedLeading = org.multipliedLeading;
    canvas = org.canvas;
    canvases = org.canvases;
    lineStatus = org.lineStatus;
    indent = org.indent;
    followingIndent = org.followingIndent;
    rightIndent = org.rightIndent;
    extraParagraphSpace = org.extraParagraphSpace;
    rectangularWidth = org.rectangularWidth;
    rectangularMode = org.rectangularMode;
    spaceCharRatio = org.spaceCharRatio;
    lastWasNewline = org.lastWasNewline;
    linesWritten = org.linesWritten;
    arabicOptions = org.arabicOptions;
    runDirection = org.runDirection;
    descender = org.descender;
    composite = org.composite;
    splittedRow = org.splittedRow;
    if (org.composite) {
        compositeElements = new LinkedList<Element>(org.compositeElements);
        if (splittedRow) {
            PdfPTable table = (PdfPTable) compositeElements.getFirst();
            compositeElements.set(0, new PdfPTable(table));
        }
        if (org.compositeColumn != null)
            compositeColumn = duplicate(org.compositeColumn);
    }
    listIdx = org.listIdx;
    firstLineY = org.firstLineY;
    leftX = org.leftX;
    rightX = org.rightX;
    firstLineYDone = org.firstLineYDone;
    waitPhrase = org.waitPhrase;
    useAscender = org.useAscender;
    filledWidth = org.filledWidth;
    adjustFirstLine = org.adjustFirstLine;
}
