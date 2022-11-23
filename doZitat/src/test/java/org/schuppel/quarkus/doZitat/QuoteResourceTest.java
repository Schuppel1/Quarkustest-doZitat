package org.schuppel.quarkus.doZitat;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static java.lang.String.valueOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

@QuarkusTest
public class QuoteResourceTest {

    @Test
    public void shouldGetAllQuotes() {
        given().
            header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON).
        when().
            get("/quotes").
        then().statusCode(Response.Status.OK.getStatusCode()).body(
                "size()",is(Integer.valueOf(get("/quotes/count").getBody().print())));
    }

    @Test
    public void shouldCountAllQuotes() {
        given().
            header(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN).
        when().
            get("/quotes/count").
        then().statusCode(Response.Status.OK.getStatusCode()).body(matchesPattern("[0-9]+"));
    }

    @Test
    public void shouldGetAQuote() {
        Integer zero = 0;

        given().
            header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON).
        when().
            get("/quotes/1").
        then().statusCode(Response.Status.OK.getStatusCode())
                .body("id" ,greaterThan(zero))
                .body("quote" ,notNullValue())
                .body("profName" ,notNullValue())
                .body("course" ,notNullValue());
    }
}