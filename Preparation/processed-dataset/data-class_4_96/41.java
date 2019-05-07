/** 
	 * Returns client info. 
	 * @since 1.4.5
	 */
public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("{TheClient ");
    if (socket != null)
        sb.append(socket);
    else
        sb.append("no socket");
    sb.append(", Event: " + event);
    sb.append('}');
    return sb.toString();
}
