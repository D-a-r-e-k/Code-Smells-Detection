private void removeParse(Parse parse) {
    parse.trailer = parse.more.trailer;
    parse.more = parse.more.more;
}
