package Client;

import java.io.File;

public class Utils {

    static boolean pathExists(String newFilePath) {
        boolean isExist = false;
        File checkedFile = new File(newFilePath);
        if (checkedFile.exists() && !checkedFile.isDirectory()) {
            isExist = true;
        }
        return isExist;
    }

}
