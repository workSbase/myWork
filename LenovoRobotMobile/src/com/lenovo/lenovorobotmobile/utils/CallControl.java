//package com.lenovo.lenovorobotmobile.utils;
//
//import java.net.URI;
//
//import com.lenovo.vcs.weaver.enginesdk.a.api.WeaverConstants;
//import com.lenovo.vcs.weaver.enginesdk.a.api.WeaverService;
//import com.lenovo.vcs.weaver.enginesdk.a.model.WeaverRequest;
//import com.lenovo.vcs.weaver.enginesdk.b.logic.sip.SipConstants;
//import com.lenovo.vcs.weaver.enginesdk.common.StringUtility;
//
//public class CallControl {
//
//	//结束通话
//	public static void endCall(){
//		URI uri = StringUtility.generateUri(WeaverConstants.WEAVER_SCHEME,
//				SipConstants.LOGIC_HOST, SipConstants.LogicPath.END_CALL);
//		WeaverRequest req = new WeaverRequest(uri, null);
//		req.addParameter(SipConstants.LogicParam.CALL_ID, -1);
//		WeaverService.getInstance().dispatchRequest(req);
//	}
// }
