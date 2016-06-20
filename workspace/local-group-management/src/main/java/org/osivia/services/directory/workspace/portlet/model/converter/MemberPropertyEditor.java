package org.osivia.services.directory.workspace.portlet.model.converter;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.services.directory.workspace.portlet.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Member property editor.
 *
 * @author Cédric Krommenhoek
 * @see PropertyEditorSupport
 */
@Component
public class MemberPropertyEditor extends PropertyEditorSupport {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Person service. */
    @Autowired
    private PersonService personService;


    /**
     * Constructor.
     */
    public MemberPropertyEditor() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        // Member
        Member member = this.applicationContext.getBean(Member.class);

        if (StringUtils.isNotEmpty(text)) {
            // Person
            Person person = this.personService.getPerson(text);

            // Member
            if (person != null) {
                member.setId(person.getUid());
                member.setDisplayName(person.getDisplayName());
                member.setAvatar(person.getAvatar().getUrl());
                member.setMail(person.getMail());
            }
        }

        this.setValue(member);
    }

}
