/**
	 * Creates and returns a copy of this object.
	 * @since 1.3.3
	 */
public Object clone() {
    Object object = null;
    try {
        object = super.clone();
        QuickServer _qs = (QuickServer) object;
        _qs.setQSAdminServer(new QSAdminServer(_qs));
    } catch (CloneNotSupportedException e) {
        logger.warning("Error cloning : " + e);
    }
    return object;
}
