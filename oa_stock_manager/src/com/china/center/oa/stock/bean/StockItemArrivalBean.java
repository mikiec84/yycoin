/**
 *
 */
package com.china.center.oa.stock.bean;


import com.china.center.jdbc.annotation.*;


/**
 * 2015/12/1 采购到货行
 * @author Simon
 */
@Entity(name = "采购到货ITEM",inherit = true)
@Table(name = "T_CENTER_STOCKITEMARRIAL")
public class StockItemArrivalBean extends StockItemBean
{

    @Override
    public String toString() {
        return "采购到货信息{" +
                "商品='" + this.productId + '\'' +
                ", 数量='" + this.amount + '\'' +
                ", 出货日期='" + this.deliveryDate + '\'' +
                ", 预计到货日期='" + this.arrivalDate + '\'' +
                '}';
    }
}
