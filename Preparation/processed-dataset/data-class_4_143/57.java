/**
   * Close a {@link JextFrame} by calling {@link JextFrame#closeToQuit()} and if it it the last window and we are not keeping the server open
   * we close Jext completely.
   */
public static void closeToQuit(JextFrame frame) {
    closeToQuit(frame, false);
}
