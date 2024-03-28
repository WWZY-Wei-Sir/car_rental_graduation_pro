package wsir.carrental.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wsir.carrental.entity.domain.Car;
import wsir.carrental.entity.domain.CarName;
import wsir.carrental.entity.domain.Image;
import wsir.carrental.entity.domain.RotateImage;
import wsir.carrental.entity.dto.RotateImageDto;
import wsir.carrental.mapper.CarMapper;
import wsir.carrental.mapper.CarNameMapper;
import wsir.carrental.mapper.RotateImageMapper;
import wsir.carrental.service.CarNameService;
import wsir.carrental.service.RotateImageService;

import java.util.List;

@Service
public class RotateImageServiceImpl implements RotateImageService {
    @Autowired
    private RotateImageMapper rotateImageMapper;

    @Override
    @Transactional
    public IPage<RotateImageDto> getPages(Long current,
                                          Long size) {
        MPJLambdaWrapper<RotateImage> wrapper = new MPJLambdaWrapper<RotateImage>()
                .selectAll(RotateImage.class)
                .selectAs(Image::getUrl, RotateImageDto::getUrl)
                .leftJoin(Image.class, Image::getId, RotateImage::getImageId);

        Page<RotateImageDto> page = new Page<>(current, size);
        return rotateImageMapper.selectJoinPage(page, RotateImageDto.class, wrapper);
    }

    @Override
    @Transactional
    public List<RotateImageDto> getList() {
        MPJLambdaWrapper<RotateImage> wrapper = new MPJLambdaWrapper<RotateImage>()
                .selectAll(RotateImage.class)
                .selectAs(Image::getUrl, RotateImageDto::getUrl)
                .leftJoin(Image.class, Image::getId, RotateImage::getImageId);

        return rotateImageMapper.selectJoinList(RotateImageDto.class, wrapper);
    }

    @Override
    @Transactional
    public Integer insertOne(RotateImage rotateImage) {
        return rotateImageMapper.insert(rotateImage);
    }

    @Override
    @Transactional
    public Integer deleteBatch(List<String> ids) {
        return rotateImageMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional
    public Integer chgOne(RotateImage rotateImage) {
        return rotateImageMapper.updateById(rotateImage);
    }
}
