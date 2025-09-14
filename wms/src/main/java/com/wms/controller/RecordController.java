package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goods;
import com.wms.entity.Record;
import com.wms.service.GoodsService;
import com.wms.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wms
 * @since 2025-09-12
 */
@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private GoodsService goodsService;
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        String name = null;
        String goodstype = (String) param.get("goodstype");
        String storage = (String) param.get("storage");
        String roleId = (String) param.get("roleId");
        String userId = (String) param.get("userId");

        if (param != null && param.get("name") != null) {
            name = param.get("name").toString();
        }

        Page<Record> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.apply(" a.goods = b.id and b.storage=c.id and b.goodsType=d.id ");

        if("2".equals(roleId)){
            wrapper.apply(" a.userId =" +userId);
        }

        if (StringUtils.hasText(name)) {
            //wrapper.like(Record::getName, name);
            wrapper.like("b.name",name);
        }
        if (StringUtils.hasText(goodstype)) {
            //wrapper.eq(Record::getRecord, goodstype);
            wrapper.eq("d.id",goodstype);
        }
        if (StringUtils.hasText(storage)) {
            //wrapper.eq(Record::getStorage, storage);
            wrapper.eq("c.id",storage);
        }
        IPage<Record> result = recordService.pageCC(page, wrapper);
        return Result.suc(result.getRecords(), result.getTotal());
    }
    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Record record) {
        Goods goods = goodsService.getById(record.getGoods());
        int n = record.getCount();
        if("2".equals(record.getAction())){
            n = -n;
            record.setCount(n);
        }
        int num = goods.getCount() + n;
        goods.setCount(num);
        goodsService.updateById(goods);

        return recordService.save(record)?Result.suc():Result.fail();
    }
}
