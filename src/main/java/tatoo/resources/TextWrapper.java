package tatoo.resources;

import antafes.utilities.ConfigurationFactory;
import antafes.utilities.language.LanguageInterface;
import lombok.Setter;
import tatoo.Configuration;

import java.util.MissingResourceException;

/**
 * Points to and manages the Resource from which the text is read. At the Moment
 * it points to the language files in ./tatoo.
 * @deprecated Use language objects from tatoo.Configuration
 */
public class TextWrapper
{
    /**
     * The language of tatoo. Default is Englisch
     */
    @Setter
    private static LanguageInterface language;

    /**
     * Standard Constructor. Set the default language which is English. Use
     * {@link #TextWrapper(LanguageInterface)} to set another language.
     */
    public TextWrapper()
    {
        Configuration configuration = (Configuration) ConfigurationFactory.getConfiguration(Configuration.class);
        setLanguage(configuration.getLanguageObject());
    }

    /**
     * Constructor to set another language than the default.
     *
     * @param lang the Language to set.
     */
    public TextWrapper(LanguageInterface lang)
    {
        setLanguage(lang);
    }

    /**
     * Get a String from the ResourceBundle.
     *
     * @param key
     * @return
     */
    public static String getString(String key)
    {
        try {
            return language.translate(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
