package page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class HeadHunterPage {

    private final SelenideElement NAME = $(By.xpath("//h2[@data-qa='resume-personal-name']"));
    private final SelenideElement BIRTHDAY = $(By.xpath("//span[@data-qa='resume-personal-birthday']"));
    private final SelenideElement AGE = $(By.xpath("//span[@data-qa='resume-personal-age']"));
    private final SelenideElement ADDRESS = $(By.xpath("//span[@data-qa='resume-personal-address']"));
    private final SelenideElement EMAIL = $(By.xpath("//div[@data-qa='resume-contact-email']"));
    private final SelenideElement PHONE = $(By.xpath("//div[@data-qa='resume-contacts-phone']"));

    public String getName() {
        return NAME.getText();
    }

    public String getBirthday() {
        return BIRTHDAY.getText();
    }

    public String getAge() {
        return AGE.getText();
    }

    public String getAddress() {
        return ADDRESS.getText();
    }

    public String getEmail() {
        return EMAIL.getText();
    }

    public String getPhone() {
        return PHONE.getText();
    }


}
