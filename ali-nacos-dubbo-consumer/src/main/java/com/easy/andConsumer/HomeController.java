package com.easy.andConsumer;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.easy.and.api.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HomeController {

    @Reference
    HelloService helloService;

    @GetMapping("/hello")
    @SentinelResource(value = "protected-resource", blockHandler = "handleBlock")
    public String hello() {
        return helloService.hello("云天");
    }
    
    public String handleBlock(Integer id, BlockException e) {
        return "限流";
    }
}