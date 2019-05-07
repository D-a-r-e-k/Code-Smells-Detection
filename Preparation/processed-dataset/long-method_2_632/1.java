@Override
protected ModelAndView processFormSubmission(HttpServletRequest aReq, HttpServletResponse aRes, Object aCommand, BindException aError) throws Exception {
    ModelAndView mav = null;
    CreateUserCommand userCmd = (CreateUserCommand) aCommand;
    if (userCmd != null) {
        if (aError.hasErrors()) {
            mav = new ModelAndView();
            List agencies = agencyUserManager.getAgenciesForLoggedInUser();
            mav.addObject(CreateUserCommand.MDL_AGENCIES, agencies);
            String mode = userCmd.getMode();
            if (CreateUserCommand.ACTION_EDIT.equals(mode)) {
                mav.addObject(CreateUserCommand.ACTION_EDIT, mode);
            }
            mav.addObject(Constants.GBL_CMD_DATA, aError.getTarget());
            mav.addObject(Constants.GBL_ERRORS, aError);
            mav.setViewName("newUser");
        } else if (CreateUserCommand.ACTION_NEW.equals(userCmd.getAction())) {
            mav = new ModelAndView();
            List agencies = agencyUserManager.getAgenciesForLoggedInUser();
            mav.addObject(CreateUserCommand.MDL_AGENCIES, agencies);
            mav.setViewName("newUser");
        } else if (CreateUserCommand.ACTION_VIEW.equals(userCmd.getAction()) || CreateUserCommand.ACTION_EDIT.equals(userCmd.getAction())) {
            //View/Edit an existing user  
            mav = new ModelAndView();
            Long userOid = userCmd.getOid();
            User user = agencyUserManager.getUserByOid(userOid);
            CreateUserCommand editCmd = new CreateUserCommand();
            editCmd.setOid(userOid);
            editCmd.setActive(user.isActive());
            editCmd.setAddress(user.getAddress());
            editCmd.setAgencyOid(user.getAgency().getOid());
            editCmd.setUsername(user.getUsername());
            editCmd.setFirstname(user.getFirstname());
            editCmd.setLastname(user.getLastname());
            editCmd.setTitle(user.getTitle());
            editCmd.setPhone(user.getPhone());
            editCmd.setEmail(user.getEmail());
            editCmd.setNotificationsByEmail(user.isNotificationsByEmail());
            editCmd.setTasksByEmail(user.isTasksByEmail());
            editCmd.setExternalAuth(user.isExternalAuth());
            editCmd.setNotifyOnGeneral(user.isNotifyOnGeneral());
            editCmd.setNotifyOnHarvestWarnings(user.isNotifyOnHarvestWarnings());
            editCmd.setMode(userCmd.getAction());
            List agencies = agencyUserManager.getAgenciesForLoggedInUser();
            mav.addObject(CreateUserCommand.MDL_AGENCIES, agencies);
            List assignedRoles = agencyUserManager.getAssociatedRolesForUser(userCmd.getOid());
            mav.addObject(CreateUserCommand.MDL_ASSIGNED_ROLES, assignedRoles);
            mav.addObject(Constants.GBL_CMD_DATA, editCmd);
            mav.setViewName("newUser");
        } else if (CreateUserCommand.ACTION_SAVE.equals(userCmd.getAction())) {
            try {
                User user = new User();
                boolean update = (userCmd.getOid() != null);
                if (update == true) {
                    // Update an existing user object by loading it in first  
                    user = agencyUserManager.getUserByOid(userCmd.getOid());
                } else {
                    //                  Save the newly created User object  
                    user.setActive(true);
                    //load Agency  
                    Long agencyOid = userCmd.getAgencyOid();
                    Agency agency = agencyUserManager.getAgencyByOid(agencyOid);
                    user.setAgency(agency);
                    user.setExternalAuth(userCmd.isExternalAuth());
                    //                  Only set the password for WCT Authenticating users  
                    if (userCmd.isExternalAuth() == false) {
                        String pwd = userCmd.getPassword();
                        String encodedPwd = passwordEncoder.encodePassword(pwd, saltSource.getSystemWideSalt());
                        user.setPassword(encodedPwd);
                        //                      force a password change only for WCT users, not LDAP users  
                        user.setForcePasswordChange(true);
                    }
                    user.setRoles(null);
                }
                user.setAddress(userCmd.getAddress());
                user.setEmail(userCmd.getEmail());
                user.setFirstname(userCmd.getFirstname());
                user.setLastname(userCmd.getLastname());
                user.setNotificationsByEmail(userCmd.isNotificationsByEmail());
                user.setTasksByEmail(userCmd.isTasksByEmail());
                user.setPhone(userCmd.getPhone());
                user.setTitle(userCmd.getTitle());
                user.setUsername(userCmd.getUsername());
                user.setNotifyOnGeneral(userCmd.isNotifyOnGeneral());
                user.setNotifyOnHarvestWarnings(userCmd.isNotifyOnHarvestWarnings());
                agencyUserManager.updateUser(user, update);
                List userDTOs = agencyUserManager.getUserDTOsForLoggedInUser();
                List agencies = null;
                if (authorityManager.hasPrivilege(Privilege.MANAGE_USERS, Privilege.SCOPE_ALL)) {
                    agencies = agencyUserManager.getAgencies();
                } else {
                    User loggedInUser = AuthUtil.getRemoteUserObject();
                    Agency usersAgency = loggedInUser.getAgency();
                    agencies = new ArrayList<Agency>();
                    agencies.add(usersAgency);
                }
                mav = new ModelAndView();
                String message;
                if (update == true) {
                    message = messageSource.getMessage("user.updated", new Object[] { userCmd.getUsername() }, Locale.getDefault());
                } else {
                    message = messageSource.getMessage("user.created", new Object[] { userCmd.getUsername() }, Locale.getDefault());
                }
                String agencyFilter = (String) aReq.getSession().getAttribute(UserCommand.MDL_AGENCYFILTER);
                if (agencyFilter == null) {
                    agencyFilter = AuthUtil.getRemoteUserObject().getAgency().getName();
                }
                mav.addObject(UserCommand.MDL_AGENCYFILTER, agencyFilter);
                mav.addObject(UserCommand.MDL_LOGGED_IN_USER, AuthUtil.getRemoteUserObject());
                mav.addObject(UserCommand.MDL_USERS, userDTOs);
                mav.addObject(UserCommand.MDL_AGENCIES, agencies);
                mav.addObject(Constants.GBL_MESSAGES, message);
                mav.setViewName("viewUsers");
            } catch (DataAccessException e) {
                mav = new ModelAndView();
                List agencies = agencyUserManager.getAgenciesForLoggedInUser();
                mav.addObject(CreateUserCommand.MDL_AGENCIES, agencies);
                String mode = userCmd.getMode();
                if (CreateUserCommand.ACTION_EDIT.equals(mode)) {
                    mav.addObject(CreateUserCommand.ACTION_EDIT, mode);
                }
                aError.reject("user.duplicate");
                mav.addObject(Constants.GBL_CMD_DATA, aError.getTarget());
                mav.addObject(Constants.GBL_ERRORS, aError);
                mav.setViewName("newUser");
            }
        }
    } else {
        log.warn("No Action provided for CreateUserController.");
    }
    return mav;
}
