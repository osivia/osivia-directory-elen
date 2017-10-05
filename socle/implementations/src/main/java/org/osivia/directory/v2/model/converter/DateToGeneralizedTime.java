package org.osivia.directory.v2.model.converter;

import java.util.Date;

import org.apache.directory.api.util.GeneralizedTime;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Date to generalized time converter.
 * 
 * @author Cédric Krommenhoek
 * @see Converter
 */
@Component
public class DateToGeneralizedTime implements Converter<Date, String> {

    /**
     * Constructor.
     */
    public DateToGeneralizedTime() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String convert(Date source) {
        GeneralizedTime generalizedTime = new GeneralizedTime(source);
        return generalizedTime.toGeneralizedTime();
    }

}
