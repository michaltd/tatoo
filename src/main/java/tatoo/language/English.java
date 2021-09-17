package tatoo.language;

import java.util.Locale;
import java.util.ResourceBundle;

public class English extends Language
{
    public English()
    {
        this.setTranslations(ResourceBundle.getBundle("lang", Locale.ENGLISH));
    }

    @Override
    public String getLanguage()
    {
        return Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH);
    }
}
