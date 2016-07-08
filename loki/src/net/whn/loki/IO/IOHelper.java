/**
 *Project: Loki Render - A distributed job queue manager.
 *Version 0.7.2
 *Copyright (C) 2014 Daniel Petersen
 *Created on Sep 2, 2009
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
package net.whn.loki.IO;

import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import net.whn.loki.common.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.zip.ZipInputStream;
import net.whn.loki.common.ICommon.FileCacheType;

/**
 * 输入输出助手类
 *
 * @author daniel
 */
public class IOHelper {

    /**
     * 在用户主目录创建loki配置文件夹如果配置不存在的情况下， 还会验证文件夹的读写权限，确保我们处理其他文件系统活动时正常。
     * 子目录'fileCache'也会做同样处理
     *
     * @return Loki文件基础目录File文件对象BaseDir, 如果失败返回null
     */
    public static File setupLokiCfgDir() {
        //基础目录
        File lokiBaseDir;
        //缓存目录
        File fileCacheDir;
        //临时目录
        File tmpDir;
        //配置文件夹名
        String lokiConfDir = ".loki";

        //首先获取用户主目录
        String userHomeDir = System.getProperty("user.home");

        if (userHomeDir != null) {
            lokiBaseDir = new File(userHomeDir, lokiConfDir);
            log.finest(" Loki文件基础目录lokiBaseDir: " + lokiBaseDir.getAbsolutePath());
        } else {
            log.severe("无法获取用户主目录路径!");
            return null;
        }

        //现在检查基础目录是否存在;如果不存在,创建基础目录
        if (!lokiBaseDir.isDirectory()) {
            //如果不存在;创建基础目录
            if (!lokiBaseDir.mkdir()) {
                log.severe("无法创建目录:" + lokiConfDir);
                return null;
            }
        }

        //现在检查基础目录是否可以写入文件和文件夹
        if (!isDirWritable(lokiBaseDir)) {
            log.severe("目录无写入权限: " + lokiConfDir);
            return null;
        }

        fileCacheDir = new File(lokiBaseDir, "fileCache");

        //现在检查缓存目录是否存在;如果不存在,创建缓存目录
        if (!fileCacheDir.isDirectory()) {
            //如果不存在;创建缓存目录
            if (!fileCacheDir.mkdir()) {
                log.severe("无法创建目录:" + fileCacheDir.toString());
                return null;
            }
        }

        if (!isDirWritable(fileCacheDir)) {
            log.severe("目录无写入权限:" + fileCacheDir.toString());
            return null;
        }

        tmpDir = new File(lokiBaseDir, "tmp");

        //如果临时存在目录不, 删除它及其目录下所有内容
        if (tmpDir.isDirectory()) {
            deleteDirectory(tmpDir);
        }

        //创建空的临时文件夹
        if (!tmpDir.mkdir()) {
            log.severe("无法创建目录:" + tmpDir.toString());
            return null;
        }

        if (!isDirWritable(tmpDir)) {
            log.severe("目录无写入权限:" + tmpDir.toString());
            return null;
        }

        //所有检查完毕, 返回
        return lokiBaseDir;
    }

    public static void deleteRunningLock(File lokiCfgDir) {
        File runningFile = new File(lokiCfgDir, ".runningLock");
        if (runningFile.exists()) {
            runningFile.delete();
        }
    }

    /**
     * generates MD5 for given file.
     *
     * @param oFile
     * @return md5 as hex string, or null if failed
     * @throws IOException
     */
    public static String generateMD5(File oFile) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        InputStream inFile = null;
        MessageDigest digest = null;

        try {
            digest = MessageDigest.getInstance("MD5");
            inFile = new FileInputStream(oFile);

            int amountRead;
            while (true) {
                amountRead = inFile.read(buffer);
                if (amountRead == -1) {
                    break;
                }
                digest.update(buffer, 0, amountRead);
            }
            return binToHex(digest.digest());

        } catch (NoSuchAlgorithmException ex) { //TODO what to do?
            log.severe("md5 algorithm not available!");
        } catch (FileNotFoundException ex) {    //TODO what to do?
            log.severe("file not found: " + ex.getMessage());
        } finally {
            if (inFile != null) {
                inFile.close();
            }
        }
        return null;
    }

    public static long getFileCacheSize(
            ConcurrentHashMap<String, ProjFile> fCMap) {
        long total = 0;
        ProjFile currentpFile;
        Iterator it = fCMap.entrySet().iterator();
        Map.Entry pair;
        while (it.hasNext()) {
            pair = (Map.Entry) it.next();
            currentpFile = (ProjFile) pair.getValue();
            total += currentpFile.getSize();
        }
        return total;
    }

    /**
     * 设置运行锁
     *
     * @param lokiCfgDir 配置目录
     * @return 如果 .runningLock 文件不存在返回false ；否则true，程序已经运行
     * @throws IOException
     */
    public static boolean setupRunningLock(File lokiCfgDir) throws IOException {
        File runningLock = new File(lokiCfgDir, ".runningLock");

        if (runningLock.createNewFile()) {
            runningLock.deleteOnExit();
            return false;
        } else {
            return true;   //哎呀; loki已经在运行
        }
    }

    /**
     * zips up a given directory. skips subdirectories!
     *
     * @param dir
     * @param outputZipFile
     * @return
     */
    public static boolean zipDirectory(File dir, File outputZipFile) {
        if (outputZipFile.exists()) {
            outputZipFile.delete();
            log.warning(outputZipFile.toString() + " already exists: overwriting");
        }
        IOHelper.start = System.currentTimeMillis();
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputZipFile));
            out.setLevel(1);
            addDir(dir, out);
            out.close();
            log.info("zipped blendcache in (ms): " + (System.currentTimeMillis() - IOHelper.start));
            return true;
        } catch (Exception ex) {
            log.warning("failed to zip blendcache directory: " + ex.toString());
        }
        return false;
    }

    /**
     * creates the output directory, and unzips files contents into it.
     *
     * @param zipFile contains contents to be unzipped
     * @param outputDir the directory to create and unzip contents into
     * @return true if succeeds, false otherwise
     */
    public static boolean unzipDirectory(File zipFile, File outputDir) {
        try {
            if (!zipFile.isFile()) {
                return false;
            }
            //create directory
            if (!outputDir.mkdir()) {
                return false;
            }

            start = System.currentTimeMillis();
            ZipInputStream zin
                    = new ZipInputStream(new FileInputStream(zipFile));
            FileOutputStream fos = null;

            ZipEntry zipEntry;
            File fileEntry;
            while ((zipEntry = zin.getNextEntry()) != null) {
                int read;
                byte[] buffer = new byte[4096];

                fileEntry = new File(outputDir, zipEntry.getName());
                fos = new FileOutputStream(fileEntry);
                while ((read = zin.read(buffer)) > 0) {
                    fos.write(buffer, 0, read);
                }
                fos.close();
            }

            zin.close();

            return true;
        } catch (Exception ex) {
            log.warning("failed to unpack zip file: " + zipFile.toString());
        }
        return false;
    }

    /**
     * 删除目录及其所含内容
     *
     * @param dir 待删除目录
     * @return 成功标志
     */
    public static boolean deleteDirectory(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);//递归删除目录下文件
                } else {
                    files[i].delete();
                }
            }
        }
        return (dir.delete());//删除目录
    }

    public static String generateBlendCacheDirName(String blendFileName) {
        int dotIndex = blendFileName.lastIndexOf('.');

        if (dotIndex != -1) {
            blendFileName = blendFileName.substring(0, dotIndex);
        }

        return "blendcache_" + blendFileName;
    }

    /*PROTECTED*/
    protected static final int BUFFER_SIZE = 8192;
    protected static long start;

    /**
     * converts bytes to a hex string
     *
     * @param bytes
     * @return
     */
    protected static String binToHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }

    /**
     * adds a previously copied tmp file into the cache (if it's unique)
     *
     * @param fileCacheMap
     * @param md5
     * @param lokiCacheDir
     * @param tmpCacheFile
     */
    protected static void addTmpToCache(FileCacheType fcType,
            ConcurrentHashMap<String, ProjFile> fileCacheMap,
            String md5, File lokiCacheDir, File tmpCacheFile, Config cfg)
            throws IOException {

        File md5File = null;
        ProjFile pFile = null;

        //check if file already exists in cache
        if (!fileCacheMap.containsKey(md5)) {
            log.finest("unique md5; adding to cache");
            //new file, so rename and add to map:

            //rename file
            if (fcType == FileCacheType.BLEND) {
                md5File = new File(lokiCacheDir, md5 + ".blend");
            } else {
                md5File = new File(lokiCacheDir, md5);
            }

            if (md5File.exists()) {
                log.warning("fileCache key set is out of sync w/ files:\n"
                        + "File: " + md5File.getAbsolutePath()
                        + " already exists; overwriting.");
                md5File.delete();
            }
            int limit = 0;
            while (!tmpCacheFile.renameTo(md5File)) {
                if (limit > 9) {
                    log.severe("failed to rename CacheFile: "
                            + tmpCacheFile.getAbsolutePath() + " to "
                            + md5File.getAbsolutePath());
                    throw new IOException("failed to rename CacheFile!");
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    break;
                }
                limit++;
            }

            //create new ProjFile object
            pFile = new ProjFile(fcType, md5File, md5);

            //insert it into the fileCache
            fileCacheMap.put(md5, pFile);

            if (pFile.getSize() > cfg.getCacheSizeLimitBytes()) {
                cfg.setCacheSizeLimitBytes((pFile.getSize() * 4));
                log.info("increasing cache limit to: " + pFile.getSize() * 4);
            }
            manageCacheLimit(fileCacheMap, cfg);
        } else {
            //file is already in cache, so delete tmp file
            tmpCacheFile.delete();
            //that's all we need to do. md5 string will be returned and
            //placed in job
            log.finest("md5 not unique; we already have the file!");
        }
    }

    /*PRIVATE*/
    //logging
    private static final String className
            = "net.whn.loki.common.LokiFileHelper";
    private static final Logger log = Logger.getLogger(className);

    /**
     * should be called after a new file has been added to the cache. if we're
     * over the limit, should iteratively remove oldest used files until we meet
     * the limit constraint.
     */
    private static void manageCacheLimit(
            ConcurrentHashMap<String, ProjFile> fileCacheMap, Config cfg) {

        //we need to delete files using a "longest ago used" algorithm
        //until we fall under the limit
        if (!fileCacheMap.isEmpty()) {
            while (cfg.getCacheSize() > cfg.getCacheSizeLimitBytes()) {
                ProjFile oldestPFile = null;
                Iterator it = fileCacheMap.entrySet().iterator();
                long lowestTime = System.currentTimeMillis() + 1000000;
                Map.Entry pair;

                while (it.hasNext()) {
                    pair = (Map.Entry) it.next();
                    ProjFile currentPFile = (ProjFile) pair.getValue();
                    if (currentPFile.getTimeLastUsed() < lowestTime) {
                        oldestPFile = currentPFile;
                        lowestTime = oldestPFile.getTimeLastUsed();
                    }
                }
                //we now have our delete candidate, so delete it.

                if (!oldestPFile.isInUse()
                        && cfg.getJobsModel().isPFileOrphaned(
                                oldestPFile.getMD5())) {
                    if (!oldestPFile.getProjFile().delete()) {
                        log.severe("failed to delete cache file");
                    }
                    fileCacheMap.remove(oldestPFile.getMD5());
                    log.finer("deleting file: " + oldestPFile.getMD5());
                } else {
                    log.fine("manageCacheLimit wanted to delete file in use!");
                    break;
                }
            }
        }
    }

    /**
     * 判断目录是否具有写入权限
     *
     * @param bDir 基础目录
     * @return 成功标志
     */
    private static boolean isDirWritable(File bDir) {
        //成功标志
        boolean ok = true;
        //临时目录名
        String tDir = "lokiDir";

        try {
            if (!bDir.isDirectory()) {
                ok = false;
            }
            //是否能在当前工作目录写入一个文件?
            File tempFile = new File(bDir, "loki.tmp");

            if (!tempFile.createNewFile()) {
                ok = false; //无法创建文件
            } else //文件创建成功
            if (!tempFile.delete()) {
                ok = false; //无法删除文件
            }

            if (ok) {
                //是否能在当前工作目录写入一个文件夹?
                File tempDir = new File(bDir, tDir);
                if (!tempDir.mkdir()) {
                    ok = false; //无法创建目录
                } else //目录创建成功
                if (!tempDir.delete()) {
                    ok = false; //无法删除目录
                }
            }
        } catch (IOException ex) {
            log.severe("目录无写入权限!" + ex.getMessage());
            ok = false;
        }

        return ok;
    }

    private static void addDir(File dir, ZipOutputStream out) throws IOException {
        File[] files = dir.listFiles();
        byte[] tmpBuf = new byte[4096];
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                //addDir(files[i], out);
                continue;
            }
            FileInputStream in = new FileInputStream(files[i].getAbsolutePath());
            //out.putNextEntry(new ZipEntry(files[i].getAbsolutePath()));
            out.putNextEntry(new ZipEntry(files[i].getName()));
            int len;
            while ((len = in.read(tmpBuf)) > 0) {
                out.write(tmpBuf, 0, len);
            }
            out.closeEntry();
            in.close();
        }
    }

}
