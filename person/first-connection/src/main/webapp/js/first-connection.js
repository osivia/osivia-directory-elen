// First connection functions

$JQry(function() {
	
	$JQry(".first-connection input[type=password]").each(function(index, element) {
		var $element = $JQry(element),
			$formGroup = $element.closest(".form-group")
			$progressContainer = $formGroup.find(".progress-container"),
			$textContainer = $formGroup.find(".text-container");
		
		if ($progressContainer.get(0) !== undefined) {
			$element.strengthMeter("progressBar", {
				container: $progressContainer,
				base: 100,
				hierarchy: {
				    "0": "progress-bar-danger",
				    "20": "progress-bar-warning",
				    "40": "progress-bar-success"
				}
			});
		}
		
		if ($textContainer.get(0) !== undefined) {
			$element.strengthMeter("text", {
				container: $textContainer.children().first(),
				hierarchy: {
					"0": ["text-muted", $textContainer.data("empty")],
					"1": ["text-danger", $textContainer.data("too-weak")],
				    "20": ["text-warning", $textContainer.data("weak")],
				    "30": ["text-warning", $textContainer.data("good")],
				    "40": ["text-success", $textContainer.data("strong")],
				}
			});
		}
	});
	
});
