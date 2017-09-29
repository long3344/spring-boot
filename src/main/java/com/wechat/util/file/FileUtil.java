package com.wechat.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 描述：文件操作工具类
 * 作者: TWL
 * 创建日期: 2017/9/29
 */
public class FileUtil {

    /**
     * 使用NIO进行快速的文件拷贝
     * @param in
     * @param out
     */
    public static void fileCopy(File in,File out)throws IOException{

        FileChannel inChannel=new FileInputStream(in).getChannel();
        FileChannel outChannel=new FileInputStream(out).getChannel();

        try {
            // inChannel.transferTo(0,inChannel.size(),outChannel);在Windows上面大的文件可能有问题
            //magic number for windows,64Mb -32kb

            int maxCount =(64*1024*1024)-(32*1024);
            long size =inChannel.size();
            long position=0;
            while (position<size){
                position+=inChannel.transferTo(position,maxCount,outChannel);
            }
        } finally {
            if (inChannel!=null){
                inChannel.close();
            }
            if (outChannel!=null){
                outChannel.close();
            }
        }

    }
}
