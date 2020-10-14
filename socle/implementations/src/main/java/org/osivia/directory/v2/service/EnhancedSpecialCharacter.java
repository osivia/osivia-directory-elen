package org.osivia.directory.v2.service;

import org.passay.AbstractCharacterRule;
import org.passay.PasswordUtils;
import org.passay.SpecialCharacterRule;

/**
 * The Class EnhancedSpecialCharacter.
 * Add special caracters
 */
public class EnhancedSpecialCharacter extends SpecialCharacterRule {
    /** Special characters, value is {@value}. */
    public static final String CHARS = SpecialCharacterRule.CHARS +"€";



    /** Default constructor. */
    public EnhancedSpecialCharacter() {}


    /**
     * Creates a new non alphanumeric character rule.
     *
     * @param  num  number of non-alphanumeric characters to enforce
     */
    public EnhancedSpecialCharacter(final int num)
    {
      super(num);
    }


    @Override
    public String getValidCharacters()
    {
      return CHARS;
    }




    @Override
    protected String getCharacterTypes(final String password)
    {
      return PasswordUtils.getMatchingCharacters(CHARS, password);
    }    

}
