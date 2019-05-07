private void standartIndex(IRequest req, StringBuffer sb) {
    sb.append("<form action=/ADMIN method=post>");
    sb.append("send message: <input type=text name=msg><input type=submit value=send>");
    sb.append("<input type=hidden name=do value=sendmessage></form>");
    sb.append("<form action=/ADMIN method=post>");
    sb.append("send message: <input type=text name=msg><br>");
    sb.append(" to user: <input type=text name=username><input type=submit value=send>");
    sb.append("<input type=hidden name=do value=sendmessagetouser></form>");
    sb.append("<form action=/ADMIN method=post>");
    sb.append("send message: <input type=text name=msg><br>");
    sb.append(" to group: <input type=text name=groupname><input type=submit value=send>");
    sb.append("<input type=hidden name=do value=sendmessagetogroup></form>");
    sb.append("<form action=/ADMIN method=post>");
    sb.append("<input type=text name=usr>");
    sb.append("<input type=submit value=search&nbsp;User>");
    sb.append("<input type=hidden name=do value=searchuser></form>");
    sb.append("<form action=/ADMIN method=post>");
    sb.append("<input type=text name=group>");
    sb.append("<input type=submit value=search&nbsp;Group>");
    sb.append("<input type=hidden name=do value=searchgroup></form>");
    sb.append("<a href=/ADMIN?do=userlist>show&nbsp;userlist</a>&nbsp;|&nbsp;");
    sb.append("<a href=/ADMIN?do=banlist>show&nbsp;banlist</a>&nbsp;|&nbsp;");
    sb.append("<a href=/ADMIN?do=actionstorelist>show&nbsp;actionstorelist</a>&nbsp;|&nbsp;");
    sb.append("<a href=/ADMIN?do=grouplist>grouplist</a>&nbsp;|&nbsp;");
    sb.append("<a href=/ADMIN?do=configoverview>configoverview</a>&nbsp;|&nbsp;");
    sb.append("<a href=/ADMIN?do=shutdown>shutdown</a>");
}
