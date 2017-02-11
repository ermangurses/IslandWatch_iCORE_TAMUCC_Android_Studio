package com.example.egurses.islandwatch_icore_tamucc_android_studio;

/**
 * Created by egurses on 2/10/17.
 */


import java.io.File;
import android.os.Environment;

public final class IslandWatch_iCORE_TAMUCC_BaseAlbumDirFactory extends IslandWatch_iCORE_TAMUCC_AlbumStorageDirFactory {

    // Standard storage location for digital camera files
    private static final String CAMERA_DIR = "/dcim/";

    @Override
    public File getAlbumStorageDir(String albumName) {
        return new File (
                Environment.getExternalStorageDirectory()
                        + CAMERA_DIR
                        + albumName
        );
    }
}

