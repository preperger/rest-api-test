package com.example;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class JsonPlaceholderApiTest {

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    @Test(priority = 1)
    public void testPOST() {
        Map<String, Object> message = new HashMap<>();
        message.put("userid", 1);
        message.put("title", "Test Title");
        message.put("body", "Body for testing purposes");

        given().
                log().all().
                contentType(ContentType.JSON).
                body(message).
        when().
                post("/posts").
        then().
                log().all().
                statusCode(201).
        and().
                body("userid", equalTo(1)).
                body("title", is("Test Title")).
                body("body", is("Body for testing purposes"));
    }

    @Test(priority = 2)
    public void testPUT() {
        Map<String, Object> message = new HashMap<>();
        message.put("id", 1);
        message.put("userid", 1);
        message.put("title", "Updated Title");
        message.put("body", "Updated body for testing purposes");

        given().
                log().all().
                contentType(ContentType.JSON).
                body(message).
        when().
                put("/posts/1").
        then().
                log().all().
                statusCode(200).
        and().
                body("userid", equalTo(1)).
                body("title", is("Updated Title")).
                body("body", is("Updated body for testing purposes"));
    }

    @Test(priority = 3)
    public void testGET() {
        given().
                log().all().
                queryParam("userId", 1).
        when().
                get("/posts").
        then().
                log().all().
                statusCode(200).
        and().
                body("userId", everyItem(equalTo(1))).
                body("", hasSize(10));
    }

    @Test(priority = 4)
    public void testDELETE() {
        given().
                log().all().
        when().
                delete("/posts/1").
        then().
                log().all().
                statusCode(200).
        and().
                body("isEmpty()", is(true));
    }

}
