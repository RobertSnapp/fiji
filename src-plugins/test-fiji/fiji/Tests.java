package fiji;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

//import org.junit.Test;

import org.junit.runner.JUnitCore;

public class Tests extends TestSuite {
	
	public Tests() {
		addScriptTests();
	}

	void addScriptTests() {
		String path = System.getProperty("fiji.dir") + "/tests";
		File dir = new File(path);
		String[] list = dir.list();
		for (String file : list)
			if ((file.endsWith(".py") || file.endsWith(".rb")) &&
					!file.startsWith("lib.") &&
					!file.startsWith("record."))
				addTest(new ScriptTest(path + "/" + file));
	}

	class ScriptTest extends TestCase {
		String path;

		ScriptTest(String path) {
			this.path = path;
		}

		public String getName() {
			return path;
		}

		protected void runTest() {
			assertTrue("failed",
					runFiji(new String[] { "--", path }));
		}
	}

	public static Test suite() {
		return new Tests();
	}

	protected static String fijiExecutable;

	public static boolean runFiji(String[] args) {
		if (fijiExecutable == null) {
			fijiExecutable = System.getProperty("fiji.executable");
			if (fijiExecutable == null)
				throw new RuntimeException("Could not find the "
					+ "fiji executable");
		}

		String[] newArgs = new String[args.length + 1];
		newArgs[0] = fijiExecutable;
		System.arraycopy(args, 0, newArgs, 1, args.length);

		try {
			Process process = Runtime.getRuntime().exec(newArgs);
			return process.waitFor() == 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {
		JUnitCore.main(new String[] {
			"fiji.Tests"
		});
	}
}
