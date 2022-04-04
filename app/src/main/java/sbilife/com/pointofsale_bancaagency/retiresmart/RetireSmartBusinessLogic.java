/************************************************************************************************************************************
 Author-> Vrushali Chaudhari
 Class Description-> Business logic of ULIP-Retire Smart Calculator is implemented here in this class
 *************************************************************************************************************************************/

package sbilife.com.pointofsale_bancaagency.retiresmart;

import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;

class RetireSmartBusinessLogic {

	private CommonForAllProd comm = null;
	private RetireSmartProperties RS_prop = null;
	private  RetireSmartDB retireSmartDB = null;
	public RetireSmartBean retiresmartbean = null;

	private String month_E = null, year_F = null, age_H = null, policyInforce_G = "Y",
			premium_I = null, topUpPremium_J = null,
			premiumAllocationCharge_K = null, topUpCharges_L = null,
			ServiceTaxOnAllocation_M = null,
			amountAvailableForInvestment_N = null,
			sumAssuredRelatedCharges_O = null, riderCharges_P = null,
			policyAdministrationCharges_Q = null,
			serviceTaxOnPolicyAdminisrtationCharge_R = null,
			mortalityCharges_S = null, accTPDCharges_T = null,
			totalCharges_U = null, serviceTaxExclOfSTOnAllocAndSurr_V = null,
			totalServiceTax_W = null, additionToFundIfAny_X = null,
			fundBeforeFMC_Y = null, fundManagementCharge_Z = null,
			guranteeCharge_AA = null, serviceTaxOnFMC_AB = null,
			fundValueAfterFMCBerforeGA_AC = null, guaranteedAddition_AD = null,
			loyaltyAddition_AE = null, fundValueAtEnd_AF = null,
			surrenderCharges_AG = null, serviceTaxOnSurrenderCharges_AH = null,
			surrenderValue_AI = null, deathBenefit_AJ = null,
			mortalityCharges_AK = null, accTPDCharges_AL = null,
			totalCharges_AM = null, serviceTaxExclOfSTOnAllocAndSurr_AN = null,
			totalServiceTax_AO = null, additionToFundIfAny_AP = null,
			fundBeforeFMC_AQ = null, fundManagementCharge_AR = null,
			guranteeCharge_AS = null, serviceTaxOnFMC_AT = null,
			fundValueAfterFMCBerforeGA_AU = null, guaranteedAddition_AV = null,
			loyaltyAddition_AW = null, fundValueAtEnd_AX = null,
			surrenderCharges_AY = null, serviceTaxOnSurrenderCharges_AZ = null,
			surrenderValue_BA = null, deathBenefit_BB = null,
			surrenderCap_BC = null,
			oneHundredPercentOfCummulativePremium_BD = null,
			fundBeforeFMCReductionYield_Y = null,
			fundManagementChargeReductionYield_Z = null,
			guranteeChargeReductionYield_AA = null,
			guranteeChargeReductionYield_AB =null,
			serviceTaxOnFMCReductionYield_AB = null,
			totalServiceTaxReductionYield_W = null,
			fundValueAfterFMCBerforeGAReductionYield_AC = null,
			guaranteedAdditionReductionYield_AD = null,
			loyaltyAdditionReductionYield_AE = null,
			fundValueAtEndReductionYield_AF = null,
			surrenderChargesReductionYield_AG = null,
			serviceTaxOnSurrenderChargesReductionYield_AH = null,
			surrenderValueReductionYield_AI = null,
			deathBenefitReductionYield_AJ = null,
			totalServiceTaxReductionYield_AO = null,
			fundValueAfterFMCBerforeGAReductionYield_AU = null,
			guaranteedAdditionReductionYield_AV = null,
			loyaltyAdditionReductionYield_AW = null,
			fundValueAtEndReductionYield_AX = null,
			surrenderChargesReductionYield_AY = null,
			serviceTaxOnSurrenderChargesReductionYield_AZ = null,
			surrenderValueReductionYield_BA = null,
			deathBenefitReductionYield_BB = null, monthNumber_BT = null,
			reductionYield_BV = null, reductionYield_BZ = null,
			irrAnnual_BV = null, irrAnnual_BZ = null, irrAnnual_BU = null,
			reductionInYieldMaturityAt = null,
			reductionInYieldNumberOfYearsElapsedSinceInception = null,
			annuityPayableBasedOnTotalVestingBenefit4Per = null,
			annuityPayableBasedOnTotalVestingBenefit8Per = null,
			netYieldAt4Percent = null, netYieldAt8Percent = null,
			reductionYield_BU = null, annuityPay4pa = null,
			annuityPay8pa = null, annuityRates = null;

	public RetireSmartBusinessLogic(RetireSmartBean retireSmartBean) {
		comm = new CommonForAllProd();
		RS_prop = new RetireSmartProperties();
		retireSmartDB = new RetireSmartDB();
		retiresmartbean = retireSmartBean;
	}

	public void setMonth_E(int rowNum) {
		this.month_E = ("" + rowNum);
	}

	public String getMonth_E() {
		return month_E;
	}

	public void setYear_F() {
		this.year_F = comm.getRoundUp(""
				+ (Double.parseDouble(getMonth_E()) / 12));
	}

	public String getYear_F() {
		return year_F;
	}

	public String getPolicyInForce_G() {
		return policyInforce_G;
	}

	public void setAge_H(int ageAtEntry) {
		this.age_H = "" + (ageAtEntry + Integer.parseInt(getYear_F()) - 1);
	}

	public String getAge_H() {
		return age_H;
	}

	public void setPremium_I(int premiumPayingTerm, int PF, double annualPremium) {
		if (getPolicyInForce_G().equals("Y")) {
			if ((Integer.parseInt(getYear_F()) <= premiumPayingTerm)
					&& (((Integer.parseInt(getMonth_E()) - 1) % (12 / PF)) == 0)) {
				premium_I = "" + (annualPremium / PF);
			} else {
				premium_I = ("" + 0);
			}
		} else {
			premium_I = ("" + 0);
		}
	}

	public String getPremium_I() {
		return premium_I;
	}

	public void setTopUpPremium_J(boolean topUp, double effectiveTopUpPrem,
								  String addTopUp) {
		if (topUp) {
			if (getMonth_E().equals("1") && addTopUp.equals("Yes")) {
				this.topUpPremium_J = ("" + effectiveTopUpPrem);
			} else {
				this.topUpPremium_J = ("" + 0);
			}
		} else {
			this.topUpPremium_J = ("" + 0);
		}
	}

	public String getTopUpPremium_J() {
		return topUpPremium_J;
	}

	public void setPremiumAllocationCharge_K(int policyTerm, boolean staffDisc,boolean bancAssuranceDisc,double effectivePremium,String planType,int PPT) {
		/*int findYear;
		double discCharge, allocationCharge, max;
		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			if (Integer.parseInt(getYear_F()) > 10) {
				findYear = 11;
			} else {
				findYear = Integer.parseInt(getYear_F());
			}

			if (policyTerm == 10) {
				double[] discChargeArr = { 0.05, 0.02, 0.02, 0.02, 0.02, 0.02,
						0.02, 0.02, 0.02, 0.02, 0.02, 0.02 };
				discCharge = discChargeArr[findYear - 1];
			} else {
				double[] discChargeArr = { 0.075, 0.02, 0.02, 0.02, 0.02, 0.02,
						0.02, 0.02, 0.02, 0.02, 0.02, 0.02 };
				discCharge = discChargeArr[findYear - 1];
			}

			if (staffDisc) {
				double[] alloactionChargeArr = { 0.0575, 0.0425, 0.04, 0.04,
						0.04, 0.04, 0.04, 0.04, 0.04, 0.04, 0.025 };
				allocationCharge = alloactionChargeArr[findYear - 1]
						- discCharge;
			} else {
				double[] alloactionChargeArr = { 0.0575, 0.0425, 0.04, 0.04,
						0.04, 0.04, 0.04, 0.04, 0.04, 0.04, 0.025 };
				allocationCharge = alloactionChargeArr[findYear - 1];
			}

			max = Math.max(allocationCharge, 0);
			// this.premiumAllocationCharge_K=comm.roundUp_Level2(comm.getStringWithout_E(allocationCharge*Double.parseDouble(getPremium_I())));
			this.premiumAllocationCharge_K = comm.getRoundUp(comm
					.getStringWithout_E(max
							* Double.parseDouble(getPremium_I())));

		} else {
			this.premiumAllocationCharge_K = "" + 0;
		}*/
		if (Integer.parseInt(getYear_F()) <= policyTerm)
//        {this.premiumAllocationCharge_K= cfap.getRoundUp(cfap.getStringWithout_E(getAllocationCharge(staffDisc,bancAssuranceDisc,planType,PPT,effectivePremium)* Double.parseDouble(getPremium_I())),"roundUpII");}
			/*        {this.premiumAllocationCharge_K= cfap.getRoundUp(cfap.getStringWithout_E(getAllocationCharge(staffDisc,bancAssuranceDisc,planType,PPT,effectivePremium)* Double.parseDouble(getPremium_I())));}*/

		{this.premiumAllocationCharge_K= comm.getRound(comm.getStringWithout_E(getAllocationCharge(staffDisc,bancAssuranceDisc,planType,PPT,effectivePremium,policyTerm)* Double.parseDouble(getPremium_I())));}
//		System.out.println("aaaaaaa"+getAllocationCharge(staffDisc,bancAssuranceDisc,planType,PPT,effectivePremium,policyTerm));
		else
		{this.premiumAllocationCharge_K=(""+0);}
	}

	public double getAllocationCharge(boolean staffDisc,boolean bancAssuranceDisc,String planType,int PPT,double effectivePremium,int policyTerm)
	{
		double temp=0;
		int yearFactor=0;
		if(getYear_F().equals("1"))
		{yearFactor=1;}
		else{yearFactor=0;}
		if(planType.equals("LPPT") ||planType.equals("Limited") || planType.equals("Regular") )
		{
			if(getYear_F().equals("1"))
			{
				if(policyTerm >=10 && policyTerm <15){
					if(staffDisc ==true && bancAssuranceDisc==false){
						temp=0.0575-0.05;}
					else if(staffDisc ==false && bancAssuranceDisc==true)
					{temp=(0.0575-0.05);}
					else
					{temp=0.0575;}
					return Math.max(temp,0);

				}else{
					if(staffDisc ==true && bancAssuranceDisc==false){
						temp=0.0575-0.0575;}
					else if(staffDisc ==false && bancAssuranceDisc==true)
					{temp=(0.0575-0.0575);}
					else
					{temp=0.0575;}
					return Math.max(temp,0);
				}
			}
			else if(getYear_F().equals("2"))
			{
				if(staffDisc ==true && bancAssuranceDisc==false)
				{temp=0.0425-0.02;}
				else if(staffDisc ==false && bancAssuranceDisc==true)
				{temp=(0.0425-0.02);}
				else
				{temp=0.0425;}
				return Math.max(temp,0);
			}
			else if(getYear_F().equals("3"))
			{
				if(staffDisc ==true && bancAssuranceDisc==false)
				{temp=0.04-0.02;}
				else if(staffDisc ==false && bancAssuranceDisc==true)
				{temp=(0.04-0.02);}
				else
				{temp=0.04;}
				return Math.max(temp,0);
			}
			else if(getYear_F().equals("4"))
			{
				if(staffDisc ==true && bancAssuranceDisc==false)
				{temp=0.04-0.02;}
				else if(staffDisc ==false && bancAssuranceDisc==true)
				{temp=(0.04-0.02);}
				else
				{temp=0.04;}
				return Math.max(temp,0);
			}
			else if(getYear_F().equals("5"))
			{
				if(staffDisc ==true && bancAssuranceDisc==false)
				{temp=0.04-0.02;}
				else if(staffDisc ==false && bancAssuranceDisc==true)
				{temp=(0.04-0.02);}
				else
				{temp=0.04;}
				return Math.max(temp,0);
			}
			else if(getYear_F().equals("6"))
			{
				if(staffDisc ==true && bancAssuranceDisc==false)
				{temp=0.04-0.02;}
				else if(staffDisc ==false && bancAssuranceDisc==true)
				{temp=(0.04-0.02);}
				else
				{temp=0.04;}
				return Math.max(temp,0);
			}
			else if(getYear_F().equals("7"))
			{
				if(staffDisc ==true && bancAssuranceDisc==false)
				{temp=0.04-0.02;}
				else if(staffDisc ==false && bancAssuranceDisc==true)
				{temp=(0.04-0.02);}
				else
				{temp=0.04;}
				return Math.max(temp,0);
			}
			else if(getYear_F().equals("8"))
			{
				if(staffDisc ==true && bancAssuranceDisc==false)
				{temp=0.04-0.02;}
				else if(staffDisc ==false && bancAssuranceDisc==true)
				{temp=(0.04-0.02);}
				else
				{temp=0.04;}
				return Math.max(temp,0);
			}
			else if(getYear_F().equals("9"))
			{
				if(staffDisc ==true && bancAssuranceDisc==false)
				{temp=0.04-0.02;}
				else if(staffDisc ==false && bancAssuranceDisc==true)
				{temp=(0.04-0.02);}
				else
				{temp=0.04;}
				return Math.max(temp,0);
			}
			else if(getYear_F().equals("10"))
			{
				if(staffDisc ==true && bancAssuranceDisc==false)
				{temp=0.04-0.02;}
				else if(staffDisc ==false && bancAssuranceDisc==true)
				{temp=(0.04-0.02);}
				else
				{temp=0.04;}
				return Math.max(temp,0);
			}else if(getYear_F().equals("11"))
			{
				if(staffDisc ==true && bancAssuranceDisc==false)
				{temp=0.025-0.02;}
				else if(staffDisc ==false && bancAssuranceDisc==true)
				{temp=(0.025-0.02);}
				else
				{temp=0.025;}
				return Math.max(temp,0);
//	            }else if(getYear_F().equals("12"))
			}else
			{
				if(staffDisc ==true && bancAssuranceDisc==false)
				{temp=0.025-0.02;}
				else if(staffDisc ==false && bancAssuranceDisc==true)
				{temp=(0.025-0.02);}
				else
				{temp=0.025;}
				return Math.max(temp,0);
			}

//	            else{return 0;}
		}
		//For Single Mode of Premium Freqency
		else if(getYear_F().equals("1"))
		{
			if(getYear_F().equals("1"))
			{ if(staffDisc==true  && bancAssuranceDisc==false)
			{return Math.max(0,(0.03-0.02));}
			else if(staffDisc==false  &&  bancAssuranceDisc==true)
			{return Math.max(0,((0.03-0.02) * yearFactor));}
			else
			{return Math.max(0,0.03);}
			}
		}

		else{

			return 0;

		}
		return yearFactor;
	}


	public String getPremiumAllocationCharge_K() {
		return premiumAllocationCharge_K;
	}

	public void setTopUpCharges_L(double topup) {
		this.topUpCharges_L = comm.getRoundUp(comm.getStringWithout_E(Double
				.parseDouble(getTopUpPremium_J()) * topup));
	}

	public String getTopUpCharges_L() {
		return topUpCharges_L;
	}

	public void setServiceTaxOnAllocation_M(boolean allocationCharges,
											double serviceTax) {
		if (allocationCharges) {
			this.ServiceTaxOnAllocation_M = comm
					.getRoundOffLevel2New(comm.getStringWithout_E((Double
							.parseDouble(getPremiumAllocationCharge_K()) + Double
							.parseDouble(getTopUpCharges_L()))
							* serviceTax));
		} else {
			this.ServiceTaxOnAllocation_M = "" + 0;
		}
	}

	public String getServiceTaxOnAllocation_M() {
		return ServiceTaxOnAllocation_M;
	}

	public void setAmountAvailableForInvestment_N(int policyTerm) {
		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			this.amountAvailableForInvestment_N = comm
					.getRoundOffLevel2New(comm.getStringWithout_E(Double
							.parseDouble(getPremium_I())
							+ Double.parseDouble(getTopUpPremium_J())
							- Double.parseDouble(getPremiumAllocationCharge_K())
							- Double.parseDouble(getTopUpCharges_L())
							- Double.parseDouble(getServiceTaxOnAllocation_M())));
		} else {
			this.amountAvailableForInvestment_N = "" + 0;
		}
	}

	public String getAmountAvailableForInvestment_N() {
		return amountAvailableForInvestment_N;
	}

	public void setSumAssuredRelatedCharges_O(int policyTerm,
											  double sumAssured, double charge_SumAssuredBase) {
		double a, b;
		if (Integer.parseInt(getMonth_E()) == 1) {
			a = charge_SumAssuredBase;
		} else {
			a = 0;
		}
		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			b = charge_SumAssuredBase;
		} else {
			b = 0;
		}
		this.sumAssuredRelatedCharges_O = comm.roundUp_Level2(comm
				.getStringWithout_E(sumAssured * (a + b)));
	}

	public String getSumAssuredRelatedCharges_O() {
		return sumAssuredRelatedCharges_O;
	}

	public void setRiderCharges_P() {
		riderCharges_P = "" + 0;
	}

	public String getRiderCharges_P() {
		return riderCharges_P;
	}

	public void setPolicyAdministrationCharge_Q(
			double _policyAdministrationCharge_Q, int charge_Inflation,
			double fixedMonthlyExp_RP, int inflation_pa_RP) {
		double charge_PP_ren;
		if (getPolicyInForce_G().equals("Y")) {
			if (((Integer.parseInt(getMonth_E()) - 1) % 12) == 0) {
				if (Integer.parseInt(getYear_F()) < 6) {
					charge_PP_ren = getCharge_PP_Ren(fixedMonthlyExp_RP,
							inflation_pa_RP);
				} else {
					charge_PP_ren = 70 * 12;
				}
				this.policyAdministrationCharges_Q = (comm.getStringWithout_E(charge_PP_ren
						/ 12
						* comm.getPowerOutput(
						(1 + charge_Inflation),
						Integer.parseInt(""
								+ (Integer
								.parseInt(getMonth_E()) / 12)))));

			} else {
				this.policyAdministrationCharges_Q = (comm
						.getStringWithout_E(_policyAdministrationCharge_Q));
			}
		} else {
			this.policyAdministrationCharges_Q = "" + 0;
		}
	}

	public double getCharge_PP_Ren(double fixedMonthlyExp_RP,
								   int inflation_pa_RP) {
		return fixedMonthlyExp_RP * 12 * comm.pow(1 + inflation_pa_RP, 0);
	}

	public String getPolicyAdministrationCharge_Q() {
		return policyAdministrationCharges_Q;
	}

	public void setServiceTaxOnPolicyAdministrationCharges_R(
			boolean administrationCharge, double serviceTax) {
		if (administrationCharge) {
			this.serviceTaxOnPolicyAdminisrtationCharge_R = comm
					.getRoundUp(comm.getStringWithout_E(Double
							.parseDouble(getPolicyAdministrationCharge_Q())
							* serviceTax));
		} else {
			this.serviceTaxOnPolicyAdminisrtationCharge_R = "" + 0;
		}
	}

	public String getServiceTaxOnPolicyAdministrationCharges_R() {
		return serviceTaxOnPolicyAdminisrtationCharge_R;
	}

	public void setOneHundredPercentOfCummulativePremium_BD(
			double _oneHundredPercentOfCummulativePremium_BD) {
		double a;
		if (getPolicyInForce_G().equals("Y")) {
			a = Double.parseDouble(getPremium_I())
					+ (_oneHundredPercentOfCummulativePremium_BD / 1.05);
		} else {
			a = 0;
		}
		this.oneHundredPercentOfCummulativePremium_BD = "" + (a * 1.05);
	}

	public String getOneHundredPercentOfCummulativePremium_BD() {
		return oneHundredPercentOfCummulativePremium_BD;
	}

	public void setMortalityCharges_S(int ageAtEntry, double sumAssured,
									  String[] forBIArray, int policyTerm, boolean mortalityCharges,
									  double _fundValueAtEnd_AF, String premPaymentOption) {
		if (!(getPolicyInForce_G().equals("Y"))
				|| Integer.parseInt(getYear_F()) > policyTerm) {
			this.mortalityCharges_S = "" + 0;
		} else {
			double arrOutput = Double
					.parseDouble(forBIArray[(ageAtEntry + Integer
							.parseInt(getYear_F())) - 1]);
			// System.out.println("ARRAY OUTPUT "+arrOutput);
			double a = 1 - arrOutput / 1000;
			// System.out.println("a/1000 "+a);
			double b = (double) 1 / 12;
			// System.out.println("1/12 "+b);
			// System.out.println("whole value "+(comm.pow(a,b)));
			// double c=(sumAssured *
			// getIncreasingSA(increasingCoverOption)-(Double.parseDouble(getAmountAvailableForInvestment_N())+_fundValueAtEnd_AC));
			double c = sumAssured * getIncreasingSA(premPaymentOption);
			// System.out.println("c "+c);
			int d = 0;
			if (mortalityCharges) {
				d = 1;
			} else {
				d = 0;
			}

			double max1 = Math
					.max(Double
									.parseDouble(getOneHundredPercentOfCummulativePremium_BD()),
							c);
			// System.out.println("e max "+max1);
			double diff = max1
					- (Double.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AF);

			// System.out.println("diff" +diff);
			// this.mortalityCharges_R=
			// comm.roundUp_Level2(comm.getStringWithout_E((((1-(comm.pow(a,b)))*Math.max(diff,
			// 0))*d)));
			this.mortalityCharges_S = comm.roundUp_Level2(comm
					.getStringWithout_E((((1 - (comm.pow(a, b))) * Math.max(
							diff, 0)) * d)));
		}
	}

	public String getMortalityCharges_S() {
		return mortalityCharges_S;
	}

	private double getIncreasingSA(String premPaymentOption) {
		double multiplier = 0;
		if (premPaymentOption.equals("Yes")
				&& Integer.parseInt(getYear_F()) > 5) {

			if (Integer.parseInt(getYear_F()) >= 6
					&& Integer.parseInt(getYear_F()) < 11)
				multiplier = 1.1;
			else if (Integer.parseInt(getYear_F()) >= 11
					&& Integer.parseInt(getYear_F()) < 16)
				multiplier = 1.2;
			else if (Integer.parseInt(getYear_F()) >= 16
					&& Integer.parseInt(getYear_F()) < 21)
				multiplier = 1.3;
			else if (Integer.parseInt(getYear_F()) >= 21
					&& Integer.parseInt(getYear_F()) < 26)
				multiplier = 1.4;
			else if (Integer.parseInt(getYear_F()) >= 26
					&& Integer.parseInt(getYear_F()) <= 30)
				multiplier = 1.5;
		} else {
			multiplier = 1;
		}
		return multiplier;
	}

	public String[] getForBIArr(double mortalityCharges_AsPercentOfofLICtable) {
		int[] ageArr = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
				13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28,
				29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44,
				45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60,
				61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76,
				77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92,
				93, 94, 95, 96, 97, 98, 99, 100 };
		String[] strArrTReturn = new String[99];
		RetireSmartDB retireSmartDB = new RetireSmartDB();
		// System.out.println("length "+smartPowerDB.getIAIarray().length);
		for (int i = 0; i < retireSmartDB.getIAIarray().length - 2; i++) {
			// strArrTReturn[i]=comm.roundUp_Level2(comm.getStringWithout_E(((smartPowerDB.getIAIarray()[i]
			// + smartPowerDB.getIAIarray()[i+1])/2) *
			// mortalityCharges_AsPercentOfofLICtable * 1000));
			strArrTReturn[i] = comm
					.getStringWithout_E(((retireSmartDB.getIAIarray()[i] + retireSmartDB
							.getIAIarray()[i + 1]) / 2)
							* mortalityCharges_AsPercentOfofLICtable * 1000);
		}
		return strArrTReturn;
	}

	public void setAccTPDCharge_T(int policyTerm, double accTPDCharge,
								  double sumAssured, boolean mortalityCharges,
								  double _fundValueAtEnd_AF, String premPaymentOption) {
		if (!(getPolicyInForce_G().equals("Y"))
				|| Integer.parseInt(getYear_F()) > policyTerm) {
			this.accTPDCharges_T = "" + 0;
		} else {
			double accTPD = accTPDCharge / 12000;
			// System.out.println("accTPD "+accTPD);
			double a = sumAssured * getIncreasingSA(premPaymentOption);
			// System.out.println("a "+a);
			int b = 0;
			if (mortalityCharges) {
				b = 1;
			} else {
				b = 0;
			}
			double max1 = Math
					.max(Double
									.parseDouble(getOneHundredPercentOfCummulativePremium_BD()),
							a);
			// System.out.println("e max "+max1);
			double diff = max1
					- (Double.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AF);
			// System.out.println("diff" +diff);
			this.accTPDCharges_T = comm.getRoundOffLevel2New(comm
					.getStringWithout_E(((accTPD * Math.max(diff, 0)) * b)));
		}
	}

	public String getAccTPDCharge_T() {
		return accTPDCharges_T;
	}

	public void setTotalCharges_U(int policyTerm) {
		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			this.totalCharges_U = ""
					+ (Double.parseDouble(getPolicyAdministrationCharge_Q())
					+ Double.parseDouble(getMortalityCharges_S())
					+ Double.parseDouble(getAccTPDCharge_T())
					+ Double.parseDouble(getSumAssuredRelatedCharges_O()) + Double
					.parseDouble(getRiderCharges_P()));
		} else {
			this.totalCharges_U = "" + 0;
		}
	}

	public String getTotalCharges_U() {
		return totalCharges_U;
	}

	public void setServiceTaxExclOfSTOnAllocAndSurr_V(
			boolean mortalityAndRiderCharges, boolean administrationCharges,
			double serviceTax) {
		double add1, add2;
		if (mortalityAndRiderCharges) {
			add1 = (Double.parseDouble(getMortalityCharges_S()) + Double
					.parseDouble(getAccTPDCharge_T()));
		} else {
			add1 = 0;
		}
		if (administrationCharges) {
			add2 = Double.parseDouble(getPolicyAdministrationCharge_Q());
		} else {
			add2 = 0;
		}
		this.serviceTaxExclOfSTOnAllocAndSurr_V = comm.getRoundOffLevel2New(comm
				.getStringWithout_E((add1 + add2) * serviceTax));
	}

	public String getServiceTaxExclOfSTOnAllocAndSurr_V() {
		return serviceTaxExclOfSTOnAllocAndSurr_V;
	}

	public void setAdditionToFundIfAny_X(double _fundValueAtEnd_AF,
										 int policyTerm, double int1) {
		/*if (Integer.parseInt(getYear_F()) <= policyTerm) {
			// System.out.println("without Roundup "+(_fundValueAtEnd_AF+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_U())-Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_V())+Double.parseDouble(getRiderCharges_P()))*(comm.pow((1+int1),(double)1/12)-1));
			this.additionToFundIfAny_X = comm
					.getRoundOffLevel2New(comm.getStringWithout_E((_fundValueAtEnd_AF
							+ Double.parseDouble(getAmountAvailableForInvestment_N())
							- Double.parseDouble(getTotalCharges_U())
							- Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_V()) + Double
							.parseDouble(getRiderCharges_P()))
							* (comm.pow((1 + int1), (double) 1 / 12) - 1)));
		} else {
			this.additionToFundIfAny_X = "" + 0;
		}*/

		/*System.out.println("_fundValueAtEnd_AF"+_fundValueAtEnd_AF);
		System.out.println("getAmountAvailableForInvestment_N()"+getAmountAvailableForInvestment_N());
		System.out.println("getTotalCharges_U()"+getTotalCharges_U());
		System.out.println("getServiceTaxExclOfSTOnAllocAndSurr_V()"+getServiceTaxExclOfSTOnAllocAndSurr_V());
		System.out.println("getRiderCharges_P()"+getRiderCharges_P());*/
		if(Integer.parseInt(getYear_F()) <= policyTerm)
		{
			double temp1=0,temp2=0;
			temp1=(_fundValueAtEnd_AF+Double.parseDouble(getAmountAvailableForInvestment_N())- Double.parseDouble(getTotalCharges_U())-Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_V())+ Double.parseDouble(getRiderCharges_P()));
			double a=(double)1/12;
			temp2=(comm.pow((1+int1),a))-1;
			this.additionToFundIfAny_X= comm.roundUp_Level2( comm.getStringWithout_E(temp1*temp2));
		}
		else
		{this.additionToFundIfAny_X=""+0;}
	}

	public String getAdditionToFundIfAny_X() {
		return additionToFundIfAny_X;
	}

	public void setFundBeforeFMC_Y(double _fundValueAtEnd_AF, int policyTerm) {
		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			this.fundBeforeFMC_Y = ""
					+ (_fundValueAtEnd_AF
					+ Double.parseDouble(getAmountAvailableForInvestment_N())
					- Double.parseDouble(getTotalCharges_U())
					+ Double.parseDouble(getAdditionToFundIfAny_X())
					- Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_V()) + Double
					.parseDouble(getRiderCharges_P()));
		} else {
			this.fundBeforeFMC_Y = "" + 0;
		}
	}

	public String getFundBeforeFMC_Y() {
		return fundBeforeFMC_Y;
	}

	public void setFundManagementCharge_Z(int policyTerm,
										  double percentToBeInvested_EquityPensionFund,
										  double percentToBeInvested_EquityOptPensionFund,
										  double percentToBeInvested_GrowthPensionFund,
										  double percentToBeInvested_BondPensionFund,
										  double percentToBeInvested_MoneyMarketPesionFund,String fundOption) {
		double temp1 = 0, temp2 = 0;
		if (Integer.parseInt(getMonth_E()) == 1) {
			temp1 = RS_prop.charge_Fund;
		} else {
			temp1 = 0;
		}
		/*temp2 = getCharge_FundRen(percentToBeInvested_EquityPensionFund,
				percentToBeInvested_EquityOptPensionFund,
				percentToBeInvested_GrowthPensionFund,
				percentToBeInvested_BondPensionFund,
				percentToBeInvested_MoneyMarketPesionFund,fundOption,policyTerm);
//		System.out.println("temp2 "+temp2);

		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			this.fundManagementCharge_Z = comm.getRoundOffLevel2New(comm
					.getStringWithout_E(Double
							.parseDouble(getFundBeforeFMC_Y())
							* (temp2 / 12)));
		} else {
			this.fundManagementCharge_Z = "" + 0;
		}*/

		/*System.out.println("aaaaaaaaaaaaaaaaaaaaaa"+getCharge_FundRen(percentToBeInvested_EquityPensionFund,
    				percentToBeInvested_EquityOptPensionFund,
    				percentToBeInvested_GrowthPensionFund,
    				percentToBeInvested_BondPensionFund,
    				percentToBeInvested_MoneyMarketPesionFund,fundOption,policyTerm));*/
		/*System.out.println("output"+(Double.parseDouble(getFundBeforeFMC_Y()) * ((getCharge_FundRen(percentToBeInvested_EquityPensionFund,
    				percentToBeInvested_EquityOptPensionFund,
    				percentToBeInvested_GrowthPensionFund,
    				percentToBeInvested_BondPensionFund,
    				percentToBeInvested_MoneyMarketPesionFund,fundOption,policyTerm)/12))));*/
		if(Integer.parseInt(getYear_F()) <= policyTerm)
		{
			if(Integer.parseInt(getMonth_E())==1)
			{this.fundManagementCharge_Z=comm.roundUp_Level2(comm.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_Y()) * ((getCharge_FundRen(percentToBeInvested_EquityPensionFund,
					percentToBeInvested_EquityOptPensionFund,
					percentToBeInvested_GrowthPensionFund,
					percentToBeInvested_BondPensionFund,
					percentToBeInvested_MoneyMarketPesionFund,fundOption,policyTerm)/12))));}
			else
			{this.fundManagementCharge_Z=comm.roundUp_Level2(comm.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_Y()) * (getCharge_FundRen(percentToBeInvested_EquityPensionFund,
					percentToBeInvested_EquityOptPensionFund,
					percentToBeInvested_GrowthPensionFund,
					percentToBeInvested_BondPensionFund,
					percentToBeInvested_MoneyMarketPesionFund,fundOption,policyTerm)/12)));}
		}
		else
		{this.fundManagementCharge_Z=""+0;}
	}

	public String getFundManagementCharge_Z() {
		return fundManagementCharge_Z;
	}

	public double getCharge_FundRen(double percentToBeInvested_EquityPensionFund,
									double percentToBeInvested_EquityOptPensionFund,
									double percentToBeInvested_GrowthPensionFund,
									double percentToBeInvested_BondPensionFund,
									double percentToBeInvested_MoneyMarketPesionFund,String fundOption,int policyTerm) {
		// System.out.println("trigger "+(0.8*SPW_prop.FMC_EquityFund +
		// 0.2*SPW_prop.FMC_BondFund));
		//return (0.75 * RS_prop.FMC_EquityFund + 0.25 * RS_prop.FMC_BondFund);
		// percentToBeInvested_EquityFund*SPW_prop.FMC_EquityFund +
		// percentToBeInvested_BondFund*SPW_prop.FMC_BondFund +
		// percentToBeInvested_Top300Fund*SPW_prop.FMC_Top300Fund+
		// percentToBeInvested_EquityOptFund*SPW_prop.FMC_EquityOptFund+
		// percentToBeInvested_GrowthFund*SPW_prop.FMC_GrowthFund+
		// percentToBeInvested_BalancedFund*SPW_prop.FMC_BalancedFund+
		// percentToBeInvested_MoneyMarketFund*SPW_prop.FMC_MoneyMarketFund;

		if(fundOption.equals("Advantage Plan")){

			int a =35-policyTerm+Integer.parseInt(getYear_F());
//    		System.out.println("*** 3 *** "+a);
//    		System.out.println("*** 3 *** "+autoAssetAllocationStrategy);

			if(a <= 20){

//    			System.out.println("a "+a+" value "+ (0.70*prop.FMC_EquityFund+0.30*prop.FMC_CorpFund+0*prop.FMC_MoneyMarketFund));
				return  0.75*RS_prop.FMC_EquityFund+
						0.25*RS_prop.FMC_BondFund+
						0*RS_prop.FMC_MoneyMarketFund;
			}
			else if(a> 20 && a<=25 ){

//				System.out.println("a "+a+" value "+ (0.65*prop.FMC_EquityFund+0.35*prop.FMC_CorpFund+0*prop.FMC_MoneyMarketFund));

				return  0.5*RS_prop.FMC_EquityFund+
						0.5*RS_prop.FMC_BondFund+
						0*RS_prop.FMC_MoneyMarketFund;

			}

			else if(a>25 && a<30){

//				System.out.println("a "+a+" value "+ (0.60*prop.FMC_EquityFund+0.40*prop.FMC_CorpFund+0*prop.FMC_MoneyMarketFund));

				return  0.4*RS_prop.FMC_EquityFund+
						0.6*RS_prop.FMC_BondFund+
						0*RS_prop.FMC_MoneyMarketFund;

			}

			else if(a == 30){

//				System.out.println("a "+a+" value "+ (0.30*prop.FMC_EquityFund+0.65*prop.FMC_CorpFund+0.05*prop.FMC_MoneyMarketFund));

				return  0.35*RS_prop.FMC_EquityFund+
						0.6*RS_prop.FMC_BondFund+
						0.05*RS_prop.FMC_MoneyMarketFund;

			}

			else if(a == 31){

//				System.out.println("a "+a+" value "+ (0.25*prop.FMC_EquityFund+0.65*prop.FMC_CorpFund+0.10*prop.FMC_MoneyMarketFund));
				return  0.3*RS_prop.FMC_EquityFund+
						0.3*RS_prop.FMC_BondFund+
						0.4*RS_prop.FMC_MoneyMarketFund;
			}

			else if(a == 32){

//				System.out.println("a "+a+" value "+ (0.20*prop.FMC_EquityFund+0.65*prop.FMC_CorpFund+0.15*prop.FMC_MoneyMarketFund));
				return  0.3*RS_prop.FMC_EquityFund+
						0.25*RS_prop.FMC_BondFund+
						0.45*RS_prop.FMC_MoneyMarketFund;
			}

			else if(a == 33){

//				System.out.println("a "+a+" value "+ (0.15*prop.FMC_EquityFund+0.65*prop.FMC_CorpFund+0.20*prop.FMC_MoneyMarketFund));
				return  0.3*RS_prop.FMC_EquityFund+
						0.2*RS_prop.FMC_BondFund+
						0.5*RS_prop.FMC_MoneyMarketFund;
			}

			else if(a == 34){

//				System.out.println("a "+a+" value "+ (0.10*prop.FMC_EquityFund+0.65*prop.FMC_CorpFund+0.25*prop.FMC_MoneyMarketFund));

				return  0.3*RS_prop.FMC_EquityFund+
						0.15*RS_prop.FMC_BondFund+
						0.55*RS_prop.FMC_MoneyMarketFund;
			}

			else {

//				System.out.println("a "+a+" value "+ (0.05*prop.FMC_EquityFund+0.65*prop.FMC_CorpFund+0.30*prop.FMC_MoneyMarketFund));

				return  0.3*RS_prop.FMC_EquityFund+
						0.1*RS_prop.FMC_BondFund+
						0.60*RS_prop.FMC_MoneyMarketFund;
			}
		}
		else{
			//System.out.println("percentToBeInvested_EquityPensionFund "+percentToBeInvested_EquityPensionFund);

			return  percentToBeInvested_EquityPensionFund*RS_prop.FMC_EquityFund+
					percentToBeInvested_EquityOptPensionFund*RS_prop.FMC_EquityOptFund+
					percentToBeInvested_GrowthPensionFund*RS_prop.FMC_GrowthFund+
					percentToBeInvested_BondPensionFund*RS_prop.FMC_BondFund+
					percentToBeInvested_MoneyMarketPesionFund*RS_prop.FMC_MoneyMarketFund;
		}
	}

	public void setGuaranteeCharge_AA(double guarantee_chg, int policyTerm,
									  boolean guaranteeCharge,String fundOption) {
		double temp1;
		String temp2;
		if (guaranteeCharge) {
			temp1 = 1;
		} else {
			temp1 = 0;
		}

		if(fundOption.equals("Smart Choice Plan"))
		{
			guarantee_chg=0;
		}




		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			temp2 = ((comm.getStringWithout_E(Double
					.parseDouble(getFundBeforeFMC_Y()) * guarantee_chg / 12)));
		} else {
			temp2 = "" + 0;
		}
		this.guranteeCharge_AA = comm.getRoundOffLevel2New(comm.getStringWithout_E(Double.parseDouble(temp2)));
	}

	public String getGuaranteeCharge_AA() {
		return guranteeCharge_AA;
	}

	/* Modifed by akshaya */
	public void setServiceTaxOnFMC_AB(boolean fundManagementCharges,
									  double serviceTax, double percentToBeInvested_EquityPensionFund,
									  double percentToBeInvested_EquityOptPensionFund,
									  double percentToBeInvested_GrowthPensionFund,
									  double percentToBeInvested_BondPensionFund,
									  double percentToBeInvested_MoneyMarketPesionFund,String fundOption,int policyTerm) {

		double a = 0;
		// System.out.println("charge fund "+getCharge_FundRen(
		// percentToBeInvested_EquityFund,
		// percentToBeInvested_BondFund,percentToBeInvested_Top300Fund,percentToBeInvested_EquityOptFund,percentToBeInvested_GrowthFund,
		// percentToBeInvested_BalancedFund,
		// percentToBeInvested_MoneyMarketFund));
//		System.out.println("getGuaranteeCharge_AA() "+getGuaranteeCharge_AA());
//		System.out.println("getFundManagementCharge_Z() "+getFundManagementCharge_Z());
		String b=comm.getRoundOffLevel2New(comm.getStringWithout_E(Double.parseDouble(getFundManagementCharge_Z())
				* getCharge_FundRen(percentToBeInvested_EquityPensionFund,
				percentToBeInvested_EquityOptPensionFund,
				percentToBeInvested_GrowthPensionFund,
				percentToBeInvested_BondPensionFund,
				percentToBeInvested_MoneyMarketPesionFund,
				fundOption,
				policyTerm)
				/ getCharge_FundRen(percentToBeInvested_EquityPensionFund,
				percentToBeInvested_EquityOptPensionFund,
				percentToBeInvested_GrowthPensionFund,
				percentToBeInvested_BondPensionFund,
				percentToBeInvested_MoneyMarketPesionFund,
				fundOption,
				policyTerm)));
//		System.out.println("b "+b);

		if (fundManagementCharges) {
			a = 1;
		} else {
			a = 0;
		}
		// System.out.println("a "+a);
		this.serviceTaxOnFMC_AB = comm.getRoundOffLevel2New(comm.getStringWithout_E((Double.parseDouble(getGuaranteeCharge_AA()) + Double.parseDouble(b))* serviceTax *1));
	}

	public String getServiceTaxOnFMC_AB() {
		return serviceTaxOnFMC_AB;
	}

	public void setTotalServiceTax_W( boolean riderCharges, double serviceTax) {
		// System.out.println("aaa "+(Double.parseDouble(getServiceTaxOnAllocation_M())+Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_V())+Double.parseDouble(getGuaranteeCharge_AA())+Double.parseDouble(getServiceTaxOnFMC_AB())));
//		System.out.println("getServiceTaxOnAllocation_M : "+getServiceTaxOnAllocation_M());
//		System.out.println("getServiceTaxExclOfSTOnAllocAndSurr_V : "+getServiceTaxExclOfSTOnAllocAndSurr_V());
//		System.out.println("getServiceTaxOnFMC_AB : "+getServiceTaxOnFMC_AB());
//		System.out.println("serviceTax : "+serviceTax);
		double temp;
		if (riderCharges) {
			temp = Double.parseDouble(getRiderCharges_P());
		} else {
			temp = 0;
		}


		this.totalServiceTax_W = comm
				.getRoundOffLevel2New(comm.getStringWithout_E(
						Double.parseDouble(getServiceTaxOnAllocation_M())
								+ Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_V())
								+ Double.parseDouble(getServiceTaxOnFMC_AB())
								+ temp
								* serviceTax));
	}

	public String getTotalServiceTax_W() {
		return totalServiceTax_W;
	}

	public void setFundValueAfterFMCandBeforeGA_AC(double serviceTax,
												   int policyTerm) {
		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			this.fundValueAfterFMCBerforeGA_AC = ""
					+ (Double.parseDouble(getFundBeforeFMC_Y())
					- Double.parseDouble(getFundManagementCharge_Z())
					- Double.parseDouble(getGuaranteeCharge_AA())
					- Double.parseDouble(getServiceTaxOnFMC_AB()));
		} else {
			this.fundValueAfterFMCBerforeGA_AC = "" + 0;
		}
	}

	public String getFundValueAfterFMCandBeforeGA_AC() {
		return fundValueAfterFMCBerforeGA_AC;
	}

	public double getGA(String premPayingOption,int policyTerm) {
		double GA = 0;
		if(premPayingOption.equals("Single")){
			if (Integer.parseInt(getYear_F()) > 10) {
				if (Integer.parseInt(getMonth_E()) % 12 == 0) {
					if (Integer.parseInt(getYear_F()) == 15)
						GA =0.01;
					else if (Integer.parseInt(getYear_F()) > 15
							&& Integer.parseInt(getYear_F()) <= 20)
						GA = 0.01;
					else if (Integer.parseInt(getYear_F()) > 20
							&& Integer.parseInt(getYear_F()) <= 25)
						GA = 0.01;
					else if (Integer.parseInt(getYear_F()) > 25
							&& Integer.parseInt(getYear_F()) <= 30)
						GA = 0.01;
					else if (Integer.parseInt(getYear_F()) > 30
							&& Integer.parseInt(getYear_F()) <= 35)
						GA = 0.01;
				}
			} else {
				GA = 0;
			}
		}
		else if (Integer.parseInt(getYear_F()) > 10) {
			if (Integer.parseInt(getMonth_E()) % 12 == 0) {
				if (Integer.parseInt(getYear_F()) == 15)
					GA = 0.1;
				else if (Integer.parseInt(getYear_F()) > 15
						&& Integer.parseInt(getYear_F()) <= 20)
					GA = 0.1;
				else if (Integer.parseInt(getYear_F()) > 20
						&& Integer.parseInt(getYear_F()) <= 25)
					GA = 0.1;
				else if (Integer.parseInt(getYear_F()) > 25
						&& Integer.parseInt(getYear_F()) <= 30)
					GA = 0.1;
				else if (Integer.parseInt(getYear_F()) > 30
						&& Integer.parseInt(getYear_F()) <= 35)
					GA = 0.1;
			}
		} else {
			GA = 0;
		}
		return GA;
	}

	public void setGuaranteedAddition_AD(int policyTerm, double annualPremium,String premPayingOption) {
		// System.out.println("GA "+getGA());
		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			this.guaranteedAddition_AD =comm.getRoundOffLevel2New(comm.getStringWithout_E( getGA(premPayingOption,policyTerm) * annualPremium));
		} else {
			this.guaranteedAddition_AD = "" + 0;
		}
	}

	public String getGuaranteedAdditon_AD() {
		return guaranteedAddition_AD;
	}

	public void setLoyaltyAddition_AE(double loyaltyAddition, int policyTerm) {
		if (Integer.parseInt(getMonth_E()) == policyTerm * 12) {
			this.loyaltyAddition_AE =comm.getRoundOffLevel2New(comm.getStringWithout_E(loyaltyAddition * (Double
					.parseDouble(getFundValueAfterFMCandBeforeGA_AC()) + Double
					.parseDouble(getGuaranteedAdditon_AD()))));
		} else {
			this.loyaltyAddition_AE = "" + 0;
		}
	}

	public String getLoyaltyAddition_AE() {
		return loyaltyAddition_AE;
	}

	public void setFundValueAtEnd_AF() {
		this.fundValueAtEnd_AF = ""
				+ (Double.parseDouble(getFundValueAfterFMCandBeforeGA_AC())
				+ Double.parseDouble(getLoyaltyAddition_AE())
				+ Double.parseDouble(getGuaranteedAdditon_AD()));
	}

	public String getFundValueAtEnd_AF() {
		return fundValueAtEnd_AF;
	}

	public void setSurrenderCap_BC(double annualPremium,String premPayingOption) {
		/*if (getPolicyInForce_G().equals("Y")) {
			this.surrenderCap_BC = ""
					+ (Double.parseDouble(calCapArr(annualPremium,premPayingOption)[Math.min(
					(Integer.parseInt(getYear_F()) - 1), 11)]));
		} else {
			this.surrenderCap_BC = "" + 0;
		}*/

		double a=0;
		if(premPayingOption.equals("Single")){
			if(annualPremium >25000){
				if(annualPremium <=300000){
					if(getYear_F().equals("1"))
					{
						a = 3000;
					}
					else if(getYear_F().equals("2"))
					{
						a = 2000;
					}
					else if(getYear_F().equals("3"))
					{
						a = 1500;
					}
					else if(getYear_F().equals("4"))
					{
						a = 1000;
					}
					else
					{
						a=0;
					}
				} else {
					if(getYear_F().equals("1"))
					{
						a = 6000;
					}
					else if(getYear_F().equals("2"))
					{
						a = 5000;
					}
					else if(getYear_F().equals("3"))
					{
						a = 4000;
					}
					else if(getYear_F().equals("4"))
					{
						a = 2000;
					}
					else
					{
						a=0;
					}
				}
			} else{
				if(getYear_F().equals("1"))
				{
					a = 3000;
				}
				else if(getYear_F().equals("2"))
				{
					a = 2000;
				}
				else if(getYear_F().equals("3"))
				{
					a = 1500;
				}
				else if(getYear_F().equals("4"))
				{
					a = 1000;
				}
				else
				{
					a=0;
				}
			}
		}else{
			if(annualPremium >25000){
				if(annualPremium >50000){
					if(getYear_F().equals("1"))
					{
						a = 6000;
					}
					else if(getYear_F().equals("2"))
					{
						a = 5000;
					}
					else if(getYear_F().equals("3"))
					{
						a = 4000;
					}
					else if(getYear_F().equals("4"))
					{
						a = 2000;
					}
					else
					{
						a=0;
					}
				}else{
					if(getYear_F().equals("1"))
					{
						a = 3000;
					}
					else if(getYear_F().equals("2"))
					{
						a = 2000;
					}
					else if(getYear_F().equals("3"))
					{
						a = 1500;
					}
					else if(getYear_F().equals("4"))
					{
						a = 1000;
					}
					else
					{
						a=0;
					}
				}
			}else{
				if(getYear_F().equals("1"))
				{
					a = 3000;
				}
				else if(getYear_F().equals("2"))
				{
					a = 2000;
				}
				else if(getYear_F().equals("3"))
				{
					a = 1500;
				}
				else if(getYear_F().equals("4"))
				{
					a = 1000;
				}
				else
				{
					a=0;
				}
			}
		}

		this.surrenderCap_BC =""+a;
	}

	public String getSurrenderCap_BC() {
		return surrenderCap_BC;
	}

	public String[] calCapArr(double annualPremium,String premPayingOption) {
		if(premPayingOption.equals("Single"))
		{
			if (annualPremium <= 25000) {
				return new String[] { "0", "0", "0", "0", "0", "0",
						"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
						"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
						"0" };
			} else {
				return new String[] { "3000", "2000", "1500", "1000", "0", "0",
						"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
						"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
						"0" };
			}
		}
		else
		{
			if (annualPremium <= 25000) {
				return new String[] { "3000", "2000", "1500", "1000", "0", "0",
						"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
						"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
						"0" };
			} else {
				return new String[] { "3000", "2000", "1500", "1000", "0", "0",
						"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
						"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
						"0" };
			}
		}
	}

	public void setSurrenderCharges_AG(double annualPremium, int PPT,String premPayingOption) {
		double min1 = Math.min(Double.parseDouble(getFundValueAtEnd_AF()),
				annualPremium);
		double a = getSurrenderCharge(annualPremium, PPT,premPayingOption);
//		this.surrenderCharges_AG = comm.getRoundOffLevel2New(comm
//				.getStringWithout_E(Math.min((min1 * a),
//						Double.parseDouble(getSurrenderCap_BC()))));
//		System.out.println("PPT "+PPT);
//		System.out.println("min1 "+min1);
//		System.out.println("a "+a);

		this.surrenderCharges_AG = comm.getRoundOffLevel2New(comm
				.getStringWithout_E(Math.min((min1 * a),
						Double.parseDouble(getSurrenderCap_BC()))));
	}

	public String getSurrenderCharges_AG() {
		return surrenderCharges_AG;
	}

	public double getSurrenderCharge(double annualPremium, int PPT, String premPayingOption) {
		double surrenderCharge = 0;
		/*if (PPT == 1) {
			surrenderCharge = Double.parseDouble(calCapArr(annualPremium,premPayingOption)[Math
					.min((Integer.parseInt(getYear_F()) - 1), 11)]);
		} else {
			surrenderCharge = Double
					.parseDouble(calSurrRateArr(annualPremium,premPayingOption)[Math.min(
							Integer.parseInt(getYear_F()) - 1, 11)]);
		}
		return surrenderCharge;*/

		if(premPayingOption.equals("Single")){
			if(annualPremium >25000){
				if(annualPremium <=300000){
					if(getYear_F().equals("1"))
					{
						surrenderCharge = 0.02;
					}
					else if(getYear_F().equals("2"))
					{
						surrenderCharge = 0.015;
					}
					else if(getYear_F().equals("3"))
					{
						surrenderCharge = 0.01;
					}
					else if(getYear_F().equals("4"))
					{
						surrenderCharge = 0.005;
					}
					else
					{
						surrenderCharge=0;
					}
				} else {
					if(getYear_F().equals("1"))
					{
						surrenderCharge = 0.01;
					}
					else if(getYear_F().equals("2"))
					{
						surrenderCharge = 0.007;
					}
					else if(getYear_F().equals("3"))
					{
						surrenderCharge = 0.005;
					}
					else if(getYear_F().equals("4"))
					{
						surrenderCharge = 0.0035;
					}
					else
					{
						surrenderCharge=0;
					}
				}
			} else{
				if(getYear_F().equals("1"))
				{
					surrenderCharge = 0.02;
				}
				else if(getYear_F().equals("2"))
				{
					surrenderCharge = 0.015;
				}
				else if(getYear_F().equals("3"))
				{
					surrenderCharge = 0.01;
				}
				else if(getYear_F().equals("4"))
				{
					surrenderCharge = 0.005;
				}
				else
				{
					surrenderCharge=0;
				}
			}
		}
		else{
			if(annualPremium >25000){
				if(annualPremium > 50000){
					if(getYear_F().equals("1"))
					{
						surrenderCharge = 0.06;
					}
					else if(getYear_F().equals("2"))
					{
						surrenderCharge = 0.04;
					}
					else if(getYear_F().equals("3"))
					{
						surrenderCharge = 0.03;
					}
					else if(getYear_F().equals("4"))
					{
						surrenderCharge = 0.02;
					}
					else
					{
						surrenderCharge=0;
					}
				} else {
					if(getYear_F().equals("1"))
					{
						surrenderCharge = 0.2;
					}
					else if(getYear_F().equals("2"))
					{
						surrenderCharge = 0.15;
					}
					else if(getYear_F().equals("3"))
					{
						surrenderCharge = 0.1;
					}
					else if(getYear_F().equals("4"))
					{
						surrenderCharge = 0.05;
					}
					else
					{
						surrenderCharge=0;
					}
				}
			} else{
				if(getYear_F().equals("1"))
				{
					surrenderCharge = 0.2;
				}
				else if(getYear_F().equals("2"))
				{
					surrenderCharge = 0.15;
				}
				else if(getYear_F().equals("3"))
				{
					surrenderCharge = 0.1;
				}
				else if(getYear_F().equals("4"))
				{
					surrenderCharge = 0.05;
				}
				else
				{
					surrenderCharge=0;
				}
			}
		}

	    	 /*double surrenderCharge=0;
	         if(PPT==1)
	         {
	             //surrenderCharge=1;
	             surrenderCharge=Double.parseDouble(calSurrRateArrSP()[Math.min((Integer.parseInt(getYear_F())-1), 11)]);
	         }
	         else
	         {surrenderCharge=Double.parseDouble(calSurrRateArrRP(effectivePremium)[Math.min((Integer.parseInt(getYear_F())-1),11)]);}*/

		return surrenderCharge;
	}

	public String[] calSurrRateArr(double annualPremium,String premPayingOption) {

		if(premPayingOption.equals("Single"))
		{
			if (annualPremium <= 25000) {
				return new String[] { "0.00", "0.00", "0.00", "0.00", "0", "0",
						"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
						"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" };
			} else {
				return new String[] { "0.02", "0.015", "0.01", "0.005", "0", "0",
						"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
						"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
						"0" };
			}
		}
		else
		{
			if (annualPremium <= 25000) {
				return new String[] { "0.20", "0.15", "0.10", "0.05", "0", "0",
						"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
						"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" };
			} else {
				return new String[] { "0.20", "0.15", "0.10", "0.05", "0", "0",
						"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
						"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
						"0" };
			}
		}
	}

	public void setServiceTaxOnSurrenderCharges_AH(boolean surrenderCharges,
												   double serviceTax) {
		if (surrenderCharges) {
			this.serviceTaxOnSurrenderCharges_AH = comm
					.getRoundOffLevel2New(comm.getStringWithout_E(Double
							.parseDouble(getSurrenderCharges_AG()) * serviceTax));
		} else {
			this.serviceTaxOnSurrenderCharges_AH = "" + 0;
		}
	}

	public String getServiceTaxOnSurrenderCharges_AH() {
		return serviceTaxOnSurrenderCharges_AH;
	}

	public void setSurrenderValue_AI() {
		this.surrenderValue_AI = ""
				+ (Double.parseDouble(getFundValueAtEnd_AF())
				- Double.parseDouble(getSurrenderCharges_AG()) - Double
				.parseDouble(getServiceTaxOnSurrenderCharges_AH()));
	}

	public String getSurrenderValue_AI() {
		return surrenderValue_AI;
	}

	public void setDeathBenefit_AJ(double sumOfPremium, double minGuarantee,
								   double loyaltyAddition, int policyTerm) {
		double temp1, temp2, a;
		temp1 = sumOfPremium * (1 + minGuarantee);
		if (Integer.parseInt(getMonth_E()) == policyTerm * 12) {
			a = 0;
		} else {
			a = loyaltyAddition;
		}

		temp2 = Double.parseDouble(getFundValueAtEnd_AF()) * (1 + a);

		if (Integer.parseInt(getYear_F()) <= policyTerm) {
//			this.deathBenefit_AJ = "" + ((Math.max(temp1, temp2)) + Double.parseDouble(getGuaranteedAdditon_AD()));
			this.deathBenefit_AJ = "" + ((Math.max(temp1, temp2)));
		}
	}

	public String getDeathBenefit_AJ() {
		return deathBenefit_AJ;
	}

	public void setMortalityCharges_AK(int ageAtEntry, double sumAssured,
									   String[] forBIArray, int policyTerm, boolean mortalityCharges,
									   double _fundValueAtEnd_AX, String premPaymentOption) {
		if (!(getPolicyInForce_G().equals("Y"))
				|| Integer.parseInt(getYear_F()) > policyTerm) {
			this.mortalityCharges_S = "" + 0;
		} else {
			double arrOutput = Double
					.parseDouble(forBIArray[(ageAtEntry + Integer
							.parseInt(getYear_F())) - 1]);
			// System.out.println("ARRAY OUTPUT "+arrOutput);
			double a = 1 - arrOutput / 1000;
			// System.out.println("a/1000 "+a);
			double b = (double) 1 / 12;
			// System.out.println("1/12 "+b);
			// System.out.println("whole value "+(comm.pow(a,b)));
			// double c=(sumAssured *
			// getIncreasingSA(increasingCoverOption)-(Double.parseDouble(getAmountAvailableForInvestment_N())+_fundValueAtEnd_AC));
			double c = sumAssured * getIncreasingSA(premPaymentOption);
			// System.out.println("c "+c);
			int d = 0;
			if (mortalityCharges) {
				d = 1;
			} else {
				d = 0;
			}

			double max1 = Math
					.max(Double
									.parseDouble(getOneHundredPercentOfCummulativePremium_BD()),
							c);
			// System.out.println("e max "+max1);
			double diff = max1
					- (Double.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AX);

			// System.out.println("diff" +diff);
			// this.mortalityCharges_R=
			// comm.roundUp_Level2(comm.getStringWithout_E((((1-(comm.pow(a,b)))*Math.max(diff,
			// 0))*d)));
			this.mortalityCharges_AK = comm.roundUp_Level2(comm
					.getStringWithout_E((((1 - (comm.pow(a, b))) * Math.max(
							diff, 0)) * d)));
		}
	}

	public String getMortalityCharges_AK() {
		return mortalityCharges_AK;
	}

	public void setAccTPDCharge_AL(int policyTerm, double accTPDCharge,
								   double sumAssured, boolean mortalityCharges,
								   double _fundValueAtEnd_AX, String premPaymentOption) {
		if (!(getPolicyInForce_G().equals("Y"))
				|| Integer.parseInt(getYear_F()) > policyTerm) {
			this.accTPDCharges_T = "" + 0;
		} else {
			double accTPD = accTPDCharge / 12000;
			// System.out.println("accTPD "+accTPD);
			double a = sumAssured * getIncreasingSA(premPaymentOption);
			// System.out.println("a "+a);
			int b = 0;
			if (mortalityCharges) {
				b = 1;
			} else {
				b = 0;
			}
			double max1 = Math
					.max(Double
									.parseDouble(getOneHundredPercentOfCummulativePremium_BD()),
							a);
			// System.out.println("e max "+max1);
			double diff = max1
					- (Double.parseDouble(getAmountAvailableForInvestment_N()) + _fundValueAtEnd_AX);
			// System.out.println("diff" +diff);
			this.accTPDCharges_AL = comm.getRoundOffLevel2New(comm
					.getStringWithout_E(((accTPD * Math.max(diff, 0)) * b)));
		}
	}

	public String getAccTPDCharge_AL() {
		return accTPDCharges_AL;
	}

	public void setTotalCharges_AM(int policyTerm) {
		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			this.totalCharges_AM = ""
					+ (Double.parseDouble(getPolicyAdministrationCharge_Q())
					+ Double.parseDouble(getMortalityCharges_AK())
					+ Double.parseDouble(getAccTPDCharge_AL())
					+ Double.parseDouble(getSumAssuredRelatedCharges_O()) + Double
					.parseDouble(getRiderCharges_P()));
		} else {
			this.totalCharges_AM = "" + 0;
		}
	}

	public String getTotalCharges_AM() {
		return totalCharges_AM;
	}

	public void setServiceTaxExclOfSTOnAllocAndSurr_AN(
			boolean mortalityAndRiderCharges, boolean administrationCharges,
			double serviceTax) {
		double add1, add2;
		if (mortalityAndRiderCharges) {
			add1 = (Double.parseDouble(getMortalityCharges_AK()) + Double
					.parseDouble(getAccTPDCharge_AL()));
		} else {
			add1 = 0;
		}
		if (administrationCharges) {
			add2 = Double.parseDouble(getPolicyAdministrationCharge_Q());
		} else {
			add2 = 0;
		}
		this.serviceTaxExclOfSTOnAllocAndSurr_AN = comm.getRoundOffLevel2New(comm
				.getStringWithout_E((add1 + add2) * serviceTax));
	}

	public String getServiceTaxExclOfSTOnAllocAndSurr_AN() {
		return serviceTaxExclOfSTOnAllocAndSurr_AN;
	}

	public void setAdditionToFundIfAny_AP(double _fundValueAtEnd_AX,
										  int policyTerm, double int1) {
		/*if (Integer.parseInt(getYear_F()) <= policyTerm) {
			// System.out.println("without Roundup "+(_fundValueAtEnd_AF+Double.parseDouble(getAmountAvailableForInvestment_N())-Double.parseDouble(getTotalCharges_U())-Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_V())+Double.parseDouble(getRiderCharges_P()))*(comm.pow((1+int1),(double)1/12)-1));
			this.additionToFundIfAny_AP = comm
					.getRoundOffLevel2New(comm.getStringWithout_E((_fundValueAtEnd_AX
							+ Double.parseDouble(getAmountAvailableForInvestment_N())
							- Double.parseDouble(getTotalCharges_AM())
							- Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_AN()) + Double
							.parseDouble(getRiderCharges_P()))
							* (comm.pow((1 + int1), (double) 1 / 12) - 1)));
		} else {
			this.additionToFundIfAny_AP = "" + 0;
		}*/

		if(Integer.parseInt(getYear_F()) <= policyTerm)
		{
			double temp1=0,temp2=0;
			temp1=(_fundValueAtEnd_AX+Double.parseDouble(getAmountAvailableForInvestment_N())- Double.parseDouble(getTotalCharges_AM())-Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_AN())+ Double.parseDouble(getRiderCharges_P()));
			double a=(double)1/12;
			temp2=(comm.pow((1+int1),a))-1;
			this.additionToFundIfAny_AP= comm.roundUp_Level2( comm.getStringWithout_E(temp1*temp2));
		}
		else
		{this.additionToFundIfAny_AP=""+0;}
	}

	public String getAdditionToFundIfAny_AP() {
		return additionToFundIfAny_AP;
	}

	public void setFundBeforeFMC_AQ(double _fundValueAtEnd_AX, int policyTerm) {
		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			this.fundBeforeFMC_AQ = ""
					+ (_fundValueAtEnd_AX
					+ Double.parseDouble(getAmountAvailableForInvestment_N())
					- Double.parseDouble(getTotalCharges_AM())
					+ Double.parseDouble(getAdditionToFundIfAny_AP())
					- Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_AN()) + Double
					.parseDouble(getRiderCharges_P()));
		} else {
			this.fundBeforeFMC_AQ = "" + 0;
		}
	}

	public String getFundBeforeFMC_AQ() {
		return fundBeforeFMC_AQ;
	}

	public void setFundManagementCharge_AR(int policyTerm,
										   double percentToBeInvested_EquityPensionFund,
										   double percentToBeInvested_EquityOptPensionFund,
										   double percentToBeInvested_GrowthPensionFund,
										   double percentToBeInvested_BondPensionFund,
										   double percentToBeInvested_MoneyMarketPesionFund,String fundOption) {
		double temp1 = 0, temp2 = 0;
		if (Integer.parseInt(getMonth_E()) == 1) {
			temp1 = RS_prop.charge_Fund;
		} else {
			temp1 = 0;
		}
		/*temp2 = getCharge_FundRen(percentToBeInvested_EquityPensionFund,
				percentToBeInvested_EquityOptPensionFund,
				percentToBeInvested_GrowthPensionFund,
				percentToBeInvested_BondPensionFund,
				percentToBeInvested_MoneyMarketPesionFund,
				fundOption,policyTerm);
		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			this.fundManagementCharge_AR = comm.getRoundOffLevel2New(comm
					.getStringWithout_E(Double
							.parseDouble(getFundBeforeFMC_AQ())
							* (temp1 + temp2 / 12)));
		} else {
			this.fundManagementCharge_AR = "" + 0;
		}*/

		if(Integer.parseInt(getYear_F()) <= policyTerm)
		{
			if(Integer.parseInt(getMonth_E())==1)
			{this.fundManagementCharge_AR=comm.roundUp_Level2(comm.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_AQ()) * ((getCharge_FundRen(percentToBeInvested_EquityPensionFund,
					percentToBeInvested_EquityOptPensionFund,
					percentToBeInvested_GrowthPensionFund,
					percentToBeInvested_BondPensionFund,
					percentToBeInvested_MoneyMarketPesionFund,fundOption,policyTerm)/12))));}
			else
			{this.fundManagementCharge_AR=comm.roundUp_Level2(comm.getStringWithout_E(Double.parseDouble(getFundBeforeFMC_AQ()) * (getCharge_FundRen(percentToBeInvested_EquityPensionFund,
					percentToBeInvested_EquityOptPensionFund,
					percentToBeInvested_GrowthPensionFund,
					percentToBeInvested_BondPensionFund,
					percentToBeInvested_MoneyMarketPesionFund,fundOption,policyTerm)/12)));}
		}
		else
		{this.fundManagementCharge_AR=""+0;}

	}

	public String getFundManagementCharge_AR() {
		return fundManagementCharge_AR;
	}

	public void setGuaranteeCharge_AS(double guarantee_chg, int policyTerm,
									  boolean guaranteeCharge,String fundOption) {
		double temp1;
		String temp2;
		if (guaranteeCharge) {
			temp1 = 1;
		} else {
			temp1 = 0;
		}

		if(fundOption.equals("Smart Choice Plan"))
		{
			guarantee_chg=0;
		}

		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			temp2 = (comm.getStringWithout_E(Double
					.parseDouble(getFundBeforeFMC_AQ()) * guarantee_chg / 12));
		} else {
			temp2 = "" + 0;
		}
		this.guranteeCharge_AS = ""+(temp2);
	}

	public String getGuaranteeCharge_AS() {
		return guranteeCharge_AS;
	}

	/* Modified by akhaya */
	public void setServiceTaxOnFMC_AT(boolean fundManagementCharges,
									  double serviceTax, double percentToBeInvested_EquityPensionFund,
									  double percentToBeInvested_EquityOptPensionFund,
									  double percentToBeInvested_GrowthPensionFund,
									  double percentToBeInvested_BondPensionFund,
									  double percentToBeInvested_MoneyMarketPesionFund,String fundOption,int policyTerm) {
		/*double a = 0;
		// System.out.println("charge fund "+getCharge_FundRen(
		// percentToBeInvested_EquityFund,
		// percentToBeInvested_BondFund,percentToBeInvested_Top300Fund,percentToBeInvested_EquityOptFund,percentToBeInvested_GrowthFund,
		// percentToBeInvested_BalancedFund,
		// percentToBeInvested_MoneyMarketFund));

		String b=comm.getRoundOffLevel2New(comm.getStringWithout_E(Double.parseDouble(getFundManagementCharge_AR())
				* getCharge_FundRen(percentToBeInvested_EquityPensionFund,
						percentToBeInvested_EquityOptPensionFund,
						percentToBeInvested_GrowthPensionFund,
						percentToBeInvested_BondPensionFund,
						percentToBeInvested_MoneyMarketPesionFund,
						fundOption,
						policyTerm)
				/ getCharge_FundRen(percentToBeInvested_EquityPensionFund,
						percentToBeInvested_EquityOptPensionFund,
						percentToBeInvested_GrowthPensionFund,
						percentToBeInvested_BondPensionFund,
						percentToBeInvested_MoneyMarketPesionFund,
						fundOption,
						policyTerm)));
//		System.out.println("b "+b);

		if (fundManagementCharges) {
			a = 1;
		} else {
			a = 0;
		}
//		System.out.println("a "+a);
//		this.serviceTaxOnFMC_AT = comm.getRoundOffLevel2New(comm.getStringWithout_E((Double.parseDouble(getGuaranteeChargeReductionYield_AB()) + Double.parseDouble(b))* serviceTax *1 *a));
		this.serviceTaxOnFMC_AT =""+0;*/
		double a = 0;
		// System.out.println("charge fund "+getCharge_FundRen(
		// percentToBeInvested_EquityFund,
		// percentToBeInvested_BondFund,percentToBeInvested_Top300Fund,percentToBeInvested_EquityOptFund,percentToBeInvested_GrowthFund,
		// percentToBeInvested_BalancedFund,
		// percentToBeInvested_MoneyMarketFund));
//		System.out.println("getGuaranteeCharge_AA() "+getGuaranteeCharge_AA());
//		System.out.println("getFundManagementCharge_Z() "+getFundManagementCharge_Z());
		String b=comm.getRoundOffLevel2New(comm.getStringWithout_E(Double.parseDouble(getFundManagementCharge_AR())
				* getCharge_FundRen(percentToBeInvested_EquityPensionFund,
				percentToBeInvested_EquityOptPensionFund,
				percentToBeInvested_GrowthPensionFund,
				percentToBeInvested_BondPensionFund,
				percentToBeInvested_MoneyMarketPesionFund,
				fundOption,
				policyTerm)
				/ getCharge_FundRen(percentToBeInvested_EquityPensionFund,
				percentToBeInvested_EquityOptPensionFund,
				percentToBeInvested_GrowthPensionFund,
				percentToBeInvested_BondPensionFund,
				percentToBeInvested_MoneyMarketPesionFund,
				fundOption,
				policyTerm)));
//		System.out.println("b "+b);

		if (fundManagementCharges) {
			a = 1;
		} else {
			a = 0;
		}
		// System.out.println("a "+a);
		this.serviceTaxOnFMC_AT = ""+comm.getRoundOffLevel2New(comm.getStringWithout_E((Double.parseDouble(getGuaranteeCharge_AS()) + Double.parseDouble(b))* serviceTax *1));


	}

	public String getServiceTaxOnFMC_AT() {
		return serviceTaxOnFMC_AT;
	}

	public void setTotalServiceTax_AO(boolean riderCharges, double serviceTax) {
		double temp;
		if (riderCharges) {
			temp = Double.parseDouble(getRiderCharges_P());
		} else {
			temp = 0;
		}
		this.totalServiceTax_AO = comm.getRoundOffLevel2New(comm
				.getStringWithout_E(Double
						.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_AN())
						+ Double.parseDouble(getServiceTaxOnFMC_AT())
						+ Double.parseDouble(getServiceTaxOnAllocation_M())
						+ temp
						* serviceTax));
	}

	public String getTotalServiceTax_AO() {
		return totalServiceTax_AO;
	}

	public void setFundValueAfterFMCandBeforeGA_AU(double serviceTax,
												   int policyTerm) {
		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			this.fundValueAfterFMCBerforeGA_AU = ""
					+ (Double.parseDouble(getFundBeforeFMC_AQ())
					- Double.parseDouble(getFundManagementCharge_AR())
					- Double.parseDouble(getGuaranteeCharge_AS())
					- Double.parseDouble(getServiceTaxOnFMC_AT()));
		} else {
			this.fundValueAfterFMCBerforeGA_AU = "" + 0;
		}
	}

	public String getFundValueAfterFMCandBeforeGA_AU() {
		return fundValueAfterFMCBerforeGA_AU;
	}

	public void setGuaranteedAddition_AV(int policyTerm, double annualPremium,String premPayingOption) {
		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			this.guaranteedAddition_AV =comm.getRoundOffLevel2New(comm.getStringWithout_E( getGA(premPayingOption,policyTerm) * annualPremium));
		} else {
			this.guaranteedAddition_AV = "" + 0;
		}
	}

	public String getGuaranteedAdditon_AV() {
		return guaranteedAddition_AV;
	}

	public void setLoyaltyAddition_AW(double loyaltyAddition, int policyTerm) {
		if (Integer.parseInt(getMonth_E()) == policyTerm * 12) {
			this.loyaltyAddition_AW = comm
					.getRoundOffLevel2New(comm.getStringWithout_E(loyaltyAddition
							* (Double
							.parseDouble(getFundValueAfterFMCandBeforeGA_AU()) + Double
							.parseDouble(getGuaranteedAdditon_AV()))));
		} else {
			this.loyaltyAddition_AW = "" + 0;
		}
	}

	/*** Added by Priyanka on 18-nov-2014 start ***/
	public String getnetYieldAt8Percent() {
		return netYieldAt8Percent;
	}

	public void setnetYieldAt8Percent() {
		this.netYieldAt8Percent = "" + Double.parseDouble(getIRRAnnual_BV())
				* 100;
	}

	public String getnetYieldAt4Percent() {
		return netYieldAt4Percent;
	}

	public void setnetYieldAt4Percent() {
		this.netYieldAt4Percent = "" + Double.parseDouble(getIRRAnnual_BU())
				* 100;
	}

	public double getCommission_AQ(double annualisedPrem, double topupPrem,
								   RetireSmartBean retireSmartBean) {
		double topup = 0.01;
//		System.out.println("getCommistionRate "
//				+ getCommistionRate(retireSmartBean) + " " + annualisedPrem
//				+ " " + topupPrem);
		if (retireSmartBean.getStaffDisc())
			return 0;
		else
			return getCommistionRate(retireSmartBean) * annualisedPrem
					+ topupPrem * topup;

	}

	private double getCommistionRate(RetireSmartBean retireSmartBean) {
		int findYear;

		if (Integer.parseInt(getYear_F()) > 10) {
			findYear = 11;
		} else
			findYear = Integer.parseInt(getYear_F());

		if (retireSmartBean.getStaffDisc()) {
			return 0;
		} else if (retireSmartBean.getPolicyTerm() == 10) {
			double[] commArr = { 0.05, 0.02, 0.02, 0.02, 0.02, 0.02, 0.02,
					0.02, 0.02, 0.02, 0.02 };
			double temp = commArr[findYear - 1];
			return commArr[findYear - 1];
		} else {
			double[] commArr = { 0.075, 0.02, 0.02, 0.02, 0.02, 0.02, 0.02,
					0.02, 0.02, 0.02, 0.02 };
			double temp = commArr[findYear - 1];
			return commArr[findYear - 1];
		}
	}

	public double getTotalFirstYearPremium(double sum_C, double sum_E,
										   double sum_FY_Premium) {
		boolean adb = false;
		String Eligibility_ADB = "Not Eligible";
		double sum = 0;
		if (adb && Eligibility_ADB.equals("Eligible")) {
			sum = sum_C + sum_E + sum_FY_Premium;
		} else {
			sum = sum_E + sum_FY_Premium;
		}
		return sum;
	}

	/*** Added by Priyanka on 18-nov-2014 start ***/
	public String getLoyaltyAddition_AW() {
		return loyaltyAddition_AW;
	}

	public void setFundValueAtEnd_AX() {
		this.fundValueAtEnd_AX = ""
				+ (Double.parseDouble(getFundValueAfterFMCandBeforeGA_AU())
				+ Double.parseDouble(getGuaranteedAdditon_AV()) + Double
				.parseDouble(getLoyaltyAddition_AW()));
	}

	public String getFundValueAtEnd_AX() {
		return fundValueAtEnd_AX;
	}

	public void setSurrenderCharges_AY(double annualPremium, int PPT,String premPayingOption) {
		double min1 = Math.min(Double.parseDouble(getFundValueAtEnd_AX()),
				annualPremium);
		double a = getSurrenderCharge(annualPremium, PPT,premPayingOption);
		this.surrenderCharges_AY = comm.getRoundOffLevel2New(comm
				.getStringWithout_E(Math.min((min1 * a),
						Double.parseDouble(getSurrenderCap_BC()))));
	}

	public String getSurrenderCharges_AY() {
		return surrenderCharges_AY;
	}

	public void setServiceTaxOnSurrenderCharges_AZ(boolean surrenderCharges,
												   double serviceTax) {
		if (surrenderCharges) {
			this.serviceTaxOnSurrenderCharges_AZ = comm
					.getRoundOffLevel2New(comm.getStringWithout_E(Double
							.parseDouble(getSurrenderCharges_AY()) * serviceTax));
		} else {
			this.serviceTaxOnSurrenderCharges_AZ = "" + 0;
		}
	}

	public String getServiceTaxOnSurrenderCharges_AZ() {
		return serviceTaxOnSurrenderCharges_AZ;
	}

	public void setSurrenderValue_BA() {
		this.surrenderValue_BA = ""
				+ (Double.parseDouble(getFundValueAtEnd_AX())
				- Double.parseDouble(getSurrenderCharges_AY()) - Double
				.parseDouble(getServiceTaxOnSurrenderCharges_AZ()));
	}

	public String getSurrenderValue_BA() {
		return surrenderValue_BA;
	}

	public void setDeathBenefit_BB(double sumOfPremium, double minGuarantee,
								   double loyaltyAddition, int policyTerm) {
		double temp1, temp2, a;
		temp1 = sumOfPremium * (1 + minGuarantee);
		if (Integer.parseInt(getMonth_E()) == policyTerm * 12) {
			a = 0;
		} else {
			a = loyaltyAddition;
		}

		temp2 = Double.parseDouble(getFundValueAtEnd_AX()) * (1 + a);

		if (Integer.parseInt(getYear_F()) <= policyTerm) {
//			this.deathBenefit_BB =comm.getRoundOffLevel2New(comm.getStringWithout_E(Math.max(temp1, temp2) +Double.parseDouble(getGuaranteedAdditon_AV())));
			this.deathBenefit_BB =comm.getRoundOffLevel2New(comm.getStringWithout_E(Math.max(temp1, temp2)));
		}
	}

	public String getDeathBenefit_BB() {
		return deathBenefit_BB;
	}

	public void setFundBeforeFMCReductionYield_Y(double _fundValueAtEnd_AF,
												 int policyTerm) {
		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			this.fundBeforeFMCReductionYield_Y =
					(comm.getStringWithout_E(_fundValueAtEnd_AF
							+ Double.parseDouble(getAmountAvailableForInvestment_N())
							- Double.parseDouble(getTotalCharges_U())
							+ Double.parseDouble(getAdditionToFundIfAny_X())
							- Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_V())
							+ Double.parseDouble(getRiderCharges_P())));
		} else {
			this.fundBeforeFMCReductionYield_Y = "" + 0;
		}
	}

	public String getFundBeforeFMCReductionYield_Y() {
		return fundBeforeFMCReductionYield_Y;
	}

	public void setFundManagementChargeReductionYield_Z(int policyTerm,
														double percentToBeInvested_EquityPensionFund,
														double percentToBeInvested_EquityOptPensionFund,
														double percentToBeInvested_GrowthPensionFund,
														double percentToBeInvested_BondPensionFund,
														double percentToBeInvested_MoneyMarketPesionFund,String fundOption) {
		double temp1 = 0, temp2 = 0;
		if (Integer.parseInt(getMonth_E()) == 1) {
			temp1 = RS_prop.charge_Fund;
		} else {
			temp1 = 0;
		}
		temp2 = getCharge_FundRen(percentToBeInvested_EquityPensionFund,
				percentToBeInvested_EquityOptPensionFund,
				percentToBeInvested_GrowthPensionFund,
				percentToBeInvested_BondPensionFund,
				percentToBeInvested_MoneyMarketPesionFund,
				fundOption,policyTerm);
		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			this.fundManagementChargeReductionYield_Z = comm
					.getRoundOffLevel2New(comm.getStringWithout_E(Double
							.parseDouble(getFundBeforeFMCReductionYield_Y())
							* (temp1 + temp2 / 12)));
		} else {
			this.fundManagementChargeReductionYield_Z = "" + 0;
		}
	}

	public String getFundManagementChargeReductionYield_Z() {
		return fundManagementChargeReductionYield_Z;
	}

	public void setGuaranteeChargeReductionYield_AA(double guarantee_chg,
													int policyTerm, boolean guaranteeCharge,String fundOption) {
		/*double temp1;
		String temp2;
		if (guaranteeCharge) {
			temp1 = 1;
		} else {
			temp1 = 0;
		}
		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			temp2 = (comm.getRoundUp(comm.getStringWithout_E(Double
					.parseDouble(getFundBeforeFMCReductionYield_Y())
					* guarantee_chg / 12)));
		} else {
			temp2 = "" + 0;
		}
		this.guranteeChargeReductionYield_AA =comm.getRoundOffLevel2New(comm.getStringWithout_E(Double.parseDouble(temp2)));*/
		double temp1;
		if (guaranteeCharge) {
			temp1 = 1;
		} else {
			temp1 = 0;
		}

		this.guranteeChargeReductionYield_AA =""+0;
	}

	public String getGuaranteeChargeReductionYield_AA() {
		return guranteeChargeReductionYield_AA;
	}

	public void setGuaranteeChargeReductionYield_AB(double guarantee_chg,
													int policyTerm, boolean guaranteeCharge,String fundOption) {
		/*double temp1;
		String temp2;
		if (guaranteeCharge) {
			temp1 = 1;
		} else {
			temp1 = 0;
		}

		if(fundOption.equals("Smart Choice Plan"))
		{
			guarantee_chg=0;
		}

		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			temp2 = ((comm.getStringWithout_E(Double
					.parseDouble(getFundBeforeFMCReductionYield_Y())
					* guarantee_chg / 12)));
		} else {
			temp2 = "" + 0;
		}
		this.guranteeChargeReductionYield_AA =comm.getRoundOffLevel2New(comm.getStringWithout_E(Double.parseDouble(temp2)));*/
		double temp1;
		if (guaranteeCharge) {
			temp1 = 1;
		} else {
			temp1 = 0;
		}

		this.guranteeChargeReductionYield_AB =""+0;
	}

	public String getGuaranteeChargeReductionYield_AB() {
		return guranteeChargeReductionYield_AB;
	}
	public void setServiceTaxOnFMCReductionYield_AB(
			boolean fundManagementCharges, double serviceTax,
			double percentToBeInvested_EquityPensionFund,
			double percentToBeInvested_EquityOptPensionFund,
			double percentToBeInvested_GrowthPensionFund,
			double percentToBeInvested_BondPensionFund,
			double percentToBeInvested_MoneyMarketPesionFund,String fundOption,int policyTerm) {
		double a = 0;
		// System.out.println("charge fund "+getCharge_FundRen(
		// percentToBeInvested_EquityFund,
		// percentToBeInvested_BondFund,percentToBeInvested_Top300Fund,percentToBeInvested_EquityOptFund,percentToBeInvested_GrowthFund,
		// percentToBeInvested_BalancedFund,
		// percentToBeInvested_MoneyMarketFund));
		if (fundManagementCharges) {
			a = (Double.parseDouble(getGuaranteeChargeReductionYield_AA()) + Double
					.parseDouble(getFundManagementChargeReductionYield_Z())
					* 0.0135
					/ getCharge_FundRen(percentToBeInvested_EquityPensionFund,
					percentToBeInvested_EquityOptPensionFund,
					percentToBeInvested_GrowthPensionFund,
					percentToBeInvested_BondPensionFund,
					percentToBeInvested_MoneyMarketPesionFund,fundOption,policyTerm));
		} else {
			a = 0;
		}
		// System.out.println("a "+a);
		this.serviceTaxOnFMCReductionYield_AB = comm.getRoundOffLevel2New(comm
				.getStringWithout_E(a * serviceTax));
	}

	public String getServiceTaxOnFMCReductionYield_AB() {
		return serviceTaxOnFMCReductionYield_AB;
	}

	public void setTotalServiceTaxReductionYield_W(double serviceTax,
												   boolean riderCharges) {
		double a = 0;
		if (riderCharges) {
			a = Double.parseDouble(getRiderCharges_P());
		} else {
			a = 0;
		}

		this.totalServiceTaxReductionYield_W = comm
				.getRoundOffLevel2New(comm.getStringWithout_E(Double
						.parseDouble(getServiceTaxOnAllocation_M())
						+ Double.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_V())
						+ Double.parseDouble(getServiceTaxOnFMCReductionYield_AB())
						+ a * serviceTax));
	}

	public String getTotalServiceTaxReductionYield_W() {
		return totalServiceTaxReductionYield_W;
	}

	public void setFundValueAfterFMCandBeforeGAReductionYield_AC(
			double serviceTax, int policyTerm) {
		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			this.fundValueAfterFMCBerforeGAReductionYield_AC = ""
					+ (Double.parseDouble(getFundBeforeFMCReductionYield_Y())
					- Double.parseDouble(getFundManagementChargeReductionYield_Z())
					- Double.parseDouble(getGuaranteeChargeReductionYield_AA()) - Double
					.parseDouble(getServiceTaxOnFMCReductionYield_AB()));
		} else {
			this.fundValueAfterFMCBerforeGAReductionYield_AC = "" + 0;
		}
	}

	public String getFundValueAfterFMCandBeforeGAReductionYield_AC() {
		return fundValueAfterFMCBerforeGAReductionYield_AC;
	}

	public void setGuaranteedAdditionReductionYield_AD(int policyTerm,
													   double annualPremium, boolean guaranteedAddition) {
		double a = 0;
		if (guaranteedAddition) {
			a = 1;
		} else {
			a = 0;
		}
		// System.out.println("GA "+getGA());
		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			/*this.guaranteedAdditionReductionYield_AD = "" + getGA(premPayingOption)
					annualPremium * a;*/
			this.guaranteedAdditionReductionYield_AD = "" + 0;
		} else {
			this.guaranteedAdditionReductionYield_AD = "" + 0;
		}
	}

	public String getGuaranteedAdditonReductionYield_AD() {
		return guaranteedAdditionReductionYield_AD;
	}

	public void setLoyaltyAdditionReductionYield_AE(double loyaltyAddition,
													int policyTerm, boolean terminalAddition) {
		double a = 0;
		if (terminalAddition) {
			a = 1;
		} else {
			a = 0;
		}

		if (Integer.parseInt(getMonth_E()) == policyTerm * 12) {
			this.loyaltyAdditionReductionYield_AE =comm.getRoundOffLevel2New( comm.getStringWithout_E (loyaltyAddition
					* (Double
					.parseDouble(getFundValueAfterFMCandBeforeGAReductionYield_AC()) + Double
					.parseDouble(getGuaranteedAdditonReductionYield_AD())) * a));
		} else {
			this.loyaltyAdditionReductionYield_AE = "" + 0;
		}
	}

	public String getLoyaltyAdditionReductionYield_AE() {
		return loyaltyAdditionReductionYield_AE;
	}

	public void setFundValueAtEndReductionYield_AF() {/*
		this.fundValueAtEndReductionYield_AF = ""
				+ (Double
				.parseDouble(getFundValueAfterFMCandBeforeGAReductionYield_AC())
				+ Double.parseDouble(getGuaranteedAdditonReductionYield_AD()) + Double
				.parseDouble(getLoyaltyAdditionReductionYield_AE()));
	*/

		this.fundValueAtEndReductionYield_AF = ""
				+ (Double
				.parseDouble(getFundValueAfterFMCandBeforeGAReductionYield_AC())
				+ Double.parseDouble(getLoyaltyAdditionReductionYield_AE()));

	}

	public String getFundValueAtEndReductionYield_AF() {
		return fundValueAtEndReductionYield_AF;
	}

	public void setSurrenderChargesReductionYield_AG(double annualPremium,
													 int PPT,String premPayingOption) {
		double min1 = Math.min(
				Double.parseDouble(getFundValueAtEndReductionYield_AF()),
				annualPremium);
		double a = getSurrenderCharge(annualPremium, PPT,premPayingOption);
		this.surrenderChargesReductionYield_AG = comm.getRoundOffLevel2New(comm
				.getStringWithout_E(Math.min((min1 * a),
						Double.parseDouble(getSurrenderCap_BC()))));
	}

	public String getSurrenderChargesRedutionYield_AG() {
		return surrenderChargesReductionYield_AG;
	}

	public void setServiceTaxOnSurrenderChargesReductionYield_AH(
			boolean surrenderCharges, double serviceTax) {
		if (surrenderCharges) {
			this.serviceTaxOnSurrenderChargesReductionYield_AH = comm
					.getRoundOffLevel2New(comm.getStringWithout_E(Double
							.parseDouble(getSurrenderChargesRedutionYield_AG())
							* serviceTax));
		} else {
			this.serviceTaxOnSurrenderChargesReductionYield_AH = "" + 0;
		}
	}

	public String getServiceTaxOnSurrenderChargesReductionYield_AH() {
		return serviceTaxOnSurrenderChargesReductionYield_AH;
	}

	public void setSurrenderValueReductionYield_AI() {
		this.surrenderValueReductionYield_AI = ""
				+ (Double.parseDouble(getFundValueAtEndReductionYield_AF())
				- Double.parseDouble(getSurrenderChargesRedutionYield_AG()) - Double
				.parseDouble(getServiceTaxOnSurrenderChargesReductionYield_AH()));
	}

	public String getSurrenderValueReductionYield_AI() {
		return surrenderValueReductionYield_AI;
	}

	public void setDeathBenefitReductionYield_AJ(double sumOfPremium,
												 double minGuarantee, double loyaltyAddition, int policyTerm) {
		double temp1, temp2, a;
		temp1 = sumOfPremium * (1 + minGuarantee);
		// if(Integer.parseInt(getMonth_E())==policyTerm*12)
		// {a=0;}
		// else
		// {a=loyaltyAddition;}

		temp2 = Double.parseDouble(getFundValueAtEndReductionYield_AF())
				* (1 + loyaltyAddition);

		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			this.deathBenefitReductionYield_AJ = comm.getRoundOffLevel2New(comm
					.getStringWithout_E(Math.max(temp1, temp2)));
		}
	}

	public String getDeathBenefitReductionYield_AJ() {
		return deathBenefitReductionYield_AJ;
	}

	public void setTotalServiceTaxReductionYield_AO(boolean riderCharges,
													double serviceTax) {
		double temp;
		if (riderCharges) {
			temp = Double.parseDouble(getRiderCharges_P());
		} else {
			temp = 0;
		}
		this.totalServiceTaxReductionYield_AO = comm.getRoundOffLevel2New(comm
				.getStringWithout_E(Double
						.parseDouble(getServiceTaxExclOfSTOnAllocAndSurr_AN())
						+ Double.parseDouble(getServiceTaxOnFMC_AT())
						+ Double.parseDouble(getServiceTaxOnAllocation_M())
						+ temp * serviceTax));
	}

	public String getTotalServiceTaxReductionYield_AO() {
		return totalServiceTaxReductionYield_AO;
	}

	/*public void setFundValueAfterFMCandBeforeGAReductionYield_AU(
			double serviceTax, int policyTerm) {
		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			this.fundValueAfterFMCBerforeGAReductionYield_AU = (comm.getStringWithout_E(Double
							.parseDouble(getFundBeforeFMC_AQ())
							- Double.parseDouble(getFundManagementCharge_AR())
							- Double.parseDouble(getGuaranteeChargeReductionYield_AB())
							- Double.parseDouble(getServiceTaxOnFMC_AT())));
		} else {
			this.fundValueAfterFMCBerforeGAReductionYield_AU = "" + 0;
		}
	}*/

	public void setFundValueAfterFMCandBeforeGAReductionYield_AU(
			double serviceTax, int policyTerm) {/*
		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			this.fundValueAfterFMCBerforeGAReductionYield_AU = (comm.getStringWithout_E(Double
							.parseDouble(getFundBeforeFMC_AQ())
							- Double.parseDouble(getFundManagementCharge_AR())
							- Double.parseDouble(getGuaranteeCharge_AS())
							- Double.parseDouble(getServiceTaxOnFMC_AT())));
		} else {
			this.fundValueAfterFMCBerforeGAReductionYield_AU = "" + 0;
		}
	*/

		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			this.fundValueAfterFMCBerforeGAReductionYield_AU = (comm.getStringWithout_E(Double
					.parseDouble(getFundBeforeFMC_AQ())
					- Double.parseDouble(getFundManagementCharge_AR())));
		} else {
			this.fundValueAfterFMCBerforeGAReductionYield_AU = "" + 0;
		}
	}

	/*public String getFundValueAfterFMCandBeforeGAReductionYield_AU() {
		return fundValueAfterFMCBerforeGAReductionYield_AU;
	}*/

	public String getFundValueAfterFMCandBeforeGAReductionYield_AU() {
		return fundValueAfterFMCBerforeGAReductionYield_AU;
	}

	public void setGuaranteedAdditionReductionYield_AV(int policyTerm,
													   double annualPremium, boolean guaranteedAddition,String premPayingOption) {
		double a = 0;
		if (guaranteedAddition) {
			a = 1;
		} else {
			a = 0;
		}

		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			this.guaranteedAdditionReductionYield_AV =comm.getRoundOffLevel2New(comm.getStringWithout_E( getGA(premPayingOption,policyTerm)
					* annualPremium * a));
		} else {
			this.guaranteedAdditionReductionYield_AV = "" + 0;
		}

		/*double a = 0;
		if (guaranteedAddition) {
			a = 1;
		} else {
			a = 0;
		}*/

		/*if (Integer.parseInt(getYear_F()) <= policyTerm) {
			this.guaranteedAdditionReductionYield_AV =comm.getRoundOffLevel2New(comm.getStringWithout_E( getGA()
					* annualPremium * a));
		}*/
		/*if (Integer.parseInt(getYear_F()) <= policyTerm) {
			this.guaranteedAdditionReductionYield_AV =comm.getRoundOffLevel2New(comm.getStringWithout_E( annualPremium * a));
			this.guaranteedAdditionReductionYield_AV =""+0;
		} else {
			this.guaranteedAdditionReductionYield_AV = "" + 0;
		}*/
	}

	public String getGuaranteedAdditonReductionYield_AV() {
		return guaranteedAdditionReductionYield_AV;
	}

	public void setLoyaltyAdditionReductionYield_AW(double loyaltyAddition,
													int policyTerm, boolean terminalAddition) {
		double a = 0;
		if (terminalAddition) {
			a = 1;
		} else {
			a = 0;
		}

		if (Integer.parseInt(getMonth_E()) == policyTerm * 12) {
			this.loyaltyAdditionReductionYield_AW = comm
					.getRoundOffLevel2New(comm.getStringWithout_E(loyaltyAddition
							* (Double
							.parseDouble(getFundValueAfterFMCandBeforeGAReductionYield_AU()) + Double
							.parseDouble(getGuaranteedAdditonReductionYield_AV()))
							* a));
		} else {
			this.loyaltyAdditionReductionYield_AW = "" + 0;
		}
	}

	public String getLoyaltyAdditionReductionYield_AW() {
		return loyaltyAdditionReductionYield_AW;
	}

	public void setFundValueAtEndReductionYield_AX() {/*
		this.fundValueAtEndReductionYield_AX = ""
				+ (Double
				.parseDouble(getFundValueAfterFMCandBeforeGAReductionYield_AU())
				+ Double.parseDouble(getGuaranteedAdditonReductionYield_AV()) + Double
				.parseDouble(getLoyaltyAdditionReductionYield_AW()));
	*/

		/*this.fundValueAtEndReductionYield_AX = ""
				+ (Double
						.parseDouble(getFundValueAfterFMCandBeforeGAReductionYield_AU())
						+ Double.parseDouble(getLoyaltyAdditionReductionYield_AW()));*/

		this.fundValueAtEndReductionYield_AX = ""
				+ (Double
				.parseDouble(getFundValueAfterFMCandBeforeGAReductionYield_AU())
				+ Double.parseDouble(getGuaranteedAdditonReductionYield_AV()) + Double
				.parseDouble(getLoyaltyAdditionReductionYield_AW()));

	}

	public String getFundValueAtEndReductionYield_AX() {
		return fundValueAtEndReductionYield_AX;
	}

	public void setSurrenderChargesReductionYield_AY(double annualPremium,
													 int PPT,String premPayingOption) {
		double min1 = Math.min(
				Double.parseDouble(getFundValueAtEndReductionYield_AX()),
				annualPremium);
		double a = getSurrenderCharge(annualPremium, PPT,premPayingOption);
		this.surrenderChargesReductionYield_AY = comm.getRoundOffLevel2New(comm
				.getStringWithout_E(Math.min((min1 * a),
						Double.parseDouble(getSurrenderCap_BC()))));
	}

	public String getSurrenderChargesReductionYield_AY() {
		return surrenderChargesReductionYield_AY;
	}

	public void setServiceTaxOnSurrenderChargesReductionYield_AZ(
			boolean surrenderCharges, double serviceTax) {
		if (surrenderCharges) {
			this.serviceTaxOnSurrenderChargesReductionYield_AZ = comm
					.getRoundOffLevel2New(comm.getStringWithout_E(Double
							.parseDouble(getSurrenderChargesReductionYield_AY())
							* serviceTax));
		} else {
			this.serviceTaxOnSurrenderChargesReductionYield_AZ = "" + 0;
		}
	}

	public String getServiceTaxOnSurrenderChargesReductionYield_AZ() {
		return serviceTaxOnSurrenderChargesReductionYield_AZ;
	}

	public void setSurrenderValueReductionYield_BA() {
		this.surrenderValueReductionYield_BA = ""
				+ (Double.parseDouble(getFundValueAtEndReductionYield_AX())
				- Double.parseDouble(getSurrenderChargesReductionYield_AY()) - Double
				.parseDouble(getServiceTaxOnSurrenderChargesReductionYield_AZ()));
	}

	public String getSurrenderValueReductionYield_BA() {
		return surrenderValueReductionYield_BA;
	}

	public void setDeathBenefitReductionYield_BB(double sumOfPremium,
												 double minGuarantee, double loyaltyAddition, int policyTerm) {
		double temp1, temp2, a;
		temp1 = sumOfPremium * (1 + minGuarantee);
		if (Integer.parseInt(getMonth_E()) == policyTerm * 12) {
			a = 0;
		} else {
			a = loyaltyAddition;
		}

		temp2 = Double.parseDouble(getFundValueAtEndReductionYield_AX())
				* (1 + a);

		if (Integer.parseInt(getYear_F()) <= policyTerm) {
			this.deathBenefitReductionYield_BB =comm.getRoundOffLevel2New(comm.getStringWithout_E (Math.max(temp1, temp2)));
		}
	}

	public String getDeathBenefitReductionYield_BB() {
		return deathBenefitReductionYield_BB;
	}

	public void setMonth_BT(int monthNumber) {
		this.monthNumber_BT = monthNumber + "";
	}

	public String getMonth_BT() {
		return monthNumber_BT;
	}

	public void setReductionYield_BZ(int noOfYearsElapsedSinceInception,
									 double _fundValueAtEnd_AX) {
		double a, b;
		// if((Integer.parseInt(getMonth_E())) <=
		// (noOfYearsElapsedSinceInception*12)+1)
		if ((Integer.parseInt(getMonth_BT())) < (noOfYearsElapsedSinceInception * 12)) {
			a = -(Double.parseDouble(getPremium_I()));
		} else {
			a = 0;
		}

		if ((Integer.parseInt(getMonth_BT())) == (noOfYearsElapsedSinceInception * 12)) {
			b = _fundValueAtEnd_AX;
		} else {
			b = 0;
		}
		// System.out.println("a_BU "+a);
		// System.out.println("b_BU "+b);
		this.reductionYield_BZ = "" + (a + b);
	}

	public String getReductionYield_BZ() {
		return reductionYield_BZ;
	}

	public void setReductionYield_BV(int policyTerm, double _fundValueAtEnd_AX) {
		double a, b;
		if ((Integer.parseInt(getMonth_BT())) == (policyTerm * 12)) {

			// System.out.println("getFundValueAtEndReductionYield_AS "+getFundValueAtEndReductionYield_AS());
			a = _fundValueAtEnd_AX;
		} else {
			a = 0;
		}

		if ((Integer.parseInt(getMonth_BT())) < (policyTerm * 12)) {
			b = -(Double.parseDouble(getPremium_I()));
		} else {
			b = 0;
		}
		// System.out.println("a_BQ" +a);
		// this.reductionYield_BQ=""+(Double.parseDouble(getPremium_I()) + a);
		this.reductionYield_BV = "" + (b + a);
	}

	public String getReductionYield_BV() {
		return reductionYield_BV;
	}

	public void setReductionYield_BU(int policyTerm, double _fundValueAtEnd_AF) {
		double a, b;
		if ((Integer.parseInt(getMonth_BT())) == (policyTerm * 12)) {

			// System.out.println("getFundValueAtEndReductionYield_AS "+getFundValueAtEndReductionYield_AS());
			a = _fundValueAtEnd_AF;
		} else {
			a = 0;
		}

		if ((Integer.parseInt(getMonth_BT())) < (policyTerm * 12)) {
			b = -(Double.parseDouble(getPremium_I()));
		} else {
			b = 0;
		}
		// System.out.println("a_BQ" +a);
		// this.reductionYield_BQ=""+(Double.parseDouble(getPremium_I()) + a);
		this.reductionYield_BU = "" + (b + a);
	}

	public String getReductionYield_BU() {
		return reductionYield_BU;
	}

	public double irr(ArrayList<String> values, double guess) {
		int maxIterationCount = 20;
		double absoluteAccuracy = 1E-7;
		double[] arr = new double[values.size()];
		double x0 = guess;
		double x1;

		int i = 0;
		// System.out.println("inside irr ");
		// System.out.println("values "+values);
		while (i < maxIterationCount) {

			// the value of the function (NPV) and its derivate can be
			// calculated in the same loop
			double fValue = 0;
			double fDerivative = 0;
			for (int k = 0; k < values.size(); k++) {
				// System.out.println("value "+Double.parseDouble(values.get(k)));
				arr[k] = Double.parseDouble(values.get(k));
				fValue += arr[k] / Math.pow(1.0 + x0, k);
				fDerivative += -k * arr[k] / Math.pow(1.0 + x0, k + 1);
			}

			// the essense of the Newton-Raphson Method
			x1 = x0 - fValue / fDerivative;

			if (Math.abs(x1 - x0) <= absoluteAccuracy) {
				return x1;
			}

			x0 = x1;
			++i;
		}
		// maximum number of iterations is exceeded
		return 0;
	}

	public void setIRRAnnual_BV(double ans) {
		// System.out.println("aaaaaaa "+((cfap.pow((1+ans),12)-1)*100));
		this.irrAnnual_BV = "" + ((comm.pow((1 + ans), 12) - 1));
	}

	public String getIRRAnnual_BV() {
		return irrAnnual_BV;
	}

	public void setIRRAnnual_BU(double ans) {
		// System.out.println("aaaaaaa "+((cfap.pow((1+ans),12)-1)*100));
		this.irrAnnual_BU = "" + ((comm.pow((1 + ans), 12) - 1));
	}

	public String getIRRAnnual_BU() {
		return irrAnnual_BU;
	}

	public void setIRRAnnual_BZ(double ans) {
		// System.out.println("aaaaaaa "+((comm.pow((1+ans),12)-1)*100));
		this.irrAnnual_BZ = "" + ((comm.pow((1 + ans), 12) - 1));
	}

	public String getIRRAnnual_BZ() {
		return irrAnnual_BZ;
	}

	public void setReductionInYieldMaturityAt(double int2) {
		this.reductionInYieldMaturityAt = ""
				+ ((int2 - Double.parseDouble(getIRRAnnual_BV())) * 100);
	}

	public String getReductionInYieldMaturityAt() {
		return reductionInYieldMaturityAt;
	}

	public void setReductionInYieldNumberOfYearsElapsedSinceInception(
			double int2) {
		this.reductionInYieldNumberOfYearsElapsedSinceInception = ""
				+ ((int2 - Double.parseDouble(getIRRAnnual_BZ())) * 100);
	}

	public String getReductionInYieldNumberOfYearsElapsedSinceInception() {
		return reductionInYieldNumberOfYearsElapsedSinceInception;
	}

	private double setABC(int policyTerm, int ageAtEntry) {
		int sum = policyTerm + ageAtEntry;
		double a = 0, b = 0;
		RetireSmartDB retireSmartDB = new RetireSmartDB();
//		System.out
//				.println("lenth " + retireSmartDB.getAnnuityPlusRate().length);
		// for(int i=0;i<=retireSmartDB.getAnnuityPlusRate().length;i++)
		// {
		// System.out.println("i "+i);
		// if(i==sum)
		// {
		// a=retireSmartDB.getAnnuityPlusRate()[i-40];
		// }
		// System.out.println("a "+a);
		// }

		for (int j = 40; j <= sum; j++) {
//			System.out.println("j " + j);
			if (j == sum) {
				b = retireSmartDB.getAnnuityPlusRate()[j - 40];
			}
//			System.out.println("b " + b);
		}
		return b;
	}

	// public void setAnnuityPay4pa(int policyTerm,int ageAtEntry,double
	// _fundValueAtEnd_AF)
	// {
	// System.out.println(" AF : "+_fundValueAtEnd_AF);
	//
	// this.annuityPay4pa=""+(setABC(policyTerm,
	// ageAtEntry)*_fundValueAtEnd_AF/(1000*(1-0.02))*1.035);
	// }
	// public String getAnnuityPay4pa()
	// {return annuityPay4pa;}
	//
	// public void setAnnuityPay8pa(int policyTerm,int ageAtEntry,double
	// _fundValueAtEnd_AX)
	// {
	// System.out.println(" AX : "+_fundValueAtEnd_AX);
	//
	//
	// this.annuityPay8pa=""+(setABC(policyTerm,
	// ageAtEntry)*_fundValueAtEnd_AX/(1000*(1-0.02))*1.035);
	// }
	// public String getAnnuityPay8pa()
	// {return annuityPay8pa;}
	//
	//
	// public void setAnnuityRates(int policyTerm, int ageAtEntry) {
	// // System.out.println(" ABC "+setABC(policyTerm, ageAtEntry));
	//
	// this.annuityRates = ""
	// + (setABC(policyTerm, ageAtEntry) / (1 * (1 - 0.02)) * 1.035);
	// }
	//
	// public String getAnnuityRates() {
	// return annuityRates;
	// }

	public void setAnnuityPay4pa(double maxVestingBenefit) {

//		double a = 0, b = 0, premRateAftLoadng = 0, freqMode, serviceTax = 0, amountAfterST = 0, purchasePerice = 0;
//		// double[] lifetimeIncome_Arr=objDB.getLifeTimeIncome_Arr();
//		// System.out.println(saralPensionBean.getPolicyTerm()+saralPensionBean.getAge()-1);
//		b = setABC(policyTerm, ageAtEntry);
//
//		if (premFreq.equalsIgnoreCase("Yearly")) {
//			premRateAftLoadng = b * 1.035;
//			freqMode = 1;
//		} else {
//			premRateAftLoadng = b * 1.035;
//			freqMode = 1;
//		}
////		_fundValueAtEnd_AF=16972505;
////		System.out.println(" _fundValueAtEnd_AF : "+_fundValueAtEnd_AF);
//		/****** Added by - Priyanka Warekar 25-08-2015 - Start *****/
//		if (false)
//			serviceTax = comm.roundDown(
//					(_fundValueAtEnd_AF - (_fundValueAtEnd_AF / (1 + 0))), 4);
//		else
//			serviceTax = comm.roundDown(
//					(_fundValueAtEnd_AF - (_fundValueAtEnd_AF / (1 + 0.014))),
//					4)+comm.roundDown(
//							(_fundValueAtEnd_AF - (_fundValueAtEnd_AF / (1 + 0.0005))),
//							4)+comm.roundDown(
//									(_fundValueAtEnd_AF - (_fundValueAtEnd_AF / (1 + 0.0005))),
//									4);
//		/****** Added by - Priyanka Warekar 25-08-2015 - end *****/
////		amountAfterST = _fundValueAtEnd_AF
////				- Double.parseDouble(comm.getRoundOffLevel2(String
////						.valueOf(serviceTax)));
//
//		amountAfterST = _fundValueAtEnd_AF
//				- Double.parseDouble(comm.getRoundOffLevel2(comm.getStringWithout_E(serviceTax)));
//
//		if (amountAfterST < 150000)
//			purchasePerice = 0;
//		else if (amountAfterST >= 150000 && amountAfterST <= 299999)
//			purchasePerice = 2.5;
//		else if (amountAfterST >= 300000 && amountAfterST <= 499999)
//			purchasePerice = 3.5;
//		else if (amountAfterST >= 500000)
//			purchasePerice = 4.25;
//		else
//			purchasePerice = 0;
////		System.out.println(purchasePerice + "   " + premRateAftLoadng + "  "
////				+ freqMode + "    " + amountAfterST + "    " + serviceTax
////				+ "   " + b);
//
//		double temp=((premRateAftLoadng + purchasePerice) / freqMode)
//				/ 980 * amountAfterST;
////		System.out.println(" temp : "+temp);
//		this.annuityPay4pa = ""
//				+ comm.getStringWithout_E(Math
//						.round(((premRateAftLoadng + purchasePerice) / freqMode)
//								/ 980 * amountAfterST));

/**************************Modified by SAURABH JAIN on 10/01/2019*******Start*************************/

		/*int val1 = retiresmartbean.getAge() + retiresmartbean.getPolicyTerm();
//		double val2 = TotalVestingbenifit4/(1000*(1-0.02)) * 1.035 ;
        double val2 = Math.round(TotalVestingbenifit4 / (1 + 0.018));
//		System.out.println(" val2: " +val2);
        double AnnuityRate = 0;
        double [] PWBarr=retiresmartdb.getAnnuityPlusRate();

        *//*********Modified by SAURABH JAIN on 15/01/2019****Start*************************//*
        double a =0;
        if(val2 <150000){
        	 a=0;
        }
        else if(val2 >=150000 && val2 <=299999){
        	 a=2.5;
        }
        else if(val2 >=300000 && val2<=499999){
        	a=3.5;
        }
        else if(val2 >=500000){
        	a=4.25;
        }

        *//*********Modified by SAURABH JAIN on 15/01/2019****end*************************//*

//        AnnuityRate = Math.round(PWBarr[val1-40] * val2 );
        double val3 = (((PWBarr[val1-40] * 1.035)+a)/(1-0.02));
//        System.out.println(" val3: " +val3);
//        System.out.println("AnnuityRate : " +AnnuityRate);

        AnnuityRate = Math.round((val2 * val3) / 1000);
//        System.out.println("AnnuityRate : " +AnnuityRate);

        this.annuityPay4pa = "" + AnnuityRate;
		*//**************************Modified by SAURABH JAIN on 10/01/2019*******End*************************/
		int val1 = retiresmartbean.getAge() + retiresmartbean.getPolicyTerm();
		double aa =0;
		if(retiresmartbean.isKerlaDisc()){

			aa= 0.019;
		}else{

			aa =0.018;
		}
		double val2 = Math.round(maxVestingBenefit/(1+aa)) ;
//		System.out.println(" val2: " +val2);
//        double [] PWBarr=objDB.getAnnuityRatesForLifetimeIncome();

		double pr =getpremiumRate_Monthly();

		double a =0;
		if(val2 <1000000){
			a=0;
		}
		else if(val2 >=1000000 && val2 <=1499999){
			a=0.5;
		}
		else if(val2 >=1500000){
			a=1;
		}

		double val3 = (((pr * 1.035)+a)/(1-0.02));
//        System.out.println(" val3: " +val3);
		this.annuityPay4pa = "" + Math.round((val2*val3)/1000);

	}

	public String getAnnuityPay4pa() {
		return annuityPay4pa;
	}

	public void setAnnuityPay8pa(double maxVestingBenefit) {

//		double a = 0, b = 0, premRateAftLoadng = 0, freqMode, serviceTax = 0, amountAfterST = 0, purchasePerice = 0;
//		// double[] lifetimeIncome_Arr=objDB.getLifeTimeIncome_Arr();
//		// System.out.println(saralPensionBean.getPolicyTerm()+saralPensionBean.getAge()-1);
//		b = setABC(policyTerm, ageAtEntry);
//
//		if (premFreq.equalsIgnoreCase("Yearly")) {
//			premRateAftLoadng = b * 1.035;
//			freqMode = 1;
//		} else {
//			premRateAftLoadng = b * 1.035;
//			freqMode = 1;
//		}
//		/****** Added by - Priyanka Warekar 25-08-2015 - Start *****/
//		if (false)
//			serviceTax = comm.roundDown(
//					(_fundValueAtEnd_AX - (_fundValueAtEnd_AX / (1 + 0))), 4);
//		else
//			serviceTax = comm.roundDown(
//					(_fundValueAtEnd_AX - (_fundValueAtEnd_AX / (1 + 0.014))),
//					4)+comm.roundDown(
//							(_fundValueAtEnd_AX - (_fundValueAtEnd_AX / (1 + 0.0005))),
//							4)+comm.roundDown(
//									(_fundValueAtEnd_AX - (_fundValueAtEnd_AX / (1 + 0.0005))),
//									4);
//		/****** Added by - Priyanka Warekar 25-08-2015 - end *****/
////		amountAfterST = _fundValueAtEnd_AX
////				- Double.parseDouble(comm.getRoundOffLevel2(String
////						.valueOf(serviceTax)));
//
//
//		amountAfterST = _fundValueAtEnd_AX
//					- Double.parseDouble(comm.getRoundOffLevel2(comm.getStringWithout_E(serviceTax)));
//
//		if (amountAfterST < 150000)
//			purchasePerice = 0;
//		else if (amountAfterST >= 150000 && amountAfterST <= 299999)
//			purchasePerice = 2.5;
//		else if (amountAfterST >= 300000 && amountAfterST <= 499999)
//			purchasePerice = 3.5;
//		else if (amountAfterST >= 500000)
//			purchasePerice = 4.25;
//		else
//			purchasePerice = 0;
////		System.out.println(purchasePerice + "   " + premRateAftLoadng + "  "
////				+ freqMode + "    " + amountAfterST + "    " + serviceTax
////				+ "   " + b);
//		double temp=((premRateAftLoadng + purchasePerice) / freqMode)
//				/ 980 * amountAfterST;
////		System.out.println(" temp : "+temp);
//		this.annuityPay8pa = ""
//				+ comm.getStringWithout_E(Math
//						.round(((premRateAftLoadng + purchasePerice) / freqMode)
//								/ 980 * amountAfterST));
//
//		// System.out.println(" AX : "+_fundValueAtEnd_AX);
//		//
//		// System.out.println("8 % setABC(policyTerm, ageAtEntry) "+setABC(policyTerm,
//		// ageAtEntry));
//		// this.annuityPay8pa=""+(setABC(policyTerm,
//		// ageAtEntry)*_fundValueAtEnd_AX/(1000*(1-0.02))*1.035);


		/**************************Modified by SAURABH JAIN on 10/01/2019*******Start*************************/

	/*	int val1 = retiresmartbean.getAge() + retiresmartbean.getPolicyTerm();
//		double val2 = TotalVestingbenifit8/(1000*(1-0.02)) * 1.035 ;
        double val2 = Math.round(TotalVestingbenifit8 / (1 + 0.018));
//		System.out.println(" val2: " +val2);
        double AnnuityRate8 = 0;
        double [] PWBarr=retiresmartdb.getAnnuityPlusRate();

        *//*********Modified by SAURABH JAIN on 15/01/2019****Start*************************//*
        double a =0;
        if(val2 <150000){
        	 a=0;
        }
        else if(val2 >=150000 && val2 <=299999){
        	 a=2.5;
        }
        else if(val2 >=300000 && val2<=499999){
        	a=3.5;
        }
        else if(val2 >=500000){
        	a=4.25;
        }

        *//*********Modified by SAURABH JAIN on 15/01/2019****end*************************//*



//        AnnuityRate8 = Math.round(PWBarr[val1-40] * val2 );
        double val3 = (((PWBarr[val1-40] * 1.035)+a)/(1-0.02));
//        System.out.println(" val3: " +val3);
//        System.out.println("AnnuityRate : " +AnnuityRate);

        AnnuityRate8 = Math.round((val2 * val3) / 1000);
//        System.out.println("AnnuityRate : " +AnnuityRate8);

        this.annuityPay8pa = "" + AnnuityRate8;

		*//**************************Modified by SAURABH JAIN on 10/01/2019*******Start*************************//*	*/
		int val1 = retiresmartbean.getAge() + retiresmartbean.getPolicyTerm();
		double aa =0;
		if(retiresmartbean.isKerlaDisc()){

			aa= 0.019;
		}else{

			aa =0.018;
		}
		double val2 = Math.round(maxVestingBenefit/(1+aa)) ;
//		System.out.println(" val2: " +val2);
//        double [] PWBarr=objDB.getAnnuityRatesForLifetimeIncome();

		double pr =getpremiumRate_Monthly();

		double a =0;
		if(val2 <1000000){
			a=0;
		}
		else if(val2 >=1000000 && val2 <=1499999){
			a=0.5;
		}
		else if(val2 >=1500000){
			a=1;
		}

		double val3 = (((pr * 1.035)+a)/(1-0.02));
//        System.out.println(" val3: " +val3);
		this.annuityPay8pa = "" + Math.round((val2*val3)/1000);

	}



	public String getAnnuityPay8pa() {
		return annuityPay8pa;
	}

	public void setAnnuityRates(int policyTerm, int ageAtEntry) {
		// System.out.println(" ABC "+setABC(policyTerm, ageAtEntry));


		int val1 = retiresmartbean.getAge() + retiresmartbean.getPolicyTerm();
		double AnnuityRate = 0;
		double[] PWBarr = retireSmartDB.getAnnuityPlusRate();


		AnnuityRate = PWBarr[val1 - 40];


		this.annuityRates = comm.getRoundOffLevel2New(comm.getStringWithout_E(AnnuityRate / (1 * (1 - 0.02)) * 1.035));
	}

	public String getAnnuityRates() {
		return annuityRates;
	}

	public double getpremiumRate_Monthly()
	{
		int sheetGroup=getSheetGroup();

//		System.out.println("Sheet Group : "+sheetGroup);
		if(sheetGroup==1)
		{
			double prDouble=0;

			String[] prStrArr=comm.split(retireSmartDB.getPRArr_SingleLifeAnnuity(),",");
			String annuityOption=null;
			int optionRank=0;
			String pr=null;
			int arrCount=0;
			annuityOption=retiresmartbean.getAnnuityOption();
			//double prDouble=0;

			if (annuityOption.contains("Option 1.10")) {
				optionRank = 10;
			} else if (annuityOption.contains("Option 1.1")) {
				optionRank = 1;
			} else if (annuityOption.contains("Option 1.2")) {
				optionRank = 2;
			} else if (annuityOption.contains("Option 1.3")) {
				optionRank = 3;
			} else if (annuityOption.contains("Option 1.4")) {
				optionRank = 4;
			} else if (annuityOption.contains("Option 1.5")) {
				optionRank = 5;
			} else if (annuityOption.contains("Option 1.6")) {
				optionRank = 6;
			} else if (annuityOption.contains("Option 1.7")) {
				optionRank = 7;
			} else if (annuityOption.contains("Option 1.8")) {
				optionRank = 8;
			} else if (annuityOption.contains("Option 1.9")) {
				optionRank = 9;
			}

				/*if(annuityOption.equals("Lifetime Income"))
				{optionRank=1;}
				else if(annuityOption.equals("Lifetime Income with Capital Refund"))
				{optionRank=2;}
				else if(annuityOption.equals("Lifetime Income with Capital Refund in Parts"))
				{optionRank=3;}
				else if(annuityOption.equals("Lifetime Income with Balance Capital Refund"))
				{optionRank=4;}
				else if(annuityOption.equals("Lifetime Income with Annual Increase of 3%"))
				{optionRank=5;}
				else if(annuityOption.equals("Lifetime Income with Annual Increase of 5%"))
				{optionRank=6;}
				else if(annuityOption.equals("Lifetime Income with Certain Period of 5 Years"))
				{optionRank=7;}
				else if(annuityOption.equals("Lifetime Income with Certain Period of 10 Years"))
				{optionRank=8;}
				else if(annuityOption.equals("Lifetime Income with Certain Period of 15 Years"))
				{optionRank=9;}
				else if(annuityOption.equals("Lifetime Income with Certain Period of 20 Years"))
				{optionRank=10;}*/

			//for (int i=40; i<=80;i++)
			for (int i=0; i<=80;i++)
			{
				for (int j = 1; j <=10; j++)
				{
					if(i==retiresmartbean.getVestingAge() && j ==optionRank)
					{
						pr=prStrArr[arrCount];
						prDouble=Double.parseDouble(pr);
						break;
					}
					arrCount++;
				}
			}
//				                System.out.println("*****************Premium Rate:=> "+prDouble);
			//}
			double a = prDouble ;
			return prDouble;
		}

		else if(sheetGroup==2)
		{
			//VLOOKUP(Input!B26,'Joint Life 50%'!B13:AQ54,MATCH(Input!B22,'Joint Life 	50%'!B13:AQ13,0),FALSE)
			String[] prStrArr=comm.split(retireSmartDB.getPRArr_JointLife50Percent(),",");
			String pr=null;
			int arrCount=0;
			double prDouble=0;

			int ageOfSecondAnnuitant=retiresmartbean.getVestingAge();
//			if(ageOfSecondAnnuitant==0)
//			{ageOfSecondAnnuitant=40;}

			//for (int i=40; i<=80;i++)
			for (int i=40; i<=80;i++)
			{
				//for (int j = 40; j <=80; j++)
				for (int j =40; j <=80; j++)
				{
					if(i==ageOfSecondAnnuitant && j ==retiresmartbean.getVestingAge())
					{
						pr=prStrArr[arrCount];
						prDouble=Double.parseDouble(pr);
						break;
					}
					arrCount++;
				}
			}
//			   System.out.println("Premium Rate:=> "+prDouble);
			return prDouble;
		}
		else if(sheetGroup==3)
		{
			//VLOOKUP(Input!B26,'Joint Life 50%'!B13:AQ54,MATCH(Input!B22,'Joint Life 	50%'!B13:AQ13,0),FALSE)
			String[] prStrArr=comm.split(retireSmartDB.getPRArr_JointLife100Percent(),",");
			String pr=null;
			int arrCount=0;
			double prDouble=0;
			int ageOfSecondAnnuitant=retiresmartbean.getVestingAge();
//			if(ageOfSecondAnnuitant==0)
//			{ageOfSecondAnnuitant=40;}

			//for (int i=40; i<=80;i++)
			for (int i=40; i<=80;i++)
			{
				//for (int j = 40; j <=80; j++)
				for (int j =40; j <=80; j++)
				{
					if(i==ageOfSecondAnnuitant && j ==retiresmartbean.getVestingAge())
					{
						pr=prStrArr[arrCount];
						prDouble=Double.parseDouble(pr);
						break;
					}
					arrCount++;
				}
			}
//			   System.out.println("Premium Rate:=> "+prDouble);
			return prDouble;
		}
		else if(sheetGroup==4)
		{
			//VLOOKUP(Input!B26,'Joint Life 50%_ROC'!B13:AQ54,MATCH(Input!B22,'Joint Life 	50%_ROC'!B13:AQ13,0),FALSE)
			String[] prStrArr=comm.split(retireSmartDB.getPRArr_JointLife50Percent_ROC(),",");
			String pr=null;
			int arrCount=0;
			double prDouble=0;

			int ageOfSecondAnnuitant=retiresmartbean.getVestingAge();
//			if(ageOfSecondAnnuitant==0)
//			{ageOfSecondAnnuitant=40;}

			//for (int i=40; i<=80;i++)
			for (int i=40; i<=80;i++)
			{
				//for (int j = 40; j <=80; j++)
				for (int j =40; j <=80; j++)
				{
					//System.out.println("Age of First Annuitant(i) => "+i+"   Age of Second Annuitant(j) => "+j+"   PR=> "+prStrArr[arrCount]);
					if(i==ageOfSecondAnnuitant && j ==retiresmartbean.getVestingAge())
					{
						pr=prStrArr[arrCount];
						prDouble=Double.parseDouble(pr);
						//System.out.println("PR Returned=> "+prDouble);
						break;
					}
					arrCount++;
				}
			}
//			   System.out.println("Premium Rate:=> "+prDouble);
			return prDouble;
		}
		else if(sheetGroup==5)
		{
			//VLOOKUP(Input!B26,'Joint_Life_100%_ROC'!B13:AQ54,MATCH(Input!B22,'Joint_Life_100%_RO	C'!B13:AQ13,0),FALSE)
			String[] prStrArr=comm.split(retireSmartDB.getPRArr_JointLife100Percent_ROC(),",");
			String pr=null;
			int arrCount=0;
			double prDouble=0;
			int ageOfSecondAnnuitant=retiresmartbean.getVestingAge();
//			if(ageOfSecondAnnuitant==0)
//			{ageOfSecondAnnuitant=40;}

			//for (int i=40; i<=80;i++)
			for (int i=40; i<=80;i++)
			{
				//for (int j = 40; j <=80; j++)
				for (int j =40; j <=80; j++)
				{
					if(i==ageOfSecondAnnuitant && j ==retiresmartbean.getVestingAge())
					{
						pr=prStrArr[arrCount];
						prDouble=Double.parseDouble(pr);
						break;
					}
					arrCount++;
				}
			}
//			   System.out.println("Premium Rate:=> "+prDouble);
			return prDouble;
		}
		else if(sheetGroup==6)
		{
			//VLOOKUP(Input!B22,Temporary_Annuity!B13:D77,MATCH(F8,Temporary_Annuity!B14:D14,0),FALSE)
			//VLOOKUP( Age ,Temporary_Annuity!B13:D77,MATCH(F8,Temporary_Annuity!B14:D14,0),FALSE)
			//NOT COMPLETE
			return 0;
		}
		else
		{return 0;}
	}

	public int getSheetGroup()
	{
		String annuityOption = retiresmartbean.getAnnuityOption();
		if (annuityOption.contains("Option 1.1") ||
				annuityOption.contains("Option 1.2") ||
				annuityOption.contains("Option 1.3") ||
				annuityOption.contains("Option 1.4") ||
				annuityOption.contains("Option 1.5") ||
				annuityOption.contains("Option 1.6") ||
				annuityOption.contains("Option 1.7") ||
				annuityOption.contains("Option 1.8") ||
				annuityOption.contains("Option 1.9") ||
				annuityOption.contains("Option 1.10"))


		{
			return 1;
		} else if (annuityOption.contains("Option 2.1")) {
			return 2;
		} else if (annuityOption.contains("Option 2.2")) {
			return 3;
		} else if (annuityOption.contains("Option 2.3")) {
			return 4;
		} else if (annuityOption.contains("Option 2.4")) {
			return 5;
		}
		/*else if(annuityOption.contains("Income for a period with Balance Capital Refund for Period of 15 years"))
		{return 6;}*/

		else {
			return 0;
		}
	}
	/*{
		String annuityOption=retiresmartbean.getAnnuityOption();
		if(annuityOption.equals("Lifetime Income")|| annuityOption.equals("Lifetime Income with Capital Refund")|| annuityOption.equals("Lifetime Income with Capital Refund in Parts")|| annuityOption.equals("Lifetime Income with Balance Capital Refund")|| annuityOption.equals("Lifetime Income with Annual Increase of 3%")||annuityOption.equals("Lifetime Income with Annual Increase of 5%")||annuityOption.equals("Lifetime Income with Certain Period of 5 Years")||annuityOption.equals("Lifetime Income with Certain Period of 10 Years")||annuityOption.equals("Lifetime Income with Certain Period of 15 Years")||annuityOption.equals("Lifetime Income with Certain Period of 20 Years") )
		{return 1;}
		else if(annuityOption.equals("Life and Last Survivor - 50% Income"))
		{return 2;}
		else if(annuityOption.equals("Life and Last Survivor - 100% Income"))
		{return 3;}
		else if(annuityOption.equals("Life and Last Survivor with Capital Refund - 50% Income"))
		{return 4;}
		else if(annuityOption.equals("Life and Last Survivor with Capital Refund - 100% Income"))
		{return 5;}
		else if(annuityOption.equals("Income for a period with Balance Capital Refund for Period of 15 years"))
		{return 6;}
		else
		{return 0;}
	}*/

	public double getAnnuityAmount(double maxVestingBenefit) {

		int val1 = retiresmartbean.getAge() + retiresmartbean.getPolicyTerm();
		double aa =0;
		if(retiresmartbean.isKerlaDisc()){

			aa= 0.019;
		}else{

			aa =0.018;
		}
		double val2 = Math.round(maxVestingBenefit/(1+aa)) ;
//		System.out.println(" val2: " +val2);
//        double [] PWBarr=objDB.getAnnuityRatesForLifetimeIncome();

		double pr =getpremiumRate_Monthly();

		double a =0;
		if(val2 <1000000){
			a=0;
		}
		else if(val2 >=1000000 && val2 <=1499999){
			a=0.5;
		}
		else if(val2 >=1500000){
			a=1;
		}

		double val3 = (((pr * 1.035)+a)/(1-0.02));
//        System.out.println(" val3: " +val3);
		return Math.round((val2*val3)/1000);

		/**************************Modified by SAURABH JAIN on 07/03/2019*******End*************************/
	}
}
