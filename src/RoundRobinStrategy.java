import java.io.*;
import java.sql.DriverManager;
import java.util.*;

public class RoundRobinStrategy implements LoadBalancingStrategy {
	{
		System.out.println("round robin created !");
	}

Map<String,Integer> currentIp=new HashMap();
Map<String,List<String>> servers=new HashMap();


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
//}s

{
this.loadProperties();	
}

public void loadProperties() {
    Properties properties = new Properties();
    
    

    try (InputStream input =this.getClass().getClassLoader().getResourceAsStream("config1.properties")) {
        if (input == null) {
            System.out.println("Sorry, unable to find config1.properties :(");
            return;
        }

     
        properties.load(input);

        for (String key : properties.stringPropertyNames()) {
           
            String[] hosts = properties.getProperty(key).split(",");
            List<String> serveurs = new ArrayList<>();

            for (String host : hosts) {
               serveurs.add(host);
            }

            servers.put("/" + key, serveurs);

            
            currentIp.put("/" + key, 0);
        }
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}
	@Override
	public String getHost(String path) {
	
	System.out.println("path : "+path);
	
	
String service=null;
for(String key:servers.keySet()) {
	
	if(path.contains(key)) {
		
		service=key;
		break;
		
	}
	
}
System.out.println(service);

String host=servers.get(service)//list of hosts
				   .get(currentIp.get(service));


this.currentIp.put(service,
		
		(currentIp.get(service)+1)%servers.get(service).size());

	
		return host;
	}

}
