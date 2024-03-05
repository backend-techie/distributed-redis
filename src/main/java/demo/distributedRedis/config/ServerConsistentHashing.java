package demo.distributedRedis.config;

import demo.distributedRedis.utils.HashFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.SortedMap;
import java.util.TreeMap;

@Configuration
public class ServerConsistentHashing {


    final static int LIMIT = 4;





    @Bean
    public SortedMap<Integer, String> serverBuckets(){

        final SortedMap<Integer, String> bucketIdToServer = new TreeMap<>();

        // Set up the servers
        for (int i = 1; i <= 4; i++) {
            final String server = "Server_" + i;
            // Can be situation of hash collision, which would override the previous server. Else again hash with some other function.
            bucketIdToServer.put(HashFunction.getHashValue(server), server);
        }

        return bucketIdToServer;

    }







}
