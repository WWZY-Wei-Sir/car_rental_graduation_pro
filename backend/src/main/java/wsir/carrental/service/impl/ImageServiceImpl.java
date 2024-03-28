package wsir.carrental.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wsir.carrental.entity.domain.Image;
import wsir.carrental.exception.ServiceException;
import wsir.carrental.mapper.ImageMapper;
import wsir.carrental.service.ImageService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ImageServiceImpl implements ImageService {
    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Autowired
    private ImageMapper imageMapper;

    public Image upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        String uuid = IdUtil.fastSimpleUUID();
        String time = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss").format(LocalDateTime.now());
        String uploadFileName = time + StrUtil.UNDERLINE + uuid + StrUtil.DOT + type;
        File uploadFile = new File(fileUploadPath + uploadFileName); //文件路径

        //先存到磁盘
        if (!uploadFile.getParentFile().exists()) {
            //  存储下载文件目录不存在，则创建
            if (!uploadFile.getParentFile().mkdirs()) {
                throw new ServiceException(HttpURLConnection.HTTP_INTERNAL_ERROR, "创建缓存目录失败，请联系管理员");
            }
        }

        file.transferTo(uploadFile); //  将文件存储到对应位置
        String md5 = SecureUtil.md5(uploadFile); //  得到该文件的md5，必须在上传文件后才能得到


        Image byMd5 = imageMapper.selectOne(new LambdaQueryWrapper<Image>().eq(Image::getMd5, md5));
        String url;
        if (byMd5 == null) {
            Image image = new Image();
            url = "http://localhost:8090/image/" + uploadFileName; //新建url
            //  存到数据库
            image.setImageType(type);
            image.setSize(size / 1024);
            image.setMd5(md5);
            image.setUrl(url);
            imageMapper.insert(image);
            return image;
        } else {
            //  若存在则销毁
            if (!uploadFile.delete()) {
                throw new ServiceException(HttpURLConnection.HTTP_INTERNAL_ERROR, "删除文件失败，请联系管理员");
            }
            return byMd5;
        }
    }

    public void download(String uploadFileName, HttpServletResponse response) throws IOException {
        //  获取文件唯一标识
        File downloadFile = new File(fileUploadPath + uploadFileName);
        //  设置浏览器输出流
        ServletOutputStream out = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(uploadFileName, StandardCharsets.UTF_8));
        response.setContentType("application/octet-stream");

        //  读文件
        out.write(FileUtil.readBytes(downloadFile));
        out.flush();
        out.close();
    }
}
