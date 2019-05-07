public void args(String[] argv) throws IOException {
    if (argv.length != 2) {
        System.err.println("usage: java fit.FileRunner input-file output-file");
        System.exit(-1);
    }
    File in = new File(argv[0]);
    File out = new File(argv[1]);
    fixture.summary.put("input file", in.getAbsolutePath());
    fixture.summary.put("input update", new Date(in.lastModified()));
    fixture.summary.put("output file", out.getAbsolutePath());
    input = read(in);
    output = new PrintWriter(new BufferedWriter(new FileWriter(out)));
}
