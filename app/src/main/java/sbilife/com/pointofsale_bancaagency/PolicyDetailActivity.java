package sbilife.com.pointofsale_bancaagency;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolderPolicyDetail;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolderTopUp;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolderTotalPreUnpaid;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolderTotalUnUnpaid;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

@SuppressWarnings("deprecation")
public class PolicyDetailActivity extends AppCompatActivity {
	/*
	 * these are all global variables.
	 */

	private String strCIFBDMUserId = "";
	private String strCIFBDMEmailId = "";
	private String strCIFBDMPassword = "";
	private String strCIFBDMMObileNo = "";

	private TableLayout tblHistory;

	private ListView lstView;
	private ListView listViewTotalPaid;
	private ListView listViewTotalUnPaid;
	private ListView listViewTopUp;

	private SelectedAdapterPolicyDetail selectedAdapterPolicyDetail;
	private SelectedAdapterTotalPrePaid selectedAdapterTotalPrePaid;
	private SelectedAdapterTotalUnPaid selectedAdapterTotalUnPaid;
	private SelectedAdapterTopUp selectedAdapterTopUp;

	private final Context context = this;

	private DownloadFileAsyncPolicyList taskPolicyList;
	private  final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;

	private String strPolicyNo;
	private String UserId = "";

	private String strPolicyListErrorCOde;
	// TextView txttotalpolicy;

	private TextView txtproductname;
	private TextView txtpolicyholder;
	private TextView txtnominee;
	private TextView txtadddress;
	private TextView txtchannel;
	private TextView txtchannelid;
	private TextView txtcomment;
	private TextView txtnumero;
	private TextView txtproposal;
	private TextView txtfreq;
	private TextView txtsumassu;
	private TextView txtinstpre;
	private TextView txtDOC;
	private TextView txtpstatus;
	private TextView txtfupdate;
	private TextView txtonum;

	private TextView txtDOP;
	private TextView txtplace;
	private TextView txtuserid;
	private TextView txtdesc;

	TextView txtpaymentsummary;
	TextView txtservicinghist;

	private String strProductName;
	private String strPolicyHolder;
	private String strNominee;
	private String strAddress;
	private String strChannel;
	private String strChannelId;
	private String strComments;
	private String strNumero;
	private String strProposalNo;
	private String strFrequency;
	private String strSumAssured;
	private String strInstPre;
	private String strDOC;
	private String strStatus;
	private String strFupDate;
	private String strOnum;

	private String strDOP = "";
	private String strPlace = "";
	private String strUserId = "";
	private String strDesc = "";

	long lstPolicyListCount = 0;

	private Button btnpaymentsummary;
	private Button btnservicinghistory;

	private  final String NAMESPACE = "http://tempuri.org/";
	private  final String URl = ServiceURL.SERVICE_URL;

	private  final String METHOD_NAME_POLICY_LIST = "showPolicyDtls";

	// for agency service

	// private  final String SOAP_ACTION_POLICY_DETAIL_AGENCY =
	// "http://tempuri.org/getAgentPolicyDtls";
	// private  final String METHOD_NAME_POLICY_DETAIL_AGENCY =
	// "getAgentPolicyDtls";

	protected ListView lv;
	private CommonMethods mCommonMethods;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.policydetailview);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

		mCommonMethods = new CommonMethods();
		DatabaseHelper dbhelper = new DatabaseHelper(this);
		mCommonMethods.setApplicationToolbarMenu(this, "Policy Details");

		lstView = findViewById(R.id.listView11);

		listViewTotalPaid = findViewById(R.id.listViewTotalPaid);
		listViewTotalUnPaid = findViewById(R.id.listViewUnPaid);
		listViewTopUp = findViewById(R.id.listViewTopUp);

		// txttotalpolicy = (TextView)findViewById(R.id.txttotalpolicy);

		txtproductname = findViewById(R.id.txtproductname);
		txtpolicyholder = findViewById(R.id.txtpolicyholder);
		txtnominee = findViewById(R.id.txtnominee);
		txtadddress = findViewById(R.id.txtadddress);
		txtchannel = findViewById(R.id.txtchannel);
		txtchannelid = findViewById(R.id.txtchannelid);
		txtcomment = findViewById(R.id.txtcomment);
		txtnumero = findViewById(R.id.txtnumero);
		txtproposal = findViewById(R.id.txtproposal);
		txtfreq = findViewById(R.id.txtfreq);
		txtsumassu = findViewById(R.id.txtsumassu);
		txtinstpre = findViewById(R.id.txtinstpre);
		txtDOC = findViewById(R.id.txtDOC);
		txtpstatus = findViewById(R.id.txtpstatus);
		txtfupdate = findViewById(R.id.txtfupdate);
		txtonum = findViewById(R.id.txtonum);

		txtDOP = findViewById(R.id.txtDOP);
		txtplace = findViewById(R.id.txtplace);
		txtuserid = findViewById(R.id.txtuserid);
		txtdesc = findViewById(R.id.txtdesc);

		TextView txtcustinfo = findViewById(R.id.txtcustinfo);
		TextView txtpolicyinfo = findViewById(R.id.txtpolicyinfo);
		/*
		 * txtpaymentsummary = (TextView)findViewById(R.id.txtpaymentsummary);
		 * txtservicinghist = (TextView)findViewById(R.id.txtservicinghist);
		 */

		txtcustinfo.setText(Html.fromHtml("<u>Customer Information</u>"));
		txtpolicyinfo.setText(Html.fromHtml("<u>Policy Information</u>"));
		/*
		 * txtpaymentsummary.setText(Html.fromHtml("<u>Payment Summary</u>"));
		 * txtservicinghist.setText(Html.fromHtml("<u>Servicing History</u>"));
		 */

		tblHistory = findViewById(R.id.tblHistory);

		btnpaymentsummary = findViewById(R.id.btnpaymentsummary);
		btnservicinghistory = findViewById(R.id.btnservicinghistory);

		Intent i = getIntent();
		strPolicyNo = i.getStringExtra("PolicyNo");
		String userType = i.getStringExtra("strUserType");
		UserId = i.getStringExtra("strUserId");

		String agentCode = i.getStringExtra("strAgentCode");
		String email = i.getStringExtra("strEmail");
		String mobile = i.getStringExtra("strMobileNo");
		String password = i.getStringExtra("strPassword");

			try {
				strCIFBDMUserId = SimpleCrypto.decrypt("SBIL",
						dbhelper.GetCIFNo());
				strCIFBDMEmailId = SimpleCrypto.decrypt("SBIL",
						dbhelper.GetEmailId());
				strCIFBDMPassword = dbhelper.GetPassword();
				strCIFBDMMObileNo = SimpleCrypto.decrypt("SBIL",
						dbhelper.GetMobileNo());
			} catch (Exception e) {
				e.printStackTrace();
			}

			/*
			 * String strUserType = ""; try { strUserType =
			 * SimpleCrypto.decrypt("SBIL", dbhelper.GetUserType()); } catch
			 * (Exception e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */


		mProgressDialog = new ProgressDialog(this);
		taskPolicyList = new DownloadFileAsyncPolicyList();

		if (mCommonMethods.isNetworkConnected(context)) {
			// startDownloadAuth();
			service_hits();
		} else {
			intereneterror();
		}
	}

	/*
	 * it is download detail based on policy no from server.
	 */

	private void startDownloadAuth() {
		taskPolicyList.execute("demo");
	}

	private void service_hits() {

		mProgressDialog = new ProgressDialog(context,
				ProgressDialog.THEME_HOLO_LIGHT);
		String Message = "Loading Please wait...";
		mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>"
				+ Message + "<b></font>"));
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setMax(100);
		mProgressDialog.show();

		String METHOD_NAME_SH = "saveSmartAdvisorServiceHits";
		String SOAP_ACTION_SH = "http://tempuri.org/saveSmartAdvisorServiceHits";
		ServiceHits service = new ServiceHits(context, mProgressDialog,
				NAMESPACE, URl, SOAP_ACTION_SH, METHOD_NAME_SH);
		service.execute();

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:

			mProgressDialog = new ProgressDialog(context,
					ProgressDialog.THEME_HOLO_LIGHT);
			String Message = "Loading. Please wait...";
			mProgressDialog.setMessage(Html
					.fromHtml("<font color='#00a1e3'><b>" + Message
							+ "<b></font>"));
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(true);

			mProgressDialog.setButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							taskPolicyList.cancel(true);
							mProgressDialog.dismiss();
						}
					});

			mProgressDialog.setMax(100);
			mProgressDialog.show();
			return mProgressDialog;

		default:
			return null;
		}
	}

	/*
	 * this is async task which can get detail data of policy no from server
	 */

	class DownloadFileAsyncPolicyList extends AsyncTask<String, String, String> {

		private volatile boolean running = true;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		@Override
		protected String doInBackground(String... aurl) {
			try {

				running = true;
				SoapObject request = null;

				/*if (UserType.contentEquals("MAN")
						|| UserType.contentEquals("BDM")
						|| UserType.contentEquals("CIF")) {
					request = new SoapObject(NAMESPACE, METHOD_NAME_POLICY_LIST);
					request.addProperty("strPolicyNumber", strPolicyNo);
					request.addProperty("userId", UserId);
				} else {
					// request = new SoapObject(NAMESPACE,
					// METHOD_NAME_POLICY_DETAIL_AGENCY);
					

					
					 * request.addProperty("strAgentNo",AgentCode);
					 * request.addProperty("policyNo",strPolicyNo);
					 * request.addProperty("strEmailId",Email);
					 * request.addProperty("strMobileNo",Mobile);
					 * request.addProperty("strAuthKey",Password);
					 
				}*/
				
				request = new SoapObject(NAMESPACE, METHOD_NAME_POLICY_LIST);
				request.addProperty("strPolicyNumber", strPolicyNo);
				request.addProperty("userId", UserId);
				// request.addProperty("strPolicyNumber",strPolicyNo);

				mCommonMethods.TLSv12Enable();
				
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
				try {
					/*if (UserType.contentEquals("MAN")
							|| UserType.contentEquals("BDM")
							|| UserType.contentEquals("CIF")) {
						androidHttpTranport.call(SOAP_ACTION_POLICY_LIST,
								envelope);
					} else {
						// androidHttpTranport.call(SOAP_ACTION_POLICY_DETAIL_AGENCY,
						// envelope);
						
					}*/

					String SOAP_ACTION_POLICY_LIST = "http://tempuri.org/showPolicyDtls";
					androidHttpTranport.call(SOAP_ACTION_POLICY_LIST,
							envelope);
					Object response = envelope.getResponse();
					
					System.out.println("response.toString():"+response.toString());

					if (!response.toString().contentEquals("anyType{}")) {

						SoapPrimitive sa = null;
						try {
							sa = (SoapPrimitive) envelope.getResponse();

							String inputpolicylist = sa.toString();

							ParseXML prsObj = new ParseXML();

							/*if (UserType.contentEquals("MAN")
									|| UserType.contentEquals("BDM")
									|| UserType.contentEquals("CIF")) {
								inputpolicylist = prsObj.parseXmlTag(
										inputpolicylist, "CustDls");
							} else {
								// inputpolicylist =
								// prsObj.parseXmlTag(inputpolicylist,
								// "CIFPolicyList");
								
							}*/
							inputpolicylist = prsObj.parseXmlTag(
									inputpolicylist, "CustDls");
							// inputpolicylist =
							// prsObj.parseXmlTag(inputpolicylist,
							// "CIFPolicyList");
							inputpolicylist = new ParseXML().parseXmlTag(
									inputpolicylist, "ScreenData");
							strPolicyListErrorCOde = inputpolicylist;

							if (strPolicyListErrorCOde == null) {
								inputpolicylist = sa.toString();

								/*if (UserType.contentEquals("MAN")
										|| UserType.contentEquals("BDM")
										|| UserType.contentEquals("CIF")) {
									inputpolicylist = prsObj.parseXmlTag(
											inputpolicylist, "CustDls");
								} else {
									// inputpolicylist =
									// prsObj.parseXmlTag(inputpolicylist,
									// "CIFPolicyList");
									inputpolicylist = prsObj.parseXmlTag(
											inputpolicylist, "CustDls");
								}*/
								inputpolicylist = prsObj.parseXmlTag(
										inputpolicylist, "CustDls");
								// inputpolicylist =
								// prsObj.parseXmlTag(inputpolicylist,
								// "CIFPolicyList");
								inputpolicylist = prsObj.parseXmlTag(
										inputpolicylist,
										"CustomerPolicyDetails");

								strProductName = new ParseXML().parseXmlTag(
										inputpolicylist, "PRODUCT");
								strPolicyHolder = new ParseXML().parseXmlTag(
										inputpolicylist, "POLICYHOLDER");
								strNominee = new ParseXML().parseXmlTag(
										inputpolicylist, "NOMIEE");
								strAddress = new ParseXML().parseXmlTag(
										inputpolicylist, "ADDRESS");
								strChannel = new ParseXML().parseXmlTag(
										inputpolicylist, "CHANNEL");
								strChannelId = new ParseXML().parseXmlTag(
										inputpolicylist, "CAHNNELID");
								strComments = new ParseXML().parseXmlTag(
										inputpolicylist, "COMMENTS");
								strNumero = new ParseXML().parseXmlTag(
										inputpolicylist, "NUMERO");
								strProposalNo = new ParseXML().parseXmlTag(
										inputpolicylist, "PROPOSAL_NO");
								strFrequency = new ParseXML().parseXmlTag(
										inputpolicylist, "FREQUENCY");
								strSumAssured = new ParseXML().parseXmlTag(
										inputpolicylist, "SUM_ASSURED");
								strInstPre = new ParseXML().parseXmlTag(
										inputpolicylist, "INST_PREM");
								strDOC = new ParseXML().parseXmlTag(
										inputpolicylist, "DOC");
								strStatus = new ParseXML().parseXmlTag(
										inputpolicylist, "STATUS");
								strFupDate = new ParseXML().parseXmlTag(
										inputpolicylist, "FUP_DATE");
								strOnum = new ParseXML().parseXmlTag(
										inputpolicylist, "O__NUM");

								inputpolicylist = sa.toString();
								inputpolicylist = prsObj.parseXmlTag(
										inputpolicylist, "CustDls");
								List<String> Node = prsObj.parseParentNode(
										inputpolicylist, "PolicyDetails");

								List<XMLHolderPolicyDetail> nodeData = prsObj
										.parseNodeElementPolicyDetail(Node);

								// final List<XMLHolder> lst;
								List<XMLHolderPolicyDetail> lstPolicyDetail = new ArrayList<XMLHolderPolicyDetail>();
								lstPolicyDetail.clear();

								for (XMLHolderPolicyDetail node : nodeData) {

									lstPolicyDetail.add(node);
								}

								selectedAdapterPolicyDetail = new SelectedAdapterPolicyDetail(
										context, 0, lstPolicyDetail);
								selectedAdapterPolicyDetail
										.setNotifyOnChange(true);

								// paid premium

								inputpolicylist = sa.toString();
								inputpolicylist = prsObj.parseXmlTag(
										inputpolicylist, "CustDls");
								List<String> NodeTot = prsObj.parseParentNode(
										inputpolicylist, "TotalPremiumpaidDT");

								List<XMLHolderTotalPreUnpaid> nodeDataTot = prsObj
										.parseNodeElementTotalPre(NodeTot);

								// final List<XMLHolder> lst;
								List<XMLHolderTotalPreUnpaid> lstTotalUnpaidPre = new ArrayList<XMLHolderTotalPreUnpaid>();
								lstTotalUnpaidPre.clear();

								for (XMLHolderTotalPreUnpaid node : nodeDataTot) {

									lstTotalUnpaidPre.add(node);
								}

								selectedAdapterTotalPrePaid = new SelectedAdapterTotalPrePaid(
										context, 0, lstTotalUnpaidPre);
								selectedAdapterTotalPrePaid
										.setNotifyOnChange(true);

								// unpaid premium

								inputpolicylist = sa.toString();
								inputpolicylist = prsObj.parseXmlTag(
										inputpolicylist, "CustDls");
								List<String> NodeTotUnPaid = prsObj
										.parseParentNode(inputpolicylist,
												"TotalTopuppaidDT");

								List<XMLHolderTotalUnUnpaid> nodeDataTotUnPaid = prsObj
										.parseNodeElementTotalUnPaid(NodeTotUnPaid);

								// final List<XMLHolder> lst;
								List<XMLHolderTotalUnUnpaid> lstTotalUnPaid = new ArrayList<XMLHolderTotalUnUnpaid>();
								lstTotalUnPaid.clear();

								for (XMLHolderTotalUnUnpaid node : nodeDataTotUnPaid) {

									lstTotalUnPaid.add(node);
								}

								selectedAdapterTotalUnPaid = new SelectedAdapterTotalUnPaid(
										context, 0, lstTotalUnPaid);
								selectedAdapterTotalUnPaid
										.setNotifyOnChange(true);

								// top up

								inputpolicylist = sa.toString();
								inputpolicylist = prsObj.parseXmlTag(
										inputpolicylist, "CustDls");
								List<String> NodeTotUp = prsObj
										.parseParentNode(inputpolicylist,
												"LastTopUpDT");

								List<XMLHolderTopUp> nodeDataTotUp = prsObj
										.parseNodeElementTopUp(NodeTotUp);

								List<XMLHolderTopUp> lstTopUp = new ArrayList<XMLHolderTopUp>();
								lstTopUp.clear();

								for (XMLHolderTopUp node : nodeDataTotUp) {

									lstTopUp.add(node);
								}

								selectedAdapterTopUp = new SelectedAdapterTopUp(
										context, 0, lstTopUp);
								selectedAdapterTopUp.setNotifyOnChange(true);

								inputpolicylist = sa.toString();
								inputpolicylist = prsObj.parseXmlTag(
										inputpolicylist, "CustDls");
								inputpolicylist = prsObj.parseXmlTag(
										inputpolicylist, "RequestHistory");

								if (inputpolicylist != null) {

									strDOP = new ParseXML().parseXmlTag(
											inputpolicylist, "DOP");
									strPlace = new ParseXML().parseXmlTag(
											inputpolicylist, "PLACE");
									strUserId = new ParseXML().parseXmlTag(
											inputpolicylist, "USERID");
									strDesc = new ParseXML().parseXmlTag(
											inputpolicylist, "DESCRIPTION");
								} else // added by Akshaya
								{
									strDOP = "NA";
									strPlace = "NA";
									strUserId = "NA";
									strDesc = "NA";
								}

							} else {
								// txterrordesc.setText("No Data");
							}

						} catch (Exception e) {
							try {
								throw (e);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							mProgressDialog.dismiss();
							running = false;
						}
					} else {

					}

				} catch (IOException e) {
					try {
						throw (e);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					mProgressDialog.dismiss();
					running = false;
				} catch (XmlPullParserException e) {
					try {
						throw (e);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					mProgressDialog.dismiss();
					running = false;
				}
			} catch (Exception e) {
				try {
					throw (e);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				mProgressDialog.dismiss();
				running = false;
			}
			return null;

		}

		@Override
		protected void onProgressUpdate(String... progress) {
			Log.d("ANDRO_ASYNC", progress[0]);
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		@Override
		protected void onPostExecute(String unused) {

			try {
				dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (running) {
				if (strPolicyListErrorCOde == null) {
					txtproductname.setText(strProductName);
					txtpolicyholder.setText(strPolicyHolder);
					txtnominee.setText(strNominee);
					txtadddress.setText(strAddress);
					txtchannel.setText(strChannel);
					txtchannelid.setText(strChannelId);
					txtcomment.setText(strComments);
					txtnumero.setText(strNumero);
					txtproposal.setText(strProposalNo);
					txtfreq.setText(strFrequency);
					txtsumassu.setText(strSumAssured);
					txtinstpre.setText(strInstPre);
					txtDOC.setText(strDOC);
					txtpstatus.setText(strStatus);
					txtfupdate.setText(strFupDate);
					txtonum.setText(strOnum);

					txtDOP.setText(strDOP);
					txtplace.setText(strPlace);
					txtuserid.setText(strUserId);
					txtdesc.setText(strDesc);

					lstView.setAdapter(selectedAdapterPolicyDetail);
					listViewTotalPaid.setAdapter(selectedAdapterTotalPrePaid);
					listViewTotalUnPaid.setAdapter(selectedAdapterTotalUnPaid);
					listViewTopUp.setAdapter(selectedAdapterTopUp);

					setListViewPolicyDetail(lstView);
					Utility.setListViewHeightBasedOnChildren(listViewTotalPaid);
					Utility.setListViewHeightBasedOnChildren(listViewTotalUnPaid);
					Utility.setListViewHeightBasedOnChildren(listViewTopUp);
				} else {
					/*
					 * List<XMLHolderPolicyDetail> lst; XMLHolderPolicyDetail
					 * node = null; lst = new
					 * ArrayList<XMLHolderPolicyDetail>(); lst.clear();
					 * lst.add(node); selectedAdapterPolicyDetail = new
					 * SelectedAdapterPolicyDetail(context,0,lst);
					 * selectedAdapterPolicyDetail.setNotifyOnChange(true);
					 * lstView.setAdapter(selectedAdapterPolicyDetail);
					 * 
					 * //paid
					 * 
					 * List<XMLHolderTotalPreUnpaid> lstTotal;
					 * XMLHolderTotalPreUnpaid nodeTotal = null; lstTotal = new
					 * ArrayList<XMLHolderTotalPreUnpaid>(); lstTotal.clear();
					 * lstTotal.add(nodeTotal); selectedAdapterTotalPrePaid =
					 * new SelectedAdapterTotalPrePaid(context,0,lstTotal);
					 * selectedAdapterTotalPrePaid.setNotifyOnChange(true);
					 * listViewTotalPaid
					 * .setAdapter(selectedAdapterTotalPrePaid);
					 * 
					 * //unpaid
					 * 
					 * List<XMLHolderTotalUnUnpaid> lstTotalUnPaid;
					 * XMLHolderTotalUnUnpaid nodeTotalUnPaid = null;
					 * lstTotalUnPaid = new ArrayList<XMLHolderTotalUnUnpaid>();
					 * lstTotalUnPaid.clear();
					 * lstTotalUnPaid.add(nodeTotalUnPaid);
					 * selectedAdapterTotalUnPaid = new
					 * SelectedAdapterTotalUnPaid(context,0,lstTotalUnPaid);
					 * selectedAdapterTotalUnPaid.setNotifyOnChange(true);
					 * listViewTotalUnPaid
					 * .setAdapter(selectedAdapterTotalUnPaid);
					 * 
					 * //top up
					 * 
					 * List<XMLHolderTopUp> lstTopUp; XMLHolderTopUp nodeTopUp =
					 * null; lstTopUp = new ArrayList<XMLHolderTopUp>();
					 * lstTopUp.clear(); lstTopUp.add(nodeTopUp);
					 * selectedAdapterTopUp = new
					 * SelectedAdapterTopUp(context,0,lstTopUp);
					 * selectedAdapterTopUp.setNotifyOnChange(true);
					 * listViewTopUp.setAdapter(selectedAdapterTopUp);
					 */
				}
			} else {
				WeakReference<PolicyDetailActivity> mainActivityWeakRef = new WeakReference<PolicyDetailActivity>(PolicyDetailActivity.this);
				  
				  if (mainActivityWeakRef.get() != null && !mainActivityWeakRef.get().isFinishing()) {
					  servererror();
				  }
			}
		}
	}

	class ServiceHits extends AsyncTask<String, Void, String> {
		final Context mContext;
		ProgressDialog progressDialog = null;
		String NAMESPACE = "";
		String URL = "";
		String SOAP_ACTION = "";
		String METHOD_NAME = "";

		ServiceHits(Context mContext, ProgressDialog progressDialog,
					String NAMESPACE, String URL, String SOAP_ACTION,
					String METHOD_NAME) {
			// TODO Auto-generated constructor stub

			this.NAMESPACE = NAMESPACE;
			this.URL = URL;
			this.SOAP_ACTION = SOAP_ACTION;
			this.METHOD_NAME = METHOD_NAME;
			this.mContext = mContext;
			this.progressDialog = progressDialog;
		}

		@Override
		protected String doInBackground(String... param) {
			// TODO Auto-generated method stub

			if (mCommonMethods.isNetworkConnected(context)) {

				try {

					SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

					request.addProperty("serviceName", METHOD_NAME_POLICY_LIST);
					request.addProperty("strProdCode", "");
					request.addProperty("serviceInput", strPolicyNo);
					request.addProperty("serviceReqUserId", strCIFBDMUserId);
					request.addProperty("strEmailId", strCIFBDMEmailId);
					request.addProperty("strMobileNo", strCIFBDMMObileNo);
					request.addProperty("strAuthKey", strCIFBDMPassword.trim());

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);
					// Enable this envelope if service is written in dot net
					envelope.dotNet = true;
					envelope.setOutputSoapObject(request);
					HttpTransportSE androidHttpTransport = new HttpTransportSE(
							URL);

					mCommonMethods.TLSv12Enable();
					
					StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
							.permitAll().build();
					StrictMode.setThreadPolicy(policy);

					androidHttpTransport.call(SOAP_ACTION, envelope);
					SoapPrimitive response = (SoapPrimitive) envelope
							.getResponse();

					String result = response.toString();

					if (result.contains("1")) {
						return "Success";
					} else {
						return "Failure";
					}

				} catch (Exception e) {
					return "Server not Found. Please try after some time.";
				}

			} else
				return "Please Activate Internet connection";

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			try {
				if (progressDialog.isShowing())
					progressDialog.dismiss();

			} catch (Exception e) {
				e.getMessage();
			}

			taskPolicyList = new DownloadFileAsyncPolicyList();
			startDownloadAuth();

		}

	}

	/*
	 * if Internet connection not present in device then it will show the alert.
	 */

	private void intereneterror() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.loading_window);
		TextView text = dialog.findViewById(R.id.txtalertheader);
		text.setText("Internet Connection Not Present,Try again..");
		Button dialogButton = dialog.findViewById(R.id.btnalert);
		dialogButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/*
	 * while downloading data from server,if any data failed or not there then
	 * it will show alert
	 */

	private void servererror() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.loading_window);
		TextView text = dialog.findViewById(R.id.txtalertheader);
		text.setText("Server Not Responding,Try again..");
		Button dialogButton = dialog.findViewById(R.id.btnalert);
		dialogButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/*
	 * it is base adapter bind with policy detail listview
	 */

	class SelectedAdapterPolicyDetail extends
			ArrayAdapter<XMLHolderPolicyDetail> {
		private int selectedPos = -1;

		final List<XMLHolderPolicyDetail> lst;

		SelectedAdapterPolicyDetail(Context context,
									int textViewResourceId, List<XMLHolderPolicyDetail> objects) {
			super(context, textViewResourceId, objects);

			lst = objects;
		}

		public void setSelectedPosition(int pos) {
			selectedPos = pos;
			notifyDataSetChanged();
		}

		public int getSelectedPosition() {
			return selectedPos;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) this.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.view_policy_detail, null);
			}

			TextView txtridername = v
					.findViewById(R.id.txtridername);
			TextView txtsumassured = v
					.findViewById(R.id.txtsumassured);
			TextView txtriskpremium = v
					.findViewById(R.id.txtriskpremium);
			TextView txtpremiumterm = v
					.findViewById(R.id.txtpremiumterm);
			TextView txtbenefitterm = v
					.findViewById(R.id.txtbenefitterm);

			Object obj = null;
			boolean i = lst.contains(obj);

			if (!i) {
				txtridername.setText(lst.get(position).getRiderName());
				txtsumassured.setText(lst.get(position).getSumAssured());
				txtriskpremium.setText(lst.get(position).getRiskPre());
				txtpremiumterm.setText(lst.get(position).getPremiumTerm());
				txtbenefitterm.setText(lst.get(position).getBenefitTerm());

			}

			return (v);
		}
	}

	/*
	 * it is base adapter bind with total premium listview
	 */

	class SelectedAdapterTotalPrePaid extends
			ArrayAdapter<XMLHolderTotalPreUnpaid> {
		private int selectedPos = -1;

		final List<XMLHolderTotalPreUnpaid> lst;

		SelectedAdapterTotalPrePaid(Context context,
									int textViewResourceId, List<XMLHolderTotalPreUnpaid> objects) {
			super(context, textViewResourceId, objects);

			lst = objects;
		}

		public void setSelectedPosition(int pos) {
			selectedPos = pos;
			notifyDataSetChanged();
		}

		public int getSelectedPosition() {
			return selectedPos;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) this.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.totalpaidpremium, null);
			}

			TextView txtpaid = v.findViewById(R.id.txttotalprepaid);
			TextView txtunpaid = v
					.findViewById(R.id.txtlastunpaiddate);

			Object obj = null;
			boolean i = lst.contains(obj);

			if (!i) {
				txtpaid.setText(lst.get(position).getTotalPaid());
				txtunpaid.setText(lst.get(position).getLastUnpaid());
			}

			return (v);
		}
	}

	/*
	 * it is base adapter bind with total unpaid premium listview
	 */

	class SelectedAdapterTotalUnPaid extends
			ArrayAdapter<XMLHolderTotalUnUnpaid> {
		private int selectedPos = -1;

		final List<XMLHolderTotalUnUnpaid> lst;

		SelectedAdapterTotalUnPaid(Context context,
								   int textViewResourceId, List<XMLHolderTotalUnUnpaid> objects) {
			super(context, textViewResourceId, objects);

			lst = objects;
		}

		public void setSelectedPosition(int pos) {
			selectedPos = pos;
			notifyDataSetChanged();
		}

		public int getSelectedPosition() {
			return selectedPos;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) this.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.totalunpaidpremium, null);
			}

			TextView txtpaid = v
					.findViewById(R.id.txttotalunprepaid);
			TextView txtunpaid = v
					.findViewById(R.id.txtlastpreunpaiddate);

			Object obj = null;
			boolean i = lst.contains(obj);

			if (!i) {
				txtpaid.setText(lst.get(position).getTotalPaid());
				txtunpaid.setText(lst.get(position).getLastUnpaid());
			}

			return (v);
		}
	}

	/*
	 * it is base adapter bind with top up listview
	 */

	class SelectedAdapterTopUp extends ArrayAdapter<XMLHolderTopUp> {
		private int selectedPos = -1;

		final List<XMLHolderTopUp> lst;

		SelectedAdapterTopUp(Context context, int textViewResourceId,
							 List<XMLHolderTopUp> objects) {
			super(context, textViewResourceId, objects);

			lst = objects;
		}

		public void setSelectedPosition(int pos) {
			selectedPos = pos;
			notifyDataSetChanged();
		}

		public int getSelectedPosition() {
			return selectedPos;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) this.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.topuppremium, null);
			}

			TextView txtlasttopup = v
					.findViewById(R.id.txtlasttopup);
			TextView txtlasttopupdate = v
					.findViewById(R.id.txtlasttopupdate);

			Object obj = null;
			boolean i = lst.contains(obj);

			if (!i) {
				txtlasttopup.setText(lst.get(position).getLastTopUp());
				txtlasttopupdate.setText(lst.get(position).getLastTopUpDate());
			}

			return (v);
		}
	}

	/*
	 * based on button it will show view
	 */

	public void clik_payment_summary(View v) {
		btnpaymentsummary.setBackgroundResource(R.drawable.exp_selected);
		btnservicinghistory.setBackgroundResource(R.drawable.exp_unselected);

		listViewTotalPaid.setVisibility(View.VISIBLE);
		listViewTotalUnPaid.setVisibility(View.VISIBLE);
		listViewTopUp.setVisibility(View.VISIBLE);

		tblHistory.setVisibility(View.GONE);
	}

	/*
	 * based on button it will show view
	 */

	public void clik_servicing_history(View v) {
		btnpaymentsummary.setBackgroundResource(R.drawable.exp_unselected);
		btnservicinghistory.setBackgroundResource(R.drawable.exp_selected);

		tblHistory.setVisibility(View.VISIBLE);

		listViewTotalPaid.setVisibility(View.GONE);
		listViewTotalUnPaid.setVisibility(View.GONE);
		listViewTopUp.setVisibility(View.GONE);
	}


	private  void setListViewPolicyDetail(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
			//totalHeight += listItem.getMeasuredHeight()/6;
			totalHeight += listItem.getMeasuredHeight()/12;
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}
}
