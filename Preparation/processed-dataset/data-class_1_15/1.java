public static void main(String argv[]) {
    try {
        new FileRunner().run(argv);
    } catch (Exception e) {
        System.err.println(e.getMessage());
        System.exit(-1);
    }
}
