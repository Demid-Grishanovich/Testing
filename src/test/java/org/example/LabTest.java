package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LabTest {
    private WebDriver driver;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "E:\\Univer/chromedriver.exe"); // Замените на свой путь к chromedriver
        driver = new ChromeDriver();
        driver.navigate().to("http://svyatoslav.biz/testlab/wt/");
    }

    @Test
    public void testMainPageContainsMenuAndBanners() {
        Assert.assertTrue(driver.getPageSource().contains("menu"));
        Assert.assertTrue(driver.getPageSource().contains("banners"));
    }

    @Test
    public void testFooterContainsCoolSoftBySomebody() {
        WebElement footer = driver.findElement(By.xpath("//td[contains(text(),'CoolSoft by Somebody')]"));
        Assert.assertTrue(footer.isDisplayed());
    }

    @Test
    public void testFormFieldsAreEmptyByDefault() {
        WebElement inputHeight = driver.findElement(By.name("height"));
        WebElement inputWeight = driver.findElement(By.name("weight"));
        List<WebElement> genders = driver.findElements(By.name("gender"));

        inputHeight.clear();
        Assert.assertEquals(inputHeight.getAttribute("value"), "");
        inputWeight.clear();
        Assert.assertEquals(inputWeight.getAttribute("value"), "");

        boolean genderSelected = false;
        for(WebElement gender : genders) {
            if(gender.isSelected()) {
                genderSelected = true;
                break;
            }
        }
        Assert.assertFalse(genderSelected);
    }

    @Test
    public void testFormDisappearsOnBodyMassIndexTooHigh() {
        driver.findElement(By.name("height")).sendKeys("50");
        driver.findElement(By.name("weight")).sendKeys("3");
        driver.findElement(By.cssSelector("input[type='submit'][value='Рассчитать']")).click();
        Assert.assertTrue(driver.getPageSource().contains("Слишком большая масса тела"));
    }

    @Test
    public void testFormExistsOnMainPage() {
        Assert.assertTrue(driver.findElement(By.name("height")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.name("weight")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.name("gender")).isDisplayed());
        WebElement button = driver.findElement(By.cssSelector("input[type='submit'][value='Рассчитать']"));
        Assert.assertTrue(button.isDisplayed());
    }


    @Test
    public void testInvalidHeightAndWeightInput() {
        driver.findElement(By.name("height")).sendKeys("40");
        driver.findElement(By.cssSelector("input[type='submit']")).click();
        Assert.assertTrue(driver.getPageSource().contains("Рост может быть в диапазоне 50-300 см"));

        driver.findElement(By.name("height")).clear();
        driver.findElement(By.name("height")).sendKeys("50");
        driver.findElement(By.name("weight")).sendKeys("2");
        driver.findElement(By.cssSelector("input[type='submit']")).click();
        Assert.assertTrue(driver.getPageSource().contains("Вес может быть в диапазоне 3-500 кг"));
    }

    @Test
    public void testMainPageContainsCurrentDate() {
        String currentDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        Assert.assertTrue(driver.getPageSource().contains(currentDate));
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
