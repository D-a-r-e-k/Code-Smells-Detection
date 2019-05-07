private File makeAbsoluteToConfig(String fileName) {
    Assertion.affirm(fileName != null, "FileName can't be null");
    return ConfigReader.makeAbsoluteToConfig(fileName, getConfig());
}
