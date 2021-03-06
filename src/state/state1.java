package state;

import java.util.function.Consumer;

public interface state1 {
  interface Logger {
    enum Level { ERROR, WARNING }
    
    public void error(String message);
    public void warning(String message);
    
    Logger quiet();
    Logger normal();
  }
  
  class Loggers {
    static Logger logger(Consumer<String> printer) {
      return new NormalLogger(printer);
    }
  }
  
  /* private JDK9 */ class NormalLogger implements Logger {
    
    private final Consumer<String> printer;
    
    NormalLogger(Consumer<String> printer) {
      this.printer = printer;
    }
    
    @Override
    public void error(String message) {
      printer.accept(message);
    }
    @Override
    public void warning(String message) {
      printer.accept(message);
    }
    
    @Override
    public Logger quiet() {
      return new QuietLogger(printer);
    }
    @Override
    public Logger normal() {
      return this;
    }
  }
  
  /* private JDK9 */ class QuietLogger implements Logger {
    
    private final Consumer<String> printer;
    
    QuietLogger(Consumer<String> printer) {
      this.printer = printer;
    }
    
    @Override
    public void error(String message) {
      printer.accept(message);
    }
    @Override
    public void warning(String message) {
      // empty
    }
    
    @Override
    public Logger quiet() {
      return this;
    }
    @Override
    public Logger normal() {
      return new NormalLogger(printer);
    }
  }
  
  public static void main(String[] args) {
    Logger logger = Loggers.logger(new Consumer<String>() {
      @Override
      public void accept(String msg) {
        System.out.println(msg);
      }
    });
    logger.error("ERROR");
    logger.warning("WARNING");
    
    Logger quiet = logger.quiet();
    quiet.error("ERROR");
    quiet.warning("WARNING");
    
    Logger logger2 = quiet.normal();
    logger2.error("ERROR");
    logger2.warning("WARNING");
  }
}
