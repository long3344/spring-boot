package com.wechat.controller.echarts;

import com.wechat.dao.WebpageMapper;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述：
 * 作者: TWL
 * 创建日期: 2017/12/7
 */

@Controller
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private WebpageMapper webpageMapper;

    @RequestMapping("/echartsPage")
    public ModelAndView toEchart(){
        return new ModelAndView("echarts/echartsPage");
    }

    @RequestMapping("/getDate")
    @ResponseBody
    public Map<String,Object> getDate(){

        Map<String,Object> maps=new HashMap<String,Object>();
        List<Map<String,Object>> results =webpageMapper.getDate();

        String[] keys = new String[results.size()];
        String[] values = new String[results.size()];
        Object[] res = new Object[results.size()];

        for (int i=0; i<results.size();i++){
            Map item=new HashMap();
            item.put("name",results.get(i).get("dd"));
            item.put("value",results.get(i).get("PV"));

            res[i]=item;

            keys[i]=results.get(i).get("dd")+"";
            values[i]=results.get(i).get("PV")+"";
        }
        maps.put("keys",keys);
        maps.put("values",values);
        maps.put("results",results);
        maps.put("pies",res);

        return maps;
    }
}
