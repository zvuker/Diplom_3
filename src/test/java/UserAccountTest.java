import api.UserRegistrationApi;
import org.junit.*;
import pageobject.MainSite;
import pageobject.UserAuthentication;
import pageobject.UserAccount;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

import java.util.concurrent.TimeUnit;

public class UserAccountTest {

    private WebDriver driver;
    private final static String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private final static String USER_EMAIL = "2055@gmail.com";
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
    @DisplayName("переход в личный кабинет")
    @Description("проверка перехода в личный кабинет")
    public void userProfilePageTest() {
        MainSite mainSite = new MainSite(driver);
        mainSite.clickAccountButton();
        UserAuthentication userAuthentication = new UserAuthentication(driver);
        userAuthentication.waitForPageLoad();
        Assert.assertTrue("Страница авторизации не отобразилась", driver.findElement(userAuthentication.signInTitle).isDisplayed());
    }

    @Test
    @DisplayName("переход в конструктор из кабинета.")
    @Description("проверка перехода по вкладке Конструктор")
    public void transitionToConstructorFromProfilePageTest() {
        MainSite mainSite = new MainSite(driver);
        mainSite.waitInvisibilityAnimation();
        mainSite.clickAccountButton();
        UserAuthentication userAuthentication = new UserAuthentication(driver);
        userAuthentication.waitForPageLoad();
        userAuthentication.clickBuildButton();
        mainSite.waitMainPageLoad();
        Assert.assertTrue("переход в конструктор не состоялся", driver.findElement(mainSite.burgerMainText).isDisplayed());
    }

    @Test
    @DisplayName("клик логотип")
    @Description("проверка перехода в конструктор по клику на логотип")
    public void profileToConstructorNavigationTest() {
        MainSite mainSite = new MainSite(driver);
        mainSite.clickAccountButton();
        UserAuthentication userAuthentication = new UserAuthentication(driver);
        userAuthentication.waitForPageLoad();
        userAuthentication.clickSiteLogo();
        mainSite.waitMainPageLoad();
        Assert.assertTrue("по клику на лого Конструктор не загрузился", driver.findElement(mainSite.burgerMainText).isDisplayed());
    }

    @Test
    @DisplayName("выход из аккаунта")
    @Description("проверка выхода из личного кабинета.")
    public void profileExitTest() {
        MainSite mainSite = new MainSite(driver);
        mainSite.clickAccountButton();
        UserAuthentication userAuthentication = new UserAuthentication(driver);
        userAuthentication.waitForPageLoad();
        userAuthentication.authUser(USER_EMAIL, USER_PASSWORD);
        mainSite.waitMainPageLoad();
        mainSite.clickAccountButton();
        UserAccount userAccount = new UserAccount(driver);
        userAccount.waitProfilePageLoad();
        userAccount.clickQuitButton();
        mainSite.waitInvisibilityAnimation();
        Assert.assertTrue("выход из аккаунта не состоялся", driver.findElement(userAuthentication.signInTitle).isDisplayed());
    }

    @After
    public void finish() {
        driver.quit();
    }

    @After
    public void deleteUser() {
        UserRegistrationApi userRegistrationAPI = new UserRegistrationApi();
        userRegistrationAPI.deleteUser(USER_EMAIL);
    }
}
