
import java.io.File;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Run {

	public static void main(String[] args) throws Exception {
		

		
		System.setProperty("webdriver.chrome.driver",
		"C:\\Users\\Andrzej Kondas\\Downloads\\chromedriver_win32\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		
		// * ------ PODAJ STRONE ------ * //
		String strona = "https://www.deviantart.com/";
		driver.get(strona);
	
		List<WebElement> listImages = driver.findElements(By.tagName("img"));
		System.out.println("Number of images "+listImages.size());
		
		// *----------------------------* //
		int ostatni = 0;
		for(int n = 0 ; n < listImages.size() ; n++){
			
			String link = listImages.get(n).getAttribute("src");
			
			if(!(listImages.get(n).getAttribute("src").equals("")) && !(listImages.get(n).getAttribute("src") == null)) {

				System.out.println(listImages.get(n).getAttribute("src"));


				    File out = new File("C:\\Users\\Andrzej Kondas\\Desktop\\Projekty\\Projekt OI\\Obrazki\\test" + ostatni + ".jpg");
				    	new Thread(new Download(link, out)).start();
					    ostatni++;

			}
		}
	}
}
