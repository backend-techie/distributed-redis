package demo.distributedRedis.controller;

import demo.distributedRedis.model.LbRequest;
import demo.distributedRedis.model.LbResponse;
import demo.distributedRedis.service.LoadBalancerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/lb")
public class LoadBalancerController {


    @Autowired
    LoadBalancerService loadBalancerService;

    @PostMapping("/redis-action")
    @ResponseBody
    public List<LbResponse> put(@RequestBody List<LbRequest> lbRequests) throws Exception{
        System.out.println("Received lb request : " + lbRequests);
        return loadBalancerService.directRequestToServer(lbRequests);
    }


}
