package com.lhiot.uc.basic;

import com.leon.microx.util.SnowflakeId;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@Data
@ConfigurationProperties(prefix = "uc-modules.basic-user-service.snowflake-id")
public class BasicUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicUserServiceApplication.class, args);
    }

    private  long workerId;
    private long dataCenterId;

    @Bean
    public SnowflakeId snowflakeId(){
        return new SnowflakeId(workerId,dataCenterId);
    }
}
