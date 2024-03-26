import api.UserAccountApi;
import org.junit.*;
import pageobject.MainSite;
import pageobject.RecoverPassword;
import pageobject.Register;
import pageobject.UserAuthentication;
import api.UserRegistrationApi;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

import java.util.concurrent.TimeUnit;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public class UserAuthorizationTest {
    private WebDriver driver;
    private String browserType;
    private final static String BASE_URL = "https://stellarburgers.nomoreparties.site/";
    private final static String USER_EMAIL = "2054@gmail.com";
    private final static String USER_PASSWORD = "1234567";

    @Before
    public void setUp() {
        String browserType = System.getenv("BROWSER");
        if (browserType == null || browserType.isEmpty()) {
            browserType = "chromedriver";
        }

        if (browserType.equals("chromedriver")) {
            String chromeDriverPath = "src/main/resources/chromedriver.exe";
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);
            ChromeOptions options = new ChromeOptions();
            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.navigate().to(BASE_URL);
        } else if (browserType.equals("yandexdriver")) {
            String yandexDriverPath = "src/main/resources/yandexdriver.exe";
            System.setProperty("webdriver.chrome.driver", yandexDriverPath);
            ChromeOptions options = new ChromeOptions();
            options.setBinary("src/main/resources/yandexdriver.exe");
            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.navigate().to(BASE_URL);
        }
    }

    @Before
    public void createUser() {
        UserRegistrationApi userRegistrationAPI = new UserRegistrationApi();
        userRegistrationAPI.registerUser("Username", USER_EMAIL, USER_PASSWORD);
    }


    @Test
    @DisplayName("вход по кнопке Войти")
    @Description("проверка входа в аккаунт")
    public void clickLoginButtonTest() {
        MainSite mainSite = new MainSite(driver);
        mainSite.clickLoginButton();
        UserAuthentication userAuthentication = new UserAuthentication(driver);
        userAuthentication.authUser(USER_EMAIL, USER_PASSWORD);
        mainSite.waitMainPageLoad();
    }

    @Test
    @DisplayName("вход в личный кабинет")
    @Description("проверка входа по кнопке Личный Кабинет")
    public void enterByPersonalAccountButtonTest() {
        MainSite mainSite = new MainSite(driver);
        mainSite.clickAccountButton();
        UserAuthentication userAuthentication = new UserAuthentication(driver);
        userAuthentication.authUser(USER_EMAIL, USER_PASSWORD);
        mainSite.waitMainPageLoad();
    }

    @Test
    @DisplayName("вход через форму регистрации")
    @Description("проверка входа через форму регистрации")
    public void signInByPersonalAccountButtonTest() {
        MainSite mainSite = new MainSite(driver);
        mainSite.clickLoginButton();
        UserAuthentication userAuthentication = new UserAuthentication(driver);
        userAuthentication.clickRegisterButton();
        Register register = new Register(driver);
        String name = randomAlphanumeric(3, 10);
        String email = randomAlphanumeric(3, 10) + "@yandex.ru";
        String password = randomAlphanumeric(5, 15);
        register.registerUser(name, email, password);
        userAuthentication.waitForPageLoad();
        userAuthentication.authUser(email, password);
        mainSite.waitMainPageLoad();
        UserAccountApi userAccountApi = new UserAccountApi();
        userAccountApi.deleteUser(USER_EMAIL);
    }

    @Test
    @DisplayName("вход через форму восстановления пароля")
    @Description("проверка входа через форму восстановления пароля и удаление пользователя")
    public void verifyPasswordRecoveryFormatTest() {
        MainSite mainSite = new MainSite(driver);
        mainSite.clickAccountButton();
        UserAuthentication userAuthentication = new UserAuthentication(driver);
        userAuthentication.clickResetPasswordLink();
        RecoverPassword recoverPassword = new RecoverPassword(driver);
        recoverPassword.waitRecoveryPageLoad();
        recoverPassword.clickOnEnter();
        userAuthentication.authUser(USER_EMAIL, USER_PASSWORD);
        mainSite.waitMainPageLoad();
        UserAccountApi userAccountApi = new UserAccountApi();
        userAccountApi.deleteUser(USER_EMAIL);
    }

    @After
    public void finish() {
        driver.quit();
    }
}
