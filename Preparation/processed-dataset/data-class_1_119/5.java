/**
     * Efficiency: O(N) implementation, try to take the next!
     */
public boolean hasNext() {
    if ((this.permutationCounter == 0) || (this.wasNextValueCalculatedAlready)) {
        return true;
    } else if (this.endWasReached) {
        return false;
    }
    boolean result = true;
    // calculate the next value into this.value  save the current result. in 
    // the end swap the arrays there is no way to know when to stop , but 
    // the out-of-bound 
    /*  try
         *  {
         *      this.wasNextValueCalculatedAlready=true;
         *      getNextStartingWith2();
         *  }
         *  catch (ArrayIndexOutOfBoundsException outOfBoundException)
         *  {
         *      endWasReached=true;
         *      result=false;
         *  }*/
    getNextStartingWith2();
    this.wasNextValueCalculatedAlready = true;
    if (endWasReached) {
        return false;
    }
    ////////////////////////////// 
    return result;
}
