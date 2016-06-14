$JQry(function() {
	
	$JQry(".workspace-local-group-management select.select2").each(function(index, element) {
		var $element = $JQry(element),
 			options = {
					theme : "bootstrap"
				};
		
		
		// Result template
		options["templateResult"] = function(params) {
			var $element = $JQry(params.element),
				displayName = $element.data("displayname"),
				avatar = $element.data("avatar"),
				mail = $element.data("mail");
			
			$result = $JQry(document.createElement("div"));
			
			if (params.loading) {
				$result.text(params.text);
			} else {
				$result.addClass("workspace-member-result");
				
				// Media
				$media = $JQry(document.createElement("div"));
				$media.addClass("media");
				$media.appendTo($result);
				
				// Media left
				$mediaLeft = $JQry(document.createElement("div"));
				$mediaLeft.addClass("media-left media-middle");
				$mediaLeft.appendTo($media);
				
				// Media object
				$mediaObject = $JQry(document.createElement("div"));
				$mediaObject.addClass("media-object");
				$mediaObject.appendTo($mediaLeft);
				
				if (avatar) {
					// Avatar
					$avatar = $JQry(document.createElement("img"));
					$avatar.addClass("center-block");
					$avatar.attr("src", avatar);
					$avatar.attr("alt", "");
					$avatar.appendTo($mediaObject);
				}
				
				// Media body
				$mediaBody = $JQry(document.createElement("div"));
				$mediaBody.addClass("media-body");
				$mediaBody.appendTo($media);
				
				// Display name
				$displayName = $JQry(document.createElement("div"));
				$displayName.text(displayName);
				$displayName.appendTo($mediaBody);
				
				// Extra infos : name + mail
				$extra = $JQry(document.createElement("div"));
				$extra.addClass("text-muted small");
				text = params.id;
				if (mail) {
					text += " – ";
					text += mail;
				}
				$extra.text(text);
				$extra.appendTo($mediaBody);
			}

			return $result;
		};
		
		
		// Selection template
		options["templateSelection"] = function(params) {
			var $element = $JQry(params.element),
				displayName = $element.data("displayname"),
				avatar = $element.data("avatar");
			
			// Selection
			$selection = $JQry(document.createElement("div"));
			$selection.addClass("workspace-member-selection");
			
			if (avatar) {
				// Avatar
				$avatar = $JQry(document.createElement("img"));
				$avatar.attr("src", avatar);
				$avatar.attr("alt", "");
				$avatar.appendTo($selection);
			}
			
			// Display name
			$displayName = $JQry(document.createElement("span"));
			$displayName.text(displayName);
			$displayName.appendTo($selection);
			
			return $selection;
		};
		
		
		// Internationalization
		options["language"] = {};
		if ($element.data("input-too-short") !== undefined) {
			options["language"]["inputTooShort"] = function() {
				return $element.data("input-too-short");
			}
		}
		if ($element.data("error-loading") !== undefined) {
			options["language"]["errorLoading"] = function() {
				return $element.data("error-loading");
			}
		}
		if ($element.data("loading-more") !== undefined) {
			options["language"]["loadingMore"] = function() {
				return $element.data("loading-more");
			}
		}
		if ($element.data("searching") !== undefined) {
			options["language"]["searching"] = function() {
				return $element.data("searching");
			}
		}
		if ($element.data("no-results") !== undefined) {
			options["language"]["noResults"] = function() {
				return $element.data("no-results");
			}
		}
		
		
		// Force width to 100%
		$element.css({
			width: "100%"
		});
		
		
		$element.select2(options);
		
		
		$element.on("select2:select", function(event) {
			var $target = $JQry(event.target),
				$form = $target.closest("form"),
				$submit = $form.find("input[type=submit][name=add]");
			
			$submit.click();
        });
	});
	
	
	$JQry(".workspace-local-group-management input").focus(function(event) {
		var $target = $JQry(event.target),
			$form = $target.closest("form"),
			$collapse = $form.find(".collapse");
		
		if (!$collapse.hasClass("in")) {
			$collapse.collapse("show");
		}
	});
	
	
	$JQry(".workspace-local-group-management button[data-type=delete-local-group]").click(function(event) {
		var $target = $JQry(event.target),
			$fieldset = $target.closest("fieldset"),
			$row = $fieldset.closest(".row"),
			$hidden = $row.find("input[type=hidden]"),
			$form = $fieldset.closest("form"),
			$collapse = $form.find(".collapse");
		
		$hidden.val(true);
		$fieldset.prop("disabled", true);
		if (!$collapse.hasClass("in")) {
			$collapse.collapse("show");
		}
	});
	
	
	$JQry(".workspace-local-group-management button[data-type=delete-member]").click(function(event) {
		var $target = $JQry(event.target),
			$fieldset = $target.closest("fieldset"),
			$hidden = $fieldset.find("input[type=hidden]");
		
		$hidden.val(true);
		$fieldset.prop("disabled", true);
	});
	
	
	$JQry(".workspace-local-group-management a[data-type=delete]").click(function(event) {
		var $target = $JQry(event.target),
			text = $target.data("text");
		
		return confirm(text);
	});
	
	
	$JQry(".workspace-local-group-management button[type=reset]").click(function(event) {
		var $target = $JQry(event.target),
			$form = $target.closest("form");
		
		$form.find("fieldset[disabled]").prop("disabled", false);
		$form.find("input[type=hidden][value=true]").val(false);
	});
	
});
