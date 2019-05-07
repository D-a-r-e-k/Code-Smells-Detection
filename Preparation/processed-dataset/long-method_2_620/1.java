public static void savePersistentValue(String name, String value, Date date, int ttl) {
    try {
        PersistentValue existingPersistentValue = PersistentValueManager.findByName(name);
        if (existingPersistentValue != null) {
            PersistentValue persistentValueCount = PersistentValueManager.findByName(name + ".count");
            if (persistentValueCount != null) {
                try {
                    PersistentValueManager.deletePersistentValue(existingPersistentValue);
                    int counter = Integer.parseInt(persistentValueCount.getValue());
                    for (int i = 2; i <= counter; i++) {
                        existingPersistentValue = PersistentValueManager.findByName(name + "." + i);
                        if (existingPersistentValue != null)
                            PersistentValueManager.deletePersistentValue(existingPersistentValue);
                    }
                    PersistentValueManager.deletePersistentValue(persistentValueCount);
                } catch (Exception ex) {
                    Tools.logException(Tools.class, ex, name);
                }
            } else {
                try {
                    PersistentValueManager.deletePersistentValue(existingPersistentValue);
                } catch (Exception ex) {
                    Tools.logException(Tools.class, ex, name);
                }
            }
        }
        if (value.length() <= 32672) {
            PersistentValue persistentValue = new PersistentValue(name, value, date, new Integer(ttl));
            PersistentValueManager.createPersistentValue(persistentValue);
        } else {
            int counter = 0;
            int start = 0;
            int end = start + 32672;
            String sub = value.substring(start, end);
            while (sub.length() > 0) {
                String postfix = "";
                if (++counter > 1)
                    postfix = "." + counter;
                existingPersistentValue = new PersistentValue(name + postfix, sub, date, new Integer(ttl));
                PersistentValueManager.createPersistentValue(existingPersistentValue);
                start = end;
                end = start + 32672;
                if (end > value.length())
                    end = value.length();
                sub = value.substring(start, end);
            }
            PersistentValue persistentValue = new PersistentValue(name + ".count", String.valueOf(counter), date, new Integer(ttl));
            PersistentValueManager.createPersistentValue(persistentValue);
        }
    } catch (HibernateException ex) {
        log.error("PersistentValue save failed", ex);
    }
}
