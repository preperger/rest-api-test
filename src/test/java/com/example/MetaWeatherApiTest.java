package com.example;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class MetaWeatherApiTest {
    
    private int woeidBudapest;

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://www.metaweather.com";
        RestAssured.basePath = "api/location";
    }

    @Test
    public void testLocationSearchByKeyword() {
        woeidBudapest =
                given().
                        log().all().
                        queryParam("query", "budapest").
                when().
                        get("/search").
                then().
                        log().all().
                        statusCode(200).
                and().
                        contentType(ContentType.JSON).
                and().
                        body("[0].title", is("Budapest")).
                        body("[0].woeid", equalTo(804365)).
                and().
                        extract().path("[0].woeid");
    }

    @Test(dependsOnMethods = "testLocationSearchByKeyword")
    public void testWeatherInfoByLocationId() {
        given().
                log().all().
        when().
                get("/" + woeidBudapest).
        then().
                log().all().
                statusCode(200).
        and().
                contentType(ContentType.JSON).
        and().
                body("title", is("Budapest"));
    }

    @Test(dependsOnMethods = "testLocationSearchByKeyword")
    public void testWeatherInfoByLocationIdAndDate(){
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String today = df.format(new Date());

        given().
                log().all().
        when().
                get("/" + woeidBudapest + "/" + today).
        then().
                log().all().
                statusCode(200).
        and().
                contentType(ContentType.JSON).
        and().
                body("applicable_date", everyItem(is(today.replaceAll("/", "-"))));
    }
    
}
