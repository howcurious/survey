package cn.nbbandxdd.survey.ncov.controller.vo;

import lombok.Data;

@Data
public class NCoVStatVO {

    private String dprtNam;

    private String grpNam;

    private Integer onCntI;

    private Integer offCntI;

    private Integer onCntE;

    private Integer offCntE;
}