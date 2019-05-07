// [dbarashev] This method violates the rule: rendering model knows (almost) 
// nothing about 
// specific rendering library (such as java.awt.*) and knows absolutely 
// nothing about 
// application framework (such as GanttGraphicArea) 
// I understand that it is nice to render coordinators with bold font and 
// linebreak. However, 
// there exist other ways of doing this 
/*
     * private void processText(int xInit, int yInit, String text) { String save =
     * text; boolean startNew = false; StringTokenizer st = new
     * StringTokenizer(text, "{}", true); while (st.hasMoreTokens()) { String
     * token = st.nextToken(); if (token.equals("{")) { startNew = true;
     * continue; } if (token.equals("}")) { startNew = false; continue; } if
     * (startNew) { Text t =
     * getPrimitiveContainer().getLayer(2).createText(xInit, yInit, token);
     * xInit += TextLengthCalculatorImpl.getTextLength(myModel
     * .getArea().getGraphics(), token); t.setFont(new Font(null, Font.BOLD,
     * 9)); continue; } getPrimitiveContainer().getLayer(2).createText(xInit,
     * yInit, token); xInit +=
     * TextLengthCalculatorImpl.getTextLength(myModel.getArea() .getGraphics(),
     * token); } }
     */
private void drawPreviousStateTask(GanttPreviousStateTask task, int row) {
    int topy = (row * getRowHeight()) + getRowHeight() - 8;
    int posX = myPosX;
    if (task.getStart().getTime().compareTo(myUnitStart) >= 0) {
        TaskLength deltaLength = myModel.getTaskManager().createLength(getChartModel().getTimeUnitStack().getDefaultTimeUnit(), myUnitStart, task.getStart().getTime());
        int deltaX = (int) (deltaLength.getLength(myCurrentUnit) * getChartModel().getBottomUnitWidth());
        posX += deltaX;
    }
    int duration = task.getEnd(myModel.getTaskManager().getCalendar()).diff(task.getStart());
    TaskLength tl = myModel.getTaskManager().createLength(duration);
    int length = (int) (tl.getLength(myCurrentUnit) * getChartModel().getBottomUnitWidth());
    Integer nextOrdNumber = (Integer) myActivity2ordinalNumber.get(task);
    GraphicPrimitiveContainer container = getPrimitiveContainer();
    Rectangle rect = container.createRectangle(posX, topy, length, 6);
    String style = "";
    if (task.isMilestone()) {
        style = "previousStateTask.milestone";
    } else if (task.hasNested()) {
        style = "previousStateTask.super";
        if (task.isAPart())
            style = "previousStateTask.super.apart";
    } else {
        style = "previousStateTask";
    }
    if (task.getState() == GanttPreviousStateTask.EARLIER)
        style = style + ".earlier";
    else if (task.getState() == GanttPreviousStateTask.LATER)
        style = style + ".later";
    rect.setStyle(style);
}
