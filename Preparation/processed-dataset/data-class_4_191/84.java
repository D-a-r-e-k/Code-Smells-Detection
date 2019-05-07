/**
   * This applies each MessageFilter in filters array on the given
   * MessageProxy objects.
   *
   * @return a Vector containing the removed MessageProxy objects.
   */
public Vector applyFilters(List messages, net.suberic.util.swing.ProgressDialog pd) {
    Vector notRemovedYet = new Vector(messages);
    Vector removed = new Vector();
    if (backendFilters != null)
        for (int i = 0; i < backendFilters.length; i++) {
            if (backendFilters[i] != null) {
                List justRemoved = backendFilters[i].filterMessages(notRemovedYet, pd);
                removed.addAll(justRemoved);
                notRemovedYet.removeAll(justRemoved);
            }
        }
    if (removed.size() > 0) {
        try {
            expunge();
        } catch (OperationCancelledException oce) {
        } catch (MessagingException me) {
            me.printStackTrace();
        }
    }
    return removed;
}
