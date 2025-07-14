package gift.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class HostInterceptor implements HandlerInterceptor {

    private static final String[] WHITE_LIST = {
            "localhost:8080"
    };

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
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
