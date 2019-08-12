package com.testing.statuses;

import com.testing.constants.Auth;
import com.testing.constants.EndPoints;
import com.testing.constants.Path;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class SimpleFramework {

    String tweetId = "";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = Path.BASE_URI;
        RestAssured.basePath = Path.BASE_PATH;
    }

    @Test()
    public void postTweet() {
        Response response =
                given ( )
                        .auth ( )
                        .oauth (Auth.CONSUMER_KEY, Auth.CONSUMER_SECRET, Auth.ACCESS_TOKEN, Auth.ACCESS_SCRET)
                        .queryParam ("status", "Intership API bla1")
                        .when ( )
                        .post (EndPoints.STATUSES_TWEET_POST)
                        .then ( )
                        .statusCode (200)
                        .extract ( )
                        .response ( );
        //validations

        tweetId = response.path ("id_str");
        System.out.println ("Tweet id es: " + tweetId);
    }

    @Test(dependsOnMethods = {"postTweet"})
    public void readTweet(){
        Response res = //Esta es el response como objeto de java
                given()
                        .auth()
                        .oauth(Auth.CONSUMER_KEY, Auth.CONSUMER_SECRET, Auth.ACCESS_TOKEN, Auth.ACCESS_SCRET)
                        .queryParam("id", tweetId)
                        .when()
                        .get(EndPoints.STATUSES_TWEET_READ_SINGLE)
                        .then()
                        .statusCode(200)
                        .extract().response();

        //First extract method using response path with Response java object
        String text = res.path("text");
        System.out.println("Tweet Text is: " + text);

        String resTweetId = res.path ("id_str");
        System.out.println ("The tweet id is: " + resTweetId);
        Assert.assertTrue (resTweetId.contains (tweetId));
    }

    @Test(dependsOnMethods = {"readTweet"}, enabled = true)
    public void deleteTweet(){
        given()
                .auth()
                .oauth(Auth.CONSUMER_KEY, Auth.CONSUMER_SECRET, Auth.ACCESS_TOKEN, Auth.ACCESS_SCRET)
                .pathParam("id", tweetId) //pathparameter
                .when()
                .post(EndPoints.STATUSES_TWEET_DESTROY)
                .then()
                .statusCode(200);
    }
}
