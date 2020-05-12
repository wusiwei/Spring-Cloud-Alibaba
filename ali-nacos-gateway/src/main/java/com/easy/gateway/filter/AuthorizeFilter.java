package com.easy.gateway.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
/**
 * 全局过滤器
 * @author wusiwei
 *
 */
public class AuthorizeFilter implements GlobalFilter, Ordered{
	
	private static final String AUTHORIZE_TOKEN = "Authorization";
	private static final String AUTHORIZE_UID = "uid";
	
	

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();

        ServerHttpRequest.Builder mutate = request.mutate();

        String token = headers.getFirst( AUTHORIZE_TOKEN );
        String uid = headers.getFirst( AUTHORIZE_UID );
        String method = request.getMethodValue();

        log.info( "AuthorizeFilter token 全局过滤器 token:{},uid:{}",token,uid );
        
        if (token == null) {
            token = request.getQueryParams().getFirst( AUTHORIZE_TOKEN );
        }
        if(StringUtils.isNotBlank(token)){
             
        }

        return chain.filter( exchange );
	}
	
	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

}
