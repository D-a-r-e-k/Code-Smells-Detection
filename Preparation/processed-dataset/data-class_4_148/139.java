protected void signalSchedulingChangeOnTxCompletion(long candidateNewNextFireTime) {
    Long sigTime = sigChangeForTxCompletion.get();
    if (sigTime == null && candidateNewNextFireTime >= 0L)
        sigChangeForTxCompletion.set(candidateNewNextFireTime);
    else {
        if (candidateNewNextFireTime < sigTime)
            sigChangeForTxCompletion.set(candidateNewNextFireTime);
    }
}
