package com.gcmassari.tempmonitor.springconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

// enable job scheduling
@EnableScheduling
// same as <mvc:annotation-driven/>
@EnableWebMvc
@Configuration
@ComponentScan(basePackages = {
    "com.gcmassari.tempmonitor.controller",
    "com.gcmassari.tempmonitor.validator"}
)
public class WebConfig extends WebMvcConfigurerAdapter {

  // Allows direct access to static resources: equivalent for <mvc:resources/> tags
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
  }


  @Bean
  public ReloadableResourceBundleMessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();
    messageSource.setBasenames(new String[] {"/resources/messages/messages"});
    return messageSource;
  }

  // Bean name must be "multipartResolver", by default Spring uses method name as bean name.
  @Bean
  // public MultipartResolver multipartResolver() {
  // TODO GIM try to remove those config and re.enable the web.xml part: <multipart-config>
  public CommonsMultipartResolver multipartResolver() {
    CommonsMultipartResolver resolver = new CommonsMultipartResolver();
    resolver.setMaxUploadSizePerFile(102400); // 100 KB-< NB if file is bigger you got an Error 500
    // !!
    // resolver.setDefaultEncoding("UTF-8");
    resolver.setResolveLazily(true);
    return resolver;
    // return new StandardServletMultipartResolver();
  }

  @Bean
  public InternalResourceViewResolver viewResolver() {
    InternalResourceViewResolver resolver = new InternalResourceViewResolver();
    resolver.setPrefix("/WEB-INF/view/");
    resolver.setSuffix(".jsp");
    return resolver;
  }


  // TODO GIM use (multi-language?) properties
  // @Bean
  // public ReloadableResourceBundleMessageSource messageSource() {
  // ReloadableResourceBundleMessageSource messageSource = new
  // ReloadableResourceBundleMessageSource();
  // messageSource.setBasenames(new String[] {"/resources/messages/messages"});
  // return messageSource;
  // }

}
