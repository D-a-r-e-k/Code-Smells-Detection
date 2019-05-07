private void createDownSideText(TaskActivity taskActivity) {
    String text;
    text = getTaskLabel(taskActivity.getTask(), DOWN);
    if (text.length() > 0) {
        java.awt.Rectangle taskRectangle = myModel.getBoundingRectangle(taskActivity.getTask());
        int xOrigin = (int) taskRectangle.getMinX() + (int) taskRectangle.getWidth() / 2;
        int yOrigin = (int) taskRectangle.getMaxY() + 2;
        Text textPrimitive = processText(xOrigin, yOrigin, text);
        textPrimitive.setAlignment(HAlignment.CENTER, VAlignment.TOP);
    }
}
