package online.pangge.wechat.damain.message.req;

/**
 * 图片消息
 *
 * @author Jimmy
 */
public class ImageMessage extends BaseMessage {
	// 图片链接
	private String PicUrl;

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
}
