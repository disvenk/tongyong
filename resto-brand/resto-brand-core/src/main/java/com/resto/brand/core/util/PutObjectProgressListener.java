package com.resto.brand.core.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import com.aliyun.oss.model.PutObjectRequest;

import javax.servlet.http.HttpSession;
import java.io.FileInputStream;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/12/12/0012 16:09
 * @Description:
 */
public class PutObjectProgressListener implements ProgressListener {

    private HttpSession session;
    private long bytesWritten = 0;
    private long totalBytes = -1;
    private boolean succeed = false;
    private int percent = 0;

    //构造方法中加入session
    public PutObjectProgressListener() {
    }
    public PutObjectProgressListener(HttpSession mSession) {
        this.session = mSession;
        session.setAttribute("upload_percent", percent);
    }

    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        long bytes = progressEvent.getBytes();
        ProgressEventType eventType = progressEvent.getEventType();
        switch (eventType) {
            case TRANSFER_STARTED_EVENT:
                System.out.println("Start to upload......");
                break;
            case REQUEST_CONTENT_LENGTH_EVENT:
                this.totalBytes = bytes;
                System.out.println(this.totalBytes + " bytes in total will be uploaded to OSS");
                break;
            case REQUEST_BYTE_TRANSFER_EVENT:
                this.bytesWritten += bytes;
                if (this.totalBytes != -1) {
                    percent = (int) (this.bytesWritten * 100.0 / this.totalBytes);
                    //将进度percent放入session中
                    session.setAttribute("upload_percent", percent);
                    System.out.println(bytes + " bytes have been written at this time, upload progress: " + percent + "%(" + this.bytesWritten + "/" + this.totalBytes + ")");
                } else {
                    System.out.println(bytes + " bytes have been written at this time, upload ratio: unknown" + "(" + this.bytesWritten + "/...)");
                }
                break;
            case TRANSFER_COMPLETED_EVENT:
                this.succeed = true;
                System.out.println("Succeed to upload, " + this.bytesWritten + " bytes have been transferred in total");
                break;
            case TRANSFER_FAILED_EVENT:
                System.out.println("Failed to upload, " + this.bytesWritten + " bytes have been transferred");
                break;
            default:
                break;
        }
    }

    public boolean isSucceed() {
        return succeed;
    }
}
