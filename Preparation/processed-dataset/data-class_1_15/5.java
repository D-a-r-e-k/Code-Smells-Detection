protected String read(File input) throws IOException {
    char chars[] = new char[(int) (input.length())];
    FileReader in = new FileReader(input);
    in.read(chars);
    in.close();
    return new String(chars);
}
