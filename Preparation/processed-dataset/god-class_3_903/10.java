/** This function computes the closure and also updates the kind so that
     * any time there is a move to this state, it can go on epsilon to a
     * new state in the epsilon moves that might have a lower kind of token
     * number for the same length.
   */
private void EpsilonClosure() {
    int i = 0;
    if (closureDone || mark[id])
        return;
    mark[id] = true;
    // Recursively do closure 
    for (i = 0; i < epsilonMoves.size(); i++) ((NfaState) epsilonMoves.get(i)).EpsilonClosure();
    Enumeration e = epsilonMoves.elements();
    while (e.hasMoreElements()) {
        NfaState tmp = (NfaState) e.nextElement();
        for (i = 0; i < tmp.epsilonMoves.size(); i++) {
            NfaState tmp1 = (NfaState) tmp.epsilonMoves.get(i);
            if (tmp1.UsefulState() && !epsilonMoves.contains(tmp1)) {
                InsertInOrder(epsilonMoves, tmp1);
                done = false;
            }
        }
        if (kind > tmp.kind)
            kind = tmp.kind;
    }
    if (HasTransitions() && !epsilonMoves.contains(this))
        InsertInOrder(epsilonMoves, this);
}
