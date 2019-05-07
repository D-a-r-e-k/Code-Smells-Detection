/**
     * This function analyses an event and calls the right methods to take care
     * of the user's requests.
     *
     * @param event The incoming ActionEvent.
     */
public void actionPerformed(ActionEvent event) {
    String command = event.getActionCommand();
    if (OK.equals(command)) {
        getGUI().removeFromCanvas(this);
    } else {
        select(command);
    }
}
