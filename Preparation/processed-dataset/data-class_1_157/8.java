/**
     * Main hook for the conversion process.
     * @param args
     */
public static void main(String[] args) {
    if (args.length > 0) {
        for (int x = 0; x < args.length; x++) {
            WebMacro converter = new WebMacro();
            converter.convert(args[x]);
        }
    } else {
        usage();
    }
}
