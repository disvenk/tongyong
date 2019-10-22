package com.resto.geekpos.web.utils;/**
 * Created by user on 2016/3/4.
 */

import com.resto.geekpos.web.model.MobilePackageBean;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * MobileCommunicatePackateUtil
 *
 * @author ken zhao
 * @date 2016/3/4
 */
public class MobileCommunicatePackateUtil {

    /**
     * 报文14位头长度
     */
    private final static int HEAD_LEN = 14;
    private final static int LEN_SIZE = 7;
    private final static int BUFF_SIZE = 8196;
    private final static int PACK_MAX = 65536;
    private final static int ENCRYPT_LEN = 50;
    private final static String EN_CODING = "UTF-8";
    private final static String FORMAT_JSON = "j";
    private final static String SPLIT_STR = "#";
    private final static String FORMAT_XML = "x";
    private final static String FORMAT_HTML5 = "h";
    private final static String FORMAT_FILE = "f";
    private final static int[] REPORT_HEAD = new int[]{5, 1, 7, 1};
    private final static String ERROR_CODE_0000001 = "0000001";//报文不全
    private final static String ERROR_CODE_0000002 = "0000002";//报文解析失败
    private final Logger logger = Logger.getLogger(MobileCommunicatePackateUtil.class);

    public static MobileCommunicatePackateUtil mobileCommunicatePackateUtil = new MobileCommunicatePackateUtil();

    public static MobileCommunicatePackateUtil getInstance() {
        return mobileCommunicatePackateUtil;
    }

    /**
     * 打包并发送
     *
     * @param packate  包内容
     * @param response 响应用户请求
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     */
    public void pack(MobilePackageBean packate, HttpServletResponse response) {
        try {
            response.setContentType("text/json;charset=utf-8");
            response.setCharacterEncoding(EN_CODING);
            response.setHeader("Cache-Control", "max-age=3600");
            response.setHeader("Expires", String.valueOf(System.currentTimeMillis() + 3600));
            //response.setBufferSize(BUFF_SIZE);
            StringBuilder str = new StringBuilder();
            int len = 0;

            len = packate.getContent().toString().length();
//            str.append(getLengthToStr(len));
            str.append(packate.getContent().toString());

            logger.debug("mobilePack [" + packate.getApiMethod() + "]交易请求响应打包完成:" + str.toString());
            String src = str.toString().replaceAll("\r\n", "\\r\\n");
            src = src.replaceAll("\n", "\\n");
            response.setContentLength(src.getBytes(EN_CODING).length);
            response.getWriter().write(src);
            response.getWriter().flush();

        } catch (Exception e) {
            logger.error("mobilePack 发送报文异常：", e);
        }
    }


    private String getLengthToStr(int len) {
        String lenStr = String.valueOf(len);
        int l = lenStr.length();
        for (int i = LEN_SIZE; i > l; i--) {
            lenStr = "0" + lenStr;
        }
        return lenStr;

    }

    private int getUnicodeCount(String str) {

        if (str == null || str.length() == 0) {
            return 0;
        }
        return str.getBytes().length;
    }


    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }

    public MobilePackageBean unpack(HttpServletRequest request) {
        return unpack(request, "mobile");
    }

    /**
     * 接收并解析移动端报文
     *
     * @param request Client请求
     * @return
     * @throws IOException
     */
    public MobilePackageBean unpack(HttpServletRequest request, String attributeName) {
        if (request.getAttribute(attributeName) != null) {
            return (MobilePackageBean) request.getAttribute(attributeName);
        }
        MobilePackageBean mobile = new MobilePackageBean();
        request.setAttribute(attributeName, mobile); // 第一次解析放入 requestAttribute
        BufferedReader reader = null;
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(request.getInputStream(), EN_CODING);
            reader = new BufferedReader(inputStreamReader);
            String[] checks = check(reader);
            char[] b = new char[HEAD_LEN];
            int readLen = reader.read(b);
            String[] headVal = new String[REPORT_HEAD.length];
            if (readLen == HEAD_LEN) {

                String headStr = new String(b);
                logger.debug("mobilePack 请求头:" + headStr);
                int start = 0;
                int end = 0;
                for (int i = 0; i < REPORT_HEAD.length; i++) {
                    end = end + REPORT_HEAD[i];
                    headVal[i] = headStr.substring(start, end);
                    start = start + REPORT_HEAD[i];
                }
                if (!checks[0].equals(headVal[0]) || !checks[2].equals(headVal[2])) {
                    logger.error("mobilePack 验证报文头异常。transcode check[" + checks[0] + "] transcode[" + headVal[0] + "] length check[" + checks[2] + "] length[" + headVal[2] + "] ");
                    throw new IOException("mobilePack 验证报文头异常。");
                }
                mobile.setTransCode(headVal[0]);
                mobile.setPackageFormat(headVal[1]);
                mobile.setLength(Integer.parseInt(headVal[2]));
                if (mobile.getLength() > PACK_MAX) {
                    logger.error("mobilePack 报文传输已超过最大大小:" + PACK_MAX);
                    mobile.setReturnCode(ERROR_CODE_0000002);
                    return mobile;
                }
                mobile.setTerminalType(headVal[3]);
                StringBuilder content = new StringBuilder();
                b = new char[BUFF_SIZE];
                while (content.length() < mobile.getLength()) {
                    readLen = reader.read(b);
                    if (readLen > 0) {
                        content.append(new String(b, 0, readLen));
                    } else {
                        break;
                    }
                }
                logger.debug("mobilePack 读取报文内容为:" + content.toString());

                if (content.length() > 0) {
                    if (FORMAT_JSON.equals(mobile.getPackageFormat())) {
                        String src = content.toString().replaceAll("\r\n", "\\r\\n");
                        src = src.replaceAll("\n", "\\n");
                        Map<String, String> contentVal = JSONUtils.parseJson(src, Map.class);
                        mobile.setContent(contentVal);
                        logger.debug("mobilePack 解析后内容为:" + contentVal.toString());
                    } else {
                        mobile.setContent(content);
                    }
                }
            } else {
                mobile.setReturnCode(ERROR_CODE_0000001);
                logger.error("mobilePack 读取报文头异常。");
            }
        } catch (Exception e) {
            logger.error("mobilePack 解析移动端请求报文失败！", e);
            mobile.setReturnCode(ERROR_CODE_0000002);
        } finally {
            if (inputStreamReader != null) {
                IOUtils.closeQuietly(inputStreamReader);
            }
            if (reader != null) {
                IOUtils.closeQuietly(reader);
            }
        }
        return mobile;
    }


    private String[] check(BufferedReader read) throws IOException {
        char[] b = new char[ENCRYPT_LEN];
        int readLen = read.read(b);

        if (readLen < ENCRYPT_LEN) {
            logger.error("mobilePack 报文校验出错,不是一个有效的移动端请求。");
            return null;
        }
        String encrypt = new String(b);
        logger.debug("mobilePack 加密校验串[" + encrypt + "] ");
//        System.out.println("mobilePack 加密校验串["+encrypt+"] " + readLen);
        encrypt = Encrypter.decrypt(encrypt.trim());
        logger.debug("mobilePack 解密校验串[" + encrypt + "] ");
//        System.out.println("mobilePack 解密校验串["+encrypt+"] " + readLen);
        String[] rst = encrypt.split(SPLIT_STR);
        return rst;
    }


    public static void main(String[] args) {
        try {
            System.out.println(JSONUtils.parseJson("{\"a\":\"\",\"content\":\"\"}", Map.class));
            double a = 100000;
            for (int i = 0; i < 100; i++) {
                a = a + a * 0.08;
                System.out.println(a);
            }
            System.out.println(a);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
