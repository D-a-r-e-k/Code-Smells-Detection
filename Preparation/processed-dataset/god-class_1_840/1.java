public String HTML() {
    Text = unescapeAscii(Text);
    return Fixture.escape(Text);
}
