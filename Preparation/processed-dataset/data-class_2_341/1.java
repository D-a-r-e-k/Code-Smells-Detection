/**
   * Returns a clone of this variable stack.
   *
   * @return  a clone of this variable stack.
   *
   * @throws CloneNotSupportedException
   */
public synchronized Object clone() throws CloneNotSupportedException {
    VariableStack vs = (VariableStack) super.clone();
    // I *think* I can get away with a shallow clone here?  
    vs._stackFrames = (XObject[]) _stackFrames.clone();
    vs._links = (int[]) _links.clone();
    return vs;
}
