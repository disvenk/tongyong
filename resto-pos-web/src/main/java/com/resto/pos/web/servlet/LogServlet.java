package com.resto.pos.web.servlet;

import com.resto.brand.core.util.LogUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by KONATA on 2017/3/13.
 */
public class LogServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String encoding = System.getProperty("file.encoding");
        req.setCharacterEncoding(encoding);

        String brandName = req.getParameter("brandName");
        brandName = new String(brandName.getBytes(), encoding);
        String fileName = req.getParameter("fileName");
        fileName = new String(fileName.getBytes(), encoding);
        String content = req.getParameter("content");
        content = new String(content.getBytes(), encoding);
        String type = req.getParameter("type");

        LogUtils.writeLog(brandName, fileName, content,type);

    }


}
