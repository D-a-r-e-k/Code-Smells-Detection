@Override
protected ModelAndView processFormSubmission(HttpServletRequest aReq, HttpServletResponse aRes, Object aCmd, BindException aErrors) throws Exception {
    InTrayCommand intrayCmd = (InTrayCommand) aCmd;
    ModelAndView mav = null;
    // get value of page size cookie  
    String currentPageSize = CookieUtils.getPageSize(aReq);
    int taskPage = intrayCmd.getTaskPage();
    int notificationPage = intrayCmd.getNotificationPage();
    if (intrayCmd.getSelectedPageSize() != null) {
        if (!intrayCmd.getSelectedPageSize().equals(currentPageSize)) {
            // user has selected a new page size, so reset to first page..  
            currentPageSize = intrayCmd.getSelectedPageSize();
            CookieUtils.setPageSize(aRes, currentPageSize);
            taskPage = 0;
            notificationPage = 0;
        }
    }
    if (intrayCmd.getAction() != null) {
        int pageSize = Integer.parseInt(currentPageSize);
        if (InTrayCommand.ACTION_DELETE_NOTIFICATION.equals(intrayCmd.getAction())) {
            mav = deleteNotification(intrayCmd, pageSize);
        } else if (InTrayCommand.ACTION_VIEW_NOTIFICATION.equals(intrayCmd.getAction())) {
            mav = viewNotification(intrayCmd, pageSize);
        } else if (InTrayCommand.ACTION_DELETE_TASK.equals(intrayCmd.getAction())) {
            mav = deleteTask(intrayCmd, pageSize, aErrors);
        } else if (InTrayCommand.ACTION_VIEW_TASK.equals(intrayCmd.getAction())) {
            mav = viewTask(intrayCmd, pageSize);
        } else if (InTrayCommand.ACTION_CLAIM_TASK.equals(intrayCmd.getAction())) {
            mav = claimTask(intrayCmd, pageSize);
        } else if (InTrayCommand.ACTION_UNCLAIM_TASK.equals(intrayCmd.getAction())) {
            mav = unclaimTask(intrayCmd, pageSize);
        } else if (InTrayCommand.ACTION_NEXT.equals(intrayCmd.getAction()) || InTrayCommand.ACTION_PREVIOUS.equals(intrayCmd.getAction())) {
            mav = defaultView(taskPage, notificationPage, pageSize);
        } else if (InTrayCommand.ACTION_DELETE_ALL_NOTIFICATIONS.equals(intrayCmd.getAction())) {
            mav = deleteAllNotifications(intrayCmd, pageSize);
        }
    } else {
        //invalid action command so redirect to the view intray screen  
        log.warn("A form was posted to the InTrayController without a valid action attribute, redirecting to the showForm flow.");
        return showForm(aReq, aRes, aErrors);
    }
    return mav;
}
