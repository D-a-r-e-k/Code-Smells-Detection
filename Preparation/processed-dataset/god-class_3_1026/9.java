public void setDescription(String value) {
    mDescription = value.trim().replaceAll("\\.\\.\\.", ", ").replaceAll("\\n", " ");
}
