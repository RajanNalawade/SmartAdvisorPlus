package sbilife.com.pointofsale_bancaagency.reports.policyservicing;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class UnrealisedDataDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_unrealised_data_details);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        CommonMethods commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Unrealised Details");

        TextView tvProposalNumber, tvCustomerName, tvHolderGender, tvProductName, tvPolicyCurrenStatus,
                tvInstallmentPremium, tvServiceBranchName,
                tvDrawnBank, tvDrawnBranch, tvCollectionBank, tvMoneyInDate, tvInstrumentNumber, tvMoneyInAmount,
                tvCheckDate, tvSBILBranch, tvPaymentType, tvPaymentMode, tvCheckType, tvSourceId, tvCashEntryDate,
                tvPaymentValid, tvPremiumGrossAmount, tvFrequency, tvDOC, tvPremiumFUP, tvAlternateMode,
                tvServiceRegionName, tvServiceRegionNameOPS, tvPolicySumAssured, tvRAGFlag, tvTier1MKTName,
                tvTier2MKTName, tvTierROCMKTName, tvPolicyMaturityDate, tvAnnualisedPremium, tvPolicyTerm,
                tvPolicyPaymentTerm, tvIACIFCode, tvIACIFName,
                tvUMBankName, tvChannelName, tvChannelActiveStatus, tvPolicyPaymentMechanism, tvPolicyIssueDate,
                tvCustomerId, tvUMBankCode, tvChannelType, tvBranchArea, tvBranchDivision, tvPolicyType,
                tvPolicySubType, tvResidualAmount, tvCustBankAcNo;


        tvProductName = findViewById(R.id.tvProductName);
        tvPolicyCurrenStatus = findViewById(R.id.tvPolicyCurrenStatus);

        tvInstallmentPremium = findViewById(R.id.tvInstallmentPremium);
        tvServiceBranchName = findViewById(R.id.tvServiceBranchName);

        tvDrawnBank = findViewById(R.id.tvDrawnBank);
        tvDrawnBranch = findViewById(R.id.tvDrawnBranch);
        tvCollectionBank = findViewById(R.id.tvCollectionBank);
        tvMoneyInDate = findViewById(R.id.tvMoneyInDate);
        tvInstrumentNumber = findViewById(R.id.tvInstrumentNumber);
        tvMoneyInAmount = findViewById(R.id.tvMoneyInAmount);

        tvCheckDate = findViewById(R.id.tvCheckDate);
        tvSBILBranch = findViewById(R.id.tvSBILBranch);
        tvPaymentType = findViewById(R.id.tvPaymentType);
        tvPaymentMode = findViewById(R.id.tvPaymentMode);
        tvCheckType = findViewById(R.id.tvCheckType);
        tvSourceId = findViewById(R.id.tvSourceId);
        tvCashEntryDate = findViewById(R.id.tvCashEntryDate);

        tvPaymentValid = findViewById(R.id.tvPaymentValid);
        tvPremiumGrossAmount = findViewById(R.id.tvPremiumGrossAmount);
        tvFrequency = findViewById(R.id.tvFrequency);
        tvDOC = findViewById(R.id.tvDOC);
        tvPremiumFUP = findViewById(R.id.tvPremiumFUP);
        tvAlternateMode = findViewById(R.id.tvAlternateMode);

        tvServiceRegionName = findViewById(R.id.tvServiceRegionName);
        tvServiceRegionNameOPS = findViewById(R.id.tvServiceRegionNameOPS);
        tvPolicySumAssured = findViewById(R.id.tvPolicySumAssured);
        tvRAGFlag = findViewById(R.id.tvRAGFlag);
        tvTier1MKTName = findViewById(R.id.tvTier1MKTName);

        tvTier2MKTName = findViewById(R.id.tvTier2MKTName);
        tvTierROCMKTName = findViewById(R.id.tvTierROCMKTName);
        tvPolicyMaturityDate = findViewById(R.id.tvPolicyMaturityDate);
        tvAnnualisedPremium = findViewById(R.id.tvAnnualisedPremium);
        tvPolicyTerm = findViewById(R.id.tvPolicyTerm);

        tvPolicyPaymentTerm = findViewById(R.id.tvPolicyPaymentTerm);
        tvIACIFCode = findViewById(R.id.tvIACIFCode);
        tvIACIFName = findViewById(R.id.tvIACIFName);

        tvUMBankName = findViewById(R.id.tvUMBankName);
        tvChannelName = findViewById(R.id.tvChannelName);
        tvChannelActiveStatus = findViewById(R.id.tvChannelActiveStatus);
        tvPolicyPaymentMechanism = findViewById(R.id.tvPolicyPaymentMechanism);
        tvPolicyIssueDate = findViewById(R.id.tvPolicyIssueDate);

        tvCustomerId = findViewById(R.id.tvCustomerId);
        tvUMBankCode = findViewById(R.id.tvUMBankCode);
        tvChannelType = findViewById(R.id.tvChannelType);
        tvBranchArea = findViewById(R.id.tvBranchArea);
        tvBranchDivision = findViewById(R.id.tvBranchDivision);
        tvPolicyType = findViewById(R.id.tvPolicyType);

        tvPolicySubType = findViewById(R.id.tvPolicySubType);
        tvResidualAmount = findViewById(R.id.tvResidualAmount);
        tvCustBankAcNo = findViewById(R.id.tvCustBankAcNo);


        tvProposalNumber = findViewById(R.id.tvProposalNumber);

        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvHolderGender = findViewById(R.id.tvHolderGender);
        tvProductName = findViewById(R.id.tvProductName);
        tvPolicyCurrenStatus = findViewById(R.id.tvPolicyCurrenStatus);

        tvInstallmentPremium = findViewById(R.id.tvInstallmentPremium);
        tvServiceBranchName = findViewById(R.id.tvServiceBranchName);

        tvDrawnBank = findViewById(R.id.tvDrawnBank);
        tvDrawnBranch = findViewById(R.id.tvDrawnBranch);
        tvCollectionBank = findViewById(R.id.tvCollectionBank);
        tvMoneyInDate = findViewById(R.id.tvMoneyInDate);
        tvInstrumentNumber = findViewById(R.id.tvInstrumentNumber);
        tvMoneyInAmount = findViewById(R.id.tvMoneyInAmount);

        tvCheckDate = findViewById(R.id.tvCheckDate);
        tvSBILBranch = findViewById(R.id.tvSBILBranch);
        tvPaymentType = findViewById(R.id.tvPaymentType);
        tvPaymentMode = findViewById(R.id.tvPaymentMode);
        tvCheckType = findViewById(R.id.tvCheckType);
        tvSourceId = findViewById(R.id.tvSourceId);
        tvCashEntryDate = findViewById(R.id.tvCashEntryDate);

        tvPaymentValid = findViewById(R.id.tvPaymentValid);
        tvPremiumGrossAmount = findViewById(R.id.tvPremiumGrossAmount);
        tvFrequency = findViewById(R.id.tvFrequency);
        tvDOC = findViewById(R.id.tvDOC);
        tvPremiumFUP = findViewById(R.id.tvPremiumFUP);
        tvAlternateMode = findViewById(R.id.tvAlternateMode);

        tvServiceRegionName = findViewById(R.id.tvServiceRegionName);
        tvServiceRegionNameOPS = findViewById(R.id.tvServiceRegionNameOPS);
        tvPolicySumAssured = findViewById(R.id.tvPolicySumAssured);
        tvRAGFlag = findViewById(R.id.tvRAGFlag);
        tvTier1MKTName = findViewById(R.id.tvTier1MKTName);

        tvTier2MKTName = findViewById(R.id.tvTier2MKTName);
        tvTierROCMKTName = findViewById(R.id.tvTierROCMKTName);
        tvPolicyMaturityDate = findViewById(R.id.tvPolicyMaturityDate);
        tvAnnualisedPremium = findViewById(R.id.tvAnnualisedPremium);
        tvPolicyTerm = findViewById(R.id.tvPolicyTerm);

        tvPolicyPaymentTerm = findViewById(R.id.tvPolicyPaymentTerm);
        tvIACIFCode = findViewById(R.id.tvIACIFCode);
        tvIACIFName = findViewById(R.id.tvIACIFName);

        tvUMBankName = findViewById(R.id.tvUMBankName);
        tvChannelName = findViewById(R.id.tvChannelName);
        tvChannelActiveStatus = findViewById(R.id.tvChannelActiveStatus);
        tvPolicyPaymentMechanism = findViewById(R.id.tvPolicyPaymentMechanism);
        tvPolicyIssueDate = findViewById(R.id.tvPolicyIssueDate);

        tvCustomerId = findViewById(R.id.tvCustomerId);
        tvUMBankCode = findViewById(R.id.tvUMBankCode);
        tvChannelType = findViewById(R.id.tvChannelType);
        tvBranchArea = findViewById(R.id.tvBranchArea);
        tvBranchDivision = findViewById(R.id.tvBranchDivision);
        tvPolicyType = findViewById(R.id.tvPolicyType);

        tvPolicySubType = findViewById(R.id.tvPolicySubType);
        tvResidualAmount = findViewById(R.id.tvResidualAmount);
        tvCustBankAcNo = findViewById(R.id.tvCustBankAcNo);

        String unrealiseDetailsString = getIntent().getStringExtra("UnrealiseDetails");

        List<UnrealizedDataActivity.UnrealisedDataValuesModel> UnrealiseDetails
                = Arrays.asList(new Gson().fromJson(unrealiseDetailsString,
                UnrealizedDataActivity.UnrealisedDataValuesModel.class));

        if (UnrealiseDetails != null) {
            tvProposalNumber.setText(UnrealiseDetails.get(0).getPOLICYPROPOSALNUMBER());

            tvCustomerName.setText(UnrealiseDetails.get(0).getCUSTOMER_NAME());
            tvHolderGender.setText(UnrealiseDetails.get(0).getHOLDERGENDER());
            tvProductName.setText(UnrealiseDetails.get(0).getPRODUCTNAME());
            tvPolicyCurrenStatus.setText(UnrealiseDetails.get(0).getPOLICYCURRENTSTATUS());

            tvInstallmentPremium.setText(UnrealiseDetails.get(0).getINSTALLMENT_PREMIUM());
            tvServiceBranchName.setText(UnrealiseDetails.get(0).getSERVICEBRANCHNAME());

            tvDrawnBank.setText(UnrealiseDetails.get(0).getDRAWN_BANK());
            tvDrawnBranch.setText(UnrealiseDetails.get(0).getDRAWN_BRANCH());
            tvCollectionBank.setText(UnrealiseDetails.get(0).getCOLLECTION_BANK());
            tvMoneyInDate.setText(UnrealiseDetails.get(0).getMONEY_IN_DATE());
            tvInstrumentNumber.setText(UnrealiseDetails.get(0).getINSTRUMENT_NUMBER());
            tvMoneyInAmount.setText(UnrealiseDetails.get(0).getMONEY_IN_AMOUNT());

            tvCheckDate.setText(UnrealiseDetails.get(0).getCHEQUE_DATE());
            tvSBILBranch.setText(UnrealiseDetails.get(0).getSBIL_BRANCH());
            tvPaymentType.setText(UnrealiseDetails.get(0).getPAYMENT_TYPE());
            tvPaymentMode.setText(UnrealiseDetails.get(0).getPAYMENT_MODE());
            tvCheckType.setText(UnrealiseDetails.get(0).getCHEQUE_TYPE());
            tvSourceId.setText(UnrealiseDetails.get(0).getSOURCE_ID());
            tvCashEntryDate.setText(UnrealiseDetails.get(0).getCASH_ENTRY_DATE());

            tvPaymentValid.setText(UnrealiseDetails.get(0).getPAYMENTVALID());
            tvPremiumGrossAmount.setText(UnrealiseDetails.get(0).getPREMIUMGROSSAMOUNT());
            tvFrequency.setText(UnrealiseDetails.get(0).getFREQUENCY());
            tvDOC.setText(UnrealiseDetails.get(0).getDOC());
            tvPremiumFUP.setText(UnrealiseDetails.get(0).getPREMIUMFUP());
            tvAlternateMode.setText(UnrealiseDetails.get(0).getALTERNATEMODE());

            tvServiceRegionName.setText(UnrealiseDetails.get(0).getSERVICEREGIONNAME());
            tvServiceRegionNameOPS.setText(UnrealiseDetails.get(0).getSERVICEREGIONNAMEOPS());
            tvPolicySumAssured.setText(UnrealiseDetails.get(0).getPOLICYSUMASSURED());
            tvRAGFlag.setText(UnrealiseDetails.get(0).getRAG_FLAG());
            tvTier1MKTName.setText(UnrealiseDetails.get(0).getTIER1_MKT_NAME());

            tvTier2MKTName.setText(UnrealiseDetails.get(0).getTIER2_MKT_NAME());
            tvTierROCMKTName.setText(UnrealiseDetails.get(0).getTIERROC_MKT_NAME());
            tvPolicyMaturityDate.setText(UnrealiseDetails.get(0).getPOLICY_MATURITY_DATE());
            tvAnnualisedPremium.setText(UnrealiseDetails.get(0).getANNUALIZEDPREMIUM());
            tvPolicyTerm.setText(UnrealiseDetails.get(0).getPOLICY_TERM());

            tvPolicyPaymentTerm.setText(UnrealiseDetails.get(0).getPOLICYPAYMENTTERM());
            tvIACIFCode.setText(UnrealiseDetails.get(0).getIA_CIF_CODE());
            tvIACIFName.setText(UnrealiseDetails.get(0).getIA_CIF_NAME());

            tvUMBankName.setText(UnrealiseDetails.get(0).getUM_BANKNAME());
            tvChannelName.setText(UnrealiseDetails.get(0).getCHANNEL_NAME());
            tvChannelActiveStatus.setText(UnrealiseDetails.get(0).getCHANNEL_ACTIVE_STATUS());
            tvPolicyPaymentMechanism.setText(UnrealiseDetails.get(0).getPOLICYPAYMENTMECHANISM());
            tvPolicyIssueDate.setText(UnrealiseDetails.get(0).getPOLICYISSUEDATE());

            tvCustomerId.setText(UnrealiseDetails.get(0).getCUSTOMERID());
            tvUMBankCode.setText(UnrealiseDetails.get(0).getUMBANKCODE());
            tvChannelType.setText(UnrealiseDetails.get(0).getCHANNELTYPE());
            tvBranchArea.setText(UnrealiseDetails.get(0).getBRANCHAREA());
            tvBranchDivision.setText(UnrealiseDetails.get(0).getBRANCHDIVISION());
            tvPolicyType.setText(UnrealiseDetails.get(0).getPOLICYTYPE());

            tvPolicySubType.setText(UnrealiseDetails.get(0).getPOLICYTYPESUB());
            tvResidualAmount.setText(UnrealiseDetails.get(0).getRESIDUAL_AMOUNT());
            tvResidualAmount.setText(UnrealiseDetails.get(0).getRESIDUALAMOUNT());
            tvCustBankAcNo.setText(UnrealiseDetails.get(0).getCUSTOMERBANKACCOUNTNO());
        }
    }
}
