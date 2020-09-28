import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;

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
            body(Util.newContact(firstName, lastName, phoneNumber)).
        when().
            post().
        then().
            assertThat().
            statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void testCreateInvalidContactWithBlankFirstName() {
        final String firstName = "";
        final String lastName = "last_name";
        final String phoneNumber = "+32 32 123456";

        given().
            contentType(ContentType.JSON).
            body(Util.newContact(firstName, lastName, phoneNumber)).
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
            body(Util.newContact(firstName, lastName, phoneNumber)).
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
            body(Util.newContact(firstName, lastName, phoneNumber)).
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
            body(Util.newContact(firstName, lastName, phoneNumber)).
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
            body(Util.newContact(firstName, lastName, phoneNumber)).
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
            body(Util.newContact(firstName, lastName, phoneNumber)).
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
            body(Util.newContact(firstName, lastName, phoneNumber)).
        when().
            post().
        then().
            assertThat().
            statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void testCreateAndUpdateSearchWithValidContact() {
        final String firstName = "first_name";
        final String lastName = "last_name";
        final String updatedFirstName = "updated_first_name";
        final String updatedLastName = "updated_last_name";
        final String phoneNumber = Util.randomPhoneNumber();

        given().
            contentType(ContentType.JSON).
            body(Util.newContact(firstName, lastName, phoneNumber)).
        when().
            post().
        then().
            assertThat().
            statusCode(HttpStatus.SC_OK);

        given().
            contentType(ContentType.JSON).
            body(Util.newContact(updatedFirstName, updatedLastName, phoneNumber)).
        when().
            put().
        then().
            assertThat().
            statusCode(HttpStatus.SC_OK);

        given().
            contentType(ContentType.JSON).
            body(Util.newSearch(phoneNumber)).
        when().
            post(SEARCH).
        then().
            assertThat().
            statusCode(HttpStatus.SC_OK).
        and().
            body("$", hasSize(1)).
            body("[0].firstName", equalTo(updatedFirstName)).
            body("[0].lastName", equalTo(updatedLastName)).
            body("[0].phoneNumber", equalTo(phoneNumber));
    }
}
