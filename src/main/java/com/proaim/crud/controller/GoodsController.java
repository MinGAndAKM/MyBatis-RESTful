package com.proaim.crud.controller;

import com.proaim.crud.model.entity.Goods;
import com.proaim.crud.model.vo.QueryVO;
import com.proaim.crud.service.GoodsService;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class GoodsController {
    @Autowired
    GoodsService goodsService;

    @GetMapping("/")
    public String selectGoods(Model model) {
        List<Goods> goods = goodsService.selectGoods();
        model.addAttribute("goods", goods);
        return "goods/index";
    }

    //添加商品
    @PostMapping("/goods")
    public String createGoods(QueryVO vo) {
        goodsService.insertGoods(vo);
        return "redirect:/";
    }

    @DeleteMapping("/goods/{id}")
    public String deleteGoods(@PathVariable("id") Integer id) {
        goodsService.deleteGoodsById(id);
        return "redirect:/";
    }

    @PutMapping("/goods/{id}")
    public String updateGoods(Model model, @PathVariable("id") Integer id) {
        Goods goods = goodsService.selectGoodsById(id);
        model.addAttribute("goods", goods);
        return "goods/goods";
    }

    @PutMapping("/goods")
    public String updateGoodsSave(QueryVO vo) {
        goodsService.updateGoodsById(vo);
        return "redirect:/";
    }

    @GetMapping("/goods")
    public String updateSelectGoods(QueryVO vo) {
        goodsService.updateSelectGoods(vo);
        return "redirect:/";
    }

    @PostMapping("/uploadPic")
    public void uploadPic(QueryVO vo, HttpServletRequest request, HttpServletResponse response) {
        try {
            // 获取图片原始文件名
            String originalFilename = vo.getPicfile().getOriginalFilename();
            System.out.println(originalFilename);

            // 文件名使用当前时间
            String newName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

            // 获取上传图片的扩展名(jpg/png/...)
            // pom.xml需导入commons-io
            String extension = FilenameUtils.getExtension(originalFilename);

            // 图片上传的相对路径（因为相对路径放到页面上就可以显示图片）
            String path = "/upload/" + newName + "." + extension;

            // 图片上传的绝对路径
            String urlPath = request.getSession().getServletContext().getRealPath("");
            String url = urlPath + path;
            System.out.println("完整绝对路径：" + urlPath);
            System.out.println("完整文件路径：" + url);
            File dir = new File(urlPath);
            System.out.println(dir.isDirectory());
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }

            // 上传图片
            vo.getPicfile().transferTo(dir);

            // 将相对路径写回（json格式）
            JSONObject jsonObject = new JSONObject();
            // 将图片上传到本地
            jsonObject.put("path", path);

            // 设置响应数据的类型json
            response.setContentType("application/json; charset=utf-8");
            // 写回
            response.getWriter().write(jsonObject.toString());

            // https://github.com/blueimp/jQuery-File-Upload 图片上传组件

        } catch (Exception e) {
            /*throw new RuntimeException("服务器繁忙，上传图片失败");*/
        }

    }
}
