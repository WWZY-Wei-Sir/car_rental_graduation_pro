package wsir.carrental.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import wsir.carrental.entity.domain.CarUserComment;
import wsir.carrental.entity.dto.CarUserCommentDto;

public interface CarUserCommentService {
    IPage<CarUserCommentDto> getPages(String carId, Long current, Long size);

    Integer insertOne(CarUserComment carUserComment);
}
