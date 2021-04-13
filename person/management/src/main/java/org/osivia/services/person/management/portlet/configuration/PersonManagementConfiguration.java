/**
 * 
 */
package org.osivia.services.person.management.portlet.configuration;

import javax.portlet.PortletContext;

import org.osivia.portal.api.Constants;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.dynamic.IDynamicService;
import org.osivia.portal.api.locator.Locator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.portlet.context.PortletContextAware;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * Person management configuration.
 * 
 * @author Loïc Billon
 * @author Cédric Krommenhoek
 * @see PortletContextAware
 */
@Configuration
@ComponentScan(basePackages = "org.osivia.services.person.management.portlet")
public class PersonManagementConfiguration implements PortletContextAware {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public PersonManagementConfiguration() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        portletContext.setAttribute(Constants.PORTLET_ATTR_WEBAPP_CONTEXT,applicationContext);
    }


    /**
     * Get view resolver.
     *
     * @return view resolver
     */
    @Bean
    public InternalResourceViewResolver getViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setCache(true);
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }


    /**
     * Get message source.
     *
     * @return message source
     */
    @Bean(name = "messageSource")
    public ResourceBundleMessageSource getMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("Resource");
        return messageSource;
    }


    /**
     * Get person service.
     *
     * @return person service
     */
    @Bean
    public PersonService getPersonService() {
    	return DirServiceFactory.getService(PersonService.class);
    }
    
    @Bean
    public IDynamicService getDynamicWindowService() {
        return Locator.getService(IDynamicService.class);
    }

}
