package org.osivia.services.person.creation.plugin.configuration;

import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Person creation plugin configuration.
 * 
 * @author Cédric Krommenhoek
 */
@Configuration
@ComponentScan(basePackages = "org.osivia.services.person.creation")
public class PersonCreationPluginConfiguration {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public PersonCreationPluginConfiguration() {
        super();
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
    public PersonUpdateService getPersonService() {
        return DirServiceFactory.getService(PersonUpdateService.class);
    }


    /**
     * Get internationalization bundle factory.
     * 
     * @return internationalization bundle factory
     */
    @Bean
    public IBundleFactory getBundleFactory() {
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        return internationalizationService.getBundleFactory(this.getClass().getClassLoader(), this.applicationContext);
    }

}
