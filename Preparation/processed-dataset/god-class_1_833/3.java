public String ErrorMessage() {
    try {
        loadFixture();
        return "(none)";
    } catch (Exception e) {
        return e.getMessage();
    }
}
