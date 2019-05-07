/**
     * Checks the status variable and looks if there's still some text.
     */
public static boolean hasMoreText(int status) {
    return (status & ColumnText.NO_MORE_TEXT) == 0;
}
