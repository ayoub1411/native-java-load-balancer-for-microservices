import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadBalancer extends ServerSocket {
	
	ExecutorService executorService =null;
	
	
	LoadBalancingStrategy strategy;
	

	public LoadBalancer() throws Exception {
		
		
	super(AutoConfigurationService.loadPortFromGlobalConfig());
    this.strategy=AutoConfigurationService.loadStrategyFromConfig();
    int maxThreads=AutoConfigurationService.loadMaxThreadsFromGlobalConfig();
    this.executorService= Executors.newFixedThreadPool(maxThreads);
		
		
	}



public void start () throws Exception  {
	System.out.println("Wait for Clients ...");

	while(true) {
		System.out.println("wait for new client");
		Socket client=this.accept();//connection established
		System.out.println("client connected : "+client.getPort()+" | "+client.hashCode());
	//get a free thread from the thread pool
		executorService.execute(new ProxyHandling(client));
		
		
		
	}
	
}
class ProxyHandling implements Runnable{
	Socket client;
	public ProxyHandling(Socket c) {
		client=c;
	}

	@Override
	public void run() {
		BufferedReader requestReaderFromClient;
		try {
			requestReaderFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
		
		String line=""; 
		
		
//		
		
		String request="";
	OutputStream responseForwarderToClient=client.getOutputStream();
	PrintWriter pw2=new PrintWriter(responseForwarderToClient,true);
	
	
//the first line of request 
	line=requestReaderFromClient.readLine();
	String path=line.split(" ")[1];
	
String host =strategy.getHost(path); //exemple :192.168.1.100:80
System.out.println("***********host : "+host+"**************");
int port=Integer.parseInt(host.split(":")[1]);
String ip=host.split(":")[0];

//System.out.println("port : "+port+" | path : "+path);
	Socket server=new Socket(ip,port);
	OutputStream requestForwarderToServer=server.getOutputStream();
	PrintWriter pw=new PrintWriter(requestForwarderToServer,true);
	BufferedReader responseReaderFromServer=new BufferedReader(new InputStreamReader(server.getInputStream()));
	request+=line+"\r\n";
	  pw.print(line+"\r\n");
	  
	  
	while (!(line =  requestReaderFromClient.readLine()).equals("")) {
			
  request+=line+"\r\n";
  pw.print(line+"\r\n");

         }
		
		request+="\r\n";
		
	pw.print("\r\n");
//				
//		System.out.println("REQUEST : ");
//		System.out.println(request);
		
		
		//requestForwarderToServer.write(request.getBytes());
		pw.flush();
		requestForwarderToServer.flush();
		//System.out.println("end of Forward !");
		
		
		//System.out.println("response : ");
		int c=0;
		char data;
//supose that transfer-encoding is chucked instead of content-length:number
		//if is it chuncked the response finish always with a '0\r\n'
	while((line =  responseReaderFromServer.readLine())!=null) {
		

	
//System.out.println(line);
	
		pw2.println(line);
		

	}

	pw2.flush();//flusher le buffer
	client.close();
	server.close();
//		System.out.println("closed   connection ?? : "+server.isClosed());
//		System.out.println("end of Program !");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}




}

