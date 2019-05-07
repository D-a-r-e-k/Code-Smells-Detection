/**
     * Sets the display duration for the page (for presentations)
     * @param seconds   the number of seconds to display the page
     */
void setDuration(int seconds) {
    if (seconds > 0)
        this.duration = seconds;
    else
        this.duration = -1;
}
