void fireEvent(final RegistryChangeDataImpl data) {
    data.dump();
    if (listeners.isEmpty()) {
        return;
    }
    // make local copy  
    RegistryChangeListener[] arr = listeners.toArray(new RegistryChangeListener[listeners.size()]);
    data.beforeEventFire();
    if (log.isDebugEnabled()) {
        log.debug("propagating registry change event");
    }
    for (RegistryChangeListener element : arr) {
        element.registryChanged(data);
    }
    if (log.isDebugEnabled()) {
        log.debug("registry change event propagated");
    }
    data.afterEventFire();
}
