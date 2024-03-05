package demo.distributedRedis.repository;

import demo.distributedRedis.exception.KeyNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

public class RedisRepository {

    private static volatile RedisRepository instance;

    private HashMap <String, String> redisDB;


    private RedisRepository() {
        redisDB = new HashMap<>();
    };

    public static RedisRepository getInstance() {
        if (instance == null) {
            synchronized (RedisRepository.class) {
                if (instance == null) {
                    instance = new RedisRepository();
                }
            }
        }
        return instance;
    }

    public void addData(String key, String value) {
        redisDB.put(key, value);
    }

    public String getData(String key) throws KeyNotFoundException {
        try {
            return redisDB.get(key);
        } catch (Exception e){
            throw new KeyNotFoundException();
        }
    }





}
