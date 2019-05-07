public void addNonEncodedArgument(String name, String value, String metadata) {
    HTTPArgument arg = new HTTPArgument(name, value, metadata, false);
    arg.setAlwaysEncoded(false);
    this.getArguments().addArgument(arg);
}
