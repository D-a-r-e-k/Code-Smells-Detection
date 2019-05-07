public void actionPerformed(ActionEvent evt) {
    JEditTextArea textArea = getTextArea(evt);
    if (textArea.getFirstLine() > 0)
        textArea.setFirstLine(textArea.getFirstLine() - 1);
}
