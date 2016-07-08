/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.whn.loki.common;

import net.whn.loki.error.DefaultExceptionHandler;
import net.whn.loki.IO.IOHelper;
import net.whn.loki.CL.CLHelper;
import java.awt.Point;
import java.io.File;
import net.whn.loki.master.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import net.whn.loki.grunt.*;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import net.whn.loki.common.ICommon.LokiRole;
import net.whn.loki.error.ErrorHelper;

/**
 * Loki主程序
 *
 * @author daniel
 */
public class Main {

    /**
     * 程序入口
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        //创建Loki程序窗体
        LokiForm lokiForm = null;
        //手工主地址
        manualMasterIP = null;
        //自动发现主地址开启
        autoDiscoverMaster = true;

        //处理命令行下参数
        String blenderExe = handleArgs(args);

        //有blender参数输入，为分支命令行模式
        if (blenderExe != null) {
            gruntcl = true;
        }

        //这是loki配置文件夹
        lokiCfgDir = IOHelper.setupLokiCfgDir();

        if (!gruntcl) {//非分支命令行模式
            lokiForm = new LokiForm();//创建loki窗体
            setNativeLAF();//设置图形界面外观
        }

        //系统未捕获异常处理，如进程运行后再建立新的进程抛出的异常
        defaultHandler = new DefaultExceptionHandler(lokiForm);
        Thread.setDefaultUncaughtExceptionHandler(defaultHandler);

        try {
            //运行标志
            boolean launch = true;
            //如果程序已经运行
            if (IOHelper.setupRunningLock(lokiCfgDir)) {
                int result = -1;
                if (!gruntcl) {//非命令行模式
                    Object[] options = {"开始", "退出"};
                    result = JOptionPane.showOptionDialog(
                            lokiForm,
                            alreadyRunningText,
                            "已经在运行, 或者非正常关闭",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null,
                            options,
                            options[0]);
                } else {//命令行模式
                    System.out.println("已经在运行或者非正常关闭.\n"
                            + "如果Loki非正常关闭, 你需要\n"
                            + "删除 .loki/.runningLock 文件.");
                }
                log.warning("已经在运行, 或者非正常关闭");

                if (result != 0) {//0为开始
                    launch = false;
                }
            }

            if (launch) {//如果运行
                if (lokiCfgDir == null) {//不存在配置目录
                    String writeMsg
                            = "请授予Loki读写权限到\n"
                            + "目录: '" + System.getProperty("user.home") + "'\n"
                            + "，然后重启Loki.";
                    if (!gruntcl) {//非命令行模式
                        EQCaller.showMessageDialog(lokiForm,
                                "Loki需要写权限",
                                writeMsg, JOptionPane.WARNING_MESSAGE);
                    } else {//命令行模式
                        System.out.println(writeMsg);
                    }

                    log.severe("文件系统不可写. Loki退出.");
                } else {  //文件系统可写，继续
                    //设置日志记录
                    setupLogging();
                    try {
                        cfg = Config.readCfgFile(lokiCfgDir);
                        if (!autoDiscoverMaster) {//非自动发现主地址模式
                            //修改配置主地址及标志
                            cfg.setMasterIp(manualMasterIP);
                            cfg.setAutoDiscoverMaster(autoDiscoverMaster);
                        }
                        if (gruntcl) {//命令行模式
                            cfg.setBlenderBin(blenderExe);
                            System.out.println("Loki分支开始运行在命令行下");
                            System.out.println("尝试连接主地址中......");
                        }
                        startLoki(lokiForm);

                    } catch (IOException ex) {//
                        //fatal error during Announce startup
                        String errMsg
                                = "Loki遇到了一个致命的错误.\n"
                                + "点击确定退出.";

                        ErrorHelper.outputToLogMsgAndKill(lokiForm, gruntcl, log, errMsg, ex);

                        System.exit(-1);
                    }
                }
            }
        } catch (IOException ex) {
            String ioMsg
                    = "Loki Render 存在文件系统的 问题:\n"
                    + ex.getMessage() + "\n点击确定退出.";

            if (!gruntcl) {
                JOptionPane.showMessageDialog(lokiForm, ioMsg);
                lokiForm.dispose();//释放窗体资源
            } else {
                System.out.println(ioMsg);
            }
        }
    }

    /* 私有变量定义 */
    //通用配置
    /**
     * 分支命令行模式
     */
    private static boolean gruntcl = false;//分支命令模式
    private static DefaultExceptionHandler defaultHandler;//默认异常处理器
    private static File lokiCfgDir; //lok配置基础目录
    private static LokiRole myRole;//loki角色
    private static int masterMessageQueueSize = 100;//主分支消息队列值
    private static Config cfg = null;//配置文件对象
    private static String alreadyRunningText = "已经在运行, 或者非正常关闭.如果Loki\n"
            + "没有在运行, 你可以安全的选择'开始'";//已运行提示
    //主要
    private static MasterR master;
    private static Thread masterThread;
    private static MasterForm masterForm;
    //分支
    private static GruntR grunt;
    private static Thread gruntReceiverThread;
    private static GruntForm gruntForm;
    /**
     * 手工配置主地址
     */
    private static InetAddress manualMasterIP;
    /**
     * 手工发现主地址
     */
    private static boolean autoDiscoverMaster;

    //日志记录
    /**
     * log日志记录配置
     */
    private static final String className = "net.whn.loki.common.Main";
    /**
     * log日志记录配置
     */
    private static final Logger log = Logger.getLogger(className);

    /**
     * 
     *
     * @param hiddenForm
     * @throws IOException -startMaster() *FATAL
     */
    private static void startLoki(LokiForm hiddenForm) throws IOException {

        if (!gruntcl) {//非分支命令行模式
            if (cfg.getRole() == LokiRole.ASK) {//未设置角色
                int role = getRole(hiddenForm);//弹出选择角色对话框
                if (role == 0) {
                    myRole = LokiRole.GRUNT;
                } else if (role == 1) {
                    myRole = LokiRole.MASTER;
                } else if (role == 2) {
                    myRole = LokiRole.MASTER_GRUNT;
                } else {
                    log.fine("退出对话框; 退出中.");
                    System.exit(0);
                }
            } else {//以设置从配置中读取
                myRole = cfg.getRole();
            }
        } else {//分支命令行模式
            myRole = LokiRole.GRUNTCL;
        }

        if (myRole == LokiRole.GRUNT || myRole == LokiRole.MASTER_GRUNT) {
            if (!CLHelper.determineBlenderBin(cfg)) {//blender路径是否设置正确
                System.exit(0);
            }
        }

        if (myRole == LokiRole.GRUNT || myRole == LokiRole.GRUNTCL) {
            startGrunt(null, null);
        } else if (myRole == LokiRole.MASTER) {
            startMaster(hiddenForm, false);
        } else if (myRole == LokiRole.MASTER_GRUNT) {
            startMaster(hiddenForm, true);
        } else //窗口关闭
        {
            log.fine("getRole意外的返回:" + myRole + ".退出中.");
            System.exit(0);
        }
    }

    /**
     * loki角色选择
     *
     * @param hiddenForm loki窗体
     * @return 选择的角色
     */
    private static int getRole(LokiForm hiddenForm) {
        String[] options = {"分支", "主干", "主干和分支"};

        int response = JOptionPane.showOptionDialog(hiddenForm,
                "请选择本电脑Loki的角色.",
                "Loki 角色",
                0,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        return response;
    }

    /**
     * 设置图形界面外观
     */
    private static void setNativeLAF() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            log.logp(Level.FINEST, className, "setNativeLAF()", "设置图形界面外观成功.");
        } catch (Exception ex) {
            //我们还可以可以使用丑陋的金属主题...继续使用吧
        }
    }

    /**
     * 
     * @param hiddenForm
     * @param localGrunt
     * @throws IOException -from AnnounceR() *FATAL*
     */
    private static void startMaster(LokiForm hiddenForm, boolean localGrunt) throws IOException {
        AnnouncerR announcer;
        announcer = new AnnouncerR(cfg, hiddenForm);
        master = new MasterR(lokiCfgDir, cfg, announcer, masterMessageQueueSize);

        masterThread = new Thread(master);
        masterThread.setName("master");

        masterForm = new MasterForm(master);
        master.setMasterForm(masterForm);

        masterThread.start();
        masterForm.setVisible(true);

        //we'll have a local grunt so we need to start it and tell it we're here
        if (localGrunt) {
            Point p = masterForm.getLocation();
            Point gPoint = new Point(p.x, (p.y + masterForm.getHeight()));
            startGrunt(master, gPoint);
        }
    }

    /**
     * 
     * @param master
     * @param myPoint 
     */
    private static void startGrunt(MasterR master, Point myPoint) {
        grunt = new GruntR(master, lokiCfgDir, cfg);
        gruntReceiverThread = new Thread(grunt, "grunt");

        if (!gruntcl) {
            gruntForm = new GruntForm(grunt);
            grunt.setGruntForm(gruntForm);

            if (myPoint == null) {
                gruntForm.setLocationRelativeTo(null);
            } else {
                gruntForm.setLocation(myPoint);
            }

            gruntForm.setVisible(true);
        }
        gruntReceiverThread.start();
    }

    /**
     * 设置日志记录
     */
    private static void setupLogging() {
        //日志绝对路径
        String logAbsolutePath;
        //无默认日志配置时
        if (System.getProperty("java.util.logging.config.class") == null && System.getProperty("java.util.logging.config.file") == null) {
            //设置日志路径
            logAbsolutePath = lokiCfgDir.getAbsolutePath() + File.separator + "loki.log";
            try {
                //Logger.getLogger("net.whn.loki").setLevel(Level.ALL);
                //日志文件循环数量，不新增新的日志文件，三个文件增长到设置大小后轮流循环写入，基本文件名中添加 "0"、"1"、"2" 等来依次命名旧文件
                final int LOG_ROTATION_COUNT = 2;
                //单个日志文件大小
                final int fileSize = 500000;
                Handler fHandler = new FileHandler(logAbsolutePath, fileSize, LOG_ROTATION_COUNT, true);//追加模式
                fHandler.setFormatter(new SimpleFormatter());
                Logger.getLogger("").addHandler(fHandler);
            } catch (IOException ex) {
                log.log(Level.SEVERE, "无法创建日志文件处理器", ex);
            }

            /**
             * 设置默认控制台处理器为finest级别
             */
            // 控制台处理器 (如果已经存在则复用)
            Handler consoleHandler = null;
            //查询是否已经存在一个控制台处理器
            for (Handler handler : Logger.getLogger("").getHandlers()) {//遍历所有处理器
                if (handler instanceof ConsoleHandler) {//这个对象是否是这个特定类或者是它的子类的一个实例。
                    //找到控制台处理器
                    consoleHandler = handler;
                    break;
                }
            }

            if (consoleHandler == null) {
                //如果这里没有控制台处理器，则创建新的
                consoleHandler = new ConsoleHandler();
                Logger.getLogger("").addHandler(consoleHandler);
            }
            //设置控制日志处理器的等级FINEST:FINEST 指示一条最详细的跟踪消息
            consoleHandler.setLevel(java.util.logging.Level.FINEST);

            //最后, 设置类的日志记录所有日志
            //log.setLevel(Level.ALL);
        }
    }

    /**
     * 处理命令行下输入的参数
     *
     * @param args 参数
     * @return blender程序路径
     */
    private static String handleArgs(String[] args) {
        String blenderExe = null;
        String usage = "\n用法: java -jar LokiRender-<ver>.jar [<Blender执行路径>] [<主IP地址>]\n\n"
                + "例如:\n"
                + "java -jar LokiRender-071.jar\n"
                + "java -jar LokiRender-071.jar /path/to/blender\n"
                + "java -jar LokiRender-071.jar 192.168.17.45\n"
                + "java -jar LokiRender-071.jar /path/to/blender 192.168.17.45\n\n"
                + "如果输入参数中包含可行性的Blender路径，Loki将会作为分支在命令行模式下启动(无图形用户界面).\n\n";

        if (args.length == 1) {
            //首先检查第一个参数是否是一个合法的IP地址
            if (PreferencesForm.validateIP(args[0])) {
                try {
                    //获取互联网IP地址
                    InetAddress testy = InetAddress.getByName(args[0]);
                    //手动设置主IP
                    manualMasterIP = testy;
                    //关闭自动发现主IP
                    autoDiscoverMaster = false;
                } catch (UnknownHostException uhex) {
                    log.info("请输入一个合法的主IP地址.");
                    System.out.print(usage);
                    System.exit(0);
                }
                //检查是否是一个可执行的blender程序  
            } else if (CLHelper.isBlenderExe(args[0])) {
                blenderExe = args[0];
            } else {
                log.info("无效参数.");
                System.out.print(usage);
                System.exit(1);
            }

        } else if (args.length == 2) {
            if (CLHelper.isBlenderExe(args[0])) {
                blenderExe = args[0];
            } else {
                log.info("非法的blender可执行程序.");
                System.exit(1);
            }

            if (PreferencesForm.validateIP(args[1])) {
                try {
                    InetAddress testy
                            = InetAddress.getByName(args[1]);
                    manualMasterIP = testy;
                    autoDiscoverMaster = false;
                } catch (UnknownHostException uhex) {
                    log.info("请输入一个合法的主IP地址.");
                    System.out.print(usage);
                    System.exit(0);
                }
            } else {
                log.info("无效参数.");
                System.out.print(usage);
                System.exit(0);
            }
        } else if (args.length > 2) {
            System.out.print(usage);
            System.exit(0);
        }
        return blenderExe;
    }
}
