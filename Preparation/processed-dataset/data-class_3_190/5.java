/**
   * Returns the message for this exception.
   */
public String getMessage() {
    if (message == null)
        return "Error changing value " + getProperty() + " to '" + getRejectedValue() + "':  " + getReason();
    else
        return message;
}
