public static String hexEncode(String str) {
    if (StringUtils.isEmpty(str))
        return str;
    return RegexUtil.encode(str);
}
