package com.resto.brand.core.payUtil;

import com.alibaba.fastjson.JSONObject;
import com.resto.brand.core.util.DateUtil;
import com.resto.brand.core.util.QRCodeUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/24.
 */
public class PayConfigUtil {

    public static final String RETURNHTML = "<h1>参数错误！</h1>";

    //sms charge(alipay)
    public static final String SMS_ALIPAY_NOTIFY_URL = "paynotify/alipay_notify";

    public static final String  SMS_ALIPAY_RETURN_URL = "paynotify/alipay_return";

    public static final String SMS_SUBJECT = "【餐加】短信充值";

    //sms charge(wechatPay)




    //account charge(alipay)
    public static final String ACCOUNT_ALIPAY_NOTIFY_URL = "account_paynotify/alipay_notify";

    public static final String  ACCOUNT_ALIPAY_RETURN_URL = "account_paynotify/alipay_return";

    public static final String ACCOUNT_SUBJECT = "【餐加】账户充值";

    //account charge(wechatPay)
	public static final String ACCOUNT_WECHAT_NOTIFY_URL = "account_paynotify/wxpay_notify";
	public static final String BODY = "【餐加】账户充值";



	//---------------alipay begin -----------------------
    /**
     * alipay
     * 输出到页面
     * @param body
     * @param response
     */
    public static void outprint(String body,HttpServletResponse response){
        try {
            //页面输出
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(body);
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    //-----------------------alipay end -------------------------------------

	//-----------------------wxpay begin -------------------------------------

	/**
	 * 生成微信支付的页面
	 * @return
	 */
	public static String getWxPayHtml(){
		StringBuffer str = new StringBuffer();
		str.append("<style>.closeBtn{line-height:30px; height:30px; width:163px; color:#ffffff; background-color:#d9534f; font-size:16px; font-weight:normal; font-family:Arial; border:0px solid #dcdcdc; -webkit-border-top-left-radius:3px; -moz-border-radius-topleft:3px; border-top-left-radius:3px; -webkit-border-top-right-radius:3px; -moz-border-radius-topright:3px; border-top-right-radius:3px; -webkit-border-bottom-left-radius:3px; -moz-border-radius-bottomleft:3px; border-bottom-left-radius:3px; -webkit-border-bottom-right-radius:3px; -moz-border-radius-bottomright:3px; border-bottom-right-radius:3px; -moz-box-shadow: inset 0px 0px 0px 0px #ffffff; -webkit-box-shadow: inset 0px 0px 0px 0px #ffffff; box-shadow: inset 0px 0px 0px 0px #ffffff; text-align:center; display:inline-block; text-decoration:none;}.closeBtn:hover{background-color:#c9302c; cursor:pointer;}</style>");
		str.append("<body style='height:100%;overflow:hidden;'>");
		str.append("<div style='position:absolute; left:0; top:0px; width:100%; height:100%; background:#BBB;text-align: center;'>");
		str.append("<img src = 'createWxPayCode' style='margin-top:150px;'>");
		str.append("<p><span style='color:#FFF;'><strong>扫码即可使用微信支付</strong></span></p>");
		str.append("<button class=\"closeBtn\" onclick=\"javascript:window.opener=null;window.open('','_self');window.close();\">关闭页面</button>");
		str.append("</div></body>");
		return str.toString();
	}

	/**
	 * 生成微信支付的 二维码
	 * @param response
	 * @param request
	 */

	public static void createWxPayCode(HttpServletResponse response,HttpServletRequest request){
		FileInputStream fis = null;
		File file = null;
		response.setContentType("image/gif");
		String fileName = System.currentTimeMillis()+"";
		try {
			String content = (String) request.getSession().getAttribute("wxPayCode");
			QRCodeUtil.createQRCode(content, getFilePath(request, null), fileName);
			OutputStream out = response.getOutputStream();
			file = new File(getFilePath(request, fileName));
			fis = new FileInputStream(file);
			byte[] b = new byte[fis.available()];
			fis.read(b);
			out.write(b);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.gc();//手动回收垃圾，清空文件占用情况，解决无法删除文件
			file.delete();
		}
	}

	/**
	 * 得到文件路径
	 * @param request
	 * @param fileName
	 * @return
	 */
	public static String getFilePath(HttpServletRequest request,String fileName){
		String systemPath = request.getServletContext().getRealPath("");
		systemPath = systemPath.replaceAll("\\\\", "/");
		int lastR = systemPath.lastIndexOf("/");
		systemPath = systemPath.substring(0,lastR)+"/";
		String filePath = "qrCodeFiles/";
		if(fileName!=null){
			filePath += fileName;
		}
		return systemPath+filePath;
	}







}
