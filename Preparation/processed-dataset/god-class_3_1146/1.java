//  
// XSCMValidator methods  
//  
/**
     * check whether the given state is one of the final states
     *
     * @param state       the state to check
     *
     * @return whether it's a final state
     */
public boolean isFinalState(int state) {
    return (state < 0) ? false : fFinalStateFlags[state];
}
