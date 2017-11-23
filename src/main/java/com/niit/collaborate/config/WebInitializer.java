package com.niit.collaborate.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer
{
  @Override
  protected Class<?>[] getRootConfigClasses()
  {
	  return new Class[]{WebResolver.class};
  }
  @Override
  protected Class<?>[] getServletConfigClasses()
  {
	return null;  
  }
  @Override
  protected String[] getServletMappings()
  {
	  return new String[] {"/"};
  }
}
