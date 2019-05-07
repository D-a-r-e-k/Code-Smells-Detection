// @compat 0.9.x 
private String mangle(String id) {
    int index = id.lastIndexOf('.');
    if (index == -1) {
        return id;
    } else {
        return id.substring(0, index + 1) + id.substring(index + 1, index + 2).toLowerCase(Locale.US) + id.substring(index + 2);
    }
}
