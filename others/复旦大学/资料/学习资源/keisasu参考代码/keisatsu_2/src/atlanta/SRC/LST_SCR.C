/*************************************************************************
*	System		: POPLAR/ANZU_L
*	File Name	: LST_SCR.C
*	Author		: 桑原 美紀
*	Date		: 1997/03/11
*	RealTimeOS	: ＲＩＳＣ（ＳＨ７０４３）モニター
*	Description	: 親展者リスト
*	Maintenance	: 
*
*	Copyright (C) 1996 Murata Machinery,Ltd. All rights reserved.
*************************************************************************/
#include	"\src\atlanta\define\product.h"
#include	"\src\atlanta\define\apl_msg.h"
#include	"\src\atlanta\define\std.h"
#include	"\src\atlanta\define\sysmailb.h"

#include	"\src\atlanta\define\cmn_def.h"
#include	"\src\atlanta\define\lst_def.h"

#include	"\src\atlanta\define\cmn_pro.h"
#include	"\src\atlanta\define\lst_pro.h"

#include	"\src\atlanta\ext_v\bkupram.h"
#include	"\src\atlanta\ext_v\lst_data.h"
#include	"\src\atlanta\ext_v\lst_wrd.h"

#if	( PRO_PANEL == ANK )
#include "\src\atlanta\opr\ank\define\opr_def.h"
#endif
#if	( PRO_PANEL == KANJI )
#include "\src\atlanta\opr\kanji\define\opr_def.h"
#endif

/*************************************************************************
	module		:[親展者ﾘｽﾄ]
	function	:[親展者ﾘｽﾄを印刷する]
	common		:[
		List		:
		SYB_MailBox	:
	]
	condition	:[]
	commment	:[]
	return		:[印刷結果]
	machine		:[SH7043]
	language	:[SHC]
	keyword		:[LST]
	date		:[93/11/25]
	author		:[増田次郎]
*************************************************************************/
WORD				Print_Mailbox_List(
						 void )				/*なし*/
{
	UBYTE	i;						/*loop*/
	register WORD	ret;					/*実行結果*/

	/*ﾃﾞｰﾀなしﾁｪｯｸ*/
	for ( i = 0; i < SYS_MAILBOX_MAX; i++ ) {
		if ( SYB_MailBox[i].Status != SYS_MAILBOX_NO_SET ) {
			break;
		}
	}
	if ( i == SYS_MAILBOX_MAX ) {
		return ( MSG_PRN_LST_NO_LIST );
	}

	if ( (ret = CharacterPrint( LST_OPEN )) != OK ) {
		return ( ret );
	}

	/*１改行＋発信元名、番号印刷＋２改行＋見出し＋１改行*/
#if ( PRO_PANEL == KANJI ) || ( PRO_JIS_CODE == ENABLE )
		if ( (ret = Print_Head1_Common( PWRD_SecureMailBList_K,
		 LST_NOT, 23 )) != OK ) {/** "1-1" -> "23" By M.Kuwahara 1997/02/12 */
			return ( ret );
		}
#else
	if ( (ret = Print_Head1_Common( PWRD_SecureMailBList,
	  LST_XXX_TWI_XXX, 18 )) != OK ) { /** "1-1" -> "18" By H.Hirao 1996/07/11 */
		return ( ret );
	}

#endif

	/*現在時間*/
	if ( (ret = CurrentTimePrint( 30 )) != OK ) { /** "8-1" -> "30" By H.Hirao 1996/07/11 */
		return ( ret );
	}

	/*�ｮ�｢�ﾎ�｢�｢�｢�｢�｢�ｲ*/
	Print_Mailbox_List_Line( 0, List.S.Print, List.S.Attribute );
	if ( (ret = CharacterPrint( LST_PRINT )) != OK ) {
		return ( ret );
	}

	/*�､ Box�､ｼﾝﾃﾝｼｬﾒｲ�､*/
	Print_Mailbox_List_Line( 1, List.S.Print, List.S.Attribute );
	if ( (ret = CharacterPrint( LST_PRINT )) != OK ) {
		return ( ret );
	}

	for ( i = 0; i < SYS_MAILBOX_MAX; i++ ) {
		if ( SYB_MailBox[i].Status != SYS_MAILBOX_NO_SET ) {
			/*�ｾ�｢�｢�ﾞ�｢�｢�｢�ﾆ*/
			Print_Mailbox_List_Line( 2, List.S.Print, List.S.Attribute );
			if ( (ret = CharacterPrint( LST_PRINT )) != OK ) {
				return ( ret );
			}

			/*�､ 9 �､xxxxxx�､*/
			Prn_Att_Str_Clear( List.S.Print, List.S.Attribute );
			List.S.Print[29] = LST_KLINEI; /** "7-1" -> "29" By H.Hirao 1996/07/11 */
#if ( PRO_CPU == SH7043 )	/* FX,MMLで親展BOXNo.が、0〜or1〜と違う時のため */
			if ( (i+(OPR_MAILBOX_MIN)) >= 10 ){
				CMN_UnsignedIntToASC( &List.S.Print[30],
				 (UWORD)i + ( OPR_MAILBOX_MIN ), 0, 0 ); /** "8-1" -> "30" By H.Hirao 1996/07/11 */
			}
			else {
				CMN_UnsignedIntToASC( &List.S.Print[31],
				 (UWORD)i + ( OPR_MAILBOX_MIN ), 0, 0 ); /** "9-1" -> "31" By H.Hirao 1996/07/11 */
			}
#else
			CMN_UnsignedIntToASC( &List.S.Print[9-1], (UWORD)i, 0, 0 );
#endif
			List.S.Print[33] = LST_KLINEI; /** "11-1" -> "33" By H.Hirao 1996/07/11 */
			/** "12-1" -> "34" By H.Hirao 1996/07/11 */
			CMN_StringCopy(&List.S.Print[34], SYB_MailBox[i].BoxName ); /*1996/03/12 Eguchi */
			List.S.Print[50] = LST_KLINEI; /** "28-1" -> "50" By H.Hirao 1996/07/11 */
			if ( (ret = CharacterPrint( LST_PRINT )) != OK ) {
				return ( ret );
			}
		}
	}
	/*�ｶ�｢�｢�ﾖ�｢�｢�｢�｢�ｺ*/
	Print_Mailbox_List_Line( 3, List.S.Print, List.S.Attribute );
	if ( (ret = CharacterPrint( LST_PRINT )) != OK ) {
		return ( ret );
	}

	/*１行印字（改行）*/
	if ( (ret = CharacterPrint( LST_LINE_FEED )) != OK ) {
		return ( ret );
	}

	if ( (ret = CharacterPrint( LST_CLOSE )) != OK ) {
		return ( ret );
	}
	return ( OK );
}

/*************************************************************************
	module		:[親展者ﾘｽﾄの横区切り罫線作成]
	function	:[親展者ﾘｽﾄの横区切り罫線を作成する。
	common		:[]
	condition	:[]
	commment	:[]
	return		:[印刷結果]
	machine		:[SH7043]
	language	:[SHC]
	keyword		:[LST]
	date		:[93/11/25]
	author		:[増田次郎]
*************************************************************************/
void			Print_Mailbox_List_Line(
						 UBYTE Category,			/*罫線種類*/
						 register UBYTE *AscString,	/*印刷文字列*/
						 register UBYTE *AttString )/*修飾文字列*/
{
	Prn_Att_Str_Clear( AscString, AttString );
	switch ( Category ) {
		case 0:
			/*�ｮ�｢�ﾎ�｢�｢�｢�｢�｢�ｲ*/
			AscString[29] = LST_KLINE7; /** "7-1" -> "29" By H.Hirao 1996/07/11 */
			CMN_MemorySet( &AscString[30], 20, LST_KLINE_ ); /** "8-1"-> "30" By H.Hirao */
			AscString[33] = LST_KLINE8; /** "11-1" -> "33" By H.Hirao 1996/07/11 */
			AscString[50] = LST_KLINE9; /** "28-1" -> "50" By H.Hirao 1996/07/11 */
			break;
		case 1:
			/*�､ Box�､ｼﾝﾃﾝｼｬﾒｲ�､*/
			AscString[29] = LST_KLINEI; /** "7-1" -> "29" By H.Hirao 1996/07/11 */
			AttributeSet( &AscString[30], &AttString[30], PWRD_Box,
			  LST_NOT ); /** "8-1" -> "30" By H.Hirao 1996/07/11 */
			AscString[33] = LST_KLINEI; /** "11-1" -> "33" By H.Hirao 1996/07/11 */
			AttributeSet( &AscString[38], &AttString[38], PWRD_UserName,
			  LST_NOT ); /** "16-1" -> "38" By H.Hirao 1996/07/11 */
			AscString[50] = LST_KLINEI; /** "28-1" -> "50" By H.Hirao 1996/07/11 */
			break;
		case 2:
			/*�ｾ�｢�｢�ﾞ�｢�｢�｢�ﾆ*/
			AscString[29] = LST_KLINE4; /** "7-1" -> "29" By H.Hirao 1996/07/11 */
			CMN_MemorySet( &AscString[30], 20, LST_KLINE_ ); /** "8-1" -> "30" By H.Hirao 1996/07/11 */
			AscString[33] = LST_KLINE5; /** "11-1" -> "33" By H.Hirao 1996/07/11 */
			AscString[50] = LST_KLINE6; /** "28-1" -> "50" By H.Hirao 1996/07/11 */
			break;
		case 3:
			/*�ｶ�｢�｢�ﾖ�｢�｢�｢�｢�ｺ*/
			AscString[29] = LST_KLINE1; /** "7-1" -> "29" By H.Hirao 1996/07/11 */
			CMN_MemorySet( &AscString[30], 20, LST_KLINE_ ); /** "8-1" -> "30" By H.Hirao 1996/07/11 */
			AscString[33] = LST_KLINE2; /** "11-1" -> "33" By H.Hirao 1996/07/11 */
			AscString[50] = LST_KLINE3; /** "28-1" -> "50" By H.Hirao 1996/07/11 */
			break;
	}
}

