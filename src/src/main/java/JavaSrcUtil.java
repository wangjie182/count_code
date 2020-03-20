package src.main.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.stream.Stream;

/**
 * 源代码统计量
 *
 * @createTime: 2012-8-1 下午01:25:56
 * @author: <a href="mailto:hubo@feinno.com">hubo</a>
 * @version: 0.1
 * @lastVersion: 0.1
 * @updateTime:
 * @updateAuthor: <a href="mailto:hubo@feinno.com">hubo</a>
 * @changesSum:
 *
 */
public class JavaSrcUtil {

    private static final String FILE_TYPE = "java";

    private long rows = 0;

    private final StringBuffer sbBuffer = new StringBuffer();

    /**
     * 根据文件计算代码行数
     */
    public long staticRowsByFile(final File file) throws IOException {
        if (file.isDirectory()) { // 非文件
            throw new IOException("is not file:" + file);
        } else if (!file.getName().endsWith("." + FILE_TYPE)) { // 非java文件
            return 0;
        }

        long rows = 0;

        final FileInputStream fis = new FileInputStream(file);
        final BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        String str = null;
        while ((str = br.readLine()) != null) {
            str = str.trim();
            if (str.length() > 1 && !str.startsWith("/") && !str.startsWith("*")) {
                rows++;
            }
        }

        return rows;
    }

    /**
     * 根据目录计算目录以内的代码行数
     *
     * @param dirFile
     * @return
     * @throws IOException
     * @auther <a href="mailto:hubo@feinno.com">hubo</a>
     * @return long 2012-8-1 下午01:43:52
     */
    public long staticRowsByDirectory(final File dirFile) throws IOException {
        if (!dirFile.isDirectory()) {
            throw new IOException("is not Directory:" + dirFile);
        }

        final File[] files = dirFile.listFiles();
        for (final File childFile : files) {
            if (childFile.isDirectory()) {
                staticRowsByDirectory(childFile);
            } else {
                rows += staticRowsByFile(childFile);
            }
        }
        return rows;
    }

    /**
     * 获取文件代码
     *
     * @param file
     * @return
     * @throws IOException
     * @auther <a href="mailto:hubo@feinno.com">hubo</a>
     * @return long 2012-8-1 下午01:43:38
     */
    public String getSrcByFile(final File file) throws IOException {
        if (file.isDirectory()) { // 非文件
            throw new IOException("is not file:" + file);
        } else if (!file.getName().endsWith("." + FILE_TYPE)) { // 非java文件
            return "";
        }

        final StringBuffer sbBuffer = new StringBuffer();

        final FileInputStream fis = new FileInputStream(file);
        final BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String str = null;
        while ((str = br.readLine()) != null) {
            if (str.trim().length() > 0) {
                //应俊哥要求添加条件，使得import语句不被保存入文件
                if(str.trim().startsWith("import") != true) {
                sbBuffer.append(str);
                sbBuffer.append("   ");

                // sbBuffer.append("\\r\\n");
            }}
        }

        return sbBuffer.toString();
    }

    /**
     * 获取目录以内所有的源代码
     *
     * @param dirFile
     * @return
     * @throws IOException
     * @auther <a href="mailto:hubo@feinno.com">hubo</a>
     * @return long 2012-8-1 下午01:43:52
     */
    public String getSrcByDirectory(final File dirFile) throws IOException {
        if (!dirFile.isDirectory()) {
            throw new IOException("is not Directory:" + dirFile);
        }

        final File[] files = dirFile.listFiles();
        for (final File childFile : files) {
            if (childFile.isDirectory()) {
                getSrcByDirectory(childFile);
            } else {
                sbBuffer.append(getSrcByFile(childFile));
            }
        }
        return sbBuffer.toString();
    }

    public static void main(final String[] args) throws IOException {
        // ass——14014 common——6896 protocol——3531 short-master——1163
        // server——7666+178+7703=15547 web——6583
        final File file = new File("C:\\Users\\Administrator\\Desktop\\work\\俊哥0313");
        final JavaSrcUtil jsu = new JavaSrcUtil();
        //统计源代码行数
        System.out.println(jsu.staticRowsByDirectory(file));
        //汇总源代码总量并保存
        final OutputStream os = new FileOutputStream(
                new File("C:\\Users\\Administrator\\Desktop\\work\\CountCode\\colla3.java"));
        os.write(jsu.getSrcByDirectory(file).getBytes());
    }
}