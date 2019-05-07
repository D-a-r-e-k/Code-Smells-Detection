/**
	 * This method serialize the object with the default level of detail, that
	 * removes private and hidden attributes
	 *
	 * @param out
	 *            the output serializer
	 */
@Override
public void writeObject(marauroa.common.net.OutputSerializer out) throws java.io.IOException {
    try {
        writeObject(out, DetailLevel.NORMAL);
    } catch (NullPointerException e) {
        logger.warn(this, e);
        throw e;
    }
}
