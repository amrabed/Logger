package org.magnum.logger;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor
{
	private static final String TAG = "ENCRYPT";
	private static final String ENCODING = "US-ASCII";
	private static final String ALGORITHM = "HmacSHA256";

	private Encryptor()
	{
	}

	public static String encryptPhoneNumber(String number, Context context)
	{
		// Keep digits only
		number = number.replaceAll("\\D+", "");
		return encrypt(number, context);
	}

	public static String encrypt(String data, Context context)
	{
		try
		{
			if (data != null)
			{
				Mac mac = Mac.getInstance(ALGORITHM);
				mac.init(new SecretKeySpec(data.getBytes(ENCODING), ALGORITHM));
				return new String(mac.doFinal(getSalt(context).toString().getBytes(ENCODING)), ENCODING);
			}
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}
		return "";

	}

	public static String hash(String data)
	{
		try
		{
			if (data != null)
			{
				Mac mac = Mac.getInstance(ALGORITHM);
				mac.init(new SecretKeySpec(data.getBytes(ENCODING), ALGORITHM));
				return new String(mac.doFinal(), ENCODING);
			}
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}
		return "";
	}

	private static Integer getSalt(Context context)
	{
		Integer salt;
		if (PreferenceManager.getDefaultSharedPreferences(context).contains(TAG))
		{
			salt = PreferenceManager.getDefaultSharedPreferences(context).getInt(TAG, 0);
		}
		else
		{
			salt = new Random(System.currentTimeMillis()).nextInt();
			PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(TAG, salt).commit();
		}
		return salt;
	}

	static class Codes
	{

		HashMap<String, Integer> map = new HashMap<String, Integer>();

		Codes()
		{
			for (int i = 0; i < ISO.length; i++)
			{
				map.put(ISO[i], CODE[i]);
			}
		}

		public Integer get(String iso)
		{
			return map.get(iso);
		}

		private static final String[] ISO = {"AF",// 93
				"AL",// 355
				"DZ",// 213
				"AD",// 376
				"AO",// 244
				"AQ",// 672
				"AR",// 54
				"AM",// 374
				"AW",// 297
				"AU",// 61
				"AT",// 43
				"AZ",// 994
				"BH",// 973
				"BD",// 880
				"BY",// 375
				"BE",// 32
				"BZ",// 501
				"BJ",// 229
				"BT",// 975
				"BO",// 591
				"BA",// 387
				"BW",// 267
				"BR",// 55
				"BN",// 673
				"BG",// 359
				"BF",// 226
				"MM",// 95
				"BI",// 257
				"KH",// 855
				"CM",// 237
				"CA",// 1
				"CV",// 238
				"CF",// 236
				"TD",// 235
				"CL",// 56
				"CN",// 86
				"CX",// 61
				"CC",// 61
				"CO",// 57
				"KM",// 269
				"CG",// 242
				"CD",// 243
				"CK",// 682
				"CR",// 506
				"HR",// 385
				"CU",// 53
				"CY",// 357
				"CZ",// 420
				"DK",// 45
				"DJ",// 253
				"TL",// 670
				"EC",// 593
				"EG",// 20
				"SV",// 503
				"GQ",// 240
				"ER",// 291
				"EE",// 372
				"ET",// 251
				"FK",// 500
				"FO",// 298
				"FJ",// 679
				"FI",// 358
				"FR",// 33
				"PF",// 689
				"GA",// 241
				"GM",// 220
				"GE",// 995
				"DE",// 49
				"GH",// 233
				"GI",// 350
				"GR",// 30
				"GL",// 299
				"GT",// 502
				"GN",// 224
				"GW",// 245
				"GY",// 592
				"HT",// 509
				"HN",// 504
				"HK",// 852
				"HU",// 36
				"IN",// 91
				"ID",// 62
				"IR",// 98
				"IQ",// 964
				"IE",// 353
				"IM",// 44
				"IL",// 972
				"IT",// 39
				"CI",// 225
				"JP",// 81
				"JO",// 962
				"KZ",// 7
				"KE",// 254
				"KI",// 686
				"KW",// 965
				"KG",// 996
				"LA",// 856
				"LV",// 371
				"LB",// 961
				"LS",// 266
				"LR",// 231
				"LY",// 218
				"LI",// 423
				"LT",// 370
				"LU",// 352
				"MO",// 853
				"MK",// 389
				"MG",// 261
				"MW",// 265
				"MY",// 60
				"MV",// 960
				"ML",// 223
				"MT",// 356
				"MH",// 692
				"MR",// 222
				"MU",// 230
				"YT",// 262
				"MX",// 52
				"FM",// 691
				"MD",// 373
				"MC",// 377
				"MN",// 976
				"ME",// 382
				"MA",// 212
				"MZ",// 258
				"NA",// 264
				"NR",// 674
				"NP",// 977
				"NL",// 31
				"AN",// 599
				"NC",// 687
				"NZ",// 64
				"NI",// 505
				"NE",// 227
				"NG",// 234
				"NU",// 683
				"KP",// 850
				"NO",// 47
				"OM",// 968
				"PK",// 92
				"PW",// 680
				"PA",// 507
				"PG",// 675
				"PY",// 595
				"PE",// 51
				"PH",// 63
				"PN",// 870
				"PL",// 48
				"PT",// 351
				"PR",// 1
				"QA",// 974
				"RO",// 40
				"RU",// 7
				"RW",// 250
				"BL",// 590
				"WS",// 685
				"SM",// 378
				"ST",// 239
				"SA",// 966
				"SN",// 221
				"RS",// 381
				"SC",// 248
				"SL",// 232
				"SG",// 65
				"SK",// 421
				"SI",// 386
				"SB",// 677
				"SO",// 252
				"ZA",// 27
				"KR",// 82
				"ES",// 34
				"LK",// 94
				"SH",// 290
				"PM",// 508
				"SD",// 249
				"SR",// 597
				"SZ",// 268
				"SE",// 46
				"CH",// 41
				"SY",// 963
				"TW",// 886
				"TJ",// 992
				"TZ",// 255
				"TH",// 66
				"TG",// 228
				"TK",// 690
				"TO",// 676
				"TN",// 216
				"TR",// 90
				"TM",// 993
				"TV",// 688
				"AE",// 971
				"UG",// 256
				"GB",// 44
				"UA",// 380
				"UY",// 598
				"US",// 1
				"UZ",// 998
				"VU",// 678
				"VA",// 39
				"VE",// 58
				"VN",// 84
				"WF",// 681
				"YE",// 967
				"ZM",// 260
				"ZW",// 263
		};
		private static final int[] CODE = {93,// AF
				355,// AL
				213,// DZ
				376,// AD
				244,// AO
				672,// AQ
				54,// AR
				374,// AM
				297,// AW
				61,// AU
				43,// AT
				994,// AZ
				973,// BH
				880,// BD
				375,// BY
				32,// BE
				501,// BZ
				229,// BJ
				975,// BT
				591,// BO
				387,// BA
				267,// BW
				55,// BR
				673,// BN
				359,// BG
				226,// BF
				95,// MM
				257,// BI
				855,// KH
				237,// CM
				1,// CA
				238,// CV
				236,// CF
				235,// TD
				56,// CL
				86,// CN
				61,// CX
				61,// CC
				57,// CO
				269,// KM
				242,// CG
				243,// CD
				682,// CK
				506,// CR
				385,// HR
				53,// CU
				357,// CY
				420,// CZ
				45,// DK
				253,// DJ
				670,// TL
				593,// EC
				20,// EG
				503,// SV
				240,// GQ
				291,// ER
				372,// EE
				251,// ET
				500,// FK
				298,// FO
				679,// FJ
				358,// FI
				33,// FR
				689,// PF
				241,// GA
				220,// GM
				995,// GE
				49,// DE
				233,// GH
				350,// GI
				30,// GR
				299,// GL
				502,// GT
				224,// GN
				245,// GW
				592,// GY
				509,// HT
				504,// HN
				852,// HK
				36,// HU
				91,// IN
				62,// ID
				98,// IR
				964,// IQ
				353,// IE
				44,// IM
				972,// IL
				39,// IT
				225,// CI
				81,// JP
				962,// JO
				7,// KZ
				254,// KE
				686,// KI
				965,// KW
				996,// KG
				856,// LA
				371,// LV
				961,// LB
				266,// LS
				231,// LR
				218,// LY
				423,// LI
				370,// LT
				352,// LU
				853,// MO
				389,// MK
				261,// MG
				265,// MW
				60,// MY
				960,// MV
				223,// ML
				356,// MT
				692,// MH
				222,// MR
				230,// MU
				262,// YT
				52,// MX
				691,// FM
				373,// MD
				377,// MC
				976,// MN
				382,// ME
				212,// MA
				258,// MZ
				264,// NA
				674,// NR
				977,// NP
				31,// NL
				599,// AN
				687,// NC
				64,// NZ
				505,// NI
				227,// NE
				234,// NG
				683,// NU
				850,// KP
				47,// NO
				968,// OM
				92,// PK
				680,// PW
				507,// PA
				675,// PG
				595,// PY
				51,// PE
				63,// PH
				870,// PN
				48,// PL
				351,// PT
				1,// PR
				974,// QA
				40,// RO
				7,// RU
				250,// RW
				590,// BL
				685,// WS
				378,// SM
				239,// ST
				966,// SA
				221,// SN
				381,// RS
				248,// SC
				232,// SL
				65,// SG
				421,// SK
				386,// SI
				677,// SB
				252,// SO
				27,// ZA
				82,// KR
				34,// ES
				94,// LK
				290,// SH
				508,// PM
				249,// SD
				597,// SR
				268,// SZ
				46,// SE
				41,// CH
				963,// SY
				886,// TW
				992,// TJ
				255,// TZ
				66,// TH
				228,// TG
				690,// TK
				676,// TO
				216,// TN
				90,// TR
				993,// TM
				688,// TV
				971,// AE
				256,// UG
				44,// GB
				380,// UA
				598,// UY
				1,// US
				998,// UZ
				678,// VU
				39,// VA
				58,// VE
				84,// VN
				681,// WF
				967,// YE
				260,// ZM
				263,// ZW
		};
	}
}