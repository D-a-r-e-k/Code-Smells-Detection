protected void centerFrame(JFrame frame) {
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension comp = frame.getSize();
    frame.setLocation(((screen.width - comp.width) / 2), ((screen.height - comp.height) / 2));
}
