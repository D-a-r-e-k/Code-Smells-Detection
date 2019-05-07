public void setParser(Parser parser) {
    super.setParser(parser);
    if (_arguments != null) {
        final int n = _arguments.size();
        for (int i = 0; i < n; i++) {
            final Expression exp = (Expression) _arguments.elementAt(i);
            exp.setParser(parser);
            exp.setParent(this);
        }
    }
}
