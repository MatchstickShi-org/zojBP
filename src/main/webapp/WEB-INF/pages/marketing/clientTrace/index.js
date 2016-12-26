$(function()
{
	var $orderDatagrid = $('table#orderDatagrid');
	var $infoerNameTextbox = $('#clientTrace\\.infoerNameInput');
	var $orderNameTextbox = $('#clientTrace\\.nameInput');
	var $telTextbox = $('#clientTrace\\.telInput');
	var $statusCheckbox = $('[name="clientTrace.status"][checked]');
	var $queryOrderBtn = $('a#queryOrderBtn');
	var $addClientVisitWindow = $('div#addClientVisitWindow');
	var $addClientWindow = $('div#addClientWindow');
	var $applyOrderWindow = $('div#applyOrderWindow');
	var $selectInfoerWindow = $('div#selectInfoerWindow');
	var $clientMgrTab = $('div#clientMgrTab');
	var $editClientForm = $('form#editOrderForm');
	var $submitUpdateClientFormBtn = $('a#submitUpdateClientFormBtn');
	var $refreshUpdateClientFormBtn = $('a#refreshUpdateClientFormBtn');
	var $orderVisitGrid = $('table#orderVisitGrid');
	
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
			border: false,
			columns:
			[[
				{field:'id', hidden: true},
				{field: 'ck', checkbox: true},
				{field:'name', title:'名称', width: 3},
				{field:'telAll', title:'联系电话', width: 5},
				{field:'orgAddr', title:'单位地址', width: 8},
				{field:'projectName', title:'工程名称', width: 8},
				{field:'projectAddr', title:'工程地址', width: 8},
				{field:'infoerName', title:'信息员', width: 3},
				{field:'salesmanName', title:'业务员', width: 3},
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
								return '未评级';
								break;
						}
					}
				},
				{field:'insertTime', title:'录入日期', width: 5}
			]],
			pagination: true,
			singleSelect: true,
			selectOnCheck: false,
			checkOnSelect: false,
			url: 'marketing/clientMgr/getAllClientTrace',
			onSelect: function(idx, row)
			{
				loadTabData($clientMgrTab.tabs('getSelected').panel('options').title, row);
			},
			nowrap: false,
		});
		
		$queryOrderBtn.linkbutton
		({
			'onClick': function()
			{
				$orderDatagrid.datagrid('loading');
				var chk_value =''; 
				$('input[name="statusInput"]:checked').each(function(){ 
					chk_value = chk_value+$(this).val()+","; 
				}); 
				$.ajax
				({
					url: 'marketing/clientMgr/getAllClientTrace',
					data: {name: $orderNameTextbox.textbox('getValue'), tel: $telTextbox.textbox('getValue'),infoerName: $infoerNameTextbox.textbox('getValue'),status:chk_value},
					success: function(data, textStatus, jqXHR)
					{
						if(data.returnCode == 0)
							$orderDatagrid.datagrid('loadData', data);
						else
							$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});   
						$orderDatagrid.datagrid('loaded');
					}
				});
			}
		});
		
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
		
		$submitUpdateClientFormBtn.linkbutton({'onClick': submitEditClientForm});
		$refreshUpdateClientFormBtn.linkbutton({'onClick': function()
		{
			var selRows = $orderDatagrid.datagrid('getSelections');
			if(selRows.length == 1)
				loadTabData($clientMgrTab.tabs('getSelected').panel('options').title, selRows[0]);
		}});
		
		$orderVisitGrid.datagrid
		({
			idField: 'id',
			toolbar: '#orderVisitGridToolbar',
			columns:
			[[
				{field:'id', hidden: true},
				{field:'content', title:'回访内容', width: 5},
				{field:'date', title:'回访日期', width: 5}
			]],
			pagination: true
		});

		$orderVisitGrid.datagrid('options').url = 'marketing/clientMgr/getOrderVisitByOrder';
		
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
			}
		}
		
		function submitEditClientForm()
		{
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
		
		
		function removeOrder()
		{
			var selIds = $orderDatagrid.datagrid('getCheckedRowPkValues');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请<span style="color: red;">勾选</span>要放弃的客户。');
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
			$.messager.confirm('警告','确定要放弃勾选的客户吗？',function(r)
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
		$addClientWindow.window({width: 450});
		$applyOrderWindow.window({width: 450});
		$selectInfoerWindow.window({width: 350});
		
		function showApplyOrderWindow()
		{
			var selIds = $orderDatagrid.datagrid('getSelections');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请选中要申请在谈单的客户。');
				return;
			}
			var selRows = $orderDatagrid.datagrid("getSelections");
			if(selRows[0].status !=10){
				$.messager.alert('提示', '只能申请状态为<span style="color: red;">正跟踪</span>的客户为在谈单。');
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
			'			<td align="right"><label>客户名称：</label></td>' + 
			'			<td><input id="clientName" name="name" readonly="readonly" class="easyui-textbox" /></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>联系电话：</label></td>' + 
			'			<td><input id="telAll" name="telAll" readonly="readonly" class="easyui-textbox" style="width: 230px;" /></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>备&nbsp;&nbsp;注：</label></td>' + 
			'			<td><textarea name="remark" required="required" class="easyui-textbox" style="width: 230px;height:50px;"></textarea></td>' + 
			'		</tr>' + 
			'		<input id="orderId"  name="orderId" type="hidden" value="" />' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitApplyOrderForm();" href="javascript:void(0)">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$applyOrderWindow.window(\'close\');" href="javascript:void(0)">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $applyOrderWindow = $(\'div#applyOrderWindow\');' +
			'var $orderDatagrid = $(\'table#orderDatagrid\');' +
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
			'				$applyOrderWindow.window(\'close\');' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		
		function showAddClientVisitWindow()
		{
			$addClientVisitWindow.window('clear');
			$addClientVisitWindow.window('open').window
			({
				title: '新增回访记录',
				content: addClientVisitWindowHtml
			}).window('open').window('center');
		}
		
		var addClientVisitWindowHtml = 
			'<form id="addClientVisitForm" action="marketing/clientMgr/addOrderVisit" method="post" style="width: 100%;">' + 
			'	<table width="100%">' + 
			'		<tr>' + 
			'			<td align="right"><label>回访内容：</label></td>' + 
			'			<td><textarea name="content" required="required" style="width: 230px;height:100px;"></textarea></td>' + 
			'		</tr>' + 
			'		<input id="orderId"  name="orderId" type="hidden" value="" />' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitAddClientVisitForm();" href="javascript:void(0)">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$addClientVisitWindow.window(\'close\');" href="javascript:void(0)">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $addClientVisitWindow = $(\'div#addClientVisitWindow\');' +
			'var $orderDatagrid = $(\'table#orderDatagrid\');' +
			'var $orderVisitGrid = $(\'table#orderVisitGrid\');' +
			'var selRows = $orderDatagrid.datagrid("getSelections");' +
			'$addClientVisitWindow.find(\'#orderId\').val(selRows[0].id);' +
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
			'				$orderVisitGrid.datagrid("unselectAll").datagrid(\'reload\');' + 
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
			'	<table width="100%" id="clientTab" >' + 
			'		<tr>' + 
			'			<td style="width: 120px;" align="right"><label>联系人：</label></td>' + 
			'			<td style="width: 180px;"><input name="name" class="easyui-textbox" required="required" style="width: 150px;"/></td>' + 
			'			<td></td>' + 
			'		</tr>' + 
			'		<tr id="clientTelTr">' + 
			'			<td align="right"><label>联系电话：</label></td>' + 
			'			<td><input name="tel1" id="tel1" onblur="checkClientTelValue(this);" required="required" style="width: 150px;"/><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="addClientTelBtn"></a></td>' + 
			'			<td><font id="errorclienttel1" color="red"></font></td>' + 
			'		</tr>' + 
			'		<input id="clientTelCount" type="hidden" value="1" />' +
			'		<input type="hidden" name="infoerId" id="addClientForm_infoerIdInput" type="hidden" value="1" />' +
			'		<tr>' + 
			'			<td align="right"><label>所属信息员：</label></td>' + 
			'			<td><input id="addClientForm_infoerSearchbox" name="infoerName" prompt="请选择信息员" editable="false" class="easyui-searchbox" style="width: 160px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>单位地址：</label></td>' + 
			'			<td><input name="orgAddr" class="easyui-textbox" style="width: 200px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>工程名称：</label></td>' + 
			'			<td><input name="projectName" class="easyui-textbox" style="width: 200px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>工程地址：</label></td>' + 
			'			<td><input name="projectAddr" class="easyui-textbox" style="width: 200px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="center" colspan="3">' + 
			'				<a class="easyui-linkbutton" onclick="submitAddClientForm();" href="javascript:void(0)">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$addClientWindow.window(\'close\');" href="javascript:void(0)">取消</a>' + 
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
			'function submitAddClientForm()' + 
			'{' + 
			'	$addClientWindow.find(\'form#addClientForm\').form(\'submit\',' + 
			'	{' + 
			'		onSubmit: function()' + 
			'		{' + 
			'			if(!$(this).form(\'validate\'))' + 
			'				return false;' + 
			'			if($(\'#tel1\').val() == ""){' + 
			'      			$(\'#errorclienttel1\').html("联系电话未填！"); '+
			'				return false;' + 
			'			}' + 
			'			var reg = /^1[0-9]{10}$/;'+
			'			if(!(reg.test($(\'#tel1\').val()))){'+
			'				$(\'#errorclienttel1\').html("无效的手机号码！");'+
			'				$(\'#tel1\').val().focus(); '+
			'				return false; '+
			'			}'+
			'		},' + 
			'		success: function(data)' + 
			'		{' + 
			'			data = $.fn.form.defaults.success(data);' + 
			'			if(data.returnCode == 0)' + 
			'			{' + 
			'				$orderDatagrid.datagrid(\'reload\');' + 
			'				$addClientWindow.window(\'close\');' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'function checkClientTelValue(obj)' + 
			'{' + 
			'	var errorId = obj.name;'+
			'	errorId = errorId.charAt(errorId.length - 1);'+
			'	if(obj.value.length==0) '+
		    ' 	{ '+
		    '      $(\'#errorclienttel\'+errorId+\'\').html("联系电话未填！"); '+
		    '      return; '+
		    '   } '+
			'	var reg = /^1[0-9]{10}$/;'+
			'	if(!(reg.test(obj.value))){'+
			'		$(\'#errorclienttel\'+errorId+\'\').html("无效的手机号码！");'+
			'		obj.focus(); '+
			'		return; '+
			'	}else{'+
			'		$(\'#errorclienttel\'+errorId+\'\').html("");'+
			'	}'+
			'	if(errorId > 1) '+
			' 	{ '+
			'		if($(\'#tel1\').val() == obj.value){' + 
			'      		$(\'#errorclienttel\'+errorId+\'\').html("联系电话重复！"); '+
			'      		return; '+
			'   	} '+
			'   } '+
			'	$.ajax'+
			'	({'+
			'		url: \'marketing/infoerMgr/checkClientTel\','+
			'		data: {tel:obj.value},'+
			'		success: function(data, textStatus, jqXHR)'+
			'		{'+
			'			if(data.returnCode != 0){'+
			'				$(\'#errorclienttel\'+errorId+\'\').html(data.msg); '+  
			'				obj.focus(); '+  
			'			}else{ '+  
			'				$(\'#errorclienttel\'+errorId+\'\').html(""); '+  
			'			} '+  
			'		}'+
			'	});'+
			'} ' + 
			'function removeTelAdd(obj)' +
			'{' +
			'	$.messager.confirm(\'确认\',\'您确认想要删除此联系电话吗？\',function(r){' +  
			'   	if (r){ ' +
			' 			var count = $(\'#clientTelCount\').val();' + 
			' 			count = parseInt(count)-1;' + 
			' 			$(\'#clientTelCount\').val(count);' +
			'			obj.parent().parent().remove();' +
			'    	}  ' +
			'	}); ' +
			'} ' +
			'$(\'#addClientTelBtn\').click(function()'+
			'{' + 
			' 	var count = $(\'#clientTelCount\').val();' + 
			' 	count = parseInt(count)+1;' + 
			' 	if(count <6){' + 
			' 		$(\'#clientTelCount\').val(count);' + 
			'		var appendHtml =\'<tr>'+
			'		<td align="right"><label>联系电话：</label></td>'+
			'		<td><input name="tel\'+count+\'" onblur="checkClientTelValue(this);" class="easyui-textbox" required="required" style="width: 150px;"/><a href="javascript:void(0)" onclick="removeTelAdd($(this));" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="removeClientTelBtn">删除</a></td>' +
			'		<td><font style="text-align:top" id="errorclienttel\'+count+\'" color="red"></font></td>' +
			'		</tr>\';' + 
			'		$(\'#clientTelTr\').after(appendHtml);' +   
			'	}else{' +   
			'		$.messager.alert(\'提示\', \'联系电话最多只能添加5个！\');' +   
			'	}' +   
			'});' + 
			'</script>';
	}

	init();
});