Dynamic Load Balancer (proxy) for microservices  with Java,Socket,MultiThreading (thread pool approach) ,Strategy Design Pattern and finally reflection api for dynamic dependency injection

This load balancer acts as a proxy. It receives HTTP requests from clients and determines the context path of the requested microservice  from the request. Based on this context path, it looks up the list of servers that host the microservice copy. Then using a specified strategy, the load balancer selects an appropriate server, establishes a connection, and forwards the client request to it. Once it receives the HTTP response from the server, it then forwards the response back to the client.

Note: In my case, I use two microservices (customer-microservice and product-microservice)each microservice has an 2 copy and each copy deployed in diferent server(embeded tomcat of spring-boot) wich make the total of server is 4

For Confguration This load balancer supports different load balancing strategies, such as round-robin and weighted round-robin, and can be easily configured through external property files. This setup allows for flexible and scalable management of microservices, where configurations such as the listening port, thread pool size, and load balancing strategies can be adjusted without modifying the code. The project uses three main configuration files: global-configuration.properties for basic settings, config1.properties for round-robin settings, and config2.properties for weighted round-robin settings, enabling dynamic and tailored load distribution across microservices.

To use weighted load balancing, modify the config2.properties file with the format microservice-name=ip:port:weight,ip2:port:weight,.... 
For example, if the context of your microservice is /payment-microservice and you have 2 instance for the microservices one deployed in server with (192.168.1.100:8080) and the weight is 4
and the second copy deployed in (192.168.1.200:9090) and the weight is 2 then you should modify config2.properties(file for weighted round robin config) and add : 

payment-microservice=192.168.1.100:8080:4,192.168.1.200:9090:4 . For round-robin load balancing, use the format microservice-name=ip:port,ip2:port,..., such as payment-microservice=192.168.1.100:8080,192.168.1.200:9090.


 Note : This load balancer works with any HTTP microservice, no matter what programming language is used (e.g., Go, Java, Node.js, etc.).



![image](https://github.com/user-attachments/assets/7da86792-8837-477b-9475-333fc927a7b1)
