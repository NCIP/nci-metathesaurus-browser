package gov.nih.nci.evs.testUtil.ui;
import gov.nih.nci.evs.testUtil.*;

import gov.nih.nci.evs.browser.utils.*;
import gov.nih.nci.evs.browser.bean.*;

import java.io.*;
import java.util.*;
import java.net.*;

public class NCImUITestGeneratorRunner {

	private static String PACKAGE_NAME = "package_name";
	private static String CLASS_NAME = "class_name";
	private static String REMOTE_WEB_DRIVER_URL = "remoteWebDriverURL";
	private static String BASE_URL = "baseUrl";
	private static String SERVICE_URL = "serviceUrl";
	private static String DELAY = "delay";

	private Properties properties = null;
    private String propertyFile = "resources/Test.properties";
    private CodeGeneratorConfiguration config = null;
    private NCImUITestGenerator generator = null;

    public NCImUITestGeneratorRunner() {
		initialize();
	}

    public NCImUITestGeneratorRunner(String propertyFile) {
		this.propertyFile = propertyFile;
		initialize();
	}

	public void initialize() {
		properties = loadProperties();
	    String packageName = properties.getProperty(PACKAGE_NAME);
	    String className = properties.getProperty(CLASS_NAME);
	    String remoteWebDriverURL = properties.getProperty(REMOTE_WEB_DRIVER_URL);
	    String baseUrl = properties.getProperty(BASE_URL);
	    String serviceUrl = properties.getProperty(SERVICE_URL);
	    String delay_str = properties.getProperty(DELAY);
	    int delay = 1;
	    if (delay_str != null) {
	    	delay = Integer.parseInt(delay_str);
		}

	    config = new CodeGeneratorConfiguration();
	    config.setPackageName(packageName);
	    config.setClassName(className);
	    config.setRemoteWebDriverURL(remoteWebDriverURL);
	    config.setBaseUrl(baseUrl);
	    config.setServiceUrl(serviceUrl);
	    config.setDelay(delay);

	    System.out.println("packageName: " + packageName);
	    System.out.println("className: " + className);
	    System.out.println("remoteWebDriverURL: " + remoteWebDriverURL);
	    System.out.println("baseUrl: " + baseUrl);
	    System.out.println("serviceUrl: " + serviceUrl);
	    System.out.println("delay: " + delay);

	    System.out.println("Instantiate NCImUITestGenerator... ");
	    generator = new NCImUITestGenerator(config);
	    System.out.println("NCImUITestGenerator instantiated.");
	}

	public void run() {
	    generator.run();
	}

	private Properties loadProperties() {
		try{
			properties = new Properties();
			if(propertyFile != null && propertyFile.length() > 0){
				FileInputStream fis = new FileInputStream(new File(propertyFile));
				properties.load(fis);
			}
			for(Iterator i = properties.keySet().iterator(); i.hasNext();){
				String key = (String)i.next();
				String value  = properties.getProperty(key);
			}
			return properties;
		} catch (Exception e){
			System.out.println("Error reading properties file");
			e.printStackTrace();
			return null;
		}
	}

    public static void main(String[] args) {
		if (args.length>0) {
			String propertyFile = args[0];
			NCImUITestGeneratorRunner runner = new NCImUITestGeneratorRunner(propertyFile);
			runner.run();
		} else {
			NCImUITestGeneratorRunner runner = new NCImUITestGeneratorRunner();
			runner.run();
		}
    }
}




