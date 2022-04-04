package sbilife.com.pointofsale_bancaagency.agent_on_boarding;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import org.ksoap2.serialization.SoapObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cropper.CropImage;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.AsyncUploadFile_Common;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.CompressImage;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;

public class ActivityAOBTermsConditionsDeclaration extends AppCompatActivity implements View.OnClickListener,
        AsyncUploadFile_Common.Interface_Upload_File_Common {

    public final String FILE_NAME = "AGENT_FORM";
    public final String FILE_NAME_IA_UPGRADE = "IA_UPGRADE_AGENT_FORM";
    public final String APPLICANT_PHOTO_FILE_NAME = "applicant_photo";
    public final String APPLICANT_SIGNATURE_FILE_NAME = "applicant_signature";

    private final String NAMESPACE = "http://tempuri.org/";

    private final String METHOD_NAME_UPLOAD_ALL_AOB_DOC = "UploadFile_AgentEnroll";

    private final int REQUEST_CODE_CAPTURE_DOCUMENT = 200;

    private StorageUtils mStorageUtils;
    private CommonMethods mCommonMethods;
    private Context mContext;
    private DatabaseHelper db;
    private TextView txt_aob_terms_and_conditions, txt_aob_applicant_declaration_date, txt_aob_nominee_conditions;
    private CheckBox chkbox_aob_terms_conditions_agreed, chkbox_aob_applicant_declaration_1, chkbox_aob_applicant_declaration_2,
            chkbox_aob_applicant_declaration_3, chkbox_aob_applicant_declaration_4, chkbox_aob_applicant_declaration_5,
            chkbox_aob_applicant_declaration_6, chkbox_aob_applicant_declaration_7, chkbox_aob_nominee_conditions_agreed;
    private EditText edt_aob_applicant_declaration_place;
    private Button btn_aob_terms_conditions_submit, btn_aob_terms_conditions_back;
    private ImageButton imgbtn_aob_applicant_declaration_sign, imgbtn_aob_applicant_declaration_photo;
    private String str_signature_applicant = "", str_pan_no = "", str_photo_applicant = "",
            str_doc_type = "", str_data_save_err = "",
            str_app_created_date = "", str_training_end_date = "", strCIFBDMUserId = "",
            strCIFBDMEmailId = "", strCIFBDMMObileNo = "", str_applicant_mobile = "", str_capture_doc_type = "";
    private StringBuilder str_terms_conditions;
    private File agentFormFile, mApplicantPhoto, mApplicantSign;
    private AsyncUploadFile_Common mAsyncUploadFileCommon;

    private ProgressDialog mProgressDialog;
    private boolean is_dashboard = false, is_back_pressed = false, is_ia_upgrade = false, is_bsm_questions = false;
    private ParseXML mParseXML;
    private Calendar mCalender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_aob_terms_conditions_declaration);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cifenrollment_pf_activity_title);

        if (getIntent().hasExtra("is_dashboard"))
            is_dashboard = getIntent().getBooleanExtra("is_dashboard", false);

        if (getIntent().hasExtra("is_bsm_questions"))
            is_bsm_questions = getIntent().getBooleanExtra("is_bsm_questions", false);

        if (getIntent().hasExtra("is_ia_upgrade"))
            is_ia_upgrade = getIntent().getBooleanExtra("is_ia_upgrade", false);

        initialisation();

        //non editable with no saving
        //editable
        enableDisableAllFields(!is_dashboard && !is_bsm_questions);
    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = mCommonMethods.setUserDetails(mContext);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    public void initialisation() {

        mContext = this;
        mCommonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        mCalender = Calendar.getInstance();

        mCommonMethods.setApplicationToolbarMenu1(this, "Agent on Boarding");

        db = new DatabaseHelper(mContext);

        getUserDetails();

        mParseXML = new ParseXML();

        txt_aob_terms_and_conditions = (TextView) findViewById(R.id.txt_aob_terms_and_conditions);
        txt_aob_applicant_declaration_date = (TextView) findViewById(R.id.txt_aob_applicant_declaration_date);
        txt_aob_nominee_conditions = (TextView) findViewById(R.id.txt_aob_nominee_conditions);

        chkbox_aob_terms_conditions_agreed = (CheckBox) findViewById(R.id.chkbox_aob_terms_conditions_agreed);
        chkbox_aob_applicant_declaration_1 = (CheckBox) findViewById(R.id.chkbox_aob_applicant_declaration_1);
        chkbox_aob_applicant_declaration_2 = (CheckBox) findViewById(R.id.chkbox_aob_applicant_declaration_2);
        chkbox_aob_applicant_declaration_3 = (CheckBox) findViewById(R.id.chkbox_aob_applicant_declaration_3);
        chkbox_aob_applicant_declaration_4 = (CheckBox) findViewById(R.id.chkbox_aob_applicant_declaration_4);
        chkbox_aob_applicant_declaration_5 = (CheckBox) findViewById(R.id.chkbox_aob_applicant_declaration_5);
        chkbox_aob_applicant_declaration_6 = (CheckBox) findViewById(R.id.chkbox_aob_applicant_declaration_6);
        chkbox_aob_applicant_declaration_7 = (CheckBox) findViewById(R.id.chkbox_aob_applicant_declaration_7);

        chkbox_aob_nominee_conditions_agreed = (CheckBox) findViewById(R.id.chkbox_aob_nominee_conditions_agreed);

        edt_aob_applicant_declaration_place = (EditText) findViewById(R.id.edt_aob_applicant_declaration_place);

        btn_aob_terms_conditions_submit = (Button) findViewById(R.id.btn_aob_terms_conditions_submit);
        btn_aob_terms_conditions_submit.setOnClickListener(this);

        btn_aob_terms_conditions_back = (Button) findViewById(R.id.btn_aob_terms_conditions_back);
        btn_aob_terms_conditions_back.setOnClickListener(this);

        imgbtn_aob_applicant_declaration_sign = (ImageButton) findViewById(R.id.imgbtn_aob_applicant_declaration_sign);
        imgbtn_aob_applicant_declaration_sign.setOnClickListener(this);

        imgbtn_aob_applicant_declaration_photo = (ImageButton) findViewById(R.id.imgbtn_aob_applicant_declaration_photo);
        imgbtn_aob_applicant_declaration_photo.setOnClickListener(this);

        String str_nominee_conditions = "<pre>&nbsp &nbsp &nbsp(a) A Nomination will not be effective unless it is communicated to SBI Life Insurance Co Ltd and registered by SBI Life Insurance Co Ltd</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(b) Any change in Nomination shall be duly informed to SBI Life Insurance Co Ltd and registered by it in its records. SBI Life Insurance Co Ltd shall not be liable in any manner whatsoever for payment made to the registered nominees as per its records.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(c) By registering a nomination , SBI Life Insurance Co Ltd makes no admission and expresses no opinion whatsoever as to the validity or effect of the nomination , it being understood that the parties satisfy themselves as to the form of nomination and all other points relating to the nomination before sending it to SBI Life Insurance Co Ltd .</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(d) By registering a nomination, SBI Life Insurance Co Ltd does not Admit any liability for payment of commission or any other sum in the event of death of the Insurance Advisor.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(e) The liability of SBI Life Insurance Co Ltd for payment of commission or any other sum in the event of death of the Insurance Advisor would be determined solely by the fact of whether the said Insurance Advisor is qualified for such payment in accordance with the laws in force in India and the applicable regulations at the time of his/ her death</pre><br/>";

        String str_terms_and_conditions = "<pre>The Insurance Agent of the SBI LIFE INSURANCE COMPANY LTD, on his appointment as the Insurance Agent of the Company, has to strictly adhere to the terms and conditions as laid down below and also as amended from time to time by the Company.</pre><br/><br/>"
                + "<pre>The SBI LIFE INSURANCE COMPANY LTD, [hereinafter called as the Company], incorporated under the Companies Act, 1956 for carrying on the business of life insurance and is desirous of obtaining the services of the agent for the purpose of soliciting and procuring life insurance business for the Company and for performing such other functions which are incidental to his acting as an Insurance agent and as may be entrusted to him by the Company from time to time, has framed the following terms and conditions to be strictly followed by its Insurance Agents.</pre><br/><br/>"
                + "<pre>The Agent, who holds a valid appointment letter to act as an Insurance Agent of the Company, has to strictly follow the following terms and conditions:</pre><br/><br/>"
                + "<b>1. Definations</b><br/><br/>"
                + "<pre>The following words and expressions used in terms & conditions shall have the meaning assigned to them as under and any words or expressions used herein but not defined shall have the meaning assigned to them in the Insurance Act, 1938, the Insurance Regulatory and Development Authority Act, 1999 or the rules and regulations made under those enactments.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp(a) “Act” means the Insurance Act, 1938;</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(b) “Unit Manager” means the employee of the Company under whose supervision and control the Agent is for the time being placed;</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(c) “Agent” means the person appointed to act as an Insurance agent of the Company in terms of this Agreement;</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(d) “Company” means the SBI Life Insurance Company Limited;</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(e) “IRDAI” means the Insurance Regulatory and Development Authority of India established under the IRDA Act; and</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(f) “IRDA Act” means the Insurance Regulatory and Development Authority Act, 1999 (41 of 1999).</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(g) “Rules and Regulations” means the rules and regulations made under the Act and the IRDA Act and include the orders, directives, circulars, guidelines and notifications issued by the IRDAI from time to time.</pre><br/><br/>"
                + "<b>2. Appointment</b><br/><br/>"
                + "<pre>The appointment of Insurance Agents shall be subject to the review conducted by SBI Life Insurance Co. Ltd from time to time, except for termination due to fraud or non-performance.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp(a) The terms and conditions shall govern the appointment of an Agent for the purposes of soliciting and procuring life insurance business (“business”) for the Company in such areas as may be determined by the Company and notified to the Agent and to perform such other functions as are incidental to his acting as an Insurance Agent and as may be entrusted to him by the Company from time to time. The Agent has to accept the said appointment and the terms and conditions governing such appointment and the agent will be designated as “Insurance Agent”.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(b) The Agent shall not solicit or procure business from person resident outside of the area notified to him/her.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(c) The appointment of the Agent in the area mentioned in clause 2(a) shall not preclude the Company from appointing any other person or persons to act as insurance Agent or Corporate Agent of the Company in such area; nor shall such appointment abridge or derogate from the obligations of the Agent under the terms & conditions laid down here.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(d) The Company may, at its sole discretion, decide on the Insurance products for which the Agent may solicit and procure proposals and to change, add or discontinue any such product at any time.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(e) Agency appointment may be refused to the applicant if he/she does not fulfill any of the conditions prescribed for appointment of insurance agents.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(f) An applicant aggrieved by the decision of refusal to grant agency appointment, may submit a review application to VP – Channel Administration for review of the decision.</pre><br/><br/>"
                + "<b>3. Obligations of the Agent</b><br/><br/>"
                + "<pre>During the tenure the Agent shall serve the Company as an Insurance Agent with all due and proper diligence, act dutifully and in good faith, observe all instructions given by the Unit Manager or generally by the Company to all it’s Insurance Agents, as to his activities as an Insurance Agent of the Company, and use his best endeavors in the discharge of his obligations under these terms and conditions.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp(a) Without prejudice to the generality of the foregoing and, in particular, the Agent shall fulfill such minimum business requirements an d p er s is t enc y c r it er ia as may be prescribed by the Company from time to time, in the matter of procurement of new life insurance business, premium income, number of lives to be assured, number of policies to be sold or in the matter of conversation and preservation of business or in relation to policy- servicing. Agent’s appointment is liable for termination for not meeting the requirements/criteria as may be defined and communicated by the Company from time to time.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp(b) In order to satisfactorily discharge his obligations under these terms and conditions, the Agent shall fully familiarize himself with all the insurance products offered for sale by the Company including the rate of premium charged for each such product, and the policies and practices followed by the Company in the matter of customer service and conservation of business.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp(c) The Agent shall not allow or offer to allow, either directly or indirectly, as an inducement to any person to take out or renew or continue an insurance policy, any rebate of the whole or part of the commission payable to him or otherwise, except as may be allowed under the Rules and Regulations, of the premium shown on the policy.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp(d) The Agent shall familiarize himself with all the statutory requirements as may relate to the discharge of his obligations as an Insurance Agent and shall fully conform to, comply with and observe and follow such requirements and in the event of his/her failure to do so, the Agent shall be personally liable for all consequences thereto and shall also indemnify the Company in respect of any loss or injury caused to or suffered by it as a result of such failure.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp(e) The Code of Conduct as prescribed by Clause VIII of the IRDAI Guidelines on Appointment of Insurance Agents, 2015 dated March 16, 2015 and/or as amended from time to time, any statutory modification or substitution thereof and any Code of Conduct as prescribed by the Company shall be deemed to have been set out in and be part of these terms and conditions. The Agent shall scrupulously follow and comply with the same. The Code of Conduct is incorporated in the letter of appointment issued to you by the Company.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp(f) Without prejudice to the generality of the obligations under Clause 3.5, and in particular the Agent shall strictly observe, conform to and comply with the, requirements in relation to customer service, insurance advertisements and on any matter relating to Insurance Agents as prescribed by the Rules & Regulations for insurance agents, from time to time.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp(g) All communications and all information whether written, verbal or visual and all other material supplied to or obtained by the Agent in the course of or as a result of the discharge of his obligations under these terms and conditions and all information in relation to any particular assurance obtained by him as Agent shall be treated by the Agent as confidential and shall not be disclosed by him to any third party without prior written consent of the Company.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp(h) The Agent shall hold in trust for the company any application, report, form, security or other property received by him in the course of his duties as the Agent of the Company and shall, dispatch, makeover to the Company all such applications, reports, forms, securities or other properties within twenty four hours of such receipt excluding public holidays. The Agent shall not have the authority, either expressed or implied to receive any payment except the premium towards proposal deposit from the public on behalf of the Company. If, however, the agent receives any payment whatsoever, the same shall be remitted to the company within 24 hours of its receipt. Under no circumstances, shall the payment made to the agent be deemed to be a payment made to the Company.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp(i) The Agent shall attend all meetings, conventions, workshops or the like called for by the Company and shall also observe, follow and comply with any directive or direction, issued by the Company generally in relation to its insurance agents or with particular reference to the Agent.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp(j) The Agent shall keep true, full and correct records of all the transactions entered into by him as Agent of the company, and if so required by the Company, furnish for its inspection (including making of copies thereof) all or any such records, early.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp(k) During the continuance of his Agency, the Agent will engage in activities as an Insurance Agent exclusively of the Company and not of any other insurer carrying on life insurance business or in soliciting or selling of even other than insurance products or products, which are similar to or identical to the insurance products of the Company. The Agent also undertakes not to have any tie up with any other insurer as an employee or director or promoter and not to hold any insurance activity related license or hold any position with any Corporate Agent/ Broker/ TPA / Insurance Marketing Firm/ any other insurance intermediary.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp(l) The Agent has no authority to and shall not take part in any dispute or institute or defend any proceedings or settle or attempt to settle or make any admission concerning any dispute, proceeding or other claim relating to the business or affairs of the Company except with the prior written permission of the Company.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp(m) If so required by the Company, the Agent shall open and maintain a bank account in his own name (only) to which the Company may credit the commission, remuneration or reward or any other amount due and payable to the Agent. All the expenses for opening and maintaining the account shall be borne by the Agent.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp(n) The Agent shall indemnify and keep indemnified the Company against loss or injury or costs or expenses suffered or incurred by the Company in consequence of any act or omission of the Agent tantamounting to a breach of these terms and conditions.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp(o) Nothing in these terms and conditions or otherwise shall entitle the Agent to make any representation or warranties on behalf of the Company or to enter into any contract or agreement on behalf of the Company or bind the Company in any manner whatsoever by his/her acts of commission or omission.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp(p) T he Agent covenants with the Company that he is qualified to act as an Insurance Agent under law and does not suffer any disqualification rendering invalid his appointment as Agent.</pre><br/><br/>"
                + "<b>4. Remuneration</b><br/><br/>"
                + "<pre>As consideration for soliciting and procuring insurance business for the Company the Agent shall be paid by way of commission / remuneration on such scale as may be notified by the Company from time to time subject to IRDAI Regulations or Guidelines issued and/or the Company’s Board approved policy for ‘Appointment of Insurance Agents’.</pre><br/><br/>"
                + "<pre>The commission / remuneration shall be payable to the Agent only for life insurance business procured by him which results in policies issued by the Company and not in respect of any incomplete or invalid transaction or proposal.</pre><br/><br/>"
                + "<pre>Subject to the Rules and Regulations, The Act and the IRDA Act, the Company shall have absolute freedom to revise the scale of commission in respect of one or more of the insurance products offered for sale by the Company, provided that the Agent shall be given notice of not less than 30 days of such revision through one or more methods of communication generally employed by the Company for internal communication. The revised rates shall be made applicable prospectively and shall not be applicable to policies issued in pursuance of proposals already registered with the Company, unless otherwise, stipulated by the Act, the IRDA Act or Rules and Regulations.</pre><br/><br/>"
                + "<pre>In the event of any change in the scale of commission, brought about by reason of amendment/changes/issuance of the Rules and Regulations or the Act or the IRDA Act or the Company’s Board approved policy for ‘Appointment of Insurance Agents’, the Company’s liability to pay commission to the Agent in respect of any policies issued by it shall be limited to what is permissible under such law, rules, regulations, order or directive.</pre><br/><br/>"
                + "<pre>The Agent shall not be entitled to any commission in respect of the sale of a policy effected through him which is declared null and void by the Company for any reason or which has been cancelled for any reason whatsoever.</pre><br/><br/>"
                + "<pre>Payment of commission or remuneration or reward to Insurance Agents shall be made as per the Reward & Recognition schemes designed and approved by the Company from time to time with an objective to appropriately incentivize high performance. The above payments will be subject to limits prescribed as per extant applicable Guidelines/Regulations issued by IRDAI from time to time.</pre><br/><br/>"
                + "<pre>Where so ordered by a court of law or a statutory or other authority who is empowered by law to so order, the Company may withhold or retain the commission or remuneration or reward or any other amount due and payable to the Agent or to recover and remit to such court or statutory or other authority the amount so ordered to be remitted.</pre><br/><br/>"
                + "<pre>All payments made to the Agent shall be subject to deduction of taxes as per the applicable tax laws.</pre><br/><br/>"
                + "<pre>The Company shall pay the standard renewal commission as prescribed in the product even in case of his agency getting cancelled in accordance with the Company’s Board approved policy on ‘Appointment of Insurance Agents’, provided the agent is eligible for such renewal commission as per the prevailing rules, regulations and policies. However, the Company reserves the right to forfeit commission in certain cases like fraud, forgery, misconduct, etc. on grounds of disciplinary action as approved by the disciplinary authority of the Company.</pre><br/><br/>"
                + "<pre>In case of unfortunate event of death of an Agent, hereditary commission will continue to be paid to the nominee or heirs of the Agent for so long as such commission would have been payable had the Insurance Agent been alive, in accordance with the Company’s Board approved policy on ‘Appointment of Insurance Agents’ subject to the guidelines, rules and regulations of the IRDAI and the Company.</pre><br/><br/>"
                + "<pre>The Company reserves the right to recover any commission, remuneration or reward paid wrongly, or in excess of what is due and payable to the Agent from any amount due and payable to the Agent.</pre><br/><br/><br/>"
                + "<pre>Without prejudice to the generality of what is stated in the above clause, the Company shall have the absolute right to recover or retain from any amount due and payable to the Agent, any commission, remuneration or reward paid to him under one or more of the following situations:</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp(a) Where any policy issued in pursuance of a proposal procured by the Agent is declared null and void by the Company.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(b) Where any cheque or other instrument tendered or any payment effected through the ECS or similar mode of payment towards premium is dishonored or as the case may be reversed or countermanded for any reason;</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(c) Where a policyholder cancels a policy by exercising the “Free Look Option”. Explanation: For the purpose of this clause “Free Look Option” shall mean the option referred to in sub regulation (2) of Regulation 6 of the Insurance Regulatory and Development Authority (Protection of Policyholder’s Interest) Regulation 2002.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(d) Where the Company has been directed to repay or refund any premium paid under a Policy on account of any order, judgment or direction of any court, Tribunal, Consumer forum or Insurance Ombudsman or where the Company is satisfied that such repayment or refund is in the best interest of the Company.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(e) If any Policy issued in pursuance of a proposal proceed by the Agent is allowed to lapse within months of its issue</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(f) Where on a complaint by a Policyholder, the Company or, as the case may be, a court, tribunal or other authority empowered by law to adjudicate the complaint, holds that the Policyholder has been sold a Policy not opted for by him or under a plan of insurance different from or on terms other than those agreed to by him, resulting in the cancellation of the Policy, the Company shall be entitled to recover from the commission, remuneration or reward or any other amount due and payable to the Agent, the commission paid to him in respect of that Policy, besides withdrawing the credit of the business allotted to the Agent for any other purpose in relation to the said Policy.</pre><br/>"
                + "<b>5. Suspension or Cancellation of Agency:</b><br/><br/>"
                + "<pre>As per terms and conditions laid down here, Company holds the rights to suspend or cancel the appointment in any of the following events:-</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp <b>5.1 Suspension or cancellation of Appointment:</b></pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp Appointment may be suspended or cancelled after due notice and after giving the Agent reasonable opportunity of being heard:- </pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(a) where the Agent does not hold a valid appointment letter;</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(b) where the appointment to act as an Insurance Agent issued to the agent is cancelled for any reason; upon the death of the Agent;</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(c) where a petition to declare the Agent as an insolvent is presented to a court of competent Jurisdiction;</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(d) where on account of any change in law the operation of these terms and conditions has become unlawful;</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(e) where the Company is convinced that the Agent is guilty of an act involving moral turpitude and that it would reflect poorly on the fair name or the conduct of affairs or of business of the Company;</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(f) where the Agent is convicted of an offence and sentenced to any term of imprisonment by a court of competent jurisdiction.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp <b>Note:-</b> The fact that the Agent has preferred an appeal against such conviction or sentence shall not derogate from the operation of this provision during the pendency of such appeal. Further that the Agent has been acquitted in any proceeding or in any appeal initiated against him on a compliant made to or by the Company shall not entitle him to claim any compensation or damages against the Company</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(g) violates the provisions of the Insurance Act,1938, Insurance Regulatory and Development Authority Act, 1999 or rules or regulations, made there under as amended from time to time;</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(h) attracts any of the disqualifications mentioned in Guidelines/Regulations issued by IRDAI from time to time..</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(i) fails to comply with the code of conduct stipulated in the Company’s Policy for Appointment of Insurance Agents and notified from time to time.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(j) violates terms of appointment.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(k) fails to furnish any information relating to his/her activities as an agent as required by the Company or the Authority;</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(l) fails to comply with the directions issued by the Authority;</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(m) furnishes wrong or false information; or conceal or fail to disclose material facts in the application submitted for appointment of Agent or during the period of its validity.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(n) does not submit periodical returns as required by the Company/Authority;</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(o) does not co-operate with any inspection or enquiry conducted by the Authority;</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(p) fails to resolve the complaints of the policyholders or fails to give a satisfactory reply to the Authority in this behalf;</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(q) On failure to perform any other duties or obligation lawfully required of him or under these terms and conditions generally as an insurance agent of the Company,</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(r) Where there is allegation of fraud, dishonesty or misdemeanor against the agent by any policy holder of the Company or an officer or employees of the Company.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(s) Further the Company shall also have the right to suspend/cancel the agency if the Company feels that the continuation of the agency may be against the public interest.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp Where the suspension / cancellation of the appointment of the Agent is being contemplated or is in process, or pending enquiry into the allegations of fraud, misappropriations etc against the Agent, the Company may, by notice in writing, direct the Agent not to procure business or to perform his functions as the Agent of the Company for such period as may be specified. Any breach by the Agent of any such direction shall, besides disentitling the Agent to any remuneration for any business brought in by him during the period of operation of prohibition or for any other benefit pertaining to such business, also be construed as a breach of this terms and conditions. W here, however, the Company does not initiate or proceed with the termination , the Agent shall not be entitled to any compensation or damages on the ground of loss of business or of profit or on any other ground whatsoever in respect of any such direction as is referred to above against the Company.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp <b>5.2 Manner of holding enquiry before/after suspension of appointment of the Insurance Agent:</b></pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(a) The appointment of an Insurance Agent shall be cancelled only after an enquiry has been conducted in accordance with the procedure specified by Guidelines/Regulations issued by IRDAI from time to time.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(b) For the purpose of holding an enquiry, the Company may appoint an Officer as an Enquiry Officer within 15 days of the issue of the suspension order;</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(c) The Enquiry Officer shall issue a show cause notice to the Insurance Agent at the registered address of the insurance agent calling for all information / data as deemed necessary to conduct the enquiry and grant the insurance agent a time of 21 days from date of receipt of the show cause notice, for submission of his/her reply and such information / data called for;</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(d) The insurance Agent may, within 21 days from the date of receipt of such notice, furnish to the enquiry officer a reply to the Show cause notice together with copies of documentary or other evidence relied on by him or sought by the Enquiry Officer;</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(e) The Enquiry Officer shall give a reasonable opportunity of hearing to the insurance agent to enable him to make submissions in support of his/her reply;</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(f) The Insurance Agent may either appear in person or through any person duly authorised by him to present his case, provided however that the prior approval of the Company is obtained for the appearance of the ‘Authorised Person’;</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(g) If it is considered necessary, the Enquiry Officer may require the Company to present its case through one of its officers;</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(h) If it is considered necessary, the Enquiry Officer may call for feedback/information from any other related entity during the course of enquiry;</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(i) If it is considered necessary, the Enquiry Officer may call for additional papers from the Insurance Agent;</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(j) The Enquiry Officer shall make all necessary efforts to complete the proceeding at the earliest but in no case beyond 45 days of the commencement of the enquiry</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(k) Provided that in case the enquiry cannot be completed within the prescribed time limit of 45 days as mentioned in (10) above; the enquiry officer may seek additional time from the Company stating the reason thereof;</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(l) The Enquiry Officer shall, after taking into account all relevant facts and submissions made by the Insurance Agent, shall furnish a report making his/her recommendations to the Designated Official. The Designated Official shall pass a final order in writing with reasons. The order of designated official shall be signed and dated and communicated to the agent.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp <b>5.3 Procedure for Cancellation of Agency:</b></pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp On the issue of the final order for cancellation of agency of the Insurance Agent, the agent shall cease to act as an Insurance Agent from the date of the final order. Registration of new business by the suspended/cancelled agent will be stopped.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp <b>5.4 Effect of suspension/cancellation of Agency appointment:</b></pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp On and from the date of suspension or cancellation of the agency, the Insurance Agent, shall cease to act as an Insurance Agent.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(a) The Appointment letter and Identity card has to be submitted to the Company by the agent whose appointment has been cancelled by the Company within 7 days of issuance of final order effecting cancellation of appointment.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(b) The Company shall black list the agent and enter the details of the agent whose appointment is suspended/cancelled into the black listed agents database maintained by the Authority and the centralized list of agents database maintained by the Authority, in online mode, immediately after issuance of the order effecting suspension/cancellation. Further, the Company also reserves the right to display the order of suspension/cancelation of appointment of Insurance Agent on its W ebsite.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(c) In case a suspension is revoked in respect of any agent on conclusion of disciplinary action by way of issuance of a speaking order by Designated Official, the details of such agent shall be removed from list of black listed Agents as soon as the Speaking Order revoking his/her suspension is issued.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(d) The Company shall also inform other insurers, Life or General or Health Insurer or mono line insurer with whom he/she is acting as an agent, of the action taken against the Agent for their records and necessary action.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp <b>5.5 Appeal Provision:</b></pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp An agent who is aggrieved by the order of cancellation can appeal to the Company within 45 days of the order. The Company shall appoint an Appellate Officer who shall examine the appeal and give his decision in the matter in writing within 30 days of receipt of the appeal.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp <b>5.6 Procedure to be followed in respect of resignation/surrender of appointment by an Insurance Agent:</b></pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(a) In case an insurance agent appointed by the Company wishes to surrender his agency with the Company, he/she shall surrender his appointment letter and identity card to the designated official of the Company.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(b) The Company shall issue the cessation certificate as detailed in Form 1-C within a period of 15 days from the date of resignation or surrender of appointment.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(c) An agent who has surrendered his appointment may seek fresh appointment with other insurer. In such a case, the agent has to furnish to the new insurer all the details of his/her previous agency and produce Cessation Certificate issued by the previous insurer issued in Form I-C, along with his agency application form.</pre><br/>"
                + "<pre>&nbsp &nbsp &nbsp(d) Upon the cancellation of the Agency as per terms and conditions, the Agent shall not for a period of ninety days thereafter solicit or procure, directly or indirectly, any life insurance business for any other life insurance company or be engaged, in any manner, in any competing activity against the Company.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp Upon the cancellation of the Agency as per the terms and conditions, the Agent shall forthwith return to the Company all materials (including printed materials) and stores belonging to the Company which are in his possession or custody. On the neglect or failure of the Agent to do so, the Company shall be entitled, without being accused of trespass, to enter any premises occupied by the Agent for the purpose of recovering such materials and stores.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp Upon the cancellation of the Agency as per terms and conditions, the Agent shall not in any manner represent himself as the Agent of the Company.</pre><br/><br/>"
                + "<pre>&nbsp &nbsp &nbsp Upon the cancellation of the Agency as per terms and conditions, the Company shall be at liberty to publish notices of such cancellation in any newspaper or other publications or to communicate the same together with the grounds for such cancellation to the policyholders, other offices and Agents of the Company or to any other insurance Company or to the general public and of the fact that the Agent is not empowered to act for or on behalf of the Company or bind or represent the Company in any manner whatsoever.</pre><br/><br/>"
                + "<b>6. Agent not an employee: </b><br/><br/>"
                + "<pre>Nothing in this terms and conditions shall imply or be construed to constitute the relationship between the Company and the Agent as anything other than that of an insurer and Insurance Agent.</pre><br/><br/>"
                + "<pre>It shall not be open to the Agent to raise any claim of being an employee of the Company or for preference in the matter of recruitment to the service of the Company.</pre><br/><br/>"
                + "<b>7. MISCELLANEOUS: </b><br/><br/>"
                + "<pre>As per these terms and conditions, unless the context requires, words importing the singular shall include the plural and vice-versa and words importing a gender shall include every gender and reference to persons shall include bodies incorporate and unincorporated. These terms and conditions shall be operative from the date of appointment of the agent and binding on them.</pre><br/><br/>"
                + "<pre>No waiver, alteration, variation or addition to this laid terms and conditions shall be effective unless made in writing on or after the date of appointment and / or otherwise accepted by the Agent and the authorized representative of the Company.</pre><br/><br/>"
                + "<pre>The Company shall have a first lien upon all sums payable to the Agent in respect of any indebtedness of the Agent to the Company. The Company is entitled to set off, adjust or otherwise recover from the moneys due and payable to the Agent all such sums as are due to the Company, on account of such indebtedness of the Agent, including any indebtedness arising out of breach of any covenants as per terms and conditions on appointment of Agent.</pre><br/><br/>"
                + "<pre>The interpretation, construction and effect of any of the provisions of these terms and conditions on appointment of Agent laid down by the Company and amended time to time shall be final and binding on the Agent.</pre><br/><br/>"
                + "<pre>All notices, documents, consents, approvals or other communications (“Notice”) to be given hereunder shall be in writing and shall be transmitted by registered post or by facsimile or other electronic means in a form generating a record copy to the party being served at the relevant address for that party. Notice sent by mail shall be deemed to have been duly served not later than four working days after the date of posting. Any Notice sent by facsimile or other electronic means shall be deemed to have been duly served at the time of transmission (if transmitted during normal business hours at the location of the recipient and if not so transmitted then at the start of normal business hours on the next day which is a business day for the company).</pre><br/><br/>"
                + "<pre>If any term or provision in these terms and conditions on appointment of Agent shall be held to be illegal or unenforceable, in whole or in part, under any enactment or rule or law, such term or provision or part shall to that extent be deemed not to form part of these terms and conditions but the validity and enforceability of the remainder of these terms and conditions shall not be affected.</pre><br/><br/>"
                + "<pre>The waiver or forbearance or failure of the Company in any one or more instances upon the performance of any provisions of these terms and conditions on appointment of agent shall not be construed as a waiver or relinquishment of the Company’s rights and the Agent’s obligations in respect of due performance of obligations shall continue in full force and effect.</pre><br/><br/>"
                + "<pre>The Agency shall be governed by all the related regulations, Acts and Statutes besides these terms and conditions. These terms and conditions shall not substitute any of the statutory provisions but they are in addition to the statutory provisions governing the appointment of Insurance Agents.</pre><br/><br/>";

        txt_aob_terms_and_conditions.setText(Html.fromHtml(str_terms_and_conditions));

        txt_aob_nominee_conditions.setText(Html.fromHtml(str_nominee_conditions));

        ArrayList<PojoAOB> lst = db.get_agent_on_boarding_details_by_ID(Activity_AOB_Authentication.row_details);
        if (lst.size() > 0) {
            str_pan_no = lst.get(0).getStr_pan_no();

            str_app_created_date = lst.get(0).getStr_created_date();
            str_app_created_date = str_app_created_date == null ? "" : str_app_created_date;

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            if (is_dashboard && !str_app_created_date.equals("")) {
                //convert long to date
                try {
                    txt_aob_applicant_declaration_date.setText(dateFormat.format(
                            new Date(Long.valueOf(str_app_created_date))));
                } catch (Exception ex) {
                    mCommonMethods.printLog("parse Error : ", ex.getMessage());
                    String[] arr = str_app_created_date.split("-");
                    txt_aob_applicant_declaration_date.setText(arr[1] + "-" + arr[0] + "-" + arr[2]);
                }
            } else {
                //mCommonMethods.printLog("parse Error : ", "created date is blank");
                try {
                    txt_aob_applicant_declaration_date.setText(dateFormat.format(new Date(mCalender.getTimeInMillis())));
                } catch (Exception ex) {
                    mCommonMethods.printLog("parse Error : ", ex.getMessage());
                }
            }

            //applicant mobile number
            str_applicant_mobile = mParseXML.parseXmlTag(lst.get(0).getStr_personal_info(), "personal_info_mobile_no");

            //get training end date
            str_training_end_date = mParseXML.parseXmlTag(lst.get(0).getStr_exam_training_details(), "training_details_end_date");
            str_training_end_date = str_training_end_date == null ? "" : str_training_end_date;
            if (!str_training_end_date.equals("")) {
                String[] arrEndDate = str_training_end_date.split("-");
                str_training_end_date = arrEndDate[1] + "-" + arrEndDate[0] + "-" + arrEndDate[2];
            } else {
                mCommonMethods.showToast(mContext, "Training Date Prase error");
                return;
            }

            String str_terms_conditions = lst.get(0).getStr_declarations_conditions();
            str_terms_conditions = str_terms_conditions == null ? "" : str_terms_conditions;

            if (!str_terms_conditions.equals("")) {

                chkbox_aob_terms_conditions_agreed.setChecked(true);
                chkbox_aob_applicant_declaration_1.setChecked(true);
                chkbox_aob_applicant_declaration_2.setChecked(true);
                chkbox_aob_applicant_declaration_3.setChecked(true);
                chkbox_aob_applicant_declaration_4.setChecked(true);
                chkbox_aob_applicant_declaration_5.setChecked(true);
                chkbox_aob_applicant_declaration_6.setChecked(true);
                chkbox_aob_applicant_declaration_7.setChecked(true);
                chkbox_aob_nominee_conditions_agreed.setChecked(true);

                String str_place = mParseXML.parseXmlTag(str_terms_conditions, "terms_conditions_place");
                String str_date = mParseXML.parseXmlTag(str_terms_conditions, "terms_conditions_date");
                str_date = str_date == null ? "" : str_date;

                edt_aob_applicant_declaration_place.setText(str_place);
                if (is_dashboard && !str_date.equals("")) {
                    txt_aob_applicant_declaration_date.setText(str_date);
                } else {
                    //mCommonMethods.printLog("parse Error : ", "created date is blank");
                    try {
                        txt_aob_applicant_declaration_date.setText(dateFormat.format(new Date(mCalender.getTimeInMillis())));
                    } catch (Exception ex) {
                        mCommonMethods.printLog("parse Error : ", ex.getMessage());
                    }
                }

                //set Images

                // External sdcard location
                /*File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        "/SBI-Smart Advisor/");

                // Create the storage directory if it does not exist
                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        mCommonMethods.showToast(mContext, "oops Failed create");
                    }
                }*/

                //applicant photo
                String imageFileName = str_pan_no + "_" + APPLICANT_PHOTO_FILE_NAME + ".jpg";

                mApplicantPhoto = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);

                Bitmap bmp = BitmapFactory.decodeFile(mApplicantPhoto.getPath());
                bmp = bmp != null ? bmp.copy(Bitmap.Config.RGB_565, true) : null;

                if (bmp != null) {

                    Bitmap scaled = Bitmap.createScaledBitmap(bmp, 230, 200, true);

                    imgbtn_aob_applicant_declaration_photo.setImageBitmap(scaled);

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    scaled.compress(Bitmap.CompressFormat.PNG, 100, out);
                    byte[] signByteArray = out.toByteArray();
                    str_photo_applicant = Base64.encodeToString(signByteArray, Base64.DEFAULT);

                } else {
                    mCommonMethods.showToast(mContext, "Null object..");
                }

                //aplicant sign
                String imageSignFileName = str_pan_no + "_" + APPLICANT_SIGNATURE_FILE_NAME + ".jpg";

                mApplicantSign = mStorageUtils.createFileToAppSpecificDir(mContext, imageSignFileName);

                Bitmap bmpSign = BitmapFactory.decodeFile(mApplicantSign.getPath());
                bmpSign = bmpSign != null ? bmpSign.copy(Bitmap.Config.RGB_565, true) : null;

                if (bmpSign != null) {

                    Bitmap scaled = Bitmap.createScaledBitmap(bmpSign, 230, 200, true);

                    imgbtn_aob_applicant_declaration_sign.setImageBitmap(scaled);

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    scaled.compress(Bitmap.CompressFormat.PNG, 100, out);
                    byte[] signByteArray = out.toByteArray();
                    str_signature_applicant = Base64.encodeToString(signByteArray, Base64.DEFAULT);

                } else {
                    mCommonMethods.showToast(mContext, "Null object..");
                }

                //self declaration image
                /*String imageSelfDeclareFileName = str_pan_no + "_" + APPLICANT_SELF_DECLARATION_FILE_NAME + ".jpg";

                mApplicantSelfDeclaration = new File(mediaStorageDir.getPath() + File.separator + imageSelfDeclareFileName);

                Bitmap bmpSelfDeclaration = BitmapFactory.decodeFile(mApplicantSelfDeclaration.getPath());
                bmpSelfDeclaration = bmpSelfDeclaration != null ? bmpSelfDeclaration.copy(Bitmap.Config.RGB_565, true) : null;

                if (bmpSelfDeclaration != null) {

                    Bitmap scaled = Bitmap.createScaledBitmap(bmpSelfDeclaration, 230, 200, true);

                    imgbtn_aob_applicant_self_declaration_upload.setImageBitmap(scaled);

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    scaled.compress(Bitmap.CompressFormat.PNG, 100, out);
                    byte[] signByteArray = out.toByteArray();
                    str_self_declaration_applicant = Base64.encodeToString(signByteArray, Base64.DEFAULT);

                } else {
                    mCommonMethods.showToast(mContext, "Null object..");
                }*/
            }

        } else {
            str_pan_no = "";
        }
    }

    private void get_terms_conditions_xml() {

        String str_place = edt_aob_applicant_declaration_place.getText().toString();
        String str_date = txt_aob_applicant_declaration_date.getText().toString();

        str_terms_conditions = new StringBuilder();

        //str_terms_conditions.append("<terms_conditions>");
        str_terms_conditions.append("<terms_conditions_place>").append(str_place).append("</terms_conditions_place>");
        str_terms_conditions.append("<terms_conditions_date>").append(str_date).append("</terms_conditions_date>");
        //str_terms_conditions.append("</terms_conditions>");
    }

    public String validateDetails() {

        if (!chkbox_aob_terms_conditions_agreed.isChecked()) {
            chkbox_aob_terms_conditions_agreed.requestFocus();
            return "Please check Terms And Coditions";
        } else if (!chkbox_aob_applicant_declaration_1.isChecked()) {
            chkbox_aob_applicant_declaration_1.requestFocus();
            return "Please check applicant first declaration";
        } else if (!chkbox_aob_applicant_declaration_2.isChecked()) {
            chkbox_aob_applicant_declaration_2.requestFocus();
            return "Please check applicant second declaration";
        } else if (!chkbox_aob_applicant_declaration_3.isChecked()) {
            chkbox_aob_applicant_declaration_3.requestFocus();
            return "Please check applicant third declaration";
        } else if (!chkbox_aob_applicant_declaration_4.isChecked()) {
            chkbox_aob_applicant_declaration_4.requestFocus();
            return "Please check applicant fourth declaration";
        } else if (!chkbox_aob_applicant_declaration_5.isChecked()) {
            chkbox_aob_applicant_declaration_5.requestFocus();
            return "Please check applicant fifth declaration";
        } else if (!chkbox_aob_applicant_declaration_6.isChecked()) {
            chkbox_aob_applicant_declaration_6.requestFocus();
            return "Please check applicant sixth declaration";
        } else if (!chkbox_aob_applicant_declaration_7.isChecked()) {
            chkbox_aob_applicant_declaration_7.requestFocus();
            return "Please check applicant seventh declaration";
        } else if (edt_aob_applicant_declaration_place.getText().toString().equals("")) {
            edt_aob_applicant_declaration_place.requestFocus();
            return "Please Enter Place";
        } else if (str_photo_applicant.equals("")) {
            imgbtn_aob_applicant_declaration_photo.requestFocus();
            return "please add applicant photo";
        } else if (str_signature_applicant.equals("")) {
            imgbtn_aob_applicant_declaration_sign.requestFocus();
            return "Please capture applicant signature";
        } else if (!chkbox_aob_nominee_conditions_agreed.isChecked()) {
            chkbox_aob_nominee_conditions_agreed.requestFocus();
            return "please check nominee conditions";
        } else
            return "";
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ActivityAOBTermsConditionsDeclaration.this, ActivityAOBExamTraining.class);
        if (is_bsm_questions)
            intent.putExtra("is_bsm_questions", is_bsm_questions);
        else if (is_dashboard) {
            intent.putExtra("is_dashboard", is_dashboard);
        } else if (is_ia_upgrade) {
            intent.putExtra("is_ia_upgrade", is_ia_upgrade);
        }
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_aob_terms_conditions_back:

                is_back_pressed = true;
                onBackPressed();

                break;

            case R.id.btn_aob_terms_conditions_submit:

                if (is_dashboard) {
                    Intent mIntent = new Intent(ActivityAOBTermsConditionsDeclaration.this, ActivityAOBDocumentUpload.class);
                    mIntent.putExtra("is_dashboard", is_dashboard);
                    startActivity(mIntent);
                } else if (is_bsm_questions) {
                    Intent mIntent = new Intent(ActivityAOBTermsConditionsDeclaration.this, ActivityAOBDocumentUpload.class);
                    mIntent.putExtra("is_bsm_questions", is_bsm_questions);
                    startActivity(mIntent);
                } else if (!is_dashboard && !is_bsm_questions) {

                    //1. validate details
                    String str_error = validateDetails();

                    if (str_error.equals("")) {

                        //create xml for data save
                        get_terms_conditions_xml();

                        //3. update data against global row id
                        ContentValues cv = new ContentValues();
                        cv.put(db.AGENT_ON_BOARDING_DECLARATION_CONDITIONS, str_terms_conditions.toString());
                        cv.put(db.AGENT_ON_BOARDING_UPDATED_BY, strCIFBDMUserId);

                        //save date in long
                        cv.put(db.AGENT_ON_BOARDING_UPDATED_DATE, new Date(mCalender.getTimeInMillis()).getTime() + "");
                        cv.put(db.AGENT_ON_BOARDING_SYNCH_STATUS, "8");

                        int i = db.update_agent_on_boarding_details(cv, db.AGENT_ON_BOARDING_ID + " =? ",
                                new String[]{Activity_AOB_Authentication.row_details + ""});

                        new AsynchCreateAllPdf().execute();

                    } else {
                        mCommonMethods.showMessageDialog(mContext, str_error);
                    }

                }
                break;

            case R.id.imgbtn_aob_applicant_declaration_sign:
                capture_docs(APPLICANT_SIGNATURE_FILE_NAME);
                break;

            case R.id.imgbtn_aob_applicant_declaration_photo:
                capture_docs(APPLICANT_PHOTO_FILE_NAME);
                break;

            default:
                break;

        }

    }

    public void capture_docs(String str_doc_type) {

        str_capture_doc_type = str_doc_type;

        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            String imageFileName = str_pan_no + str_capture_doc_type + ".jpg";
            if (str_capture_doc_type.equals(APPLICANT_PHOTO_FILE_NAME)) {
                mApplicantPhoto = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);
                // Continue only if the File was successfully created
                if (mApplicantPhoto != null) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCommonMethods.getContentUri(mContext,
                                mApplicantPhoto));
                    } else {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mApplicantPhoto));
                    }
                    startActivityForResult(takePictureIntent, REQUEST_CODE_CAPTURE_DOCUMENT);
                }
            } else if (str_capture_doc_type.equals(APPLICANT_SIGNATURE_FILE_NAME)) {
                mApplicantSign = mStorageUtils.createFileToAppSpecificDir(mContext, imageFileName);
                // Continue only if the File was successfully created
                if (mApplicantSign != null) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCommonMethods.getContentUri(mContext,
                                mApplicantSign));
                    } else {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mApplicantSign));
                    }
                    startActivityForResult(takePictureIntent, REQUEST_CODE_CAPTURE_DOCUMENT);
                }
            }

        } catch (Exception exp) {
            exp.printStackTrace();
            mCommonMethods.printLog("Capture : ", exp.getMessage());
            if (str_capture_doc_type.equals(APPLICANT_PHOTO_FILE_NAME)) {
                mApplicantPhoto = null;
            } else if (str_capture_doc_type.equals(APPLICANT_SIGNATURE_FILE_NAME)) {
                mApplicantSign = null;
            }
        }
    }

    private void create_all_pdf_pages() {

        try {

            ArrayList<PojoAOB> lst = db.get_agent_on_boarding_details_by_ID(Activity_AOB_Authentication.row_details);
            if (lst.size() > 0) {
                Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
                Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);
                Font small_bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
                Font small_bold2 = new Font(Font.FontFamily.TIMES_ROMAN, 4, Font.BOLD);
                Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);

                if (is_ia_upgrade) {
                    agentFormFile = mStorageUtils.createFileToAppSpecificDir(mContext, str_pan_no + "_" + FILE_NAME_IA_UPGRADE + ".pdf");
                } else {
                    agentFormFile = mStorageUtils.createFileToAppSpecificDir(mContext, str_pan_no + "_" + FILE_NAME + ".pdf");
                }

                Rectangle rect = new Rectangle(594f, 792f);

                //Document document = new Document(rect, 50, 50, 50, 50);
                Document document = new Document(PageSize.A4, 50, 50, 50, 50);
                PdfWriter pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(agentFormFile.getPath()));

                document.open();

                // For SBI- Life Logo starts
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.sbi_life_logo);
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                Image img_sbi_logo = Image.getInstance(stream.toByteArray());
                img_sbi_logo.setAlignment(Image.LEFT);
                img_sbi_logo.getSpacingAfter();
                img_sbi_logo.scaleToFit(80, 50);

                Paragraph para_img_logo = new Paragraph("");
                para_img_logo.add(img_sbi_logo);

                Paragraph para_img_logo_after_space_1 = new Paragraph(" ");

                document.add(para_img_logo);
                // For SBI- Life Logo ends

                // To draw line after the sbi logo image
                document.add(new LineSeparator());
                document.add(para_img_logo_after_space_1);

                //get personal information data
                String str_personal_info = lst.get(0).getStr_personal_info();

                //to Personal Information tittle
                Paragraph para_tittle_1 = new Paragraph("Personal Information", headerBold);
                para_tittle_1.setAlignment(Element.ALIGN_CENTER);
                document.add(para_tittle_1);

                int serial_no = 0;

                //personal info full name
                String str_personal_info_full_name = mParseXML.parseXmlTag(str_personal_info, "personal_info_title")
                        + ". " + mParseXML.parseXmlTag(str_personal_info, "personal_info_full_name");
                serial_no++;

                Paragraph para_pan_name_tittle = new Paragraph("\n" + serial_no + ". Name(As appearing on PAN): " + str_personal_info_full_name, small_bold);
                para_pan_name_tittle.setAlignment(Element.ALIGN_LEFT);
                document.add(para_pan_name_tittle);

                //personal info father's / husband's name
                String str_relation_with_applicant = mParseXML.parseXmlTag(str_personal_info, "personal_info_relation_with_applicant");
                String str_personal_info_father_name = mParseXML.parseXmlTag(str_personal_info, "personal_info_father_husband_name");

                serial_no++;

                Paragraph para_father_name_title = new Paragraph(serial_no + ". " + str_relation_with_applicant + "'s Name: "
                        + str_personal_info_father_name, small_bold);
                para_father_name_title.setAlignment(Element.ALIGN_LEFT);
                document.add(para_father_name_title);


                //personal info PAN
                String str_personal_info_pan = mParseXML.parseXmlTag(str_personal_info,
                        "personal_info_pan_no");

                serial_no++;

                Paragraph para_pan_no = new Paragraph(serial_no + ". " + "Permanent Account Number(PAN): "
                        + str_personal_info_pan, small_bold);
                para_pan_no.setAlignment(Element.ALIGN_LEFT);
                document.add(para_pan_no);

                //personal info aadhaar no.
                String str_personal_info_aadhaar_no = mParseXML.parseXmlTag(str_personal_info,
                        "personal_info_aadhaar_no");
                str_personal_info_aadhaar_no = str_personal_info_aadhaar_no == null ? "" : str_personal_info_aadhaar_no;

                if (!str_personal_info_aadhaar_no.equals("")) {

                    serial_no++;

                    Paragraph para_aadhaar_no = new Paragraph(serial_no + ". " + "Aadhaar No: " + str_personal_info_aadhaar_no, small_bold);
                    para_aadhaar_no.setAlignment(Element.ALIGN_LEFT);
                    document.add(para_aadhaar_no);
                }

                //personal info Gender
                String str_personal_info_gender = mParseXML.parseXmlTag(str_personal_info, "personal_info_gender");
                serial_no++;

                Paragraph para_gender = new Paragraph(serial_no + ". " + "Gender: " + str_personal_info_gender, small_bold);
                para_gender.setAlignment(Element.ALIGN_LEFT);
                document.add(para_gender);

                //personal info marital status
                String str_personal_info_marital_status = mParseXML.parseXmlTag(str_personal_info, "personal_info_marital_status");

                serial_no++;
                Paragraph para_marital_status = new Paragraph(serial_no + ". " + "Marital Status: " + str_personal_info_marital_status, small_bold);
                para_marital_status.setAlignment(Element.ALIGN_LEFT);
                document.add(para_marital_status);

                //personal info maiden name
                if (mParseXML.parseXmlTag(str_personal_info, "personal_info_title").equalsIgnoreCase("Mrs")) {

                    String str_personal_info_maiden_name = mParseXML.parseXmlTag(str_personal_info, "personal_info_maiden_name");
                    serial_no++;

                    Paragraph para_maiden_name_title = new Paragraph(serial_no + ". " + "Maiden Name (Name before marraige in case of married women applicant): "
                            + str_personal_info_maiden_name, small_bold);
                    para_maiden_name_title.setAlignment(Element.ALIGN_LEFT);
                    document.add(para_maiden_name_title);
                }

                //personal info birthdate
                String str_personal_info_birthdate = mParseXML.parseXmlTag(str_personal_info, "personal_info_dob");
                String[] arrDate = str_personal_info_birthdate.split("-");
                serial_no++;
                Paragraph para_date_of_birth = new Paragraph(serial_no + ". " + "Date of Birth: "
                        + arrDate[1] + "-" + arrDate[0] + "-" + arrDate[2], small_bold);
                para_date_of_birth.setAlignment(Element.ALIGN_LEFT);
                document.add(para_date_of_birth);

                //personal info mobile no
                String str_personal_info_mob_no = mParseXML.parseXmlTag(str_personal_info, "personal_info_mobile_no");

                serial_no++;
                Paragraph para_mobile_no = new Paragraph(serial_no + ". " + "Mobile Number: " + str_personal_info_mob_no, small_bold);
                para_mobile_no.setAlignment(Element.ALIGN_LEFT);
                document.add(para_mobile_no);

                //personal info residence no
                String str_personal_info_residence_no = mParseXML.parseXmlTag(str_personal_info, "personal_info_residence_no");
                serial_no++;
                Paragraph para_residence_no = new Paragraph(serial_no + ". " + "Residence Number: " + str_personal_info_residence_no, small_bold);
                para_residence_no.setAlignment(Element.ALIGN_LEFT);
                document.add(para_residence_no);

                //personal info email id
                String str_personal_info_emailid = mParseXML.parseXmlTag(str_personal_info, "personal_info_email_id");
                serial_no++;
                Paragraph para_eamil_id = new Paragraph(serial_no + ". " + "Email ID: " + str_personal_info_emailid, small_bold);
                para_eamil_id.setAlignment(Element.ALIGN_LEFT);
                document.add(para_eamil_id);

                //personal info caste catagory
                String str_personal_info_caste = mParseXML.parseXmlTag(str_personal_info, "personal_info_caste_category");
                serial_no++;
                Paragraph para_caste_catagory = new Paragraph(serial_no + ". " + "Caste: " + str_personal_info_caste, small_bold);
                para_caste_catagory.setAlignment(Element.ALIGN_LEFT);
                document.add(para_caste_catagory);

                //personal info address for communication

                serial_no++;
                Paragraph para_comm_address_title = new Paragraph(serial_no + ". " + "Address for communication:", small_bold);
                para_comm_address_title.setAlignment(Element.ALIGN_LEFT);
                document.add(para_comm_address_title);

                String str_personal_info_comm_address = mParseXML.parseXmlTag(str_personal_info, "personal_info_communication_address1")
                        + " " + mParseXML.parseXmlTag(str_personal_info, "personal_info_communication_address2")
                        + " " + mParseXML.parseXmlTag(str_personal_info, "personal_info_communication_address3");

                //communication address pincode
                String str_personal_info_comm_address_pin = mParseXML.parseXmlTag(str_personal_info, "personal_info_communication_address_pin");

                Paragraph para_comm_address_title_val = new Paragraph(str_personal_info_comm_address
                        + "\nPin : " + str_personal_info_comm_address_pin, small_normal);
                para_comm_address_title_val.setIndentationLeft(25);
                document.add(para_comm_address_title_val);

                //personal info Permanent address
                String str_personal_info_permanent_address = mParseXML.parseXmlTag(str_personal_info, "personal_info_permanant_address1")
                        + " " + mParseXML.parseXmlTag(str_personal_info, "personal_info_permanant_address2")
                        + " " + mParseXML.parseXmlTag(str_personal_info, "personal_info_permanant_address3");

                //permanent address pincode
                String str_personal_info_permanent_address_pin = mParseXML.parseXmlTag(str_personal_info, "personal_info_permanant_address_pin");
                serial_no++;
                Paragraph para_permanent_address_title = new Paragraph(serial_no + ". " + "Permanent Address:", headerBold);
                para_permanent_address_title.setAlignment(Element.ALIGN_LEFT);
                document.add(para_permanent_address_title);

                Paragraph para_permanent_address_title_val = new Paragraph(str_personal_info_permanent_address
                        + "\nPin: " + str_personal_info_permanent_address_pin, small_normal);
                para_permanent_address_title_val.setIndentationLeft(25);
                document.add(para_permanent_address_title_val);

                //personal info educationl details
                Paragraph para_educational_details_title = new Paragraph("Educational Details:", small_bold);
                para_educational_details_title.setAlignment(Element.ALIGN_CENTER);
                document.add(para_educational_details_title);

                //Basic Qualification
                String str_personal_info_basic_qualification = mParseXML.parseXmlTag(str_personal_info, "personal_info_educational_details_basic_qualification");
                serial_no++;

                Paragraph para_basic_qualification_val = new Paragraph(serial_no + ". " + "Basic Qualification: " + str_personal_info_basic_qualification, small_bold);
                para_permanent_address_title.setAlignment(Element.ALIGN_LEFT);
                document.add(para_basic_qualification_val);

                //passing details title
                Paragraph para_passing_details_title = new Paragraph("Passing Details:", small_bold);
                para_passing_details_title.setIndentationLeft(25);
                document.add(para_passing_details_title);

                //Roll Number
                String str_personal_info_roll_no = mParseXML.parseXmlTag(str_personal_info, "personal_info_educational_details_passing_roll_no");
                serial_no++;
                Paragraph para_roll_no_val = new Paragraph(serial_no + ". " + "Roll Number: " + str_personal_info_roll_no, small_bold);
                para_roll_no_val.setIndentationLeft(25);
                document.add(para_roll_no_val);

                //Board / University
                String str_personal_info_board_university = mParseXML.parseXmlTag(str_personal_info, "personal_info_educational_details_passing_university");
                serial_no++;
                Paragraph para_board_university_val = new Paragraph(serial_no + ". " + "Board / University: " + str_personal_info_board_university, small_bold);
                para_board_university_val.setIndentationLeft(25);
                document.add(para_board_university_val);

                //year of passing
                String str_personal_info_pass_year = mParseXML.parseXmlTag(str_personal_info, "personal_info_educational_details_passing_university");
                serial_no++;
                Paragraph para_pass_year_val = new Paragraph(serial_no + ". " + "Year Passing: " + str_personal_info_pass_year, small_bold);
                para_pass_year_val.setIndentationLeft(25);
                document.add(para_pass_year_val);

                //personal info Professional Qualification
                String str_personal_info_pro_qualification = mParseXML.parseXmlTag(str_personal_info,
                        "personal_info_educational_details_professional_qualification");
                serial_no++;
                Paragraph para_pro_qualification_title = new Paragraph(serial_no + ". " + "Professional Qualification: "
                        + str_personal_info_pro_qualification, small_bold);
                para_pro_qualification_title.setIndentationLeft(25);
                document.add(para_pro_qualification_title);

                if (str_personal_info_pro_qualification.equalsIgnoreCase("Others")) {
                    //professional qualification if others
                    String str_personal_info_others = mParseXML.parseXmlTag(str_personal_info, "personal_info_educational_details_professional_qualification_others");

                    Paragraph para_pro_qaulification_others = new Paragraph("Comment: " + str_personal_info_others, small_bold);
                    para_pro_qaulification_others.setIndentationLeft(25);
                    document.add(para_pro_qaulification_others);
                }

                //personal info CKYC number
                String str_applicant_ckyc_no = mParseXML.parseXmlTag(str_personal_info, "personal_info_ckyc_no");
                str_applicant_ckyc_no = str_applicant_ckyc_no == null ? "" : str_applicant_ckyc_no;
                if (!str_applicant_ckyc_no.equals("")) {
                    Paragraph para_applicant_ckyc_no = new Paragraph("CKYC No.: " + str_applicant_ckyc_no, small_bold);
                    para_applicant_ckyc_no.setIndentationLeft(25);
                    document.add(para_applicant_ckyc_no);
                }

                //get occupational details data
                String str_occupational_details = lst.get(0).getStr_occupation_info();

                //to occupational details tittle
                Paragraph para_tittle_2 = new Paragraph("\nOccupation Details", headerBold);
                para_tittle_2.setAlignment(Element.ALIGN_CENTER);
                document.add(para_tittle_2);

                Paragraph para_tittle_21 = new Paragraph("\nEmployment Details", small_bold);
                para_tittle_21.setAlignment(Element.ALIGN_CENTER);
                document.add(para_tittle_21);

                String str_occ_applicable = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_applicable");
                serial_no++;
                Paragraph para_occu_applicable = new Paragraph(serial_no + ". " + "Whichever Applicable: "
                        + str_occ_applicable, small_bold);
                para_occu_applicable.setAlignment(Element.ALIGN_LEFT);
                document.add(para_occu_applicable);

                String str_occ_self_emp = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_self_emp");
                serial_no++;
                Paragraph para_occu_self_employeed = new Paragraph(serial_no + ". " + "Are Self employed?: "
                        + str_occ_self_emp, small_bold);
                para_occu_self_employeed.setAlignment(Element.ALIGN_LEFT);
                document.add(para_occu_self_employeed);

                if (str_occ_self_emp.equalsIgnoreCase("Yes")) {
                    String str_occ_self_emp_cmnt = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_self_emp_cmnt");
                    Paragraph para_occu_self_employed_cmnt = new Paragraph("Comment: " + str_occ_self_emp_cmnt, small_bold);
                    para_occu_self_employed_cmnt.setIndentationLeft(25);
                    document.add(para_occu_self_employed_cmnt);
                }

                String str_occ_operation_area = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_area_ops");
                serial_no++;
                Paragraph para_occu_operation_area = new Paragraph(serial_no + ". " + "Area Of Operation: "
                        + str_occ_operation_area, small_bold);
                para_occu_operation_area.setAlignment(Element.ALIGN_LEFT);
                document.add(para_occu_operation_area);

                String str_occ_annual_income = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_annual_income");
                serial_no++;
                Paragraph para_occu_annual_income = new Paragraph(serial_no + ". " + "Annual Income (Self + Spouse): "
                        + str_occ_annual_income + " Rs.", small_bold);
                para_occu_annual_income.setAlignment(Element.ALIGN_LEFT);
                document.add(para_occu_annual_income);

                String str_occ_hold_agency = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_occu_agency");
                serial_no++;
                Paragraph para_occu_hold_agency = new Paragraph("\n" + serial_no + ". " + "Do you hold agency with any Life/General Insurance company / Health /Mono line insurer (YES / NO) (If yes, please provide details): " + str_occ_hold_agency, small_bold);
                para_occu_hold_agency.setAlignment(Element.ALIGN_LEFT);
                document.add(para_occu_hold_agency);

                /*Paragraph para_occu_hold_agency_val = new Paragraph(str_occ_hold_agency, small_bold);
                para_occu_hold_agency_val.setIndentationLeft(25);
                document.add(para_occu_hold_agency_val);*/

                if (!str_occ_hold_agency.equalsIgnoreCase("None")) {

                    String str_occ_hold_agency_company = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_occu_company");
                    Paragraph para_occu_hold_agency_comapny = new Paragraph("Name of the Firm / Comapny: "
                            + str_occ_hold_agency_company, small_bold);
                    para_occu_hold_agency_comapny.setIndentationLeft(25);
                    document.add(para_occu_hold_agency_comapny);

                    String str_occ_hold_agency_company_cmnt = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_occu_company_cmnt");
                    Paragraph para_occu_hold_agency_company_cmnt = new Paragraph("Comment: " + str_occ_hold_agency_company_cmnt, small_bold);
                    para_occu_hold_agency_company_cmnt.setIndentationLeft(25);
                    document.add(para_occu_hold_agency_company_cmnt);

                }

                String str_occ_ever_surrender = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_evr_surrendered");
                serial_no++;
                Paragraph para_occu_ever_surrender = new Paragraph("\n" + serial_no + ". " + "Have you ever surrendered your insurance license/agency or has your insurance license/agency ever been Cancelled / Blacklisted: (YES / NO) (if yes, please give details along with cessation certificate): "
                        + str_occ_ever_surrender, small_bold);
                para_occu_ever_surrender.setAlignment(Element.ALIGN_LEFT);
                document.add(para_occu_ever_surrender);

                /*Paragraph para_occu_ever_surrender_val = new Paragraph(str_occ_ever_surrender, small_bold);
                para_occu_ever_surrender_val.setIndentationLeft(25);
                document.add(para_occu_ever_surrender_val);*/

                if (str_occ_hold_agency.equalsIgnoreCase("Yes")) {

                    String str_occ_ever_surrender_cmnt = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_evr_surrendered_cmnt");
                    Paragraph para_occu_ever_surrender_cmnt = new Paragraph("Comment: " + str_occ_ever_surrender_cmnt, small_bold);
                    para_occu_ever_surrender_cmnt.setIndentationLeft(25);
                    document.add(para_occu_ever_surrender_cmnt);
                }

                String str_occ_other_insurer = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_other_insurer");
                serial_no++;
                Paragraph para_occu_other_insurer = new Paragraph("\n" + serial_no + ". " + "Do you have any tie up with any other insurer as an employee or director or promoter Or hold any insurance activity related license or hold any position with any Corporate Agent / Broker/ TPA / Insurance Marketing Firm/ any other insurance intermediary: (YES /NO) (if yes, please give details): "
                        + str_occ_other_insurer, small_bold);
                para_occu_other_insurer.setAlignment(Element.ALIGN_LEFT);
                document.add(para_occu_other_insurer);

                /*Paragraph para_occu_other_insurer_val = new Paragraph(str_occ_other_insurer, small_bold);
                para_occu_other_insurer_val.setIndentationLeft(25);
                document.add(para_occu_other_insurer_val);*/

                if (str_occ_other_insurer.equalsIgnoreCase("Yes")) {

                    String str_occ_other_insurer_company = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_other_insurer_company");
                    Paragraph para_occu_other_insurer_Company = new Paragraph("Name of the Firm / Company: " + str_occ_other_insurer_company
                            , small_bold);
                    para_occu_other_insurer_Company.setIndentationLeft(25);
                    document.add(para_occu_other_insurer_Company);

                    String str_occ_other_insurer_company_cmnt = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_other_insurer_company_cmnt");
                    Paragraph para_occu_other_insurer_company_cmnt = new Paragraph("Comment: " + str_occ_other_insurer_company_cmnt
                            , small_bold);
                    para_occu_other_insurer_company_cmnt.setIndentationLeft(25);
                    document.add(para_occu_other_insurer_company_cmnt);
                }

                String str_occ_r_u_promoter = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_r_u_promoter");
                serial_no++;
                Paragraph para_occu_r_u_promoter = new Paragraph("\n" + serial_no + ". " + "Are you one of the promoter or partner or director in any firm or company?: "
                        + str_occ_r_u_promoter, small_bold);
                para_occu_r_u_promoter.setAlignment(Element.ALIGN_LEFT);
                document.add(para_occu_r_u_promoter);

                if (str_occ_r_u_promoter.equalsIgnoreCase("Yes")) {

                    String str_occ_r_u_promoter_company = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_r_u_promoter_company");
                    Paragraph para_occu_r_u_promoter_company = new Paragraph("Name of the Firm / Company: "
                            + str_occ_r_u_promoter_company, small_bold);
                    para_occu_r_u_promoter_company.setIndentationLeft(25);
                    document.add(para_occu_r_u_promoter_company);

                    String str_occ_r_u_promoter_pan = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_r_u_promoter_company_pan");
                    Paragraph para_occu_r_u_promoter_pan = new Paragraph("PAN of the Firm / Company: "
                            + str_occ_r_u_promoter_pan, small_bold);
                    para_occu_r_u_promoter_pan.setIndentationLeft(25);
                    document.add(para_occu_r_u_promoter_pan);

                    String str_occ_r_u_promoter_tin = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_r_u_promoter_company_tin");
                    Paragraph para_occu_r_u_promoter_tin = new Paragraph("TIN of the Firm / Company: "
                            + str_occ_r_u_promoter_tin, small_bold);
                    para_occu_r_u_promoter_tin.setIndentationLeft(25);
                    document.add(para_occu_r_u_promoter_tin);

                    String str_occ_r_u_promoter_associate_sbil = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_asso_with_sbil");
                    Paragraph para_occu_r_u_promoter_associate_sbil = new Paragraph("Is the firm/Company associated/empanelled with SBI Life as vendor?: "
                            + str_occ_r_u_promoter_associate_sbil, small_bold);
                    para_occu_r_u_promoter_associate_sbil.setIndentationLeft(25);
                    document.add(para_occu_r_u_promoter_associate_sbil);

                    if (str_occ_r_u_promoter_associate_sbil.equalsIgnoreCase("Yes")) {
                        String str_occ_r_u_promoter_associate_sbil_cmnt = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_asso_with_sbil_cmnt");
                        Paragraph para_occu_r_u_promoter_associate_sbil_cmnt = new Paragraph("Comment: "
                                + str_occ_r_u_promoter_associate_sbil_cmnt, small_bold);
                        para_occu_r_u_promoter_associate_sbil_cmnt.setIndentationLeft(25);
                        document.add(para_occu_r_u_promoter_associate_sbil_cmnt);
                    }

                }

                String str_occ_relat_sbi_emp = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_related_sbil_emp");
                serial_no++;
                Paragraph para_occu_relat_sbi_emp = new Paragraph("\n" + serial_no + ". " + "Are you related to any employee of SBI Life Insurance Co. Ltd or State Bank of India (SBI)/any of SBI Associates: "
                        + str_occ_relat_sbi_emp, small_bold);
                para_occu_relat_sbi_emp.setAlignment(Element.ALIGN_LEFT);
                document.add(para_occu_relat_sbi_emp);

                /*Paragraph para_occu_relat_sbi_emp_val = new Paragraph(str_occ_relat_sbi_emp, small_bold);
                para_occu_relat_sbi_emp_val.setIndentationLeft(25);
                document.add(para_occu_relat_sbi_emp_val);*/

                if (str_occ_relat_sbi_emp.equalsIgnoreCase("Yes")) {

                    String str_occ_relat_sbi_emp_name = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_related_sbil_emp_name");
                    Paragraph para_occu_relat_sbi_emp_name = new Paragraph("Name of the Employee: " + str_occ_relat_sbi_emp_name, small_bold);
                    para_occu_relat_sbi_emp_name.setIndentationLeft(25);
                    document.add(para_occu_relat_sbi_emp_name);

                    String str_occ_relat_sbi_emp_designation = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_related_sbil_emp_designation");
                    Paragraph para_occu_relat_sbi_emp_designation = new Paragraph("Designation: " + str_occ_relat_sbi_emp_designation
                            , small_bold);
                    para_occu_relat_sbi_emp_designation.setIndentationLeft(25);
                    document.add(para_occu_relat_sbi_emp_designation);

                    String str_occ_relat_sbi_emp_relation = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_related_sbil_emp_relation");
                    Paragraph para_occu_relat_sbi_emp_relation = new Paragraph("Employee Relation With Applicant: " + str_occ_relat_sbi_emp_relation
                            , small_bold);
                    para_occu_relat_sbi_emp_relation.setIndentationLeft(25);
                    document.add(para_occu_relat_sbi_emp_relation);

                    String str_occ_relat_sbi_emp_bank = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_related_sbil_emp_insu_off");
                    Paragraph para_occu_relat_sbi_emp_bank = new Paragraph("Details of Bank/SBI Associate /SBI Life Insurance Office: \n"
                            + str_occ_relat_sbi_emp_bank, small_bold);
                    para_occu_relat_sbi_emp_bank.setIndentationLeft(25);
                    document.add(para_occu_relat_sbi_emp_bank);

                    String str_occ_relat_aforeside = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_aforeside_relative");
                    Paragraph para_occu_relat_aforeside = new Paragraph("You are dependent on the aforesaid relative: "
                            + str_occ_relat_aforeside, small_bold);
                    para_occu_relat_aforeside.setIndentationLeft(25);
                    document.add(para_occu_relat_aforeside);

                    if (str_occ_relat_aforeside.equalsIgnoreCase("Yes")) {
                        String str_occ_relat_aforeside_cmnt = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_related_sbil_bank_address");
                        Paragraph para_occu_relat_aforeside_cmnt = new Paragraph("Bank Address: "
                                + str_occ_relat_aforeside_cmnt, small_bold);
                        para_occu_relat_aforeside_cmnt.setIndentationLeft(25);
                        document.add(para_occu_relat_aforeside_cmnt);
                    }
                }

                String str_occ_ex_employee = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_sbi_x_emp");
                serial_no++;
                Paragraph para_occu_ex_employee = new Paragraph("\n" + serial_no + ". " + "Whether you are ex-employee of SBI or its associate banks: "
                        + str_occ_ex_employee, small_bold);
                para_occu_ex_employee.setAlignment(Element.ALIGN_LEFT);
                document.add(para_occu_ex_employee);

                if (str_occ_ex_employee.equalsIgnoreCase("Yes")) {
                    String str_occ_ex_employee_cmnt = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_sbi_x_emp_cmnt");
                    Paragraph para_occu_ex_employee_cmnt = new Paragraph("Comment: "
                            + str_occ_ex_employee_cmnt, small_bold);
                    para_occu_ex_employee_cmnt.setIndentationLeft(25);
                    document.add(para_occu_ex_employee_cmnt);

                    String str_occ_ex_employee_last_date = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_emp_last_date");
                    Paragraph para_occu_ex_employee_last_date = new Paragraph("Last Date Of Employment: "
                            + str_occ_ex_employee_last_date, small_bold);
                    para_occu_ex_employee_last_date.setIndentationLeft(25);
                    document.add(para_occu_ex_employee_last_date);

                    String str_occ_ex_employee_last_date_cmnt = mParseXML.parseXmlTag(str_occupational_details, "occupatinal_info_emp_cmnt");
                    Paragraph para_occu_ex_employee_last_date_cmnt = new Paragraph("Comment: "
                            + str_occ_ex_employee_last_date_cmnt, small_bold);
                    para_occu_ex_employee_last_date_cmnt.setIndentationLeft(25);
                    document.add(para_occu_ex_employee_last_date_cmnt);
                }

                //get Nominational details data

                /*String str_nomination_details = lst.get(0).getStr_nomination_info();

                //to Nominational details tittle
                Paragraph para_tittle_3 = new Paragraph("\nNomination Details", headerBold);
                para_tittle_3.setAlignment(Element.ALIGN_CENTER);
                document.add(para_tittle_3);

                String str_nominee_name = mParseXML.parseXmlTag(str_nomination_details, "nominee_info_nom_title") + ". "
                        + mParseXML.parseXmlTag(str_nomination_details, "nominee_info_nom_full_name");

                Paragraph para_nominee_name = new Paragraph("\nName: " + str_nominee_name, small_bold);
                para_nominee_name.setAlignment(Element.ALIGN_LEFT);
                document.add(para_nominee_name);

                String str_nominee_dob = mParseXML.parseXmlTag(str_nomination_details, "nominee_info_nom_dob");

                Paragraph para_nominee_dob = new Paragraph("Date of Birth: " + str_nominee_dob, small_bold);
                para_nominee_dob.setAlignment(Element.ALIGN_LEFT);
                document.add(para_nominee_dob);

                String str_nominee_gender = mParseXML.parseXmlTag(str_nomination_details, "nominee_info_nom_gender");

                Paragraph para_nominee_gender = new Paragraph("Gender: " + str_nominee_gender, small_bold);
                para_nominee_gender.setAlignment(Element.ALIGN_LEFT);
                document.add(para_nominee_gender);

                String str_nominee_percentage = mParseXML.parseXmlTag(str_nomination_details, "nominee_info_nom_percentage");

                Paragraph para_nominee_percentage = new Paragraph("Percentage: " + str_nominee_percentage + "%", small_bold);
                para_nominee_percentage.setAlignment(Element.ALIGN_LEFT);
                document.add(para_nominee_percentage);

                String str_nominee_relationship = mParseXML.parseXmlTag(str_nomination_details, "nominee_info_nom_relation");

                Paragraph para_nominee_relationship = new Paragraph("Relationship with nominee: " + str_nominee_relationship, small_bold);
                para_nominee_relationship.setAlignment(Element.ALIGN_LEFT);
                document.add(para_nominee_relationship);

                String str_nominee_address = mParseXML.parseXmlTag(str_nomination_details, "nominee_info_nom_address") + "\n"
                        + mParseXML.parseXmlTag(str_nomination_details, "nominee_info_nom_address_city") + "\n"
                        + mParseXML.parseXmlTag(str_nomination_details, "nominee_info_nom_address_state") + "\n"
                        + mParseXML.parseXmlTag(str_nomination_details, "nominee_info_nom_address_pincode");

                Paragraph para_nominee_address = new Paragraph("Address: " + str_nominee_address, small_bold);
                para_nominee_address.setAlignment(Element.ALIGN_LEFT);
                document.add(para_nominee_address);

                String str_appointee_name = mParseXML.parseXmlTag(str_nomination_details, "nominee_info_appointee_title") + ". "
                        + mParseXML.parseXmlTag(str_nomination_details, "nominee_info_appointee_full_name");

                str_appointee_name = str_appointee_name == null ? "" : str_appointee_name;

                if (!str_appointee_name.equalsIgnoreCase("")) {

                    Paragraph para_appointee_note = new Paragraph("In case of minor nominee", headerBold);
                    para_appointee_note.setAlignment(Element.ALIGN_LEFT);
                    document.add(para_appointee_note);

                    Paragraph para_appointee_name = new Paragraph("Name Of Appointee: " + str_appointee_name, small_bold);
                    para_appointee_name.setAlignment(Element.ALIGN_LEFT);
                    document.add(para_appointee_name);

                    String str_appointee_dob = mParseXML.parseXmlTag(str_nomination_details, "nominee_info_appointee_dob");
                    Paragraph para_appointee_dob = new Paragraph("Date of Birth: " + str_appointee_dob, small_bold);
                    para_appointee_dob.setAlignment(Element.ALIGN_LEFT);
                    document.add(para_appointee_dob);

                    String str_appointee_relation = mParseXML.parseXmlTag(str_nomination_details, "nominee_info_appointee_relation");
                    Paragraph para_appointee_relation = new Paragraph("Relation with minor nominee: " + str_appointee_relation, small_bold);
                    para_appointee_relation.setAlignment(Element.ALIGN_LEFT);
                    document.add(para_appointee_relation);

                    String str_appointee_address = mParseXML.parseXmlTag(str_nomination_details, "nominee_info_appointee_address") + "\n"
                            + mParseXML.parseXmlTag(str_nomination_details, "nominee_info_appointee_address_city") + "\n"
                            + mParseXML.parseXmlTag(str_nomination_details, "nominee_info_appointee_address_state") + "\n"
                            + mParseXML.parseXmlTag(str_nomination_details, "nominee_info_appointee_address_pincode");

                    Paragraph para_appointee_address = new Paragraph("Address: " + str_appointee_address, small_bold);
                    para_appointee_address.setAlignment(Element.ALIGN_LEFT);
                    document.add(para_appointee_address);
                }

                String str_nominee_sign_lang_witness_occupation = mParseXML.parseXmlTag(str_nomination_details, "nominee_info_witness_occupation");
                str_nominee_sign_lang_witness_occupation = str_nominee_sign_lang_witness_occupation == null ? "" : str_nominee_sign_lang_witness_occupation;

                if (!str_nominee_sign_lang_witness_occupation.equalsIgnoreCase("")) {
                    Paragraph para_nominee_sign_lang = new Paragraph("Is the sign of application in any language other than in English? : Yes", small_bold);
                    para_nominee_sign_lang.setAlignment(Element.ALIGN_LEFT);
                    document.add(para_nominee_sign_lang);

                    Paragraph para_nominee_sign_lang_witness_occupation = new Paragraph("Witness Occupation :"
                            + str_nominee_sign_lang_witness_occupation, small_bold);
                    para_nominee_sign_lang_witness_occupation.setAlignment(Element.ALIGN_LEFT);
                    document.add(para_nominee_sign_lang_witness_occupation);

                    String str_nominee_sign_lang_witness_address = mParseXML.parseXmlTag(str_nomination_details, "nominee_info_witness_full_address");

                    Paragraph para_nominee_sign_lang_witness_address = new Paragraph("Witness Address :"
                            + str_nominee_sign_lang_witness_address, small_bold);
                    para_nominee_sign_lang_witness_address.setAlignment(Element.ALIGN_LEFT);
                    document.add(para_nominee_sign_lang_witness_address);

                    String str_nominee_sign_lang_witness_name = mParseXML.parseXmlTag(str_nomination_details, "nominee_info_witness_name");

                    Paragraph para_nominee_sign_lang_witness_name = new Paragraph("Witness Name :"
                            + str_nominee_sign_lang_witness_name, small_bold);
                    para_nominee_sign_lang_witness_name.setAlignment(Element.ALIGN_LEFT);
                    document.add(para_nominee_sign_lang_witness_name);

                } else {
                    Paragraph para_nominee_sign_lang = new Paragraph("Is the sign of application in any language other than in English? : No", small_bold);
                    para_nominee_sign_lang.setAlignment(Element.ALIGN_LEFT);
                    document.add(para_nominee_sign_lang);
                }*/

                //get Bank details data
                /*String str_bank_details = lst.get(0).getStr_bank_details();

                //to Bank details tittle
                Paragraph para_tittle_4 = new Paragraph("\nBank Details", headerBold);
                para_tittle_4.setAlignment(Element.ALIGN_CENTER);
                document.add(para_tittle_4);

                String str_bank_ac_type = mParseXML.parseXmlTag(str_bank_details, "bank_info_acc_type");

                Paragraph para_bank_ac_type = new Paragraph("\nBank Account Type: " + str_bank_ac_type, small_bold);
                para_bank_ac_type.setAlignment(Element.ALIGN_LEFT);
                document.add(para_bank_ac_type);

                String str_bank_ac_no = mParseXML.parseXmlTag(str_bank_details, "bank_info_acc_number");

                Paragraph para_bank_ac_no = new Paragraph("Bank Account Number: " + str_bank_ac_no, small_bold);
                para_bank_ac_no.setAlignment(Element.ALIGN_LEFT);
                document.add(para_bank_ac_no);

                if (str_bank_ac_type.equalsIgnoreCase("EFT")) {
                    String str_bank_branch_code = mParseXML.parseXmlTag(str_bank_details, "bank_info_branch_code");

                    Paragraph para_bank_branch_code = new Paragraph("Bank Branch Code: " + str_bank_branch_code, small_bold);
                    para_bank_branch_code.setAlignment(Element.ALIGN_LEFT);
                    document.add(para_bank_branch_code);
                } else {
                    String str_bank_ifsc_code = mParseXML.parseXmlTag(str_bank_details, "bank_info_ifsc_code");

                    Paragraph para_bank_ifsc_code = new Paragraph("Bank IFSC Code: " + str_bank_ifsc_code, small_bold);
                    para_bank_ifsc_code.setAlignment(Element.ALIGN_LEFT);
                    document.add(para_bank_ifsc_code);
                }*/

                //get form 1A details
                /*String str_form1a_details = lst.get(0).getStr_form_1_a();

                //to Form 1-A details tittle
                Paragraph para_tittle_5 = new Paragraph("\nForm 1-A", headerBold);
                para_tittle_5.setAlignment(Element.ALIGN_CENTER);
                document.add(para_tittle_5);

                String str_form1a_insu_agency = mParseXML.parseXmlTag(str_form1a_details, "form1a_info_any_insurance");

                Paragraph para_form1a_insu_agency = new Paragraph("\nIs any insurance agency in force or ever hold by the applicant: " + str_form1a_insu_agency, small_bold);
                para_form1a_insu_agency.setAlignment(Element.ALIGN_LEFT);
                document.add(para_form1a_insu_agency);

                if (str_form1a_insu_agency.equalsIgnoreCase("Yes")) {
                    String str_form1a_insu_name = mParseXML.parseXmlTag(str_form1a_details, "form1a_info_insurance_name");

                    Paragraph para_form1a_insu_name = new Paragraph("Name of the Insurer: " + str_form1a_insu_name, small_bold);
                    para_form1a_insu_name.setAlignment(Element.ALIGN_LEFT);
                    document.add(para_form1a_insu_name);

                    String str_form1a_insu_code = mParseXML.parseXmlTag(str_form1a_details, "form1a_info_insurance_agency_code");

                    Paragraph para_form1a_insu_code = new Paragraph("Agency Code: " + str_form1a_insu_code, small_bold);
                    para_form1a_insu_code.setAlignment(Element.ALIGN_LEFT);
                    document.add(para_form1a_insu_code);

                    String str_form1a_insu_appointment_date = mParseXML.parseXmlTag(str_form1a_details, "form1a_info_insurance_appointment_date");

                    Paragraph para_form1a_insu_appointment_date = new Paragraph("Date of Appointment as Agent: " + str_form1a_insu_appointment_date, small_bold);
                    para_form1a_insu_appointment_date.setAlignment(Element.ALIGN_LEFT);
                    document.add(para_form1a_insu_appointment_date);

                    String str_form1a_insu_cessation_date = mParseXML.parseXmlTag(str_form1a_details, "form1a_info_insurance_cessation_date");
                    str_form1a_insu_cessation_date = str_form1a_insu_cessation_date == null ? "" : str_form1a_insu_cessation_date;

                    Paragraph para_form1a_insu_cessation_date = new Paragraph("Date of Cessation Of Agency: " + str_form1a_insu_cessation_date, small_bold);
                    para_form1a_insu_cessation_date.setAlignment(Element.ALIGN_LEFT);
                    document.add(para_form1a_insu_cessation_date);

                    if (!str_form1a_insu_cessation_date.equals("")) {
                        String str_form1a_insu_cessation_reason = mParseXML.parseXmlTag(str_form1a_details, "form1a_info_insurance_cessation_date");

                        Paragraph para_form1a_insu_cessation_reason = new Paragraph("Reason for Cessation of Agency: " + str_form1a_insu_cessation_reason, small_bold);
                        para_form1a_insu_cessation_reason.setAlignment(Element.ALIGN_LEFT);
                        document.add(para_form1a_insu_cessation_reason);
                    }
                }*/

                //get exam training details
                /*String str_exam_training_details = lst.get(0).getStr_exam_training_details();

                //to exam details tittle
                Paragraph para_tittle_6 = new Paragraph("\nExam Details", headerBold);
                para_tittle_6.setAlignment(Element.ALIGN_CENTER);
                document.add(para_tittle_6);

                String str_exam_details_place = mParseXML.parseXmlTag(str_exam_training_details, "exam_details_place");

                Paragraph para_exam_details_place = new Paragraph("\nPlace Of Exam: " + str_exam_details_place, small_bold);
                para_exam_details_place.setAlignment(Element.ALIGN_LEFT);
                document.add(para_exam_details_place);

                String str_exam_details_language = mParseXML.parseXmlTag(str_exam_training_details, "exam_details_language");

                Paragraph para_exam_details_language = new Paragraph("Language of Exam: " + str_exam_details_language, small_bold);
                para_exam_details_language.setAlignment(Element.ALIGN_LEFT);
                document.add(para_exam_details_language);*/

                //to training details tittle
                /*Paragraph para_tittle_7 = new Paragraph("\nTraining Details", headerBold);
                para_tittle_7.setAlignment(Element.ALIGN_CENTER);
                document.add(para_tittle_7);

                String str_training_start_date = mParseXML.parseXmlTag(str_exam_training_details, "training_details_start_date");

                Paragraph para_training_start_date = new Paragraph("\nTraining Start Date: " + str_training_start_date, small_bold);
                para_training_start_date.setAlignment(Element.ALIGN_LEFT);
                document.add(para_training_start_date);

                String str_training_end_date = mParseXML.parseXmlTag(str_exam_training_details, "training_details_end_date");

                Paragraph para_training_end_date = new Paragraph("Training End Date: " + str_training_end_date, small_bold);
                para_training_end_date.setAlignment(Element.ALIGN_LEFT);
                document.add(para_training_end_date);*/

                document.newPage();

                //terms and condition details
                Paragraph para_tittle_8 = new Paragraph("\nTerms & Conditions on Appointment Of Insurance Agent", headerBold);
                para_tittle_8.setAlignment(Element.ALIGN_CENTER);
                document.add(para_tittle_8);

                //para
                Paragraph para1 = new Paragraph("\nThe Insurance Agent of the SBI LIFE INSURANCE COMPANY LTD, on his appointment as the Insurance Agent of the Company, has to strictly adhere to the terms and conditions as laid down below and also as amended from time to time by the Company.\n" +
                        "\nThe SBI LIFE INSURANCE COMPANY LTD, [hereinafter called as the Company], incorporated under the Companies Act, 1956 for carrying on the business of life insurance and is desirous of obtaining the services of the agent for the purpose of soliciting and procuring life insurance business for the Company and for performing\n" +
                        "\nsuch other functions which are incidental to his acting as an Insurance agent and as may be entrusted to him by the Company from time to time, has framed the following terms and conditions to be strictly followed by its Insurance Agents.\n" +
                        "\nThe Agent, who holds a valid appointment letter to act as an Insurance Agent of the Company, has to strictly follow the following terms and conditions:\n", small_normal);
                document.add(para1);

                //to definations tittle
                Paragraph para_definations_tittle = new Paragraph("\n1. Definitions:", headerBold);
                para_definations_tittle.setAlignment(Element.ALIGN_LEFT);
                document.add(para_definations_tittle);

                Paragraph para_definations = new Paragraph("\nThe following words and expressions used in terms & conditions shall have the meaning assigned to them as under and any words or expressions used herein but not defined shall have the meaning assigned to them in the Insurance Act, 1938, the Insurance Regulatory and Development Authority Act, 1999 or the rules and regulations made under those enactments.\n", small_normal);
                document.add(para_definations);

                Paragraph ph_definations_a = new Paragraph("\na) “Act” means the Insurance Act, 1938;", small_normal);
                ph_definations_a.setIndentationLeft(36);
                document.add(ph_definations_a);

                Paragraph ph_definations_b = new Paragraph("b) “Unit Manager” means the employee of the Company under whose supervision and control the Agent is for the time being placed;", small_normal);
                ph_definations_b.setIndentationLeft(36);
                document.add(ph_definations_b);

                Paragraph ph_definations_c = new Paragraph("c) “Agent” means the person appointed to act as an Insurance agent of the Company in terms of this Agreement;", small_normal);
                ph_definations_c.setIndentationLeft(36);
                document.add(ph_definations_c);

                Paragraph ph_definations_d = new Paragraph("d) “Company” means the SBI Life Insurance Company Limited;", small_normal);
                ph_definations_d.setIndentationLeft(36);
                document.add(ph_definations_d);

                Paragraph ph_definations_e = new Paragraph("e) “IRDAI” means the Insurance Regulatory and Development Authority o f I n d i a established under the IRDA Act; and", small_normal);
                ph_definations_e.setIndentationLeft(36);
                document.add(ph_definations_e);

                Paragraph ph_definations_f = new Paragraph("f) “IRDA Act” means the Insurance Regulatory and Development Authority Act, 1999 (41 of 1999).", small_normal);
                ph_definations_f.setIndentationLeft(36);
                document.add(ph_definations_f);

                Paragraph ph_definations_g = new Paragraph("g) “Rules and Regulations” means the rules and regulations made under the Act and the IRDA Act and include the orders, directives, circulars, guidelines and notifications issued by the IRDAI from time to time.", small_normal);
                ph_definations_g.setIndentationLeft(36);
                document.add(ph_definations_g);

                //to appointment tittle
                Paragraph para_appointment_tittle = new Paragraph("\n2. Appointment:", headerBold);
                para_appointment_tittle.setAlignment(Element.ALIGN_LEFT);
                document.add(para_appointment_tittle);

                Paragraph para_appointment = new Paragraph("\nThe appointment of Insurance Agents shall be subject to the review conducted by SBI Life Insurance Co. Ltd from time to time, except for termination due to fraud or non-performance.\n", small_normal);
                document.add(para_appointment);

                Paragraph ph_appointment_a = new Paragraph("\na) The terms and conditions shall govern the appointment of an Agent for the purposes of soliciting and procuring life insurance business (“business”) for the Company in such areas as may be determined by the Company and notified to the Agent and to perform such other functions as are incidental to his acting as an Insurance Agent and as may be entrusted to him by the Company from time to time. The Agent has to accept the said appointment and the terms and conditions governing such appointment and the agent will be designated as “Insurance Agent”.", small_normal);
                ph_appointment_a.setIndentationLeft(36);
                document.add(ph_appointment_a);

                Paragraph ph_appointment_b = new Paragraph("b) The Agent shall not solicit or procure business from person resident outside of the area notified to him/her.", small_normal);
                ph_appointment_b.setIndentationLeft(36);
                document.add(ph_appointment_b);

                Paragraph ph_appointment_c = new Paragraph("c) The appointment of the Agent in the area mentioned in clause 2(a) shall not preclude the Company from appointing any other person or persons to act as insurance Agent or Corporate Agent of the Company in such area; nor shall such appointment abridge or derogate from the obligations of the Agent under the terms & conditions laid down here.", small_normal);
                ph_appointment_c.setIndentationLeft(36);
                document.add(ph_appointment_c);

                Paragraph ph_appointment_d = new Paragraph("d) The Company may, at its sole discretion, decide on the Insurance products for which the Agent may solicit and procure proposals and to change, add or discontinue any such product at any time.", small_normal);
                ph_appointment_d.setIndentationLeft(36);
                document.add(ph_appointment_d);

                Paragraph ph_appointment_e = new Paragraph("e) Agency appointment may be refused to the applicant if he/she does not fulfill any of the conditions prescribed for appointment of insurance agents.", small_normal);
                ph_appointment_e.setIndentationLeft(36);
                document.add(ph_appointment_e);

                Paragraph ph_appointment_f = new Paragraph("f) An applicant aggrieved by the decision of refusal to grant agency appointment, may submit a review application to VP – Channel Administration for review of the decision.", small_normal);
                ph_appointment_f.setIndentationLeft(36);
                document.add(ph_appointment_f);

                /*//start with new page
                document.newPage();

                //add header manually to new page
                document.add(para_img_logo);
                // For SBI- Life Logo ends

                // To draw line after the sbi logo image
                document.add(new LineSeparator());
                document.add(para_img_logo_after_space_1);*/

                //to definations tittle
                Paragraph para_obligations_tittle = new Paragraph("3. Obligations of the Agent:", headerBold);
                para_obligations_tittle.setAlignment(Element.ALIGN_LEFT);
                document.add(para_obligations_tittle);

                Paragraph para_obligation = new Paragraph("\nDuring the tenure the Agent shall serve the Company as an Insurance Agent with all due and proper diligence, act dutifully and in good faith, observe all instructions given by the Unit Manager or generally by the Company to all it’s Insurance Agents, as to his activities as an Insurance Agent of the Company, and use his best endeavors in the discharge of his obligations under these terms and conditions.", small_normal);
                document.add(para_obligation);

                Paragraph para_obligation_a = new Paragraph("\na) Without prejudice to the generality of the foregoing and, in particular, the Agent shall fulfill such minimum business requirements and persistency criteria as may be prescribed by the Company from time to time, in the matter of procurement of new life insurance business, premium income, number of lives to be assured, number of policies to be sold or in the matter of conversation and preservation of business or in relation to policy- servicing. Agent’s appointment is liable for termination for not meeting the requirements/criteria as may be defined and communicated by the Company from time to time.", small_normal);
                para_obligation_a.setIndentationLeft(36);
                document.add(para_obligation_a);

                Paragraph para_obligation_b = new Paragraph("\nb) In order to satisfactorily discharge his obligations under these terms and conditions, the Agent shall fully familiarize himself with all the insurance products offered for sale by the Company including the rate of premium charged for each such product, and the policies and practices followed by the Company in the matter of customer service and conservation of business.", small_normal);
                para_obligation_b.setIndentationLeft(36);
                document.add(para_obligation_b);

                Paragraph para_obligation_c = new Paragraph("\nc) The Agent shall not allow or offer to allow, either directly or indirectly, as an inducement to any person to take out or renew or continue an insurance policy, any rebate of the whole or part of the commission payable to him or otherwise, except as may be allowed under the Rules and Regulations, of the premium shown on the policy.", small_normal);
                para_obligation_c.setIndentationLeft(36);
                document.add(para_obligation_c);

                Paragraph para_obligation_d = new Paragraph("\nd) The Agent shall familiarize himself with all the statutory requirements as may relate to the discharge of his obligations as an Insurance Agent and shall fully conform to, comply with and observe and follow such requirements and in the event of his/her failure to do so, the Agent shall be personally liable for all consequences thereto and shall also indemnify the Company in respect of any loss or injury caused to or suffered by it as a result of such failure.", small_normal);
                para_obligation_d.setIndentationLeft(36);
                document.add(para_obligation_d);

                Paragraph para_obligation_e = new Paragraph("\ne) The Code of Conduct as prescribed by Clause VIII of the IRDAI Guidelines on Appointment of Insurance Agents, 2015 dated March 16, 2015 and/or as amended from time to time, any statutory modification or substitution thereof and any Code of Conduct as prescribed by the Company shall be deemed to have been set out in and be part of these terms and conditions. The Agent shall scrupulously follow and comply with the same. The Code of Conduct is incorporated in the letter of appointment issued to you by the Company.", small_normal);
                para_obligation_e.setIndentationLeft(36);
                document.add(para_obligation_e);

                Paragraph para_obligation_f = new Paragraph("\nf) Without prejudice to the generality of the obligations under Clause 3.5, and in particular the Agent shall strictly observe, conform to and comply with the, requirements in relation to customer service, insurance advertisements and on any matter relating to Insurance Agents as prescribed by the Rules & Regulations for insurance agents, from time to time.", small_normal);
                para_obligation_f.setIndentationLeft(36);
                document.add(para_obligation_f);

                Paragraph para_obligation_g = new Paragraph("\ng) All communications and all information whether written, verbal or visual and all other material supplied to or obtained by the Agent in the course of or as a result of the discharge of his obligations under these terms and conditions and all information in relation to any particular assurance obtained by him as Agent shall be treated by the Agent as confidential and shall not be disclosed by him to any third party without prior written consent of the Company.", small_normal);
                para_obligation_g.setIndentationLeft(36);
                document.add(para_obligation_g);

                Paragraph para_obligation_h = new Paragraph("\nh) The Agent shall hold in trust for the company any application, report, form, security or other property received by him in the course of his duties as the Agent of the Company and shall, dispatch, makeover to the Company all such applications, reports, forms, securities or other properties within twenty four hours of such receipt excluding public holidays. The Agent shall not have the authority, either expressed or implied to receive any payment except the premium towards proposal deposit from the public on behalf of the Company. If, however, the agent receives any payment whatsoever, the same shall be remitted to the company within 24 hours of its receipt. Under no circumstances, shall the payment made to the agent be deemed to be a payment made to the Company.", small_normal);
                para_obligation_h.setIndentationLeft(36);
                document.add(para_obligation_h);

                Paragraph para_obligation_i = new Paragraph("\ni) The Agent shall attend all meetings, conventions, workshops or the like called for by the Company and shall also observe, follow and comply with any directive or direction, issued by the Company generally in relation to its insurance agents or with particular reference to the Agent.", small_normal);
                para_obligation_i.setIndentationLeft(36);
                document.add(para_obligation_i);

                Paragraph para_obligation_j = new Paragraph("\nj) The Agent shall keep true, full and correct records of all the transactions entered into by him as Agent of the company, and if so required by the Company, furnish for its inspection (including making of copies thereof) all or any such records, early.", small_normal);
                para_obligation_j.setIndentationLeft(36);
                document.add(para_obligation_j);

                /*//start with new page
                document.newPage();

                //add header manually to new page
                document.add(para_img_logo);
                // For SBI- Life Logo ends

                // To draw line after the sbi logo image
                document.add(new LineSeparator());
                document.add(para_img_logo_after_space_1);*/

                Paragraph para_obligation_k = new Paragraph("\nk) During the continuance of his Agency, the Agent will engage in activities as an Insurance Agent exclusively of the Company and not of any other insurer carrying on life insurance business or in soliciting or selling of even other than insurance products or products, which are similar to or identical to the insurance products of the Company. The Agent also undertakes not to have any tie up with any other insurer as an employee or director or promoter and not to hold any insurance activity related license or hold any position with any Corporate Agent/ Broker/ TPA / Insurance Marketing Firm/ any other insurance intermediary.", small_normal);
                para_obligation_k.setIndentationLeft(36);
                document.add(para_obligation_k);

                Paragraph para_obligation_l = new Paragraph("\nl) The Agent has no authority to and shall not take part in any dispute or institute or defend any proceedings or settle or attempt to settle or make any admission concerning any dispute, proceeding or other claim relating to the business or affairs of the Company except with the prior written permission of the Company.", small_normal);
                para_obligation_l.setIndentationLeft(36);
                document.add(para_obligation_l);

                Paragraph para_obligation_m = new Paragraph("\nm) If so required by the Company, the Agent shall open and maintain a bank account in his own name (only) to which the Company may credit the commission, remuneration or reward or any other amount due and payable to the Agent. All the expenses for opening and maintaining the account shall be borne by the Agent.", small_normal);
                para_obligation_m.setIndentationLeft(36);
                document.add(para_obligation_m);

                Paragraph para_obligation_n = new Paragraph("\nn) The Agent shall indemnify and keep indemnified the Company against loss or injury or costs or expenses suffered or incurred by the Company in consequence of any act or omission of the Agent tantamounting to a breach of these terms and conditions.", small_normal);
                para_obligation_n.setIndentationLeft(36);
                document.add(para_obligation_n);

                Paragraph para_obligation_o = new Paragraph("\no) Nothing in these terms and conditions or otherwise shall entitle the Agent to make any representation or warranties on behalf of the Company or to enter into any contract or agreement on behalf of the Company or bind the Company in any manner whatsoever by his/her acts of commission or omission.", small_normal);
                para_obligation_o.setIndentationLeft(36);
                document.add(para_obligation_o);

                Paragraph para_obligation_p = new Paragraph("\np) The Agent covenants with the Company that he is qualified to act as an Insurance Agent under law and does not suffer any disqualification rendering invalid his appointment as Agent.", small_normal);
                para_obligation_p.setIndentationLeft(36);
                document.add(para_obligation_p);

                //to appointment tittle
                Paragraph para_remuneration_tittle = new Paragraph("\n4. Remuneration:", headerBold);
                para_remuneration_tittle.setAlignment(Element.ALIGN_LEFT);
                document.add(para_remuneration_tittle);

                Paragraph para_remuneration1 = new Paragraph("\nAs consideration for soliciting and procuring insurance business for the Company the Agent shall be paid by way of commission / remuneration on such scale as may be notified by the Company from time to time subject to IRDAI Regulations or Guidelines issued and/or the Company’s Board approved policy for ‘Appointment of Insurance Agents’.\n", small_normal);
                document.add(para_remuneration1);

                Paragraph para_remuneration2 = new Paragraph("\nThe commission / remuneration shall be payable to the Agent only for life insurance business procured by him which results in policies issued by the Company and not in respect of any incomplete or invalid transaction or proposal.\n", small_normal);
                document.add(para_remuneration2);

                Paragraph para_remuneration3 = new Paragraph("\nSubject to the Rules and Regulations, The Act and the IRDA Act, the Company shall have absolute freedom to revise the scale of commission in respect of one or more of the insurance products offered for sale by the Company, provided that the Agent shall be given notice of not less than 30 days of such revision through one or more methods of communication generally employed by the Company for internal communication. The revised rates shall be made applicable prospectively and shall not be applicable to policies issued in pursuance of proposals already registered with the Company, unless otherwise, stipulated by the Act, the IRDA Act or Rules and Regulations.\n", small_normal);
                document.add(para_remuneration3);

                Paragraph para_remuneration4 = new Paragraph("\nIn the event of any change in the scale of commission, brought about by reason of amendment/changes/issuance of the Rules and Regulations or the Act or the IRDA Act or the Company’s Board approved policy for ‘Appointment of Insurance Agents’, the Company’s liability to pay commission to the Agent in respect of any policies issued by it shall be limited to what is permissible under such law, rules, regulations, order or directive.\n", small_normal);
                document.add(para_remuneration4);

                Paragraph para_remuneration5 = new Paragraph("\nThe Agent shall not be entitled to any commission in respect of the sale of a policy effected through him which is declared null and void by the Company for any reason or which has been cancelled for any reason whatsoever.\n", small_normal);
                document.add(para_remuneration5);

                Paragraph para_remuneration6 = new Paragraph("\nPayment of commission or remuneration or reward to Insurance Agents shall be made as per the Reward & Recognition schemes designed and approved by the Company from time to time with an objective to appropriately incentivize high performance. The above payments will be subject to limits prescribed as per extant applicable Guidelines/Regulations issued by IRDAI from time to time.\n", small_normal);
                document.add(para_remuneration6);

                Paragraph para_remuneration7 = new Paragraph("\nWhere so ordered by a court of law or a statutory or other authority who is empowered by law to so order, the Company may withhold or retain the commission or remuneration or reward or any other amount due and payable to the Agent or to recover and remit to such court or statutory or other authority the amount so ordered to be remitted.\n", small_normal);
                document.add(para_remuneration7);

                /*//start with new page
                document.newPage();

                //add header manually to new page
                document.add(para_img_logo);
                // For SBI- Life Logo ends

                // To draw line after the sbi logo image
                document.add(new LineSeparator());
                document.add(para_img_logo_after_space_1);*/

                Paragraph para_remuneration8 = new Paragraph("\nAll payments made to the Agent shall be subject to deduction of taxes as per the applicable tax laws.\n", small_normal);
                document.add(para_remuneration8);

                Paragraph para_remuneration9 = new Paragraph("\nThe Company shall pay the standard renewal commission as prescribed in the product even in case of his agency getting cancelled in accordance with the Company’s Board approved policy on ‘Appointment of Insurance Agents’, provided the agent is eligible for such renewal commission as per the prevailing rules, regulations and policies. However, the Company reserves the right to forfeit commission in certain cases like fraud, forgery, misconduct, etc. on grounds of disciplinary action as approved by the disciplinary authority of the Company.\n", small_normal);
                document.add(para_remuneration9);

                Paragraph para_remuneration10 = new Paragraph("\nIn case of unfortunate event of death of an Agent, hereditary commission will continue to be paid to the nominee or heirs of the Agent for so long as such commission would have been payable had the Insurance Agent been alive, in accordance with the Company’s Board approved policy on ‘Appointment of Insurance Agents’ subject to the guidelines, rules and regulations of the IRDAI and the Company..\n", small_normal);
                document.add(para_remuneration10);

                Paragraph para_remuneration11 = new Paragraph("\nThe Company reserves the right to recover any commission, remuneration or reward paid wrongly, or in excess of what is due and payable to the Agent from any amount due and payable to the Agent.\n", small_normal);
                document.add(para_remuneration11);

                Paragraph para_remuneration12 = new Paragraph("\nWithout prejudice to the generality of what is stated in the above clause, the Company shall have the absolute right to recover or retain from any amount due and payable to the Agent, any commission, remuneration or reward paid to him under one or more of the following situations:\n", small_normal);
                document.add(para_remuneration12);

                Paragraph para_remuneration_a = new Paragraph("\na) Where any policy issued in pursuance of a proposal procured by the Agent is declared null and void by the Company.", small_normal);
                para_remuneration_a.setIndentationLeft(36);
                document.add(para_remuneration_a);

                Paragraph para_remuneration_b = new Paragraph("b) Where any cheque or other instrument tendered or any payment effected through the ECS or similar mode of payment towards premium is dishonored or as the case may be reversed or countermanded for any reason;", small_normal);
                para_remuneration_b.setIndentationLeft(36);
                document.add(para_remuneration_b);

                Paragraph para_remuneration_c = new Paragraph("c) Where a policyholder cancels a policy by exercising the “Free Look Option”. Explanation: For the purpose of this clause “Free Look Option” shall mean the option referred to in sub regulation (2) of Regulation 6 of the Insurance Regulatory and Development Authority (Protection of Policyholder’s Interest) Regulation 2002.", small_normal);
                para_remuneration_c.setIndentationLeft(36);
                document.add(para_remuneration_c);

                Paragraph para_remuneration_d = new Paragraph("d) W here the Company has been directed to repay or refund any premium paid under a Policy on account of any order, judgment or direction of any court, Tribunal, Consumer forum or Insurance Ombudsman or where the Company is satisfied that such repayment or refund is in the best interest of the Company.", small_normal);
                para_remuneration_d.setIndentationLeft(36);
                document.add(para_remuneration_d);

                Paragraph para_remuneration_e = new Paragraph("e) If any Policy issued in pursuance of a proposal proceed by the Agent is allowed to lapse within months of its issue", small_normal);
                para_remuneration_e.setIndentationLeft(36);
                document.add(para_remuneration_e);

                Paragraph para_remuneration_f = new Paragraph("f) Where on a complaint by a Policyholder, the Company or, as the case may be, a court, tribunal or other authority empowered by law to adjudicate the complaint, holds that the Policyholder has been sold a Policy not opted for by him or under a plan of insurance different from or on terms other than those agreed to by him, resulting in the cancellation of the Policy, the Company shall be entitled to recover from the commission, remuneration or reward or any other amount due and payable to the Agent, the commission paid to him in respect of that Policy, besides withdrawing the credit of the business allotted to the Agent for any other purpose in relation to the said Policy.", small_normal);
                para_remuneration_f.setIndentationLeft(36);
                document.add(para_remuneration_f);

                //to appointment tittle
                Paragraph para_suspension_tittle = new Paragraph("\n5. Suspension or Cancellation of Agency:", headerBold);
                para_suspension_tittle.setAlignment(Element.ALIGN_LEFT);
                document.add(para_suspension_tittle);

                Paragraph para_suspension1 = new Paragraph("As per terms and conditions laid down here, Company holds the rights to suspend or cancel the appointment in any of the following events:-\n", small_normal);
                document.add(para_suspension1);

                Paragraph para_suspension_tittle1 = new Paragraph("\n5.1 Suspension or cancellation of Appointment:", headerBold);
                para_suspension_tittle1.setAlignment(Element.ALIGN_LEFT);
                para_suspension_tittle1.setIndentationLeft(36);
                document.add(para_suspension_tittle1);

                Paragraph para_suspension5_1 = new Paragraph("Appointment may be suspended or cancelled after due notice and after giving the Agent reasonable opportunity of being heard:--\n", small_normal);
                para_suspension5_1.setIndentationLeft(36);
                document.add(para_suspension5_1);

                Paragraph para_suspension5_1_a = new Paragraph("a) where the Agent does not hold a valid appointment letter;", small_normal);
                para_suspension5_1_a.setIndentationLeft(36);
                document.add(para_suspension5_1_a);

                Paragraph para_suspension5_1_b = new Paragraph("b) where the appointment to act as an Insurance Agent issued to the agent is cancelled for any reason; upon the death of the Agent;", small_normal);
                para_suspension5_1_b.setIndentationLeft(36);
                document.add(para_suspension5_1_b);

                Paragraph para_suspension5_1_c = new Paragraph("c) where a petition to declare the Agent as an insolvent is presented to a court of competent Jurisdiction;", small_normal);
                para_suspension5_1_c.setIndentationLeft(36);
                document.add(para_suspension5_1_c);

                Paragraph para_suspension5_1_d = new Paragraph("d) where on account of any change in law the operation of these terms and conditions has become unlawful;", small_normal);
                para_suspension5_1_d.setIndentationLeft(36);
                document.add(para_suspension5_1_d);

                Paragraph para_suspension5_1_e = new Paragraph("e) where the Company is convinced that the Agent is guilty of an act involving moral turpitude and that it would reflect poorly on the fair name or the conduct of affairs or of business of the Company;", small_normal);
                para_suspension5_1_e.setIndentationLeft(36);
                document.add(para_suspension5_1_e);

                Paragraph para_suspension5_1_f = new Paragraph("f) where the Agent is convicted of an offence and sentenced to any term of imprisonment by a court of competent jurisdiction.\n" +
                        "Note: - The fact that the Agent has preferred an appeal against such conviction or sentence shall not derogate from the operation of this provision during the pendency of such appeal. Further that the Agent has been acquitted in any proceeding or in any appeal initiated against him on a compliant made to or by the Company shall not entitle him to claim any compensation or damages against the Company", small_normal);
                para_suspension5_1_f.setIndentationLeft(36);
                document.add(para_suspension5_1_f);

                Paragraph para_suspension5_1_g = new Paragraph("g) violates the provisions of the Insurance Act,1938, Insurance Regulatory and Development Authority Act, 1999 or rules or regulations, made there under as amended from time to time;", small_normal);
                para_suspension5_1_g.setIndentationLeft(36);
                document.add(para_suspension5_1_g);

                Paragraph para_suspension5_1_h = new Paragraph("h) attracts any of the disqualifications mentioned in Guidelines/Regulations issued by IRDAI from time to time..", small_normal);
                para_suspension5_1_h.setIndentationLeft(36);
                document.add(para_suspension5_1_h);

                Paragraph para_suspension5_1_i = new Paragraph("i) fails to comply with the code of conduct stipulated in the Company’s Policy for Appointment of Insurance Agents and notified from time to time.", small_normal);
                para_suspension5_1_i.setIndentationLeft(36);
                document.add(para_suspension5_1_i);

                Paragraph para_suspension5_1_j = new Paragraph("j) violates terms of appointment.", small_normal);
                para_suspension5_1_j.setIndentationLeft(36);
                document.add(para_suspension5_1_j);

                /*//start with new page
                document.newPage();

                //add header manually to new page
                document.add(para_img_logo);
                // For SBI- Life Logo ends

                // To draw line after the sbi logo image
                document.add(new LineSeparator());
                document.add(para_img_logo_after_space_1);*/

                Paragraph para_suspension5_1_k = new Paragraph("k) fails to furnish any information relating to his/her activities as an agent as required by the Company or the Authority;", small_normal);
                para_suspension5_1_k.setIndentationLeft(36);
                document.add(para_suspension5_1_k);

                Paragraph para_suspension5_1_l = new Paragraph("l) fails to comply with the directions issued by the Authority;", small_normal);
                para_suspension5_1_l.setIndentationLeft(36);
                document.add(para_suspension5_1_l);

                Paragraph para_suspension5_1_m = new Paragraph("m) furnishes wrong or false information; or conceal or fail to disclose material facts in the application submitted for appointment of Agent or during the period of its validity.", small_normal);
                para_suspension5_1_m.setIndentationLeft(36);
                document.add(para_suspension5_1_m);

                Paragraph para_suspension5_1_n = new Paragraph("n) does not submit periodical returns as required by the Company/Authority;", small_normal);
                para_suspension5_1_n.setIndentationLeft(36);
                document.add(para_suspension5_1_n);

                Paragraph para_suspension5_1_o = new Paragraph("o) does not co-operate with any inspection or enquiry conducted by the Authority;", small_normal);
                para_suspension5_1_o.setIndentationLeft(36);
                document.add(para_suspension5_1_o);

                Paragraph para_suspension5_1_p = new Paragraph("p) fails to resolve the complaints of the policyholders or fails to give a satisfactory reply to the Authority in this behalf;", small_normal);
                para_suspension5_1_p.setIndentationLeft(36);
                document.add(para_suspension5_1_p);

                Paragraph para_suspension5_1_q = new Paragraph("q) On failure to perform any other duties or obligation lawfully required of him or under these terms and conditions generally as an insurance agent of the Company,", small_normal);
                para_suspension5_1_q.setIndentationLeft(36);
                document.add(para_suspension5_1_q);

                Paragraph para_suspension5_1_r = new Paragraph("r) Where there is allegation of fraud, dishonesty or misdemeanor against the agent by any policy holder of the Company or an officer or employees of the Company.", small_normal);
                para_suspension5_1_r.setIndentationLeft(36);
                document.add(para_suspension5_1_r);

                Paragraph para_suspension5_1_s = new Paragraph("s) Further the Company shall also have the right to suspend/cancel the agency if the Company feels that the continuation of the agency may be against the public interest", small_normal);
                para_suspension5_1_s.setIndentationLeft(36);
                document.add(para_suspension5_1_s);

                Paragraph para_suspension2 = new Paragraph("\nWhere the suspension / cancellation of the appointment of the Agent is being contemplated or is in process, or pending enquiry into the allegations of fraud, misappropriations etc against the Agent, the Company may, by notice in writing, direct the Agent not to procure business or to perform his functions as the Agent of the Company for such period as may be specified. Any breach by the Agent of any such direction shall, besides disentitling the Agent to any remuneration for any business brought in by him during the period of operation of prohibition or for any other benefit pertaining to such business, also be construed as a breach of this terms and conditions. W here, however, the Company does not initiate or proceed with the termination , the Agent shall not be entitled to any compensation or damages on the ground of loss of business or of profit or on any other ground whatsoever in respect of any such direction as is referred to above against the Company.", small_normal);
                para_suspension2.setIndentationLeft(36);
                document.add(para_suspension2);

                Paragraph para_suspension_tittle2 = new Paragraph("\n5.2 Manner of holding enquiry before/after suspension of appointment of the Insurance Agent:", headerBold);
                para_suspension_tittle2.setAlignment(Element.ALIGN_LEFT);
                para_suspension_tittle2.setIndentationLeft(36);
                document.add(para_suspension_tittle2);

                Paragraph para_suspension5_2_a = new Paragraph("a) The appointment of an Insurance Agent shall be cancelled only after an enquiry has been conducted in accordance with the procedure specified by Guidelines/Regulations issued by IRDAI from time to time.", small_normal);
                para_suspension5_2_a.setIndentationLeft(36);
                document.add(para_suspension5_2_a);

                Paragraph para_suspension5_2_b = new Paragraph("b) For the purpose of holding an enquiry, the Company may appoint an Officer as an Enquiry Officer within 15 days of the issue of the suspension order;", small_normal);
                para_suspension5_2_b.setIndentationLeft(36);
                document.add(para_suspension5_2_b);

                Paragraph para_suspension5_2_c = new Paragraph("c) The Enquiry Officer shall issue a show cause notice to the Insurance Agent at the registered address of the insurance agent calling for all information / data as deemed necessary to conduct the enquiry and grant the insurance agent a time of 21 days from date of receipt of the show cause notice, for submission of his/her reply and such information / data called for;", small_normal);
                para_suspension5_2_c.setIndentationLeft(36);
                document.add(para_suspension5_2_c);

                Paragraph para_suspension5_2_d = new Paragraph("d) The insurance Agent may, within 21 days from the date of receipt of such notice, furnish to the enquiry officer a reply to the Show cause notice together with copies of documentary or other evidence relied on by him or sought by the Enquiry Officer;", small_normal);
                para_suspension5_2_d.setIndentationLeft(36);
                document.add(para_suspension5_2_d);

                Paragraph para_suspension5_2_e = new Paragraph("e) The Enquiry Officer shall give a reasonable opportunity of hearing to the insurance agent to enable him to make submissions in support of his/her reply;", small_normal);
                para_suspension5_2_e.setIndentationLeft(36);
                document.add(para_suspension5_2_e);

                Paragraph para_suspension5_2_f = new Paragraph("f) The Insurance Agent may either appear in person or through any person duly authorised by him to present his case, provided however that the prior approval of the Company is obtained for the appearance of the ‘Authorised Person’;", small_normal);
                para_suspension5_2_f.setIndentationLeft(36);
                document.add(para_suspension5_2_f);

                Paragraph para_suspension5_2_g = new Paragraph("g) If it is considered necessary, the Enquiry Officer may require the Company to present its case through one of its officers;", small_normal);
                para_suspension5_2_g.setIndentationLeft(36);
                document.add(para_suspension5_2_g);

                Paragraph para_suspension5_2_h = new Paragraph("h) If it is considered necessary, the Enquiry Officer may call for feedback/information from any other related entity during the course of enquiry;", small_normal);
                para_suspension5_2_h.setIndentationLeft(36);
                document.add(para_suspension5_2_h);

                Paragraph para_suspension5_2_i = new Paragraph("i) If it is considered necessary, the Enquiry Officer may call for additional papers from the Insurance Agent;", small_normal);
                para_suspension5_2_i.setIndentationLeft(36);
                document.add(para_suspension5_2_i);

                Paragraph para_suspension5_2_j = new Paragraph("j) The Enquiry Officer shall make all necessary efforts to complete the proceeding at the earliest but in no case beyond 45 days of the commencement of the enquiry", small_normal);
                para_suspension5_2_j.setIndentationLeft(36);
                document.add(para_suspension5_2_j);

                Paragraph para_suspension5_2_k = new Paragraph("k) Provided that in case the enquiry cannot be completed within the prescribed time limit of 45 days as mentioned in (10) above; the enquiry officer may seek additional time from the Company stating the reason thereof;", small_normal);
                para_suspension5_2_k.setIndentationLeft(36);
                document.add(para_suspension5_2_k);

                Paragraph para_suspension5_2_l = new Paragraph("l) The Enquiry Officer shall, after taking into account all relevant facts and submissions made by the Insurance Agent, shall furnish a report making his/her recommendations to the Designated Official. The Designated Official shall pass a final order in writing with reasons. The order of designated official shall be signed and dated and communicated to the agent.", small_normal);
                para_suspension5_2_l.setIndentationLeft(36);
                document.add(para_suspension5_2_l);

                Paragraph para_procedure_cancelation_tittle = new Paragraph("\n5.3 Procedure for Cancellation of Agency:", headerBold);
                para_procedure_cancelation_tittle.setAlignment(Element.ALIGN_LEFT);
                para_procedure_cancelation_tittle.setIndentationLeft(36);
                document.add(para_procedure_cancelation_tittle);

                Paragraph para_procedure_cancelation = new Paragraph("On the issue of the final order for cancellation of agency of the Insurance Agent, the agent shall cease to act as an Insurance Agent from the date of the final order. Registration of new business by the suspended/cancelled agent will be stopped.", small_normal);
                para_procedure_cancelation.setIndentationLeft(36);
                document.add(para_procedure_cancelation);

                /*//start with new page
                document.newPage();

                //add header manually to new page
                document.add(para_img_logo);
                // For SBI- Life Logo ends

                // To draw line after the sbi logo image
                document.add(new LineSeparator());
                document.add(para_img_logo_after_space_1);*/

                Paragraph para_cancelation_effect_tittle = new Paragraph("5.4 Effect of suspension/cancellation of Agency appointment:", headerBold);
                para_cancelation_effect_tittle.setAlignment(Element.ALIGN_LEFT);
                para_cancelation_effect_tittle.setIndentationLeft(36);
                document.add(para_cancelation_effect_tittle);

                Paragraph para_cancelation_effect = new Paragraph("On and from the date of suspension or cancellation of the agency, the Insurance Agent, shall cease to act as an Insurance Agent.", small_normal);
                para_cancelation_effect.setIndentationLeft(36);
                document.add(para_cancelation_effect);

                Paragraph para_cancelation_effect_a = new Paragraph("a) The Appointment letter and Identity card has to be submitted to the Company by the agent whose appointment has been cancelled by the Company within 7 days of issuance of final order effecting cancellation of appointment.", small_normal);
                para_cancelation_effect_a.setIndentationLeft(36);
                document.add(para_cancelation_effect_a);

                Paragraph para_cancelation_effect_b = new Paragraph("b) The Company shall black list the agent and enter the details of the agent whose appointment is suspended/cancelled into the black listed agents database maintained by the Authority and the centralized list of agents database maintained by the Authority, in online mode, immediately after issuance of the order effecting suspension/cancellation. Further, the Company also reserves the right to display the order of suspension/cancelation of appointment of Insurance Agent on its W ebsite.", small_normal);
                para_cancelation_effect_b.setIndentationLeft(36);
                document.add(para_cancelation_effect_b);

                Paragraph para_cancelation_effect_c = new Paragraph("c) In case a suspension is revoked in respect of any agent on conclusion of disciplinary action by way of issuance of a speaking order by Designated Official, the details of such agent shall be removed from list of black listed Agents as soon as the Speaking Order revoking his/her suspension is issued.", small_normal);
                para_cancelation_effect_c.setIndentationLeft(36);
                document.add(para_cancelation_effect_c);

                Paragraph para_cancelation_effect_d = new Paragraph("d) The Company shall also inform other insurers, Life or General or Health Insurer or mono line insurer with whom he/she is acting as an agent, of the action taken against the Agent for their records and necessary action.", small_normal);
                para_cancelation_effect_d.setIndentationLeft(36);
                document.add(para_cancelation_effect_d);

                Paragraph para_appeal_provision_tittle = new Paragraph("\n5.5 Appeal Provision:", headerBold);
                para_appeal_provision_tittle.setAlignment(Element.ALIGN_LEFT);
                para_appeal_provision_tittle.setIndentationLeft(36);
                document.add(para_appeal_provision_tittle);

                Paragraph para_appeal_provision = new Paragraph("An agent who is aggrieved by the order of cancellation can appeal to the Company within 45 days of the order. The Company shall appoint an Appellate Officer who shall examine the appeal and give his decision in the matter in writing within 30 days of receipt of the appeal.", small_normal);
                para_procedure_cancelation.setIndentationLeft(36);
                document.add(para_procedure_cancelation);

                Paragraph para_surrender_tittle = new Paragraph("\n5.6 Procedure to be followed in respect of resignation/surrender of appointment by an Insurance Agent:", headerBold);
                para_surrender_tittle.setAlignment(Element.ALIGN_LEFT);
                para_surrender_tittle.setIndentationLeft(36);
                document.add(para_surrender_tittle);

                Paragraph para_surrender_a = new Paragraph("\na) In case an insurance agent appointed by the Company wishes to surrender his agency with the Company, he/she shall surrender his appointment letter and identity card to the designated official of the Company.", small_normal);
                para_surrender_a.setIndentationLeft(36);
                document.add(para_surrender_a);

                Paragraph para_surrender_b = new Paragraph("\nb) The Company shall issue the cessation certificate as detailed in Form 1-C within a period of 15 days from the date of resignation or surrender of appointment.", small_normal);
                para_surrender_b.setIndentationLeft(36);
                document.add(para_surrender_b);

                Paragraph para_surrender_c = new Paragraph("\nc) An agent who has surrendered his appointment may seek fresh appointment with other insurer. In such a case, the agent has to furnish to the new insurer all the details of his/her previous agency and produce Cessation Certificate issued by the previous insurer issued in Form I-C, along with his agency application form.", small_normal);
                para_surrender_c.setIndentationLeft(36);
                document.add(para_surrender_c);

                Paragraph para_surrender_d = new Paragraph("\nd) Upon the cancellation of the Agency as per terms and conditions, the Agent shall not for a period of ninety days thereafter solicit or procure, directly or indirectly, any life insurance business for any other life insurance company or be engaged, in any manner, in any competing activity against the Company.", small_normal);
                para_surrender_d.setIndentationLeft(36);
                document.add(para_surrender_d);

                Paragraph para_surrender1 = new Paragraph("\nUpon the cancellation of the Agency as per the terms and conditions, the Agent shall forthwith return to the Company all materials (including printed materials) and stores belonging to the Company which are in his possession or custody. On the neglect or failure of the Agent to do so, the Company shall be entitled, without being accused of trespass, to enter any premises occupied by the Agent for the purpose of recovering such materials and stores.", small_normal);
                document.add(para_surrender1);

                Paragraph para_surrender2 = new Paragraph("\nUpon the cancellation of the Agency as per terms and conditions, the Agent shall not in any manner represent himself as the Agent of the Company.", small_normal);
                document.add(para_surrender2);

                Paragraph para_surrender3 = new Paragraph("\nUpon the cancellation of the Agency as per terms and conditions, the Company shall be at liberty to publish notices of such cancellation in any newspaper or other publications or to communicate the same together with the grounds for such cancellation to the policyholders, other offices and Agents of the Company or to any other insurance Company or to the general public and of the fact that the Agent is not empowered to act for or on behalf of the Company or bind or represent the Company in any manner whatsoever.", small_normal);
                document.add(para_surrender3);

                //to agent not employee tittle
                Paragraph para_agent_not_emp_tittle = new Paragraph("\n6. Agent not an employee:", headerBold);
                para_agent_not_emp_tittle.setAlignment(Element.ALIGN_LEFT);
                document.add(para_agent_not_emp_tittle);

                Paragraph para_agent_not_emp1 = new Paragraph("\nNothing in this terms and conditions shall imply or be construed to constitute the relationship between the Company and the Agent as anything other than that of an insurer and Insurance Agent.\n", small_normal);
                document.add(para_agent_not_emp1);

                Paragraph para_agent_not_emp2 = new Paragraph("\nIt shall not be open to the Agent to raise any claim of being an employee of the Company or for preference in the matter of recruitment to the service of the Company.\n", small_normal);
                document.add(para_agent_not_emp2);

                /*//start with new page
                document.newPage();

                //add header manually to new page
                document.add(para_img_logo);
                // For SBI- Life Logo ends

                // To draw line after the sbi logo image
                document.add(new LineSeparator());
                document.add(para_img_logo_after_space_1);*/

                //to agent not employee tittle
                Paragraph para_miscellaneous_tittle = new Paragraph("\n7. MISCELLANEOUS:", headerBold);
                para_miscellaneous_tittle.setAlignment(Element.ALIGN_LEFT);
                document.add(para_miscellaneous_tittle);

                Paragraph para_miscellaneous1 = new Paragraph("\nAs per these terms and conditions, unless the context requires, words importing the singular shall include the plural and vice-versa and words importing a gender shall include every gender and reference to persons shall include bodies incorporate and unincorporated. These terms and conditions shall be operative from the date of appointment of the agent and binding on them.\n", small_normal);
                document.add(para_miscellaneous1);

                Paragraph para_miscellaneous2 = new Paragraph("\nNo waiver, alteration, variation or addition to this laid terms and conditions shall be effective unless made in writing on or after the date of appointment and / or otherwise accepted by the Agent and the authorized representative of the Company.\n", small_normal);
                document.add(para_miscellaneous2);

                Paragraph para_miscellaneous3 = new Paragraph("\nThe Company shall have a first lien upon all sums payable to the Agent in respect of any indebtedness of the Agent to the Company. The Company is entitled to set off, adjust or otherwise recover from the moneys due and payable to the Agent all such sums as are due to the Company, on account of such indebtedness of the Agent, including any indebtedness arising out of breach of any covenants as per terms and conditions on appointment of Agent.\n", small_normal);
                document.add(para_miscellaneous3);

                Paragraph para_miscellaneous4 = new Paragraph("\nThe interpretation, construction and effect of any of the provisions of these terms and conditions on appointment of Agent laid down by the Company and amended time to time shall be final and binding on the Agent.\n", small_normal);
                document.add(para_miscellaneous4);

                Paragraph para_miscellaneous5 = new Paragraph("\nAll notices, documents, consents, approvals or other communications (“Notice”) to be given hereunder shall be in writing and shall be transmitted by registered post or by facsimile or other electronic means in a form generating a record copy to the party being served at the relevant address for that party. Notice sent by mail shall be deemed to have been duly served not later than four working days after the date of posting. Any Notice sent by facsimile or other electronic means shall be deemed to have been duly served at the time of transmission (if transmitted during normal business hours at the location of the recipient and if not so transmitted then at the start of normal business hours on the next day which is a business day for the company).\n", small_normal);
                document.add(para_miscellaneous5);

                Paragraph para_miscellaneous6 = new Paragraph("\nIf any term or provision in these terms and conditions on appointment of Agent shall be held to be illegal or unenforceable, in whole or in part, under any enactment or rule or law, such term or provision or part shall to that extent be deemed not to form part of these terms and conditions but the validity and enforceability of the remainder of these terms and conditions shall not be affected.\n", small_normal);
                document.add(para_miscellaneous6);

                Paragraph para_miscellaneous7 = new Paragraph("\nThe waiver or forbearance or failure of the Company in any one or more instances upon the performance of any provisions of these terms and conditions on appointment of agent shall not be construed as a waiver or relinquishment of the Company’s rights and the Agent’s obligations in respect of due performance of obligations shall continue in full force and effect.\n", small_normal);
                document.add(para_miscellaneous7);

                Paragraph para_miscellaneous8 = new Paragraph("\nThe Agency shall be governed by all the related regulations, Acts and Statutes besides these terms and conditions. These terms and conditions shall not substitute any of the statutory provisions but they are in addition to the statutory provisions governing the appointment of Insurance Agents.\n", small_normal);
                document.add(para_miscellaneous8);

                Paragraph para_miscellaneous9 = new Paragraph("\n“I hereby agree to abide by the terms and conditions of Appointment as an Insurance Agent.”\n", small_normal);
                document.add(para_miscellaneous9);

                // pdf Declaration assigned by applicant -1
                //go to new page
                document.newPage();

                //applicant Self declaration
                /*Paragraph para_applicant_self_declaration = new Paragraph("Applicant Self Declaration", small_bold);
                para_applicant_self_declaration.setAlignment(Element.ALIGN_RIGHT);
                document.add(para_applicant_self_declaration);

                byte[] fbyt_applican_self_declaration = Base64.decode(str_self_declaration_applicant, 0);
                Bitmap applicantSelfDeclarationBitmap = BitmapFactory.decodeByteArray(fbyt_applican_self_declaration,
                        0, fbyt_applican_self_declaration.length);

                ByteArrayOutputStream applicant_self_declaration_stream = new ByteArrayOutputStream();

                (applicantSelfDeclarationBitmap).compress(Bitmap.CompressFormat.PNG, 100, applicant_self_declaration_stream);

                Image applicant_self_declaration = Image.getInstance(applicant_self_declaration_stream.toByteArray());

                PdfPTable DD_table = new PdfPTable(1);
                DD_table.setWidthPercentage(100);

            *//*img_doc.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
            img_doc.setAbsolutePosition(
                    0, (PageSize.A4.getHeight() - DD_table.getTotalHeight()) / 2);*//*

                PdfPCell DocumentUpload_row2_cell = new PdfPCell();
                DocumentUpload_row2_cell
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                DocumentUpload_row2_cell.setPadding(5);
                DocumentUpload_row2_cell.setImage(applicant_self_declaration);

                DD_table.addCell(DocumentUpload_row2_cell);

                document.add(DD_table);

                document.newPage();*/

                /*//add header manually to new page
                document.add(para_img_logo);
                // For SBI- Life Logo ends

                // To draw line after the sbi logo image
                document.add(new LineSeparator());
                document.add(para_img_logo_after_space_1);*/

                //to main tittle
                Paragraph para_declaration_title = new Paragraph("Declaration to be signed by the applicant", headerBold);
                para_declaration_title.setAlignment(Element.ALIGN_CENTER);
                document.add(para_declaration_title);

                Paragraph para_applicant_pic = new Paragraph("Applicant Photo", small_bold);
                para_applicant_pic.setAlignment(Element.ALIGN_RIGHT);
                document.add(para_applicant_pic);

                //applicant main image
                byte[] fbyt_applican_pic = Base64.decode(str_photo_applicant, 0);
                Bitmap applicantPicBitmap = BitmapFactory.decodeByteArray(fbyt_applican_pic, 0, fbyt_applican_pic.length);

                ByteArrayOutputStream applicant_pic_stream = new ByteArrayOutputStream();

                (applicantPicBitmap).compress(Bitmap.CompressFormat.PNG, 50, applicant_pic_stream);

                Image applicant_photo = Image.getInstance(applicant_pic_stream.toByteArray());
                applicant_photo.setAlignment(Element.ALIGN_RIGHT);
                //applicant_photo.setAbsolutePosition(100f, 30f);
                document.add(applicant_photo);

                //para
                Paragraph para_declaration = new Paragraph("\nI hereby declare that :-", small_normal);
                document.add(para_declaration);

                Paragraph para_declaration_a = new Paragraph("\na) The information/details furnished herein are true to the best of my knowledge and belief. I further declare that I have not suppressed or concealed any detail sought herein.", small_normal);
                para_declaration_a.setIndentationLeft(36);
                document.add(para_declaration_a);

                Paragraph para_declaration_b = new Paragraph("b) I have not been found guilty of criminal misappropriation or criminal breach of trust or cheating or forgery or an abetment of or attempt to commit any such offence by a court of competent jurisdiction.", small_normal);
                para_declaration_b.setIndentationLeft(36);
                document.add(para_declaration_b);

                Paragraph para_declaration_c = new Paragraph("c) I have not been found guilty of or to have knowingly participated in or connived at any fraud, dishonestly or mis-representation against an insurer or an insured in the course of any judicial proceeding relation to any policy of insurance or the winding up of an insurance company or in the course of an investigation of the affairs of an insurer and", small_normal);
                para_declaration_c.setIndentationLeft(36);
                document.add(para_declaration_c);

                Paragraph para_declaration_d = new Paragraph("d) I undertake to inform SBI Life of any change in the above information.", small_normal);
                para_declaration_d.setIndentationLeft(36);
                document.add(para_declaration_d);

                Paragraph para_declaration_e = new Paragraph("e) I authorise the company to verify, check, investigate and share or reveal any or all the information provided by me in this application to any other person or insurer or entity or authority as the company may deem fit,.", small_normal);
                para_declaration_e.setIndentationLeft(36);
                document.add(para_declaration_e);

                Paragraph para_declaration_f = new Paragraph("f) The terms & conditions mentioned in the application form are acceptable to me and shall abide by the same.", small_normal);
                para_declaration_f.setIndentationLeft(36);
                document.add(para_declaration_f);

                Paragraph para_declaration_g = new Paragraph("g) I hereby agree that if any information/details provided herein is found by the company to be misstated or concealed, the company may take any action deemed fit, including but not limited to           denial of appointment as an agent and if subsequently detected after granting  agency or appointment as an Agent, initiate such penal actions which the company may deem fit which may include [ but not limited to ] levy of Financial Penalty or Suspension / Cancellation of my appointment.", small_normal);
                para_declaration_g.setIndentationLeft(36);
                document.add(para_declaration_g);

                // add a couple of blank lines
                document.add(Chunk.NEWLINE);

                // add a couple of blank lines
                document.add(Chunk.NEWLINE);

                /*// add a couple of blank lines
                document.add(Chunk.NEWLINE);

                // add a couple of blank lines
                document.add(Chunk.NEWLINE);

                // add a couple of blank lines
                document.add(Chunk.NEWLINE);

                // add a couple of blank lines
                document.add(Chunk.NEWLINE);*/

                //place , date and signature
                PdfPTable BI_PdftableApplicant = new PdfPTable(3);
                BI_PdftableApplicant.setWidths(new float[]{5f, 5f, 5f});
                BI_PdftableApplicant.setWidthPercentage(100);

            /*byte[] fbyt_applicant = Base64.decode(str_signature_applicant, 0);
            Bitmap applicantBitmap = BitmapFactory.decodeByteArray(fbyt_applicant, 0, fbyt_applicant.length);

            ByteArrayOutputStream applicant_signature_stream = new ByteArrayOutputStream();

            (applicantBitmap).compress(Bitmap.CompressFormat.PNG, 50, applicant_signature_stream);
            Image applicant_signature = Image.getInstance(applicant_signature_stream.toByteArray());

            applicant_signature.scaleToFit(90, 90);*/

                PdfPCell Nocell = new PdfPCell(new Paragraph("", small_bold));
                Nocell.setBorder(Rectangle.NO_BORDER);

                //row 1
                BI_PdftableApplicant.addCell(Nocell);
                BI_PdftableApplicant.addCell(Nocell);
                BI_PdftableApplicant.addCell(Nocell);

                //row 2
                PdfPCell sign_cell = new PdfPCell(new Paragraph(
                        "Place : " + edt_aob_applicant_declaration_place.getText().toString(), small_bold));
                sign_cell.setHorizontalAlignment(Element.ALIGN_BOTTOM);
                sign_cell.setPadding(5);
                sign_cell.setBorder(Rectangle.NO_BORDER);
                BI_PdftableApplicant.addCell(sign_cell);

                BI_PdftableApplicant.addCell(Nocell);

                //applicant Sign
                /*Paragraph para_applicant_sign = new Paragraph("Applicant Signature", small_bold);
                para_applicant_sign.setAlignment(Element.ALIGN_RIGHT);
                document.add(para_applicant_sign);*/

                byte[] fbyt_applican_sign = Base64.decode(str_signature_applicant, 0);
                Bitmap applicantSignBitmap = BitmapFactory.decodeByteArray(fbyt_applican_sign, 0, fbyt_applican_sign.length);

                ByteArrayOutputStream applicant_sign_stream = new ByteArrayOutputStream();

                (applicantSignBitmap).compress(Bitmap.CompressFormat.PNG, 50, applicant_sign_stream);

                Image applicant_sign = Image.getInstance(applicant_sign_stream.toByteArray());
                applicant_sign.scaleToFit(90, 90);
                /*applicant_sign.setAlignment(Element.ALIGN_RIGHT);
                //applicant_photo.setAbsolutePosition(100f, 30f);
                document.add(applicant_sign);*/

                sign_cell = new PdfPCell(applicant_sign);
                sign_cell.setHorizontalAlignment(Element.ALIGN_TOP);
                sign_cell.setPadding(5);
                sign_cell.setBorder(Rectangle.NO_BORDER);
                BI_PdftableApplicant.addCell(sign_cell);

                //row 3
                sign_cell = new PdfPCell(new Paragraph(
                        "Date : " + txt_aob_applicant_declaration_date.getText().toString(), small_bold));
                sign_cell.setHorizontalAlignment(Element.ALIGN_TOP);
                sign_cell.setPadding(5);
                sign_cell.setBorder(Rectangle.NO_BORDER);
                BI_PdftableApplicant.addCell(sign_cell);

                BI_PdftableApplicant.addCell(Nocell);

                sign_cell = new PdfPCell(new Paragraph(
                        "Applicant Signature", small_bold));
                sign_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                sign_cell.setBorder(Rectangle.TOP);
                sign_cell.setPadding(5);
                BI_PdftableApplicant.addCell(sign_cell);

                document.add(BI_PdftableApplicant);

                // pdf nomination conditions
                //go to new page
                document.newPage();

                /*//add header manually to new page
                document.add(para_img_logo);
                // For SBI- Life Logo ends

                // To draw line after the sbi logo image
                document.add(new LineSeparator());
                document.add(para_img_logo_after_space_1);*/

                //to main tittle
                Paragraph para_nom_condition_title = new Paragraph("Conditions Applicable To Nomination", headerBold);
                para_nom_condition_title.setAlignment(Element.ALIGN_CENTER);
                document.add(para_nom_condition_title);

                //para
                Paragraph para_nom_condition_a = new Paragraph("\n• A Nomination will not be effective unless it is communicated to SBI Life Insurance Co Ltd and registered by SBI Life Insurance Co Ltd.", small_normal);
                para_nom_condition_a.setIndentationLeft(36);
                document.add(para_nom_condition_a);

                Paragraph para_nom_condition_b = new Paragraph("\n• Any change in Nomination shall be duly informed to SBI Life Insurance Co Ltd and registered by it in its records. SBI Life Insurance Co Ltd shall not be liable in any manner whatsoever for payment made to the registered nominees as per its records.", small_normal);
                para_nom_condition_b.setIndentationLeft(36);
                document.add(para_nom_condition_b);

                Paragraph para_nom_condition_c = new Paragraph("\n• By registering a nomination , SBI Life Insurance Co Ltd makes no admission and expresses no opinion whatsoever as to the validity or effect of the nomination , it being understood that the parties satisfy themselves as to the form of nomination and all other points relating to the nomination before sending it to SBI Life Insurance Co Ltd .", small_normal);
                para_nom_condition_c.setIndentationLeft(36);
                document.add(para_nom_condition_c);

                Paragraph para_nom_condition_d = new Paragraph("\n• By registering a nomination, SBI Life Insurance Co Ltd does not Admit any liability for payment of commission or any other sum in the event of death of the Insurance Advisor.", small_normal);
                para_nom_condition_d.setIndentationLeft(36);
                document.add(para_nom_condition_d);

                Paragraph para_nom_condition_e = new Paragraph("\n• The liability of SBI Life Insurance Co Ltd for payment of commission or any other sum in the event of death of the Insurance Advisor would be determined solely by the fact of whether the said Insurance Advisor is qualified for such payment in accordance with the laws in force in India and the applicable regulations at the time of his/ her death", small_normal);
                para_nom_condition_e.setIndentationLeft(36);
                document.add(para_nom_condition_e);

                document.close();
            } else {
                mCommonMethods.showToast(mContext, "No Data Found Against This PAN.");
            }

        } catch (Exception e) {
            mCommonMethods.showToast(mContext, e.toString() + "Error in creating pdf");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAPTURE_DOCUMENT) {
                if (str_capture_doc_type.equals(APPLICANT_PHOTO_FILE_NAME)) {
                    CropImage.activity(Uri.fromFile(mApplicantPhoto)).start(this);
                } else if (str_capture_doc_type.equals(APPLICANT_SIGNATURE_FILE_NAME)) {
                    CropImage.activity(Uri.fromFile(mApplicantSign)).start(this);
                }
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri resultUri = result.getUri();

                if (str_capture_doc_type.equals(APPLICANT_PHOTO_FILE_NAME)) {
                    mApplicantPhoto = new File(resultUri.getPath());
                    CompressImage.compressImage(mApplicantPhoto.getPath());
                    Bitmap bmp = BitmapFactory.decodeFile(mApplicantPhoto.getPath());
                    bmp = bmp != null ? bmp.copy(Bitmap.Config.RGB_565, true) : null;

                    if (bmp != null) {

                        Bitmap scaled = Bitmap.createScaledBitmap(bmp, 230, 200, true);

                        imgbtn_aob_applicant_declaration_photo.setImageBitmap(scaled);

                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        scaled.compress(Bitmap.CompressFormat.PNG, 100, out);
                        byte[] signByteArray = out.toByteArray();
                        str_photo_applicant = Base64.encodeToString(signByteArray, Base64.DEFAULT);

                    } else {
                        mCommonMethods.showToast(mContext, "File Not Available..");
                    }
                } else if (str_capture_doc_type.equals(APPLICANT_SIGNATURE_FILE_NAME)) {
                    mApplicantSign = new File(resultUri.getPath());
                    CompressImage.compressImage(mApplicantSign.getPath());
                    Bitmap bmp = BitmapFactory.decodeFile(mApplicantSign.getPath());
                    bmp = bmp != null ? bmp.copy(Bitmap.Config.RGB_565, true) : null;

                    if (bmp != null) {

                        Bitmap scaled = Bitmap.createScaledBitmap(bmp, 230, 200, true);

                        imgbtn_aob_applicant_declaration_sign.setImageBitmap(scaled);

                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        scaled.compress(Bitmap.CompressFormat.PNG, 100, out);
                        byte[] signByteArray = out.toByteArray();
                        str_signature_applicant = Base64.encodeToString(signByteArray, Base64.DEFAULT);

                    } else {
                        mCommonMethods.showToast(mContext, "File not found..");
                    }
                }
            }

        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Exception error = result.getError();
            mCommonMethods.showMessageDialog(mContext, error.getMessage());
        }
    }

    public void enableDisableAllFields(boolean is_enable) {

        txt_aob_terms_and_conditions.setEnabled(is_enable);
        txt_aob_applicant_declaration_date.setEnabled(is_enable);
        txt_aob_nominee_conditions.setEnabled(is_enable);

        chkbox_aob_terms_conditions_agreed.setEnabled(is_enable);
        chkbox_aob_applicant_declaration_1.setEnabled(is_enable);
        chkbox_aob_applicant_declaration_2.setEnabled(is_enable);
        chkbox_aob_applicant_declaration_3.setEnabled(is_enable);
        chkbox_aob_applicant_declaration_4.setEnabled(is_enable);
        chkbox_aob_applicant_declaration_5.setEnabled(is_enable);
        chkbox_aob_applicant_declaration_6.setEnabled(is_enable);
        chkbox_aob_applicant_declaration_7.setEnabled(is_enable);
        chkbox_aob_nominee_conditions_agreed.setEnabled(is_enable);

        edt_aob_applicant_declaration_place.setEnabled(is_enable);

        imgbtn_aob_applicant_declaration_sign.setEnabled(is_enable);

        imgbtn_aob_applicant_declaration_photo.setEnabled(is_enable);
    }

    public void showMessageDialog(String message) {
        try {
            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_with_ok_button);
            dialog.setCancelable(false);

            TextView text = (TextView) dialog.findViewById(R.id.tv_title);
            text.setText(message);

            Button dialogButton = (Button) dialog.findViewById(R.id.bt_ok);
            dialogButton.setText("Ok");
            dialogButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();

                    Activity_AOB_Authentication.row_details = 0;

                    //redirect to home page
                    Intent i = new Intent(ActivityAOBTermsConditionsDeclaration.this, Activity_AOB_Authentication.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateProcessStatus(String status) {
        //3. update data against global row id
        ContentValues cv = new ContentValues();
        cv.put(db.AGENT_ON_BOARDING_UPDATED_BY, strCIFBDMUserId);

        //save date in long
        cv.put(db.AGENT_ON_BOARDING_UPDATED_DATE, new Date(mCalender.getTimeInMillis()).getTime() + "");
        cv.put(db.AGENT_ON_BOARDING_SYNCH_STATUS, status);

        int i = db.update_agent_on_boarding_details(cv, db.AGENT_ON_BOARDING_ID + " =? ",
                new String[]{Activity_AOB_Authentication.row_details + ""});
    }

    private void createSoapRequestToUploadDoc(final File mFiles) {

        Single.fromCallable(() -> mCommonMethods.read(mFiles))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<byte[]>() {
                    @Override
                    public void accept(byte[] result) throws Exception {
                        if (result != null) {

                            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_UPLOAD_ALL_AOB_DOC);

                            request.addProperty("f", result);
                            request.addProperty("fileName", mFiles.getName().toString());
                            request.addProperty("PAN", str_pan_no);

                            mCommonMethods.appendSecurityParams(mContext, request, strCIFBDMEmailId, strCIFBDMMObileNo);

                            mAsyncUploadFileCommon = new AsyncUploadFile_Common(mContext, ActivityAOBTermsConditionsDeclaration.this,
                                    request, METHOD_NAME_UPLOAD_ALL_AOB_DOC);
                            mAsyncUploadFileCommon.execute();

                        } else {
                            mCommonMethods.showToast(mContext, "File Not Found");
                        }
                    }
                }, throwable -> {
                    mCommonMethods.showToast(mContext, "File Not Found");
                });
    }

    @Override
    public void onUploadComplete(Boolean result) {
        if (result) {

            if (str_doc_type.equals(FILE_NAME)) {

                if (is_ia_upgrade) {
                    str_doc_type = APPLICANT_PHOTO_FILE_NAME;
                    //update status
                    updateProcessStatus("10");
                    createSoapRequestToUploadDoc(mApplicantPhoto);
                } else {
                    str_doc_type = APPLICANT_PHOTO_FILE_NAME;
                    //update status
                    updateProcessStatus("9");
                    createSoapRequestToUploadDoc(mApplicantPhoto);
                }
            } else if (str_doc_type.equals(APPLICANT_PHOTO_FILE_NAME)) {

                str_doc_type = APPLICANT_SIGNATURE_FILE_NAME;
                //update status
                updateProcessStatus("10");
                createSoapRequestToUploadDoc(mApplicantSign);

            } else if (str_doc_type.equals(APPLICANT_SIGNATURE_FILE_NAME)) {

                //compare current date with training end date

                //current date
                        /*
                        String mont = ((mCalender.get(Calendar.MONTH) + 1) < 10 ? "0" : "") + (mCalender.get(Calendar.MONTH) + 1);
                        String day = (mCalender.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") + mCalender.get(Calendar.DAY_OF_MONTH);

                        String str_current_date = day + "-" + mont + "-" + mCalender.get(Calendar.YEAR);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                        Date currentDate = null, trainingEndDate = null;

                        try {
                            currentDate = dateFormat.parse(str_current_date);
                            trainingEndDate = dateFormat.parse(str_training_end_date);
                        } catch (ParseException ex) {
                            mCommonMethods.printLog("parse Error : ", ex.getMessage());
                        }*/

                //update status
                updateProcessStatus("11");

                mCommonMethods.showToast(mContext, "Document Upload Successfully...");

                //if (trainingEndDate.before(currentDate) || trainingEndDate.equals(currentDate)) {
                Intent mIntent = new Intent(ActivityAOBTermsConditionsDeclaration.this, ActivityAOBDocumentUpload.class);
                if (is_ia_upgrade)
                    mIntent.putExtra("is_ia_upgrade", is_ia_upgrade);
                startActivity(mIntent);
                        /*}else{
                            showMessageDialog("The application can be submitted only upon completion of the training");
                        }*/
            }
        } else {
            mCommonMethods.showToast(mContext, "PLease try agian later..");
        }
    }

    public class AsynchCreateAllPdf extends AsyncTask<String, String, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext,
                    ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog
                    .setTitle(Html
                            .fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                running = true;

                //create all forms pdf
                create_all_pdf_pages();
            } catch (Exception e) {
                e.printStackTrace();
                running = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                //1.agent form (status - 9)
                //2. customer photo (status - 10)
                //3. suctomer signature (status - 11)
                str_doc_type = FILE_NAME;

                createSoapRequestToUploadDoc(agentFormFile);
            } else {
                Toast.makeText(mContext, s.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

