private void createUpSideText(TaskActivity taskActivity) {
    String text;
    text = getTaskLabel(taskActivity.getTask(), UP);
    if (text.length() > 0) {
        java.awt.Rectangle taskRectangle = myModel.getBoundingRectangle(taskActivity.getTask());
        int xOrigin = (int) taskRectangle.getMinX() + (int) taskRectangle.getWidth() / 2;
        int yOrigin = (int) taskRectangle.getMinY() - 3;
        Text textPrimitive = processText(xOrigin, yOrigin, text);
        textPrimitive.setAlignment(HAlignment.CENTER, VAlignment.BOTTOM);
    }
}
