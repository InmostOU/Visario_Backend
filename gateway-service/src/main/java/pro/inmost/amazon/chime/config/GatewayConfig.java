package pro.inmost.amazon.chime.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix
public class GatewayConfig {

    @Autowired
    AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/users/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://user-service"))

                .route("auth-service", r -> r.path("/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://auth-service"))
                .route("contact-service", r -> r.path("/contact/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://contact-service"))
                .route("contact-service", r -> r.path("/user/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://contact-service"))
                .route("messaging-service", r -> r.path("/channels/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://messaging-service"))
                .route("messaging-service", r -> r.path("/messages/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://messaging-service"))
                .route("meeting-service", r -> r.path("/meeting/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://meeting-service"))
                .route("messaging-service", r -> r.path("/websocket/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://messaging-service"))
                .build();
    }

}
