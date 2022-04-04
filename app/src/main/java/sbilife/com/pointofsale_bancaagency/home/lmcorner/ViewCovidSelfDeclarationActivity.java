package sbilife.com.pointofsale_bancaagency.home.lmcorner;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class ViewCovidSelfDeclarationActivity extends AppCompatActivity {

    private CommonMethods commonMethods;
    private Context context;
    private TextView tvFamilySympTomsStatus, tvrgContainmentZone,
            tvCovidPatientContact, tvInternationalTravelHistory, tvChronicMedicalCondition, tvFever, tvCough, tvShortBreath,
            tvSoreThroat, tvDate;
    private TextView tvName, tvDesignation, tvEmployeeCode, tvGender, tvBranch, tvRegion, tvZone,
            tvContactNumber, tvEmergencyContactNumber, tvEmergencyContactName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_view_covid_self_declaration);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        commonMethods = new CommonMethods();
        context = this;
        commonMethods.setApplicationToolbarMenu(this, "View Self Declaration");
        Intent intent = getIntent();
        String xmlString = intent.getStringExtra("xmlString");
        ParseXML parseXML = new ParseXML();

        String greenRedTag = parseXML.parseXmlTag(xmlString, "GreenRedFlag");

        LinearLayout llViewCovidSelfDeclaration = findViewById(R.id.llViewCovidSelfDeclaration);
        if (greenRedTag.equalsIgnoreCase("Yes")) {
            llViewCovidSelfDeclaration.setBackgroundColor(Color.parseColor("#008000"));
        } else {
            llViewCovidSelfDeclaration.setBackgroundColor(Color.parseColor("#FF0000"));
        }


        tvDate = findViewById(R.id.tvDate);
        tvName = findViewById(R.id.tvName);
        tvDesignation = findViewById(R.id.tvDesignation);
        tvEmployeeCode = findViewById(R.id.tvEmployeeCode);
        tvGender = findViewById(R.id.tvGender);
        tvBranch = findViewById(R.id.tvBranch);
        tvRegion = findViewById(R.id.tvRegion);
        tvZone = findViewById(R.id.tvZone);
        tvContactNumber = findViewById(R.id.tvContactNumber);
        tvEmergencyContactNumber = findViewById(R.id.tvEmergencyContactNumber);
        tvEmergencyContactName = findViewById(R.id.tvEmergencyContactName);

        tvrgContainmentZone = findViewById(R.id.tvrgContainmentZone);
        tvCovidPatientContact = findViewById(R.id.tvCovidPatientContact);
        tvInternationalTravelHistory = findViewById(R.id.tvInternationalTravelHistory);
        tvChronicMedicalCondition = findViewById(R.id.tvChronicMedicalCondition);
        tvFever = findViewById(R.id.tvFever);
        tvCough = findViewById(R.id.tvCough);
        tvShortBreath = findViewById(R.id.tvShortBreath);
        tvSoreThroat = findViewById(R.id.tvSoreThroat);
        tvFamilySympTomsStatus = findViewById(R.id.tvFamilySympTomsStatus);

        String DBDate = parseXML.parseXmlTag(xmlString, "DBDate");
        String DBTime = parseXML.parseXmlTag(xmlString, "DBTime");
        tvDate.setText(DBDate + " " + DBTime);

        tvName.setText(parseXML.parseXmlTag(xmlString, "NAME"));
        tvDesignation.setText(parseXML.parseXmlTag(xmlString, "DESIGNATION"));
        tvEmployeeCode.setText(parseXML.parseXmlTag(xmlString, "EMPLOYEE_CODE"));
        tvGender.setText(parseXML.parseXmlTag(xmlString, "GENDER"));
        tvBranch.setText(parseXML.parseXmlTag(xmlString, "BRANCH"));
        tvRegion.setText(parseXML.parseXmlTag(xmlString, "REGION"));
        tvZone.setText(parseXML.parseXmlTag(xmlString, "ZONE"));
        tvContactNumber.setText(parseXML.parseXmlTag(xmlString, "CONTACT_NUMBER"));
        tvEmergencyContactNumber.setText(parseXML.parseXmlTag(xmlString, "EMERGENCY_CONTACT_NUMBER"));
        tvEmergencyContactName.setText(parseXML.parseXmlTag(xmlString, "EMERGENCY_CONTACT_NAME"));

        tvrgContainmentZone.setText(parseXML.parseXmlTag(xmlString, "CONTAINMENT_ZONE_status"));
        String Covid_Patient_Contact = parseXML.parseXmlTag(xmlString, "Covid_Patient_Contact");
        tvCovidPatientContact.setText(Covid_Patient_Contact);

        tvInternationalTravelHistory.setText(parseXML.parseXmlTag(xmlString, "International_Travel_History"));

        String CHRONIC_MEDICAL_CONDITION = parseXML.parseXmlTag(xmlString, "CHRONIC_MEDICAL_CONDITION");
        tvChronicMedicalCondition.setText(CHRONIC_MEDICAL_CONDITION);
        tvFever.setText(parseXML.parseXmlTag(xmlString, "FEVER"));
        tvCough.setText(parseXML.parseXmlTag(xmlString, "COUGH"));

        String SHORTNESS_OF_BREATH = parseXML.parseXmlTag(xmlString, "SHORTNESS_OF_BREATH");
        tvShortBreath.setText(SHORTNESS_OF_BREATH);

        String SORE_THROAT = parseXML.parseXmlTag(xmlString, "SORE_THROAT");
        tvSoreThroat.setText(SORE_THROAT);
        tvFamilySympTomsStatus.setText(parseXML.parseXmlTag(xmlString, "Family_Symptoms_Status"));
    }
}