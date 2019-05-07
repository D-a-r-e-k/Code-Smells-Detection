private void createLeftSideText(TaskActivity taskActivity) {
    String text;
    text = getTaskLabel(taskActivity.getTask(), LEFT);
    if (text.length() > 0) {
        java.awt.Rectangle taskRectangle = myModel.getBoundingRectangle(taskActivity.getTask());
        int xOrigin = (int) taskRectangle.getMinX() - 9;
        int yOrigin = (int) (taskRectangle.getMaxY() - 3);
        Text textPrimitive = processText(xOrigin, yOrigin, text);
        textPrimitive.setAlignment(HAlignment.RIGHT, VAlignment.BOTTOM);
    }
}
