public void updateMenuBar() {
    if (frame != null && frame.getJMenuBar() != null) {
        ((FreeColMenuBar) frame.getJMenuBar()).update();
    }
}
