package page_object;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class RecoverPassword {

    private final WebDriver driver;

    private final By userEmail = By.xpath(".//div[./label[text()='Email']]/input[@name='name']");
    public final By resetPassword = By.xpath(".//main/div/h2[text()='Восстановление пароля']");
    private final By recoverPasswordButton = By.xpath(".//form/button[text()='Восстановить']");
    private final By signInLink = By.xpath(".//div/p/a[@href = '/login' and text() = 'Войти']");


    public RecoverPassword(WebDriver driver) {
        this.driver = driver;
    }


    @Step("почта")
    public void setUserEmail(String email) {
        driver.findElement(userEmail).sendKeys(email);
    }

    @Step("клик кнопка Восстановить")
    public void clickRecoverButton() {
        driver.findElement(recoverPasswordButton).click();
        waitInvisibilityAnimation();
    }

    @Step("клик ссылка Войти")
    public void clickOnEnter() {
        driver.findElement(signInLink).click();
        waitInvisibilityAnimation();
    }

    @Step("пароль восстановление")
    public void recoverPassword(String email) {
        setUserEmail(email);
        clickRecoverButton();
    }

    @Step("ожидание загрузки страницы, анимация пропала")
    public void waitInvisibilityAnimation() {
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.invisibilityOfElementLocated
                        (By.xpath(".//img[@src='./static/media/loading.89540200.svg' and @alt='loading animation']")));
    }

    @Step("ожидание загрузки после восстановления пароля")
    public void waitRecoveryPageLoad() {
        new WebDriverWait(driver, 3)
                .until(ExpectedConditions.visibilityOfElementLocated(resetPassword));
    }
}
