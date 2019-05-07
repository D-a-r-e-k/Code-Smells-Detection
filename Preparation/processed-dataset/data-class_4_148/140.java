protected Long clearAndGetSignalSchedulingChangeOnTxCompletion() {
    Long t = sigChangeForTxCompletion.get();
    sigChangeForTxCompletion.set(null);
    return t;
}
