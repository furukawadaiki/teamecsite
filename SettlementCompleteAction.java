package com.internousdev.gerbera.action;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.gerbera.dao.CartInfoDAO;
import com.internousdev.gerbera.dao.PurchaseHistoryInfoDAO;
import com.internousdev.gerbera.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class SettlementCompleteAction extends ActionSupport implements SessionAware {

	private String destinationId;
	private Map<String,Object> session;

	public String execute(){

		if(!session.containsKey("mCategoryList")){
			return "sessionTimeOut";
		}

		String result=ERROR;

		CartInfoDAO cartInfoDAO = new CartInfoDAO();

		List<CartInfoDTO> cartInfoDtoList = new ArrayList<CartInfoDTO>();
		cartInfoDtoList = cartInfoDAO.getCartInfo(String.valueOf(session.get("loginId")));


		PurchaseHistoryInfoDAO purchaseHistoryInfoDAO = new PurchaseHistoryInfoDAO();
		int count=0;

		for(int i=0; i<cartInfoDtoList.size();i++){
			count += purchaseHistoryInfoDAO.regist(
					String.valueOf(session.get("loginId")),
					cartInfoDtoList.get(i).getProductId(),
					cartInfoDtoList.get(i).getProductCount(),
					Integer.parseInt(destinationId),
					(cartInfoDtoList.get(i).getPrice())*(cartInfoDtoList.get(i).getProductCount())
					);
		}

		if(count > 0){
			count = cartInfoDAO.deleteAll(String.valueOf(session.get("loginId")));
			result = SUCCESS;
		}
		return result;
	}
	public String getDestinationId(){
		return destinationId;
	}
	public void setDestinationId(String destinationId){
		this.destinationId = destinationId;
	}
	public Map<String,Object> getSession(){
		return session;
	}
	public void setSession(Map<String,Object> session){
		this.session = session;
	}
}
