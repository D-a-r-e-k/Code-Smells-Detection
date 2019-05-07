@Override
protected ModelAndView processFormSubmission(HttpServletRequest aReq, HttpServletResponse aRes, Object aCmd, BindException aErrors) throws Exception {
    GeneratePermissionTemplateCommand tempCmd = (GeneratePermissionTemplateCommand) aCmd;
    User loggedInUser = AuthUtil.getRemoteUserObject();
    ModelAndView mav = new ModelAndView();
    if (tempCmd != null) {
        if (aErrors.hasErrors()) {
            mav.addObject(Constants.GBL_CMD_DATA, aErrors.getTarget());
            mav.addObject(Constants.GBL_ERRORS, aErrors);
            mav.setViewName("generate-request");
        } else if (GeneratePermissionTemplateCommand.ACTION_GENERATE_TEMPLATE.equals(tempCmd.getAction())) {
            Long templateOid = tempCmd.getTemplateOid();
            Long permissionOid = tempCmd.getPermissionOid();
            PermissionTemplate template = permissionTemplateManager.completeTemplate(templateOid, AuthUtil.getRemoteUserObject(), permissionOid);
            mav.addObject(GeneratePermissionTemplateCommand.MDL_TEMPLATE, template);
            mav.addObject(Constants.GBL_CMD_DATA, tempCmd);
            mav.setViewName("generate-request");
        } else if (GeneratePermissionTemplateCommand.ACTION_PRINTIT.equals(tempCmd.getAction())) {
            Long permissionOid = tempCmd.getPermissionOid();
            Permission perm = permissionTemplateManager.getPermission(permissionOid);
            // User chose to print a permission authorisation request  
            // so change permission's status to 'Requested'.  
            perm.setStatus(Permission.STATUS_REQUESTED);
            perm.setPermissionSentDate(new Date());
            perm.setDirty(true);
            perm.getSite().setPermissions(perm.getSite().getPermissions());
            // During the following siteManager.save(), Hibernate throws a HibernateException  
            // with the following message; 'Found two representations of same collection..'  
            // However the permission status appears to be saved OK anyway,   
            // so we'll just catch the exception and continue..  
            try {
                siteManager.save(perm.getSite());
            } catch (Exception e) {
            }
            mav.addObject(Constants.GBL_MESSAGES, messageSource.getMessage("site.print.success", new Object[] { null }, Locale.getDefault()));
            mav.addObject("permissions", perm.getSite().getPermissions());
            List templates = permissionTemplateManager.getTemplates(AuthUtil.getRemoteUserObject());
            mav.addObject(GeneratePermissionTemplateCommand.MDL_TEMPLATES, templates);
            mav.setViewName("select-permission");
        } else if (GeneratePermissionTemplateCommand.ACTION_SEND_EMAIL.equals(tempCmd.getAction())) {
            Long templateOid = tempCmd.getTemplateOid();
            Long permissionOid = tempCmd.getPermissionOid();
            Permission perm = permissionTemplateManager.getPermission(permissionOid);
            PermissionTemplate template = permissionTemplateManager.completeTemplate(templateOid, AuthUtil.getRemoteUserObject(), permissionOid);
            if (perm.getAuthorisingAgent().getEmail() != null) {
                if (!"".equals(perm.getAuthorisingAgent().getEmail().trim())) {
                    String recipientAddress = perm.getAuthorisingAgent().getEmail();
                    Mailable email = new Mailable();
                    if (template.getTemplateOverwriteFrom() && template.getTemplateFrom() != null && template.getTemplateFrom().length() > 0)
                        email.setSender(template.getTemplateFrom());
                    else
                        email.setSender(loggedInUser.getEmail());
                    if (template.getTemplateCc() != null && template.getTemplateCc().length() > 0)
                        email.setCcs(template.getTemplateCc());
                    if (template.getTemplateBcc() != null && template.getTemplateBcc().length() > 0)
                        email.setBccs(template.getTemplateBcc());
                    email.setRecipients(recipientAddress);
                    //email.setMessage(template.getTemplate());  
                    email.setMessage(template.getParsedText());
                    if (template.getTemplateSubject() != null && template.getTemplateSubject().length() > 0)
                        email.setSubject(template.getTemplateSubject());
                    else
                        email.setSubject("Web Curator Tool - Permission Request Email");
                    mailServer.send(email);
                    // A permission authorisation request email was sent to  
                    // the SMTP server OK, so change permission's status  
                    // to 'Requested'.  
                    perm.setStatus(Permission.STATUS_REQUESTED);
                    perm.setPermissionSentDate(new Date());
                    perm.setDirty(true);
                    perm.getSite().setPermissions(perm.getSite().getPermissions());
                    // During the following siteManager.save() Hibernate throws a HibernateException  
                    // with the following message; 'Found two representations of same collection..'  
                    // However the permission status appears to be saved OK anyway,   
                    // so we'll just catch the exception and continue..  
                    try {
                        siteManager.save(perm.getSite());
                    } catch (Exception e) {
                    }
                    mav.addObject(Constants.GBL_MESSAGES, messageSource.getMessage("site.email.success", new Object[] { recipientAddress }, Locale.getDefault()));
                } else {
                    //The email address is an empty string but not null  
                    mav.addObject(Constants.GBL_MESSAGES, messageSource.getMessage("site.invalid.email.for.template", new Object[] { template.getTemplateName() }, Locale.getDefault()));
                }
            } else {
                //The Harvest Authorisation object doesn't have a valid contact email address (it is null)  
                mav.addObject(Constants.GBL_MESSAGES, messageSource.getMessage("site.invalid.email.for.template", new Object[] { template.getTemplateName() }, Locale.getDefault()));
            }
            mav.addObject("permissions", perm.getSite().getPermissions());
            List templates = permissionTemplateManager.getTemplates(AuthUtil.getRemoteUserObject());
            mav.addObject(GeneratePermissionTemplateCommand.MDL_TEMPLATES, templates);
            mav.setViewName("select-permission");
        }
    }
    return mav;
}
