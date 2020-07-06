package cn.nbbandxdd.survey.resprec.logic;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.nbbandxdd.survey.common.CommonUtils;
import cn.nbbandxdd.survey.common.ICommonConstDefine;
import cn.nbbandxdd.survey.common.jwt.JwtHolder;
import cn.nbbandxdd.survey.resprec.dao.RespRecDAO;
import cn.nbbandxdd.survey.resprec.dao.RespRecSendLstDAO;
import cn.nbbandxdd.survey.resprec.dao.entity.DtlRecEntity;
import cn.nbbandxdd.survey.resprec.dao.entity.RespRecEntity;
import cn.nbbandxdd.survey.resprec.dao.entity.RespRecIntvEntity;
import cn.nbbandxdd.survey.resprec.dao.entity.RespRecSendLstEntity;
import cn.nbbandxdd.survey.resprec.dao.entity.RespRecUsrRankEntity;
import cn.nbbandxdd.survey.resprec.dao.entity.RespRecUsrStatEntity;
import cn.nbbandxdd.survey.resprec.dao.entity.WeekRptModel;
import cn.nbbandxdd.survey.resprec.dao.entity.RespRecExamStatEntity;
import cn.nbbandxdd.survey.resprec.dao.entity.RespRecGrpStatEntity;

@Component
public class RespRecLogic {

	@Autowired
	private RespRecDAO respRecDAO;
	
	@Autowired
	private RespRecSendLstDAO respRecSendLstDAO;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String sendMailAddress;
	
	@Transactional
	public DtlRecEntity insertDtl(DtlRecEntity entity) {
		
		entity.setLastMantTmstp(new Timestamp(new Date().getTime()));
		entity.setScre(CommonUtils.fillbackQuesScre(entity.getQuesCd(), entity.getAnswList()));
		entity.setAnswCd(String.join(",", entity.getAnswList()));
		entity.setOpenId(JwtHolder.get());

		respRecDAO.insertDtl(entity);
		
		entity.setAnswList(CommonUtils.fillbackRightAnsw(entity.getQuesCd()));
		
		// resp_rec
		if (ICommonConstDefine.COMMON_IND_YES.equals(entity.getLastInd())) {
			
			RespRecEntity infoEntity = new RespRecEntity();
			infoEntity.setExamCd(entity.getExamCd());
			RespRecEntity respRecEntity = insertResp(infoEntity);
			
			entity.setRespScre(respRecEntity.getScre());
			entity.setRespSpnd(respRecEntity.getSpnd());
		}
		
		return entity;
	}
	
	@Transactional
	public RespRecEntity insertResp(RespRecEntity entity) {
		
		entity.setOpenId(JwtHolder.get());
		entity.setDat(new SimpleDateFormat("yyyyMMdd").format(new Date()));
		
		entity.setSpnd(respRecDAO.findRespSpnd(entity));
		
		String typCd = CommonUtils.fillbackTypCd(entity.getExamCd());
		if (ICommonConstDefine.EXAM_TYP_CD_DEFAULT.contentEquals(typCd) ||
			ICommonConstDefine.EXAM_TYP_CD_DEFINITE.contentEquals(typCd)) {
			
			entity.setScre(respRecDAO.findScreForDefinite(entity));
		} else if (ICommonConstDefine.EXAM_TYP_CD_RANDOM.contentEquals(typCd)) {
			
			entity.setScre(respRecDAO.findScreForRandom(entity));
		} else {
			
			throw new RuntimeException(String.format("类型为“%s”的问卷类型不存在。", typCd));
		}
		
		respRecDAO.insertResp(entity);
		
		return entity;
	}
	
	public DtlRecEntity loadDtlByKey(DtlRecEntity entity) {
		
		entity.setOpenId(JwtHolder.get());
		
		return respRecDAO.loadDtlByKey(entity);
	}
	
	public RespRecEntity loadRespByKey(RespRecEntity entity) {
		
		entity.setOpenId(JwtHolder.get());
		
		return respRecDAO.loadRespByKey(entity);
	}
	
	public Page<RespRecEntity> findRespList(Integer pageNum, Integer pageSize) {
		
		RespRecEntity infoEntity = new RespRecEntity();
		infoEntity.setOpenId(JwtHolder.get());
		
		PageHelper.startPage(pageNum, pageSize);
		return respRecDAO.findRespList(infoEntity);
	}
	
	public Page<RespRecUsrRankEntity> findUsrRank(RespRecUsrRankEntity entity, Integer pageNum, Integer pageSize) {
		
		PageHelper.startPage(pageNum, pageSize);
		return respRecDAO.findUsrRank(entity);
	}
	
	public RespRecExamStatEntity findExamStat(RespRecExamStatEntity entity) {
		
		RespRecExamStatEntity se = respRecDAO.findExamStat(entity);
		if (null == se) {
			
			se = new RespRecExamStatEntity();
			se.setExamCd(entity.getExamCd());
			se.setCnt(0);
			se.setAvgScre(0.0);
			se.setAvgSpnd(0.0);
			se.setCntU40(0);
			se.setRateU40(0.0);
			se.setCntU70(0);
			se.setRateU70(0.0);
			se.setCntU100(0);
			se.setRateU100(0.0);
			se.setCnt100(0);
			se.setRate100(0.0);
		}
		
		return se;
	}

	public List<RespRecGrpStatEntity> findGrpStat(RespRecGrpStatEntity entity) {
		
		if (StringUtils.isBlank(entity.getDprtNam())) {
			
			entity.setDprtNam(CommonUtils.fillbackDprtNam());
		}

		List<RespRecGrpStatEntity> sel = respRecDAO.findGrpStat(entity);
		if (null == sel) {
			
			return null;
		}
		
		sel.stream().forEach(a -> a.setPtpnRate(0 == a.getTotCnt() ? 0.0 : (double)a.getCnt() / a.getTotCnt()));
		
		return sel;
	}
	
	@Scheduled(cron = "0 0 18 ? * FRI")
	public void sendWeekRpt() {

		// 每次仅有一个实例生成并发送报表
		Date now = new Date();
		Calendar calLast = Calendar.getInstance();
		calLast.setTime(now);
		calLast.add(Calendar.DAY_OF_YEAR, -7);
		calLast.set(Calendar.HOUR_OF_DAY, 0);
		calLast.set(Calendar.MINUTE, 0);
		calLast.set(Calendar.SECOND, 0);
		calLast.set(Calendar.MILLISECOND, 0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		if (!CommonUtils.validateUniJob(ICommonConstDefine.UNI_JOB_RESP_REC_SEND,
			sdf.format(calLast.getTime()), sdf.format(now))) {

			return;
		}

		// 生成并发送报表
		List<RespRecSendLstEntity> lis = respRecSendLstDAO.findList();
		for (RespRecSendLstEntity one : lis) {

			one.setLastSendTmstp(new Timestamp(new Date().getTime()));
			try {
				
				MimeMessage message = mailSender.createMimeMessage();
				message.addRecipients(MimeMessage.RecipientType.CC, sendMailAddress);
				
				MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
				helper.setFrom(sendMailAddress, "揭谛答题");
				helper.setTo(StringUtils.split(one.getMailAddr(), ','));
				helper.setSubject(one.getMailSubj());
				
				WeekRptModel model = genWeekRptModel(one.getOpenId(), one.getDprtNam());
				if (model != null) {
					
					Context textContext = genWeekRptMailContext(model);
					
					helper.setText(templateEngine.process("PMExamMail", textContext), true);
					
					helper.addInline(
						"imgLine", new ByteArrayDataSource(genWeekRptCntChart(model), "image/png"));
					helper.addInline(
						"imgBar", new ByteArrayDataSource(genWeekRptPtpnRateChart(model), "image/png"));
					helper.addAttachment(
						MimeUtility.encodeWord(one.getMailSubj() + ".docx", "UTF-8", "B"),
						new ByteArrayDataSource(
							genWeekRptAttachment(model),
							"application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
					
					mailSender.send(message);
					
					one.setErrMsg("发送成功");
				} else {
					
					one.setErrMsg("无报告");
				}
			} catch (Exception e) {
				
				one.setErrMsg(e.getMessage());
			} finally {
				
				respRecSendLstDAO.update(one);
			}
		}
	}
	
	private List<RespRecExamStatEntity> findIntvExamStat(RespRecIntvEntity entity) {

		return respRecDAO.findIntvExamStat(entity);
	}
	
	private List<RespRecGrpStatEntity> findIntvGrpStat(RespRecIntvEntity entity) {

		List<RespRecGrpStatEntity> sel = respRecDAO.findIntvGrpStat(entity);
		if (null == sel) {
			
			return null;
		}

		int examNum =
			CommonUtils.fillbackExamCntByIntv(
				entity.getLastMantUsr(), entity.getBgnTimeBgn(), entity.getBgnTimeEnd());
		
		sel.stream().forEach(a -> a.setPtpnRate(
			0 == a.getTotCnt() || 0 == examNum ? 0.0 : (double)a.getCnt() / a.getTotCnt() / examNum));
		
		return sel;
	}
	
	private List<RespRecUsrStatEntity> findIntvUsrStatByCnt(RespRecIntvEntity entity) {

		List<RespRecUsrStatEntity> sel = respRecDAO.findIntvUsrStatByCnt(entity);
		if (null == sel) {
			
			return null;
		}
		
		for (int i = 0; i < sel.size(); ++i) {
			
			sel.get(i).setRowNum(i + 1);
		}
		return sel;
	}
	
	private List<RespRecUsrStatEntity> findIntvUsrStatByAvgScre(RespRecIntvEntity entity) {

		List<RespRecUsrStatEntity> sel = respRecDAO.findIntvUsrStatByAvgScre(entity);
		if (null == sel) {
			
			return null;
		}
		
		for (int i = 0; i < sel.size(); ++i) {
			
			sel.get(i).setRowNum(i + 1);
		}
		return sel;
	}
	
	private WeekRptModel genWeekRptModel(String openId, String dprtNam) {
		
		// 数据准备
		Date now = new Date();
		
		Calendar calFirst = Calendar.getInstance();
		calFirst.setTime(now);
		calFirst.add(Calendar.DAY_OF_YEAR, -((calFirst.get(Calendar.DAY_OF_WEEK) + 5) % 7));
		calFirst.set(Calendar.HOUR_OF_DAY, 0);
		calFirst.set(Calendar.MINUTE, 0);
		calFirst.set(Calendar.SECOND, 0);
		calFirst.set(Calendar.MILLISECOND, 0);
		
		RespRecIntvEntity infoIntvEntity = new RespRecIntvEntity();
		infoIntvEntity.setBgnTimeBgn(new Timestamp(calFirst.getTimeInMillis()));
		infoIntvEntity.setBgnTimeEnd(new Timestamp(now.getTime()));
		infoIntvEntity.setLastMantUsr(openId);
		infoIntvEntity.setDprtNam(dprtNam);
		
		// 数据模型
		WeekRptModel model = new WeekRptModel();
		model.setDprtNam(dprtNam);
		model.setFirst(calFirst.getTime());
		model.setNow(now);
		
		model.setExamStat(findIntvExamStat(infoIntvEntity));
		model.setGrpStat(findIntvGrpStat(infoIntvEntity));
		model.setUsrCntStat(findIntvUsrStatByCnt(infoIntvEntity));
		
		infoIntvEntity.setOthIntCond((int)(CollectionUtils.size(model.getExamStat()) * 0.8));
		model.setUsrAvgScreStat(findIntvUsrStatByAvgScre(infoIntvEntity));
		
		if (CollectionUtils.isEmpty(model.getExamStat()) ||
			CollectionUtils.isEmpty(model.getGrpStat()) ||
			CollectionUtils.isEmpty(model.getUsrCntStat()) ||
			CollectionUtils.isEmpty(model.getUsrAvgScreStat())) {
			
			return null;
		}
		
		return model;
	}
	
	private byte[] genWeekRptCntChart(WeekRptModel model) throws IOException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("M月d日");
		
		DefaultCategoryDataset lineDataset = new DefaultCategoryDataset();
		model.getExamStat().forEach(a -> lineDataset.addValue(a.getCnt(), "参与人数", sdf.format(a.getBgnTime())));
		model.getExamStat().forEach(a -> lineDataset.addValue(a.getAvgScre(), "平均分", sdf.format(a.getBgnTime())));
		model.getExamStat().forEach(a -> lineDataset.addValue(a.getCnt100(), "场满分人数", sdf.format(a.getBgnTime())));
		
		StandardChartTheme theme = new StandardChartTheme("CN");
		theme.setExtraLargeFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
		theme.setLargeFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		theme.setRegularFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		theme.setSmallFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		theme.setPlotBackgroundPaint(Color.WHITE);
		theme.setPlotOutlinePaint(Color.WHITE);
		ChartFactory.setChartTheme(theme);
		
		JFreeChart lineChart = ChartFactory.createLineChart(
			String.format("%s%s-%s线上练习数据分布", model.getDprtNam(), sdf.format(model.getFirst()), sdf.format(model.getNow())),
			StringUtils.EMPTY, StringUtils.EMPTY,
			lineDataset, PlotOrientation.VERTICAL, 
			true, true, false);
		lineChart.setBackgroundPaint(Color.WHITE);
		
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			
			ChartUtils.writeChartAsPNG(out, lineChart, 480, 290);
			out.flush();
			
			return out.toByteArray();
		}
	}
	
	private byte[] genWeekRptPtpnRateChart(WeekRptModel model) throws IOException {
		
		DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
		model.getGrpStat().sort((a, b) -> (int)(b.getPtpnRate() * 100.0 - a.getPtpnRate() * 100.0));
		model.getGrpStat().forEach(a -> barDataset.addValue(a.getPtpnRate() * 100.0, "参与率", a.getGrpNam()));
		
		StandardChartTheme theme = new StandardChartTheme("CN");
		theme.setExtraLargeFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
		theme.setLargeFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		theme.setRegularFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		theme.setSmallFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		theme.setPlotBackgroundPaint(Color.WHITE);
		theme.setPlotOutlinePaint(Color.WHITE);
		ChartFactory.setChartTheme(theme);
		
		JFreeChart barChart = ChartFactory.createBarChart(
			String.format("%s各职能组练习参与率", model.getDprtNam()),
			StringUtils.EMPTY, StringUtils.EMPTY,
			barDataset, PlotOrientation.VERTICAL,
			false, false, false);
		barChart.setBackgroundPaint(Color.WHITE);
		
		CategoryPlot plot = barChart.getCategoryPlot();
		BarRenderer render = (BarRenderer)plot.getRenderer();
		render.setSeriesPaint(0, new Color(218, 150, 149));
		
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			
			ChartUtils.writeChartAsPNG(out, barChart, 480, 290);
			out.flush();
			
			return out.toByteArray();
		}
	}
	
	private Context genWeekRptMailContext(WeekRptModel model) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("M月d日");
		
		Context context = new Context();
		
		// para1
		context.setVariable("para1", String.format("%s技委会项目经理线上考试筹备组于%s至%s，共编写并发布%d期线上练习题目。",
			model.getDprtNam(), sdf.format(model.getFirst()), sdf.format(model.getNow()), model.getExamStat().size()));
		
		// para2
		context.setVariable("para2", String.format(
			"本期共组织%d场项目经理在线练习，场均%d人参与，峰值参与人数达到%d人，场平均分%.2f分，场均满分人数%d人，场均满分人数占比%.2f%%。",
			model.getExamStat().size(),
			model.getExamStat().stream().mapToInt(a -> a.getCnt()).sum() / model.getExamStat().size(),
			model.getExamStat().stream().mapToInt(a -> a.getCnt()).max().getAsInt(),
			model.getExamStat().stream().mapToDouble(a -> a.getAvgScre()).sum() / model.getExamStat().size(),
			model.getExamStat().stream().mapToInt(a -> a.getCnt100()).sum() / model.getExamStat().size(),
			model.getExamStat().stream().mapToDouble(a -> a.getRate100()).sum() / model.getExamStat().size() * 100.0
		));
		
		// para31 para32
		model.getGrpStat().sort((a, b) -> (int)(b.getPtpnRate() * 100.00 - a.getPtpnRate() * 100.0));
		List<String> lisSnip3 = model.getGrpStat().stream().map(a -> String.format("%s参与率为%.0f%%", a.getGrpNam(), a.getPtpnRate() * 100.0)).collect(Collectors.toList());

		context.setVariable("para31", String.format(
				"经统计各职能组参与线上练习参与率：%s。", String.join("，", lisSnip3)
		));
		context.setVariable("para32", String.format("整体参与率为%.0f%%，",
			model.getGrpStat().stream().mapToDouble(a -> a.getPtpnRate() * a.getTotCnt()).sum() /
				model.getGrpStat().stream().mapToDouble(a -> a.getTotCnt()).sum() * 100.0
		));

		// para41 para42
		List<String> lisSnip4 = model.getUsrAvgScreStat().stream().map(a -> String.format("%s（%s）", a.getUsrNam(), a.getGrpNam())).collect(Collectors.toList());
		
		context.setVariable("para41", String.format(
			"个人平均分排名纳入数据统计的共计%d期练习，为排除小样本统计导致的排名靠前，本次筛选至少参加过%d场练习的人员进行平均分排名。",
			model.getExamStat().size(), (int)(model.getExamStat().size() * 0.8)
		));
		context.setVariable("para42", String.format("位居前五名的分别为%s。", String.join("、", lisSnip4)));
		
		return context;
	}
	
	private byte[] genWeekRptAttachment(WeekRptModel model) throws IOException, InvalidFormatException {
		
		try (
			XWPFDocument doc= new XWPFDocument();
			ByteArrayOutputStream out = new ByteArrayOutputStream()
		) {
			SimpleDateFormat sdf = new SimpleDateFormat("M月d日");
			
			// 项目经理线上练习分析
			{
				// 题目
				XWPFParagraph titlePar = doc.createParagraph();
				titlePar.setAlignment(ParagraphAlignment.CENTER);
				titlePar.setSpacingAfter(31 * 20);
				
				XWPFRun titleRun = titlePar.createRun();
				titleRun.setText(model.getDprtNam() + "项目经理线上练习分析");
				titleRun.setFontFamily("黑体");
				titleRun.setFontSize(20);
			}
			
			// 一、本期线上考试概况
			{
				// 题目
				XWPFParagraph titlePar = doc.createParagraph();
				titlePar.setAlignment(ParagraphAlignment.LEFT);
				
				XWPFRun titleRun = titlePar.createRun();
				titleRun.setText("一、本期线上练习概况");
				titleRun.setFontFamily("仿宋");
				titleRun.setFontSize(18);
				titleRun.setBold(true);
				
				// 自然段
				XWPFParagraph paraPar = doc.createParagraph();
				paraPar.setAlignment(ParagraphAlignment.LEFT);
				paraPar.setIndentationFirstLine(16 * 20 * 2);
				
				XWPFRun paraRun = paraPar.createRun();
				paraRun.setText(String.format("%s技委会项目经理线上考试筹备组于%s至%s，共编写并发布%d期线上练习题目。",
					model.getDprtNam(), sdf.format(model.getFirst()), sdf.format(model.getNow()), model.getExamStat().size()));
				paraRun.setFontFamily("仿宋");
				paraRun.setFontSize(16);
			}
			
			// 二、本期数据分析
			{
				// 题目
				XWPFParagraph titlePar = doc.createParagraph();
				titlePar.setAlignment(ParagraphAlignment.LEFT);
				
				XWPFRun titleRun = titlePar.createRun();
				titleRun.setText("二、本期数据分析");
				titleRun.setFontFamily("仿宋");
				titleRun.setFontSize(18);
				titleRun.setBold(true);
				
				// （1）练习数据分布
				{
					// 题目
					XWPFParagraph subTitlePar = doc.createParagraph();
					subTitlePar.setAlignment(ParagraphAlignment.LEFT);
					subTitlePar.setIndentationFirstLine(16 * 20 * 1);
					
					XWPFRun subTitleRun = subTitlePar.createRun();
					subTitleRun.setText("（1）练习数据分布");
					subTitleRun.setFontFamily("仿宋");
					subTitleRun.setFontSize(16);
					subTitleRun.setBold(true);
					
					// 自然段
					XWPFParagraph subParaPar = doc.createParagraph();
					subParaPar.setAlignment(ParagraphAlignment.LEFT);
					subParaPar.setIndentationFirstLine(16 * 20 * 2);
					
					XWPFRun subParaRun = subParaPar.createRun();
					subParaRun.setText(String.format(
						"本期共组织%d场项目经理在线练习，场均%d人参与，峰值参与人数达到%d人，场平均分%.2f分，场均满分人数%d人，场均满分人数占比%.2f%%。",
						model.getExamStat().size(),
						model.getExamStat().stream().mapToInt(a -> a.getCnt()).sum() / model.getExamStat().size(),
						model.getExamStat().stream().mapToInt(a -> a.getCnt()).max().getAsInt(),
						model.getExamStat().stream().mapToDouble(a -> a.getAvgScre()).sum() / model.getExamStat().size(),
						model.getExamStat().stream().mapToInt(a -> a.getCnt100()).sum() / model.getExamStat().size(),
						model.getExamStat().stream().mapToDouble(a -> a.getRate100()).sum() / model.getExamStat().size() * 100.0
					));
					subParaRun.setFontFamily("仿宋");
					subParaRun.setFontSize(16);
					
					// 图表
					XWPFChart subChart = doc.createChart();
					subChart.setTitleText(
						String.format("%s-%s线上练习数据分布", sdf.format(model.getFirst()), sdf.format(model.getNow())));
					subChart.getOrAddLegend().setPosition(LegendPosition.BOTTOM);
					subChart.setChartHeight(3000000L);
					subChart.setChartWidth(5000000L);
					
					XDDFCategoryAxis bottomAxis = subChart.createCategoryAxis(AxisPosition.BOTTOM);
					XDDFValueAxis leftAxis = subChart.createValueAxis(AxisPosition.LEFT);
					leftAxis.setMinimum(0);
					XDDFChartData subChartData = subChart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
					
					// x
					XDDFDataSource<String> dsDate = XDDFDataSourcesFactory.fromArray(
						model.getExamStat().stream().map(a -> sdf.format(a.getBgnTime()))
						.collect(Collectors.toList()).toArray(new String[0]));
					
					// y
					XDDFNumericalDataSource<Integer> dsCnt = XDDFDataSourcesFactory.fromArray(
						model.getExamStat().stream().map(a -> a.getCnt())
						.collect(Collectors.toList()).toArray(new Integer[0]));
					subChartData.addSeries(dsDate, dsCnt).setTitle("参与人数", null);
					
					XDDFNumericalDataSource<Double> dsAvgScre = XDDFDataSourcesFactory.fromArray(
						model.getExamStat().stream().map(a -> a.getAvgScre())
						.collect(Collectors.toList()).toArray(new Double[0]));
					subChartData.addSeries(dsDate, dsAvgScre).setTitle("平均分", null);
					
					XDDFNumericalDataSource<Integer> dsCnt100 = XDDFDataSourcesFactory.fromArray(
						model.getExamStat().stream().map(a -> a.getCnt100())
						.collect(Collectors.toList()).toArray(new Integer[0]));
					subChartData.addSeries(dsDate, dsCnt100).setTitle("场满分人数", null);
					
					XDDFNumericalDataSource<Double> dsRate100 = XDDFDataSourcesFactory.fromArray(
						model.getExamStat().stream().map(a -> a.getRate100())
						.collect(Collectors.toList()).toArray(new Double[0]));
					subChartData.addSeries(dsDate, dsRate100).setTitle("场满分人数占比", null);
					
					subChart.plot(subChartData);
				}
				
				// （2）各职能组练习参与情况
				{
					// 题目
					XWPFParagraph subTitlePar = doc.createParagraph();
					subTitlePar.setAlignment(ParagraphAlignment.LEFT);
					subTitlePar.setIndentationFirstLine(16 * 20 * 1);
					subTitlePar.setSpacingBefore(16 * 20);
					
					XWPFRun subTitleRun = subTitlePar.createRun();
					subTitleRun.setText("（2）各职能组练习参与情况");
					subTitleRun.setFontFamily("仿宋");
					subTitleRun.setFontSize(16);
					subTitleRun.setBold(true);
					
					// 自然段
					XWPFParagraph subParaPar = doc.createParagraph();
					subParaPar.setAlignment(ParagraphAlignment.LEFT);
					subParaPar.setIndentationFirstLine(16 * 20 * 2);

					model.getGrpStat().sort((a, b) -> (int)(b.getPtpnRate() * 100.00 - a.getPtpnRate() * 100.0));
					List<String> lisSnip = model.getGrpStat().stream().map(a -> String.format("%s参与率为%.0f%%", a.getGrpNam(), a.getPtpnRate() * 100.0)).collect(Collectors.toList());
					
					XWPFRun subParaRun = subParaPar.createRun();
					subParaRun.setText(String.format(
						"经统计各职能组参与线上练习参与率：%s。整体参与率为%.0f%%，后续将进一步调动大家参与的积极性，多加练习。",
						String.join("，", lisSnip),
						model.getGrpStat().stream().mapToDouble(a -> a.getPtpnRate() * a.getTotCnt()).sum() /
							model.getGrpStat().stream().mapToDouble(a -> a.getTotCnt()).sum() * 100.0));
					subParaRun.setFontFamily("仿宋");
					subParaRun.setFontSize(16);
					
					// 图表
					XWPFChart subChart = doc.createChart();
					subChart.setTitleText(
						String.format("%s-%s各职能组练习参与率", sdf.format(model.getFirst()), sdf.format(model.getNow())));
					subChart.setChartHeight(3000000L);
					subChart.setChartWidth(5000000L);
					
					XDDFCategoryAxis bottomAxis = subChart.createCategoryAxis(AxisPosition.BOTTOM);
					XDDFValueAxis leftAxis = subChart.createValueAxis(AxisPosition.LEFT);
					leftAxis.setMinimum(0);
					leftAxis.setMaximum(1);
					XDDFChartData subChartData = subChart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
					
					// x
					XDDFDataSource<String> dsGrpNam = XDDFDataSourcesFactory.fromArray(
						model.getGrpStat().stream().map(a -> a.getGrpNam())
						.collect(Collectors.toList()).toArray(new String[0]));
					
					// y
					XDDFNumericalDataSource<Double> dsPtpnRate = XDDFDataSourcesFactory.fromArray(
						model.getGrpStat().stream().map(a -> a.getPtpnRate())
						.collect(Collectors.toList()).toArray(new Double[0]));
					subChartData.addSeries(dsGrpNam, dsPtpnRate);
					
					subChart.plot(subChartData);
				}
				
				// （3）各职能组练习平均分
				{
					// 题目
					XWPFParagraph subTitlePar = doc.createParagraph();
					subTitlePar.setAlignment(ParagraphAlignment.LEFT);
					subTitlePar.setIndentationFirstLine(16 * 20 * 1);
					subTitlePar.setSpacingBefore(16 * 20);
					
					XWPFRun subTitleRun = subTitlePar.createRun();
					subTitleRun.setText("（3）各职能组练习平均分");
					subTitleRun.setFontFamily("仿宋");
					subTitleRun.setFontSize(16);
					subTitleRun.setBold(true);
					
					// 自然段
					XWPFParagraph subParaPar = doc.createParagraph();
					subParaPar.setAlignment(ParagraphAlignment.LEFT);
					subParaPar.setIndentationFirstLine(16 * 20 * 2);
					
					model.getGrpStat().sort((a, b) -> (int)(b.getAvgScre() - a.getAvgScre()));
					List<String> lisSnip = model.getGrpStat().stream().map(a -> String.format("%s平均分为%.2f", a.getGrpNam(), a.getAvgScre())).collect(Collectors.toList());
					
					XWPFRun subParaRun = subParaPar.createRun();
					subParaRun.setText(
						String.format("经统计各职能组参与线上练习平均分：%s。", String.join("，", lisSnip))
					);
					subParaRun.setFontFamily("仿宋");
					subParaRun.setFontSize(16);
					
					// 图表
					XWPFChart subChart = doc.createChart();
					subChart.setTitleText(
						String.format("%s-%s各职能组练习平均分", sdf.format(model.getFirst()), sdf.format(model.getNow())));
					subChart.setChartHeight(3000000L);
					subChart.setChartWidth(5000000L);
					
					XDDFCategoryAxis bottomAxis = subChart.createCategoryAxis(AxisPosition.BOTTOM);
					XDDFValueAxis leftAxis = subChart.createValueAxis(AxisPosition.LEFT);
					leftAxis.setMinimum(0);
					leftAxis.setMaximum(100);
					XDDFChartData subChartData = subChart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
					
					// x
					XDDFDataSource<String> dsGrpNam = XDDFDataSourcesFactory.fromArray(
						model.getGrpStat().stream().map(a -> a.getGrpNam())
						.collect(Collectors.toList()).toArray(new String[0]));
					
					// y
					XDDFNumericalDataSource<Double> dsAvgScre = XDDFDataSourcesFactory.fromArray(
						model.getGrpStat().stream().map(a -> a.getAvgScre())
						.collect(Collectors.toList()).toArray(new Double[0]));
					subChartData.addSeries(dsGrpNam, dsAvgScre);
					
					subChart.plot(subChartData);
				}
				
				// （4）个人参与度排名
				{
					// 题目
					XWPFParagraph subTitlePar = doc.createParagraph();
					subTitlePar.setAlignment(ParagraphAlignment.LEFT);
					subTitlePar.setIndentationFirstLine(16 * 20 * 1);
					subTitlePar.setSpacingBefore(16 * 20);
					
					XWPFRun subTitleRun = subTitlePar.createRun();
					subTitleRun.setText("（4）个人参与度排名");
					subTitleRun.setFontFamily("仿宋");
					subTitleRun.setFontSize(16);
					subTitleRun.setBold(true);
					
					// 自然段
					XWPFParagraph subParaPar = doc.createParagraph();
					subParaPar.setAlignment(ParagraphAlignment.LEFT);
					subParaPar.setIndentationFirstLine(16 * 20 * 2);
					
					XWPFRun subParaRun = subParaPar.createRun();
					subParaRun.setText(
						String.format(
							"个人参与度排名纳入数据统计的共计%d期考试，排名规则如下：个人参与次数越多排名越靠前，个人参与次数相同的按照个人平均分越高排名越靠前。",
							model.getExamStat().size())
					);
					subParaRun.setFontFamily("仿宋");
					subParaRun.setFontSize(16);
					
					// 表格
					XWPFTable subTable = doc.createTable(model.getUsrCntStat().size() + 1, 5);
					
					XWPFTableRow header = subTable.getRow(0);
					header.setHeight(300);
					
					XWPFTableCell cell0 = header.getCell(0);
					cell0.setWidth("1000");
					cell0.setText("排名");
					
					XWPFTableCell cell1 = header.getCell(1);
					cell1.setWidth("1900");
					cell1.setText("职能组");
					
					XWPFTableCell cell2 = header.getCell(2);
					cell2.setWidth("1900");
					cell2.setText("姓名");
					
					XWPFTableCell cell3 = header.getCell(3);
					cell3.setWidth("1800");
					cell3.setText("参与次数");
					
					XWPFTableCell cell4 = header.getCell(4);
					cell4.setWidth("1800");
					cell4.setText("平均分");
					
					for (int i = 0; i < model.getUsrCntStat().size(); ++i) {
						
						XWPFTableRow row = subTable.getRow(i + 1);
						row.setHeight(300);
						
						row.getCell(0).setText(model.getUsrCntStat().get(i).getRowNum().toString());
						row.getCell(0).setWidth("1000");
						
						row.getCell(1).setText(model.getUsrCntStat().get(i).getGrpNam());
						row.getCell(1).setWidth("1900");
						
						row.getCell(2).setText(model.getUsrCntStat().get(i).getUsrNam());
						row.getCell(2).setWidth("1900");
						
						row.getCell(3).setText(model.getUsrCntStat().get(i).getCnt().toString());
						row.getCell(3).setWidth("1800");
						
						row.getCell(4).setText(String.format("%.2f", model.getUsrCntStat().get(i).getAvgScre()));
						row.getCell(4).setWidth("1800");
					}
				}
				
				// （5）个人平均分排名
				{
					// 题目
					XWPFParagraph subTitlePar = doc.createParagraph();
					subTitlePar.setAlignment(ParagraphAlignment.LEFT);
					subTitlePar.setIndentationFirstLine(16 * 20 * 1);
					subTitlePar.setSpacingBefore(16 * 20);
					
					XWPFRun subTitleRun = subTitlePar.createRun();
					subTitleRun.setText("（5）个人平均分排名");
					subTitleRun.setFontFamily("仿宋");
					subTitleRun.setFontSize(16);
					subTitleRun.setBold(true);
					
					// 自然段
					XWPFParagraph subParaPar = doc.createParagraph();
					subParaPar.setAlignment(ParagraphAlignment.LEFT);
					subParaPar.setIndentationFirstLine(16 * 20 * 2);
					
					List<String> lisSnip = model.getUsrAvgScreStat().stream().map(a -> String.format("%s（%s）", a.getUsrNam(), a.getGrpNam())).collect(Collectors.toList());
					
					XWPFRun subParaRun = subParaPar.createRun();
					subParaRun.setText(String.format(
						"个人平均分排名纳入数据统计的共计%d期练习，为排除小样本统计导致的排名靠前，本次筛选至少参加过%d场练习的人员进行平均分排名。位居前五名的分别为%s。",
						model.getExamStat().size(), (int)(model.getExamStat().size() * 0.8),
						String.join("、", lisSnip)
					));
					subParaRun.setFontFamily("仿宋");
					subParaRun.setFontSize(16);
					
					// 表格
					XWPFTable subTable = doc.createTable(model.getUsrAvgScreStat().size() + 1, 5);
					
					XWPFTableRow header = subTable.getRow(0);
					header.setHeight(300);
					
					XWPFTableCell cell0 = header.getCell(0);
					cell0.setWidth("1000");
					cell0.setText("排名");
					
					XWPFTableCell cell1 = header.getCell(1);
					cell1.setWidth("1900");
					cell1.setText("职能组");
					
					XWPFTableCell cell2 = header.getCell(2);
					cell2.setWidth("1900");
					cell2.setText("姓名");
					
					XWPFTableCell cell4 = header.getCell(3);
					cell4.setWidth("1800");
					cell4.setText("平均分");
					
					XWPFTableCell cell3 = header.getCell(4);
					cell3.setWidth("1800");
					cell3.setText("参与次数");
					
					for (int i = 0; i < model.getUsrAvgScreStat().size(); ++i) {
						
						XWPFTableRow row = subTable.getRow(i + 1);
						row.setHeight(300);
						
						row.getCell(0).setText(model.getUsrAvgScreStat().get(i).getRowNum().toString());
						row.getCell(0).setWidth("1000");
						
						row.getCell(1).setText(model.getUsrAvgScreStat().get(i).getGrpNam());
						row.getCell(1).setWidth("1900");
						
						row.getCell(2).setText(model.getUsrAvgScreStat().get(i).getUsrNam());
						row.getCell(2).setWidth("1900");
						
						row.getCell(3).setText(String.format("%.2f", model.getUsrAvgScreStat().get(i).getAvgScre()));
						row.getCell(3).setWidth("1800");
						
						row.getCell(4).setText(model.getUsrAvgScreStat().get(i).getCnt().toString());
						row.getCell(4).setWidth("1800");
					}
				}
			}
			
			// 结束语
			{
				XWPFParagraph paraPar = doc.createParagraph();
				paraPar.setAlignment(ParagraphAlignment.LEFT);
				paraPar.setIndentationFirstLine(16 * 20 * 2);
				paraPar.setSpacingBefore(16 * 20);
				
				XWPFRun paraRun = paraPar.createRun();
				paraRun.setText("筹备组将持续优化目前的训练方式，提高全体同事的考试热情，为项目经理考试打下坚实基础。");
				paraRun.setFontFamily("仿宋");
				paraRun.setFontSize(16);
				
				XWPFParagraph namePar = doc.createParagraph();
				namePar.setAlignment(ParagraphAlignment.RIGHT);
				namePar.setSpacingBefore(16 * 20);
				
				XWPFRun nameRun = namePar.createRun();
				nameRun.setText(model.getDprtNam() + "项目经理线上考试筹备组");
				nameRun.setFontFamily("仿宋");
				nameRun.setFontSize(16);
				
				XWPFParagraph datePar = doc.createParagraph();
				datePar.setAlignment(ParagraphAlignment.RIGHT);
				
				XWPFRun dateRun = datePar.createRun();
				dateRun.setText(new SimpleDateFormat("yyyy年M月d日").format(model.getNow()));
				dateRun.setFontFamily("仿宋");
				dateRun.setFontSize(16);
			}
			
			doc.write(out);
			out.flush();
			
			return out.toByteArray();
		}
	}	
}
