package sbilife.com.pointofsale_bancaagency.ekyc;

public class AppConstants {
	/** */
	public static final int SIMULATED_REFRESH_LENGTH = 2000;

	/**
	 * If DEBUG is FALSE then No Need to write Log File <strong>If Developer is
	 * in Debug Mode then he should change this variable to true otherwise make
	 * it false at the time of installation<strong>
	 */
	public static boolean DEBUG = true;

	/** It Will Decide Service Download For Module */
	public static boolean IS_MODULE = false;

	/** It will give info whether service is running or not */
	public static boolean IS_SERVICE_RUNNING = false;

	/** It will give module Name to download */
	public static String MODULE_NAME = "";

	/** Use In Service for Retrieving all running Module */
	public static String APP_BASE_PACKAGE = "com.alpha.bankconnectplus.";

	/** Single Activity object Initialized In Login Class */

	/** Single DBHelper object Initialized In Login Class */

}
