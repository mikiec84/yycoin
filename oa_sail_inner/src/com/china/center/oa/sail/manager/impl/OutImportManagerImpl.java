package com.china.center.oa.sail.manager.impl;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import com.china.center.oa.client.vo.CustomerVO;
import com.china.center.oa.product.constant.DepotConstant;
import com.china.center.oa.product.helper.StorageRelationHelper;
import com.china.center.oa.publics.bean.*;
import com.china.center.oa.publics.dao.*;
import com.china.center.oa.publics.vo.StafferVO;
import com.china.center.oa.sail.bean.*;
import com.china.center.oa.sail.bean.BaseBean;
import com.china.center.oa.sail.dao.*;
import com.china.center.oa.sail.vo.BaseVO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.client.bean.AddressBean;
import com.china.center.oa.client.bean.CustomerBean;
import com.china.center.oa.client.dao.AddressDAO;
import com.china.center.oa.client.dao.CustomerMainDAO;
import com.china.center.oa.client.dao.StafferVSCustomerDAO;
import com.china.center.oa.client.vo.AddressVO;
import com.china.center.oa.client.vo.StafferVSCustomerVO;
import com.china.center.oa.product.bean.DepotpartBean;
import com.china.center.oa.product.bean.PriceConfigBean;
import com.china.center.oa.product.bean.ProductBean;
import com.china.center.oa.product.constant.ProductConstant;
import com.china.center.oa.product.dao.DepotpartDAO;
import com.china.center.oa.product.dao.PriceConfigDAO;
import com.china.center.oa.product.dao.ProductDAO;
import com.china.center.oa.product.dao.ProductVSGiftDAO;
import com.china.center.oa.product.dao.StorageRelationDAO;
import com.china.center.oa.product.manager.PriceConfigManager;
import com.china.center.oa.product.manager.StorageRelationManager;
import com.china.center.oa.product.vo.ProductVSGiftVO;
import com.china.center.oa.product.vs.StorageRelationBean;
import com.china.center.oa.product.wrap.ProductChangeWrap;
import com.china.center.oa.publics.constant.IDPrefixConstant;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.constanst.OutImportConstant;
import com.china.center.oa.sail.constanst.SailConstant;
import com.china.center.oa.sail.helper.OutHelper;
import com.china.center.oa.sail.manager.OutImportManager;
import com.china.center.oa.sail.manager.OutManager;
import com.china.center.oa.sail.manager.SailConfigManager;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.ListTools;
import com.china.center.tools.MathTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;

public class OutImportManagerImpl implements OutImportManager
{
	private final Log _logger = LogFactory.getLog(getClass());
	
	private final Log operationLog = LogFactory.getLog("opr");
	
	private CommonDAO commonDAO = null;
	
	private OutImportDAO outImportDAO = null;
	
	private StorageRelationDAO storageRelationDAO = null;
	
	private ProductDAO productDAO = null;
	
	private FlowLogDAO flowLogDAO = null;
	
	private LocationDAO locationDAO = null;
	
	private StafferDAO stafferDAO = null;
	
    private OutDAO outDAO = null;

    private BaseDAO baseDAO = null;
    
    private DistributionDAO distributionDAO = null; 
	
    private OutImportResultDAO outImportResultDAO  = null;
    
    private ReplenishmentDAO replenishmentDAO  = null;
    
    private ConsignDAO consignDAO = null;
    
    private OutImportLogDAO outImportLogDAO = null;
    
    private SailConfigManager sailConfigManager = null;
    
	private StorageRelationManager storageRelationManager = null;
	
	private PlatformTransactionManager transactionManager = null;
	
	private CustomerMainDAO customerMainDAO = null;
	
	private StafferVSCustomerDAO stafferVSCustomerDAO = null;
	
	private ProvinceDAO provinceDAO = null;
	
	private CityDAO cityDAO = null;
	
	private AreaDAO areaDAO = null;
	
	private PriceConfigDAO priceConfigDAO = null;
	
	private PriceConfigManager priceConfigManager = null;
	
	private DistributionBaseDAO distributionBaseDAO = null;
	
	private BatchApproveDAO batchApproveDAO = null;
	
	private BatchSwatchDAO batchSwatchDAO = null;
	
	private OutManager outManager = null;
	
	private AddressDAO addressDAO = null;
	
	private ProductVSGiftDAO productVSGiftDAO = null;
	
	private DepotpartDAO depotpartDAO = null;
	
	private PackageItemDAO packageItemDAO = null;

    private PackageDAO packageDAO = null;
	
	private BatchReturnLogDAO batchReturnLogDAO = null;
	
	private BankSailDAO bankSailDAO = null;
	
	private EstimateProfitDAO estimateProfitDAO = null;

    private ImapMailClient imapMailClient = null;

    private OlOutDAO olOutDAO = null;

    private OlBaseDAO olBaseDAO = null;

    private InvoiceDAO invoiceDAO = null;

    private OutBackItemDAO outBackItemDAO = null;

    private OutBackDAO outBackDAO = null;

	private final static String SPLIT = "_";
	
	public OutImportManagerImpl()
	{
		
	}
	
	@Transactional(rollbackFor = MYException.class)
	public String addBean(List<OutImportBean> list)
			throws MYException
	{
		JudgeTools.judgeParameterIsNull(list);
		
        String id = commonDAO.getSquenceString20();
        
        int i = 0;
        try{
        
            for (OutImportBean each : list)
            {
                //each.setItype(0);

                each.setBatchId(id);

                each.setId(++i);

				if(each.getImportFromMail() == 1 && each.getStatus() == 3){
					continue;
				}else{
					each.setStatus(OutImportConstant.STATUS_INIT);
				}

                if (each.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT
                        && each.getPrice() != 0)
                {
                    throw new MYException("赠送类型订单单价须为0");
                }
            }

            outImportDAO.saveAllEntityBeans(list);
        }catch(Exception e){
            e.printStackTrace();
            _logger.error("Fail to import out:",e);
        }
        
        // 清除 10天前的数据至备份表
        //delHisData();
        
		return id;
	}
	
	@Transactional(rollbackFor = MYException.class)
	public String addPufaBean(List<OutImportBean> list)
			throws MYException
	{
		JudgeTools.judgeParameterIsNull(list);
		
        String id = commonDAO.getSquenceString20();
        
        int i = 0;
        
        for (OutImportBean each : list)
        {
        	//each.setItype(1); // 浦发
        	
        	each.setBatchId(id);
        	
        	each.setId(++i);
        	
        	each.setStatus(OutImportConstant.STATUS_INIT);
        	
        	if (each.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT
        			&& each.getPrice() != 0)
        	{
        		throw new MYException("赠送类型订单单价须为0");
        	}
        }
        
        outImportDAO.saveAllEntityBeans(list);
        
		return id;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public boolean process(final List<OutImportBean> list)
			throws MYException
	{
		JudgeTools.judgeParameterIsNull(list);
		_logger.info("***begin process imported item***" + list.size());

		OutImportBean bean = list.get(0);
		
		check(bean);
		
		saveLogInner(bean, OutImportConstant.LOGSTATUS_ING, "处理中...");
		
		try
		{
			TransactionTemplate tran = new TransactionTemplate(transactionManager);
			
			tran.execute(new TransactionCallback()
			{
				public Object doInTransaction(TransactionStatus status)
				{
					processInner(list);
					
					return Boolean.TRUE;
				}
			}
			);
		}
		catch (TransactionException e)
        {
			saveLogInner(bean, OutImportConstant.LOGSTATUS_FAIL, "处理失败,数据库内部错误");
			
			operationLog.error("订单接口数据处理错误：", e);
            throw new MYException("数据库内部错误");
        }
        catch (DataAccessException e)
        {
        	saveLogInner(bean, OutImportConstant.LOGSTATUS_FAIL, "处理失败,数据访问异常");
        	
        	operationLog.error("订单接口数据处理错误：", e);
            throw new MYException(e.getCause().toString());
        }
        catch (Exception e)
        {
        	saveLogInner(bean, OutImportConstant.LOGSTATUS_FAIL, "处理失败,系统错误，请联系管理员");
        	
        	operationLog.error("订单接口数据处理错误：", e);
            throw new MYException("系统错误，请联系管理员:" + e);
        }
        
		return true;
	}
	
	/**
	 * 检查是否已生成了OA单
	 * 
	 * @param bean
	 * @throws MYException
	 */
	private void check(OutImportBean bean) throws MYException
	{
		String batchId = bean.getBatchId();
		
		OutImportLogBean logBean = outImportLogDAO.findByBatchIdAndStatus(batchId, OutImportConstant.LOGSTATUS_ING);
		
		if (null != logBean)
		{
			throw new MYException("批次号：" + batchId + " 正在处理中，不可重复处理");
		}
		
		logBean = outImportLogDAO.findByBatchIdAndStatus(batchId, OutImportConstant.LOGSTATUS_SUCCESSFULL);
		
		if (null != logBean)
		{
			throw new MYException("批次号：" + batchId + " 已处理完成了，不可重复产生OA单");
		}
	}
	
	private boolean saveLogInner(final OutImportBean bean, final int status, final String message)
	{
		try
		{
			TransactionTemplate tran = new TransactionTemplate(transactionManager);
			
			tran.execute(new TransactionCallback()
			{
				public Object doInTransaction(TransactionStatus tstatus)
				{
					outImportLogDAO.deleteEntityBeansByFK(bean.getBatchId());
					
					OutImportLogBean logBean = new OutImportLogBean();
					
					logBean.setId(commonDAO.getSquenceString20());
					logBean.setBatchId(bean.getBatchId());
					logBean.setLogTime(TimeTools.now());
					logBean.setMessage(message);
					logBean.setStatus(status);
					
					outImportLogDAO.saveEntityBean(logBean);
					
					return Boolean.TRUE;
				}
			}
			);
		}
        catch (Exception e)
        {
            throw new RuntimeException("系统错误，请联系管理员saveLogInner:" + e);
        }
		
		return true;
	}
	
	/**
	 * 
	 * @param bean
	 * @param status
	 * @param message
	 * @return
	 */
	private boolean saveLogInnerWithoutTransaction(OutImportBean bean, int status, String message)
	{
		outImportLogDAO.deleteEntityBeansByFK(bean.getBatchId());
		
		OutImportLogBean logBean = new OutImportLogBean();
		
		logBean.setId(commonDAO.getSquenceString20());
		logBean.setBatchId(bean.getBatchId());
		logBean.setLogTime(TimeTools.now());
		logBean.setMessage(message);
		logBean.setStatus(status);
		
		outImportLogDAO.saveEntityBean(logBean);
		_logger.info(logBean.getBatchId() + "***create logBean****" + logBean.getId() + "***status***" + status);
		
		return true;
	}
	
	/**
	 * 导入订单数据合并成OA订单 - CORE
	 * 按批号为处理单元（暂定），一个批号要不成功，要不失败。
	 * 
	 * 方法体事务，unChecked 异常，有异常抛出RuntimeException
	 * 
	 */
	private void processInner(List<OutImportBean> list)
	{
		String batchId = list.get(0).getBatchId();
		
//		List<OutImportBean> list1 = outImportDAO.queryEntityBeansByFK(batchId);
		
		if (!ListTools.isEmptyOrNull(list))
		{
			// 去掉处理中与成功处理的数据
			if (list.get(0).getStatus() == OutImportConstant.STATUS_SUCCESSFULL)
			{
                String msg = "已处理过的接口数据，不可再生成OA库单。";
                _logger.error(msg);
				throw new RuntimeException(msg);
			}
			
			Map<String, List<OutImportBean>> map = new HashMap<String, List<OutImportBean>>();
			
			// 处理需要处理的 - 1.按客户与送货时间分组
			for (OutImportBean each : list)
			{
				// 根据网点获取客户ID
				CustomerBean cbean = customerMainDAO.findByUnique(each.getComunicatonBranchName());
				
				if (null == cbean)
				{
                    String msg = each.getCiticNo()+" 客户（网点）不存在:"+each.getComunicatonBranchName();
                    _logger.error(msg);
					throw new RuntimeException(msg);
				}
				else
					each.setCustomerId(cbean.getId());
				
				String key = each.getCustomerId() + SPLIT + each.getArriveDate();
				
				if (map.containsKey(key))
				{
					List<OutImportBean> mapList = map.get(key);
					
					mapList.add(each);
				}
				else
				{
					List<OutImportBean> importList = new ArrayList<OutImportBean>();
					
					importList.add(each);
					
					map.put(key, importList);
				}
			}
			
			List<OutImportBean> uList = new ArrayList<OutImportBean>();

            _logger.info("***before create out with map values "+map.values().size());
			// 2.对分组根据库存情况再次分组 - 根据产品 +仓区+所有者生成销售单
			for (List<OutImportBean> eachList : map.values())
			{
				// 合并同一产品为一行，数量，金额合并  (暂不合并数量)
				//List<OutImportBean> mergeList = mergeList(eachList);
				
				// CORE -- 要求一个商品生成一单子
				List<OutImportBean> useList = new ArrayList<OutImportBean>();
				
				for (OutImportBean eachOut : eachList)
				{
					useList.clear();
					
					// size == 1, support size than 1
					useList.add(eachOut);
					
					// create Out
					OutBean outBean = createOut(useList);
					if (outBean == null){
						continue;
					}
					
					// 中信类型 产生赠品订单
                    //#153:赠品订单类型判断对0和2有效（中信和招商）
//                    createGiftOut(outBean);
					if (eachOut.getItype() == 0 || eachOut.getItype() == 2){
						createGiftOut(outBean);
					}
					
					eachOut.setOANo(outBean.getFullId());
					
					eachOut.setStatus(OutImportConstant.STATUS_SUCCESSFULL);
					_logger.info("***update out status to success***"+eachOut.getId());
					
					uList.add(eachOut);
				}
			}
			
			// 防止数据被重复处理，再次检查下状态
			List<OutImportBean> reList = outImportDAO.queryEntityBeansByFK(batchId);
			
			for (OutImportBean each : reList)
			{
				if (each.getStatus() == OutImportConstant.STATUS_SUCCESSFULL)
				{
					throw new RuntimeException("数据被重复处理，请确认");
				}
			}
			
			outImportDAO.updateAllEntityBeans(uList);
			
			saveLogInnerWithoutTransaction(list.get(0), OutImportConstant.LOGSTATUS_SUCCESSFULL, "成功");
		}
		
	}
	
	/**
	 * 合并商品一样的数量
	 * 
	 * @param eachList
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<OutImportBean> mergeList(List<OutImportBean> eachList)
	{
		Map<String, OutImportBean> map = new HashMap<String, OutImportBean>();
		
		for (OutImportBean each : eachList)
		{
			if (map.containsKey(each.getProductId()))
			{
				OutImportBean bean = map.get(each.getProductId());
				
				bean.setAmount(bean.getAmount() + each.getAmount());
				
				bean.setValue(bean.getAmount() * bean.getPrice());
			}
			else
			{
				map.put(each.getProductId(), each);
			}
		}
		
        Collection<OutImportBean> values = map.values();

		List<OutImportBean> mergeList = new ArrayList<OutImportBean>();
		
        for (OutImportBean eachBean : values)
        {
        	mergeList.add(eachBean);
        }
		
		return mergeList;
	}
	
	/**
	 * 创建销售库单
	 * 
	 * @param list
	 * 			
	 */
	private OutBean createOut(List<OutImportBean> list)
	{
		_logger.info("***begin create Out***");
		String newOutId;
		int itype = 0;
		
		OutImportBean bean = list.get(0);
		
		itype = bean.getItype();

        //#222 2016/6/24 邮件下载订单失败
        if (bean.getImportFromMail() == 1 && bean.getStatus() == 3)
        {
            String msg =  bean.getCiticNo()+"生成邮件订单失败";
            _logger.error(msg);
//            throw new RuntimeException(msg);
			return null;
        }
		
		String mess = "";
		
		if (itype == 0){
			mess = "中信";
		}else
		{
			//mess = "浦发";
		}
		
		OutBean newOutBean = new OutBean();
    	
    	String id = getAll(commonDAO.getSquence());

        String time = TimeTools.getStringByFormat(new Date(), "yyMMddHHmm");

        String flag = OutHelper.getSailHead(OutConstant.OUT_TYPE_OUTBILL, bean.getOutType());
        
        newOutId = flag + time + id;

        newOutBean.setId(getOutId(id));
    	
    	newOutBean.setFullId(newOutId);
    	
    	newOutBean.setOutTime(TimeTools.now_short());
    	
    	newOutBean.setCustomerId(bean.getCustomerId());
    	
    	newOutBean.setCustomerName(bean.getComunicatonBranchName());
    	
    	newOutBean.setLocationId("999");
    	
    	newOutBean.setLocation(bean.getDepotId());
    	
    	newOutBean.setType(0);
    	
    	newOutBean.setOutType(bean.getOutType());
    	
    	newOutBean.setReserve3(OutImportConstant.CITIC_PAYTYPE);
    	
    	// 中信银行导入订单
    	newOutBean.setFlowId(OutImportConstant.CITIC);
    	
    	newOutBean.setReday(bean.getReday());
    	
		long add = newOutBean.getReday()  * 24 * 3600 * 1000L;
		
		String redate = TimeTools.getStringByFormat(new Date(new Date().getTime() + add), "yyyy-MM-dd");
		
    	newOutBean.setRedate(redate);
    	
    	newOutBean.setArriveDate(TimeTools.now_short(2));
    	
    	// 结算中心审批时间
    	newOutBean.setManagerTime("");
    	
    	//newOutBean.setDutyId(OutImportConstant.CITIC_DUTY);
    	
    	newOutBean.setInvoiceId(OutImportConstant.CITIC_INVOICEID);
    	
    	//
    	if (bean.getOutType() == OutConstant.OUTTYPE_OUT_SWATCH)
    	{
    		newOutBean.setStafferId(bean.getStafferId());
    		
    		StafferBean staffer = stafferDAO.find(bean.getStafferId());
    		
    		newOutBean.setStafferName(staffer.getName());
    	}else
    	{
        	StafferVSCustomerVO vsCustVO = stafferVSCustomerDAO.findVOByUnique(bean.getCustomerId());
        	
    		newOutBean.setStafferId(vsCustVO.getStafferId());
        	
        	newOutBean.setStafferName(vsCustVO.getStafferName());
    	}
    	
    	setInvoiceId(newOutBean);
    	
    	newOutBean.setStatus(OutConstant.STATUS_SUBMIT);
    	
    	double total = 0.0d;
    	
    	int amounts = 0;
    	
    	for (OutImportBean each : list)
    	{
    		total += each.getPrice() * each.getAmount();
    		
    		amounts += each.getAmount();
    	}
    	
    	newOutBean.setTotal(total);
    	
		newOutBean.setPay(0);
		
    	newOutBean.setArriveDate(bean.getArriveDate());
    	
    	newOutBean.setChangeTime("");
    	
    	newOutBean.setOperator(bean.getReason());
    	
    	StafferBean staff = stafferDAO.find(bean.getReason());
    	
    	if (null != staff) {
    		newOutBean.setOperatorName(staff.getName());    		
    	}
    	
    	// 赠送类型
    	newOutBean.setPresentFlag(bean.getPresentFlag());

        //podate to be same as 中信订单时间
        if (!StringTools.isNullOrNone(bean.getCiticOrderDate())){
            newOutBean.setPodate(bean.getCiticOrderDate());
        }
    	
    	newOutBean.setDescription("数据接口批量导入，银行单号" + bean.getCiticNo() + "." + bean.getDescription());
    	
    	final StafferBean stafferBean = stafferDAO.find(newOutBean.getStafferId());
    	
    	List<ReplenishmentBean> replenishmentList = new ArrayList<ReplenishmentBean>();
    	
    	List<BaseBean> baseList = new ArrayList<BaseBean>();
    	
		String dutyId = "";
		int mtype = 0;
    	
		for (OutImportBean each : list)
		{
			BaseBean base = new BaseBean();
			
			ReplenishmentBean replenishment = new ReplenishmentBean();
			
			base.setId(commonDAO.getSquenceString());
			
			base.setOutId(newOutId);
			
			base.setProductId(each.getProductId());
			replenishment.setProductId(each.getProductId());
			
			base.setProductName(each.getProductName());
			replenishment.setProductName(each.getProductName());
			
			base.setUnit("套");
			
			base.setAmount(each.getAmount());
			
			base.setPrice(each.getPrice());
			
			base.setValue(each.getValue());
			
			base.setLocationId(newOutBean.getLocation());
			
			base.setDepotpartId(bean.getDepotpartId());
			replenishment.setDepotpartId(bean.getDepotpartId());
			
			base.setDepotpartName(bean.getComunicationBranch());
			replenishment.setDepotpartName(bean.getComunicationBranch());
			
			
			base.setOwner("0");
			
			base.setOwnerName("公共");

            //2015/5/4 中收激励金额
            //TODO
            base.setIbMoney(each.getIbMoney());
            base.setMotivationMoney(each.getMotivationMoney());
			
			// 业务员结算价，总部结算价
			ProductBean product = productDAO.find(base.getProductId());
			
			if (null == product)
			{
				throw new RuntimeException("产品不存在");
			}
			
			mtype = MathTools.parseInt(product.getReserve4());
			
			if (mtype == PublicConstant.MANAGER_TYPE_COMMON)
			{
				dutyId = PublicConstant.DEFAULR_DUTY_ID;
				
				if (product.getConsumeInDay() == ProductConstant.PRODUCT_OLDGOOD) {
					base.setTaxrate(0.02);
				} else if (product.getConsumeInDay() == ProductConstant.PRODUCT_OLDGOOD_YES) {
					base.setTaxrate(0.17);
				} else {
					base.setTaxrate(0);
				}
			}
			else
			{
				dutyId = PublicConstant.MANAGER2_DUTY_ID;
				
				base.setTaxrate(0);
			}
			
			double sailPrice = product.getSailPrice();
			
        	// 根据配置获取结算价
        	List<PriceConfigBean> pcblist = priceConfigDAO.querySailPricebyProductId(product.getId());
        	
        	if (!ListTools.isEmptyOrNull(pcblist))
        	{
        		PriceConfigBean cb = priceConfigManager.calcSailPrice(pcblist.get(0));
        		
        		sailPrice = cb.getSailPrice();
        	}
			
			// 获取销售配置
            SailConfBean sailConf = sailConfigManager.findProductConf(stafferBean,
                product);
            
            // 总部结算价(产品结算价 * (1 + 总部结算率))
            base.setPprice(sailPrice
                           * (1 + sailConf.getPratio() / 1000.0d));

            // 事业部结算价(产品结算价 * (1 + 总部结算率 + 事业部结算率))
            base.setIprice(sailPrice
                           * (1 + sailConf.getIratio() / 1000.0d + sailConf
                               .getPratio() / 1000.0d));

            // 业务员结算价就是事业部结算价
            base.setInputPrice(base.getIprice());

            //2014/12/9 导入时取消检查结算价为0的控制，将此检查移到“商务审批”通过环节
//            if (base.getInputPrice() == 0)
//            {
//                throw new RuntimeException(base.getProductName() + " 业务员结算价不能为0");
//            }
            
			// 配送 方式及毛利率
            base.setDeliverType(0);
            
        	// 毛利，毛利率（针对业务员的）
        	double profit = 0.0d;
        	
        	double profitRatio = 0.0d;
        	
        	if (base.getValue() != 0)
        	{
            	profit = base.getAmount() * (base.getPrice() - base.getInputPrice());
            	
            	profitRatio = profit / base.getValue();
        	}

        	base.setProfit(profit);
        	base.setProfitRatio(profitRatio);
			
        	baseList.add(base);
        	
        	replenishmentList.add(replenishment);
		}

		newOutBean.setDutyId(dutyId);
		
		newOutBean.setMtype(mtype);
		
		outDAO.saveEntityBean(newOutBean);
    	
    	baseDAO.saveAllEntityBeans(baseList);

        _logger.info("create newOutBean and base bean for:"+newOutBean.getFullId());
    	
    	newOutBean.setBaseList(baseList);
    	
    	// 配送地址
    	DistributionBean distBean = new DistributionBean();
        
        //distBean.setId(commonDAO.getSquenceString());
        
        distBean.setOutId(newOutId);
        
        distBean.setProvinceId(bean.getProvinceId());
        distBean.setCityId(bean.getCityId());
        distBean.setAddress(bean.getAddress());
        
        //saveAddress(bean, newOutBean, distBean);
        
        distBean.setMobile(bean.getHandPhone());
        
        distBean.setReceiver(bean.getReceiver());
        
        distBean.setExpressPay(bean.getExpressPay());
        
        distBean.setTransportPay(bean.getTransportPay());
        
        distBean.setTransport1(bean.getTransport1());
        
        distBean.setTransport2(bean.getTransport2());
        
        distBean.setShipping(bean.getShipping());
        
        //distributionDAO.saveEntityBean(distBean);
        List<DistributionBean> distList = saveDistributionInner(distBean, baseList);
        
        // 产生发货数据
/*        for(DistributionBean each : distList)
        {
        	ConsignBean cbean = new ConsignBean();

            cbean.setCurrentStatus(SailConstant.CONSIGN_PASS);

            cbean.setGid(commonDAO.getSquenceString20());

            cbean.setDistId(each.getId());
            
            cbean.setFullId(newOutBean.getFullId());

            cbean.setArriveDate(newOutBean.getArriveDate());
            
            cbean.setReveiver(each.getReceiver());

            consignDAO.addConsign(cbean);
        }*/
    	
		// 接口处理结果数据
		OutImportResultBean resultBean = new OutImportResultBean();
		
		resultBean.setCiticNo(bean.getCiticNo());
		
		resultBean.setOANo(newOutId);
		
		resultBean.setOAamount(amounts);
		
		resultBean.setOAmoney(total);
		
		outImportResultDAO.saveEntityBean(resultBean);

		// 记录导入的产品信息，便于纺计库存情况
		for (Iterator<ReplenishmentBean> iterator = replenishmentList.iterator(); iterator.hasNext();)
		{
			ReplenishmentBean each = iterator.next();
			
			// 只增加没有的
			ReplenishmentBean replen = replenishmentDAO.
										findByProductIdAndDepotpartIdAndOwner(each.getProductId(), each.getDepotpartId(), each.getOwner());
			
			if (null != replen)
			{
				iterator.remove();
			}
		}
		
		if (!ListTools.isEmptyOrNull(replenishmentList))
		{
			replenishmentDAO.saveAllEntityBeans(replenishmentList);
		}
        
    	// 记录退货审批日志 操作人系统，自动审批 
    	FlowLogBean log = new FlowLogBean();

        log.setActor("系统");

        log.setDescription(mess + "银行数据导入系统自动审批");
        log.setFullId(newOutBean.getFullId());
        log.setOprMode(PublicConstant.OPRMODE_PASS);
        log.setLogTime(TimeTools.now());

        log.setPreStatus(OutConstant.STATUS_SAVE);

        log.setAfterStatus(newOutBean.getStatus());

        flowLogDAO.saveEntityBean(log);
        
    	return newOutBean;
	}

	private void saveAddress(OutImportBean bean, OutBean newOutBean,
			DistributionBean distBean)
	{
		// 
        ConditionParse con = new ConditionParse();
        
        con.addWhereStr();
        con.addCondition("AddressBean.customerId", "=", newOutBean.getCustomerId());
        
        con.addCondition(" order by AddressBean.id desc");
        
        List<AddressVO> addrList = addressDAO.queryEntityVOsByCondition(con);
        
        if (ListTools.isEmptyOrNull(addrList)){
        	// 
        	if (!StringTools.isNullOrNone(bean.getAddress().trim())){
        		AddressBean addrBean = new AddressBean();
        		
        		addrBean.setId(commonDAO.getSquenceString());
        		addrBean.setCustomerId(newOutBean.getCustomerId());
        		addrBean.setCustomerName(newOutBean.getCustomerName());
        		addrBean.setStafferId(newOutBean.getStafferId());
        		addrBean.setAddress(bean.getAddress());
        		addrBean.setReceiver(bean.getReceiver());
        		addrBean.setMobile(bean.getHandPhone());
        		addrBean.setLogTime(TimeTools.now());
        		
        		addressDAO.saveEntityBean(addrBean);
        	}
        }else{
        	AddressVO addrBean = addrList.get(0);
        	
        	if (StringTools.isNullOrNone(bean.getAddress().trim()))
        	{
        		distBean.setAddress(addrBean.getAddress());
                
            	distBean.setProvinceId(addrBean.getProvinceId());
              
            	distBean.setCityId(addrBean.getCityId());
            	
            	distBean.setAreaId(addrBean.getAreaId());
        	}else{
        		if (!bean.getAddress().equals(addrBean.getAddress())
    					|| !bean.getReceiver().equals(addrBean.getReceiver())
    					|| !bean.getHandPhone().equals(addrBean.getMobile()))
    			{
        			AddressBean addBean = new AddressBean();
            		
        			addBean.setId(commonDAO.getSquenceString());
        			addBean.setCustomerId(newOutBean.getCustomerId());
        			addBean.setCustomerName(newOutBean.getCustomerName());
        			addBean.setStafferId(newOutBean.getStafferId());
        			addBean.setAddress(bean.getAddress());
        			addBean.setReceiver(bean.getReceiver());
        			addBean.setMobile(bean.getHandPhone());
        			addBean.setLogTime(TimeTools.now());
            		
            		addressDAO.saveEntityBean(addBean);
    			}
        	}
        }
	}
	
	/**
	 * 产生多个配送单
	 */
	private List<DistributionBean> saveDistributionInner(DistributionBean distBean, List<BaseBean> baseList)
	{
		// 预处理distBean，将详细地址中有与省、市、区一样的信息清除
		String address = distBean.getAddress().trim();
		
		String provinceId = distBean.getProvinceId();
		
		String provinceName = "";
		
		if (!StringTools.isNullOrNone(provinceId))
		{
			ProvinceBean province = provinceDAO.find(provinceId);
			
			if (null != province)
			{
				provinceName = province.getName();
				
				int p = address.indexOf(provinceName);
				
				if (p == 0)
				{
					address = address.substring(province.getName().length());
				}
			}
		}
		
		String cityId = distBean.getCityId();
		
		if (!StringTools.isNullOrNone(cityId))
		{
			CityBean city = cityDAO.find(cityId);
			
			if (null != city)
			{
				if (provinceName.equals(city.getName()))
				{
					distBean.setProvinceId("");
				}
				
				int p = address.indexOf(city.getName());
				
				if (p == 0)
				{
					address = address.substring(city.getName().length());
				}
			}
		}
		
		String areaId = distBean.getAreaId();
		
		if (!StringTools.isNullOrNone(areaId))
		{
			AreaBean area = areaDAO.find(areaId);
			
			if (null != area)
			{
				int p = address.indexOf(area.getName());
				
				if (p == 0)
				{
					address = address.substring(area.getName().length());
				}
			}
		}
		
		distBean.setAddress(address);
		
		List<DistributionBean> distList = new ArrayList<DistributionBean>();
		
		Map<String,List<BaseBean>> map = new HashMap<String,List<BaseBean>>();
        
        for (BaseBean each : baseList)
        {
        	String key = each.getDeliverType()+"";
        	
        	if (!map.containsKey(key))
        	{
        		List<BaseBean> ebaseList = new ArrayList<BaseBean>();
        		
        		ebaseList.add(each);
        		
        		map.put(key, ebaseList);
        	}else{
        		List<BaseBean> ebaseList = map.get(key);
        		
        		ebaseList.add(each);
        	}
        }
        
        // 
        for (Map.Entry<String, List<BaseBean>> entry : map.entrySet())
        {
        	List<BaseBean> blist = entry.getValue();
        	
        	DistributionBean newDist = new DistributionBean();
        	
        	BeanUtil.copyProperties(newDist, distBean);
        	
        	String id = commonDAO.getSquenceString20(IDPrefixConstant.ID_DISTRIBUTION_PRIFIX);
        	
        	newDist.setId(id);
        	
        	distList.add(newDist);
        	
        	distributionDAO.saveEntityBean(newDist);
        	
        	for(BaseBean eachB : blist)
        	{
        		DistributionBaseBean dbb = new DistributionBaseBean();
        		
        		dbb.setRefId(id);
        		
        		dbb.setOutId(eachB.getOutId());
        		
        		dbb.setBaseId(eachB.getId());
        		
        		distributionBaseDAO.saveEntityBean(dbb);
        	}
        }
        
        return distList;
	}
	
    private void setInvoiceId(final OutBean outBean)
    {
    	// 行业
        StafferBean sb = stafferDAO.find(outBean.getStafferId());

        outBean.setIndustryId(sb.getIndustryId());

        if (StringTools.isNullOrNone(sb.getIndustryId2()))
        {
        	outBean.setIndustryId2(sb.getIndustryId());
        }
        else
        {
        	outBean.setIndustryId2(sb.getIndustryId2());
        }
        
        if (StringTools.isNullOrNone(sb.getIndustryId3()))
        {
        	outBean.setIndustryId3(outBean.getIndustryId2());
        }
        else
        {
        	outBean.setIndustryId3(sb.getIndustryId3());
        }
        
    }
	
    // 创建赠品订单
    private void createGiftOut(OutBean out)
    {
        _logger.info("create gift out for out:" + out);
    	// 判断产品是否有对应赠品关系 - 中信订单一个订单一个产品
    	BaseBean base = out.getBaseList().get(0);
    	
    	String productId = base.getProductId();
    	
    	List<ProductVSGiftVO> giftList = productVSGiftDAO.queryEntityVOsByFK(productId);

//        List<ProductVSGiftVO> qualifiedGiftList = new ArrayList<ProductVSGiftVO>();
        int priority = -1;
        ProductVSGiftVO giftVO = null;
    	
    	if (ListTools.isEmptyOrNull(giftList))
    		return;
        else {
            _logger.info("giftList size:"+giftList.size());
            //2015/5/2 必须在有效期内才生成赠品订单
            for (ProductVSGiftVO gift: giftList){
				_logger.info("*************check gift************"+gift);
                int result = this.satisfy(out, gift);
                if (result>priority){
                    _logger.info("***update priority from "+priority+" to "+result);
                    priority = result;
                    giftVO = gift;
                }
            }
        }

        if (priority == -1){
            _logger.warn("qualifiedGiftList size is zero, so will not create gift out bean!");
        } else{
            OutBean newOutBean = new OutBean();

            BeanUtil.copyProperties(newOutBean, out);

            String id = getAll(commonDAO.getSquence());

            String time = TimeTools.getStringByFormat(new Date(), "yyMMddHHmm");

            String flag = OutHelper.getSailHead(OutConstant.OUT_TYPE_OUTBILL, OutConstant.OUTTYPE_OUT_PRESENT);

            String newOutId = flag + time + id;

            newOutBean.setId(getOutId(id));

            newOutBean.setFullId(newOutId);

            newOutBean.setTotal(0);

            newOutBean.setOutType(OutConstant.OUTTYPE_OUT_PRESENT);

            String fullId = out.getFullId();
            newOutBean.setDescription("自动生成赠品订单，关联销售单：" + fullId+" 关联赠品活动："+giftVO.getActivity());

            //2015/9/29 自动导入生成赠品订单时关联refOutFullId
            newOutBean.setRefOutFullId(fullId);

            outDAO.saveEntityBean(newOutBean);

            //2016/1/6 多个赠送活动满足条件，只允许参加一个

            BaseBean newBaseBean = new BaseBean();

            BeanUtil.copyProperties(newBaseBean, base);

            newBaseBean.setId(commonDAO.getSquenceString());

            //2015/6/14 检查销售数量与赠品数量
            double giftPercentage = ((double)giftVO.getAmount())/giftVO.getSailAmount();
            newBaseBean.setAmount((int)(giftPercentage*base.getAmount()));

            newBaseBean.setProductId(giftVO.getGiftProductId());
            newBaseBean.setProductName(giftVO.getGiftProductName());
            newBaseBean.setPrice(0);
            newBaseBean.setValue(0);
            newBaseBean.setProfit(0);
            newBaseBean.setProfitRatio(0);
            newBaseBean.setOutId(newOutId);

            baseDAO.saveEntityBean(newBaseBean);

			_logger.info("***giftVO product2**"+giftVO.getGiftProductId2()+"**prod3***"+ giftVO.getGiftProductId3());
			//2015/1/21 买赠可多个商品
			if (!StringTools.isNullOrNone(giftVO.getGiftProductId2())
					&& giftVO.getAmount2()> 0){
				_logger.info("***create extra base bean for gift2***");
				BaseBean newBaseBean2 = new BaseBean();
				BeanUtil.copyProperties(newBaseBean2, base);

				newBaseBean2.setId(commonDAO.getSquenceString());
				double giftPercentage2 = ((double)giftVO.getAmount2())/giftVO.getSailAmount();
				newBaseBean2.setAmount((int)(giftPercentage2*base.getAmount()));

				newBaseBean2.setProductId(giftVO.getGiftProductId2());
				newBaseBean2.setProductName(giftVO.getGiftProductName2());
				newBaseBean2.setPrice(0);
				newBaseBean2.setValue(0);
				newBaseBean2.setProfit(0);
				newBaseBean2.setProfitRatio(0);
				newBaseBean2.setOutId(newOutId);

				baseDAO.saveEntityBean(newBaseBean2);
			}

			if (!StringTools.isNullOrNone(giftVO.getGiftProductId3())
					&& giftVO.getAmount3()>0){
				_logger.info("***create extra base bean for gift3***");
				BaseBean newBaseBean3 = new BaseBean();
				BeanUtil.copyProperties(newBaseBean3, base);

				newBaseBean3.setId(commonDAO.getSquenceString());
				double giftPercentage3 = ((double)giftVO.getAmount3())/giftVO.getSailAmount();
				newBaseBean3.setAmount((int)(giftPercentage3*base.getAmount()));

				newBaseBean3.setProductId(giftVO.getGiftProductId3());
				newBaseBean3.setProductName(giftVO.getGiftProductName3());
				newBaseBean3.setPrice(0);
				newBaseBean3.setValue(0);
				newBaseBean3.setProfit(0);
				newBaseBean3.setProfitRatio(0);
				newBaseBean3.setOutId(newOutId);

				baseDAO.saveEntityBean(newBaseBean3);
			}


            // 配送
            List<DistributionBean> distList = distributionDAO.queryEntityBeansByFK(out.getFullId());

            for(DistributionBean each : distList)
            {
                String distId = each.getId();

                DistributionBean newDist = new DistributionBean();

                String newDistId = commonDAO.getSquenceString20(IDPrefixConstant.ID_DISTRIBUTION_PRIFIX);

                BeanUtil.copyProperties(newDist, each);

                newDist.setId(newDistId);
                newDist.setOutId(newOutId);

                distributionDAO.saveEntityBean(newDist);
            }

            // 记录退货审批日志 操作人系统，自动审批
            FlowLogBean log = new FlowLogBean();

            log.setActor("系统");

            log.setDescription("银行数据导入系统[赠品]自动审批");
            log.setFullId(newOutBean.getFullId());
            log.setOprMode(PublicConstant.OPRMODE_PASS);
            log.setLogTime(TimeTools.now());

            log.setPreStatus(OutConstant.STATUS_SAVE);

            log.setAfterStatus(newOutBean.getStatus());

            flowLogDAO.saveEntityBean(log);
        }
    }

    /** 2015/1/6 #156
     * check whether OutBean satisfy gift config
     * @param out
     * @param gift
     * @return 优先级从高到低为人员～城市～部门～大区～事业部～银行
	 * 2015/1/12 新增“省份”，优先级在城市 和部门 中间
     */
    private int satisfy(OutBean out, ProductVSGiftVO gift) {
        final Map<String,Integer> priority = new HashMap<String ,Integer>();
        priority.put("人员", 7);
        priority.put("城市", 6);
		priority.put("省份", 5);
        priority.put("部门", 4);
        priority.put("大区", 3);
        priority.put("事业部", 2);
        priority.put("银行", 1);

        int result = -1;
        String msg = out+" vs gift configuration:"+gift;
        _logger.info(msg);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date end = sdf.parse(gift.getEndDate());
            Date begin = sdf.parse(gift.getBeginDate());

            //2015/6/14 compare with PODATE
            if (StringTools.isNullOrNone(out.getPodate())){
                Date now = new Date();
                if (end.before(now) || now.before(begin)){
                    _logger.warn(gift+" gift is out of date:"+now);
                    return -1;
                }
            } else{
                Date poDate = sdf.parse(out.getPodate());
                if (poDate.before(begin) || poDate.after(end)){
                    _logger.warn(gift+" gift is out of date:"+poDate);
                    return -1;
                }
            }

        }catch(Exception e){
            _logger.error(" Exception when create gift out:"+msg,e);
            return -1 ;
        }

		//2016/1/18 产品数量必须满足
		BaseBean base = out.getBaseList().get(0);
		if (base.getAmount()< gift.getSailAmount()){
			_logger.warn(base+" amount does not reach gift config:"+gift);
			return -1;
		}

		//如果所有条件都不设置，默认都参加活动
		if (StringTools.isNullOrNone(gift.getStafferName()) &&
				StringTools.isNullOrNone(gift.getCity()) &&
				StringTools.isNullOrNone(gift.getIndustryName()) &&
				StringTools.isNullOrNone(gift.getIndustryName2()) &&
				StringTools.isNullOrNone(gift.getIndustryName3()) &&
				StringTools.isNullOrNone(gift.getBank())
				){
			_logger.info("gift satisfy default rule ***"+gift);
			return 100;

		}

        //2015/6/14 银行指 客户名中包括“适用银行”字段值
        //2016/1/5 银行可多选，用分号；分割
        String bank = gift.getBank();
        if (!StringTools.isNullOrNone(bank)){
            String customerName = out.getCustomerName();
            String[] banks = bank.split(";");
            if (!this.contains2(banks, customerName)){
                _logger.warn(customerName+" bank is not suitable:"+bank);
                return -1;
            }else{
                result = priority.get("银行");
            }
        }

        //事业部，大区，部门，人员就取人员管理上面的信息
        StafferVO stafferVO = this.stafferDAO.findVO(out.getStafferId());

        if (stafferVO!= null){
            //事业部
            String industryName = gift.getIndustryName();
            if (!StringTools.isNullOrNone(industryName)){
                String[] industryNames = industryName.split(";");
                if (!this.contains(industryNames, stafferVO.getIndustryName())){
                    _logger.info(industryName+" is not suitable:"+stafferVO.getIndustryName());
                    return -1;
                } else{
                   result = priority.get("事业部");
                }
            }

            //大区
            String industryName2 = gift.getIndustryName2();
            if (!StringTools.isNullOrNone(industryName2)){
                String[] industryName2s = industryName2.split(";");
                if (!this.contains(industryName2s, stafferVO.getIndustryName2())){
                    _logger.info(industryName2+" is not suitable:"+stafferVO.getIndustryName2());
                    return -1;
                } else{
                    result = priority.get("大区");
                }
            }

            //部门
            String industryName3 = gift.getIndustryName3();
            if (!StringTools.isNullOrNone(industryName3)){
                String[] industryName3s = industryName3.split(";");
                if (!this.contains(industryName3s, stafferVO.getIndustryName3())){
                    _logger.info(industryName3+" is not suitable:"+stafferVO.getIndustryName3());
                    return -1;
                }  else{
                    result = priority.get("部门");
                }
            }
        }

		CustomerVO customerVO = this.customerMainDAO.findVO(out.getCustomerId());
		if (customerVO!= null){
			//省份
			String province = gift.getProvince();
			if (!StringTools.isNullOrNone(province)){
				//省份取客户信息表中的省份信息
				String customerProvince = customerVO.getProvinceName();
				String[] provinces = province.split(";");
				if (!this.contains(provinces, customerProvince)){
					_logger.info(customerProvince+" is not suitable:"+province);
					return -1;
				} else{
					result = priority.get("省份");
				}
			}

			//城市
			String city = gift.getCity();
			if (!StringTools.isNullOrNone(city)){
				//城市取客户信息表中的城市信息
				//城市
				String customerCity = customerVO.getCityName();
				String[] cities = city.split(";");
				if (!this.contains(cities, customerCity)){
					_logger.info(customerCity+" is not suitable:"+city);
					return -1;
				} else{
					result = priority.get("城市");
				}
			}
		}



        //人员
        String stafferName = gift.getStafferName();
        if (!StringTools.isNullOrNone(stafferName)){
           String[] stafferNames = stafferName.split(";");
           String staffer = out.getStafferName();
            if (!this.contains(stafferNames, staffer)){
                _logger.info(stafferName+" is not suitable:"+staffer);
                return -1;
            } else{
                result = priority.get("人员");
            }
        }

        _logger.info(msg+" satisfy gift config:"+gift+"***result***"+result);
        return result;
    }

    /**
     * whether array contains element
     * @param array
     * @param element
     * @return
     */
    private boolean contains(String[] array, String element){
        List<String> list  = Arrays.asList(array);
        return list.contains(element);
    }

    /**
     * whether element contains any of array
     * @param array
     * @param element
     * @return
     */
    private boolean contains2(String[] array, String element){
        for (String el : array){
            if (element.contains(el)){
                return true;
            }
        }
        return false;
    }
    
	/**
	 * 异步处理
	 * {@inheritDoc}
	 */
	public boolean processAsyn(List<OutImportBean> list)
	{
		ProcessBeanThread thread = new ProcessBeanThread(list);
		
		try
		{
			thread.start();
		}
		catch(Exception e)
		{
			_logger.error(e, e);
		}
		
		return true;
	}
	
	class ProcessBeanThread extends Thread
	{
		private List<OutImportBean> list;
		
		public ProcessBeanThread(List<OutImportBean> list)
		{
			this.list = list;
		}

		public List<OutImportBean> getList()
		{
			return list;
		}

		public void run()
        {
            try
            {
            	process(list);
            }
            catch (MYException e)
            {
            	_logger.warn(e, e);
            }
        }
	}

	public List<ReplenishmentBean> queryReplenishmentBean() throws MYException
	{
		List<ReplenishmentBean> list = replenishmentDAO.listEntityBeans();
		
		final List<ReplenishmentBean> delList = new ArrayList<ReplenishmentBean>();
		
		for (Iterator<ReplenishmentBean> iterator = list.iterator(); iterator.hasNext();)
		{
			ReplenishmentBean bean = iterator.next();
			
			// 库存量
			int amount = storageRelationDAO.sumByDepotpartIdAndProductIdAndStafferId(bean.getDepotpartId(), bean.getProductId(), bean.getOwner());
			
			// 在途
			int inwayAmount = storageRelationManager.sumPreassignByStorageRelation2(bean.getDepotpartId(), bean.getProductId(), bean.getOwner());
			
			if (amount >= inwayAmount)
			{
				delList.add(bean);
				
				iterator.remove();
			}
			
			bean.setAmount(inwayAmount - amount);
		}
		
		if (!ListTools.isEmptyOrNull(delList))
		{
			deleteReplenishmentBeans(delList);
		}
		
		return list;
	}
	
	@Transactional(rollbackFor = MYException.class)
	private boolean deleteReplenishmentBeans(final List<ReplenishmentBean> delList) throws MYException
	{
		for (ReplenishmentBean each : delList)
		{
			replenishmentDAO.deleteEntityBean(each.getId());
		}
		
		return true;
	}
	
    private String getAll(int i)
    {
        String s = "00000000" + i;

        return s.substring(s.length() - 9);
    }
    
    private String getOutId(String idStr)
    {
        while (idStr.length() > 0 && idStr.charAt(0) == '0')
        {
            idStr = idStr.substring(1);
        }

        return idStr;
    }
    
    @Transactional(rollbackFor = MYException.class)
	public String batchApproveImport(List<BatchApproveBean> list, int type)
			throws MYException
	{
    	JudgeTools.judgeParameterIsNull(list);
    	
    	String batchId = commonDAO.getSquenceString20();
    	
    	for(BatchApproveBean each :list)
    	{
    		each.setBatchId(batchId);
    		
    		OutBean out = outDAO.find(each.getOutId());
    		
    		if (out == null)
    		{
    			each.setRet(1);
    			each.setResult(each.getOutId() + " 订单不存在") ;
    			
    			continue;
    		}
    		
    		each.setStatus(out.getStatus());
    		
    		// 须是销售单
    		if (out.getType() != OutConstant.OUT_TYPE_OUTBILL)
    		{
    			each.setRet(1);
    			each.setResult(each.getOutId() + " 订单不是销售单") ;
    			
    			continue;
    		}
    		
    		// 须是中信订单
    		/*if (!out.getFlowId().equals("CITIC"))
    		{
    			each.setRet(1);
    			each.setResult(each.getOutId() + " 订单不是中信订单") ;
    			
    			continue;
    		}*/
    		
    		// 须是结算中心或库管状态
    		if (type == 0 && out.getStatus() != OutConstant.STATUS_SUBMIT)
    		{
    			each.setRet(1);
    			each.setResult(each.getOutId() + " 订单状态只能是待商务审批") ;
    			
    			continue;
    		}
    		
    		if (type == 1 && out.getStatus() != OutConstant.STATUS_FLOW_PASS)
    		{
    			each.setRet(1);
    			each.setResult(each.getOutId() + " 订单状态只能是待库管审批") ;
    			
    			continue;
    		}
    	}
    	
    	batchApproveDAO.saveAllEntityBeans(list);
    	
		return batchId;
	}

    /**
     * 一单一单处理，并记录处理结果
     * {@inheritDoc}
     */
	public boolean batchApprove(User user, String batchId) throws MYException
	{
		List<BatchApproveBean> list = batchApproveDAO.queryEntityBeansByFK(batchId);
    	
    	// 只处理检查是OK的数据
    	for(Iterator<BatchApproveBean> iterator = list.iterator(); iterator.hasNext();)
    	{
    		BatchApproveBean  bean = iterator.next();
    		
    		if (bean.getRet() == 1)
    			iterator.remove();
    	}
		
    	//
    	for (BatchApproveBean each : list)
        {
            try
            {
            	processBatchApprove(user,each);
            	
            	// 成功的
            	updateBatchApprove(each, "批量处理成功");
            }
            catch (MYException e)
            {
                operationLog.error(e, e);
                
                // 异常的
                updateBatchApprove(each, e.getErrorContent());
            }
        }
    	
    	return true;
	}
	
	private void updateBatchApprove(final BatchApproveBean bean, final String result)
	{
		try
		{
			TransactionTemplate tran = new TransactionTemplate(transactionManager);
			
			tran.execute(new TransactionCallback() {
							 public Object doInTransaction(TransactionStatus tstatus) {
								 bean.setResult(result);

								 batchApproveDAO.updateEntityBean(bean);

								 return Boolean.TRUE;
							 }
						 }
			);
		}
        catch (Exception e)
        {
            throw new RuntimeException("系统错误，请联系管理员updateBatchApprove:" + e);
        }
		
        return;
	}
	
	/**
	 * CORE
	 * 
	 * @param user
	 * @param bean
	 * @throws MYException
	 */
	private void processBatchApprove(User user, BatchApproveBean bean) throws MYException
	{
		OutBean out = outDAO.find(bean.getOutId());
		
		if (null == out)
		{
			throw new MYException("销售单不存在");
		}
		
		// 商务审批,但状态已改变
		if (bean.getType()== 0)
		{
			if (out.getStatus() != OutConstant.STATUS_SUBMIT)
				throw new MYException("销售单不是待商务审批状态");
			else
			{
				if (bean.getAction().equals("通过"))
				{
					outManager.pass(bean.getOutId(), user, OutConstant.STATUS_FLOW_PASS, bean.getReason(), "");
				}else if (bean.getAction().equals("驳回")){
					
					outManager.reject(bean.getOutId(), user, bean.getReason());
					
				}else
				{
					throw new MYException("审批结果不是通过与驳回");
				}
			}
		}
		
		// 库管审批,但状态已改变
		if (bean.getType()== 1)
		{
			if (out.getStatus() != OutConstant.STATUS_FLOW_PASS)
				throw new MYException("销售单不是待库管审批状态");
			else
				if (bean.getAction().equals("通过"))
				{
					outManager.pass(bean.getOutId(), user, OutConstant.STATUS_PASS, bean.getReason(), "");
				}else if (bean.getAction().equals("驳回")){
					
					outManager.reject(bean.getOutId(), user, bean.getReason());
					
				}else
				{
					throw new MYException("审批结果不是通过与驳回");
				}
		}
	}
	
	@Transactional(rollbackFor = MYException.class)
	public String batchSwatchImport(List<BatchSwatchBean> list)
			throws MYException
	{
		JudgeTools.judgeParameterIsNull(list);
		
		String batchId = commonDAO.getSquenceString20();
		
		// 一个单号中不同的产品
		Map<String, List<BatchSwatchBean>> map = new HashMap<String, List<BatchSwatchBean>>();
		
		// 同一个单子同一个产品不能出现两次
		for (BatchSwatchBean each : list)
		{
			each.setBatchId(batchId);
			
			OutBean out = outDAO.find(each.getOutId());
			
			if (null == out)
			{
				each.setRet(1);
				each.setResult(each.getOutId() + " 销售单不存在");
			}
			
			if (out.getType() != OutConstant.OUT_TYPE_OUTBILL)
			{
				each.setRet(1);
				each.setResult(each.getOutId() + " 不是销售单");
			}
			
			if (out.getOutType() != OutConstant.OUTTYPE_OUT_SWATCH 
					&& out.getOutType() != OutConstant.OUTTYPE_OUT_SHOW
                    //2015/3/17 新增银行领样 （与银行铺货类拟）
                    && out.getOutType() != OutConstant.OUTTYPE_OUT_BANK_SWATCH
					&& out.getOutType() != OutConstant.OUTTYPE_OUT_SHOWSWATCH)
			{
				each.setRet(1);
				each.setResult(each.getOutId() + " 不是领样、铺货领样、巡展领样销售单");
			}
			
			String key = each.getOutId();
			
			if (map.containsKey(key))
			{
				List<BatchSwatchBean> blist = map.get(key);
				
				blist.add(each);
			}else{
				List<BatchSwatchBean> blist = new ArrayList<BatchSwatchBean>();
				
				blist.add(each);
				
				map.put(key, blist);
			}
		}
		
		// 确定 退货明细的有效性 - 一单一单的检查
		for(Entry<String, List<BatchSwatchBean>> entry : map.entrySet())
		{
			String outId = entry.getKey();
			
			List<BatchSwatchBean> bList = entry.getValue();
			
			//evalueSwatch
			List<BaseBean> baseList = evalueSwatch(outId);
            
            // 再与导入的数据进行匹配
            for (BatchSwatchBean each : bList)
            {
            	for(BaseBean eachBase : baseList)
            	{
            		int canUseAmount = eachBase.getAmount() - eachBase.getInway();
            		
            		if (each.getProductName().equals(eachBase.getProductName()) && each.getAmount() <= canUseAmount)
            		{
            			each.setRet(0);
            			each.setResult("OK");
            			
            			each.setBaseId(eachBase.getId());
            			
            			break;
            		}
            	}
            }
            
            batchSwatchDAO.saveAllEntityBeans(bList);
		}
		
		return batchId;
	}

	/**
	 * 计算某一领样或巡展单可以退或转的数量
	 * @param outId
	 * @return
	 */
	private List<BaseBean> evalueSwatch(String outId)
	{
		List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(outId);
		
		// 检查商品是否存在
		
		// 要求导入的产品数量不能超过原单数量-已退-已转
		// 查询已转的销售
		ConditionParse con1 = new ConditionParse();
		
		con1.clear();

		con1.addWhereStr();

		con1.addCondition("OutBean.refOutFullId", "=", outId);

		con1.addCondition("OutBean.type", "=", OutConstant.OUT_TYPE_OUTBILL);

		List<OutBean> refList = outDAO.queryEntityBeansByCondition(con1);
		
		// 查询已退的退货
		List<OutBean> refBuyList = queryRefOut4(outId);
		
		// 计算出已经退货的数量
		for (Iterator<BaseBean> iterator = baseList.iterator(); iterator.hasNext();)
		{
		    BaseBean baseBean = iterator.next();

		    int hasBack = 0;

		    // 退库
		    for (OutBean ref : refBuyList)
		    {
		        List<BaseBean> refBaseList = ref.getBaseList();

		        for (BaseBean refBase : refBaseList)
		        {
		            if (refBase.equals(baseBean))
		            {
		                hasBack += refBase.getAmount();

		                break;
		            }
		        }
		    }

		    // 转销售的
		    for (OutBean ref : refList)
		    {
		        List<BaseBean> refBaseList = baseDAO.queryEntityBeansByFK(ref.getFullId());

		        for (BaseBean refBase : refBaseList)
		        {
		            if (refBase.equals(baseBean))
		            {
		                hasBack += refBase.getAmount();

		                break;
		            }
		        }
		    }

		    baseBean.setInway(hasBack);

		    int last = baseBean.getAmount() - baseBean.getInway();

			if (last <= 0)
				iterator.remove();
		}
		
		return baseList;
	}
	
	private List<OutBean> queryRefOut4(String outId)
	{
		// 查询当前已经有多少个人领样
		ConditionParse con = new ConditionParse();

		con.addWhereStr();

		con.addCondition("OutBean.refOutFullId", "=", outId);

		con.addIntCondition("OutBean.type", "=", OutConstant.OUT_TYPE_INBILL);

		// 排除其他入库(对冲单据)
		con.addCondition("OutBean.reserve8", "<>", "1");

		List<OutBean> refBuyList = outDAO.queryEntityBeansByCondition(con);

		for (OutBean outBean : refBuyList)
		{
			List<BaseBean> list = baseDAO.queryEntityBeansByFK(outBean
					.getFullId());

			outBean.setBaseList(list);
		}

		return refBuyList;
	}

	/**
	 * 处理领样批量转销售或退货
	 * 
	 * {@inheritDoc}
	 */
	public boolean batchSwatch(User user, String batchId) throws MYException
	{
		List<BatchSwatchBean> list = batchSwatchDAO.queryEntityBeansByFK(batchId);
    	
    	// 只处理检查是OK的数据
    	for(Iterator<BatchSwatchBean> iterator = list.iterator(); iterator.hasNext();)
    	{
    		BatchSwatchBean  bean = iterator.next();
    		
    		if (bean.getRet() == 1)
    			iterator.remove();
    	}
    	
    	if (ListTools.isEmptyOrNull(list))
    		return true;
    	
    	Map<String, List<BatchSwatchBean>> map = new HashMap<String, List<BatchSwatchBean>>();
		
		// 同一个单子同一个产品不能出现两次  - 
		for (BatchSwatchBean each : list)
		{
			String key = each.getOutId();
			
			if (map.containsKey(key))
			{
				List<BatchSwatchBean> blist = map.get(key);
				
				blist.add(each);
			}else{
				List<BatchSwatchBean> blist = new ArrayList<BatchSwatchBean>();
				
				blist.add(each);
				
				map.put(key, blist);
			}
		}
    	
		// 在事务中完成
		for(Entry<String, List<BatchSwatchBean>> entry : map.entrySet())
		{
			List<BatchSwatchBean> bList = entry.getValue();
			
			try
            {
            	processBatchSwatch(user,bList, batchId);
            	
            	// 成功的
            	updateBatchSwatch(bList, "批量处理成功");
            }
            catch (MYException e)
            {
                operationLog.error(e, e);
                
                // 异常的
                updateBatchSwatch(bList, e.getErrorContent());
            }
		}
		
    	return true;
	}
	
	/**
	 * CORE
	 * @param user
	 * @param bList
	 * @throws MYException
	 */
	private void processBatchSwatch(final User user, List<BatchSwatchBean> bList, final String batchId) throws MYException
	{
		final BatchSwatchBean bean = bList.get(0);
		
		OutBean out = outDAO.find(bean.getOutId());
		
		if (null == out)
		{
			throw new MYException("销售单不存在");
		}
		
		final List<BaseBean> backSwatchList = new ArrayList<BaseBean>();
		
		final List<BaseBean> outSwatchList = new ArrayList<BaseBean>();
		
		// 再次检查是否有足够的数量可以转或退
		List<BaseBean> baseList = evalueSwatch(bean.getOutId());
        
        // 再与导入的数据进行匹配
        for (BatchSwatchBean each : bList)
        {
        	for(BaseBean eachBase : baseList)
        	{
        		int canUseAmount = eachBase.getAmount() - eachBase.getInway();
        		
        		if (each.getBaseId().equals(eachBase.getId()))
        		{
        			if (each.getAmount() > canUseAmount)
            		{
            			throw new MYException("产品:"+ each.getProductName() + " 处理数量溢出.");
            		}
            		
            		if (each.getAction().equals("退货"))
            		{
            			BaseBean base = new BaseBean();
            			
            			BeanUtil.copyProperties(base, eachBase);
            			
            			base.setAmount(each.getAmount());
            			base.setValue(base.getAmount() * base.getPrice());
            			base.setInway(0);
            			base.setDescription(String.valueOf(eachBase
    							.getCostPrice()));

            			backSwatchList.add(base);
            		}
            		
            		if (each.getAction().equals("销售"))
            		{
            			BaseBean base = new BaseBean();
            			
            			BeanUtil.copyProperties(base, eachBase);
            			
            			base.setAmount(each.getAmount());
            			base.setValue(base.getAmount() * base.getPrice());
            			base.setInway(0);
            			
            			outSwatchList.add(base);
            		}
        		}
        	}
        }
        
        TransactionTemplate tran = new TransactionTemplate(transactionManager);
        
        try{
        	tran.execute(new TransactionCallback()
    		{
    			public Object doInTransaction(TransactionStatus tstatus)
    			{
    				try{
    					// 分别处理
    			        if (outSwatchList.size() > 0)
    			        {
    			        	OutBean outBean = setOutBean(user, bean, outSwatchList);
    			        	
    			        	outManager.addSwatchToSailWithoutTrans(user, outBean);
    			        	
    			        	int newNextStatus = OutConstant.STATUS_FLOW_PASS;
    			        	
    			        	// 修改状态
    			            outDAO.modifyOutStatus(outBean.getFullId(), newNextStatus);
    			        	
    			        	// 直接审批结束
    			        	// outManager.directPassWithoutTrans(user, outBean, OutConstant.OUT_TYPE_OUTBILL);
    			        	
    			        	addOutLog(outBean.getFullId(), user, outBean, "OK", SailConstant.OPR_OUT_PASS,
    			                    newNextStatus);
    			        	
    			        	saveLogInner(batchId, outBean);
    			        }
    			        
    			        if (backSwatchList.size() > 0)
    			        {
    			        	OutBean outBean = setOutBackBean(user, bean, backSwatchList);
    			        	
    			        	outManager.addSwatchToSailWithoutTrans(user, outBean);
    			        	
    			        	int newNextStatus = OutConstant.BUY_STATUS_SUBMIT;
    			        	
    			        	// 修改状态
    			            outDAO.modifyOutStatus(outBean.getFullId(), newNextStatus);
    			        	
    			        	// 直接审批结束
    			        	// outManager.directPassWithoutTrans(user, outBean, OutConstant.OUT_TYPE_INBILL);
    			        	
    			        	addOutLog(outBean.getFullId(), user, outBean, "OK", SailConstant.OPR_OUT_PASS,
    			                    newNextStatus);
    			        	
    			        	saveLogInner(batchId, outBean);
    			        }
    				}catch(MYException e)
    				{
    					throw new RuntimeException(e.getErrorContent(), e);
    				}
    		        
    				return Boolean.TRUE;
    			}
    		}
    		);
        } catch (TransactionException e)
        {
            _logger.error(e, e);
            throw new MYException("数据库内部错误");
        }
        catch (DataAccessException e)
        {
            _logger.error(e, e);
            throw new MYException("数据库内部错误");
        }
        catch (Exception e)
        {
            _logger.error(e, e);
            throw new MYException("处理异常:" + e.getMessage());
        }
	}
	
	private void saveLogInner(String batchId, OutBean outBean)
	{
		BatchReturnLog log = new BatchReturnLog();
		
		log.setBatchId(batchId);
		log.setOutId(outBean.getFullId());
		log.setOperator(outBean.getOperator());
		log.setOperatorName(outBean.getOperatorName());
		log.setLogTime(TimeTools.now_short());
		
		batchReturnLogDAO.saveEntityBean(log);
	}
	
	/**
     * 增加日志
     * 
     * @param fullId
     * @param user
     * @param outBean
     */
    private void addOutLog(final String fullId, final User user, final OutBean outBean, String des,
                           int mode, int astatus)
    {
        FlowLogBean log = new FlowLogBean();

        if (user == null){
            log.setActor("线下移动订单Job");
        }else{
            log.setActor(user.getStafferName());
        }

        log.setDescription(des);
        log.setFullId(fullId);
        log.setOprMode(mode);
        log.setLogTime(TimeTools.now());

        log.setPreStatus(outBean.getStatus());

        log.setAfterStatus(astatus);

        flowLogDAO.saveEntityBean(log);
    }
	
	private OutBean setOutBean(User user, BatchSwatchBean sbean, List<BaseBean> bList)
    throws MYException
    {
    	OutBean bean = outDAO.find(sbean.getOutId());
    	
    	if (null == bean)
    	{
    		throw new MYException("数据错误");
    	}
    	
    	// 生成销售单,然后保存
        OutBean newOut = new OutBean();

        newOut.setOutTime(TimeTools.now_short());
        newOut.setType(OutConstant.OUT_TYPE_OUTBILL);
        newOut.setOutType(OutConstant.OUTTYPE_OUT_COMMON);
        newOut.setRefOutFullId(sbean.getOutId());
        newOut.setDutyId(bean.getDutyId());
        newOut.setMtype(bean.getMtype());
        newOut.setDescription("领样转销售,领样单据（批量导入）:" + sbean.getOutId() + "," + sbean.getDescription());
        newOut.setDepartment(bean.getDepartment());
        newOut.setLocation(bean.getLocation());
        newOut.setLocationId(bean.getLocationId());
        newOut.setDepotpartId(bean.getDepotpartId());
        newOut.setStafferId(bean.getStafferId());
        newOut.setStafferName(bean.getStafferName());
		newOut.setTransportNo(bean.getTransportNo());
        
        if (bean.getOutType() == OutConstant.OUTTYPE_OUT_SHOW
                //2015/3/17 新增银行领样 （与银行铺货类拟）
                ||bean.getOutType() == OutConstant.OUTTYPE_OUT_BANK_SWATCH)
        {
        	newOut.setCustomerId(bean.getCustomerId());
        	newOut.setCustomerName(bean.getCustomerName());
        	newOut.setConnector(bean.getConnector());
        }else{
        	newOut.setCustomerId(sbean.getCustomerId());
        	newOut.setCustomerName(sbean.getCustomerName());
        }
        
        newOut.setReday(1);
        newOut.setReserve3(1);

    	newOut.setOperator(user.getStafferId());
    	newOut.setOperatorName(user.getStafferName());
        
        newOut.setBaseList(bList);
        
        return newOut;
    }
	
	private OutBean setOutBackBean(User user, BatchSwatchBean sbean, List<BaseBean> bList)
    throws MYException
    {
    	OutBean oldOut = outDAO.find(sbean.getOutId());
    	
    	if (null == oldOut)
    	{
    		throw new MYException("数据错误");
    	}
    	
    	// 生成销售单,然后保存
    	OutBean out = new OutBean();

		out.setStatus(OutConstant.STATUS_SAVE);

		out.setStafferName(oldOut.getStafferName());

		out.setStafferId(oldOut.getStafferId());

		out.setType(OutConstant.OUT_TYPE_INBILL);

		out.setOutTime(TimeTools.now_short());

		out.setDepartment("采购部");

		if (oldOut.getOutType() == OutConstant.OUTTYPE_OUT_SHOW
                //2015/3/17 新增银行领样 （与银行铺货类拟）
                || oldOut.getOutType() == OutConstant.OUTTYPE_OUT_BANK_SWATCH)
		{
			out.setCustomerId(oldOut.getCustomerId());

			out.setCustomerName(oldOut.getCustomerName());
		}
		else
		{
			out.setCustomerId("99");

			out.setCustomerName("系统内置供应商");
		}

		// 所在区域
		out.setLocationId(user.getLocationId());

		// 目的仓库
		out.setLocation(sbean.getDirDeport());

		out.setDestinationId(oldOut.getLocation());

		out.setInway(OutConstant.IN_WAY_NO);

		out.setOutType(OutConstant.OUTTYPE_IN_SWATCH);

		out.setRefOutFullId(sbean.getOutId());

		out.setDutyId(oldOut.getDutyId());

		out.setInvoiceId(oldOut.getInvoiceId());

		out.setDescription("个人领样退库(批量导入),领样单号:" + sbean.getOutId() + "," + sbean.getDescription());
		out.setTransportNo(sbean.getTransportNo());

		out.setOperator(user.getStafferId());
		out.setOperatorName(user.getStafferName());
        
		DepotpartBean okDepotpart = depotpartDAO.findDefaultOKDepotpart(out.getLocation());

		if (okDepotpart == null)
		{
			throw new MYException("仓库下没有良品仓");
		}
		
		for (BaseBean each : bList)
		{
			if (oldOut.getLocation().equals(out.getLocation()))
			{
				each.setDepotpartId(each.getDepotpartId());
				each.setDepotpartName(each.getDepotpartName());
			}
			else
			{
				each.setDepotpartId(okDepotpart.getId());
				each.setDepotpartName(okDepotpart.getName());
			}
		}
		
        out.setBaseList(bList);
        
        return out;
    }
	
	private void updateBatchSwatch(final List<BatchSwatchBean> bList, final String result)
	{
		try
		{
			TransactionTemplate tran = new TransactionTemplate(transactionManager);
			
			tran.execute(new TransactionCallback()
			{
				public Object doInTransaction(TransactionStatus tstatus)
				{
					for (BatchSwatchBean each : bList)
					{
						each.setResult(result);
						
						batchSwatchDAO.updateEntityBean(each);

						//#202
						if (!StringTools.isNullOrNone(each.getOutId()) && !StringTools.isNullOrNone(each.getTransportNo())){
							outDAO.updateTransportNo(each.getOutId(), each.getTransportNo());
						}
					}
					
					return Boolean.TRUE;
				}
			}
			);
		}
        catch (Exception e)
        {
            throw new RuntimeException("系统错误，请联系管理员updateBatchApprove:" + e);
        }
		
        return;
	}
	
	@Transactional(rollbackFor = MYException.class)
	public boolean batchUpdateConsign(List<ConsignBean> list) throws MYException
	{
        _logger.info("batchUpdateConsign with size:" + list.size());
		JudgeTools.judgeParameterIsNull(list);
		
		for (ConsignBean each : list)
		{
            _logger.info("each consign bean " + each);
			Set<String> set = new HashSet<String>();
			
			// each.getDistId() 改为导入的是 出库单，根据出库单找到销售单
			List<PackageItemBean> itemList = packageItemDAO.queryEntityBeansByFK(each.getDistId());
			
			for (PackageItemBean eachItem : itemList)
			{
				if (!set.contains(eachItem.getOutId()))
				{
					set.add(eachItem.getOutId());
				}
			}
			
			for (String s : set)
			{
				ConsignBean oldBean = consignDAO.findDefaultConsignByFullId(s);
                List<DistributionBean> distList = distributionDAO.queryEntityBeansByFK(s);

				if (null != oldBean)
				{
					oldBean.setTransport(each.getTransport());
					oldBean.setTransportNo(each.getTransportNo());
					oldBean.setSendPlace(each.getSendPlace());
					oldBean.setPreparer(each.getPreparer());
					oldBean.setChecker(each.getChecker());
					oldBean.setPackager(each.getPackager());
					oldBean.setPackageTime(each.getPackageTime());
					oldBean.setMathine(each.getMathine());
					oldBean.setPackageAmount(each.getPackageAmount());
					oldBean.setPackageWeight(each.getPackageWeight());
					oldBean.setTransportFee(each.getTransportFee());
					oldBean.setReveiver(each.getReveiver());
					oldBean.setApplys(each.getApplys());
                    oldBean.setSfReceiveDate(each.getSfReceiveDate());
					oldBean.setShipping(each.getShipping());
					oldBean.setPay(each.getPay());
					
					consignDAO.updateConsign(oldBean);
				} else {
					
					if (!ListTools.isEmptyOrNull(distList)) {
						oldBean = new ConsignBean();

						oldBean.setCurrentStatus(SailConstant.CONSIGN_PASS);

						oldBean.setGid(commonDAO.getSquenceString20());

						oldBean.setDistId(distList.get(0).getId());
			            
						oldBean.setFullId(s);

						oldBean.setTransport(each.getTransport());
						oldBean.setTransportNo(each.getTransportNo());
						oldBean.setSendPlace(each.getSendPlace());
						oldBean.setPreparer(each.getPreparer());
						oldBean.setChecker(each.getChecker());
						oldBean.setPackager(each.getPackager());
						oldBean.setPackageTime(each.getPackageTime());
						oldBean.setMathine(each.getMathine());
						oldBean.setPackageAmount(each.getPackageAmount());
						oldBean.setPackageWeight(each.getPackageWeight());
						oldBean.setTransportFee(each.getTransportFee());
						oldBean.setReveiver(each.getReveiver());
						oldBean.setApplys(each.getApplys());
                        oldBean.setSfReceiveDate(each.getSfReceiveDate());
						oldBean.setShipping(each.getShipping());
						oldBean.setPay(each.getPay());
						
			            consignDAO.addConsign(oldBean);
					}
				}

                //#287 2016/8/13 update distribution
                if (!ListTools.isEmptyOrNull(distList)){
                    for (DistributionBean distributionBean : distList){
                        distributionBean.setShipping(each.getShipping());
                        distributionBean.setExpressPay(each.getPay());
                        distributionBean.setTransport1(Integer.valueOf(each.getTransport()));
                        distributionBean.setTransportPay(each.getPay());
                        distributionBean.setTransport2(Integer.valueOf(each.getTransport()));
                        this.distributionDAO.updateEntityBean(distributionBean);
                    }
                }

				// 2016/2/29
				// #168 将对应的销售单的状态更新为 已发货
				OutBean outBean = this.outDAO.find(s);
				if (outBean == null && s.startsWith("A")){
                    //#287 2016/8/13 update invoiceins
                        this.outDAO.updateInvoiceIns(s, each.getShipping(), each.getPay(), Integer.valueOf(each.getTransport()),
								each.getPay(), Integer.valueOf(each.getTransport()));
                        _logger.info("update invoiceinsBean "+s);
				} else if (outBean == null && s.startsWith("FP")){
					//预开票
					//#287 2016/8/13 update invoiceins
					this.outDAO.updatePreInvoiceIns(s, each.getShipping(), each.getPay(), Integer.valueOf(each.getTransport()),
							each.getPay(), Integer.valueOf(each.getTransport()));
					_logger.info("update T_CENTER_PREINVOICE "+s);
				} else if (outBean!= null){
                    outBean.setStatus(OutConstant.STATUS_SEC_PASS);
                    this.outDAO.updateEntityBean(outBean);
                    _logger.info("update out to STATUS_SEC_PASS***"+s);
                }
			}

            //2015/6/27 把发货单号和发货日期写入CK单
            if (!StringTools.isNullOrNone(each.getDistId())){
                PackageBean packageBean = this.packageDAO.find(each.getDistId());
                if (packageBean!= null){
                    packageBean.setTransportNo(each.getTransportNo());
                    packageBean.setSfReceiveDate(each.getSfReceiveDate());
					packageBean.setShipping(each.getShipping());
                    if (each.getShipping() == OutConstant.OUT_SHIPPING_3PL
							|| each.getShipping() == OutConstant.OUT_SHIPPING_PROXY){
                        packageBean.setExpressPay(each.getPay());
                        packageBean.setTransport1(Integer.valueOf(each.getTransport()));
                    } else if (each.getShipping() == OutConstant.OUT_SHIPPING_TRANSPORT){
                        packageBean.setTransportPay(each.getPay());
                        packageBean.setTransport2(Integer.valueOf(each.getTransport()));
                    } else if (each.getShipping() == OutConstant.OUT_SHIPPING_3PLANDDTRANSPORT){
                        packageBean.setExpressPay(each.getPay());
                        packageBean.setTransport1(Integer.valueOf(each.getTransport()));
                        packageBean.setTransportPay(each.getPay());
                        packageBean.setTransport2(Integer.valueOf(each.getTransport()));
                    }

                    this.packageDAO.updateEntityBean(packageBean);
                    _logger.info("update packageBean to setSfReceiveDate ***" + packageBean);
                } else{
                    _logger.info("Do not find package for:"+each.getDistId());
                }
            } else {
                _logger.warn("can not find package for ConsignBean:"+each);
            }
		}
		
		return true;
	}
	
	/***
	 * 批量更新配送地址
	 */
	@Transactional(rollbackFor = MYException.class)
	public boolean batchUpdateDistAddr(List<DistributionBean> list)
	throws MYException
	{
        if (ListTools.isEmptyOrNull(list))
		{
			throw new MYException("导入异常");
		}
        for(DistributionBean each : list)
		{
            List<DistributionBean> distList = null;
            try{
                distList = distributionDAO.queryEntityBeansByFK(each.getOutId());
            }catch(Exception e){
                e.printStackTrace();
            }
            if (ListTools.isEmptyOrNull(distList))
			{
				throw new MYException("T_CENTER_DISTRIBUTION不存在:"+each.getOutId());
			}
			
			for (DistributionBean reach : distList)
			{
//				_logger.info("地址更新，销售单：" + reach.getOutId());
//                _logger.info("新省：" + each.getProvinceId());
//                _logger.info("新市：" + each.getCityId());
//                _logger.info("新区：" + each.getAreaId());
//                _logger.info("新地址：" + each.getAddress());
//                _logger.info("新接收人：" + each.getReceiver());
//                _logger.info("新手机：" + each.getMobile());
//                _logger.info("新发货方式：" + each.getShipping());
//                _logger.info("新快递公司：" + each.getTransport1());
                _logger.info("***update distribution "+reach);

				Boolean ret = distributionDAO.updateBean(reach.getId(), each);
				
				if (!ret)
				{
					throw new MYException("更新失败,请检查源文件");
				}

                Boolean ret2 = this.outDAO.updateDescription(reach.getOutId(), each.getDescription());

                if (!ret2)
                {
                    throw new MYException("更新销售单备注失败,请检查源文件");
                }
			}
		}
		
		return true;
	}
	
	/***
	 * 批量更新销售单的 紧急 状态 （0 -> 1)
	 */
	@Transactional(rollbackFor = MYException.class)
	public boolean batchUpdateEmergency(List<ConsignBean> list)
	throws MYException
	{
		if (ListTools.isEmptyOrNull(list))
		{
			throw new MYException("导入异常");
		}
		
		for(ConsignBean each : list)
		{
            String outId = each.getFullId();
			OutBean out = outDAO.find(outId);
			
			if (out == null)
			{
//				throw new MYException("导入异常，请退出重新登陆");
                _logger.info("OutBean does not found:"+outId);
			}

            _logger.info("紧急状态更新，销售单：" + outId);


			outDAO.updateEmergency(outId, 1);

            try{
                ConditionParse condtion = new ConditionParse();
//                condtion.addCondition("PackageItemBean.outId", "=", outId);
                condtion.addWhereStr();
                condtion.addCondition(" and exists (select PackageItemBean.id from T_CENTER_PACKAGE_ITEM PackageItemBean where PackageBean.id = PackageItemBean.packageId and PackageItemBean.outId = '"+outId+"')");
                List<PackageBean> packages = this.packageDAO.queryEntityBeansByCondition(condtion);
                if (!ListTools.isEmptyOrNull(packages)){
                     for (PackageBean pack: packages){
                         pack.setEmergency(1);
                         this.packageDAO.updateEntityBean(pack);
                         _logger.info("CK updated to emergency****"+pack.getId()+" for fullId:"+outId);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
                _logger.error("***CK update emergency fail:",e);
            }
		}
		
		return true;
	}

	@Transactional(rollbackFor = MYException.class)
	@Override
	public boolean batchUpdateProductName(List<BaseVO> list) throws MYException {
		Set<String> outIds = new HashSet<String>();
		for(BaseVO each : list)
		{
			ConditionParse conditionParse = new ConditionParse();
			conditionParse.addWhereStr();
			String outId = each.getOutId();
			String productId = each.getProductId();
			conditionParse.addCondition("outId", "=", outId);
			conditionParse.addCondition("productId", "=", productId);
			_logger.info("***oudId***"+outId+"***productId***"+productId);
			List<BaseBean> baseBeans = this.baseDAO.queryEntityBeansByCondition(conditionParse);
			if (!ListTools.isEmptyOrNull(baseBeans)){
				for (BaseBean bean : baseBeans){
					bean.setProductId(each.getDestProductId());
					bean.setProductName(each.getDestProductName());
					bean.setPrice(each.getPrice());
					bean.setValue(each.getPrice()*bean.getAmount());
					this.baseDAO.updateEntityBean(bean);
					_logger.info("***update base bean***"+bean);
				}
			}

			if (!outIds.contains(each.getOutId())){
				outIds.add(each.getOutId());
			}
		}

		//update OutBean's total
		for (String outId : outIds){
			List<BaseBean> baseBeans = this.baseDAO.queryEntityBeansByFK(outId);
			double total = 0.0;
			for (BaseBean baseBean: baseBeans){
				total += baseBean.getAmount()*baseBean.getPrice();
			}
			_logger.info(outId + "****update total***" + total);
			this.outDAO.updateTotal(outId, total);
		}

		return true;
	}

	/***
	 * 批量更新销售单的 仓库、仓区
	 */
	@Transactional(rollbackFor = MYException.class)
	public boolean batchUpdateDepot(List<BaseBean> list)
	throws MYException
	{
		if (ListTools.isEmptyOrNull(list))
		{
			throw new MYException("导入异常");
		}
		
		// 本批次导入，检查订单对应的产品数与单据中行项目数是否一致，须一致
		Map<String, Integer> cmap = new HashMap<String, Integer>();
		
		for (BaseBean each : list) {
			if (cmap.containsKey(each.getOutId())) {
				int i = cmap.get(each.getOutId());
				
				cmap.put(each.getOutId(), i + 1);
			} else {
				cmap.put(each.getOutId(), 1);
			}
		}
		
		for (Entry<String, Integer> eachMap : cmap.entrySet()) {
			int count = baseDAO.countByFK(eachMap.getKey());
			
			if (count != eachMap.getValue()) {
				throw new MYException("批量修改仓库，要求导入的文件中含有全部的商品行项目.");
			}
		}
		
		for(BaseBean each : list)
		{
			OutBean out = outDAO.find(each.getOutId());
			
			if (out == null)
			{
				throw new MYException("导入异常，请退出重新登陆");
			}
			
			if (out.getStatus() != OutConstant.STATUS_SUBMIT) {
				throw new MYException("[%s]状态不是待商务审批。", each.getOutId());
			}

			outDAO.updateLocation(each.getOutId(), each.getLocationId());

			baseDAO.updateLocationIdAndDepotpartByOutIdAndProductId(each.getLocationId(), each.getDepotpartId(),
					each.getDepotpartName(), each.getOutId(), each.getProductId());
			
			operationLog.info("仓库更新，销售单：" + each.getOutId());
		}
		
		return true;
	}
	
	/***
	 * 批量延期账期
	 */
	@Transactional(rollbackFor = MYException.class)
	public boolean batchUpdateRedate(List<OutBean> list)
	throws MYException
	{
		for(OutBean each : list)
		{
			OutBean out = outDAO.find(each.getFullId());
			
			if (null != out)
			{
				out.setReday(out.getReday() + each.getReday());
				
				out.setRedate(TimeTools.getSpecialDateStringByDays(out.getRedate() + " 00:00:01", each.getReday(), "yyyy-MM-dd"));
				
				outDAO.updateEntityBean(out);
			}
		}
		
		return true;
	}
	
	/**
	 * preUseAmountCheck
	 */
	public List<OutImportBean> preUseAmountCheck(String batchId)
	{
		List<OutImportBean> list = outImportDAO.queryEntityBeansByFK(batchId);
		
		Map<String, OutImportBean> map = new HashMap<String, OutImportBean>();
		
		// 1.根据产品进行分组
		for (OutImportBean each : list)
		{
            //预占库存只针对招行销售单
            //2014/12/1 去掉此限制
//			if (each.getItype() != 2)
//			{
//				break;
//			}
			
			if (each.getPreUse() == OutImportConstant.PREUSE_YES)
			{
				continue;
			}
			
			OutBean out = outDAO.find(each.getOANo());

			if (null == out)
			{
				continue;
			}
			
			if (out.getStatus() != OutConstant.STATUS_SUBMIT)
			{
				continue;
			}
			
			if (!map.containsKey(each.getProductId()))
			{
				map.put(each.getProductId(), each);
			}else
			{
				OutImportBean bean = map.get(each.getProductId());
				
				bean.setAmount(bean.getAmount() + each.getAmount());
			}
		}
		
		List<OutImportBean> resultList = new ArrayList<OutImportBean>();

		// 2.根据汇总的数据，查找商品的库总数
		for (Entry<String, OutImportBean> entry : map.entrySet())
		{
			OutImportBean each = entry.getValue();
			
			resultList.add(each);
			
			ProductChangeWrap wrap = new ProductChangeWrap();

            wrap.setDepotpartId(each.getDepotpartId());
            wrap.setPrice(0);
            wrap.setProductId(each.getProductId());
            wrap.setStafferId("0");

            wrap.setChange( -each.getAmount());

            List<StorageRelationBean> relationList = null;
			try
			{
				relationList = storageRelationManager.checkStorageRelation3(wrap, false);
			}
			catch (MYException e)
			{
				each.setMayAmount(0);
				
				continue;
			}
            
            // 足够库存，开始拆分
            if (!ListTools.isEmptyOrNull(relationList))
            {
            	int amount = 0;
            	for (StorageRelationBean eachs : relationList)
            	{
            		if (eachs.getAmount() <= 0)
            			continue;
            		
            		amount += eachs.getAmount();
            	}
            	
            	each.setMayAmount(amount);
            }
            else
            {
            	each.setMayAmount(0);
            }
		}
		
		return resultList;
	}
	
	/**
	 * processSplitOut
	 */
	public void processSplitOut(final String batchId)
	{
		List<OutImportBean> list = outImportDAO.queryEntityBeansByFK(batchId);
		
		for (OutImportBean each : list)
		{
            //预占库存只针对招行销售单
            //2014/11/30 去掉此限制
//			if (each.getItype() != 2)
//			{
//				break;
//			}
			
			if (each.getPreUse() == OutImportConstant.PREUSE_YES)
			{
				continue;
			}

			final OutBean out = outDAO.find(each.getOANo());

			if (null == out)
			{
				continue;
			}
			
			if (out.getStatus() != OutConstant.STATUS_SUBMIT)
			{
				continue;
			}
			
			final List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(out.getFullId());
			
			if (baseList.get(0).getCostPrice() != 0)
			{
				continue;
			}
			
			final String id = each.getFullId();
			
        	TransactionTemplate tran = new TransactionTemplate(transactionManager);
        	
            try
            {
                tran.execute(new TransactionCallback()
                {
                    public Object doInTransaction(TransactionStatus arg0)
                    {
                		List<BaseBean> newBaseList = null;
						try
						{
							newBaseList = outManager.splitBase(baseList);
						}
						catch (MYException e)
						{
							throw new RuntimeException(e.getErrorContent(), e);
						}

						baseDAO.deleteEntityBeansByFK(out.getFullId());
                    	
                    	baseDAO.saveAllEntityBeans(newBaseList);
                    	
                    	// 更新状态
                    	outImportDAO.updatePreUse(id, OutImportConstant.PREUSE_YES);
                    	
                    	/*if (!link.contains(batchId))
                    	{
                    		outImportLogDAO.updateStatus(batchId, OutImportConstant.LOGSTATUS_SUCCESSPREUSE);
                    		
                    		link.add(batchId);
                    	}*/
                    	
                        return Boolean.TRUE;
                    }
                });
            }
            catch (Exception e)
            {
            	operationLog.error(e, e);
            }
		}
		
	}

	@Override
	public void batchProcessSplitOut(List<OutBean> list) {
        if (ListTools.isEmptyOrNull(list)){
            _logger.info("batchProcessSplitOut no list!");
        } else{
            _logger.info("batchProcessSplitOut with list size:"+list.size());
            for (OutBean each : list)
            {
                final OutBean out = outDAO.find(each.getFullId());

                if (null == out)
                {
                    continue;
                }

                if (out.getStatus() != OutConstant.STATUS_SUBMIT)
                {
                    continue;
                }

                final List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(out.getFullId());

                if (baseList.get(0).getCostPrice() != 0)
                {
                    continue;
                }

                TransactionTemplate tran = new TransactionTemplate(transactionManager);

                try
                {
                    tran.execute(new TransactionCallback()
                    {
                        public Object doInTransaction(TransactionStatus arg0)
                        {
                            List<BaseBean> newBaseList = null;
                            try
                            {
                                newBaseList = outManager.splitBase(baseList);
                            }
                            catch (MYException e)
                            {
                                throw new RuntimeException(e.getErrorContent(), e);
                            }

                            baseDAO.deleteEntityBeansByFK(out.getFullId());

                            baseDAO.saveAllEntityBeans(newBaseList);
                            _logger.info("update new base List:"+newBaseList);

                            // 更新状态
                            outImportDAO.updatePreUseByFullId(out.getFullId(), OutImportConstant.PREUSE_YES);
                            _logger.info(newBaseList+" update new base List; import bean status:"+out.getFullId());

                            return Boolean.TRUE;
                        }
                    });
                }
                catch (Exception e)
                {
                    operationLog.error(e, e);
                }
            }
        }
	}

	@Transactional(rollbackFor = MYException.class)
	public String addBankSail(User user, List<BankSailBean> list) throws MYException
	{
		JudgeTools.judgeParameterIsNull(user, list);
		
		String batchId = commonDAO.getSquenceString20();
		
		for (BankSailBean each : list) {
			each.setBatchId(batchId);
		}
		
		bankSailDAO.saveAllEntityBeans(list);
		
		return batchId;
	}
	
	@Transactional(rollbackFor = MYException.class)
	public boolean deleteBankSail(User user,String batchId)	throws MYException {
		JudgeTools.judgeParameterIsNull(user, batchId);
		
		bankSailDAO.deleteEntityBeansByFK(batchId);

		operationLog.info(user.getStafferName() + " /delete 银行导入销售数据, 批次号:" + batchId);
		
		return true;
	}
	
	@Transactional(rollbackFor = MYException.class)
	public boolean addEstimateProfit(User user, List<EstimateProfitBean> list)
			throws MYException
	{
		JudgeTools.judgeParameterIsNull(user, list);
		
		//String batchId = commonDAO.getSquenceString20();
		
		for (EstimateProfitBean each : list) {
			EstimateProfitBean old = estimateProfitDAO.findByUnique(each.getProductName());
			
			if (old != null)
			{
				estimateProfitDAO.deleteEntityBean(old.getId());
			}
		}
		
		estimateProfitDAO.saveAllEntityBeans(list);
		
		return true;
	}

	@Transactional(rollbackFor = MYException.class)
	public boolean deleteEstimateProfit(User user, String id)
			throws MYException
	{
		JudgeTools.judgeParameterIsNull(user, id);
		
		EstimateProfitBean bean = estimateProfitDAO.find(id);
		
		if (null != bean) {
			operationLog.info(user.getStafferName() + " /delete 银行导入产品预估毛利, 产品:" + bean.getProductName());
		}
		
		estimateProfitDAO.deleteEntityBean(id);

		return true;
	}

	/**
	 * 2016/4/14 #222
	 * 邮件附件下载并生成SO单JOB
	 */
	@Override
	@Transactional(rollbackFor = MYException.class)
	public void downloadOrderFromMailAttachment(){
		_logger.info("***downloadOrderFromMailAttachment running***");
		try {
            //step1 download email to temp order table
			List<String> mailList = this.imapMailClient.receiveEmail("imap.exmail.qq.com", "yycoinoa@yycoin.com", "Yycoin135");

            for (String mailId : mailList){
                //step2 convert to out import table
				Map<String,List<OutImportBean>>  mail2ImportMap = this.imapMailClient.convertToOutImport(mailId);
				for (List<OutImportBean> importItemList: mail2ImportMap.values()){
					if (ListTools.isEmptyOrNull(importItemList)){
						_logger.info("No out to process***");
					} else{
						_logger.info(mailId+"***begin import order***"+importItemList.size());
						String batchId = "";
						try
						{
							batchId = this.addBean(importItemList);
						}
						catch(MYException e)
						{
							_logger.error("Fail to import order from mail：",e);
						}

						// 异步处理
						List<OutImportBean> list = outImportDAO.queryEntityBeansByFK(batchId);

						if (!ListTools.isEmptyOrNull(list))
						{
							_logger.info("before outImportManager.processAsyn***"+list.size());
							this.processAsyn(list);
						}

						//step3 callback to update temp order flag after import OA
						this.imapMailClient.onCreateOA(mailId, list);
					}
				}
            }
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private List<OlBaseBean> removeDuplicateOlBaseBeans(List<OlBaseBean> olOutBeans){
		List<OlBaseBean> result = new ArrayList<OlBaseBean>();
		for (OlBaseBean olOutBean: olOutBeans){
			if (!result.contains(olOutBean)){
				result.add(olOutBean);
			}
		}
		return result;
	}

    @Override
    @Transactional(rollbackFor = MYException.class)
    public void offlineOrderJob() {
        //To change body of implemented methods use File | Settings | File Templates.
        //status=0 and pricestatus=1 and ibmostatus=1 同时满足这三个条件的订单被抓取生成OA订单
        ConditionParse conditionParse = new ConditionParse();
        conditionParse.addCondition("status","=","0");
        conditionParse.addCondition("priceStatus","=","1");
        conditionParse.addCondition("ibMotStatus","=","1");
        List<OlOutBean> olOutBeans = this.olOutDAO.queryEntityBeansByCondition(conditionParse);
		//<code,product>
		Map<String,ProductBean> productCodeMap = new HashMap<String,ProductBean>();
		_logger.info("***offlineOrderJob running with size "+olOutBeans.size());
        if (!ListTools.isEmptyOrNull(olOutBeans)){
            for (OlOutBean olOutBean : olOutBeans){
                ConditionParse con2 = new ConditionParse();
                con2.addCondition("outId","=",olOutBean.getOlFullId());
                List<OlBaseBean> olBaseBeans2 = this.olBaseDAO.queryEntityBeansByCondition(con2);
                if (ListTools.isEmptyOrNull(olBaseBeans2)){
                    _logger.error("No OlBaseBean found "+olOutBean.getOlFullId());
					this.updateOlOutDescription(olOutBean,olOutBean.getDescription()+"_ERROR_"+"olbase表没有对应记录");
                    continue;
                } else{
					//check duplicate olbase bean
					List<OlBaseBean> olBaseBeans = this.removeDuplicateOlBaseBeans(olBaseBeans2);
					_logger.info(olOutBean.getOlFullId() + "***olBaseBeans " + olBaseBeans.size()+" after remove duplicate "+olBaseBeans.size());
					if (olBaseBeans.size() >=50){
						_logger.error("Too many OlBaseBean found "+olOutBean.getOlFullId());
						this.updateOlOutDescription(olOutBean,olOutBean.getDescription()+"_ERROR_"+"olbase表记录太多");
						continue;
					}
                    //<sailInvoice,taxRate>
                    Map<String, Double> sailInvoice2TaxRateMap = new HashMap<String, Double>();
                    for(OlBaseBean olBaseBean : olBaseBeans){
						String productCode = olBaseBean.getProductCode();
                        ProductBean productBean = this.productDAO.findByUnique(productCode);
                        if (productBean == null){
                            _logger.error("No product found "+productCode);
							this.updateOlOutDescription(olOutBean, olOutBean.getDescription() + "_ERROR_" + "product code不存在:" + productCode);
							continue;
                        } else{
							productCodeMap.put(productCode, productBean);
                            String sailInvoice = productBean.getSailInvoice().trim();
                            if (StringTools.isNullOrNone(sailInvoice)){
                                _logger.error("sailInvoice is empty for product "+productCode);
								this.updateOlOutDescription(olOutBean, olOutBean.getDescription() + "_ERROR_" + "product表sailInvoice为空:" + productCode);
								continue;
                            } else{
                                if (sailInvoice2TaxRateMap.get(sailInvoice) == null){
                                    InvoiceBean  invoiceBean = this.invoiceDAO.find(sailInvoice);
                                    if (invoiceBean == null){
                                        _logger.error("Invoice not found "+sailInvoice);
										this.updateOlOutDescription(olOutBean, olOutBean.getDescription() + "_ERROR_" + "invoice表不存在:" + sailInvoice);
                                        continue;
                                    } else{
                                        sailInvoice2TaxRateMap.put(sailInvoice, invoiceBean.getVal()/100);
                                    }
                                }
                            }
                        }
                    }

					for (OlBaseBean olBaseBean : olBaseBeans){
						OutBean out = new OutBean();

                        out.setType(OutConstant.OUT_TYPE_OUTBILL);
						try {
							out.setOutType(Integer.valueOf(olOutBean.getType()));
						}catch(NumberFormatException e){
							_logger.error("Type不是整数:"+olOutBean.getType());
							this.updateOlOutDescription(olOutBean,olOutBean.getDescription()+"_ERROR_"+"Type不是整数");
							continue;
						}
						out.setStafferId(olOutBean.getStafferId());
                        out.setStafferName(olOutBean.getStafferName());
                        out.setCustomerId(olOutBean.getCustomerId());
                        out.setCustomerName(olOutBean.getCustomerName());
                        //在生成OA订单时，把olfullid写入订单备注
						String prefix = "OLFULLID_";
						if (StringTools.isNullOrNone(olOutBean.getDescription())){
							out.setDescription(prefix+ olOutBean.getOlFullId());
						} else{
							out.setDescription(olOutBean.getDescription() + prefix + olOutBean.getOlFullId());
						}
						out.setEmergency(olOutBean.getEmergency());
                        String now = TimeTools.now_short();
                        out.setOutTime(now);
                        out.setPodate(now);
                        out.setRedate(now);
                        out.setArriveDate(now);
                        String nowLong = TimeTools.now();
                        out.setLogTime(nowLong);
                        out.setChangeTime(nowLong);

                        out.setOperatorName("系统");

                        //industryid,2,3几个字段根据stafferid到表oastaffer表取对应值
						String stafferId = out.getStafferId();
                        StafferBean stafferBean = stafferDAO.find(stafferId);
                        if (stafferBean == null){
                            _logger.error("No staffer found "+stafferId);
							this.updateOlOutDescription(olOutBean, olOutBean.getDescription() + "_ERROR_" + "stafferId不存在:" + stafferId);
							continue;
                        } else{
                            out.setIndustryId(stafferBean.getIndustryId());
                            out.setIndustryId2(stafferBean.getIndustryId2());
                            out.setIndustryId3(stafferBean.getIndustryId3());
                        }
						String id = getAll(commonDAO.getSquence());
                        String time = TimeTools.getStringByFormat(new Date(), "yyMMddHHmm");
                        String flag = OutHelper.getSailHead(out.getType(), out.getOutType());

                        String fullId = flag + time + id;
                        out.setId(getOutId(id));
                        out.setFullId(fullId);

						DistributionBean distributionBean = new DistributionBean();
                        distributionBean.setId(commonDAO.getSquenceString20(IDPrefixConstant.ID_DISTRIBUTION_PRIFIX));
                        distributionBean.setOutId(out.getFullId());
						try {
							distributionBean.setShipping(Integer.valueOf(olOutBean.getExpress()));
						}catch (NumberFormatException e){
							_logger.error("express不是整数:"+olOutBean.getExpress());
							this.updateOlOutDescription(olOutBean,olOutBean.getDescription()+"_ERROR_"+"express不是整数");
							continue;
						}
						try {
							distributionBean.setExpressPay(Integer.valueOf(olOutBean.getExpressPay()));
						}catch (NumberFormatException e){
							_logger.error("expressPay不是整数:"+olOutBean.getExpressPay());
							this.updateOlOutDescription(olOutBean,olOutBean.getDescription()+"_ERROR_"+"expressPay不是整数");
							continue;
						}

                        //快递
                        if(distributionBean.getShipping() == OutConstant.OUT_SHIPPING_3PL
								|| distributionBean.getShipping() == OutConstant.OUT_SHIPPING_PROXY){
							try {
								distributionBean.setTransport1(Integer.valueOf(olOutBean.getExpressCompany()));
							}catch (NumberFormatException e){
								_logger.error("expressCompany不是整数:"+olOutBean.getExpressPay());
								this.updateOlOutDescription(olOutBean,olOutBean.getDescription()+"_ERROR_"+"expressCompany不是整数");
								continue;
							}
                        } else if (distributionBean.getShipping() ==  OutConstant.OUT_SHIPPING_SELFSERVICE){
                            //自提
							try {
								distributionBean.setTransport2(Integer.valueOf(olOutBean.getExpressCompany()));
							}catch(NumberFormatException e){
								_logger.error("expressCompany不是整数:"+olOutBean.getExpressPay());
								this.updateOlOutDescription(olOutBean,olOutBean.getDescription()+"_ERROR_"+"expressCompany不是整数");
								continue;
							}
                        }

                        distributionBean.setProvinceId(olOutBean.getProvinceId());
                        distributionBean.setCityId(olOutBean.getCityId());
                        distributionBean.setAddress(olOutBean.getAddress());
                        distributionBean.setReceiver(olOutBean.getReceiver());
                        distributionBean.setMobile(olOutBean.getTelephone());
                        distributionDAO.saveEntityBean(distributionBean);

                        //olbase表中的字段写入OA的 base表中的对应同名字段，value取对应商品的amount*price
                        double total = 0.0f;
//                        List<OlBaseBean> olBaseBeansList = sailInvoice2lBaseMap.get(key);
                        List<BaseBean> baseBeans = new ArrayList<BaseBean>();

						//total根据olfullid到表olbase中取对outid相同的行项目的，amount*price的合计
						total += olBaseBean.getAmount()*olBaseBean.getPrice();
						//olbase表中的字段写入OA的 base表中的对应同名字段，value取对应商品的amount*price
						BaseBean baseBean = new BaseBean();
						baseBean.setId(commonDAO.getSquenceString());
						baseBean.setOutId(fullId);

                        if (StringTools.isNullOrNone(olBaseBean.getDepot())){
                            baseBean.setLocationId(DepotConstant.CENTER_DEPOT_ID);
                            baseBean.setDepotpartId("1");
                            baseBean.setDepotpartName("可发成品仓");
                        } else{
                            baseBean.setLocationId(olBaseBean.getDepot());
                            DepotpartBean defaultOKDepotpart = depotpartDAO.findDefaultOKDepotpart(olBaseBean.getDepot());
                            if (defaultOKDepotpart == null){
                                _logger.error("defaultOKDepotpart is null:"+olBaseBean.getDepot());
                                this.updateOlOutDescription(olOutBean,olOutBean.getDescription()+"_ERROR_"+"depot不存在");
                                continue;
                            } else{
                                baseBean.setDepotpartId(defaultOKDepotpart.getId());
                                baseBean.setDepotpartName(defaultOKDepotpart.getName());
                            }
                        }

						ProductBean product = productCodeMap.get(olBaseBean.getProductCode());
						if (product == null) {
							_logger.error("no product");
							continue;
						} else{
							baseBean.setProductId(product.getId());
						}
						baseBean.setProductName(olBaseBean.getProductName());

						baseBean.setUnit("套");
						baseBean.setAmount(olBaseBean.getAmount());
						baseBean.setPrice(olBaseBean.getPrice());
						baseBean.setValue(olBaseBean.getAmount() * olBaseBean.getPrice());

						baseBean.setIbMoney(olBaseBean.getIbMoney());
						baseBean.setMotivationMoney(olBaseBean.getMotivationMoney());

						baseBean.setOwner("0");
						baseBean.setOwnerName("公共");

						// 业务员结算价，总部结算价
						double sailPrice = product.getSailPrice();

						// 根据配置获取结算价
						List<PriceConfigBean> pcblist = priceConfigDAO.querySailPricebyProductId(product.getId());

						if (!ListTools.isEmptyOrNull(pcblist))
						{
							PriceConfigBean cb = priceConfigManager.calcSailPrice(pcblist.get(0));

							sailPrice = cb.getSailPrice();
						}

						// 获取销售配置
						SailConfBean sailConf = sailConfigManager.findProductConf(stafferBean,
								product);

						// 总部结算价(产品结算价 * (1 + 总部结算率))
						baseBean.setPprice(sailPrice
								* (1 + sailConf.getPratio() / 1000.0d));

						// 事业部结算价(产品结算价 * (1 + 总部结算率 + 事业部结算率))
						baseBean.setIprice(sailPrice
								* (1 + sailConf.getIratio() / 1000.0d + sailConf
								.getPratio() / 1000.0d));

						// 业务员结算价就是事业部结算价
						baseBean.setInputPrice(baseBean.getIprice());

						if (baseBean.getInputPrice() == 0)
						{
							_logger.error(baseBean.getProductName() + " 业务员结算价不能为0");
							this.updateOlOutDescription(olOutBean, olOutBean.getDescription() + "_ERROR_" + baseBean.getProductName() + " 业务员结算价不能为0");
							continue;
						}

						// 配送 方式及毛利率
						baseBean.setDeliverType(0);

						// 毛利，毛利率（针对业务员的）
						double profit = 0.0d;

						double profitRatio = 0.0d;

						if (baseBean.getValue() != 0)
						{
							profit = baseBean.getAmount() * (baseBean.getPrice() - baseBean.getInputPrice());

							profitRatio = profit / baseBean.getValue();
						}

						baseBean.setProfit(profit);
						baseBean.setProfitRatio(profitRatio);

						String key = product.getSailInvoice().trim();
						if (sailInvoice2TaxRateMap.get(key) == null){
							_logger.error("税率为空");
							this.updateOlOutDescription(olOutBean, olOutBean.getDescription() + "_ERROR_" + " 税率为空"+key);
							continue;
						} else{
							baseBean.setTaxrate(sailInvoice2TaxRateMap.get(key));
						}

						baseBeans.add(baseBean);

						//生成OA订单后，回写OA单号至olout与olbase表的OANO字段
						this.olBaseDAO.updateOaNo(olBaseBean.getId(), fullId);

                        out.setTotal(total);
                        out.setStatus(OutConstant.STATUS_SUBMIT);
                        out.setLocation(DepotConstant.CENTER_DEPOT_ID);
                        out.setLocationId("999");

                        out.setInvoiceId(key);
                        out.setDutyId("90201008080000000001");
                        out.setReserve3(OutConstant.OUT_SAIL_TYPE_CREDIT_AND_CUR);
                        out.setFlowId("CITIC");
						out.setDepotpartId(baseBean.getDepotpartId());

						try {
							outDAO.saveEntityBean(out);
							baseDAO.saveAllEntityBeans(baseBeans);
							_logger.info("create out in offlineOrderJob " + out);
							this.olOutDAO.updateStatus(olOutBean.getOlFullId(), 9);
							this.clearOlOutErrorDescription(olOutBean);
							addOutLog(fullId, null, out, "提交", SailConstant.OPR_OUT_PASS, 1);
						}catch(Exception e){
							_logger.error("数据库异常" + olOutBean.getOlFullId());
							this.updateOlOutDescription(olOutBean, olOutBean.getDescription() + "_ERROR_" + "数据库异常，请检查是否数据重复");
							continue;
						}
                    }
                }
            }
        }
    }

	private void updateOlOutDescription(OlOutBean item, String description){
		if (StringTools.isNullOrNone(item.getDescription()) || !item.getDescription().contains("ERROR")){
			this.olOutDAO.updateDescription(item.getOlFullId(), description);
		}
	}

	private void clearOlOutErrorDescription(OlOutBean item){
		if (!StringTools.isNullOrNone(item.getDescription()) && item.getDescription().contains("ERROR")){
			String description = item.getDescription().split("ERROR")[0];
			this.olOutDAO.updateDescription(item.getOlFullId(), description);
		}
	}

    private void mergeItem(List<OutBackItemBean> items,OutBackItemBean item){
        boolean merged = false;
        for (OutBackItemBean itemBean: items){
			if (itemBean.equals(item)){
				itemBean.setDuplicate(true);
				item.setDuplicate(true);
				break;
			} else if (itemBean.getOutId().equals(item.getOutId()) && itemBean.getProductId().equals(item.getProductId())) {
				try {
					int sum = Integer.valueOf(itemBean.getAmount()) + Integer.valueOf(item.getAmount());
					itemBean.setAmount(String.valueOf(sum));
				}catch(NumberFormatException e){e.printStackTrace();}
                merged = true;
                break;
            }
        }

        if(!merged){
            items.add(item);
        }
    }

    @Override
    @Transactional(rollbackFor = MYException.class)
    public void offlineStorageInJob() {
        //To change body of implemented methods use File | Settings | File Templates.
        ConditionParse conditionParse = new ConditionParse();
        conditionParse.addCondition("status","=","0");
        List<OutBackItemBean> outBackItemBeans = this.outBackItemDAO.queryEntityBeansByCondition(conditionParse);
        _logger.info("***offlineStorageInJob with item size "+outBackItemBeans.size());
        if (!ListTools.isEmptyOrNull(outBackItemBeans)){
            //一行对应一单,生成out/base表
            for (OutBackItemBean item : outBackItemBeans){
                StringBuilder generatedOutId = new StringBuilder();
                //检查对应销售单的出库数量，减去已退库数量，与outback_item当前行的数量比较，如大于，则生成退库单
                String outId = item.getOutId();
                OutBean originalOut = this.outDAO.find(outId);

                if (originalOut == null){
                    _logger.error("No out found "+outId);
                    this.updateDescriptionByOutId(item,item.getDescription()+"_ERROR_"+"no t_center_out found");
                    continue;
                } else{
                    double value = 0;

                    //industryid,2,3几个字段根据stafferid到表oastaffer表取对应值
                    StafferBean stafferBean = stafferDAO.find(originalOut.getStafferId());
                    if (stafferBean == null){
                        _logger.error("No staffer found "+originalOut.getStafferId());
                        continue;
                    }

                    //原销售单出库数量
                    int total = 0;
                    ConditionParse conditionParse1 = new ConditionParse();
                    conditionParse1.addCondition("outId","=",outId);
                    conditionParse1.addCondition("productId","=",item.getProductId());
                    //原销售单base表
                    List<BaseBean> outBaseBeans = baseDAO.queryEntityBeansByCondition(conditionParse1);
                    if (ListTools.isEmptyOrNull(outBaseBeans)){
                        _logger.error("No base beans found "+outId);
                        this.updateDescription(item,item.getDescription()+"_ERROR_"+"t_center_base does not exist:"+outId+":productId:"+item.getProductId());
                        continue;
                    } else{
                        for (BaseBean baseBean : outBaseBeans){
                            total += baseBean.getAmount();
                        }
                    }

                    // 减去已退库数量
                    //拿outid,到out表的refoutid里找相同的单号且状态为3或4，type为1
                    int stockInAmount = 0;
                    ConditionParse conditionParse2 = new ConditionParse();
                    conditionParse2.addCondition("refOutFullId","=", outId);
                    conditionParse2.addCondition("type","=", 1);
                    conditionParse2.addCondition(" and status in (3,4)");
                    List<OutBean> outBeans = this.outDAO.queryEntityBeansByCondition(conditionParse2);
                    //所有已退库的base记录
                    List<BaseBean> inBaseBeans = new ArrayList<BaseBean>();
                    if (!ListTools.isEmptyOrNull(outBeans)){
                        for (OutBean bean: outBeans){
                            ConditionParse conditionParse3 = new ConditionParse();
                            conditionParse3.addCondition("outId","=",bean.getFullId());
                            conditionParse3.addCondition("productId","=",item.getProductId());
                            List<BaseBean> baseBeans = baseDAO.queryEntityBeansByCondition(conditionParse3);
                            if (!ListTools.isEmptyOrNull(baseBeans)){
                                inBaseBeans.addAll(baseBeans);
                                for (BaseBean baseBean : baseBeans){
                                    stockInAmount += baseBean.getAmount();
                                }
                            }
                        }
                    }

                    _logger.info("***total " + total + "***stockInAmount***" + stockInAmount + "***to in**" + item.getAmount());

                    int amount = 0;

                    try {
                        amount = Integer.valueOf(item.getAmount());
                    }catch(NumberFormatException e){
                        _logger.error("amount should be integer "+item.getAmount());
                        this.updateDescription(item,item.getDescription()+"_ERROR_"+"amount should be integer:"+item.getAmount());
                        continue;
                    }

                    if (total - stockInAmount>= amount){
                        //同一产品因成本不一样可能会有多个商品行，但退库时可能一起退单，需要根据不同成本拆单
                        List<BaseBean> refBaseBeans = this.findBaseBeanToBack(amount, outBaseBeans, inBaseBeans);
                        _logger.info("***refBaseBeans size***"+refBaseBeans.size());
                        for (BaseBean refBaseBean : refBaseBeans){
                            _logger.info("***find refBaseBean ***"+refBaseBean);
                            OutBean outBean = new OutBean();
							outBean.setType(OutConstant.OUT_TYPE_INBILL);
							outBean.setOutType(Integer.valueOf(item.getType()));

                            String id = getAll(commonDAO.getSquence());
                            String time = TimeTools.getStringByFormat(new Date(), "yyMMddHHmm");
                            String flag = OutHelper.getSailHead(outBean.getType(), outBean.getOutType());

                            String fullId = flag + time + id;
                            outBean.setId(getOutId(id));
                            outBean.setFullId(fullId);

                            outBean.setIndustryId(stafferBean.getIndustryId());
                            outBean.setIndustryId2(stafferBean.getIndustryId2());
                            outBean.setIndustryId3(stafferBean.getIndustryId3());

                            outBean.setStafferId(originalOut.getStafferId());
                            outBean.setStafferName(originalOut.getStafferName());
                            outBean.setCustomerId(originalOut.getCustomerId());
                            if ("99".equals(outBean.getCustomerId())){
                                outBean.setCustomerName("系统内置供应商");
                            } else{
                                outBean.setCustomerName(originalOut.getCustomerName());
                            }

                            outBean.setDescription("线下入库"+"_"+outId);
                            String now = TimeTools.now_short();
                            outBean.setOutTime(now);
                            outBean.setPodate(now);
                            outBean.setRedate(now);
                            outBean.setArriveDate(now);
                            String nowLong = TimeTools.now();
                            outBean.setLogTime(nowLong);
                            outBean.setChangeTime(nowLong);

                            outBean.setOperatorName("系统");


                            BaseBean baseBean = new BaseBean();

                            baseBean.setId(commonDAO.getSquenceString());
                            baseBean.setOutId(fullId);

                            if (StringTools.isNullOrNone(item.getDepot())){
                                baseBean.setLocationId(DepotConstant.CENTER_DEPOT_ID);
                                baseBean.setDepotpartId("1");
                                baseBean.setDepotpartName("可发成品仓");
                            } else{
                                baseBean.setLocationId(item.getDepot());
                                DepotpartBean defaultOKDepotpart = depotpartDAO.findDefaultOKDepotpart(item.getDepot());
                                if (defaultOKDepotpart == null){
                                    _logger.error("defaultOKDepotpart is null:"+item.getDepot());
                                    this.updateDescription(item,item.getDescription()+"_ERROR_"+"depot does not exist:"+item.getDepot());
                                    continue;
                                } else{
                                    baseBean.setDepotpartId(defaultOKDepotpart.getId());
                                    baseBean.setDepotpartName(defaultOKDepotpart.getName());
                                }
                            }

                            ProductBean product = this.productDAO.find(item.getProductId());
                            if (product == null){
                                _logger.error("No product found "+item.getProductId());
                                this.updateDescription(item,item.getDescription()+"_ERROR_"+"no product:"+item.getProductId());
                                continue;
                            }
                            baseBean.setProductId(item.getProductId());
                            baseBean.setProductName(item.getProductName());

                            baseBean.setUnit("套");
                            baseBean.setAmount(refBaseBean.getAmount());
                            baseBean.setPrice(refBaseBean.getPrice());
                            baseBean.setValue(baseBean.getAmount() * baseBean.getPrice());
                            baseBean.setCostPrice(refBaseBean.getCostPrice());
                            baseBean.setCostPriceKey(StorageRelationHelper
                                    .getPriceKey(baseBean.getCostPrice()));

                            baseBean.setOwner("0");
                            baseBean.setOwnerName("公共");

                            // 业务员结算价，总部结算价
                            double sailPrice = product.getSailPrice();

                            // 根据配置获取结算价
                            List<PriceConfigBean> pcblist = priceConfigDAO.querySailPricebyProductId(product.getId());

                            if (!ListTools.isEmptyOrNull(pcblist))
                            {
                                PriceConfigBean cb = priceConfigManager.calcSailPrice(pcblist.get(0));

                                sailPrice = cb.getSailPrice();
                            }

                            // 获取销售配置
                            SailConfBean sailConf = sailConfigManager.findProductConf(stafferBean,
                                    product);

                            // 总部结算价(产品结算价 * (1 + 总部结算率))
                            baseBean.setPprice(sailPrice
                                    * (1 + sailConf.getPratio() / 1000.0d));

                            // 事业部结算价(产品结算价 * (1 + 总部结算率 + 事业部结算率))
                            baseBean.setIprice(sailPrice
                                    * (1 + sailConf.getIratio() / 1000.0d + sailConf
                                    .getPratio() / 1000.0d));

                            // 业务员结算价就是事业部结算价
                            baseBean.setInputPrice(baseBean.getIprice());

                            if (baseBean.getInputPrice() == 0)
                            {
                                _logger.error(baseBean.getProductName() + " 业务员结算价不能为0");
                                this.updateDescription(item,item.getDescription()+"_ERROR_"+baseBean.getProductName() + " t_center_base inputPrice should not be 0");
                                continue;
                            }

                            value += baseBean.getValue();

                            //生成退货单号时，也同时写入t_center_outback表的description字段中，增加在现有字段后，根据outbackid 到outback表找对应的id
                            OutBackBean outBackBean = this.outBackDAO.find(item.getOutBackId());
                            if (outBackBean!= null){
                                this.outBackDAO.updateDescription(item.getOutBackId(), outBackBean.getDescription() + "_"+fullId);
                                //更新out表的transportNo
                                outBean.setTransportNo(outBackBean.getTransportNo());
                            }

                            outBean.setTotal(value);
                            outBean.setStatus(OutConstant.STATUS_SUBMIT);
                            outBean.setLocation(DepotConstant.CENTER_DEPOT_ID);
                            outBean.setLocationId("999");

                            outBean.setInvoiceId(originalOut.getInvoiceId());
                            outBean.setDutyId("90201008080000000001");
                            outBean.setRefOutFullId(outId);
							outBean.setDepotpartId(baseBean.getDepotpartId());
                            outDAO.saveEntityBean(outBean);
                            baseDAO.saveEntityBean(baseBean);
                            _logger.info("create out in offlineStorageInJob "+outBean+"***with base bean***"+baseBean);
                            generatedOutId.append(fullId).append(",");
                        }
                    } else{
                        _logger.error("Can not in stock " + outId);
                        this.updateDescription(item, item.getDescription() + "_ERROR_" + "amount exceed");
                        continue;
                    }
                }

                String fullId = generatedOutId.toString();
                if (!StringTools.isNullOrNone(fullId)){
                    this.outBackItemDAO.updateOano(item.getId(), StringUtils.removeEnd(fullId, ","));
                }
            }
        }
    }

    @Transactional(rollbackFor = MYException.class)
    @Deprecated   //original version
    public void offlineStorageInJob2() {
        //To change body of implemented methods use File | Settings | File Templates.
        ConditionParse conditionParse = new ConditionParse();
        conditionParse.addCondition("status","=","0");
        List<OutBackItemBean> outBackItemBeans = this.outBackItemDAO.queryEntityBeansByCondition(conditionParse);
        _logger.info("***offlineStorageInJob with item size "+outBackItemBeans.size());
        Map<String, List<OutBackItemBean>> outId2ItemMap = new HashMap<String, List<OutBackItemBean>>();
        if (!ListTools.isEmptyOrNull(outBackItemBeans)){
            //step1 同一outid合并起来,里面如果有同一productid也合并，把数量加起来
            for (OutBackItemBean item : outBackItemBeans){
                String outId = item.getOutId();
                if (outId2ItemMap.get(outId) == null){
                    List<OutBackItemBean> items = new ArrayList<OutBackItemBean>();
                    items.add(item);
                    outId2ItemMap.put(outId, items);
                } else{
                    List<OutBackItemBean> items = outId2ItemMap.get(outId);
                    this.mergeItem(items, item);
                }
            }

            //step2 生成out/base表
            for (String outId : outId2ItemMap.keySet()){
                //检查对应销售单的出库数量，减去已退库数量，与outback_item当前行的数量比较，如大于，则生成退库单
                OutBean originalOut = this.outDAO.find(outId);
				List<OutBackItemBean> items = outId2ItemMap.get(outId);
				_logger.info(outId+"***out back item size "+items.size());
				OutBackItemBean firstItem = items.get(0);

                if (originalOut == null){
                    _logger.error("No out found "+outId);
					this.updateDescriptionByOutId(firstItem,firstItem.getDescription()+"_ERROR_"+"no t_center_out found");
                    continue;
                } else{
                    List<BaseBean> baseBeanList = new ArrayList<BaseBean>();
                    double value = 0;

                    OutBean outBean = new OutBean();

                    outBean.setType(OutConstant.OUT_TYPE_INBILL);
                    outBean.setOutType(Integer.valueOf(firstItem.getType()));

                    outBean.setStafferId(originalOut.getStafferId());
                    outBean.setStafferName(originalOut.getStafferName());
                    outBean.setCustomerId(originalOut.getCustomerId());
                    if ("99".equals(outBean.getCustomerId())){
                        outBean.setCustomerName("系统内置供应商");
                    } else{
                        outBean.setCustomerName(originalOut.getCustomerName());
                    }

                    outBean.setDescription("线下入库"+"_"+outId);
//                    outBean.setEmergency(originalOut.getEmergency());
                    String now = TimeTools.now_short();
                    outBean.setOutTime(now);
                    outBean.setPodate(now);
                    outBean.setRedate(now);
                    outBean.setArriveDate(now);
                    String nowLong = TimeTools.now();
                    outBean.setLogTime(nowLong);
                    outBean.setChangeTime(nowLong);

                    outBean.setOperatorName("系统");

                    //industryid,2,3几个字段根据stafferid到表oastaffer表取对应值
                    StafferBean stafferBean = stafferDAO.find(originalOut.getStafferId());
                    if (stafferBean == null){
                        _logger.error("No staffer found "+originalOut.getStafferId());
                        continue;
                    } else{
                        outBean.setIndustryId(stafferBean.getIndustryId());
                        outBean.setIndustryId2(stafferBean.getIndustryId2());
                        outBean.setIndustryId3(stafferBean.getIndustryId3());
                    }

                    String id = getAll(commonDAO.getSquence());
                    String time = TimeTools.getStringByFormat(new Date(), "yyMMddHHmm");
                    String flag = OutHelper.getSailHead(outBean.getType(), outBean.getOutType());

                    String fullId = flag + time + id;
                    outBean.setId(getOutId(id));
                    outBean.setFullId(fullId);

                    for (OutBackItemBean item: items){
						if (item.isDuplicate()){
							_logger.error("Duplicate item "+item.getId());
							this.updateDescription(item,item.getDescription()+"_ERROR_"+"duplicate item");
							continue;
						}

                        //原销售单出库数量
                        int total = 0;
                        ConditionParse conditionParse1 = new ConditionParse();
                        conditionParse1.addCondition("outId","=",outId);
                        conditionParse1.addCondition("productId","=",item.getProductId());
						//原销售单base表
                        List<BaseBean> outBaseBeans = baseDAO.queryEntityBeansByCondition(conditionParse1);
                        if (ListTools.isEmptyOrNull(outBaseBeans)){
                            _logger.error("No base beans found "+outId);
                            this.updateDescription(item,item.getDescription()+"_ERROR_"+"t_center_base does not exist:"+outId+":productId:"+item.getProductId());
                            continue;
                        } else{
                            for (BaseBean baseBean : outBaseBeans){
                                total += baseBean.getAmount();
                            }
                        }

                        // 减去已退库数量
                        //拿outid,到out表的refoutid里找相同的单号且状态为3或4，type为1
                        int stockInAmount = 0;
                        ConditionParse conditionParse2 = new ConditionParse();
                        conditionParse2.addCondition("refOutFullId","=", outId);
                        conditionParse2.addCondition("type","=", 1);
                        conditionParse2.addCondition(" and status in (3,4)");
                        List<OutBean> outBeans = this.outDAO.queryEntityBeansByCondition(conditionParse2);
						//所有已退库的base记录
						List<BaseBean> inBaseBeans = new ArrayList<BaseBean>();
                        if (!ListTools.isEmptyOrNull(outBeans)){
                            for (OutBean bean: outBeans){
                                ConditionParse conditionParse3 = new ConditionParse();
                                conditionParse3.addCondition("outId","=",bean.getFullId());
                                conditionParse3.addCondition("productId","=",item.getProductId());
                                List<BaseBean> baseBeans = baseDAO.queryEntityBeansByCondition(conditionParse3);
                                if (!ListTools.isEmptyOrNull(baseBeans)){
									inBaseBeans.addAll(baseBeans);
                                    for (BaseBean baseBean : baseBeans){
                                        stockInAmount += baseBean.getAmount();
                                    }
                                }
                            }
                        }

                        _logger.info("***total " + total + "***stockInAmount***" + stockInAmount + "***to in**" + item.getAmount());

						int amount = 0;

						try {
							amount = Integer.valueOf(item.getAmount());
						}catch(NumberFormatException e){
							_logger.error("amount should be integer "+item.getAmount());
							this.updateDescription(item,item.getDescription()+"_ERROR_"+"amount should be integer:"+item.getAmount());
							continue;
						}

                        if (total - stockInAmount>= amount){
                            List<BaseBean> refBaseBeans = this.findBaseBeanToBack(amount, outBaseBeans, inBaseBeans);
                            for (BaseBean refBaseBean : refBaseBeans){
                                BaseBean baseBean = new BaseBean();

                                baseBean.setId(commonDAO.getSquenceString());
                                baseBean.setOutId(fullId);

                                if (StringTools.isNullOrNone(item.getDepot())){
                                    baseBean.setLocationId(DepotConstant.CENTER_DEPOT_ID);
                                    baseBean.setDepotpartId("1");
                                    baseBean.setDepotpartName("可发成品仓");
                                } else{
                                    baseBean.setLocationId(item.getDepot());
                                    DepotpartBean defaultOKDepotpart = depotpartDAO.findDefaultOKDepotpart(item.getDepot());
                                    if (defaultOKDepotpart == null){
                                        _logger.error("defaultOKDepotpart is null:"+item.getDepot());
                                        this.updateDescription(item,item.getDescription()+"_ERROR_"+"depot does not exist:"+item.getDepot());
                                        continue;
                                    } else{
                                        baseBean.setDepotpartId(defaultOKDepotpart.getId());
                                        baseBean.setDepotpartName(defaultOKDepotpart.getName());
                                    }
                                }

                                ProductBean product = this.productDAO.find(item.getProductId());
                                if (product == null){
                                    _logger.error("No product found "+item.getProductId());
                                    this.updateDescription(item,item.getDescription()+"_ERROR_"+"no product:"+item.getProductId());
                                    continue;
                                }
                                baseBean.setProductId(item.getProductId());
                                baseBean.setProductName(item.getProductName());

                                baseBean.setUnit("套");
                                baseBean.setAmount(amount);

                                baseBean.setPrice(refBaseBean.getPrice());
								baseBean.setValue(baseBean.getAmount() * baseBean.getPrice());
								baseBean.setCostPrice(refBaseBean.getCostPrice());
								baseBean.setCostPriceKey(StorageRelationHelper
										.getPriceKey(baseBean.getCostPrice()));

                                baseBean.setOwner("0");
                                baseBean.setOwnerName("公共");

                                // 业务员结算价，总部结算价
                                double sailPrice = product.getSailPrice();

                                // 根据配置获取结算价
                                List<PriceConfigBean> pcblist = priceConfigDAO.querySailPricebyProductId(product.getId());

                                if (!ListTools.isEmptyOrNull(pcblist))
                                {
                                    PriceConfigBean cb = priceConfigManager.calcSailPrice(pcblist.get(0));

                                    sailPrice = cb.getSailPrice();
                                }

                                // 获取销售配置
                                SailConfBean sailConf = sailConfigManager.findProductConf(stafferBean,
                                        product);

                                // 总部结算价(产品结算价 * (1 + 总部结算率))
                                baseBean.setPprice(sailPrice
                                        * (1 + sailConf.getPratio() / 1000.0d));

                                // 事业部结算价(产品结算价 * (1 + 总部结算率 + 事业部结算率))
                                baseBean.setIprice(sailPrice
                                        * (1 + sailConf.getIratio() / 1000.0d + sailConf
                                        .getPratio() / 1000.0d));

                                // 业务员结算价就是事业部结算价
                                baseBean.setInputPrice(baseBean.getIprice());

                                if (baseBean.getInputPrice() == 0)
                                {
                                    _logger.error(baseBean.getProductName() + " 业务员结算价不能为0");
                                    this.updateDescription(item,item.getDescription()+"_ERROR_"+baseBean.getProductName() + " t_center_base inputPrice should not be 0");
                                    continue;
                                }

                                value += baseBean.getValue();
                                baseBeanList.add(baseBean);

                                //生成退货单号时，也同时写入t_center_outback表的description字段中，增加在现有字段后，根据outbackid 到outback表找对应的id
                                OutBackBean outBackBean = this.outBackDAO.find(item.getOutBackId());
                                if (outBackBean!= null){
                                    this.outBackDAO.updateDescription(item.getOutBackId(), outBackBean.getDescription() + "_"+fullId);
                                    //更新out表的transportNo
                                    outBean.setTransportNo(outBackBean.getTransportNo());
                                }
                            }
                        } else{
                            _logger.error("Can not in stock " + outId);
                            this.updateDescription(item, item.getDescription() + "_ERROR_" + "amount exceed");
                            continue;
                        }
                    }

                    if (!ListTools.isEmptyOrNull(baseBeanList)) {
                        outBean.setTotal(value);
                        outBean.setStatus(OutConstant.STATUS_SUBMIT);
                        outBean.setLocation(DepotConstant.CENTER_DEPOT_ID);
                        outBean.setLocationId("999");

                        outBean.setInvoiceId(originalOut.getInvoiceId());
                        outBean.setDutyId("90201008080000000001");
                        outBean.setRefOutFullId(outId);
                        outDAO.saveEntityBean(outBean);
                        baseDAO.saveAllEntityBeans(baseBeanList);
                        this.outBackItemDAO.updateOanoWithOutId(outId, fullId);
                        _logger.info("create out in offlineStorageInJob "+outBean);
                    }
            }
        }
        }
    }

	/**
	 * 找到可退库的baseBean
	 * @param outBaseBeans
	 * @param inBaseBeans  with same productId as  outBaseBeans
	 * @return
	 */
    @Deprecated
	private BaseBean findBaseBeanToBack(List<BaseBean> outBaseBeans, List<BaseBean> inBaseBeans){
		_logger.info("***outBaseBeans "+outBaseBeans.size()+" vs inBaseBeans "+inBaseBeans.size());
        if (ListTools.isEmptyOrNull(inBaseBeans) && !ListTools.isEmptyOrNull(outBaseBeans)){
            //尚未入库
            return outBaseBeans.get(0);
        } else if (outBaseBeans.size() == 1){
            //原base表仅有一行
            return outBaseBeans.get(0);
        } else {
            //减掉已入库
            outBaseBeans.removeAll(inBaseBeans);
            _logger.info("***after remove***"+outBaseBeans.size());
            if (!ListTools.isEmptyOrNull(outBaseBeans)){
                //同一商品按成本价从高到低取
                Collections.sort(outBaseBeans, new Comparator<BaseBean>() {
                    public int compare(BaseBean o1, BaseBean o2) {
                        return (int)(o2.getCostPrice() - o1.getCostPrice());
                    }
                });
                return outBaseBeans.get(0);
            }
            _logger.info("***outBaseBeans size2 ***"+outBaseBeans.size());
        }

		return null;
	}

    /**
     *  refer to OutManagerImpl.splitBase()
     * @param inAmount 待入库数量
     * @param outBaseBeans 出库记录
     * @param inBaseBeans 已入库记录
     * @return
     */
    private List<BaseBean> findBaseBeanToBack(int inAmount, List<BaseBean> outBaseBeans, List<BaseBean> inBaseBeans){
        _logger.info("***outBaseBeans "+outBaseBeans.size()+" vs inBaseBeans "+inBaseBeans.size());
        List<BaseBean> result = new ArrayList<BaseBean>();

        //同一商品按成本价从高到低取
        Collections.sort(outBaseBeans, new Comparator<BaseBean>() {
            public int compare(BaseBean o1, BaseBean o2) {
                return (int)(o2.getCostPrice() - o1.getCostPrice());
            }
        });

//        for (BaseBean out: outBaseBeans){
//            _logger.info("***out base***"+out);
//        }

        Collections.sort(inBaseBeans, new Comparator<BaseBean>() {
            public int compare(BaseBean o1, BaseBean o2) {
                return (int)(o2.getCostPrice() - o1.getCostPrice());
            }
        });

//        for (BaseBean in: inBaseBeans){
//            _logger.info("***in base**"+in);
//        }

//        if (ListTools.isEmptyOrNull(inBaseBeans) && !ListTools.isEmptyOrNull(outBaseBeans)){
//            //尚未入库
//            result.add(outBaseBeans.get(0));
//        }
        if (outBaseBeans.size() == 1){
            //原base表仅有一行
			BaseBean baseBean = outBaseBeans.get(0);
			if (inAmount <= baseBean.getAmount()){
				baseBean.setAmount(inAmount);
				result.add(baseBean);
			} else{
				_logger.error("入库拆分行项目出错,amount exceed");
			}
        } else {
            //TODO 减掉已入库的数量,productId+costPriceKey一致
//            outBaseBeans.removeAll(inBaseBeans);
            this.subtract(outBaseBeans, inBaseBeans);
//            _logger.info("***after remove***"+outBaseBeans);
//            for (BaseBean in: outBaseBeans){
//                _logger.info("***after in base**"+in);
//            }
            //检查是否拆单
//            _logger.info(outBaseBeans+"***outBaseBeans size2 ***"+outBaseBeans.size());
            int amount = inAmount;
            for (BaseBean baseBean: outBaseBeans){
                if (amount <= baseBean.getAmount()){
                    //不需要拆单
                    baseBean.setAmount(amount);
                    result.add(baseBean);
                    break;
                } else{
                    result.add(baseBean);
                }
                amount -= baseBean.getAmount();
            }

            if (amount != 0)
            {
                _logger.error("入库拆分行项目出错");
            }
        }

        return result;
    }

    private void  subtract(List<BaseBean> outBaseBeans, List<BaseBean> inBaseBeans){
        for (BaseBean out: outBaseBeans){
            for (BaseBean in : inBaseBeans){
                if (out.equals(in)){
                    out.setAmount(out.getAmount()-in.getAmount());
                }
            }
        }
    }

    private void updateDescription(OutBackItemBean item, String description){
		_logger.info(item.getId()+" updateDescription***"+item.getDescription()+":"+description);
        if (StringTools.isNullOrNone(item.getDescription()) || !item.getDescription().contains("ERROR")){
			_logger.info(item.getId()+" updateDescription now***"+item.getDescription()+":"+description);
            this.outBackItemDAO.updateDescription(item.getId(),description);
        }
    }

	private void updateDescriptionByOutId(OutBackItemBean item, String description){
		if (StringTools.isNullOrNone(item.getDescription()) || !item.getDescription().contains("ERROR")){
			this.outBackItemDAO.updateDescriptionByOutId(item.getOutId(),description);
		}
	}

    public PlatformTransactionManager getTransactionManager()
	{
		return transactionManager;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager)
	{
		this.transactionManager = transactionManager;
	}

	public CommonDAO getCommonDAO()
	{
		return commonDAO;
	}

	public void setCommonDAO(CommonDAO commonDAO)
	{
		this.commonDAO = commonDAO;
	}

	public OutImportDAO getOutImportDAO()
	{
		return outImportDAO;
	}

	public void setOutImportDAO(OutImportDAO outImportDAO)
	{
		this.outImportDAO = outImportDAO;
	}

	public StorageRelationDAO getStorageRelationDAO()
	{
		return storageRelationDAO;
	}

	public void setStorageRelationDAO(StorageRelationDAO storageRelationDAO)
	{
		this.storageRelationDAO = storageRelationDAO;
	}

	public StorageRelationManager getStorageRelationManager()
	{
		return storageRelationManager;
	}

	public void setStorageRelationManager(
			StorageRelationManager storageRelationManager)
	{
		this.storageRelationManager = storageRelationManager;
	}

	public ProductDAO getProductDAO()
	{
		return productDAO;
	}

	public void setProductDAO(ProductDAO productDAO)
	{
		this.productDAO = productDAO;
	}

	public FlowLogDAO getFlowLogDAO()
	{
		return flowLogDAO;
	}

	public void setFlowLogDAO(FlowLogDAO flowLogDAO)
	{
		this.flowLogDAO = flowLogDAO;
	}

	public LocationDAO getLocationDAO()
	{
		return locationDAO;
	}

	public void setLocationDAO(LocationDAO locationDAO)
	{
		this.locationDAO = locationDAO;
	}

	public StafferDAO getStafferDAO()
	{
		return stafferDAO;
	}

	public void setStafferDAO(StafferDAO stafferDAO)
	{
		this.stafferDAO = stafferDAO;
	}

	public OutDAO getOutDAO()
	{
		return outDAO;
	}

	public void setOutDAO(OutDAO outDAO)
	{
		this.outDAO = outDAO;
	}

	public BaseDAO getBaseDAO()
	{
		return baseDAO;
	}

	public void setBaseDAO(BaseDAO baseDAO)
	{
		this.baseDAO = baseDAO;
	}

	public SailConfigManager getSailConfigManager()
	{
		return sailConfigManager;
	}

	public void setSailConfigManager(SailConfigManager sailConfigManager)
	{
		this.sailConfigManager = sailConfigManager;
	}

	public DistributionDAO getDistributionDAO()
	{
		return distributionDAO;
	}

	public void setDistributionDAO(DistributionDAO distributionDAO)
	{
		this.distributionDAO = distributionDAO;
	}

	public OutImportResultDAO getOutImportResultDAO()
	{
		return outImportResultDAO;
	}

	public void setOutImportResultDAO(OutImportResultDAO outImportResultDAO)
	{
		this.outImportResultDAO = outImportResultDAO;
	}

	public ReplenishmentDAO getReplenishmentDAO()
	{
		return replenishmentDAO;
	}

	public void setReplenishmentDAO(ReplenishmentDAO replenishmentDAO)
	{
		this.replenishmentDAO = replenishmentDAO;
	}

	public ConsignDAO getConsignDAO()
	{
		return consignDAO;
	}

	public void setConsignDAO(ConsignDAO consignDAO)
	{
		this.consignDAO = consignDAO;
	}

	public OutImportLogDAO getOutImportLogDAO()
	{
		return outImportLogDAO;
	}

	public void setOutImportLogDAO(OutImportLogDAO outImportLogDAO)
	{
		this.outImportLogDAO = outImportLogDAO;
	}

	public CustomerMainDAO getCustomerMainDAO() {
		return customerMainDAO;
	}

	public void setCustomerMainDAO(CustomerMainDAO customerMainDAO) {
		this.customerMainDAO = customerMainDAO;
	}

	public StafferVSCustomerDAO getStafferVSCustomerDAO()
	{
		return stafferVSCustomerDAO;
	}

	public void setStafferVSCustomerDAO(StafferVSCustomerDAO stafferVSCustomerDAO)
	{
		this.stafferVSCustomerDAO = stafferVSCustomerDAO;
	}

	public ProvinceDAO getProvinceDAO()
	{
		return provinceDAO;
	}

	public void setProvinceDAO(ProvinceDAO provinceDAO)
	{
		this.provinceDAO = provinceDAO;
	}

	public CityDAO getCityDAO()
	{
		return cityDAO;
	}

	public void setCityDAO(CityDAO cityDAO)
	{
		this.cityDAO = cityDAO;
	}

	public PriceConfigDAO getPriceConfigDAO()
	{
		return priceConfigDAO;
	}

	public void setPriceConfigDAO(PriceConfigDAO priceConfigDAO)
	{
		this.priceConfigDAO = priceConfigDAO;
	}

	public PriceConfigManager getPriceConfigManager()
	{
		return priceConfigManager;
	}

	public void setPriceConfigManager(PriceConfigManager priceConfigManager)
	{
		this.priceConfigManager = priceConfigManager;
	}

	/**
	 * @return the distributionBaseDAO
	 */
	public DistributionBaseDAO getDistributionBaseDAO()
	{
		return distributionBaseDAO;
	}

	/**
	 * @param distributionBaseDAO the distributionBaseDAO to set
	 */
	public void setDistributionBaseDAO(DistributionBaseDAO distributionBaseDAO)
	{
		this.distributionBaseDAO = distributionBaseDAO;
	}

	/**
	 * @return the batchApproveDAO
	 */
	public BatchApproveDAO getBatchApproveDAO()
	{
		return batchApproveDAO;
	}

	/**
	 * @param batchApproveDAO the batchApproveDAO to set
	 */
	public void setBatchApproveDAO(BatchApproveDAO batchApproveDAO)
	{
		this.batchApproveDAO = batchApproveDAO;
	}

	/**
	 * @return the outManager
	 */
	public OutManager getOutManager()
	{
		return outManager;
	}

	/**
	 * @param outManager the outManager to set
	 */
	public void setOutManager(OutManager outManager)
	{
		this.outManager = outManager;
	}

	/**
	 * @return the batchSwatchDAO
	 */
	public BatchSwatchDAO getBatchSwatchDAO()
	{
		return batchSwatchDAO;
	}

	/**
	 * @param batchSwatchDAO the batchSwatchDAO to set
	 */
	public void setBatchSwatchDAO(BatchSwatchDAO batchSwatchDAO)
	{
		this.batchSwatchDAO = batchSwatchDAO;
	}

	/**
	 * @return the addressDAO
	 */
	public AddressDAO getAddressDAO()
	{
		return addressDAO;
	}

	/**
	 * @param addressDAO the addressDAO to set
	 */
	public void setAddressDAO(AddressDAO addressDAO)
	{
		this.addressDAO = addressDAO;
	}

	public ProductVSGiftDAO getProductVSGiftDAO()
	{
		return productVSGiftDAO;
	}

	public void setProductVSGiftDAO(ProductVSGiftDAO productVSGiftDAO)
	{
		this.productVSGiftDAO = productVSGiftDAO;
	}

	public DepotpartDAO getDepotpartDAO()
	{
		return depotpartDAO;
	}

	public void setDepotpartDAO(DepotpartDAO depotpartDAO)
	{
		this.depotpartDAO = depotpartDAO;
	}

	/**
	 * @return the areaDAO
	 */
	public AreaDAO getAreaDAO()
	{
		return areaDAO;
	}

	/**
	 * @param areaDAO the areaDAO to set
	 */
	public void setAreaDAO(AreaDAO areaDAO)
	{
		this.areaDAO = areaDAO;
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
	 * @return the batchReturnLogDAO
	 */
	public BatchReturnLogDAO getBatchReturnLogDAO()
	{
		return batchReturnLogDAO;
	}

	/**
	 * @param batchReturnLogDAO the batchReturnLogDAO to set
	 */
	public void setBatchReturnLogDAO(BatchReturnLogDAO batchReturnLogDAO)
	{
		this.batchReturnLogDAO = batchReturnLogDAO;
	}

	/**
	 * @return the bankSailDAO
	 */
	public BankSailDAO getBankSailDAO()
	{
		return bankSailDAO;
	}

	/**
	 * @param bankSailDAO the bankSailDAO to set
	 */
	public void setBankSailDAO(BankSailDAO bankSailDAO)
	{
		this.bankSailDAO = bankSailDAO;
	}

	/**
	 * @return the estimateProfitDAO
	 */
	public EstimateProfitDAO getEstimateProfitDAO()
	{
		return estimateProfitDAO;
	}

	/**
	 * @param estimateProfitDAO the estimateProfitDAO to set
	 */
	public void setEstimateProfitDAO(EstimateProfitDAO estimateProfitDAO)
	{
		this.estimateProfitDAO = estimateProfitDAO;
	}

    /**
     * @return the packageDAO
     */
    public PackageDAO getPackageDAO() {
        return packageDAO;
    }


    /**
     * @param packageDAO the packageDAO to set
     */
    public void setPackageDAO(PackageDAO packageDAO) {
        this.packageDAO = packageDAO;
    }

    public ImapMailClient getImapMailClient() {
        return imapMailClient;
    }

    public void setImapMailClient(ImapMailClient imapMailClient) {
        this.imapMailClient = imapMailClient;
    }

    public OlOutDAO getOlOutDAO() {
        return olOutDAO;
    }

    public void setOlOutDAO(OlOutDAO olOutDAO) {
        this.olOutDAO = olOutDAO;
    }

    public OlBaseDAO getOlBaseDAO() {
        return olBaseDAO;
    }

    public void setOlBaseDAO(OlBaseDAO olBaseDAO) {
        this.olBaseDAO = olBaseDAO;
    }

    public InvoiceDAO getInvoiceDAO() {
        return invoiceDAO;
    }

    public void setInvoiceDAO(InvoiceDAO invoiceDAO) {
        this.invoiceDAO = invoiceDAO;
    }

    public OutBackItemDAO getOutBackItemDAO() {
        return outBackItemDAO;
    }

    public void setOutBackItemDAO(OutBackItemDAO outBackItemDAO) {
        this.outBackItemDAO = outBackItemDAO;
    }

    public OutBackDAO getOutBackDAO() {
        return outBackDAO;
    }

    public void setOutBackDAO(OutBackDAO outBackDAO) {
        this.outBackDAO = outBackDAO;
    }
}
