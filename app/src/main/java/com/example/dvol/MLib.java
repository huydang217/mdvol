package com.example.dvol;

import android.content.Context;
import android.util.Log;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MLib {
    public static native void MLibInit(Context ctx);

    private Python mPython;

    public void SetPython(Python instancePython) {
        mPython = instancePython;
        mPython.getModule("sys")
                    .get("path")
                    .callAttr(
                            "append",
                            "/storage/emulated/0/Android/data/com.example.myapplication/files");
    }

    public void __HFix__HomeFragment() {
    	
    }
    
    public boolean Reload(String moduleName) {
        if (mPython != null) {
            PyObject _modetModule = mPython.getModule(moduleName);
            PyObject _modReloaddz = mPython.getModule("importlib");
            _modReloaddz.callAttr("reload", _modetModule);
            return true;
        } 
        return false; 
    }

    // have HFix-001

    public static boolean copyFile(String sourcePath, String destinationPath) {
        if (sourcePath.equals(destinationPath)) {
            // Source and destination paths are the same, no need to copy
            return true;
        }

        File sourceFile = new File(sourcePath);
        File destinationFile = new File(destinationPath);

        // Check if source file exists
        if (!sourceFile.exists()) {
            Log.e("copyFile", "Source file does not exist: " + sourcePath);
            return false;
        }

        // Check if destination file exists
        if (destinationFile.exists()) {
            // Compare file sizes and timestamps to determine if copying is necessary
            long sourceLastModified = sourceFile.lastModified();
            long destinationLastModified = destinationFile.lastModified();

            if (sourceLastModified <= destinationLastModified) {
                // Destination file is newer or has the same timestamp, no need to copy
                Log.d("copyFile", "Destination file is up-to-date: " + destinationPath);
                return true;
            }
        }

        try (FileInputStream in = new FileInputStream(sourceFile);
                FileOutputStream out = new FileOutputStream(destinationFile)) {

            byte[] buffer = new byte[1024];
            int read;

            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            return true;

        } catch (IOException e) {
            Log.e("copyFile", "Error copying file: " + e.getMessage());
            return false;
        }
    }
}
