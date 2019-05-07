boolean isPageEmpty() {
    return writer == null || writer.getDirectContent().size() == 0 && writer.getDirectContentUnder().size() == 0 && (pageEmpty || writer.isPaused());
}
