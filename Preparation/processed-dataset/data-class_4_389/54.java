@Override
public Vector<Report> victoryReport() {
    Vector<Report> vDesc = new Vector<Report>();
    Report r = new Report(7025);
    r.type = Report.PUBLIC;
    r.addDesc(this);
    vDesc.addElement(r);
    r = new Report(7035);
    r.type = Report.PUBLIC;
    r.newlines = 0;
    vDesc.addElement(r);
    vDesc.addAll(crew.getDescVector(false));
    r = new Report(7070, Report.PUBLIC);
    r.add(getKillNumber());
    vDesc.addElement(r);
    if (isDestroyed()) {
        Entity killer = game.getEntity(killerId);
        if (killer == null) {
            killer = game.getOutOfGameEntity(killerId);
        }
        if (killer != null) {
            r = new Report(7072, Report.PUBLIC);
            r.addDesc(killer);
        } else {
            r = new Report(7073, Report.PUBLIC);
        }
        vDesc.addElement(r);
    }
    r.newlines = 2;
    return vDesc;
}
