protected synchronized String getFiredTriggerRecordId() {
    return getInstanceId() + ftrCtr++;
}
