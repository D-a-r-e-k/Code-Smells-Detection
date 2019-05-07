/**
	 * Sets the debug flag to ByteBufferOutputStream and
	 * ByteBufferInputStream class that are used in non-blcking mode
	 * @since 1.4.5
	 */
public static void setDebugNonBlockingMode(boolean flag) {
    org.quickserver.util.io.ByteBufferOutputStream.setDebug(flag);
    org.quickserver.util.io.ByteBufferInputStream.setDebug(flag);
}
