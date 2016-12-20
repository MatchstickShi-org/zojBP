$(function()
{
	var $editUserForm = $('form#editUserForm');
	var $submitUpdateUserFormBtn = $('a#submitUpdateUserFormBtn');
	var $refreshUpdateUserFormBtn = $('a#refreshUpdateUserFormBtn');
	
	function init()
	{
		$submitUpdateUserFormBtn.linkbutton({'onClick': submitEditUserForm});
		$refreshUpdateUserFormBtn.linkbutton({'onClick': function(){$('div#idxCenterDiv').panel('refresh');}});
		
		function submitEditUserForm()
		{
			$editUserForm.form('submit',
			{
				onSubmit: function()
				{
					if(!$(this).form('validate'))
						return false;
					if($(this).find('[name="pwd"]').val() != $(this).find('[name="confirmPwd"]').val())
					{
						$.messager.alert('提示', '两次密码输入不一致，请重新输入密码。');
						return false;
					}
				},
				success: function(data)
				{
					data = $.fn.form.defaults.success(data);
				}
			});
		}
	}

	init();
});