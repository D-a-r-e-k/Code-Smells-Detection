@Override
protected void _close() {
    _defPane.requestFocusInWindow();
    if (_displayed)
        stopListening();
    super._close();
}
