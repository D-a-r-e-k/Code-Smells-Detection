protected void signalSchedulingChangeImmediately(long candidateNewNextFireTime) {
    schedSignaler.signalSchedulingChange(candidateNewNextFireTime);
}
