// generates code (without outputting it) and returns the name used. 
void GenerateCode() {
    if (stateName != -1)
        return;
    if (next != null) {
        next.GenerateCode();
        if (next.kind != Integer.MAX_VALUE)
            kindToPrint = next.kind;
    }
    if (stateName == -1 && HasTransitions()) {
        NfaState tmp = GetEquivalentRunTimeState();
        if (tmp != null) {
            stateName = tmp.stateName;
            //???? 
            //tmp.inNextOf += inNextOf; 
            //???? 
            dummy = true;
            return;
        }
        stateName = generatedStates++;
        indexedAllStates.add(this);
        GenerateNextStatesCode();
    }
}
