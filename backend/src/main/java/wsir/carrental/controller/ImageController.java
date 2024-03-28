package wsir.carrental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wsir.carrental.entity.domain.Image;
import wsir.carrental.service.ImageService;
import wsir.carrental.util.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public Result<Image> upload(@RequestParam MultipartFile file) throws IOException {
        return Result.success(imageService.upload(file));
    }

    @GetMapping("/{uploadFileName}")
    public void download(@PathVariable String uploadFileName, HttpServletResponse response) throws IOException {
        imageService.download(uploadFileName, response);
    }
}
