// --------------------------------------------------------- Public Methods 
/**
     * Return a String rendering of this object.
     */
@Override
public String toString() {
    StringBuilder sb = new StringBuilder("SingleSignOnMessage[action=");
    sb.append(getAction()).append(", ssoId=").append(getSsoId());
    sb.append(", sessionId=").append(getSessionId()).append(", username=");
    sb.append(getUsername()).append("]");
    return (sb.toString());
}
