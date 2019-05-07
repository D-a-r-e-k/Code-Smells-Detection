/**
     * Handle job action.
     * @param handler CrawlJobHandler to operate on.
     * @param request Http request.
     * @param response Http response.
     * @param redirectBasePath Full path for where to go next if an error.
     * E.g. "/admin/jobs/per/overview.jsp".
     * @param currDomain Current domain.  Pass null for global domain.
     * @param reference 
     * @return The crawljob configured.
     * @throws IOException
     * @throws AttributeNotFoundException
     * @throws InvocationTargetException
     * @throws InvalidAttributeValueException
     */
public static CrawlJob handleJobAction(CrawlJobHandler handler, HttpServletRequest request, HttpServletResponse response, String redirectBasePath, String currDomain, String reference) throws IOException, AttributeNotFoundException, InvocationTargetException, InvalidAttributeValueException {
    // Load the job to manipulate 
    CrawlJob theJob = checkCrawlJob(handler.getJob(request.getParameter("job")), response, redirectBasePath, currDomain);
    XMLSettingsHandler settingsHandler = theJob.getSettingsHandler();
    // If currDomain is null, then we're at top-level. 
    CrawlerSettings settings = settingsHandler.getSettingsObject(currDomain);
    if (reference != null) {
        // refinement 
        Refinement refinement = settings.getRefinement(reference);
        settings = refinement.getSettings();
    }
    // See if we need to take any action 
    if (request.getParameter(ACTION) != null) {
        // Need to take some action. 
        String action = request.getParameter(ACTION);
        String subaction = request.getParameter(SUBACTION);
        if (action.equals(FILTERS)) {
            // Doing something with the filters. 
            String map = request.getParameter(MAP);
            if (map != null && map.length() > 0) {
                String filter = request.getParameter(FILTER);
                MapType filterMap = (MapType) settingsHandler.getComplexTypeByAbsoluteName(settings, map);
                if (subaction.equals(ADD)) {
                    // Add filter 
                    String className = request.getParameter(map + ".class");
                    String typeName = request.getParameter(map + ".name");
                    if (typeName != null && typeName.length() > 0 && className != null && className.length() > 0) {
                        ModuleType tmp = SettingsHandler.instantiateModuleTypeFromClassName(typeName, className);
                        filterMap.addElement(settings, tmp);
                    }
                } else if (subaction.equals(MOVEUP)) {
                    // Move a filter down in a map 
                    if (filter != null && filter.length() > 0) {
                        filterMap.moveElementUp(settings, filter);
                    }
                } else if (subaction.equals(MOVEDOWN)) {
                    // Move a filter up in a map 
                    if (filter != null && filter.length() > 0) {
                        filterMap.moveElementDown(settings, filter);
                    }
                } else if (subaction.equals(REMOVE)) {
                    // Remove a filter from a map 
                    if (filter != null && filter.length() > 0) {
                        filterMap.removeElement(settings, filter);
                    }
                }
            }
            // Finally save the changes to disk 
            settingsHandler.writeSettingsObject(settings);
        } else if (action.equals(DONE)) {
            // Ok, done editing. 
            if (subaction.equals(CONTINUE)) {
                // was editting an override/refinement, simply continue 
                if (theJob.isRunning()) {
                    handler.kickUpdate();
                }
                String overParam = ((currDomain != null && currDomain.length() > 0) ? "&currDomain=" + currDomain : "");
                String refParam = ((reference != null && reference.length() > 0) ? "&reference=" + reference : "");
                String messageParam = (refParam.length() > 0) ? "&message=Refinement changes saved" : "&message=Override changes saved";
                response.sendRedirect(redirectBasePath + "?job=" + theJob.getUID() + overParam + refParam + messageParam);
            } else {
                // on main, truly 'done' 
                if (theJob.isNew()) {
                    handler.addJob(theJob);
                    response.sendRedirect(redirectBasePath + "?message=Job created");
                } else {
                    if (theJob.isRunning()) {
                        handler.kickUpdate();
                    }
                    if (theJob.isProfile()) {
                        response.sendRedirect(redirectBasePath + "?message=Profile modified");
                    } else {
                        response.sendRedirect(redirectBasePath + "?message=Job modified");
                    }
                }
            }
        } else if (action.equals(GOTO)) {
            // Goto another page of the job/profile settings 
            String overParam = ((currDomain != null && currDomain.length() > 0) ? "&currDomain=" + currDomain : "");
            String refParam = ((reference != null && reference.length() > 0) ? "&reference=" + reference : "");
            response.sendRedirect(request.getParameter(SUBACTION) + overParam + refParam);
        }
    }
    return theJob;
}
