package org.osivia.directory.v2.model.converter;

import java.text.ParseException;
import java.util.Date;

import org.apache.directory.api.util.GeneralizedTime;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Generalized time to date converter.
 * 
 * @author Cédric Krommenhoek
 * @see Converter
 * @see String
 * @see Date
 */
@Component
public class GeneralizedTimeToDate implements Converter<String, Date> {

    /**
     * Constructor.
     */
    public GeneralizedTimeToDate() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Date convert(String source) {
        Date date;
        try {
            GeneralizedTime generalizedTime = new GeneralizedTime(source);
            date = generalizedTime.getDate();
        } catch (ParseException e) {
            date = null;
        }
        return date;
    }

}
