package variousConcepts;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CRMTest {

	WebDriver driver;
	String browser;
	String url;

	By USERNAME_FIELD = By.xpath("//input[@id='username']");
	By PASSWORD_FIELD = By.xpath("//input[@id='password']");
	By SIGNIN_BUTTON_FIEL = By.xpath("/html/body/div/div/div/form/div[3]/button");
	By DASHBOARD_HEADER_FIELD = By.xpath("//h2[contains(text(), 'Dashboard')]");

	By CUSTOMER_MENU_FIELD = By.xpath("//span[contains(text(), 'Customers')]");
	By ADD_CUSTOMER_MENU_FIELD = By.xpath("//a[contains(text(), 'Add Customer')]");
	By ADD_CONTACT_HEADER_FIELD = By.xpath("//h5[text() ='Add Contact']");

	By FULL_NAME_FIELD = By.xpath("//input[@id = 'account']");
	By COMPANY_DROPDOWN_FIELD = By.xpath("//select[@name ='cid']");
	By EMAIL_FIELD = By.xpath("//input[@id ='email']");
	By PHONE_FIELD = By.xpath("//input[@id ='phone']");
	By ADDRESS_FIELD = By.xpath("//input[@id ='address']");
	By CITY_FIELD = By.xpath("//input[@id ='city']");
	By STATE_FIELD = By.xpath("//input[@id ='state']");
	By ZIP_FIELD = By.xpath("//input[@id ='zip']");
	By SAVE_FIELD = By.xpath("//button[@id ='submit']");
	By LIST_CUSTOMERS_FIELD = By.xpath("//a[contains(text(), 'List Customers')]");

	// login data
	String userName = "demo@techfios.com";
	String password = "abc123";
	
	//Test or Mock Data
	String fullName = "Jingren Shi";
	String Email = "jingrenshi1025@gmail.com";
	String phone = "123-456-7788";
	String address = "123 main street";
	String city = "Dallas";
	String state = "TX";
	String zip = "75011";

	@BeforeClass
	public void readConfig() {
		// 4 ways to read a file,(connect file to class)
		// scanner //BufferedRead // InputStream // FileReader
		Properties prop = new Properties();
		try {
			InputStream input = new FileInputStream("src\\main\\java\\config\\config.properties");
			prop.load(input);
			browser = prop.getProperty("browser");
			System.out.println("Browser used: " + browser);
			url = prop.getProperty("url");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@BeforeMethod
	public void init() {
		if (browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.driver", "driver\\geckodriver.exe");
			driver = new FirefoxDriver();
		}

		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	//@Test
	public void loginTest() {
		driver.findElement(USERNAME_FIELD).sendKeys(userName);
		driver.findElement(PASSWORD_FIELD).sendKeys(password);
		driver.findElement(SIGNIN_BUTTON_FIEL).click();

		String dashboardHeader = driver.findElement(DASHBOARD_HEADER_FIELD).getText();
		Assert.assertEquals(dashboardHeader, "Dashboard", "Dashboard page not found!!");

	}

    @Test
	public void addContact() throws InterruptedException {
		driver.findElement(USERNAME_FIELD).sendKeys(userName);
		driver.findElement(PASSWORD_FIELD).sendKeys(password);
		driver.findElement(SIGNIN_BUTTON_FIEL).click();

		String dashboardHeader = driver.findElement(DASHBOARD_HEADER_FIELD).getText();
		Assert.assertEquals(dashboardHeader, "Dashboard", "Dashboard page not found!!");

		driver.findElement(CUSTOMER_MENU_FIELD).click();	
		driver.findElement(ADD_CUSTOMER_MENU_FIELD).click();
		waitForElement(driver, 5, ADD_CONTACT_HEADER_FIELD);// calling the customized method to execute explicit wait. 
		
		String addContactHeader = driver.findElement(ADD_CONTACT_HEADER_FIELD).getText();
		System.out.println("================"+addContactHeader);
		Assert.assertEquals(addContactHeader, "Add Contact", "Add Contact page not found!!");
		
		Random rnd = new Random();
		int randomNumber = rnd.nextInt(999);
		driver.findElement(FULL_NAME_FIELD).sendKeys(fullName + randomNumber);
		Select sel = new Select(driver.findElement(COMPANY_DROPDOWN_FIELD));
		sel.selectByVisibleText("Techfios");
//		driver.findElement(EMAIL_FIELD).sendKeys(Email);
//		driver.findElement(PHONE_FIELD).sendKeys(phone);
		driver.findElement(ADDRESS_FIELD).sendKeys(address);
		driver.findElement(CITY_FIELD).sendKeys(city);
		driver.findElement(STATE_FIELD).sendKeys(state);
		driver.findElement(ZIP_FIELD).sendKeys(zip);
		driver.findElement(SAVE_FIELD).click();

	}

    // customized method for explicit wait.
	private void waitForElement(WebDriver driver, int timeInSecond, By locator) {
		WebDriverWait wait = new WebDriverWait(driver, timeInSecond);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		
	}

	// @AfterMethod
	public void tearDown() {
		driver.close();
	}
}
