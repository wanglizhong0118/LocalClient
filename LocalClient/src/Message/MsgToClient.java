package Message;

public class MsgToClient {

    public final static String NEWLINE = System.getProperty("line.separator");

    /* Message Header definition */
    final public static String CONNECTION_SSL = "Connected to the SSL server\n";

    final public static String FILE_DOWNLOAD_REPLACE = "A file with same name already exists. Replace? (Y/N)";
    final public static String FILE_DOWNLOAD_CANCEL = "File download canceled \n";
    final public static String FILE_DOWNLOAD_PATH_UNKNOWN = "Specified download path does not exist \n";

    final public static String FILE_UPLOAD_NOTEXIST = "Target file does not exist \n";
    final public static String FILE_UPLOAD_SENDING = "Target file is sending \n";
    final public static String FILE_UPLOAD_PARAMETER_WRONG = "ERRO: bad number of parameter \n";

    final public static String SUCESS_FILE_UPLOAD = "OK upload down" + NEWLINE;
    final public static String SUCESS_FILE_DOWNLOAD = "OK download done" + NEWLINE;
    final public static String SUCESS_FILE_DELETE = "OK delete done" + NEWLINE;
}
