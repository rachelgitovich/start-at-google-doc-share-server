package docSharing.utils;

import docSharing.entity.Folder;
import docSharing.entity.GeneralItem;
import docSharing.service.AuthService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validations {

    public static void validate(String regex, String data) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);

        if (!matcher.matches())
            throw new IllegalArgumentException(ExceptionMessage.VALIDATION_FAILED.toString() + data);
    }

    public static Boolean validateAction(AuthService service, GeneralItem item, String token) {
        Long userId;
        try {
            userId = service.validateToken(token);
        } catch (NullPointerException e) {
            return false;
        }
        if (userId != item.getUserId()) return false;
        return true;
    }
}
