package org.osivia.services.group.management.portlet.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.portal.theme.ThemeConstants;
import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.osivia.directory.v2.model.PortalGroup;
import org.osivia.directory.v2.service.PortalGroupService;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.dynamic.IDynamicService;
import org.osivia.services.group.management.portlet.model.GroupManagementForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * Group management portlet service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see GroupManagementService
 */
@Service
public class GroupManagementServiceImpl implements GroupManagementService {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** View resolver. */
    @Autowired
    private InternalResourceViewResolver viewResolver;

    /** Group service. */
    @Autowired
    private PortalGroupService groupService;
    
    /** Dynamic page service. */
    @Autowired
    private IDynamicService dynamicService;


    /** Log. */
    private final Log log;


    /**
     * Constructor.
     */
    public GroupManagementServiceImpl() {
        super();
        this.log = LogFactory.getLog(this.getClass());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public GroupManagementForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        return this.applicationContext.getBean(GroupManagementForm.class);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void select(PortalControllerContext portalControllerContext, GroupManagementForm form) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Selected group identifier
        String selected = form.getSelected();
        // Region
        String region = System.getProperty(REGION_PROPERTY);

        if (region == null) {
            this.log.error("Unable to start person card portlet: property '" + REGION_PROPERTY + "' is not defined.");
        } else if (StringUtils.isNotEmpty(selected)) {
            // Window properties
            Map<String, String> properties = new HashMap<>();
            properties.put("osivia.hideTitle", "1");
            properties.put("osivia.bootstrapPanelStyle", String.valueOf(true));
            properties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, String.valueOf(true));
            properties.put("osivia.ajaxLink", "1");
            properties.put("osivia.group.cn", selected);


            try {
                dynamicService.startDynamicWindow(portalControllerContext, region, "directory-group-card-instance", properties);
            } catch (PortalException e) {
                throw new PortletException(e);
            }

        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void search(PortalControllerContext portalControllerContext, GroupManagementForm form) throws PortletException {
        // Displayed results indicator;
        boolean displayed;
        // Search results
        List<PortalGroup> results;

        if (StringUtils.length(form.getFilter()) >= FILTER_MINIMUM_LENGTH) {
            displayed = true;

            // Search criteria
            PortalGroup criteria = this.getSearchCriteria(portalControllerContext, form);

            results = this.groupService.search(criteria);
        } else {
            displayed = false;
            results = null;
        }

        // Update model
        form.setDisplayed(displayed);
        form.setGroups(results);
    }


    /**
     * Get search criteria.
     *
     * @param portalControllerContext portal controller context
     * @param filters search filters
     * @return search criteria
     */
    protected PortalGroup getSearchCriteria(PortalControllerContext portalControllerContext, GroupManagementForm form) {
        // Criteria
        PortalGroup criteria = this.groupService.getEmpty();

        // Filter
        String filter = form.getFilter();

        if (StringUtils.isNotBlank(filter)) {
            // Stripped filter
            String strippedFilter = StringUtils.strip(StringUtils.trim(filter), "*");
            // Tokenized filter
            String tokenizedFilter;
            if (StringUtils.isBlank(strippedFilter)) {
                tokenizedFilter = "*";
            } else {
                tokenizedFilter = "*" + strippedFilter + "*";
            }

            criteria.setCn(tokenizedFilter);
            criteria.setDisplayName(tokenizedFilter);
        }

        return criteria;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void search(PortalControllerContext portalControllerContext, GroupManagementForm form, String filters) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();

        // Filters map
        Map<String, String> map = this.getFiltersMap(portalControllerContext, filters);

        // Update form
        String filter;
        if (MapUtils.isEmpty(map)) {
            filter = null;
        } else {
            filter = map.get("filter");
        }
        form.setFilter(filter);

        // Search
        this.search(portalControllerContext, form);

        // Set form in request
        request.setAttribute("form", form);
    }


    /**
     * Get search filters map.
     * 
     * @param portalControllerContext portal controller context
     * @param filters search filters
     * @return search filters map
     * @throws PortletException
     */
    protected Map<String, String> getFiltersMap(PortalControllerContext portalControllerContext, String filters) throws PortletException {
        Map<String, String> map;
        String[] arguments = StringUtils.split(filters, "&");
        if (ArrayUtils.isEmpty(arguments)) {
            map = null;
        } else {
            map = new HashMap<>(arguments.length);
            for (String argument : arguments) {
                String[] split = StringUtils.splitPreserveAllTokens(argument, "=");
                if (split.length == 2) {
                    try {
                        String key = URLDecoder.decode(split[0], CharEncoding.UTF_8);
                        String value = URLDecoder.decode(split[1], CharEncoding.UTF_8);
                        map.put(key, value);
                    } catch (UnsupportedEncodingException e) {
                        throw new PortletException(e);
                    }
                }
            }
        }

        return map;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String resolveViewPath(PortalControllerContext portalControllerContext, String name) throws PortletException {
        // View
        View view;
        try {
            view = this.viewResolver.resolveViewName(name, null);
        } catch (Exception e) {
            throw new PortletException(e);
        }

        // Path
        String path;
        if ((view != null) && (view instanceof JstlView)) {
            JstlView jstlView = (JstlView) view;
            path = jstlView.getUrl();
        } else {
            path = null;
        }

        return path;
    }

}
