import api.UserRegistrationApi;
import org.junit.*;
import pageobject.MainSite;
import pageobject.Register;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

import java.util.concurrent.TimeUnit;

public class AccountRegistrationTest {

    private WebDriver driver;
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

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

    @Test
    @DisplayName("регистрация")
    @Description("проверка регистрации")
    public void registrationSuccessTest() {
        MainSite mainSite = new MainSite(driver);
        mainSite.clickLoginButton();
        Register register = new Register(driver);
        register.waitRegisterPage(10);
        String userName = "testUser";
        String userEmail = "test@example.com";
        String userPassword = "testPassword";
    }

    @Test
    @DisplayName("неуспешная регистрация")
    @Description("проверка неуспешная регистрация, пароль < 6 символов, сообщение Некорректный пароль")
    public void incorrectPasswordRegistrationTest() {
        MainSite mainSite = new MainSite(driver);
        mainSite.clickLoginButton();
        Register register = new Register(driver);
        register.waitRegisterPage(10);
        String userName = "testUser";
        String userEmail = "test@example.com";
        String userPassword = "test";
        UserRegistrationApi.registerUser(userName, userEmail, userPassword);
        Assert.assertTrue("Текст об ошибке отсутствует", driver.findElement(register.wrongPasswordText).isDisplayed());
    }

    @After
    public void finish() {
        driver.quit();
    }
}
