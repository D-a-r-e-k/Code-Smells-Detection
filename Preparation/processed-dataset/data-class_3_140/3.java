/**
   * Returns the text area which was visible when the event has been
   * fired as the selected text area may have changed when event is received
   * due to the action of another listener.
   * @return A <code>JextTextArea</code> designating the selected text area when event
   * has been fired
   */
public JextTextArea getTextArea() {
    return textArea;
}
