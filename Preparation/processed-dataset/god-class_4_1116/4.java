public void setSubType(int type) {
    exprSubType = type;
    if (exprSubType == OpTypes.ALL_QUANTIFIED || exprSubType == OpTypes.ANY_QUANTIFIED) {
        isQuantified = true;
    }
}
