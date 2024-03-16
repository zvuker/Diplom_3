import page_object.MainSite;
import page_object.Register;
import page_object.RecoverPassword;
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
import java.util.concurrent.TimeUnit;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@RunWith(Parameterized.class)
public class UserAuthorization {
    private WebDriver driver;
    private String browserType;
    private final static String USER_EMAIL = "2054@gmail.com";
    private final static String USER_PASSWORD = "1234567";

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
    public static Object[][] dataDriver() {
        return new Object[][]{
                {"chromedriver"},
                {"yandexdriver"},
        };
    }

    @Test
    @DisplayName("вход по кнопке Войти")
    @Description("проверка входа в аккаунт")
    public void clickLoginButtonTest() {
        MainSite mainSite = new MainSite(driver);
        mainSite.clickLoginButton();
        page_object.UserAuthentication userAuthentication = new page_object.UserAuthentication(driver);
        userAuthentication.authUser(USER_EMAIL, USER_PASSWORD);
        mainSite.waitMainPageLoad();
    }

    @Test
    @DisplayName("вход в личный кабинет")
    @Description("проверка входа по кнопке Личный Кабинет")
    public void enterByPersonalAccountButtonTest() {
        MainSite mainSite = new MainSite(driver);
        mainSite.clickAccountButton();
        page_object.UserAuthentication userAuthentication = new page_object.UserAuthentication(driver);
        userAuthentication.authUser(USER_EMAIL, USER_PASSWORD);
        mainSite.waitMainPageLoad();
    }

    @Test
    @DisplayName("вход через форму регистрации")
    @Description("проверка входа через форму регистрации")
    public void signInByPersonalAccountButtonTest() {
        MainSite mainSite = new MainSite(driver);
        mainSite.clickLoginButton();
        page_object.UserAuthentication userAuthentication = new page_object.UserAuthentication(driver);
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
        page_object.UserAuthentication userAuthentication = new page_object.UserAuthentication(driver);
        userAuthentication.clickResetPasswordLink();
        RecoverPassword recoverPassword = new RecoverPassword(driver);
        recoverPassword.waitRecoveryPageLoad();
        recoverPassword.clickOnEnter();
        userAuthentication.authUser(USER_EMAIL, USER_PASSWORD);
        mainSite.waitMainPageLoad();
    }

    @After
    public void finish() {
        // Закрытие браузера
        driver.quit();
    }
}
