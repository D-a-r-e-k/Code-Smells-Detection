protected long calcFailedIfAfter(SchedulerStateRecord rec) {
    return rec.getCheckinTimestamp() + Math.max(rec.getCheckinInterval(), (System.currentTimeMillis() - lastCheckin)) + 7500L;
}
