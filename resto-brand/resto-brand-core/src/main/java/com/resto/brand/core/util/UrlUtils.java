package com.resto.brand.core.util;

import java.net.URI;

/**
 * Created by disvenk.dai on 2018-06-14 11:52
 */
public class UrlUtils {

    public static URI getIP(URI uri) {
        URI effectiveURI = null;

        try {
            // URI(String scheme, String userInfo, String host, int port, String
            // path, String query,String fragment)
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
        } catch (Throwable var4) {
            effectiveURI = null;
        }

        return effectiveURI;
    }

    /*System.out.println(getIP("http://www.baidu.com/system/verList"));
        System.out.println(getIP("http://WWW.BAIDU.COM/TESE/verList"));
        System.out.println(getIP("http://blog.csdn.net/weasleyqi/article/details/7912647"));
        System.out.println(getIP("https://segmentfault.com/q/1010000000703645"));
        System.out.println(getIP("http://www.cnblogs.com/afarmer/archive/2011/08/29/2158860.html"));
        System.out.println(getIP("http://127.0.0.1/123/1"));
        System.out.println("1:" + getIP("http://127.0.0.1:9040/system/verList"));
        System.out.println("2:"
                + getIP("http://127.0.0.1:9040/system/verList?loginName=1&password=AD07FB25AA2D3A9F96EE12F25E0BE902"));
        System.out.println("3:" + getIP("http://127.0.0.1:9040/"));*/

    //http://www.baidu.com
    //http://WWW.BAIDU.COM
    //http://blog.csdn.net
    //https://segmengfault.com
    //http://127.0.0.1
    //http://127.0.0.1:9040
    //http://127.0.0.1:9094
    //http://127.0.0.1:9094


}
