import data.UserData;
import io.restassured.RestAssured;
import org.junit.Before;

public class BaseAPITest {
    @Before
    public void setUp() {
        RestAssured.baseURI = UserData.BASE_API_URL;
    }
}