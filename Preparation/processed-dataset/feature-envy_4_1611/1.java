public void process(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, Input input, Output output, PortletWindowInternal windowInfos, boolean isAction) throws PortletContainerException {
    long startTime = System.currentTimeMillis();
    log_.debug("process() method in PortletApplicationHandler entered");
    //objects to get using a pool 
    PortletObjectsWrapper portletObjectsWrapper = null;
    PortletSessionImp session = null;
    SharedSessionWrapper sharedSession = null;
    CustomRequestWrapper requestWrapper = null;
    CustomResponseWrapper responseWrapper = null;
    PortletRequestImp portletRequest = null;
    PortletResponseImp portletResponse = null;
    String portletAppName = windowInfos.getWindowID().getPortletApplicationName();
    String portletName = windowInfos.getWindowID().getPortletName();
    try {
        PortalContainer manager = PortalContainer.getInstance();
        PortletApplicationProxy proxy = (PortletApplicationProxy) manager.getComponentInstance(portletAppName);
        if (!holder.isModeSuported(portletAppName, portletName, input.getMarkup(), input.getPortletMode())) {
            throw new PortletContainerException("The portlet mode " + input.getPortletMode().toString() + " is not supported for the " + input.getMarkup() + " markup language.");
        }
        if (!holder.isStateSupported(input.getWindowState(), portletAppName)) {
            log_.debug("Window state : " + input.getWindowState() + " not supported, set the window state to normal");
            input.setWindowState(WindowState.NORMAL);
        }
        String exception_key = PortletContainerConstants.EXCEPTION + portletAppName + portletName;
        //PortletApplicationProxy portletApp = getPortletApplication(windowInfos.getPortletApplicationName()); 
        PortletContext portletContext = PortletAPIObjectFactory.getInstance().createPortletContext(servletContext);
        if (nbInstances > 0) {
            log_.debug("Extract objects from pool");
            try {
                portletObjectsWrapper = (PortletObjectsWrapper) portletObjectsWrapperPool.borrowObject();
            } catch (Exception e) {
                log_.error("Can not borrow object from pool", e);
                throw new PortletContainerException("Can not borrow object from pool", e);
            }
        } else {
            log_.debug("Create new object (no use of pool)");
            portletObjectsWrapper = PortletObjectsWrapperFactory.getInstance().createObject();
        }
        if (conf.isSharedSessionEnable()) {
            log_.debug("shared session enable");
            sharedSession = portletObjectsWrapper.getSharedSessionWrapper();
        }
        session = (PortletSessionImp) portletObjectsWrapper.getPortletSession();
        requestWrapper = portletObjectsWrapper.getCustomRequestWrapper();
        responseWrapper = portletObjectsWrapper.getCustomResponseWrapper();
        if (isAction) {
            portletRequest = (ActionRequestImp) portletObjectsWrapper.getActionRequest();
            portletResponse = (ActionResponseImp) portletObjectsWrapper.getActionResponse();
        } else {
            portletRequest = (RenderRequestImp) portletObjectsWrapper.getRenderRequest();
            portletResponse = (RenderResponseImp) portletObjectsWrapper.getRenderResponse();
        }
        long portletAppVersionNumber = 1;
        portletAppVersionNumber = monitor.getPortletVersionNumber(portletAppName);
        log_.debug("Get portlet version number : " + portletAppVersionNumber);
        //create a PortletSession object 
        if (conf.isSharedSessionEnable()) {
            sharedSession.fillSharedSessionWrapper(request.getSession(), portletAppName);
            sharedSession.init();
            session.fillPortletSession(sharedSession, portletContext, windowInfos.getWindowID().getUniqueID());
        } else {
            session.fillPortletSession(request.getSession(), portletContext, windowInfos.getWindowID().getUniqueID());
        }
        //create a servlet request wrapper 
        requestWrapper.fillCustomRequestWrapper(request, windowInfos.getWindowID().getUniqueID());
        //create a servlet response wrapper 
        responseWrapper.fillResponseWrapper(response);
        //create an ActionRequestImp object 
        portletRequest.fillPortletRequest(requestWrapper, portalContext, portletContext, session, holder.getPortletMetaData(portletAppName, portletName), input, windowInfos, holder.getPortletApplication(portletAppName).getSecurityConstraint(), holder.getPortletApplication(portletAppName).getUserAttribute(), holder.getPortletApplication(portletAppName).getCustomPortletMode(), holder.getPortletApplication(portletAppName).getCustomWindowState(), holder.getRoles(portletAppName), conf.getSupportedContent());
        //@todo sort the attributes 
        portletRequest.setAttribute(PortletRequest.USER_INFO, input.getUserAttributes());
        portletResponse.fillPortletResponse(responseWrapper, output, holder.getPortletApplication(portletAppName).getCustomWindowState());
        if (isAction) {
            ((ActionResponseImp) portletResponse).fillActionResponse(input, holder.getPortletMetaData(portletAppName, portletName));
        } else {
            ((RenderRequestImp) portletRequest).fillRenderRequest(((RenderInput) input).getRenderParameters(), ((RenderInput) input).isUpdateCache());
            ((RenderResponseImp) portletResponse).fillRenderResponse(windowInfos.getWindowID().getUniqueID(), input, holder.getPortletMetaData(portletAppName, portletName), request.isSecure(), conf.getSupportedContent(), Collections.enumeration(holder.getWindowStates(portletAppName)));
        }
        monitor.setLastAccessTime(portletAppName, portletName, startTime);
        boolean isBroken = monitor.isBroken(portletAppName, portletName);
        boolean isAvailable = monitor.isAvailable(portletAppName, portletName, startTime);
        boolean isDestroyed = monitor.isDestroyed(portletAppName, portletName);
        if (isDestroyed) {
            log_.debug("Portlet is destroyed");
            generateOutputForException(portletRequest, isAction, null, output);
            return;
        } else if (isBroken || !isAvailable || portletRequest.getAttribute(exception_key) != null) {
            log_.debug("Portlet is borken, not available or the request contains an associated error");
            generateOutputForException(portletRequest, isAction, exception_key, output);
            return;
        } else {
            Portlet portlet = null;
            try {
                portlet = proxy.getPortlet(portletContext, portletName);
            } catch (PortletException e) {
                log_.error("unable to get portlet :  " + portletName, e);
                portletRequest.setAttribute(exception_key, e);
                generateOutputForException(portletRequest, isAction, exception_key, output);
                return;
            }
            try {
                if (isAction) {
                    portlet.processAction((ActionRequest) portletRequest, (ActionResponse) portletResponse);
                    if (((ActionResponseImp) portletResponse).isSendRedirectAlreadyOccured()) {
                        String location = ((ActionResponseImp) portletResponse).getLocation();
                        log_.debug("need to redirect to " + location);
                        output.addProperty(Output.SEND_REDIRECT, location);
                    }
                } else {
                    portlet.render((RenderRequest) portletRequest, (RenderResponse) portletResponse);
                    if (((RenderInput) input).getTitle() != null) {
                        log_.debug("overide default title");
                        ((RenderOutput) output).setTitle(((RenderInput) input).getTitle());
                    }
                }
            } catch (Throwable t) {
                log_.error("exception returned by processAction() or render() methods", t);
                monitor.setLastFailureAccessTime(portletAppName, portletName, startTime);
                if (t instanceof PortletException) {
                    log_.debug("It is a portlet exception");
                    PortletException e = (PortletException) t;
                    if (t instanceof UnavailableException) {
                        log_.debug("It is an unavailable exception");
                        UnavailableException ex = (UnavailableException) e;
                        if (!ex.isPermanent()) {
                            log_.debug("but a non permanent one");
                            monitor.setUnavailabilityPeriod(portletAppName, portletName, ex.getUnavailableSeconds());
                        } else {
                            log_.debug("a permanent one, so destroy the portlet and borke it");
                            proxy.destroy(portletName);
                            monitor.brokePortlet(portletAppName, portletName);
                        }
                    }
                    portletRequest.setAttribute(exception_key, e);
                    generateOutputForException(portletRequest, isAction, exception_key, output);
                    return;
                }
                log_.debug("It is not a portlet exception, borke the portlet");
                monitor.brokePortlet(portletAppName, portletName);
                proxy.destroy(portletName);
                portletRequest.setAttribute(exception_key, t);
                generateOutputForException(portletRequest, isAction, exception_key, output);
                return;
            }
        }
    } finally {
        if (nbInstances > 0) {
            try {
                portletObjectsWrapperPool.returnObject(portletObjectsWrapper);
            } catch (Exception e) {
                log_.error("Can not return object to pool", e);
                throw new PortletContainerException("Can not return object to pool", e);
            }
        }
        long endTime = System.currentTimeMillis();
        PortletRuntimeDatasImpl rtd = monitor.getPortletRuntimeData(portletAppName, portletName);
        if (rtd != null) {
            //should fix this later ,  this can happen if the portlet is broken 
            if (isAction) {
                rtd.logProcessActionRequest(startTime, endTime);
            } else {
                boolean cacheHit = ((RenderOutput) output).isCacheHit();
                rtd.logRenderRequest(startTime, endTime, cacheHit);
            }
        }
    }
}
