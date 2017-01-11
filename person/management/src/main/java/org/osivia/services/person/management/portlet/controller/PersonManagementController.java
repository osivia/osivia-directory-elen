/**
 * 
 */
package org.osivia.services.person.management.portlet.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.dom4j.QName;
import org.dom4j.dom.DOMAttribute;
import org.dom4j.dom.DOMElement;
import org.jboss.portal.theme.ThemeConstants;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.html.HTMLConstants;
import org.osivia.portal.api.windows.StartingWindowBean;
import org.osivia.services.person.management.portlet.service.PersonManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * @author Loïc Billon
 *
 *
 */
@Controller
@SessionAttributes("personManagementForm")
@RequestMapping("VIEW")
public class PersonManagementController extends CMSPortlet implements PortletContextAware, PortletConfigAware {

	@Autowired
	private PersonManagementService service;
	
	@Autowired
	private PersonService personService;
	
	private PortletContext portletContext;

	private PortletConfig portletConfig;
	
    @RenderMapping
    public String view() {
    	return "view";
	}
    

    /**
     * Action when a person is selected, open person card in new window
     * @param request
     * @param response
     * @param form
     * @throws IOException
     * @throws PortalException
     */
    @ActionMapping("selectPerson")
    public void selectPerson(ActionRequest request, ActionResponse response, @ModelAttribute("personManagementForm") PersonManagementForm form) throws IOException, PortalException {
    	
    	
    	String selectedPerson = form.getSelectedPerson();
    	
    	if(selectedPerson != null) {
    		
    		// Portal controller context
            PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
    		 
            Map<String, String> windowProperties = new HashMap<String, String>();
            
            windowProperties.put(ThemeConstants.PORTAL_PROP_REGION, "col-2");
            windowProperties.put("uidFichePersonne", selectedPerson);

            windowProperties.put("osivia.ajaxLink", "1");
            windowProperties.put("osivia.hideTitle", "1");

            request.setAttribute(Constants.PORTLET_ATTR_START_WINDOW, new StartingWindowBean("personne","directory-person-card-instance",windowProperties));
    	
    	}
    	
    	
    }

    /**
     * 
     * Ajax action for searching persons
     * @param request
     * @param response
     * @param filter
     * @param form
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping("search")
    public void search(ResourceRequest request, ResourceResponse response, 
            @RequestParam(value = "filter", required = false) String filter, @ModelAttribute("personManagementForm") PersonManagementForm form) 
            throws PortletException, IOException {
    	
    	// Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Search results
        List<Person> persons = this.service.searchPersons(portalControllerContext, filter);

    	DOMElement filler = new DOMElement(QName.get(HTMLConstants.DIV));
    	filler.add(new DOMAttribute(QName.get(HTMLConstants.CLASS), "portlet-filler"));
        String html = "";
    	
    	
        for (Person person : persons) {

        	DOMElement parentDiv = new DOMElement(QName.get(HTMLConstants.DIV));
        	
        	if(form.getSelectedPerson() != null && form.getSelectedPerson().equals(person.getUid())) {
        		parentDiv.add(new DOMAttribute(QName.get(HTMLConstants.CLASS), "table-row bg-primary data ui-selectee ui-selected"));
        	}
        	else {
        		parentDiv.add(new DOMAttribute(QName.get(HTMLConstants.CLASS), "table-row"));
        	}
        	
        	filler.add(parentDiv);
        	
        	DOMElement mainDiv = new DOMElement(QName.get(HTMLConstants.DIV));
        	mainDiv.add(new DOMAttribute(QName.get(HTMLConstants.CLASS), "person"));
        	
        	parentDiv.add(mainDiv);
        	
        	DOMElement avatarDiv = new DOMElement(QName.get(HTMLConstants.DIV));
        	avatarDiv.add(new DOMAttribute(QName.get(HTMLConstants.CLASS), "person-avatar"));
        	DOMElement avatarImg = new DOMElement(QName.get(HTMLConstants.IMG));
        	avatarImg.add(new DOMAttribute(QName.get(HTMLConstants.SRC), person.getAvatar().getUrl()));
        	avatarDiv.add(avatarImg);
        	mainDiv.add(avatarDiv);
        	
        	DOMElement titleDiv = new DOMElement(QName.get(HTMLConstants.DIV));
        	titleDiv.add(new DOMAttribute(QName.get(HTMLConstants.CLASS), "person-title"));
        	DOMElement titleLink = new DOMElement(QName.get(HTMLConstants.A));
        	titleLink.add(new DOMAttribute(QName.get(HTMLConstants.HREF), "#"));
        	titleLink.add(new DOMAttribute(QName.get(HTMLConstants.CLASS), "person-link"));
        	titleLink.add(new DOMAttribute(QName.get("data-uid"), person.getUid()));
        	titleLink.setText(person.getDisplayName());
        	titleDiv.add(titleLink);
        	mainDiv.add(titleDiv);
        	
        	DOMElement extraDiv = new DOMElement(QName.get(HTMLConstants.DIV));
        	extraDiv.add(new DOMAttribute(QName.get(HTMLConstants.CLASS), "person-extra"));
        	extraDiv.setText(person.getMail());
        	mainDiv.add(extraDiv);
        	
        }
        
        html = filler.asXML();
        
        // Content type
        response.setContentType("text/html");

        // Content
        PrintWriter printWriter = new PrintWriter(response.getPortletOutputStream());
        printWriter.write(html.toString());
        printWriter.close();
    }
    
	
    
    @ModelAttribute("personManagementForm")
    public PersonManagementForm getForm() {
    	return new PersonManagementForm();
    }
	
	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.context.PortletConfigAware#setPortletConfig(javax.portlet.PortletConfig)
	 */
	@Override
	public void setPortletConfig(PortletConfig portletConfig) {
		this.portletConfig = portletConfig;
		
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.context.PortletContextAware#setPortletContext(javax.portlet.PortletContext)
	 */
	@Override
	public void setPortletContext(PortletContext portletContext) {
		this.portletContext = portletContext;
		
	}

	
}
