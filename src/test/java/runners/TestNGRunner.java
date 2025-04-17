package runners;

import org.testng.TestNG;

import java.util.ArrayList;
import java.util.List;

public class TestNGRunner {
    public static void main(String[] args) {
        TestNG testNG = new TestNG();
        List<String> suites = new ArrayList<>();
        suites.add("testng.xml");
        testNG.setTestSuites(suites);
        testNG.run();
    }
}
