import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class LoadBalancer extends ServerSocket {

	
	LoadBalancingStrategy strategy;
	
	
	public LoadBalancer(LoadBalancingStrategy strategy) throws IOException {
		
		super();
		this.strategy=strategy;
		
	}
	public static void main(String [] args) throws Exception {
		new LoadBalancer(1411,new WeightedRoundRobinStrategy() ).start();;
	}
public LoadBalancer(int port,LoadBalancingStrategy strategy) throws IOException {
		
		super(port);
		this.strategy=strategy;
		
		
	}

public void start () throws Exception  {
	System.out.println("Wait for Clients ...");

	while(true) {
		
		Socket client=this.accept();//connection established
		System.out.println("client connected : "+client.getPort()+" | "+client.hashCode());
		BufferedReader requestReaderFromClient=new BufferedReader(new InputStreamReader(client.getInputStream()));
		String line=""; 
		
		
//		
		
		String request="";
	OutputStream responseForwarderToClient=client.getOutputStream();
	PrintWriter pw2=new PrintWriter(responseForwarderToClient,true);
	
	
//the first line of request 
	line=requestReaderFromClient.readLine();
	String path=line.split(" ")[1];
	
int port =strategy.getHost(path);
System.out.println("port : "+port+" | path : "+path);
	Socket server=new Socket("localhost",port);
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
				
		System.out.println("REQUEST : ");
		System.out.println(request);
		
		
		//requestForwarderToServer.write(request.getBytes());
		pw.flush();
		requestForwarderToServer.flush();
		System.out.println("end of Forward !");
		
		
		System.out.println("response : ");
		int c=0;
		char data;
//supose that transfer-encoding is chucked instead of content-length:number
		//if is it chuncked the response finish always with a '0\r\n'
	while((line =  responseReaderFromServer.readLine())!=null) {
		

	
System.out.println(line);
	
		pw2.println(line);
		

	}

	pw2.flush();//flusher le buffer
	client.close();
	server.close();
		System.out.println("closed   connection ?? : "+server.isClosed());
		System.out.println("end of Program !");

	}
	
}
}
