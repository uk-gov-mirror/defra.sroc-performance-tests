// SRoC project - Automated regression script for the Tactical Charging Module (TCM)

// 
// Author: Paul Murdock
// Date: 03/10/2018
//
// Charging module version: 2.6
///////////////////////////////////////////////////////////////////////////////////
package package_SRoC;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.apache.commons.codec.binary.Base64;
import java.util.concurrent.ThreadLocalRandom;

public class SRoC_Allregimes_TEST {

	public static void main(final String[] args) throws InterruptedException {

		// declaration and instantiation of objects/variables
		System.setProperty("webdriver.gecko.driver",
				"C:\\Users\\pmurdock\\Desktop\\MyFiles - not backed up\\geckodriver-v0.21.0-win32\\geckodriver.exe");
		System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
		WebDriver driver = new FirefoxDriver();

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// launch Firefox and direct it to the TCM login page (dev)
		//driver.get("https://sroc-dev.aws.defra.cloud/auth/sign_in");

		// launch Firefox and direct it to the NEW TCM DEV login page
		// driver.get("https://tcm-dev.aws.defra.cloud/auth/sign_in");

		// launch Firefox and direct it to the NEW TCM TEST login page
		//driver.get("https://tcm-tst.aws.defra.cloud/auth/sign_in");

		// launch Firefox and direct it to the NEW TCM PRE-PROD login page
		driver.get("https://tcm-pre.aws.defra.cloud/auth/sign_in");

		// launch Firefox and direct it to the TCM login page (test)s
		// driver.get("https://sroc-tst.aws.defra.cloud/");

		// launch Firefox and direct it to the TCM login page (pre-prod)
		// driver.get("https://sroc-pre.aws.defra.cloud/");

		// launch Firefox and direct it to the TCM login page (prod)
		//driver.get("https://tcm-prd.aws.defra.cloud/");

		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")));

		if (driver.findElement(By.tagName("h1")).getText().equals("Sign in")) {
			System.out.println("[CHECK] TCM login page displayed");

			// driver.manage().window().maximize();

			// Type in Username

			wait = new WebDriverWait(driver, 15);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user_email")));
			driver.findElement(By.id("user_email")).sendKeys("paul.murdock@environment-agency.gov.uk");

			String encodePW = "Qm9ybzU2Nzg=";
			// Type in password
			driver.findElement(By.id("user_password")).sendKeys(decodeStr(encodePW));

			// Click the <Submit> button
			driver.findElement(By.name("commit")).click();

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("navbarUserMenuLink")));

			// Check log in successful and 'Transactions To Be Billed' screen displayed
			if (driver.findElement(By.tagName("h1")).getText().equals("Transactions to be billed")) {
				System.out.println("[CHECK] TCM login successful");

				// Signed in as
				String signIn = driver.findElement(By.id("navbarUserMenuLink")).getText();
				System.out.println("[CHECK] " + signIn);

				// Select Water Quality from regime dropdown menu item
				driver.findElement(By.id("navbarRegimeSelectorLink")).click();
				driver.findElement(By.linkText("Water Quality")).click();

				System.out.println("WATER QUALITY REGIME");

				// Select 'Transactions to be billed'
				driver.findElement(By.id("navbarTransactionsSelectorLink")).click();
				driver.findElement(By.linkText("Transactions to be billed")).click();
				System.out.println("[CHECK - Water Quality] 'Transactions to be billed' selected");

				// Confirm at least one transaction present on TTBB for WQ

				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("table-responsive")));

				WebElement baseTable = driver.findElement(By.className("table-responsive"));
				int tableRows = baseTable.findElements(By.xpath("//tbody/tr")).size();
				System.out.println("[DATA - Water Quality] Number of table rows found on current page: " + tableRows);

				if (tableRows > 0) {
					System.out.println("[CHECK - Water Quality] Transaction found");

					WebElement baseTableWQ = driver.findElement(By.className("table-responsive"));
					baseTableWQ = driver.findElement(By.className("table-responsive"));
					WebElement tableHeaderWQ = baseTableWQ.findElement(By.xpath("//table/thead"));

					// Check region selected
					Select select = new Select(driver.findElement(By.id("region")));
					WebElement option = select.getFirstSelectedOption();
					String region = option.getText();
					System.out.println("[CHECK - Water Quality] Region selected is " + region);

					// Set Financial Year to 'All'
					Select financialYear = new Select(driver.findElement(By.id("fy")));
					financialYear.selectByValue("all");
					System.out.println("[CHECK - Water Quality] Financial year set to 'All'");

					// Select 50 records from 'Items per page' drop-down
					Select itemsperpage = new Select(driver.findElement(By.id("per_page")));
					itemsperpage.selectByValue("50");
					System.out.println("[CHECK - Water Quality] 50 items per page selected");

					// Check expected columns displayed and column sorting
					// Sort by File Reference
					Thread.sleep(1000);
					wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tbody/tr")));
					baseTableWQ = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[2]/a")).click();
					System.out.println("[CHECK - Water Quality] Sort by 'File Reference' (asc)");
					Thread.sleep(1000);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWQ = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[2]/a")).click();
					System.out.println("[CHECK - Water Quality] Sort by 'File Reference' (desc)");

					// Sort by File Date
					Thread.sleep(1000);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWQ = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[3]/a")).click();
					System.out.println("[CHECK - Water Quality] Sort by 'File Date' (asc)");
					Thread.sleep(1000);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWQ = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[3]/a")).click();
					System.out.println("[CHECK - Water Quality] Sort by 'File Date' (desc)");

					// Sort by Customer
					Thread.sleep(1000);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWQ = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[4]/a")).click();
					System.out.println("[CHECK - Water Quality] Sort by 'Customer' (asc)");
					Thread.sleep(1000);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWQ = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[4]/a")).click();
					System.out.println("[CHECK - Water Quality] Sort by 'Customer' (desc)");

					// Sort by Consent
					Thread.sleep(1000);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWQ = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[5]/a")).click();
					System.out.println("[CHECK - Water Quality] Sort by 'Consent' (asc)");
					Thread.sleep(1000);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWQ = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[5]/a")).click();
					System.out.println("[CHECK - Water Quality] Sort by 'Consent' (desc)");

					Thread.sleep(1000);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWQ = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[6]/span[1]")).isDisplayed();
					System.out.println("[CHECK - Water Quality] Version column displayed");

					driver.findElement(By.xpath("//table/thead/tr/th[7]/span[1]")).isDisplayed();
					System.out.println("[CHECK - Water Quality] Discharge column displayed");

					// Sort by Percentage discount
					Thread.sleep(1000);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWQ = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[10]/a")).click();
					System.out.println("[CHECK - Water Quality] Sort by 'Percentage discount' (asc)");
					Thread.sleep(1000);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWQ = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[10]/a")).click();
					System.out.println("[CHECK - Water Quality] Sort by 'Percentage discount' (desc)");

					// Sort by Charge Period
					Thread.sleep(1000);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWQ = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[12]/a")).click();
					System.out.println("[CHECK - Water Quality] Sort by 'Charge period' (asc)");
					Thread.sleep(1000);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWQ = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[12]/a")).click();
					System.out.println("[CHECK - Water Quality] Sort by 'Charge period' (desc)");

					// Sort by category
					Thread.sleep(1000);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWQ = driver.findElement(By.className("table-responsive"));
					tableHeaderWQ = baseTableWQ.findElement(By.xpath("//table/thead/tr/th[8]/a"));
					tableHeaderWQ.click();
					System.out.println("[CHECK - Water Quality] Sort by 'Permit Category' (asc)");
					Thread.sleep(2000);

					// Ensure record on first row does not have a charge. if it does, re-sort by
					// category

					baseTableWQ = driver.findElement(By.className("table-responsive"));
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					WebElement cell13Value = baseTableWQ.findElement(By.xpath("//table/tbody/tr[1]/td[13]"));
					String chargeValue = cell13Value.getText();
					System.out.println("[DATA - Water Quality] Charge value : " + chargeValue);
					if (chargeValue.matches(".*\\d+.*")) {
						Thread.sleep(1000);
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
						baseTableWQ = driver.findElement(By.className("table-responsive"));
						tableHeaderWQ = baseTableWQ.findElement(By.xpath("//table/thead/tr/th[8]/a"));
						tableHeaderWQ.click();
						System.out.println("[CHECK - Water Quality] Sort by 'Permit Category' (Desc)");
					}

					Thread.sleep(1000);
					// Find first row of table
					baseTableWQ = driver.findElement(By.className("table-responsive"));

					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table/tbody/tr[1]")));

					if (driver.findElements(By.xpath("//table/tbody/tr[1]")).size() != 0) {
						WebElement tableRowWQ = baseTableWQ.findElement(By.xpath("//table/tbody/tr[1]"));
						String rowtextWQ = tableRowWQ.getText();
						System.out.println("[DATA - Water Quality] Transaction found on TTBB with charge row detail: "
								+ rowtextWQ);
					}
					
					// If first transaction is excluded then re-instate it

					baseTableWQ = driver.findElement(By.className("table-responsive"));
					WebElement tableRow1 = baseTableWQ.findElement(By.xpath("//table/tbody/tr[1]"));
					tableRow1.findElement(By.xpath("//button[@class='btn btn-sm show-details-button']"))
							.click();

					// Check whether transaction already excluded, if so re-instate it
					int isPresent = driver
							.findElements(By.xpath(
									"//input[@class='btn btn-success' and @value = 'Reinstate for Billing']"))
							.size();
					if (isPresent > 0) {
						driver.findElement(By.xpath(
								"//input[@class='btn btn-success' and @value = 'Reinstate for Billing']"))
								.click();
						Thread.sleep(1000);
					}
					
					// Click the <Back> link to return to TTBB
					driver.findElement(By.xpath("//a[@class='back-link']")).click();

					// Pause to allow sufficient time for TTBB to be re-displayed
					Thread.sleep(2000);
					
					// After exclusion, ensure record on first row does not have a charge. if it does, re-sort by
					// category

					baseTableWQ = driver.findElement(By.className("table-responsive"));
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					cell13Value = baseTableWQ.findElement(By.xpath("//table/tbody/tr[1]/td[13]"));
					chargeValue = cell13Value.getText();
					System.out.println("[DATA - Water Quality] Charge value : " + chargeValue);
					if (chargeValue.matches(".*\\d+.*")) {
						Thread.sleep(1000);
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
						baseTableWQ = driver.findElement(By.className("table-responsive"));
						tableHeaderWQ = baseTableWQ.findElement(By.xpath("//table/thead/tr/th[8]/a"));
						tableHeaderWQ.click();
						System.out.println("[CHECK - Water Quality] Sort by 'Permit Category' (Desc)");
					}

					Thread.sleep(1000);
					

					// Get ConsentRef
					baseTableWQ = driver.findElement(By.className("table-responsive"));
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					WebElement cell5Value = baseTableWQ.findElement(By.xpath("//table/tbody/tr[1]/td[5]"));
					String permitRef = cell5Value.getText();
					System.out.println("[DATA - Water Quality] Consent reference : " + permitRef);

					// Search TTBB by stored consent reference
					driver.findElement(By.id("search")).sendKeys(permitRef + "");
					driver.findElement(By.xpath("//input[@class='btn btn-outline-primary']")).click();
					Thread.sleep(2000);

					// Check records found
					if (driver.findElement(By.xpath("//*[contains(text(), 'matching entries')]")).isDisplayed()) {
						System.out.println("[CHECK - Water Quality] Search returned one or more records as expected");
					} else {
						System.out.println("[ERROR!! - Water Quality] Search did NOT return expected result");
					}

					// Set permit category on transaction
					int randomNum = ThreadLocalRandom.current().nextInt(1, 75);

					// Thread.sleep(2000);
					baseTableWQ = driver.findElement(By.className("table-responsive"));
					wait.until(ExpectedConditions.visibilityOfElementLocated(
							By.xpath("//input[@class='tcm-select-input' and @value = '']")));
					baseTableWQ.findElement(By.xpath("//input[@class='tcm-select-input' and @value = '']")).click();
					Thread.sleep(1000);
					int iT = 1;
					while (iT <= randomNum) {
						baseTableWQ.findElement(By.xpath("//input[@class='tcm-select-input' and @value = '']"))
								.sendKeys(Keys.DOWN);
						iT++;
					}
					Thread.sleep(200);
					baseTableWQ.findElement(By.xpath("//input[@class='tcm-select-input' and @value = '']"))
							.sendKeys(Keys.RETURN);

					// Pause to allow sufficient time for Rules Service to calculate charge
					Thread.sleep(2000);

					// Check that a charge has been successfully generated
					baseTableWQ = driver.findElement(By.className("table-responsive"));
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table/tbody/tr[1]")));

					// Find first row of table
					WebElement tableRow = baseTableWQ.findElement(By.xpath("//table/tbody/tr[1]"));
					String rowtext = tableRow.getText();
					System.out.println("[DATA] Transaction with charge row detail: " + rowtext);

					// Sort transactions in TTBB by Category

					baseTableWQ = driver.findElement(By.className("table-responsive"));
					tableHeaderWQ = baseTableWQ.findElement(By.xpath("//table/thead/tr/th[8]/a"));
					tableHeaderWQ.click();
					Thread.sleep(1000);

					// Find charge value
					baseTableWQ = driver.findElement(By.className("table-responsive"));
					cell13Value = baseTableWQ.findElement(By.xpath("//table/tbody/tr[1]/td[13]"));
					chargeValue = cell13Value.getText();
					System.out.println("[DATA] Charge value : " + chargeValue);

					// Check Rules Service has returned a charge value
					if (chargeValue.matches(".*\\d+.*")) {
						System.out.println("[CHECK - Water Quality] Charge successfully generated");
					} else {
						System.out.println("[ERROR!! - Water Quality] Charge value not found! ");
					}

					// Select "Transaction Detail" page for permit
					baseTable = driver.findElement(By.className("table-responsive"));
					tableRow = baseTable.findElement(By.xpath("//table/tbody/tr[1]"));
					tableRow.findElement(By.xpath("//button[@class='btn btn-sm show-details-button']")).click();

					// Pause to allow sufficient time to display page
					Thread.sleep(2000);

					// Check "Transaction Detail" page displayed
					if (driver.findElement(By.tagName("h1")).getText().equals("Transaction detail")) {
						System.out.println("[CHECK - Water Quality] Transaction Detail page displayed");

						// Check "Suggested category" section displayed
						if (driver.findElement(By.tagName("h2")).getText().equals("Suggested category")) {
							System.out.println(
									"[CHECK - Water Quality] Transaction Detail page - Suggested category section displayed");
						} else {
							System.out.println(
									"[WARNING!! - Water Quality] Transaction Detail page - Suggested category section NOT displayed");
						}

						// View Change History for Transaction

						driver.findElement(By.xpath("//a[@class='btn btn-outline-info']")).click();

						// Pause to allow sufficient time to display page
						Thread.sleep(2000);

						if (driver.findElement(By.tagName("h1")).getText().equals("Transaction change history")) {
							System.out.println("[CHECK - Water Quality] Transaction Change History page displayed");

							baseTableWQ = driver.findElement(By.className("table-responsive"));

						wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table/tbody/tr[1]")));

							if (driver.findElements(By.xpath("//table/tbody/tr[1]")).size() != 0) {
								WebElement tableRowWQ = baseTableWQ.findElement(By.xpath("//table/tbody/tr[1]"));
								String rowtextWQ = tableRowWQ.getText();
								System.out.println("[DATA - Water Quality] First audit event row: " + rowtextWQ);
						}
							// Click the 'Back' link from Transaction Change History
							driver.findElement(By.xpath("//a[@class='back-link']")).click();

							// Scroll down page
							JavascriptExecutor jse = (JavascriptExecutor) driver;
							jse.executeScript("window.scrollBy(0,900)", "");

							int h2tagelements = driver.findElements(By.tagName("h2")).size();
							System.out.println("[CHECK - Water Quality] h2 TAGS FOUND = " + h2tagelements);

							if (h2tagelements > 1) {
								List<WebElement> h2tags = driver.findElements(By.tagName("h2"));
								WebElement h2tag1 = h2tags.get(1);

								// System.out.println("[CHECK - Water Quality] h2 TAGS FOUND = "+h2tag1);
								if (h2tag1.getText().equals("Related billed transactions")) {
									System.out.println(
											"[CHECK - Water Quality] Transaction Detail page - Related billed transactions section displayed");

									if (h2tagelements > 2) {
										WebElement h2tag2 = h2tags.get(2);
										if (h2tag2.getText().equals("Related unbilled transactions")) {
											System.out.println(
													"[CHECK - Water Quality] Transaction Detail page - Related unbilled transactions section displayed");
										}
									}
								}
							}
							// Click the <Back> link to return to TTBB
							driver.findElement(By.xpath("//a[@class='back-link']")).click();

							// Pause to allow sufficient time for TTBB to be re-displayed
							Thread.sleep(2000);

							// Check Export button displayed
							if (driver.findElement(By.cssSelector("button.btn-sm.btn-outline-secondary"))
									.isDisplayed()) {
								System.out.println("[CHECK - Water Quality] Export Transactions button displayed");
								driver.findElement(By.cssSelector("button.btn-sm.btn-outline-secondary")).click();
								Thread.sleep(1000);
								// Check 'Export Transaction Data' modal displayed
								driver.switchTo().activeElement();
								if (driver.findElement(By.tagName("h5")).getText().equals("Export Transaction Data")) {
									System.out.println(
											"[CHECK - Water Quality] 'Export Transaction Data' modal displayed");
									// Cancel
									driver.findElement(By.xpath("//button[@class='btn btn-secondary']")).click();
									Thread.sleep(1000);
								} else {
									System.out.println(
											"[ERROR!! - Water Quality] 'Export Transaction Data' modal NOT displayed");
								}
							} else {
								System.out
										.println("[ERROR!! - Water Quality] Export Transactions button NOT displayed");
							}

							// Sort transactions in TTBB by Consent

							baseTableWQ = driver.findElement(By.className("table-responsive"));
							tableHeaderWQ = baseTableWQ.findElement(By.xpath("//table/thead/tr/th[5]/a"));
							tableHeaderWQ.click();
							Thread.sleep(1000);

							// Set the 'Temporary cessation' flag to 'Y' for the first transaction
							baseTableWQ = driver.findElement(By.className("table-responsive"));

							isPresent = baseTableWQ
									.findElements(
											By.xpath("//select[@class='form-control temporary-cessation-select']"))
									.size();
							if (isPresent > 0) {
								Select dropdown = new Select(baseTableWQ.findElement(
										By.xpath("//select[@class='form-control temporary-cessation-select']")));
								dropdown.selectByValue("true");
								System.out.println("[DATA - Water Quality] Temporary cessation flag set to 'Y'");
							}

							// Exclude transaction

							baseTableWQ = driver.findElement(By.className("table-responsive"));
							WebElement tableRow2 = baseTableWQ.findElement(By.xpath("//table/tbody/tr[1]"));
							tableRow2.findElement(By.xpath("//button[@class='btn btn-sm show-details-button']"))
									.click();

							// Check whether transaction already excluded, if so re-instate it
							isPresent = driver
									.findElements(By.xpath(
											"//input[@class='btn btn-success' and @value = 'Reinstate for Billing']"))
									.size();
							if (isPresent > 0) {
								driver.findElement(By.xpath(
										"//input[@class='btn btn-success' and @value = 'Reinstate for Billing']"))
										.click();
								Thread.sleep(1000);
							}
							driver.findElement(By.xpath("//button[@class='btn btn-danger exclude-button']")).click();

							Thread.sleep(1000);

							// Check 'Exclude Transaction' modal displayed
							if (driver.findElement(By.tagName("h5")).getText().equals("Exclude Transaction")) {
								System.out.println("[CHECK - Water Quality] 'Exclude Transaction' modal displayed");

								// Click "Exclude Transaction" button in modal
								driver.findElement(By.xpath("//input[@class='btn btn-danger']")).click();

								// Confirm transaction successfully marked for exclusion

								if (driver.findElement(By.xpath("//span[@class='badge badge-pill badge-danger']"))
										.isDisplayed()) {
									System.out.println("[CHECK - Water Quality] Transaction 'Marked for exclusion'");
								} else {
									System.out.println(
											"[ERROR!! - Water Quality] Transaction NOT 'Marked for exclusion'!");
								}

								// Confirm 'Reinstate for billing' button displayed

								if (driver.findElement(By.xpath("//input[@value='Reinstate for Billing']"))
										.isDisplayed()) {
									System.out.println("[CHECK - Water Quality] 'Reinstate for Billing' buttton found");
								} else {
									System.out.println(
											"[ERROR!! - Water Quality] 'Reinstate for Billing' buttton NOT found!");
								}

								// Click the <Back> link to return to TTBB
								driver.findElement(By.xpath("//a[@class='back-link']")).click();
								Thread.sleep(500);
								baseTable = driver.findElement(By.className("table-responsive"));
								driver.findElement(By.xpath("//table/thead/tr/th[8]/a")).click();
								System.out.println("[CHECK - Water Quality] Sort by 'Category' (asc)");

								// Set a charge for any remaining transactions for the same Consent reference
								Thread.sleep(1000);
								baseTableWQ = driver.findElement(By.className("table-responsive"));

								int elemcount = baseTableWQ
										.findElements(By.xpath("//input[@class='tcm-select-input' and @value = '']"))
										.size();
								System.out.println("[CHECK - Water Quality] " + elemcount
										+ " records in same group do not have a charge");
								Thread.sleep(1000);
								baseTableWQ = driver.findElement(By.className("table-responsive"));
								List<WebElement> elements = baseTableWQ
										.findElements(By.xpath("//input[@class='tcm-select-input' and @value = '']"));
								Thread.sleep(1000);
								java.util.Iterator<WebElement> i = elements.iterator();
								while (i.hasNext()) {
									baseTableWQ = driver.findElement(By.className("table-responsive"));
									elements = baseTableWQ.findElements(
											By.xpath("//input[@class='tcm-select-input' and @value = '']"));
									WebElement element = i.next();

									int isThere = driver
											.findElements(
													By.xpath("//input[@class='tcm-select-input' and @value = '']"))
											.size();
									if (isThere > 0) {
										baseTableWQ = driver.findElement(By.className("table-responsive"));

										// Set permit category on first transaction
										randomNum = ThreadLocalRandom.current().nextInt(1, 80);

										Thread.sleep(1000);
										baseTableWQ = driver.findElement(By.className("table-responsive"));
										wait = new WebDriverWait(driver, 15);
										wait.until(ExpectedConditions.visibilityOfElementLocated(
												By.xpath("//input[@class='tcm-select-input' and @value = '']")));
										baseTableWQ
												.findElement(
														By.xpath("//input[@class='tcm-select-input' and @value = '']"))
												.click();
										Thread.sleep(100);
										iT = 1;
										while (iT <= randomNum) {
											baseTableWQ
													.findElement(By.xpath(
															"//input[@class='tcm-select-input' and @value = '']"))
													.sendKeys(Keys.DOWN);
											// Thread.sleep(50);
											iT++;
										}

										baseTableWQ
												.findElement(
														By.xpath("//input[@class='tcm-select-input' and @value = '']"))
												.sendKeys(Keys.RETURN);
										Thread.sleep(2000);
										System.out.println(
												"[CHECK - Water Quality] Charge set for consent in same group");
									}
								}
							}
							
							// Click 'Un-approved Only button
							

							// Click 'Approve All' button
							driver.findElement(By.xpath("//button[@class='btn btn-success approve-all-btn']"))
									.click();
							System.out.println("[CHECK - Water Quality] 'Approve All' button clicked");
							Thread.sleep(2000);

							// GENERATE TRANSACTION FILE
							// Click Generate Transaction File button
							wait = new WebDriverWait(driver, 15);
							wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tbody/tr")));

							driver.findElement(
									By.xpath("//button[@class='btn btn-primary generate-transaction-file-btn mr-4']"))
									.click();

							Thread.sleep(1000);

							// Confirm Transaction File Summary displayed
							driver.switchTo().activeElement();
							if (driver.findElement(By.className("modal-header")).isEnabled()) {
								System.out.println("[CHECK - Water Quality] Transaction File Summary displayed");

								// Click "I have checked the summary and wish to proceed" checkbox
								if (driver.findElement(By.xpath("//input[@class='form-check-input ml-2']"))
										.isEnabled()) {
									driver.findElement(By.xpath("//input[@class='form-check-input ml-2']")).click();
									System.out.println(
											"[CHECK - Water Quality] 'I have checked the summary and wish to proceed' checkbox clicked");
									Thread.sleep(1000);
								} else {
									System.out.println(
											"[WARNING!! - Water Quality] 'I have checked the summary and wish to proceed' checkbox is not enabled");
								}

								Thread.sleep(1000);

								if (driver.findElement(By.xpath("//input[@class='btn btn-primary file-generate-btn']"))
										.isEnabled()) {
									// Click Generate Transaction File button
									driver.findElement(By.xpath("//input[@class='btn btn-primary file-generate-btn']"))
											.click();
									System.out.println(
											"[CHECK - Water Quality] Generate Transaction File button clicked");

									Thread.sleep(2000);
									// Confirm file generated successfully
									String message = driver
											.findElement(By.xpath(
													"//div[@class='alert alert-success alert-dismissable fade show']"))
											.getText();
									System.out.println("[CHECK - Water Quality]:" + message);

								} else {
									// No records included in file. Click 'Cancel' button
									driver.findElement(By.xpath("//button[@class='btn btn-secondary']")).click();
									System.out.println(
											"[WARNING!! - Water Quality] No approved records in File Summary. File not generated.");
									Thread.sleep(1000);
								}

							} else {
								System.out.println("[ERROR!! - Water Quality] Transaction File Summary NOT displayed!");
							}

						} else {
							System.out.println(
									"[ERROR!! - Water Quality] Transaction Change History page NOT displayed!");
						}

					} else {
						System.out.println("[ERROR!! - Water Quality] Transaction Detail page NOT displayed!");
					}

				} else {
					System.out.println("[WARNING!! - Water Quality] No transactions found on TTBB");
				}

				// Select Transaction File History for Water Quality
				driver.findElement(By.id("navbarTransactionsSelectorLink")).click();
				driver.findElement(By.linkText("Transaction File History")).click();

				Thread.sleep(2000);

				// Check Transaction File History screen displayed
				if (driver.findElement(By.tagName("h1")).getText().equals("Transaction File History")) {
					System.out.println("[CHECK - Water Quality] Transaction File History screen displayed");
				} else {
					System.out.println("[ERROR!! -Water Quality] Transaction File History screen NOT displayed");
				}

				// Set Region to 'All'
				Select dropdown = new Select(driver.findElement(By.id("region")));
				dropdown.selectByVisibleText("All");
				System.out.println("[CHECK - Water Quality] Transaction File History - region filter set to 'All'");

				Thread.sleep(1000);

				// Set Pre/Post-SRoC to 'All'
				dropdown = new Select(driver.findElement(By.id("prepost")));
				dropdown.selectByVisibleText("Post");
				System.out.println(
						"[CHECK - Water Quality] Transaction File History - Pre/Post-SRoC filter set to 'Post'");

				Thread.sleep(1000);

				// Sort records by 'Date Generated' (desc)

				Thread.sleep(1000);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
				WebElement baseTableWQ = driver.findElement(By.className("table-responsive"));
				driver.findElement(By.xpath("//table/thead/tr/th[2]/a")).click();
				Thread.sleep(1000);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
				baseTableWQ = driver.findElement(By.className("table-responsive"));
				driver.findElement(By.xpath("//table/thead/tr/th[2]/a")).click();
				System.out.println("[CHECK - Water Quality] Sort by 'Date Generated' (desc)");

				Thread.sleep(1000);

				if (driver.findElements(By.xpath("//table/tbody/tr[1]")).size() != 0) {
					baseTableWQ = driver.findElement(By.className("table-responsive"));
					WebElement tableRowWQ = baseTableWQ.findElement(By.xpath("//table/tbody/tr[1]"));
					String rowtextWQ = tableRowWQ.getText();
					System.out.println("[DATA - Water Quality] Transaction File History record found: " + rowtextWQ);
				} else {
					System.out.println("[WARNING!! - Water Quality] No Transaction File History records found");
				}

				// Select Excluded Transactions

				driver.findElement(By.id("navbarTransactionsSelectorLink")).click();
				driver.findElement(By.linkText("Excluded Transactions")).click();

				Thread.sleep(2000);

				// Check Excluded Transactions screen displayed
				if (driver.findElement(By.tagName("h1")).getText().equals("Excluded Transactions")) {
					System.out.println("[CHECK - Water Quality] Excluded Transactions screen displayed");
				} else {
					System.out.println("[ERROR!! -Water Quality] Excluded Transactions screen NOT displayed");
				}

				// Find first row of 'Excluded Transactions' table

				if (driver.findElements(By.xpath("//table/tbody/tr[1]")).size() != 0) {
					baseTableWQ = driver.findElement(By.className("table-responsive"));
					WebElement tableRowWQ = baseTableWQ.findElement(By.xpath("//table/tbody/tr[1]"));
					String rowtextWQ = tableRowWQ.getText();
					System.out
							.println("[DATA - Water Quality] Excluded Transaction record found with charge row detail: "
									+ rowtextWQ);
				} else {
					System.out.println("[WARNING!! - Water Quality] No excluded transaction records found");
				}

				// Check Export button displayed
				if (driver.findElement(By.xpath("//button[@class='btn btn-sm btn-outline-secondary table-export-btn']"))
						.isDisplayed()) {
					System.out.println(
							"[CHECK - Water Quality] Excluded Transactions - 'Export Transactions' button displayed");
					driver.findElement(By.xpath("//button[@class='btn btn-sm btn-outline-secondary table-export-btn']"))
							.click();
					Thread.sleep(1000);
					// Check 'Export Transaction Data' modal displayed
					driver.switchTo().activeElement();
					if (driver.findElement(By.tagName("h5")).getText().equals("Export Transaction Data")) {
						System.out.println(
								"[CHECK - Water Quality] Excluded Transactions - 'Export Transaction Data' modal displayed");
						// Cancel
						driver.findElement(By.xpath("//button[@class='btn btn-secondary']")).click();
						Thread.sleep(1000);
					} else {
						System.out.println(
								"[ERROR!! - Water Quality] Excluded Transactions - 'Export Transaction Data' modal NOT displayed");
					}
				} else {
					System.out.println(
							"[ERROR!! - Water Quality] Excluded Transactions - 'Export Transactions' button NOT displayed");
				}

				// Select Transaction History
				dropdown = new Select(driver.findElement(By.id("mode")));
				dropdown.selectByValue("historic");

				Thread.sleep(1000);

				// Check Transactions History screen displayed
				if (driver.findElement(By.tagName("h1")).getText().equals("Transaction History")) {
					System.out.println("[CHECK - Water Quality] Transaction History screen displayed");
				} else {
					System.out.println("[ERROR!! - Water Quality] Transaction History screen not displayed");
				}

				// Find first row of 'Transaction History ' table
				if (driver.findElements(By.xpath("//table/tbody/tr[1]")).size() != 0) {
					baseTableWQ = driver.findElement(By.className("table-responsive"));
					WebElement tableRowWQ = baseTableWQ.findElement(By.xpath("//table/tbody/tr[1]"));
					String rowtextWQ = tableRowWQ.getText();
					System.out
							.println("[DATA - Water Quality] Transaction History record found with charge row detail: "
									+ rowtextWQ);
				} else {
					System.out.println("[WARNING!! - Water Quality] No Transaction History records found");
				}

				// Check Export button displayed
				if (driver.findElement(By.xpath("//button[@class='btn btn-sm btn-outline-secondary table-export-btn']"))
						.isDisplayed()) {
					System.out.println(
							"[CHECK - Water Quality] Transaction History - 'Export Transactions' button displayed");
					driver.findElement(By.xpath("//button[@class='btn btn-sm btn-outline-secondary table-export-btn']"))
							.click();
					Thread.sleep(1000);
					// Check 'Export Transaction Data' modal displayed
					driver.switchTo().activeElement();
					if (driver.findElement(By.tagName("h5")).getText().equals("Export Transaction Data")) {
						System.out.println(
								"[CHECK - Water Quality] Transaction History - 'Export Transaction Data' modal displayed");
						// Cancel
						driver.findElement(By.xpath("//button[@class='btn btn-secondary']")).click();
						Thread.sleep(1000);
					} else {
						System.out.println(
								"[ERROR!! - Water Quality] Transaction History - 'Export Transaction Data' modal NOT displayed");
					}
				} else {
					System.out.println(
							"[ERROR!! - Water Quality] Transaction History - 'Export Transactions' button NOT displayed");
				}

				// Select Retrospective Transactions
				dropdown = new Select(driver.findElement(By.id("mode")));
				dropdown.selectByValue("retrospective");

				Thread.sleep(1500);

				// Check Retrospectives to be billed screen displayed
				if (driver.findElement(By.tagName("h1")).getText().equals("Pre-April 2018 Transactions to be billed")) {
					System.out.println(
							"[CHECK - Water Quality] Pre-April 2018 Transactions to be billed screen displayed");
				} else {
					System.out.println(
							"[ERROR!! - Water Quality] Pre-April 2018 Transactions to be billed screen NOT found");
				}

				// Select region A
				dropdown = new Select(driver.findElement(By.id("region")));
				dropdown.selectByVisibleText("A");

				Thread.sleep(1000);

				// Clear searchbox and perform new search
				driver.findElement(By.id("search")).clear();
				driver.findElement(By.xpath("//input[@class='btn btn-outline-primary']")).click();
				System.out.println(
						"[CHECK - Water Quality] Seacrh term cleared and new search performed on 'Pre-April 2018 transactions' screen");
				Thread.sleep(2000);

				// Find first row of 'Pre-April 2018 Transactions to be billed' table

				baseTableWQ = driver.findElement(By.className("table-responsive"));

				if (driver.findElements(By.xpath("//table/tbody/tr[1]")).size() != 0) {
					WebElement tableRowWQ = baseTableWQ.findElement(By.xpath("//table/tbody/tr[1]"));
					String rowtextWQ = tableRowWQ.getText();
					System.out
							.println("[DATA - Water Quality] Pre-April 2018 transaction found with charge row detail: "
									+ rowtextWQ);
				} else {
					System.out.println("[WARNING!! - Water Quality] No Pre-April 2018 transactions found");
				}

				// Check Export button displayed
				if (driver.findElement(By.xpath("//button[@class='btn btn-sm btn-outline-secondary table-export-btn']"))
						.isDisplayed()) {
					System.out.println(
							"[CHECK - Water Quality] Pre-April 2018 TTBB - 'Export Transactions' button displayed");
					driver.findElement(By.xpath("//button[@class='btn btn-sm btn-outline-secondary table-export-btn']"))
							.click();
					Thread.sleep(1000);
					// Check 'Export Transaction Data' modal displayed
					driver.switchTo().activeElement();
					if (driver.findElement(By.tagName("h5")).getText().equals("Export Transaction Data")) {
						System.out.println(
								"[CHECK - Water Quality] Pre-April 2018 TTBB - 'Export Transaction Data' modal displayed");
						// Cancel
						driver.findElement(By.xpath("//button[@class='btn btn-secondary']")).click();
						Thread.sleep(1000);
					} else {
						System.out.println(
								"[ERROR!! - Water Quality] Pre-April 2018 TTBB - 'Export Transaction Data' modal NOT displayed");
					}
				} else {
					System.out.println(
							"[ERROR!! - Water Quality] Pre-April 2018 TTBB - 'Export Transactions' button NOT displayed");
				}

				// Generate Pre-SRoC File

				driver.findElement(By.xpath("//button[@class='btn btn-primary mr-4 generate-transaction-file-btn']"))
						.click();

				Thread.sleep(2000);

				// Confirm Pre-SRoC File Summary displayed
				driver.switchTo().activeElement();
				if (driver.findElement(By.className("modal-header")).isEnabled()) {
					System.out.println("[CHECK - Water Quality] Generate Pre-SROC File Summary displayed");

					// Click "I have checked the summary and wish to proceed" checkbox
					if (driver.findElement(By.xpath("//input[@class='form-check-input ml-2']")).isEnabled()) {
						driver.findElement(By.xpath("//input[@class='form-check-input ml-2']")).click();
						System.out.println(
								"[CHECK - Water Quality] 'I have checked the summary and wish to proceed' checkbox clicked in Pre-SRoC File Summary");
						Thread.sleep(1000);

						if (driver.findElement(By.xpath("//input[@class='btn btn-primary file-generate-btn']"))
								.isEnabled()) {
							// Click Generate Transaction File button
							driver.findElement(By.xpath("//input[@class='btn btn-primary file-generate-btn']")).click();
							System.out.println("[CHECK - Water Quality] Generate Pre-SROC File button clicked");

							Thread.sleep(2000);
							// Confirm file generated successfully
							String message = driver
									.findElement(
											By.xpath("//div[@class='alert alert-success alert-dismissable fade show']"))
									.getText();
							System.out.println("[CHECK - Water Quality]:" + message);

						} else {
							// No records available for inclusion in file. Click 'Cancel' button
							driver.findElement(By.xpath("//button[@class='btn btn-secondary']")).click();
							System.out.println(
									"[WARNING!! - Water Quality] No records available for inclusion in Pre-SRoC file. File not generated.");
							Thread.sleep(1000);
						}

					} else {
						// No records available for inclusion in file. Click 'Cancel' button
						driver.findElement(By.xpath("//button[@class='btn btn-secondary']")).click();
						System.out.println(
								"[WARNING!! - Water Quality] No records available for inclusion in Pre-SRoC file. Confirm button not clicked.");
						Thread.sleep(1000);
					}

				} else {
					System.out.println("[ERROR!! - Water Quality] Transaction File Summary NOT displayed!");
				}
				
				// Select Download Transaction Data from Transactions menu
				//driver.findElement(By.id("navbarTransactionsSelectorLink")).click();
				//driver.findElement(By.linkText("Download Transaction Data")).click();
				
				// Confirm Data Protection Notice on 'Download Transaction Data' page displayed
				//if (driver.findElement(By.tagName("h5")).getText().equals("Data Protection Notice")) {
					//System.out.println(
						//	"[CHECK - Water Quality] 'Data Protection Notice on Download Transaction Data screen is displayed");
						//	driver.findElement(By.xpath("//a[@class='btn btn-primary']")).click();
						//	System.out.println(
							//		"[CHECK - Water Quality] 'Agree and download transaction data' button clicked");
					//	} else {
						//	System.out.println(
						//	"[ERROR!! - Water Quality] 'Data Protection Notice on Download Transaction Data screen NOT displayed");
			//	}
				
				// Select Imported Transaction Files from Transactions menu
				driver.findElement(By.id("navbarTransactionsSelectorLink")).click();
				driver.findElement(By.linkText("Imported Transaction Files")).click();
				
				// Confirm 'Imported Transaction Files' page displayed
				if (driver.findElement(By.tagName("h1")).getText().equals("Imported Transaction Files")) {
					System.out.println(
							"[CHECK - Water Quality] 'Imported Transaction Files screen is displayed");
					
					// Retrieve data in first row, ie for the most recently imported file
					Thread.sleep(1000);

					if (driver.findElements(By.xpath("//table/tbody/tr[1]")).size() != 0) {
						baseTableWQ = driver.findElement(By.className("table-responsive"));
						WebElement tableRowWQ = baseTableWQ.findElement(By.xpath("//table/tbody/tr[1]"));
						String rowtextWQ = tableRowWQ.getText();
						System.out.println("[DATA - Water Quality] Most recently imported transaction file record: " + rowtextWQ);
						} else {
							System.out.println("[WARNING!! - Water Quality] No Imported Transaction File records found");
					} 
				}
				
				
				///////////////////////////////////////////////////////////////////

				// INSTALLATIONS

				// Select Installations from regime dropdown menu item
				driver.findElement(By.id("navbarRegimeSelectorLink")).click();
				driver.findElement(By.linkText("Installations")).click();
				Thread.sleep(1000);

				System.out.println("INSTALLATIONS REGIME");

				// Select 'Transactions to be billed'
				driver.findElement(By.id("navbarTransactionsSelectorLink")).click();
				driver.findElement(By.linkText("Transactions to be billed")).click();
				System.out.println("[CHECK - Installations] 'Transactions to be billed' selected");

				// Confirm at least one transaction present on TTBB for Installations
				Thread.sleep(2000);
				wait = new WebDriverWait(driver, 15);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("table-responsive")));

				WebElement baseTablePAS = driver.findElement(By.className("table-responsive"));
				int tableRowsPAS = baseTablePAS.findElements(By.xpath("//tbody/tr")).size();
				System.out
						.println("[DATA - Installations] Number of table rows found on current page: " + tableRowsPAS);

				if (tableRowsPAS > 0) {
					System.out.println("[CHECK - Installations] Transaction found");

					baseTablePAS = driver.findElement(By.className("table-responsive"));
					WebElement tableHeaderPAS = baseTablePAS.findElement(By.xpath("//table/thead"));

					Select select = new Select(driver.findElement(By.id("region")));
					WebElement option = select.getFirstSelectedOption();
					String region = option.getText();
					System.out.println("[CHECK - Installations] Region selected is " + region);

					// Set Financial Year to 'All'
					Select financialYear = new Select(driver.findElement(By.id("fy")));
					financialYear.selectByValue("all");
					System.out.println("[CHECK - Installations] Financial year set to 'All'");

					// Select 50 records from 'Items per page' drop-down
					Select itemsperpage = new Select(driver.findElement(By.id("per_page")));
					itemsperpage.selectByValue("50");
					System.out.println("[CHECK - Installatiions] 50 items per page selected");

					// Check expected columns displayed and column sorting
					// Sort by File Reference
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tbody/tr")));
					baseTablePAS = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[2]/a")).click();
					System.out.println("[CHECK - Installations] Sort by 'File Reference' (asc)");
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTablePAS = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[2]/a")).click();
					System.out.println("[CHECK - Installations] Sort by 'File Reference' (desc)");

					// Sort by File Date
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTablePAS = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[3]/a")).click();
					System.out.println("[CHECK - Installatiions] Sort by 'File Date' (asc)");
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTablePAS = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[3]/a")).click();
					System.out.println("[CHECK - Installations] Sort by 'File Date' (desc)");

					// Sort by Customer
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTablePAS = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[4]/a")).click();
					System.out.println("[CHECK - Installations] Sort by 'Customer' (asc)");
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTablePAS = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[4]/a")).click();
					System.out.println("[CHECK - Installations] Sort by 'Customer' (desc)");

					// Sort by Permit reference
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTablePAS = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[5]/a")).click();
					System.out.println("[CHECK - Installations] Sort by 'Permit reference' (asc)");
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWQ = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[5]/a")).click();
					System.out.println("[CHECK - Installations] Sort by 'Permit reference' (desc)");

					// Sort by Original Permit reference
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTablePAS = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[6]/a")).click();
					System.out.println("[CHECK - Installations] Sort by 'Original Permit reference' (asc)");
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTablePAS = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[6]/a")).click();
					System.out.println("[CHECK - Installations] Sort by 'Original Permit reference' (desc)");

					// Sort by Band
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTablePAS = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[9]/a")).click();
					System.out.println("[CHECK - Installations] Sort by 'Discount Band' (asc)");
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTablePAS = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[9]/a")).click();
					System.out.println("[CHECK - Installations] Sort by 'Discount Band' (desc)");

					// Sort by Charge Period
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTablePAS = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[11]/a")).click();
					System.out.println("[CHECK - Installations] Sort by 'Charge period' (asc)");
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTablePAS = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[11]/a")).click();
					System.out.println("[CHECK - Installations] Sort by 'Charge period' (desc)");

					// Sort by category
					Thread.sleep(2000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTablePAS = driver.findElement(By.className("table-responsive"));
					tableHeaderPAS = baseTablePAS.findElement(By.xpath("//table/thead/tr/th[7]/a"));
					tableHeaderPAS.click();
					System.out.println("[CHECK - Installations] Sort by 'Permit Category' (asc)");
					Thread.sleep(1000);

					// Ensure record on first row does not have a charge. if it does, re-sort by
					// category

					baseTablePAS = driver.findElement(By.className("table-responsive"));
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					WebElement cell12Value = baseTablePAS.findElement(By.xpath("//table/tbody/tr[1]/td[12]"));
					String chargeValue = cell12Value.getText();
					System.out.println("[DATA - Installatiions] Charge value : " + chargeValue);
					if (chargeValue.matches(".*\\d+.*")) {
						Thread.sleep(1000);
						wait = new WebDriverWait(driver, 15);
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
						baseTablePAS = driver.findElement(By.className("table-responsive"));
						tableHeaderPAS = baseTablePAS.findElement(By.xpath("//table/thead/tr/th[7]/a"));
						tableHeaderPAS.click();
						System.out.println("[CHECK - Installations] Sort by 'Permit Category' (Desc)");
					}

					Thread.sleep(1000);
					// Find first row of table
					baseTablePAS = driver.findElement(By.className("table-responsive"));

					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table/tbody/tr[1]")));

					if (driver.findElements(By.xpath("//table/tbody/tr[1]")).size() != 0) {
						WebElement tableRowPAS = baseTablePAS.findElement(By.xpath("//table/tbody/tr[1]"));
						String rowtextPAS = tableRowPAS.getText();
						System.out.println("[DATA - Installations] Transaction found on TTBB with charge row detail: "
								+ rowtextPAS);
					}

					// Get ConsentRef
					WebElement cell5Value = baseTablePAS.findElement(By.xpath("//table/tbody/tr[1]/td[5]"));
					String permitRef = cell5Value.getText();
					System.out.println("[DATA - Installations] Consent reference : " + permitRef);

					// Search TTBB by stored consent reference
					driver.findElement(By.id("search")).sendKeys(permitRef + "");
					driver.findElement(By.xpath("//input[@class='btn btn-outline-primary']")).click();
					Thread.sleep(2000);

					// Check records found
					if (driver.findElement(By.xpath("//*[contains(text(), 'matching entries')]")).isDisplayed()) {
						System.out.println("[CHECK - Installations] Search returned one or more records as expected");
					} else {
						System.out.println("[ERROR!! - Installations] Search did NOT return expected result");
					}

					// Set permit category on transaction
					int randomNum = ThreadLocalRandom.current().nextInt(1, 130);

					// Thread.sleep(2000);
					baseTablePAS = driver.findElement(By.className("table-responsive"));
					wait.until(ExpectedConditions.visibilityOfElementLocated(
							By.xpath("//input[@class='tcm-select-input' and @value = '']")));
					baseTablePAS.findElement(By.xpath("//input[@class='tcm-select-input' and @value = '']")).click();
					Thread.sleep(1000);
					int iT = 1;
					while (iT <= randomNum) {
						baseTablePAS.findElement(By.xpath("//input[@class='tcm-select-input' and @value = '']"))
								.sendKeys(Keys.DOWN);
						iT++;
					}
					Thread.sleep(200);
					baseTablePAS.findElement(By.xpath("//input[@class='tcm-select-input' and @value = '']"))
							.sendKeys(Keys.RETURN);

					// Pause to allow sufficient time for Rules Service to calculate charge
					Thread.sleep(2000);

					// Check that a charge has been successfully generated
					baseTablePAS = driver.findElement(By.className("table-responsive"));
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table/tbody/tr[1]")));

					// Find first row of table
					WebElement tableRow = baseTablePAS.findElement(By.xpath("//table/tbody/tr[1]"));
					String rowtext = tableRow.getText();
					System.out.println("[DATA] Transaction with charge row detail: " + rowtext);

					// Sort transactions in TTBB by Category

					baseTablePAS = driver.findElement(By.className("table-responsive"));
					tableHeaderPAS = baseTablePAS.findElement(By.xpath("//table/thead/tr/th[7]/a"));
					tableHeaderPAS.click();
					Thread.sleep(1000);

					// Find charge value
					baseTablePAS = driver.findElement(By.className("table-responsive"));
					WebElement cell13Value = baseTablePAS.findElement(By.xpath("//table/tbody/tr[1]/td[13]"));
					chargeValue = cell13Value.getText();
					System.out.println("[DATA] Charge value : " + chargeValue);

					// Check Rules Service has returned a charge value
					if (chargeValue.matches(".*\\d+.*")) {
						System.out.println("[CHECK - Installations] Charge successfully generated");
					} else {
						System.out.println("[ERROR!! - Installations] Charge value not found! ");
					}

					// Select "Transaction Detail" page for permit
					baseTablePAS = driver.findElement(By.className("table-responsive"));
					WebElement tableRowPAS = baseTablePAS.findElement(By.xpath("//table/tbody/tr[1]"));
					tableRowPAS.findElement(By.xpath("//button[@class='btn btn-sm show-details-button']")).click();

					// Pause to allow sufficient time to display page
					Thread.sleep(2000);

					// Check "Transaction Detail" page displayed
					if (driver.findElement(By.tagName("h1")).getText().equals("Transaction detail")) {
						System.out.println("[CHECK - Installations] Transaction Detail page displayed");

						// Check "Suggested category" section displayed
						if (driver.findElement(By.tagName("h2")).getText().equals("Suggested category")) {
							System.out.println(
									"[CHECK - Installations] Transaction Detail page - Suggested category section displayed");
						} else {
							System.out.println(
									"[WARNING!! - Installations] Transaction Detail page - Suggested category section NOT displayed");
						}

						// View Change History for Transaction

						driver.findElement(By.xpath("//a[@class='btn btn-outline-info']")).click();

						// Pause to allow sufficient time for page to be re-displayed
						Thread.sleep(1000);

						if (driver.findElement(By.tagName("h1")).getText().equals("Transaction change history")) {
							System.out.println("[CHECK - Installations] Transaction Change History page displayed");

							baseTablePAS = driver.findElement(By.className("table-responsive"));

							wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table/tbody/tr[1]")));

							if (driver.findElements(By.xpath("//table/tbody/tr[1]")).size() != 0) {
								WebElement tableRowWQ = baseTablePAS.findElement(By.xpath("//table/tbody/tr[1]"));
								String rowtextPAS = tableRowWQ.getText();
								System.out.println("[DATA - Installations] First audit event row: " + rowtextPAS);
							}

							// Click the 'Back' link from Transaction Change History
							driver.findElement(By.xpath("//a[@class='back-link']")).click();

							// Scroll down page
							JavascriptExecutor jse = (JavascriptExecutor) driver;
							jse.executeScript("window.scrollBy(0,900)", "");

							int h2tagelements = driver.findElements(By.tagName("h2")).size();
							System.out.println("[CHECK - Installations] h2 TAGS FOUND = " + h2tagelements);

							if (h2tagelements > 1) {
								List<WebElement> h2tags = driver.findElements(By.tagName("h2"));
								WebElement h2tag1 = h2tags.get(1);

								// System.out.println("[CHECK - Water Quality] h2 TAGS FOUND = "+h2tag1);
								if (h2tag1.getText().equals("Related billed transactions")) {
									System.out.println(
											"[CHECK - Installations] Transaction Detail page - Related billed transactions section displayed");
									if (h2tagelements > 2) {
										WebElement h2tag2 = h2tags.get(2);
										// System.out.println("[CHECK - Water Quality] h2 TAGS FOUND = "+h2tag2);
										if (h2tag2.getText().equals("Related unbilled transactions")) {
											System.out.println(
													"[CHECK - Installations] Transaction Detail page - Related unbilled transactions section displayed");
										}
									}
								}
							}
							// Click the <Back> link to return to TTBB
							driver.findElement(By.xpath("//a[@class='back-link']")).click();

							// Pause to allow sufficient time for TTBB to be re-displayed
							Thread.sleep(2000);

							// Check Export button displayed
							if (driver.findElement(By.cssSelector("button.btn-sm.btn-outline-secondary"))
									.isDisplayed()) {
								System.out.println("[CHECK - Installations] Export Transactions button displayed");
								driver.findElement(By.cssSelector("button.btn-sm.btn-outline-secondary")).click();
								Thread.sleep(1000);
								// Check 'Export Transaction Data' modal displayed
								driver.switchTo().activeElement();
								if (driver.findElement(By.tagName("h5")).getText().equals("Export Transaction Data")) {
									System.out.println(
											"[CHECK - Installations] 'Export Transaction Data' modal displayed");
									// Cancel
									driver.findElement(By.xpath("//button[@class='btn btn-secondary']")).click();
									Thread.sleep(1000);
								} else {
									System.out.println(
											"[ERROR!! - Installations] 'Export Transaction Data' modal NOT displayed");
								}
							} else {
								System.out
										.println("[ERROR!! - Installations] Export Transactions button NOT displayed");
							}
							// Sort transactions in TTBB by Customer

							baseTablePAS = driver.findElement(By.className("table-responsive"));
							tableHeaderPAS = baseTablePAS.findElement(By.xpath("//table/thead/tr/th[5]/a"));
							tableHeaderPAS.click();
							Thread.sleep(1000);

							// Set the 'Temporary cessation' flag to 'Y' for the first transaction
							baseTablePAS = driver.findElement(By.className("table-responsive"));

							int isPresent = baseTablePAS
									.findElements(
											By.xpath("//select[@class='form-control temporary-cessation-select']"))
									.size();
							if (isPresent > 0) {
								dropdown = new Select(baseTablePAS.findElement(
										By.xpath("//select[@class='form-control temporary-cessation-select']")));
								dropdown.selectByValue("true");
								System.out.println("[DATA - Installations] Temporary cessation flag set to 'Y'");
							}

							// Exclude transaction

							baseTablePAS = driver.findElement(By.className("table-responsive"));
							WebElement tableRow2 = baseTablePAS.findElement(By.xpath("//table/tbody/tr[1]"));
							tableRow2.findElement(By.xpath("//button[@class='btn btn-sm show-details-button']"))
									.click();

							// Check whether transaction already excluded, if so re-instate it
							isPresent = driver
									.findElements(By.xpath(
											"//input[@class='btn btn-success' and @value = 'Reinstate for Billing']"))
									.size();
							if (isPresent > 0) {
								driver.findElement(By.xpath(
										"//input[@class='btn btn-success' and @value = 'Reinstate for Billing']"))
										.click();
								Thread.sleep(1000);
							}
							driver.findElement(By.xpath("//button[@class='btn btn-danger exclude-button']")).click();

							Thread.sleep(1000);

							// Check 'Exclude Transaction' modal displayed
							if (driver.findElement(By.tagName("h5")).getText().equals("Exclude Transaction")) {
								System.out.println("[CHECK - Installations] 'Exclude Transaction' modal displayed");

								// Click "Exclude Transaction" button in modal
								driver.findElement(By.xpath("//input[@class='btn btn-danger']")).click();

								// Confirm transaction successfully marked for exclusion

								if (driver.findElement(By.xpath("//span[@class='badge badge-pill badge-danger']"))
										.isDisplayed()) {
									System.out.println("[CHECK - Installations] Transaction 'Marked for exclusion'");
								} else {
									System.out.println(
											"[ERROR!! - Installations] Transaction NOT 'Marked for exclusion'!");
								}

								// Confirm 'Reinstate for billing' button displayed

								if (driver.findElement(By.xpath("//input[@value='Reinstate for Billing']"))
										.isDisplayed()) {
									System.out.println("[CHECK - Installations] 'Reinstate for Billing' buttton found");
								} else {
									System.out.println(
											"[ERROR!! - Installations] 'Reinstate for Billing' buttton NOT found!");
								}

								// Click the <Back> link to return to TTBB
								driver.findElement(By.xpath("//a[@class='back-link']")).click();
								Thread.sleep(500);
								baseTablePAS = driver.findElement(By.className("table-responsive"));
								driver.findElement(By.xpath("//table/thead/tr/th[7]/a")).click();
								System.out.println("[CHECK - Installations] Sort by 'Category' (asc)");

								// Set a charge for any remaining transactions for the same Permit reference
								Thread.sleep(1000);
								baseTablePAS = driver.findElement(By.className("table-responsive"));

								int elemcount = baseTablePAS
										.findElements(By.xpath("//input[@class='tcm-select-input' and @value = '']"))
										.size();
								System.out.println("[CHECK - Installations] " + elemcount
										+ " records in same group do not have a charge");
								Thread.sleep(1000);
								baseTablePAS = driver.findElement(By.className("table-responsive"));
								List<WebElement> elements = baseTablePAS
										.findElements(By.xpath("//input[@class='tcm-select-input' and @value = '']"));
								Thread.sleep(1000);
								java.util.Iterator<WebElement> i = elements.iterator();
								while (i.hasNext()) {
									baseTablePAS = driver.findElement(By.className("table-responsive"));
									elements = baseTablePAS.findElements(
											By.xpath("//input[@class='tcm-select-input' and @value = '']"));
									WebElement element = i.next();

									int isThere = driver
											.findElements(
													By.xpath("//input[@class='tcm-select-input' and @value = '']"))
											.size();
									if (isThere > 0) {
										baseTablePAS = driver.findElement(By.className("table-responsive"));

										// Set permit category on first transaction
										randomNum = ThreadLocalRandom.current().nextInt(1, 130);

										// Thread.sleep(1000);
										baseTablePAS = driver.findElement(By.className("table-responsive"));
										wait = new WebDriverWait(driver, 15);
										wait.until(ExpectedConditions.visibilityOfElementLocated(
												By.xpath("//input[@class='tcm-select-input' and @value = '']")));
										baseTablePAS
												.findElement(
														By.xpath("//input[@class='tcm-select-input' and @value = '']"))
												.click();
										Thread.sleep(600);
										iT = 1;
										while (iT <= randomNum) {
											baseTablePAS
													.findElement(By.xpath(
															"//input[@class='tcm-select-input' and @value = '']"))
													.sendKeys(Keys.DOWN);
											// Thread.sleep(50);
											iT++;
										}

										baseTablePAS
												.findElement(
														By.xpath("//input[@class='tcm-select-input' and @value = '']"))
												.sendKeys(Keys.RETURN);
										Thread.sleep(2000);
										System.out.println(
												"[CHECK - Installations] Charge set for consent in same group");
									}
								}
							}

							// Click 'Approve All' button
							driver.findElement(By.xpath("//button[@class='btn btn-success approve-all-btn']"))
									.click();
							System.out.println("[CHECK - Installations] 'Approve All' button clicked");
							Thread.sleep(3000);

							// GENERATE TRANSACTION FILE
							// Click Generate Transaction File button
							wait = new WebDriverWait(driver, 15);
							wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tbody/tr")));

							driver.findElement(
									By.xpath("//button[@class='btn btn-primary generate-transaction-file-btn mr-4']"))
									.click();

							Thread.sleep(2000);

							// Confirm Transaction File Summary displayed
							driver.switchTo().activeElement();
							if (driver.findElement(By.className("modal-header")).isEnabled()) {
								System.out.println("[CHECK - Installations] Transaction File Summary displayed");

								// Click "I have checked the summary and wish to proceed" checkbox
								if (driver.findElement(By.xpath("//input[@class='form-check-input ml-2']"))
										.isEnabled()) {
									driver.findElement(By.xpath("//input[@class='form-check-input ml-2']")).click();
									System.out.println(
											"[CHECK - Installations] 'I have checked the summary and wish to proceed' checkbox clicked");
									Thread.sleep(1000);
								} else {
									System.out.println(
											"[WARNING!! - Installations] 'I have checked the summary and wish to proceed' checkbox is not enabled");
								}

								Thread.sleep(1000);

								if (driver.findElement(By.xpath("//input[@class='btn btn-primary file-generate-btn']"))
										.isEnabled()) {
									// Click Generate Transaction File button
									driver.findElement(By.xpath("//input[@class='btn btn-primary file-generate-btn']"))
											.click();
									System.out.println(
											"[CHECK - Installations] Generate Transaction File button clicked");

									Thread.sleep(1000);
									// Confirm file generated successfully
									String message = driver
											.findElement(By.xpath(
													"//div[@class='alert alert-success alert-dismissable fade show']"))
											.getText();
									System.out.println("[CHECK - Installations]:" + message);

								} else {
									// No records included in file. Click 'Cancel' button
									driver.findElement(By.xpath("//button[@class='btn btn-secondary']")).click();
									Thread.sleep(1000);
									System.out.println(
											"[WARNING!! - Installations] No approved records in File Summary. File not generated.");
								}

							} else {
								System.out.println("[ERROR!! - Installations] Transaction File Summary NOT displayed!");
							}

						} else {
							System.out.println(
									"[ERROR!! - Installations] Transaction Change History page NOT displayed!");
						}

					} else {
						System.out.println("[ERROR!! - Installations] Transaction Detail page NOT displayed!");
					}

				} else {
					System.out.println("[WARNING!! - Installations] No transactions found on TTBB");
				}

				// Select Transaction File History for Installations
				driver.findElement(By.id("navbarTransactionsSelectorLink")).click();
				driver.findElement(By.linkText("Transaction File History")).click();

				Thread.sleep(2000);

				// Check Transaction File History screen displayed
				if (driver.findElement(By.tagName("h1")).getText().equals("Transaction File History")) {
					System.out.println("[CHECK - Installations] Transaction File History screen displayed");
				} else {
					System.out.println("[ERROR!! - Installations] Transaction File History screen NOT displayed");
				}

				// Set Region to 'All'
				dropdown = new Select(driver.findElement(By.id("region")));
				dropdown.selectByVisibleText("All");
				System.out.println("[CHECK - Installations] Transaction File History - region filter set to 'All'");

				Thread.sleep(1000);

				// Set Pre/Post-SRoC to 'All'
				dropdown = new Select(driver.findElement(By.id("prepost")));
				dropdown.selectByVisibleText("Post");
				System.out.println(
						"[CHECK - Installations] Transaction File History - Pre/Post-SRoC filter set to 'Post'");

				Thread.sleep(1000);

				// Sort records by 'Date Generated' (desc)

				Thread.sleep(1000);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
				baseTablePAS = driver.findElement(By.className("table-responsive"));
				driver.findElement(By.xpath("//table/thead/tr/th[2]/a")).click();
				Thread.sleep(1000);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
				baseTablePAS = driver.findElement(By.className("table-responsive"));
				driver.findElement(By.xpath("//table/thead/tr/th[2]/a")).click();
				System.out.println("[CHECK - Installations] Sort by 'Date Generated' (desc)");

				Thread.sleep(1000);

				if (driver.findElements(By.xpath("//table/tbody/tr[1]")).size() != 0) {
					baseTablePAS = driver.findElement(By.className("table-responsive"));
					WebElement tableRowPAS = baseTablePAS.findElement(By.xpath("//table/tbody/tr[1]"));
					String rowtextPAS = tableRowPAS.getText();
					System.out.println("[DATA - Installations] Transaction File History record found: " + rowtextPAS);
				} else {
					System.out.println("[WARNING!! - Installations] No Transaction File History records found");
				}

				// Select Excluded Transactions

				driver.findElement(By.id("navbarTransactionsSelectorLink")).click();
				driver.findElement(By.linkText("Excluded Transactions")).click();

				Thread.sleep(2000);

				// Check Excluded Transactions screen displayed
				if (driver.findElement(By.tagName("h1")).getText().equals("Excluded Transactions")) {
					System.out.println("[CHECK - Installations] Excluded Transactions screen displayed");
				} else {
					System.out.println("[ERROR!! - Installations] Excluded Transactions screen NOT displayed");
				}

				if (driver.findElements(By.xpath("//table/tbody/tr[1]")).size() != 0) {
					baseTablePAS = driver.findElement(By.className("table-responsive"));
					WebElement tableRowPAS = baseTablePAS.findElement(By.xpath("//table/tbody/tr[1]"));
					String rowtextPAS = tableRowPAS.getText();
					System.out
							.println("[DATA - Installations] Excluded Transaction record found with charge row detail: "
									+ rowtextPAS);
				} else {
					System.out.println("[WARNING!! - Installations] No excluded transaction records found");
				}
				

				// Check Export button displayed
				if (driver.findElement(By.xpath("//button[@class='btn btn-sm btn-outline-secondary table-export-btn']"))
						.isDisplayed()) {
					System.out.println(
							"[CHECK - Installations] Excluded Transactions - 'Export Transactions' button displayed");
					driver.findElement(By.xpath("//button[@class='btn btn-sm btn-outline-secondary table-export-btn']"))
							.click();
					Thread.sleep(1000);
					// Check 'Export Transaction Data' modal displayed
					driver.switchTo().activeElement();
					if (driver.findElement(By.tagName("h5")).getText().equals("Export Transaction Data")) {
						System.out.println(
								"[CHECK - Installations] Excluded Transactions - 'Export Transaction Data' modal displayed");
						// Cancel
						driver.findElement(By.xpath("//button[@class='btn btn-secondary']")).click();
						Thread.sleep(1000);
					} else {
						System.out.println(
								"[ERROR!! - Installations] Excluded Transactions - 'Export Transaction Data' modal NOT displayed");
					}
				} else {
					System.out.println(
							"[ERROR!! - Installations] Excluded Transactions - 'Export Transactions' button NOT displayed");
				}

				// Select Transaction History
				dropdown = new Select(driver.findElement(By.id("mode")));
				dropdown.selectByValue("historic");

				Thread.sleep(1000);

				// Check Transactions History screen displayed
				if (driver.findElement(By.tagName("h1")).getText().equals("Transaction History")) {
					System.out.println("[CHECK - Installations] Transaction History screen displayed");
				} else {
					System.out.println("[ERROR!! - Installations] Transaction History screen not displayed");
				}

				// Find first row of 'Transaction History ' table
				if (driver.findElements(By.xpath("//table/tbody/tr[1]")).size() != 0) {
					baseTablePAS = driver.findElement(By.className("table-responsive"));
					WebElement tableRowPAS = baseTablePAS.findElement(By.xpath("//table/tbody/tr[1]"));
					String rowtextPAS = tableRowPAS.getText();
					System.out
							.println("[DATA - Installations] Transaction History record found with charge row detail: "
									+ rowtextPAS);
				} else {
					System.out.println("[WARNING!! - Installations] No Transaction History records found");
				}


				// Check Export button displayed
				if (driver.findElement(By.xpath("//button[@class='btn btn-sm btn-outline-secondary table-export-btn']"))
						.isDisplayed()) {
					System.out.println(
							"[CHECK - Installations] Excluded Transactions - 'Export Transactions' button displayed");
					driver.findElement(By.xpath("//button[@class='btn btn-sm btn-outline-secondary table-export-btn']"))
							.click();
					Thread.sleep(1000);
					// Check 'Export Transaction Data' modal displayed
					driver.switchTo().activeElement();
					if (driver.findElement(By.tagName("h5")).getText().equals("Export Transaction Data")) {
						System.out.println(
								"[CHECK - Installations] Excluded Transactions - 'Export Transaction Data' modal displayed");
						// Cancel
						driver.findElement(By.xpath("//button[@class='btn btn-secondary']")).click();
						Thread.sleep(1000);
					} else {
						System.out.println(
								"[ERROR!! - Installations] Excluded Transactions - 'Export Transaction Data' modal NOT displayed");
					}
				} else {
					System.out.println(
							"[ERROR!! - Installations] Excluded Transactions - 'Export Transactions' button NOT displayed");
				}

				// Select Retrospective Transactions
				dropdown = new Select(driver.findElement(By.id("mode")));
				dropdown.selectByValue("retrospective");

				Thread.sleep(1500);

				// Check Retrospectives to be billed screen displayed
				if (driver.findElement(By.tagName("h1")).getText().equals("Pre-April 2018 Transactions to be billed")) {
					System.out.println(
							"[CHECK - Installations] Pre-April 2018 Transactions to be billed screen displayed");
				} else {
					System.out.println(
							"[ERROR!! - Installations] Pre-April 2018 Transactions to be billed screen NOT found");
				}

				// Select region A
				dropdown = new Select(driver.findElement(By.id("region")));
				dropdown.selectByVisibleText("A");

				Thread.sleep(1000);

				// Clear searchbox and perform new search
				driver.findElement(By.id("search")).clear();
				driver.findElement(By.xpath("//input[@class='btn btn-outline-primary']")).click();
				System.out.println(
						"[CHECK - Installations] Seacrh term cleared and new search performed on 'Pre-April 2018 transactions' screen");
				Thread.sleep(2000);

				// Find first row of 'Pre-April 2018 Transactions to be billed' table

				baseTablePAS = driver.findElement(By.className("table-responsive"));

				if (driver.findElements(By.xpath("//table/tbody/tr[1]")).size() != 0) {
					WebElement tableRowPAS = baseTablePAS.findElement(By.xpath("//table/tbody/tr[1]"));
					String rowtextPAS = tableRowPAS.getText();
					System.out
							.println("[DATA - Installations] Pre-April 2018 transaction found with charge row detail: "
									+ rowtextPAS);
				} else {
					System.out.println("[WARNING!! - Installations] No Pre-April 2018 transactions found");
				}
				
				// Check Export button displayed
				if (driver.findElement(By.xpath("//button[@class='btn btn-sm btn-outline-secondary table-export-btn']"))
						.isDisplayed()) {
					System.out.println(
							"[CHECK - Installations] Excluded Transactions - 'Export Transactions' button displayed");
					driver.findElement(By.xpath("//button[@class='btn btn-sm btn-outline-secondary table-export-btn']"))
							.click();
					Thread.sleep(1000);
					// Check 'Export Transaction Data' modal displayed
					driver.switchTo().activeElement();
					if (driver.findElement(By.tagName("h5")).getText().equals("Export Transaction Data")) {
						System.out.println(
								"[CHECK - Installations] Excluded Transactions - 'Export Transaction Data' modal displayed");
						// Cancel
						driver.findElement(By.xpath("//button[@class='btn btn-secondary']")).click();
						Thread.sleep(1000);
					} else {
						System.out.println(
								"[ERROR!! - Installations] Excluded Transactions - 'Export Transaction Data' modal NOT displayed");
					}
				} else {
					System.out.println(
							"[ERROR!! - Installations] Excluded Transactions - 'Export Transactions' button NOT displayed");
				}

				// Generate Installations Pre-SRoC File

				driver.findElement(By.xpath("//button[@class='btn btn-primary mr-4 generate-transaction-file-btn']"))
						.click();

				Thread.sleep(1000);

				// Confirm Pre-SRoC File Summary displayed
				driver.switchTo().activeElement();
				if (driver.findElement(By.className("modal-header")).isEnabled()) {
					System.out.println("[CHECK - Installations] Generate Pre-SROC File Summary displayed");

					// Click "I have checked the summary and wish to proceed" checkbox
					driver.findElement(By.xpath("//input[@class='form-check-input ml-2']")).click();
					System.out.println(
							"[CHECK - Installations] 'I have checked the summary and wish to proceed' checkbox clicked in Pre-SRoC File Summary");
					Thread.sleep(1000);

					if (driver.findElement(By.xpath("//input[@class='btn btn-primary file-generate-btn']"))
							.isEnabled()) {
						// Click Generate Transaction File button
						driver.findElement(By.xpath("//input[@class='btn btn-primary file-generate-btn']")).click();
						System.out.println("[CHECK - Installations] Generate Pre-SROC File button clicked");

						Thread.sleep(2000);
						// Confirm file generated successfully
						String message = driver
								.findElement(
										By.xpath("//div[@class='alert alert-success alert-dismissable fade show']"))
								.getText();
						System.out.println("[CHECK - Installations]:" + message);

					} else {
						// No records available for inclusion in file. Click 'Cancel' button
						driver.findElement(By.xpath("//button[@class='btn btn-secondary']")).click();
						System.out.println(
								"[WARNING!! - Installations] No records available for inclusion in Pre-SRoC file. File not generated.");
						Thread.sleep(1000);
					}

				} else {
					System.out.println("[ERROR!! - Installations] Transaction File Summary NOT displayed!");
				}
				
				// Select Download Transaction Data from Transactions menu
			//	driver.findElement(By.id("navbarTransactionsSelectorLink")).click();
			//	driver.findElement(By.linkText("Download Transaction Data")).click();
				
				// Confirm Data Protection Notice on 'Download Transaction Data' page displayed
			//	if (driver.findElement(By.tagName("h5")).getText().equals("Data Protection Notice")) {
				//	System.out.println(
						//	"[CHECK - Installations] 'Data Protection Notice on Download Transaction Data screen is displayed");
					//		driver.findElement(By.xpath("//a[@class='btn btn-primary']")).click();
						//	System.out.println(
					//				"[CHECK - Installations] 'Agree and download transaction data' button clicked");
					//	} else {
					//		System.out.println(
						//	"[ERROR!! - Installations] 'Data Protection Notice on Download Transaction Data screen NOT displayed");
			//	}
				Thread.sleep(1000);
				// Select Imported Transaction Files from Transactions menu
				driver.findElement(By.id("navbarTransactionsSelectorLink")).click();
				driver.findElement(By.linkText("Imported Transaction Files")).click();
				
				// Confirm 'Imported Transaction Files' page displayed
				if (driver.findElement(By.tagName("h1")).getText().equals("Imported Transaction Files")) {
					System.out.println(
							"[CHECK - Installations] 'Imported Transaction Files screen is displayed");
					
					// Retrieve data in first row, ie for the most recently imported file
					Thread.sleep(1000);

					if (driver.findElements(By.xpath("//table/tbody/tr[1]")).size() != 0) {
						baseTableWQ = driver.findElement(By.className("table-responsive"));
						WebElement tableRowWQ = baseTableWQ.findElement(By.xpath("//table/tbody/tr[1]"));
						String rowtextWQ = tableRowWQ.getText();
						System.out.println("[DATA - Installations] Most recently imported transaction file record: " + rowtextWQ);
						} else {
							System.out.println("[WARNING!! - Installations] No Imported Transaction File records found");
					} 
				}

				//////////////////////////////////////////////////////////////////////////////////////////////////////

				// WASTE REGIME

				// Select Waste from regime dropdown menu item
				driver.findElement(By.id("navbarRegimeSelectorLink")).click();
				driver.findElement(By.linkText("Waste")).click();
				Thread.sleep(1000);

				System.out.println("WASTE REGIME");

				// Select 'Transactions to be billed'
				driver.findElement(By.id("navbarTransactionsSelectorLink")).click();
				driver.findElement(By.linkText("Transactions to be billed")).click();
				System.out.println("[CHECK - Waste] 'Transactions to be billed' selected");

				// Confirm at least one transaction present on TTBB for Waste
				Thread.sleep(2000);
				wait = new WebDriverWait(driver, 15);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("table-responsive")));

				WebElement baseTableWABS = driver.findElement(By.className("table-responsive"));
				int tableRowsWABS = baseTableWABS.findElements(By.xpath("//tbody/tr")).size();
				System.out.println("[DATA - Waste] Number of table rows found on current page: " + tableRowsWABS);

				if (tableRowsWABS > 0) {
					System.out.println("[CHECK - Waste] Transaction found");

					baseTableWABS = driver.findElement(By.className("table-responsive"));
					WebElement tableHeaderWABS = baseTableWABS.findElement(By.xpath("//table/thead"));

					Select select = new Select(driver.findElement(By.id("region")));
					WebElement option = select.getFirstSelectedOption();
					String region = option.getText();
					System.out.println("[CHECK - Waste] Region selected is " + region);

					// Set Financial Year to 'All'
					// Select financialYear = new Select(driver.findElement(By.id("fy")));
					// financialYear.selectByValue("all");
					// System.out.println("[CHECK - Waste] Financial year set to 'All'");

					// Select 50 records from 'Items per page' drop-down
					Select itemsperpage = new Select(driver.findElement(By.id("per_page")));
					itemsperpage.selectByValue("50");
					System.out.println("[CHECK - Waste] 50 items per page selected");

					// Check expected columns displayed and column sorting
					// Sort by File Reference
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tbody/tr")));
					baseTableWABS = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[2]/a")).click();
					System.out.println("[CHECK - Waste] Sort by 'File Reference' (asc)");
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWABS = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[2]/a")).click();
					System.out.println("[CHECK - Waste] Sort by 'File Reference' (desc)");

					// Sort by File Date
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWABS = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[3]/a")).click();
					System.out.println("[CHECK - Waste] Sort by 'File Date' (asc)");
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWABS = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[3]/a")).click();
					System.out.println("[CHECK - Waste] Sort by 'File Date' (desc)");

					// Sort by Customer
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWABS = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[4]/a")).click();
					System.out.println("[CHECK - Waste] Sort by 'Customer' (asc)");
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWABS = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[4]/a")).click();
					System.out.println("[CHECK - Waste] Sort by 'Customer' (desc)");

					// Sort by Permit reference
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWABS = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[5]/a")).click();
					System.out.println("[CHECK - Waste] Sort by 'Permit reference' (asc)");
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWABS = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[5]/a")).click();
					System.out.println("[CHECK - Waste] Sort by 'Permit reference' (desc)");

					// Sort by Band
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWABS = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[8]/a")).click();
					System.out.println("[CHECK - Waste] Sort by 'Discount Band' (asc)");
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWABS = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[8]/a")).click();
					System.out.println("[CHECK - Waste] Sort by 'Discount Band' (desc)");

					// Sort by Charge Period
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWABS = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[10]/a")).click();
					System.out.println("[CHECK - Waste] Sort by 'Charge period' (asc)");
					Thread.sleep(1000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWABS = driver.findElement(By.className("table-responsive"));
					driver.findElement(By.xpath("//table/thead/tr/th[10]/a")).click();
					System.out.println("[CHECK - Waste] Sort by 'Charge period' (desc)");

					// Sort by category
					Thread.sleep(2000);
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					baseTableWABS = driver.findElement(By.className("table-responsive"));
					tableHeaderWABS = baseTableWABS.findElement(By.xpath("//table/thead/tr/th[6]/a"));
					tableHeaderWABS.click();
					System.out.println("[CHECK - Waste] Sort by 'Permit Category' (asc)");
					Thread.sleep(1000);

					// Ensure record on first row does not have a charge. if it does, re-sort by
					// category

					baseTableWABS = driver.findElement(By.className("table-responsive"));
					wait = new WebDriverWait(driver, 15);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
					WebElement cell12Value = baseTableWABS.findElement(By.xpath("//table/tbody/tr[1]/td[11]"));
					String chargeValue = cell12Value.getText();
					System.out.println("[DATA - Waste] Charge value : " + chargeValue);
					if (chargeValue.matches(".*\\d+.*")) {
						Thread.sleep(1000);
						wait = new WebDriverWait(driver, 15);
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
						baseTableWABS = driver.findElement(By.className("table-responsive"));
						tableHeaderWABS = baseTableWABS.findElement(By.xpath("//table/thead/tr/th[6]/a"));
						tableHeaderWABS.click();
						System.out.println("[CHECK - Waste] Sort by 'Permit Category' (Desc)");
					}

					Thread.sleep(1000);
					// Find first row of table
					baseTableWABS = driver.findElement(By.className("table-responsive"));

					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table/tbody/tr[1]")));

					if (driver.findElements(By.xpath("//table/tbody/tr[1]")).size() != 0) {
						WebElement tableRowWABS = baseTableWABS.findElement(By.xpath("//table/tbody/tr[1]"));
						String rowtextWABS = tableRowWABS.getText();
						System.out.println(
								"[DATA - Waste] Transaction found on TTBB with charge row detail: " + rowtextWABS);
					}

					// Get ConsentRef
					WebElement cell5Value = baseTableWABS.findElement(By.xpath("//table/tbody/tr[1]/td[5]"));
					String permitRef = cell5Value.getText();
					System.out.println("[DATA - Waste] Permit reference : " + permitRef);

					// Search TTBB by stored consent reference
					driver.findElement(By.id("search")).sendKeys(permitRef + "");
					driver.findElement(By.xpath("//input[@class='btn btn-outline-primary']")).click();
					Thread.sleep(2000);

					// Check records found
					if (driver.findElement(By.xpath("//*[contains(text(), 'matching entries')]")).isDisplayed()) {
						System.out.println("[CHECK - Waste] Search returned one or more records as expected");
					} else {
						System.out.println("[ERROR!! - Waste] Search did NOT return expected result");
					}

					// Set permit category on transaction
					int randomNum = ThreadLocalRandom.current().nextInt(1, 50);

					// Thread.sleep(2000);
					baseTableWABS = driver.findElement(By.className("table-responsive"));
					wait.until(ExpectedConditions.visibilityOfElementLocated(
							By.xpath("//input[@class='tcm-select-input' and @value = '']")));
					baseTableWABS.findElement(By.xpath("//input[@class='tcm-select-input' and @value = '']")).click();
					Thread.sleep(1000);
					int iT = 1;
					while (iT <= randomNum) {
						baseTableWABS.findElement(By.xpath("//input[@class='tcm-select-input' and @value = '']"))
								.sendKeys(Keys.DOWN);
						iT++;
					}
					Thread.sleep(200);
					baseTableWABS.findElement(By.xpath("//input[@class='tcm-select-input' and @value = '']"))
							.sendKeys(Keys.RETURN);

					// Pause to allow sufficient time for Rules Service to calculate charge
					Thread.sleep(2000);

					// Check that a charge has been successfully generated
					baseTableWABS = driver.findElement(By.className("table-responsive"));
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table/tbody/tr[1]")));

					// Find first row of table
					WebElement tableRow = baseTableWABS.findElement(By.xpath("//table/tbody/tr[1]"));
					String rowtext = tableRow.getText();
					System.out.println("[DATA] Transaction with charge row detail: " + rowtext);

					// Sort transactions in TTBB by Category

					baseTableWABS = driver.findElement(By.className("table-responsive"));
					tableHeaderWABS = baseTableWABS.findElement(By.xpath("//table/thead/tr/th[6]/a"));
					tableHeaderWABS.click();
					Thread.sleep(1000);

					// Find charge value
					baseTableWABS = driver.findElement(By.className("table-responsive"));
					WebElement cell13Value = baseTableWABS.findElement(By.xpath("//table/tbody/tr[1]/td[11]"));
					chargeValue = cell13Value.getText();
					System.out.println("[DATA] Charge value : " + chargeValue);

					// Check Rules Service has returned a charge value
					if (chargeValue.matches(".*\\d+.*")) {
						System.out.println("[CHECK - Waste] Charge successfully generated");
					} else {
						System.out.println("[ERROR!! - Waste] Charge value not found! ");
					}

					// Select "Transaction Detail" page for permit
					baseTableWABS = driver.findElement(By.className("table-responsive"));
					WebElement tableRowWABS = baseTableWABS.findElement(By.xpath("//table/tbody/tr[1]"));
					tableRowWABS.findElement(By.xpath("//button[@class='btn btn-sm show-details-button']")).click();

					// Pause to allow sufficient time to display page
					Thread.sleep(4000);

					// Check "Transaction Detail" page displayed
					if (driver.findElement(By.tagName("h1")).getText().equals("Transaction detail")) {
						System.out.println("[CHECK - Waste] Transaction Detail page displayed");

						// Check "Suggested category" section displayed
						if (driver.findElement(By.tagName("h2")).getText().equals("Suggested category")) {
							System.out.println(
									"[CHECK - Waste] Transaction Detail page - Suggested category section displayed");
						} else {
							System.out.println(
									"[WARNING!! - Waste] Transaction Detail page - Suggested category section NOT displayed");
						}

						// View Change History for Transaction

						driver.findElement(By.xpath("//a[@class='btn btn-outline-info']")).click();

						// Pause to allow sufficient time for page to be re-displayed
						Thread.sleep(1000);

						if (driver.findElement(By.tagName("h1")).getText().equals("Transaction change history")) {
							System.out.println("[CHECK - Waste] Transaction Change History page displayed");

							baseTableWABS = driver.findElement(By.className("table-responsive"));

							wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table/tbody/tr[1]")));

							if (driver.findElements(By.xpath("//table/tbody/tr[1]")).size() != 0) {
								tableRowWABS = baseTableWABS.findElement(By.xpath("//table/tbody/tr[1]"));
								String rowtextPAS = tableRowWABS.getText();
								System.out.println("[DATA - Waste] First audit event row: " + rowtextPAS);
							}

							// Click the 'Back' link from Transaction Change History
							driver.findElement(By.xpath("//a[@class='back-link']")).click();

							// Scroll down page
							JavascriptExecutor jse = (JavascriptExecutor) driver;
							jse.executeScript("window.scrollBy(0,900)", "");

							int h2tagelements = driver.findElements(By.tagName("h2")).size();
							System.out.println("[CHECK - Waste] h2 TAGS FOUND = " + h2tagelements);

							if (h2tagelements > 1) {
								List<WebElement> h2tags = driver.findElements(By.tagName("h2"));
								WebElement h2tag1 = h2tags.get(1);

								if (h2tag1.getText().equals("Related billed transactions")) {
									System.out.println(
											"[CHECK - Waste] Transaction Detail page - Related billed transactions section displayed");
									if (h2tagelements > 2) {
										WebElement h2tag2 = h2tags.get(2);
										// System.out.println("[CHECK - Water Quality] h2 TAGS FOUND = "+h2tag2);
										if (h2tag2.getText().equals("Related unbilled transactions")) {
											System.out.println(
													"[CHECK - Waste] Transaction Detail page - Related unbilled transactions section displayed");
										}
									}
								}
						}
							// Click the <Back> link to return to TTBB
							driver.findElement(By.xpath("//a[@class='back-link']")).click();

							// Pause to allow sufficient time for TTBB to be re-displayed
							Thread.sleep(2000);

							// Check Export button displayed
							if (driver.findElement(By.cssSelector("button.btn-sm.btn-outline-secondary"))
									.isDisplayed()) {
								System.out.println("[CHECK - Waste] Export Transactions button displayed");
								driver.findElement(By.cssSelector("button.btn-sm.btn-outline-secondary")).click();
								Thread.sleep(1000);
								// Check 'Export Transaction Data' modal displayed
								driver.switchTo().activeElement();
								if (driver.findElement(By.tagName("h5")).getText().equals("Export Transaction Data")) {
									System.out.println(
											"[CHECK - Waste] 'Export Transaction Data' modal displayed");
									// Cancel
									driver.findElement(By.xpath("//button[@class='btn btn-secondary']")).click();
									Thread.sleep(1000);
								} else {
									System.out.println(
											"[ERROR!! - Waste] 'Export Transaction Data' modal NOT displayed");
								}
							} else {
								System.out
										.println("[ERROR!! - Waste] Export Transactions button NOT displayed");
							}
							// Sort transactions in TTBB by Customer

							baseTableWABS = driver.findElement(By.className("table-responsive"));
							tableHeaderWABS = baseTableWABS.findElement(By.xpath("//table/thead/tr/th[4]/a"));
							tableHeaderWABS.click();
							Thread.sleep(1000);

							// Set the 'Temporary cessation' flag to 'Y' for the first transaction
							baseTableWABS = driver.findElement(By.className("table-responsive"));

							int isPresent = baseTableWABS
									.findElements(
											By.xpath("//select[@class='form-control temporary-cessation-select']"))
									.size();
							if (isPresent > 0) {
								dropdown = new Select(baseTableWABS.findElement(
										By.xpath("//select[@class='form-control temporary-cessation-select']")));
								dropdown.selectByValue("true");
								System.out.println("[DATA - Waste] Temporary cessation flag set to 'Y'");
							}

							// Exclude second transaction

							baseTableWABS = driver.findElement(By.className("table-responsive"));
							WebElement tableRow2 = baseTableWABS.findElement(By.xpath("//table/tbody/tr[1]"));
							tableRow2.findElement(By.xpath("//button[@class='btn btn-sm show-details-button']"))
									.click();

							// Check whether transaction already excluded, if so re-instate it
							isPresent = driver
									.findElements(By.xpath(
											"//input[@class='btn btn-success' and @value = 'Reinstate for Billing']"))
									.size();
							if (isPresent > 0) {
								driver.findElement(By.xpath(
										"//input[@class='btn btn-success' and @value = 'Reinstate for Billing']"))
										.click();
								Thread.sleep(1000);
							}
							driver.findElement(By.xpath("//button[@class='btn btn-danger exclude-button']")).click();

							Thread.sleep(1000);

							// Check 'Exclude Transaction' modal displayed
							if (driver.findElement(By.tagName("h5")).getText().equals("Exclude Transaction")) {
								System.out.println("[CHECK - Waste] 'Exclude Transaction' modal displayed");

								// Click "Exclude Transaction" button in modal
								driver.findElement(By.xpath("//input[@class='btn btn-danger']")).click();

								// Confirm transaction successfully marked for exclusion

								if (driver.findElement(By.xpath("//span[@class='badge badge-pill badge-danger']"))
										.isDisplayed()) {
									System.out.println("[CHECK - Waste] Transaction 'Marked for exclusion'");
								} else {
									System.out.println("[ERROR!! - Waste] Transaction NOT 'Marked for exclusion'!");
								}

								// Confirm 'Reinstate for billing' button displayed

								if (driver.findElement(By.xpath("//input[@value='Reinstate for Billing']"))
										.isDisplayed()) {
									System.out.println("[CHECK - Waste] 'Reinstate for Billing' buttton found");
								} else {
									System.out.println("[ERROR!! - Waste] 'Reinstate for Billing' buttton NOT found!");
								}

								// Click the <Back> link to return to TTBB
								driver.findElement(By.xpath("//a[@class='back-link']")).click();
								Thread.sleep(500);
								baseTableWABS = driver.findElement(By.className("table-responsive"));
								driver.findElement(By.xpath("//table/thead/tr/th[6]/a")).click();
								System.out.println("[CHECK - Waste] Sort by 'Category' (asc)");

								// Set a charge for any remaining transactions for the same Permit reference
								Thread.sleep(1000);
								baseTableWABS = driver.findElement(By.className("table-responsive"));

								int elemcount = baseTableWABS
										.findElements(By.xpath("//input[@class='tcm-select-input' and @value = '']"))
										.size();
								System.out.println(
										"[CHECK - Waste] " + elemcount + " records in same group do not have a charge");
								Thread.sleep(1000);
								baseTableWABS = driver.findElement(By.className("table-responsive"));
								List<WebElement> elements = baseTableWABS
										.findElements(By.xpath("//input[@class='tcm-select-input' and @value = '']"));
								Thread.sleep(1000);
								java.util.Iterator<WebElement> i = elements.iterator();
								while (i.hasNext()) {
									baseTableWABS = driver.findElement(By.className("table-responsive"));
									elements = baseTableWABS.findElements(
											By.xpath("//input[@class='tcm-select-input' and @value = '']"));
									WebElement element = i.next();

									int isThere = driver
											.findElements(
													By.xpath("//input[@class='tcm-select-input' and @value = '']"))
											.size();
									if (isThere > 0) {
										baseTableWABS = driver.findElement(By.className("table-responsive"));

										// Set permit category on first transaction
										randomNum = ThreadLocalRandom.current().nextInt(1, 50);

										Thread.sleep(1000);
										baseTableWABS = driver.findElement(By.className("table-responsive"));
										wait = new WebDriverWait(driver, 15);
										wait.until(ExpectedConditions.visibilityOfElementLocated(
												By.xpath("//input[@class='tcm-select-input' and @value = '']")));
										baseTableWABS
												.findElement(
														By.xpath("//input[@class='tcm-select-input' and @value = '']"))
												.click();
										Thread.sleep(100);
										iT = 1;
										while (iT <= randomNum) {
											baseTableWABS
													.findElement(By.xpath(
															"//input[@class='tcm-select-input' and @value = '']"))
													.sendKeys(Keys.DOWN);
											// Thread.sleep(50);
											iT++;
										}

										baseTableWABS
												.findElement(
														By.xpath("//input[@class='tcm-select-input' and @value = '']"))
												.sendKeys(Keys.RETURN);
										Thread.sleep(2000);
										System.out.println("[CHECK - Waste] Charge set for consent in same group");
									}
								}
							}

							// Click 'Approve All' button
							driver.findElement(By.xpath("//button[@class='btn btn-success approve-all-btn']"))
									.click();
							System.out.println("[CHECK - Waste] 'Approve All' button clicked");
							Thread.sleep(2000);

							// GENERATE TRANSACTION FILE
							// Click Generate Transaction File button
							wait = new WebDriverWait(driver, 15);
							wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tbody/tr")));

							driver.findElement(
									By.xpath("//button[@class='btn btn-primary generate-transaction-file-btn mr-4']"))
									.click();

							Thread.sleep(1000);

							// Confirm Transaction File Summary displayed
							driver.switchTo().activeElement();
							if (driver.findElement(By.className("modal-header")).isEnabled()) {
								System.out.println("[CHECK - Waste] Transaction File Summary displayed");

								// Click "I have checked the summary and wish to proceed" checkbox
								if (driver.findElement(By.xpath("//input[@class='form-check-input ml-2']"))
										.isEnabled()) {
									driver.findElement(By.xpath("//input[@class='form-check-input ml-2']")).click();
									System.out.println(
											"[CHECK - Waste] 'I have checked the summary and wish to proceed' checkbox clicked");
									Thread.sleep(1000);
								} else {
									System.out.println(
											"[WARNING!! - Waste] 'I have checked the summary and wish to proceed' checkbox is not enabled");
								}

								Thread.sleep(1000);

								if (driver.findElement(By.xpath("//input[@class='btn btn-primary file-generate-btn']"))
										.isEnabled()) {
									// Click Generate Transaction File button
									driver.findElement(By.xpath("//input[@class='btn btn-primary file-generate-btn']"))
											.click();
									System.out.println("[CHECK - Waste] Generate Transaction File button clicked");

									Thread.sleep(2000);

									// Confirm file generated successfully
									String message = driver
											.findElement(By.xpath(
													"//div[@class='alert alert-success alert-dismissable fade show']"))
											.getText();
									System.out.println("[CHECK - Waste]:" + message);

								} else {
									// No records included in File Summary. Click 'Cancel' button
									driver.findElement(By.xpath("//button[@class='btn btn-secondary']")).click();
									Thread.sleep(1000);
									System.out.println(
											"[WARNING!! - Waste] No approved records in File Summary. File not generated.");
								}

							} else {
								System.out.println("[ERROR!! - Waste] Transaction File Summary NOT displayed!");
							}

						} else {
							System.out.println("[ERROR!! - Waste] Transaction Change History page NOT displayed!");
						}

					} else {
						System.out.println("[ERROR!! - Waste] Transaction Detail page NOT displayed!");
					}

				} else {
					System.out.println("[WARNING!! - Waste] No transactions found on TTBB");
				}

				// Select Transaction File History for Waste
				driver.findElement(By.id("navbarTransactionsSelectorLink")).click();
				driver.findElement(By.linkText("Transaction File History")).click();

				Thread.sleep(2000);

				// Check Transaction File History screen displayed
				if (driver.findElement(By.tagName("h1")).getText().equals("Transaction File History")) {
					System.out.println("[CHECK - Waste] Transaction File History screen displayed");
				} else {
					System.out.println("[ERROR!! - Waste] Transaction File History screen NOT displayed");
				}

				// Set Region to 'All'
				dropdown = new Select(driver.findElement(By.id("region")));
				dropdown.selectByVisibleText("All");
				System.out.println("[CHECK - Waste] Transaction File History - region filter set to 'All'");

				Thread.sleep(1000);

				// Sort records by 'Date Generated' (desc)

				Thread.sleep(1000);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
				baseTableWABS = driver.findElement(By.className("table-responsive"));
				driver.findElement(By.xpath("//table/thead/tr/th[2]/a")).click();
				Thread.sleep(1000);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr")));
				baseTableWABS = driver.findElement(By.className("table-responsive"));
				driver.findElement(By.xpath("//table/thead/tr/th[2]/a")).click();
				System.out.println("[CHECK - Waste] Sort by 'Date Generated' (desc)");

				Thread.sleep(1000);

				if (driver.findElements(By.xpath("//table/tbody/tr[1]")).size() != 0) {
					baseTableWABS = driver.findElement(By.className("table-responsive"));
					WebElement tableRowWABS = baseTableWABS.findElement(By.xpath("//table/tbody/tr[1]"));
					String rowtextWABS = tableRowWABS.getText();
					System.out.println("[DATA - Waste] Transaction File History record found: " + rowtextWABS);
				} else {
					System.out.println("[WARNING!! - Waste] No Transaction File History records found");
				}

				// Select Excluded Transactions

				driver.findElement(By.id("navbarTransactionsSelectorLink")).click();
				driver.findElement(By.linkText("Excluded Transactions")).click();

				Thread.sleep(2000);

				// Check Excluded Transactions screen displayed
				if (driver.findElement(By.tagName("h1")).getText().equals("Excluded Transactions")) {
					System.out.println("[CHECK - Waste] Excluded Transactions screen displayed");
				} else {
					System.out.println("[ERROR!! - Waste] Excluded Transactions screen NOT displayed");
				}

				// Find first row of 'Excluded Transactions' table
				// WebElement baseTableWABS =
				// driver.findElement(By.className("table-responsive"));

				if (driver.findElements(By.xpath("//table/tbody/tr[1]")).size() != 0) {
					baseTableWABS = driver.findElement(By.className("table-responsive"));
					WebElement tableRowWABS = baseTableWABS.findElement(By.xpath("//table/tbody/tr[1]"));
					String rowtextWABS = tableRowWABS.getText();
					System.out.println(
							"[DATA - Waste] Excluded Transaction record found with charge row detail: " + rowtextWABS);
				} else {
					System.out.println("[WARNING!! - Waste] No excluded transaction records found");
				}
				
				// Check Export button displayed
				if (driver.findElement(By.xpath("//button[@class='btn btn-sm btn-outline-secondary table-export-btn']"))
						.isDisplayed()) {
					System.out.println(
							"[CHECK - Waste] Excluded Transactions - 'Export Transactions' button displayed");
					driver.findElement(By.xpath("//button[@class='btn btn-sm btn-outline-secondary table-export-btn']"))
							.click();
					Thread.sleep(1000);
					// Check 'Export Transaction Data' modal displayed
					driver.switchTo().activeElement();
					if (driver.findElement(By.tagName("h5")).getText().equals("Export Transaction Data")) {
						System.out.println(
								"[CHECK - Waste] Excluded Transactions - 'Export Transaction Data' modal displayed");
						// Cancel
						driver.findElement(By.xpath("//button[@class='btn btn-secondary']")).click();
						Thread.sleep(1000);
					} else {
						System.out.println(
								"[ERROR!! - Waste] Excluded Transactions - 'Export Transaction Data' modal NOT displayed");
					}
				} else {
					System.out.println(
							"[ERROR!! - Waste] Excluded Transactions - 'Export Transactions' button NOT displayed");
				}

				// Select Transaction History
				dropdown = new Select(driver.findElement(By.id("mode")));
				dropdown.selectByValue("historic");

				Thread.sleep(1000);

				// Check Transactions History screen displayed
				if (driver.findElement(By.tagName("h1")).getText().equals("Transaction History")) {
					System.out.println("[CHECK - Waste] Transaction History screen displayed");
				} else {
					System.out.println("[ERROR!! - Waste] Transaction History screen not displayed");
				}

				// Find first row of 'Transaction History ' table
				if (driver.findElements(By.xpath("//table/tbody/tr[1]")).size() != 0) {
					baseTableWABS = driver.findElement(By.className("table-responsive"));
					WebElement tableRowWABS = baseTableWABS.findElement(By.xpath("//table/tbody/tr[1]"));
					String rowtextWABS = tableRowWABS.getText();
					System.out.println(
							"[DATA - Waste] Transaction History record found with charge row detail: " + rowtextWABS);
				} else {
					System.out.println("[WARNING!! - Waste] No Transaction History records found");
				}

				// Check Export button displayed
				if (driver.findElement(By.xpath("//button[@class='btn btn-sm btn-outline-secondary table-export-btn']"))
						.isDisplayed()) {
					System.out.println(
							"[CHECK - Waste] Excluded Transactions - 'Export Transactions' button displayed");
					driver.findElement(By.xpath("//button[@class='btn btn-sm btn-outline-secondary table-export-btn']"))
							.click();
					Thread.sleep(1000);
					// Check 'Export Transaction Data' modal displayed
					driver.switchTo().activeElement();
					if (driver.findElement(By.tagName("h5")).getText().equals("Export Transaction Data")) {
						System.out.println(
								"[CHECK - Waste] Excluded Transactions - 'Export Transaction Data' modal displayed");
						// Cancel
						driver.findElement(By.xpath("//button[@class='btn btn-secondary']")).click();
						Thread.sleep(1000);
					} else {
						System.out.println(
								"[ERROR!! - Waste] Excluded Transactions - 'Export Transaction Data' modal NOT displayed");
					}
				} else {
					System.out.println(
							"[ERROR!! - Waste] Excluded Transactions - 'Export Transactions' button NOT displayed");
				}
				
				// Select Download Transaction Data from Transactions menu
		//		driver.findElement(By.id("navbarTransactionsSelectorLink")).click();
		//		driver.findElement(By.linkText("Download Transaction Data")).click();
				
				// Confirm Data Protection Notice on 'Download Transaction Data' page displayed
		//		if (driver.findElement(By.tagName("h5")).getText().equals("Data Protection Notice")) {
			//		System.out.println(
				//			"[CHECK - Waste] 'Data Protection Notice on Download Transaction Data screen is displayed");
				//			driver.findElement(By.xpath("//a[@class='btn btn-primary']")).click();
				//			System.out.println(
				//					"[CHECK - Waste] 'Agree and download transaction data' button clicked");
				//		} else {
				//			System.out.println(
				//			"[ERROR!! - Waste] 'Data Protection Notice on Download Transaction Data screen NOT displayed");
			//	}
				
				// Select Imported Transaction Files from Transactions menu
				driver.findElement(By.id("navbarTransactionsSelectorLink")).click();
				driver.findElement(By.linkText("Imported Transaction Files")).click();
				
				// Confirm 'Imported Transaction Files' page displayed
				if (driver.findElement(By.tagName("h1")).getText().equals("Imported Transaction Files")) {
					System.out.println(
							"[CHECK - Waste] 'Imported Transaction Files screen is displayed");
					
					// Retrieve data in first row, ie for the most recently imported file
					Thread.sleep(1000);

					if (driver.findElements(By.xpath("//table/tbody/tr[1]")).size() != 0) {
						baseTableWQ = driver.findElement(By.className("table-responsive"));
						WebElement tableRowWQ = baseTableWQ.findElement(By.xpath("//table/tbody/tr[1]"));
						String rowtextWQ = tableRowWQ.getText();
						System.out.println("[DATA - Waste] Most recently imported transaction file record: " + rowtextWQ);
						} else {
							System.out.println("[WARNING!! - Waste] No Imported Transaction File records found");
					} 
				}


				// Sign out of the TCM
				driver.findElement(By.id("navbarUserMenuLink")).click();
				driver.findElement(By.linkText("Sign out")).click();

				// Confirm successful sign out

				wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")));

				if (driver.findElement(By.tagName("h1")).getText().equals("Sign in")) {
					System.out.println("[CHECK] TCM Sign out successful");
				} else {
					System.out.println("[ERROR!!] TCM sign out unsuccessful!");
				}

			} else {
				System.out.println("[ERROR!!] Login to the TCM failed!");
			}

		} else {
			System.out.println("[ERROR!!] TCM login page not found!");
		}
		

		// Close the browser and WebDriver
		// driver.close();
		// driver.quit();
	}
	

	public static String decodeStr(String encodedStr) {
		byte[] decoded = Base64.decodeBase64(encodedStr);
		return new String(decoded);
	}

}
