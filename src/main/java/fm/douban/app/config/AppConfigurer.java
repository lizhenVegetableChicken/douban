//package fm.douban.app.config;
//
//import fm.douban.app.interceptor.UserInterceptor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class AppConfigurer implements WebMvcConfigurer {
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 多个拦截器组成一个拦截器链
//         registry.addInterceptor(new UserInterceptor())
//        .addPathPatterns("/index");
//    }
//
//}
