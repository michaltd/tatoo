package tatoo.resources;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

//import tatoo.resources.TextWrapper.Language;

/**
 * Points to and manages the Resource from which the text is read. At the Moment
 * it points to the language files in ./tatoo.
 */
public class TextWrapper {

    // /**
    // * enum to define the languages available
    // */
    // public enum Language {
    // /** English */
    // LANG_EN ("tatoo.resources.lang_en", "English"),
    // /** German */
    // LANG_DE ("tatoo.resources.lang_de", "German"),
    // /** Spanish */
    // LANG_DE ("tatoo.resources.lang_de", "German"),
    // /** Italian */
    // LANG_DE ("tatoo.resources.lang_de", "German");
    //
    // private final String l;
    // private final String l_name;
    //
    // Language (String s, String s_name) {
    // l = s;
    // l_name = s_name;
    // }
    //
    // public String getValue () {
    // return l;
    // };
    //
    // /**
    // * get the name of the language
    // *
    // * @return name of language
    // */
    // public String getName () {
    // return l_name;
    // }
    // }

    /** The language of tatoo. Default is Englisch */
    // private static Language language = Language.LANG_EN;
    private static Locale         language        = Locale.ENGLISH;

    /**
     * Points to and manages the Resource from which the text is read. At the
     * Moment it points to the language files in ./tatoo.
     */
    // private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle
    // (language.getValue ());
    private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle ("tatoo.resources.lang", language);

    /**
     * The default language.
     */
    private Locale                defaultLang     = Locale.ENGLISH;

    /**
     * Standard Constructor. Set the default language which is English. Use
     * {@link #TextWrapper(Language lang)} to set another language.
     */
    public TextWrapper () {
        this (Locale.ENGLISH);
    }

    /**
     * Constructor to set another language than the default.
     * 
     * @param lang
     * the Language to set.
     */
    public TextWrapper (Locale lang) {
        setLanguage (lang);
    }

    /**
     * set another language
     * 
     * @param lang
     * the language to set
     */
    // TODO: absichern gegen Fehler bei nicht vorhandensein von Sprachen
    public void setLanguage (Locale lang) {
        language = lang;
        RESOURCE_BUNDLE = ResourceBundle.getBundle ("tatoo.resources.lang", language);
    }

    /**
     * Get a String from the ResourceBundle.
     * 
     * @param key
     * @return
     */
    public static String getString (String key) {
        try {
            return RESOURCE_BUNDLE.getString (key);
        }
        catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    public void setDefault (Locale lang) {
        this.defaultLang = lang;
    }
}
