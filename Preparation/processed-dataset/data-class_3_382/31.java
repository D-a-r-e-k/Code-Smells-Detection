void setUserData(Map<String, String> data) {
    if (data == null) {
        userData = Collections.<String, String>emptyMap();
    } else {
        userData = data;
    }
}
