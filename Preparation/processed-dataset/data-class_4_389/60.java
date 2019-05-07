@Override
public void autoSetInternal() {
    int nInternal = (int) Math.ceil(weight / 10.0);
    // No internals in the body location.  
    initializeInternal(IArmorState.ARMOR_NA, LOC_BODY);
    for (int x = 1; x < locations(); x++) {
        initializeInternal(nInternal, x);
    }
}
