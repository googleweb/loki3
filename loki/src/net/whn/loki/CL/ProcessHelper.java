/**
 *Project: Loki Render - A distributed job queue manager.
 *Version 0.7.2
 *Copyright (C) 2014 Daniel Petersen
 *Created on Oct 28, 2009
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
package net.whn.loki.CL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;

/**
 * 进程助手类，用于命令行交互
 *
 * @author daniel
 */
public class ProcessHelper {

    /**
     * 构造函数
     */
    public ProcessHelper(String[] tCL) {
        taskCL = tCL;
    }

    /**
     * 进程执行
     *
     * @return 执行结果
     */
    public String[] runProcess() {
        //结果信息
        String result[] = {"", ""};
        try {
            //初始化执行对象
            //process = Runtime.getRuntime().exec(taskCL);
            process = new ProcessBuilder(taskCL).start();
            
            //创建Callable实现类的实例，使用FutureTask类来包装Callable对象，该FutureTask对象封装了该Callable对象的call方法的返回值
            
            //获取进程执行的标准输出流
            Output stdout = new Output(process.getInputStream());
            FutureTask<String> stdTask = new FutureTask<String>(stdout);
            Thread stdoutThread = new Thread(stdTask);
            stdoutThread.start();

            //获取进程执行的错误输出流
            Output errout = new Output(process.getErrorStream());
            FutureTask<String> errTask = new FutureTask<String>(errout);
            Thread erroutThread = new Thread(errTask);
            erroutThread.start();
            
            //等待进程结束
            process.waitFor();

            //获取执行结果
            result[0] = stdTask.get();
            result[1] = errTask.get();

            process.getOutputStream().close();

        } catch (IOException ex) {
            result[1] = "IOException: " + ex.getMessage();
            log.warning(ex.getMessage());

        } catch (InterruptedException ex) {
            result[1] = "InterruptedException: " + ex.getMessage();
            process.destroy();
            log.fine("finished interruptedException handling");

        } catch (ExecutionException ex) {
            result[1] = "ExecutionException: " + ex.getMessage();
            log.warning(ex.getMessage());
        }

        return result;
    }

    /**
     * log日志记录配置
     */
    private static final String className = "net.whn.loki.CL";
    /**
     * log日志记录配置
     */
    private static final Logger log = Logger.getLogger(className);

    /**
     * 命令行参数
     */
    private final String[] taskCL;
    /**
     * 进程对象
     */
    private Process process;
    
    /**
     * 输出处理对象类
     */
    private class Output implements Callable<String> {

        Output(InputStream is) {
            this.is = is;
        }

        /**
         * 输入流转换为字符串
         * @return 字符串
         */
        @Override
        public String call() {
            try {
                //add utf-8 code
                //utf-8编码处理
                BufferedReader in = new BufferedReader(new InputStreamReader(is, "utf-8"));
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = in.readLine()) != null) {
                    sb.append(line + "\n");
                }
                in.close();

                return sb.toString();

            } catch (IOException ex) {
                log.warning("无法从进程获取输出: " + ex.getMessage());
            }
            return null;
        }

        /**
         * 输入流对象
         */
        private final InputStream is;
    }
}
