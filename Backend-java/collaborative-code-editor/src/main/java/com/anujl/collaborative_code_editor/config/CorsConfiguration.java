package com.anujl.collaborative_code_editor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // This mapping applies to the specific login path
        registry.
                addMapping("/**") // Adjust the path as needed
                // Allows requests from the origin of your Angular application
                .allowedOrigins("http://localhost:4200")
                // Allows the POST method, which is used for login
                .allowedMethods("*")
                // Allows all headers in the request
                .allowedHeaders("*")
                // Allows credentials (like cookies or authentication headers)
                .allowCredentials(true);

        // For development, you might configure a more general rule.
        // Uncomment the lines below to enable a broader CORS policy.
        /*
        registry.addMapping("/**") // Applies to all paths
                .allowedOrigins("*") // Be cautious with "*" in production
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
        */
    }
}
