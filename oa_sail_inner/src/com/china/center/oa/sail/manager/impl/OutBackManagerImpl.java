package com.china.center.oa.sail.manager.impl;

import java.util.List;

import com.china.center.oa.publics.bean.FlowLogBean;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.dao.FlowLogDAO;
import com.china.center.oa.sail.constanst.SailConstant;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.publics.bean.AttachmentBean;
import com.china.center.oa.publics.dao.AttachmentDAO;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.sail.bean.OutBackBean;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.dao.OutBackDAO;
import com.china.center.oa.sail.manager.OutBackManager;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.TimeTools;

public class OutBackManagerImpl implements OutBackManager
{
	//private final Log operationLog = LogFactory.getLog("opr");
	
	private OutBackDAO outBackDAO = null;
	
	private CommonDAO commonDAO = null;
	
	private AttachmentDAO attachmentDAO = null;

	private FlowLogDAO flowLogDAO = null;

	public OutBackManagerImpl()
	{
	}

	@Transactional(rollbackFor = MYException.class)
	public boolean addOutBack(User user, OutBackBean bean) throws MYException
	{
		JudgeTools.judgeParameterIsNull(user, bean);
		
		bean.setId(commonDAO.getSquenceString20());
		
		outBackDAO.saveEntityBean(bean);
		
		return true;
	}

	@Transactional(rollbackFor = MYException.class)
	public boolean updateOutBack(User user, OutBackBean bean)
			throws MYException
	{
		JudgeTools.judgeParameterIsNull(user, bean, bean.getId());
		
		String id = bean.getId();
		
		OutBackBean oldBean = outBackDAO.find(id);
		
		if (null == oldBean)
		{
			throw new MYException("数据异常,请重新操作");
		}
		
		if (oldBean.getStatus() != OutConstant.OUTBACK_STATUS_CLAIM)
		{
			throw new MYException("已认领状态，不能修改");
		}
		
		outBackDAO.updateEntityBean(oldBean);
		
		return true;
	}

	@Transactional(rollbackFor = MYException.class)
	public boolean deleteOutBack(User user, String id) throws MYException
	{
		JudgeTools.judgeParameterIsNull(user, id);
		
		OutBackBean oldBean = outBackDAO.find(id);
		
		if (null == oldBean)
		{
			throw new MYException("数据异常,请重新操作");
		}
		
		if (oldBean.getStatus() != OutConstant.OUTBACK_STATUS_CLAIM)
		{
			throw new MYException("非待认领状态，不能删除");
		}
		
		outBackDAO.deleteEntityBean(id);
		
		return true;
	}

	@Transactional(rollbackFor = MYException.class)
	public boolean unclaimOutBack(User user, String id) throws MYException
	{
		JudgeTools.judgeParameterIsNull(user, id);
		
		OutBackBean old = outBackDAO.find(id);
		
		if (null == old)
		{
			throw new MYException("数据异常");
		}
		
		if (old.getStatus() != OutConstant.OUTBACK_STATUS_CHECK)
		{
			throw new MYException("已不是待验货状态,不能退领");
		}
		
		if (!old.getClaimer().equals(user.getStafferName()))
		{
			throw new MYException("只能退领自己认领的");
		}
		
		old.setClaimer("");
		old.setClaimTime("");
		old.setStatus(OutConstant.OUTBACK_STATUS_CLAIM);
		
		outBackDAO.updateEntityBean(old);
		
		attachmentDAO.deleteEntityBeansByFK(id);
		
		return true;
	}

	@Transactional(rollbackFor = MYException.class)
	public boolean claimOutBack(User user, OutBackBean bean) throws MYException
	{
        JudgeTools.judgeParameterIsNull(user, bean, bean.getId());
		
		OutBackBean old = outBackDAO.find(bean.getId());
		
		if (null == old)
		{
			throw new MYException("数据异常,请重新操作");
		}
		
		if (old.getStatus() != OutConstant.OUTBACK_STATUS_CLAIM)
		{
			throw new MYException("不是待认领状态,请确认");
		}
		
		old.setClaimer(bean.getClaimer());
		old.setClaimTime(bean.getClaimTime());
		old.setStatus(bean.getStatus());
		
//		List<AttachmentBean> attachmentList = bean.getAttachmentList();
//
//        for (AttachmentBean attachmentBean : attachmentList) {
//            attachmentBean.setId(commonDAO.getSquenceString20());
//            attachmentBean.setRefId(bean.getId());
//        }
//
//        attachmentDAO.saveAllEntityBeans(attachmentList);
        
        outBackDAO.updateEntityBean(old);

		this.log(user, old.getId(), OutConstant.OUTBACK_STATUS_CLAIM, bean.getStatus());
		
		return true;
	}

	@Transactional(rollbackFor = MYException.class)
	public boolean checkOutBack(User user, OutBackBean bean, String reason)
			throws MYException
	{
        String id = bean.getId();
		JudgeTools.judgeParameterIsNull(user, id);
		
		OutBackBean old = outBackDAO.find(id);
		
		if (null == old)
		{
			throw new MYException("数据异常");
		}
		
		if (old.getStatus() != OutConstant.OUTBACK_STATUS_CHECK)
		{
			throw new MYException("已不是待验货状态,不能退领");
		}
		
		old.setChecker(user.getStafferName());
		old.setCheckTime(TimeTools.now());
		old.setStatus(OutConstant.OUTBACK_STATUS_CHECK_HANDOVER);
		old.setCheckReason(reason);

//        List<AttachmentBean> attachmentList = bean.getAttachmentList();
//
//        for (AttachmentBean attachmentBean : attachmentList) {
//            attachmentBean.setId(commonDAO.getSquenceString20());
//            attachmentBean.setRefId(bean.getId());
//        }
//
//        attachmentDAO.saveAllEntityBeans(attachmentList);
		
		outBackDAO.updateEntityBean(old);

		this.log(user, old.getId(), OutConstant.OUTBACK_STATUS_CHECK, OutConstant.OUTBACK_STATUS_CHECK_HANDOVER);
		
		return true;
	}

    @Override
    @Transactional(rollbackFor = MYException.class)
    public boolean checkAndHandOverBack(User user, String id, String reason) throws MYException {
        JudgeTools.judgeParameterIsNull(user, id);

        OutBackBean old = outBackDAO.find(id);

        if (null == old)
        {
            throw new MYException("数据异常");
        }

        if (old.getStatus() != OutConstant.OUTBACK_STATUS_CHECK_HANDOVER)
        {
            throw new MYException("已不是待验货交接状态,不能退领");
        }

        old.setHandoverChecker(user.getStafferName());
		old.setHandoverCheckTime(TimeTools.now());
        old.setStatus(OutConstant.OUTBACK_STATUS_CONFIRM);
        old.setHandoverReason(reason);

        outBackDAO.updateEntityBean(old);

		this.log(user, old.getId(), OutConstant.OUTBACK_STATUS_CHECK_HANDOVER, OutConstant.OUTBACK_STATUS_CONFIRM);

        return true;
    }

    @Override
    @Transactional(rollbackFor = MYException.class)
    public boolean confirmOutBack(User user, String id, String note) throws MYException {
        JudgeTools.judgeParameterIsNull(user, id);

        OutBackBean old = outBackDAO.find(id);

        if (null == old)
        {
            throw new MYException("数据异常");
        }

        if (old.getStatus() != OutConstant.OUTBACK_STATUS_CONFIRM)
        {
            throw new MYException("已不是待商务确认状态,不能退领");
        }

        old.setConfirmChecker(user.getStafferName());
		old.setConfirmCheckTime(TimeTools.now());
        old.setStatus(OutConstant.OUTBACK_STATUS_IN);
        old.setNote(note);

        outBackDAO.updateEntityBean(old);

		this.log(user, old.getId(), OutConstant.OUTBACK_STATUS_CONFIRM, OutConstant.OUTBACK_STATUS_IN);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
	public boolean finishOutBack(User user, String id) throws MYException
	{
		JudgeTools.judgeParameterIsNull(user, id);
		
		OutBackBean old = outBackDAO.find(id);
		
		if (null == old)
		{
			throw new MYException("数据异常");
		}
		
		if (old.getStatus() != OutConstant.OUTBACK_STATUS_IN)
		{
			throw new MYException("已不是待入库状态,不能结束入库");
		}
		
		old.setStocker(user.getStafferName());
		old.setStockTime(TimeTools.now());
		old.setStatus(OutConstant.OUTBACK_STATUS_FINISH);
		
		outBackDAO.updateEntityBean(old);
		
		return true;
	}

	private void log(User user, String id, int preStatus, int afterStatus){
		FlowLogBean log = new FlowLogBean();

		log.setActor(user.getStafferName());

		log.setDescription("通过");
		log.setFullId(id);
		log.setOprMode(PublicConstant.OPRMODE_PASS);
		log.setLogTime(TimeTools.now());

		log.setPreStatus(preStatus);

		log.setAfterStatus(afterStatus);

		flowLogDAO.saveEntityBean(log);
	}

	/**
	 * @return the outBackDAO
	 */
	public OutBackDAO getOutBackDAO()
	{
		return outBackDAO;
	}

	/**
	 * @param outBackDAO the outBackDAO to set
	 */
	public void setOutBackDAO(OutBackDAO outBackDAO)
	{
		this.outBackDAO = outBackDAO;
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
	 * @return the attachmentDAO
	 */
	public AttachmentDAO getAttachmentDAO()
	{
		return attachmentDAO;
	}

	/**
	 * @param attachmentDAO the attachmentDAO to set
	 */
	public void setAttachmentDAO(AttachmentDAO attachmentDAO)
	{
		this.attachmentDAO = attachmentDAO;
	}

	public FlowLogDAO getFlowLogDAO() {
		return flowLogDAO;
	}

	public void setFlowLogDAO(FlowLogDAO flowLogDAO) {
		this.flowLogDAO = flowLogDAO;
	}
}
