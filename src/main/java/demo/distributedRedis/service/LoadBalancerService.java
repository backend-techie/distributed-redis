package demo.distributedRedis.service;

import demo.distributedRedis.constant.Method;
import demo.distributedRedis.model.LbRequest;
import demo.distributedRedis.model.LbResponse;
import demo.distributedRedis.utils.HashFunction;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static java.lang.Thread.sleep;

@Service
public class LoadBalancerService {

    @Value("${server-1.config.address}")
    private String server1Address;

    @Value("${server-2.config.address}")
    private String server2Address;

    @Value("${server-3.config.address}")
    private String server3Address;

    @Value("${server-4.config.address}")
    private String server4Address;

    @Value("${redis.api.ttl}")
    private int redisApiTimeout;

    SortedMap<Integer, String> serverBuckets;
    Map <String, String > serverIdToAddressMap;

    @Autowired
    OkHttpClient okHttpClient;

    @Autowired
    public LoadBalancerService(SortedMap<Integer, String> serverBuckets){
        this.serverBuckets = serverBuckets;
        serverIdToAddressMap = new HashMap<>();

    }



    public List<LbResponse> directRequestToServer(List<LbRequest> lbRequests) throws Exception{
        serverIdToAddressMap.put("Server_1", server1Address);
        serverIdToAddressMap.put("Server_2", server2Address);
        serverIdToAddressMap.put("Server_3", server3Address);
        serverIdToAddressMap.put("Server_4", server4Address);
        ExecutorService executorService = ExecutorServiceManager.getExecutorService();
        List<Future<String>> futures = new ArrayList<>();
        for (LbRequest request : lbRequests){
            Callable<String> task = () -> redisTask(request);
            Future<String> future = executorService.submit(task);
            futures.add(future);
        }
        ExecutorServiceManager.shutdownExecutorService();

        try {
            sleep(redisApiTimeout);
        } catch (Exception e){
            System.out.println("Interrupted !!");
        }

        List <LbResponse> response = new ArrayList<>();

        for (Future<String> future : futures) {
            String result = future.get();
            System.out.println("Task result: " + result);
            response.add(new LbResponse(result));
        }
        return response;
    }


    private String redisTask(LbRequest lbRequest){

        int hashValue = HashFunction.getHashValue(lbRequest.getKey());
        final SortedMap<Integer,String> mapViewWithKeyGreaterThanUserBucket = serverBuckets.tailMap(hashValue);
        final Integer bucketIdWhichWillHandleTheUser = mapViewWithKeyGreaterThanUserBucket.isEmpty() ? serverBuckets.firstKey() : mapViewWithKeyGreaterThanUserBucket.firstKey();
        final String serverWhichWillHandleTheUser = serverBuckets.get(bucketIdWhichWillHandleTheUser);
        System.out.println(serverWhichWillHandleTheUser);
        String url = serverIdToAddressMap.get(serverWhichWillHandleTheUser);

        if (Method.GET.equals(lbRequest.getMethod())){
            url += fetchGETRequest(lbRequest);
        } else {
            url += fetchPUTRequest(lbRequest);
        }

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(null, new byte[0]))
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            System.out.println("Response: " + response);
            return responseBody;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String fetchGETRequest(LbRequest lbRequest){
        return "redis/get?key=" + lbRequest.getKey();
    }

    private String fetchPUTRequest(LbRequest lbRequest){
        return "redis/put?key=" + lbRequest.getKey() + "&value=" + lbRequest.getValue();
    }


}
