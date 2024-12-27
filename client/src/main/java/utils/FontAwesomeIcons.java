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
  
  public static Icon emojiIcon(int size) {
    FontIcon icon = FontIcon.of(FontAwesomeSolid.SMILE, size);
    icon.setIconColor(AppColors.SECONDARY_COLOR);
    return icon;
  }
  
  public static Icon fileIcon(int size) {
    FontIcon icon = FontIcon.of(FontAwesomeSolid.FILE, size);
    icon.setIconColor(AppColors.SECONDARY_COLOR);
    return icon;
  }
  public static Icon sendIcon(int size) {
    FontIcon icon = FontIcon.of(FontAwesomeSolid.PAPER_PLANE, size);
    icon.setIconColor(AppColors.getBLUE_LIGHT_COLOR());
    return icon;
  }
}
