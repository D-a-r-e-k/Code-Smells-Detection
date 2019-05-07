/**
     * Returns a value as a String array.
     * The values are separated by commas with optional white space.
     */
public static String[] toCommaSeparatedArray(String str) throws IOException {
    return str.split("\\s*,\\s*");
}
