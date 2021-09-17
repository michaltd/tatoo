package tatoo;

import antafes.utilities.BaseConfiguration;
import antafes.utilities.Utilities;
import lombok.Getter;
import tatoo.db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Configuration extends BaseConfiguration
{
    @Override
    public String getBasePath()
    {
        return System.getProperty("user.home") + "/.tatoo/";
    }

    public void setDatabase(DBConnection.DBases database)
    {
        this.getProperties().setProperty("database", database.toString());
    }

    public DBConnection.DBases getDatabase()
    {
        if (!this.getProperties().containsKey("database")) {
            setDatabase(DBConnection.DBases.H2);
        }

        return DBConnection.DBases.valueOf(this.getProperties().getProperty("database"));
    }

    /**
     * Get the selected language.
     *
     * @return Language enum of the selected language
     */
    @Override
    public LanguageInterface getLanguage()
    {
        if (!this.getProperties().containsKey("language")) {
            setLanguage(Language.ENGLISH);
        }

        return antafes.utilities.Configuration.Language.valueOf(this.getProperties().getProperty("language"));
    }

    /**
     * Get a language object from the currently selected language.
     *
     * @return Language object fetched from the enum of the currently selected language
     */
    public antafes.utilities.language.LanguageInterface getLanguageObject()
    {
        antafes.utilities.language.LanguageInterface language = null;

        try {
            language = (antafes.utilities.language.LanguageInterface) Class.forName(
                this.getLanguage().getLanguageString()
            ).getDeclaredConstructor().newInstance();
        } catch (
            ClassNotFoundException
                | InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException ex
        ) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }

        return language;
    }

    @Getter
    public enum Language implements LanguageInterface {
        ENGLISH ("tatoo.language.English", "English", "images/english.png"),
        GERMAN ("tatoo.language.German", "German", "images/german.png");

        private final String languageString;
        private final String name;
        private final ImageIcon icon;

        Language(String languageString, String name, String iconPath) {
            this.languageString = languageString;
            this.name = name;

            Toolkit kit = Toolkit.getDefaultToolkit();
            Image img = kit.createImage(
                Utilities.getResourceInJar(iconPath)
            );
            this.icon = new ImageIcon(img);
        }
    }
}
