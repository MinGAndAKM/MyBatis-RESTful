package com.proaim.crud.model.vo;


import com.proaim.crud.model.entity.Goods;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class QueryVO {
    private Goods goods;
    private List<Goods> goodsList;
    private MultipartFile picfile;
}
