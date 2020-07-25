package cn.nbbandxdd.survey.pubserno.logic;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.nbbandxdd.survey.pubserno.dao.PubSerNoDAO;
import cn.nbbandxdd.survey.pubserno.dao.entity.PubSerNoEntity;

/**
 * <p>公共序列号Logic。
 * 
 * <ul>
 * <li>获取公共序列号，<strong>内部使用接口</strong>，使用{@link #getPubSerNo(String)}。</li>
 * </ul>
 * 
 * @author howcurious
 */
@Component
public class PubSerNoLogic {

	@Autowired
	private PubSerNoDAO pubSerNoDAO;
	
	/**
	 * <p> 获取公共序列号。序列号可以循环使用。
	 * 
	 * @param typ 序列号类型。
	 * 问卷序列号：{@link #cn.nbbandxdd.survey.common.ICommonConstDefine.PUB_SER_NO_EXAM_EXAM_CD}，
	 * 题目序列号：{@link #cn.nbbandxdd.survey.common.ICommonConstDefine.PUB_SER_NO_QUES_QUES_CD}。
	 * @return String 序列号
	 * @throws RuntimeException 序号类型不存在时抛出异常
	 * @see cn.nbbandxdd.survey.common.ICommonConstDefine
	 */
	@Transactional
	public String getPubSerNo(String typ) {
		
		PubSerNoEntity infoEntity = new PubSerNoEntity();
		infoEntity.setSerNoTyp(typ);
		
		PubSerNoEntity entity = pubSerNoDAO.loadByKey(infoEntity);
		if (null == entity) {
			
			throw new RuntimeException(String.format("类型为“%s”的发号器不存在。", typ));
		}
		
		int bgn = entity.getBgnSerNo();
		int cur = entity.getCurSerNo();
		if (cur >= bgn) {
			
			int next = cur + entity.getStpSprd();
			if (next > entity.getEndSerNo()) {
				
				entity.setCurSerNo(bgn);
			} else {
				
				entity.setCurSerNo(next);
			}
			
			pubSerNoDAO.update(entity);
		}
		
		return StringUtils.leftPad(String.valueOf(cur), entity.getFmtOutLen(), '0');
	}
}
