protected Parse rows(Iterator keys) {
    if (keys.hasNext()) {
        Object key = keys.next();
        Parse result = tr(td(key.toString(), td(summary.get(key).toString(), null)), rows(keys));
        if (key.equals(countsKey)) {
            mark(result);
        }
        return result;
    } else {
        return null;
    }
}
