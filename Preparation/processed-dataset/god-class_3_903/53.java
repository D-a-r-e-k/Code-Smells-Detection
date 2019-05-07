private boolean selfLoop() {
    if (next == null || next.epsilonMovesString == null)
        return false;
    int[] set = (int[]) allNextStates.get(next.epsilonMovesString);
    return ElemOccurs(stateName, set) >= 0;
}
