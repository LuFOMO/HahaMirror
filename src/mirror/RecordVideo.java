package mirror;

import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.media.Buffer;
import javax.media.CannotRealizeException;
import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Codec;
import javax.media.ConfigureCompleteEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DataSink;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoDataSourceException;
import javax.media.NoPlayerException;
import javax.media.NoProcessorException;
import javax.media.Player;
import javax.media.Processor;
import javax.media.ProcessorModel;
import javax.media.RealizeCompleteEvent;
import javax.media.UnsupportedPlugInException;
import javax.media.control.FrameGrabbingControl;
import javax.media.control.TrackControl;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.FileTypeDescriptor;
import javax.media.protocol.SourceCloneable;
import javax.media.util.BufferToImage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.media.format.AviVideoFormat;

import jmapps.ui.VideoPanel;

public class RecordVideo implements Runnable {
	private static final long serialVersionUID = -8040938476462674297L;

	// 本地摄像头的注册信息(通过JMF.exe获得)
	static String url = "vfw:Microsoft WDM Image Capture (Win32):0";
	// 获取的摄像头信息
	private static CaptureDeviceInfo captureDeviceInfo = null;
	// 从获取的摄像头信息中提取的摄像头地址
	private static MediaLocator mediaLocator = null;
	// 原始的数据源
	private static DataSource OutputData = null;
	// 由原始数据源转变成的，可以被克隆的数据源
	// private static DataSource cloneableDataSource = null;
	// 由可以克隆的数据源 cloneableDataSource 克隆出来的 clonedDataSource
	private static DataSource clonedDataSource = null;
	// 用来播放的 player
	public static Player player = null;
	public static Player transPlayer = null;
	public static Component comp = null;
	// 处理录制的视频的 Processor
	private static Processor processor = null;
	private static Processor videoProcessor = null;
	// 保存录制数据的数据池(datasink: 数据接收装置; 数据接收器)
	private static DataSink dataSink = null;
	// StateHelper 是处理 Player 或 Processor 的同步状态转换的一个很有用的类。

	// buffer 用于截取实时图像的缓冲区
	private static Buffer buffer = null;
	// 将 buffer 转换为图像的“中间变量”
	private static BufferToImage buffer2image = null;
	// 拍下来的照片赋给img
	private static Image img = null;
	// 保存图片的宽度和高度
	private int imgWidth = 320;
	private int imgHeight = 240;
	static int i = 1;

	static ConfigFrame conf;

	private static String sufix = ".avi";

	private static String capturePath = System.getProperty("user.dir") + File.separator + "Capture" + File.separator;

	private static Processor captureProssor;

	private static String fileTypeDescriptor = FileTypeDescriptor.MSVIDEO;

	private static AviVideoFormat aviFormat = new AviVideoFormat(VideoFormat.YUV);

	private String defaultImageFeomat = "jpg";
	private String format;
	Thread thread;
	private static Format suitableFormat = new Format(VideoFormat.RGB);

	/*
	 * 此 WebCamSwing 函数完成的功能： 创建一个对话框。 将摄像头的物理地址转化为电脑能够识别的 medialocator(媒体定位器) ，
	 * 并且通过 medialocator 产生一个数据源 dataSource ， 通过这个 dataSource 数据源生成 player 用来播放视频。
	 * 然后将数据源进行处理，产生一个能够被克隆的 cloneableDataSource， 再这个 cloneableDataSource 进行克隆，产生一个
	 * clonedDataSource 数据源， 用于后面的 processor 录制视频
	 */
	public RecordVideo() {
		format = defaultImageFeomat;
	}

	public static Component startplay() {
		//视频捕获
		captureDeviceInfo = CaptureDeviceManager.getDevice(url); // CaptureDeviceInfo: 捕获设备信息
		// 根据捕获设备信息(是一份设备的列表) 获取 媒体定位器
		mediaLocator = captureDeviceInfo.getLocator(); // MediaLocator: 媒体定位器
		ContentDescriptor CONTENT_DESCRIPTOR = new ContentDescriptor(ContentDescriptor.RAW);
		Format[] FORMATS = new Format[] { suitableFormat };
		try {
			processor = Manager.createRealizedProcessor(new ProcessorModel(mediaLocator, FORMATS, CONTENT_DESCRIPTOR));
			OutputData = processor.getDataOutput();
			OutputData = Manager.createCloneableDataSource(OutputData);
			clonedDataSource = ((SourceCloneable) OutputData).createClone();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoProcessorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotRealizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			// player 用 cloneableDataSource 数据源，processor 用 clonedDataSource 的数据源
			player = Manager.createRealizedPlayer(OutputData);
			// videoProcessor = Manager.createProcessor(clonedDataSource);
			processor.start();
			player.start();
			// 调用 player.start() 以后就可以用那个一个可视的组件来呈现摄像头拍摄的景象了！
			comp = player.getVisualComponent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return comp;
	}

	/*
	 * 点击拍照 ActionEvent: 指示发生了组件定义的动作的语义事件。 当特定于组件的动作（比如 被按下）发生时， 由组件（比如
	 * Button）生成此高级别事件。 事件被传递给每一个 ActionListener 对象， 这些对 象是使用组件的 addActionListener
	 * 方法注册的，用以接收这类事件。 注：要使用键盘在 Button 上触发 ActionEvent，请使用空格键。
	 */
	public static void capture(String savepath, String format) throws IOException {
		//截图
		File file = new File(savepath);
		if (!file.exists()) {
			file.mkdirs();
		}
		// 取得对 Player 的控制(反射机制的典型应用)
		FrameGrabbingControl fgc = (FrameGrabbingControl) player.getControl("javax.media.control.FrameGrabbingControl");
		buffer = fgc.grabFrame();
		// System.out.println(buffer.getFormat());
		buffer2image = new BufferToImage((VideoFormat) buffer.getFormat());
		img = buffer2image.createImage(buffer);
		String savename = String.valueOf(i) + "." + format;
		// ImageIO.write是将图片保存到上面已经写好的路径里，并且用上面的savename命名
		try {
			ImageIO.write((RenderedImage) img, "GIF", new File(savepath + savename));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		i++;
		img = null;
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void stop() {
		// TODO Auto-generated method stub
		if (thread != null) {
			thread = null;
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Thread thisThread = Thread.currentThread();
		String savepath;
		try {
			savepath = System.getProperty("user.dir") + "\\tem\\";
			while (thread == thisThread) {
				capture(savepath, format);
			}
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 哈哈镜效果变换
	public static Component transform(int i) {

		ContentDescriptor CONTENT_DESCRIPTOR = new ContentDescriptor(ContentDescriptor.RAW);
		TrackControl[] tc = null;
		try {
			videoProcessor = Manager.createProcessor(clonedDataSource);
		} catch (NoProcessorException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (videoProcessor != null) {
			try {
				videoProcessor.configure();
				while (videoProcessor.getState() != videoProcessor.Configured && videoProcessor.getState() != 600) {
					Thread.sleep(1000);
					System.out.println("Configuring is" + videoProcessor.getState());
				}
				System.out.println("Configured is" + videoProcessor.getState());
				if (videoProcessor.getState() == videoProcessor.Configured) {
					videoProcessor.setContentDescriptor(CONTENT_DESCRIPTOR);
					tc = videoProcessor.getTrackControls();
					if (tc.length > 0) {
						Codec[] cd = new Codec[1];
						cd[0] = new HahaEffect(i);//变换类
						try {
							tc[0].setCodecChain(cd);
						} catch (UnsupportedPlugInException e) {
							// TODO: handle exception
							e.toString();
						}
					}
				}
				videoProcessor.realize();

				while (true) {
					if (videoProcessor.getState() == videoProcessor.Realized) {
						break;
					}
					System.out.println("realizing is" + videoProcessor.getState());
				}
				System.out.println("realized is" + videoProcessor.getState());
				transPlayer = Manager.createRealizedPlayer(videoProcessor.getDataOutput());
				videoProcessor.start();
				transPlayer.start();
				comp = transPlayer.getVisualComponent();
				return comp;
			} catch (CannotRealizeException e) {
				// TODO: handle exception
				e.printStackTrace();
			} catch (NoPlayerException e) {
				// TODO: handle exception
				e.printStackTrace();
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
		return null;
	}

}
