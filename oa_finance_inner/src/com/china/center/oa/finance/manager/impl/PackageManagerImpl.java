package com.china.center.oa.finance.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.center.china.osgi.config.ConfigLoader;
import com.china.center.oa.finance.dao.PreInvoiceApplyDAO;
import com.china.center.oa.finance.vo.PreInvoiceApplyVO;
import com.china.center.oa.sail.bean.*;
import com.china.center.oa.sail.constanst.ShipConstant;
import com.china.center.oa.sail.dao.*;
import com.china.center.oa.sail.manager.OutManager;
import com.china.center.oa.sail.vo.ProductExchangeConfigVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.china.center.common.MYException;
import com.china.center.jdbc.annosql.constant.AnoConstant;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.finance.bean.InsVSInvoiceNumBean;
import com.china.center.oa.finance.bean.InvoiceinsBean;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.dao.InsVSInvoiceNumDAO;
import com.china.center.oa.finance.dao.InvoiceinsDAO;
import com.china.center.oa.finance.dao.InvoiceinsItemDAO;
import com.china.center.oa.finance.manager.PackageManager;
import com.china.center.oa.finance.vo.InvoiceinsItemVO;
import com.china.center.oa.finance.vo.InvoiceinsVO;
import com.china.center.oa.product.bean.DepotBean;
import com.china.center.oa.product.constant.DepotConstant;
import com.china.center.oa.product.dao.DepotDAO;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.vo.StafferVO;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.vo.DistributionVO;
import com.china.center.oa.sail.vo.OutVO;
import com.china.center.oa.sail.vo.PackageVO;
import com.china.center.tools.ListTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;

/**
 * 〈一句话功能简述〉<br> 
 * 〈功能详细描述〉
 *
 * @author smart
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class PackageManagerImpl implements PackageManager {
	private final Log triggerLog = LogFactory.getLog("trigger");

    private final Log blockedLog = LogFactory.getLog("blocked");

    private final Log _logger = LogFactory.getLog(getClass());
	
	private PreConsignDAO preConsignDAO = null;
	
	private PackageDAO packageDAO = null;
	
	private PackageItemDAO packageItemDAO = null;
	
	private OutDAO outDAO = null;
	
	private BaseDAO baseDAO = null;
	
	private DistributionDAO distributionDAO = null;
	
	private CommonDAO commonDAO = null;
	
	private DepotDAO depotDAO = null;
	
	private PackageVSCustomerDAO packageVSCustomerDAO = null;
	
	private InvoiceinsDAO invoiceinsDAO = null;
	
	private InsVSInvoiceNumDAO insVSInvoiceNumDAO = null;
	
	private StafferDAO stafferDAO = null;
	
	private InvoiceinsItemDAO invoiceinsItemDAO = null;
	
	private OutImportDAO outImportDAO = null;

    private OutManager outManager = null;
	
	private PlatformTransactionManager transactionManager = null;

    private Object lock = new Object();

    private PreInvoiceApplyDAO preInvoiceApplyDAO = null;

    private ProductExchangeConfigDAO productExchangeConfigDAO = null;

	public PackageManagerImpl()
	{
	}

	@Override
	public void checkOrderWithoutCKJob() {

		String msg = "*******************checkOrderWithoutCKJob running***********************";
		_logger.info(msg);

		long statsStar = System.currentTimeMillis();

		TransactionTemplate tran = new TransactionTemplate(transactionManager);

		try
		{
			tran.execute(new TransactionCallback()
			{
				public Object doInTransaction(TransactionStatus arg0)
				{
					try{
						processZsOrderWithoutCK();
					}catch(MYException e)
					{
						e.printStackTrace();
						_logger.error(e);
						throw new RuntimeException(e);
					}

					return Boolean.TRUE;
				}
			});
		}
		catch (Exception e)
		{
			triggerLog.error(e, e);
		}

		_logger.info("checkOrderWithoutCKJob 统计结束... ,共耗时：" + (System.currentTimeMillis() - statsStar));

		return;
	}

	private void processZsOrderWithoutCK() throws MYException{
		ConditionParse conditionParse = new ConditionParse();
		conditionParse.addWhereStr();
		//销售单
		conditionParse.addCondition("OutBean.type","=",  OutConstant.OUT_TYPE_OUTBILL);
		//只针对出问题的赠送单
		//2016/12/12 所有销售单都检查
//		conditionParse.addCondition("OutBean.outType","=", OutConstant.OUTTYPE_OUT_PRESENT);
		//状态为 “已出库”
		conditionParse.addCondition("OutBean.status", "=", OutConstant.STATUS_PASS);
		conditionParse.addCondition("OutBean.outTime", ">", "2016-10-01");
		//没有生成CK单的订单
		conditionParse.addCondition("and not exists(select p.id from T_CENTER_PACKAGE_ITEM p where p.outId = OutBean.fullId)");

		List<OutVO> outBeans = this.outDAO.queryEntityVOsByCondition(conditionParse);
		if (!ListTools.isEmptyOrNull(outBeans)){
			_logger.info("processZsOrderWithoutCK with out size:"+outBeans.size());
			for (OutVO out: outBeans){
				//发货方式不是“空发”
				List<DistributionVO> distList = distributionDAO.queryEntityVOsByFK(out.getFullId());

				if (ListTools.isEmptyOrNull(distList) ||
						distList.get(0).getShipping() == OutConstant.OUT_SHIPPING_NOTSHIPPING)
				{
					_logger.info("no distribution bean or 99:"+out.getFullId());
					continue;
				} else{
					//check duplicate preconsign
					ConditionParse con = new ConditionParse();
					con.addWhereStr();
					con.addCondition("outId","=",out.getFullId());
					List<PreConsignBean> preConsignBeans = this.preConsignDAO.queryEntityBeansByCondition(con);
					if (ListTools.isEmptyOrNull(preConsignBeans)){
						PreConsignBean preConsign = new PreConsignBean();

						preConsign.setOutId(out.getFullId());

						preConsignDAO.saveEntityBean(preConsign);
						_logger.info(preConsign.getId()+" preconsign created processZsOrderWithoutCK for out:"+out.getFullId());
					}
				}
			}
		} else{
			_logger.info("processZsOrderWithoutCK with out size 0!");
		}
	}

	/**
	 * 生成发货单 (根据收货打包)
	 */
	public void createPackage()
	{
        synchronized (this.lock){

            long statsStar = System.currentTimeMillis();

            TransactionTemplate tran = new TransactionTemplate(transactionManager);

            try
            {
                tran.execute(new TransactionCallback()
                {
                    public Object doInTransaction(TransactionStatus arg0)
                    {
                        try{
                            processOut();
                        }catch(MYException e)
                        {
                            throw new RuntimeException(e);
                        }

                        return Boolean.TRUE;
                    }
                });
            }
            catch (Exception e)
            {
                triggerLog.error(e, e);
            }

            triggerLog.info("createPackage 统计结束... ,共耗时："+ (System.currentTimeMillis() - statsStar));

            return;
        }
    }
	
	/**
	 * 生成发货单核心
	 * @throws MYException
	 */
	private void processOut() throws MYException
	{
        ConditionParse conditionParse = new ConditionParse();
		conditionParse.addWhereStr();
		conditionParse.addCondition(" order by logTime asc");

        int batchSize = 50;
        String batchSizeStr = ConfigLoader.getProperty("batchSize");
        if (!StringTools.isNullOrNone(batchSizeStr)){
           batchSize = Integer.valueOf(batchSizeStr);
        }

		List<PreConsignBean> list = preConsignDAO.queryEntityBeansByLimit(conditionParse, batchSize);

        String msg = "*******************createPackage with size"+list.size();
        System.out.println(msg);
        triggerLog.info(msg);

        int count = 0;
		for (PreConsignBean each : list) {
//			ConditionParse conditionParse1 = new ConditionParse();
//			conditionParse1.addWhereStr();
//			conditionParse1.addCondition("fullId","=",each.getOutId().trim());
//			List<OutVO> outBean = outDAO.queryEntityVOsByCondition(conditionParse1);
			OutVO outBean = outDAO.findVO(each.getOutId().trim());

			if (null != outBean) {
				_logger.info(count+"======is out======" + each.getOutId()+"==="+outBean.getOutType());
				createPackage(each, outBean);
			} else {
				_logger.info(count+"=========is not out=========="+each.getOutId());
				InvoiceinsBean insBean = invoiceinsDAO.find(each.getOutId());
				
				if (null != insBean) {
					_logger.info(count+"======is invoiceins======" + each.getOutId());
					createInsPackage(each, insBean.getId());
				} else {
                    //2015/3/1 预开票申请也需要进入CK单
                    PreInvoiceApplyVO applyBean = this.preInvoiceApplyDAO.findVO(each.getOutId());

                    if (applyBean!= null){
						_logger.info(count+"======is PreInvoiceApplyBean======" + each.getOutId());
                        this.createPreInsPackage(each, applyBean);
                    } else{
                        triggerLog.warn(count+"======is other, direct delete, handle nothing======"+each.getOutId());
                        preConsignDAO.deleteEntityBean(each.getId());

                        continue;
                    }
				}
			}
            count++;
		}
	}

	private void createNewPackage(OutVO outBean,
			List<BaseBean> baseList, DistributionVO distVO, String fullAddress, String location)
	{
        _logger.info("**************createNewPackage for Out now "+outBean.getFullId());

        String id = commonDAO.getSquenceString20("CK");
		
		int allAmount = 0;
		
		PackageBean packBean = new PackageBean();
		
		packBean.setId(id);
		packBean.setCustomerId(outBean.getCustomerId());
		packBean.setShipping(distVO.getShipping());
		packBean.setTransport1(distVO.getTransport1());
		packBean.setExpressPay(distVO.getExpressPay());
		packBean.setTransport2(distVO.getTransport2());
		packBean.setTransportPay(distVO.getTransportPay());
		packBean.setAddress(fullAddress);
		packBean.setReceiver(distVO.getReceiver());
		packBean.setMobile(distVO.getMobile());
		packBean.setLocationId(location);
		packBean.setCityId(distVO.getCityId());
		
		packBean.setStafferName(outBean.getStafferName());
		packBean.setIndustryName(outBean.getIndustryName());
		packBean.setDepartName(outBean.getIndustryName3());
		
		packBean.setTotal(outBean.getTotal());
		packBean.setStatus(0);
		packBean.setLogTime(TimeTools.now());
		
		List<PackageItemBean> itemList = new ArrayList<PackageItemBean>();
		
		boolean isEmergency = false;
		Map<String, List<BaseBean>> pmap = new HashMap<String, List<BaseBean>>();
		for (BaseBean base : baseList)
		{
			PackageItemBean item = new PackageItemBean();
			
			item.setPackageId(id);
			item.setOutId(outBean.getFullId());
			item.setBaseId(base.getId());
			item.setProductId(base.getProductId());
			item.setProductName(base.getProductName());

			//2015/8/12 调拨单产生的CK单的数量应该取绝对值，因为调拨出库是负的数量
			item.setAmount(Math.abs(base.getAmount()));
			item.setPrice(Math.abs(base.getPrice()));

			item.setValue(base.getValue());
			item.setOutTime(outBean.getOutTime());
			item.setDescription(outBean.getDescription());
			item.setCustomerId(outBean.getCustomerId());
			item.setEmergency(outBean.getEmergency());
			
			if (item.getEmergency() == 1) {
				isEmergency = true;
			}
			//TODO 2015/11/1 商品转换功能
            ConditionParse condition = new ConditionParse();
            condition.addWhereStr();
            condition.addCondition("ProductExchangeConfigBean.srcProductId", "=", item.getProductId());
			List<ProductExchangeConfigVO> list = this.productExchangeConfigDAO.queryEntityVOsByCondition(condition);
            if (!ListTools.isEmptyOrNull(list)){
				ProductExchangeConfigVO vo = list.get(0);

                if (item.getAmount()*vo.getDestAmount()%vo.getSrcAmount() == 0){
                    item.setAmount(item.getAmount()*vo.getDestAmount()/vo.getSrcAmount());
                    item.setProductId(vo.getDestProductId());
                    item.setProductName(vo.getDestProductName());
                    _logger.info(item+" create package bean for product exchange:"+vo);
                } else{
                    _logger.warn(item+" does not match product exchange:"+vo);
                }
            } else{
				_logger.info("no ProductExchangeConfigVO found:"+item.getOutId());
			}

			itemList.add(item);
			
			allAmount += item.getAmount();
			
			if (!pmap.containsKey(base.getProductId()))
			{
				List<BaseBean> blist = new ArrayList<BaseBean>();
				
				blist.add(base);
				
				pmap.put(base.getProductId(), blist);
			}else
			{
				List<BaseBean> blist = pmap.get(base.getProductId());
				
				blist.add(base);
			}
		}
		
		packBean.setAmount(allAmount);
		
		if (isEmergency) {
			packBean.setEmergency(OutConstant.OUT_EMERGENCY_YES);
		}
		packBean.setProductCount(pmap.values().size());

		PackageVSCustomerBean vsBean = new PackageVSCustomerBean();
		
		vsBean.setPackageId(id);
		vsBean.setCustomerId(outBean.getCustomerId());
		vsBean.setCustomerName(outBean.getCustomerName());
		vsBean.setIndexPos(1);

		packBean.setPrintInvoiceinsStatus(itemList);
		packageDAO.saveEntityBean(packBean);
		packageItemDAO.saveAllEntityBeans(itemList);
		_logger.info(packBean.getId()+"***package created with size***"+itemList.size());

		packageVSCustomerDAO.saveEntityBean(vsBean);
	}

	/**
	 * for invoiceins
	 * @param ins
	 * @param distVO
	 * @param fullAddress
	 * @param location
	 */
	private void createNewInsPackage(InvoiceinsVO ins,
			List<InsVSInvoiceNumBean> numList, DistributionVO distVO, String fullAddress, String location)
	{
		_logger.info("****createNewInsPackage now****");
		String id = commonDAO.getSquenceString20("CK");
		
		int allAmount = 0;
		
		PackageBean packBean = new PackageBean();
		
		packBean.setId(id);
		packBean.setCustomerId(ins.getCustomerId());
		packBean.setShipping(distVO.getShipping());
		packBean.setTransport1(distVO.getTransport1());
		packBean.setExpressPay(distVO.getExpressPay());
		packBean.setTransport2(distVO.getTransport2());
		packBean.setTransportPay(distVO.getTransportPay());
		packBean.setAddress(fullAddress);
		packBean.setReceiver(distVO.getReceiver());
		packBean.setMobile(distVO.getMobile());
		packBean.setLocationId(location);
		packBean.setCityId(distVO.getCityId());
		
		packBean.setStafferName(ins.getStafferName());
		
		StafferVO staff = stafferDAO.findVO(ins.getStafferId());
		
		if (null != staff) {
			packBean.setIndustryName(staff.getIndustryName());
			packBean.setDepartName(staff.getIndustryName3());
		}
		
		packBean.setTotal(ins.getMoneys());
		packBean.setStatus(0);
		packBean.setLogTime(TimeTools.now());
		
		StringBuilder sb = getPrintTextForIns(ins);
		
		List<PackageItemBean> itemList = new ArrayList<PackageItemBean>();
		
		boolean first = false;
		
		for (InsVSInvoiceNumBean base : numList)
		{
			PackageItemBean item = new PackageItemBean();
			
			item.setPackageId(id);
			item.setOutId(ins.getId());
			item.setBaseId(base.getId());
			item.setProductId(base.getInvoiceNum());
			item.setProductName("发票号：" + base.getInvoiceNum());
			item.setAmount(1);
			item.setPrice(base.getMoneys());
			item.setValue(base.getMoneys());
			item.setOutTime(TimeTools.changeFormat(ins.getLogTime(), TimeTools.LONG_FORMAT, TimeTools.SHORT_FORMAT));
			item.setDescription(ins.getDescription());
			item.setCustomerId(ins.getCustomerId());
			if (!first) {
				item.setPrintText(sb.toString());	
			}
			
			first = true;
			
			itemList.add(item);
			
			allAmount += item.getAmount();
		}
		
		packBean.setAmount(allAmount);
		
		packBean.setProductCount(numList.size());
		
		PackageVSCustomerBean vsBean = new PackageVSCustomerBean();
		
		vsBean.setPackageId(id);
		vsBean.setCustomerId(ins.getCustomerId());
		vsBean.setCustomerName(ins.getCustomerName());
		vsBean.setIndexPos(1);

		packBean.setPrintInvoiceinsStatus(itemList);
		packageDAO.saveEntityBean(packBean);
		
		packageItemDAO.saveAllEntityBeans(itemList);
		
		packageVSCustomerDAO.saveEntityBean(vsBean);
	}

	/**
	 * 功能描述: <br>
	 * 〈功能详细描述〉
	 *
	 * @param ins
	 * @return
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	private StringBuilder getPrintTextForIns(InvoiceinsVO ins) {
		StringBuilder sb = new StringBuilder();
		
		List<InvoiceinsItemVO> insVOList = invoiceinsItemDAO.queryEntityVOsByFK(ins.getId());
		
		Set<String> uniqueSO = new HashSet<String>();
		
//		sb.append(ins.getCustomerName()).append(";商品明细：");
		
		for (InvoiceinsItemVO insVO : insVOList) {
//			sb.append(insVO.getProductName()).append(";");
			
			if (!uniqueSO.contains(insVO.getOutId()) && insVO.getType() == FinanceConstant.INSVSOUT_TYPE_OUT) {
				uniqueSO.add(insVO.getOutId());
			}
		}
		
		sb.append("银行订单号：");
		for (String outid : uniqueSO) {
			List<OutImportBean> outiList = outImportDAO.queryEntityBeansByFK(outid, AnoConstant.FK_FIRST);
			
			if (!ListTools.isEmptyOrNull(outiList))
			{
				if (!StringTools.isNullOrNone(outiList.get(0).getCiticNo())) {
					sb.append(outiList.get(0).getCiticNo()).append(";");
				}
			}
		}
		return sb;
	}

    //2015/1/13 update
    private void setInnerCondition(DistributionVO distVO, String location, ConditionParse con)
    {
       int shipping = distVO.getShipping();
       if (shipping == 0){
           //发货方式也必须一致
           con.addIntCondition("PackageBean.shipping", "=", distVO.getShipping());

            //自提：收货人，电话一致，才合并
            con.addCondition("PackageBean.receiver", "=", distVO.getReceiver());

            con.addCondition("PackageBean.mobile", "=", distVO.getMobile());

//            con.addIntCondition("PackageBean.status", "=", 0);
//		   con.addCondition("PackageBean.pickupId","=","");
		   con.addCondition(" and (PackageBean.pickupId ='' or PackageBean.pickupId IS NULL)");
		   con.addCondition(" and PackageBean.status in(0,5)");
        } else if (shipping == 2){
            //第三方快递：地址、收货人、电话完全一致，才合并.能不能判断地址后6个字符一致，电话，收货人一致，就合并
           String fullAddress = distVO.getProvinceName()+distVO.getCityName()+distVO.getAddress();
           String temp = fullAddress.trim();

           if (temp.length()>=6){
               con.addCondition("PackageBean.address", "like", "%"+temp.substring(temp.length()-6));
           }else{
               con.addCondition("PackageBean.address", "like", "%"+temp);
           }

           con.addIntCondition("PackageBean.shipping", "=", distVO.getShipping());

           con.addCondition("PackageBean.receiver", "=", distVO.getReceiver());

           con.addCondition("PackageBean.mobile", "=", distVO.getMobile());

//		   con.addIntCondition("PackageBean.status", "=", 0);
		   //#328
//		   con.addCondition("PackageBean.pickupId","=","");
		   con.addCondition(" and (PackageBean.pickupId ='' or PackageBean.pickupId IS NULL)");
		   con.addCondition(" and PackageBean.status in(0,5)");
        } else{
           //Keep default behavior
           //con.addCondition("PackageBean.customerId", "=", outBean.getCustomerId());
           con.addCondition("PackageBean.cityId", "=", distVO.getCityId());  // 借用outId 用于存储城市。生成出库单增加 城市 维度

           con.addIntCondition("PackageBean.shipping", "=", distVO.getShipping());

           con.addIntCondition("PackageBean.transport1", "=", distVO.getTransport1());

           con.addIntCondition("PackageBean.expressPay", "=", distVO.getExpressPay());

           con.addIntCondition("PackageBean.transport2", "=", distVO.getTransport2());

           con.addIntCondition("PackageBean.transportPay", "=", distVO.getTransportPay());

           con.addCondition("PackageBean.locationId", "=", location);

           con.addCondition("PackageBean.receiver", "=", distVO.getReceiver());

           con.addCondition("PackageBean.mobile", "=", distVO.getMobile());

//           con.addIntCondition("PackageBean.status", "=", 0);
//		   con.addCondition("PackageBean.pickupId","=","");
		   con.addCondition(" and (PackageBean.pickupId ='' or PackageBean.pickupId IS NULL)");
		   con.addCondition(" and PackageBean.status in(0,5)");
       }
    }

//	private void setInnerCondition(DistributionVO distVO, String location, ConditionParse con)
//	{
//		//con.addCondition("PackageBean.customerId", "=", outBean.getCustomerId());
//		con.addCondition("PackageBean.cityId", "=", distVO.getCityId());  // 借用outId 用于存储城市。生成出库单增加 城市 维度
//
//		con.addIntCondition("PackageBean.shipping", "=", distVO.getShipping());
//
//		con.addIntCondition("PackageBean.transport1", "=", distVO.getTransport1());
//
//		con.addIntCondition("PackageBean.expressPay", "=", distVO.getExpressPay());
//
//		con.addIntCondition("PackageBean.transport2", "=", distVO.getTransport2());
//
//		con.addIntCondition("PackageBean.transportPay", "=", distVO.getTransportPay());
//
//		con.addCondition("PackageBean.locationId", "=", location);
//
//		con.addCondition("PackageBean.receiver", "=", distVO.getReceiver());
//
//		con.addCondition("PackageBean.mobile", "=", distVO.getMobile());
//
//		con.addIntCondition("PackageBean.status", "=", 0);
//	}

	/**
	 * 
	 */
	public void createPackage(PreConsignBean pre, OutVO out) throws MYException
	{
		String location = "";
        String fullId = out.getFullId();
		
		// 通过仓库获取 仓库地点
		DepotBean depot = depotDAO.find(out.getLocation());
		
		if (depot != null)
			location = depot.getIndustryId2();
		
		List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(fullId);
		
		List<DistributionVO> distList = distributionDAO.queryEntityVOsByFK(fullId);
		
		if (ListTools.isEmptyOrNull(distList))
		{
			triggerLog.warn("======createPackage== (distList is null or empty)====" + fullId);
			preConsignDAO.deleteEntityBean(pre.getId());
			
			return;
		}
		
		DistributionVO distVO = distList.get(0);
		
		// 如果是空发,则不处理
		if (distVO.getShipping() == OutConstant.OUT_SHIPPING_NOTSHIPPING)
		{
			triggerLog.warn("======createPackage== (shipping is OUT_SHIPPING_NOTSHIPPING)====" + fullId);
			preConsignDAO.deleteEntityBean(pre.getId());
			
			return;
		}
		
		// 地址不全,不发
		if (distVO.getAddress().trim().equals("0") && distVO.getReceiver().trim().equals("0") && distVO.getMobile().trim().equals("0"))
		{
            triggerLog.warn("======address not complete==" + fullId);
			return;
		}
		
		String fullAddress = distVO.getProvinceName()+distVO.getCityName()+distVO.getAddress();
        String fullAddressTrim = fullAddress.trim();
		// 此客户是否存在同一个发货包裹,且未拣配
		ConditionParse con = new ConditionParse();
		
		con.addWhereStr();
		
		setInnerCondition(distVO, location, con);
		
		List<PackageVO> packageList = packageDAO.queryVOsByCondition(con);

		if (ListTools.isEmptyOrNull(packageList))
		{
            _logger.info("****create new package now***"+fullId);
			createNewPackage(out, baseList, distVO, fullAddressTrim, location);
		}else{
            _logger.info(location+"****package already exist***"+fullId);
            String id = packageList.get(0).getId();
			
			PackageBean packBean = packageDAO.find(id);
			
			// 不存在或已不是初始状态(可能已被拣配)
            if (null == packBean ||
                    (packBean.getStatus() != 0 && packBean.getStatus()!= ShipConstant.SHIP_STATUS_PRINT_INVOICEINS))
            {
                _logger.info(fullId+"****added to new package***");
				createNewPackage(out, baseList, distVO, fullAddressTrim, location);
			}else
			{
                //#18 2015/2/5 同一个CK单中的所有SO单必须location一致才能合并
                List<PackageItemBean> currentItems = this.packageItemDAO.queryEntityBeansByFK(packBean.getId());
                if (!ListTools.isEmptyOrNull(currentItems)){
					_logger.info(fullId+"****add SO to existent package "+packBean.getId()+" current size:"+currentItems.size());
                   PackageItemBean first = currentItems.get(0);
					if (fullId.contains("DB") && first.getOutId().contains("SO")){
						_logger.warn("***not merge with different out type***"+first.getOutId());
						createNewPackage(out, baseList, distVO, fullAddressTrim, location);
					}else{
						OutVO outBean = outDAO.findVO(first.getOutId());
						if (outBean!= null){
							String lo = outBean.getLocation();
							//#18 2016/6/15 如果销售单对应depot表中industryId2不一致，就不能合并。而不管仓库是否一致
							DepotBean depot2 = this.depotDAO.find(lo);
							if (depot2!= null && !location.equals(depot2.getIndustryId2())) {
								String msg = first.getOutId()+"****industryId2 is not same****"+out.getFullId();
								blockedLog.warn(out.getFullId()+":"+packBean.getId());
								_logger.warn(msg);
								preConsignDAO.deleteEntityBean(pre.getId());
								return ;
							}
						}

						//2015/2/15 检查重复SO单
						for (PackageItemBean p: currentItems){
							if (out.getFullId().equals(p.getOutId())){
								_logger.warn("****duplicate package item***"+fullId);
								preConsignDAO.deleteEntityBean(pre.getId());
								return;
							}
						}
					}
                } else{
                    _logger.warn("***no package items exist***"+packBean.getId());
                }
				List<PackageItemBean> itemList = new ArrayList<PackageItemBean>();
				
				int allAmount = 0;
				double total = 0;
				
				Map<String, List<BaseBean>> pmap = new HashMap<String, List<BaseBean>>();
				boolean isEmergency = false;
				for (BaseBean base : baseList)
				{
					PackageItemBean item = new PackageItemBean();
					
					item.setPackageId(id);
					item.setOutId(out.getFullId());
					item.setBaseId(base.getId());
					item.setProductId(base.getProductId());
					item.setProductName(base.getProductName());
					item.setAmount(Math.abs(base.getAmount()));
					item.setPrice(Math.abs(base.getPrice()));
					item.setValue(base.getValue());
					item.setOutTime(out.getOutTime());
					item.setDescription(out.getDescription());
					item.setCustomerId(out.getCustomerId());
					item.setEmergency(out.getEmergency());
					
					if (item.getEmergency() == 1) {
						isEmergency = true;
					}
					
					itemList.add(item);
					
					allAmount += item.getAmount();
					total += base.getValue();
					
					if (!pmap.containsKey(base.getProductId()))
					{
						List<BaseBean> blist = new ArrayList<BaseBean>();
						
						blist.add(base);
						
						pmap.put(base.getProductId(), blist);
					}else
					{
						List<BaseBean> blist = pmap.get(base.getProductId());
						
						blist.add(base);
					}
				}

				packBean.setAmount(packBean.getAmount() + allAmount);
				packBean.setTotal(packBean.getTotal() + total);
				packBean.setProductCount(packBean.getProductCount() + pmap.values().size());
				
				if (isEmergency) {
					packBean.setEmergency(OutConstant.OUT_EMERGENCY_YES);
				}
				
				packageDAO.updateEntityBean(packBean);
				
				packageItemDAO.saveAllEntityBeans(itemList);
				
				// 包与客户关系
				PackageVSCustomerBean vsBean = packageVSCustomerDAO.findByUnique(id, out.getCustomerId());
				
				if (null == vsBean)
				{
					int count = packageVSCustomerDAO.countByCondition("where packageId = ?", id);
					
					PackageVSCustomerBean newvsBean = new PackageVSCustomerBean();
					
					newvsBean.setPackageId(id);
					newvsBean.setCustomerId(out.getCustomerId());
					newvsBean.setCustomerName(out.getCustomerName());
					newvsBean.setIndexPos(count + 1);
					
					packageVSCustomerDAO.saveEntityBean(newvsBean);
				}
			}
		}
		
		preConsignDAO.deleteEntityBean(pre.getId());
	}
	
	/**
	 * 
	 */
	public void createInsPackage(PreConsignBean pre, String insId) throws MYException
	{
		String location = "";
		
		// 通过仓库获取 仓库地点
		DepotBean depot = depotDAO.find(DepotConstant.CENTER_DEPOT_ID);
		
		if (depot != null)
			location = depot.getIndustryId2();
		
		InvoiceinsVO ins = invoiceinsDAO.findVO(insId);
		
		if (null == ins) {
			_logger.error("ins not found***"+insId);
			preConsignDAO.deleteEntityBean(pre.getId());
			return;
		}
		
		List<InsVSInvoiceNumBean> numList = insVSInvoiceNumDAO.queryEntityBeansByFK(insId);
		
		if (ListTools.isEmptyOrNull(numList)) {
			_logger.warn("======createInsPackage== (numList is null or empty)====" + insId);
			return;
		}
		
		List<DistributionVO> distList = distributionDAO.queryEntityVOsByFK(ins.getId());
		
		if (ListTools.isEmptyOrNull(distList))
		{
			_logger.warn("======createInsPackage==(distList is null or empty)====" + insId);
			preConsignDAO.deleteEntityBean(pre.getId());
			return;
		}
		
		DistributionVO distVO = distList.get(0);
		
		// 如果是空发,则不处理
		if (distVO.getShipping() == OutConstant.OUT_SHIPPING_NOTSHIPPING) {
			_logger.warn("======createInsPackage==(shipping is OUT_SHIPPING_NOTSHIPPING)====" + insId);
			preConsignDAO.deleteEntityBean(pre.getId());
			return;
		}
		
		// 历史数据无配送信息，pass
		if (StringTools.isNullOrNone(distVO.getAddress())
				&& StringTools.isNullOrNone(distVO.getReceiver())
				&& StringTools.isNullOrNone(distVO.getMobile())) {
			_logger.warn("======createInsPackage==(distList detail is null or empty)====" + insId);
			preConsignDAO.deleteEntityBean(pre.getId());
			return;
		}
		
		// 地址不全,不发
		if (distVO.getAddress().trim().equals("0") && distVO.getReceiver().trim().equals("0") && distVO.getMobile().trim().equals("0"))
		{
			_logger.warn("***wrong address***");
			return;
		}
		
		String fullAddress = distVO.getProvinceName()+distVO.getCityName()+distVO.getAddress();
		
		// 此客户是否存在同一个发货包裹,且未拣配
		ConditionParse con = new ConditionParse();
		
		con.addWhereStr();
		
		setInnerCondition(distVO, location, con);
		List<PackageVO> packageList = packageDAO.queryVOsByCondition(con);

		if (ListTools.isEmptyOrNull(packageList))
		{
			createNewInsPackage(ins, numList, distVO, fullAddress, location);
		}else{
			String id = packageList.get(0).getId();
			
			PackageBean packBean = packageDAO.find(id);
			
			// 不存在或已不是初始状态(可能已被拣配)或打印发票状态
			if (null == packBean ||
				(packBean.getStatus() != 0 && packBean.getStatus()!= ShipConstant.SHIP_STATUS_PRINT_INVOICEINS))
			{
				_logger.info("***packBean wrong status***"+packBean);
				createNewInsPackage(ins, numList, distVO, fullAddress, location);
			}else
			{
				//#200 合并入现有CK单时检查是否有重复outId
				List<PackageItemBean> itemList = new ArrayList<PackageItemBean>();
				
				int allAmount = 0;
				double total = 0;
				
				StringBuilder sb = getPrintTextForIns(ins);
				
				boolean first = false;
				
				for (InsVSInvoiceNumBean base : numList)
				{
					if (this.contains(itemList, insId)){
						_logger.warn(insId+"***insId already packaged***"+packBean.getId());
						continue;
					}
					PackageItemBean item = new PackageItemBean();
					
					item.setPackageId(id);
					item.setOutId(ins.getId());
					item.setBaseId(base.getId());
					item.setProductId(base.getInvoiceNum());
					item.setProductName("发票号：" + base.getInvoiceNum());
					item.setAmount(1);
					item.setPrice(base.getMoneys());
					item.setValue(base.getMoneys());
					item.setOutTime(TimeTools.changeFormat(ins.getLogTime(), TimeTools.LONG_FORMAT, TimeTools.SHORT_FORMAT));
					item.setDescription(ins.getDescription());
					item.setCustomerId(ins.getCustomerId());
					if (!first) {
						item.setPrintText(sb.toString());	
					}
					
					first = true;
					
					itemList.add(item);
					
					allAmount += item.getAmount();
					total += base.getMoneys();
				}
				
				packBean.setAmount(packBean.getAmount() + allAmount);
				packBean.setTotal(packBean.getTotal() + total);
				packBean.setProductCount(packBean.getProductCount() + numList.size());
				
				packageDAO.updateEntityBean(packBean);
				_logger.info(insId+"***merge to exist package***"+packBean.getId());

				if (!ListTools.isEmptyOrNull(itemList)) {
					packageItemDAO.saveAllEntityBeans(itemList);
				}
				
				// 包与客户关系
				PackageVSCustomerBean vsBean = packageVSCustomerDAO.findByUnique(id, ins.getCustomerId());
				
				if (null == vsBean)
				{
					int count = packageVSCustomerDAO.countByCondition("where packageId = ?", id);
					
					PackageVSCustomerBean newvsBean = new PackageVSCustomerBean();
					
					newvsBean.setPackageId(id);
					newvsBean.setCustomerId(ins.getCustomerId());
					newvsBean.setCustomerName(ins.getCustomerName());
					newvsBean.setIndexPos(count + 1);
					
					packageVSCustomerDAO.saveEntityBean(newvsBean);
				}
			}
		}

		_logger.info("***delete preconsign***"+pre.getId());
		preConsignDAO.deleteEntityBean(pre.getId());
	}

	private boolean contains(List<PackageItemBean> items, String insId){
		for (PackageItemBean item : items){
			if (item.getOutId().equals(insId)){
				return true;
			}
		}
		return false;
	}
    /**
     *  2015/3/1 预开票也需要进入CK单
     * @param pre
     * @param bean
     * @throws MYException
     */
    public void createPreInsPackage(PreConsignBean pre, PreInvoiceApplyVO bean) throws MYException
    {
        // 此客户是否存在同一个发货包裹,且未拣配
        ConditionParse con = new ConditionParse();
        con.addWhereStr();

        String fullAddress = bean.getAddress();
        String temp = fullAddress.trim();

        if (temp.length()>=6){
            con.addCondition("PackageBean.address", "like", "%"+temp.substring(temp.length()-6));
        }else{
            con.addCondition("PackageBean.address", "like", "%"+temp);
        }

        con.addCondition("PackageBean.receiver", "=", bean.getReceiver());

        con.addCondition("PackageBean.mobile", "=", bean.getMobile());

        con.addIntCondition("PackageBean.status", "=", 0);


        List<PackageVO> packageList = packageDAO.queryVOsByCondition(con);

//		if (packageList.size() > 1){
//			throw new MYException("数据异常,生成发货单出错.");
//		}

        if (ListTools.isEmptyOrNull(packageList))
        {
            //TODO
            createNewPreInsPackage(bean);
        }else{
            String id = packageList.get(0).getId();

            PackageBean packBean = packageDAO.find(id);

            // 不存在或已不是初始状态(可能已被拣配)
            if (null == packBean ||
                    (packBean.getStatus() != 0 && packBean.getStatus()!= ShipConstant.SHIP_STATUS_PRINT_INVOICEINS))
            {
                createNewPreInsPackage(bean);
            }else
            {
                List<PackageItemBean> itemList = new ArrayList<PackageItemBean>();
                PackageItemBean item = new PackageItemBean();

                item.setPackageId(id);
                item.setOutId(bean.getId());
                item.setBaseId(bean.getId());
				//2015/12/8 发票号作为productId
                item.setProductId(bean.getInvoiceNumber());
                item.setProductName("发票号：" + bean.getInvoiceNumber());
                item.setAmount(1);
                item.setPrice(bean.getInvoiceMoney());
                item.setValue(bean.getInvoiceMoney());
                item.setOutTime(TimeTools.changeFormat(bean.getLogTime(), TimeTools.LONG_FORMAT, TimeTools.SHORT_FORMAT));
                item.setDescription(bean.getDescription());
                item.setCustomerId(bean.getCustomerId());
                item.setPrintText("test text");

                itemList.add(item);

                packBean.setAmount(packBean.getAmount() + 1);
                packBean.setTotal(packBean.getTotal() + bean.getInvoiceMoney());
                packBean.setProductCount(packBean.getProductCount() + 1);

                packageDAO.updateEntityBean(packBean);

                packageItemDAO.saveAllEntityBeans(itemList);

                // 包与客户关系
                PackageVSCustomerBean vsBean = packageVSCustomerDAO.findByUnique(id, bean.getCustomerId());

                if (null == vsBean)
                {
                    int count = packageVSCustomerDAO.countByCondition("where packageId = ?", id);

                    PackageVSCustomerBean newvsBean = new PackageVSCustomerBean();

                    newvsBean.setPackageId(id);
                    newvsBean.setCustomerId(bean.getCustomerId());
                    newvsBean.setCustomerName(bean.getCustomerName());
                    newvsBean.setIndexPos(count + 1);

                    packageVSCustomerDAO.saveEntityBean(newvsBean);
                }
            }
        }

        preConsignDAO.deleteEntityBean(pre.getId());
    }

    private void createNewPreInsPackage(PreInvoiceApplyVO ins)
    {
        String id = commonDAO.getSquenceString20("CK");

        PackageBean packBean = new PackageBean();

        packBean.setId(id);
        packBean.setCustomerId(ins.getCustomerId());

        packBean.setShipping(ins.getShipping());
        packBean.setTransport1(ins.getTransport1());
        packBean.setExpressPay(ins.getExpressPay());
        packBean.setTransport2(ins.getTransport2());
        packBean.setTransportPay(ins.getTransportPay());
        packBean.setCityId(ins.getCityId());

        packBean.setLocationId(packBean.getLocationId());
        packBean.setAddress(ins.getAddress());
        packBean.setReceiver(ins.getReceiver());
        packBean.setMobile(ins.getMobile());

        packBean.setStafferName(ins.getStafferName());

        StafferVO staff = stafferDAO.findVO(ins.getStafferId());

        if (null != staff) {
            packBean.setIndustryName(staff.getIndustryName());
            packBean.setDepartName(staff.getIndustryName3());
        }

        packBean.setTotal(ins.getTotal());
        packBean.setStatus(0);
        packBean.setLogTime(TimeTools.now());

//        StringBuilder sb = getPrintTextForIns(ins);

        List<PackageItemBean> itemList = new ArrayList<PackageItemBean>();

        PackageItemBean item = new PackageItemBean();

        item.setPackageId(id);
        item.setOutId(ins.getId());

        item.setProductName("发票号：" + ins.getInvoiceNumber());
		//2015/12/8 预开票申请已发票号作为productId
		item.setProductId(ins.getInvoiceNumber());

        item.setAmount(1);
        item.setPrice(ins.getInvoiceMoney());
        item.setValue(ins.getInvoiceMoney());
        item.setOutTime(TimeTools.changeFormat(ins.getLogTime(), TimeTools.LONG_FORMAT, TimeTools.SHORT_FORMAT));
        item.setDescription(ins.getDescription());
        item.setCustomerId(ins.getCustomerId());
        //TODO
        item.setPrintText("test print text");

        itemList.add(item);


        packBean.setAmount(1);

        packBean.setProductCount(1);

        PackageVSCustomerBean vsBean = new PackageVSCustomerBean();

        vsBean.setPackageId(id);
        vsBean.setCustomerId(ins.getCustomerId());
        vsBean.setCustomerName(ins.getCustomerName());
        vsBean.setIndexPos(1);

		packBean.setPrintInvoiceinsStatus(itemList);
        packageDAO.saveEntityBean(packBean);
        _logger.info("***create new package for preinvoice****"+ins.getId());

        packageItemDAO.saveAllEntityBeans(itemList);

        packageVSCustomerDAO.saveEntityBean(vsBean);
    }

    /**
     * 2015/2/3 票随货发合并订单及发票
     * @param outIdList
     * @throws MYException
     */
    @Override
    @Deprecated
    public void createPackage(final List<String> outIdList) throws MYException {
        //To change body of implemented methods use File | Settings | File Templates.
        if (!ListTools.isEmptyOrNull(outIdList)){
            long statsStar = System.currentTimeMillis();

            TransactionTemplate tran = new TransactionTemplate(transactionManager);

            try
            {
                tran.execute(new TransactionCallback()
                {
                    public Object doInTransaction(TransactionStatus arg0)
                    {
                        try{
                            processOut2(outIdList);
                        }catch(MYException e)
                        {
                            throw new RuntimeException(e);
                        }

                        return Boolean.TRUE;
                    }
                });
            }
            catch (Exception e)
            {
                triggerLog.error(e, e);
            }

            triggerLog.info("createPackage 票随货发合并订单及发票统计结束... ,共耗时："+ (System.currentTimeMillis() - statsStar));
         }
    }

    private void processOut2(List<String> outIdList) throws MYException{
        for (int i=0;i<outIdList.size();i++) {
            String outId = outIdList.get(i);
            InvoiceinsBean insBean = invoiceinsDAO.find(outId);
            if (insBean!= null){
                insBean.setPackaged(1);
                this.invoiceinsDAO.updateEntityBean(insBean);
                _logger.info("InsVSOutBean updated to packaged***"+outId);
            }
        }
    }

//    private void processOut2(List<String> outIdList) throws MYException{
//        PackageBean packBean = new PackageBean();
//        String customerId = null;
//        String customerName = null;
//        List<PackageItemBean> itemList = new ArrayList<PackageItemBean>();
//        String id = commonDAO.getSquenceString20("CK");
//        List<InvoiceinsBean> insBeans = new ArrayList<InvoiceinsBean>();
//        _logger.info("****createPackage11111111111111111111***********");
//        for (int i=0;i<outIdList.size();i++) {
//            String outId = outIdList.get(i);
//            OutVO outBean = outDAO.findVO(outId);
//            InvoiceinsBean insBean = null;
//            List<BaseBean> baseList = new ArrayList<BaseBean>();
//            List<InsVSInvoiceNumBean> numList = new ArrayList<InsVSInvoiceNumBean>();
//            Map<String, List<BaseBean>> pmap = new HashMap<String, List<BaseBean>>();
//            _logger.info("****createPackage2222222222222222222***********");
//            if (outBean == null){
//                insBean = invoiceinsDAO.find(outId);
//                if (insBean!= null){
//                    numList = insVSInvoiceNumDAO.queryEntityBeansByFK(insBean.getId());
//                    insBeans.add(insBean);
//                    _logger.info("****insBean found***********"+outId);
//                }
//            } else{
//                if (customerId == null){
//                    customerId = outBean.getCustomerId();
//                }
//                if (customerName == null){
//                    customerName = outBean.getCustomerName();
//                }
//                baseList = baseDAO.queryEntityBeansByFK(outId);
//                _logger.info("****createPackage baseList size***********"+baseList.size());
//            }
//
//            int allAmount = 0;
//
//            if (i == 0){
//
//                List<DistributionVO> distList = distributionDAO.queryEntityVOsByFK(outId);
//
//                if (ListTools.isEmptyOrNull(distList))
//                {
//                    _logger.info("======createPackage== (distList is null or empty)====" + outId);
//                    return;
//                }
//
//                DistributionVO distVO = distList.get(0);
//
//                String location = "";
//
//                // 通过仓库获取 仓库地点
//                DepotBean depot = depotDAO.find(outBean.getLocation());
//
//                if (depot != null)
//                    location = depot.getIndustryId2();
//
////                    List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(outId);
//
//                // 如果是空发,则不处理
//                if (distVO.getShipping() == OutConstant.OUT_SHIPPING_NOTSHIPPING)
//                {
//                    _logger.info("======createPackage== (shipping is OUT_SHIPPING_NOTSHIPPING)====" + outId);
//                    return;
//                }
//
//                // 地址不全,不发
//                if (distVO.getAddress().trim().equals("0") && distVO.getReceiver().trim().equals("0") && distVO.getMobile().trim().equals("0"))
//                {
//                    _logger.info("======address not complete==" + outId);
//                    return;
//                }
//
//                String fullAddress = distVO.getProvinceName()+distVO.getCityName()+distVO.getAddress();
//
//
//                packBean.setId(id);
//                packBean.setCustomerId(outBean.getCustomerId());
//                packBean.setShipping(distVO.getShipping());
//                packBean.setTransport1(distVO.getTransport1());
//                packBean.setExpressPay(distVO.getExpressPay());
//                packBean.setTransport2(distVO.getTransport2());
//                packBean.setTransportPay(distVO.getTransportPay());
//                packBean.setAddress(fullAddress);
//                packBean.setReceiver(distVO.getReceiver());
//                packBean.setMobile(distVO.getMobile());
//                packBean.setLocationId(location);
//                packBean.setCityId(distVO.getCityId());
//                packBean.setStafferName(outBean.getStafferName());
//
//                StafferVO staff = stafferDAO.findVO(outBean.getStafferId());
//
//                if (null != staff) {
//                    packBean.setIndustryName(staff.getIndustryName());
//                    packBean.setDepartName(staff.getIndustryName3());
//                }
//
//                //TODO
////                    packBean.setTotal(outBean.getMoneys());
//                packBean.setStatus(0);
//                packBean.setLogTime(TimeTools.now());
//
//                _logger.info("****createPackage000000000000000000000***********");
//            }
//
//            double total = 0.0;
//            if (!ListTools.isEmptyOrNull(baseList)){
//                boolean isEmergency = false;
//
//                for (BaseBean base : baseList)
//                {
//                    PackageItemBean item = new PackageItemBean();
//
//                    item.setPackageId(id);
//                    item.setOutId(outBean.getFullId());
//                    item.setBaseId(base.getId());
//                    item.setProductId(base.getProductId());
//                    item.setProductName(base.getProductName());
//                    item.setAmount(base.getAmount());
//                    item.setPrice(base.getPrice());
//                    item.setValue(base.getValue());
//                    item.setOutTime(outBean.getOutTime());
//                    item.setDescription(outBean.getDescription());
//                    item.setCustomerId(outBean.getCustomerId());
//                    item.setEmergency(outBean.getEmergency());
//                    total += base.getValue();
//
//                    if (item.getEmergency() == 1) {
//                        isEmergency = true;
//                        packBean.setEmergency(OutConstant.OUT_EMERGENCY_YES);
//                    }
//
//                    itemList.add(item);
//
//                    allAmount += item.getAmount();
//
//                    if (!pmap.containsKey(base.getProductId()))
//                    {
//                        List<BaseBean> blist = new ArrayList<BaseBean>();
//
//                        blist.add(base);
//
//                        pmap.put(base.getProductId(), blist);
//                    }else
//                    {
//                        List<BaseBean> blist = pmap.get(base.getProductId());
//
//                        blist.add(base);
//                    }
//                }
//            }
//            _logger.info("****createPackage44444444444444444444***********");
//            if (!ListTools.isEmptyOrNull(numList)){
//                for (InsVSInvoiceNumBean base : numList)
//                {
//                    PackageItemBean item = new PackageItemBean();
//
//                    item.setPackageId(id);
//                    item.setOutId(insBean.getId());
//                    item.setBaseId(base.getId());
//                    item.setProductId(base.getInvoiceNum());
//                    item.setProductName("发票号：" + base.getInvoiceNum());
//                    item.setAmount(1);
//                    item.setPrice(base.getMoneys());
//                    item.setValue(base.getMoneys());
//                    item.setOutTime(TimeTools.changeFormat(insBean.getLogTime(), TimeTools.LONG_FORMAT, TimeTools.SHORT_FORMAT));
//                    item.setDescription(insBean.getDescription());
//                    item.setCustomerId(insBean.getCustomerId());
//
//                    itemList.add(item);
//
//                    allAmount += item.getAmount();
//                }
//            }
//
//            _logger.info("****createPackage55555555555555555***********");
//            packBean.setAmount(packBean.getAmount() + allAmount);
//            packBean.setTotal(packBean.getTotal() + total);
//            packBean.setProductCount(packBean.getProductCount() + pmap.values().size());
//        }
//
//        if (!StringTools.isNullOrNone(packBean.getId())){
//            this.packageDAO.saveEntityBean(packBean);
//            _logger.info("****package is created****"+packBean.getId());
//            packageItemDAO.saveAllEntityBeans(itemList);
//
//            PackageVSCustomerBean vsBean = new PackageVSCustomerBean();
//
//            vsBean.setPackageId(id);
//            vsBean.setCustomerId(customerId);
//            vsBean.setCustomerName(customerName);
//            vsBean.setIndexPos(1);
//            packageVSCustomerDAO.saveEntityBean(vsBean);
//
//            for (InvoiceinsBean insBean :insBeans){
//                insBean.setPackaged(1);
//                this.invoiceinsDAO.updateEntityBean(insBean);
//                _logger.info("********InvoiceinsBean is packaged***********"+insBean.getId());
//            }
//        }
//    }


    /**
	 * @return the preConsignDAO
	 */
	public PreConsignDAO getPreConsignDAO()
	{
		return preConsignDAO;
	}

	/**
	 * @param preConsignDAO the preConsignDAO to set
	 */
	public void setPreConsignDAO(PreConsignDAO preConsignDAO)
	{
		this.preConsignDAO = preConsignDAO;
	}

	/**
	 * @return the packageDAO
	 */
	public PackageDAO getPackageDAO()
	{
		return packageDAO;
	}

	/**
	 * @param packageDAO the packageDAO to set
	 */
	public void setPackageDAO(PackageDAO packageDAO)
	{
		this.packageDAO = packageDAO;
	}

	/**
	 * @return the packageItemDAO
	 */
	public PackageItemDAO getPackageItemDAO()
	{
		return packageItemDAO;
	}

	/**
	 * @param packageItemDAO the packageItemDAO to set
	 */
	public void setPackageItemDAO(PackageItemDAO packageItemDAO)
	{
		this.packageItemDAO = packageItemDAO;
	}

	/**
	 * @return the outDAO
	 */
	public OutDAO getOutDAO()
	{
		return outDAO;
	}

	/**
	 * @param outDAO the outDAO to set
	 */
	public void setOutDAO(OutDAO outDAO)
	{
		this.outDAO = outDAO;
	}

	/**
	 * @return the baseDAO
	 */
	public BaseDAO getBaseDAO()
	{
		return baseDAO;
	}

	/**
	 * @param baseDAO the baseDAO to set
	 */
	public void setBaseDAO(BaseDAO baseDAO)
	{
		this.baseDAO = baseDAO;
	}

	/**
	 * @return the distributionDAO
	 */
	public DistributionDAO getDistributionDAO()
	{
		return distributionDAO;
	}

	/**
	 * @param distributionDAO the distributionDAO to set
	 */
	public void setDistributionDAO(DistributionDAO distributionDAO)
	{
		this.distributionDAO = distributionDAO;
	}

	/**
	 * @return the commonDAO
	 */
	public CommonDAO getCommonDAO()
	{
		return commonDAO;
	}

	/**
	 * @param commonDAO the commonDAO to set
	 */
	public void setCommonDAO(CommonDAO commonDAO)
	{
		this.commonDAO = commonDAO;
	}

	/**
	 * @return the transactionManager
	 */
	public PlatformTransactionManager getTransactionManager()
	{
		return transactionManager;
	}

	/**
	 * @param transactionManager the transactionManager to set
	 */
	public void setTransactionManager(PlatformTransactionManager transactionManager)
	{
		this.transactionManager = transactionManager;
	}

	/**
	 * @return the depotDAO
	 */
	public DepotDAO getDepotDAO()
	{
		return depotDAO;
	}

	/**
	 * @param depotDAO the depotDAO to set
	 */
	public void setDepotDAO(DepotDAO depotDAO)
	{
		this.depotDAO = depotDAO;
	}

	/**
	 * @return the packageVSCustomerDAO
	 */
	public PackageVSCustomerDAO getPackageVSCustomerDAO()
	{
		return packageVSCustomerDAO;
	}

	/**
	 * @param packageVSCustomerDAO the packageVSCustomerDAO to set
	 */
	public void setPackageVSCustomerDAO(PackageVSCustomerDAO packageVSCustomerDAO)
	{
		this.packageVSCustomerDAO = packageVSCustomerDAO;
	}

	public InvoiceinsDAO getInvoiceinsDAO() {
		return invoiceinsDAO;
	}

	public void setInvoiceinsDAO(InvoiceinsDAO invoiceinsDAO) {
		this.invoiceinsDAO = invoiceinsDAO;
	}

	public InsVSInvoiceNumDAO getInsVSInvoiceNumDAO() {
		return insVSInvoiceNumDAO;
	}

	public void setInsVSInvoiceNumDAO(InsVSInvoiceNumDAO insVSInvoiceNumDAO) {
		this.insVSInvoiceNumDAO = insVSInvoiceNumDAO;
	}

	public StafferDAO getStafferDAO() {
		return stafferDAO;
	}

	public void setStafferDAO(StafferDAO stafferDAO) {
		this.stafferDAO = stafferDAO;
	}

	/**
	 * @return the invoiceinsItemDAO
	 */
	public InvoiceinsItemDAO getInvoiceinsItemDAO() {
		return invoiceinsItemDAO;
	}

	/**
	 * @param invoiceinsItemDAO the invoiceinsItemDAO to set
	 */
	public void setInvoiceinsItemDAO(InvoiceinsItemDAO invoiceinsItemDAO) {
		this.invoiceinsItemDAO = invoiceinsItemDAO;
	}

	/**
	 * @return the outImportDAO
	 */
	public OutImportDAO getOutImportDAO() {
		return outImportDAO;
	}

	/**
	 * @param outImportDAO the outImportDAO to set
	 */
	public void setOutImportDAO(OutImportDAO outImportDAO) {
		this.outImportDAO = outImportDAO;
	}

    public OutManager getOutManager() {
        return outManager;
    }

    public void setOutManager(OutManager outManager) {
        this.outManager = outManager;
    }


    /**
     * @return the preInvoiceApplyDAO
     */
    public PreInvoiceApplyDAO getPreInvoiceApplyDAO() {
        return preInvoiceApplyDAO;
    }


    /**
     * @param preInvoiceApplyDAO the preInvoiceApplyDAO to set
     */
    public void setPreInvoiceApplyDAO(PreInvoiceApplyDAO preInvoiceApplyDAO) {
        this.preInvoiceApplyDAO = preInvoiceApplyDAO;
    }

	public ProductExchangeConfigDAO getProductExchangeConfigDAO() {
		return productExchangeConfigDAO;
	}

	public void setProductExchangeConfigDAO(ProductExchangeConfigDAO productExchangeConfigDAO) {
		this.productExchangeConfigDAO = productExchangeConfigDAO;
	}
}
