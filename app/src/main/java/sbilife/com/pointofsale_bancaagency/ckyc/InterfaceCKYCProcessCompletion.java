package sbilife.com.pointofsale_bancaagency.ckyc;

public interface InterfaceCKYCProcessCompletion {

    int CKYC_SEARCH_PROCESS = 1;
    int CKYC_DOWNLOAD_DETAILS_PROCESS = 2;
    int CKYC_UPLOAD_CKYC_DOC_PROCESS = 3;

    void onCKYCProcessComppletion(int processCount, boolean isProcessComplete, String Result);

}
