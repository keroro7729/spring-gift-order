package gift.config;

import gift.config.interceptor.HostInterceptor;
import gift.config.interceptor.JwtInterceptor;
import gift.config.resolver.CurrentMemberArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;
    private final HostInterceptor hostInterceptor;
    private final CurrentMemberArgumentResolver currentMemberArgumentResolver;

    public WebConfig(JwtInterceptor jwtInterceptor,
                     HostInterceptor hostInterceptor,
                     CurrentMemberArgumentResolver currentMemberArgumentResolver) {
        this.jwtInterceptor = jwtInterceptor;
        this.hostInterceptor = hostInterceptor;
        this.currentMemberArgumentResolver = currentMemberArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/wishes/**");

        registry.addInterceptor(hostInterceptor)
                .addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentMemberArgumentResolver);
    }
}

