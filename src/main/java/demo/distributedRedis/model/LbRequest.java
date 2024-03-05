package demo.distributedRedis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import demo.distributedRedis.constant.Method;
import lombok.Data;

import java.security.Key;

@Data
public class LbRequest {

    Method method;

    String key;

    String value;

}
