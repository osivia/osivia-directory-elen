package org.osivia.services.directory.workspace.portlet.configuration;

import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * Workspace member management configuration.
 *
 * @author Cédric Krommenhoek
 */
@Configuration
@ComponentScan(basePackages = "org.osivia.services.directory.workspace.portlet")
public class WorkspaceMemberManagementConfiguration {

    /**
     * Constructor.
     */
    public WorkspaceMemberManagementConfiguration() {
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
    
    @Bean
    public PersonService getPersonService() {
    	return DirServiceFactory.getService(PersonService.class);
    }

    @Bean
    public WorkspaceService getWorkspaceService() {
    	return DirServiceFactory.getService(WorkspaceService.class);
    }
}
