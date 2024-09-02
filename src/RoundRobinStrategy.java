import java.io.*;
import java.sql.DriverManager;
import java.util.*;

public class RoundRobinStrategy implements LoadBalancingStrategy {


Map<String,Integer> currentIp=new HashMap();
Map<String,List<Integer>> servers=new HashMap();

//{
//	
//servers.put("/microservice-product",List.of(5001,5002));
//
//servers.put("/microservice-customer",List.of(5003,5004));
//
//currentIp.put("/microservice-product", 0);
//currentIp.put("/microservice-customer", 0);
//
//
//}

{
this.loadProperties();	
}

public void loadProperties() {
    Properties properties = new Properties();
    
    

    try (InputStream input =this.getClass().getClassLoader().getResourceAsStream("servers.properties")) {
        if (input == null) {
            System.out.println("Sorry, unable to find servers.properties");
            return;
        }

        // Load the properties file
        properties.load(input);

        for (String key : properties.stringPropertyNames()) {
            // Get the ports and split by comma
            String[] ports = properties.getProperty(key).split(",");
            List<Integer> portList = new ArrayList<>();

            for (String port : ports) {
                portList.add(Integer.parseInt(port));
            }

            // Populate the servers map
            servers.put("/" + key, portList);

            // Initialize currentIp map with zero for each service
            currentIp.put("/" + key, 0);
        }
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}
	@Override
	public int getHost(String path) {
	
	System.out.println("path : "+path);
	
	
String service=null;
for(String key:servers.keySet()) {
	
	if(path.contains(key)) {
		
		service=key;
		break;
		
	}
	
}
System.out.println(service);
int port=servers
.get(service)//list of port
.get(currentIp
		.get(service));


this.currentIp
.put(service,
		(currentIp.get(service)+1)%servers.get(service).size());

		
		return port;
	}

}
