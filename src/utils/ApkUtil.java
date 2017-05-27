package utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;

import org.xmlpull.v1.XmlPullParser;

import android.content.res.AXmlResourceParser;
import android.util.TypedValue;

import com.dangbei.apk.Version;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text;

import entity.ApkInfo;
import entity.ImpliedFeature;

public class ApkUtil {
	public static final String VERSION_CODE = "versionCode";
	public static final String VERSION_NAME = "versionName";
	public static final String SDK_VERSION = "sdkVersion";
	public static final String TARGET_SDK_VERSION = "targetSdkVersion";
	public static final String USES_PERMISSION = "uses-permission";
	public static final String APPLICATION_LABEL = "application-label";
	public static final String APPLICATION_ICON = "application-icon";
	public static final String USES_FEATURE = "uses-feature";
	public static final String USES_IMPLIED_FEATURE = "uses-implied-feature";
	public static final String SUPPORTS_SCREENS = "supports-screens";
	public static final String SUPPORTS_ANY_DENSITY = "supports-any-density";
	public static final String DENSITIES = "densities";
	public static final String PACKAGE = "package";
	public static final String APPLICATION = "application:";
	public static final String LAUNCHABLE_ACTIVITY = "launchable-activity";
	public static final String UMEN_APPKEY = "UMEN_APPKEY";

	private ProcessBuilder mBuilder;
	private ProcessBuilder mBuilder2;

	private static final String SPLIT_REGEX = "(: )|(=')|(' )|'";
	private static final String FEATURE_SPLIT_REGEX = "(:')|(',')|'";
	/**
	 * aapt所在的目录。
	 */
	private String mAaptPath = "lib/aapt";

	private String mImagePath = "D:\\WalleApk";

	public ApkUtil() {
		mBuilder = new ProcessBuilder();
		mBuilder.redirectErrorStream(true);
		mBuilder2 = new ProcessBuilder();
		mBuilder2.redirectErrorStream(true);
	}

	/**
	 * 返回一个apk程序的信息。
	 * 
	 * @param apkPath
	 *            apk的路径。
	 * @return apkInfo 一个Apk的信息。
	 */
	public ApkInfo getApkInfo(String apkPath) throws Exception {
		Process process = mBuilder.command(mAaptPath, "d", "badging", apkPath)
				.start();
		InputStream is = null;
		is = process.getInputStream();
		BufferedReader br = new BufferedReader(
				new InputStreamReader(is, "utf8"));
		String tmp = br.readLine();
		try {
			if (tmp == null || !tmp.startsWith("package")) {
				throw new Exception("参数不正确，无法正常解析APK包。输出结果为:\n" + tmp + "...");
			}
			ApkInfo apkInfo = new ApkInfo();
			apkInfo.setPath(apkPath);
			apkInfo.setMd5(getFileMd5(apkPath));
			String size = FileSizeUtil.getFileSize(new File(apkPath))
					+ "字节("
					+ FileSizeUtil.getFileOrFilesSize(apkPath,
							FileSizeUtil.SIZETYPE_MB) + "MB)";
			apkInfo.setFilesize(size);

			FileSizeUtil.createDir(mImagePath);
			do {
				setApkInfoProperty(apkInfo, tmp);
			} while ((tmp = br.readLine()) != null);
			getChannel(apkInfo, apkPath);
			getUMENkey(apkInfo, apkPath);

			String path = apkInfo.getApplicationIcons().get(
					ApkInfo.APPLICATION_ICON_240);
			String destPath = mImagePath
					+ path.substring(path.lastIndexOf(".") - 2);
			IconUtil.extractFileFromApk(apkPath, path, destPath);
			apkInfo.setApplicationIcon(destPath);
			return apkInfo;
		} catch (Exception e) {
			throw e;
		} finally {
			process.destroy();
			closeIO(is);
			closeIO(br);
		}
	}

	private ApkInfo getChannel(ApkInfo apkInfo, String apkPath) {
		Process process = null;
		InputStream is = null;
		BufferedReader br = null;
		try {
			process = mBuilder.command("java", "-jar", "lib/walle-cli-all.jar",
					"show", apkPath).start();
			is = process.getInputStream();
			br = new BufferedReader(new InputStreamReader(is, "utf8"));
			String tmp = br.readLine();

			if (tmp == null) {
				throw new Exception("参数不正确，无法正常解析APK包。输出结果为:\n" + tmp + "...");
			}
			do {
				apkInfo.setChannel(tmp.substring(tmp.lastIndexOf("=") + 1,
						tmp.length() - 1));
			} while ((tmp = br.readLine()) != null);
			return apkInfo;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			process.destroy();
			closeIO(is);
			closeIO(br);
		}

		return apkInfo;
	}
	public  void getUMENkey(ApkInfo apkInfo,String apkPath){
		
		try {
			IconUtil.extractFileFromApk( apkPath, "lib/armeabi/libdangbeilib.so", mImagePath+"//libdangbeilib.so");

			IconUtil.extractFileFromApk( apkPath, "AndroidManifest.xml", mImagePath+"//AndroidManifest.xml");

			AXmlResourceParser parser=new AXmlResourceParser();
			parser.open(new FileInputStream( mImagePath+"//AndroidManifest.xml"));
			StringBuilder indent=new StringBuilder(10);
			final String indentStep="	";
			while (true) {
				int type=parser.next();
				if (type==XmlPullParser.END_DOCUMENT) {
					break;
				}
				switch (type) {
					case XmlPullParser.START_DOCUMENT:
					{
						break;
					}
					case XmlPullParser.START_TAG:
					{

						indent.append(indentStep);
						
						int namespaceCountBefore=parser.getNamespaceCount(parser.getDepth()-1);
						int namespaceCount=parser.getNamespaceCount(parser.getDepth());
						
						
						for (int i=0;i!=parser.getAttributeCount();++i) {
							if ("UMENG_APPKEY".equals(getAttributeValue(parser,i))) {
//								System.out.println("--------"+getAttributeValue(parser,i+1));
								apkInfo.setUmkey(getAttributeValue(parser,i+1));
							}	
							if ("UMENG_CHANNEL".equals(getAttributeValue(parser,i))) {
								if (apkInfo.getChannel()==null ||apkInfo.getChannel().length()<1
										||apkInfo.getChannel().contains("\\")||apkInfo.getChannel().contains("apk")
										||apkInfo.getChannel().contains(":")||apkInfo.getChannel().contains("Error")) {
									apkInfo.setChannel(getAttributeValue(parser,i+1));
								}
							}
						}
						break;
					}
					case XmlPullParser.END_TAG:
					{
						indent.setLength(indent.length()-indentStep.length());

						break;
					}
					case XmlPullParser.TEXT:
					{
						break;
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	/**
	 * 设置APK的属性信息。
	 * 
	 * @param apkInfo
	 * @param source
	 */
	private void setApkInfoProperty(ApkInfo apkInfo, String source) {

		if (source.startsWith(PACKAGE)) {
			splitPackageInfo(apkInfo, source);
		} else if (source.startsWith(LAUNCHABLE_ACTIVITY)) {
			apkInfo.setLaunchableActivity(getPropertyInQuote(source));
		} else if (source.startsWith(SDK_VERSION)) {
			apkInfo.setSdkVersion(getPropertyInQuote(source));
		} else if (source.startsWith(TARGET_SDK_VERSION)) {
			apkInfo.setTargetSdkVersion(getPropertyInQuote(source));
		} else if (source.startsWith(USES_PERMISSION)) {
			apkInfo.addToUsesPermissions(getPropertyInQuote(source));
		} else if (source.startsWith(APPLICATION_LABEL)) {
			apkInfo.setApplicationLable(getPropertyInQuote(source));
		} else if (source.startsWith(APPLICATION_ICON)) {
			apkInfo.addToApplicationIcons(getKeyBeforeColon(source),
					getPropertyInQuote(source));
		} else if (source.startsWith(APPLICATION)) {
			String[] rs = source.split("( icon=')|'");
			apkInfo.setApplicationIcon(rs[rs.length - 1]);
		} else if (source.startsWith(USES_FEATURE)) {
			apkInfo.addToFeatures(getPropertyInQuote(source));
		} else if (source.startsWith(USES_IMPLIED_FEATURE)) {
			apkInfo.addToImpliedFeatures(getFeature(source));
		} else {
			// System.out.println(source);
		}
	}

	private ImpliedFeature getFeature(String source) {
		String[] result = source.split(FEATURE_SPLIT_REGEX);
		ImpliedFeature impliedFeature = new ImpliedFeature(result[1], result[2]);
		return impliedFeature;
	}

	/**
	 * 返回出格式为name: 'value'中的value内容。
	 * 
	 * @param source
	 * @return
	 */
	private String getPropertyInQuote(String source) {
		int index = source.indexOf("'") + 1;
		return source.substring(index, source.indexOf('\'', index));
	}

	/**
	 * 返回冒号前的属性名称
	 * 
	 * @param source
	 * @return
	 */
	private String getKeyBeforeColon(String source) {
		return source.substring(0, source.indexOf(':'));
	}

	/**
	 * 分离出包名、版本等信息。
	 * 
	 * @param apkInfo
	 * @param packageSource
	 */
	private void splitPackageInfo(ApkInfo apkInfo, String packageSource) {
		String[] packageInfo = packageSource.split(SPLIT_REGEX);
		apkInfo.setPackageName(packageInfo[2]);
		apkInfo.setVersionCode(packageInfo[4]);
		apkInfo.setVersionName(packageInfo[6]);
	}

	/**
	 * 释放资源。
	 * 
	 * @param c
	 *            将关闭的资源
	 */
	private final void closeIO(Closeable c) {
		if (c != null) {
			try {
				c.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private static String getAttributeValue(AXmlResourceParser parser,int index) {
		int type=parser.getAttributeValueType(index);
		int data=parser.getAttributeValueData(index);
		if (type==TypedValue.TYPE_STRING) {
			return parser.getAttributeValue(index);
		}
		if (type==TypedValue.TYPE_ATTRIBUTE) {
			return String.format("?%s%08X",getPackage(data),data);
		}
		if (type==TypedValue.TYPE_REFERENCE) {
			return String.format("@%s%08X",getPackage(data),data);
		}
		if (type==TypedValue.TYPE_FLOAT) {
			return String.valueOf(Float.intBitsToFloat(data));
		}
		if (type==TypedValue.TYPE_INT_HEX) {
			return String.format("0x%08X",data);
		}
		if (type==TypedValue.TYPE_INT_BOOLEAN) {
			return data!=0?"true":"false";
		}
		if (type==TypedValue.TYPE_DIMENSION) {
			return Float.toString(complexToFloat(data))+
				DIMENSION_UNITS[data & TypedValue.COMPLEX_UNIT_MASK];
		}
		if (type==TypedValue.TYPE_FRACTION) {
			return Float.toString(complexToFloat(data))+
				FRACTION_UNITS[data & TypedValue.COMPLEX_UNIT_MASK];
		}
		if (type>=TypedValue.TYPE_FIRST_COLOR_INT && type<=TypedValue.TYPE_LAST_COLOR_INT) {
			return String.format("#%08X",data);
		}
		if (type>=TypedValue.TYPE_FIRST_INT && type<=TypedValue.TYPE_LAST_INT) {
			return String.valueOf(data);
		}
		return String.format("<0x%X, type 0x%02X>",data,type);
	}
	private static String getPackage(int id) {
		if (id>>>24==1) {
			return "android:";
		}
		return "";
	}
	public static float complexToFloat(int complex) {
		return (float)(complex & 0xFFFFFF00)*RADIX_MULTS[(complex>>4) & 3];
	}
	
	private static final float RADIX_MULTS[]={
		0.00390625F,3.051758E-005F,1.192093E-007F,4.656613E-010F
	};
	private static final String DIMENSION_UNITS[]={
		"px","dip","sp","pt","in","mm","",""
	};
	private static final String FRACTION_UNITS[]={
		"%","%p","","","","","",""
	};
	/**
	 * 获取文件的md5值
	 * 
	 * @param path
	 *            文件的全路径名称
	 * @return
	 */
	public static String getFileMd5(String path) {
		try {
			File file = new File(path);
			MessageDigest digest = MessageDigest.getInstance("md5");
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[1024 * 10];
			int len = -1;
			while ((len = fis.read(buffer)) != -1) {
				digest.update(buffer, 0, len);
			}
			byte[] result = digest.digest();
			StringBuffer sb = new StringBuffer();
			for (byte b : result) {
				int number = b & 0xff;
				String str = Integer.toHexString(number);
				if (str.length() == 1) {
					sb.append("0");
				}
				sb.append(str);
			}
			try {

				if (fis != null)
					fis.close();
			} catch (Exception e) {
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static void main(String[] args) {
		try {
			String demo = "F://db.apk";
			if (args.length > 0) {
				if (args[0].equals("-version") || args[0].equals("-v")) {
					System.out.println("ApkUtil   -by Geek_Soledad");
					System.out.println("Version:" + Version.getVersion());
					return;
				}
				demo = args[0];
			}
			ApkInfo apkInfo = new ApkUtil().getApkInfo(demo);
			System.out.println(apkInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getmAaptPath() {
		return mAaptPath;
	}

	public void setmAaptPath(String mAaptPath) {
		this.mAaptPath = mAaptPath;
	}
}
