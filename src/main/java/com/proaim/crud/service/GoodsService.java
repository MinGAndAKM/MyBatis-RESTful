package com.proaim.crud.service;

import com.proaim.crud.model.entity.Goods;
import com.proaim.crud.model.vo.QueryVO;

import java.io.IOException;
import java.util.List;

public interface GoodsService {

    Integer insertGoods(QueryVO vo);

    Integer deleteGoodsById(Integer id);

    Integer updateGoodsById(QueryVO vo);

    Integer updateSelectGoods(QueryVO vo);

    List<Goods> selectGoods();

    Goods selectGoodsById(Integer id);

}
