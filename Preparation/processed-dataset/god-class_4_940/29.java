private String renderTemplate(IRequest req, String name) {
    TemplateSet ts = Server.srv.templatemanager.getTemplateSet("admin");
    Template tpl = ts.getTemplate(name);
    if (tpl == null) {
        Server.log(this, "File " + name + " not loaded", Server.MSG_ERROR, Server.LVL_MAJOR);
        return "";
    }
    return tpl.render(req);
}
