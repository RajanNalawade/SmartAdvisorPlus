package sbilife.com.pointofsale_bancaagency.common;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

public class FileLoader extends AsyncTask<Uri, String, File> {
    private WeakReference<Context> mContextRef;
    public FileLoaderResponce delegate = null;
    private String strFileName ="", strModuleType = "";

    public interface FileLoaderResponce{
        void fileLoadFinished(File fileOutput);
    }

    public FileLoader(Context mContext,String strModuleType, String strFileName, FileLoaderResponce delegate) {
        this.mContextRef = new WeakReference<>(mContext);
        this.strModuleType = strModuleType;
        this.strFileName = strFileName;
        this.delegate = delegate;
    }

    @Override
    protected File doInBackground(Uri... uris) {

        try{
            Context mContext = mContextRef.get();

            //writeFileContent
            InputStream mSelectedFileInputStream = mContext.getContentResolver().openInputStream(uris[0]);
            if (mSelectedFileInputStream != null){

                File mFile;
                if (strModuleType.equals(StorageUtils.DIRECT_DIRECTORY_CIF)){
                    mFile = new StorageUtils().createFileToAppSpecificDirCIF(mContext, strFileName);
                }else{
                    mFile = new StorageUtils().createFileToAppSpecificDir(mContext, strFileName);
                }

                OutputStream mSelectedFileOutputStream = new FileOutputStream(mFile.getAbsolutePath());
                byte[] buffer = new byte[1024];
                int len;
                while ((len = mSelectedFileInputStream.read(buffer)) > 0){
                    mSelectedFileOutputStream.write(buffer, 0, len);
                }
                mSelectedFileOutputStream.flush();
                mSelectedFileOutputStream.close();
                return mFile;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(File fileOutput) {
        delegate.fileLoadFinished(fileOutput);
        super.onPostExecute(fileOutput);
    }
}
