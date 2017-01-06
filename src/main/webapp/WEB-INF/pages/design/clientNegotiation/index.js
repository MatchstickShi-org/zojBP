$(function()
{
	var $orderDatagrid = $('table#orderDatagrid');
	var $orderCheckDatagrid = $('table#orderCheckDatagrid');
	var $designerNameTextbox = $('#clientTrace\\.designerNameInput');
	var $orderNameTextbox = $('#clientTrace\\.nameInput');
	var $telTextbox = $('#clientTrace\\.telInput');
	var $orderCheckDesignerNameTextbox = $('#order\\.designerNameInput');
	var $orderCheckNameTextbox = $('#order\\.nameInput');
	var $orderCheckTelTextbox = $('#order\\.telInput');
	var $queryOrderBtn = $('a#queryOrderBtn');
	var $queryCheckOrderBtn = $('a#queryCheckOrderBtn');
	var $addClientVisitWindow = $('div#addClientVisitWindow');
	var $addClientWindow = $('div#addClientWindow');
	var $permitOrderWindow = $('div#permitOrderWindow');
	var $rejectOrderWindow = $('div#rejectOrderWindow');
	var $dealOrderWindow = $('div#dealOrderWindow');
	var $deadOrderWindow = $('div#deadOrderWindow');
	var $disagreeOrderWindow = $('div#disagreeOrderWindow');
	var $repulseOrderWindow = $('div#repulseOrderWindow');
	var $selectDesignerWindow = $('div#selectDesignerWindow');
	var $clientMgrTab = $('div#clientMgrTab');
	var $orderCheckMgrTab = $('div#orderCheckMgrTab');
	var $editClientForm = $('form#editOrderForm');
	var $submitUpdateClientFormBtn = $('a#submitUpdateClientFormBtn');
	var $refreshUpdateClientFormBtn = $('a#refreshUpdateClientFormBtn');
	var $orderVisitGrid = $('table#orderVisitGrid');
	var $orderStylistVisitGrid = $('table#orderStylistVisitGrid');
	
	showSelectDesignerWindow = function()
	{
		$selectDesignerWindow.window('clear');
		$selectDesignerWindow.window('open').window
		({
			title: '请选择设计师',
		}).window('open').window('refresh', 'design/clientMgr/showSelectDesignerWindow');
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
				{field: 'ck', checkbox: true},
				{field:'name', title:'名称', width: 3},
				{field:'telAll', title:'联系电话', width: 5},
				{field:'orgAddr', title:'单位地址', width: 8},
				{field:'projectName', title:'工程名称', width: 8},
				{field:'projectAddr', title:'工程地址', width: 8},
				{field:'infoerName', title:'信息员', width: 3},
				{field:'salesmanId', hidden: true},
				{field:'salesmanName', title:'业务员', width: 3},
				{field:'salesmanStatus', hidden: true},
				{field:'designerId', hidden: true},
				{field:'designerName', title:'设计师', width: 3},
				{field:'designerStatus', hidden: true},
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
							case 30:
							case 32:
								return '在谈单审核中';
								break;
							case 34:
								return '在谈单已批准';
								break;
							case 60:
							case 62:
								return '不准单审核中';
								break;
							case 64:
								return '不准单';
								break;
							case 14:
								return '已打回';
								break;
							case 90:
								return '已签单';
								break;
							case 0:
								return '死单';
								break;
							default:
								return '无状态';
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
			url: 'design/clientMgr/getAllClientNegotiation',
			onSelect: function(idx, row)
			{
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
				$orderDatagrid.datagrid('loading');
				var chk_value =[]; 
				$('#orderDatagridToolbar :input[name="statusInput"]:checked').each(function(){ 
					chk_value.push($(this).val());   
				}); 
				$.ajax
				({
					url: 'design/clientMgr/getAllClientNegotiation',
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
		$orderCheckMgrTab.tabs({});
		if(_session_loginUserRole != 6)
			$orderCheckMgrTab.tabs('hideHeader');
		$clientMgrTab.tabs
		({
			border: false,
			onSelect: function(title, index)
			{
				var selTab = $orderCheckMgrTab.tabs('getSelected');
				var index = $orderCheckMgrTab.tabs('getTabIndex',selTab);
				var selRows = null;
				if(index == 0)
					selRows = $orderDatagrid.datagrid('getSelections');
				else
					selRows = $orderCheckDatagrid.datagrid('getSelections');
				if(selRows.length == 1)
					loadTabData(title, selRows[0]);
				else
					clearTabData(title);
			}
		});
		
		$submitUpdateClientFormBtn.linkbutton({'onClick': submitEditClientForm});
		$refreshUpdateClientFormBtn.linkbutton({'onClick': function()
		{
			var selTab = $orderCheckMgrTab.tabs('getSelected');
			var index = $orderCheckMgrTab.tabs('getTabIndex',selTab);
			var selRows = null;
			if(index == 0)
				selRows = $orderDatagrid.datagrid('getSelections');
			else
				selRows = $orderCheckDatagrid.datagrid('getSelections');
			if(selRows.length == 1)
				loadTabData($clientMgrTab.tabs('getSelected').panel('options').title, selRows[0]);
			else
				$.messager.alert('提示', '请选中一个客户。');
		}});
		
		$orderCheckDatagrid.datagrid
		({
			idField: 'id',
			toolbar: '#orderCheckDatagridToolbar',
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
				{field:'designerName', title:'设计师', width: 3},
				{field:'salesmanStatus', hidden: true},
				{
					field:'status', title:'状态', width: 4, formatter: function(value, row, index)
					{
						switch (value)
						{
							case 32:
								return '在谈单审核中';
								break;
							case 60:
								return '不准单审核中';
								break;
							default:
								return '无状态';
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
			url: 'design/clientMgr/getAllClientCheck',
			onSelect: function(idx, row)
			{
				loadTabData($clientMgrTab.tabs('getSelected').panel('options').title, row);
			},
		});
		
		$queryCheckOrderBtn.linkbutton
		({
			'onClick': function()
			{
				$orderCheckDatagrid.datagrid('loading');
				var chk_value =[]; 
				$('#orderCheckDatagridToolbar :input[name="orderStatusInput"]:checked').each(function(){ 
					chk_value.push($(this).val());   
				}); 
				$.ajax
				({
					url: 'design/clientMgr/getAllClientCheck',
					data: {name: $orderCheckNameTextbox.textbox('getValue'), tel: $orderCheckTelTextbox.textbox('getValue'),infoerName: $orderCheckInfoerNameTextbox.textbox('getValue'),status:chk_value},
					success: function(data, textStatus, jqXHR)
					{
						if(data.returnCode == 0)
							$orderCheckDatagrid.datagrid('loadData', data);
						else
							$.messager.show({title:'提示', msg:'操作失败\n' + data.msg});   
						$orderCheckDatagrid.datagrid('loaded');
					}
				});
			}
		});
		
		$orderCheckMgrTab.tabs
		({
			border: false,
			onSelect: function(title, index)
			{
				var selRows = null;
				if(title == '在谈单查询')
					$orderDatagrid.datagrid('unselectAll').datagrid('reload');
				else
					$orderCheckDatagrid.datagrid('unselectAll').datagrid('reload');
				$editClientForm.form('clear');
				$orderVisitGrid.datagrid('loadData', []);
				$orderStylistVisitGrid.datagrid('loadData', []);
			}
		});
		
		$orderVisitGrid.datagrid
		({
			idField: 'id',
			columns:
			[[
				{field:'id', hidden: true},
				{field:'content', title:'回访内容', width: 5},
				{field:'date', title:'回访日期', width: 5}
			]],
			pagination: true
		});

		$orderVisitGrid.datagrid('options').url = 'design/clientMgr/getOrderVisitByOrder';
		
		$orderStylistVisitGrid.datagrid
		({
			idField: 'id',
			toolbar: '#orderVisitGridToolbar',
			columns:
				[[
				  {field:'id', hidden: true},
				  {field:'content', title:'回访内容', width: 5},
				  {field:'date', title:'回访日期', width: 5},
				  {field:'comment', title:'批示', width: 5}
				  ]],
				  pagination: true
		});
		
		$orderStylistVisitGrid.datagrid('options').url = 'design/clientMgr/getStylistOrderVisitByOrder';
		
		function loadTabData(title, row)
		{
			switch (title)
			{
				case '详情':
					$editClientForm.form('clear').form('load', 'design/clientMgr/getOrderById?orderId=' + row.id);
					break;
				case '业务员回访记录':
					$orderVisitGrid.datagrid('unselectAll').datagrid('reload', {orderId: row.id});
					break;
				case '设计师回访记录':
					$orderStylistVisitGrid.datagrid('unselectAll').datagrid('reload', {orderId: row.id});
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
				case '业务员回访记录':
					$orderVisitGrid.datagrid('loadData', []);
					break;
				case '设计师回访记录':
					$orderStylistVisitGrid.datagrid('loadData', []);
					break;
			}
		}
		
		function submitEditClientForm()
		{
			var selTab = $orderCheckMgrTab.tabs('getSelected');
			var index = $orderCheckMgrTab.tabs('getTabIndex',selTab);
			var selRows = null;
			if(index == 0)
				selRows = $orderDatagrid.datagrid('getSelections');
			else
				selRows = $orderCheckDatagrid.datagrid('getSelections');
			if(selRows.length == 0)
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
					if(data.returnCode == 0){
						if(index == 0)
							$orderDatagrid.datagrid('reload');
						else
							$orderCheckDatagrid.datagrid('reload');
					}
					else
						$.messager.show({title: '提示', msg: data.msg});
				}
			});
		}
		
		$('#showAddOrderWindowBtn').linkbutton({onClick: showAddClientWindow});
		$('#removeOrderBtn').linkbutton({onClick: removeOrder});
		$('#addOrderVisitBtn').linkbutton({onClick: showAddClientVisitWindow});
		$('#dealOrderWindowBtn').linkbutton({onClick: showDealOrderWindow});
		$('#deadOrderWindowBtn').linkbutton({onClick: showDeadOrderWindow});
		$('#disagreeOrderWindowBtn').linkbutton({onClick: showDisagreeOrderWindow});
		$('#repulseOrderWindowBtn').linkbutton({onClick: showRepulseOrderWindow});
		$('#showPermitOrderWindowBtn').linkbutton({onClick: showPermitOrderWindow});
		$('#showRejectOrderWindowBtn').linkbutton({onClick: showRejectOrderWindow});
		
		
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
					'design/clientMgr/deleteOrderByIds',
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
		$permitOrderWindow.window({width: 340});
		$rejectOrderWindow.window({width: 340});
		$dealOrderWindow.window({width: 340});
		$deadOrderWindow.window({width: 340});
		$disagreeOrderWindow.window({width: 340});
		$repulseOrderWindow.window({width: 340});
		$selectDesignerWindow.window({width: 350});
		
		function showPermitOrderWindow()
		{
			var selIds = $orderCheckDatagrid.datagrid('getSelections');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请选中要批准的客户。');
				return;
			}
			$permitOrderWindow.window('clear');
			$permitOrderWindow.window('open').window
			({
				title: '批准',
				content: selIds[0].status ==32 ? permitOrderWindowHtml:permitDisagreeOrderWindowHtml
			}).window('open').window('center');
		}
		
		var permitOrderWindowHtml = 
			'<form id="permitOrderForm" action="design/clientMgr/permitOrder" method="post" style="width: 100%;">' + 
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
			'			<td align="right"><label>申 请 人：</label></td>' + 
			'			<td><input id="salesmanName" name="salesmanName" readonly="readonly" class="easyui-textbox" style="width: 230px;" /><input id="salesmanId"  name="claimer" type="hidden" value="" /></td>' + 
			'		</tr>' + 
			'		<input type="hidden" name="designerId" id="permitOrderForm_designerIdInput" type="hidden" value="1" />' +
			'		<tr>' + 
			'			<td align="right"><label>分配设计师：</label></td>' + 
			'			<td><input id="permitOrderForm_designerNameSearchbox"  required="required" name="desingerName" prompt="请选择设计师" editable="false" class="easyui-searchbox" style="width: 160px;"/></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>备&nbsp;&nbsp;注：</label></td>' + 
			'			<td><input name="remark" required="required" multiline="true" class="easyui-textbox" style="width: 230px;height:50px;" /></td>' + 
			'		</tr>' + 
			'		<input id="orderId"  name="orderId" type="hidden" value="" />' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitPermitOrderForm();" href="javascript:void(0)">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$permitOrderWindow.window(\'close\');" href="javascript:void(0)">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $permitOrderWindow = $(\'div#permitOrderWindow\');' +
			'var $orderCheckDatagrid = $(\'table#orderCheckDatagrid\');' +
			'var selRows = $orderCheckDatagrid.datagrid("getSelections");' +
			'$permitOrderWindow.find(\'#orderId\').val(selRows[0].id);' +
			'$permitOrderWindow.find(\'#clientName\').val(selRows[0].name);' +
			'$permitOrderWindow.find(\'#salesmanName\').val(selRows[0].salesmanName);' +
			'$permitOrderWindow.find(\'#salesmanId\').val(selRows[0].salesmanId);' +
			'$permitOrderWindow.find(\'#telAll\').val(selRows[0].telAll);' +
			'var $designerNameSearchbox = $permitOrderWindow.find("#permitOrderForm_designerNameSearchbox");' +
			'$designerNameSearchbox.searchbox({searcher: function(){showSelectDesignerWindow();}});' + 
			'function submitPermitOrderForm()' + 
			'{' + 
			'	$permitOrderWindow.find(\'form#permitOrderForm\').form(\'submit\',' + 
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
			'				$orderCheckDatagrid.datagrid("unselectAll").datagrid(\'reload\');' + 
			'				$permitOrderWindow.window(\'close\');' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		var permitDisagreeOrderWindowHtml = 
			'<form id="permitOrderForm" action="design/clientMgr/permitOrder" method="post" style="width: 100%;">' + 
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
			'			<td align="right"><label>申 请 人：</label></td>' + 
			'			<td><input id="salesmanName" name="salesmanName" readonly="readonly" class="easyui-textbox" style="width: 230px;" /><input id="salesmanId"  name="claimer" type="hidden" value="" /></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>备&nbsp;&nbsp;注：</label></td>' + 
			'			<td><input name="remark" required="required" multiline="true" class="easyui-textbox" style="width: 230px;height:50px;" /></td>' + 
			'		</tr>' + 
			'		<input id="orderId"  name="orderId" type="hidden" value="" />' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitPermitOrderForm();" href="javascript:void(0)">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$permitOrderWindow.window(\'close\');" href="javascript:void(0)">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $permitOrderWindow = $(\'div#permitOrderWindow\');' +
			'var $orderCheckDatagrid = $(\'table#orderCheckDatagrid\');' +
			'var selRows = $orderCheckDatagrid.datagrid("getSelections");' +
			'$permitOrderWindow.find(\'#orderId\').val(selRows[0].id);' +
			'$permitOrderWindow.find(\'#clientName\').val(selRows[0].name);' +
			'$permitOrderWindow.find(\'#salesmanName\').val(selRows[0].salesmanName);' +
			'$permitOrderWindow.find(\'#salesmanId\').val(selRows[0].salesmanId);' +
			'$permitOrderWindow.find(\'#telAll\').val(selRows[0].telAll);' +
			'function submitPermitOrderForm()' + 
			'{' + 
			'	$permitOrderWindow.find(\'form#permitOrderForm\').form(\'submit\',' + 
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
			'				$orderCheckDatagrid.datagrid("unselectAll").datagrid(\'reload\');' + 
			'				$permitOrderWindow.window(\'close\');' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		
		function showRejectOrderWindow()
		{
			var selIds = $orderCheckDatagrid.datagrid('getSelections');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请选中要驳回的客户。');
				return;
			}
			$rejectOrderWindow.window('clear');
			$rejectOrderWindow.window('open').window
			({
				title: '驳回',
				content: rejectOrderWindowHtml
			}).window('open').window('center');
		}
		
		var rejectOrderWindowHtml = 
			'<form id="rejectOrderForm" action="design/clientMgr/rejectOrder" method="post" style="width: 100%;">' + 
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
			'			<td align="right"><label>申 请 人：</label></td>' + 
			'			<td><input id="salesmanName" name="salesmanName" readonly="readonly" class="easyui-textbox" style="width: 230px;" /><input id="salesmanId"  name="claimer" type="hidden" value="" /></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>备&nbsp;&nbsp;注：</label></td>' + 
			'			<td><input name="remark" required="required" multiline="true" class="easyui-textbox" style="width: 230px;height:50px;" /></td>' + 
			'		</tr>' + 
			'		<input id="orderId"  name="orderId" type="hidden" value="" />' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitRejectOrderForm();" href="javascript:void(0)">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$rejectOrderWindow.window(\'close\');" href="javascript:void(0)">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $rejectOrderWindow = $(\'div#rejectOrderWindow\');' +
			'var $orderCheckDatagrid = $(\'table#orderCheckDatagrid\');' +
			'var selRows = $orderCheckDatagrid.datagrid("getSelections");' +
			'$rejectOrderWindow.find(\'#orderId\').val(selRows[0].id);' +
			'$rejectOrderWindow.find(\'#clientName\').val(selRows[0].name);' +
			'$rejectOrderWindow.find(\'#salesmanName\').val(selRows[0].salesmanName);' +
			'$rejectOrderWindow.find(\'#salesmanId\').val(selRows[0].salesmanId);' +
			'$rejectOrderWindow.find(\'#telAll\').val(selRows[0].telAll);' +
			'function submitRejectOrderForm()' + 
			'{' + 
			'	$rejectOrderWindow.find(\'form#rejectOrderForm\').form(\'submit\',' + 
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
			'				$orderCheckDatagrid.datagrid("unselectAll").datagrid(\'reload\');' + 
			'				$rejectOrderWindow.window(\'close\');' + 
			'			}else{' + 
			'				$.messager.show({title:\'提示\', msg:\'操作失败！\' + data.msg}); ' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		
		function showDealOrderWindow()
		{
			var selIds = $orderDatagrid.datagrid('getSelections');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请选中要签单的客户。');
				return;
			}
			if(selIds[0].status != 34)
			{
				$.messager.alert('提示', '只能申请状态为<span style="color: red;">在谈单已批准</span>的客户。');
				return;
			}
			$dealOrderWindow.window('clear');
			$dealOrderWindow.window('open').window
			({
				title: '签单',
				content: dealOrderWindowHtml
			}).window('open').window('center');
		}
		
		var dealOrderWindowHtml = 
			'<form id="dealOrderForm" action="design/clientMgr/dealOrder" method="post" style="width: 100%;">' + 
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
			'			<td align="right"><label>申 请 人：</label></td>' + 
			'			<td><input id="salesmanName" name="salesmanName" readonly="readonly" class="easyui-textbox" style="width: 230px;" /><input id="salesmanId"  name="claimer" type="hidden" value="" /></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>备&nbsp;&nbsp;注：</label></td>' + 
			'			<td><input name="remark" required="required" multiline="true" class="easyui-textbox" style="width: 230px;height:50px;" /></td>' + 
			'		</tr>' + 
			'		<input id="orderId"  name="orderId" type="hidden" value="" />' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitDealOrderForm();" href="javascript:void(0)">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$dealOrderWindow.window(\'close\');" href="javascript:void(0)">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $dealOrderWindow = $(\'div#dealOrderWindow\');' +
			'var $orderDatagrid = $(\'table#orderDatagrid\');' +
			'var selRows = $orderDatagrid.datagrid("getSelections");' +
			'$dealOrderWindow.find(\'#orderId\').val(selRows[0].id);' +
			'$dealOrderWindow.find(\'#clientName\').val(selRows[0].name);' +
			'$dealOrderWindow.find(\'#salesmanName\').val(selRows[0].salesmanName);' +
			'$dealOrderWindow.find(\'#salesmanId\').val(selRows[0].salesmanId);' +
			'$dealOrderWindow.find(\'#telAll\').val(selRows[0].telAll);' +
			'function submitDealOrderForm()' + 
			'{' + 
			'	$dealOrderWindow.find(\'form#dealOrderForm\').form(\'submit\',' + 
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
			'				$dealOrderWindow.window(\'close\');' + 
			'			}else{' + 
			'				$.messager.show({title:\'提示\', msg:\'操作失败！\' + data.msg}); ' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		
		function showDeadOrderWindow()
		{
			var selIds = $orderDatagrid.datagrid('getSelections');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请选中申请死单的客户。');
				return;
			}
			if(selIds[0].status != 34)
			{
				$.messager.alert('提示', '只能申请状态为<span style="color: red;">在谈单已批准</span>的客户。');
				return;
			}
			$deadOrderWindow.window('clear');
			$deadOrderWindow.window('open').window
			({
				title: '申请死单',
				content: deadOrderWindowHtml
			}).window('open').window('center');
		}
		
		var deadOrderWindowHtml = 
			'<form id="deadOrderForm" action="design/clientMgr/deadOrder" method="post" style="width: 100%;">' + 
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
			'			<td align="right"><label>申 请 人：</label></td>' + 
			'			<td><input id="salesmanName" name="salesmanName" readonly="readonly" class="easyui-textbox" style="width: 230px;" /><input id="salesmanId"  name="claimer" type="hidden" value="" /></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>备&nbsp;&nbsp;注：</label></td>' + 
			'			<td><input name="remark" required="required" multiline="true" class="easyui-textbox" style="width: 230px;height:50px;" /></td>' + 
			'		</tr>' + 
			'		<input id="orderId"  name="orderId" type="hidden" value="" />' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitDeadOrderForm();" href="javascript:void(0)">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$deadOrderWindow.window(\'close\');" href="javascript:void(0)">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $deadOrderWindow = $(\'div#deadOrderWindow\');' +
			'var $orderDatagrid = $(\'table#orderDatagrid\');' +
			'var selRows = $orderDatagrid.datagrid("getSelections");' +
			'$deadOrderWindow.find(\'#orderId\').val(selRows[0].id);' +
			'$deadOrderWindow.find(\'#clientName\').val(selRows[0].name);' +
			'$deadOrderWindow.find(\'#salesmanName\').val(selRows[0].salesmanName);' +
			'$deadOrderWindow.find(\'#salesmanId\').val(selRows[0].salesmanId);' +
			'$deadOrderWindow.find(\'#telAll\').val(selRows[0].telAll);' +
			'function submitDeadOrderForm()' + 
			'{' + 
			'	$deadOrderWindow.find(\'form#deadOrderForm\').form(\'submit\',' + 
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
			'				$deadOrderWindow.window(\'close\');' + 
			'			}else{' + 
			'				$.messager.show({title:\'提示\', msg:\'操作失败！\' + data.msg}); ' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		
		function showDisagreeOrderWindow()
		{
			var selIds = $orderDatagrid.datagrid('getSelections');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请选中要申请不准单的客户。');
				return;
			}
			if(selIds[0].status != 34)
			{
				$.messager.alert('提示', '只能申请状态为<span style="color: red;">在谈单已批准</span>的客户。');
				return;
			}
			$disagreeOrderWindow.window('clear');
			$disagreeOrderWindow.window('open').window
			({
				title: '申请不准单',
				content: disagreeOrderWindowHtml
			}).window('open').window('center');
		}
		
		var disagreeOrderWindowHtml = 
			'<form id="disagreeOrderForm" action="design/clientMgr/disagreeOrder" method="post" style="width: 100%;">' + 
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
			'			<td align="right"><label>申 请 人：</label></td>' + 
			'			<td><input id="salesmanName" name="salesmanName" readonly="readonly" class="easyui-textbox" style="width: 230px;" /><input id="salesmanId"  name="claimer" type="hidden" value="" /></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>备&nbsp;&nbsp;注：</label></td>' + 
			'			<td><input name="remark" required="required" multiline="true" class="easyui-textbox" style="width: 230px;height:50px;" /></td>' + 
			'		</tr>' + 
			'		<input id="orderId"  name="orderId" type="hidden" value="" />' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitDisagreeOrderForm();" href="javascript:void(0)">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$disagreeOrderWindow.window(\'close\');" href="javascript:void(0)">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $disagreeOrderWindow = $(\'div#disagreeOrderWindow\');' +
			'var $orderDatagrid = $(\'table#orderDatagrid\');' +
			'var selRows = $orderDatagrid.datagrid("getSelections");' +
			'$disagreeOrderWindow.find(\'#orderId\').val(selRows[0].id);' +
			'$disagreeOrderWindow.find(\'#clientName\').val(selRows[0].name);' +
			'$disagreeOrderWindow.find(\'#salesmanName\').val(selRows[0].salesmanName);' +
			'$disagreeOrderWindow.find(\'#salesmanId\').val(selRows[0].salesmanId);' +
			'$disagreeOrderWindow.find(\'#telAll\').val(selRows[0].telAll);' +
			'function submitDisagreeOrderForm()' + 
			'{' + 
			'	$disagreeOrderWindow.find(\'form#disagreeOrderForm\').form(\'submit\',' + 
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
			'				$disagreeOrderWindow.window(\'close\');' + 
			'			}else{' + 
			'				$.messager.show({title:\'提示\', msg:\'操作失败！\' + data.msg}); ' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		function showRepulseOrderWindow()
		{
			var selIds = $orderDatagrid.datagrid('getSelections');
			if(selIds.length == 0)
			{
				$.messager.alert('提示', '请选中要打回正跟踪的客户。');
				return;
			}
			if(selIds[0].status != 34)
			{
				$.messager.alert('提示', '只能申请状态为<span style="color: red;">在谈单已批准</span>的客户。');
				return;
			}
			$repulseOrderWindow.window('clear');
			$repulseOrderWindow.window('open').window
			({
				title: '打回正跟踪',
				content: repulseOrderWindowHtml
			}).window('open').window('center');
		}
		
		var repulseOrderWindowHtml = 
			'<form id="repulseOrderForm" action="design/clientMgr/repulseOrder" method="post" style="width: 100%;">' + 
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
			'			<td align="right"><label>申 请 人：</label></td>' + 
			'			<td><input id="salesmanName" name="salesmanName" readonly="readonly" class="easyui-textbox" style="width: 230px;" /><input id="salesmanId"  name="claimer" type="hidden" value="" /></td>' + 
			'		</tr>' + 
			'		<tr>' + 
			'			<td align="right"><label>备&nbsp;&nbsp;注：</label></td>' + 
			'			<td><input name="remark" required="required" multiline="true" class="easyui-textbox" style="width: 230px;height:50px;" /></td>' + 
			'		</tr>' + 
			'		<input id="orderId"  name="orderId" type="hidden" value="" />' + 
			'		<tr>' + 
			'			<td align="center" colspan="4">' + 
			'				<a class="easyui-linkbutton" onclick="submitRepulseOrderForm();" href="javascript:void(0)">保存</a>' + 
			'				<a class="easyui-linkbutton" onclick="$repulseOrderWindow.window(\'close\');" href="javascript:void(0)">取消</a>' + 
			'			</td>' + 
			'		</tr>' +
			'	</table>' + 
			'</form>' +
			'<script type="text/javascript">' + 
			'var $repulseOrderWindow = $(\'div#repulseOrderWindow\');' +
			'var $orderDatagrid = $(\'table#orderDatagrid\');' +
			'var selRows = $orderDatagrid.datagrid("getSelections");' +
			'$repulseOrderWindow.find(\'#orderId\').val(selRows[0].id);' +
			'$repulseOrderWindow.find(\'#clientName\').val(selRows[0].name);' +
			'$repulseOrderWindow.find(\'#salesmanName\').val(selRows[0].salesmanName);' +
			'$repulseOrderWindow.find(\'#salesmanId\').val(selRows[0].salesmanId);' +
			'$repulseOrderWindow.find(\'#telAll\').val(selRows[0].telAll);' +
			'function submitRepulseOrderForm()' + 
			'{' + 
			'	$repulseOrderWindow.find(\'form#repulseOrderForm\').form(\'submit\',' + 
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
			'				$repulseOrderWindow.window(\'close\');' + 
			'			}else{' + 
			'				$.messager.show({title:\'提示\', msg:\'操作失败！\' + data.msg}); ' + 
			'			}' + 
			'		}' + 
			'	});' + 
			'}' + 
			'</script>';
		
		function showAddClientVisitWindow()
		{
			var selTab = $orderCheckMgrTab.tabs('getSelected');
			var index = $orderCheckMgrTab.tabs('getTabIndex',selTab);
			var selRows = null;
			if(index == 0)
				selRows = $orderDatagrid.datagrid('getSelections');
			else
				selRows = $orderCheckDatagrid.datagrid('getSelections');
			if(selRows.length == 0)
			{
				$.messager.alert('提示', '请选中一个客户。');
				return;
			}
			if(selRows[0].designerName == null || selRows[0].designerName == '')
			{
				$.messager.alert('提示', '请先分配设计师。');
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
			'<form id="addClientVisitForm" action="design/clientMgr/addOrderVisit" method="post" style="width: 100%;">' + 
			'	<table width="100%">' + 
			'		<tr>' + 
			'			<td align="right"><label>回访内容：</label></td>' + 
			'			<td><input name="content" required="required" multiline="true" class="easyui-textbox" style="width: 230px;height:50px;" /></td>' + 
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
			'var $orderCheckDatagrid = $(\'table#orderCheckDatagrid\');' +
			'var $orderStylistVisitGrid = $(\'table#orderStylistVisitGrid\');' +
			'var $orderCheckMgrTab = $(\'div#orderCheckMgrTab\');' +
			'var $clientMgrTab = $(\'div#clientMgrTab\');' +
			'var selTab = $orderCheckMgrTab.tabs("getSelected");' +
			'var index = $orderCheckMgrTab.tabs("getTabIndex",selTab);' +
			'var selRows = null;' +
			'if(index == 0)' +
			'	selRows = $orderDatagrid.datagrid("getSelections");' +
			'else' +
			'	selRows = $orderCheckDatagrid.datagrid("getSelections");' +
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
			'				if($clientMgrTab.tabs(\'getSelected\').panel(\'options\').title == "设计师回访记录")' + 
			'					$orderStylistVisitGrid.datagrid("unselectAll").datagrid(\'reload\');' + 
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
			'<form id="addClientForm" action="design/infoerMgr/addClient" method="post" style="width: 100%;">' + 
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
			'		url: \'design/infoerMgr/checkClientTel\','+
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