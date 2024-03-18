import pageobject.MainSite;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
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
public class UserPageTransitionsTest {

    private WebDriver driver;
    private String browserType;

    public UserPageTransitionsTest(String browserType) {
        this.browserType = browserType;
    }

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Chromewebdriver\\WebDriver\\bin\\yandexdriver.exe");
        ChromeOptions options = new ChromeOptions();
        if (browserType.equals("yandexdriver")) {
            options.setBinary("C:\\Users\\ПК\\AppData\\Local\\Yandex\\YandexBrowser\\Application\\browser.exe");
        }
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.navigate().to("https://stellarburgers.nomoreparties.site/");
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

    @Test
    @DisplayName("переход в Булки")
    @Description("проверка перехода в Булки")
    public void bunsBuilderTest() throws InterruptedException {
        MainSite mainSite = new MainSite(driver);
        mainSite.clickSauceButton();
        mainSite.clickBunButton();
        mainSite.testToppingBun();
    }

    @Test
    @DisplayName("переход в Начинки")
    @Description("проверка перехода в Начинки")
    public void fillingsBuilderTest() throws InterruptedException {
        MainSite mainSite = new MainSite(driver);
        mainSite.clickFillingButton();
        mainSite.testToppingFillings();
    }

    @Test
    @DisplayName("переход в Соусы")
    @Description("проверка перехода в Соусы")
    public void saucesBuilderTest() throws InterruptedException {
        MainSite mainSite = new MainSite(driver);
        mainSite.clickSauceButton();
        mainSite.testToppingSauce();
    }

    @After
    public void finish() {
        driver.quit();
    }

}