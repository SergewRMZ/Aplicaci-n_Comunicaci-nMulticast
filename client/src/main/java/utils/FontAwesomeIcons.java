package utils;

import javax.swing.Icon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

public class FontAwesomeIcons {
  public static Icon userIcon(int size) {
    FontIcon icon = FontIcon.of(FontAwesomeSolid.USER, size);
    icon.setIconColor(AppColors.PRIMARY_COLOR);
    return icon;
  }
  
  public static Icon backIcon(int size) {
    FontIcon icon = FontIcon.of(FontAwesomeSolid.ARROW_LEFT, size);
    icon.setIconColor(AppColors.PRIMARY_COLOR);
    return icon;
  }
  
    public static Icon EmojiIcon(int size) {
        FontIcon icon = FontIcon.of(FontAwesomeSolid.BLIND, size);
        icon.setIconColor(AppColors.PRIMARY_COLOR);
        return icon;
    }
    
}
