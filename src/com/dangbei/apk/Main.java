package com.dangbei.apk;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.dangbei.apk.label.LinkLabel;

import utils.ApkUtil;
import entity.ApkInfo;

public class Main {

	private JFrame frame;
	private JTextField packnameField;
	private JTextField apknameField;
	private JTextField versionCodeField;
	private JTextField versionNameField;
	private JTextField channelField;

	private JLabel iconJLabel;
	private JLabel infoTitleJLable;
	private JLabel packnameJLabel;
	private JLabel versionCodeJLabel;
	private JLabel versionNameJLabel;

	ApkInfo apkInfo = null;
	private JLabel fileTitleJLable;
	private JLabel md5jLabel;
	private JTextField md5Field;
	private JLabel pathJLabel;
	private JLabel sizeJLabel;
	private JTextField sizeField;
	private JTextField pathField;
	private ImageIcon imageIcon;
	private JLabel channelJLabel;
	private JTextField uMeglField;
	private JLabel uMenJLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();

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
	public Main() {
		initialize();
		initDangbei();
	}

	private void initDangbei() {
		
		JLabel tagJLabel = new LinkLabel("软件由世界最大的智能电视网提供www.znds.com","http:www.znds.com","red");
		tagJLabel.setFont(new Font("宋体", Font.PLAIN, 13));
		tagJLabel.setBounds(12, 390, 330, 30);
		frame.getContentPane().add(tagJLabel);
		
		
		ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("/image/dangbei_icon.png"));
		imageIcon.setImage(imageIcon.getImage().getScaledInstance(55, 55,
				Image.SCALE_DEFAULT));
		JLabel iconJLabel = new JLabel(imageIcon);
		iconJLabel.setBounds(10, 5, 55, 55);
		frame.getContentPane().add(iconJLabel);
		
		JPanel line1 = new JPanel();
		line1.setBounds(5, 63, 300, 1);
		line1.setBackground(Color.gray);
		frame.getContentPane().add(line1);
		
		JLabel dbJLabel = new JLabel("当贝市场");
		dbJLabel.setFont(new Font("宋体", Font.BOLD, 15));
		dbJLabel.setForeground(Color.blue);
		dbJLabel.setBounds(70, 6, 100, 30);
		frame.getContentPane().add(dbJLabel);
		

		JLabel dbtgJLabel = new JLabel("<html>--专为智能电视打造的应用市场，千万电视用户的选择</html>");
		dbtgJLabel.setFont(new Font("宋体", Font.BOLD, 12));
		dbtgJLabel.setForeground(Color.blue);
		dbtgJLabel.setBounds(70, 18, 160, 50);
		frame.getContentPane().add(dbtgJLabel);
		
		
		
		JLabel downJLabel = new LinkLabel("点击下载","http:www.dangbei.com","blue");
		downJLabel.setFont(new Font("宋体", Font.PLAIN, 15));
		downJLabel.setBounds(240, 25, 60, 30);
		frame.getContentPane().add(downJLabel);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("APK Helper(walle版)");
		Image image = Toolkit.getDefaultToolkit().getImage(
				this.getClass().getResource("/image/dangbei_icon.png"));
		frame.setIconImage(image);
		frame.setBounds(0, 0, 330, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		frame.getContentPane().setLayout(null);
		drag(frame);

		infoTitleJLable = new JLabel("APK信息");
		infoTitleJLable.setFont(new Font("宋体", Font.PLAIN, 14));
		infoTitleJLable.setBounds(30, 67, 60, 15);
		infoTitleJLable.setForeground(Color.green);

		frame.getContentPane().add(infoTitleJLable);

		JPanel line1 = new JPanel();
		line1.setBounds(85, 76, 210, 1);
		line1.setBackground(Color.gray);
		frame.getContentPane().add(line1);

		packnameJLabel = new JLabel("包名：");
		packnameJLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		packnameJLabel.setBounds(50, 90, 67, 15);
		frame.getContentPane().add(packnameJLabel);

		packnameField = new JTextField();
		packnameField.setBounds(90, 90, 200, 20);
		frame.getContentPane().add(packnameField);
		packnameField.setColumns(20);
		packnameField.setEditable(false);
		packnameField.setBackground(Color.white);

		JLabel apknameJLabel = new JLabel("名称：");
		apknameJLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		apknameJLabel.setBounds(50, 120, 67, 15);
		frame.getContentPane().add(apknameJLabel);

		apknameField = new JTextField();
		apknameField.setBounds(90, 120, 200, 20);
		frame.getContentPane().add(apknameField);
		apknameField.setColumns(20);
		apknameField.setEditable(false);
		apknameField.setBackground(Color.white);

		imageIcon = new ImageIcon(this.getClass().getResource(
				"/image/zndslogo.png"));
		iconJLabel = new JLabel(imageIcon);
		iconJLabel.setBounds(200, 140, 100, 100);
		frame.getContentPane().add(iconJLabel);

		versionCodeJLabel = new JLabel("版本号：");
		versionCodeJLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		versionCodeJLabel.setBounds(36, 150, 67, 15);
		frame.getContentPane().add(versionCodeJLabel);

		versionCodeField = new JTextField();
		versionCodeField.setBounds(90, 150, 100, 20);
		frame.getContentPane().add(versionCodeField);
		versionCodeField.setColumns(20);
		versionCodeField.setEditable(false);
		versionCodeField.setBackground(Color.white);

		versionNameJLabel = new JLabel("内部版本号：");
		versionNameJLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		versionNameJLabel.setBounds(8, 180, 100, 15);
		frame.getContentPane().add(versionNameJLabel);

		versionNameField = new JTextField();
		versionNameField.setBounds(90, 180, 100, 20);
		frame.getContentPane().add(versionNameField);
		versionNameField.setColumns(20);
		versionNameField.setEditable(false);
		versionNameField.setBackground(Color.white);

		channelJLabel = new JLabel("渠道：");
		channelJLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		channelJLabel.setBounds(50, 210, 67, 15);
		frame.getContentPane().add(channelJLabel);

		channelField = new JTextField();
		channelField.setBounds(90, 210, 100, 20);
		frame.getContentPane().add(channelField);
		channelField.setColumns(20);
		channelField.setEditable(false);
		channelField.setBackground(Color.white);
		
		uMenJLabel = new JLabel("友盟key：");
		uMenJLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		uMenJLabel.setBounds(30, 240, 67, 15);
		frame.getContentPane().add(uMenJLabel);

		uMeglField = new JTextField();
		uMeglField.setBounds(90, 240, 210, 20);
		frame.getContentPane().add(uMeglField);
		uMeglField.setColumns(20);
		uMeglField.setEditable(false);
		uMeglField.setBackground(Color.white);

		fileTitleJLable = new JLabel("文件信息");
		fileTitleJLable.setFont(new Font("宋体", Font.PLAIN, 14));
		fileTitleJLable.setBounds(24, 270, 60, 15);
		fileTitleJLable.setForeground(Color.green);
		frame.getContentPane().add(fileTitleJLable);

		JPanel line2 = new JPanel();
		line2.setBounds(85, 280, 220, 1);
		line2.setBackground(Color.gray);
		frame.getContentPane().add(line2);

		md5jLabel = new JLabel("MD5：");
		md5jLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		md5jLabel.setBounds(56, 300, 100, 15);
		frame.getContentPane().add(md5jLabel);

		md5Field = new JTextField();
		md5Field.setBounds(90, 300, 210, 20);
		frame.getContentPane().add(md5Field);
		md5Field.setColumns(20);
		md5Field.setEditable(false);
		md5Field.setBackground(Color.white);

		pathJLabel = new JLabel("文件路径：");
		pathJLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		pathJLabel.setBounds(22, 330, 100, 15);
		frame.getContentPane().add(pathJLabel);

		pathField = new JTextField();
		pathField.setBounds(90, 330, 210, 20);
		frame.getContentPane().add(pathField);
		pathField.setColumns(20);
		pathField.setEditable(false);
		pathField.setBackground(Color.white);

		sizeJLabel = new JLabel("文件大小：");
		sizeJLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		sizeJLabel.setBounds(22, 360, 100, 15);
		frame.getContentPane().add(sizeJLabel);

		sizeField = new JTextField();
		sizeField.setBounds(90, 360, 210, 20);
		frame.getContentPane().add(sizeField);
		sizeField.setColumns(20);
		sizeField.setEditable(false);
		sizeField.setBackground(Color.white);
		
		
	}

	public void fillData() {
		// image.getImage().getScaledInstance(width,height,Image.SCALE_DEFAULT)
		imageIcon = new ImageIcon(apkInfo.getApplicationIcon());
		imageIcon.setImage(imageIcon.getImage().getScaledInstance(90, 90,
				Image.SCALE_DEFAULT));
		iconJLabel.setIcon(imageIcon);

		channelField.setText(apkInfo.getChannel());
		versionNameField.setText(apkInfo.getVersionCode());
		versionCodeField.setText(apkInfo.getVersionName());
		apknameField.setText(apkInfo.getApplicationLable());
		packnameField.setText(apkInfo.getPackageName());
		sizeField.setText(apkInfo.getFilesize());
		md5Field.setText(apkInfo.getMd5());
		pathField.setText(apkInfo.getPath());
		uMeglField.setText(apkInfo.getUmkey());
	}

	public void drag(JFrame panel)// 定义的拖拽方法
	{
		// panel表示要接受拖拽的控件
		new DropTarget(panel, DnDConstants.ACTION_COPY_OR_MOVE,
				new DropTargetAdapter() {
					@Override
					public void drop(DropTargetDropEvent dtde)// 重写适配器的drop方法
					{
						try {
							if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))// 如果拖入的文件格式受支持
							{
								dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);// 接收拖拽来的数据
								List<File> list = (List<File>) (dtde
										.getTransferable()
										.getTransferData(DataFlavor.javaFileListFlavor));
								String path = "";
								for (File file : list)
									path += file.getAbsolutePath();

								apkInfo = new ApkUtil().getApkInfo(path);

								fillData();
								dtde.dropComplete(true);// 指示拖拽操作已完成
							} else {
								dtde.rejectDrop();// 否则拒绝拖拽来的数据
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

}
