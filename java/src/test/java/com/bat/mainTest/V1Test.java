package com.bat.mainTest;

import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/5 0005.
 */
public class V1Test {
        //操作根目录
        private final String FILE_DIR_PATH = "D:\\迅雷下载";
        //文件头列表
        private static Map<String,String> FILE_TYPE_MAP = new HashMap<String, String>();

        static
        {
            FILE_TYPE_MAP.put("ffd8ffe000104a464946", "jpg");
            FILE_TYPE_MAP.put("89504e470d0a1a0a0000", "png"); //PNG (png)
            FILE_TYPE_MAP.put("47494638396126026f01", "gif"); //GIF (gif)
            FILE_TYPE_MAP.put("3c21444f435459504520", "html"); //HTML (html)
            FILE_TYPE_MAP.put("3c21646f637479706520", "htm"); //HTM (htm)
            FILE_TYPE_MAP.put("48544d4c207b0d0a0942", "css"); //css
            FILE_TYPE_MAP.put("696b2e71623d696b2e71", "js"); //js
            FILE_TYPE_MAP.put("255044462d312e350d0a", "pdf"); //Adobe Acrobat (pdf)
            FILE_TYPE_MAP.put("504b0304140000000000", "zip");
            FILE_TYPE_MAP.put("526172211a0700cf9073", "rar");
        }
        @Test
        public void testFileInput() throws Exception {

            File dir = new File(FILE_DIR_PATH);

            if(dir.isDirectory()){
                File[] files = dir.listFiles();
                for(File file : files){
                    if(file.isFile()){
                        String val = getTypeOfFile(file);
                        dealFile(file, val);
                    }
                }
            }
        }
        //遍历文件头列表，匹配时处理文件
        private void dealFile(File file, String val) throws IOException {

            Iterator iterator = FILE_TYPE_MAP.keySet().iterator();
            while(iterator.hasNext()){
                String key = (String)iterator.next();
                //if匹配成功
                if(val.contains(key)){
                    //获取该文件头类型的目录
                    File file1 = new File( FILE_DIR_PATH + "\\" + FILE_TYPE_MAP.get(key) );
                    if(!file1.exists())
                        file1.mkdir();
                    //拷贝
                    FileInputStream fileInputStream1 = new FileInputStream(file);
                    copyToPath(fileInputStream1,new FileOutputStream(new File
                            ( FILE_DIR_PATH + "\\" + FILE_TYPE_MAP.get(key)+"\\"+file.getName())) );
                    fileInputStream1.close();
                    //删除原文件
                    file.delete();
                    continue;
                }
            }
        }

        //获得头文件字符串
        private String getTypeOfFile(File file) throws IOException {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] byteps = new byte[10];
            //TODO
            fileInputStream.read(byteps,0,byteps.length);
            fileInputStream.close();
            //转化为16进制
            return bytesToHexString(byteps);
        }

        //流传输
        public boolean copyToPath(InputStream input, OutputStream output) throws IOException {

            StringBuilder stringBuilder = new StringBuilder();
            byte[] bytes = new byte[4096];
            while (input.read(bytes)>0){
                output.write(bytes,0,bytes.length);
            }
            return true;
        }
        //将获得的字节转换为16进制，用来对比文件头表
        public static String bytesToHexString(byte[] src) {
            StringBuilder stringBuilder = new StringBuilder();
            if (src == null || src.length <= 0) {
                return null;
            }
            for (int i = 0; i < src.length; i++) {
                int v = src[i] & 0xFF;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuilder.append(0);
                }
                stringBuilder.append(hv);
            }
            return stringBuilder.toString();
        }


}
