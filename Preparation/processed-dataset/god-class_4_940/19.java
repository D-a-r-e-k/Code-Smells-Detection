private void renderUserlist(IRequest req, StringBuffer sb) {
    sb.append("<b>FreeCS-Userlist</b><br><table class=mainTable>");
    User[] users = UserManager.mgr.ustr.toArray();
    for (int i = 0; i < users.length; i++) {
        User u = users[i];
        displayUser(u, sb);
    }
}
