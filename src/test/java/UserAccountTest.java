import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.*;
import pageobject.MainSite;
import pageobject.UserAuthentication;
import pageobject.UserAccount;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(Parameterized.class)
public class UserAccountTest {

    private WebDriver driver;
    private String browserType;
    private final static String userEmail = "2055@gmail.com";
    private final static String userPassword = "1234567";

    public UserAccountTest(String browserType) {
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
        userAuthentication.authUser(userEmail, userPassword);
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

    public void deleteUser() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        Response response = RestAssured.given()
                .queryParam("email", userEmail)
                .when()
                .delete("/api/users");

        Assert.assertEquals(200, response.getStatusCode());
    }
}