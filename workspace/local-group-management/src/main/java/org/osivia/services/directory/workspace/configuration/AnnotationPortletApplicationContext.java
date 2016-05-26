package org.osivia.services.directory.workspace.configuration;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.web.portlet.context.AbstractRefreshablePortletApplicationContext;

/**
 * Annotation portlet application context.
 *
 * @author Cédric Krommenhoek
 * @see AbstractRefreshablePortletApplicationContext
 */
public class AnnotationPortletApplicationContext extends AbstractRefreshablePortletApplicationContext {

    /**
     * Constructor.
     */
    public AnnotationPortletApplicationContext() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
        this.loadConfigBeanDefinitions(beanFactory);
    }


    /**
     * Load config bean definitions.
     *
     * @param beanFactory bean factory
     */
    protected void loadConfigBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        for (String configurationClassName : this.getConfigLocations()) {
            BeanDefinition configBeanDefinition = new GenericBeanDefinition();
            configBeanDefinition.setBeanClassName(configurationClassName);
            this.loadConfigBeanDefinition(configBeanDefinition, beanFactory);
        }
    }


    /**
     * Load config bean definition.
     *
     * @param beanDefinition bean definition
     * @param beanFactory bean factory
     */
    protected void loadConfigBeanDefinition(BeanDefinition beanDefinition, DefaultListableBeanFactory beanFactory) {
        beanFactory.registerBeanDefinition(beanDefinition.getBeanClassName(), beanDefinition);
    }

}
