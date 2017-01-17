$(function()
{
	var $orderDatagrid = $('table#orderDatagrid');
	var $setTracingClientBtn = $('a#setTracingClientBtn');
	var $infoerNameTextbox = $('#clientTrace\\.infoerNameInput');
	var $orderNameTextbox = $('#clientTrace\\.nameInput');
	var $telTextbox = $('#clientTrace\\.telInput');
	var $statusCheckbox = $('[name="clientTrace.status"][checked]');
	var $queryOrderBtn = $('a#queryOrderBtn');
	var $addClientVisitWindow = $('div#addClientVisitWindow');
	var $businessTransferWindow = $('div#businessTransferWindow');
	var $addClientWindow = $('div#addClientWindow');
	var $applyOrderWindow = $('div#applyOrderWindow');
	var $selectInfoerWindow = $('div#selectInfoerWindow');
	var $clientMgrTab = $('div#clientMgrTab');
	var $editClientForm = $('form#editOrderForm');
	var $submitUpdateClientFormBtn = $('a#submitUpdateClientFormBtn');
	var $refreshUpdateClientFormBtn = $('a#refreshUpdateClientFormBtn');
	var $orderVisitGrid = $('table#orderVisitGrid');
	var $orderApproveGrid = $('table#orderApproveGrid');
	
	showSelectInfoerWindow = function()
	{
		$selectInfoerWindow.window('clear');
		$selectInfoerWindow.window('open').window
		({
			title: '请选择信息员',
		}).window('open').window('refresh', 'marketing/clientMgr/showSelectInfoerWindow');
	}
	
	function init()
	{
		$orderDatagrid.datagrid
		({
			idField: 'id',
			toolbar: '#orderDatagridToolbar',
			columns:
			[[
				{field:'id', hidden: true},
				{field:'isKey', hidden: true},
				{field: 'ck', checkbox: true},
				{field:'name', title:'名称', width: 3},
				{field:'telAll', title:'联系电话', width: 5},
				{field:'orgAddr', title:'单位地址', width: 8},
				{field:'projectName', title:'工程名称', width: 8},
				{field:'projectAddr', title:'面积', width: 8},
				{field:'infoerName', title:'信息员', width: 3},
				{field:'salesmanName', title:'业务员', width: 3},
				{field:'salesmanStatus', hidden: true},
				{
					field:'status', title:'状态', width: 3, formatter: function(value, row, index)
					{
						switch (value)
						{
							case 10:
								return '正跟踪';
								break;
							case 12:
								return '已放弃';
								break;
							case 30:
							case 32:
								return '审核中';
								break;
							case 14:
								return '已打回';
								break;
							default:
								return '未知';
								break;
						}
					}
				},
				{field:'insertTime', title:'录入日期', width: 5},
				{
					field:'notVisitDays', title:'未回访天数', width: 3, sortable: true,
					styler: function (value, row, index)
					{
						if(value > 5)
							return 'background-color: orange;';
		           }
				}
			]],
			border: false,
			pagination: true,
			singleSelect: true,
			selectOnCheck: false,
			checkOnSelect: false,
			rowStyler: function(index, row)
			{
				if(row.isKey == 1)
					return 'color: red;';
			},
			url: 'marketing/clientMgr/getAllClientTrace',
			onSelect: function(idx, row)
			{
				if(row.salesmanId == _session_loginUserId || _session_loginUserRole == -1)
				{
					$('#addOrderVisitBtn').linkbutton('enable');
					$clientMgrTab.tabs('showTool');
					$submitUpdateClientFormBtn.linkbutton('enable');
					$('#removeOrderBtn').linkbutton('enable');
					$('#applyOrderBtn').linkbutton('enable');
				}
				else
				{
					$('#addOrderVisitBtn').linkbutton('disable');
					$clientMgrTab.tabs('hideTool');
					$submitUpdateClientFormBtn.linkbutton('disable');
					$('#removeOrderBtn').linkbutton('disable');
					$('#applyOrderBtn').linkbutton('disable');
				}
				
				if(_session_loginUserRole == -1 || _session_loginUserRole == 3)
				{
					if(row.status == 12)	//已放弃
						$setTracingClientBtn.linkbutton('enable');
					else
						$setTracingClientBtn.linkbutton('disable');
				}
				loadTabData($clientMgrTab.tabs('getSelected').panel('options').title, row);
			},
		});
		
		$('#orderDatagridToolbar :checkbox').click(function()
		{
			if($(this).attr('value') == '')		//全选
				$('#orderDatagridToolbar :checkbox[value!=""]').attr("checked", false);
			else
				$('#orderDatagridToolbar :checkbox[value=""]').attr("checked", false);
		});
		
		$queryOrderBtn.linkbutton
		({
			'onClick': function()
			{
				var status =[];
				$('#orderDatagridToolbar :input[name="statusInput"]:checked').each(function(){ 
					status.push($(this).val());
				});
				$orderDatagrid.datagrid('load', 
				{
					name: $orderNameTextbox.textbox('getValue'),
					tel: $telTextbox.textbox('getValue'),
					infoerName: $infoerNameTextbox.textbox('getValue'),
					filter: $(':radio[name="clientTrace-infoerFilterInput"]:checked').val(),
					isKey: $(':checkbox[name="clientTrace-isKey"]:checked').val(),
					status: status
				});
			}
		});
		
		$setTracingClientBtn.linkbutton({onClick: function()
		{
			var orderIds = $orderDatagrid.datagrid('getSelectRowPkValues');
			if(orderIds.length == 0)
			{
				$.messager.alert('提示', '请选择要设为正跟踪的<span style="color: red;">已放弃</span>客户。');
				return;
			}
			$.messager.confirm('警告','确定要设置选择客户为正跟踪客户吗？',function(r)
			{
				if (!r)
					return;
				$.ajax
				({
					url: 'marketing/clientMgr/setOrder2Tracing',
					data: {orderId: orderIds[0]},
					success: function(data, textStatus, jqXHR)
					{
						if(data.returnCode == 0)
							$orderDatagrid.datagrid('reload');
					}
				});
			});
		}});
		
		$submitUpdateClientFormBtn.linkbutton({'onClick': submitEditClientForm});
		$refreshUpdateClientFormBtn.linkbutton({'onClick': function()
		{
			var selRows = $orderDatagrid.datagrid('getSelections');
			if(selRows.length == 1)
				loadTabData($clientMgrTab.tabs('getSelected').panel('options').title, selRows[0]);
			else
				$.messager.alert('提示', '请选中一个客户。');
		}});
		
		$orderVisitGrid.datagrid
		({
			idField: 'id',
			columns:
			[[
				{field:'id', hidden: true},
				{field:'content', title:'回访内容', width: 5},
				{field:'date', title:'回访日期', width: 5}
			]],
			pagination: true,
			onDblClickRow: function(index, row){
				$.messager.alert('回访内容', row.content);
			}
		});

		$orderVisitGrid.datagrid('options').url = 'marketing/clientMgr/getOrderVisitByOrder';
		
		$orderApproveGrid.datagrid
		({
			idField: 'id',
			columns:
				[[
				  {field:'id', hidden: true},
				  {field:'claimerName', title:'申请人', width: 2},
				  {field:'approverName', title:'审核人', width: 2},
				  {
						field:'operate', title:'操作', width: 1, formatter: function(value, row, index)
						{
							switch (value)
							{
								case 0:
									return '驳回';
									break;
								case 1:
									return '批准';
									break;
								case 2:
									return '申请';
									break;
								case 3:
									return '打回';
									break;
								default:
									return '无操作';
									break;
							}
						}
					},
				  {
						field:'status', title:'状态', width: 4, formatter: function(value, row, index)
						{
							switch (value)
							{
								case 10:
									return '正跟踪';
									break;
								case 12:
									return '已放弃';
									break;
								case 14:
									return '在谈单-设计师已打回';
									break;
								case 30:
									return '在谈单-商务部经理审核中';
									break;
								case 32:
									return '在谈单-主案部经理审核中';
									break;
								case 34:
									return '在谈单-设计师跟踪中';
									break;
								case 90:
									return '已签单';
									break;
								case 0:
									return '死单';
									break;
								case 60:
									return '不准单-主案部经理审核中';
									break;
								case 62:
									return '不准单-商务部经理审核中';
									break;
								case 64:
									return '不准单';
									break;
								default:
									return '无状态';
									break;
							}
						}
					},
			  {field:'remark', title:'备注', width: 6},
			  {field:'operateTime', title:'操作日期', width: 5}
			  ]],
			  pagination: true
		});
		
		$orderApproveGrid.datagrid('options').url = 'marketing/clientMgr/getOrderApproveByOrderId';
		
		function loadTabData(title, row)
		{
			switch (title)
			{
				case '详情':
					$editClientForm.form('clear').form('load', 'marketing/clientMgr/getOrderById?orderId=' + row.id);
					break;
				case '回访记录':
					$orderVisitGrid.datagrid('unselectAll').datagrid('reload', {orderId: row.id});
					break;
				case '审核流程':
					$orderApproveGrid.datagrid('unselectAll').datagrid('reload', {orderId: row.id});
					break;
			}
		}
		
		function clearTabData(title)
		{
			switch (title)
			{
				case '详情':
					$editClientForm.form('clear');
					break;
				case '回访记录':
					$orderVisitGrid.datagrid('loadData', []);
					break;
				case '审核流程':
					$orderApproveGrid.datagrid('loadData', []);
					break;
			}
		}
		
		function submitEditClientForm()
		{
			var selIds = $orderDatagrid.datagrid('getSelections');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请选中一个客户。');
				return;
			}
			$editClientForm.form('submit',
			{
				onSubmit: function()
				{
					if(!$(this).form('validate'))
						return false;
				},
				success: function(data)
				{
					data = $.fn.form.defaults.success(data);
					if(data.returnCode == 0)
						$orderDatagrid.datagrid('reload');
					else
						$.messager.show({title: '提示', msg: data.msg});
				}
			});
		}
		
		$('#showAddOrderWindowBtn').linkbutton({onClick: showAddClientWindow});
		$('#removeOrderBtn').linkbutton({onClick: removeOrder});
		$('#addOrderVisitBtn').linkbutton({onClick: showAddClientVisitWindow});
		$('#applyOrderBtn').linkbutton({onClick: showApplyOrderWindow});
		$('#showBusinessTransferWindowBtn').linkbutton({onClick: showBusinessTransferWindow});
		
		$clientMgrTab.tabs
		({
			border: false,
			onSelect: function(title, index)
			{
				var selRows = $orderDatagrid.datagrid('getSelections');
				
				if(selRows.length == 1)
					loadTabData(title, selRows[0]);
				else
					clearTabData(title);
			}
		});
		$clientMgrTab.tabs('hideTool');
		
		function showBusinessTransferWindow()
		{
			var selIds = $orderDatagrid.datagrid('getCheckedRowPkValues');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请<span style="color: red;">勾选</span>需要业务转移的客户。');
				return;
			}
			var selRows = $orderDatagrid.datagrid('getChecked');
			if(selRows.length > 0){
				var flag = false;
				$.each(selRows,function(index,obj){
					if(obj.salesmanStatus ==1){
						if(obj.status ==90 || obj.status ==0 || obj.status ==64){
							flag = true;
							return;
						}
					}
				});
				if(flag){
					$.messager.alert('提示', '在职业务员的客户状态为<span style="color: red;">已签单、死单、不准单</span>的不能转移。');
					return;
				}
			}
			$businessTransferWindow.window('clear');
			$businessTransferWindow.window('open').window
			({
				title: '请选择业务员',
			}).window('open').window('refresh', 'marketing/clientMgr/showAllSalesman');
		}
		
		
		function removeOrder()
		{
			var selIds = $orderDatagrid.datagrid('getCheckedRowPkValues');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请<span style="color: red;">勾选</span>状态为<span style="color: red;">正跟踪</span>的客户。');
				return;
			}
			var selRows = $orderDatagrid.datagrid('getChecked');
			if(selRows.length > 0){
				var flag = false;
				$.each(selRows,function(index,obj){
					if(obj.status !=10){
						flag = true;
						return;
					}
				});
				if(flag){
					$.messager.alert('提示', '只能放弃状态为<span style="color: red;">正跟踪</span>的客户。');
					return;
				}
			}
			$.messager.confirm('警告','确定要放弃该客户吗？',function(r)
			{
				if (!r)
					return;
				$.post
				(
					'marketing/clientMgr/deleteOrderByIds',
					{delIds : selIds},
					function(data, textStatus, jqXHR)
					{
						if(data.returnCode == 0)
						{
							$.messager.show({title:'提示',msg:'操作成功。'});
							$orderDatagrid.datagrid('reload');
						}
						else
							$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});   
					}
				);
			});
		}
		
		$addClientVisitWindow.window({width: 350});
		$addClientWindow.window({width: 500});
		$applyOrderWindow.window({width: 340});
		$selectInfoerWindow.window({width: 350});
		$businessTransferWindow.window({width: 500});
		
		function showApplyOrderWindow()
		{
			var selIds = $orderDatagrid.datagrid('getSelections');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请选中状态为<span style="color: red;">正跟踪</span>或<span style="color: red;">已打回</span>的客户。');
				return;
			}
			var selRows = $orderDatagrid.datagrid("getSelections");
			if(selRows[0].status !=10 && selRows[0].status !=14){
				$.messager.alert('提示', '只能申请状态为<span style="color: red;">正跟踪</span>或<span style="color: red;">已打回</span>的客户。');
				return;
			}
			$applyOrderWindow.window('clear');
			$applyOrderWindow.window('open').window
			({
				title: '申请在谈单',
				content: applyOrderWindowHtml
			}).window('open').window('center');
		}
		
		var applyOrderWindowHtml = 
			'<form id="applyOrderForm" action="marketing/clientMgr/applyOrder" method="post" style="width: 100%;">' + 
			'	<table width="100%">' + 
			'		<tr>' + 
			'			<td align="right" style="min-width: 70px;"><label>客户名称：</label></td>' + 
			'			<td><input id="clientName" name="name" readonly="readonly" class="easyui-textbox" style="width: 230px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>联系电话：</label></td>' + 
			'			<td><input id="telAll" name="telAll" readonly="readonly" class="easyui-textbox" style="width: 230px;" /></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>备注：</label></td>' + 
			'			<td><input name="remark" required="required" class="easyui-textbox" multiline="true" style="width: 230px; height:50px;" /></td>' + 
			'		</tr>' + 
			'		<input id="orderId"  name="orderId" type="hidden" value="" />' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitApplyOrderForm();" iconCls="icon-ok" href="javascript:void(0)">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$applyOrderWindow.window(\'close\');" iconCls="icon-cancel" href="javascript:void(0)">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $applyOrderWindow = $(\'div#applyOrderWindow\');' +
			'var $orderDatagrid = $(\'table#orderDatagrid\');' +
			'var $orderApproveGrid = $(\'table#orderApproveGrid\');' +
			'var $clientMgrTab = $(\'div#clientMgrTab\');' +
			'var selRows = $orderDatagrid.datagrid("getSelections");' +
			'$applyOrderWindow.find(\'#orderId\').val(selRows[0].id);' +
			'$applyOrderWindow.find(\'#clientName\').val(selRows[0].name);' +
			'$applyOrderWindow.find(\'#telAll\').val(selRows[0].telAll);' +
			'function submitApplyOrderForm()' + 
			'{' + 
			'	$applyOrderWindow.find(\'form#applyOrderForm\').form(\'submit\',' + 
			'	{' + 
			'		onSubmit: function()' + 
			'		{' + 
			'			if(!$(this).form(\'validate\'))' + 
			'				return false;' + 
			'		},' + 
			'		success: function(data)' + 
			'		{' + 
			'			data = $.fn.form.defaults.success(data);' + 
			'			if(data.returnCode == 0)' + 
			'			{' + 
			'				$orderDatagrid.datagrid("unselectAll").datagrid(\'reload\');' +
			'				if($clientMgrTab.tabs("getSelected").panel("options").title == "审核流程")' + 
			'					$orderApproveGrid.datagrid("unselectAll").datagrid(\'reload\');' + 
			'				$applyOrderWindow.window(\'close\');' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		
		function showAddClientVisitWindow()
		{
			var selIds = $orderDatagrid.datagrid('getSelections');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请选中一个客户。');
				return;
			}
			$addClientVisitWindow.window('clear');
			$addClientVisitWindow.window('open').window
			({
				title: '新增回访记录',
				content: addClientVisitWindowHtml
			}).window('open').window('center');
		}
		
		var addClientVisitWindowHtml = 
			'<form id="addClientVisitForm" action="marketing/clientMgr/addOrderVisit" method="post" style="width: 100%;">' + 
			'	<input id="orderId"  name="orderId" type="hidden" value="" />' + 
			'	<input id="visitorId"  name="visitorId" type="hidden" value="" />' + 
			'	<table width="100%">' + 
			'		<tr>' + 
			'			<td align="right"><label>回访内容：</label></td>' + 
			'			<td><input name="content" required="required" multiline="true" class="easyui-textbox" style="width: 230px;height:50px;" /></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitAddClientVisitForm();" iconCls="icon-ok" href="javascript:void(0)">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$addClientVisitWindow.window(\'close\');" iconCls="icon-cancel" href="javascript:void(0)">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $addClientVisitWindow = $(\'div#addClientVisitWindow\');' +
			'var $orderDatagrid = $(\'table#orderDatagrid\');' +
			'var $orderVisitGrid = $(\'table#orderVisitGrid\');' +
			'var $clientMgrTab = $(\'div#clientMgrTab\');' +
			'var selRows = $orderDatagrid.datagrid("getSelections");' +
			'$addClientVisitWindow.find(\'#orderId\').val(selRows[0].id);' +
			'$addClientVisitWindow.find(\'#visitorId\').val(selRows[0].salesmanId);' +
			'function submitAddClientVisitForm()' + 
			'{' + 
			'	$addClientVisitWindow.find(\'form#addClientVisitForm\').form(\'submit\',' + 
			'	{' + 
			'		onSubmit: function()' + 
			'		{' + 
			'			if(!$(this).form(\'validate\'))' + 
			'				return false;' + 
			'		},' + 
			'		success: function(data)' + 
			'		{' + 
			'			data = $.fn.form.defaults.success(data);' + 
			'			if(data.returnCode == 0)' + 
			'			{' + 
			'				if($clientMgrTab.tabs("getSelected").panel("options").title == "回访记录")' + 
			'					$orderVisitGrid.datagrid("unselectAll").datagrid(\'reload\');' + 
			'				$addClientVisitWindow.window(\'close\');' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		
		function showAddClientWindow()
		{
			$addClientWindow.window('clear');
			$addClientWindow.window('open').window
			({
				title: '新增客户',
				content: addClientWindowHtml
			}).window('open').window('center');
		}
		
		var addClientWindowHtml = 
			'<form id="addClientForm" action="marketing/infoerMgr/addClient" method="post" style="width: 100%;">' + 
			'	<input id="clientTelCount" type="hidden" value="1" />' +
			'	<input type="hidden" name="infoerId" id="addClientForm_infoerIdInput" type="hidden" value="1" />' +
			'	<table width="100%" id="clientTab" >' + 
			'		<tr>' + 
			'			<td style="min-width: 70px;" align="right"><label>联系人：</label></td>' + 
			'			<td><input name="name" class="easyui-textbox" required="required" style="width: 140px;"/></td>' + 
			'			<td style="min-width: 75px;" align="right"><label>所属信息员：</label></td>' + 
			'			<td><input id="addClientForm_infoerSearchbox" name="infoerName" required="required" prompt="请选择信息员" editable="false" class="easyui-searchbox" style="width: 136px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>联系电话1：</label></td>' + 
			'			<td><input name="tel1" id="tel1" required="required" style="width: 140px;"/></td>' + 
			'			<td align="right"><label>联系电话2：</label></td>' + 
			'			<td><input name="tel2" id="tel2" style="width: 140px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>联系电话3：</label></td>' + 
			'			<td><input name="tel3" id="tel3" style="width: 140px;"/></td>' + 
			'			<td align="right"><label>联系电话4：</label></td>' + 
			'			<td><input name="tel4" id="tel4" style="width: 140px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>联系电话5：</label></td>' + 
			'			<td><input name="tel5" id="tel5" style="width: 140px;"/></td>' + 
			'			<td colspan="2"><font id="errorclienttel" color="red"></font></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>单位地址：</label></td>' + 
			'			<td colspan="3"><input name="orgAddr" class="easyui-textbox" style="width: 398px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>工程名称：</label></td>' + 
			'			<td colspan="3"><input name="projectName" class="easyui-textbox" style="width: 398px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>面积：</label></td>' + 
			'			<td colspan="3"><input name="projectAddr" class="easyui-textbox" style="width: 398px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitAddClientForm();" iconCls="icon-ok" href="javascript:void(0)">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$addClientWindow.window(\'close\');" iconCls="icon-cancel" href="javascript:void(0)">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $addClientWindow = $(\'div#addClientWindow\');' +
			'var $orderDatagrid = $(\'table#orderDatagrid\');' +
			'var selRows = $orderDatagrid.datagrid("getSelections");' +
			'var $infoerNameSearchbox = $addClientWindow.find("#addClientForm_infoerSearchbox");' +
			'$infoerNameSearchbox.searchbox({searcher: function(){showSelectInfoerWindow();}});' + 
			'var $tel1Input = $(\'table input#tel1\');' +
			'$tel1Input.textbox({});' +
			'$tel1Input.textbox("textbox").bind("blur", function(){checkClientTelValue($tel1Input.get(0));});' +
			'var $tel2Input = $(\'table input#tel2\');' +
			'$tel2Input.textbox({});' +
			'$tel2Input.textbox("textbox").bind("blur", function(){checkClientTelValue($tel2Input.get(0));});' +
			'var $tel3Input = $(\'table input#tel3\');' +
			'$tel3Input.textbox({});' +
			'$tel3Input.textbox("textbox").bind("blur", function(){checkClientTelValue($tel3Input.get(0));});' +
			'var $tel4Input = $(\'table input#tel4\');' +
			'$tel4Input.textbox({});' +
			'$tel4Input.textbox("textbox").bind("blur", function(){checkClientTelValue($tel4Input.get(0));});' +
			'var $tel5Input = $(\'table input#tel5\');' +
			'$tel5Input.textbox({});' +
			'$tel5Input.textbox("textbox").bind("blur", function(){checkClientTelValue($tel5Input.get(0));});' +
			'function submitAddClientForm()' + 
			'{' + 
			'	var errortel = $(\'#errorclienttel\');'+
			'	$addClientWindow.find(\'form#addClientForm\').form(\'submit\',' + 
			'	{' + 
			'		onSubmit: function()' + 
			'		{' + 
			'			if(!$(this).form(\'validate\'))' + 
			'				return false;' + 
			'			if(errortel.html().length > 0){' + 
			'				return false;' + 
			'			}'+
			'			for(var i=1;i<6;i++){' + 
			'				checkClientTelValue($(\'table input#tel\'+i+\'\').get(0));'+
			'			}'+
			'		},' + 
			'		success: function(data)' + 
			'		{' + 
			'			data = $.fn.form.defaults.success(data);' + 
			'			if(data.returnCode == 0)' + 
			'			{' + 
			'				$orderDatagrid.datagrid(\'reload\');' + 
			'				$addClientWindow.window(\'close\');' + 
			'			}else' + 
			'				errortel.html(data.msg);' + 
			'		}' + 
			'	});' + 
			'}' + 
			'function checkClientTelValue(obj)' + 
			'{' + 
			'	var errorId = obj.id;'+
			'	errorId = errorId.charAt(errorId.length - 1);'+
			'	if(obj.value.length==0) '+
		    ' 	{ '+
		    '		if(errorId ==1) '+
		    ' 		{ '+
		    '      		$(\'#errorclienttel\').html("联系电话未填！"); '+
		    '      		return false; '+
		    '     	}'+
		    '   }else{ '+
			'		var reg = /^1[0-9]{10}$/;'+
			'		if(!(reg.test(obj.value))){'+
			'			$(\'#errorclienttel\').html("无效的手机号码！");'+
			'			return false; '+
			'		}else{'+
			'			$(\'#errorclienttel\').html("");'+
			'		}'+
			'		if(errorId > 1) '+
			' 		{ '+
			'			if($(\'#tel1\').val() == obj.value){' + 
			'      			$(\'#errorclienttel\').html("联系电话重复！"); '+
			'      			return false; '+
			'   		} '+
			'   	} '+
			'	}'+
			'	$.ajax'+
			'	({'+
			'		url: \'marketing/infoerMgr/checkClientTel\','+
			'		data: {tel:obj.value},'+
			'		success: function(data, textStatus, jqXHR)'+
			'		{'+
			'			if(data.returnCode != 0){'+
			'				$(\'#errorclienttel\').html(data.msg); '+  
			'			}else{ '+  
			'				$(\'#errorclienttel\').html(""); '+  
			'			} '+  
			'		}'+
			'	});'+
			'} ' + 
			'</script>';
	}

	init();
});