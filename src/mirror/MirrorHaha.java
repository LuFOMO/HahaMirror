package mirror;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.media.Player;
import javax.swing.event.*;
import javax.swing.event.ListSelectionListener;
import java.awt.Component;

public class MirrorHaha {

	private JFrame frame;
	ConfigFrame configFrame;
	public static Player player = null; // ����һ��player������ player��media���е�һ����
	private static Component component = null;
	private static Component transComponent = null;
	RecordVideo r;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MirrorHaha window = new MirrorHaha();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MirrorHaha() {
		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(500, 300, 623, 325);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JPanel panel_2 = new JPanel();
		frame.getContentPane().add(panel_2, BorderLayout.CENTER);

		JButton btn1 = new JButton("\u6355\u83B7\u89C6\u9891");// ��ʼ����
		panel.add(btn1);
		btn1.addActionListener(new ActionListener() {
			// ������Ƶ����
			@Override
			public void actionPerformed(ActionEvent e) {
				component = RecordVideo.startplay();
				if (component != null) {
					panel_2.add(component);
					frame.pack();

					btn1.setEnabled(false);
				}
			}
		});

		JButton btn3 = new JButton("\u622A\u56FE");
		panel.add(btn3);// ��ͼ
		btn3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (player == null && component == null) {
					System.out.println("δ��������ͷ����");
				} else {
					try {
						String savepath = ConfigFrame.GetProperties("ImgContent");
						String format = ConfigFrame.GetProperties("SaveImg");
						RecordVideo.capture(savepath, format);
						System.out.println("capture success:" + savepath);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			}
		});
		JButton btn4 = new JButton("\u5F55\u5236\u89C6\u9891");
		panel.add(btn4);// ��ʼ¼��
		btn4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (player == null && component == null) {
					System.out.println("δ��������ͷ����");
				} else {
					r = new RecordVideo();
					btn4.setEnabled(false);
					r.start();
				}
			}
		});
		JButton btn2 = new JButton("\u505C\u6B62\u5F55\u5236");
		panel.add(btn2);// ����¼��
		btn2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				if (player == null && component == null) {
					System.out.println("δ��������ͷ����");
				} else {
					r.stop();
					PicToAviUtil avi = new PicToAviUtil();
					try {
						avi.convertPicToAvi();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						System.out.println("error");
					}
					btn4.setEnabled(true);
				}

			}
		});
		JButton btn5 = new JButton("\u8BBE\u7F6E");// ����
		panel.add(btn5);
		btn5.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				configFrame = new ConfigFrame();
				try {
					configFrame.open();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.WEST);

		JLabel label = new JLabel("\u8BF7\u9009\u62E9\u7279\u6548");//ѡ��任��Ч
		panel_1.setLayout(new BorderLayout(0, 0));
		panel_1.add(label, BorderLayout.NORTH);

		final JList<String> list = new JList<String>();
		list.setPreferredSize(new java.awt.Dimension(150, 150));
		list.setListData(new String[] { "��Ӱ��Ч", "����������Ч", "������͹��Ч", "���Ϲ�����", "�����ڰ���Ч" });

		panel_1.add(list, BorderLayout.CENTER);

		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub

				if (component == null) {
					System.out.println("comp==null");
				} else {
					int i = list.getSelectedIndex();
					System.out.println(i);
					transComponent = RecordVideo.transform(i);
					if (transComponent != null) {
						panel_2.removeAll();
						panel_2.add(transComponent);
						transComponent.setVisible(false);
						transComponent.setVisible(true);
					}
					frame.pack();

				}

			}

		});

	}

}
