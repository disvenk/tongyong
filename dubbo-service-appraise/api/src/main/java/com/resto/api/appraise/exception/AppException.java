package com.resto.api.appraise.exception;

public class AppException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ErrorMsg msg=new ErrorMsg(0, "");
	public static final ErrorMsg CUSTOMER_NOT_EXISTS= new ErrorMsg(1,"用户不存在！");
	public static final ErrorMsg NOT_BIND_PHONE = new ErrorMsg(2,"未绑定手机");
	public static final ErrorMsg ORDER_ITEMS_EMPTY = new ErrorMsg(3,"没有订单项");
	public static final ErrorMsg COUPON_IS_USED = new ErrorMsg(4,"优惠券已使用");
	public static final ErrorMsg COUPON_MODE_ERR = new ErrorMsg(5,"优惠券使用模式错误");
	public static final ErrorMsg COUPON_MIN_AMOUNT_ERR = new ErrorMsg(6,"订单金额不足以使用优惠券");
	public static final ErrorMsg COUPON_NOT_USEACCOUNT = new ErrorMsg(7,"不可以和余额一起使用");
	public static final ErrorMsg COUPON_IS_EXPIRE = new ErrorMsg(8,"优惠券已过期");
	public static final ErrorMsg COUPON_TIME_ERR = new ErrorMsg(9,"优惠券使用时间段错误");
	public static final ErrorMsg UNSUPPORT_ITEM_TYPE = new ErrorMsg(10,"不支持的餐品类型！");
	public static final ErrorMsg ORDER_STATE_ERR = new ErrorMsg(11, "订单状态异常");
	public static final ErrorMsg ORDER_MODE_CHECK = new ErrorMsg(12, "");
	public static final ErrorMsg ORDER_NOT_ALL_APPRAISE = new ErrorMsg(13, "该订单不允许评论");
	public static final ErrorMsg PHONE_IS_BIND = new ErrorMsg(14, "该手机号已被绑定！");
	public static final ErrorMsg ORDER_IS_CLOSED = new ErrorMsg(15, "订单已经被取消!");
	public static final ErrorMsg ORDER_IS_PRINTED = new ErrorMsg(16, "该订单已经打印过了！");
	public static final ErrorMsg ORDER_IS_NULL = new ErrorMsg(17, "该订单不存在或无商品信息！");
    public static final ErrorMsg COUPON_IS_SHOP = new ErrorMsg(18, "不是该店铺的优惠券！");
	public static final ErrorMsg DISCOUNT_TIMEOUT = new ErrorMsg(19, "菜品供应时间变动，价格需重新计算！");
	public static final ErrorMsg TABLE_USED = new ErrorMsg(20,"不好意思，这桌有人了");
	
	
	public AppException(ErrorMsg msg) {
		this.msg=msg;
	}
	
	public AppException(ErrorMsg msg, String string) {
		this.msg = new ErrorMsg(msg.getCode(), string);
	}

	@Override
	public String getMessage() {
		return this.msg.toString();
	}
	
	
	static class ErrorMsg{
		private final int code;
		private final String msg;
		ErrorMsg(int code, String msg){
			this.code=code;
			this.msg=msg;
		}
		public int getCode() {
			return code;
		}
		public String getMsg() {
			return msg;
		}
		@Override
		public String toString() {
			return "{code:"+code+",msg:\""+msg+"\"}";
		}
	}
}
