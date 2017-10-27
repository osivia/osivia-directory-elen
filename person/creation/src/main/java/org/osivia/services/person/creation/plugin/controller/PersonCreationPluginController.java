package org.osivia.services.person.creation.plugin.controller;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.services.person.creation.plugin.form.PersonCreationFormFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;

/**
 * Person creation plugin controller.
 * 
 * @author Cédric Krommenhoek
 * @see AbstractPluginPortlet
 */
@Controller
public class PersonCreationPluginController extends AbstractPluginPortlet {

    /** Plugin name. */
    private static final String PLUGIN_NAME = "person.creation.plugin";


    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet config. */
    @Autowired
    private PortletConfig portletConfig;


    /**
     * Constructor.
     */
    public PersonCreationPluginController() {
        super();
    }


    /**
     * Post-construct.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
        super.init();
    }


    /**
     * Pre-destroy.
     */
    @PreDestroy
    public void preDestroy() {
        super.destroy();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPluginName() {
        return PLUGIN_NAME;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void customizeCMSProperties(CustomizationContext context) {
        // Form filters
        this.customizeFormFilters(context);
    }


    /**
     * Customize form filters.
     * 
     * @param context customization context
     */
    private void customizeFormFilters(CustomizationContext context) {
        // Form filters
        Map<String, FormFilter> filters = this.getFormFilters(context);

        // Person creation
        FormFilter personCreation = this.applicationContext.getBean(PersonCreationFormFilter.class);
        filters.put(personCreation.getId(), personCreation);
    }

}
