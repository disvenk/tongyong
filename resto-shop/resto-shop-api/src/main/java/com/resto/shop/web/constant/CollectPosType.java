package com.resto.shop.web.constant;

/**
 * 同步机制
 */
public class CollectPosType {


    //来福士
    public static final Integer LAIFUSHI=1;
    //伟牛
    public static final Integer VINIU=2;
    //月星环球港
    public static final Integer XIAOMAN=3;
    //余之城
    public static final Integer YUZHICHENG=4;
    //号外小锅鱼
    public static final Integer HAOWAIXIAOGUOYU=5;


    /**
     * 添加店铺id
     * @return
     */
    public static String[] getShopId() {
        String[] shopId = {"b4f6b93e1f744319834534f9bc13418d"};
        return shopId;
    }

    //来福士路径
    public static final String LAIFUSHI_URL="http://139.224.205.143:8082/service-collect-pos/collect";

    //小满品牌id
    public static final String XIAOMAN_BID="e475f6dece0147f8ad2eb230f3d6a252";
    //小满（月星环球港）店铺id
    public static final String XIAOMAN_YXHQG_SID="ca7d4da674eb42b99536e6e948aaab3f";
    //小满（来福士）店铺Id
    public  static final String XIAOMAN_LAIFUSHI="b4f6b93e1f744319834534f9bc13418d";
    //（来福士）、（月星环球港）
    public static final String[] XIAOMAN_SID = {"b4f6b93e1f744319834534f9bc13418d","ca7d4da674eb42b99536e6e948aaab3f"};
    //掌柜小店
    public static final String ZHANGGUI_BID="e475f6dece0147f8ad2eb230f3d6a252";
    //余之城店
    public static final String ZHANGGUI_YUZHICHENG="e475f6dece0147f8ad2eb230f3d6a252";
    //简厨
    public static final String JIANCHU_BID="974b0b1e31dc4b3fb0c3d9a0970d22e4";
    //伟牛(简厨-南丰城店)
    public static final String JIANCHU_VINIU="d9e0d73854e54a738c9bdbd810e78258";
    //号外小锅鱼
    public static final String HAOWAIXIAOGUOYU_BID="25285df83b6a4b3eace544e74ab803dd";
    //号外小锅鱼(店铺)
    public static final String HAOWAIXIAOGUOYU_SID="7787b0d48cb048aca35f324fa53cfb57";
}
