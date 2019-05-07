void method0() { 
/**
     * layout,attachParent,layoutLeaf,join,merge,offset and bridge
     * methods below were taken line by line from Moen's algorithm.
     *
     * Attempts to understand the above methods without reading the paper
     * first are strongly discouraged.
     *
     *  http://www.computer.org/software/so1990/s4021abs.htm
     *
     * */
public static final Object CELL_WRAPPER = new Object();
public static final int LEFT_TO_RIGHT = 0;
public static final int UP_TO_DOWN = 1;
public static final int DEFAULT_ORIENTATION = LEFT_TO_RIGHT;
JGraph jgraph;
protected int orientation;
protected int childParentDistance;
}
