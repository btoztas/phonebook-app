import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Util {

    private static final Random random = new Random(System.currentTimeMillis());

    public static Map<String, String> newContact(final String firstName, final String lastName, final String phoneNumber) {
        return new HashMap<String, String>() {{
            put("firstName", firstName);
            put("lastName", lastName);
            put("phoneNumber", phoneNumber);
        }};
    }

    public static Map<String, String> newSearch(final String token) {
        return new HashMap<String, String>() {{
            put("token", token);
        }};
    }

    public static String randomPhoneNumber() {
        return "+32 32 " + random.nextInt(1000000);
    }
}
