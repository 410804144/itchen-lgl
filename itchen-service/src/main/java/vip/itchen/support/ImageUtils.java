package vip.itchen.support;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifDirectoryBase;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片处理
 * @author alabimofa
 * @date 2020/8/24
 */
public class ImageUtils {

    public static final String IMAGE_HEIGHT = "height";
    public static final String IMAGE_WIDTH = "width";

    /**
     * 根据文件流读取图片文件真实类型
     *
     * @param bytes
     * @return
     */
    public static String getTypeByStream(byte[] bytes) {
        byte[] b = new byte[10];
        for (int i = 0; i < 10; i++) {
            b[i] = bytes[i];
        }
        String type = bytesToHexString(b).toUpperCase();
        if (type.contains("FFD8FF")) {
            return "jpg,image/jpeg";
        } else if (type.startsWith("89504E47")) {
            return "png,image/png";
        } else if (type.startsWith("47494638")) {
            return "gif,image/gif";
        } else if (type.startsWith("49492A00")) {
            return "tif,image/tiff";
        } else if (type.startsWith("424D")) {
            return "bmp,image/bmp";
        } else {
            throw new IllegalArgumentException("解析出图片格式不合法");
        }
    }

    /**
     * 获取图片的宽度及高度
     * @param bytes 图片流
     * @return 宽度/高度
     */
    public static Map<String, Integer> getImageWidthAndHeight(byte[] bytes) {
        try {
            Map<String, Integer> imageProperties = new HashMap<>(2);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
            if (image != null) {
                imageProperties.put(IMAGE_WIDTH, image.getWidth());
                imageProperties.put(IMAGE_HEIGHT, image.getHeight());
            }
            return imageProperties;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 获取图片的宽度及高度
     * @param url 图片地址
     * @return 宽度/高度
     */
    public static Map<String, Integer> getImageWidthAndHeightByUrl(String url) {
        try {
            Map<String, Integer> imageProperties = new HashMap<>(2);
            BufferedImage image = ImageIO.read(new URL(url));
            if (image != null) {
                imageProperties.put(IMAGE_WIDTH, image.getWidth());
                imageProperties.put(IMAGE_HEIGHT, image.getHeight());
            }
            return imageProperties;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * byte数组转换成16进制字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 获取图片Exif方向 （原始图片，即没有经过压缩旋转裁剪等操作）
     * @return
     */
    public static int getExifOrientation(byte[] bytes) {
        int angel = 0;
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(bytes));
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    if (tag.getTagType() == ExifDirectoryBase.TAG_ORIENTATION) {
                        String description = tag.getDescription();
                        if (description.contains("90")) {
                            // 顺时针旋转90度
                            angel = 90;
                        } else if (description.contains("180")) {
                            // 顺时针旋转180度
                            angel = 180;
                        } else if (description.contains("270")) {
                            // 顺时针旋转270度
                            angel = 270;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return angel;
    }


    /**
     * 图片裁剪
     *
     * @param bytes
     * @param x      X轴
     * @param y      Y轴
     * @param w      裁剪的宽
     * @param h      裁剪的高
     * @param width  宽缩放不超过width
     * @param height 高缩放不超过height
     * @param format 图片格式
     * @return
     */
    public static byte[] imageCut(byte[] bytes, int x, int y, int w, int h, int width, int height, String format) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Thumbnails.of(new ByteArrayInputStream(bytes))
                    //指定坐标 w*y区域
                    .sourceRegion(x, y, w, y)
                    //宽缩小为<=width,高缩小为<=height
                    .size(width, height)
                    //不按照比例，指定大小进行缩放
                    .keepAspectRatio(false)
                    //输出格式
                    .outputFormat(format)
                    .toOutputStream(out);
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 图片等比例压缩不超过指定大小
     *
     * @param bytes
     * @param size   指定大小
     * @param format 图片格式
     * @return
     */
    public static byte[] imageCompress(byte[] bytes, long size, String format) {
        try {
            if (bytes.length > size) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                Thumbnails.of(new ByteArrayInputStream(bytes))
                        //缩小原图0.8
                        .scale(0.8)
                        //输出格式
                        .outputFormat(format)
                        .toOutputStream(out);
                return imageCompress(out.toByteArray(), size, format);
            }
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取图片Exif方向旋转，按照大小等比例压缩
     *
     * @param bytes
     * @param format 图片格式
     */
    public static byte[] imageRotateAndCompress(byte[] bytes, String format) {
        try {
            //获取拍照时旋转角度
            int angel = getExifOrientation(bytes);
            double scale = 1.0;
            if ((bytes.length > 1024 * 100) && (bytes.length <= 1024 * 200)) {
                scale = 0.8;
            } else if ((bytes.length > 1024 * 200) && (bytes.length <= 1024 * 400)) {
                scale = 0.7;
            } else if ((bytes.length > 1024 * 400) && (bytes.length <= 1024 * 600)) {
                scale = 0.6;
            } else if ((bytes.length > 1024 * 600) && (bytes.length <= 1024 * 800)) {
                scale = 0.5;
            } else if ((bytes.length > 1024 * 800) && (bytes.length <= 1024 * 1024)) {
                scale = 0.3;
            } else if ((bytes.length > 1024 * 1024) && (bytes.length <= 1024 * 1024 * 3)) {
                scale = 0.2;
            } else if (bytes.length > 1024 * 1024 * 3) {
                scale = 0.1;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Thumbnails.of(new ByteArrayInputStream(bytes))
                    //rotate(角度),正数：顺时针 负数：逆时针
                    .rotate(angel)
                    //scale(比例),1等比，大于1放大，小于1缩小
                    .scale(scale)
                    //输出格式
                    .outputFormat(format)
                    .toOutputStream(out);
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\alabimofa\\Desktop\\app-release-08251053.apk");
        Map<String, Integer> map  = getImageWidthAndHeightByUrl("http://oss-public.bitbika.com/1923396169-1597139350372.jpg");
        System.out.println(map);
    }
}
