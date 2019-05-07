static void InsertInOrder(List v, NfaState s) {
    int j;
    for (j = 0; j < v.size(); j++) if (((NfaState) v.get(j)).id > s.id)
        break;
    else if (((NfaState) v.get(j)).id == s.id)
        return;
    v.add(j, s);
}
