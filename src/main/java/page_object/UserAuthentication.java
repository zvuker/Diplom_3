package page_object;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;


public class UserAuthentication {

    private final WebDriver driver;

    public final By signInTitle = By.xpath(".//main/div/h2[text()='Вход']");
    private final By userEmail = By.xpath(".//div[@class='input pr-6 pl-6 input_type_text input_size_default']/input[@name='name']");
    private final By userPassword = By.xpath(".//div[@class='input pr-6 pl-6 input_type_password input_size_default']/input[@name='Пароль']");
    private final By signInButton = By.xpath(".//button[text()='Войти']");
    public final By accessButton = By.xpath(".//div/a[@href='/']");
    private final By createBurgerButton = By.xpath(".//a/p[text()='Конструктор']");
    private final By signUpLink = By.xpath(".//a[@href='/register' and text()='Зарегистрироваться']");
    private final By resetPasswordLink = By.xpath(".//a[@href='/forgot-password' and text()='Восстановить пароль']");


    public UserAuthentication(WebDriver driver) {
        this.driver = driver;
    }

    @Step("почта")
    public void setUserEmail(String email) {
        driver.findElement(userEmail).sendKeys(email);
    }

    @Step("пароль")
    public void setUserPassword(String password) {
        driver.findElement(userPassword).sendKeys(password);
    }

    @Step("клик кнопка Войти")
    public void clickEnterButton() {
        driver.findElement(signInButton).click();
        waitInvisibilityAnimation();
    }

    @Step("клик ссылка Зарегистрироваться")
    public void clickRegisterButton() {
        driver.findElement(signUpLink).click();
        waitInvisibilityAnimation();
    }
    @Step("клик кнопка Конструктор")
    public void clickBuildButton() {
        driver.findElement(createBurgerButton).click();
        waitInvisibilityAnimation();
    }

    @Step("клик ссылка Восстановить пароль")
    public void clickResetPasswordLink() {
        driver.findElement(resetPasswordLink).click();
        waitInvisibilityAnimation();
    }

    @Step("клик кнопка Stella Burgers")
    public void clickSiteLogo() {
        driver.findElement(accessButton).click();
        waitInvisibilityAnimation();
    }

    @Step("авторизация")
    public void authUser(String email, String password) {
        setUserEmail(email);
        setUserPassword(password);
        clickEnterButton();
    }

    @Step("ожидание загрузки страницы, текст Вход")
    public void waitForPageLoad() {
        new WebDriverWait(driver, 3)
                .until(ExpectedConditions.visibilityOfElementLocated(signInTitle));
    }

    @Step("ожидание загрузки страницы, анимация пропала")
    public void waitInvisibilityAnimation() {
        new WebDriverWait(driver, 5)
                .until(ExpectedConditions.invisibilityOfElementLocated
                        (By.xpath(".//img[@src='./static/media/loading.89540200.svg' and @alt='loading animation']")));
    }
}
