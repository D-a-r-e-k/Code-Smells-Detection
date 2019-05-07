protected void setFontSize(Component component, int fontSize) {
    Font oldFont = component.getFont();
    Font newFont = new Font(oldFont.getFontName(), oldFont.getStyle(), fontSize);
    component.setFont(newFont);
}
