import page_object.MainSite;
import page_object.UserAuthentication;
import page_object.Register;
import org.junit.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.qameta.allure.junit4.DisplayName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.qameta.allure.Description;
import org.openqa.selenium.*;
import java.util.concurrent.TimeUnit;
import static org.apache.commons.lang3.RandomStringUtils.*;

@RunWith(Parameterized.class)
public class AccountRegistrationTest {

    private WebDriver driver;
    private String browserType;

    String USER_NAME = randomAlphanumeric(3, 10);
    String USER_EMAIL = randomAlphanumeric(3, 10) + "@gmail.com";
    String USER_PASSWORD = randomAlphanumeric(5, 15);
    String USER_PASSWORD_FAILED = randomAlphanumeric(0, 5);

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
    public static Object[][] dataDriver() {
        return new Object[][]{
                {"yandexdriver"},
                {"chromedriver"},
        };
    }

    @Test
    @DisplayName("регистрация")
    @Description("проверка регистрация")
    public void RegistrationSuccessTest() {
        MainSite mainSite = new MainSite(driver);
        mainSite.clickLoginButton();
        UserAuthentication userAuthentication = new UserAuthentication(driver);
        userAuthentication.clickRegisterButton();
        Register register = new Register(driver);
        register.waitRegisterPage();
        register.registerUser(USER_NAME, USER_EMAIL, USER_PASSWORD);
        userAuthentication.waitForPageLoad();
    }

    @Test
    @DisplayName("Неуспешная регистрация")
    @Description("проверка неуспешная регистрация, пароля < 6 символов, сообщение Некорректный пароль")
    public void IncorrectPasswordRegistrationTest() {
        MainSite mainSite = new MainSite(driver);
        mainSite.clickLoginButton();
        UserAuthentication userAuthentication = new UserAuthentication(driver);
        userAuthentication.clickRegisterButton();
        Register register = new Register(driver);
        register.waitRegisterPage();
        register.registerUser(USER_NAME, USER_EMAIL, USER_PASSWORD_FAILED);
        Assert.assertTrue("Текст об ошибке отсутствует", driver.findElement(register.wrongPasswordText).isDisplayed());
    }

    @After
    public void finish() {
        driver.quit();
    }

//    @AfterClass
//    public static void afterClass() {
//        UserClient.deleteUser(accessToken);
//    }
}