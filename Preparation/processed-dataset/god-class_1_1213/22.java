// --------------------------------------------------------- Public Methods 
/**
     * This method is invoked by Tomcat on each query.
     * 
     * @param request The Request object.
     * @param response The Response object.
     *
     * @exception IOException Should not be thrown.
     * @exception ServletException Database SQLException is wrapped 
     * in a ServletException.
     */
@Override
public void invoke(Request request, Response response) throws IOException, ServletException {
    getNext().invoke(request, response);
    log(request, response, 0);
}
