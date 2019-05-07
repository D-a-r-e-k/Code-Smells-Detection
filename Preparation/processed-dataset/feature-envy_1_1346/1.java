/**
    * Makes a UML2JagGenerator with an external logger.
    *
    * @param logger somewhere to redirect output, other than System.out
    */
public UML2JagGenerator(ConsoleLogger logger) {
    this.logger = logger;
    model = new SimpleModel();
}
