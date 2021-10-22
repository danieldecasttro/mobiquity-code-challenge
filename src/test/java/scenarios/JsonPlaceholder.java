package scenarios;

import org.junit.*;
import io.restassured.RestAssured;
import org.junit.rules.TestName;
import java.util.ArrayList;
import resources.*;

public class JsonPlaceholder {

    static String baseURL = "https://jsonplaceholder.typicode.com";
    String username = "Delphine";

    Tools tools = new Tools();

    @Rule
    public TestName testname = new TestName();

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = baseURL;
    }

    @Before
    public void startScenario() {
        System.out.println(">>> Starting test method \"" + testname.getMethodName() + "\" ...");
    }

    @After
    public void finishScenario() {
        System.out.println("### Finishing test method \"" + testname.getMethodName() + "\" .\n");
    }

    @Test
    public void validateUserId() {
        Assert.assertTrue(tools.getUserId(username) > 0);
    }

    @Test
    public void validatePosts() {
        int userId = tools.getUserId(username);
        Assert.assertTrue(tools.getPostIdsList(userId).size() > 0);
    }

    @Test
    public void validateEmailsPerComment() {
        ArrayList comments = tools.getPostCommentsList(username);
        int commentsCounter = 0;
        for (Object commentsPerPost : comments) {
            commentsCounter += ((ArrayList) commentsPerPost).size();
        }

        ArrayList emails = tools.getPostCommentsEmailsList(username);
        int emailsCounter = 0;
        for (Object emailsPerComment : emails) {
            emailsCounter += ((ArrayList) emailsPerComment).size();
        }

        boolean result = false;
        if(commentsCounter == emailsCounter) {
            result = true;
            System.out.println("=== There are \"" + commentsCounter + "\" comments and \"" + emailsCounter + "\" emails...");
        }

        Assert.assertTrue(result);
    }

    @Test
    public void validateEmailsFormat() {
        ArrayList emails = tools.getPostCommentsEmailsList(username);
        ArrayList emailsPerPost;
        String email;

        for (Object value : emails) {
            emailsPerPost = ((ArrayList) value);
            for (Object o : emailsPerPost) {
                email = o.toString();
                System.out.println("... Validating email: \"" + email + "\" ...");
                String allowedSpecialChars = "abcdefghijklmnopqrstuvwxyz0123456789@-_.";
                for (int i = 0; i < email.length(); i++) {
                    Assert.assertTrue(allowedSpecialChars.contains(email.substring(i,i+1).toLowerCase()));
                }
                int indexOfAt = email.indexOf("@");
                Assert.assertTrue(indexOfAt > 0);
                Assert.assertTrue(indexOfAt < email.length());
                String domain = email.substring(indexOfAt);
                int indexOfDot = domain.indexOf(".");
                Assert.assertTrue(indexOfDot > 0);
                Assert.assertTrue(indexOfDot < domain.length());
            }
        }
    }
}
