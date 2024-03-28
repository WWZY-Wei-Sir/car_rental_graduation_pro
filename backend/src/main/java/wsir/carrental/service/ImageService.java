package wsir.carrental.service;

import org.springframework.web.multipart.MultipartFile;
import wsir.carrental.entity.domain.Image;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ImageService {
    Image upload(MultipartFile file) throws IOException;

    void download(String uploadFileName, HttpServletResponse response) throws IOException;
}
