package com.resto.api.appraise.util;

/**
 * Created by bruce on 2017/12/5.
 */
public class ProjectConstant {

    //正确code码
    public static final int SUCCESS_CODE = 200;
    //错误code码
    public static final int ERROR_CODE = 500;
    //返回正确的success
    public static final boolean TRUE = true;
    //返回错误的success
    public static final boolean FALSE = false;
    //返回正确的message
    public static final String MESSAGE_OK = "ok";

    public static final String BASE_NAME = "dubbo-service-appraise";

    public static final String COMMENT= BASE_NAME + "/appraiseComment";

    public static final String FILE= BASE_NAME + "/appraiseFile";

    public static final String GRADE= BASE_NAME + "/appraiseGrade";

    public static final String NEW= BASE_NAME + "/appraiseNew";

    public static final String PRAISE= BASE_NAME + "/appraisePraise";

    public static final String OLD= BASE_NAME + "/appraiseOld";

    public static final String STEP= BASE_NAME + "/appraiseStep";

}
