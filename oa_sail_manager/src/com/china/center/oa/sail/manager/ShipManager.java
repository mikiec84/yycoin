package com.china.center.oa.sail.manager;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.sail.bean.BranchRelationBean;
import com.china.center.oa.sail.bean.DistributionBean;
import com.china.center.oa.sail.bean.PackageItemBean;
import com.china.center.oa.sail.bean.PreConsignBean;
import com.china.center.oa.sail.vo.OutVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ShipManager
{
//	void createPackage();

    /**
     * 2015/7/25 捡配前检查同一收货人或电话
     * @param user
     * @param packageIds
     * @return
     * @throws MYException
     */
    public Map<String, Set<String>> prePickup(User user, String packageIds) throws MYException;

    boolean addPickup(User user, String packageIds) throws MYException;

    boolean deletePackage(User user, String packageIds) throws MYException;

    /**  2015/2/26
     * 撤销“已拣配”或“已打印”状态的CK单
     * @param user
     * @param packageIds
     * @return
     * @throws MYException
     */
    boolean cancelPackage(User user, String packageIds) throws MYException;

    boolean updateStatus(User user, String pickupId) throws MYException;

    boolean updatePackagesStatus(User user, String packageIds) throws MYException;

    boolean updatePrintStatus(String pickupId, int index_pos) throws MYException;

    void createPackage(PreConsignBean pre, OutVO out) throws MYException;

    void sendMailForShipping() throws MYException;

    void sendShippingMailToSails() throws MYException;

    void sendMailForNbShipping() throws MYException;

    void sendMailForNbBeforeShipping() throws MYException;

    void saveAllEntityBeans(List<BranchRelationBean> importItemList) throws MYException;

    List<String> autoPickup(int pickupCount, String productName) throws MYException;

    /**
     * 2015/3/10 自动捡配时新增批次
     *
     * @param packageIds
     * @param pickupCount
     * @param currentPickupCount
     * @return 新建的批次号
     * @throws MYException
     */
    List<String> addPickup(String packageIds, int pickupCount, int currentPickupCount) throws MYException;

    // 2015/2/8 后台Job，商品拣配的排序默认按订单日期由远到近的顺序排列
    void sortPackagesJob() throws MYException;
    //
//	void createInsPackage(PreConsignBean pre, String insId) throws MYException;
    //2015/2/25 手工合并CK单
    void mergePackages(String user, String packageIds, int shipping, int transport1, int transport2, int expressPay, int transportPay, String cityId, String address, String receiver, String phone) throws MYException;

    /**
     * get product name from package item
     * @param item
     * @return
     */
    public String getProductName(PackageItemBean item);

    void updateShipping(String packageId, DistributionBean distributionBean);
}