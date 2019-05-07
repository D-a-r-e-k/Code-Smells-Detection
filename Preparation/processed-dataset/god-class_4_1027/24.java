protected static void fireEvent(ApplicationEvent appEvent) {
    Vector listeners = (Vector) mApplicationEventListeners.clone();
    for (int i = 0; i < listeners.size(); i++) {
        ((ApplicationEventListener) listeners.get(i)).handleApplicationEvent(appEvent);
    }
}
