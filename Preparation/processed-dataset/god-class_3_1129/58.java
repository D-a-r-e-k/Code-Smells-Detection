/**
     * @return boolean that indicates body will be sent as is
     */
public boolean getPostBodyRaw() {
    return getPropertyAsBoolean(POST_BODY_RAW, POST_BODY_RAW_DEFAULT);
}
