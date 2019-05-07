/**
     * Adds extra space.
     * This method should probably be rewritten.
     */
protected void addSpacing(float extraspace, float oldleading, Font f) {
    if (extraspace == 0)
        return;
    if (pageEmpty)
        return;
    if (currentHeight + line.height() + leading > indentTop() - indentBottom())
        return;
    leading = extraspace;
    carriageReturn();
    if (f.isUnderlined() || f.isStrikethru()) {
        f = new Font(f);
        int style = f.getStyle();
        style &= ~Font.UNDERLINE;
        style &= ~Font.STRIKETHRU;
        f.setStyle(style);
    }
    Chunk space = new Chunk(" ", f);
    space.process(this);
    carriageReturn();
    leading = oldleading;
}
