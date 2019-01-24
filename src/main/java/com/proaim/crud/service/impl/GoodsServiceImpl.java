package com.proaim.crud.service.impl;

import com.proaim.crud.model.entity.Goods;
import com.proaim.crud.model.vo.QueryVO;
import com.proaim.crud.service.GoodsService;
import com.proaim.crud.service.mapper.GoodsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * 获取UUID
     *
     * @return 获取UUID
     */
    private String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 上传图片文件
     */
    public String uploadImageFile(QueryVO vo) {
        MultipartFile picfile = vo.getPicfile();
        log.info("[文件类型] - [{}]", picfile.getContentType());
        log.info("[文件名称] - [{}]", picfile.getOriginalFilename());
        log.info("[文件大小] - [{}]", picfile.getSize());
        String oldName = picfile.getOriginalFilename();
        String newName = null;
        if (!StringUtils.isEmpty(oldName)) {
            newName = getUUID() + oldName.substring(oldName.lastIndexOf("."));
        }
        try {
            File path = new File("F:/images/");
            System.out.println(path.getPath());
            File imageFile = new File(path.getPath() +"/"+ newName);
            if (!path.isDirectory()) {
                path.mkdirs();
                log.info("当前文件绝对路径" + imageFile.getAbsolutePath());
                log.info("当前文件相对路径" + imageFile.getPath());
            }
            picfile.transferTo(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("上传失败");
        }
        return newName;
    }

    @Override
    public Integer insertGoods(QueryVO vo) {
        String imageFile = uploadImageFile(vo);
        vo.getGoods().setPicfile(imageFile);
        if (StringUtils.isEmpty(goodsMapper.insert(vo.getGoods()))) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public Integer deleteGoodsById(Integer id) {
        return goodsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer updateGoodsById(QueryVO vo) {
        if (StringUtils.isEmpty(goodsMapper.updateByPrimaryKey(vo.getGoods()))) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public Integer updateSelectGoods(QueryVO vo) {
        int i = 0;
        for (Goods goods : vo.getGoodsList()) {
            if (!StringUtils.isEmpty(goods.getId()) && StringUtils.isEmpty(goods.getPicfile())) {
                i += goodsMapper.updateByPrimaryKeySelective(goods);
            }
        }
        //Lambda表达式
        /*vo.getGoodsList().forEach(goods -> {
            if (!StringUtils.isEmpty(goods.getId()) && StringUtils.isEmpty(goods.getPicfile())) {
                goodsMapper.updateByPrimaryKey(goods);
            }
        });*/
        return i;
    }

    @Override
    public List<Goods> selectGoods() {
        return goodsMapper.selectByExample(null);
    }

    @Override
    public Goods selectGoodsById(Integer id) {
        return goodsMapper.selectByPrimaryKey(id);
    }
}
