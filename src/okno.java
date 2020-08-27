import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;

import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Realm;




@SuppressWarnings("deprecation")
public class okno {

	protected Shell shell;
	private Text PodajLink;
	private Label lbposteptext;
	private Button btnDirectory;
	private Label lblPodajciekDo;

	/**
	 * Launch the application.
	 * @param args
	 */
	
	public static void main(String[] args) {
		Display display = Display.getDefault();
		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
			public void run() {
				try {
					okno window = new okno();
					window.open();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setBackground(SWTResourceManager.getColor(51, 51, 51));
		shell.setSize(450, 300);
		shell.setText("Pobieracz Obrazów");
		
		PodajLink = new Text(shell, SWT.BORDER);
		PodajLink.setBounds(10, 124, 414, 21);
		
		Label FindPathLabel = new Label(shell, SWT.NONE);
		FindPathLabel.setText("C:\\\\Users\\\\Andrzej Kondas\\\\Desktop\\\\Projekty\\\\Projekt OI\\\\Obrazki");
		FindPathLabel.setForeground(SWTResourceManager.getColor(255, 255, 255));
		FindPathLabel.setBackground(SWTResourceManager.getColor(51, 51, 51));
		FindPathLabel.setBounds(10, 41, 414, 15);
		
		Label lblPodajLinkDo = new Label(shell, SWT.NONE);
		lblPodajLinkDo.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lblPodajLinkDo.setForeground(SWTResourceManager.getColor(255, 255, 255));
		lblPodajLinkDo.setBackground(SWTResourceManager.getColor(51, 51, 51));
		lblPodajLinkDo.setBounds(10, 94, 333, 25);
		lblPodajLinkDo.setText("Podaj link do strony, z kt\u00F3rej chcesz pobra\u0107 zdj\u0119cia");
		
		Button btnPobierz = new Button(shell, SWT.NONE);
		btnPobierz.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				try {
					 String FilePath = FindPathLabel.getText();
					 String strona = PodajLink.getText();
					 
					 System.setProperty("webdriver.chrome.driver",
							"C:\\Users\\Andrzej Kondas\\Downloads\\chromedriver_win32\\chromedriver.exe");
					 lbposteptext.setText("Praca w toku... Poczekaj kilka sekund.");
							
					 WebDriver driver = new ChromeDriver();
					 driver.manage().window().setPosition(new Point(-2000, 0));
					 driver.get(strona);

					 List<WebElement> listImages = driver.findElements(By.tagName("img"));
					 System.out.println("Number of images "+listImages.size());

					 for(int n = 0 ; n < listImages.size() ; n++){
						 	
						 String link = listImages.get(n).getAttribute("src");
						 String linkTemp= link.replace("jpg", "png");
						 
						 if (link.startsWith("http://i.4cdn") || link.startsWith("https://i.4cdn")) {
								 link = link.replace("s","");
						 }
						 
						 if (linkTemp.startsWith("http://i.4cdn") || linkTemp.startsWith("https://i.4cdn")) {
							 linkTemp = linkTemp.replace("s","");
						 }
						 
						 String NazwaPliku = link.substring(link.lastIndexOf('/')+1, link.length());
						 String NazwaPlikuTemp = linkTemp.substring(linkTemp.lastIndexOf('/')+1, linkTemp.length());
						    
						 System.out.println(link);
						 
						 if (null != NazwaPliku && NazwaPliku.length() > 0 )
						 {
						     int endIndex = NazwaPliku.lastIndexOf("?");
						     if (endIndex != -1)  
						     {
						         NazwaPliku = NazwaPliku.substring(0, endIndex); // not forgot to put check if(endIndex != -1)
						     }
						 }  

						 String Sciezka = FilePath + "\\" + NazwaPliku;
						 String SciezkaTemp = FilePath + "\\" + NazwaPlikuTemp;
						 
						 	if(!(listImages.get(n).getAttribute("src").equals("")) && !(listImages.get(n).getAttribute("src") == null)) {
						 		System.out.println(listImages.get(n).getAttribute("src"));
						 		if(!(link.endsWith("secure=1") || link.startsWith("http://s.4cdn.org/image/title/") || link.startsWith("http://s.4cdn.org/image/contest") || link.endsWith("geo=35") || link.endsWith("minus.png") || link.endsWith("_") || link.endsWith("=") || link.endsWith("content") || link.endsWith("-?\\d+(\\.\\d+)?"))) {
						 			File out = new File(Sciezka);

						 			
						 			new Thread(new Download(link, out)).start();
						 			if(linkTemp.startsWith("http://i.4cdn.org") || linkTemp.startsWith("https://i.4cdn.org")) {
						 			File outTemp = new File(SciezkaTemp);
							 		new Thread(new Download(linkTemp, outTemp)).start();
						 			}

						 		
								}
						 	}
					 	}
					 		lbposteptext.setText("Pobieranie zakoñczy³o siê");
							driver.close();
					}
					catch(Exception exc){
						MessageDialog.openError(shell,"Error","Program nie by³ w stanie pobraæ wszystkich wykrytych plików!");
						
					}

				}
		});
		btnPobierz.setBounds(180, 208, 75, 25);
		btnPobierz.setText("Pobierz");
		
		lbposteptext = new Label(shell, SWT.NONE);
		lbposteptext.setForeground(SWTResourceManager.getColor(255, 255, 255));
		lbposteptext.setBackground(SWTResourceManager.getColor(51, 51, 51));
		lbposteptext.setBounds(10, 151, 269, 15);
		
		btnDirectory = new Button(shell, SWT.NONE);
		btnDirectory.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				try {
					
					JFileChooser chooser = new JFileChooser();
				    chooser.setCurrentDirectory(new java.io.File("."));
				    chooser.setDialogTitle("choosertitle");
				    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				    chooser.setAcceptAllFileFilterUsed(false);

				    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				      System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
				      System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
				      FindPathLabel.setText("" + chooser.getSelectedFile());
				    } else {
				      System.out.println("No Selection ");
				    }
					}
					catch(Exception exc){
						MessageDialog.openError(shell,"Error","Coœ posz³o nie tak!");
					}

				}
		});
		btnDirectory.setBounds(349, 10, 75, 25);
		btnDirectory.setText("Wybierz");
		
		lblPodajciekDo = new Label(shell, SWT.NONE);
		lblPodajciekDo.setBackground(SWTResourceManager.getColor(51, 51, 51));
		lblPodajciekDo.setForeground(SWTResourceManager.getColor(255, 255, 255));
		lblPodajciekDo.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lblPodajciekDo.setBounds(10, 10, 333, 25);
		lblPodajciekDo.setText("Wybierz \u015Bcie\u017Ck\u0119, do kt\u00F3rej chcesz zapisa\u0107 zdj\u0119cia");
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		return bindingContext;
	}
}
