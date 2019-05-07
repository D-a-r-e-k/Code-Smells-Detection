@Override
public void log(Request request, Response response, long time) {
    final String EMPTY = "";
    String remoteHost;
    if (resolveHosts)
        remoteHost = request.getRemoteHost();
    else
        remoteHost = request.getRemoteAddr();
    String user = request.getRemoteUser();
    String query = request.getRequestURI();
    long bytes = response.getContentCountLong();
    if (bytes < 0)
        bytes = 0;
    int status = response.getStatus();
    String virtualHost = EMPTY;
    String method = EMPTY;
    String referer = EMPTY;
    String userAgent = EMPTY;
    if (pattern.equals("combined")) {
        virtualHost = request.getServerName();
        method = request.getMethod();
        referer = request.getHeader("referer");
        userAgent = request.getHeader("user-agent");
    }
    synchronized (this) {
        int numberOfTries = 2;
        while (numberOfTries > 0) {
            try {
                open();
                ps.setString(1, remoteHost);
                ps.setString(2, user);
                ps.setTimestamp(3, new Timestamp(getCurrentTimeMillis()));
                ps.setString(4, query);
                ps.setInt(5, status);
                if (useLongContentLength) {
                    ps.setLong(6, bytes);
                } else {
                    if (bytes > Integer.MAX_VALUE)
                        bytes = -1;
                    ps.setInt(6, (int) bytes);
                }
                if (pattern.equals("combined")) {
                    ps.setString(7, virtualHost);
                    ps.setString(8, method);
                    ps.setString(9, referer);
                    ps.setString(10, userAgent);
                }
                ps.executeUpdate();
                return;
            } catch (SQLException e) {
                // Log the problem for posterity 
                container.getLogger().error(sm.getString("jdbcAccessLogValve.exception"), e);
                // Close the connection so that it gets reopened next time 
                if (conn != null)
                    close();
            }
            numberOfTries--;
        }
    }
}
