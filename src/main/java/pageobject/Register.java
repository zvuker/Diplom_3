package pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class Register {

    private final WebDriver driver;

    private final By userName = By.xpath(".//div[./label[text()='Имя']]/input[@name='name']");
    private final By userEmail = By.xpath(".//div[./label[text()='Email']]/input[@name='name']");
    private final By userPassword = By.xpath(".//div[./label[text()='Пароль']]/input[@name='Пароль']");
    private final By singInButton = By.xpath(".//button[text()='Зарегистрироваться']");
    public final By wrongPasswordText = By.xpath(".//p[text()='Некорректный пароль']");
    public final By singInText = By.xpath(".//div/h2[text()='Регистрация']");


    public Register(WebDriver driver) {
        this.driver = driver;
    }

    public void setUsername(String name) {
        driver.findElement(userName).sendKeys(name);
    }

    public void setUserEmail(String email) {
        driver.findElement(userEmail).sendKeys(email);
    }

    public void setUserPassword(String password) {
        driver.findElement(userPassword).sendKeys(password);
    }

    public void clickSingInButton() {
        driver.findElement(singInButton).click();
        waitInvisibilityAnimation();
    }

    @Step("регистрация")
    public void registerUser(String name, String email, String password) {
        setUsername(name);
        setUserEmail(email);
        setUserPassword(password);
        clickSingInButton();
    }

    @Step("ожидание загрузки страницы после текста регистрация")
    public void waitRegisterPage() {
        new WebDriverWait(driver, 3)
                .until(ExpectedConditions.visibilityOfElementLocated(singInText));
    }

    @Step("ожидание загрузки страницы, анимация пропала")
    public void waitInvisibilityAnimation() {
        new WebDriverWait(driver, 5)
                .until(ExpectedConditions.invisibilityOfElementLocated
                        (By.xpath(".//img[@src='./static/media/loading.89540200.svg' and @alt='loading animation']")));
    }
}