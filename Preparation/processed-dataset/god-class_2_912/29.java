public String formatTotal(HeaderCell header, Object total) {
    Object displayValue = total;
    if (header.getColumnDecorators().length > 0) {
        for (int i = 0; i < header.getColumnDecorators().length; i++) {
            DisplaytagColumnDecorator decorator = header.getColumnDecorators()[i];
            try {
                displayValue = decorator.decorate(total, this.getPageContext(), tableModel.getMedia());
            } catch (DecoratorException e) {
                logger.warn(e.getMessage(), e);
            }
        }
    }
    return displayValue != null ? displayValue.toString() : "";
}
