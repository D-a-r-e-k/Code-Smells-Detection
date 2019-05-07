public String toString() {
    String result = CustomPermissionsURLClassLoader.class.getName() + " " + System.identityHashCode(this) + ":";
    URL[] urls = getURLs();
    for (URL url : urls) {
        result += "\n\t" + url.toString();
    }
    return result;
}
