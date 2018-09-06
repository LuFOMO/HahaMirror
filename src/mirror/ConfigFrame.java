package mirror;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ConfigFrame extends JFrame {

	/** 
	 * javaʵ����Ƶ��ʽ����Ƶ·��
	 * ͼƬ·����ͼƬ·��
	 * �Լ����������ļ�·���ı���
	 * config.properties����src��Ŀ¼�²�ʹ�����·����ʽ��ȡ
	 */  

	private JFrame conf;
	public static Properties prop = new Properties();
	 
	public void open() throws IOException {
		// TODO Auto-generated method stub
		conf =new JFrame("����");
		conf.setBounds(300, 200, 400, 300);
		conf.getContentPane().setLayout(new GridLayout(1,1));
		conf.setVisible(true);		
		JPanel panel = new JPanel();
		conf.getContentPane().add(panel,new GridLayout(5,2,20,30));
		
		
		JLabel l1 =  new JLabel("�洢��ͼĿ¼");
		panel.add(l1);
		JEditorPane eImgContent = new JEditorPane();
		eImgContent.setSize(250, 30);
		eImgContent.setText(GetProperties("ImgContent"));
		panel.add(eImgContent);
		
		JLabel l2 =  new JLabel("�洢��ƵĿ¼");
		panel.add(l2);
		JEditorPane eVideoContent = new JEditorPane();
		eVideoContent.setSize(250, 30);
		eVideoContent.setText(GetProperties("VideoContent"));
		panel.add(eVideoContent);
		
		JLabel l3 =  new JLabel("��ͼ�����ʽ");
		panel.add(l3);
		JEditorPane eSaveImg = new JEditorPane();
		eSaveImg.setSize(250, 30);
		eSaveImg.setText(GetProperties("SaveImg"));
		panel.add(eSaveImg);
		
		JLabel l4 =  new JLabel("��Ƶ�����ʽ");
		panel.add(l4);
		JEditorPane eSaveVideo = new JEditorPane();
		eSaveVideo.setSize(250, 30);
		eSaveVideo.setText(GetProperties("SaveVideo"));
		panel.add(eSaveVideo);
		
		JButton btnSave = new JButton("����");
		panel.add(btnSave);
		
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					String sic = eImgContent.getText();
					SetProperties("ImgContent", sic);
					String svc = eVideoContent.getText();
					SetProperties("VideoContent", svc);
					String simg = eSaveImg.getText();
					SetProperties("SaveImg",simg);
					String svideo = eSaveVideo.getText();
					SetProperties("SaveVideo", svideo);
					conf.dispose();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		JButton btnCancel = new JButton("ȡ��");
		panel.add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				conf.dispose();
			}
		});
		
		
	}
	public void SetProperties(String k , String p ) throws IOException {
		// TODO Auto-generated method stub
		FileOutputStream outputStream;
		String file=System.getProperty("user.dir")+"\\src\\config.properties";
		try {
			outputStream = new FileOutputStream(file,false);//true ��ʾ׷�Ӵ�
			prop.setProperty(k,p);
			prop.store(outputStream, "config.properties");
			outputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String GetProperties(String k) throws IOException {
		
		InputStream inputStream;
		//���·��
		String file=System.getProperty("user.dir")+"\\src\\config.properties";
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file));
			 prop.load(inputStream);
			 Iterator<String> it = prop.stringPropertyNames().iterator();
			 while(it.hasNext())
			 {
				 String key = it.next();
				 if(key.equals(k))
				 {
					 //System.out.println(prop.getProperty(key));
					 return prop.getProperty(key);
				 }
			 }
			 inputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
		
	}

}
