import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class AutoConfigurationService {

	
	public static LoadBalancingStrategy loadStrategyFromConfig() throws Exception {
	    Properties properties = new Properties();
	    InputStream input = LoadBalancer.class.getClassLoader().getResourceAsStream("global-configuration.properties");
	    if (input == null) {
	        throw new FileNotFoundException("CoNFIG not Found :( ");
	    }

	    properties.load(input);
	    String strategyClassName = properties.getProperty("strategy");

	    // Dynamically instantiate the strategy class using reflection
	    Class<?> strategyClass = Class.forName(strategyClassName);
	    return (LoadBalancingStrategy) strategyClass.getDeclaredConstructor().newInstance();
	}

	public  static Integer loadPortFromGlobalConfig() throws Exception {
	    Properties properties = new Properties();
	    InputStream input = LoadBalancer.class.getClassLoader().getResourceAsStream("global-configuration.properties");
	    if (input == null) {
	        throw new FileNotFoundException("CoNFIG not Found :( ");
	    }

	    properties.load(input);
	    String port = properties.getProperty("port");

	  
	    return Integer.parseInt(port);
	}
	public  static Integer loadMaxThreadsFromGlobalConfig() throws Exception {
	    Properties properties = new Properties();
	    InputStream input = LoadBalancer.class.getClassLoader().getResourceAsStream("global-configuration.properties");
	    if (input == null) {
	        throw new FileNotFoundException("CoNFIG not Found :( ");
	    }

	    properties.load(input);
	    String port = properties.getProperty("max-threads");

	  
	    return Integer.parseInt(port);
	}

	
}
