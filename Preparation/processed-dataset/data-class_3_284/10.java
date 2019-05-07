/**
     * Really need a way to validate that this is called before using the
     * scanner, like Spring's init-method property.
     * For now, will just check explicitly before using.
     */
public void setRawLeadinPrompt(String rawLeadinPrompt) {
    this.rawLeadinPrompt = rawLeadinPrompt;
}
