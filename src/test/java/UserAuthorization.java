import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import pageobject.MainSite;
import pageobject.Register;
import pageobject.RecoverPassword;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@RunWith(Parameterized.class)
public class UserAuthorization {
    private WebDriver driver;
    private String browserType;
    private final static String userEmail = "2054@gmail.com";
    private final static String userPassword = "1234567";

    public UserAuthorization(String browserType) {
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
                        "\"email\": \"" + userEmail + "\",\n" +
                        "\"password\": \"" + userPassword + "\",\n" +
                        "\"name\": \"Username\"\n" +
                        "}")
                .when()
                .post("/api/auth/register");

        if (response.getStatusCode() != 200 && !response.getBody().asString().contains("User already exists")) {
            System.out.println("Ошибка создания пользователя: " + response.getBody().asString());
            Assert.fail("Ошибка создания пользователя");
        } else if (response.getStatusCode() == 200) {
            System.out.println("Пользователь успешно создан");
        }
    }

    @Test
    @DisplayName("вход по кнопке Войти")
    @Description("проверка входа в аккаунт")
    public void clickLoginButtonTest() {
        MainSite mainSite = new MainSite(driver);
        mainSite.clickLoginButton();
        pageobject.UserAuthentication userAuthentication = new pageobject.UserAuthentication(driver);
        userAuthentication.authUser(userEmail, userPassword);
        mainSite.waitMainPageLoad();
    }

    @Test
    @DisplayName("вход в личный кабинет")
    @Description("проверка входа по кнопке Личный Кабинет")
    public void enterByPersonalAccountButtonTest() {
        MainSite mainSite = new MainSite(driver);
        mainSite.clickAccountButton();
        pageobject.UserAuthentication userAuthentication = new pageobject.UserAuthentication(driver);
        userAuthentication.authUser(userEmail, userPassword);
        mainSite.waitMainPageLoad();
    }

    @Test
    @DisplayName("вход через форму регистрации")
    @Description("проверка входа через форму регистрации")
    public void signInByPersonalAccountButtonTest() {
        MainSite mainSite = new MainSite(driver);
        mainSite.clickLoginButton();
        pageobject.UserAuthentication userAuthentication = new pageobject.UserAuthentication(driver);
        userAuthentication.clickRegisterButton();
        Register register = new Register(driver);
        String name = randomAlphanumeric(3, 10);
        String email = randomAlphanumeric(3, 10) + "@yandex.ru";;
        String password = randomAlphanumeric(5, 15);
        register.registerUser(name, email, password);
        userAuthentication.waitForPageLoad();
        userAuthentication.authUser(email, password);
        mainSite.waitMainPageLoad();
    }

    @Test
    @DisplayName("вход через форму восстановления пароля")
    @Description("проверка входа через форму восстановления пароля")
    public void verifyPasswordRecoveryFormatTest() {
        MainSite mainSite = new MainSite(driver);
        mainSite.clickAccountButton();
        pageobject.UserAuthentication userAuthentication = new pageobject.UserAuthentication(driver);
        userAuthentication.clickResetPasswordLink();
        RecoverPassword recoverPassword = new RecoverPassword(driver);
        recoverPassword.waitRecoveryPageLoad();
        recoverPassword.clickOnEnter();
        userAuthentication.authUser(userEmail, userPassword);
        mainSite.waitMainPageLoad();
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
