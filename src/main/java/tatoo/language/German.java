package tatoo.language;

import java.util.Locale;
import java.util.ResourceBundle;

public class German extends Language
{
    public German()
    {
        this.setTranslations(ResourceBundle.getBundle("lang", Locale.GERMAN));
    }

    @Override
    public String getLanguage()
    {
        return Locale.GERMAN.getDisplayLanguage(Locale.GERMAN);
    }
}
