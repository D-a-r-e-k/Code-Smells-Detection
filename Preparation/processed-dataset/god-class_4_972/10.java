/******************************************************************************/
/**
 * Runs the algorithm. First a running sequence is initialised. If 
 * shouldComputePermuation is <b><code>true</code><b> then a new
 * permutation is computed for every round, else a single determined sequence is
 * established. After this for every Cell a current impulse is calculated, 
 * position and temperature is updated. This is done, until the graph is frozen,
 * a maximum on rounds is reached or cancel on the progress dialog is pressed.
 * 
 * @return <b><code>true</code></b> when cancel on the progress dialog is 
 * pressed
 * @see #computeCurrentImpulse(CellView)
 * @see #createPermutation(int)
 * @see #isFrozen()
 * @see #updatePosAndTemp(CellView)
 */
private boolean calculate() {
    int length = applyCellList.size();
    int[] sequence = new int[length];
    boolean isCanceled = false;
    //case no permutation is desired, the series is computed one time only         
    if (!shouldComputePermutation)
        //else is in the loop below 
        for (int i = 0; i < length; i++) sequence[i] = i;
    while (!isFrozen() && countRounds <= maxRounds && (!isCanceled)) {
        //case permutation is desired, it's calculated every round 
        if (shouldComputePermutation)
            sequence = createPermutation(length);
        //loop over all nodes (order is in sequence) 
        for (int i = 0; i < sequence.length; i++) {
            CellView view = (CellView) applyCellList.get(sequence[i]);
            computeCurrentImpulse(view);
            //computes direction of impulse 
            updatePosAndTemp(view);
        }
        countRounds++;
    }
    return false;
}
