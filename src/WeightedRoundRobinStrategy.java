import java.io.*;
import java.util.*;

public class WeightedRoundRobinStrategy implements LoadBalancingStrategy {

    // Data structure to hold server ports and their weights
    private final Map<String, List<Server>> servers = new HashMap<>();
    private final Map<String, Integer> currentIndex = new HashMap<>();
    private final Map<String, Integer> totalWeight = new HashMap<>();

    // Inner class to hold server information
    private static class Server {
        int port;
        int weight;

        Server(int port, int weight) {
            this.port = port;
            this.weight = weight;
        }
    }

    public WeightedRoundRobinStrategy() {
        this.loadProperties();
    }

    public void loadProperties() {
        Properties properties = new Properties();

        try (InputStream input = this.getClass().getClassLoader().getResourceAsStream("servers.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find servers.properties");
                return;
            }

            // Load the properties file
            properties.load(input);

            for (String key : properties.stringPropertyNames()) {
                String[] entries = properties.getProperty(key).split(";");
                List<Server> serverList = new ArrayList<>();
                int totalWeightForService = 0;

                for (String entry : entries) {
                    String[] parts = entry.split(",");
                    int port = Integer.parseInt(parts[0]);
                    int weight = Integer.parseInt(parts[1]);
                    serverList.add(new Server(port, weight));
                    totalWeightForService += weight;
                }

                // Populate the servers map
                servers.put("/" + key, serverList);
                totalWeight.put("/" + key, totalWeightForService);

                // Initialize currentIndex map with zero for each service
                currentIndex.put("/" + key, 0);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getHost(String path) {
        String service = null;
        for (String key : servers.keySet()) {
            if (path.contains(key)) {
                service = key;
                break;
            }
        }

        if (service == null) {
            throw new IllegalArgumentException("No matching service found for path: " + path);
        }

        List<Server> serverList = servers.get(service);
        int totalWeightForService = totalWeight.get(service);
        int index = currentIndex.get(service);

        // Find the server based on weighted round-robin logic
        int currentWeight = 0;
        int currentTotalWeight = 0;
        Server selectedServer = null;

        for (int i = 0; i < serverList.size(); i++) {
            currentWeight += serverList.get(index).weight;
            currentTotalWeight += serverList.get(index).weight;
            if (currentWeight >= (currentTotalWeight % totalWeightForService)) {
                selectedServer = serverList.get(index);
                break;
            }
            index = (index + 1) % serverList.size();
        }

        // Update currentIndex for the next call
        currentIndex.put(service, (index + 1) % serverList.size());

        if (selectedServer == null) {
            throw new IllegalStateException("No server selected for the path: " + path);
        }

        return selectedServer.port;
    }
}
