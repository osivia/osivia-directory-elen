package org.osivia.directory.v2.model.converter;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Boolean to string converter.
 * 
 * @author Cédric Krommenhoek
 * @see Converter
 */
@Component
public class BooleanToString implements Converter<Boolean, String> {

    /**
     * Constructor.
     */
    public BooleanToString() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String convert(Boolean source) {
        return BooleanUtils.toString(source, "TRUE", "FALSE", null);
    }

}
