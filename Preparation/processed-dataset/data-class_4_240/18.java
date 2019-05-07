private void processFirstActivity(TaskActivity taskActivity) {
    boolean stop = taskActivity.getIntensity() == 0f;
    if (!stop) {
        processRegularActivity(taskActivity);
    }
}
