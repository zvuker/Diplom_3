package pageobject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;


public class UserAccount {

    private final WebDriver driver;
    private final By createBurgerButton = By.xpath(".//a[@href='/']/p[text()='Конструктор']");
    private final By signOutButton = By.xpath(".//li/button[text()='Выход']");
    public final By profilePageText = By.xpath(".//nav/p[text()='В этом разделе вы можете изменить свои персональные данные']");


    public UserAccount(WebDriver driver) {
        this.driver = driver;
    }


    @Step("клик кнопка Конструктор")
    public void clickOnConstructorButton() {
        driver.findElement(createBurgerButton).click();
        waitInvisibilityAnimation();
    }

    @Step("клик кнопка выйти")
    public void clickQuitButton() {
        driver.findElement(signOutButton).click();
        waitInvisibilityAnimation();
    }

    @Step("ожидание загрузки страницы, анимация пропала")
    public void waitInvisibilityAnimation() {
        new WebDriverWait(driver, 5)
                .until(ExpectedConditions.invisibilityOfElementLocated
                        (By.xpath(".//img[@src='./static/media/loading.89540200.svg' and @alt='loading animation']")));
    }

    @Step("ожидание загрузки кабинета с текстом")
    public void waitProfilePageLoad() {
        new WebDriverWait(driver, 3)
                .until(ExpectedConditions.visibilityOfElementLocated(profilePageText));
    }
}