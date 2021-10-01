package pro.inmost.amazon.chime.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
public class RouterValidator {

    public static List<String> openApiEndpoints = new ArrayList<>();

    static {
        openApiEndpoints.add("/auth/register");
        openApiEndpoints.add("/auth/login");
        openApiEndpoints.add("/auth/verify-user");
    }

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
