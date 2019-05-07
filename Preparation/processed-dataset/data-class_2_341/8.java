/**
   * Allocates memory (called a stackframe) on the stack; used to store
   * local variables and parameter arguments.
   *
   * <p>I use the link/unlink concept because of distant
   * <a href="http://math.millikin.edu/mprogers/Courses/currentCourses/CS481-ComputerArchitecture/cs481.Motorola68000.html">
   * Motorola 68000 assembler</a> memories.</p>
   *
   * @param size The size of the stack frame allocation.  This ammount should
   * normally be the maximum number of variables that you can have allocated
   * at one time in the new stack frame.
   *
   * @return The bottom of the stack frame, from where local variable addressing
   * should start from.
   */
public int link(final int size) {
    _currentFrameBottom = _frameTop;
    _frameTop += size;
    if (_frameTop >= _stackFrames.length) {
        XObject newsf[] = new XObject[_stackFrames.length + XPathContext.RECURSIONLIMIT + size];
        System.arraycopy(_stackFrames, 0, newsf, 0, _stackFrames.length);
        _stackFrames = newsf;
    }
    if (_linksTop + 1 >= _links.length) {
        int newlinks[] = new int[_links.length + (CLEARLIMITATION * 2)];
        System.arraycopy(_links, 0, newlinks, 0, _links.length);
        _links = newlinks;
    }
    _links[_linksTop++] = _currentFrameBottom;
    return _currentFrameBottom;
}
