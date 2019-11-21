package com.boiqin.pdf.utils

import android.util.Log
import java.io.*

/**
 * Created by chenbo on 2017/7/2.
 */
object FileUtils {
    private val TAG = FileUtils::class.java.simpleName
    /**
     * * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    fun copyFile(oldPath: String?, newPath: String?) {
        var inStream: InputStream? = null
        var fs: FileOutputStream? = null
        try {
            var bytesum = 0
            var byteread = 0
            val oldfile = File(oldPath)
            if (oldfile.exists()) { //文件存在时
                inStream = FileInputStream(oldPath) //读入原文件
                fs = FileOutputStream(newPath)
                val buffer = ByteArray(1444)
                while (inStream.read(buffer).also { byteread = it } != -1) {
                    bytesum += byteread //字节数 文件大小
                    println(bytesum)
                    fs.write(buffer, 0, byteread)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "copy simple file error")
            e.printStackTrace()
        } finally {
            if (inStream != null) {
                try {
                    inStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (fs != null) {
                try {
                    fs.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    fun copyFolder(oldPath: String, newPath: String) {
        try {
            File(newPath).mkdirs() //如果文件夹不存在 则建立新文件夹
            val a = File(oldPath)
            val file = a.list()
            var temp: File? = null
            for (i in file.indices) {
                temp = if (oldPath.endsWith(File.separator)) {
                    File(oldPath + file[i])
                } else {
                    File(oldPath + File.separator + file[i])
                }
                if (temp.isFile) {
                    var input: FileInputStream? = null
                    var output: FileOutputStream? = null
                    try {
                        input = FileInputStream(temp)
                        output = FileOutputStream(
                            newPath + "/" +
                                    temp.name
                        )
                        val b = ByteArray(1024 * 5)
                        var len: Int
                        while (input.read(b).also { len = it } != -1) {
                            output.write(b, 0, len)
                        }
                        output.flush()
                    } finally {
                        if (output != null) {
                            try {
                                output.close()
                            } catch (e: IOException) {
                                Log.e(TAG, e.toString())
                            }
                        }
                        if (input != null) {
                            try {
                                input.close()
                            } catch (e: IOException) {
                                Log.e(TAG, e.toString())
                            }
                        }
                    }
                }
                if (temp.isDirectory) { //如果是子文件夹
                    copyFolder(
                        oldPath + "/" + file[i],
                        newPath + "/" + file[i]
                    )
                }
            }
        } catch (e: Exception) {
            println("复制整个文件夹内容操作出错")
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    fun decompressZip(
        zipInputStream: InputStream?,
        target: String?
    ): Boolean {
        return FileUtil.decompressZip(zipInputStream, target)
    }
}