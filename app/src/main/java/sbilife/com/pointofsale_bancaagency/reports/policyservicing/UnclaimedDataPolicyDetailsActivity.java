package sbilife.com.pointofsale_bancaagency.reports.policyservicing;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class UnclaimedDataPolicyDetailsActivity extends AppCompatActivity implements Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_unclaimed_data_policy_details);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        CommonMethods commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Unclaimed Details");

        TextView tvProposalNumber,
                tvHolderName, tvHolderGender, tvLifeAssuredName, tvCustomerName,
                tvChannelType, tvPremiumGrossAmount, tvPolicyIssueDate, tvOperation, tvDueDate,
                tvEffectiveDate, tvCheckNumber, tvUnclaimedAmount, tvOperationStatus, tvStateDCAction,
                tvUnclaimedUploadedDate, tvEntryDate, tvEntryAmount, tvFundValAtEntry, tvEntryUnit,
                tvEntryNavValue, tvIssuanceAgeing, tvCheckRealisedDate, tvAgeing, tvPCName, tvRegionName,
                tvUnclaimId, tvNavValue, tvPolicyCurrenStatus, tvCorrespondenceAddress, tvCorrespondenceCity,
                tvCorrespondenceState, tvPostCode, tvFrequency, tvDOC, tvPremiumFUP, tvServiceRegionName,
                tvPolicySumAssured, tvTier1MKTName, tvTierROCMKTName, tvAnnualisedPremium, tvPolicyTerm,
                tvPolicyPaymentTerm, tvIACIFCode, tvIACIFName, tvUMBankName, tvChannelActiveStatus,
                tvPolicyPaymentMechanism, tvCustomerId, tvUMBankCode, tvBankType, tvBankCode, tvBankName,
                tvBankBranchCode, tvBankBranchName, tvBankCircleName, tvBankModuleName, tvBankRegionName,
                tvPolicyType, tvPolicySubType, tvAccuredExitPayableAmount;

        tvProposalNumber = findViewById(R.id.tvProposalNumber);
        tvHolderName = findViewById(R.id.tvHolderName);
        tvHolderGender = findViewById(R.id.tvHolderGender);
        tvLifeAssuredName = findViewById(R.id.tvLifeAssuredName);
        tvCustomerName = findViewById(R.id.tvCustomerName);

        tvChannelType = findViewById(R.id.tvChannelType);
        tvPremiumGrossAmount = findViewById(R.id.tvPremiumGrossAmount);
        tvPolicyIssueDate = findViewById(R.id.tvPolicyIssueDate);
        tvOperation = findViewById(R.id.tvOperation);
        tvDueDate = findViewById(R.id.tvDueDate);

        tvEffectiveDate = findViewById(R.id.tvEffectiveDate);
        tvCheckNumber = findViewById(R.id.tvCheckNumber);
        tvUnclaimedAmount = findViewById(R.id.tvUnclaimedAmount);
        tvOperationStatus = findViewById(R.id.tvOperationStatus);
        tvStateDCAction = findViewById(R.id.tvStateDCAction);

        tvUnclaimedUploadedDate = findViewById(R.id.tvUnclaimedUploadedDate);
        tvEntryDate = findViewById(R.id.tvEntryDate);
        tvEntryAmount = findViewById(R.id.tvEntryAmount);
        tvFundValAtEntry = findViewById(R.id.tvFundValAtEntry);
        tvEntryUnit = findViewById(R.id.tvEntryUnit);

        tvEntryNavValue = findViewById(R.id.tvEntryNavValue);
        tvIssuanceAgeing = findViewById(R.id.tvIssuanceAgeing);
        tvCheckRealisedDate = findViewById(R.id.tvCheckRealisedDate);
        tvAgeing = findViewById(R.id.tvAgeing);
        tvPCName = findViewById(R.id.tvPCName);
        tvRegionName = findViewById(R.id.tvRegionName);

        tvUnclaimId = findViewById(R.id.tvUnclaimId);
        tvNavValue = findViewById(R.id.tvNavValue);
        tvPolicyCurrenStatus = findViewById(R.id.tvPolicyCurrenStatus);
        tvCorrespondenceAddress = findViewById(R.id.tvCorrespondenceAddress);
        tvCorrespondenceCity = findViewById(R.id.tvCorrespondenceCity);

        tvCorrespondenceState = findViewById(R.id.tvCorrespondenceState);
        tvPostCode = findViewById(R.id.tvPostCode);
        tvFrequency = findViewById(R.id.tvFrequency);
        tvDOC = findViewById(R.id.tvDOC);
        tvPremiumFUP = findViewById(R.id.tvPremiumFUP);
        tvServiceRegionName = findViewById(R.id.tvServiceRegionName);

        tvPolicySumAssured = findViewById(R.id.tvPolicySumAssured);
        tvTier1MKTName = findViewById(R.id.tvTier1MKTName);
        tvTierROCMKTName = findViewById(R.id.tvTierROCMKTName);
        tvAnnualisedPremium = findViewById(R.id.tvAnnualisedPremium);
        tvPolicyTerm = findViewById(R.id.tvPolicyTerm);

        tvPolicyPaymentTerm = findViewById(R.id.tvPolicyPaymentTerm);
        tvIACIFCode = findViewById(R.id.tvIACIFCode);
        tvIACIFName = findViewById(R.id.tvIACIFName);
        tvUMBankName = findViewById(R.id.tvUMBankName);
        tvChannelActiveStatus = findViewById(R.id.tvChannelActiveStatus);

        tvPolicyPaymentMechanism = findViewById(R.id.tvPolicyPaymentMechanism);
        tvCustomerId = findViewById(R.id.tvCustomerId);
        tvUMBankCode = findViewById(R.id.tvUMBankCode);
        tvBankType = findViewById(R.id.tvBankType);
        tvBankCode = findViewById(R.id.tvBankCode);
        tvBankName = findViewById(R.id.tvBankName);

        tvBankBranchCode = findViewById(R.id.tvBankBranchCode);
        tvBankBranchName = findViewById(R.id.tvBankBranchName);
        tvBankCircleName = findViewById(R.id.tvBankCircleName);
        tvBankModuleName = findViewById(R.id.tvBankModuleName);
        tvBankRegionName = findViewById(R.id.tvBankRegionName);

        tvPolicyType = findViewById(R.id.tvPolicyType);
        tvPolicySubType = findViewById(R.id.tvPolicySubType);
        tvAccuredExitPayableAmount = findViewById(R.id.tvAccuredExitPayableAmount);

        String unclaimDetailsString = getIntent().getStringExtra("UnclaimDetails");

        List<UnclaimedDataActivity.UnclaimedDataValuesModel> unclaimDetails
                =  Arrays.asList(new Gson().fromJson(unclaimDetailsString,
                UnclaimedDataActivity.UnclaimedDataValuesModel.class));

        if (unclaimDetails != null) {
            tvProposalNumber.setText(unclaimDetails.get(0).getPOLICYPROPOSALNUMBER());

            tvHolderName.setText(unclaimDetails.get(0).getHOLDERNAME());
            tvHolderGender.setText(unclaimDetails.get(0).getHOLDER_GENDER());
            tvLifeAssuredName.setText(unclaimDetails.get(0).getLIFEASSUREDNAME());
            tvCustomerName.setText(unclaimDetails.get(0).getCUSTOMER_NAME());

            tvChannelType.setText(unclaimDetails.get(0).getCHANNELTYPE());
            tvPremiumGrossAmount.setText(unclaimDetails.get(0).getPREMIUMGROSSAMOUNT());
            tvPolicyIssueDate.setText(unclaimDetails.get(0).getPOLICYISSUEDATE());
            tvOperation.setText(unclaimDetails.get(0).getOPERATION());
            tvDueDate.setText(unclaimDetails.get(0).getDUE_DATE());

            tvEffectiveDate.setText(unclaimDetails.get(0).getEFFECTIVE_DATE());
            tvCheckNumber.setText(unclaimDetails.get(0).getCHEQUE_NO());
            tvUnclaimedAmount.setText(unclaimDetails.get(0).getUNCLAIMED_AMT());
            tvOperationStatus.setText(unclaimDetails.get(0).getOPERATION_STATUS());
            tvStateDCAction.setText(unclaimDetails.get(0).getSTALE_DC_ACTION());

            tvUnclaimedUploadedDate.setText(unclaimDetails.get(0).getUNCLAIMED_UPLOADED_DATE());
            tvEntryDate.setText(unclaimDetails.get(0).getENTRY_DATE());
            tvEntryAmount.setText(unclaimDetails.get(0).getENTRY_AMOUNT());
            tvFundValAtEntry.setText(unclaimDetails.get(0).getFUND_VALUE_AT_ENTRY());
            tvEntryUnit.setText(unclaimDetails.get(0).getENTRY_UNIT());

            tvEntryNavValue.setText(unclaimDetails.get(0).getENTY_NAV_VALUE());
            tvIssuanceAgeing.setText(unclaimDetails.get(0).getISSUANCEAGEING());
            tvCheckRealisedDate.setText(unclaimDetails.get(0).getCHQ_REALISED_DATE());
            tvAgeing.setText(unclaimDetails.get(0).getAGEING());
            tvPCName.setText(unclaimDetails.get(0).getPC_NAME());
            tvRegionName.setText(unclaimDetails.get(0).getREGION_NAME());

            tvUnclaimId.setText(unclaimDetails.get(0).getUNCLAIM_ID());
            tvNavValue.setText(unclaimDetails.get(0).getNAVVALUE());
            tvPolicyCurrenStatus.setText(unclaimDetails.get(0).getPOLICYCURRENTSTATUS());

            String address = unclaimDetails.get(0).getCORRESPONDENCEADDRESS1() + " "
                    + unclaimDetails.get(0).getCORRESPONDENCEADDRESS2() + " " + unclaimDetails.get(0).getCORRESPONDENCEADDRESS3();
            tvCorrespondenceAddress.setText(address);
            tvCorrespondenceCity.setText(unclaimDetails.get(0).getCORRESPONDENCECITY());

            tvCorrespondenceState.setText(unclaimDetails.get(0).getCORRESPONDENCESTATE());
            tvPostCode.setText(unclaimDetails.get(0).getCORRESPONDENCEPOSTCODE());
            tvFrequency.setText(unclaimDetails.get(0).getFREQUENCY());
            tvDOC.setText(unclaimDetails.get(0).getDOC());
            tvPremiumFUP.setText(unclaimDetails.get(0).getPREMIUMFUP());
            tvServiceRegionName.setText(unclaimDetails.get(0).getSERVICEREGIONNAMEOPS());

            tvPolicySumAssured.setText(unclaimDetails.get(0).getPOLICYSUMASSURED());
            tvTier1MKTName.setText(unclaimDetails.get(0).getTIER1_MKT_NAME());
            tvTierROCMKTName.setText(unclaimDetails.get(0).getTIERROC_MKT_NAME());
            tvAnnualisedPremium.setText(unclaimDetails.get(0).getANNUALIZED_PREMIUM());
            tvPolicyTerm.setText(unclaimDetails.get(0).getPOLICY_TERM());

            tvPolicyPaymentTerm.setText(unclaimDetails.get(0).getPOLICYPAYMENTTERM());
            tvIACIFCode.setText(unclaimDetails.get(0).getIA_CIF_CODE());
            tvIACIFName.setText(unclaimDetails.get(0).getIA_CIF_NAME());
            tvUMBankName.setText(unclaimDetails.get(0).getUM_BANKNAME());
            tvChannelActiveStatus.setText(unclaimDetails.get(0).getCHANNELACTIVESTATUS());

            tvPolicyPaymentMechanism.setText(unclaimDetails.get(0).getPOLICYPAYMENTMECHANISM());
            tvCustomerId.setText(unclaimDetails.get(0).getCUSTOMER_ID());
            tvUMBankCode.setText(unclaimDetails.get(0).getUM_BANKCODE());
            tvBankType.setText(unclaimDetails.get(0).getBANKTYPE());
            tvBankCode.setText(unclaimDetails.get(0).getBANKCODE());
            tvBankName.setText(unclaimDetails.get(0).getBANKNAME());

            tvBankBranchCode.setText(unclaimDetails.get(0).getBANKBRANCHCODE());
            tvBankBranchName.setText(unclaimDetails.get(0).getBANKBRANCHNAME());
            tvBankCircleName.setText(unclaimDetails.get(0).getBANKCIRCLENAME());
            tvBankModuleName.setText(unclaimDetails.get(0).getBANKMODULENAME());
            tvBankRegionName.setText(unclaimDetails.get(0).getBANKREGION());

            tvPolicyType.setText(unclaimDetails.get(0).getPOLICYTYPE());
            tvPolicySubType.setText(unclaimDetails.get(0).getPOLICYTYPESUB());
            tvAccuredExitPayableAmount.setText(unclaimDetails.get(0).getACCURED_EXIT_PAYABLE_AMOUNT());
        }
    }
}
