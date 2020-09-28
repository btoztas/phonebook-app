import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;

/**
 * Tests the overall functionality of the API. Should be executed with the Docker Environment up
 */
public class ContactApiTest {

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
        final String phoneNumber = "+32 32 123456";

        given().
            contentType(ContentType.JSON).
            body(newContact(firstName, lastName, phoneNumber)).
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
            body(newContact(firstName, lastName, phoneNumber)).
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
            body(newContact(firstName, lastName, phoneNumber)).
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
            body(newContact(firstName, lastName, phoneNumber)).
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
            body(newContact(firstName, lastName, phoneNumber)).
        when().
            post().
        then().
            assertThat().
            statusCode(HttpStatus.SC_BAD_REQUEST);
    }


    private Map<String, String> newContact(final String firstName, final String lastName, final String phoneNumber) {
        return new HashMap<String, String>() {{
            {
                put("firstName", firstName);
                put("lastName", lastName);
                put("phoneNumber", phoneNumber);
            }
        }};
    }
}
