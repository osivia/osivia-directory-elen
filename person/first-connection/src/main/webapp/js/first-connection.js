// First connection functions
$JQry(function() {
    var passwordRulesInformationTimer;
    var passwordRulesInformationXhr;


    $JQry(".first-connection input[name=password]").each(function(index, element) {
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
        var $placeholder = $JQry(".first-connection [data-password-information-placeholder]");
        var $input = $JQry(".first-connection input[name=password]");

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
	
