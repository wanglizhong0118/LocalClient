package Client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import Message.MsgFromServer;
import Message.MsgToServer;

public class ClientMain {

    public static void main(String[] args) throws IOException, InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        boolean runClient = true;
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        Socket clientSocket = new Socket(Setter.HOST, Setter.PORT_NUMBER);
        OutputStream outWriter = clientSocket.getOutputStream();
        BufferedInputStream inReader = new BufferedInputStream(clientSocket.getInputStream());
        System.out.println("Welcome to the SSL server");
        System.out.println("Please enter your username and password");

        while (runClient) {
            boolean login = false;
            while (!login) {
                fetechInputAndOutwrite(stdIn, outWriter);
                String deLogin = readAndDecryptMsg(inReader);
                String[] deLoginArray = deLogin.split(Setter.NEWLINE);
                if (deLoginArray[1].equals(MsgFromServer.SUCCESS_lOGIN)) {
                    System.out.println(deLogin);
                    fetechInputAndOutwrite(stdIn, outWriter);
                    String emailAuth = readAndDecryptMsg(inReader);
                    String[] emailAuthArray = emailAuth.split(Setter.NEWLINE);
                    if (emailAuthArray[1].equals(MsgFromServer.EMAIL_AUTHN_SUCCESS)) {
                        System.out.println(emailAuth);
                        login = true;
                        break;
                    } else {
                        System.out.println(emailAuth);
                        break;
                    }
                } else {
                    System.out.println(deLogin);
                    break;
                }
            }

            while (login) {
                boolean uploadSuc = true;
                String[] commandArray = stdIn.readLine().split("\\s");
                String formedRequest = String.join(Setter.NEWLINE, commandArray);

                if (commandArray[0].equals(MsgToServer.HEADER_FILE)
                        && commandArray[1].equals(MsgToServer.FILE_UPLOAD)) {
                    uploadSuc = FileUploadRequest.init(commandArray, outWriter);
                } else {
                    String AESRequest = Encryption.encrypt(formedRequest, Setter.SECURITY_KEY);
                    byte[] requestArray = new byte[AESRequest.length()];
                    requestArray = AESRequest.getBytes(StandardCharsets.UTF_8);
                    outWriter.write(requestArray, 0, requestArray.length);
                }

                if (uploadSuc) {
                    String deAESResponse = readAndDecryptMsg(inReader);
                    String[] responseArray = deAESResponse.split(Setter.NEWLINE);
                    String responseHeader = responseArray[0];
                    switch (responseHeader) {
                    case MsgFromServer.HEADER_LOGIN:
                        System.out.println(deAESResponse);
                        break;
                    case MsgFromServer.HEADER_INFO:
                        System.out.println(deAESResponse);
                        break;
                    case MsgFromServer.HEADER_FILE:
                        FileHandler.init(deAESResponse);
                        break;
                    case MsgFromServer.HEADER_SYSTEM:
                        runClient = false;
                        System.out.println(deAESResponse);
                        break;
                    default:
                        System.out.println(deAESResponse);
                        break;
                    }
                }
            }
        }
    }

    private static void fetechInputAndOutwrite(BufferedReader stdIn, OutputStream outWriter)
            throws IOException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String[] loginReuqest = stdIn.readLine().split("\\s");
        String formedLoginReuqest = String.join(Setter.NEWLINE, loginReuqest);
        String AESLogin = Encryption.encrypt(formedLoginReuqest, Setter.SECURITY_KEY);
        byte[] loginReBy = new byte[AESLogin.length()];
        loginReBy = AESLogin.getBytes(StandardCharsets.UTF_8);
        outWriter.write(loginReBy, 0, loginReBy.length);
    }

    private static String readAndDecryptMsg(BufferedInputStream inReader)
            throws IOException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] fromServer = new byte[Setter.MAX_BUFFER];
        int bytesRead = 0;
        int bufferLength;
        while ((bufferLength = inReader.read(fromServer, bytesRead, 256)) != -1) {
            bytesRead += bufferLength;
            if (bytesRead == Setter.MAX_BUFFER || inReader.available() == 0) {
                break;
            }
        }
        String responseFromServer = new String(fromServer, 0, bytesRead);
        String deAESResponse = Encryption.decrypt(responseFromServer, Setter.SECURITY_KEY);
        return deAESResponse;
    }

}
