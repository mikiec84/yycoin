package com.china.center.oa.sail.manager.impl;

import com.china.center.common.MYException;

/**
 * Created by user on 2016/6/24.
 */
public class MailOrderException extends MYException {
    private Object order;

    public Object getOrder() {
        return order;
    }

    public void setOrder(Object order) {
        this.order = order;
    }

    public MailOrderException(String errorContent, Object order) {
        super(errorContent);
        this.order = order;
    }
}
