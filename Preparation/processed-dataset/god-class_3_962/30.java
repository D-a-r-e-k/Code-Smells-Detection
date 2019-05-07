/**
   * When the fontsize of a JTextArea is changed, the word-wrapped lines
   * may become garbled.  This method clears and resets the text of the
   * text area.
   */
protected void refresh(JTextArea textArea) {
    String text = textArea.getText();
    textArea.setText("");
    textArea.setText(text);
}
