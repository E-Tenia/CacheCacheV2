package fr.ethan.cachecache.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileManager {
    public static void saveFile(InputStream in, File dest, String name, boolean overwrite) throws IOException {
        if (in == null)
            return;  if (!dest.exists()) {
            dest.mkdirs();
        }

        File result = new File(dest, name);
        if (result.exists()) {
            if (overwrite) {
                result.delete();
            } else {
                return;
            }
        }

        OutputStream out = new FileOutputStream(result);
        byte[] buffer = new byte[1024];

        int length;

        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }

        in.close();
        out.close();
    }
}
