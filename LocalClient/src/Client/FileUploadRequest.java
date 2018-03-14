package Client;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import Message.MsgToClient;
import Message.MsgToServer;

public class FileUploadRequest {

    public static boolean init(String[] response, OutputStream outWriter) throws IOException, InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        if (response.length != 6) {
            System.out.println(MsgToClient.FILE_UPLOAD_PARAMETER_WRONG);
            return false;
        } else {
            String filename = response[2];
            String fileowner = response[3];
            String filetarget = response[4];
            String filePath = response[5];

            if (!Utils.pathExists(filePath)) {
                System.out.println(MsgToClient.FILE_UPLOAD_NOTEXIST);
                return false;
            } else {
                String requestHeader = MsgToServer.HEADER_FILE + Setter.NEWLINE + MsgToServer.FILE_UPLOAD
                        + Setter.NEWLINE + filename + Setter.NEWLINE + fileowner + Setter.NEWLINE + filetarget
                        + Setter.NEWLINE;
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                String request = requestHeader + content;
                String AESrequest = AEScoding.encrypt(request, Setter.AES_KEY);
                byte[] requestArray = new byte[(int) AESrequest.length()];
                requestArray = AESrequest.getBytes(StandardCharsets.UTF_8);
                outWriter.write(requestArray, 0, requestArray.length);
                System.out.print(MsgToClient.FILE_UPLOAD_SENDING);
            }
        }
        return true;
    }
}
