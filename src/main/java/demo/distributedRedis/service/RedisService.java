package demo.distributedRedis.service;

import demo.distributedRedis.exception.KeyNotFoundException;
import demo.distributedRedis.repository.RedisRepository;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    public void addData(String key, String value){
        RedisRepository redisRepository = RedisRepository.getInstance();
        redisRepository.addData(key, value);
    }

    public String getData(String key) throws KeyNotFoundException {
        RedisRepository redisRepository = RedisRepository.getInstance();
        return redisRepository.getData(key);
    }

}
