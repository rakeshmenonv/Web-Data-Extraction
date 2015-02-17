package com.infotop.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.infotop.util.DateTimeUtil;
import net.infotop.util.OperationNoUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

	/**
	 * 上传文件
	 * 
	 * @param file
	 * @param filePath
	 * @return 0：上传失败1：超出大小限制 2：上传成功
	 * @throws IOException
	 */
	public static int uploadFile(MultipartFile file, String filePath,
			String fileName) throws IOException {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		if (file.getSize() > 0) {
			inputStream = file.getInputStream();
//			if (file.getSize() > 100000000) {
//				return 1;
//			}

			if (!(new java.io.File(filePath).isDirectory())) {
				new java.io.File(filePath).mkdirs();
			}

			outputStream = new FileOutputStream(filePath + fileName);

			int readBytes = 0;
			byte[] buffer = new byte[5242880];
			while ((readBytes = inputStream.read(buffer, 0, 5242880)) != -1) {
				outputStream.write(buffer, 0, readBytes);
			}
			outputStream.close();
			inputStream.close();

			return 2;
		}
		return 0;
	}

	/**
	 * 根据路径删除指定的目录或文件，无论存在与否
	 * 
	 * @param sPath
	 *            要删除的目录或文件
	 * @return 删除成功返回 true，否则返回 false。
	 */
	public static boolean DeleteFolder(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 判断目录或文件是否存在
		if (!file.exists()) { // 不存在返回 false
			return flag;
		} else {
			// 判断是否为文件
			if (file.isFile()) { // 为文件时调用删除文件方法
				return deleteFile(sPath);
			} else { // 为目录时调用删除目录方法
				return deleteDirectory(sPath);
			}
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 上传文件重命名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String generateFileName(String fileName) {
		int position = fileName.lastIndexOf(".");
		String extension = fileName.substring(position);
		return OperationNoUtil.getUUID() + extension;
	}
	
	public static int uploadFile1(MultipartFile file, String filePath,
			String fileName) throws IOException {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		if (StringUtils.isNotBlank(fileName)) {
			String ext = "";
			try {
				ext = getExtention(fileName);
			} catch (Exception ex) {
				System.out.println(filePath+fileName);
				return 3;
			}
			if (".png,.jpeg,.gif,.jpg,.bmp,.PNG,.JPEG,.GIF,.JPG,.BMP".contains(ext)) {
				if (file.getSize() > 0) {
					if (file.getSize() > 512000) {
						return 1;
					}
					inputStream = file.getInputStream();

					int total = (int) file.getSize();
					if (!(new java.io.File(filePath).isDirectory())) {
						new java.io.File(filePath).mkdirs();
					}
					outputStream = new FileOutputStream(filePath + fileName);

					int readBytes = 0;
					byte[] buffer = new byte[total];
					int len = buffer.length;
					if (len > total)
						len = total;

					while (total > 0) {
						try {
							readBytes = inputStream.read(buffer, 0, len);
							if (readBytes > 0) {
								outputStream.write(buffer, 0, readBytes);
								total -= readBytes;
								len = buffer.length;
								if (len > total)
									len = total;
							}
						} catch (java.net.SocketTimeoutException ste) {
							System.out.println(DateTimeUtil.nowTimeStr() + ":"
									+ ste.toString());
						} catch (Exception ste) {
							System.out.println(DateTimeUtil.nowTimeStr() + ":"
									+ ste.toString());
							break;
						}
					}

					// try {
					// while ((readBytes = inputStream.read(buffer, 0, 512000))
					// !=
					// -1) {
					// outputStream.write(buffer, 0, readBytes);
					// }
					// } catch (java.net.SocketTimeoutException ste) {
					// System.out.println(ste.toString());
					// } catch (Exception ste) {
					// System.out.println(ste.toString());
					// }
					outputStream.close();
					inputStream.close();

					return 2;
				}
			} else {
				return 3;
			}
		}
		return 0;
	}

	/**
	 * 获得文件后缀名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getExtention(String fileName) {
		int pos = fileName.lastIndexOf(".");
		return fileName.substring(pos);
	}

}
