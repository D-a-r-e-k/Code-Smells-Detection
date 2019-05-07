private void createRightSideText(TaskActivity taskActivity) {
    java.awt.Rectangle bounds = getBoundingRectangle(taskActivity);
    String text = "";
    int xText, yText;
    text = getTaskLabel(taskActivity.getTask(), RIGHT);
    xText = (int) bounds.getMaxX() + 9;
    yText = (int) myModel.getBoundingRectangle(taskActivity.getTask()).getMaxY() - 3;
    Text textPrimitive = processText(xText, yText, text);
}
