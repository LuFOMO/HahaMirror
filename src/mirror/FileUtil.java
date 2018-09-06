package mirror;

import java.io.File;
import java.io.IOException;

public class FileUtil {

	//�ƶ��ļ�Ŀ¼
	public static void moveContent(String aviFileName) throws IOException {
		// TODO Auto-generated method stub
		String path = ConfigFrame.GetProperties("VideoContent");
		String startPath = System.getProperty("user.dir") + File.separator + aviFileName;
		System.out.println("startpath:" + startPath);
		String endPath = path;

		File startFile = new File(startPath);
		File file = new File(endPath);
		if (!file.exists() && !file.isDirectory()) {
			System.out.println("������");
			file.mkdir();
		} else {
			delAllFile(endPath);
		}

		if (startFile.renameTo(new File(endPath + startFile.getName()))) {
			System.out.println("File is moved successful!");
			System.out.println("�ļ��ƶ��ɹ����ļ�������{}�� Ŀ��·����{}" + aviFileName + endPath);
		} else {
			System.out.println("File is failed to move!");
			System.out.println("�ļ��ƶ�ʧ�ܣ��ļ�������{}�� ��ʼ·����{}" + aviFileName + startPath);
		}

	}

	// ɾ��ָ���ļ����������ļ�
	// param path �ļ�����������·��
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
				delAllFile(path + "/" + tempList[i]);// ��ɾ���ļ���������ļ�
				flag = true;
			}
		}

	}
	//ɾ��֪���ļ�
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
