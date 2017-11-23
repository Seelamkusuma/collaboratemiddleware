package com.niit.collaborate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan("com.niit.collaborate")
public class WebResolver {

	  @Bean
	  public InternalResourceViewResolver getViewResolver()
	  {
		  InternalResourceViewResolver internalResourceViewResolver=new InternalResourceViewResolver();
		  internalResourceViewResolver.setPrefix("/WEB-INF");
		  internalResourceViewResolver.setSuffix(".jsp");
		  System.out.println("Resolver Created");
		  return internalResourceViewResolver;
		   }
	  @Bean(name = "multipartResolver")
	  public CommonsMultipartResolver getCommonsMultipartResolver() {
	  	CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
	  	multipartResolver.setMaxUploadSize(20971520); // 20MB
	  	multipartResolver.setMaxInMemorySize(1048576);	// 1MB
	  	return multipartResolver;
	  }
}
