package com.kingstargroup.fdykt.banktransfer;

import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.kingstar.bcc.bccclt;
import com.kingstargroup.fdykt.BccctlInit;
import com.kingstargroup.fdykt.KSConfiguration;
import com.kingstargroup.fdykt.dao.DB2AccountDAO;
import com.kingstargroup.fdykt.dao.DB2CardDAO;
import com.kingstargroup.fdykt.dao.KSDaoSessionFactory;
import com.kingstargroup.fdykt.dto.CardDTO;
import com.kingstargroup.fdykt.trade.GetSidyAction;
import com.kingstargroup.fdykt.util.DateUtil;
import com.kingstargroup.fdykt.util.XMLGenerateUtil;

public class BankTransferAction extends Action {

	private static final Logger logger = Logger.getLogger(GetSidyAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		KSDaoSessionFactory.createSession();
		ActionForward forward = dotrans(mapping, form, request, response);
		try {
			KSDaoSessionFactory.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return forward;
	}

	private ActionForward dotrans(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
			DB2CardDAO carddao = new DB2CardDAO();
			DB2AccountDAO accdao = new DB2AccountDAO();
			String card_bala = request.getParameter("cardBala");
			String payCardCnt = request.getParameter("payCardCnt");
			String cardphyid = request.getParameter("cardphyid");
			String money = request.getParameter("money");
			String bankcardno = request.getParameter("bankcardno");
			String refno = request.getParameter("refno");
			String radomstr = request.getParameter("radomstr");
			String serialno = request.getParameter("serialno");
			
			if (null == cardphyid || null == money || null == payCardCnt
					|| null == refno || null == card_bala
					|| null == bankcardno || "".equals(card_bala)
					|| "".equals(cardphyid) || "".equals(money)
					|| "".equals(bankcardno) || "".equals(refno)
					|| "".equals(payCardCnt)) {
				String xmlout = XMLGenerateUtil.XMLGenerate("7777", "�������ݴ���",
						"", "", "", "", "", null);
				XMLGenerateUtil.XMLSendOut(request, response, xmlout);
				return null;
			}
			
			String hexcardno = Integer.toHexString(Integer.parseInt(cardphyid))
			.toUpperCase();
			for (int i = hexcardno.length(); i < 8; i++) {
				hexcardno = "0" + hexcardno;//pad left
			}

			CardDTO card = new CardDTO();
			try {
				card = carddao.getCardInfoById(hexcardno);
				if(card==null){
					String xmlout = XMLGenerateUtil.XMLGenerate("8888", "����Ϣ�쳣�������Ƿ�������!",
							"", "", "", "", "", null);
					XMLGenerateUtil.XMLSendOut(request, response, xmlout);
					return null;
				}
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("����Ϣ��ȡʧ��");

				String xmlout = XMLGenerateUtil.XMLGenerate("8888", "����Ϣ��ȡ����",
						"", "", "", "", "", null);
				XMLGenerateUtil.XMLSendOut(request, response, xmlout);
				return null;
			}


			long xpackhandle = 0;
			int drtpno = 0;
			int errcode[] = { 1 };
			byte errmsg[] = new byte[300];
			int branchno = 0;// ��̨drtp���ܺ�
			int mainfunction = KSConfiguration.getInstance().getPropertyAsInt(
					"mainfunction", 9000);
			int func = KSConfiguration.getInstance().getPropertyAsInt(
					"logicfunc_banktrans_trans", 240021);// ��̨ҵ���ܺ�
//			int deviceid =  KSConfiguration.getInstance().getPropertyAsInt("logicfunc_web_deviceid", 1);
			String reqip = request.getRemoteAddr();
			int deviceid = -2;
			try {
				deviceid = accdao.getdevidFromip(reqip);
				if (deviceid < 0) {
					String xmlout = XMLGenerateUtil.XMLGenerate("9999", "�豸δע�ᣬ����ʹ�ô˹��ܣ�","", "", "", "", "",null);
					XMLGenerateUtil.XMLSendOut(request, response, xmlout);
					return null;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				String xmlout = XMLGenerateUtil.XMLGenerate("9999", "�豸δע�ᣬ����ʹ�ô˹��ܣ�","", "", "", "", "", null);
				XMLGenerateUtil.XMLSendOut(request, response, xmlout);
				return null;
			}
			
			int[] RetCode = new int[1];
			int[] RecordCount = new int[1];
			bccclt bcc = null;
			try {
				bcc = BccctlInit.getBccctlInstance();
				bcc.XPackInit(1);
				xpackhandle = bcc.NewXpackHandle(KSConfiguration.getInstance()
						.getProperty("cpackfile", "/opt/cpack.dat").getBytes());
				if (0 != xpackhandle) {
					bcc.SetRequestType(xpackhandle, func);

					bcc.SetIntFieldByName(xpackhandle, 0, "lcert_code".getBytes(),
							deviceid);// �ͻ���id
					
					bcc.SetIntFieldByName(xpackhandle, 0, "lvol0".getBytes(),
							Integer.parseInt(card.getCardId()));// ����
					
					
					bcc.SetStringFieldByName(xpackhandle, 0, "sname2"
							.getBytes(), refno.getBytes());// �ο���
					bcc.SetStringFieldByName(xpackhandle, 0, "stx_pwd"
							.getBytes(), radomstr.getBytes());// �ο���
					
					bcc.SetIntFieldByName(xpackhandle, 0, "lvol7".getBytes(),
							Integer.parseInt(serialno));// ������ˮ��

					boolean bccret = bcc.CallRequest(xpackhandle, drtpno,
							branchno, mainfunction, 30000, errcode, errmsg);

					if (bccret) {
						bcc.GetRetCode(xpackhandle, RetCode);
						bcc.GetRecordCount(xpackhandle, RecordCount);
						if (0 == RetCode[0]) {
							if (0 < RecordCount[0]) {
								int[] outcardamount = new int[1];
								bcc.GetIntFieldByName(xpackhandle, 0, "lvol2"
										.getBytes(), outcardamount);// ����ֵ

								String out_card_amount = String.valueOf(outcardamount[0]);
								HashMap datamap = new HashMap();
								datamap.put("amount", money);
								datamap.put("cardBala", card_bala);
								datamap.put("OutCardAmount", out_card_amount);
								datamap.put("refno", refno);
								datamap.put("radomstr", radomstr);
								datamap.put("cardphyid", cardphyid);
								datamap.put("CardId", cardphyid);
								datamap.put("payCardCnt", payCardCnt);
								datamap.put("txCnt", payCardCnt);
								datamap.put("sdate3", DateUtil.getNow("yyyyMMdd"));
								datamap.put("stime3", DateUtil.getNow("HHmmss"));
								

								String xmlout = XMLGenerateUtil.XMLGenerate(
										"0000", "����ת�˳ɹ�", datamap, null);
								
								
								XMLGenerateUtil.XMLSendOut(request, response,
										xmlout);
								return null;
							} else {
								String xmlout = XMLGenerateUtil.XMLGenerate(
										"0001", "�����ݷ���!", "", "", "", "", "", null);
								XMLGenerateUtil.XMLSendOut(request, response,
										xmlout);
								return null;
							}

						} else {
							// ʧ��
							if (0 < RecordCount[0]) {

								byte[] vsmess = new byte[300];
								bcc.GetStringFieldByName(xpackhandle, 0,
										"vsmess".getBytes(), vsmess, 300);
								int i;
								for (i = 0; i < vsmess.length; i++) {
									if (vsmess[i] == 0) {
										break;
									}
								}

								String vs_mess = new String(vsmess, 0, i);

								String xmlout = XMLGenerateUtil.XMLGenerate(
										"0002", vs_mess, "", "", "", "", "", null);
								XMLGenerateUtil.XMLSendOut(request, response,
										xmlout);
								return null;
							} else {
								String xmlout = XMLGenerateUtil.XMLGenerate(
										"0001", "�����ݷ���!", "", "", "", "", "", null);
								XMLGenerateUtil.XMLSendOut(request, response,
										xmlout);
								return null;
							}

						}

					} else {
						// ����ʧ��

						int i;
						for (i = 0; i < errmsg.length; i++) {
							if (errmsg[i] == 0) {
								break;
							}
						}
						String msg = new String(errmsg, 0, i);

						logger.error("connect:errcode=" + errcode[0] + "msg="
								+ msg);
						String xmlout = XMLGenerateUtil.XMLGenerate("1001",
								"��̨ͨѶ��ʱ�����ã����Ժ����ԣ� " + msg, "", "", "", "", "", null);
						XMLGenerateUtil.XMLSendOut(request, response, xmlout);
						return null;
					}
				}
				String xmlout = XMLGenerateUtil.XMLGenerate("1002",
						"ϵͳ�����쳣�����Ժ����ԣ�", "", "", "", "", "", null);
				XMLGenerateUtil.XMLSendOut(request, response, xmlout);
				return null;

			} catch (Exception e) {
				// TODO: handle exception
				logger.error(e.getMessage());
				String xmlout = XMLGenerateUtil.XMLGenerate("1003",
						"ϵͳ�����쳣�����Ժ����ԣ�", "", "", "", "", "", null);
				XMLGenerateUtil.XMLSendOut(request, response, xmlout);
				return null;
			} catch (UnsatisfiedLinkError e) {
				logger.error(e.getMessage());
				String xmlout = XMLGenerateUtil.XMLGenerate("1004",
						"ϵͳ��ʼ���쳣�����Ժ����ԣ�", "", "", "", "", "", null);
				XMLGenerateUtil.XMLSendOut(request, response, xmlout);
				return null;
			} finally {
				if (xpackhandle != 0) {
					bcc.DeleteXpackHandle(xpackhandle);
				}

			}
	}

}