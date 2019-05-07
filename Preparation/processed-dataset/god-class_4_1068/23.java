private void processLastActivity(TaskActivity taskActivity) {
    if (taskActivity.getIntensity() != 0f) {
        processRegularActivity(taskActivity);
    }
    if (taskActivity.getTask().isMilestone()) {
        return;
    }
    createRightSideText(taskActivity);
    createDownSideText(taskActivity);
    createUpSideText(taskActivity);
    createLeftSideText(taskActivity);
}
