package loc.abondarev.sweater.util;

import loc.abondarev.sweater.domain.User;

public abstract class MessageHelper {
    public static String getAuthorName(User author) {
        return author != null ? author.getUsername() : "<none>";
    }
}
