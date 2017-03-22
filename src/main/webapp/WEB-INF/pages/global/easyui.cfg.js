String.prototype.startWith = function(str)
{
	if (str == null || str == "" || this.length == 0 || str.length > this.length)
		return false;
	if (this.substr(0, str.length) != str)
		return false;
	return true;
};

String.prototype.endWith = function(str)
{
	if (str == null || str == "" || this.length == 0 || str.length > this.length)
		return false;
	if (this.substring(this.length - str.length) != str)
		return false;
	return true;
};

$.ajaxSetup
({
	accepts: 'application/json, text/javascript, */*;',
	success: function(data, textStatus, jqXHR)
	{
		if(jqXHR.getResponseHeader('sessionTimeout') == 'true')
		{
			$.messager.alert('警告', '你已长时间未操作，请重新登录。', 'warning', function()
			{
				window.location.href = '';
			});
			return;
		}
		if(typeof data == 'string')
		{
			try
			{
				data = $.parseJSON(data);
			}
			catch (e){}
		}
		if(typeof data == 'object')
		{
			if(data.returnCode == 0)
				$.messager.show({title: '提示', msg: data.msg || '操作成功。'})
			else
				$.messager.alert('提示', '操作失败。<br>详情：' + data.msg, 'warning');
		}
	},
	error : function(jqXHR, textStatus, errorThrown)
	{
		var msg;
		try
		{
			msg = jqXHR.responseJSON ? jqXHR.responseJSON.msg : $.parseJSON(jqXHR.responseText).msg;
		}
		catch (e){}
		$.messager.alert('警告', msg || '服务器内部错误，请稍后再试。', 'warning');
	},
	dataType : "JSON",
	type : "POST",
	timeout: 120000
});

$(function()
{
	$.extend($.fn.panel.defaults,
	{
		onLoadError: function(jqXHR, textStatus, errorThrown)
		{
			$(this).html(jqXHR.responseText);
		},
		loader: function(data, successFn, failFn)
		{
			var opts = $(this).panel("options");
            if (!opts.href)
            {
                return false;
            }
            $.ajax
            ({
                type: opts.method,
                url: opts.href,
                cache: false,
                data: data,
                dataType: "html",
                success: function(data, textStatus, jqXHR)
                {
                	if(jqXHR.getResponseHeader('sessionTimeout') == 'true')
                	{
                		$.messager.alert('警告', '你已长时间未操作，请重新登录。', 'warning', function()
        				{
        					window.location.href = '';
        				});
                	}
                	else
                		successFn(data);
                },
                error: function(xhr, status, error)
                {
                	failFn.apply(this, arguments);
                }
            });
		}
	});
	
	$.extend($.fn.layout.paneldefaults,
	{
		loader: $.fn.panel.defaults.loader
	});
	
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
	
	$.extend($.fn.datagrid.defaults,
	{
		fitColumns: true,
		rownumbers: true,
		fit: true,
		striped: true,
		onLoadError: function(jqXHR, textStatus, errorThrown)
		{
			$(this).datagrid('loaded');
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

	$.extend($.fn.linkbutton.methods,
	{
		hide: function(jq)
		{
			$(jq).linkbutton().hide();
		},
		show: function(jq)
		{
			$(jq).linkbutton().show();
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
			return $.parseJSON(data);
		}
	});
});