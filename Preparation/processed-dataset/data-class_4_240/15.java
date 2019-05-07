private void processActivity(TaskActivity nextStarted) {
    if (nextStarted.isLast()) {
        processLastActivity(nextStarted);
    } else if (nextStarted.isFirst()) {
        processFirstActivity(nextStarted);
    } else {
        processRegularActivity(nextStarted);
    }
}
