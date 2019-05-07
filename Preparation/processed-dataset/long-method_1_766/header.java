void method0() { 
/**
   * limitation for 1K
   */
public static final int CLEARLIMITATION = 1024;
/**
   * The stack frame where all variables and params will be kept.
   * @serial
   */
XObject[] _stackFrames;
/**
   * The top of the stack frame (<code>_stackFrames</code>).
   * @serial
   */
int _frameTop;
/**
   * The bottom index of the current frame (relative to <code>_stackFrames</code>).
   * @serial
   */
private int _currentFrameBottom;
/**
   * The stack of frame positions.  I call 'em links because of distant
   * <a href="http://math.millikin.edu/mprogers/Courses/currentCourses/CS481-ComputerArchitecture/cs481.Motorola68000.html">
   * Motorola 68000 assembler</a> memories.  :-)
   * @serial
   */
int[] _links;
/**
   * The top of the links stack.
   */
int _linksTop;
/** NEEDSDOC Field m_nulls          */
private static XObject[] m_nulls = new XObject[CLEARLIMITATION];
}
