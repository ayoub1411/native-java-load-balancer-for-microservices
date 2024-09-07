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

  Map<String,List<ServerInfo>> servers=new HashMap();
  Map<String,Integer> currentServers=new HashMap();
  {
	  
	  servers.put("/microservice-customer",
			  List.of(new ServerInfo("localhost:5003",3)
					  ,new ServerInfo("localhost:5004",2)));
	  this.currentServers.put("/microservice-customer", 0);
	  
	  
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
}
