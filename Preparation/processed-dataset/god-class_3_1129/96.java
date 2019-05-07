/**
     * Determine if the HTTP status code is successful or not
     * i.e. in range 200 to 399 inclusive
     *
     * @return whether in range 200-399 or not
     */
protected boolean isSuccessCode(int code) {
    return (code >= 200 && code <= 399);
}
