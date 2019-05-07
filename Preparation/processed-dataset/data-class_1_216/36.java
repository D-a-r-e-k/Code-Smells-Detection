/**
     * @param tags
     * @return
     */
public static List splitStringAsTags(String tags) {
    String[] tagsarr = StringUtils.split(tags, TAG_SPLIT_CHARS);
    if (tagsarr == null)
        return Collections.EMPTY_LIST;
    return Arrays.asList(tagsarr);
}
