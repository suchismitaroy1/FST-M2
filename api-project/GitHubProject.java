package liveProject;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class GitHubProject {
    RequestSpecification requestSpec;
    int id;

    @BeforeClass
    public void setUp() {
        String token = "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";
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://api.github.com/user/keys")
                .setContentType(ContentType.JSON)
                .addHeader("Authorization","token "+token)
                .build();

    }

    @Test(priority=1)
    public void postRequest(){
        String reqBody ="{\"title\": \"TestAPIKey\",\"key\": \"ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCGogdb/U2tHUB48skg2RyLP7y7sCWZXIFGjkWkmS7hKTFSg24ZCCmk6TKjZZKuEwc6ZHQUo50nMC+cJhOS9CPvEdVoHbLPkD7UyaWFHPbk6MbayKxRd8IrCd4u4c4y4Y4wy4nEjM0iMyP+f7FrPA6x1jq4bg+5u+8x+JrIsgt2SgJne0E/ylFB7bJBrw2D2BYh+uKJDnVo7ajDn1r2ySx6IHTXpUbXCGtNKuE81/ZrMIWwRhvPR7H+870rpouoxavNOktAoWJgPSqWSdJGI7XFwXxZz7yLnC98vXUBon6JbUDzGJ6zKX9IYhsStr4n1bvyZqPeoASv7CZ62s5hUhHB\"}";
        Response response =given().spec(requestSpec)
                .body(reqBody)
                .when().post();

        System.out.println(response.getBody().asString());
        id = response.then().extract().path("id");
        System.out.println("Id is "+id);
        response.then().statusCode(201);
    }
    @Test(priority=2)
    public void GetKey(){
        //Generate response
        Response response = given().spec(requestSpec)
                .pathParam("keyId",id)
                .when().get("/{keyId}");

        System.out.println(response.getBody().asString());
        response.then().statusCode(200);
    }

    @Test(priority=3)
    public void DeleteToken(){
        Response response = given().spec(requestSpec)
                .pathParam("keyId",id)
                .when().delete("/{keyId}");

        System.out.println(response.getBody().asString());

        //Assertions
        response.then().statusCode(204);
    }

}
