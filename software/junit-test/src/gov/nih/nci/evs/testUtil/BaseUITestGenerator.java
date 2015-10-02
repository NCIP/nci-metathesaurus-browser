package gov.nih.nci.evs.testUtil;


import gov.nih.nci.evs.browser.bean.*;
import gov.nih.nci.evs.browser.utils.*;
import java.io.*;


/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008-2015 NGIT. This software was developed in conjunction
 * with the National Cancer Institute, and so to the extent government
 * employees are co-authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *   1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the disclaimer of Article 3,
 *      below. Redistributions in binary form must reproduce the above
 *      copyright notice, this list of conditions and the following
 *      disclaimer in the documentation and/or other materials provided
 *      with the distribution.
 *   2. The end-user documentation included with the redistribution,
 *      if any, must include the following acknowledgment:
 *      "This product includes software developed by NGIT and the National
 *      Cancer Institute."   If no such end-user documentation is to be
 *      included, this acknowledgment shall appear in the software itself,
 *      wherever such third-party acknowledgments normally appear.
 *   3. The names "The National Cancer Institute", "NCI" and "NGIT" must
 *      not be used to endorse or promote products derived from this software.
 *   4. This license does not authorize the incorporation of this software
 *      into any third party proprietary programs. This license does not
 *      authorize the recipient to use any trademarks owned by either NCI
 *      or NGIT
 *   5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED
 *      WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *      OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE
 *      DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
 *      NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT,
 *      INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *      BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *      LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *      CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 *      LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 *      ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *      POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * @author EVS Team
 * @version 1.0
 *
 * Modification history:
 *     Initial implementation kim.ong@ngc.com
 *
 */


public class BaseUITestGenerator {

   public static void printImportStatements(PrintWriter out) {
	  out.println("import gov.nih.nci.evs.testUtil.ui.*;");
	  out.println("import gov.nih.nci.evs.testUtil.*;");
      out.println("import gov.nih.nci.evs.browser.utils.*;");
      out.println("import gov.nih.nci.evs.browser.common.*;");
      out.println("import gov.nih.nci.evs.security.SecurityToken;");
      out.println("import gov.nih.nci.system.client.ApplicationServiceProvider;");
      out.println("import java.io.File;");
      out.println("import java.io.IOException;");
      out.println("import java.net.*;");
      out.println("import java.text.SimpleDateFormat;");
      out.println("import java.util.Date;");
      out.println("import java.util.Iterator;");
      out.println("import java.util.List;");
      out.println("import java.util.Set;");
      out.println("import java.util.HashSet;");
      out.println("import java.util.Vector;");
      out.println("import java.util.concurrent.TimeUnit;");
      out.println("import org.LexGrid.LexBIG.DataModel.Collections.*;");
      out.println("import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;");
      out.println("import org.LexGrid.LexBIG.DataModel.Collections.ModuleDescriptionList;");
      out.println("import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;");
      out.println("import org.LexGrid.LexBIG.DataModel.Core.*;");
      out.println("import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;");
      out.println("import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;");
      out.println("import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;");
      out.println("import org.LexGrid.LexBIG.DataModel.Core.types.*;");
      out.println("import org.LexGrid.LexBIG.DataModel.InterfaceElements.*;");
      out.println("import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;");
      out.println("import org.LexGrid.LexBIG.DataModel.InterfaceElements.ModuleDescription;");
      out.println("import org.LexGrid.LexBIG.Exceptions.*;");
      out.println("import org.LexGrid.LexBIG.Exceptions.LBException;");
      out.println("import org.LexGrid.LexBIG.Extensions.Generic.*;");
      out.println("import org.LexGrid.LexBIG.Extensions.Generic.CodingSchemeReference;");
      out.println("import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods.*;");
      out.println("import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension;");
      out.println("import org.LexGrid.LexBIG.LexBIGService.*;");
      out.println("import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.*;");
      out.println("import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;");
      out.println("import org.LexGrid.LexBIG.LexBIGService.LexBIGService;");
      out.println("import org.LexGrid.LexBIG.Utility.*;");
      out.println("import org.LexGrid.LexBIG.Utility.Constructors;");
      out.println("import org.LexGrid.LexBIG.Utility.Iterators.*;");
      out.println("import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;");
      out.println("import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;");
      out.println("import org.LexGrid.codingSchemes.*;");
      out.println("import org.LexGrid.codingSchemes.CodingScheme;");
      out.println("import org.LexGrid.concepts.*;");
      out.println("import org.LexGrid.naming.*;");
      out.println("import org.apache.commons.codec.language.*;");
      out.println("import org.apache.commons.io.FileUtils;");
      out.println("import org.apache.log4j.*;");
      out.println("import org.junit.*;");
      out.println("import org.junit.Assert;");
      out.println("import org.openqa.selenium.Alert;");
      out.println("import org.openqa.selenium.By;");
      out.println("import org.openqa.selenium.JavascriptExecutor;");
      out.println("import org.openqa.selenium.Keys;");
      out.println("import org.openqa.selenium.NoAlertPresentException;");
      out.println("import org.openqa.selenium.NoSuchElementException;");
      out.println("import org.openqa.selenium.OutputType;");
      out.println("import org.openqa.selenium.TakesScreenshot;");
      out.println("import org.openqa.selenium.WebDriver;");
      out.println("import org.openqa.selenium.WebElement;");
      out.println("import org.openqa.selenium.chrome.ChromeDriver;");
      out.println("import org.openqa.selenium.chrome.ChromeOptions;");
      out.println("import org.openqa.selenium.firefox.FirefoxBinary;");
      out.println("import org.openqa.selenium.firefox.FirefoxDriver;");
      out.println("import org.openqa.selenium.firefox.FirefoxProfile;");
      out.println("import org.openqa.selenium.ie.InternetExplorerDriver;");
      out.println("import org.openqa.selenium.interactions.Actions;");
      out.println("import org.openqa.selenium.remote.DesiredCapabilities;");
      out.println("import org.openqa.selenium.remote.RemoteWebDriver;");
      out.println("import org.openqa.selenium.support.ui.ExpectedConditions;");
      out.println("import org.openqa.selenium.support.ui.Select;");
      out.println("import org.openqa.selenium.support.ui.WebDriverWait;");
      out.println("import static org.junit.Assert.*;");
      out.println("\n");
   }


   public static void printWindowUtils(PrintWriter out) {
	  out.println("\n");
      out.println("	public void popUpWindow(String wndLabel) throws Exception {");
      out.println("		driver.findElement(By.linkText(wndLabel)).click();");
      out.println("		Thread.sleep(8000);");
      out.println("		String windowTitle= getCurrentWindowTitle();");
      out.println("		Thread.sleep(1000);");
      out.println("		String mainWindow = getMainWindowHandle(driver);");
      out.println("		Thread.sleep(1000);");
      out.println("		closeAllOtherWindows(mainWindow);");
      out.println("	}");
      out.println("");
      out.println("	public String getMainWindowHandle(WebDriver driver) {");
      out.println("		return driver.getWindowHandle();");
      out.println("	}");
      out.println("");
      out.println("	public String getCurrentWindowTitle() {");
      out.println("		String windowTitle = driver.getTitle();");
      out.println("		return windowTitle;");
      out.println("	}");
      out.println("");
      out.println("	public boolean closeAllOtherWindows(String openWindowHandle) {");
      out.println("		Set<String> allWindowHandles = driver.getWindowHandles();");
      out.println("		for (String currentWindowHandle : allWindowHandles) {");
      out.println("			if (!currentWindowHandle.equals(openWindowHandle)) {");
      out.println("				driver.switchTo().window(currentWindowHandle);");
      out.println("				driver.close();");
      out.println("			}");
      out.println("		}");
      out.println("");
      out.println("		driver.switchTo().window(openWindowHandle);");
      out.println("		if (driver.getWindowHandles().size() == 1)");
      out.println("			return true;");
      out.println("		else");
      out.println("			return false;");
      out.println("	}");
      out.println("");



	   out.println("	public boolean containsText(String text) {");
	   out.println("	    try {");
	   out.println("		    if (driver.findElement(By.xpath(\"//*[contains(.,'\" + text + \"')]\")) != null) {");
	   out.println("		        return true;");
	   out.println("		    }");
	   out.println("		} catch (Exception e) {");
	   out.println("		    return false;");
	   out.println("	    }");
	   out.println("	    return false;");
	   out.println("	}");
	   out.println("");

	   out.println("    public void goBack() {");
	   out.println("		driver.navigate().back();");
	   out.println("	}");
	   out.println("");

	   out.println("	public void navigateTo(String url) {");
	   out.println("		driver.get(url);");
	   out.println("	}");
	   out.println("");
	   out.println("");
	   out.println("    public void maximizeWindow() {");
	   out.println("		driver.manage().window().maximize();");
	   out.println("	}");
	   out.println("");
   }



   public static void printClassDefinition(PrintWriter out, String className) {
       out.println("public class " + className + " {");
       out.println("  private WebDriver driver;");
       out.println("  private String baseUrl;");
       out.println("  private boolean acceptNextAlert = true;");
       out.println("  private StringBuffer verificationErrors = new StringBuffer();");
 	   out.println("");
	   out.println("  private LexBIGService lbSvc = null;");
	   out.println("  private String serviceUrl = null;");
	   out.println("  private SimpleSearchUtils simpleSearchUtils = null;");
	   out.println("  private MappingSearchUtils mappingSearchUtils = null;");
	   out.println("  private ValueSetSearchUtils valueSetSearchUtils = null;");
	   out.println("  private ConceptDetails conceptDetails = null;");
	   out.println("  private SearchUtils searchUtils = null;");
	   out.println("");
	   out.println("  private String scheme = null;");
	   out.println("  private String version = null;");
	   out.println("  private String matchText = null;");
	   out.println("  private String target = null;");
	   out.println("  private int searchOption = 2;");
	   out.println("  private String algorithm = null;");
	   out.println("  private String propertyName = null;");
	   out.println("  private ResolvedConceptReferencesIterator rcr_iterator = null;");
	   out.println("  private ResolvedConceptReference rcref = null;");

       out.println("  private int search_direction = gov.nih.nci.evs.browser.common.Constants.SEARCH_SOURCE;");
       out.println("  private int maxToReturn = -1;");
       out.println("  private String source = null;");
       out.println("  private String[] associationsToNavigate = null;");
       out.println("  private String[] association_qualifier_names = null;");
       out.println("  private String[] association_qualifier_values = null;");
       out.println("  private boolean excludeDesignation = true;");
       out.println("  private boolean ranking = true;");
	   out.println("");
  }



   public static void printSetUp(PrintWriter out, String CHROME_SERVER_URL, String baseUrl, String serviceUrl) {
	   out.println("  @Before");
       out.println("  public void setUp() throws Exception {");
       out.println("    //driver = new FirefoxDriver();");
       out.println("    driver = new RemoteWebDriver(new URL(\"" + CHROME_SERVER_URL + "\"), DesiredCapabilities.chrome());");
       out.println("    serviceUrl = \"" + serviceUrl + "\";");
       out.println("    baseUrl = \"" + baseUrl + "\";");
       out.println("    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);");
       out.println("    driver.get(baseUrl + \"/ncitbrowser/pages/multiple_search.jsf\");");
	   out.println("");
	   out.println("    lbSvc = LexEVSServiceUtil.getLexBIGService(serviceUrl);");
	   out.println("    simpleSearchUtils = new SimpleSearchUtils(lbSvc);");
	   out.println("    mappingSearchUtils = new MappingSearchUtils(lbSvc);");
	   out.println("    valueSetSearchUtils = new ValueSetSearchUtils(lbSvc);");
       out.println("  }");
	   out.println("");
   }

   public static void printAfter(PrintWriter out) {

       out.println("	public static int getHTTPResponseCode(String url) {");
       out.println("		try {");
       out.println("		    URL u = new URL(url);");
       out.println("		    HttpURLConnection huc = (HttpURLConnection)u.openConnection();");
       out.println("		    huc.setRequestMethod(\"GET\");");
       out.println("		    huc.connect() ;");
       out.println("		    int code = huc.getResponseCode();");
       out.println("		    return code;");
       out.println("		} catch (Exception ex) {");
       out.println("			ex.printStackTrace();");
       out.println("		}");
       out.println("		return -1;");
       out.println("	}");
       out.println("\n");

       out.println("  @After");
       out.println("  public void tearDown() throws Exception {");
       out.println("    if (driver != null) driver.quit();");
       out.println("    String verificationErrorString = verificationErrors.toString();");
       out.println("    if (!\"\".equals(verificationErrorString)) {");
       out.println("      fail(verificationErrorString);");
       out.println("    }");
       out.println("  }");
       out.println("");
       out.println("  private boolean isElementPresent(By by) {");
       out.println("    try {");
       out.println("      driver.findElement(by);");
       out.println("      return true;");
       out.println("    } catch (NoSuchElementException e) {");
       out.println("      return false;");
       out.println("    }");
       out.println("  }");
       out.println("");
       out.println("  private boolean isAlertPresent() {");
       out.println("    try {");
       out.println("      driver.switchTo().alert();");
       out.println("      return true;");
       out.println("    } catch (NoAlertPresentException e) {");
       out.println("      return false;");
       out.println("    }");
       out.println("  }");
       out.println("");
       out.println("  private String closeAlertAndGetItsText() {");
       out.println("    try {");
       out.println("      Alert alert = driver.switchTo().alert();");
       out.println("      String alertText = alert.getText();");
       out.println("      if (acceptNextAlert) {");
       out.println("        alert.accept();");
       out.println("      } else {");
       out.println("        alert.dismiss();");
       out.println("      }");
       out.println("      return alertText;");
       out.println("    } finally {");
       out.println("      acceptNextAlert = true;");
       out.println("    }");
       out.println("  }");
       out.println("}");
   }

   public void printPackageStatement(PrintWriter out, String packageName) {
	   out.println("package " + packageName + ";");
	   out.println("\n");
   }

   public static void printLicenseStatement(PrintWriter out) {
      out.println("/**");
      out.println(" * <!-- LICENSE_TEXT_START -->");
      out.println(" * Copyright 2015 NGIS. This software was developed in conjunction");
      out.println(" * with the National Cancer Institute, and so to the extent government");
      out.println(" * employees are co-authors, any rights in such works shall be subject");
      out.println(" * to Title 17 of the United States Code, section 105.");
      out.println(" * Redistribution and use in source and binary forms, with or without");
      out.println(" * modification, are permitted provided that the following conditions");
      out.println(" * are met:");
      out.println(" *   1. Redistributions of source code must retain the above copyright");
      out.println(" *      notice, this list of conditions and the disclaimer of Article 3,");
      out.println(" *      below. Redistributions in binary form must reproduce the above");
      out.println(" *      copyright notice, this list of conditions and the following");
      out.println(" *      disclaimer in the documentation and/or other materials provided");
      out.println(" *      with the distribution.");
      out.println(" *   2. The end-user documentation included with the redistribution,");
      out.println(" *      if any, must include the following acknowledgment:");
      out.println(" *      \"This product includes software developed by NGIT and the National");
      out.println(" *      Cancer Institute.\"   If no such end-user documentation is to be");
      out.println(" *      included, this acknowledgment shall appear in the software itself,");
      out.println(" *      wherever such third-party acknowledgments normally appear.");
      out.println(" *   3. The names \"The National Cancer Institute\", \"NCI\" and \"NGIT\" must");
      out.println(" *      not be used to endorse or promote products derived from this software.");
      out.println(" *   4. This license does not authorize the incorporation of this software");
      out.println(" *      into any third party proprietary programs. This license does not");
      out.println(" *      authorize the recipient to use any trademarks owned by either NCI");
      out.println(" *      or NGIT");
      out.println(" *   5. THIS SOFTWARE IS PROVIDED \"AS IS,\" AND ANY EXPRESSED OR IMPLIED");
      out.println(" *      WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES");
      out.println(" *      OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE");
      out.println(" *      DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,");
      out.println(" *      NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT,");
      out.println(" *      INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,");
      out.println(" *      BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;");
      out.println(" *      LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER");
      out.println(" *      CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT");
      out.println(" *      LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN");
      out.println(" *      ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE");
      out.println(" *      POSSIBILITY OF SUCH DAMAGE.");
      out.println(" * <!-- LICENSE_TEXT_END -->");
      out.println(" */");
      out.println("");
      out.println("/**");
      out.println(" * @author EVS Team");
      out.println(" * @version 1.0");
      out.println(" *");
      out.println(" *      Modification history Initial implementation kim.ong@ngc.com");
      out.println(" *");
      out.println(" */");
  }

}
