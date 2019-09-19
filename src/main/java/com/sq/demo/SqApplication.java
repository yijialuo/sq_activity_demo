package com.sq.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

import javax.servlet.MultipartConfigElement;

@MapperScan(basePackages = "com.sq.demo.mapper")
@SpringBootApplication
public class SqApplication {

    public static void main(String[] args) {
        SpringApplication.run(SqApplication.class, args);
    }

    //下面两个函数设置允许跨域
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addExposedHeader("info");
        corsConfiguration.addExposedHeader("allCount");
        corsConfiguration.addExposedHeader("pageCount");
        corsConfiguration.addExposedHeader("authorization");
        return corsConfiguration;
    }

    /**
     * 跨域过滤器
     *
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig()); // 4
        return new CorsFilter(source);
    }


    /**
     * 设置上传文件大小，配置文件属性设置无效
     *
     * @author 空空dream
     * @date 2018年3月8日
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory config = new MultipartConfigFactory();
        config.setMaxFileSize("9000MB");
        config.setMaxRequestSize("9000MB");
        return config.createMultipartConfig();
    }


}

