import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Tests the overall functionality of the API. Should be executed with the Docker Environment up.
 */
public class ContactApiTest {

    private static final String SEARCH = "/search";

    @BeforeClass
    public static void setup() {
        RestAssured.port = 8080;
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/backend/api/contact";
    }

    @Test
    public void testCreateValidContact() {
        final String firstName = "first_name";
        final String lastName = "last_name";
        final String phoneNumber = Util.randomPhoneNumber();

        given().
            contentType(ContentType.JSON).
            body(Util.newContactData(firstName, lastName, phoneNumber)).
        when().
            post().
        then().
            assertThat().
            statusCode(HttpStatus.SC_OK).
        and().
            body("id", notNullValue()).
            body("firstName", equalTo(firstName)).
            body("lastName", equalTo(lastName)).
            body("phoneNumber", equalTo(phoneNumber));
    }

    @Test
    public void testCreateInvalidContactWithBlankFirstName() {
        final String firstName = "";
        final String lastName = "last_name";
        final String phoneNumber = "+32 32 123456";

        given().
            contentType(ContentType.JSON).
            body(Util.newContactData(firstName, lastName, phoneNumber)).
        when().
            post().
        then().
            assertThat().
            statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void testCreateInvalidContactWithBlankLastName() {
        final String firstName = "first_name";
        final String lastName = "";
        final String phoneNumber = "+32 32 123456";

        given().
            contentType(ContentType.JSON).
            body(Util.newContactData(firstName, lastName, phoneNumber)).
        when().
            post().
        then().
            assertThat().
            statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void testCreateInvalidContactWithBlankPhoneNumber() {
        final String firstName = "first_name";
        final String lastName = "last_name";
        final String phoneNumber = "";

        given().
            contentType(ContentType.JSON).
            body(Util.newContactData(firstName, lastName, phoneNumber)).
        when().
            post().
        then().
            assertThat().
            statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void testCreateInvalidContactWithNullFirstName() {
        final String firstName = null;
        final String lastName = "last_name";
        final String phoneNumber = "+32 32 123456";

        given().
            contentType(ContentType.JSON).
            body(Util.newContactData(firstName, lastName, phoneNumber)).
        when().
            post().
        then().
            assertThat().
            statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void testCreateInvalidContactWithNullLastName() {
        final String firstName = "first_name";
        final String lastName = null;
        final String phoneNumber = "+32 32 123456";

        given().
            contentType(ContentType.JSON).
            body(Util.newContactData(firstName, lastName, phoneNumber)).
        when().
            post().
        then().
            assertThat().
            statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void testCreateInvalidContactWithNullPhoneNumber() {
        final String firstName = "first_name";
        final String lastName = "last_name";
        final String phoneNumber = null;

        given().
            contentType(ContentType.JSON).
            body(Util.newContactData(firstName, lastName, phoneNumber)).
        when().
            post().
        then().
            assertThat().
            statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void testCreateInvalidContactWithInvalidPhoneNumber() {
        final String firstName = "first_name";
        final String lastName = "last_name";
        final String phoneNumber = "+32 32 123456223223";

        given().
            contentType(ContentType.JSON).
            body(Util.newContactData(firstName, lastName, phoneNumber)).
        when().
            post().
        then().
            assertThat().
            statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void testCreateUpdateAndGet() {
        final String firstName = "first_name";
        final String lastName = "last_name";
        final String updatedFirstName = "updated_first_name";
        final String updatedLastName = "updated_last_name";
        final String phoneNumber = Util.randomPhoneNumber();

        //Create Request
        final int contactId =
        given().
            contentType(ContentType.JSON).
            body(Util.newContactData(firstName, lastName, phoneNumber)).
        when().
            post().
        then().
            assertThat().
            statusCode(HttpStatus.SC_OK).
        and().
            extract().
            path("id");

        //Update Request
        given().
            contentType(ContentType.JSON).
            body(Util.newContactData(updatedFirstName, updatedLastName, phoneNumber)).
        when().
            put("/" + contactId).
        then().
            assertThat().
            statusCode(HttpStatus.SC_NO_CONTENT);

        //Get Request
        given().
        when().
            get("/" + contactId).
        then().
            assertThat().
            statusCode(HttpStatus.SC_OK).
        and().
            body("id", equalTo(contactId)).
            body("firstName", equalTo(updatedFirstName)).
            body("lastName", equalTo(updatedLastName)).
            body("phoneNumber", equalTo(phoneNumber));
    }

    @Test
    public void testCreateSearchWith2Contacts() {
        final String uniqueName = Util.randomName();

        //Create Request #1
        given().
            contentType(ContentType.JSON).
            body(Util.newContactData(uniqueName, Util.randomName(), Util.randomPhoneNumber())).
        when().
            post().
        then().
            assertThat().
            statusCode(HttpStatus.SC_OK);

        //Create Request #2
        given().
            contentType(ContentType.JSON).
            body(Util.newContactData(uniqueName, Util.randomName(), Util.randomPhoneNumber())).
        when().
            post().
        then().
            assertThat().
            statusCode(HttpStatus.SC_OK);

        //Search Request
        given().
            contentType(ContentType.JSON).
            body(Util.newSearch(uniqueName)).
        when().
            post(SEARCH).
        then().
            assertThat().
            statusCode(HttpStatus.SC_OK).
        and().
            body("$", hasSize(2));
    }
}
