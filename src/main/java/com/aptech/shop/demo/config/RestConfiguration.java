package com.aptech.shop.demo.config;

import com.aptech.shop.demo.entity.Product;
import com.aptech.shop.demo.entity.ProductImage;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class RestConfiguration implements RepositoryRestConfigurer {
    private String theAllowedOrigins = "http://localhost:3000";

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration configuration, CorsRegistry corsRegistry){
        HttpMethod[] httpMethodsUnsupported = {
                HttpMethod.POST,
                HttpMethod.PATCH,
                HttpMethod.DELETE,
                HttpMethod.PUT};
        corsRegistry.addMapping(configuration.getBasePath()+"/**")
                .allowedOrigins(theAllowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);

        configuration.exposeIdsFor(Product.class);
        configuration.exposeIdsFor(ProductImage.class);
    }
}
