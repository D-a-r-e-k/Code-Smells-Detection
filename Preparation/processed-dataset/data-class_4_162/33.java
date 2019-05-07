/**
     *  Emits a processing instruction that will disable markup escaping. This is
     *  very useful if you want to emit HTML directly into the stream.
     *
     */
private void disableOutputEscaping() {
    addElement(new ProcessingInstruction(Result.PI_DISABLE_OUTPUT_ESCAPING, ""));
}
