package vn.kingbee.widget.imageview.circle;

public class AvatarUtils {
    private AvatarUtils() {
    }

    public static String formatDefaultAvatarName(String username) {
        String formattedName = username.replaceAll("\\s+", " ");
        formattedName = formattedName.trim();
        if (!formattedName.isEmpty()) {
            String avatarNameFirstLetter = formattedName.substring(0, 1);
            String avatarNameSecondLetter = formattedName.substring(formattedName.lastIndexOf(32) + 1).substring(0, 1);
            return formattedName.split(" ").length == 1 ? avatarNameFirstLetter : avatarNameFirstLetter + avatarNameSecondLetter;
        } else {
            return "";
        }
    }
}
