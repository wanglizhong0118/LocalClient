package Client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import Message.MsgFromServer;
import Message.MsgToClient;

public class FileHandler {

    public static void init(String deAESResponse) throws IOException {

        String[] responseArray = deAESResponse.split(Setter.NEWLINE);
        String fileOperation = responseArray[1];

        switch (fileOperation) {

        case MsgFromServer.FILE_DOWNLOAD:
            downloadFileResponse(responseArray);
            break;
        case MsgFromServer.FILE_UPLOAD:
            uploadFileResponse(responseArray);
            break;
        case MsgFromServer.FILE_DELETE:
            deleteFileResponse(responseArray);
            break;
        default:
            System.out.println(deAESResponse);
            break;
        }

    }

    public static void deleteFileResponse(String[] responseArray) {
        System.out.println(MsgToClient.SUCESS_FILE_DELETE);

    }

    public static void uploadFileResponse(String[] responseArray) {
        System.out.println(MsgToClient.SUCESS_FILE_UPLOAD);
    }

    public static void downloadFileResponse(String[] responseArray) throws IOException {

        BufferedReader answer = new BufferedReader(new InputStreamReader(System.in));
        String newFilePath = Setter.TEMP_DOWNLOAD + responseArray[2];
        String[] fileBody = Arrays.copyOfRange(responseArray, 3, responseArray.length);
        String fileContent = String.join(Setter.NEWLINE, fileBody);
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        Path downloadPath = Paths.get(Setter.TEMP_DOWNLOAD);
        if (Files.exists(downloadPath)) {
            if (Utils.pathExists(newFilePath)) {
                System.out.println(MsgToClient.FILE_DOWNLOAD_REPLACE);
                if (answer.readLine().equals("Y")) {
                    doDownload(newFilePath, inputStream);
                } else {
                    System.out.println(MsgToClient.FILE_DOWNLOAD_CANCEL);
                    return;
                }
            } else {
                doDownload(newFilePath, inputStream);
            }
        } else {
            System.out.println(MsgToClient.FILE_DOWNLOAD_PATH_UNKNOWN);
            return;
        }
    }

    private static void doDownload(String newFilePath, InputStream inputStream)
            throws FileNotFoundException, IOException {
        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            byte[] outTempArray = new byte[Setter.MAX_BUFFER];
            fos = new FileOutputStream(newFilePath);
            bos = new BufferedOutputStream(fos);
            bytesRead = inputStream.read(outTempArray, 0, outTempArray.length);
            current = bytesRead;
            do {
                bytesRead = inputStream.read(outTempArray, current, (outTempArray.length - current));
                if (bytesRead >= 0)
                    current += bytesRead;
            } while (bytesRead > -1);
            bos.write(outTempArray, 0, current);
            bos.flush();
            System.out.println(MsgToClient.SUCESS_FILE_DOWNLOAD);
        } finally {
            if (fos != null)
                fos.close();
            if (bos != null)
                bos.close();
        }
    }

}
