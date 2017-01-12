$JQry(function() {
	
	$JQry(".user-settings input[type=checkbox][name=notificationsEnabled]").change(function(event) {
		var $target = $JQry(event.target),
			$fieldset = $JQry("fieldset.workspace-notifications");
		
		if ($target.prop("checked")) {
			$fieldset.removeAttr("disabled");
		} else {
			$fieldset.attr("disabled", "disabled");
		}
	});
	
});
