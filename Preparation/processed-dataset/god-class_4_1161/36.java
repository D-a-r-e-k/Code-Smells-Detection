/**
     * Handle REARRANGE_WORKERS property change events.
     *
     * @param event The <code>PropertyChangeEvent</code>.
     */
public void propertyChange(PropertyChangeEvent event) {
    logger.finest("Property change REARRANGE_WORKERS fired.");
    requestRearrange();
}
