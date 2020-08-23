package com.learnmvc.framework;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ServletLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.Writer;

public class ViewEngine {
    // pebble模板引擎，使用jinja语法
    private final PebbleEngine engine;

    public ViewEngine(ServletContext servletContext) {
        // 定义一个servletloader加载模块
        ServletLoader loader = new ServletLoader(servletContext);
        // 模板编码
        loader.setCharset("UTF-8");
        // 加载前缀，模板必须在改路径目录下
        loader.setPrefix("/WEB-INF/templates");
        // 加载后缀
        loader.setSuffix("");
        // 创建pebble实例
        this.engine = new PebbleEngine.Builder()
                .autoEscaping(true) // 默认打开html字符转义
                .cacheActive(false)
                .loader(loader).build();
    }

    public void render(ModelAndView mv, Writer writer) throws IOException {
        // 查找模板
        PebbleTemplate template = this.engine.getTemplate(mv.view);
        // 渲染
        template.evaluate(writer, mv.model);
    }
}
