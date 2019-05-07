NfaState CreateEquivState(List states) {
    NfaState newState = ((NfaState) states.get(0)).CreateClone();
    newState.next = new NfaState();
    InsertInOrder(newState.next.epsilonMoves, ((NfaState) states.get(0)).next);
    for (int i = 1; i < states.size(); i++) {
        NfaState tmp2 = ((NfaState) states.get(i));
        if (tmp2.kind < newState.kind)
            newState.kind = tmp2.kind;
        newState.isFinal |= tmp2.isFinal;
        InsertInOrder(newState.next.epsilonMoves, tmp2.next);
    }
    return newState;
}
