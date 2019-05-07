public static void ReInit() {
    generatedStates = 0;
    idCnt = 0;
    dummyStateIndex = -1;
    done = false;
    mark = null;
    stateDone = null;
    allStates.clear();
    indexedAllStates.clear();
    equivStatesTable.clear();
    allNextStates.clear();
    compositeStateTable.clear();
    stateBlockTable.clear();
    stateNameForComposite.clear();
    stateSetsToFix.clear();
}
