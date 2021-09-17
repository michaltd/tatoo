package tatoo.language;

import antafes.utilities.language.LanguageInterface;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public abstract class Language implements LanguageInterface
{
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private ResourceBundle translations;

    @Override
    public String translate(String key)
    {
        try {
            return translations.getString(key);
        } catch (NullPointerException | MissingResourceException | ClassCastException e) {
            return "!" + key + "!";
        }
    }
}
