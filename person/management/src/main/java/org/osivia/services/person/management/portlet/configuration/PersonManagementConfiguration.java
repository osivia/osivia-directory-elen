/**
 * 
 */
package org.osivia.services.person.management.portlet.configuration;

import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * Person maangement configuration.
 * 
 * @author Loïc Billon
 * @author Cédric Krommenhoek
 */
@Configuration
@ComponentScan(basePackages = "org.osivia.services.person.management.portlet")
public class PersonManagementConfiguration {

    /**
     * Constructor.
     */
    public PersonManagementConfiguration() {
        super();
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

}
