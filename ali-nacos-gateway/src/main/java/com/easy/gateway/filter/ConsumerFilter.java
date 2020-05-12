package com.easy.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
/**
 * 单路由，或组过滤器
 * @author wusiwei
 *
 */
public class ConsumerFilter extends AbstractGatewayFilterFactory<ConsumerFilter.Config>{

	public ConsumerFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		
		
		return (exchange, chain) -> {
			String jwtToken = exchange.getRequest().getHeaders().getFirst("Authorization");
			//校验jwtToken的合法性
			log.info( "进入自定义过滤器");
			if (jwtToken != null) {
				// 合法
				// 将用户id作为参数传递下去
				return chain.filter(exchange);
			}

			//不合法(响应未登录的异常)
			ServerHttpResponse response = exchange.getResponse();
			//设置headers
			HttpHeaders httpHeaders = response.getHeaders();
			httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
			httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
			//设置body
			String warningStr = "未登录或登录超时";
			DataBuffer bodyDataBuffer = response.bufferFactory().wrap(warningStr.getBytes());

			return response.writeWith(Mono.just(bodyDataBuffer));
		};
	}
	
	public static class Config {

	}

	@Bean
	public ConsumerFilter creditFileterFactory() {
		return new ConsumerFilter();
	}
}
