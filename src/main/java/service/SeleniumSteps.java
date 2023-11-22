package service;

import com.codeborne.selenide.Browsers;
import com.codeborne.selenide.Configuration;

import static com.codeborne.selenide.Selenide.open;


public class SeleniumSteps {

//    ChromeDriver driver;

    public SeleniumSteps() {
//        this.driver = new Driver().getDriver();
        initChromeDriver();
    }

    public void openLink(String link) {
        open(link);

    }

    private void initChromeDriver() {
//        setChromeDriverPath();
//        Configuration.baseUrl = uri;
        Configuration.browser = Browsers.CHROME;
        Configuration.headless = false;
        Configuration.pageLoadStrategy = "eager";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 25000;
    }

    public void closeDriver() {
//        driver.quit();
    }
}
