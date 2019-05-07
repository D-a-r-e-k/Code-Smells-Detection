private static boolean empty(String value) {
    if (value == null)
        return true;
    else if (value.trim().length() == 0)
        return true;
    return false;
}
