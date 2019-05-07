private Text processText(int xorigin, int yorigin, String text) {
    Text res = getPrimitiveContainer().getLayer(2).createText(xorigin, yorigin, text);
    res.setStyle("text.ganttinfo");
    return res;
}
