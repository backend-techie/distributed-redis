package demo.distributedRedis.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LbResponse {

    String redisResponse;

}
