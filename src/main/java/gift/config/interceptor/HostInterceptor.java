package gift.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

@Component
public class HostInterceptor implements HandlerInterceptor {

    private static final String[] WHITE_LIST = {
            "localhost:8080"
    };

    private final Environment env;

    public HostInterceptor(Environment env) {
        this.env = env;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
            return true;
        }

        String hostHeader = request.getHeader("Host");

        if (hostHeader == null) {
            throw new SecurityException("Host header required!");
        }
        for (String white : WHITE_LIST) {
            if (!hostHeader.equals(white)) {
                throw new SecurityException("Invalid Host!");
            }
        }

        return true;
    }
}
