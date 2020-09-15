package com.zblog.admin.filter.dubbo;


import com.zblog.util.sharedb.DynamicDataSourceContextHolder;
import com.zblog.util.tool.TokenUtil;
import org.apache.dubbo.rpc.*;

/**
 * dubbo  filter
 *
 * 作用： 用于网关请求server时，网关filter中解析的用户所属库信息带过去
 */
public class DubboRequestFilter implements Filter {

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		if (DynamicDataSourceContextHolder.getDataSourceKey() != null) {
			RpcContext.getContext().setAttachment(TokenUtil.utoken,
					DynamicDataSourceContextHolder.getDataSourceKey());
		}
		Result result = invoker.invoke(invocation);
		return result;
	}
}
