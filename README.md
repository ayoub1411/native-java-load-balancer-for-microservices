Load Balancer (proxy) for microservices  with Java,Socket,MultiThreading (thread pool approach) and Strategy Design Pattern

The goal of this project is to implement a dynamic load balancer that distributes client requests across multiple microservices to ensure even load distribution and prevent any single service from being overwhelmed. The load balancer supports different load balancing strategies, such as round-robin and weighted round-robin, and can be easily configured through external property files. This setup allows for flexible and scalable management of microservices, where configurations such as the listening port, thread pool size, and load balancing strategies can be adjusted without modifying the code. The project uses three main configuration files: global-configuration.properties for basic settings, config1.properties for round-robin settings, and config2.properties for weighted round-robin settings, enabling dynamic and tailored load distribution across microservices.




![image](https://github.com/user-attachments/assets/7da86792-8837-477b-9475-333fc927a7b1)
