package runner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import scenarios.*;

@RunWith(Suite.class)
@Suite.SuiteClasses(JsonPlaceholder.class)
public class TestRunner {
}
