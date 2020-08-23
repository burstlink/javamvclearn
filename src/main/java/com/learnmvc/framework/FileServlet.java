package com.learnmvc.framework;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet(urlPatterns = { "/favicon.ico", "/static/*" })
public class FileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext ctx = req.getServletContext();
        // 根据request中的url保护的contextPath需要去掉
        String urlPath = req.getRequestURI().substring(ctx.getContextPath().length());
        // 真实路径
        String filepath = ctx.getRealPath(urlPath);
        if (filepath == null) {
            // 无法获取路径
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Path path = Paths.get(filepath);
        if (!path.toFile().isFile()) {
            // 文件不存在
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // 根据文件名猜测content-type
        String mine = Files.probeContentType(path);
        if (mine == null) {
            mine = "application/octet-stream";
        }
        resp.setContentType(mine);
        // 读取文件写入Response
        OutputStream outputStream = resp.getOutputStream();
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(filepath))){
            inputStream.transferTo(outputStream);
        }
        outputStream.flush();


    }
}
