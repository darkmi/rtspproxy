package rtspproxy.util;

public class ExecDemo {

	public static void main(String args[]) {
		Runtime r = Runtime.getRuntime();
		Process p = null;
		try {
			p = r.exec("notepad");
		} catch (Exception e) {
			System.out.println("Error executing notepad.");
		}
	}
}
