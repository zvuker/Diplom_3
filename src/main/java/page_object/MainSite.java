package page_object;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import static org.junit.Assert.*;

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

    @Step("клик кнопка Войти в аккаунт'")
    public void clickLoginButton() {
        driver.findElement(signInButton).click();
        waitInvisibilityAnimation();
    }

    @Step("клик кнопка Личный Кабинет")
    public void clickAccountButton() {
        driver.findElement(accessButton).click();
        waitInvisibilityAnimation();
    }

    @Step("клик логотип")
    public void clickOnLogo() {
        driver.findElement(logo).click();
        waitInvisibilityAnimation();
    }

    @Step("клик кнопка Конструктор")
    public void clickOnConstructorButton() {
        driver.findElement(builderButton).click();
        waitInvisibilityAnimation();
    }


    @Step("клик кнопка Булки")
    public void clickBunButton() throws InterruptedException {
        Thread.sleep(500);
        driver.findElement(bunSelector).click();

    }
    @Step("клик кнопка Начинки")
    public void clickFillingButton() throws InterruptedException {
        Thread.sleep(500);
        driver.findElement(fillingsSelector).click();
    }

    @Step("клик кнопка Соуса")
    public void clickSauceButton() throws InterruptedException {
        Thread.sleep(500);
        driver.findElement(saucesSelector).click();
    }

    public void testToppingBun() throws InterruptedException {
        Thread.sleep(500);
        String countActivity = driver.findElement(activitySelector).getText();
        assertEquals("Булки", countActivity);
    }

    public void testToppingFillings() throws InterruptedException {
        Thread.sleep(1000);
        String countActivity = driver.findElement(activitySelector).getText();
        assertEquals(countActivity,"Начинки");
    }
    public void testToppingSauce() throws InterruptedException {
        Thread.sleep(500);
        String countActivity = driver.findElement(activitySelector).getText();
        assertEquals(countActivity,"Соусы");
    }

    @Step("ожидание главная, текст Соберите бургер")
    public void waitMainPageLoad() {
        new WebDriverWait(driver, 15)
                .until(ExpectedConditions.visibilityOfElementLocated(burgerMainText));
    }

    @Step("ожидание загрузка картинка и текст на главной")
    public void waitForLoadBunsHeader() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(bunImage));
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(bunsText));
    }

    @Step("ожидание загрузка картинка с соусом на главной")
    public void waitForLoadSaucesHeader() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(saucesImage));
        waitLoadPage();

    }

    @Step("ожидание загрузка начинки на главной")
    public void waitForLoadFillingsHeader() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(fillingsImage));
        waitLoadPage();
    }

    @Step("ожидание загрузки страницы, допожидание")
    public void waitLoadPage() {
        new WebDriverWait(driver, 20)
                .until((ExpectedCondition<Boolean>) wd ->
                        ((JavascriptExecutor) wd)
                                .executeScript("return document.readyState")
                                .equals("complete"));
    }

    @Step("ожидание загрузка страницы, анимация пропала")
    public void waitInvisibilityAnimation() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.invisibilityOfElementLocated
                        (By.xpath(".//img[@src='./static/media/loading.89540200.svg' and @alt='loading animation']")));
        waitLoadPage();
    }


}