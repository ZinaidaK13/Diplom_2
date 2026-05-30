package data;

import java.net.URI;
import java.util.Locale;
import net.datafaker.Faker;
import request.UserRequest;

public class UserData {
    public static final String BASE_API_URL= "https://stellarburgers.education-services.ru";

    public static final String USER_CREATE_ENDPOINT = "/api/auth/register";
    public static final String USER_LOGIN_ENDPOINT = "/api/auth/login";
    public static final String USER_DELETE_ENDPOINT="/api/auth/user";

   public static final String WRONG_PASSWORD ="WRONG_PASSWORD_123";
    public static final String WRONG_EMAIL ="WRONG@maillll.ru";


    private static final Faker FAKER = new Faker();
    public static final String USER_PASSWORD = "password";

    public static String generateEmail() {
        String firstName = FAKER.name().firstName();
        long timestamp = System.currentTimeMillis();
        return firstName + timestamp + "@yandex.ru";
    }

    public static String generateName() {
        return FAKER.name().firstName();
    }

    public static UserRequest generateValidUser() {
        return new UserRequest(
                generateEmail(),
                USER_PASSWORD,
                generateName()

        );

}
}