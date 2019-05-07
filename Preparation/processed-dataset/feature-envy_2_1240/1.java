public String Result() {
    String inputFileName = "../../spec/" + Location;
    String outputFileName = "output/spec/" + Location;
    try {
        FileRunner runner = new FileRunner();
        runner.args(new String[] { inputFileName, outputFileName });
        runner.process();
        runner.output.close();
        Counts counts = runner.fixture.counts;
        if ((counts.exceptions == 0) && (counts.wrong == 0)) {
            return "pass";
        } else {
            return "fail: " + counts.right + " right, " + counts.wrong + " wrong, " + counts.exceptions + " exceptions";
        }
    } catch (IOException e) {
        File inputFile = new File(inputFileName);
        String fileDescription;
        try {
            fileDescription = inputFile.getCanonicalPath();
        } catch (IOException e2) {
            fileDescription = inputFile.getAbsolutePath();
        }
        return "file not found: " + fileDescription;
    }
}
