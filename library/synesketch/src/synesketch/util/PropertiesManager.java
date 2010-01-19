package synesketch.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * A class for handling XML property files.  
 * 
 * @author Uros Krcadinac email: uros@krcadinac.com
 * @version 1.0
 */
public class PropertiesManager {
	
	private Properties props;
	
	private File file;
	
	/**
	 * Class constructor that loads properties from XML file.
	 * 
	 * @param fileName	name of the XML property file
	 */
	
	public PropertiesManager(String fileName) {
		props = new Properties();
		//URL fileURL = this.getClass().getResource(fileName); 
		//InputStream is = this.getClass().getResourceAsStream(fileName);
		//file = new File(fileURL.getFile()); 
		try {
			//props.loadFromXML(new FileInputStream(file));
			props.loadFromXML(this.getClass().getResourceAsStream(fileName));
		}  catch (IOException e) {
			props = new Properties();
			e.printStackTrace();
		}
	}
	
	/**
	 * Property getter for String properties.
	 * 
	 * @param key	property name
	 * @return		String value
	 */
	public String getProperty(String key) {
		return props.getProperty(key);
	}
	
	/**
	 * Property getter for int properties.
	 * 
	 * @param key	property name
	 * @return		int value
	 */
	public int getIntProperty(String key) {
		return Integer.parseInt(props.getProperty(key));
	}
	
	/**
	 * Property getter for array of ints properties.
	 * 
	 * @param key	property name
	 * @return		array of ints value
	 */
	public int[] getIntArrayProperty(String key) {
		String line = props.getProperty(key);
		String[] strings = line.split(", ");
		int[] value = new int[strings.length];
		for (int i = 0; i < value.length; i++) {
			value[i] = Integer.parseInt(strings[i], 16);
		}
		return value;
	}
	
	/**
	 * Property getter for boolean properties.
	 * 
	 * @param key	property name
	 * @return		boolean value
	 */
	public boolean getBooleanProperty(String key) {
		return Boolean.parseBoolean(props.getProperty(key));
	}

	/**
	 * Property getter for long properties.
	 * 
	 * @param key	property name
	 * @return		long value
	 */
	public long getLongProperty(String key) {
		return Long.parseLong(props.getProperty(key));
	}

	/**
	 * Puts an object into properties.
	 * 
	 * @param key	property name
	 * @param value	property value
	 */
	public void put(Object key, Object value) {
		props.put(key, String.valueOf(value));
	}
	
	/**
	 * Stores propeties to XML propety file. 
	 */
	public void save(){
		try {
			props.storeToXML(new FileOutputStream(file), null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

}
 