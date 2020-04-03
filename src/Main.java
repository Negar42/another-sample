
public class Main {
	public static void main(String[] args) throws CloneNotSupportedException {
		Manager manager = new Manager();
		OSCommandProcessor processor = new OSCommandProcessor(manager);
		processor.installCommand();
		processor.run();

	}

}