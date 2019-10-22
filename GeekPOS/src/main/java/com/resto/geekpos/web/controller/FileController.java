package com.resto.geekpos.web.controller;

import com.resto.geekpos.web.constant.DictConstants;
import com.resto.geekpos.web.model.MobilePackageBean;
import com.resto.geekpos.web.utils.ApiUtils;
import com.resto.geekpos.web.utils.AppUtils;
import com.resto.geekpos.web.utils.ServletUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * Created by carl on 2016/9/9.
 */
@Controller
@RequestMapping("file")
public class FileController extends GenericController {

    @RequestMapping("download")
    public void download(HttpServletRequest request, HttpServletResponse response){
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        OutputStream fos = null;
        InputStream fis = null;
        String result = "";
        File downFiles = new File(request.getSession().getServletContext().getRealPath("")+"\\WEB-INF\\file\\H5B1AD84B99_0908144820.apk");
        //File downFiles = new File("D:\\ideaworkspace\\GeekPOS\\resto-geekpos-web\\src\\main\\webapp\\WEB-INF\\file\\H5B1AD84B99_0908144820.apk");
        if(!downFiles.exists()){
            return;
        }
        try {
            fis = new FileInputStream(downFiles);
            bis = new BufferedInputStream(fis);
            fos = response.getOutputStream();
            bos = new BufferedOutputStream(fos);
            ServletUtils.setFileDownloadHeader(request,response, "H5B1AD84B99_0908144820.apk");
            int byteRead = 0;
            byte[] buffer = new byte[8192];
            while((byteRead=bis.read(buffer,0,8192))!=-1){
                bos.write(buffer,0,byteRead);
            }
            bos.flush();
            fis.close();
            bis.close();
            fos.close();
            bos.close();
        } catch (Exception e) {
            log.info("下载失败了......");
        }
    }

}
