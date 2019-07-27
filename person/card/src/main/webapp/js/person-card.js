
$JQry(function() {
	var passwordRulesInformationTimer;
	var passwordRulesInformationXhr;


	// Auto upload image for preview generation
	$JQry(".person-card-edition input[type=file][name='avatar.upload']").change(function(event) {
		var $target = $JQry(event.target),
			$formGroup = $target.closest(".form-group"),
			$submit = $formGroup.find("input[type=submit][name='upload-avatar']");
		
		$submit.click();
	});


	$JQry(".person-card-password-edition input[name=newPwd]").each(function(index, element) {
		var $element = $JQry(element);

		if (!$element.data("loaded")) {
			updatePasswordRulesInformation();

			$element.on("input", function(event) {
				// Clear timer
				clearTimeout(passwordRulesInformationTimer);

				passwordRulesInformationTimer = setTimeout(updatePasswordRulesInformation, 200);
			});

			$element.data("loaded", true);
		}
	});


	function updatePasswordRulesInformation() {
		var $placeholder = $JQry(".person-card-password-edition [data-password-information-placeholder]");
		var $input = $JQry(".person-card-password-edition input[name=newPwd]");

		// Abort previous AJAX request
		if (passwordRulesInformationXhr && passwordRulesInformationXhr.readyState !== 4) {
			passwordRulesInformationXhr.abort();
		}

		passwordRulesInformationXhr = jQuery.ajax({
			url: $placeholder.data("url"),
			type: "POST",
			async: true,
			cache: false,
			data: {
				password: $input.val()
			},
			dataType: "html",
			success : function(data, status, xhr) {
				$placeholder.html(data);
			}
		});
	}

});
