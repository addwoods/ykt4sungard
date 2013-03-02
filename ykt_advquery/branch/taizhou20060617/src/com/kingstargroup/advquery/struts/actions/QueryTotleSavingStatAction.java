/**
 * 
 */
package com.kingstargroup.advquery.struts.actions;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;

import com.kingstargroup.advquery.cewolf.CustomerDrawChart;
import com.kingstargroup.advquery.cewolf.DrawBarByTypeMap;
import com.kingstargroup.advquery.cewolf.DrawPieCust;
import com.kingstargroup.advquery.cewolf.DrawTimeSeries;
import com.kingstargroup.advquery.common.DateUtil;
import com.kingstargroup.advquery.common.ErrorInfo;
import com.kingstargroup.advquery.common.GetListResultStat;
import com.kingstargroup.advquery.common.GetProperty;
import com.kingstargroup.advquery.common.SortListByResult;
import com.kingstargroup.advquery.hibernate.util.ReportActiveUtil;

/**
 * ����������ͳ�ƣ����ͱ�����
 * @author Administrator
 *
 */
public class QueryTotleSavingStatAction extends Action{

	public ActionForward execute(ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		QueryTotleSavingStatActionExecution exec = new QueryTotleSavingStatActionExecution();
		return exec.execute(mapping,form,request,response);

	}
	private class QueryTotleSavingStatActionExecution {
		private String beginDate;
		private String endDate;
		private StringBuffer dateRange = new StringBuffer(200);
		private String statTypeShow = "";
		private String showType ;
		private String picType = "";
		//��λ
		private int dataUnit = 1;
		private int dateUnit = 1;
		//������ʾ��Ϣ
		private String yLabel = "";
		private List weekResult = null;
		private String statType;
		//ͳ�����moneyΪ��numΪ����
		private String moneyornum;
		private String mapforward = "";
		//��ʾͼ���������
		private String accType[] = {"POS��ֵ","Ȧ��ת��","���ѱ���ֵ","֧Ʊ��ֵ","�ֽ��ֵ"};
		private String xData = "";
		private String columnTitle[] = {"pos","transfer","outlay","ticket","cash"};
		
		private String accTypeNum[] = {"POS��ֵ","Ȧ��ת��","�ֽ��ֵ"};
		private String columnTitleNum[] = {"pos","transfer","cash"};
		
		// �Զ���ͼ����ʾҪ����Ĳ���
		// ��ʾ�Զ�����ɫ
		private boolean showCustColor = true;
		// ��ʾ���������ֵ���ת����
		private boolean showTextAxis = true ;
		// ��ʾBarͼ���ϵ�����
		private boolean showLabel = false;
		// ��ʾ�Զ���ͼ��������ɫ
		private boolean showBackgroundColor = true;
		// ��ʾ������
		private boolean showOutLine = true;
		// ��ʾ������
		private boolean showGrid = true;
		
		ActionServlet servlet = QueryTotleSavingStatAction.this.servlet;
		public ActionForward execute(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
			throws Exception{
			//��ʼ����
			beginDate = request.getParameter("begindate");
			//��������
			endDate = request.getParameter("enddate");
			//ѡ���ͼ����ʾ����
			showType = request.getParameter("showType");		
			//��ѯ����
			statType = request.getParameter("statType");
			moneyornum = request.getParameter("moneyornum");
			if (("".equals(beginDate))||("".equals(endDate))){
				request.setAttribute("errorcont",new ErrorInfo().getErrorTip(statType,beginDate,endDate));
				return mapping.findForward("errortest");			
			}
			setInit(request);
			//�ж��Ƿ�������ͼ��ʾ
			if (new ErrorInfo().DateRangeJudeg(showType,statType,beginDate,endDate,request)){
				return mapping.findForward("errortest");
			}
			

			if ("money".equals(moneyornum)){
				String dataType = "float";
				String picAttribute = "TotleSavingMoneyBar";
				mapforward = "totlesavingmoney_queryresult";
				List result = ReportActiveUtil.getTotleSavingMoneyStat(beginDate,endDate,statType);
				if (result.isEmpty()){
					request.setAttribute("errorcont",new ErrorInfo().getErrorTip(statType,beginDate,endDate));
					return mapping.findForward("errortest");			
				}
				dateUnit = result.size()/9;
				statTypeResult(request,result,dataType,picAttribute,accType,columnTitle);

				//����Ҫ��ʾ��ͼ��������ͼ��
				showTypeResult();
				request.setAttribute("pictype",picType);
				float[] resultPie = new GetListResultStat().getListResultStatFloat(result,
						columnTitle);

				showTextAxis = new ErrorInfo().showTextAxis(result);
				request.setAttribute("stattypeshow",statTypeShow);
				// ���Ʊ�ͼ
				request
						.setAttribute("TotleSavingMoneyPieData", new DrawPieCust(
								resultPie, accType, dataType, columnTitle)
								.getDatasetProducerTotle());
				// �Զ����ͼ��ʾ
				request.setAttribute("dataColorPie", new CustomerDrawChart(showBackgroundColor).getPiePostProcessor());
				
			}else if ("num".equals(moneyornum)){
				//��ʾͼ���������
				String dataType = "integer";
				String picAttribute = "TotleSavingNumBar";
				mapforward = "totlesavingnum_queryresult";
				List result = ReportActiveUtil.getTotleSavingNumStat(beginDate,endDate,statType);
				if (result.isEmpty()){
					request.setAttribute("errorcont",new ErrorInfo().getErrorTip(statType,beginDate,endDate));
					return mapping.findForward("errortest");			
				}
				dateUnit = result.size()/9;
				statTypeResult(request,result,dataType,picAttribute,accTypeNum,columnTitleNum);
				//����Ҫ��ʾ��ͼ��������ͼ��
				showTypeResult();
				
				request.setAttribute("pictype",picType);
		
				int[] resultPie = new GetListResultStat().getListResultStat(result,
						columnTitleNum);
				request.setAttribute("stattypeshow",statTypeShow);
				// ���Ʊ�ͼ
				request
						.setAttribute("TotleSavingNumData", new DrawPieCust(
								resultPie, accTypeNum, dataType, columnTitleNum)
								.getDatasetProducerTotle());
				// �Զ����ͼ��ʾ
				request.setAttribute("dataColorPie", new CustomerDrawChart(showBackgroundColor).getPiePostProcessor());
			}
			//�Զ���ͼ����ʾ
			request.setAttribute("ylabel",yLabel);
			drawPic(request);
			request.setAttribute("daterange",dateRange.toString());
			return mapping.findForward(mapforward);
			
		}
		private void setInit(HttpServletRequest request){
			HttpSession session = request.getSession();
			if ("bydate".equals(statType)){
				beginDate = DateUtil.reFormatTime(request.getParameter("begindate"));
				endDate = DateUtil.reFormatTime(request.getParameter("enddate"));	
	 			session.setAttribute("begindate23d",request.getParameter("begindate"));
				session.setAttribute("enddate23d",request.getParameter("enddate"));
				session.setAttribute("querytype23","0");
				statTypeShow = GetProperty.getProperties("title.date",servlet.getServletContext());
				dateRange.append(GetProperty.getProperties("query.show.date",servlet.getServletContext()))
				.append(beginDate).append("-").append(endDate);
			}else if ("bymonth".equals(statType)){
				beginDate = DateUtil.getMonth(request.getParameter("begindate"));
				endDate = DateUtil.getMonth(request.getParameter("enddate"));	
	 			session.setAttribute("begindate23m",request.getParameter("begindate"));
				session.setAttribute("enddate23m",request.getParameter("enddate"));
				session.setAttribute("querytype22","1");
				statTypeShow = GetProperty.getProperties("title.month",servlet.getServletContext());
				dateRange.append(GetProperty.getProperties("query.show.month",servlet.getServletContext()))
				.append(beginDate).append("-").append(endDate);
			}else if ("byweek".equals(statType)){
				beginDate = DateUtil.reFormatTime(request.getParameter("begindate"));
				endDate = DateUtil.reFormatTime(request.getParameter("enddate"));	
	 			session.setAttribute("begindate23w",request.getParameter("begindate"));
				session.setAttribute("enddate23w",request.getParameter("enddate"));
				session.setAttribute("querytype22","2");
				//beginWeek = DateUtilExtend.getWeek(beginDate);
				//endWeek = DateUtilExtend.getWeek(endDate);	
				statTypeShow = GetProperty.getProperties("title.week",servlet.getServletContext());
				dateRange.append(GetProperty.getProperties("query.show.week",servlet.getServletContext()))
				.append(beginDate).append("-").append(endDate);
			}
		}
		private void statTypeResult(HttpServletRequest request,List result,String dataType,
				String picAttribute,String inAccType[],String inColumnTitle[]){
			if ("bydate".equals(statType)){
				request.setAttribute("result", result);
				if ("money".equals(moneyornum)){
					yLabel = new GetListResultStat().getYLabelMoney(result, "pos",dataType);
				}else if ("num".equals(moneyornum)){
					yLabel = new GetListResultStat().getYLabelNum(result,"pos",dataType);
				}
				dataUnit = new GetListResultStat().getUnitValue(result, "pos",dataType);
				xData = "balance_date";
				if ("line".equals(showType)){
					request.setAttribute(picAttribute,
					new DrawTimeSeries(result, inColumnTitle, xData,
							dataType, inAccType, dataUnit)
							.getTimeSeriesProducer());
				}else{
					request.setAttribute(picAttribute,
							new DrawBarByTypeMap(result,inAccType,xData,dataType,inColumnTitle,dataUnit)
							.getDataProducer());
				}
			}else if ("bymonth".equals(statType)){
				request.setAttribute("result",result);
				if ("money".equals(moneyornum)){
					yLabel = new GetListResultStat().getYLabelMoney(result, "pos",dataType);
				}else if ("num".equals(moneyornum)){
					yLabel = new GetListResultStat().getYLabelNum(result,"pos",dataType);
				}
				dataUnit = new GetListResultStat().getUnitValue(result, "pos",dataType);
				xData = "balance_date";
				request.setAttribute(picAttribute,
						new DrawBarByTypeMap(result,inAccType,xData,dataType,inColumnTitle,dataUnit).getDataProducer());
				showTextAxis = new ErrorInfo().showTextAxis(result);
			}else if ("byweek".equals(statType)){
				weekResult = new GetListResultStat().getListByWeek(result,beginDate,endDate);
				Collections.sort(weekResult,new SortListByResult("balance_date"));
				if ("money".equals(moneyornum)){
					yLabel = new GetListResultStat().getYLabelMoney(weekResult, "pos",dataType);
				}else if ("num".equals(moneyornum)){
					yLabel = new GetListResultStat().getYLabelNum(weekResult,"pos",dataType);
				}
				dataUnit = new GetListResultStat().getUnitValue(weekResult, "pos",dataType);
				xData = "yearweek";
				request.setAttribute("result",weekResult);
				request.setAttribute(picAttribute,
						new DrawBarByTypeMap(weekResult,inAccType,xData,dataType,inColumnTitle,dataUnit).getDataProducer());
				showTextAxis = new ErrorInfo().showTextAxis(weekResult);
			}
		}
		private void showTypeResult(){
			if ("line".equals(showType)) {
				if ("bydate".equals(statType)){
					picType = GetProperty.getProperties("pic.timeseries",servlet.getServletContext());
				}else{
					picType = GetProperty.getProperties("pic.line",servlet.getServletContext());
				}
				showOutLine = false;
			}else if ("bar".equals(showType)){
				picType = GetProperty.getProperties("pic.bar",servlet.getServletContext());
				showOutLine = true;
			}else if ("stack".equals(showType)){
				picType = GetProperty.getProperties("pic.stack",servlet.getServletContext());
				showOutLine = true;
			}
		}
		private void drawPic(HttpServletRequest request){
			if ("line".equals(showType)) {
				if ("bydate".equals(statType)){
					request.setAttribute("dataColor", new CustomerDrawChart(showBackgroundColor,showCustColor,dateUnit)
							.getTimeSeriesPostProcessor());
				}else{
					request.setAttribute("dataColor", new CustomerDrawChart(showCustColor,
							showTextAxis, showLabel, showBackgroundColor, showOutLine,
							showGrid).getChartPostProcessor());
				}
			}else if ("bar".equals(showType)) {
				request.setAttribute("dataColor", new CustomerDrawChart(showCustColor,
						showTextAxis, showLabel, showBackgroundColor, showOutLine,
						showGrid).getChartPostProcessor());
			}else if ("stack".equals(showType)) {
				request.setAttribute("dataColor", new CustomerDrawChart(showCustColor,
						showTextAxis, showLabel, showBackgroundColor, showOutLine,
						showGrid).getChartPostProcessor());
			}
		}
	}
}