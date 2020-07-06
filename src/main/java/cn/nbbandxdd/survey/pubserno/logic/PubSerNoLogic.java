package cn.nbbandxdd.survey.pubserno.logic;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.nbbandxdd.survey.pubserno.dao.PubSerNoDAO;
import cn.nbbandxdd.survey.pubserno.dao.entity.PubSerNoEntity;

@Component
public class PubSerNoLogic {

	@Autowired
	private PubSerNoDAO pubSerNoDAO;
	
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
