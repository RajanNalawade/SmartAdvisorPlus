package sbilife.com.pointofsale_bancaagency.ekyc.utilites;


import sbilife.com.pointofsale_bancaagency.R;

public class Global {
	// public static int EncryptionCertificateId =
	// R.raw.uidai_auth_encrypt_preprod;

	public static int EncryptionCertificateId_new = R.raw.uidai_auth_prod_new;

	public static int EncryptionCertificateId_old = R.raw.uidai_auth_prod_old;
	//public static int PreProdEncryptionCertificateId = R.raw.uidai_auth_encrypt_preprod;
	// public static String Url =
	// "http://cscstaging.e-kyc.in:3366/ECSAsaClientGatewaySample/ECSAsaGatewayService";
	// // Pre-Production
	public static String Url = "https://ekyc.sbilife.co.in/ekyc/ECSAsaGatewayService";

	public static String eKYC_PrePodUrl = "https://ekyc.sbilife.co.in/ekyc_V2/ECSAsaGatewayServiceV2";
	//public static String eKYC_PodUrl = "https://ekyc.sbilife.co.in/ekyc_V2/ECSAsaGatewayServiceV2";

	//new URL from 01-09-2018 for ekyc 2.5
	public static String eKYC_PodUrl = "https://ekyc.sbilife.co.in/ekyc_V25/ECSAsaGatewayServiceV25";

}
