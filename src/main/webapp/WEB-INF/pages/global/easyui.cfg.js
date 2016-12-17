$.ajaxSetup
({
	accepts: 'application/json, text/javascript, */*;',
	success: function(data)
	{
		if(data.returnCode == 0)
			$.messager.show({title: '提示', msg: data.msg || '操作成功。'})
	},
	error : function(jqXHR, textStatus, errorThrown)
	{
		var msg;
		try
		{
			msg = jqXHR.responseJSON ? jqXHR.responseJSON.msg : $.parseJSON(jqXHR.responseText).msg;
		} catch (e)
		{
		}
		$.messager.alert('警告', msg || '服务器内部错误，请稍后再试。');
	},
	dataType : "JSON",
	type : "POST",
	timeout: 120000
});

$(function()
{
	$.extend($.fn.accordion.defaults,
	{
		fit: true
	});
	
	$.extend($.fn.datalist.defaults,
	{
		lines: true,
		fit: true,
		striped: true,
		rowStyler: function(){return "cursor: pointer"},
	});
	
	$.extend($.fn.filebox.defaults,
	{
		buttonText: '选择文件',
		width: 210
	});
	
	$.extend($.fn.window.defaults,
	{
		collapsible: false,
		minimizable: false,
		maximizable: false,
		resizable: false,
		closed: true,
		modal: true,
		onLoad: function(){$(this).window('center');}
	});
	
	$.extend($.fn.layout.defaults,
	{
		fit: true
	});
	
	$.extend($.fn.panel.defaults,
	{
		onLoadError: function(jqXHR, textStatus, errorThrown)
		{
			$(this).html(jqXHR.responseText);
		}
	});
	
	$.extend($.fn.datagrid.defaults,
	{
		fitColumns: true,
		rownumbers: true,
		fit: true,
		striped: true,
		onLoadError: function(jqXHR, textStatus, errorThrown)
		{
			var msg = jqXHR.responseJSON ? jqXHR.responseJSON.msg : $.parseJSON(jqXHR.responseText).msg;
			$.messager.alert('警告', msg || '服务器内部错误，请稍后再试。');
		},
		onLoadSuccess: function(data)
		{
			$(this).datagrid('uncheckAll');
		}
	});
	
	$.extend($.fn.treegrid.defaults,
	{
		fit: true,
		striped: true,
		fitColumns: true,
		animate: true
	});
	
	$.extend($.fn.treegrid.methods,
	{
		getCheckedNodeIds: function(jq)
		{
			var chkNodeIds = [];
			var idField = $(jq).treegrid('options').idField;
			if(!idField == null)
				return chkNodeIds;
			var chkNodes = $(jq).treegrid('getCheckedNodes');
			for(var i in chkNodes)
				chkNodeIds.push(chkNodes[i][idField]);
			return chkNodeIds;
		},
		getCheckedRoot: function(jq, id)
		{
			var idField = $(jq).treegrid('options').idField;
			var row = $(jq).treegrid('find', id);
			var lastRow = null;
			while (row != null)
			{
				if(row.checked != true)
					return lastRow;
				lastRow = row;
				row = $(jq).treegrid('getParent', lastRow[idField]);
			}
			return lastRow;
		},
		getAncestralRoot: function(jq, id)
		{
			var row = $(jq).treegrid('find', id);
			var idField = $(jq).treegrid('options').idField;
			var lastRow = null;
			while(row != null)
			{
				lastRow = row;
				row = $(jq).treegrid('getParent', lastRow[idField]);
			}
			return lastRow;
		}
	});
	
	$.extend($.fn.datagrid.methods,
	{
		getSelectRowPkValues: function(jq)
		{
			var pkValues = [];
			var selRows = $(jq).datagrid('getSelections');
			if(selRows.length == 0)
				return pkValues;
			var idField = $(jq).datagrid('options').idField;
			if(!idField)
				return pkValues;
			for(var i in selRows)
				pkValues.push(selRows[i][idField]);
			return pkValues;
		},
		getCheckedRowPkValues: function(jq)
		{
			var pkValues = [];
			var selRows = $(jq).datagrid('getChecked');
			if(selRows.length == 0)
				return pkValues;
			var idField = $(jq).datagrid('options').idField;
			if(!idField)
				return pkValues;
			for(var i in selRows)
				pkValues.push(selRows[i][idField]);
			return pkValues;
		}
	});
	
	$.extend($.fn.combobox.defaults,
	{
		prompt: ' -- 全部 -- ',
		editable: false
	});
	
	$.extend($.fn.tree.defaults,
	{
		animate: true,
		lines: true
	});
	
	$.extend($.fn.tabs.defaults,
	{
		fit: true
	});
	
	$.extend($.fn.form.defaults,
	{
		iframe: false,
		success : function(data)
		{
			data = $.parseJSON(data);
			if(data.returnCode != 0)
				$.messager.alert('操作失败', data.msg || '系统内部错误，请联系管理员。');
			else
				$.messager.show({title:'提示',msg: data.msg || '操作成功。'});
			return data;
		},
		onLoadError : function(jqXHR, textStatus, errorThrown)
		{
			var msg = jqXHR.responseJSON ? jqXHR.responseJSON.msg : $.parseJSON(jqXHR.responseText).msg;
			$.messager.alert('警告', msg || '服务器内部错误，请稍后再试。');
		}
	});
});