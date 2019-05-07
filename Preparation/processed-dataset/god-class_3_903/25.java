public static int MoveFromSet(char c, List states, List newStates) {
    int tmp;
    int retVal = Integer.MAX_VALUE;
    for (int i = states.size(); i-- > 0; ) if (retVal > (tmp = ((NfaState) states.get(i)).MoveFrom(c, newStates)))
        retVal = tmp;
    return retVal;
}
