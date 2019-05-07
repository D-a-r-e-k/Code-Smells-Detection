/**
	 * The handle method is the entry method into the browse controller.
	 */
@Override
protected ModelAndView handle(HttpServletRequest req, HttpServletResponse res, Object comm, BindException errors) throws Exception {
    // Cast the command to the correct command type.  
    BrowseCommand command = (BrowseCommand) comm;
    // Build a command with the items from the URL.  
    String base = req.getContextPath() + req.getServletPath();
    String line = req.getRequestURI().substring(base.length());
    Matcher matcher = p.matcher(line);
    if (matcher.matches()) {
        command.setHrOid(Long.parseLong(matcher.group(1)));
        command.setResource(matcher.group(2));
    }
    if (req.getQueryString() != null) {
        command.setResource(command.getResource() + "?" + req.getQueryString());
    }
    // Check if the command is prefixed with a forward slash.  
    if (command.getResource().startsWith("/")) {
        command.setResource(command.getResource().substring(1));
    }
    // Now make sure that the domain name is in lowercase.  
    Pattern urlBreakerPattern = Pattern.compile("(.*?)://(.*?)/(.*)");
    Matcher urlBreakerMatcher = urlBreakerPattern.matcher(command.getResource());
    if (urlBreakerMatcher.matches()) {
        command.setResource(urlBreakerMatcher.group(1) + "://" + urlBreakerMatcher.group(2).toLowerCase() + "/" + urlBreakerMatcher.group(3));
    }
    // Load the HarvestResourceDTO from the quality review facade.  
    HarvestResourceDTO dto = qualityReviewFacade.getHarvestResourceDTO(command.getHrOid(), command.getResource());
    // If the resource is not found, go to an error page.  
    if (dto == null) {
        log.debug("Resource not found: " + command.getResource());
        return new ModelAndView("browse-tool-not-found", "resourceName", command.getResource());
    } else {
        Header[] headers = qualityReviewFacade.getHttpHeaders(dto);
        // Send the headers for a redirect.  
        if (dto.getStatusCode() == HttpServletResponse.SC_MOVED_TEMPORARILY || dto.getStatusCode() == HttpServletResponse.SC_MOVED_PERMANENTLY) {
            res.setStatus(dto.getStatusCode());
            String location = getHeaderValue(headers, "Location");
            if (location != null) {
                String newUrl = browseHelper.convertUrl(command.getHrOid(), command.getResource(), location);
                res.setHeader("Location", newUrl);
            }
        }
        // Get the content type.  
        String realContentType = getHeaderValue(headers, "Content-Type");
        String simpleContentType = this.getSimpleContentType(realContentType);
        String charset = null;
        if (realContentType != null) {
            Matcher charsetMatcher = CHARSET_PATTERN.matcher(realContentType);
            if (charsetMatcher.find()) {
                charset = charsetMatcher.group(1);
                log.info("Desired charset: " + charset + " for " + command.getResource());
            } else {
                log.info("No charset: " + charset + " (" + command.getResource());
            }
        }
        // If the content has been registered with the browseHelper to  
        // require replacements, load the content and perform the   
        // necessary replacements.  
        if (browseHelper.isReplaceable(simpleContentType)) {
            StringBuilder content = readFile(dto, charset);
            // We might need to use a different base URL if a BASE HREF tag  
            // is used. We use the TagMagix class to perform the search.   
            // Note that TagMagix leaves leading/trailing slashes on the   
            // URL, so we need to do that   
            String baseUrl = command.getResource();
            Pattern baseUrlGetter = BrowseHelper.getTagMagixPattern("BASE", "HREF");
            Matcher m = baseUrlGetter.matcher(content);
            if (m.find()) {
                String u = m.group(1);
                if (u.startsWith("\"") && u.endsWith("\"") || u.startsWith("'") && u.endsWith("'")) {
                    // Ensure the detected Base HREF is not commented  
                    // out (unusual case, but we have seen it).					  
                    int lastEndComment = content.lastIndexOf("-->", m.start());
                    int lastStartComment = content.lastIndexOf("<!--", m.start());
                    if (lastStartComment < 0 || lastEndComment > lastStartComment) {
                        baseUrl = u.substring(1, u.length() - 1);
                    }
                }
            }
            browseHelper.fix(content, simpleContentType, command.getHrOid(), baseUrl);
            ModelAndView mav = new ModelAndView("browse-tool-html");
            mav.addObject("content", content.toString());
            mav.addObject("Content-Type", realContentType);
            return mav;
        } else {
            if (dto.getLength() > MAX_MEMORY_SIZE) {
                Date dt = new Date();
                File f = qualityReviewFacade.getResource(dto);
                ModelAndView mav = new ModelAndView("browse-tool-other");
                mav.addObject("file", f);
                mav.addObject("contentType", realContentType);
                log.info("TIME TO GET RESOURCE(old): " + (new Date().getTime() - dt.getTime()));
                return mav;
            } else {
                Date dt = new Date();
                byte[] bytesBuffer = qualityReviewFacade.getSmallResource(dto);
                ModelAndView mav = new ModelAndView("browse-tool-other-small");
                mav.addObject("bytesBuffer", bytesBuffer);
                mav.addObject("contentType", realContentType);
                log.info("TIME TO GET RESOURCE(new): " + (new Date().getTime() - dt.getTime()));
                return mav;
            }
        }
    }
}
