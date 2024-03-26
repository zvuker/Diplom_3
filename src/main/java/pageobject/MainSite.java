package pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.qameta.allure.Step;

import static org.junit.Assert.assertEquals;

public class MainSite {

    private final WebDriver driver;
    private final By signInButton = By.xpath(".//button[text()='Войти в аккаунт']");
    private final By accessButton = By.xpath(".//a[@href='/account']");
    private final By logo = By.xpath(".//div/a[@href='/']");
    private final By builderButton = By.xpath(".//p[text()='Конструктор']");
    private final By bunSelector = By.xpath("//span[@class='text text_type_main-default'][text()='Булки']");
    private final By fillingsSelector = By.xpath("//span[@class='text text_type_main-default'][text()='Начинки']");
    private final By saucesSelector = By.xpath("//span[@class='text text_type_main-default'][text()='Соусы']");
    private final By activitySelector = By.xpath("//div[starts-with(@class,'tab_tab__1SPyG tab_tab_type_current__2BEPc')]//span");
    public By bunImage = By.xpath(".//img[@alt='Краторная булка N-200i']");
    public By bunsText = By.xpath(".//h2[text()='Булки']");
    public By saucesImage = By.xpath(".//p[text()='Соус с шипами Антарианского плоскоходца']");
    public By fillingsImage = By.xpath(".//img[@alt='Плоды Фалленианского дерева']");
    public By burgerMainText = By.xpath(".//section/h1[text()='Соберите бургер']");

    public MainSite(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Клик кнопка Войти в аккаунт")
    public void clickLoginButton() {
        driver.findElement(signInButton).click();
        waitInvisibilityAnimation();
    }

    @Step("Клик кнопка Личный Кабинет")
    public void clickAccountButton() {
        driver.findElement(accessButton).click();
        waitInvisibilityAnimation();
    }

    @Step("Клик логотип")
    public void clickOnLogo() {
        driver.findElement(logo).click();
        waitInvisibilityAnimation();
    }

    @Step("Клик кнопка Конструктор")
    public void clickOnConstructorButton() {
        driver.findElement(builderButton).click();
        waitInvisibilityAnimation();
    }


    @Step("Клик кнопка Булки")
    public void clickBunButton() {
        driver.findElement(bunSelector).click();
        waitLoadPage();
    }

    @Step("Клик кнопка Начинки")
    public void clickFillingButton() {
        driver.findElement(fillingsSelector).click();
        waitLoadPage();
    }

    @Step("Клик кнопка Соуса")
    public void clickSauceButton() {
        driver.findElement(saucesSelector).click();
        waitLoadPage();
    }

    public void testToppingBun() {
        String countActivity = driver.findElement(activitySelector).getText();
        assertEquals("Булки", countActivity);
    }

    public void testToppingFillings() {
        String countActivity = driver.findElement(activitySelector).getText();
        assertEquals("Начинки", countActivity);
    }

    public void testToppingSauce() {
        String countActivity = driver.findElement(activitySelector).getText();
        assertEquals("Соусы", countActivity);
    }

    @Step("проверка, что текст находится в разделе '{sectionName}'")
    public boolean isTextInSection(String sectionName) {
        String expectedClass = "tab_tab_type_current__2BEPc";
        String actualClass = driver.findElement(activitySelector).getAttribute("class");
        return actualClass.contains(expectedClass);
    }

    @Step("ожидание главной страницы, текст Соберите бургер")
    public void waitMainPageLoad() {
        new WebDriverWait(driver, 15)
                .until(ExpectedConditions.visibilityOfElementLocated(burgerMainText));
    }

    @Step("ожидание загрузки картинки и текста на главной странице")
    public void waitForLoadBunsHeader() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(bunImage));
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(bunsText));
    }

    @Step("ожидание загрузки картинки с соусом на главной странице")
    public void waitForLoadSaucesHeader() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(saucesImage));
        waitLoadPage();
    }

    @Step("ожидание загрузки начинки на главной странице")
    public void waitForLoadFillingsHeader() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(fillingsImage));
        waitLoadPage();
    }

    @Step("ожидание загрузки страницы, анимация пропала")
    public void waitInvisibilityAnimation() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.invisibilityOfElementLocated
                        (By.xpath(".//img[@src='./static/media/loading.89540200.svg' and @alt='loading animation']")));
        waitLoadPage();
    }

    @Step("ожидание загрузки страницы")
    public void waitLoadPage() {
        new WebDriverWait(driver, 20)
                .until(driver -> ((JavascriptExecutor) driver)
                        .executeScript("return document.readyState").equals("complete"));
    }
}
