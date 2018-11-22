package cn.dmego.odsp.common;

import lombok.Data;

import java.util.List;

/**
 * class_name: PageResult
 * package: cn.dmego.odsp.common
 * describe:  分页结果对象 ,以 Layui 框架的 table 为标准
 * creat_user: Dmego
 * creat_date: 2018/10/23
 * creat_time: 22:15
 **/
@Data
public class PageResult<T> {

    private Integer code; //状态码, 0 表示成功

    private String msg; //提示信息

    private long count; //总数量 (bootstrapTable 是 total)

    private List<T> data; //当前数据 (bootstrapTable 是 rows)

    public PageResult() { }

    public PageResult(List<T> rows){
        this.data = rows;
        this.count = rows.size();
        this.code = 0;
        this.msg = "";
    }

    public PageResult(long total,List<T> rows){
        this.count = total;
        this.data = rows;
        this.code = 0;
        this.msg = "";
    }

    public void setData(List<T> data) {
        this.data = data;
        this.count = data.size();
    }
}
