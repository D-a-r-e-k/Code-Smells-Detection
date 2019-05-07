// startContentModel():int[]  
// this method returns whether the last state was a valid final state  
public boolean endContentModel(int[] state) {
    final int curState = state[0];
    if (fFinalStateFlags[curState]) {
        if (fCountingStates != null) {
            Occurence o = fCountingStates[curState];
            if (o != null && state[2] < o.minOccurs) {
                // not enough loops on the current state to be considered final.  
                return false;
            }
        }
        return true;
    }
    return false;
}
