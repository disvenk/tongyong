package com.resto.pos.web.android;

/**
 * Created by carl on 2017/2/6.
 */
public class AndroidUnicast extends AndroidNotification {
    public AndroidUnicast(String appkey,String appMasterSecret) throws Exception {
        setAppMasterSecret(appMasterSecret);
        setPredefinedKeyValue("appkey", appkey);
        this.setPredefinedKeyValue("type", "unicast");
    }

    public void setDeviceToken(String token) throws Exception {
        setPredefinedKeyValue("device_tokens", token);
    }

}
