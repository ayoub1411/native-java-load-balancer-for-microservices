import java.io.*;
import java.util.*;

class ServerInfo{
	
	String host;
	int weight;
	int requestsBeforWeight;
	
	public ServerInfo(String host, int weight) {
		super();
		this.host = host;
		this.weight = weight;
		this.requestsBeforWeight=1;
	}
	
	
}


public class WeightedRoundRobinStrategy implements LoadBalancingStrategy {
	
	{
		
		System.out.println("weitghed round robin created !");
		
	}
	
  Map<String,List<ServerInfo>> servers=new HashMap();
  Map<String,Integer> currentServers=new HashMap();
  {
	  this.loadServers();
	  
	  for(String k:this.servers.keySet()) {
		  System.out.println("service : "+k);
	  }
//	  servers.put("/microservice-customer",
//			  List.of(new ServerInfo("localhost:5003",3)
//					  ,new ServerInfo("localhost:5004",2)));
//	  this.currentServers.put("/microservice-customer", 0);
	  
	  
	//servers.put("/microservice-product",List.of(5001,5002));
	  //
	  //servers.put("/microservice-customer",List.of(5003,5004));
	  //
	  //currentIp.put("/microservice-product", 0);
	  //currentIp.put("/microservice-customer", 0);
	  //
	
  }

   
    @Override
    public String getHost(String path) {
     String host=null;
     
      String service=null;
     
     for(String key:servers.keySet()) {
    	 System.out.println(key+" | "+path);
    	 if(path.contains(key)) {
    		 service=key;
    		 
    		 break;
    		 
    	 }
     }
   //  System.out.println("service found : "+service);
     
   System.out.println("error : "+service);
     
     int currentServer=this.currentServers.get(service);
    
    ServerInfo serverInfo=servers.get(service).get(currentServer);
    
    
    serverInfo.requestsBeforWeight++;
    
    if(serverInfo.requestsBeforWeight>serverInfo.weight) {
    	
    	this.currentServers.put(service,
    	(this.currentServers.get(service)+1)%this.servers.get(service).size());
    	
    	serverInfo.requestsBeforWeight=1;
    	
    }

        return serverInfo.host ;
    }
    
private void loadServers() {
		
		InputStream is=this.getClass().getClassLoader().getResourceAsStream("config2.properties");
		System.out.println("filee : "+this.getClass().getClassLoader().getResource("config2.properties"));

		Properties properties=new Properties();
		try {
			properties.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    for (String key : properties.stringPropertyNames()) {
	    	System.out.println(key);
	        String[] serverEntries = properties.getProperty(key).split(",");
	        List<ServerInfo> serverList = new ArrayList<>();
	        for (String entry : serverEntries) {
	            String[] parts = entry.split(":");
	            String host = parts[0] + ":" + parts[1];
	            int weight = Integer.parseInt(parts[2]);
	            serverList.add(new ServerInfo(host, weight));
	        }
	        this.servers.put("/"+key, serverList);
	        this.currentServers.put("/"+key, 0);
	    }
	}
}
