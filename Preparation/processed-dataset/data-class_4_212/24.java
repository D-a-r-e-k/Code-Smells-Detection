/** @see TargetManager#isTargetUsingAQA(Long targetOid). */
public boolean isTargetUsingAQA(Long targetOid) {
    Target target = targetDao.load(targetOid, false);
    return target.isUseAQA();
}
