/**
 *Project: Loki Render - A distributed job queue manager.
 *Version 0.7.2
 *Copyright (C) 2014 Daniel Petersen
 *Created on Oct 27, 2009
 */
/**
 *This program is free software: you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation, either version 3 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.whn.loki.error;

import java.io.PrintWriter;
import java.io.StringWriter;
import net.whn.loki.common.*;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * 处理未捕获异常类
 *
 * @author daniel
 */
public class DefaultExceptionHandler implements Thread.UncaughtExceptionHandler {
    /**
     * 构造函数
     * @param lForm loki窗体 
     */
    public DefaultExceptionHandler(LokiForm lForm) {
        form = lForm;
    }
    /**
     * 未捕获异常函数
     * @param t 线程
     * @param e 异常
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        JOptionPane.showMessageDialog(form, "Loki遇到错误:\n"
                + e.toString() + "\n请查看日志了解详细信息.\n"
                + "此时最好重启Loki.",
                "Loki Render 错误", JOptionPane.ERROR_MESSAGE);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        log.warning("未捕获异常: " + "\n" + sw.toString());
    }

    //日志记录
    /**
     * log日志记录配置
     */
    private static final String className = "net.whn.loki.common.DefaultExceptionHandler";
    /**
     * log日志记录配置
     */
    private static final Logger log = Logger.getLogger(className);
    /**
     * loki窗体
     */
    private final LokiForm form;

}
