package resources;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static io.restassured.RestAssured.given;

public class Tools {
    String usersCollection = "/users";
    String postsCollection = "/posts?userId=";
    String commentsCollection = "/comments?postId=";

    public Response getResponse (String endpoint) {
        System.out.println("... Retrieving registries from \"" + RestAssured.baseURI + endpoint + "\"...");
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get(endpoint)
                .then()
                .extract().response();
        boolean result = response.statusCode() == 200;
        if (result) {
            System.out.println("=== Response successfully retrieved!");
        }
        return response;
    }

    public int getUserId(String username) {
        Response users = getResponse(usersCollection);
        JsonPath usersJsonPathEvaluator = users.jsonPath();
        List<HashMap> usersList = usersJsonPathEvaluator.get("");
        int userId = 0;

        System.out.println("... Getting userId for the user \"" + username + "\"...");
        for(int i = 0; i < usersList.size(); i++)
        {
            if(usersList.get(i).get("username").toString().equals(username)){
                userId = (int) usersList.get(i).get("id");
                System.out.println("=== The userId for the username \"" + username + "\" is \"" + userId + "\".");
                break;
            }
        }

        return userId;
    }

    public ArrayList getPostIdsList(int userId) {
        System.out.println("... Getting postIds for userId \"" + userId + "\" ...");
        Response posts = getResponse(postsCollection + userId);
        JsonPath postsJsonPathEvaluator = posts.jsonPath();
        ArrayList postsIdList = postsJsonPathEvaluator.get("id");
        System.out.println("=== The postIds for userId \"" + userId + "\" are \"" + postsIdList + "\" .");
        return postsIdList;
    }

    public ArrayList getPostCommentsList(String username) {
        int userId = getUserId(username);
        ArrayList postIds = getPostIdsList(userId);
        String postId;
        ArrayList commentsPerPost = new ArrayList();
        ArrayList comments = new ArrayList();

        for(int i = 0; i < postIds.size(); i++)
        {
            postId = postIds.get(i).toString();
            System.out.println("... Getting comments from postId \"" + postId + "\" ...");
            Response postComments = getResponse(commentsCollection + postId);
            JsonPath commentsJsonPathEvaluator = postComments.jsonPath();
            commentsPerPost = commentsJsonPathEvaluator.get("");
            System.out.println("=== The comments from postId \"" + postId + "\" are \"" + commentsPerPost + "\" .");
            comments.add(commentsPerPost);
        }
        return comments;
    }

    public ArrayList getPostCommentsEmailsList(String username) {
        int userId = getUserId(username);
        ArrayList postIds = getPostIdsList(userId);
        String postId;
        ArrayList emailsPerPost = new ArrayList();
        ArrayList emails = new ArrayList();

        for(int i = 0; i < postIds.size(); i++)
        {
            postId = postIds.get(i).toString();
            System.out.println("... Getting emails from the comments for postId \"" + postId + "\" ...");
            Response comments = getResponse(commentsCollection + postId);
            JsonPath commentsJsonPathEvaluator = comments.jsonPath();
            emailsPerPost = commentsJsonPathEvaluator.get("email");
            System.out.println("=== The emails from the comments for postId \"" + postId + "\" are \"" + emailsPerPost + "\" .");
            emails.add(emailsPerPost);
        }
        return emails;
    }
}
