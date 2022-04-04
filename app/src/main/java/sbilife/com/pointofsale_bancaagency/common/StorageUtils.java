package sbilife.com.pointofsale_bancaagency.common;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

public class StorageUtils implements Serializable {

    public static final String DIRECT_DIRECTORY = "SBI-Smart Advisor";
    public static final String DIRECT_DIRECTORY_CIF = "SbiLife-CIF";
    public static final String DIRECT_DIRECTORY_DOC = "doc";
    private CommonMethods mCommonMethods;

    public StorageUtils() {
        mCommonMethods = new CommonMethods();
    }

    public boolean isDeviceAboveP() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R);
    }

    public String getMimeType(Context mContext, Uri uri) {
        String mimeType = null;
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            ContentResolver cr = mContext.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    //create temp image file
    public File createTempImageFile(Context mContext, String strFileName, String strSuffix) throws IOException {
        // Create an image file name
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_DCIM + CommonMethods.DIRECT_DIRECTORY);
        File image = File.createTempFile(
                strFileName,  /* prefix */
                strSuffix,         /* suffix e.g .jpg or .mp4*/
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private boolean saveImageToExternalStorage(Context mContext, String fileName, Bitmap mBitmap) {

        Uri imageCollection;

        if (isDeviceAboveP()) {
            imageCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            imageCollection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        ContentValues mContentValues = new ContentValues();
        mContentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        mContentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        if (isDeviceAboveP()) {
            mContentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM
                    + mCommonMethods.DIRECT_DIRECTORY);
            mContentValues.put(MediaStore.MediaColumns.IS_PENDING, 1);
        } else {
            File directory = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM + mCommonMethods.DIRECT_DIRECTORY);
            File file = new File(directory, fileName);
            mContentValues.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
        }
        mContentValues.put(MediaStore.Images.Media.WIDTH, mBitmap.getWidth());
        mContentValues.put(MediaStore.Images.Media.HEIGHT, mBitmap.getHeight());

        try {
            Uri captureImgUri = mContext.getContentResolver().insert(imageCollection, mContentValues);
            if (captureImgUri != null) {
                OutputStream mOutputStream = mContext.getContentResolver().openOutputStream(captureImgUri);

                if (!mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, mOutputStream)) {
                    throw new IOException("Couldn't save bitmap..");
                }
            } else {
                throw new IOException("Couldn't create MediaStore entry");
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public File createFileToAppSpecificDir(Context mContext, String strFilename) {
        String strSubFolderPath = mContext.getExternalFilesDir(null).getAbsolutePath()
                + File.separator + DIRECT_DIRECTORY;

        File subFolder = new File(strSubFolderPath);
        if (!subFolder.exists()) {
            subFolder.mkdirs();
        }

        return new File(subFolder, strFilename);
    }

    public File createFileToAppSpecificDirCIF(Context mContext, String strFilename) {
        String strSubFolderPath = mContext.getExternalFilesDir(null).getAbsolutePath()
                + File.separator + DIRECT_DIRECTORY_CIF;

        File subFolder = new File(strSubFolderPath);
        if (!subFolder.exists()) {
            subFolder.mkdirs();
        }

        return new File(subFolder, strFilename);
    }

    public File createFileToAppSpecificDirDoc(Context mContext, String strFilename) {
        String strSubFolderPath = mContext.getExternalFilesDir(null).getAbsolutePath()
                + File.separator + DIRECT_DIRECTORY_DOC;

        File subFolder = new File(strSubFolderPath);
        if (!subFolder.exists()) {
            subFolder.mkdirs();
        }

        return new File(subFolder, strFilename);
    }

    //save file to app-specific folder
    public File saveFileToAppSpecificDir(Context mContext, String strModuleType, String strFilename, File inputFile) {

        File destFile;
        if (strModuleType.equals(DIRECT_DIRECTORY_CIF)) {
            destFile = createFileToAppSpecificDirCIF(mContext, strFilename);
        } else {
            destFile = createFileToAppSpecificDir(mContext, strFilename);
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(destFile);
            outputStream.write(mCommonMethods.read(inputFile));
            outputStream.flush();
            outputStream.close();

            //access file
            /*byte[] bytes = new byte[1024];

                        FileInputStream outputStream1 = new FileInputStream(new File(subFolder,
                                strfileName + "_R" + increment_val + ".pdf"));

                        outputStream1.read(bytes);
                        outputStream1.close();

                        String string = new String(bytes);*/

        } catch (IOException e) {
            destFile = null;
            e.printStackTrace();
        }

        return destFile;
    }

    private String getFileDispayName(Context mContext, Uri mUri) {
        String displayName = "";
        try (Cursor mCursor = mContext.getContentResolver()
                .query(mUri, null, null, null, null, null)) {
            if (mCursor != null && mCursor.moveToFirst()) {
                displayName = mCursor.getString(mCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
        }
        return displayName;
    }

    public File saveBase64ToFile(Context mContext, String strModuleType,
                                 String strBase64, String strFileName) {
        try {

            File destFile;
            if (strModuleType.equals(DIRECT_DIRECTORY_CIF)) {
                destFile = createFileToAppSpecificDirCIF(mContext, strFileName);
            } else {
                destFile = createFileToAppSpecificDir(mContext, strFileName);
            }

            if (strBase64 != null && !strBase64.equalsIgnoreCase("")) {
                byte[] pdfAsBytes = Base64.decode(strBase64, 0);
                FileOutputStream os;
                os = new FileOutputStream(destFile, false);
                os.write(pdfAsBytes);
                os.flush();
                os.close();
            }
            return destFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
