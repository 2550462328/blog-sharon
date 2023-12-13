package cc.ryanc.halo.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <pre>
 *      获取文件hash
 * </pre>
 *
 * @author : Yawn
 * @date : 2018/12/04
 */
public class Md5Util {

    public static void main(String[] args) throws Exception {
        String appSecret = "UZkenzJoKI4M";
        String timestamp = "1695368699093";
        String notice = "1234";

        String data = appSecret + notice + timestamp;
        System.out.println(calculateHash(data, "SHA-1"));

        System.out.println(System.currentTimeMillis());
    }

    /**
     * 计算文件MD5编码
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static byte[] createChecksum(MultipartFile file) throws Exception {
        InputStream fis = file.getInputStream();

        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;

        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        fis.close();
        return complete.digest();
    }

    /**
     * 生成文件hash值
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static String getMD5Checksum(MultipartFile file) throws Exception {
        byte[] b = createChecksum(file);
        String result = "";

        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    public static String calculateHash(String data, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] hashBytes = md.digest(data.getBytes());
        StringBuilder hexHash = new StringBuilder();

        for (byte b : hashBytes) {
            hexHash.append(String.format("%02x", b));
        }

        return hexHash.toString();
    }
}
