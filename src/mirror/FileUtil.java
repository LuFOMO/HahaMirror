package mirror;

import java.io.File;
import java.io.IOException;

public class FileUtil {

	//移动文件目录
	public static void moveContent(String aviFileName) throws IOException {
		// TODO Auto-generated method stub
		String path = ConfigFrame.GetProperties("VideoContent");
		String startPath = System.getProperty("user.dir") + File.separator + aviFileName;
		System.out.println("startpath:" + startPath);
		String endPath = path;

		File startFile = new File(startPath);
		File file = new File(endPath);
		if (!file.exists() && !file.isDirectory()) {
			System.out.println("不存在");
			file.mkdir();
		} else {
			delAllFile(endPath);
		}

		if (startFile.renameTo(new File(endPath + startFile.getName()))) {
			System.out.println("File is moved successful!");
			System.out.println("文件移动成功！文件名：《{}》 目标路径：{}" + aviFileName + endPath);
		} else {
			System.out.println("File is failed to move!");
			System.out.println("文件移动失败！文件名：《{}》 起始路径：{}" + aviFileName + startPath);
		}

	}

	// 删除指定文件夹下所有文件
	// param path 文件夹完整绝对路径
	public static void delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				flag = true;
			}
		}

	}
	//删除知道文件
	public static void delFile(String name) {
		
		File file = new File(name);
		if (file.isFile()&&file.exists()) {
			file.delete();
			System.out.println("success:");
		}
		else
		{
			System.out.println("error:");
		}
	
	
	}
}
