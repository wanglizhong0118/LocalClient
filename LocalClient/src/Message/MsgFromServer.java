package Message;

public class MsgFromServer {

    /* Message Header definition */
    final public static String HEADER_FILE = "<FILE>";
    final public static String HEADER_INFO = "<INFO>";
    final public static String HEADER_LOGIN = "<LOGIN>";
    final public static String HEADER_SYSTEM = "<SYSTEM>";

    final public static String SUCCESS_lOGIN = "OK login succeed";
    final public static String EMAIL_AUTHENTICATION = "Please check email to get one time password";

    /* Message File Header definition */
    final public static String FILE_DOWNLOAD = "<DownloadFile>";
    final public static String FILE_UPLOAD = "<UploadFile>";
    final public static String FILE_DELETE = "<DeleteFile>";

    final public static String EXIT_CONNECTION = "Good bye";

    /* INFO email authentication */
    final public static String EMAIL_AUTHN_SUCCESS = "Email authentication succeed. Welcome to the file system";
    final public static String EMAIL_AUTHN_FAILED = "Email authentication failed. Pleas try to login again";

}
