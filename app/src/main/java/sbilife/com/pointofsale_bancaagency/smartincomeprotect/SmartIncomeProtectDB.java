package sbilife.com.pointofsale_bancaagency.smartincomeprotect;

class SmartIncomeProtectDB {

    public String getBasic_PR_Arr()
    // {return "0,96.52,76.23,0,96.54,76.26,0,96.57,76.29,168.69,96.6,76.33,168.72,96.64,76.36,168.76,96.68,76.4,168.79,96.71,76.43,168.83,96.74,76.46,168.85,96.77,76.49,168.88,96.79,76.51,168.9,96.81,76.53,168.91,96.82,76.54,168.92,96.83,76.56,168.93,96.85,76.57,168.94,96.86,76.59,168.95,96.87,76.6,168.95,96.88,76.62,168.96,96.9,76.65,168.97,96.92,76.68,168.99,96.95,76.71,169.01,96.98,76.76,169.03,97.02,76.81,169.06,97.07,76.87,169.09,97.12,76.94,169.13,97.18,77.03,169.18,97.26,77.12,169.23,97.35,77.24,169.3,97.45,77.37,169.37,97.56,77.51,169.46,97.69,77.68,169.56,97.85,77.88,169.67,98.02,78.09,169.8,98.22,78.34,169.95,98.45,78.61,170.13,98.7,78.91,170.33,98.98,79.24,170.56,99.29,79.6,170.81,99.63,79.99,171.09,100,80.41,171.39,100.38,80.85,171.72,100.8,81.32,172.07,101.23,81.82,172.43,101.69,82.35,172.81,102.17,0,173.2,102.68,0,173.61,103.22,0,174.05,0,0,174.51,0,0,175,0,0,175.53,0,0,176.11,0,0";}
    {
        return "0,96.52,76.23,0,96.54,76.26,0,96.57,76.29,169.27,96.6,76.33,169.27,96.64,76.36,169.27,96.68,76.4,169.48,96.71,76.43,169.48,96.74,76.46,169.48,96.77,76.49,169.64,96.79,76.51,169.64,96.81,76.53,169.64,96.82,76.54,169.64,96.83,76.56,169.64,96.85,76.57,169.64,96.86,76.59,169.64,96.87,76.6,169.64,96.88,76.62,169.82,96.9,76.65,169.82,96.92,76.68,169.82,96.95,76.71,169.82,96.98,76.76,169.98,97.02,76.81,169.98,97.07,76.87,169.98,97.12,76.94,170.23,97.18,77.03,170.23,97.26,77.12,170.47,97.35,77.24,170.47,97.45,77.37,170.79,97.56,77.51,170.99,97.69,77.68,171.23,97.85,77.88,171.5,98.02,78.09,171.82,98.22,78.34,172.18,98.45,78.61,172.6,98.7,78.91,173.08,98.98,79.24,173.62,99.29,79.6,174.22,99.63,79.99,174.88,100,80.41,175.61,100.38,80.85,176.38,100.8,81.32,177.21,101.23,81.82,178.09,101.69,82.35,179.02,102.17,0,180.01,102.68,0,181.05,103.22,0,182.18,0,0,183.41,0,0,184.75,0,0,186.24,0,0,188.1,0,0,";
    }
//     public String getPTA_PremiumRate_Arr()
//     {return "1.44,1.44,1.44,1.44,1.44,1.44,1.44,1.44,1.44,1.44,1.44,1.44,1.44,1.44,1.44,1.44,1.44,1.44,1.46,1.48,1.51,1.53,1.56,1.59,1.63,1.66,1.47,1.47,1.47,1.47,1.47,1.47,1.47,1.47,1.47,1.47,1.47,1.47,1.47,1.47,1.47,1.47,1.47,1.49,1.51,1.54,1.57,1.60,1.63,1.66,1.70,1.74,1.51,1.51,1.51,1.51,1.51,1.51,1.51,1.51,1.51,1.51,1.51,1.51,1.51,1.51,1.51,1.51,1.52,1.54,1.57,1.60,1.63,1.66,1.70,1.74,1.79,1.83,1.54,1.54,1.54,1.54,1.54,1.54,1.54,1.54,1.54,1.54,1.54,1.54,1.54,1.54,1.54,1.54,1.57,1.60,1.63,1.66,1.70,1.74,1.78,1.83,1.88,1.93,1.56,1.56,1.56,1.56,1.56,1.56,1.56,1.56,1.56,1.56,1.56,1.56,1.56,1.56,1.57,1.60,1.63,1.66,1.70,1.73,1.78,1.82,1.87,1.92,1.98,2.04,1.58,1.58,1.58,1.58,1.58,1.58,1.58,1.58,1.58,1.58,1.58,1.58,1.58,1.59,1.62,1.66,1.69,1.73,1.77,1.82,1.86,1.92,1.97,2.03,2.10,2.17,1.60,1.60,1.60,1.60,1.60,1.60,1.60,1.60,1.60,1.60,1.60,1.60,1.62,1.65,1.69,1.72,1.76,1.81,1.86,1.91,1.96,2.02,2.09,2.16,2.23,2.30,1.61,1.61,1.61,1.61,1.61,1.61,1.61,1.61,1.61,1.61,1.62,1.65,1.68,1.72,1.76,1.80,1.85,1.90,1.96,2.02,2.08,2.15,2.22,2.30,2.38,2.46,1.62,1.62,1.62,1.62,1.62,1.62,1.62,1.62,1.62,1.63,1.68,1.71,1.75,1.80,1.84,1.89,1.95,2.01,2.07,2.14,2.21,2.29,2.37,2.45,2.54,2.63,1.63,1.63,1.63,1.63,1.63,1.63,1.63,1.63,1.66,1.70,1.75,1.79,1.84,1.89,1.94,2.00,2.06,2.13,2.20,2.28,2.36,2.45,2.54,2.63,2.73,2.83,1.64,1.64,1.64,1.64,1.64,1.64,1.67,1.70,1.74,1.78,1.83,1.88,1.93,1.99,2.05,2.12,2.19,2.27,2.35,2.44,2.53,2.63,2.73,2.83,2.93,3.04,1.66,1.66,1.66,1.66,1.67,1.70,1.74,1.78,1.82,1.87,1.93,1.99,2.05,2.11,2.19,2.26,2.35,2.43,2.53,2.62,2.72,2.83,2.94,3.05,3.16,3.28,1.70,1.70,1.70,1.71,1.74,1.79,1.83,1.88,1.93,1.98,2.05,2.11,2.18,2.26,2.34,2.43,2.52,2.62,2.72,2.83,2.95,3.06,3.18,3.30,3.42,3.54,1.76,1.76,1.77,1.80,1.83,1.89,1.94,1.99,2.05,2.11,2.19,2.26,2.34,2.43,2.52,2.62,2.73,2.84,2.95,3.07,3.19,3.32,3.45,3.58,3.71,3.84,1.84,1.85,1.87,1.90,1.95,2.01,2.07,2.13,2.19,2.26,2.35,2.44,2.53,2.63,2.73,2.84,2.96,3.08,3.21,3.34,3.47,3.61,3.74,3.88,4.02,4.17,1.94,1.95,1.98,2.03,2.08,2.15,2.21,2.28,2.36,2.44,2.54,2.64,2.74,2.85,2.97,3.10,3.23,3.36,3.50,3.64,3.78,3.93,4.07,4.22,4.38,4.54,2.06,2.08,2.12,2.17,2.23,2.31,2.38,2.46,2.54,2.64,2.76,2.87,2.99,3.11,3.24,3.38,3.52,3.67,3.82,3.97,4.12,4.28,4.43,4.60,4.77,4.95,2.20,2.24,2.28,2.34,2.40,2.49,2.57,2.66,2.76,2.87,3.00,3.13,3.26,3.40,3.54,3.69,3.85,4.01,4.17,4.33,4.49,4.66,4.84,5.02,5.20,5.40,2.37,2.41,2.46,2.52,2.59,2.69,2.79,2.89,3.00,3.13,3.28,3.42,3.56,3.72,3.88,4.04,4.21,4.38,4.55,4.72,4.90,5.09,5.28,5.48,5.68,0,2.55,2.59,2.65,2.72,2.80,2.92,3.03,3.15,3.28,3.42,3.59,3.74,3.90,4.07,4.25,4.42,4.60,4.78,4.97,5.16,5.35,5.56,5.77,5.99,0,0,2.75,2.80,2.86,2.95,3.05,3.18,3.31,3.44,3.59,3.75,3.93,4.10,4.28,4.47,4.65,4.84,5.03,5.23,5.43,5.64,5.85,6.07,6.31,0,0,0,2.97,3.02,3.10,3.20,3.32,3.48,3.62,3.77,3.94,4.11,4.32,4.50,4.70,4.90,5.10,5.30,5.50,5.72,5.93,6.16,6.40,6.65,0,0,0,0,3.21,3.28,3.37,3.49,3.63,3.80,3.97,4.14,4.32,4.51,4.74,4.94,5.15,5.37,5.58,5.80,6.02,6.25,6.49,6.74,7.00,0,0,0,0,0,3.47,3.56,3.67,3.81,3.97,4.17,4.35,4.55,4.75,4.96,5.20,5.43,5.65,5.87,6.10,6.34,6.58,6.84,7.10,7.38,0,0,0,0,0,0,3.77,3.88,4.02,4.18,4.36,4.58,4.79,5.00,5.22,5.45,5.71,5.95,6.19,6.43,6.68,6.94,7.21,7.49,7.78,0,0,0,0,0,0,0,4.12,4.25,4.41,4.60,4.80,5.05,5.27,5.50,5.75,5.99,6.28,6.52,6.78,7.04,7.31,7.60,7.90,8.21,0,0,0,0,0,0,0,0,4.53,4.68,4.87,5.07,5.30,5.57,5.81,6.07,6.33,6.59,6.88,7.15,7.43,7.72,8.02,8.33,8.66,0,0,0,0,0,0,0,0,0,4.99,5.17,5.38,5.61,5.85,6.15,6.41,6.68,6.96,7.23,7.55,7.84,8.15,8.46,8.80,9.15,0,0,0,0,0,0,0,0,0,0,5.52,5.72,5.95,6.20,6.46,6.78,7.07,7.35,7.64,7.93,8.28,8.60,8.94,9.29,9.66,0,0,0,0,0,0,0,0,0,0,0,6.10,6.32,6.57,6.84,7.13,7.48,7.77,8.07,8.37,8.69,9.08,9.43,9.80,10.19,0,0,0,0,0,0,0,0,0,0,0,0,6.74,6.98,7.25,7.54,7.85,8.22,8.52,8.84,9.17,9.53,9.95,10.35,10.76,0,0,0,0,0,0,0,0,0,0,0,0,0,7.44,7.70,7.99,8.30,8.62,9.00,9.33,9.68,10.05,10.44,10.91,11.35,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8.19,8.48,8.79,9.11,9.43,9.85,10.21,10.59,11.01,11.44,11.97,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,9.01,9.31,9.63,9.96,10.31,10.76,11.16,11.60,12.05,12.54,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,9.88,10.18,10.51,10.86,11.25,11.76,12.21,12.69,13.21,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,10.79,11.10,11.45,11.84,12.28,12.85,13.36,13.90,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,11.73,12.07,12.46,12.92,13.41,14.05,14.62,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,12.74,13.12,13.58,14.10,14.66,15.38,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,13.83,14.29,14.82,15.41,16.05,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,15.05,15.59,16.20,16.88,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,16.45,17.08,17.78,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,18.09,18.80,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,19.95,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0";}

    public String getPTA_PremiumRate_Arr() {
        return "144,144,144,144,144,144,144,144,144,144,144,144,144,144,144,144,144,144,146,148,151,153,156,159,163,166,147,147,147,147,147,147,147,147,147,147,147,147,147,147,147,147,147,149,151,154,157,160,163,166,170,174,151,151,151,151,151,151,151,151,151,151,151,151,151,151,151,151,152,154,157,160,163,166,170,174,179,183,154,154,154,154,154,154,154,154,154,154,154,154,154,154,154,154,157,160,163,166,170,174,178,183,188,193,156,156,156,156,156,156,156,156,156,156,156,156,156,156,157,160,163,166,170,173,178,182,187,192,198,204,158,158,158,158,158,158,158,158,158,158,158,158,158,159,162,166,169,173,177,182,186,192,197,203,210,217,160,160,160,160,160,160,160,160,160,160,160,160,162,165,169,172,176,181,186,191,196,202,209,216,223,230,161,161,161,161,161,161,161,161,161,161,162,165,168,172,176,180,185,190,196,202,208,215,222,230,238,246,162,162,162,162,162,162,162,162,162,163,168,171,175,180,184,189,195,201,207,214,221,229,237,245,254,263,163,163,163,163,163,163,163,163,166,170,175,179,184,189,194,200,206,213,220,228,236,245,254,263,273,283,164,164,164,164,164,164,167,170,174,178,183,188,193,199,205,212,219,227,235,244,253,263,273,283,293,304,166,166,166,166,167,170,174,178,182,187,193,199,205,211,219,226,235,243,253,262,272,283,294,305,316,328,170,170,170,171,174,179,183,188,193,198,205,211,218,226,234,243,252,262,272,283,295,306,318,330,342,354,176,176,177,180,183,189,194,199,205,211,219,226,234,243,252,262,273,284,295,307,319,332,345,358,371,384,184,185,187,190,195,201,207,213,219,226,235,244,253,263,273,284,296,308,321,334,347,361,374,388,402,417,194,195,198,203,208,215,221,228,236,244,254,264,274,285,297,310,323,336,350,364,378,393,407,422,438,454,206,208,212,217,223,231,238,246,254,264,276,287,299,311,324,338,352,367,382,397,412,428,443,460,477,495,220,224,228,234,240,249,257,266,276,287,300,313,326,340,354,369,385,401,417,433,449,466,484,502,520,540,237,241,246,252,259,269,279,289,300,313,328,342,356,372,388,404,421,438,455,472,490,509,528,548,568,0,255,259,265,272,280,292,303,315,328,342,359,374,390,407,425,442,460,478,497,516,535,556,577,599,0,0,275,280,286,295,305,318,331,344,359,375,393,410,428,447,465,484,503,523,543,564,585,607,631,0,0,0,297,302,310,320,332,348,362,377,394,411,432,450,470,490,510,530,550,572,593,616,640,665,0,0,0,0,321,328,337,349,363,380,397,414,432,451,474,494,515,537,558,580,602,625,649,674,700,0,0,0,0,0,347,356,367,381,397,417,435,455,475,496,520,543,565,587,610,634,658,684,710,738,0,0,0,0,0,0,377,388,402,418,436,458,479,500,522,545,571,595,619,643,668,694,721,749,778,0,0,0,0,0,0,0,412,425,441,460,480,505,527,550,575,599,628,652,678,704,731,760,790,821,0,0,0,0,0,0,0,0,453,468,487,507,530,557,581,607,633,659,688,715,743,772,802,833,866,0,0,0,0,0,0,0,0,0,499,517,538,561,585,615,641,668,696,723,755,784,815,846,880,915,0,0,0,0,0,0,0,0,0,0,552,572,595,620,646,678,707,735,764,793,828,860,894,929,966,0,0,0,0,0,0,0,0,0,0,0,610,632,657,684,713,748,777,807,837,869,908,943,980,1019,0,0,0,0,0,0,0,0,0,0,0,0,674,698,725,754,785,822,852,884,917,953,995,1035,1076,0,0,0,0,0,0,0,0,0,0,0,0,0,744,770,799,830,862,900,933,968,1005,1044,1091,1135,0,0,0,0,0,0,0,0,0,0,0,0,0,0,819,848,879,911,943,985,1021,1059,1101,1144,1197,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,901,931,963,996,1031,1076,1116,1160,1205,1254,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,988,1018,1051,1086,1125,1176,1221,1269,1321,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1079,1110,1145,1184,1228,1285,1336,1390,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1173,1207,1246,1292,1341,1405,1462,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1274,1312,1358,1410,1466,1538,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1383,1429,1482,1541,1605,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1505,1559,1620,1688,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1645,1708,1778,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1809,1880,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1995,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0";
    }


    public String getADB_PremiumRate_Arr() {
        return "201,234,266,295,323,349,374,397,419,439,458,476,493,509,524,538,551,563,574,585,595,604,612,620,628,635";
    }

    public String getATPDB_PremiumRate_Arr() {
        return "161,187,213,236,258,279,299,318,335,351,367,381,395,407,419,430,441,450,459,468,476,483,490,496,502,508";
    }

    public String getCC13NonLinked_PremiumRate_Arr() {
        return "0.48,0.5,0.53,0.57,0.62,0.67,0.72,0.77,0.83,0.9,0.99,1.09,1.21,1.35,1.53,1.74,1.98,2.26,2.58,2.94,3.33,3.76,4.22,4.74,5.31,5.95,6.67,7.49,8.4,9.41,10.52,11.74,13.06,14.49,16.02,17.65,19.38,21.21,23.13,25.15,27.25,29.43,31.08,32.42,33.77,35.19,35.19";
    }

    public double[] getGSV_Factor_Arr() {
        //return new double[]{0.0,	0.0,	0.0,0.30,	0.0,	0.0,0.30,	0.30	,0.30,0.50,	0.50,	0.50,0.50	,0.50,	0.50,0.50,	0.50	,0.50,0.50,	0.50,	0.50,0.0	,0.55,	0.55,0.0	,0.55,	0.55,0.0,	0.55,	0.55,0.0,	0.60,	0.60,0.0	,0.60,	0.60,0.0,	0.0	,0.65,0.0,	0.0	,0.65,0.0,	0.0,	0.65};
        return new double[]{0, 0, 0, 0.3, 0.3, 0.3, 0.35, 0.35, 0.35, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.9, 0.5, 0.5, 0.9, 0.5, 0.5, 0, 0.6, 0.56, 0, 0.7, 0.62, 0, 0.8, 0.68, 0, 0.9, 0.74, 0, 0.9, 0.79, 0, 0, 0.85, 0, 0, 0.9, 0, 0, 0.9};
    }

    public double[] getSSV_RP_Arr() {//	return new double[]{0.0,0.0,0.0,0.6317,	0.0,	0.0,0.6886,	0.5679,	0.5081,0.7409,	0.6048,	0.5461,0.7928,	0.6397	,0.5772,0.8461,	0.6745,	0.6074,0.9014,	0.7099,	0.6376,0	,	0.7466,	0.6684,0	,	0.7846,	0.7002,0,	0.8245,	0.7332,0	,0.8663,	0.7677,0	,0.9104,	0.8038,0,0,		0.8418,0,0,		0.8819, 0,0,		0.9242};
        return new double[]{0, 0, 0, 0.6399, 0.5237, 0.4641, 0.6975, 0.5679, 0.5081, 0.7504, 0.6048, 0.5461, 0.803, 0.6397, 0.5772, 0.857, 0.6745, 0.6074, 0.9131, 0.7099, 0.6376, 0, 0.7466, 0.6684, 0, 0.7846, 0.7002, 0, 0.8245, 0.7332, 0, 0.8663, 0.7677, 0, 0.9104, 0.8038, 0, 0, 0.8418, 0, 0, 0.8819, 0, 0, 0.9242};
    }

}
