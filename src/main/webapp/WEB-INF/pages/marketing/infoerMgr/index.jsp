<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
<div class="easyui-layout" data-options="fit:true" style="margin: 2px;">
	<div data-options="region:'center'">
		<table id="infoerDatagrid" border="false"></table>
		<div id="infoerDatagridToolbar">
			<label>名称：</label>
			<input style="width: 120px;" class="easyui-textbox" id="infoerMgr-nameInput"/>
			<label>电话：</label>
			<input style="width: 120px;" class="easyui-textbox" id="infoerMgr-telInput"/>
			<c:if test="${sessionScope.loginUser.role != 1}">
				<label>下属业务员：</label>
				<select style="width: 120px;" class="easyui-combobox" id="infoerMgr-salesmanCombobox" data-options="textField: 'alias'">
					<option value="" style="color: gray;">-- 全部 --</option>
					<c:forEach items="${requestScope.underling}" var="underling">
						<option value="${underling.id}">${underling.alias}</option>
					</c:forEach>
				</select>
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" id="queryInfoerBtn">查询</a>
				<br>
				<label><input type="radio" value="0" name="infoerMgr-infoerFilterInput" />全部信息员</label>
				<label><input type="radio" value="1" name="infoerMgr-infoerFilterInput" checked="checked"/>我的信息员</label>
			</c:if>
			<label><input type="checkbox" value="1" name="infoerMgr-isWait" />待联系信息员</label>
			<label> 信息员等级：</label>
			<label><input type="checkbox" value="" name="levelInput" checked="checked"/>全部</label>
			<label><input type="checkbox" value="1" name="levelInput"/>金牌</label>
			<label><input type="checkbox" value="2" name="levelInput"/>银牌</label>
			<label><input type="checkbox" value="3" name="levelInput"/>铜牌</label>
			<label><input type="checkbox" value="4" name="levelInput"/>铁牌</label>
			<c:if test="${sessionScope.loginUser.role == 1}">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" id="queryInfoerBtn">查询</a>
			</c:if>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="showAddInfoerWindowBtn">新增</a>
			<c:if test="${sessionScope.loginUser.role == 3}">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" id="showBusinessTransferWindowBtn">业务转移</a>
			</c:if>
		</div>
	</div>
	<div data-options="region:'south', split:true, border: true" style="height: 270px;">
		<div id="infoerMgrTab" data-options="tools:'#infoerMgrTab-tools'">
			<div title="详情" border="false">
				<form id="editInfoerForm" action="marketing/infoerMgr/editInfoer" method="post" style="width: 100%;">
					<input type="hidden" name="id">
					<input name="level" type="hidden"/>
					<input name="salesmanId" type="hidden"/>
					<table style="width: 100%; min-width: 500px;">
						<tr>
							<td align="right" style="min-width: 80px;"><label>名称：</label></td>
							<td style="min-width: 200px;"><input name="name" class="easyui-textbox" required="required" style="width: 154px;"/></td>
							<td align="right" style="min-width: 80px;"><label>信息员等级：</label></td>
							<td width="70%"><input name="levelDesc" readonly="readonly" class="easyui-textbox" style="width: 154px;"/></td>
						</tr>
						<tr>
							<td align="right" ><label>业务员：</label></td>
							<td><input name="salesmanName" readonly="readonly" class="easyui-textbox" required="required" style="width: 154px;"/></td>
							<td align="right" ><label>联系电话1：</label></td>
							<td><input name="tel1" id="editInfoerForm-tel1" readonly="readonly" class="easyui-textbox" required="required" style="width: 154px;"/></td>
						</tr>
						<tr>
							<td align="right" ><label>联系电话2：</label></td>
							<td><input name="tel2" id="editInfoerForm-tel2" class="easyui-textbox" style="width: 154px;"/></td>
							<td align="right" ><label>联系电话3：</label></td>
							<td><input name="tel3" id="editInfoerForm-tel3" class="easyui-textbox" style="width: 154px;"/></td>
						</tr>
						<tr>
							<td align="right" ><label>联系电话4：</label></td>
							<td><input name="tel4" id="editInfoerForm-tel4" class="easyui-textbox" style="width: 154px;"/></td>
							<td align="right" ><label>联系电话5：</label></td>
							<td><input name="tel5" id="editInfoerForm-tel5" class="easyui-textbox" style="width: 154px;"/></td>
						</tr>
						<tr>
							<td colspan="4" style="vertical-align: mid;">&nbsp;&nbsp;<font id="editInfoerForm-errortel" color="red"></font></td>
						</tr>
						<tr>
							<td align="right" style="vertical-align: mid;"><label>性质：</label></td>
							<td colspan="3">
								<label><input type="radio" name="nature" value="1" checked="checked">售楼</label>
								<label><input type="radio" name="nature" value="2">商业中介</label>
								<label><input type="radio" name="nature" value="3">二手房中介</label>
								<label><input type="radio" name="nature" value="4">招商</label>
								<label><input type="radio" name="nature" value="5">物业</label>
								&nbsp;&nbsp;<label>待联系信息员：</label><label><input type="checkbox" value="1" name="isWait" />是</label>
							</td>
						</tr>
						<tr>
							<td align="right"><label>工作单位：</label></td>
							<td colspan="3"><input name="org" style="width: 442px;" class="easyui-textbox" required="required"/></td>
						</tr>
						<tr>
							<td align="right"><label>单位地址：</label></td>
							<td colspan="3"><input name="address" style="width: 442px;" class="easyui-textbox" required="required"/></td>
						</tr>
						<tr>
							<td align="center" colspan="3">
								<a id="submitUpdateInfoerFormBtn" href="javascript:void(0)" iconCls="icon-save" style="width: 60px;">保存</a>
								<a id="refreshUpdateUserFormBtn" href="javascript:void(0)" iconCls="icon-reload" style="width: 60px;">刷新</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div title="回访记录" selected="true">
	    		<table id="infoerVisitGrid" border="false"></table>
			</div>
			<div title="在谈单">
	    		<table id="orderGrid" border="false"></table>
			</div>
			<div title="信息费">
	    		<table id="infoCostGrid" border="false"></table>
			</div>
			<div title="提成">
	    		<table id="commissionCostGrid" border="false"></table>
			</div>
			<div title="客户">
	    		<table id="clientGrid" border="false"></table>
			</div>
		</div>
		<div id="infoerMgrTab-tools" style="border-top: 0px;">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="addClientBtn">新增客户</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="addInfoerVisitBtn">新增信息员回访记录</a>
		</div>
	</div>
</div>
<div id="addInfoerWindow"></div>
<div id="businessTransferWindow"></div>
<div id="addInfoerVisitWindow"></div>
<div id="addClientWindow"></div>
<script type="text/javascript">
var _session_loginUserId = ${sessionScope.loginUser.id};
var _session_loginUserRole = ${sessionScope.loginUser.role};
</script>
<script type="text/javascript" src="pages/marketing/infoerMgr/index.js"></script>
</body>
</html>