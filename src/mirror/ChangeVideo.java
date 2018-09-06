package mirror;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
/** 
 * javaʵ����Ƶ��ʽ��ת�� 
 * 
 */  
public class ChangeVideo {  

    /** 
     * @param inputFile:��Ҫת������Ƶ 
     * @param outputFile��ת�������Ƶ
     * @return 
     * @throws IOException 
     */  
    public static boolean convert(String inputFile, String outputFile) throws IOException {  
        if (!checkfile(inputFile)) {  
            System.out.println(inputFile + " is nokt file");  
            return false;  
        }  
        if (process(inputFile, outputFile)) {  
            System.out.println("ok");  
            return true;  
        }  
        return false;  
    }  
  
    // ����ļ��Ƿ����  
    private static boolean checkfile(String path) {  
        File file = new File(path);  
        if (!file.isFile()) {  
            return false;  
        }  
        return true;  
    }  
  
    /** 
     * @param inputFile 
     * @param outputFile 
     * @return 
     * ת����Ƶ�ļ� 
     * @throws IOException 
     */  
    private static boolean process(String inputFile, String outputFile) throws IOException {  
        int type = checkContentType(inputFile);  
        boolean status = false;  
        if (type == 0) {  
            status = processFLV(inputFile, outputFile);// ֱ�ӽ��ļ�תΪflv�ļ�  
        } else if (type == 1) {  
            String avifilepath = processAVI(type, inputFile);  
            if (avifilepath == null)  
                return false;// avi�ļ�û�еõ�  
            status = processFLV(avifilepath, outputFile);// ��aviתΪflv  
        }  
        return status;  
    }  
  
    private static int checkContentType(String inputFile) {  
        String type = inputFile.substring(inputFile.lastIndexOf(".") + 1,  
                inputFile.length()).toLowerCase();  
        // ffmpeg�ܽ����ĸ�ʽ����asx��asf��mpg��wmv��3gp��mp4��mov��avi��flv�ȣ�  
        if (type.equals("avi")) {  
            return 0;  
        } else if (type.equals("mpg")) {  
            return 0;  
        } else if (type.equals("wmv")) {  
            return 0;  
        } else if (type.equals("3gp")) {  
            return 0;  
        } else if (type.equals("mov")) {  
            return 0;  
        } else if (type.equals("mp4")) {  
            return 0;  
        } else if (type.equals("asf")) {  
            return 0;  
        } else if (type.equals("asx")) {  
            return 0;  
        } else if (type.equals("flv")) {  
            return 0;  
        }  
        // ��ffmpeg�޷��������ļ���ʽ(wmv9��rm��rmvb��),  
        // �������ñ�Ĺ��ߣ�mencoder��ת��Ϊavi(ffmpeg�ܽ�����)��ʽ.  
        else if (type.equals("wmv9")) {  
            return 1;  
        } else if (type.equals("rm")) {  
            return 1;  
        } else if (type.equals("rmvb")) {  
            return 1;  
        }  
        return 9;  
    }  
    // ffmpeg�ܽ����ĸ�ʽ����asx��asf��mpg��wmv��3gp��mp4��mov��avi��flv�ȣ�ֱ��ת��ΪĿ����Ƶ  
    private static boolean processFLV(String inputFile, String outputFile) throws IOException {  
        if (!checkfile(inputFile)) {  
            System.out.println(inputFile + " is not file");  
            return false;  
        }  
        List<String> commend = new ArrayList<String>();  
          
        commend.add(ConfigFrame.GetProperties("ffmpegPath"));  
        commend.add("-i");  
        commend.add(inputFile);  
        commend.add("-ab");  
        commend.add("128");  
        commend.add("-acodec");  
        commend.add("libmp3lame");  
        commend.add("-ac");  
        commend.add("1");  
        commend.add("-ar");  
        commend.add("22050");  
        commend.add("-r");  
        commend.add("29.97");  
        //��Ʒ��   
        commend.add("-qscale");  
        commend.add("6");  
        //��Ʒ��  
//      commend.add("-b");  
//      commend.add("512");  
        commend.add("-y");  
          
        commend.add(outputFile);  
        StringBuffer test = new StringBuffer();  
        for (int i = 0; i < commend.size(); i++) {  
            test.append(commend.get(i) + " ");  
        }  
  
        System.out.println(test);  
  
        try {  
            ProcessBuilder builder = new ProcessBuilder();  
            builder.command(commend);  
            builder.start();  
            return true;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        }  
    }  
    // ��ffmpeg�޷��������ļ���ʽ(wmv9��rm��rmvb��),  
    // �������ñ�Ĺ��ߣ�mencoder��ת��Ϊavi(ffmpeg�ܽ�����)��ʽ.  
    private static String processAVI(int type, String inputFile) throws IOException {  
        File file = new File(ConfigFrame.GetProperties("avifilepath"));  
        if (file.exists())  
            file.delete();  
        List<String> commend = new ArrayList<String>();  
        commend.add(ConfigFrame.GetProperties("mencoderPath"));  
        commend.add(inputFile);  
        commend.add("-oac");  
        commend.add("mp3lame");  
        commend.add("-lameopts");  
        commend.add("preset=64");  
        commend.add("-ovc");  
        commend.add("xvid");  
        commend.add("-xvidencopts");  
        commend.add("bitrate=600");  
        commend.add("-of");  
        commend.add("avi");  
        commend.add("-o");  
        commend.add(ConfigFrame.GetProperties("avifilepath"));  
        StringBuffer test = new StringBuffer();  
        for (int i = 0; i < commend.size(); i++) {  
            test.append(commend.get(i) + " ");  
        }  
  
        System.out.println(test);  
        try {  
            ProcessBuilder builder = new ProcessBuilder();  
            builder.command(commend);  
            Process p = builder.start();  
  
            final InputStream is1 = p.getInputStream();  
            final InputStream is2 = p.getErrorStream();  
            new Thread() {  
                public void run() {  
                    BufferedReader br = new BufferedReader(  
                            new InputStreamReader(is1));  
                    try {  
                        String lineB = null;  
                        while ((lineB = br.readLine()) != null) {  
                            if (lineB != null)  
                                System.out.println(lineB);  
                        }  
                    } catch (IOException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }.start();  
            new Thread() {  
                public void run() {  
                    BufferedReader br2 = new BufferedReader(  
                            new InputStreamReader(is2));  
                    try {  
                        String lineC = null;  
                        while ((lineC = br2.readLine()) != null) {  
                            if (lineC != null)  
                                System.out.println(lineC);  
                        }  
                    } catch (IOException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }.start();  
  
            // ��Mencoder����ת���������ٵ���ffmepg����  
            p.waitFor();  
            System.out.println("who cares");  
            return ConfigFrame.GetProperties("avifilepath");  
        } catch (Exception e) {  
            System.err.println(e);  
            return null;  
        }  
    }  
}  
