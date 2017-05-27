package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import entity.ApkInfo;


/**
 * 通过ApkInfo 里的applicationIcon从APK里解压出icon图片并存放到磁盘上
 */
public class IconUtil {
	
	/**
	 * 从指定的apk文件里获取指定file的流
	 * @param apkpath
	 * @param fileName
	 * @return
	 */
	public static InputStream extractFileFromApk(String apkpath, String fileName) {
		try {
			ZipFile zFile = new ZipFile(apkpath);
			ZipEntry entry = zFile.getEntry(fileName);
			entry.getComment();
			entry.getCompressedSize();
			entry.getCrc();
			entry.isDirectory();
			entry.getSize();
			entry.getMethod();
			InputStream stream = zFile.getInputStream(entry);
			return stream;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void extractFileFromApk(String apkpath, String fileName, String outputPath) throws Exception {
		System.out.println("fileName:"+fileName);
		InputStream is = extractFileFromApk(apkpath, fileName);
		
		File file = new File(outputPath);
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file), 1024);
		byte[] b = new byte[1024];
		BufferedInputStream bis = new BufferedInputStream(is, 1024);
		while(bis.read(b) != -1){
			bos.write(b);
		}
		bos.flush();
		is.close();
		bis.close();
		bos.close();
	}

	/**
	 * demo 获取apk文件的icon并写入磁盘指定位置
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String apkpath = "F://db.apk";
			ApkInfo apkInfo = new ApkUtil().getApkInfo(apkpath);
			System.out.println(apkInfo);
//			extractFileFromApk(apkpath, apkInfo.getApplicationIcon(), "D:\\DefaultApkTempSaveFolder\\3G安卓市场\\crawler\\icon.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
