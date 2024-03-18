import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.qameta.allure.junit4.DisplayName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.qameta.allure.Description;
import org.openqa.selenium.*;
import pageobject.MainSite;
import pageobject.Register;
import pageobject.UserAuthentication;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static org.apache.commons.lang3.RandomStringUtils.*;

@RunWith(Parameterized.class)
public class AccountRegistrationTest {

    private WebDriver driver;
    private String browserType;

    String userName = randomAlphanumeric(3, 10);
    String userEmail = randomAlphanumeric(3, 10) + "@gmail.com";
    String userPassword = randomAlphanumeric(5, 15);
    String userPasswordFailed = randomAlphanumeric(0, 5);

    public AccountRegistrationTest(String browserType) {
        this.browserType = browserType;
    }

    @Before
    public void setUp() {
        if (browserType.equals("chromedriver")) {
            System.setProperty("webdriver.chrome.driver", "C:\\Chromewebdriver\\WebDriver\\bin\\chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.navigate().to("https://stellarburgers.nomoreparties.site/");
        } else if (browserType.equals("yandexdriver")) {
            System.setProperty("webdriver.chrome.driver", "C:\\Chromewebdriver\\WebDriver\\bin\\yandexdriver.exe");
            ChromeOptions options = new ChromeOptions();
            options.setBinary("C:\\Users\\ПК\\AppData\\Local\\Yandex\\YandexBrowser\\Application\\browser.exe");
            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.navigate().to("https://stellarburgers.nomoreparties.site/");
        }
    }

    @Parameterized.Parameters(name = "проверка браузера: {0}")
    public static List<String> dataDriver() {
        String browser = System.getenv("BROWSER");
        if (browser != null && !browser.isEmpty()) {
            return Arrays.asList(browser);
        } else {
            return Arrays.asList("yandexdriver", "chromedriver");
        }
    }

    @Before
    public void createUser() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "\"name\": \"" + userName + "\",\n" +
                        "\"email\": \"" + userEmail + "\",\n" +
                        "\"password\": \"" + userPassword + "\"\n" +
                        "}")
                .when()
                .post("/api/auth/register");

        if (response.getStatusCode() != 200) {
            System.out.println("Ошибка создания пользователя: " + response.getBody().asString());
            Assume.assumeTrue("Ошибка создания пользователя", false);
        }
    }

    @Test
    @DisplayName("регистрация")
    @Description("проверка регистрация")
    public void registrationSuccessTest() {
        MainSite mainSite = new MainSite(driver);
        mainSite.clickLoginButton();
        UserAuthentication userAuthentication = new UserAuthentication(driver);
        userAuthentication.clickRegisterButton();
        Register register = new Register(driver);
        register.waitRegisterPage();
        register.registerUser(userName, userEmail, userPassword);
        userAuthentication.waitForPageLoad();
    }

    @Test
    @DisplayName("Неуспешная регистрация")
    @Description("проверка неуспешная регистрация, пароля < 6 символов, сообщение Некорректный пароль")
    public void incorrectPasswordRegistrationTest() {
        MainSite mainSite = new MainSite(driver);
        mainSite.clickLoginButton();
        UserAuthentication userAuthentication = new UserAuthentication(driver);
        userAuthentication.clickRegisterButton();
        Register register = new Register(driver);
        register.waitRegisterPage();
        register.registerUser(userName, userEmail, userPasswordFailed);
        Assert.assertTrue("Текст об ошибке отсутствует", driver.findElement(register.wrongPasswordText).isDisplayed());
    }

    @After
    public void finish() {
        driver.quit();
    }

    public void deleteUser() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        Response response = RestAssured.given()
                .queryParam("email", userEmail)
                .when()
                .delete("/api/users");

        Assert.assertEquals(200, response.getStatusCode());
    }
}
