public void add(PackageMetric pPackageMetric_) {
    if (pPackageMetric_ == null) {
        return;
    }
    classes += pPackageMetric_.classes;
    functions += pPackageMetric_.functions;
    ncss += pPackageMetric_.ncss;
    javadocs += pPackageMetric_.javadocs;
    javadocsLn += pPackageMetric_.javadocsLn;
    singleLn += pPackageMetric_.singleLn;
    multiLn += pPackageMetric_.multiLn;
}
