public void addArgument(String name, String value, String metadata) {
    this.getArguments().addArgument(new HTTPArgument(name, value, metadata));
}
