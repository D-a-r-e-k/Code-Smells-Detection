public void setServlet(HttpServlet servlet) {
    mainServlet = servlet;
    servletContext = servlet.getServletContext();
}
