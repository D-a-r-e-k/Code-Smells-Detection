/**
     * Returns a value as a String array.
     * The values are separated by whitespace or by commas with optional white
     * space.
     */
public static String[] toWSOrCommaSeparatedArray(String str) throws IOException {
    String[] result = str.split("(\\s*,\\s*|\\s+)");
    if (result.length == 1 && result[0].equals("")) {
        return new String[0];
    } else {
        return result;
    }
}
