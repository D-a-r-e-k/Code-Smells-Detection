public void setName(String value) {
    if (mName != null && !mName.equals(value))
        mModified = true;
    mName = value;
}
