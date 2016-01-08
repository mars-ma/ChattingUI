package dev.mxw.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileUtil {
	
	public static void copy(String oldPath, String newPath) {
		Logger.e("复制文件 原地址 " + oldPath + " 目标地址 " + newPath);

		File destFile = new File(newPath);
		if (!destFile.getParentFile().exists()) {
			destFile.getParentFile().mkdirs();
		}
		try {
//			if (!destFile.exists()) {
//				destFile.createNewFile();
//			}else{
//			}

			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldPath);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread;
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isFileExist(String path) {
		File file = new File(path);
		return file.exists();
	}
}
