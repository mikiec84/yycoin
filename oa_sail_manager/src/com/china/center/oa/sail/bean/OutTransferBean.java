package com.china.center.oa.sail.bean;

/**
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 15-8-28
 * Time: 下午3:19
 * To change this template use File | Settings | File Templates.
 */
public class OutTransferBean {
    private String srcFullId;

    private String destFullId;

    public String getSrcFullId() {
        return srcFullId;
    }

    public void setSrcFullId(String srcFullId) {
        this.srcFullId = srcFullId;
    }

    public String getDestFullId() {
        return destFullId;
    }

    public void setDestFullId(String destFullId) {
        this.destFullId = destFullId;
    }

    @Override
    public String toString() {
        return "OutTransferBean{" +
                "srcFullId='" + srcFullId + '\'' +
                ", destFullId='" + destFullId + '\'' +
                '}';
    }
}
