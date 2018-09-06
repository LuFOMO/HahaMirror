package mirror;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;

import org.jim2mov.core.DefaultMovieInfoProvider;
import org.jim2mov.core.ImageProvider;
import org.jim2mov.core.Jim2Mov;
import org.jim2mov.core.MovieInfoProvider;
import org.jim2mov.core.MovieSaveException;
import org.jim2mov.utils.MovieUtils;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

/**
 * ͼƬ����Ƶת��������
 */
public class PicToAviUtil {

	/**
	 * ��ͼƬת������Ƶ
	 * 
	 * @param jpgDirPath
	 *            jpgͼƬ�ļ��о���·��
	 * @param aviFileName
	 *            ���ɵ�avi��Ƶ�ļ���
	 * @param fps
	 *            ÿ��֡��
	 * @param mWidth
	 *            ��Ƶ�Ŀ��
	 * @param mHeight
	 *            ��Ƶ�ĸ߶�
	 * @throws Exception
	 */
	ConfigFrame conf;

	public void convertPicToAvi() throws Exception {
		// jpgsĿ¼����jpgͼƬ,ͼƬ�ļ���Ϊ(1.jpg,2.jpg...)
		String jpgDirPath = System.getProperty("user.dir") + "\\tem\\";
		String aviFileName = "test.avi"; // ���ɵ�avi��Ƶ�ļ���������·��Ϊ�����̣�
		int fps = 35; // ÿ�벥�ŵ�֡��
		int mWidth = 320; // ��Ƶ�Ŀ��
		int mHeight = 240; // ��Ƶ�ĸ߶�
		final File[] jpgs = new File(jpgDirPath).listFiles();
		if (jpgs == null || jpgs.length == 0) {
			return;
		}

		// ���ļ�����������(��ʾ���ٶ��ļ����е�����ԽС,������Ƶ��֡��Խ��ǰ)
		Arrays.sort(jpgs, new Comparator<File>() {
			public int compare(File file1, File file2) {
				String numberName1 = file1.getName().replace(".jpg", "");
				String numberName2 = file2.getName().replace(".jpg", "");
				return new Integer(numberName1) - new Integer(numberName2);
			}
		});

		// ������Ƶ������
		DefaultMovieInfoProvider dmip = new DefaultMovieInfoProvider(aviFileName);
		// ����ÿ��֡��
		dmip.setFPS(fps > 0 ? fps : 3); // ���δ���ã�Ĭ��Ϊ3
		// ������֡��
		dmip.setNumberOfFrames(jpgs.length);
		// ������Ƶ��͸ߣ������ͼƬ��߱���һֱ��
		dmip.setMWidth(mWidth > 0 ? mWidth : 320); // ���δ���ã�Ĭ��Ϊ320
		dmip.setMHeight(mHeight > 0 ? mHeight : 240); // ���δ���ã�Ĭ��Ϊ240

		try {
			new Jim2Mov(new ImageProvider() {
				public byte[] getImage(int frame) {
					try {
						// ����ѹ����
						return MovieUtils.convertImageToJPEG((jpgs[frame]), 1.0f);
					} catch (IOException e) {
						System.err.println(e);
					}
					return null;
				}
			}, dmip, null).saveMovie(MovieInfoProvider.TYPE_AVI_MJPEG);
		} catch (MovieSaveException e) {
			System.err.println(e);
		}

		System.out.println("create avi success.");

		FileUtil.delAllFile(jpgDirPath);
		changeVideo();//ת����Ƶ��ʽ

	}

	public void changeVideo() throws IOException {
		String format = ConfigFrame.GetProperties("SaveVideo");
		String inputVideo = System.getProperty("user.dir") + "\\test.avi";
		String outVideo = ConfigFrame.GetProperties("VideoContent") + "video." + format;
		ChangeVideo.convert(inputVideo, outVideo);

	}

}
