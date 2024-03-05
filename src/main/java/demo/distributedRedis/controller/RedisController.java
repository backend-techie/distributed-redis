package demo.distributedRedis.controller;

import demo.distributedRedis.exception.KeyNotFoundException;
import demo.distributedRedis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/redis")
public class RedisController {


    @Autowired
    RedisService redisService;

    @PostMapping("/put")
    @ResponseBody
    public String put(@RequestParam String key, @RequestParam String value){
        System.out.println("Received put request for key : " + key + " and value : "+ value);
        redisService.addData(key, value);
        return key+"_"+value + " saved !!";
    }

    @PostMapping("/get")
    @ResponseBody
    public String get(@RequestParam String key) throws KeyNotFoundException {
        System.out.println("Received get request for key : " + key);
        return redisService.getData(key);
    }

}
