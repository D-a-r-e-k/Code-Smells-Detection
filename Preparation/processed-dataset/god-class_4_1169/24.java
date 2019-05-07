/** Declared as a getter in order to allow subclasses to override the standard behavior. */
protected InterpretResult.Visitor<Void> resultHandler() {
    return _handler;
}
