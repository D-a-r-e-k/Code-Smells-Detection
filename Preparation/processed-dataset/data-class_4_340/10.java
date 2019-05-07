/** Sets the given ConfigPanel as the visible panel. */
private void _displayPanel(ConfigPanel cf) {
    _mainPanel.removeAll();
    _mainPanel.add(cf, BorderLayout.CENTER);
    _mainPanel.revalidate();
    _mainPanel.repaint();
}
