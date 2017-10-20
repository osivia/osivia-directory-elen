$JQry(function() {
	
	// Auto upload image for preview generation
	$JQry("input[type=file][name='avatar.upload']").change(function(event) {
		var $target = $JQry(event.target),
			$formGroup = $target.closest(".form-group"),
			$submit = $formGroup.find("input[type=submit][name='upload-avatar']");
		
		$submit.click();
	});
	
});