package liveProject;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
    //headers
    Map<String,String> reqHeaders = new HashMap();
    //Resource path
    String resourcePath = "/api/users";

    // Create contract
    @Pact(consumer="UserConsumer",provider="UserProvider")
    public RequestResponsePact createPact(PactDslWithProvider builder){
        reqHeaders.put("Content-Type","application/json");

        //Create json body

        DslPart reqResponseBody = new PactDslJsonBody()
                .numberType("id")
                .stringType("firstName")
                .stringType("lastName")
                .stringType("email");

        return builder.given("Request to create a user")
                .uponReceiving("Request to create a user")
                .method("POST")
                .headers(reqHeaders)
                .path(resourcePath)
                .body(reqResponseBody)
                .willRespondWith()
                .status(201)
                .body(reqResponseBody)
                .toPact();

    }

    @Test
    @PactTestFor(providerName="UserProvider",port ="8282")
    public void consumerSideTest(){
        //set base uri
        String baseURI = "http://localhost:8282";
        //Request Body
        Map<String,Object> reqBody = new HashMap();
        reqBody.put("id",345);
        reqBody.put("firstName","suchismita");
        reqBody.put("lastName","roy");
        reqBody.put("email","suchismita.roy1@inm.com");

        //Generate response
       given().headers(reqHeaders)
                .body(reqBody)//request specification
               .when().post(baseURI+resourcePath)//resource path
               .then().log().all().statusCode(201);//assertions

    }

}
