package tatoo;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Points to and manages the Resource from which the text is read. At the
 * Moment it points to the language files in ./tatoo.
 */
public class TextWrapper {

	/**
	 * enum to define the languages available
	 */
	public enum Language {
		/** Define English */
		LANG_EN("tatoo.lang_en", "English"), 
		/** Define German */
		LANG_DE("tatoo.lang_de", "German");

		private final String l;
		private final String l_name;

		Language(String s, String s_name) {
			l = s;
			l_name = s_name;
		}

		public String getValue() {
			return l;
		};

		/**
		 * get the name of the language
		 * @return name of language
		 */
		public String getName() {
			return l_name;
		}
	}

	/** The language of tatoo. Default is Englisch */
	private static Language language = Language.LANG_EN;

	/**
	 * Points to and manages the Resource from which the text is read. At the
	 * Moment it points to the language files in ./tatoo.
	 */
	private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(language.getValue());

	/**
	 * Standard Constructor. Set the default language which is English. Use
	 * {@link #TextWrapper(Language lang)} to set another language.
	 */
	public TextWrapper() {
	}

	/**
	 * Constructor to set another language than the default.
	 * 
	 * @param lang
	 *            the Language to set.
	 */
	public TextWrapper(Language lang) {
		setLanguage(lang);
	}

	/**
	 * set another language
	 * @param lang the language to set
	 */
	//TODO: absichern gegen Fehler bei nicht vorhandensein von Sprachen
	public void setLanguage(Language lang) {
		language = lang;
		RESOURCE_BUNDLE = ResourceBundle.getBundle(language.getValue());
	}

	/**
	 * Get a String from the ResourceBundle. 
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
