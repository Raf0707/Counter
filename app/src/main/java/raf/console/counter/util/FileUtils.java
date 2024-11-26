package raf.console.counter.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class FileUtils {

    public static String readFileToString(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        inputStream.close();
        return stringBuilder.toString();
    }

    public static void writeStringToFile(Context context, String data, String fileName) throws IOException {
        FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        outputStream.write(data.getBytes());
        outputStream.close();
    }

    public static void copyAssetToFile(AssetManager assetManager, String assetFileName, File destinationFile) throws IOException {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(assetFileName);
            out = new FileOutputStream(destinationFile);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}