package vip.itchen.api.com;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vip.itchen.common.properties.ApiConfigProperties;
import vip.itchen.support.ImageUtils;
import vip.itchen.support.exceptions.BizMsgException;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;

/**
 * @author ckh
 * @date 2021-04-14
 */
@Slf4j
@RestController
@RequestMapping("/web_api/com")
@Api(value = "/web_api/com", tags = "com")
public class ComController {

    @Resource
    private ApiConfigProperties apiConfigProperties;
    private static final String[] suffixes = new String[] {
            "jpg",
            "png"
    };

    @PostMapping("/upload")
    @ApiOperation(value = "上传文件", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        String suffix = FileUtil.getSuffix(file.getOriginalFilename());
        if (!legalSuffix(suffix)) {
            // 图片格式不合法，只支持[*.jpg]和[*.png]
            throw new BizMsgException("E.100009");
        }

        String filename = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN)
                .concat(StrUtil.DOT)
                .concat(StrUtil.nullToDefault(suffix, "unknown"));

        String destFilePath = apiConfigProperties.getUploadPath()
                .concat(filename);
        FileUtil.mkParentDirs(destFilePath);

        byte[] bytes = ImageUtils.imageCompress(file.getBytes(), 1024 * 1024, suffix);
        try (OutputStream os = FileUtil.getOutputStream(destFilePath)) {
            os.write(bytes);
        }

        return filename;
    }

    @GetMapping("/image/{imageName}")
    @ApiOperation(value = "显示文件")
    public void image(@PathVariable String imageName, HttpServletResponse response) {
        if (null == imageName || !imageName.matches("[0-9A-Za-z.]+")) {
            return;
        }
        String filename = apiConfigProperties.getUploadPath()
                .concat(imageName);
        try (OutputStream os = response.getOutputStream();
             FileInputStream fis = new FileInputStream(filename)
        ) {
            BufferedImage image = ImageIO.read(fis);
            response.setContentType("image/png");
            ImageIO.write(image, "png", os);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 判断是否合法的后缀
     * @param suffix 后缀
     * @return 是否合法
     */
    private boolean legalSuffix(String suffix) {
        for (String tmp : suffixes) {
            if (tmp.equalsIgnoreCase(suffix)) {
                return true;
            }
        }
        return false;
    }
}
