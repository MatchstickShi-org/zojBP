<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	<div data-options="region:'center'" style="width: 470px;">
		<table id="infoerDatagrid" border="false"></table>
		<div id="infoerDatagridToolbar">
			<label style="vertical-align: middle;">名称：</label>
			<input class="easyui-textbox" id="infoerMgr.nameInput"/>
			<label style="vertical-align: middle;">电话：</label>
			<input class="easyui-textbox" id="infoerMgr.telInput"/>
			<label style="vertical-align: middle;">信息员等级：</label>
			<input type="checkbox" value="0" name="levelInput" checked="checked"/>全部
			<input type="checkbox" value="1" name="levelInput"/>金牌
			<input type="checkbox" value="2" name="levelInput"/>银牌
			<input type="checkbox" value="3" name="levelInput"/>铜牌
			<input type="checkbox" value="4" name="levelInput"/>铁牌
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" id="queryInfoerBtn">查询</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="showAddInfoerWindowBtn">新增</a>
		</div>
	</div>
	<div data-options="region:'south', split:true, border: true" style="height: 270px;">
		<div id="infoerMgrTab">
			<div title="详情" border="false" style="padding: 2px;">
				<form id="editInfoerForm" action="marketing/infoerMgr/editInfoer" method="post" style="width: 100%;">
					<input type="hidden" name="id">
					<table style="width: 100%; min-width: 700px;">
						<tr>
							<td align="right" style="min-width: 80px;"><label>名称：</label></td>
							<td style="min-width: 250px;"><input name="name" class="easyui-textbox" required="required"/></td>
							<td align="right"><label>工作单位：</label></td>
							<td style="min-width: 200px;"><input name="org" style="min-width: 250px;" class="easyui-textbox" required="required"/></td>
							<td align="right" style="vertical-align: mid; min-width: 80px;"><label>地址：</label></td>
							<td style="min-width: 200px;"><input name="address" style="min-width: 250px;" class="easyui-textbox" required="required"/></td>
						</tr>
						<tr>
							<td align="right" style="min-width: 80px;"><label>联系电话：</label></td>
							<td style="min-width: 250px;"><input name="telAll" style="min-width: 250px;" readonly="readonly" class="easyui-textbox" required="required"/></td>
							<td align="right" ><label>业务员：</label></td>
							<td style="min-width: 200px;"><input name="salesmanId" type="hidden"/><input name="salesmanName" readonly="readonly" style="min-width: 250px;" class="easyui-textbox" required="required"/></td>
							<td align="right" style="vertical-align: mid; min-width: 80px;"><label>性质：</label></td>
							<td style="min-width: 200px;">
								<label><input type="radio" name="nature" value="1" checked="checked">中介
								<input type="radio" name="nature" value="2" >售楼</label>
							</td>
						</tr>
						<tr>
							<td align="right" style="vertical-align: top; min-width: 80px;"><label>信息员等级：</label></td>
							<td style="min-width: 200px;"><input name="level" type="hidden"/><input name="levelDesc" readonly="readonly" style="min-width: 250px;" class="easyui-textbox"/></td>
						</tr>
						<tr>
							<td align="center" colspan="6">
								<a id="submitUpdateInfoerFormBtn" href="javascript:void(0)" style="width: 60px;">保存</a>
								<a id="refreshUpdateUserFormBtn" href="javascript:void(0)" style="width: 60px;">刷新</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div title="回访记录" border="false" style="padding: 2px;">
				<table style="height: 100%; width: 100%;">
					<tr>
						<td>
				    		<table id="infoerVisitGrid" title="回访记录"></table>
				    		<div id="infoerVisitGridToolbar">
								<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="addInfoerVisitBtn">新增</a>
							</div>
						</td>
					</tr>
				</table>
			</div>
			<div title="在谈单" border="false" style="padding: 2px;">
				<table style="height: 100%; width: 100%;">
					<tr>
						<td>
				    		<table id="orderGrid" title="在谈单"></table>
						</td>
					</tr>
				</table>
			</div>
			<div title="信息费" border="false" style="padding: 2px;">
				<table style="height: 100%; width: 100%;">
					<tr>
						<td>
				    		<table id="infoCostGrid" title="信息费"></table>
						</td>
					</tr>
				</table>
			</div>
			<div title="提成" border="false" style="padding: 2px;">
				<table style="height: 100%; width: 100%;">
					<tr>
						<td>
				    		<table id="commissionCostGrid" title="提成"></table>
						</td>
					</tr>
				</table>
			</div>
			<div title="客户" border="false" style="padding: 2px;">
				<table style="height: 100%; width: 100%;">
					<tr>
						<td>
				    		<table id="clientGrid" title="客户"></table>
				    		<div id="clientGridToolbar">
								<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="addClientBtn">新增</a>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>
<div id="addInfoerWindow"></div>
<div id="addInfoerVisitWindow"></div>
<div id="addClientWindow"></div>
<script type="text/javascript" src="pages/marketing/infoerMgr/index.js"></script>
</body>
</html>