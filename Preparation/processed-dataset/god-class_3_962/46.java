protected JPanel createStatusArea() {
    JPanel statusArea = new JPanel();
    JLabel status = new JLabel("No log records to display.");
    _statusLabel = status;
    status.setHorizontalAlignment(JLabel.LEFT);
    statusArea.setBorder(BorderFactory.createEtchedBorder());
    statusArea.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
    statusArea.add(status);
    return (statusArea);
}
