NfaState CreateClone() {
    NfaState retVal = new NfaState();
    retVal.isFinal = isFinal;
    retVal.kind = kind;
    retVal.lookingFor = lookingFor;
    retVal.lexState = lexState;
    retVal.inNextOf = inNextOf;
    retVal.MergeMoves(this);
    return retVal;
}
