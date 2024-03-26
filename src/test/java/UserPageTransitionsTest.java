import pageobject.MainSite;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

import java.util.concurrent.TimeUnit;

public class UserPageTransitionsTest {

    private WebDriver driver;
    private final static String BASE_URL = "https://stellarburgers.nomoreparties.site/";

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
