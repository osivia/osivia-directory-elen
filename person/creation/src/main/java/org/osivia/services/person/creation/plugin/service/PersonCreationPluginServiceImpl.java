package org.osivia.services.person.creation.plugin.service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;

/**
 * Person creation plugin service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see PersonCreationPluginService
 */
@Service
public class PersonCreationPluginServiceImpl implements PersonCreationPluginService {

    /** Mail regex. */
    private static final String MAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    /** Person service. */
    @Autowired
    private PersonUpdateService personService;

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;


    /** Mail pattern. */
    private final Pattern mailPattern;

    
    /** Person identifier length. */
    int PERSON_UID_LENGTH = 10;

    
    /** Base 62 alphabet conversion table. */
    private static final char[] TO_BASE_62 = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /** Base 62. */
    private static final int BASE_62 = 62;
    
    /**
     * Constructor.
     */
    public PersonCreationPluginServiceImpl() {
        super();

        // Mail pattern
        this.mailPattern = Pattern.compile(MAIL_REGEX);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void createPerson(FormFilterContext context, FormFilterExecutor executor) throws FormFilterException {
        // HTTP servlet request
        HttpServletRequest servletRequest = context.getPortalControllerContext().getHttpServletRequest();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(servletRequest.getLocale());

        // Variables
        Map<String, String> variables = context.getVariables();

        // Mail
        String mailVariableName = context.getParamValue(executor, MAIL_FILTER_VARIABLE_NAME);
        String mail = StringUtils.trim(variables.get(mailVariableName));

        // Check mail syntax
        Matcher matcher = this.mailPattern.matcher(mail);
        if (!matcher.matches()) {
            String message = bundle.getString("PERSON_CREATION_FORM_FILTER_MESSAGE_ERROR_INVALID_MAIL");
            throw new FormFilterException(message);
        }

        // Person
        Person person = this.personService.getPerson(mail);

        if (person == null) {
            person = this.personService.getEmptyPerson();
            String uid = generateUid();
            person.setUid(uid);
            person.setCn(mail);
            person.setSn(mail);
            person.setMail(mail);
            this.personService.create(person);

            // Password
            String password = RandomStringUtils.randomAlphanumeric(8);
            String passwordVariableName = context.getParamValue(executor, PASSWORD_FILTER_VARIABLE_NAME);
            variables.put(passwordVariableName, password);
            variables.put("uid", uid);
            this.personService.updatePassword(person, password);
        } else {
            String message = bundle.getString("PERSON_CREATION_FORM_FILTER_MESSAGE_ERROR_ALREADY_EXISTS");
            throw new FormFilterException(message);
        }
    }

    
    /**
     * Generate person identifier.
     *
     * @return identifier
     */
    private String generateUid() {
        // Random generator
        Random random = new Random();

        // Search criteria
        Person criteria = this.personService.getEmptyPerson();
        // Search results
        List<Person> results = null;

        // Person identifier
        String uid = null;

        while (StringUtils.isEmpty(uid) || CollectionUtils.isNotEmpty(results)) {
            // Generate new identifier
            Character[] array = new Character[PERSON_UID_LENGTH];
            for (int i = 0; i < PERSON_UID_LENGTH; i++) {
                array[i] = TO_BASE_62[random.nextInt(BASE_62)];
            }
            uid = StringUtils.join(array);

            // Check identifier uniqueness
            criteria.setUid(uid);
            results = this.personService.findByCriteria(criteria);
        }

        return uid;
    }
    
    
}
