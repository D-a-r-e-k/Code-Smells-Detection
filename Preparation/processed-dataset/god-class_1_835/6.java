String decode(String code) {
    return code.equals(Fixture.red) ? "red" : code.equals(Fixture.green) ? "green" : code.equals(Fixture.yellow) ? "yellow" : code.equals(Fixture.gray) ? "gray" : code.equals("#808080") ? "gray" : code;
}
