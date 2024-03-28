package wsir.carrental.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import wsir.carrental.entity.domain.CarName;
import wsir.carrental.entity.domain.RotateImage;
import wsir.carrental.entity.dto.RotateImageDto;

import java.util.List;

public interface RotateImageService {
    IPage<RotateImageDto> getPages(Long current,
                                   Long size);

    List<RotateImageDto> getList();

    Integer insertOne(RotateImage rotateImage);

    Integer deleteBatch(List<String> ids);

    Integer chgOne(RotateImage rotateImage);
}
