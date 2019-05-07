public boolean equals(Object other) {
    if (other instanceof Type) {
        return super.equals(other) && ((DateTimeType) other).withTimeZone == withTimeZone;
    }
    return false;
}
